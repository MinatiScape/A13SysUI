package com.android.settingslib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import java.util.Objects;
/* loaded from: classes.dex */
public class SettingsSpinnerPreference extends Preference implements Preference.OnPreferenceClickListener {
    public final AnonymousClass1 mOnSelectedListener = new AdapterView.OnItemSelectedListener() { // from class: com.android.settingslib.widget.SettingsSpinnerPreference.1
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public final void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            SettingsSpinnerPreference settingsSpinnerPreference = SettingsSpinnerPreference.this;
            if (settingsSpinnerPreference.mPosition != i) {
                settingsSpinnerPreference.mPosition = i;
                Objects.requireNonNull(settingsSpinnerPreference);
            }
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public final void onNothingSelected(AdapterView<?> adapterView) {
            Objects.requireNonNull(SettingsSpinnerPreference.this);
        }
    };
    public int mPosition;
    public boolean mShouldPerformClick;

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public final void onPreferenceClick(Preference preference) {
        this.mShouldPerformClick = true;
        notifyChanged();
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.settingslib.widget.SettingsSpinnerPreference$1] */
    public SettingsSpinnerPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLayoutResId = 2131624476;
        this.mOnClickListener = this;
    }

    @Override // androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        Spinner spinner = (Spinner) preferenceViewHolder.findViewById(2131428895);
        spinner.setAdapter((SpinnerAdapter) null);
        spinner.setSelection(this.mPosition);
        spinner.setOnItemSelectedListener(this.mOnSelectedListener);
        if (this.mShouldPerformClick) {
            this.mShouldPerformClick = false;
            spinner.performClick();
        }
    }
}
