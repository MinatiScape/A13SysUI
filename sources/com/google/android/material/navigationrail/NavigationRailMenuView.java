package com.google.android.material.navigationrail;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarMenuView;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NavigationRailMenuView extends NavigationBarMenuView {
    public int itemMinimumHeight = -1;
    public final FrameLayout.LayoutParams layoutParams;

    public final int makeSharedHeightSpec(int i, int i2, int i3) {
        int max = i2 / Math.max(1, i3);
        int i4 = this.itemMinimumHeight;
        if (i4 == -1) {
            i4 = View.MeasureSpec.getSize(i);
        }
        return View.MeasureSpec.makeMeasureSpec(Math.min(i4, max), 0);
    }

    @Override // com.google.android.material.navigation.NavigationBarMenuView
    public final NavigationBarItemView createNavigationBarItemView(Context context) {
        return new NavigationRailItemView(context);
    }

    public NavigationRailMenuView(Context context) {
        super(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
        this.layoutParams = layoutParams;
        layoutParams.gravity = 49;
        setLayoutParams(layoutParams);
        this.itemActiveIndicatorResizeable = true;
        NavigationBarItemView[] navigationBarItemViewArr = this.buttons;
        if (navigationBarItemViewArr != null) {
            for (NavigationBarItemView navigationBarItemView : navigationBarItemViewArr) {
                Objects.requireNonNull(navigationBarItemView);
                navigationBarItemView.activeIndicatorResizeable = true;
            }
        }
    }

    public final int measureSharedChildHeights(int i, int i2, int i3, View view) {
        int i4;
        int i5;
        makeSharedHeightSpec(i, i2, i3);
        if (view == null) {
            i4 = makeSharedHeightSpec(i, i2, i3);
        } else {
            i4 = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 0);
        }
        int childCount = getChildCount();
        int i6 = 0;
        for (int i7 = 0; i7 < childCount; i7++) {
            View childAt = getChildAt(i7);
            if (childAt != view) {
                if (childAt.getVisibility() != 8) {
                    childAt.measure(i, i4);
                    i5 = childAt.getMeasuredHeight();
                } else {
                    i5 = 0;
                }
                i6 += i5;
            }
        }
        return i6;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        int i5 = i3 - i;
        int i6 = 0;
        for (int i7 = 0; i7 < childCount; i7++) {
            View childAt = getChildAt(i7);
            if (childAt.getVisibility() != 8) {
                int measuredHeight = childAt.getMeasuredHeight() + i6;
                childAt.layout(0, i6, i5, measuredHeight);
                i6 = measuredHeight;
            }
        }
    }

    @Override // android.view.View
    public final void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int size = View.MeasureSpec.getSize(i2);
        int size2 = this.menu.getVisibleItems().size();
        if (size2 <= 1 || !NavigationBarMenuView.isShifting(this.labelVisibilityMode, size2)) {
            i3 = measureSharedChildHeights(i, size, size2, null);
        } else {
            View childAt = getChildAt(this.selectedItemPosition);
            if (childAt != null) {
                int makeSharedHeightSpec = makeSharedHeightSpec(i, size, size2);
                if (childAt.getVisibility() != 8) {
                    childAt.measure(i, makeSharedHeightSpec);
                    i4 = childAt.getMeasuredHeight();
                } else {
                    i4 = 0;
                }
                size -= i4;
                size2--;
            } else {
                i4 = 0;
            }
            i3 = measureSharedChildHeights(i, size, size2, childAt) + i4;
        }
        setMeasuredDimension(View.resolveSizeAndState(View.MeasureSpec.getSize(i), i, 0), View.resolveSizeAndState(i3, i2, 0));
    }
}
