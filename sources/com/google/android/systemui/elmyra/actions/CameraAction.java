package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import android.os.Binder;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class CameraAction extends ServiceAction {
    public final String mCameraPackageName;
    public final StatusBar mStatusBar;

    /* loaded from: classes.dex */
    public static class Builder {
        public final Context mContext;
        public ArrayList mFeedbackEffects = new ArrayList();
        public final StatusBar mStatusBar;

        public Builder(Context context, StatusBar statusBar) {
            this.mContext = context;
            this.mStatusBar = statusBar;
        }
    }

    @Override // com.google.android.systemui.elmyra.actions.ServiceAction
    public final boolean checkSupportedCaller() {
        String str = this.mCameraPackageName;
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

    public CameraAction(Context context, StatusBar statusBar, ArrayList arrayList) {
        super(context, arrayList);
        this.mCameraPackageName = context.getResources().getString(2131952409);
        this.mStatusBar = statusBar;
    }
}
