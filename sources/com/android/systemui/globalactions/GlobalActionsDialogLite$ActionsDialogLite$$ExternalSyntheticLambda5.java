package com.android.systemui.globalactions;

import android.app.Dialog;
import com.android.systemui.MultiListLayout;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda5 implements MultiListLayout.RotationListener {
    public final /* synthetic */ GlobalActionsDialogLite.ActionsDialogLite f$0;

    public /* synthetic */ GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda5(GlobalActionsDialogLite.ActionsDialogLite actionsDialogLite) {
        this.f$0 = actionsDialogLite;
    }

    public final void onRotate() {
        GlobalActionsDialogLite.ActionsDialogLite actionsDialogLite = this.f$0;
        Objects.requireNonNull(actionsDialogLite);
        actionsDialogLite.mOnRefreshCallback.run();
        GlobalActionsPopupMenu globalActionsPopupMenu = actionsDialogLite.mOverflowPopup;
        if (globalActionsPopupMenu != null) {
            globalActionsPopupMenu.dismiss();
        }
        Dialog dialog = actionsDialogLite.mPowerOptionsDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
        MultiListLayout multiListLayout = actionsDialogLite.mGlobalActionsLayout;
        Objects.requireNonNull(multiListLayout);
        if (multiListLayout.mAdapter != null) {
            multiListLayout.onUpdateList();
            return;
        }
        throw new IllegalStateException("mAdapter must be set before calling updateList");
    }
}
