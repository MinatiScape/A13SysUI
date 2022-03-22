package androidx.preference;

import android.content.Context;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PreferenceScreen extends PreferenceGroup {
    public boolean mShouldUseGeneratedIds = true;

    @Override // androidx.preference.Preference
    public final void onClick() {
        if (this.mIntent == null && this.mFragment == null && getPreferenceCount() != 0) {
            PreferenceManager preferenceManager = this.mPreferenceManager;
            Objects.requireNonNull(preferenceManager);
            PreferenceManager.OnNavigateToScreenListener onNavigateToScreenListener = preferenceManager.mOnNavigateToScreenListener;
            if (onNavigateToScreenListener != null) {
                PreferenceFragment preferenceFragment = (PreferenceFragment) onNavigateToScreenListener;
                if (preferenceFragment.getActivity() instanceof PreferenceFragment.OnPreferenceStartScreenCallback) {
                    ((PreferenceFragment.OnPreferenceStartScreenCallback) preferenceFragment.getActivity()).onPreferenceStartScreen(preferenceFragment, this);
                }
            }
        }
    }

    public PreferenceScreen(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, TypedArrayUtils.getAttr(context, 2130969625, 16842891), 0);
    }
}
