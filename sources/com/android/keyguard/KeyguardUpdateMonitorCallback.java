package com.android.keyguard;

import android.hardware.biometrics.BiometricSourceType;
import com.android.settingslib.fuelgauge.BatteryStatus;
import java.util.TimeZone;
/* loaded from: classes.dex */
public class KeyguardUpdateMonitorCallback {
    public boolean mShowing;
    public long mVisibilityChangedCalled;

    public void onBiometricAcquired(BiometricSourceType biometricSourceType) {
    }

    public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
    }

    public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
    }

    public void onBiometricError(int i, String str, BiometricSourceType biometricSourceType) {
    }

    public void onBiometricHelp(int i, String str, BiometricSourceType biometricSourceType) {
    }

    public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
    }

    public void onBiometricsCleared() {
    }

    public void onClockVisibilityChanged() {
    }

    public void onDeviceProvisioned() {
    }

    public void onDreamingStateChanged(boolean z) {
    }

    public void onEmergencyCallAction() {
    }

    public void onFaceUnlockStateChanged() {
    }

    @Deprecated
    public void onFinishedGoingToSleep(int i) {
    }

    public void onKeyguardBouncerChanged(boolean z) {
    }

    public void onKeyguardOccludedChanged(boolean z) {
    }

    public void onKeyguardVisibilityChanged(boolean z) {
    }

    public void onLockedOutStateChanged(BiometricSourceType biometricSourceType) {
    }

    public void onLogoutEnabledChanged() {
    }

    public void onPhoneStateChanged() {
    }

    public void onRefreshBatteryInfo(BatteryStatus batteryStatus) {
    }

    public void onRefreshCarrierInfo() {
    }

    public void onRequireUnlockForNfc() {
    }

    public void onSecondaryLockscreenRequirementChanged(int i) {
    }

    public void onShadeExpandedChanged(boolean z) {
    }

    public void onSimStateChanged(int i, int i2, int i3) {
    }

    @Deprecated
    public void onStartedGoingToSleep$1() {
    }

    @Deprecated
    public void onStartedWakingUp() {
    }

    public void onStrongAuthStateChanged(int i) {
    }

    public void onTelephonyCapable(boolean z) {
    }

    public void onTimeChanged() {
    }

    public void onTimeFormatChanged(String str) {
    }

    public void onTimeZoneChanged(TimeZone timeZone) {
    }

    public void onTrustAgentErrorMessage(CharSequence charSequence) {
    }

    public void onTrustChanged(int i) {
    }

    public void onTrustGrantedWithFlags(int i, int i2) {
    }

    public void onTrustManagedChanged() {
    }

    public void onUserInfoChanged() {
    }

    public void onUserSwitchComplete(int i) {
    }

    public void onUserSwitching(int i) {
    }

    public void onUserUnlocked() {
    }

    public void showTrustGrantedMessage(CharSequence charSequence) {
    }
}
