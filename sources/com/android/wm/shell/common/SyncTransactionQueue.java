package com.android.wm.shell.common;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.RemoteException;
import android.util.Slog;
import android.view.SurfaceControl;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import android.window.WindowOrganizer;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda2;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.transition.LegacyTransitions$LegacyTransition;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SyncTransactionQueue {
    public final ShellExecutor mMainExecutor;
    public final TransactionPool mTransactionPool;
    public final ArrayList<SyncCallback> mQueue = new ArrayList<>();
    public SyncCallback mInFlight = null;
    public final ArrayList<TransactionRunnable> mRunnables = new ArrayList<>();
    public final LockIconViewController$$ExternalSyntheticLambda2 mOnReplyTimeout = new LockIconViewController$$ExternalSyntheticLambda2(this, 7);

    /* loaded from: classes.dex */
    public class SyncCallback extends WindowContainerTransactionCallback {
        public int mId;
        public final LegacyTransitions$LegacyTransition mLegacyTransition;
        public final WindowContainerTransaction mWCT;

        public SyncCallback(WindowContainerTransaction windowContainerTransaction) {
            this.mId = -1;
            this.mWCT = windowContainerTransaction;
            this.mLegacyTransition = null;
        }

        public final void onTransactionReady(final int i, final SurfaceControl.Transaction transaction) {
            SyncTransactionQueue.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.common.SyncTransactionQueue$SyncCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SyncTransactionQueue.SyncCallback syncCallback = SyncTransactionQueue.SyncCallback.this;
                    int i2 = i;
                    SurfaceControl.Transaction transaction2 = transaction;
                    Objects.requireNonNull(syncCallback);
                    synchronized (SyncTransactionQueue.this.mQueue) {
                        if (syncCallback.mId != i2) {
                            Slog.e("SyncTransactionQueue", "Got an unexpected onTransactionReady. Expected " + syncCallback.mId + " but got " + i2);
                            return;
                        }
                        SyncTransactionQueue syncTransactionQueue = SyncTransactionQueue.this;
                        syncTransactionQueue.mInFlight = null;
                        syncTransactionQueue.mMainExecutor.removeCallbacks(syncTransactionQueue.mOnReplyTimeout);
                        SyncTransactionQueue.this.mQueue.remove(syncCallback);
                        SyncTransactionQueue syncTransactionQueue2 = SyncTransactionQueue.this;
                        Objects.requireNonNull(syncTransactionQueue2);
                        int size = syncTransactionQueue2.mRunnables.size();
                        for (int i3 = 0; i3 < size; i3++) {
                            syncTransactionQueue2.mRunnables.get(i3).runWithTransaction(transaction2);
                        }
                        syncTransactionQueue2.mRunnables.subList(0, size).clear();
                        LegacyTransitions$LegacyTransition legacyTransitions$LegacyTransition = syncCallback.mLegacyTransition;
                        if (legacyTransitions$LegacyTransition != null) {
                            try {
                                legacyTransitions$LegacyTransition.mSyncCallback.onTransactionReady(syncCallback.mId, transaction2);
                            } catch (RemoteException e) {
                                Slog.e("SyncTransactionQueue", "Error sending callback to legacy transition: " + syncCallback.mId, e);
                            }
                        } else {
                            transaction2.apply();
                            transaction2.close();
                        }
                        if (!SyncTransactionQueue.this.mQueue.isEmpty()) {
                            SyncTransactionQueue.this.mQueue.get(0).send();
                        }
                    }
                }
            });
        }

        public final void send() {
            SyncTransactionQueue syncTransactionQueue = SyncTransactionQueue.this;
            SyncCallback syncCallback = syncTransactionQueue.mInFlight;
            if (syncCallback != this) {
                if (syncCallback == null) {
                    syncTransactionQueue.mInFlight = this;
                    if (this.mLegacyTransition != null) {
                        WindowOrganizer windowOrganizer = new WindowOrganizer();
                        LegacyTransitions$LegacyTransition legacyTransitions$LegacyTransition = this.mLegacyTransition;
                        Objects.requireNonNull(legacyTransitions$LegacyTransition);
                        int i = legacyTransitions$LegacyTransition.mTransit;
                        LegacyTransitions$LegacyTransition legacyTransitions$LegacyTransition2 = this.mLegacyTransition;
                        Objects.requireNonNull(legacyTransitions$LegacyTransition2);
                        this.mId = windowOrganizer.startLegacyTransition(i, legacyTransitions$LegacyTransition2.mAdapter, this, this.mWCT);
                    } else {
                        this.mId = new WindowOrganizer().applySyncTransaction(this.mWCT, this);
                    }
                    SyncTransactionQueue syncTransactionQueue2 = SyncTransactionQueue.this;
                    syncTransactionQueue2.mMainExecutor.executeDelayed(syncTransactionQueue2.mOnReplyTimeout, 5300L);
                    return;
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Sync Transactions must be serialized. In Flight: ");
                m.append(SyncTransactionQueue.this.mInFlight.mId);
                m.append(" - ");
                m.append(SyncTransactionQueue.this.mInFlight.mWCT);
                throw new IllegalStateException(m.toString());
            }
        }

        public SyncCallback(SplitScreenController.AnonymousClass1 r2, WindowContainerTransaction windowContainerTransaction) {
            this.mId = -1;
            this.mWCT = windowContainerTransaction;
            this.mLegacyTransition = new LegacyTransitions$LegacyTransition(r2);
        }
    }

    /* loaded from: classes.dex */
    public interface TransactionRunnable {
        void runWithTransaction(SurfaceControl.Transaction transaction);
    }

    public final void runInSync(TransactionRunnable transactionRunnable) {
        synchronized (this.mQueue) {
            if (this.mInFlight != null) {
                this.mRunnables.add(transactionRunnable);
                return;
            }
            SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
            transactionRunnable.runWithTransaction(acquire);
            acquire.apply();
            this.mTransactionPool.release(acquire);
        }
    }

    public SyncTransactionQueue(TransactionPool transactionPool, ShellExecutor shellExecutor) {
        this.mTransactionPool = transactionPool;
        this.mMainExecutor = shellExecutor;
    }

    public final void queue(WindowContainerTransaction windowContainerTransaction) {
        if (!windowContainerTransaction.isEmpty()) {
            SyncCallback syncCallback = new SyncCallback(windowContainerTransaction);
            synchronized (this.mQueue) {
                this.mQueue.add(syncCallback);
                if (this.mQueue.size() == 1) {
                    syncCallback.send();
                }
            }
        }
    }
}
