package com.android.keyguard;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import java.util.Objects;
/* compiled from: KeyguardListenModel.kt */
/* loaded from: classes.dex */
public final class KeyguardFaceListenModel extends KeyguardListenModel {
    public final boolean authInterruptActive;
    public final boolean becauseCannotSkipBouncer;
    public final boolean biometricSettingEnabledForUser;
    public final boolean bouncer;
    public final boolean faceAuthenticated;
    public final boolean faceDisabled;
    public final boolean keyguardAwake;
    public final boolean keyguardGoingAway;
    public final boolean listening;
    public final boolean listeningForFaceAssistant;
    public final boolean lockIconPressed;
    public final boolean occludingAppRequestingFaceAuth;
    public final boolean primaryUser;
    public final boolean scanningAllowedByStrongAuth;
    public final boolean secureCameraLaunched;
    public final boolean switchingUser;
    public final long timeMillis;
    public final int userId;

    public KeyguardFaceListenModel(long j, int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9, boolean z10, boolean z11, boolean z12, boolean z13, boolean z14, boolean z15, boolean z16) {
        super(0);
        this.timeMillis = j;
        this.userId = i;
        this.listening = z;
        this.authInterruptActive = z2;
        this.becauseCannotSkipBouncer = z3;
        this.biometricSettingEnabledForUser = z4;
        this.bouncer = z5;
        this.faceAuthenticated = z6;
        this.faceDisabled = z7;
        this.keyguardAwake = z8;
        this.keyguardGoingAway = z9;
        this.listeningForFaceAssistant = z10;
        this.lockIconPressed = z11;
        this.occludingAppRequestingFaceAuth = z12;
        this.primaryUser = z13;
        this.scanningAllowedByStrongAuth = z14;
        this.secureCameraLaunched = z15;
        this.switchingUser = z16;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KeyguardFaceListenModel)) {
            return false;
        }
        KeyguardFaceListenModel keyguardFaceListenModel = (KeyguardFaceListenModel) obj;
        long j = this.timeMillis;
        Objects.requireNonNull(keyguardFaceListenModel);
        return j == keyguardFaceListenModel.timeMillis && this.userId == keyguardFaceListenModel.userId && this.listening == keyguardFaceListenModel.listening && this.authInterruptActive == keyguardFaceListenModel.authInterruptActive && this.becauseCannotSkipBouncer == keyguardFaceListenModel.becauseCannotSkipBouncer && this.biometricSettingEnabledForUser == keyguardFaceListenModel.biometricSettingEnabledForUser && this.bouncer == keyguardFaceListenModel.bouncer && this.faceAuthenticated == keyguardFaceListenModel.faceAuthenticated && this.faceDisabled == keyguardFaceListenModel.faceDisabled && this.keyguardAwake == keyguardFaceListenModel.keyguardAwake && this.keyguardGoingAway == keyguardFaceListenModel.keyguardGoingAway && this.listeningForFaceAssistant == keyguardFaceListenModel.listeningForFaceAssistant && this.lockIconPressed == keyguardFaceListenModel.lockIconPressed && this.occludingAppRequestingFaceAuth == keyguardFaceListenModel.occludingAppRequestingFaceAuth && this.primaryUser == keyguardFaceListenModel.primaryUser && this.scanningAllowedByStrongAuth == keyguardFaceListenModel.scanningAllowedByStrongAuth && this.secureCameraLaunched == keyguardFaceListenModel.secureCameraLaunched && this.switchingUser == keyguardFaceListenModel.switchingUser;
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
        boolean z3 = this.becauseCannotSkipBouncer;
        if (z3) {
            z3 = true;
        }
        int i8 = z3 ? 1 : 0;
        int i9 = z3 ? 1 : 0;
        int i10 = (i7 + i8) * 31;
        boolean z4 = this.biometricSettingEnabledForUser;
        if (z4) {
            z4 = true;
        }
        int i11 = z4 ? 1 : 0;
        int i12 = z4 ? 1 : 0;
        int i13 = (i10 + i11) * 31;
        boolean z5 = this.bouncer;
        if (z5) {
            z5 = true;
        }
        int i14 = z5 ? 1 : 0;
        int i15 = z5 ? 1 : 0;
        int i16 = (i13 + i14) * 31;
        boolean z6 = this.faceAuthenticated;
        if (z6) {
            z6 = true;
        }
        int i17 = z6 ? 1 : 0;
        int i18 = z6 ? 1 : 0;
        int i19 = (i16 + i17) * 31;
        boolean z7 = this.faceDisabled;
        if (z7) {
            z7 = true;
        }
        int i20 = z7 ? 1 : 0;
        int i21 = z7 ? 1 : 0;
        int i22 = (i19 + i20) * 31;
        boolean z8 = this.keyguardAwake;
        if (z8) {
            z8 = true;
        }
        int i23 = z8 ? 1 : 0;
        int i24 = z8 ? 1 : 0;
        int i25 = (i22 + i23) * 31;
        boolean z9 = this.keyguardGoingAway;
        if (z9) {
            z9 = true;
        }
        int i26 = z9 ? 1 : 0;
        int i27 = z9 ? 1 : 0;
        int i28 = (i25 + i26) * 31;
        boolean z10 = this.listeningForFaceAssistant;
        if (z10) {
            z10 = true;
        }
        int i29 = z10 ? 1 : 0;
        int i30 = z10 ? 1 : 0;
        int i31 = (i28 + i29) * 31;
        boolean z11 = this.lockIconPressed;
        if (z11) {
            z11 = true;
        }
        int i32 = z11 ? 1 : 0;
        int i33 = z11 ? 1 : 0;
        int i34 = (i31 + i32) * 31;
        boolean z12 = this.occludingAppRequestingFaceAuth;
        if (z12) {
            z12 = true;
        }
        int i35 = z12 ? 1 : 0;
        int i36 = z12 ? 1 : 0;
        int i37 = (i34 + i35) * 31;
        boolean z13 = this.primaryUser;
        if (z13) {
            z13 = true;
        }
        int i38 = z13 ? 1 : 0;
        int i39 = z13 ? 1 : 0;
        int i40 = (i37 + i38) * 31;
        boolean z14 = this.scanningAllowedByStrongAuth;
        if (z14) {
            z14 = true;
        }
        int i41 = z14 ? 1 : 0;
        int i42 = z14 ? 1 : 0;
        int i43 = (i40 + i41) * 31;
        boolean z15 = this.secureCameraLaunched;
        if (z15) {
            z15 = true;
        }
        int i44 = z15 ? 1 : 0;
        int i45 = z15 ? 1 : 0;
        int i46 = (i43 + i44) * 31;
        boolean z16 = this.switchingUser;
        if (!z16) {
            i = z16 ? 1 : 0;
        }
        return i46 + i;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("KeyguardFaceListenModel(timeMillis=");
        m.append(this.timeMillis);
        m.append(", userId=");
        m.append(this.userId);
        m.append(", listening=");
        m.append(this.listening);
        m.append(", authInterruptActive=");
        m.append(this.authInterruptActive);
        m.append(", becauseCannotSkipBouncer=");
        m.append(this.becauseCannotSkipBouncer);
        m.append(", biometricSettingEnabledForUser=");
        m.append(this.biometricSettingEnabledForUser);
        m.append(", bouncer=");
        m.append(this.bouncer);
        m.append(", faceAuthenticated=");
        m.append(this.faceAuthenticated);
        m.append(", faceDisabled=");
        m.append(this.faceDisabled);
        m.append(", keyguardAwake=");
        m.append(this.keyguardAwake);
        m.append(", keyguardGoingAway=");
        m.append(this.keyguardGoingAway);
        m.append(", listeningForFaceAssistant=");
        m.append(this.listeningForFaceAssistant);
        m.append(", lockIconPressed=");
        m.append(this.lockIconPressed);
        m.append(", occludingAppRequestingFaceAuth=");
        m.append(this.occludingAppRequestingFaceAuth);
        m.append(", primaryUser=");
        m.append(this.primaryUser);
        m.append(", scanningAllowedByStrongAuth=");
        m.append(this.scanningAllowedByStrongAuth);
        m.append(", secureCameraLaunched=");
        m.append(this.secureCameraLaunched);
        m.append(", switchingUser=");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.switchingUser, ')');
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
