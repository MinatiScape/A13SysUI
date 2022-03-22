package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
/* loaded from: classes.dex */
public interface FalsingCollector {
    void avoidGesture();

    void isReportingEnabled();

    void onAffordanceSwipingAborted();

    void onAffordanceSwipingStarted();

    void onBouncerHidden();

    void onBouncerShown();

    void onCameraHintStarted();

    void onCameraOn();

    void onExpansionFromPulseStopped();

    void onLeftAffordanceHintStarted();

    void onLeftAffordanceOn();

    void onMotionEventComplete();

    void onNotificationDismissed();

    void onNotificationStartDismissing();

    void onNotificationStartDraggingDown();

    void onNotificationStopDismissing();

    void onNotificationStopDraggingDown();

    void onQsDown();

    void onScreenOff();

    void onScreenOnFromTouch();

    void onScreenTurningOn();

    void onStartExpandingFromPulse();

    void onSuccessfulUnlock();

    void onTouchEvent(MotionEvent motionEvent);

    void onTrackingStarted();

    void onTrackingStopped();

    void onUnlockHintStarted();

    void setNotificationExpanded();

    void setQsExpanded(boolean z);

    void setShowingAod(boolean z);

    void shouldEnforceBouncer();

    void updateFalseConfidence(FalsingClassifier.Result result);
}
