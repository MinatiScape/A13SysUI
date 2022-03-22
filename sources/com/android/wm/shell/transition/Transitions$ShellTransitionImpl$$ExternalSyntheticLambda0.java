package com.android.wm.shell.transition;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.window.RemoteTransition;
import android.window.TransitionFilter;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import com.android.wm.shell.transition.Transitions;
import com.google.android.systemui.adaptivecharging.AdaptiveChargingManager;
import com.google.android.systemui.power.AdaptiveChargingNotification;
import com.google.android.systemui.power.BatteryDefenderNotification;
import com.google.android.systemui.power.BatteryInfoBroadcast;
import com.google.android.systemui.power.PowerNotificationWarningsGoogleImpl;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class Transitions$ShellTransitionImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ Transitions$ShellTransitionImpl$$ExternalSyntheticLambda0(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                Transitions.ShellTransitionImpl shellTransitionImpl = (Transitions.ShellTransitionImpl) this.f$0;
                RemoteTransition remoteTransition = (RemoteTransition) this.f$2;
                Objects.requireNonNull(shellTransitionImpl);
                RemoteTransitionHandler remoteTransitionHandler = Transitions.this.mRemoteTransitionHandler;
                Objects.requireNonNull(remoteTransitionHandler);
                remoteTransitionHandler.handleDeath(remoteTransition.asBinder(), null);
                remoteTransitionHandler.mFilters.add(new Pair<>((TransitionFilter) this.f$1, remoteTransition));
                return;
            case 1:
                int i = NotificationInfo.$r8$clinit;
                ((View) this.f$0).setSelected(true);
                ((View) this.f$1).setSelected(false);
                ((View) this.f$2).setSelected(false);
                return;
            default:
                PowerNotificationWarningsGoogleImpl powerNotificationWarningsGoogleImpl = (PowerNotificationWarningsGoogleImpl) this.f$0;
                Context context = (Context) this.f$1;
                int i2 = PowerNotificationWarningsGoogleImpl.$r8$clinit;
                Objects.requireNonNull(powerNotificationWarningsGoogleImpl);
                long currentTimeMillis = System.currentTimeMillis();
                powerNotificationWarningsGoogleImpl.mBatteryDefenderNotification = new BatteryDefenderNotification(context, (UiEventLogger) this.f$2);
                powerNotificationWarningsGoogleImpl.mAdaptiveChargingNotification = new AdaptiveChargingNotification(context, new AdaptiveChargingManager(context));
                powerNotificationWarningsGoogleImpl.mBatteryInfoBroadcast = new BatteryInfoBroadcast(context);
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
                intentFilter.addAction("PNW.defenderResumeCharging");
                intentFilter.addAction("PNW.defenderResumeCharging.settings");
                intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
                intentFilter.addAction("com.google.android.systemui.adaptivecharging.ADAPTIVE_CHARGING_DEADLINE_SET");
                intentFilter.addAction("PNW.acChargeNormally");
                intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
                intentFilter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
                intentFilter.addAction("android.bluetooth.device.action.BATTERY_LEVEL_CHANGED");
                intentFilter.addAction("android.bluetooth.device.action.ALIAS_CHANGED");
                intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
                intentFilter.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
                intentFilter.addAction("android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED");
                powerNotificationWarningsGoogleImpl.mBroadcastDispatcher.registerReceiverWithHandler(powerNotificationWarningsGoogleImpl.mBroadcastReceiver, intentFilter, powerNotificationWarningsGoogleImpl.mHandler);
                Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
                if (registerReceiver != null) {
                    powerNotificationWarningsGoogleImpl.mBroadcastReceiver.onReceive(context, registerReceiver);
                }
                Log.d("PowerNotificationWarningsGoogleImpl", String.format("Finish initialize in %d/ms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis)));
                return;
        }
    }
}
