package com.google.android.systemui.power;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.power.PowerNotificationWarnings;
import com.android.wm.shell.transition.Transitions$ShellTransitionImpl$$ExternalSyntheticLambda0;
/* loaded from: classes.dex */
public final class PowerNotificationWarningsGoogleImpl extends PowerNotificationWarnings {
    public static final /* synthetic */ int $r8$clinit = 0;
    public AdaptiveChargingNotification mAdaptiveChargingNotification;
    public BatteryDefenderNotification mBatteryDefenderNotification;
    public BatteryInfoBroadcast mBatteryInfoBroadcast;
    public final BroadcastDispatcher mBroadcastDispatcher;
    @VisibleForTesting
    public final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.power.PowerNotificationWarningsGoogleImpl.1
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:133:0x03f4, code lost:
            if (r14.equals("android.intent.action.BATTERY_CHANGED") == false) goto L_0x03f6;
         */
        /* JADX WARN: Removed duplicated region for block: B:136:0x03f9  */
        /* JADX WARN: Removed duplicated region for block: B:148:0x0426  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onReceive(android.content.Context r14, android.content.Intent r15) {
            /*
                Method dump skipped, instructions count: 1126
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.power.PowerNotificationWarningsGoogleImpl.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    public final Handler mHandler;

    public PowerNotificationWarningsGoogleImpl(Context context, ActivityStarter activityStarter, BroadcastDispatcher broadcastDispatcher, UiEventLogger uiEventLogger) {
        super(context, activityStarter);
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.mBroadcastDispatcher = broadcastDispatcher;
        handler.post(new Transitions$ShellTransitionImpl$$ExternalSyntheticLambda0(this, context, uiEventLogger, 2));
    }
}
