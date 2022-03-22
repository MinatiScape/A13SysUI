package com.android.systemui.util.animation;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.util.MathUtils;
import com.android.systemui.animation.Interpolators;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TransitionLayoutController.kt */
/* loaded from: classes.dex */
public final class TransitionLayoutController {
    public TransitionViewState animationStartState;
    public ValueAnimator animator;
    public int currentHeight;
    public int currentWidth;
    public Function2<? super Integer, ? super Integer, Unit> sizeChangedListener;
    public TransitionLayout transitionLayout;
    public TransitionViewState currentState = new TransitionViewState();
    public TransitionViewState state = new TransitionViewState();

    public static TransitionViewState getGoneState(TransitionViewState transitionViewState, DisappearParameters disappearParameters, float f, TransitionViewState transitionViewState2) {
        int lerp;
        float constrain = MathUtils.constrain(MathUtils.map(disappearParameters.disappearStart, disappearParameters.disappearEnd, 0.0f, 1.0f, f), 0.0f, 1.0f);
        TransitionViewState copy = transitionViewState.copy(transitionViewState2);
        float f2 = transitionViewState.width;
        copy.width = (int) MathUtils.lerp(f2, disappearParameters.disappearSize.x * f2, constrain);
        float f3 = transitionViewState.height;
        copy.height = (int) MathUtils.lerp(f3, disappearParameters.disappearSize.y * f3, constrain);
        PointF pointF = copy.translation;
        PointF pointF2 = disappearParameters.gonePivot;
        float f4 = (transitionViewState.width - copy.width) * pointF2.x;
        pointF.x = f4;
        float f5 = (transitionViewState.height - lerp) * pointF2.y;
        pointF.y = f5;
        PointF pointF3 = copy.contentTranslation;
        PointF pointF4 = disappearParameters.contentTranslationFraction;
        pointF3.x = (pointF4.x - 1.0f) * f4;
        pointF3.y = (pointF4.y - 1.0f) * f5;
        copy.alpha = MathUtils.constrain(MathUtils.map(disappearParameters.fadeStartPosition, 1.0f, 1.0f, 0.0f, constrain), 0.0f, 1.0f);
        return copy;
    }

    public final void applyStateToLayout(TransitionViewState transitionViewState) {
        TransitionLayout transitionLayout = this.transitionLayout;
        if (transitionLayout != null) {
            transitionLayout.currentState = transitionViewState;
            transitionLayout.applyCurrentState();
        }
        int i = this.currentHeight;
        Objects.requireNonNull(transitionViewState);
        int i2 = transitionViewState.height;
        if (i != i2 || this.currentWidth != transitionViewState.width) {
            this.currentHeight = i2;
            int i3 = transitionViewState.width;
            this.currentWidth = i3;
            Function2<? super Integer, ? super Integer, Unit> function2 = this.sizeChangedListener;
            if (function2 != null) {
                function2.invoke(Integer.valueOf(i3), Integer.valueOf(this.currentHeight));
            }
        }
    }

    public final TransitionViewState getInterpolatedState(TransitionViewState transitionViewState, TransitionViewState transitionViewState2, float f, TransitionViewState transitionViewState3) {
        TransitionLayoutController transitionLayoutController;
        TransitionViewState transitionViewState4;
        int i;
        TransitionLayout transitionLayout;
        WidgetState widgetState;
        float f2;
        int i2;
        float f3;
        float f4;
        int i3;
        float f5;
        float f6;
        float f7;
        float f8;
        if (transitionViewState3 == null) {
            transitionViewState4 = new TransitionViewState();
            transitionLayoutController = this;
        } else {
            transitionLayoutController = this;
            transitionViewState4 = transitionViewState3;
        }
        TransitionLayout transitionLayout2 = transitionLayoutController.transitionLayout;
        if (transitionLayout2 == null) {
            return transitionViewState4;
        }
        int childCount = transitionLayout2.getChildCount();
        int i4 = 0;
        while (i4 < childCount) {
            int i5 = i4 + 1;
            int id = transitionLayout2.getChildAt(i4).getId();
            WidgetState widgetState2 = (WidgetState) transitionViewState4.widgetStates.get(Integer.valueOf(id));
            if (widgetState2 == null) {
                widgetState2 = new WidgetState(511);
            }
            WidgetState widgetState3 = (WidgetState) transitionViewState.widgetStates.get(Integer.valueOf(id));
            if (widgetState3 == null || (widgetState = (WidgetState) transitionViewState2.widgetStates.get(Integer.valueOf(id))) == null) {
                transitionLayout = transitionLayout2;
                i = childCount;
            } else {
                boolean z = widgetState3.gone;
                if (z != widgetState.gone) {
                    boolean z2 = true;
                    if (z) {
                        f4 = MathUtils.map(0.8f, 1.0f, 0.0f, 1.0f, f);
                        if (f >= 0.8f) {
                            z2 = false;
                        }
                        float f9 = widgetState.scale;
                        f3 = MathUtils.lerp(0.8f * f9, f9, f);
                        i2 = widgetState.measureWidth;
                        i3 = widgetState.measureHeight;
                        transitionLayout = transitionLayout2;
                        float lerp = MathUtils.lerp(widgetState3.x - (i2 / 2.0f), widgetState.x, f);
                        f8 = MathUtils.lerp(widgetState3.y - (i3 / 2.0f), widgetState.y, f);
                        f7 = lerp;
                        f2 = 1.0f;
                        i = childCount;
                    } else {
                        transitionLayout = transitionLayout2;
                        f4 = MathUtils.map(0.0f, 0.19999999f, 0.0f, 1.0f, f);
                        if (f <= 0.19999999f) {
                            z2 = false;
                        }
                        float f10 = widgetState3.scale;
                        f3 = MathUtils.lerp(f10, 0.8f * f10, f);
                        int i6 = widgetState3.measureWidth;
                        i3 = widgetState3.measureHeight;
                        i = childCount;
                        float lerp2 = MathUtils.lerp(widgetState3.x, widgetState.x - (i6 / 2.0f), f);
                        f8 = MathUtils.lerp(widgetState3.y, widgetState.y - (i3 / 2.0f), f);
                        f7 = lerp2;
                        i2 = i6;
                        f2 = 0.0f;
                    }
                    f5 = f8;
                    widgetState2.gone = z2;
                    f6 = f7;
                } else {
                    transitionLayout = transitionLayout2;
                    i = childCount;
                    widgetState2.gone = z;
                    i2 = widgetState.measureWidth;
                    i3 = widgetState.measureHeight;
                    f3 = MathUtils.lerp(widgetState3.scale, widgetState.scale, f);
                    f6 = MathUtils.lerp(widgetState3.x, widgetState.x, f);
                    f5 = MathUtils.lerp(widgetState3.y, widgetState.y, f);
                    f4 = f;
                    f2 = f4;
                }
                widgetState2.x = f6;
                widgetState2.y = f5;
                widgetState2.alpha = MathUtils.lerp(widgetState3.alpha, widgetState.alpha, f4);
                widgetState2.width = (int) MathUtils.lerp(widgetState3.width, widgetState.width, f2);
                widgetState2.height = (int) MathUtils.lerp(widgetState3.height, widgetState.height, f2);
                widgetState2.scale = f3;
                widgetState2.measureWidth = i2;
                widgetState2.measureHeight = i3;
                transitionViewState4.widgetStates.put(Integer.valueOf(id), widgetState2);
            }
            i4 = i5;
            transitionLayout2 = transitionLayout;
            childCount = i;
        }
        transitionViewState4.width = (int) MathUtils.lerp(transitionViewState.width, transitionViewState2.width, f);
        transitionViewState4.height = (int) MathUtils.lerp(transitionViewState.height, transitionViewState2.height, f);
        transitionViewState4.translation.x = MathUtils.lerp(transitionViewState.translation.x, transitionViewState2.translation.x, f);
        transitionViewState4.translation.y = MathUtils.lerp(transitionViewState.translation.y, transitionViewState2.translation.y, f);
        transitionViewState4.alpha = MathUtils.lerp(transitionViewState.alpha, transitionViewState2.alpha, f);
        transitionViewState4.contentTranslation.x = MathUtils.lerp(transitionViewState.contentTranslation.x, transitionViewState2.contentTranslation.x, f);
        transitionViewState4.contentTranslation.y = MathUtils.lerp(transitionViewState.contentTranslation.y, transitionViewState2.contentTranslation.y, f);
        return transitionViewState4;
    }

    public final void setMeasureState(TransitionViewState transitionViewState) {
        TransitionLayout transitionLayout = this.transitionLayout;
        if (transitionLayout != null) {
            int i = transitionViewState.width;
            int i2 = transitionViewState.height;
            if (i != transitionLayout.desiredMeasureWidth || i2 != transitionLayout.desiredMeasureHeight) {
                transitionLayout.desiredMeasureWidth = i;
                transitionLayout.desiredMeasureHeight = i2;
                if (transitionLayout.isInLayout()) {
                    transitionLayout.forceLayout();
                } else {
                    transitionLayout.requestLayout();
                }
            }
        }
    }

    public TransitionLayoutController() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.animator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.util.animation.TransitionLayoutController$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                TransitionLayoutController transitionLayoutController = TransitionLayoutController.this;
                Objects.requireNonNull(transitionLayoutController);
                if (transitionLayoutController.animationStartState != null && transitionLayoutController.animator.isRunning()) {
                    TransitionViewState transitionViewState = transitionLayoutController.animationStartState;
                    Intrinsics.checkNotNull(transitionViewState);
                    TransitionViewState interpolatedState = transitionLayoutController.getInterpolatedState(transitionViewState, transitionLayoutController.state, transitionLayoutController.animator.getAnimatedFraction(), transitionLayoutController.currentState);
                    transitionLayoutController.currentState = interpolatedState;
                    transitionLayoutController.applyStateToLayout(interpolatedState);
                }
            }
        });
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
    }
}
