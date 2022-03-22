package com.android.wm.shell.common;

import com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda11;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController$$ExternalSyntheticLambda1;
import java.lang.reflect.Array;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public interface ShellExecutor extends Executor {
    @Override // java.util.concurrent.Executor
    void execute(Runnable runnable);

    void executeDelayed(Runnable runnable, long j);

    boolean hasCallback(SuggestController$$ExternalSyntheticLambda1 suggestController$$ExternalSyntheticLambda1);

    void removeCallbacks(Runnable runnable);

    default void executeBlocking(Runnable runnable) throws InterruptedException {
        TimeUnit timeUnit = TimeUnit.SECONDS;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        execute(new ShellExecutor$$ExternalSyntheticLambda0(runnable, countDownLatch, 0));
        countDownLatch.await(2, timeUnit);
    }

    default void executeBlocking$1(Runnable runnable) throws InterruptedException {
        TimeUnit timeUnit = TimeUnit.SECONDS;
        executeBlocking(runnable);
    }

    default Object executeBlockingForResult(BubbleController$BubblesImpl$$ExternalSyntheticLambda11 bubbleController$BubblesImpl$$ExternalSyntheticLambda11) {
        Object[] objArr = (Object[]) Array.newInstance(Boolean.class, 1);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        execute(new ShellExecutor$$ExternalSyntheticLambda1(objArr, bubbleController$BubblesImpl$$ExternalSyntheticLambda11, countDownLatch, 0));
        try {
            countDownLatch.await();
            return objArr[0];
        } catch (InterruptedException unused) {
            return null;
        }
    }
}
