package com.android.systemui.statusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: classes.dex */
public final class KeyboardShortcutKeysLayout extends ViewGroup {
    public final Context mContext;
    public int mLineHeight;

    public KeyboardShortcutKeysLayout(Context context) {
        super(context);
        this.mContext = context;
    }

    public final boolean isRTL() {
        if (this.mContext.getResources().getConfiguration().getLayoutDirection() == 1) {
            return true;
        }
        return false;
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public final int mHorizontalSpacing;
        public final int mVerticalSpacing;

        public LayoutParams(int i, int i2, ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.mHorizontalSpacing = i;
            this.mVerticalSpacing = i2;
        }

        public LayoutParams(int i, int i2) {
            super(0, 0);
            this.mHorizontalSpacing = i;
            this.mVerticalSpacing = i2;
        }
    }

    public KeyboardShortcutKeysLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateDefaultLayoutParams() {
        int applyDimension = (int) TypedValue.applyDimension(1, 4.0f, getResources().getDisplayMetrics());
        return new LayoutParams(applyDimension, applyDimension);
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        int applyDimension = (int) TypedValue.applyDimension(1, 4.0f, getResources().getDisplayMetrics());
        return new LayoutParams(applyDimension, applyDimension, layoutParams);
    }

    public final void layoutChildrenOnRow(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7;
        if (!isRTL()) {
            i4 = ((getPaddingLeft() + i3) - i4) + i6;
        }
        for (int i8 = i; i8 < i2; i8++) {
            View childAt = getChildAt(i8);
            int measuredWidth = childAt.getMeasuredWidth();
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (isRTL() && i8 == i) {
                i4 = (((i3 - i4) - getPaddingRight()) - measuredWidth) - layoutParams.mHorizontalSpacing;
            }
            childAt.layout(i4, i5, i4 + measuredWidth, childAt.getMeasuredHeight() + i5);
            if (isRTL()) {
                if (i8 < i2 - 1) {
                    i7 = getChildAt(i8 + 1).getMeasuredWidth();
                } else {
                    i7 = 0;
                }
                i4 -= i7 + layoutParams.mHorizontalSpacing;
            } else {
                i4 = measuredWidth + layoutParams.mHorizontalSpacing + i4;
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int childCount = getChildCount();
        int i7 = i3 - i;
        if (isRTL()) {
            i5 = i7 - getPaddingRight();
        } else {
            i5 = getPaddingLeft();
        }
        int paddingTop = getPaddingTop();
        int i8 = i5;
        int i9 = 0;
        int i10 = 0;
        for (int i11 = 0; i11 < childCount; i11++) {
            View childAt = getChildAt(i11);
            if (childAt.getVisibility() != 8) {
                int measuredWidth = childAt.getMeasuredWidth();
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                boolean z2 = true;
                if (!isRTL() ? i8 + measuredWidth <= i7 : (i8 - getPaddingLeft()) - measuredWidth >= 0) {
                    z2 = false;
                }
                if (z2) {
                    layoutChildrenOnRow(i9, i11, i7, i8, paddingTop, i10);
                    if (isRTL()) {
                        i6 = i7 - getPaddingRight();
                    } else {
                        i6 = getPaddingLeft();
                    }
                    i8 = i6;
                    paddingTop += this.mLineHeight;
                    i9 = i11;
                }
                if (isRTL()) {
                    i8 = (i8 - measuredWidth) - layoutParams.mHorizontalSpacing;
                } else {
                    i8 = i8 + measuredWidth + layoutParams.mHorizontalSpacing;
                }
                i10 = layoutParams.mHorizontalSpacing;
            }
        }
        if (i9 < childCount) {
            layoutChildrenOnRow(i9, childCount, i7, i8, paddingTop, i10);
        }
    }

    @Override // android.view.View
    public final void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int size = (View.MeasureSpec.getSize(i) - getPaddingLeft()) - getPaddingRight();
        int childCount = getChildCount();
        int size2 = (View.MeasureSpec.getSize(i2) - getPaddingTop()) - getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        if (View.MeasureSpec.getMode(i2) == Integer.MIN_VALUE) {
            i3 = View.MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE);
        } else {
            i3 = View.MeasureSpec.makeMeasureSpec(0, 0);
        }
        int i5 = 0;
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                childAt.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), i3);
                int measuredWidth = childAt.getMeasuredWidth();
                i5 = Math.max(i5, childAt.getMeasuredHeight() + layoutParams.mVerticalSpacing);
                if (paddingLeft + measuredWidth > size) {
                    paddingLeft = getPaddingLeft();
                    paddingTop += i5;
                }
                paddingLeft = measuredWidth + layoutParams.mHorizontalSpacing + paddingLeft;
            }
        }
        this.mLineHeight = i5;
        if (View.MeasureSpec.getMode(i2) == 0) {
            size2 = paddingTop + i5;
        } else if (View.MeasureSpec.getMode(i2) == Integer.MIN_VALUE && (i4 = paddingTop + i5) < size2) {
            size2 = i4;
        }
        setMeasuredDimension(size, size2);
    }

    @Override // android.view.ViewGroup
    public final boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }
}
