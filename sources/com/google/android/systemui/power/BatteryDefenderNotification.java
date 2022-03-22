package com.google.android.systemui.power;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.AsyncTask;
import android.os.UserHandle;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda0;
/* loaded from: classes.dex */
public final class BatteryDefenderNotification {
    public int mBatteryLevel;
    public final Context mContext;
    @VisibleForTesting
    public boolean mDefenderEnabled;
    public final NotificationManager mNotificationManager;
    @VisibleForTesting
    public boolean mPostNotificationVisible;
    @VisibleForTesting
    public boolean mPrvPluggedState;
    @VisibleForTesting
    public boolean mRunBypassActionTask = true;
    public SharedPreferences mSharedPreferences;
    public final UiEventLogger mUiEventLogger;

    /* loaded from: classes.dex */
    public enum BatteryDefenderEvent implements UiEventLogger.UiEventEnum {
        BATTERY_DEFENDER_NOTIFICATION(876),
        BATTERY_DEFENDER_BYPASS_LIMIT(877),
        BATTERY_DEFENDER_BYPASS_LIMIT_FOR_TIPS(878);
        
        private final int mId;

        BatteryDefenderEvent(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    public final SharedPreferences getSharedPreferences() {
        if (this.mSharedPreferences == null) {
            this.mSharedPreferences = this.mContext.getApplicationContext().getSharedPreferences("defender_shared_prefs", 0);
        }
        return this.mSharedPreferences;
    }

    public final void resumeCharging(BatteryDefenderEvent batteryDefenderEvent) {
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        if (uiEventLogger != null) {
            uiEventLogger.logWithPosition(batteryDefenderEvent, 0, (String) null, this.mBatteryLevel);
        }
        KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(VendorAtomValue$$ExternalSyntheticOutline1.m("resume charging: "), batteryDefenderEvent.mId, "BatteryDefenderNotification");
        if (this.mRunBypassActionTask) {
            AsyncTask.execute(new WMShell$7$$ExternalSyntheticLambda0(this, 9));
        }
        this.mNotificationManager.cancelAsUser("battery_defender", 2131952265, UserHandle.ALL);
        getSharedPreferences().edit().clear().apply();
    }

    public BatteryDefenderNotification(Context context, UiEventLogger uiEventLogger) {
        this.mContext = context;
        this.mUiEventLogger = uiEventLogger;
        this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
    }
}
