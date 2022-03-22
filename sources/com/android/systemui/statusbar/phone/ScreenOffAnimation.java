package com.android.systemui.statusbar.phone;

import android.view.View;
import com.android.keyguard.KeyguardVisibilityHelper$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.LightRevealScrim;
/* compiled from: ScreenOffAnimationController.kt */
/* loaded from: classes.dex */
public interface ScreenOffAnimation {
    void animateInKeyguard(View view, KeyguardVisibilityHelper$$ExternalSyntheticLambda0 keyguardVisibilityHelper$$ExternalSyntheticLambda0);

    void initialize(StatusBar statusBar, LightRevealScrim lightRevealScrim);

    boolean isAnimationPlaying();

    boolean isKeyguardHideDelayed();

    boolean isKeyguardShowDelayed();

    void onAlwaysOnChanged(boolean z);

    void onScrimOpaqueChanged(boolean z);

    boolean overrideNotificationsDozeAmount();

    boolean shouldAnimateAodIcons();

    boolean shouldAnimateClockChange();

    boolean shouldAnimateDozingChange();

    boolean shouldAnimateInKeyguard();

    boolean shouldDelayDisplayDozeTransition();

    boolean shouldDelayKeyguardShow();

    boolean shouldHideScrimOnWakeUp();

    boolean shouldPlayAnimation();

    boolean shouldShowAodIconsWhenShade();

    boolean startAnimation();
}
