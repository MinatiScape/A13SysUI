package com.android.systemui.tuner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import androidx.preference.CheckBoxPreference;
import com.android.systemui.statusbar.ScalingDrawableWrapper;
/* loaded from: classes.dex */
public class SelectablePreference extends CheckBoxPreference {
    public final int mSize;

    public SelectablePreference(Context context) {
        super(context, null);
        this.mWidgetLayoutResId = 2131624407;
        if (!this.mSelectable) {
            this.mSelectable = true;
            notifyChanged();
        }
        this.mSize = (int) TypedValue.applyDimension(1, 32.0f, context.getResources().getDisplayMetrics());
    }

    @Override // androidx.preference.Preference
    public String toString() {
        return "";
    }

    @Override // androidx.preference.Preference
    public final void setIcon(Drawable drawable) {
        super.setIcon(new ScalingDrawableWrapper(drawable, this.mSize / drawable.getIntrinsicWidth()));
    }
}
