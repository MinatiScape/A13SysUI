package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import com.android.systemui.telephony.TelephonyListenerManager;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TelephonyActivity extends Gate {
    public boolean mIsCallBlocked;
    public final AnonymousClass1 mPhoneStateListener = new TelephonyCallback.CallStateListener() { // from class: com.google.android.systemui.elmyra.gates.TelephonyActivity.1
        public final void onCallStateChanged(int i) {
            boolean z;
            Objects.requireNonNull(TelephonyActivity.this);
            if (i == 2) {
                z = true;
            } else {
                z = false;
            }
            TelephonyActivity telephonyActivity = TelephonyActivity.this;
            if (z != telephonyActivity.mIsCallBlocked) {
                telephonyActivity.mIsCallBlocked = z;
                telephonyActivity.notifyListener();
            }
        }
    };
    public final TelephonyListenerManager mTelephonyListenerManager;
    public final TelephonyManager mTelephonyManager;

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onActivate() {
        boolean z;
        if (this.mTelephonyManager.getCallState() == 2) {
            z = true;
        } else {
            z = false;
        }
        this.mIsCallBlocked = z;
        this.mTelephonyListenerManager.addCallStateListener(this.mPhoneStateListener);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onDeactivate() {
        this.mTelephonyListenerManager.removeCallStateListener(this.mPhoneStateListener);
    }

    public TelephonyActivity(Context context, TelephonyListenerManager telephonyListenerManager) {
        super(context);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.mTelephonyListenerManager = telephonyListenerManager;
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final boolean isBlocked() {
        return this.mIsCallBlocked;
    }
}
