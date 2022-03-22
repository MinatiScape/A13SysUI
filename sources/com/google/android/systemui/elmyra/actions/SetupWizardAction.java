package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
/* loaded from: classes.dex */
public final class SetupWizardAction extends Action {
    public boolean mDeviceInDemoMode;
    public final AnonymousClass2 mKeyguardDeferredSetupListener;
    public final LaunchOpa mLaunchOpa;
    public final SettingsAction mSettingsAction;
    public final String mSettingsPackageName;
    public final StatusBar mStatusBar;
    public boolean mUserCompletedSuw;
    public final AnonymousClass1 mUserSwitchCallback;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.keyguard.KeyguardUpdateMonitorCallback, com.google.android.systemui.elmyra.actions.SetupWizardAction$1] */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.google.android.systemui.elmyra.gates.Gate$Listener, com.google.android.systemui.elmyra.actions.SetupWizardAction$2] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public SetupWizardAction(android.content.Context r5, com.google.android.systemui.elmyra.actions.SettingsAction r6, com.google.android.systemui.elmyra.actions.LaunchOpa r7, com.android.systemui.statusbar.phone.StatusBar r8) {
        /*
            r4 = this;
            r0 = 0
            r4.<init>(r5, r0)
            com.google.android.systemui.elmyra.actions.SetupWizardAction$1 r0 = new com.google.android.systemui.elmyra.actions.SetupWizardAction$1
            r0.<init>()
            r4.mUserSwitchCallback = r0
            com.google.android.systemui.elmyra.actions.SetupWizardAction$2 r1 = new com.google.android.systemui.elmyra.actions.SetupWizardAction$2
            r1.<init>()
            r4.mKeyguardDeferredSetupListener = r1
            android.content.res.Resources r2 = r5.getResources()
            r3 = 2131953262(0x7f13066e, float:1.954299E38)
            java.lang.String r2 = r2.getString(r3)
            r4.mSettingsPackageName = r2
            r4.mSettingsAction = r6
            r4.mLaunchOpa = r7
            r4.mStatusBar = r8
            java.lang.Class<com.android.keyguard.KeyguardUpdateMonitor> r6 = com.android.keyguard.KeyguardUpdateMonitor.class
            java.lang.Object r6 = com.android.systemui.Dependency.get(r6)
            com.android.keyguard.KeyguardUpdateMonitor r6 = (com.android.keyguard.KeyguardUpdateMonitor) r6
            r6.registerCallback(r0)
            com.google.android.systemui.elmyra.gates.KeyguardDeferredSetup r6 = new com.google.android.systemui.elmyra.gates.KeyguardDeferredSetup
            java.util.List r7 = java.util.Collections.emptyList()
            r6.<init>(r5, r7)
            r6.activate()
            r6.mListener = r1
            boolean r5 = r6.mDeferredSetupComplete
            r4.mUserCompletedSuw = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.elmyra.actions.SetupWizardAction.<init>(android.content.Context, com.google.android.systemui.elmyra.actions.SettingsAction, com.google.android.systemui.elmyra.actions.LaunchOpa, com.android.systemui.statusbar.phone.StatusBar):void");
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

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final boolean isAvailable() {
        if (!this.mDeviceInDemoMode && this.mLaunchOpa.isAvailable() && !this.mUserCompletedSuw && !this.mSettingsAction.isAvailable()) {
            return true;
        }
        return false;
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.mStatusBar.collapseShade();
        triggerFeedbackEffects(detectionProperties);
        if (!this.mUserCompletedSuw && !this.mSettingsAction.isAvailable()) {
            Intent intent = new Intent();
            intent.setAction("com.google.android.settings.ASSIST_GESTURE_TRAINING");
            intent.setPackage(this.mSettingsPackageName);
            intent.setFlags(268468224);
            this.mContext.startActivityAsUser(intent, UserHandle.of(-2));
        }
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final void triggerFeedbackEffects(GestureSensor.DetectionProperties detectionProperties) {
        super.triggerFeedbackEffects(detectionProperties);
        this.mLaunchOpa.triggerFeedbackEffects(detectionProperties);
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final void updateFeedbackEffects(float f, int i) {
        super.updateFeedbackEffects(f, i);
        this.mLaunchOpa.updateFeedbackEffects(f, i);
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final void onProgress(float f, int i) {
        updateFeedbackEffects(f, i);
    }
}
