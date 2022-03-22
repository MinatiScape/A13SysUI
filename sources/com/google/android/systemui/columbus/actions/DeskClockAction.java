package com.google.android.systemui.columbus.actions;

import android.app.ActivityOptions;
import android.app.IActivityManager;
import android.app.SynchronousUserSwitchObserver;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DeskClockAction.kt */
/* loaded from: classes.dex */
public abstract class DeskClockAction extends Action {
    public boolean alertFiring;
    public final DeskClockAction$alertReceiver$1 alertReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.columbus.actions.DeskClockAction$alertReceiver$1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            String str;
            String str2 = null;
            if (intent == null) {
                str = null;
            } else {
                str = intent.getAction();
            }
            if (Intrinsics.areEqual(str, DeskClockAction.this.getAlertAction())) {
                DeskClockAction.this.alertFiring = true;
            } else {
                if (intent != null) {
                    str2 = intent.getAction();
                }
                if (Intrinsics.areEqual(str2, DeskClockAction.this.getDoneAction())) {
                    DeskClockAction.this.alertFiring = false;
                }
            }
            DeskClockAction deskClockAction = DeskClockAction.this;
            Objects.requireNonNull(deskClockAction);
            deskClockAction.setAvailable(deskClockAction.alertFiring);
        }
    };
    public boolean receiverRegistered;
    public final SilenceAlertsDisabled silenceAlertsDisabled;

    public abstract Intent createDismissIntent();

    public abstract String getAlertAction();

    public abstract String getDoneAction();

    public final void updateBroadcastReceiver() {
        this.alertFiring = false;
        if (this.receiverRegistered) {
            this.context.unregisterReceiver(this.alertReceiver);
            this.receiverRegistered = false;
        }
        if (!this.silenceAlertsDisabled.isBlocking()) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(getAlertAction());
            intentFilter.addAction(getDoneAction());
            this.context.registerReceiverAsUser(this.alertReceiver, UserHandle.CURRENT, intentFilter, "com.android.systemui.permission.SEND_ALERT_BROADCASTS", null, 2);
            this.receiverRegistered = true;
        }
        setAvailable(this.alertFiring);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.systemui.columbus.actions.DeskClockAction$alertReceiver$1] */
    public DeskClockAction(Context context, SilenceAlertsDisabled silenceAlertsDisabled, IActivityManager iActivityManager) {
        super(context, null);
        this.silenceAlertsDisabled = silenceAlertsDisabled;
        Gate.Listener deskClockAction$gateListener$1 = new Gate.Listener() { // from class: com.google.android.systemui.columbus.actions.DeskClockAction$gateListener$1
            @Override // com.google.android.systemui.columbus.gates.Gate.Listener
            public final void onGateChanged(Gate gate) {
                DeskClockAction.this.updateBroadcastReceiver();
            }
        };
        SynchronousUserSwitchObserver deskClockAction$userSwitchCallback$1 = new SynchronousUserSwitchObserver() { // from class: com.google.android.systemui.columbus.actions.DeskClockAction$userSwitchCallback$1
            public final void onUserSwitching(int i) throws RemoteException {
                DeskClockAction.this.updateBroadcastReceiver();
            }
        };
        silenceAlertsDisabled.registerListener(deskClockAction$gateListener$1);
        try {
            iActivityManager.registerUserSwitchObserver(deskClockAction$userSwitchCallback$1, "Columbus/DeskClockAct");
        } catch (RemoteException e) {
            Log.e("Columbus/DeskClockAct", "Failed to register user switch observer", e);
        }
        updateBroadcastReceiver();
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" [receiverRegistered -> ");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(sb, this.receiverRegistered, ']');
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        Intent createDismissIntent = createDismissIntent();
        ActivityOptions makeBasic = ActivityOptions.makeBasic();
        makeBasic.setDisallowEnterPictureInPictureWhileLaunching(true);
        createDismissIntent.setFlags(268435456);
        createDismissIntent.putExtra("android.intent.extra.REFERRER", Uri.parse(Intrinsics.stringPlus("android-app://", this.context.getPackageName())));
        try {
            this.context.startActivityAsUser(createDismissIntent, makeBasic.toBundle(), UserHandle.CURRENT);
        } catch (ActivityNotFoundException e) {
            Log.e("Columbus/DeskClockAct", "Failed to dismiss alert", e);
        }
        this.alertFiring = false;
        setAvailable(false);
    }
}
