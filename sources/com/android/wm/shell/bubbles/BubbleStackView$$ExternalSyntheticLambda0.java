package com.android.wm.shell.bubbles;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import com.android.wm.shell.TaskView;
import com.google.android.systemui.assist.uihints.ScrimController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        switch (this.$r8$classId) {
            case 0:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                Objects.requireNonNull(bubbleStackView);
                BubbleViewProvider bubbleViewProvider = bubbleStackView.mExpandedBubble;
                if (bubbleViewProvider != null && bubbleViewProvider.getExpandedView() != null) {
                    BubbleExpandedView expandedView = bubbleStackView.mExpandedBubble.getExpandedView();
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    Objects.requireNonNull(expandedView);
                    TaskView taskView = expandedView.mTaskView;
                    if (taskView != null) {
                        taskView.setAlpha(floatValue);
                    }
                    expandedView.mPointerView.setAlpha(floatValue);
                    expandedView.setAlpha(floatValue);
                    return;
                }
                return;
            default:
                ScrimController scrimController = (ScrimController) this.f$0;
                LinearInterpolator linearInterpolator = ScrimController.ALPHA_INTERPOLATOR;
                Objects.requireNonNull(scrimController);
                float floatValue2 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                scrimController.mScrimView.setAlpha(floatValue2);
                scrimController.mOverlappedElement.setAlpha(1.0f - floatValue2);
                return;
        }
    }
}
