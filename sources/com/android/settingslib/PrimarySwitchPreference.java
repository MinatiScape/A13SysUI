package com.android.settingslib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Switch;
import androidx.annotation.Keep;
import androidx.preference.PreferenceViewHolder;
/* loaded from: classes.dex */
public class PrimarySwitchPreference extends RestrictedPreference {
    public boolean mChecked;
    public boolean mCheckedSet;
    public boolean mEnableSwitch = true;
    public Switch mSwitch;

    public PrimarySwitchPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.settingslib.widget.TwoTargetPreference
    public final int getSecondTargetResId() {
        return 2131624406;
    }

    @Override // com.android.settingslib.widget.TwoTargetPreference
    public final boolean shouldHideSecondTarget() {
        return false;
    }

    @Keep
    public Boolean getCheckedState() {
        if (this.mCheckedSet) {
            return Boolean.valueOf(this.mChecked);
        }
        return null;
    }

    public final void setChecked(boolean z) {
        boolean z2;
        if (this.mChecked != z) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2 || !this.mCheckedSet) {
            this.mChecked = z;
            this.mCheckedSet = true;
            Switch r2 = this.mSwitch;
            if (r2 != null) {
                r2.setChecked(z);
            }
        }
    }

    public PrimarySwitchPreference(Context context) {
        super(context, null);
    }

    @Override // com.android.settingslib.RestrictedPreference, com.android.settingslib.widget.TwoTargetPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(2131428997);
        if (findViewById != null) {
            findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.android.settingslib.PrimarySwitchPreference.1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    Switch r2 = PrimarySwitchPreference.this.mSwitch;
                    if (r2 == null || r2.isEnabled()) {
                        PrimarySwitchPreference primarySwitchPreference = PrimarySwitchPreference.this;
                        primarySwitchPreference.setChecked(!primarySwitchPreference.mChecked);
                        PrimarySwitchPreference primarySwitchPreference2 = PrimarySwitchPreference.this;
                        if (!primarySwitchPreference2.callChangeListener(Boolean.valueOf(primarySwitchPreference2.mChecked))) {
                            PrimarySwitchPreference primarySwitchPreference3 = PrimarySwitchPreference.this;
                            primarySwitchPreference3.setChecked(!primarySwitchPreference3.mChecked);
                            return;
                        }
                        PrimarySwitchPreference primarySwitchPreference4 = PrimarySwitchPreference.this;
                        primarySwitchPreference4.persistBoolean(primarySwitchPreference4.mChecked);
                    }
                }
            });
            findViewById.setOnTouchListener(PrimarySwitchPreference$$ExternalSyntheticLambda0.INSTANCE);
        }
        Switch r4 = (Switch) preferenceViewHolder.findViewById(2131428997);
        this.mSwitch = r4;
        if (r4 != null) {
            r4.setContentDescription(this.mTitle);
            this.mSwitch.setChecked(this.mChecked);
            this.mSwitch.setEnabled(this.mEnableSwitch);
        }
    }
}
