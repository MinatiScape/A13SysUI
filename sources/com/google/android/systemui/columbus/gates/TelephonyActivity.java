package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import com.android.systemui.telephony.TelephonyListenerManager;
import dagger.Lazy;
import java.util.Objects;
/* compiled from: TelephonyActivity.kt */
/* loaded from: classes.dex */
public final class TelephonyActivity extends Gate {
    public boolean isCallBlocked;
    public final TelephonyActivity$phoneStateListener$1 phoneStateListener = new TelephonyCallback.CallStateListener() { // from class: com.google.android.systemui.columbus.gates.TelephonyActivity$phoneStateListener$1
        public final void onCallStateChanged(int i) {
            boolean z;
            TelephonyActivity telephonyActivity = TelephonyActivity.this;
            Integer valueOf = Integer.valueOf(i);
            Objects.requireNonNull(telephonyActivity);
            if (valueOf != null && valueOf.intValue() == 2) {
                z = true;
            } else {
                z = false;
            }
            telephonyActivity.isCallBlocked = z;
            TelephonyActivity telephonyActivity2 = TelephonyActivity.this;
            Objects.requireNonNull(telephonyActivity2);
            telephonyActivity2.setBlocking(telephonyActivity2.isCallBlocked);
        }
    };
    public final Lazy<TelephonyListenerManager> telephonyListenerManager;
    public final Lazy<TelephonyManager> telephonyManager;

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        boolean z;
        Integer valueOf = Integer.valueOf(this.telephonyManager.get().getCallState());
        if (valueOf != null && valueOf.intValue() == 2) {
            z = true;
        } else {
            z = false;
        }
        this.isCallBlocked = z;
        this.telephonyListenerManager.get().addCallStateListener(this.phoneStateListener);
        setBlocking(this.isCallBlocked);
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        this.telephonyListenerManager.get().removeCallStateListener(this.phoneStateListener);
    }

    public TelephonyActivity(Context context, Lazy<TelephonyManager> lazy, Lazy<TelephonyListenerManager> lazy2) {
        super(context);
        this.telephonyManager = lazy;
        this.telephonyListenerManager = lazy2;
    }
}
