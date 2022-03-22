package com.android.systemui.tuner;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.provider.Settings;
import android.util.AttributeSet;
import androidx.preference.SwitchPreference;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.Dependency;
import com.android.systemui.R$styleable;
import com.android.systemui.tuner.TunerService;
/* loaded from: classes.dex */
public class TunerSwitch extends SwitchPreference implements TunerService.Tunable {
    public final int mAction;
    public final boolean mDefault;

    @Override // androidx.preference.Preference
    public final void onDetached() {
        ((TunerService) Dependency.get(TunerService.class)).removeTunable(this);
        unregisterDependency();
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public final void onTuningChanged(String str, String str2) {
        setChecked(TunerService.parseIntegerSwitch(str2, this.mDefault));
    }

    @Override // androidx.preference.Preference
    public final boolean persistBoolean(boolean z) {
        String[] split;
        String str;
        for (String str2 : this.mKey.split(",")) {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            if (z) {
                str = "1";
            } else {
                str = "0";
            }
            Settings.Secure.putString(contentResolver, str2, str);
        }
        return true;
    }

    public TunerSwitch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.TunerSwitch);
        this.mDefault = obtainStyledAttributes.getBoolean(0, false);
        this.mAction = obtainStyledAttributes.getInt(1, -1);
        obtainStyledAttributes.recycle();
    }

    @Override // androidx.preference.Preference
    public final void onAttached() {
        super.onAttached();
        ((TunerService) Dependency.get(TunerService.class)).addTunable(this, this.mKey.split(","));
    }

    @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
    public final void onClick() {
        super.onClick();
        int i = this.mAction;
        if (i != -1) {
            MetricsLogger.action(this.mContext, i, this.mChecked);
        }
    }
}
