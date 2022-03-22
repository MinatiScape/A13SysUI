package com.android.settingslib;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.res.TypedArrayUtils;
import androidx.leanback.R$string;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreference;
import java.util.Objects;
/* loaded from: classes.dex */
public class RestrictedSwitchPreference extends SwitchPreference {
    public RestrictedPreferenceHelper mHelper;
    public CharSequence mRestrictedSwitchSummary;
    public boolean mUseAdditionalSummary;

    public RestrictedSwitchPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, 0);
        this.mUseAdditionalSummary = false;
        this.mHelper = new RestrictedPreferenceHelper(context, this, attributeSet, 0);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$string.RestrictedSwitchPreference);
            boolean z = true;
            TypedValue peekValue = obtainStyledAttributes.peekValue(1);
            if (peekValue != null) {
                this.mUseAdditionalSummary = (peekValue.type != 18 || peekValue.data == 0) ? false : z;
            }
            TypedValue peekValue2 = obtainStyledAttributes.peekValue(0);
            obtainStyledAttributes.recycle();
            if (peekValue2 != null && peekValue2.type == 3) {
                int i3 = peekValue2.resourceId;
                if (i3 != 0) {
                    this.mRestrictedSwitchSummary = context.getText(i3);
                } else {
                    this.mRestrictedSwitchSummary = peekValue2.string;
                }
            }
        }
        if (this.mUseAdditionalSummary) {
            this.mLayoutResId = 2131624448;
            RestrictedPreferenceHelper restrictedPreferenceHelper = this.mHelper;
            Objects.requireNonNull(restrictedPreferenceHelper);
            restrictedPreferenceHelper.mDisabledSummary = false;
        }
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

    @Override // androidx.preference.SwitchPreference, androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        int i;
        super.onBindViewHolder(preferenceViewHolder);
        this.mHelper.onBindViewHolder(preferenceViewHolder);
        CharSequence charSequence = this.mRestrictedSwitchSummary;
        if (charSequence == null) {
            Context context = this.mContext;
            if (this.mChecked) {
                i = 2131952332;
            } else {
                i = 2131952285;
            }
            charSequence = context.getText(i);
        }
        ImageView imageView = (ImageView) preferenceViewHolder.itemView.findViewById(16908294);
        if (this.mUseAdditionalSummary) {
            TextView textView = (TextView) preferenceViewHolder.findViewById(2131427464);
            if (textView != null) {
                RestrictedPreferenceHelper restrictedPreferenceHelper = this.mHelper;
                Objects.requireNonNull(restrictedPreferenceHelper);
                if (restrictedPreferenceHelper.mDisabledByAdmin) {
                    textView.setText(charSequence);
                    textView.setVisibility(0);
                    return;
                }
                textView.setVisibility(8);
                return;
            }
            return;
        }
        TextView textView2 = (TextView) preferenceViewHolder.findViewById(16908304);
        if (textView2 != null) {
            RestrictedPreferenceHelper restrictedPreferenceHelper2 = this.mHelper;
            Objects.requireNonNull(restrictedPreferenceHelper2);
            if (restrictedPreferenceHelper2.mDisabledByAdmin) {
                textView2.setText(charSequence);
                textView2.setVisibility(0);
            }
        }
    }

    public RestrictedSwitchPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, 2130969916, 16843629), 0);
    }
}
