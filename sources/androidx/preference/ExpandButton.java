package androidx.preference;

import android.content.Context;
import android.text.TextUtils;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.preference.Preference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ExpandButton extends Preference {
    public long mId;

    public ExpandButton(Context context, ArrayList arrayList, long j) {
        super(context);
        this.mLayoutResId = 2131624095;
        setIcon(AppCompatResources.getDrawable(this.mContext, 2131231753));
        this.mIconResId = 2131231753;
        setTitle(2131952338);
        if (999 != this.mOrder) {
            this.mOrder = 999;
            Preference.OnPreferenceChangeInternalListener onPreferenceChangeInternalListener = this.mListener;
            if (onPreferenceChangeInternalListener != null) {
                PreferenceGroupAdapter preferenceGroupAdapter = (PreferenceGroupAdapter) onPreferenceChangeInternalListener;
                preferenceGroupAdapter.mHandler.removeCallbacks(preferenceGroupAdapter.mSyncRunnable);
                preferenceGroupAdapter.mHandler.post(preferenceGroupAdapter.mSyncRunnable);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        String str = null;
        while (it.hasNext()) {
            Preference preference = (Preference) it.next();
            Objects.requireNonNull(preference);
            CharSequence charSequence = preference.mTitle;
            boolean z = preference instanceof PreferenceGroup;
            if (z && !TextUtils.isEmpty(charSequence)) {
                arrayList2.add((PreferenceGroup) preference);
            }
            if (arrayList2.contains(preference.mParentGroup)) {
                if (z) {
                    arrayList2.add((PreferenceGroup) preference);
                }
            } else if (!TextUtils.isEmpty(charSequence)) {
                if (str == null) {
                    str = charSequence;
                } else {
                    str = this.mContext.getString(2131953343, str, charSequence);
                }
            }
        }
        setSummary(str);
        this.mId = j + 1000000;
    }

    @Override // androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.mDividerAllowedAbove = false;
    }

    @Override // androidx.preference.Preference
    public final long getId() {
        return this.mId;
    }
}
