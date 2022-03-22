package com.android.systemui.util.animation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.Objects;
/* compiled from: UniqueObjectHostView.kt */
/* loaded from: classes.dex */
public final class UniqueObjectHostView extends FrameLayout {
    public MeasurementManager measurementManager;

    /* compiled from: UniqueObjectHostView.kt */
    /* loaded from: classes.dex */
    public interface MeasurementManager {
        MeasurementOutput onMeasure(MeasurementInput measurementInput);
    }

    @Override // android.view.ViewGroup
    public final void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        boolean z;
        if (view != null) {
            if (!(view.getMeasuredWidth() == 0 || getMeasuredWidth() == 0)) {
                Object tag = view.getTag(2131428693);
                if (tag == null) {
                    z = false;
                } else {
                    z = tag.equals(Boolean.TRUE);
                }
                if (!z) {
                    invalidate();
                    addViewInLayout(view, i, layoutParams, true);
                    view.resolveRtlPropertiesIfNeeded();
                    int paddingLeft = getPaddingLeft();
                    int paddingTop = getPaddingTop();
                    view.layout(paddingLeft, paddingTop, (getMeasuredWidth() + paddingLeft) - (getPaddingEnd() + getPaddingStart()), (getMeasuredHeight() + paddingTop) - (getPaddingBottom() + getPaddingTop()));
                    return;
                }
            }
            super.addView(view, i, layoutParams);
            return;
        }
        throw new IllegalArgumentException("child must be non-null");
    }

    @Override // android.widget.FrameLayout, android.view.View
    @SuppressLint({"DrawAllocation"})
    public final void onMeasure(int i, int i2) {
        boolean z;
        int paddingEnd = getPaddingEnd() + getPaddingStart();
        int paddingBottom = getPaddingBottom() + getPaddingTop();
        MeasurementInput measurementInput = new MeasurementInput(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i) - paddingEnd, View.MeasureSpec.getMode(i)), View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2) - paddingBottom, View.MeasureSpec.getMode(i2)));
        MeasurementManager measurementManager = this.measurementManager;
        if (measurementManager == null) {
            measurementManager = null;
        }
        MeasurementOutput onMeasure = measurementManager.onMeasure(measurementInput);
        Objects.requireNonNull(onMeasure);
        int i3 = onMeasure.measuredWidth;
        int i4 = onMeasure.measuredHeight;
        if (getChildCount() != 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            super.onMeasure(i, i2);
            View childAt = getChildAt(0);
            if (childAt != null) {
                childAt.setTag(2131428693, Boolean.FALSE);
            }
        }
        setMeasuredDimension(i3 + paddingEnd, i4 + paddingBottom);
    }

    public UniqueObjectHostView(Context context) {
        super(context);
    }
}
