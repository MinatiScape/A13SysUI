package com.android.wm.shell.common;

import android.util.Pools;
import android.view.SurfaceControl;
/* loaded from: classes.dex */
public final class TransactionPool {
    public final Pools.SynchronizedPool<SurfaceControl.Transaction> mTransactionPool = new Pools.SynchronizedPool<>(4);

    public final SurfaceControl.Transaction acquire() {
        SurfaceControl.Transaction transaction = (SurfaceControl.Transaction) this.mTransactionPool.acquire();
        if (transaction == null) {
            return new SurfaceControl.Transaction();
        }
        return transaction;
    }

    public final void release(SurfaceControl.Transaction transaction) {
        if (!this.mTransactionPool.release(transaction)) {
            transaction.close();
        }
    }
}
