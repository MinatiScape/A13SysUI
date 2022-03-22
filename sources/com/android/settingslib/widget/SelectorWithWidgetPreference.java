package com.android.settingslib.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceViewHolder;
/* loaded from: classes.dex */
public class SelectorWithWidgetPreference extends CheckBoxPreference {
    public View mAppendix;
    public int mAppendixVisibility = -1;
    public ImageView mExtraWidget;
    public View mExtraWidgetContainer;
    public View.OnClickListener mExtraWidgetOnClickListener;

    @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
    public final void onClick() {
    }

    public SelectorWithWidgetPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mWidgetLayoutResId = 2131624407;
        this.mLayoutResId = 2131624401;
        if (this.mIconSpaceReserved) {
            this.mIconSpaceReserved = false;
            notifyChanged();
        }
    }

    @Override // androidx.preference.CheckBoxPreference, androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        int i;
        int i2;
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(2131428994);
        int i3 = 0;
        if (findViewById != null) {
            if (TextUtils.isEmpty(getSummary())) {
                i = 8;
            } else {
                i = 0;
            }
            findViewById.setVisibility(i);
            View findViewById2 = preferenceViewHolder.findViewById(2131427512);
            this.mAppendix = findViewById2;
            if (!(findViewById2 == null || (i2 = this.mAppendixVisibility) == -1)) {
                findViewById2.setVisibility(i2);
            }
        }
        this.mExtraWidget = (ImageView) preferenceViewHolder.findViewById(2131428829);
        View findViewById3 = preferenceViewHolder.findViewById(2131428830);
        this.mExtraWidgetContainer = findViewById3;
        View.OnClickListener onClickListener = this.mExtraWidgetOnClickListener;
        this.mExtraWidgetOnClickListener = onClickListener;
        ImageView imageView = this.mExtraWidget;
        if (imageView != null && findViewById3 != null) {
            imageView.setOnClickListener(onClickListener);
            View view = this.mExtraWidgetContainer;
            if (this.mExtraWidgetOnClickListener == null) {
                i3 = 8;
            }
            view.setVisibility(i3);
        }
    }
}
