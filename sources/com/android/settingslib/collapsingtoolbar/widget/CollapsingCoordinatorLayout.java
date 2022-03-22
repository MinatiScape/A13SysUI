package com.android.settingslib.collapsingtoolbar.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.android.systemui.R$id;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.internal.CollapsingTextHelper;
import java.util.Objects;
/* loaded from: classes.dex */
public class CollapsingCoordinatorLayout extends CoordinatorLayout {
    public AppBarLayout mAppBarLayout;
    public CollapsingToolbarLayout mCollapsingToolbarLayout;
    public boolean mIsMatchParentHeight;
    public CharSequence mToolbarTitle;

    public CollapsingCoordinatorLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CollapsingCoordinatorLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, 0);
        this.mIsMatchParentHeight = false;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$id.CollapsingCoordinatorLayout);
            this.mToolbarTitle = obtainStyledAttributes.getText(0);
            this.mIsMatchParentHeight = obtainStyledAttributes.getBoolean(1, false);
            obtainStyledAttributes.recycle();
        }
        View.inflate(getContext(), 2131624034, this);
        this.mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(2131427721);
        this.mAppBarLayout = (AppBarLayout) findViewById(2131427497);
        CollapsingToolbarLayout collapsingToolbarLayout = this.mCollapsingToolbarLayout;
        if (collapsingToolbarLayout != null) {
            CollapsingTextHelper collapsingTextHelper = collapsingToolbarLayout.collapsingTextHelper;
            Objects.requireNonNull(collapsingTextHelper);
            collapsingTextHelper.lineSpacingMultiplier = 1.1f;
            if (!TextUtils.isEmpty(this.mToolbarTitle)) {
                this.mCollapsingToolbarLayout.setTitle(this.mToolbarTitle);
            }
        }
        AppBarLayout appBarLayout = this.mAppBarLayout;
        if (appBarLayout != null) {
            AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
            behavior.onDragCallback = new AppBarLayout.Behavior.DragCallback() { // from class: com.android.settingslib.collapsingtoolbar.widget.CollapsingCoordinatorLayout.1
            };
            ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).setBehavior(behavior);
        }
    }

    @Override // android.view.ViewGroup
    public final void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        boolean z;
        if (view.getId() == 2131427742 && this.mIsMatchParentHeight) {
            layoutParams.height = -1;
        }
        ViewGroup viewGroup = (ViewGroup) findViewById(2131427742);
        if (viewGroup != null) {
            int id = view.getId();
            if (id == 2131427497 || id == 2131427742) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                viewGroup.addView(view, i, layoutParams);
                return;
            }
        }
        super.addView(view, i, layoutParams);
    }
}
