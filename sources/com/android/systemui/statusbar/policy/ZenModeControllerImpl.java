package com.android.systemui.statusbar.policy;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.notification.ZenModeConfig;
import android.text.format.DateFormat;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.util.Utils;
import com.android.systemui.volume.VolumeDialogComponent$$ExternalSyntheticLambda0;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda2;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda7;
import com.android.wm.shell.ShellInitImpl$$ExternalSyntheticLambda5;
import com.android.wm.shell.ShellInitImpl$$ExternalSyntheticLambda6;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ZenModeControllerImpl extends CurrentUserTracker implements ZenModeController, Dumpable {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final AlarmManager mAlarmManager;
    public ZenModeConfig mConfig;
    public final AnonymousClass2 mConfigSetting;
    public NotificationManager.Policy mConsolidatedNotificationPolicy;
    public final Context mContext;
    public final AnonymousClass1 mModeSetting;
    public final NotificationManager mNoMan;
    public boolean mRegistered;
    public final SetupObserver mSetupObserver;
    public int mUserId;
    public int mZenMode;
    public long mZenUpdateTime;
    public final ArrayList<ZenModeController.Callback> mCallbacks = new ArrayList<>();
    public final Object mCallbacksLock = new Object();
    public final AnonymousClass3 mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.ZenModeControllerImpl.3
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if ("android.app.action.NEXT_ALARM_CLOCK_CHANGED".equals(intent.getAction())) {
                ZenModeControllerImpl zenModeControllerImpl = ZenModeControllerImpl.this;
                Objects.requireNonNull(zenModeControllerImpl);
                synchronized (zenModeControllerImpl.mCallbacksLock) {
                    Utils.safeForeach(zenModeControllerImpl.mCallbacks, ShellInitImpl$$ExternalSyntheticLambda6.INSTANCE$2);
                }
            }
            if ("android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED".equals(intent.getAction())) {
                ZenModeControllerImpl zenModeControllerImpl2 = ZenModeControllerImpl.this;
                Objects.requireNonNull(zenModeControllerImpl2);
                synchronized (zenModeControllerImpl2.mCallbacksLock) {
                    Utils.safeForeach(zenModeControllerImpl2.mCallbacks, ShellInitImpl$$ExternalSyntheticLambda5.INSTANCE$1);
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public final class SetupObserver extends ContentObserver {
        public boolean mRegistered;
        public final ContentResolver mResolver;

        public SetupObserver(Handler handler) {
            super(handler);
            this.mResolver = ZenModeControllerImpl.this.mContext.getContentResolver();
        }

        @Override // android.database.ContentObserver
        public final void onChange(boolean z, Uri uri) {
            if (Settings.Global.getUriFor("device_provisioned").equals(uri) || Settings.Secure.getUriFor("user_setup_complete").equals(uri)) {
                ZenModeControllerImpl zenModeControllerImpl = ZenModeControllerImpl.this;
                boolean isZenAvailable = zenModeControllerImpl.isZenAvailable();
                synchronized (zenModeControllerImpl.mCallbacksLock) {
                    Utils.safeForeach(zenModeControllerImpl.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda1(isZenAvailable));
                }
            }
        }

        public final void register() {
            if (this.mRegistered) {
                this.mResolver.unregisterContentObserver(this);
            }
            this.mResolver.registerContentObserver(Settings.Global.getUriFor("device_provisioned"), false, this);
            this.mResolver.registerContentObserver(Settings.Secure.getUriFor("user_setup_complete"), false, this, ZenModeControllerImpl.this.mUserId);
            this.mRegistered = true;
            ZenModeControllerImpl zenModeControllerImpl = ZenModeControllerImpl.this;
            boolean isZenAvailable = zenModeControllerImpl.isZenAvailable();
            synchronized (zenModeControllerImpl.mCallbacksLock) {
                Utils.safeForeach(zenModeControllerImpl.mCallbacks, new ZenModeControllerImpl$$ExternalSyntheticLambda1(isZenAvailable));
            }
        }
    }

    static {
        Log.isLoggable("ZenModeController", 3);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(ZenModeController.Callback callback) {
        ZenModeController.Callback callback2 = callback;
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.add(callback2);
        }
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public final boolean areNotificationsHiddenInShade() {
        if (this.mZenMode == 0 || (this.mConsolidatedNotificationPolicy.suppressedVisualEffects & 256) == 0) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "ZenModeControllerImpl:", "  mZenMode=");
        m.append(this.mZenMode);
        printWriter.println(m.toString());
        printWriter.println("  mConfig=" + this.mConfig);
        printWriter.println("  mConsolidatedNotificationPolicy=" + this.mConsolidatedNotificationPolicy);
        printWriter.println("  mZenUpdateTime=" + ((Object) DateFormat.format("MM-dd HH:mm:ss", this.mZenUpdateTime)));
    }

    @VisibleForTesting
    public void fireConfigChanged(ZenModeConfig zenModeConfig) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new VolumeDialogComponent$$ExternalSyntheticLambda0(zenModeConfig, 2));
        }
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public final long getNextAlarm() {
        AlarmManager.AlarmClockInfo nextAlarmClock = this.mAlarmManager.getNextAlarmClock(this.mUserId);
        if (nextAlarmClock != null) {
            return nextAlarmClock.getTriggerTime();
        }
        return 0L;
    }

    public final boolean isZenAvailable() {
        boolean z;
        boolean z2;
        SetupObserver setupObserver = this.mSetupObserver;
        Objects.requireNonNull(setupObserver);
        if (Settings.Global.getInt(setupObserver.mResolver, "device_provisioned", 0) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            return false;
        }
        SetupObserver setupObserver2 = this.mSetupObserver;
        Objects.requireNonNull(setupObserver2);
        if (Settings.Secure.getIntForUser(setupObserver2.mResolver, "user_setup_complete", 0, ZenModeControllerImpl.this.mUserId) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.settings.CurrentUserTracker
    public final void onUserSwitched(int i) {
        this.mUserId = i;
        if (this.mRegistered) {
            this.mContext.unregisterReceiver(this.mReceiver);
        }
        IntentFilter intentFilter = new IntentFilter("android.app.action.NEXT_ALARM_CLOCK_CHANGED");
        intentFilter.addAction("android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED");
        this.mContext.registerReceiverAsUser(this.mReceiver, new UserHandle(this.mUserId), intentFilter, null, null);
        this.mRegistered = true;
        this.mSetupObserver.register();
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(ZenModeController.Callback callback) {
        ZenModeController.Callback callback2 = callback;
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.remove(callback2);
        }
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public final void setZen(int i, Uri uri, String str) {
        this.mNoMan.setZenMode(i, uri, str);
    }

    @VisibleForTesting
    public void updateConsolidatedNotificationPolicy() {
        NotificationManager.Policy consolidatedNotificationPolicy = this.mNoMan.getConsolidatedNotificationPolicy();
        if (!Objects.equals(consolidatedNotificationPolicy, this.mConsolidatedNotificationPolicy)) {
            this.mConsolidatedNotificationPolicy = consolidatedNotificationPolicy;
            synchronized (this.mCallbacksLock) {
                Utils.safeForeach(this.mCallbacks, new WMShell$$ExternalSyntheticLambda2(consolidatedNotificationPolicy, 3));
            }
        }
    }

    @VisibleForTesting
    public void updateZenMode(int i) {
        this.mZenMode = i;
        this.mZenUpdateTime = System.currentTimeMillis();
    }

    @VisibleForTesting
    public void updateZenModeConfig() {
        ZenModeConfig.ZenRule zenRule;
        ZenModeConfig zenModeConfig = this.mNoMan.getZenModeConfig();
        if (!Objects.equals(zenModeConfig, this.mConfig)) {
            ZenModeConfig zenModeConfig2 = this.mConfig;
            ZenModeConfig.ZenRule zenRule2 = null;
            if (zenModeConfig2 != null) {
                zenRule = zenModeConfig2.manualRule;
            } else {
                zenRule = null;
            }
            this.mConfig = zenModeConfig;
            this.mZenUpdateTime = System.currentTimeMillis();
            fireConfigChanged(zenModeConfig);
            if (zenModeConfig != null) {
                zenRule2 = zenModeConfig.manualRule;
            }
            if (!Objects.equals(zenRule, zenRule2)) {
                synchronized (this.mCallbacksLock) {
                    Utils.safeForeach(this.mCallbacks, new WMShell$$ExternalSyntheticLambda7(zenRule2, 1));
                }
            }
            NotificationManager.Policy consolidatedNotificationPolicy = this.mNoMan.getConsolidatedNotificationPolicy();
            if (!Objects.equals(consolidatedNotificationPolicy, this.mConsolidatedNotificationPolicy)) {
                this.mConsolidatedNotificationPolicy = consolidatedNotificationPolicy;
                synchronized (this.mCallbacksLock) {
                    Utils.safeForeach(this.mCallbacks, new WMShell$$ExternalSyntheticLambda2(consolidatedNotificationPolicy, 3));
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.statusbar.policy.ZenModeControllerImpl$2, com.android.systemui.qs.SettingObserver] */
    /* JADX WARN: Type inference failed for: r4v3, types: [com.android.systemui.statusbar.policy.ZenModeControllerImpl$3] */
    /* JADX WARN: Type inference failed for: r4v4, types: [com.android.systemui.qs.SettingObserver, com.android.systemui.statusbar.policy.ZenModeControllerImpl$1] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ZenModeControllerImpl(android.content.Context r2, android.os.Handler r3, com.android.systemui.broadcast.BroadcastDispatcher r4, com.android.systemui.dump.DumpManager r5, com.android.systemui.util.settings.GlobalSettings r6) {
        /*
            r1 = this;
            r1.<init>(r4)
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            r1.mCallbacks = r4
            java.lang.Object r4 = new java.lang.Object
            r4.<init>()
            r1.mCallbacksLock = r4
            com.android.systemui.statusbar.policy.ZenModeControllerImpl$3 r4 = new com.android.systemui.statusbar.policy.ZenModeControllerImpl$3
            r4.<init>()
            r1.mReceiver = r4
            r1.mContext = r2
            com.android.systemui.statusbar.policy.ZenModeControllerImpl$1 r4 = new com.android.systemui.statusbar.policy.ZenModeControllerImpl$1
            r4.<init>(r6, r3)
            r1.mModeSetting = r4
            com.android.systemui.statusbar.policy.ZenModeControllerImpl$2 r0 = new com.android.systemui.statusbar.policy.ZenModeControllerImpl$2
            r0.<init>(r6, r3)
            r1.mConfigSetting = r0
            java.lang.String r6 = "notification"
            java.lang.Object r6 = r2.getSystemService(r6)
            android.app.NotificationManager r6 = (android.app.NotificationManager) r6
            r1.mNoMan = r6
            r6 = 1
            r4.setListening(r6)
            int r4 = r4.getValue()
            r1.updateZenMode(r4)
            r0.setListening(r6)
            r1.updateZenModeConfig()
            r1.updateConsolidatedNotificationPolicy()
            java.lang.String r4 = "alarm"
            java.lang.Object r4 = r2.getSystemService(r4)
            android.app.AlarmManager r4 = (android.app.AlarmManager) r4
            r1.mAlarmManager = r4
            com.android.systemui.statusbar.policy.ZenModeControllerImpl$SetupObserver r4 = new com.android.systemui.statusbar.policy.ZenModeControllerImpl$SetupObserver
            r4.<init>(r3)
            r1.mSetupObserver = r4
            r4.register()
            java.lang.Class<android.os.UserManager> r3 = android.os.UserManager.class
            java.lang.Object r2 = r2.getSystemService(r3)
            android.os.UserManager r2 = (android.os.UserManager) r2
            r1.startTracking()
            java.lang.String r2 = "ZenModeControllerImpl"
            r5.registerDumpable(r2, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.ZenModeControllerImpl.<init>(android.content.Context, android.os.Handler, com.android.systemui.broadcast.BroadcastDispatcher, com.android.systemui.dump.DumpManager, com.android.systemui.util.settings.GlobalSettings):void");
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public final ZenModeConfig getConfig() {
        return this.mConfig;
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public final NotificationManager.Policy getConsolidatedPolicy() {
        return this.mConsolidatedNotificationPolicy;
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public final int getZen() {
        return this.mZenMode;
    }
}
