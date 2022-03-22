package com.android.systemui.tuner;

import android.app.ActivityManager;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.AttributeSet;
import androidx.preference.SwitchPreference;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.tuner.TunerService;
/* loaded from: classes.dex */
public class StatusBarSwitch extends SwitchPreference implements TunerService.Tunable {
    public ArraySet mHideList;

    @Override // androidx.preference.Preference
    public final void onDetached() {
        ((TunerService) Dependency.get(TunerService.class)).removeTunable(this);
        unregisterDependency();
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public final void onTuningChanged(String str, String str2) {
        if ("icon_blacklist".equals(str)) {
            ArraySet<String> iconHideList = StatusBarIconController.getIconHideList(this.mContext, str2);
            this.mHideList = iconHideList;
            setChecked(!iconHideList.contains(this.mKey));
        }
    }

    @Override // androidx.preference.Preference
    public final boolean persistBoolean(boolean z) {
        if (!z) {
            if (this.mHideList.contains(this.mKey)) {
                return true;
            }
            MetricsLogger.action(this.mContext, 234, this.mKey);
            this.mHideList.add(this.mKey);
            Settings.Secure.putStringForUser(this.mContext.getContentResolver(), "icon_blacklist", TextUtils.join(",", this.mHideList), ActivityManager.getCurrentUser());
            return true;
        } else if (!this.mHideList.remove(this.mKey)) {
            return true;
        } else {
            MetricsLogger.action(this.mContext, 233, this.mKey);
            Settings.Secure.putStringForUser(this.mContext.getContentResolver(), "icon_blacklist", TextUtils.join(",", this.mHideList), ActivityManager.getCurrentUser());
            return true;
        }
    }

    @Override // androidx.preference.Preference
    public final void onAttached() {
        super.onAttached();
        ((TunerService) Dependency.get(TunerService.class)).addTunable(this, "icon_blacklist");
    }

    public StatusBarSwitch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
