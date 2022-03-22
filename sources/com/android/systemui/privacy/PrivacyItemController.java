package com.android.systemui.privacy;

import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.EmptyList;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PrivacyItemController.kt */
/* loaded from: classes.dex */
public final class PrivacyItemController implements Dumpable {
    public static final Companion Companion = new Companion(0);
    public static final int[] OPS;
    public static final int[] OPS_LOCATION;
    public boolean allIndicatorsAvailable;
    public final AppOpsController appOpsController;
    public final DelayableExecutor bgExecutor;
    public final PrivacyItemController$cb$1 cb;
    public List<Integer> currentUserIds;
    public Runnable holdingRunnableCanceler;
    public final MyExecutor internalUiExecutor;
    public boolean listening;
    public boolean locationAvailable;
    public final PrivacyLogger logger;
    public List<PrivacyItem> privacyList;
    public final SystemClock systemClock;
    public final PrivacyItemController$updateListAndNotifyChanges$1 updateListAndNotifyChanges;
    public final UserTracker userTracker;
    public PrivacyItemController$userTrackerCallback$1 userTrackerCallback;
    public final ArrayList callbacks = new ArrayList();
    public final PrivacyItemController$notifyChanges$1 notifyChanges = new Runnable() { // from class: com.android.systemui.privacy.PrivacyItemController$notifyChanges$1
        @Override // java.lang.Runnable
        public final void run() {
            List<PrivacyItem> list;
            PrivacyItemController privacyItemController = PrivacyItemController.this;
            Objects.requireNonNull(privacyItemController);
            synchronized (privacyItemController) {
                list = CollectionsKt___CollectionsKt.toList(privacyItemController.privacyList);
            }
            Iterator it = PrivacyItemController.this.callbacks.iterator();
            while (it.hasNext()) {
                PrivacyItemController.Callback callback = (PrivacyItemController.Callback) ((WeakReference) it.next()).get();
                if (callback != null) {
                    callback.onPrivacyItemsChanged(list);
                }
            }
        }
    };
    public boolean micCameraAvailable = DeviceConfig.getBoolean("privacy", "camera_mic_icons_enabled", true);

    /* compiled from: PrivacyItemController.kt */
    /* loaded from: classes.dex */
    public interface Callback {
        default void onFlagLocationChanged(boolean z) {
        }

        default void onFlagMicCameraChanged(boolean z) {
        }

        void onPrivacyItemsChanged(List<PrivacyItem> list);
    }

    /* compiled from: PrivacyItemController.kt */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(int i) {
            this();
        }

        @VisibleForTesting
        public static /* synthetic */ void getTIME_TO_HOLD_INDICATORS$annotations() {
        }
    }

    /* compiled from: PrivacyItemController.kt */
    /* loaded from: classes.dex */
    public final class MyExecutor implements Executor {
        public final DelayableExecutor delegate;
        public Runnable listeningCanceller;

        public MyExecutor(DelayableExecutor delayableExecutor) {
            this.delegate = delayableExecutor;
        }

        @Override // java.util.concurrent.Executor
        public final void execute(Runnable runnable) {
            this.delegate.execute(runnable);
        }

        public final void updateListeningState() {
            Runnable runnable = this.listeningCanceller;
            if (runnable != null) {
                runnable.run();
            }
            DelayableExecutor delayableExecutor = this.delegate;
            final PrivacyItemController privacyItemController = PrivacyItemController.this;
            this.listeningCanceller = delayableExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.privacy.PrivacyItemController$MyExecutor$updateListeningState$1
                @Override // java.lang.Runnable
                public final void run() {
                    boolean z;
                    PrivacyItemController privacyItemController2 = PrivacyItemController.this;
                    PrivacyItemController.Companion companion = PrivacyItemController.Companion;
                    Objects.requireNonNull(privacyItemController2);
                    boolean z2 = !privacyItemController2.callbacks.isEmpty();
                    if (privacyItemController2.micCameraAvailable || privacyItemController2.locationAvailable) {
                        z = true;
                    } else {
                        z = false;
                    }
                    boolean z3 = z2 & z;
                    if (privacyItemController2.listening != z3) {
                        privacyItemController2.listening = z3;
                        if (z3) {
                            privacyItemController2.appOpsController.addCallback(PrivacyItemController.OPS, privacyItemController2.cb);
                            privacyItemController2.userTracker.addCallback(privacyItemController2.userTrackerCallback, privacyItemController2.bgExecutor);
                            privacyItemController2.update(true);
                            return;
                        }
                        privacyItemController2.appOpsController.removeCallback(PrivacyItemController.OPS, privacyItemController2.cb);
                        privacyItemController2.userTracker.removeCallback(privacyItemController2.userTrackerCallback);
                        privacyItemController2.update(false);
                    }
                }
            }, 0L);
        }
    }

    /* compiled from: PrivacyItemController.kt */
    /* loaded from: classes.dex */
    public static final class NotifyChangesToCallback implements Runnable {
        public final Callback callback;
        public final List<PrivacyItem> list;

        @Override // java.lang.Runnable
        public final void run() {
            Callback callback = this.callback;
            if (callback != null) {
                callback.onPrivacyItemsChanged(this.list);
            }
        }

        public NotifyChangesToCallback(Callback callback, List<PrivacyItem> list) {
            this.callback = callback;
            this.list = list;
        }
    }

    @VisibleForTesting
    public static /* synthetic */ void getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getUserTrackerCallback$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    static {
        int[] iArr = {0, 1};
        OPS_LOCATION = iArr;
        int[] copyOf = Arrays.copyOf(new int[]{26, 101, 27, 100}, 6);
        System.arraycopy(iArr, 0, copyOf, 4, 2);
        OPS = copyOf;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
    }

    public final void addCallback(Callback callback) {
        List list;
        WeakReference weakReference = new WeakReference(callback);
        this.callbacks.add(weakReference);
        if ((!this.callbacks.isEmpty()) && !this.listening) {
            this.internalUiExecutor.updateListeningState();
        } else if (this.listening) {
            MyExecutor myExecutor = this.internalUiExecutor;
            Callback callback2 = (Callback) weakReference.get();
            synchronized (this) {
                list = CollectionsKt___CollectionsKt.toList(this.privacyList);
            }
            myExecutor.execute(new NotifyChangesToCallback(callback2, list));
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        List<PrivacyItem> list;
        printWriter.println("PrivacyItemController state:");
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.listening, "  Listening: ", printWriter);
        printWriter.println(Intrinsics.stringPlus("  Current user ids: ", this.currentUserIds));
        printWriter.println("  Privacy Items:");
        synchronized (this) {
            list = CollectionsKt___CollectionsKt.toList(this.privacyList);
        }
        for (PrivacyItem privacyItem : list) {
            printWriter.print("    ");
            printWriter.println(privacyItem.toString());
        }
        printWriter.println("  Callbacks:");
        Iterator it = this.callbacks.iterator();
        while (it.hasNext()) {
            Callback callback = (Callback) ((WeakReference) it.next()).get();
            if (callback != null) {
                printWriter.print("    ");
                printWriter.println(callback.toString());
            }
        }
    }

    public final void removeCallback(Callback callback) {
        final WeakReference weakReference = new WeakReference(callback);
        this.callbacks.removeIf(new Predicate() { // from class: com.android.systemui.privacy.PrivacyItemController$removeCallback$1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                PrivacyItemController.Callback callback2 = (PrivacyItemController.Callback) ((WeakReference) obj).get();
                if (callback2 == null) {
                    return true;
                }
                return callback2.equals(weakReference.get());
            }
        });
        if (this.callbacks.isEmpty()) {
            this.internalUiExecutor.updateListeningState();
        }
    }

    public final void update(final boolean z) {
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.privacy.PrivacyItemController$update$1
            @Override // java.lang.Runnable
            public final void run() {
                if (z) {
                    PrivacyItemController privacyItemController = this;
                    List<UserInfo> userProfiles = privacyItemController.userTracker.getUserProfiles();
                    ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(userProfiles, 10));
                    for (UserInfo userInfo : userProfiles) {
                        arrayList.add(Integer.valueOf(userInfo.id));
                    }
                    privacyItemController.currentUserIds = arrayList;
                    PrivacyItemController privacyItemController2 = this;
                    privacyItemController2.logger.logCurrentProfilesChanged(privacyItemController2.currentUserIds);
                }
                this.updateListAndNotifyChanges.run();
            }
        });
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.privacy.PrivacyItemController$notifyChanges$1] */
    /* JADX WARN: Type inference failed for: r4v3, types: [com.android.systemui.privacy.PrivacyItemController$cb$1] */
    /* JADX WARN: Type inference failed for: r4v4, types: [com.android.systemui.privacy.PrivacyItemController$userTrackerCallback$1] */
    public PrivacyItemController(AppOpsController appOpsController, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, DeviceConfigProxy deviceConfigProxy, UserTracker userTracker, PrivacyLogger privacyLogger, SystemClock systemClock, DumpManager dumpManager) {
        this.appOpsController = appOpsController;
        this.bgExecutor = delayableExecutor2;
        this.userTracker = userTracker;
        this.logger = privacyLogger;
        this.systemClock = systemClock;
        EmptyList emptyList = EmptyList.INSTANCE;
        this.privacyList = emptyList;
        this.currentUserIds = emptyList;
        this.internalUiExecutor = new MyExecutor(delayableExecutor);
        this.updateListAndNotifyChanges = new PrivacyItemController$updateListAndNotifyChanges$1(this, delayableExecutor);
        Objects.requireNonNull(deviceConfigProxy);
        boolean z = true;
        boolean z2 = DeviceConfig.getBoolean("privacy", "location_indicators_enabled", false);
        this.locationAvailable = z2;
        this.allIndicatorsAvailable = (!this.micCameraAvailable || !z2) ? false : z;
        DeviceConfig.OnPropertiesChangedListener privacyItemController$devicePropertiesChangedListener$1 = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.systemui.privacy.PrivacyItemController$devicePropertiesChangedListener$1
            /* JADX WARN: Removed duplicated region for block: B:19:0x005d  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties r8) {
                /*
                    r7 = this;
                    java.lang.String r0 = r8.getNamespace()
                    java.lang.String r1 = "privacy"
                    boolean r0 = r1.equals(r0)
                    if (r0 == 0) goto L_0x00c6
                    java.util.Set r0 = r8.getKeyset()
                    java.lang.String r1 = "camera_mic_icons_enabled"
                    boolean r0 = r0.contains(r1)
                    java.lang.String r2 = "location_indicators_enabled"
                    if (r0 != 0) goto L_0x0024
                    java.util.Set r0 = r8.getKeyset()
                    boolean r0 = r0.contains(r2)
                    if (r0 == 0) goto L_0x00c6
                L_0x0024:
                    java.util.Set r0 = r8.getKeyset()
                    boolean r0 = r0.contains(r1)
                    r3 = 0
                    r4 = 1
                    if (r0 == 0) goto L_0x0072
                    com.android.systemui.privacy.PrivacyItemController r0 = com.android.systemui.privacy.PrivacyItemController.this
                    boolean r1 = r8.getBoolean(r1, r4)
                    r0.micCameraAvailable = r1
                    com.android.systemui.privacy.PrivacyItemController r0 = com.android.systemui.privacy.PrivacyItemController.this
                    java.util.Objects.requireNonNull(r0)
                    boolean r1 = r0.micCameraAvailable
                    if (r1 == 0) goto L_0x004c
                    com.android.systemui.privacy.PrivacyItemController r1 = com.android.systemui.privacy.PrivacyItemController.this
                    java.util.Objects.requireNonNull(r1)
                    boolean r1 = r1.locationAvailable
                    if (r1 == 0) goto L_0x004c
                    r1 = r4
                    goto L_0x004d
                L_0x004c:
                    r1 = r3
                L_0x004d:
                    r0.allIndicatorsAvailable = r1
                    com.android.systemui.privacy.PrivacyItemController r0 = com.android.systemui.privacy.PrivacyItemController.this
                    java.util.ArrayList r1 = r0.callbacks
                    java.util.Iterator r1 = r1.iterator()
                L_0x0057:
                    boolean r5 = r1.hasNext()
                    if (r5 == 0) goto L_0x0072
                    java.lang.Object r5 = r1.next()
                    java.lang.ref.WeakReference r5 = (java.lang.ref.WeakReference) r5
                    java.lang.Object r5 = r5.get()
                    com.android.systemui.privacy.PrivacyItemController$Callback r5 = (com.android.systemui.privacy.PrivacyItemController.Callback) r5
                    if (r5 != 0) goto L_0x006c
                    goto L_0x0057
                L_0x006c:
                    boolean r6 = r0.micCameraAvailable
                    r5.onFlagMicCameraChanged(r6)
                    goto L_0x0057
                L_0x0072:
                    java.util.Set r0 = r8.getKeyset()
                    boolean r0 = r0.contains(r2)
                    if (r0 == 0) goto L_0x00bf
                    com.android.systemui.privacy.PrivacyItemController r0 = com.android.systemui.privacy.PrivacyItemController.this
                    boolean r8 = r8.getBoolean(r2, r3)
                    java.util.Objects.requireNonNull(r0)
                    r0.locationAvailable = r8
                    com.android.systemui.privacy.PrivacyItemController r8 = com.android.systemui.privacy.PrivacyItemController.this
                    java.util.Objects.requireNonNull(r8)
                    boolean r0 = r8.micCameraAvailable
                    if (r0 == 0) goto L_0x009a
                    com.android.systemui.privacy.PrivacyItemController r0 = com.android.systemui.privacy.PrivacyItemController.this
                    java.util.Objects.requireNonNull(r0)
                    boolean r0 = r0.locationAvailable
                    if (r0 == 0) goto L_0x009a
                    r3 = r4
                L_0x009a:
                    r8.allIndicatorsAvailable = r3
                    com.android.systemui.privacy.PrivacyItemController r8 = com.android.systemui.privacy.PrivacyItemController.this
                    java.util.ArrayList r0 = r8.callbacks
                    java.util.Iterator r0 = r0.iterator()
                L_0x00a4:
                    boolean r1 = r0.hasNext()
                    if (r1 == 0) goto L_0x00bf
                    java.lang.Object r1 = r0.next()
                    java.lang.ref.WeakReference r1 = (java.lang.ref.WeakReference) r1
                    java.lang.Object r1 = r1.get()
                    com.android.systemui.privacy.PrivacyItemController$Callback r1 = (com.android.systemui.privacy.PrivacyItemController.Callback) r1
                    if (r1 != 0) goto L_0x00b9
                    goto L_0x00a4
                L_0x00b9:
                    boolean r2 = r8.locationAvailable
                    r1.onFlagLocationChanged(r2)
                    goto L_0x00a4
                L_0x00bf:
                    com.android.systemui.privacy.PrivacyItemController r7 = com.android.systemui.privacy.PrivacyItemController.this
                    com.android.systemui.privacy.PrivacyItemController$MyExecutor r7 = r7.internalUiExecutor
                    r7.updateListeningState()
                L_0x00c6:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.privacy.PrivacyItemController$devicePropertiesChangedListener$1.onPropertiesChanged(android.provider.DeviceConfig$Properties):void");
            }
        };
        this.cb = new AppOpsController.Callback() { // from class: com.android.systemui.privacy.PrivacyItemController$cb$1
            @Override // com.android.systemui.appops.AppOpsController.Callback
            public final void onActiveStateChanged(int i, int i2, String str, boolean z3) {
                Objects.requireNonNull(PrivacyItemController.Companion);
                if (ArraysKt___ArraysKt.contains(PrivacyItemController.OPS_LOCATION, i)) {
                    PrivacyItemController privacyItemController = PrivacyItemController.this;
                    Objects.requireNonNull(privacyItemController);
                    if (!privacyItemController.locationAvailable) {
                        return;
                    }
                }
                if (PrivacyItemController.this.currentUserIds.contains(Integer.valueOf(UserHandle.getUserId(i2))) || i == 100 || i == 101) {
                    PrivacyItemController.this.logger.logUpdatedItemFromAppOps(i, i2, str, z3);
                    PrivacyItemController.this.update(false);
                }
            }
        };
        this.userTrackerCallback = new UserTracker.Callback() { // from class: com.android.systemui.privacy.PrivacyItemController$userTrackerCallback$1
            @Override // com.android.systemui.settings.UserTracker.Callback
            public final void onProfilesChanged() {
                PrivacyItemController.this.update(true);
            }

            @Override // com.android.systemui.settings.UserTracker.Callback
            public final void onUserChanged(int i) {
                PrivacyItemController.this.update(true);
            }
        };
        DeviceConfig.addOnPropertiesChangedListener("privacy", delayableExecutor, privacyItemController$devicePropertiesChangedListener$1);
        dumpManager.registerDumpable("PrivacyItemController", this);
    }
}
