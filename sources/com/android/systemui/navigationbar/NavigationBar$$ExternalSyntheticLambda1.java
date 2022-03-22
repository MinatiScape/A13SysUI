package com.android.systemui.navigationbar;

import android.view.View;
import com.android.systemui.navigationbar.buttons.KeyButtonView;
import com.google.android.systemui.gamedashboard.EntryPointController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NavigationBar$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ NavigationBar$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                NavigationBar navigationBar = (NavigationBar) this.f$0;
                Objects.requireNonNull(navigationBar);
                navigationBar.mInputMethodManager.showInputMethodPickerFromSystem(true, navigationBar.mDisplayId);
                navigationBar.mUiEventLogger.log(KeyButtonView.NavBarButtonEvent.NAVBAR_IME_SWITCHER_BUTTON_TAP);
                return;
            default:
                EntryPointController.$r8$lambda$VcVcWE2qGRM_B0Sxv8eSSgrgnCU((EntryPointController) this.f$0);
                return;
        }
    }
}
