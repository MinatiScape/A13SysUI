package com.android.settingslib;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import androidx.leanback.R$string;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.RestrictedLockUtils;
import java.util.concurrent.Callable;
/* loaded from: classes.dex */
public final class RestrictedPreferenceHelper {
    public String mAttrUserRestriction;
    public final Context mContext;
    public boolean mDisabledByAdmin;
    public boolean mDisabledSummary;
    public RestrictedLockUtils.EnforcedAdmin mEnforcedAdmin;
    public final Preference mPreference;

    public final boolean setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        boolean z;
        boolean z2 = false;
        if (enforcedAdmin != null) {
            z = true;
        } else {
            z = false;
        }
        this.mEnforcedAdmin = enforcedAdmin;
        if (this.mDisabledByAdmin != z) {
            this.mDisabledByAdmin = z;
            z2 = true;
        }
        Preference preference = this.mPreference;
        if (!(preference instanceof RestrictedTopLevelPreference)) {
            preference.setEnabled(!this.mDisabledByAdmin);
        }
        return z2;
    }

    public final void onAttachedToHierarchy() {
        String str = this.mAttrUserRestriction;
        if (str != null) {
            setDisabledByAdmin(RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, str, UserHandle.myUserId()));
        }
    }

    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        final TextView textView;
        if (this.mDisabledByAdmin) {
            preferenceViewHolder.itemView.setEnabled(true);
        }
        if (this.mDisabledSummary && (textView = (TextView) preferenceViewHolder.findViewById(16908304)) != null) {
            String string = ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class)).getString("Settings.CONTROLLED_BY_ADMIN_SUMMARY", new Callable() { // from class: com.android.settingslib.RestrictedPreferenceHelper$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return textView.getContext().getString(2131952286);
                }
            });
            if (this.mDisabledByAdmin) {
                textView.setText(string);
            } else if (TextUtils.equals(string, textView.getText())) {
                textView.setText((CharSequence) null);
            }
        }
    }

    public RestrictedPreferenceHelper(Context context, Preference preference, AttributeSet attributeSet, int i) {
        CharSequence charSequence;
        String str;
        this.mAttrUserRestriction = null;
        boolean z = false;
        this.mDisabledSummary = false;
        this.mContext = context;
        this.mPreference = preference;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$string.RestrictedPreference);
            TypedValue peekValue = obtainStyledAttributes.peekValue(1);
            if (peekValue == null || peekValue.type != 3) {
                charSequence = null;
            } else {
                int i2 = peekValue.resourceId;
                if (i2 != 0) {
                    charSequence = context.getText(i2);
                } else {
                    charSequence = peekValue.string;
                }
            }
            if (charSequence == null) {
                str = null;
            } else {
                str = charSequence.toString();
            }
            this.mAttrUserRestriction = str;
            if (RestrictedLockUtilsInternal.hasBaseUserRestriction(context, str, UserHandle.myUserId())) {
                this.mAttrUserRestriction = null;
                return;
            }
            TypedValue peekValue2 = obtainStyledAttributes.peekValue(0);
            if (peekValue2 != null) {
                if (peekValue2.type == 18 && peekValue2.data != 0) {
                    z = true;
                }
                this.mDisabledSummary = z;
            }
        }
    }
}
