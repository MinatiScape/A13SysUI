package com.google.android.systemui.gamedashboard;

import android.content.Intent;
import android.view.View;
import com.google.android.systemui.dreamliner.DockGestureController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShortcutBarView$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ShortcutBarView$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                ShortcutBarView shortcutBarView = (ShortcutBarView) this.f$0;
                int i = ShortcutBarView.SHORTCUT_BAR_BACKGROUND_COLOR;
                Objects.requireNonNull(shortcutBarView);
                shortcutBarView.autoUndock(0.0f);
                return;
            default:
                DockGestureController dockGestureController = (DockGestureController) this.f$0;
                int i2 = DockGestureController.$r8$clinit;
                Objects.requireNonNull(dockGestureController);
                dockGestureController.hideGear();
                dockGestureController.sendProtectedBroadcast(new Intent("com.google.android.apps.dreamliner.SETTINGS"));
                return;
        }
    }
}
