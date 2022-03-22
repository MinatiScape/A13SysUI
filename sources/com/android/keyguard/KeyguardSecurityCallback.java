package com.android.keyguard;
/* loaded from: classes.dex */
public interface KeyguardSecurityCallback {
    void dismiss(int i);

    void dismiss(int i, boolean z);

    default void onCancelClicked() {
    }

    void onUserInput();

    void reportUnlockAttempt(int i, boolean z, int i2);

    void reset();

    void userActivity();
}
