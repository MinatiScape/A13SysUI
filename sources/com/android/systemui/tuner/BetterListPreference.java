package com.android.systemui.tuner;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.ListPreference;
/* loaded from: classes.dex */
public class BetterListPreference extends ListPreference {
    public CharSequence mSummary;

    @Override // androidx.preference.ListPreference, androidx.preference.Preference
    public final void setSummary(CharSequence charSequence) {
        super.setSummary(charSequence);
        this.mSummary = charSequence;
    }

    public BetterListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // androidx.preference.ListPreference, androidx.preference.Preference
    public final CharSequence getSummary() {
        return this.mSummary;
    }
}
