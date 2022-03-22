package com.google.android.material.navigationrail;

import android.content.Context;
import android.view.View;
import com.google.android.material.navigation.NavigationBarItemView;
/* loaded from: classes.dex */
public final class NavigationRailItemView extends NavigationBarItemView {
    @Override // com.google.android.material.navigation.NavigationBarItemView
    public final int getItemDefaultMarginResId() {
        return 2131166525;
    }

    @Override // com.google.android.material.navigation.NavigationBarItemView
    public final int getItemLayoutResId() {
        return 2131624304;
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (View.MeasureSpec.getMode(i2) == 0) {
            setMeasuredDimension(getMeasuredWidthAndState(), View.resolveSizeAndState(Math.max(getMeasuredHeight(), View.MeasureSpec.getSize(i2)), i2, 0));
        }
    }

    public NavigationRailItemView(Context context) {
        super(context);
    }
}
