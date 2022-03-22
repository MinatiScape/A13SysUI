package com.android.systemui.tuner;

import androidx.preference.Preference;
import com.android.systemui.Dependency;
import com.android.systemui.plugins.ActivityStarter;
import java.io.Serializable;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NavBarTuner$$ExternalSyntheticLambda2 implements ActivityStarter.OnDismissAction, Preference.OnPreferenceChangeListener {
    public static final /* synthetic */ NavBarTuner$$ExternalSyntheticLambda2 INSTANCE$1 = new NavBarTuner$$ExternalSyntheticLambda2();
    public static final /* synthetic */ NavBarTuner$$ExternalSyntheticLambda2 INSTANCE = new NavBarTuner$$ExternalSyntheticLambda2();

    @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
    public boolean onDismiss() {
        return false;
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Serializable serializable) {
        int[][] iArr = NavBarTuner.ICONS;
        String str = (String) serializable;
        if ("default".equals(str)) {
            str = null;
        }
        ((TunerService) Dependency.get(TunerService.class)).setValue("sysui_nav_bar", str);
        return true;
    }
}
