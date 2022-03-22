package com.google.android.systemui.elmyra.feedback;

import androidx.leanback.R$color;
import com.google.android.systemui.assist.AssistManagerGoogle;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AssistInvocationEffect implements FeedbackEffect {
    public final AssistManagerGoogle mAssistManager;
    public final OpaHomeButton mOpaHomeButton;
    public final OpaLockscreen mOpaLockscreen;

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onProgress(float f, int i) {
        AssistManagerGoogle assistManagerGoogle = this.mAssistManager;
        Objects.requireNonNull(assistManagerGoogle);
        if (!R$color.isGesturalMode(assistManagerGoogle.mNavigationMode)) {
            this.mOpaHomeButton.onProgress(f, i);
            this.mOpaLockscreen.onProgress(f, i);
            return;
        }
        this.mAssistManager.onInvocationProgress(2, f);
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onRelease() {
        AssistManagerGoogle assistManagerGoogle = this.mAssistManager;
        Objects.requireNonNull(assistManagerGoogle);
        if (!R$color.isGesturalMode(assistManagerGoogle.mNavigationMode)) {
            this.mOpaHomeButton.onRelease();
            this.mOpaLockscreen.onRelease();
            return;
        }
        this.mAssistManager.onInvocationProgress(2, 0.0f);
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        AssistManagerGoogle assistManagerGoogle = this.mAssistManager;
        Objects.requireNonNull(assistManagerGoogle);
        if (!R$color.isGesturalMode(assistManagerGoogle.mNavigationMode)) {
            this.mOpaHomeButton.onResolve(detectionProperties);
            this.mOpaLockscreen.onResolve(detectionProperties);
            return;
        }
        this.mAssistManager.onInvocationProgress(2, 1.0f);
    }

    public AssistInvocationEffect(AssistManagerGoogle assistManagerGoogle, OpaHomeButton opaHomeButton, OpaLockscreen opaLockscreen) {
        this.mAssistManager = assistManagerGoogle;
        this.mOpaHomeButton = opaHomeButton;
        this.mOpaLockscreen = opaLockscreen;
    }
}
