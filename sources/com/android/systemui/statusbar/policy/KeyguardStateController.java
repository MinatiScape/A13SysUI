package com.android.systemui.statusbar.policy;
/* loaded from: classes.dex */
public interface KeyguardStateController extends CallbackController<Callback> {

    /* loaded from: classes.dex */
    public interface Callback {
        default void onKeyguardDismissAmountChanged() {
        }

        default void onKeyguardFadingAwayChanged() {
        }

        default void onKeyguardShowingChanged() {
        }

        default void onLaunchTransitionFadingAwayChanged() {
        }

        default void onUnlockedChanged() {
        }
    }

    long calculateGoingToFullShadeDelay();

    boolean canDismissLockScreen();

    float getDismissAmount();

    long getKeyguardFadingAwayDelay();

    long getKeyguardFadingAwayDuration();

    default boolean isBypassFadingAnimation() {
        return false;
    }

    boolean isDismissingFromSwipe();

    default boolean isFaceAuthEnabled() {
        return false;
    }

    boolean isFlingingToDismissKeyguard();

    boolean isFlingingToDismissKeyguardDuringSwipeGesture();

    boolean isKeyguardFadingAway();

    boolean isKeyguardGoingAway();

    boolean isKeyguardScreenRotationAllowed();

    boolean isLaunchTransitionFadingAway();

    boolean isMethodSecure();

    boolean isOccluded();

    boolean isShowing();

    boolean isSnappingKeyguardBackAfterSwipe();

    default void notifyKeyguardDismissAmountChanged(float f, boolean z) {
    }

    default void notifyKeyguardDoneFading() {
    }

    default void notifyKeyguardFadingAway(long j, long j2, boolean z) {
    }

    default void notifyKeyguardGoingAway() {
    }

    default void notifyKeyguardState(boolean z, boolean z2) {
    }

    void notifyPanelFlingEnd();

    void notifyPanelFlingStart(boolean z);

    default void setLaunchTransitionFadingAway(boolean z) {
    }

    default long getShortenedFadingAwayDuration() {
        if (isBypassFadingAnimation()) {
            return getKeyguardFadingAwayDuration();
        }
        return getKeyguardFadingAwayDuration() / 2;
    }

    default boolean isUnlocked() {
        if (!isShowing() || canDismissLockScreen()) {
            return true;
        }
        return false;
    }
}
