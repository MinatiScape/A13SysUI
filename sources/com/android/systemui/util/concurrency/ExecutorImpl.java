package com.android.systemui.util.concurrency;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.android.keyguard.clock.ClockManager$$ExternalSyntheticLambda1;
import com.android.systemui.util.concurrency.ExecutorImpl;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class ExecutorImpl implements DelayableExecutor {
    public final Handler mHandler;

    /* loaded from: classes.dex */
    public class ExecutionToken implements Runnable {
        public final Runnable runnable;

        public ExecutionToken(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override // java.lang.Runnable
        public final void run() {
            ExecutorImpl.this.mHandler.removeCallbacksAndMessages(this);
        }
    }

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        if (!this.mHandler.post(runnable)) {
            throw new RejectedExecutionException(this.mHandler + " is shutting down");
        }
    }

    @Override // com.android.systemui.util.concurrency.DelayableExecutor
    public final ExecutionToken executeAtTime(ClockManager$$ExternalSyntheticLambda1 clockManager$$ExternalSyntheticLambda1, long j) {
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        ExecutionToken executionToken = new ExecutionToken(clockManager$$ExternalSyntheticLambda1);
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(0, executionToken), timeUnit.toMillis(j));
        return executionToken;
    }

    @Override // com.android.systemui.util.concurrency.DelayableExecutor
    public final ExecutionToken executeDelayed(Runnable runnable, long j, TimeUnit timeUnit) {
        ExecutionToken executionToken = new ExecutionToken(runnable);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, executionToken), timeUnit.toMillis(j));
        return executionToken;
    }

    public ExecutorImpl(Looper looper) {
        this.mHandler = new Handler(looper, new Handler.Callback() { // from class: com.android.systemui.util.concurrency.ExecutorImpl$$ExternalSyntheticLambda0
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(Message message) {
                Objects.requireNonNull(ExecutorImpl.this);
                if (message.what == 0) {
                    ((ExecutorImpl.ExecutionToken) message.obj).runnable.run();
                    return true;
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unrecognized message: ");
                m.append(message.what);
                throw new IllegalStateException(m.toString());
            }
        });
    }
}
