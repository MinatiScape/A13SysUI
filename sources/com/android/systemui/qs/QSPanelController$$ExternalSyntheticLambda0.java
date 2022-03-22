package com.android.systemui.qs;

import android.view.View;
import com.android.systemui.qs.customize.QSCustomizer;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.statusbar.phone.PanelViewController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class QSPanelController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ View f$1;

    public /* synthetic */ QSPanelController$$ExternalSyntheticLambda0(Object obj, View view, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = view;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                QSPanelController qSPanelController = (QSPanelController) this.f$0;
                View view = this.f$1;
                Objects.requireNonNull(qSPanelController);
                QSCustomizerController qSCustomizerController = qSPanelController.mQsCustomizerController;
                Objects.requireNonNull(qSCustomizerController);
                if (!((QSCustomizer) qSCustomizerController.mView).isCustomizing()) {
                    int[] locationOnScreen = view.getLocationOnScreen();
                    int i = locationOnScreen[0];
                    int i2 = locationOnScreen[1];
                    qSPanelController.mQsCustomizerController.show((view.getWidth() / 2) + i, (view.getHeight() / 2) + i2, false);
                    return;
                }
                return;
            default:
                PanelViewController panelViewController = (PanelViewController) this.f$0;
                View view2 = this.f$1;
                int i3 = PanelViewController.$r8$clinit;
                Objects.requireNonNull(panelViewController);
                view2.animate().translationY(0.0f).setDuration(450L).setInterpolator(panelViewController.mBounceInterpolator).start();
                return;
        }
    }
}
