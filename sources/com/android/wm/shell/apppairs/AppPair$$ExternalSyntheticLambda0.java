package com.android.wm.shell.apppairs;

import android.view.SurfaceControl;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.splitscreen.StageTaskListener;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AppPair$$ExternalSyntheticLambda0 implements SyncTransactionQueue.TransactionRunnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ AppPair$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
        switch (this.$r8$classId) {
            case 0:
                AppPair appPair = (AppPair) this.f$0;
                Objects.requireNonNull(appPair);
                transaction.remove(appPair.mDimLayer1);
                return;
            default:
                StageTaskListener stageTaskListener = (StageTaskListener) this.f$0;
                Objects.requireNonNull(stageTaskListener);
                transaction.remove(stageTaskListener.mDimLayer);
                stageTaskListener.mSplitDecorManager.release(transaction);
                return;
        }
    }
}
