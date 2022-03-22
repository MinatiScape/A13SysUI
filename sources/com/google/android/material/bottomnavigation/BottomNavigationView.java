package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.navigation.NavigationBarMenuView;
import com.google.android.material.navigation.NavigationBarView;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class BottomNavigationView extends NavigationBarView {
    @Override // com.google.android.material.navigation.NavigationBarView
    public final int getMaxItemCount() {
        return 5;
    }

    @Override // com.google.android.material.navigation.NavigationBarView
    public final NavigationBarMenuView createNavigationBarMenuView(Context context) {
        return new BottomNavigationMenuView(context);
    }

    public BottomNavigationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 2130968696, 2132018384);
        TintTypedArray obtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(getContext(), attributeSet, R$styleable.BottomNavigationView, 2130968696, 2132018384, new int[0]);
        boolean z = obtainTintedStyledAttributes.getBoolean(1, true);
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) this.menuView;
        Objects.requireNonNull(bottomNavigationMenuView);
        if (bottomNavigationMenuView.itemHorizontalTranslationEnabled != z) {
            bottomNavigationMenuView.itemHorizontalTranslationEnabled = z;
            this.presenter.updateMenuView(false);
        }
        if (obtainTintedStyledAttributes.hasValue(0)) {
            setMinimumHeight(obtainTintedStyledAttributes.getDimensionPixelSize(0, 0));
        }
        obtainTintedStyledAttributes.recycle();
        ViewUtils.doOnApplyWindowInsets(this, new ViewUtils.OnApplyWindowInsetsListener() { // from class: com.google.android.material.bottomnavigation.BottomNavigationView.1
            @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
                int i;
                relativePadding.bottom = windowInsetsCompat.getSystemWindowInsetBottom() + relativePadding.bottom;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                boolean z2 = true;
                if (ViewCompat.Api17Impl.getLayoutDirection(view) != 1) {
                    z2 = false;
                }
                int systemWindowInsetLeft = windowInsetsCompat.getSystemWindowInsetLeft();
                int systemWindowInsetRight = windowInsetsCompat.getSystemWindowInsetRight();
                int i2 = relativePadding.start;
                if (z2) {
                    i = systemWindowInsetRight;
                } else {
                    i = systemWindowInsetLeft;
                }
                int i3 = i2 + i;
                relativePadding.start = i3;
                int i4 = relativePadding.end;
                if (!z2) {
                    systemWindowInsetLeft = systemWindowInsetRight;
                }
                int i5 = i4 + systemWindowInsetLeft;
                relativePadding.end = i5;
                ViewCompat.Api17Impl.setPaddingRelative(view, i3, relativePadding.top, i5, relativePadding.bottom);
                return windowInsetsCompat;
            }
        });
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        int suggestedMinimumHeight = getSuggestedMinimumHeight();
        if (View.MeasureSpec.getMode(i2) != 1073741824 && suggestedMinimumHeight > 0) {
            int paddingTop = getPaddingTop();
            i2 = View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(i2), getPaddingBottom() + paddingTop + suggestedMinimumHeight), 1073741824);
        }
        super.onMeasure(i, i2);
    }
}
