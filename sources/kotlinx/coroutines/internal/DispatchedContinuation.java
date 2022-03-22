package kotlinx.coroutines.internal;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.util.concurrent.CancellationException;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.CompletedExceptionally;
import kotlinx.coroutines.CompletedWithCancellation;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.DispatchedTask;
import kotlinx.coroutines.EventLoop;
import kotlinx.coroutines.ThreadLocalEventLoop;
/* compiled from: DispatchedContinuation.kt */
/* loaded from: classes.dex */
public final class DispatchedContinuation<T> extends DispatchedTask<T> implements CoroutineStackFrame, Continuation<T> {
    public final Continuation<T> continuation;
    public final Object countOrElement;
    public final CoroutineDispatcher dispatcher;
    public Object _state = DispatchedContinuationKt.UNDEFINED;
    public final AtomicRef<Object> _reusableCancellableContinuation = new AtomicRef<>(null);

    public DispatchedContinuation(CoroutineDispatcher coroutineDispatcher, ContinuationImpl continuationImpl) {
        super(-1);
        this.dispatcher = coroutineDispatcher;
        this.continuation = continuationImpl;
        Object fold = getContext().fold(0, ThreadContextKt.countAll);
        Intrinsics.checkNotNull(fold);
        this.countOrElement = fold;
    }

    @Override // kotlin.coroutines.Continuation
    public final CoroutineContext getContext() {
        return this.continuation.getContext();
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public final Continuation<T> getDelegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return this;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public final StackTraceElement getStackTraceElement() {
        return null;
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public final void cancelCompletedResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(Object obj, CancellationException cancellationException) {
        if (obj instanceof CompletedWithCancellation) {
            ((CompletedWithCancellation) obj).onCancellation.invoke(cancellationException);
        }
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public final CoroutineStackFrame getCallerFrame() {
        Continuation<T> continuation = this.continuation;
        if (continuation instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) continuation;
        }
        return null;
    }

    @Override // kotlin.coroutines.Continuation
    public final void resumeWith(Object obj) {
        Object obj2;
        CoroutineContext context = this.continuation.getContext();
        Throwable th = Result.m181exceptionOrNullimpl(obj);
        if (th == null) {
            obj2 = obj;
        } else {
            obj2 = new CompletedExceptionally(th, false);
        }
        if (this.dispatcher.isDispatchNeeded()) {
            this._state = obj2;
            this.resumeMode = 0;
            this.dispatcher.dispatch(context, this);
            return;
        }
        boolean z = DebugKt.DEBUG;
        EventLoop eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines = ThreadLocalEventLoop.getEventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        if (eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.isUnconfinedLoopActive()) {
            this._state = obj2;
            this.resumeMode = 0;
            eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.dispatchUnconfined(this);
            return;
        }
        eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.incrementUseCount(true);
        try {
            CoroutineContext context2 = getContext();
            Object updateThreadContext = ThreadContextKt.updateThreadContext(context2, this.countOrElement);
            this.continuation.resumeWith(obj);
            ThreadContextKt.restoreThreadContext(context2, updateThreadContext);
            do {
            } while (eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.processUnconfinedEvent());
        } finally {
            try {
            } finally {
            }
        }
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public final Object takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        Object obj = this._state;
        boolean z = DebugKt.DEBUG;
        this._state = DispatchedContinuationKt.UNDEFINED;
        return obj;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("DispatchedContinuation[");
        m.append(this.dispatcher);
        m.append(", ");
        m.append(DebugStringsKt.toDebugString(this.continuation));
        m.append(']');
        return m.toString();
    }
}
