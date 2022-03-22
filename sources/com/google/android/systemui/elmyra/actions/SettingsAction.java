package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import android.os.Binder;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SettingsAction extends ServiceAction {
    public final LaunchOpa mLaunchOpa;
    public final String mSettingsPackageName;
    public final StatusBar mStatusBar;

    public SettingsAction(Context context, StatusBar statusBar, LaunchOpa launchOpa) {
        super(context, null);
        this.mSettingsPackageName = context.getResources().getString(2131953262);
        this.mStatusBar = statusBar;
        this.mLaunchOpa = launchOpa;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public final Context mContext;
        public final StatusBar mStatusBar;

        public Builder(Context context, StatusBar statusBar) {
            this.mContext = context;
            this.mStatusBar = statusBar;
        }
    }

    @Override // com.google.android.systemui.elmyra.actions.ServiceAction
    public final boolean checkSupportedCaller() {
        String str = this.mSettingsPackageName;
        String[] packagesForUid = this.mContext.getPackageManager().getPackagesForUid(Binder.getCallingUid());
        if (packagesForUid == null) {
            return false;
        }
        return Arrays.asList(packagesForUid).contains(str);
    }

    @Override // com.google.android.systemui.elmyra.actions.ServiceAction, com.google.android.systemui.elmyra.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.mStatusBar.collapseShade();
        super.onTrigger(detectionProperties);
    }

    @Override // com.google.android.systemui.elmyra.actions.ServiceAction
    public final void triggerAction() {
        if (this.mLaunchOpa.isAvailable()) {
            LaunchOpa launchOpa = this.mLaunchOpa;
            Objects.requireNonNull(launchOpa);
            launchOpa.launchOpa(0L);
        }
    }
}
