package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda14;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.google.android.systemui.elmyra.UserContentObserver;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SilenceCall extends Action {
    public boolean mIsPhoneRinging;
    public final AnonymousClass1 mPhoneStateListener = new TelephonyCallback.CallStateListener() { // from class: com.google.android.systemui.elmyra.actions.SilenceCall.1
        public final void onCallStateChanged(int i) {
            Objects.requireNonNull(SilenceCall.this);
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            SilenceCall silenceCall = SilenceCall.this;
            if (silenceCall.mIsPhoneRinging != z) {
                silenceCall.mIsPhoneRinging = z;
                silenceCall.notifyListener();
            }
        }
    };
    public boolean mSilenceSettingEnabled;
    public final TelecomManager mTelecomManager;
    public final TelephonyListenerManager mTelephonyListenerManager;
    public final TelephonyManager mTelephonyManager;

    public SilenceCall(Context context, TelephonyListenerManager telephonyListenerManager) {
        super(context, null);
        this.mTelecomManager = (TelecomManager) context.getSystemService(TelecomManager.class);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mTelephonyListenerManager = telephonyListenerManager;
        updatePhoneStateListener();
        new UserContentObserver(this.mContext, Settings.Secure.getUriFor("assist_gesture_silence_alerts_enabled"), new NavigationBar$$ExternalSyntheticLambda14(this, 3), true);
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final boolean isAvailable() {
        if (this.mSilenceSettingEnabled) {
            return this.mIsPhoneRinging;
        }
        return false;
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.mTelecomManager.silenceRinger();
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final String toString() {
        return super.toString() + " [mSilenceSettingEnabled -> " + this.mSilenceSettingEnabled + "]";
    }

    public final void updatePhoneStateListener() {
        boolean z;
        boolean z2 = true;
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "assist_gesture_silence_alerts_enabled", 1, -2) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (z != this.mSilenceSettingEnabled) {
            this.mSilenceSettingEnabled = z;
            if (z) {
                this.mTelephonyListenerManager.addCallStateListener(this.mPhoneStateListener);
            } else {
                this.mTelephonyListenerManager.removeCallStateListener(this.mPhoneStateListener);
            }
            if (this.mTelephonyManager.getCallState() != 1) {
                z2 = false;
            }
            this.mIsPhoneRinging = z2;
            notifyListener();
        }
    }
}
