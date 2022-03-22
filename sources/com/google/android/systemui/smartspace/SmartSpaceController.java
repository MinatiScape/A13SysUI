package com.google.android.systemui.smartspace;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.KeyValueListParser;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.smartspace.nano.SmartspaceProto$CardWrapper;
import com.android.systemui.util.Assert;
import com.google.protobuf.nano.MessageNano;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SmartSpaceController implements Dumpable {
    public static final boolean DEBUG = Log.isLoggable("SmartSpaceController", 3);
    public final AlarmManager mAlarmManager;
    public boolean mAlarmRegistered;
    public final Context mAppContext;
    public final Handler mBackgroundHandler;
    public final Context mContext;
    public final SmartSpaceData mData;
    public boolean mHidePrivateData;
    public boolean mHideWorkData;
    public final AnonymousClass1 mKeyguardMonitorCallback;
    public boolean mSmartSpaceEnabledBroadcastSent;
    public final ProtoStore mStore;
    public final ArrayList<SmartSpaceUpdateListener> mListeners = new ArrayList<>();
    public final SmartSpaceController$$ExternalSyntheticLambda0 mExpireAlarmAction = new SmartSpaceController$$ExternalSyntheticLambda0(this, 0);
    public final Handler mUiHandler = new Handler(Looper.getMainLooper());
    public int mCurrentUserId = UserHandle.myUserId();

    /* loaded from: classes.dex */
    public class UserSwitchReceiver extends BroadcastReceiver {
        public UserSwitchReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (SmartSpaceController.DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Switching user: ");
                m.append(intent.getAction());
                m.append(" uid: ");
                m.append(UserHandle.myUserId());
                Log.d("SmartSpaceController", m.toString());
            }
            if (intent.getAction().equals("android.intent.action.USER_SWITCHED")) {
                SmartSpaceController.this.mCurrentUserId = intent.getIntExtra("android.intent.extra.user_handle", -1);
                SmartSpaceData smartSpaceData = SmartSpaceController.this.mData;
                Objects.requireNonNull(smartSpaceData);
                smartSpaceData.mWeatherCard = null;
                smartSpaceData.mCurrentCard = null;
                SmartSpaceController.this.onExpire(true);
            }
            SmartSpaceController.this.onExpire(true);
        }
    }

    public final boolean isSmartSpaceDisabledByExperiments() {
        boolean z;
        String string = Settings.Global.getString(this.mContext.getContentResolver(), "always_on_display_constants");
        KeyValueListParser keyValueListParser = new KeyValueListParser(',');
        try {
            keyValueListParser.setString(string);
            z = keyValueListParser.getBoolean("smart_space_enabled", true);
        } catch (IllegalArgumentException unused) {
            Log.e("SmartSpaceController", "Bad AOD constants");
            z = true;
        }
        return !z;
    }

    public final SmartSpaceCard loadSmartSpaceData(boolean z) {
        SmartspaceProto$CardWrapper smartspaceProto$CardWrapper = new SmartspaceProto$CardWrapper();
        ProtoStore protoStore = this.mStore;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("smartspace_");
        m.append(this.mCurrentUserId);
        m.append("_");
        m.append(z);
        String sb = m.toString();
        Objects.requireNonNull(protoStore);
        File fileStreamPath = ((Context) protoStore.mContext).getFileStreamPath(sb);
        boolean z2 = false;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileStreamPath);
            int length = (int) fileStreamPath.length();
            byte[] bArr = new byte[length];
            fileInputStream.read(bArr, 0, length);
            MessageNano.mergeFrom(smartspaceProto$CardWrapper, bArr);
            fileInputStream.close();
            z2 = true;
        } catch (FileNotFoundException unused) {
            Log.d("ProtoStore", "no cached data");
        } catch (Exception e) {
            Log.e("ProtoStore", "unable to load data", e);
        }
        if (z2) {
            return SmartSpaceCard.fromWrapper(this.mContext, smartspaceProto$CardWrapper, !z);
        }
        return null;
    }

    public final void onGsaChanged() {
        if (DEBUG) {
            Log.d("SmartSpaceController", "onGsaChanged");
        }
        if (UserHandle.myUserId() == 0) {
            this.mAppContext.sendBroadcast(new Intent("com.google.android.systemui.smartspace.ENABLE_UPDATE").setPackage("com.google.android.googlequicksearchbox").addFlags(268435456));
            this.mSmartSpaceEnabledBroadcastSent = true;
        }
        ArrayList arrayList = new ArrayList(this.mListeners);
        for (int i = 0; i < arrayList.size(); i++) {
            ((SmartSpaceUpdateListener) arrayList.get(i)).onGsaChanged();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00ad A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void update() {
        /*
            r18 = this;
            r0 = r18
            com.android.systemui.util.Assert.isMainThread()
            boolean r1 = com.google.android.systemui.smartspace.SmartSpaceController.DEBUG
            java.lang.String r2 = "SmartSpaceController"
            if (r1 == 0) goto L_0x0011
            java.lang.String r3 = "update"
            android.util.Log.d(r2, r3)
        L_0x0011:
            boolean r3 = r0.mAlarmRegistered
            r4 = 0
            if (r3 == 0) goto L_0x001f
            android.app.AlarmManager r3 = r0.mAlarmManager
            com.google.android.systemui.smartspace.SmartSpaceController$$ExternalSyntheticLambda0 r5 = r0.mExpireAlarmAction
            r3.cancel(r5)
            r0.mAlarmRegistered = r4
        L_0x001f:
            com.google.android.systemui.smartspace.SmartSpaceData r3 = r0.mData
            java.util.Objects.requireNonNull(r3)
            boolean r5 = r3.hasCurrent()
            r6 = 0
            r8 = 1
            if (r5 == 0) goto L_0x0047
            com.google.android.systemui.smartspace.SmartSpaceCard r5 = r3.mWeatherCard
            if (r5 == 0) goto L_0x0033
            r5 = r8
            goto L_0x0034
        L_0x0033:
            r5 = r4
        L_0x0034:
            if (r5 == 0) goto L_0x0047
            com.google.android.systemui.smartspace.SmartSpaceCard r5 = r3.mCurrentCard
            long r9 = r5.getExpiration()
            com.google.android.systemui.smartspace.SmartSpaceCard r3 = r3.mWeatherCard
            long r11 = r3.getExpiration()
            long r9 = java.lang.Math.min(r9, r11)
            goto L_0x0061
        L_0x0047:
            boolean r5 = r3.hasCurrent()
            if (r5 == 0) goto L_0x0054
            com.google.android.systemui.smartspace.SmartSpaceCard r3 = r3.mCurrentCard
            long r9 = r3.getExpiration()
            goto L_0x0061
        L_0x0054:
            com.google.android.systemui.smartspace.SmartSpaceCard r3 = r3.mWeatherCard
            if (r3 == 0) goto L_0x005a
            r5 = r8
            goto L_0x005b
        L_0x005a:
            r5 = r4
        L_0x005b:
            if (r5 == 0) goto L_0x0063
            long r9 = r3.getExpiration()
        L_0x0061:
            r13 = r9
            goto L_0x0064
        L_0x0063:
            r13 = r6
        L_0x0064:
            int r3 = (r13 > r6 ? 1 : (r13 == r6 ? 0 : -1))
            if (r3 <= 0) goto L_0x007a
            android.app.AlarmManager r11 = r0.mAlarmManager
            r12 = 0
            com.google.android.systemui.smartspace.SmartSpaceController$$ExternalSyntheticLambda0 r3 = r0.mExpireAlarmAction
            android.os.Handler r5 = r0.mUiHandler
            java.lang.String r15 = "SmartSpace"
            r16 = r3
            r17 = r5
            r11.set(r12, r13, r15, r16, r17)
            r0.mAlarmRegistered = r8
        L_0x007a:
            java.util.ArrayList<com.google.android.systemui.smartspace.SmartSpaceUpdateListener> r3 = r0.mListeners
            if (r3 == 0) goto L_0x00ad
            if (r1 == 0) goto L_0x0092
            java.lang.String r1 = "notifying listeners data="
            java.lang.StringBuilder r1 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r1)
            com.google.android.systemui.smartspace.SmartSpaceData r3 = r0.mData
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r2, r1)
        L_0x0092:
            java.util.ArrayList r1 = new java.util.ArrayList
            java.util.ArrayList<com.google.android.systemui.smartspace.SmartSpaceUpdateListener> r2 = r0.mListeners
            r1.<init>(r2)
            int r2 = r1.size()
        L_0x009d:
            if (r4 >= r2) goto L_0x00ad
            java.lang.Object r3 = r1.get(r4)
            com.google.android.systemui.smartspace.SmartSpaceUpdateListener r3 = (com.google.android.systemui.smartspace.SmartSpaceUpdateListener) r3
            com.google.android.systemui.smartspace.SmartSpaceData r5 = r0.mData
            r3.onSmartSpaceUpdated(r5)
            int r4 = r4 + 1
            goto L_0x009d
        L_0x00ad:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.smartspace.SmartSpaceController.update():void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.keyguard.KeyguardUpdateMonitorCallback, com.google.android.systemui.smartspace.SmartSpaceController$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public SmartSpaceController(android.content.Context r7, com.android.keyguard.KeyguardUpdateMonitor r8, android.os.Handler r9, android.app.AlarmManager r10, com.android.systemui.dump.DumpManager r11) {
        /*
            r6 = this;
            r6.<init>()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r6.mListeners = r0
            com.google.android.systemui.smartspace.SmartSpaceController$$ExternalSyntheticLambda0 r0 = new com.google.android.systemui.smartspace.SmartSpaceController$$ExternalSyntheticLambda0
            r1 = 0
            r0.<init>(r6, r1)
            r6.mExpireAlarmAction = r0
            com.google.android.systemui.smartspace.SmartSpaceController$1 r0 = new com.google.android.systemui.smartspace.SmartSpaceController$1
            r0.<init>()
            r6.mKeyguardMonitorCallback = r0
            r6.mContext = r7
            android.os.Handler r2 = new android.os.Handler
            android.os.Looper r3 = android.os.Looper.getMainLooper()
            r2.<init>(r3)
            r6.mUiHandler = r2
            com.google.android.systemui.smartspace.ProtoStore r2 = new com.google.android.systemui.smartspace.ProtoStore
            r2.<init>(r7)
            r6.mStore = r2
            android.os.HandlerThread r2 = new android.os.HandlerThread
            java.lang.String r3 = "smartspace-background"
            r2.<init>(r3)
            r2.start()
            r6.mBackgroundHandler = r9
            int r9 = android.os.UserHandle.myUserId()
            r6.mCurrentUserId = r9
            r6.mAppContext = r7
            r6.mAlarmManager = r10
            com.google.android.systemui.smartspace.SmartSpaceData r9 = new com.google.android.systemui.smartspace.SmartSpaceData
            r9.<init>()
            r6.mData = r9
            boolean r10 = r6.isSmartSpaceDisabledByExperiments()
            if (r10 == 0) goto L_0x0052
            return
        L_0x0052:
            r8.registerCallback(r0)
            r8 = 1
            com.google.android.systemui.smartspace.SmartSpaceCard r8 = r6.loadSmartSpaceData(r8)
            r9.mCurrentCard = r8
            com.google.android.systemui.smartspace.SmartSpaceCard r8 = r6.loadSmartSpaceData(r1)
            r9.mWeatherCard = r8
            r6.update()
            r6.onGsaChanged()
            com.google.android.systemui.smartspace.SmartSpaceController$2 r8 = new com.google.android.systemui.smartspace.SmartSpaceController$2
            r8.<init>()
            r9 = 4
            java.lang.String r10 = "android.intent.action.PACKAGE_ADDED"
            java.lang.String r0 = "android.intent.action.PACKAGE_CHANGED"
            java.lang.String r2 = "android.intent.action.PACKAGE_REMOVED"
            java.lang.String r3 = "android.intent.action.PACKAGE_DATA_CLEARED"
            java.lang.String[] r10 = new java.lang.String[]{r10, r0, r2, r3}
            android.content.IntentFilter r0 = new android.content.IntentFilter
            r0.<init>()
            r2 = r1
        L_0x0080:
            if (r2 >= r9) goto L_0x008a
            r3 = r10[r2]
            r0.addAction(r3)
            int r2 = r2 + 1
            goto L_0x0080
        L_0x008a:
            java.lang.String r9 = "package"
            r0.addDataScheme(r9)
            java.lang.String r9 = "com.google.android.googlequicksearchbox"
            r0.addDataSchemeSpecificPart(r9, r1)
            r9 = 2
            r7.registerReceiver(r8, r0, r9)
            android.content.IntentFilter r8 = new android.content.IntentFilter
            r8.<init>()
            java.lang.String r9 = "android.intent.action.USER_SWITCHED"
            r8.addAction(r9)
            java.lang.String r9 = "android.intent.action.USER_UNLOCKED"
            r8.addAction(r9)
            com.google.android.systemui.smartspace.SmartSpaceController$UserSwitchReceiver r9 = new com.google.android.systemui.smartspace.SmartSpaceController$UserSwitchReceiver
            r9.<init>()
            r7.registerReceiver(r9, r8)
            android.content.IntentFilter r2 = new android.content.IntentFilter
            java.lang.String r8 = "com.google.android.apps.nexuslauncher.UPDATE_SMARTSPACE"
            r2.<init>(r8)
            com.google.android.systemui.smartspace.SmartSpaceBroadcastReceiver r1 = new com.google.android.systemui.smartspace.SmartSpaceBroadcastReceiver
            r1.<init>(r6)
            android.os.Handler r4 = r6.mUiHandler
            r5 = 2
            java.lang.String r3 = "android.permission.CAPTURE_AUDIO_HOTWORD"
            r0 = r7
            r0.registerReceiver(r1, r2, r3, r4, r5)
            java.lang.Class<com.google.android.systemui.smartspace.SmartSpaceController> r7 = com.google.android.systemui.smartspace.SmartSpaceController.class
            java.lang.String r7 = r7.getName()
            r11.registerDumpable(r7, r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.smartspace.SmartSpaceController.<init>(android.content.Context, com.android.keyguard.KeyguardUpdateMonitor, android.os.Handler, android.app.AlarmManager, com.android.systemui.dump.DumpManager):void");
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println();
        printWriter.println("SmartspaceController");
        StringBuilder sb = new StringBuilder();
        sb.append("  initial broadcast: ");
        StringBuilder m = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb, this.mSmartSpaceEnabledBroadcastSent, printWriter, "  weather ");
        m.append(this.mData.mWeatherCard);
        printWriter.println(m.toString());
        printWriter.println("  current " + this.mData.mCurrentCard);
        printWriter.println("serialized:");
        printWriter.println("  weather " + loadSmartSpaceData(false));
        printWriter.println("  current " + loadSmartSpaceData(true));
        printWriter.println("disabled by experiment: " + isSmartSpaceDisabledByExperiments());
    }

    public final void onExpire(boolean z) {
        Assert.isMainThread();
        this.mAlarmRegistered = false;
        if (this.mData.handleExpire() || z) {
            update();
        } else if (DEBUG) {
            Log.d("SmartSpaceController", "onExpire - cancelled");
        }
    }
}
