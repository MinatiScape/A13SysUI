package com.android.keyguard;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import java.util.Objects;
/* compiled from: KeyguardListenModel.kt */
/* loaded from: classes.dex */
public final class KeyguardActiveUnlockModel extends KeyguardListenModel {
    public final boolean authInterruptActive;
    public final boolean encryptedOrTimedOut;
    public final boolean fpLockout;
    public final boolean listening;
    public final boolean lockDown;
    public final boolean switchingUser;
    public final long timeMillis;
    public final boolean triggerActiveUnlockForAssistant;
    public final boolean userCanDismissLockScreen;
    public final int userId;

    public KeyguardActiveUnlockModel(long j, int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8) {
        super(0);
        this.timeMillis = j;
        this.userId = i;
        this.listening = z;
        this.authInterruptActive = z2;
        this.encryptedOrTimedOut = z3;
        this.fpLockout = z4;
        this.lockDown = z5;
        this.switchingUser = z6;
        this.triggerActiveUnlockForAssistant = z7;
        this.userCanDismissLockScreen = z8;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KeyguardActiveUnlockModel)) {
            return false;
        }
        KeyguardActiveUnlockModel keyguardActiveUnlockModel = (KeyguardActiveUnlockModel) obj;
        long j = this.timeMillis;
        Objects.requireNonNull(keyguardActiveUnlockModel);
        return j == keyguardActiveUnlockModel.timeMillis && this.userId == keyguardActiveUnlockModel.userId && this.listening == keyguardActiveUnlockModel.listening && this.authInterruptActive == keyguardActiveUnlockModel.authInterruptActive && this.encryptedOrTimedOut == keyguardActiveUnlockModel.encryptedOrTimedOut && this.fpLockout == keyguardActiveUnlockModel.fpLockout && this.lockDown == keyguardActiveUnlockModel.lockDown && this.switchingUser == keyguardActiveUnlockModel.switchingUser && this.triggerActiveUnlockForAssistant == keyguardActiveUnlockModel.triggerActiveUnlockForAssistant && this.userCanDismissLockScreen == keyguardActiveUnlockModel.userCanDismissLockScreen;
    }

    public final int hashCode() {
        int m = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.userId, Long.hashCode(this.timeMillis) * 31, 31);
        boolean z = this.listening;
        int i = 1;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = (m + i2) * 31;
        boolean z2 = this.authInterruptActive;
        if (z2) {
            z2 = true;
        }
        int i5 = z2 ? 1 : 0;
        int i6 = z2 ? 1 : 0;
        int i7 = (i4 + i5) * 31;
        boolean z3 = this.encryptedOrTimedOut;
        if (z3) {
            z3 = true;
        }
        int i8 = z3 ? 1 : 0;
        int i9 = z3 ? 1 : 0;
        int i10 = (i7 + i8) * 31;
        boolean z4 = this.fpLockout;
        if (z4) {
            z4 = true;
        }
        int i11 = z4 ? 1 : 0;
        int i12 = z4 ? 1 : 0;
        int i13 = (i10 + i11) * 31;
        boolean z5 = this.lockDown;
        if (z5) {
            z5 = true;
        }
        int i14 = z5 ? 1 : 0;
        int i15 = z5 ? 1 : 0;
        int i16 = (i13 + i14) * 31;
        boolean z6 = this.switchingUser;
        if (z6) {
            z6 = true;
        }
        int i17 = z6 ? 1 : 0;
        int i18 = z6 ? 1 : 0;
        int i19 = (i16 + i17) * 31;
        boolean z7 = this.triggerActiveUnlockForAssistant;
        if (z7) {
            z7 = true;
        }
        int i20 = z7 ? 1 : 0;
        int i21 = z7 ? 1 : 0;
        int i22 = (i19 + i20) * 31;
        boolean z8 = this.userCanDismissLockScreen;
        if (!z8) {
            i = z8 ? 1 : 0;
        }
        return i22 + i;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("KeyguardActiveUnlockModel(timeMillis=");
        m.append(this.timeMillis);
        m.append(", userId=");
        m.append(this.userId);
        m.append(", listening=");
        m.append(this.listening);
        m.append(", authInterruptActive=");
        m.append(this.authInterruptActive);
        m.append(", encryptedOrTimedOut=");
        m.append(this.encryptedOrTimedOut);
        m.append(", fpLockout=");
        m.append(this.fpLockout);
        m.append(", lockDown=");
        m.append(this.lockDown);
        m.append(", switchingUser=");
        m.append(this.switchingUser);
        m.append(", triggerActiveUnlockForAssistant=");
        m.append(this.triggerActiveUnlockForAssistant);
        m.append(", userCanDismissLockScreen=");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.userCanDismissLockScreen, ')');
    }

    @Override // com.android.keyguard.KeyguardListenModel
    public final boolean getListening() {
        return this.listening;
    }

    @Override // com.android.keyguard.KeyguardListenModel
    public final long getTimeMillis() {
        return this.timeMillis;
    }
}
