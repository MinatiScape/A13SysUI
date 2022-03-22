package com.android.settingslib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
/* loaded from: classes.dex */
public class TwoTargetPreference extends Preference {
    public TwoTargetPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i, 0);
        init(context);
    }

    public int getSecondTargetResId() {
        return 0;
    }

    public TwoTargetPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public final void init(Context context) {
        this.mLayoutResId = 2131624402;
        context.getResources().getDimensionPixelSize(2131167258);
        context.getResources().getDimensionPixelSize(2131167257);
        int secondTargetResId = getSecondTargetResId();
        if (secondTargetResId != 0) {
            this.mWidgetLayoutResId = secondTargetResId;
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        int i;
        super.onBindViewHolder(preferenceViewHolder);
        ImageView imageView = (ImageView) preferenceViewHolder.itemView.findViewById(16908294);
        View findViewById = preferenceViewHolder.findViewById(2131429134);
        View findViewById2 = preferenceViewHolder.findViewById(16908312);
        boolean shouldHideSecondTarget = shouldHideSecondTarget();
        int i2 = 8;
        if (findViewById != null) {
            if (shouldHideSecondTarget) {
                i = 8;
            } else {
                i = 0;
            }
            findViewById.setVisibility(i);
        }
        if (findViewById2 != null) {
            if (!shouldHideSecondTarget) {
                i2 = 0;
            }
            findViewById2.setVisibility(i2);
        }
    }

    public boolean shouldHideSecondTarget() {
        if (getSecondTargetResId() == 0) {
            return true;
        }
        return false;
    }
}
