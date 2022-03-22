package com.android.keyguard;

import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.telephony.SubscriptionManager;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.DejankUtils;
import java.util.Objects;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class KeyguardSecurityModel {
    public final boolean mIsPukScreenAvailable;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final LockPatternUtils mLockPatternUtils;

    /* loaded from: classes.dex */
    public enum SecurityMode {
        Invalid,
        None,
        Pattern,
        Password,
        PIN,
        SimPin,
        SimPuk
    }

    public final SecurityMode getSecurityMode(final int i) {
        if (this.mIsPukScreenAvailable && SubscriptionManager.isValidSubscriptionId(this.mKeyguardUpdateMonitor.getNextSubIdForState(3))) {
            return SecurityMode.SimPuk;
        }
        if (SubscriptionManager.isValidSubscriptionId(this.mKeyguardUpdateMonitor.getNextSubIdForState(2))) {
            return SecurityMode.SimPin;
        }
        int intValue = ((Integer) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.keyguard.KeyguardSecurityModel$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                KeyguardSecurityModel keyguardSecurityModel = KeyguardSecurityModel.this;
                int i2 = i;
                Objects.requireNonNull(keyguardSecurityModel);
                return Integer.valueOf(keyguardSecurityModel.mLockPatternUtils.getActivePasswordQuality(i2));
            }
        })).intValue();
        if (intValue == 0) {
            return SecurityMode.None;
        }
        if (intValue == 65536) {
            return SecurityMode.Pattern;
        }
        if (intValue == 131072 || intValue == 196608) {
            return SecurityMode.PIN;
        }
        if (intValue == 262144 || intValue == 327680 || intValue == 393216 || intValue == 524288) {
            return SecurityMode.Password;
        }
        throw new IllegalStateException(VendorAtomValue$$ExternalSyntheticOutline0.m("Unknown security quality:", intValue));
    }

    public KeyguardSecurityModel(Resources resources, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mIsPukScreenAvailable = resources.getBoolean(17891650);
        this.mLockPatternUtils = lockPatternUtils;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
    }
}
