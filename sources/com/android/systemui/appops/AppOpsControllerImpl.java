package com.android.systemui.appops;

import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioRecordingConfiguration;
import android.os.Handler;
import android.os.Looper;
import android.permission.PermissionManager;
import android.util.ArraySet;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.privacy.PrivacyItemController$cb$1;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public final class AppOpsControllerImpl extends BroadcastReceiver implements AppOpsController, AppOpsManager.OnOpActiveChangedListener, AppOpsManager.OnOpNotedListener, IndividualSensorPrivacyController.Callback, Dumpable {
    public static final int[] OPS = {42, 26, 101, 24, 27, 100, 0, 1};
    public final AppOpsManager mAppOps;
    public final AudioManager mAudioManager;
    public H mBGHandler;
    public boolean mCameraDisabled;
    public final SystemClock mClock;
    public final Context mContext;
    public final BroadcastDispatcher mDispatcher;
    public boolean mListening;
    public boolean mMicMuted;
    public final IndividualSensorPrivacyController mSensorPrivacyController;
    public final ArrayList mCallbacks = new ArrayList();
    public final SparseArray<Set<AppOpsController.Callback>> mCallbacksByCode = new SparseArray<>();
    @GuardedBy({"mActiveItems"})
    public final ArrayList mActiveItems = new ArrayList();
    @GuardedBy({"mNotedItems"})
    public final ArrayList mNotedItems = new ArrayList();
    @GuardedBy({"mActiveItems"})
    public final SparseArray<ArrayList<AudioRecordingConfiguration>> mRecordingsByUid = new SparseArray<>();
    public AnonymousClass1 mAudioRecordingCallback = new AnonymousClass1();

    /* renamed from: com.android.systemui.appops.AppOpsControllerImpl$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends AudioManager.AudioRecordingCallback {
        public AnonymousClass1() {
        }

        @Override // android.media.AudioManager.AudioRecordingCallback
        public final void onRecordingConfigChanged(List<AudioRecordingConfiguration> list) {
            synchronized (AppOpsControllerImpl.this.mActiveItems) {
                AppOpsControllerImpl.this.mRecordingsByUid.clear();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    AudioRecordingConfiguration audioRecordingConfiguration = list.get(i);
                    ArrayList<AudioRecordingConfiguration> arrayList = AppOpsControllerImpl.this.mRecordingsByUid.get(audioRecordingConfiguration.getClientUid());
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                        AppOpsControllerImpl.this.mRecordingsByUid.put(audioRecordingConfiguration.getClientUid(), arrayList);
                    }
                    arrayList.add(audioRecordingConfiguration);
                }
            }
            AppOpsControllerImpl.this.updateSensorDisabledStatus();
        }
    }

    /* loaded from: classes.dex */
    public class H extends Handler {
        public H(Looper looper) {
            super(looper);
        }
    }

    @Override // com.android.systemui.appops.AppOpsController
    public final void addCallback(int[] iArr, AppOpsController.Callback callback) {
        int length = iArr.length;
        boolean z = false;
        for (int i = 0; i < length; i++) {
            if (this.mCallbacksByCode.contains(iArr[i])) {
                this.mCallbacksByCode.get(iArr[i]).add(callback);
                z = true;
            }
        }
        if (z) {
            this.mCallbacks.add(callback);
        }
        if (!this.mCallbacks.isEmpty()) {
            setListening(true);
        }
    }

    @Override // com.android.systemui.appops.AppOpsController
    public final ArrayList getActiveAppOps() {
        return getActiveAppOps(false);
    }

    @Override // android.app.AppOpsManager.OnOpActiveChangedListener
    public final void onOpActiveChanged(String str, int i, String str2, boolean z) {
        onOpActiveChanged(str, i, str2, null, z, 0, -1);
    }

    @Override // com.android.systemui.appops.AppOpsController
    public final void removeCallback(int[] iArr, PrivacyItemController$cb$1 privacyItemController$cb$1) {
        int length = iArr.length;
        for (int i = 0; i < length; i++) {
            if (this.mCallbacksByCode.contains(iArr[i])) {
                this.mCallbacksByCode.get(iArr[i]).remove(privacyItemController$cb$1);
            }
        }
        this.mCallbacks.remove(privacyItemController$cb$1);
        if (this.mCallbacks.isEmpty()) {
            setListening(false);
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "AppOpsController state:", "  Listening: ");
        m.append(this.mListening);
        printWriter.println(m.toString());
        printWriter.println("  Active Items:");
        for (int i = 0; i < this.mActiveItems.size(); i++) {
            printWriter.print("    ");
            printWriter.println(((AppOpItem) this.mActiveItems.get(i)).toString());
        }
        printWriter.println("  Noted Items:");
        for (int i2 = 0; i2 < this.mNotedItems.size(); i2++) {
            printWriter.print("    ");
            printWriter.println(((AppOpItem) this.mNotedItems.get(i2)).toString());
        }
    }

    @Override // com.android.systemui.appops.AppOpsController
    public final ArrayList getActiveAppOps(boolean z) {
        int i;
        Assert.isNotMainThread();
        ArrayList arrayList = new ArrayList();
        synchronized (this.mActiveItems) {
            try {
                int size = this.mActiveItems.size();
                for (int i2 = 0; i2 < size; i2++) {
                    AppOpItem appOpItem = (AppOpItem) this.mActiveItems.get(i2);
                    Objects.requireNonNull(appOpItem);
                    if (PermissionManager.shouldShowPackageForIndicatorCached(this.mContext, appOpItem.mPackageName) && (z || !appOpItem.mIsDisabled)) {
                        arrayList.add(appOpItem);
                    }
                }
            } finally {
            }
        }
        synchronized (this.mNotedItems) {
            int size2 = this.mNotedItems.size();
            for (i = 0; i < size2; i++) {
                AppOpItem appOpItem2 = (AppOpItem) this.mNotedItems.get(i);
                Objects.requireNonNull(appOpItem2);
                if (PermissionManager.shouldShowPackageForIndicatorCached(this.mContext, appOpItem2.mPackageName)) {
                    arrayList.add(appOpItem2);
                }
            }
        }
        return arrayList;
    }

    public final boolean isAnyRecordingPausedLocked(int i) {
        if (this.mMicMuted) {
            return true;
        }
        ArrayList<AudioRecordingConfiguration> arrayList = this.mRecordingsByUid.get(i);
        if (arrayList == null) {
            return false;
        }
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (arrayList.get(i2).isClientSilenced()) {
                return true;
            }
        }
        return false;
    }

    public final void notifySuscribers(final int i, final int i2, final String str, final boolean z) {
        this.mBGHandler.post(new Runnable() { // from class: com.android.systemui.appops.AppOpsControllerImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AppOpsControllerImpl appOpsControllerImpl = AppOpsControllerImpl.this;
                int i3 = i;
                int i4 = i2;
                String str2 = str;
                boolean z2 = z;
                Objects.requireNonNull(appOpsControllerImpl);
                appOpsControllerImpl.notifySuscribersWorker(i3, i4, str2, z2);
            }
        });
    }

    public final void notifySuscribersWorker(int i, int i2, String str, boolean z) {
        if (this.mCallbacksByCode.contains(i) && PermissionManager.shouldShowPackageForIndicatorCached(this.mContext, str)) {
            for (AppOpsController.Callback callback : this.mCallbacksByCode.get(i)) {
                callback.onActiveStateChanged(i, i2, str, z);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x003e A[Catch: all -> 0x008c, TryCatch #1 {, blocks: (B:12:0x0015, B:15:0x0021, B:23:0x003e, B:32:0x0053, B:33:0x0057, B:37:0x0063, B:41:0x0069, B:42:0x006e, B:44:0x0071), top: B:65:0x0015 }] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0053 A[Catch: all -> 0x008c, TryCatch #1 {, blocks: (B:12:0x0015, B:15:0x0021, B:23:0x003e, B:32:0x0053, B:33:0x0057, B:37:0x0063, B:41:0x0069, B:42:0x006e, B:44:0x0071), top: B:65:0x0015 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onOpActiveChanged(java.lang.String r8, int r9, java.lang.String r10, java.lang.String r11, boolean r12, int r13, int r14) {
        /*
            r7 = this;
            int r8 = android.app.AppOpsManager.strOpToOp(r8)
            r11 = -1
            if (r14 == r11) goto L_0x0012
            if (r13 == 0) goto L_0x0012
            r11 = r13 & 1
            if (r11 != 0) goto L_0x0012
            r11 = r13 & 8
            if (r11 != 0) goto L_0x0012
            return
        L_0x0012:
            java.util.ArrayList r11 = r7.mActiveItems
            monitor-enter(r11)
            java.util.ArrayList r13 = r7.mActiveItems     // Catch: all -> 0x008c
            com.android.systemui.appops.AppOpItem r13 = getAppOpItemLocked(r13, r8, r9, r10)     // Catch: all -> 0x008c
            r14 = 0
            r6 = 1
            if (r13 != 0) goto L_0x0065
            if (r12 == 0) goto L_0x0065
            com.android.systemui.appops.AppOpItem r13 = new com.android.systemui.appops.AppOpItem     // Catch: all -> 0x008c
            com.android.systemui.util.time.SystemClock r0 = r7.mClock     // Catch: all -> 0x008c
            long r4 = r0.elapsedRealtime()     // Catch: all -> 0x008c
            r0 = r13
            r1 = r8
            r2 = r9
            r3 = r10
            r0.<init>(r1, r2, r3, r4)     // Catch: all -> 0x008c
            r0 = 27
            if (r8 == r0) goto L_0x003b
            r0 = 100
            if (r8 != r0) goto L_0x0039
            goto L_0x003b
        L_0x0039:
            r0 = r14
            goto L_0x003c
        L_0x003b:
            r0 = r6
        L_0x003c:
            if (r0 == 0) goto L_0x0045
            boolean r0 = r7.isAnyRecordingPausedLocked(r9)     // Catch: all -> 0x008c
            r13.mIsDisabled = r0     // Catch: all -> 0x008c
            goto L_0x0057
        L_0x0045:
            r0 = 26
            if (r8 == r0) goto L_0x0050
            r0 = 101(0x65, float:1.42E-43)
            if (r8 != r0) goto L_0x004e
            goto L_0x0050
        L_0x004e:
            r0 = r14
            goto L_0x0051
        L_0x0050:
            r0 = r6
        L_0x0051:
            if (r0 == 0) goto L_0x0057
            boolean r0 = r7.mCameraDisabled     // Catch: all -> 0x008c
            r13.mIsDisabled = r0     // Catch: all -> 0x008c
        L_0x0057:
            java.util.ArrayList r0 = r7.mActiveItems     // Catch: all -> 0x008c
            r0.add(r13)     // Catch: all -> 0x008c
            boolean r13 = r13.mIsDisabled     // Catch: all -> 0x008c
            if (r13 != 0) goto L_0x0062
            r13 = r6
            goto L_0x0063
        L_0x0062:
            r13 = r14
        L_0x0063:
            monitor-exit(r11)     // Catch: all -> 0x008c
            goto L_0x0073
        L_0x0065:
            if (r13 == 0) goto L_0x0071
            if (r12 != 0) goto L_0x0071
            java.util.ArrayList r0 = r7.mActiveItems     // Catch: all -> 0x008c
            r0.remove(r13)     // Catch: all -> 0x008c
            monitor-exit(r11)     // Catch: all -> 0x008c
            r13 = r6
            goto L_0x0073
        L_0x0071:
            monitor-exit(r11)     // Catch: all -> 0x008c
            r13 = r14
        L_0x0073:
            if (r13 != 0) goto L_0x0076
            return
        L_0x0076:
            java.util.ArrayList r13 = r7.mNotedItems
            monitor-enter(r13)
            java.util.ArrayList r11 = r7.mNotedItems     // Catch: all -> 0x0089
            com.android.systemui.appops.AppOpItem r11 = getAppOpItemLocked(r11, r8, r9, r10)     // Catch: all -> 0x0089
            if (r11 == 0) goto L_0x0082
            r14 = r6
        L_0x0082:
            monitor-exit(r13)     // Catch: all -> 0x0089
            if (r14 != 0) goto L_0x0088
            r7.notifySuscribers(r8, r9, r10, r12)
        L_0x0088:
            return
        L_0x0089:
            r7 = move-exception
            monitor-exit(r13)     // Catch: all -> 0x0089
            throw r7
        L_0x008c:
            r7 = move-exception
            monitor-exit(r11)     // Catch: all -> 0x008c
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.appops.AppOpsControllerImpl.onOpActiveChanged(java.lang.String, int, java.lang.String, java.lang.String, boolean, int, int):void");
    }

    public final void onOpNoted(int i, int i2, String str, String str2, int i3, int i4) {
        final AppOpItem appOpItemLocked;
        boolean z;
        boolean z2;
        if (i4 == 0) {
            synchronized (this.mNotedItems) {
                appOpItemLocked = getAppOpItemLocked(this.mNotedItems, i, i2, str);
                z = false;
                if (appOpItemLocked == null) {
                    appOpItemLocked = new AppOpItem(i, i2, str, this.mClock.elapsedRealtime());
                    this.mNotedItems.add(appOpItemLocked);
                    z2 = true;
                } else {
                    z2 = false;
                }
            }
            this.mBGHandler.removeCallbacksAndMessages(appOpItemLocked);
            final H h = this.mBGHandler;
            Objects.requireNonNull(h);
            h.removeCallbacksAndMessages(appOpItemLocked);
            h.postDelayed(new Runnable() { // from class: com.android.systemui.appops.AppOpsControllerImpl.H.1
                @Override // java.lang.Runnable
                public final void run() {
                    boolean z3;
                    AppOpsControllerImpl appOpsControllerImpl = AppOpsControllerImpl.this;
                    AppOpItem appOpItem = appOpItemLocked;
                    Objects.requireNonNull(appOpItem);
                    int i5 = appOpItem.mCode;
                    AppOpItem appOpItem2 = appOpItemLocked;
                    Objects.requireNonNull(appOpItem2);
                    int i6 = appOpItem2.mUid;
                    AppOpItem appOpItem3 = appOpItemLocked;
                    Objects.requireNonNull(appOpItem3);
                    String str3 = appOpItem3.mPackageName;
                    int[] iArr = AppOpsControllerImpl.OPS;
                    Objects.requireNonNull(appOpsControllerImpl);
                    synchronized (appOpsControllerImpl.mNotedItems) {
                        AppOpItem appOpItemLocked2 = AppOpsControllerImpl.getAppOpItemLocked(appOpsControllerImpl.mNotedItems, i5, i6, str3);
                        if (appOpItemLocked2 != null) {
                            appOpsControllerImpl.mNotedItems.remove(appOpItemLocked2);
                            synchronized (appOpsControllerImpl.mActiveItems) {
                                if (AppOpsControllerImpl.getAppOpItemLocked(appOpsControllerImpl.mActiveItems, i5, i6, str3) != null) {
                                    z3 = true;
                                } else {
                                    z3 = false;
                                }
                            }
                            if (!z3) {
                                appOpsControllerImpl.notifySuscribersWorker(i5, i6, str3, false);
                            }
                        }
                    }
                }
            }, appOpItemLocked, 5000L);
            if (z2) {
                synchronized (this.mActiveItems) {
                    if (getAppOpItemLocked(this.mActiveItems, i, i2, str) != null) {
                        z = true;
                    }
                }
                if (!z) {
                    notifySuscribers(i, i2, str, true);
                }
            }
        }
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        boolean z = true;
        if (!this.mAudioManager.isMicrophoneMute() && !this.mSensorPrivacyController.isSensorBlocked(1)) {
            z = false;
        }
        this.mMicMuted = z;
        updateSensorDisabledStatus();
    }

    @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController.Callback
    public final void onSensorBlockedChanged(final int i, final boolean z) {
        this.mBGHandler.post(new Runnable() { // from class: com.android.systemui.appops.AppOpsControllerImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AppOpsControllerImpl appOpsControllerImpl = AppOpsControllerImpl.this;
                int i2 = i;
                boolean z2 = z;
                Objects.requireNonNull(appOpsControllerImpl);
                if (i2 == 2) {
                    appOpsControllerImpl.mCameraDisabled = z2;
                } else {
                    boolean z3 = true;
                    if (i2 == 1) {
                        if (!appOpsControllerImpl.mAudioManager.isMicrophoneMute() && !z2) {
                            z3 = false;
                        }
                        appOpsControllerImpl.mMicMuted = z3;
                    }
                }
                appOpsControllerImpl.updateSensorDisabledStatus();
            }
        });
    }

    @VisibleForTesting
    public void setListening(boolean z) {
        this.mListening = z;
        if (z) {
            AppOpsManager appOpsManager = this.mAppOps;
            int[] iArr = OPS;
            appOpsManager.startWatchingActive(iArr, this);
            this.mAppOps.startWatchingNoted(iArr, this);
            this.mAudioManager.registerAudioRecordingCallback(this.mAudioRecordingCallback, this.mBGHandler);
            this.mSensorPrivacyController.addCallback(this);
            boolean z2 = true;
            if (!this.mAudioManager.isMicrophoneMute() && !this.mSensorPrivacyController.isSensorBlocked(1)) {
                z2 = false;
            }
            this.mMicMuted = z2;
            this.mCameraDisabled = this.mSensorPrivacyController.isSensorBlocked(2);
            this.mBGHandler.post(new CarrierTextManager$$ExternalSyntheticLambda0(this, 2));
            this.mDispatcher.registerReceiverWithHandler(this, new IntentFilter("android.media.action.MICROPHONE_MUTE_CHANGED"), this.mBGHandler);
            return;
        }
        this.mAppOps.stopWatchingActive(this);
        this.mAppOps.stopWatchingNoted(this);
        this.mAudioManager.unregisterAudioRecordingCallback(this.mAudioRecordingCallback);
        this.mSensorPrivacyController.removeCallback(this);
        this.mBGHandler.removeCallbacksAndMessages(null);
        this.mDispatcher.unregisterReceiver(this);
        synchronized (this.mActiveItems) {
            this.mActiveItems.clear();
            this.mRecordingsByUid.clear();
        }
        synchronized (this.mNotedItems) {
            this.mNotedItems.clear();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0029 A[Catch: all -> 0x005a, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000d, B:14:0x0029, B:23:0x003e, B:25:0x0042, B:27:0x0046, B:31:0x0052, B:32:0x0055, B:33:0x0058), top: B:38:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0030  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x003e A[Catch: all -> 0x005a, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000d, B:14:0x0029, B:23:0x003e, B:25:0x0042, B:27:0x0046, B:31:0x0052, B:32:0x0055, B:33:0x0058), top: B:38:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0046 A[Catch: all -> 0x005a, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000d, B:14:0x0029, B:23:0x003e, B:25:0x0042, B:27:0x0046, B:31:0x0052, B:32:0x0055, B:33:0x0058), top: B:38:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0055 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateSensorDisabledStatus() {
        /*
            r9 = this;
            java.util.ArrayList r0 = r9.mActiveItems
            monitor-enter(r0)
            java.util.ArrayList r1 = r9.mActiveItems     // Catch: all -> 0x005a
            int r1 = r1.size()     // Catch: all -> 0x005a
            r2 = 0
            r3 = r2
        L_0x000b:
            if (r3 >= r1) goto L_0x0058
            java.util.ArrayList r4 = r9.mActiveItems     // Catch: all -> 0x005a
            java.lang.Object r4 = r4.get(r3)     // Catch: all -> 0x005a
            com.android.systemui.appops.AppOpItem r4 = (com.android.systemui.appops.AppOpItem) r4     // Catch: all -> 0x005a
            java.util.Objects.requireNonNull(r4)     // Catch: all -> 0x005a
            int r5 = r4.mCode     // Catch: all -> 0x005a
            r6 = 27
            r7 = 1
            if (r5 == r6) goto L_0x0026
            r6 = 100
            if (r5 != r6) goto L_0x0024
            goto L_0x0026
        L_0x0024:
            r6 = r2
            goto L_0x0027
        L_0x0026:
            r6 = r7
        L_0x0027:
            if (r6 == 0) goto L_0x0030
            int r5 = r4.mUid     // Catch: all -> 0x005a
            boolean r5 = r9.isAnyRecordingPausedLocked(r5)     // Catch: all -> 0x005a
            goto L_0x0042
        L_0x0030:
            r6 = 26
            if (r5 == r6) goto L_0x003b
            r6 = 101(0x65, float:1.42E-43)
            if (r5 != r6) goto L_0x0039
            goto L_0x003b
        L_0x0039:
            r5 = r2
            goto L_0x003c
        L_0x003b:
            r5 = r7
        L_0x003c:
            if (r5 == 0) goto L_0x0041
            boolean r5 = r9.mCameraDisabled     // Catch: all -> 0x005a
            goto L_0x0042
        L_0x0041:
            r5 = r2
        L_0x0042:
            boolean r6 = r4.mIsDisabled     // Catch: all -> 0x005a
            if (r6 == r5) goto L_0x0055
            r4.mIsDisabled = r5     // Catch: all -> 0x005a
            int r6 = r4.mCode     // Catch: all -> 0x005a
            int r8 = r4.mUid     // Catch: all -> 0x005a
            java.lang.String r4 = r4.mPackageName     // Catch: all -> 0x005a
            if (r5 != 0) goto L_0x0051
            goto L_0x0052
        L_0x0051:
            r7 = r2
        L_0x0052:
            r9.notifySuscribers(r6, r8, r4, r7)     // Catch: all -> 0x005a
        L_0x0055:
            int r3 = r3 + 1
            goto L_0x000b
        L_0x0058:
            monitor-exit(r0)     // Catch: all -> 0x005a
            return
        L_0x005a:
            r9 = move-exception
            monitor-exit(r0)     // Catch: all -> 0x005a
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.appops.AppOpsControllerImpl.updateSensorDisabledStatus():void");
    }

    public AppOpsControllerImpl(Context context, Looper looper, DumpManager dumpManager, AudioManager audioManager, IndividualSensorPrivacyController individualSensorPrivacyController, BroadcastDispatcher broadcastDispatcher, SystemClock systemClock) {
        this.mDispatcher = broadcastDispatcher;
        this.mAppOps = (AppOpsManager) context.getSystemService("appops");
        this.mBGHandler = new H(looper);
        boolean z = false;
        for (int i = 0; i < 8; i++) {
            this.mCallbacksByCode.put(OPS[i], new ArraySet());
        }
        this.mAudioManager = audioManager;
        this.mSensorPrivacyController = individualSensorPrivacyController;
        this.mMicMuted = (audioManager.isMicrophoneMute() || individualSensorPrivacyController.isSensorBlocked(1)) ? true : z;
        this.mCameraDisabled = individualSensorPrivacyController.isSensorBlocked(2);
        this.mContext = context;
        this.mClock = systemClock;
        dumpManager.registerDumpable("AppOpsControllerImpl", this);
    }

    public static AppOpItem getAppOpItemLocked(ArrayList arrayList, int i, int i2, String str) {
        int size = arrayList.size();
        for (int i3 = 0; i3 < size; i3++) {
            AppOpItem appOpItem = (AppOpItem) arrayList.get(i3);
            Objects.requireNonNull(appOpItem);
            if (appOpItem.mCode == i && appOpItem.mUid == i2 && appOpItem.mPackageName.equals(str)) {
                return appOpItem;
            }
        }
        return null;
    }

    @VisibleForTesting
    public void setBGHandler(H h) {
        this.mBGHandler = h;
    }

    @Override // com.android.systemui.appops.AppOpsController
    public final boolean isMicMuted() {
        return this.mMicMuted;
    }
}
