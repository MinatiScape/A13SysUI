package com.google.android.setupdesign.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
/* loaded from: classes.dex */
public class StickyHeaderScrollView extends BottomScrollView {
    public int statusBarInset = 0;
    public View sticky;
    public View stickyContainer;

    public StickyHeaderScrollView(Context context) {
        super(context);
    }

    public final void updateStickyHeaderPosition() {
        View view;
        int i;
        View view2 = this.sticky;
        if (view2 != null) {
            View view3 = this.stickyContainer;
            if (view3 != null) {
                view = view3;
            } else {
                view = view2;
            }
            if (view3 != null) {
                i = view2.getTop();
            } else {
                i = 0;
            }
            if ((view.getTop() - getScrollY()) + i < this.statusBarInset || !view.isShown()) {
                view.setTranslationY(getScrollY() - i);
            } else {
                view.setTranslationY(0.0f);
            }
        }
    }

    public StickyHeaderScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    @TargetApi(21)
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        if (!getFitsSystemWindows()) {
            return windowInsets;
        }
        this.statusBarInset = windowInsets.getSystemWindowInsetTop();
        return windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), 0, windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
    }

    @Override // com.google.android.setupdesign.view.BottomScrollView, android.widget.ScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.sticky == null) {
            this.sticky = findViewWithTag("sticky");
            this.stickyContainer = findViewWithTag("stickyContainer");
        }
        updateStickyHeaderPosition();
    }

    @Override // com.google.android.setupdesign.view.BottomScrollView, android.view.View
    public final void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        updateStickyHeaderPosition();
    }

    public StickyHeaderScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
