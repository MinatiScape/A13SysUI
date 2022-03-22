package com.android.systemui.util.service;

import android.util.Log;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.service.ObservableServiceConnection;
import com.android.systemui.util.service.Observer;
import com.android.systemui.util.time.SystemClock;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PersistentConnectionManager<T> {
    public static final boolean DEBUG = Log.isLoggable("PersistentConnManager", 3);
    public final int mBaseReconnectDelayMs;
    public final ObservableServiceConnection<T> mConnection;
    public Runnable mCurrentReconnectCancelable;
    public final DelayableExecutor mMainExecutor;
    public final int mMaxReconnectAttempts;
    public final int mMinConnectionDuration;
    public final Observer mObserver;
    public final SystemClock mSystemClock;
    public int mReconnectAttempts = 0;
    public final AnonymousClass1 mConnectRunnable = new Runnable() { // from class: com.android.systemui.util.service.PersistentConnectionManager.1
        @Override // java.lang.Runnable
        public final void run() {
            PersistentConnectionManager persistentConnectionManager = PersistentConnectionManager.this;
            persistentConnectionManager.mCurrentReconnectCancelable = null;
            persistentConnectionManager.mConnection.bind();
        }
    };
    public final PersistentConnectionManager$$ExternalSyntheticLambda0 mObserverCallback = new Observer.Callback() { // from class: com.android.systemui.util.service.PersistentConnectionManager$$ExternalSyntheticLambda0
        @Override // com.android.systemui.util.service.Observer.Callback
        public final void onSourceChanged() {
            PersistentConnectionManager persistentConnectionManager = PersistentConnectionManager.this;
            Objects.requireNonNull(persistentConnectionManager);
            persistentConnectionManager.mReconnectAttempts = 0;
            persistentConnectionManager.mConnection.bind();
        }
    };
    public final AnonymousClass2 mConnectionCallback = new ObservableServiceConnection.Callback() { // from class: com.android.systemui.util.service.PersistentConnectionManager.2
        public long mStartTime;

        @Override // com.android.systemui.util.service.ObservableServiceConnection.Callback
        public final void onConnected(Object obj) {
            this.mStartTime = PersistentConnectionManager.this.mSystemClock.currentTimeMillis();
        }

        @Override // com.android.systemui.util.service.ObservableServiceConnection.Callback
        public final void onDisconnected() {
            PersistentConnectionManager persistentConnectionManager = PersistentConnectionManager.this;
            if (PersistentConnectionManager.this.mSystemClock.currentTimeMillis() - this.mStartTime > persistentConnectionManager.mMinConnectionDuration) {
                persistentConnectionManager.mReconnectAttempts = 0;
                persistentConnectionManager.mConnection.bind();
                return;
            }
            Runnable runnable = persistentConnectionManager.mCurrentReconnectCancelable;
            if (runnable != null) {
                runnable.run();
                persistentConnectionManager.mCurrentReconnectCancelable = null;
            }
            int i = persistentConnectionManager.mReconnectAttempts;
            if (i < persistentConnectionManager.mMaxReconnectAttempts) {
                long scalb = Math.scalb(persistentConnectionManager.mBaseReconnectDelayMs, i);
                if (PersistentConnectionManager.DEBUG) {
                    Log.d("PersistentConnManager", "scheduling connection attempt in " + scalb + "milliseconds");
                }
                persistentConnectionManager.mCurrentReconnectCancelable = persistentConnectionManager.mMainExecutor.executeDelayed(persistentConnectionManager.mConnectRunnable, scalb);
                persistentConnectionManager.mReconnectAttempts++;
            } else if (PersistentConnectionManager.DEBUG) {
                Log.d("PersistentConnManager", "exceeded max connection attempts.");
            }
        }
    };

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.util.service.PersistentConnectionManager$1] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.util.service.PersistentConnectionManager$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.util.service.PersistentConnectionManager$2] */
    public PersistentConnectionManager(SystemClock systemClock, DelayableExecutor delayableExecutor, ObservableServiceConnection observableServiceConnection, int i, int i2, int i3, PackageObserver packageObserver) {
        this.mSystemClock = systemClock;
        this.mMainExecutor = delayableExecutor;
        this.mConnection = observableServiceConnection;
        this.mObserver = packageObserver;
        this.mMaxReconnectAttempts = i;
        this.mBaseReconnectDelayMs = i2;
        this.mMinConnectionDuration = i3;
    }
}
