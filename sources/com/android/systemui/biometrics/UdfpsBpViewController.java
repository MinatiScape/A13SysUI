package com.android.systemui.biometrics;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
/* compiled from: UdfpsBpViewController.kt */
/* loaded from: classes.dex */
public final class UdfpsBpViewController extends UdfpsAnimationViewController<UdfpsBpView> {
    public final String tag = "UdfpsBpViewController";

    public UdfpsBpViewController(UdfpsBpView udfpsBpView, StatusBarStateController statusBarStateController, PanelExpansionStateManager panelExpansionStateManager, SystemUIDialogManager systemUIDialogManager, DumpManager dumpManager) {
        super(udfpsBpView, statusBarStateController, panelExpansionStateManager, systemUIDialogManager, dumpManager);
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public final String getTag() {
        return this.tag;
    }
}
