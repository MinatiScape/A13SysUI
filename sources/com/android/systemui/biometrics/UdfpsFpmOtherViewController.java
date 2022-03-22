package com.android.systemui.biometrics;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
/* compiled from: UdfpsFpmOtherViewController.kt */
/* loaded from: classes.dex */
public final class UdfpsFpmOtherViewController extends UdfpsAnimationViewController<UdfpsFpmOtherView> {
    public final String tag = "UdfpsFpmOtherViewController";

    public UdfpsFpmOtherViewController(UdfpsFpmOtherView udfpsFpmOtherView, StatusBarStateController statusBarStateController, PanelExpansionStateManager panelExpansionStateManager, SystemUIDialogManager systemUIDialogManager, DumpManager dumpManager) {
        super(udfpsFpmOtherView, statusBarStateController, panelExpansionStateManager, systemUIDialogManager, dumpManager);
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public final String getTag() {
        return this.tag;
    }
}
