package com.android.settingslib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
/* loaded from: classes.dex */
public class LayoutPreference extends Preference {
    public boolean mAllowDividerAbove;
    public boolean mAllowDividerBelow;
    public final LayoutPreference$$ExternalSyntheticLambda0 mClickListener = new LayoutPreference$$ExternalSyntheticLambda0(this, 0);
    public View mRootView;

    @Override // androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        preferenceViewHolder.itemView.setOnClickListener(this.mClickListener);
        boolean z = this.mSelectable;
        preferenceViewHolder.itemView.setFocusable(z);
        preferenceViewHolder.itemView.setClickable(z);
        preferenceViewHolder.mDividerAllowedAbove = this.mAllowDividerAbove;
        preferenceViewHolder.mDividerAllowedBelow = this.mAllowDividerBelow;
        FrameLayout frameLayout = (FrameLayout) preferenceViewHolder.itemView;
        frameLayout.removeAllViews();
        ViewGroup viewGroup = (ViewGroup) this.mRootView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(this.mRootView);
        }
        frameLayout.addView(this.mRootView);
    }

    public LayoutPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int[] iArr = R$styleable.Preference;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr);
        this.mAllowDividerAbove = obtainStyledAttributes.getBoolean(16, obtainStyledAttributes.getBoolean(16, false));
        this.mAllowDividerBelow = obtainStyledAttributes.getBoolean(17, obtainStyledAttributes.getBoolean(17, false));
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, iArr, 0, 0);
        int resourceId = obtainStyledAttributes2.getResourceId(3, 0);
        if (resourceId != 0) {
            obtainStyledAttributes2.recycle();
            View inflate = LayoutInflater.from(this.mContext).inflate(resourceId, (ViewGroup) null, false);
            this.mLayoutResId = 2131624175;
            this.mRootView = inflate;
            if (this.mShouldDisableView) {
                this.mShouldDisableView = false;
                notifyChanged();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("LayoutPreference requires a layout to be defined");
    }
}
