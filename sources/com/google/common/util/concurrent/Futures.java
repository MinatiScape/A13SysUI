package com.google.common.util.concurrent;

import com.google.android.systemui.assist.uihints.TranscriptionController$$ExternalSyntheticLambda0;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractTransformFuture;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class Futures {
    public static AbstractTransformFuture.TransformFuture transform(ListenableFuture listenableFuture, TranscriptionController$$ExternalSyntheticLambda0 transcriptionController$$ExternalSyntheticLambda0) {
        Executor executor = DirectExecutor.INSTANCE;
        int i = AbstractTransformFuture.$r8$clinit;
        AbstractTransformFuture.TransformFuture transformFuture = new AbstractTransformFuture.TransformFuture(listenableFuture, transcriptionController$$ExternalSyntheticLambda0);
        listenableFuture.addListener(transformFuture, executor);
        return transformFuture;
    }

    @CanIgnoreReturnValue
    public static Object getDone(ListenableFuture listenableFuture) throws ExecutionException {
        V v;
        Preconditions.checkState(listenableFuture.isDone(), "Future was expected to be done: %s", listenableFuture);
        boolean z = false;
        while (true) {
            try {
                v = listenableFuture.get();
                break;
            } catch (InterruptedException unused) {
                z = true;
            } catch (Throwable th) {
                if (z) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (z) {
            Thread.currentThread().interrupt();
        }
        return v;
    }
}
