package com.android.keyguard;
/* loaded from: classes.dex */
public interface ViewMediatorCallback {
    CharSequence consumeCustomMessage();

    int getBouncerPromptReason();

    boolean isScreenOn();

    void keyguardDone(int i);

    void keyguardDoneDrawing();

    void keyguardDonePending(int i);

    void keyguardGone();

    void onBouncerVisiblityChanged(boolean z);

    void onCancelClicked();

    void playTrustedSound();

    void readyForKeyguardDone();

    void resetKeyguard();

    void setNeedsInput(boolean z);

    void userActivity();
}
