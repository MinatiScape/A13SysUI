package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.telecom.TelecomManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import dagger.Lazy;
import java.util.Objects;
/* compiled from: SilenceCall.kt */
/* loaded from: classes.dex */
public final class SilenceCall extends Action {
    public boolean isPhoneRinging;
    public final SilenceAlertsDisabled silenceAlertsDisabled;
    public final Lazy<TelecomManager> telecomManager;
    public final Lazy<TelephonyListenerManager> telephonyListenerManager;
    public final Lazy<TelephonyManager> telephonyManager;
    public final String tag = "Columbus/SilenceCall";
    public final SilenceCall$phoneStateListener$1 phoneStateListener = new TelephonyCallback.CallStateListener() { // from class: com.google.android.systemui.columbus.actions.SilenceCall$phoneStateListener$1
        public final void onCallStateChanged(int i) {
            boolean z;
            SilenceCall silenceCall = SilenceCall.this;
            Objects.requireNonNull(silenceCall);
            boolean z2 = false;
            if (i == 1) {
                z = true;
            } else {
                z = false;
            }
            silenceCall.isPhoneRinging = z;
            SilenceCall silenceCall2 = SilenceCall.this;
            Objects.requireNonNull(silenceCall2);
            if (!silenceCall2.silenceAlertsDisabled.isBlocking() && silenceCall2.isPhoneRinging) {
                z2 = true;
            }
            silenceCall2.setAvailable(z2);
        }
    };

    public SilenceCall(Context context, SilenceAlertsDisabled silenceAlertsDisabled, Lazy<TelecomManager> lazy, Lazy<TelephonyManager> lazy2, Lazy<TelephonyListenerManager> lazy3) {
        super(context, null);
        this.silenceAlertsDisabled = silenceAlertsDisabled;
        this.telecomManager = lazy;
        this.telephonyManager = lazy2;
        this.telephonyListenerManager = lazy3;
        silenceAlertsDisabled.registerListener(new Gate.Listener() { // from class: com.google.android.systemui.columbus.actions.SilenceCall$gateListener$1
            @Override // com.google.android.systemui.columbus.gates.Gate.Listener
            public final void onGateChanged(Gate gate) {
                SilenceCall.this.updatePhoneStateListener();
            }
        });
        updatePhoneStateListener();
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.telecomManager.get().silenceRinger();
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" [isPhoneRinging -> ");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(sb, this.isPhoneRinging, ']');
    }

    public final void updatePhoneStateListener() {
        boolean z;
        if (this.silenceAlertsDisabled.isBlocking()) {
            this.telephonyListenerManager.get().removeCallStateListener(this.phoneStateListener);
        } else {
            this.telephonyListenerManager.get().addCallStateListener(this.phoneStateListener);
        }
        boolean z2 = false;
        if (this.telephonyManager.get().getCallState() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.isPhoneRinging = z;
        if (!this.silenceAlertsDisabled.isBlocking() && this.isPhoneRinging) {
            z2 = true;
        }
        setAvailable(z2);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }
}
