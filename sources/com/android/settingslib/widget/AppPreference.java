package com.android.settingslib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
/* loaded from: classes.dex */
public class AppPreference extends Preference {
    public AppPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLayoutResId = 2131624385;
    }

    @Override // androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((ProgressBar) preferenceViewHolder.findViewById(16908301)).setVisibility(8);
    }
}
