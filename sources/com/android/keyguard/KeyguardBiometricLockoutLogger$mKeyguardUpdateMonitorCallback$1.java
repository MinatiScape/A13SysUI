package com.android.keyguard;

import android.hardware.biometrics.BiometricSourceType;
import com.android.keyguard.KeyguardBiometricLockoutLogger;
import java.util.Objects;
/* compiled from: KeyguardBiometricLockoutLogger.kt */
/* loaded from: classes.dex */
public final class KeyguardBiometricLockoutLogger$mKeyguardUpdateMonitorCallback$1 extends KeyguardUpdateMonitorCallback {
    public final /* synthetic */ KeyguardBiometricLockoutLogger this$0;

    public KeyguardBiometricLockoutLogger$mKeyguardUpdateMonitorCallback$1(KeyguardBiometricLockoutLogger keyguardBiometricLockoutLogger) {
        this.this$0 = keyguardBiometricLockoutLogger;
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public final void onLockedOutStateChanged(BiometricSourceType biometricSourceType) {
        boolean z;
        if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = this.this$0.keyguardUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            if (keyguardUpdateMonitor.mFingerprintLockedOut || keyguardUpdateMonitor.mFingerprintLockedOutPermanent) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                KeyguardBiometricLockoutLogger keyguardBiometricLockoutLogger = this.this$0;
                if (!keyguardBiometricLockoutLogger.fingerprintLockedOut) {
                    KeyguardBiometricLockoutLogger.access$log(keyguardBiometricLockoutLogger, KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_FINGERPRINT_LOCKED_OUT);
                    this.this$0.fingerprintLockedOut = z;
                }
            }
            if (!z) {
                KeyguardBiometricLockoutLogger keyguardBiometricLockoutLogger2 = this.this$0;
                if (keyguardBiometricLockoutLogger2.fingerprintLockedOut) {
                    KeyguardBiometricLockoutLogger.access$log(keyguardBiometricLockoutLogger2, KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_FINGERPRINT_LOCKED_OUT_RESET);
                }
            }
            this.this$0.fingerprintLockedOut = z;
        } else if (biometricSourceType == BiometricSourceType.FACE) {
            KeyguardUpdateMonitor keyguardUpdateMonitor2 = this.this$0.keyguardUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor2);
            boolean z2 = keyguardUpdateMonitor2.mFaceLockedOutPermanent;
            if (z2) {
                KeyguardBiometricLockoutLogger keyguardBiometricLockoutLogger3 = this.this$0;
                if (!keyguardBiometricLockoutLogger3.faceLockedOut) {
                    KeyguardBiometricLockoutLogger.access$log(keyguardBiometricLockoutLogger3, KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_FACE_LOCKED_OUT);
                    this.this$0.faceLockedOut = z2;
                }
            }
            if (!z2) {
                KeyguardBiometricLockoutLogger keyguardBiometricLockoutLogger4 = this.this$0;
                if (keyguardBiometricLockoutLogger4.faceLockedOut) {
                    KeyguardBiometricLockoutLogger.access$log(keyguardBiometricLockoutLogger4, KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_FACE_LOCKED_OUT_RESET);
                }
            }
            this.this$0.faceLockedOut = z2;
        }
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public final void onStrongAuthStateChanged(int i) {
        boolean z;
        boolean z2;
        boolean z3;
        if (i == KeyguardUpdateMonitor.getCurrentUser()) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = this.this$0.keyguardUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            int strongAuthForUser = keyguardUpdateMonitor.mStrongAuthTracker.getStrongAuthForUser(i);
            boolean isEncryptedOrLockdown = this.this$0.keyguardUpdateMonitor.isEncryptedOrLockdown(i);
            if (isEncryptedOrLockdown) {
                KeyguardBiometricLockoutLogger keyguardBiometricLockoutLogger = this.this$0;
                if (!keyguardBiometricLockoutLogger.encryptedOrLockdown) {
                    KeyguardBiometricLockoutLogger.access$log(keyguardBiometricLockoutLogger, KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_ENCRYPTED_OR_LOCKDOWN);
                }
            }
            KeyguardBiometricLockoutLogger keyguardBiometricLockoutLogger2 = this.this$0;
            keyguardBiometricLockoutLogger2.encryptedOrLockdown = isEncryptedOrLockdown;
            Objects.requireNonNull(keyguardBiometricLockoutLogger2);
            boolean z4 = true;
            if ((strongAuthForUser & 64) != 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                KeyguardBiometricLockoutLogger keyguardBiometricLockoutLogger3 = this.this$0;
                if (!keyguardBiometricLockoutLogger3.unattendedUpdate) {
                    KeyguardBiometricLockoutLogger.access$log(keyguardBiometricLockoutLogger3, KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_UNATTENDED_UPDATE);
                }
            }
            KeyguardBiometricLockoutLogger keyguardBiometricLockoutLogger4 = this.this$0;
            keyguardBiometricLockoutLogger4.unattendedUpdate = z;
            Objects.requireNonNull(keyguardBiometricLockoutLogger4);
            if ((strongAuthForUser & 16) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!z2) {
                if ((strongAuthForUser & 128) != 0) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (!z3) {
                    z4 = false;
                }
            }
            if (z4) {
                KeyguardBiometricLockoutLogger keyguardBiometricLockoutLogger5 = this.this$0;
                if (!keyguardBiometricLockoutLogger5.timeout) {
                    KeyguardBiometricLockoutLogger.access$log(keyguardBiometricLockoutLogger5, KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_TIMEOUT);
                }
            }
            this.this$0.timeout = z4;
        }
    }
}
