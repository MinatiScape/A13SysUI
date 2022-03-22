package com.google.android.systemui.power;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import androidx.core.app.NotificationCompat$Action;
import androidx.core.app.NotificationCompat$Builder;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardPINView$$ExternalSyntheticLambda0;
import com.google.android.systemui.adaptivecharging.AdaptiveChargingManager;
import com.google.android.systemui.power.AdaptiveChargingNotification;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class AdaptiveChargingNotification {
    public final AdaptiveChargingManager mAdaptiveChargingManager;
    public final Context mContext;
    public final NotificationManager mNotificationManager;
    @VisibleForTesting
    public boolean mAdaptiveChargingQueryInBackground = true;
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    @VisibleForTesting
    public boolean mWasActive = false;

    /* renamed from: com.google.android.systemui.power.AdaptiveChargingNotification$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements AdaptiveChargingManager.AdaptiveChargingStatusReceiver {
        public final /* synthetic */ boolean val$forceUpdate;

        @Override // com.google.android.systemui.adaptivecharging.AdaptiveChargingManager.AdaptiveChargingStatusReceiver
        public final void onDestroyInterface() {
        }

        public AnonymousClass1(boolean z) {
            this.val$forceUpdate = z;
        }

        @Override // com.google.android.systemui.adaptivecharging.AdaptiveChargingManager.AdaptiveChargingStatusReceiver
        public final void onReceiveStatus(final String str, final int i) {
            Handler handler = AdaptiveChargingNotification.this.mHandler;
            final boolean z = this.val$forceUpdate;
            handler.post(new Runnable() { // from class: com.google.android.systemui.power.AdaptiveChargingNotification$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    boolean z2;
                    boolean z3;
                    AdaptiveChargingNotification.AnonymousClass1 r0 = AdaptiveChargingNotification.AnonymousClass1.this;
                    String str2 = str;
                    int i2 = i;
                    boolean z4 = z;
                    Objects.requireNonNull(r0);
                    AdaptiveChargingNotification adaptiveChargingNotification = AdaptiveChargingNotification.this;
                    Objects.requireNonNull(adaptiveChargingNotification);
                    boolean z5 = AdaptiveChargingManager.DEBUG;
                    if ("Active".equals(str2) || "Enabled".equals(str2)) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (!z2 || i2 <= 0) {
                        z3 = false;
                    } else {
                        z3 = true;
                    }
                    if (!z3) {
                        adaptiveChargingNotification.cancelNotification();
                    } else if (!adaptiveChargingNotification.mWasActive || z4) {
                        String formatTimeToFull = adaptiveChargingNotification.mAdaptiveChargingManager.formatTimeToFull(TimeUnit.SECONDS.toMillis(i2 + 29) + System.currentTimeMillis());
                        NotificationCompat$Builder notificationCompat$Builder = new NotificationCompat$Builder(adaptiveChargingNotification.mContext);
                        notificationCompat$Builder.mShowWhen = false;
                        notificationCompat$Builder.mNotification.icon = 2131231766;
                        notificationCompat$Builder.mContentTitle = NotificationCompat$Builder.limitCharSequenceLength(adaptiveChargingNotification.mContext.getString(2131951837));
                        notificationCompat$Builder.mContentText = NotificationCompat$Builder.limitCharSequenceLength(adaptiveChargingNotification.mContext.getString(2131951835, formatTimeToFull));
                        notificationCompat$Builder.mActions.add(new NotificationCompat$Action(adaptiveChargingNotification.mContext.getString(2131951838), PowerUtils.createNormalChargingIntent(adaptiveChargingNotification.mContext, "PNW.acChargeNormally")));
                        PowerUtils.overrideNotificationAppName(adaptiveChargingNotification.mContext, notificationCompat$Builder, 17039658);
                        adaptiveChargingNotification.mNotificationManager.notifyAsUser("adaptive_charging", 2131951837, notificationCompat$Builder.build(), UserHandle.ALL);
                        adaptiveChargingNotification.mWasActive = true;
                    }
                }
            });
        }
    }

    public final void cancelNotification() {
        if (this.mWasActive) {
            this.mNotificationManager.cancelAsUser("adaptive_charging", 2131951837, UserHandle.ALL);
            this.mWasActive = false;
        }
    }

    public final void checkAdaptiveChargingStatus(boolean z) {
        Objects.requireNonNull(this.mAdaptiveChargingManager);
        if (DeviceConfig.getBoolean("adaptive_charging", "adaptive_charging_notification", false)) {
            AnonymousClass1 r0 = new AnonymousClass1(z);
            if (!this.mAdaptiveChargingQueryInBackground) {
                this.mAdaptiveChargingManager.queryStatus(r0);
            } else {
                AsyncTask.execute(new KeyguardPINView$$ExternalSyntheticLambda0(this, r0, 4));
            }
        }
    }

    @VisibleForTesting
    public void resolveBatteryChangedIntent(Intent intent) {
        boolean z;
        boolean z2;
        boolean z3 = true;
        if (intent.getIntExtra("plugged", 0) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (intent.getIntExtra("status", 1) == 5) {
            z2 = true;
        } else {
            z2 = false;
        }
        int i = -1;
        int intExtra = intent.getIntExtra("level", -1);
        int intExtra2 = intent.getIntExtra("scale", 0);
        if (intExtra2 != 0) {
            i = Math.round((intExtra / intExtra2) * 100.0f);
        }
        if (!z2 && i < 100) {
            z3 = false;
        }
        if (!z || z3) {
            cancelNotification();
        } else {
            checkAdaptiveChargingStatus(false);
        }
    }

    @VisibleForTesting
    public AdaptiveChargingNotification(Context context, AdaptiveChargingManager adaptiveChargingManager) {
        this.mContext = context;
        this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mAdaptiveChargingManager = adaptiveChargingManager;
    }
}
