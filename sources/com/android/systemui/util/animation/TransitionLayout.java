package com.android.systemui.util.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.leanback.R$raw;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: TransitionLayout.kt */
/* loaded from: classes.dex */
public final class TransitionLayout extends ConstraintLayout {
    public final Rect boundsRect;
    public TransitionViewState currentState;
    public int desiredMeasureHeight;
    public int desiredMeasureWidth;
    public boolean isPreDrawApplicatorRegistered;
    public boolean measureAsConstraint;
    public final LinkedHashSet originalGoneChildrenSet;
    public final LinkedHashMap originalViewAlphas;
    public final TransitionLayout$preDrawApplicator$1 preDrawApplicator;
    public int transitionVisibility;
    public boolean updateScheduled;

    public TransitionLayout(Context context) {
        this(context, null, 0, 6, null);
    }

    public TransitionLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
    }

    public /* synthetic */ TransitionLayout(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    /* JADX WARN: Type inference failed for: r1v8, types: [com.android.systemui.util.animation.TransitionLayout$preDrawApplicator$1] */
    public TransitionLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.boundsRect = new Rect();
        this.originalGoneChildrenSet = new LinkedHashSet();
        this.originalViewAlphas = new LinkedHashMap();
        this.currentState = new TransitionViewState();
        new LinkedHashMap();
        new PointF();
        new PointF();
        this.preDrawApplicator = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.util.animation.TransitionLayout$preDrawApplicator$1
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public final boolean onPreDraw() {
                TransitionLayout transitionLayout = TransitionLayout.this;
                transitionLayout.updateScheduled = false;
                transitionLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                TransitionLayout transitionLayout2 = TransitionLayout.this;
                transitionLayout2.isPreDrawApplicatorRegistered = false;
                transitionLayout2.applyCurrentState();
                return true;
            }
        };
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public final void dispatchDraw(Canvas canvas) {
        if (canvas != null) {
            canvas.save();
        }
        if (canvas != null) {
            canvas.clipRect(this.boundsRect);
        }
        super.dispatchDraw(canvas);
        if (canvas != null) {
            canvas.restore();
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.measureAsConstraint) {
            super.onLayout(z, getLeft(), getTop(), getRight(), getBottom());
            return;
        }
        int childCount = getChildCount();
        int i5 = 0;
        while (i5 < childCount) {
            int i6 = i5 + 1;
            View childAt = getChildAt(i5);
            childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
            i5 = i6;
        }
        applyCurrentState();
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        if (this.measureAsConstraint) {
            super.onMeasure(i, i2);
            return;
        }
        int i3 = 0;
        int childCount = getChildCount();
        while (i3 < childCount) {
            int i4 = i3 + 1;
            View childAt = getChildAt(i3);
            TransitionViewState transitionViewState = this.currentState;
            Objects.requireNonNull(transitionViewState);
            WidgetState widgetState = (WidgetState) transitionViewState.widgetStates.get(Integer.valueOf(childAt.getId()));
            if (widgetState != null) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(widgetState.measureWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(widgetState.measureHeight, 1073741824));
            }
            i3 = i4;
        }
        setMeasuredDimension(this.desiredMeasureWidth, this.desiredMeasureHeight);
    }

    public final void applyCurrentState() {
        Integer num;
        int i;
        boolean z;
        int i2;
        int i3;
        int i4;
        int i5;
        int childCount = getChildCount();
        TransitionViewState transitionViewState = this.currentState;
        Objects.requireNonNull(transitionViewState);
        int i6 = (int) transitionViewState.contentTranslation.x;
        TransitionViewState transitionViewState2 = this.currentState;
        Objects.requireNonNull(transitionViewState2);
        int i7 = (int) transitionViewState2.contentTranslation.y;
        int i8 = 0;
        while (i8 < childCount) {
            int i9 = i8 + 1;
            View childAt = getChildAt(i8);
            TransitionViewState transitionViewState3 = this.currentState;
            Objects.requireNonNull(transitionViewState3);
            WidgetState widgetState = (WidgetState) transitionViewState3.widgetStates.get(Integer.valueOf(childAt.getId()));
            if (widgetState != null) {
                if (!(childAt instanceof TextView) || widgetState.width >= widgetState.measureWidth) {
                    num = null;
                } else {
                    if (((TextView) childAt).getLayout().getParagraphDirection(0) == -1) {
                        i5 = widgetState.measureWidth - widgetState.width;
                    } else {
                        i5 = 0;
                    }
                    num = Integer.valueOf(i5);
                }
                if (!(childAt.getMeasuredWidth() == widgetState.measureWidth && childAt.getMeasuredHeight() == widgetState.measureHeight)) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(widgetState.measureWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(widgetState.measureHeight, 1073741824));
                    childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
                }
                if (num == null) {
                    i = 0;
                } else {
                    i = num.intValue();
                }
                int i10 = (((int) widgetState.x) + i6) - i;
                int i11 = ((int) widgetState.y) + i7;
                boolean z2 = true;
                if (num != null) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    i2 = widgetState.measureWidth;
                } else {
                    i2 = widgetState.width;
                }
                if (z) {
                    i3 = widgetState.measureHeight;
                } else {
                    i3 = widgetState.height;
                }
                childAt.setLeftTopRightBottom(i10, i11, i2 + i10, i3 + i11);
                childAt.setScaleX(widgetState.scale);
                childAt.setScaleY(widgetState.scale);
                Rect clipBounds = childAt.getClipBounds();
                if (clipBounds == null) {
                    clipBounds = new Rect();
                }
                clipBounds.set(i, 0, widgetState.width + i, widgetState.height);
                childAt.setClipBounds(clipBounds);
                R$raw.fadeIn(childAt, widgetState.alpha, false);
                if (!widgetState.gone) {
                    if (widgetState.alpha != 0.0f) {
                        z2 = false;
                    }
                    if (!z2) {
                        i4 = 0;
                        childAt.setVisibility(i4);
                    }
                }
                i4 = 4;
                childAt.setVisibility(i4);
            }
            i8 = i9;
        }
        int left = getLeft();
        int top = getTop();
        TransitionViewState transitionViewState4 = this.currentState;
        Objects.requireNonNull(transitionViewState4);
        TransitionViewState transitionViewState5 = this.currentState;
        Objects.requireNonNull(transitionViewState5);
        setLeftTopRightBottom(left, top, transitionViewState4.width + left, transitionViewState5.height + top);
        this.boundsRect.set(0, 0, getWidth(), getHeight());
        TransitionViewState transitionViewState6 = this.currentState;
        Objects.requireNonNull(transitionViewState6);
        setTranslationX(transitionViewState6.translation.x);
        TransitionViewState transitionViewState7 = this.currentState;
        Objects.requireNonNull(transitionViewState7);
        setTranslationY(transitionViewState7.translation.y);
        TransitionViewState transitionViewState8 = this.currentState;
        Objects.requireNonNull(transitionViewState8);
        R$raw.fadeIn((View) this, transitionViewState8.alpha, false);
        int i12 = this.transitionVisibility;
        if (i12 != 0) {
            setTransitionVisibility(i12);
        }
    }

    public final TransitionViewState calculateViewState(MeasurementInput measurementInput, ConstraintSet constraintSet, TransitionViewState transitionViewState) {
        boolean z;
        int i;
        boolean z2;
        boolean z3;
        ConstraintWidget constraintWidget;
        ConstraintWidget constraintWidget2;
        int childCount = getChildCount();
        int i2 = 0;
        while (true) {
            float f = 1.0f;
            if (i2 >= childCount) {
                break;
            }
            int i3 = i2 + 1;
            View childAt = getChildAt(i2);
            if (this.originalGoneChildrenSet.contains(Integer.valueOf(childAt.getId()))) {
                childAt.setVisibility(8);
            }
            Float f2 = (Float) this.originalViewAlphas.get(Integer.valueOf(childAt.getId()));
            if (f2 != null) {
                f = f2.floatValue();
            }
            childAt.setAlpha(f);
            i2 = i3;
        }
        constraintSet.applyTo(this);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        this.measureAsConstraint = true;
        measure(measurementInput.widthMeasureSpec, measurementInput.heightMeasureSpec);
        int left = getLeft();
        int top = getTop();
        layout(left, top, getMeasuredWidth() + left, getMeasuredHeight() + top);
        this.measureAsConstraint = false;
        int childCount2 = getChildCount();
        int i4 = 0;
        while (i4 < childCount2) {
            int i5 = i4 + 1;
            View childAt2 = getChildAt(i4);
            LinkedHashMap linkedHashMap = transitionViewState.widgetStates;
            Integer valueOf = Integer.valueOf(childAt2.getId());
            Object obj = linkedHashMap.get(valueOf);
            if (obj == null) {
                obj = new WidgetState(384);
                linkedHashMap.put(valueOf, obj);
            }
            WidgetState widgetState = (WidgetState) obj;
            if (childAt2.getVisibility() == 8) {
                z2 = true;
            } else {
                z2 = false;
            }
            widgetState.gone = z2;
            if (z2) {
                ViewGroup.LayoutParams layoutParams = childAt2.getLayoutParams();
                Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
                ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) layoutParams;
                Objects.requireNonNull(layoutParams2.widget);
                widgetState.x = constraintWidget.getX();
                Objects.requireNonNull(layoutParams2.widget);
                widgetState.y = constraintWidget2.getY();
                widgetState.width = layoutParams2.widget.getWidth();
                int height = layoutParams2.widget.getHeight();
                widgetState.height = height;
                widgetState.measureHeight = height;
                widgetState.measureWidth = widgetState.width;
                widgetState.alpha = 0.0f;
                widgetState.scale = 0.0f;
            } else {
                widgetState.x = childAt2.getLeft();
                widgetState.y = childAt2.getTop();
                widgetState.width = childAt2.getWidth();
                int height2 = childAt2.getHeight();
                widgetState.height = height2;
                widgetState.measureWidth = widgetState.width;
                widgetState.measureHeight = height2;
                if (childAt2.getVisibility() == 8) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                widgetState.gone = z3;
                widgetState.alpha = childAt2.getAlpha();
                widgetState.scale = 1.0f;
            }
            i4 = i5;
        }
        transitionViewState.width = getMeasuredWidth();
        transitionViewState.height = getMeasuredHeight();
        transitionViewState.translation.set(0.0f, 0.0f);
        transitionViewState.contentTranslation.set(0.0f, 0.0f);
        transitionViewState.alpha = 1.0f;
        int childCount3 = getChildCount();
        int i6 = 0;
        while (i6 < childCount3) {
            int i7 = i6 + 1;
            View childAt3 = getChildAt(i6);
            TransitionViewState transitionViewState2 = this.currentState;
            Objects.requireNonNull(transitionViewState2);
            WidgetState widgetState2 = (WidgetState) transitionViewState2.widgetStates.get(Integer.valueOf(childAt3.getId()));
            if (widgetState2 != null && !widgetState2.gone) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                i = 4;
            } else {
                i = 0;
            }
            childAt3.setVisibility(i);
            i6 = i7;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
        if (!this.updateScheduled) {
            this.updateScheduled = true;
            if (!this.isPreDrawApplicatorRegistered) {
                getViewTreeObserver().addOnPreDrawListener(this.preDrawApplicator);
                this.isPreDrawApplicatorRegistered = true;
            }
        }
        return transitionViewState;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.isPreDrawApplicatorRegistered) {
            getViewTreeObserver().removeOnPreDrawListener(this.preDrawApplicator);
            this.isPreDrawApplicatorRegistered = false;
        }
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        int i = 0;
        while (i < childCount) {
            int i2 = i + 1;
            View childAt = getChildAt(i);
            if (childAt.getId() == -1) {
                childAt.setId(i);
            }
            if (childAt.getVisibility() == 8) {
                this.originalGoneChildrenSet.add(Integer.valueOf(childAt.getId()));
            }
            this.originalViewAlphas.put(Integer.valueOf(childAt.getId()), Float.valueOf(childAt.getAlpha()));
            i = i2;
        }
    }

    @Override // android.view.View
    public final void setTransitionVisibility(int i) {
        super.setTransitionVisibility(i);
        this.transitionVisibility = i;
    }
}
