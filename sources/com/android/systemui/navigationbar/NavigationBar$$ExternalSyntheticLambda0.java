package com.android.systemui.navigationbar;

import android.view.Display;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import com.google.android.systemui.gamedashboard.EntryPointController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NavigationBar$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ NavigationBar$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        int i;
        switch (this.$r8$classId) {
            case 0:
                NavigationBar navigationBar = (NavigationBar) this.f$0;
                Objects.requireNonNull(navigationBar);
                Display display = view.getDisplay();
                AccessibilityManager accessibilityManager = navigationBar.mAccessibilityManager;
                if (display != null) {
                    i = display.getDisplayId();
                } else {
                    i = 0;
                }
                accessibilityManager.notifyAccessibilityButtonClicked(i);
                return;
            default:
                EntryPointController.$r8$lambda$VcVcWE2qGRM_B0Sxv8eSSgrgnCU((EntryPointController) this.f$0);
                return;
        }
    }
}
