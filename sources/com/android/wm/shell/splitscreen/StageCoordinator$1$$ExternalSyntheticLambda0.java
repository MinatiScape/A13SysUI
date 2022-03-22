package com.android.wm.shell.splitscreen;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.splitscreen.StageCoordinator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StageCoordinator$1$$ExternalSyntheticLambda0 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ StageCoordinator$1$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        switch (this.$r8$classId) {
            case 0:
                StageCoordinator.AnonymousClass1 r2 = (StageCoordinator.AnonymousClass1) this.f$0;
                Objects.requireNonNull(r2);
                StageCoordinator.this.applyDividerVisibility(transaction);
                return;
            default:
                LegacySplitScreenController legacySplitScreenController = (LegacySplitScreenController) this.f$0;
                Objects.requireNonNull(legacySplitScreenController);
                legacySplitScreenController.mView.setMinimizedDockStack(legacySplitScreenController.mMinimized, legacySplitScreenController.mHomeStackResizable, transaction);
                return;
        }
    }
}
