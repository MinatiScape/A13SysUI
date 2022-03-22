package com.android.systemui.util.concurrency;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
/* loaded from: classes.dex */
public final class ThreadFactoryImpl implements ThreadFactory {
    @Override // com.android.systemui.util.concurrency.ThreadFactory
    public final ExecutorImpl buildExecutorOnNewThread(String str) {
        HandlerThread handlerThread = new HandlerThread(str);
        handlerThread.start();
        return new ExecutorImpl(handlerThread.getLooper());
    }

    @Override // com.android.systemui.util.concurrency.ThreadFactory
    public final Handler buildHandlerOnNewThread() {
        return new Handler(buildLooperOnNewThread("ScreenDecorations"));
    }

    @Override // com.android.systemui.util.concurrency.ThreadFactory
    public final Looper buildLooperOnNewThread(String str) {
        HandlerThread handlerThread = new HandlerThread(str);
        handlerThread.start();
        return handlerThread.getLooper();
    }

    @Override // com.android.systemui.util.concurrency.ThreadFactory
    public final ExecutorImpl buildDelayableExecutorOnHandler(Handler handler) {
        return new ExecutorImpl(handler.getLooper());
    }
}
