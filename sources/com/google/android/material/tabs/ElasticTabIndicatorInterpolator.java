package com.google.android.material.tabs;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.google.android.material.animation.AnimationUtils;
/* loaded from: classes.dex */
public final class ElasticTabIndicatorInterpolator extends TabIndicatorInterpolator {
    @Override // com.google.android.material.tabs.TabIndicatorInterpolator
    public final void setIndicatorBoundsForOffset(TabLayout tabLayout, View view, View view2, float f, Drawable drawable) {
        boolean z;
        float f2;
        float f3;
        RectF calculateIndicatorWidthForTab = TabIndicatorInterpolator.calculateIndicatorWidthForTab(tabLayout, view);
        RectF calculateIndicatorWidthForTab2 = TabIndicatorInterpolator.calculateIndicatorWidthForTab(tabLayout, view2);
        if (calculateIndicatorWidthForTab.left < calculateIndicatorWidthForTab2.left) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            double d = (f * 3.141592653589793d) / 2.0d;
            f2 = (float) (1.0d - Math.cos(d));
            f3 = (float) Math.sin(d);
        } else {
            double d2 = (f * 3.141592653589793d) / 2.0d;
            f2 = (float) Math.sin(d2);
            f3 = (float) (1.0d - Math.cos(d2));
        }
        int i = (int) calculateIndicatorWidthForTab.left;
        LinearInterpolator linearInterpolator = AnimationUtils.LINEAR_INTERPOLATOR;
        int round = Math.round(f2 * (((int) calculateIndicatorWidthForTab2.left) - i)) + i;
        int i2 = drawable.getBounds().top;
        int i3 = (int) calculateIndicatorWidthForTab.right;
        drawable.setBounds(round, i2, Math.round(f3 * (((int) calculateIndicatorWidthForTab2.right) - i3)) + i3, drawable.getBounds().bottom);
    }
}
