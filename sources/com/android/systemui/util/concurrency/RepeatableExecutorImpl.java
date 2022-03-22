package com.android.systemui.util.concurrency;

import com.android.systemui.media.SeekBarViewModel$checkIfPollingNeeded$1;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda17;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class RepeatableExecutorImpl implements RepeatableExecutor {
    public final DelayableExecutor mExecutor;

    /* loaded from: classes.dex */
    public class ExecutionToken implements Runnable {
        public Runnable mCancel;
        public final Runnable mCommand;
        public final TimeUnit mUnit;
        public final /* synthetic */ RepeatableExecutorImpl this$0;
        public final Object mLock = new Object();
        public final long mDelay = 100;

        public ExecutionToken(RepeatableExecutorImpl repeatableExecutorImpl, SeekBarViewModel$checkIfPollingNeeded$1 seekBarViewModel$checkIfPollingNeeded$1) {
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            this.this$0 = repeatableExecutorImpl;
            this.mCommand = seekBarViewModel$checkIfPollingNeeded$1;
            this.mUnit = timeUnit;
        }

        @Override // java.lang.Runnable
        public final void run() {
            this.mCommand.run();
            synchronized (this.mLock) {
                if (this.mCancel != null) {
                    this.mCancel = this.this$0.mExecutor.executeDelayed(this, this.mDelay, this.mUnit);
                }
            }
        }
    }

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        this.mExecutor.execute(runnable);
    }

    @Override // com.android.systemui.util.concurrency.RepeatableExecutor
    public final BubbleStackView$$ExternalSyntheticLambda17 executeRepeatedly(SeekBarViewModel$checkIfPollingNeeded$1 seekBarViewModel$checkIfPollingNeeded$1) {
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        ExecutionToken executionToken = new ExecutionToken(this, seekBarViewModel$checkIfPollingNeeded$1);
        synchronized (executionToken.mLock) {
            executionToken.mCancel = this.mExecutor.executeDelayed(executionToken, 0L, timeUnit);
        }
        return new BubbleStackView$$ExternalSyntheticLambda17(executionToken, 8);
    }

    public RepeatableExecutorImpl(DelayableExecutor delayableExecutor) {
        this.mExecutor = delayableExecutor;
    }
}
