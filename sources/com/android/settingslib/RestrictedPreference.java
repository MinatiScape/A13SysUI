package com.android.settingslib;

import android.content.Context;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.widget.TwoTargetPreference;
import java.util.Objects;
/* loaded from: classes.dex */
public class RestrictedPreference extends TwoTargetPreference {
    public RestrictedPreferenceHelper mHelper;

    public RestrictedPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i);
        this.mHelper = new RestrictedPreferenceHelper(context, this, attributeSet, 0);
    }

    @Override // androidx.preference.Preference
    public final void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        this.mHelper.onAttachedToHierarchy();
        super.onAttachedToHierarchy(preferenceManager);
    }

    @Override // androidx.preference.Preference
    public final void performClick() {
        boolean z;
        RestrictedPreferenceHelper restrictedPreferenceHelper = this.mHelper;
        Objects.requireNonNull(restrictedPreferenceHelper);
        if (restrictedPreferenceHelper.mDisabledByAdmin) {
            RestrictedLockUtils.sendShowAdminSupportDetailsIntent(restrictedPreferenceHelper.mContext, restrictedPreferenceHelper.mEnforcedAdmin);
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            super.performClick();
        }
    }

    @Override // androidx.preference.Preference
    public final void setEnabled(boolean z) {
        if (z) {
            RestrictedPreferenceHelper restrictedPreferenceHelper = this.mHelper;
            Objects.requireNonNull(restrictedPreferenceHelper);
            if (restrictedPreferenceHelper.mDisabledByAdmin) {
                this.mHelper.setDisabledByAdmin(null);
                return;
            }
        }
        super.setEnabled(z);
    }

    public RestrictedPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, 2130969626, 16842894), 0);
    }

    @Override // com.android.settingslib.widget.TwoTargetPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mHelper.onBindViewHolder(preferenceViewHolder);
    }
}
