package com.android.settingslib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
/* loaded from: classes.dex */
public class BarChartPreference extends Preference {
    public BarChartPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (this.mSelectable) {
            this.mSelectable = false;
            notifyChanged();
        }
        this.mLayoutResId = 2131624471;
        this.mContext.getResources().getDimensionPixelSize(2131166977);
    }

    @Override // androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.mDividerAllowedAbove = true;
        preferenceViewHolder.mDividerAllowedBelow = true;
        TextView textView = (TextView) preferenceViewHolder.findViewById(2131427559);
        throw null;
    }
}
