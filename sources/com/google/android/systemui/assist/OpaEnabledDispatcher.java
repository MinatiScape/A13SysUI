package com.google.android.systemui.assist;

import android.content.Context;
import android.os.UserManager;
import android.util.Log;
import android.view.View;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class OpaEnabledDispatcher implements OpaEnabledListener {
    public final Lazy<StatusBar> mStatusBarLazy;

    @Override // com.google.android.systemui.assist.OpaEnabledListener
    public final void onOpaEnabledReceived(Context context, boolean z, boolean z2, boolean z3, boolean z4) {
        boolean z5;
        if ((!z4 || !z || !z2) && !UserManager.isDeviceInDemoMode(context)) {
            z5 = false;
        } else {
            z5 = true;
        }
        StatusBar statusBar = this.mStatusBarLazy.get();
        if (statusBar.getNavigationBarView() != null) {
            ButtonDispatcher homeButton = statusBar.getNavigationBarView().getHomeButton();
            Objects.requireNonNull(homeButton);
            ArrayList<View> arrayList = homeButton.mViews;
            for (int i = 0; i < arrayList.size(); i++) {
                OpaLayout opaLayout = (OpaLayout) arrayList.get(i);
                Objects.requireNonNull(opaLayout);
                Log.i("OpaLayout", "Setting opa enabled to " + z5);
                opaLayout.mOpaEnabled = z5;
                opaLayout.mOpaEnabledNeedsUpdate = false;
                opaLayout.updateOpaLayout();
            }
        }
    }

    public OpaEnabledDispatcher(Lazy<StatusBar> lazy) {
        this.mStatusBarLazy = lazy;
    }
}
