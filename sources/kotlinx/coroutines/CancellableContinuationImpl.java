package kotlinx.coroutines;

import androidx.lifecycle.runtime.R$id;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicInt;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.internal.DispatchedContinuation;
import kotlinx.coroutines.internal.DispatchedContinuationKt;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.Symbol;
/* compiled from: CancellableContinuationImpl.kt */
/* loaded from: classes.dex */
public class CancellableContinuationImpl<T> extends DispatchedTask<T> implements CancellableContinuation<T>, CoroutineStackFrame {
    public final AtomicInt _decision = new AtomicInt();
    public final AtomicRef<Object> _state = new AtomicRef<>(Active.INSTANCE);
    public final CoroutineContext context;
    public final Continuation<T> delegate;
    public DisposableHandle parentHandle;

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public final StackTraceElement getStackTraceElement() {
        return null;
    }

    public String nameString() {
        return "CancellableContinuation";
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public final Symbol tryResume(Object obj, Object obj2) {
        return tryResumeImpl(obj, obj2, null);
    }

    public static void multipleHandlersError(Function1 function1, Object obj) {
        throw new IllegalStateException(("It's prohibited to register multiple handlers, tried to register " + function1 + ", already has " + obj).toString());
    }

    public static Object resumedState(NotCompleted notCompleted, Object obj, int i, Function1 function1, Object obj2) {
        CancelHandler cancelHandler;
        if (obj instanceof CompletedExceptionally) {
            boolean z = DebugKt.DEBUG;
            return obj;
        }
        boolean z2 = true;
        if (!(i == 1 || i == 2)) {
            z2 = false;
        }
        if (!z2 && obj2 == null) {
            return obj;
        }
        if (function1 == null && ((!(notCompleted instanceof CancelHandler) || (notCompleted instanceof BeforeResumeCancelHandler)) && obj2 == null)) {
            return obj;
        }
        if (notCompleted instanceof CancelHandler) {
            cancelHandler = (CancelHandler) notCompleted;
        } else {
            cancelHandler = null;
        }
        return new CompletedContinuation(obj, cancelHandler, function1, obj2, null, 16);
    }

    public final boolean cancel(Throwable th) {
        Object obj;
        boolean z;
        CancelHandler cancelHandler;
        AtomicRef<Object> atomicRef = this._state;
        do {
            Objects.requireNonNull(atomicRef);
            obj = atomicRef.value;
            if (!(obj instanceof NotCompleted)) {
                return false;
            }
            z = obj instanceof CancelHandler;
        } while (!this._state.compareAndSet(obj, new CancelledContinuation(this, th, z)));
        if (z) {
            cancelHandler = (CancelHandler) obj;
        } else {
            cancelHandler = null;
        }
        if (cancelHandler != null) {
            try {
                cancelHandler.invoke(th);
            } catch (Throwable th2) {
                CoroutineExceptionHandlerKt.handleCoroutineException(this.context, new CompletionHandlerException(Intrinsics.stringPlus("Exception in invokeOnCancellation handler for ", this), th2));
            }
        }
        if (!isReusable()) {
            detachChild$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        }
        dispatchResume(this.resumeMode);
        return true;
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public final void cancelCompletedResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(Object obj, CancellationException cancellationException) {
        boolean z;
        AtomicRef<Object> atomicRef = this._state;
        while (true) {
            Objects.requireNonNull(atomicRef);
            Object obj2 = atomicRef.value;
            if (obj2 instanceof NotCompleted) {
                throw new IllegalStateException("Not completed".toString());
            } else if (!(obj2 instanceof CompletedExceptionally)) {
                if (obj2 instanceof CompletedContinuation) {
                    CompletedContinuation completedContinuation = (CompletedContinuation) obj2;
                    Objects.requireNonNull(completedContinuation);
                    if (completedContinuation.cancelCause != null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (!z) {
                        if (this._state.compareAndSet(obj2, CompletedContinuation.copy$default(completedContinuation, null, cancellationException, 15))) {
                            CancelHandler cancelHandler = completedContinuation.cancelHandler;
                            if (cancelHandler != null) {
                                try {
                                    cancelHandler.invoke((Throwable) cancellationException);
                                } catch (Throwable th) {
                                    CoroutineExceptionHandlerKt.handleCoroutineException(this.context, new CompletionHandlerException(Intrinsics.stringPlus("Exception in invokeOnCancellation handler for ", this), th));
                                }
                            }
                            Function1<Throwable, Unit> function1 = completedContinuation.onCancellation;
                            if (function1 != null) {
                                try {
                                    function1.invoke(cancellationException);
                                    return;
                                } catch (Throwable th2) {
                                    CoroutineExceptionHandlerKt.handleCoroutineException(this.context, new CompletionHandlerException(Intrinsics.stringPlus("Exception in resume onCancellation handler for ", this), th2));
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                    } else {
                        throw new IllegalStateException("Must be called at most once".toString());
                    }
                } else if (this._state.compareAndSet(obj2, new CompletedContinuation(obj2, null, null, null, cancellationException, 14))) {
                    return;
                }
            } else {
                return;
            }
        }
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public final void completeResume() {
        boolean z = DebugKt.DEBUG;
        dispatchResume(this.resumeMode);
    }

    public final void detachChild$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        DisposableHandle disposableHandle = this.parentHandle;
        if (disposableHandle != null) {
            disposableHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
    }

    /* JADX WARN: Finally extract failed */
    public final void dispatchResume(int i) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        AtomicInt atomicInt = this._decision;
        while (true) {
            Objects.requireNonNull(atomicInt);
            int i2 = atomicInt.value;
            z = false;
            if (i2 == 0) {
                if (this._decision.compareAndSet(0, 2)) {
                    z2 = true;
                    break;
                }
            } else if (i2 == 1) {
                z2 = false;
            } else {
                throw new IllegalStateException("Already resumed".toString());
            }
        }
        if (!z2) {
            boolean z5 = DebugKt.DEBUG;
            Continuation<T> continuation = this.delegate;
            if (i == 4) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (!z3 && (continuation instanceof DispatchedContinuation)) {
                if (i == 1 || i == 2) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                int i3 = this.resumeMode;
                if (i3 == 1 || i3 == 2) {
                    z = true;
                }
                if (z4 == z) {
                    CoroutineDispatcher coroutineDispatcher = ((DispatchedContinuation) continuation).dispatcher;
                    CoroutineContext context = continuation.getContext();
                    if (coroutineDispatcher.isDispatchNeeded()) {
                        coroutineDispatcher.dispatch(context, this);
                        return;
                    }
                    EventLoop eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines = ThreadLocalEventLoop.getEventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
                    if (eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.isUnconfinedLoopActive()) {
                        eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.dispatchUnconfined(this);
                        return;
                    }
                    eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.incrementUseCount(true);
                    try {
                        DispatchedTaskKt.resume(this, this.delegate, true);
                        do {
                        } while (eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.processUnconfinedEvent());
                    } catch (Throwable th) {
                        try {
                            handleFatalException(th, null);
                        } finally {
                            eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.decrementUseCount(true);
                        }
                    }
                    return;
                }
            }
            DispatchedTaskKt.resume(this, continuation, z3);
        }
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public final CoroutineStackFrame getCallerFrame() {
        Continuation<T> continuation = this.delegate;
        if (continuation instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) continuation;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.DispatchedTask
    public final <T> T getSuccessfulResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(Object obj) {
        if (obj instanceof CompletedContinuation) {
            return (T) ((CompletedContinuation) obj).result;
        }
        return obj;
    }

    public final DisposableHandle installParentHandle() {
        Job job = (Job) this.context.get(Job.Key.$$INSTANCE);
        if (job == null) {
            return null;
        }
        DisposableHandle invokeOnCompletion$default = Job.DefaultImpls.invokeOnCompletion$default(job, true, new ChildContinuation(this), 2);
        this.parentHandle = invokeOnCompletion$default;
        return invokeOnCompletion$default;
    }

    public final void invokeOnCancellation(Function1<? super Throwable, Unit> function1) {
        CancelHandler cancelHandler;
        boolean z;
        if (function1 instanceof CancelHandler) {
            cancelHandler = (CancelHandler) function1;
        } else {
            cancelHandler = new InvokeOnCancel(function1);
        }
        AtomicRef<Object> atomicRef = this._state;
        while (true) {
            Objects.requireNonNull(atomicRef);
            Object obj = atomicRef.value;
            if (!(obj instanceof Active)) {
                Throwable th = null;
                if (!(obj instanceof CancelHandler)) {
                    boolean z2 = obj instanceof CompletedExceptionally;
                    if (z2) {
                        CompletedExceptionally completedExceptionally = (CompletedExceptionally) obj;
                        Objects.requireNonNull(completedExceptionally);
                        if (!completedExceptionally._handled.compareAndSet()) {
                            multipleHandlersError(function1, obj);
                            throw null;
                        } else if (obj instanceof CancelledContinuation) {
                            if (!z2) {
                                completedExceptionally = null;
                            }
                            if (completedExceptionally != null) {
                                th = completedExceptionally.cause;
                            }
                            try {
                                function1.invoke(th);
                                return;
                            } catch (Throwable th2) {
                                CoroutineExceptionHandlerKt.handleCoroutineException(this.context, new CompletionHandlerException(Intrinsics.stringPlus("Exception in invokeOnCancellation handler for ", this), th2));
                                return;
                            }
                        } else {
                            return;
                        }
                    } else if (obj instanceof CompletedContinuation) {
                        CompletedContinuation completedContinuation = (CompletedContinuation) obj;
                        if (completedContinuation.cancelHandler != null) {
                            multipleHandlersError(function1, obj);
                            throw null;
                        } else if (!(cancelHandler instanceof BeforeResumeCancelHandler)) {
                            Throwable th3 = completedContinuation.cancelCause;
                            if (th3 != null) {
                                z = true;
                            } else {
                                z = false;
                            }
                            if (z) {
                                try {
                                    function1.invoke(th3);
                                    return;
                                } catch (Throwable th4) {
                                    CoroutineExceptionHandlerKt.handleCoroutineException(this.context, new CompletionHandlerException(Intrinsics.stringPlus("Exception in invokeOnCancellation handler for ", this), th4));
                                    return;
                                }
                            } else {
                                if (this._state.compareAndSet(obj, CompletedContinuation.copy$default(completedContinuation, cancelHandler, null, 29))) {
                                    return;
                                }
                            }
                        } else {
                            return;
                        }
                    } else if (!(cancelHandler instanceof BeforeResumeCancelHandler)) {
                        if (this._state.compareAndSet(obj, new CompletedContinuation(obj, cancelHandler, null, null, null, 28))) {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    multipleHandlersError(function1, obj);
                    throw null;
                }
            } else if (this._state.compareAndSet(obj, cancelHandler)) {
                return;
            }
        }
    }

    public final boolean isReusable() {
        boolean z;
        boolean z2;
        if (this.resumeMode == 2) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) this.delegate;
            Objects.requireNonNull(dispatchedContinuation);
            AtomicRef<Object> atomicRef = dispatchedContinuation._reusableCancellableContinuation;
            Objects.requireNonNull(atomicRef);
            if (atomicRef.value != null) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                return true;
            }
        }
        return false;
    }

    public final void releaseClaimedReusableContinuation() {
        DispatchedContinuation dispatchedContinuation;
        Continuation<T> continuation = this.delegate;
        Throwable th = null;
        if (continuation instanceof DispatchedContinuation) {
            dispatchedContinuation = (DispatchedContinuation) continuation;
        } else {
            dispatchedContinuation = null;
        }
        if (dispatchedContinuation != null) {
            AtomicRef<Object> atomicRef = dispatchedContinuation._reusableCancellableContinuation;
            while (true) {
                Objects.requireNonNull(atomicRef);
                Object obj = atomicRef.value;
                Symbol symbol = DispatchedContinuationKt.REUSABLE_CLAIMED;
                if (obj == symbol) {
                    if (dispatchedContinuation._reusableCancellableContinuation.compareAndSet(symbol, this)) {
                        break;
                    }
                } else if (!(obj instanceof Throwable)) {
                    throw new IllegalStateException(Intrinsics.stringPlus("Inconsistent state ", obj).toString());
                } else if (dispatchedContinuation._reusableCancellableContinuation.compareAndSet(obj, null)) {
                    th = (Throwable) obj;
                } else {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
            }
        }
        if (th != null) {
            detachChild$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
            cancel(th);
        }
    }

    public final void resumeImpl(Object obj, int i, Function1<? super Throwable, Unit> function1) {
        Object obj2;
        AtomicRef<Object> atomicRef = this._state;
        do {
            Objects.requireNonNull(atomicRef);
            obj2 = atomicRef.value;
            if (obj2 instanceof NotCompleted) {
            } else {
                if (obj2 instanceof CancelledContinuation) {
                    CancelledContinuation cancelledContinuation = (CancelledContinuation) obj2;
                    Objects.requireNonNull(cancelledContinuation);
                    if (cancelledContinuation._resumed.compareAndSet()) {
                        if (function1 != null) {
                            try {
                                function1.invoke(cancelledContinuation.cause);
                                return;
                            } catch (Throwable th) {
                                CoroutineExceptionHandlerKt.handleCoroutineException(this.context, new CompletionHandlerException(Intrinsics.stringPlus("Exception in resume onCancellation handler for ", this), th));
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                }
                throw new IllegalStateException(Intrinsics.stringPlus("Already resumed, but proposed with update ", obj).toString());
            }
        } while (!this._state.compareAndSet(obj2, resumedState((NotCompleted) obj2, obj, i, function1, null)));
        if (!isReusable()) {
            detachChild$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        }
        dispatchResume(i);
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public final Object takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        AtomicRef<Object> atomicRef = this._state;
        Objects.requireNonNull(atomicRef);
        return atomicRef.value;
    }

    public final String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append(nameString());
        sb.append('(');
        sb.append(DebugStringsKt.toDebugString(this.delegate));
        sb.append("){");
        AtomicRef<Object> atomicRef = this._state;
        Objects.requireNonNull(atomicRef);
        Object obj = atomicRef.value;
        if (obj instanceof NotCompleted) {
            str = "Active";
        } else if (obj instanceof CancelledContinuation) {
            str = "Cancelled";
        } else {
            str = "Completed";
        }
        sb.append(str);
        sb.append("}@");
        sb.append(DebugStringsKt.getHexAddress(this));
        return sb.toString();
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public final Symbol tryResume(Object obj, Function1 function1) {
        return tryResumeImpl(obj, null, function1);
    }

    public final Symbol tryResumeImpl(Object obj, Object obj2, Function1<? super Throwable, Unit> function1) {
        Object obj3;
        AtomicRef<Object> atomicRef = this._state;
        do {
            Objects.requireNonNull(atomicRef);
            obj3 = atomicRef.value;
            if (obj3 instanceof NotCompleted) {
            } else if (!(obj3 instanceof CompletedContinuation) || obj2 == null || ((CompletedContinuation) obj3).idempotentResume != obj2) {
                return null;
            } else {
                boolean z = DebugKt.DEBUG;
                return R$id.RESUME_TOKEN;
            }
        } while (!this._state.compareAndSet(obj3, resumedState((NotCompleted) obj3, obj, this.resumeMode, function1, obj2)));
        if (!isReusable()) {
            detachChild$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        }
        return R$id.RESUME_TOKEN;
    }

    @Override // kotlinx.coroutines.CancellableContinuation
    public final Symbol tryResumeWithException(Throwable th) {
        return tryResumeImpl(new CompletedExceptionally(th, false), null, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CancellableContinuationImpl(Continuation<? super T> continuation, int i) {
        super(i);
        this.delegate = continuation;
        boolean z = DebugKt.DEBUG;
        this.context = continuation.getContext();
    }

    public Throwable getContinuationCancellationCause(JobSupport jobSupport) {
        return jobSupport.getCancellationException();
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public final Throwable getExceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(Object obj) {
        Throwable exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines = super.getExceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(obj);
        if (exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines == null) {
            return null;
        }
        Continuation<T> continuation = this.delegate;
        if (DebugKt.RECOVER_STACK_TRACES && (continuation instanceof CoroutineStackFrame)) {
            exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines = StackTraceRecoveryKt.access$recoverFromStackFrame(exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines, (CoroutineStackFrame) continuation);
        }
        return exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines;
    }

    public final Object getResult() {
        boolean z;
        boolean z2;
        Job job;
        boolean isReusable = isReusable();
        AtomicInt atomicInt = this._decision;
        while (true) {
            Objects.requireNonNull(atomicInt);
            int i = atomicInt.value;
            z = true;
            if (i == 0) {
                if (this._decision.compareAndSet(0, 1)) {
                    z2 = true;
                    break;
                }
            } else if (i == 2) {
                z2 = false;
            } else {
                throw new IllegalStateException("Already suspended".toString());
            }
        }
        if (z2) {
            if (this.parentHandle == null) {
                installParentHandle();
            }
            if (isReusable) {
                releaseClaimedReusableContinuation();
            }
            return CoroutineSingletons.COROUTINE_SUSPENDED;
        }
        if (isReusable) {
            releaseClaimedReusableContinuation();
        }
        AtomicRef<Object> atomicRef = this._state;
        Objects.requireNonNull(atomicRef);
        Object obj = atomicRef.value;
        if (obj instanceof CompletedExceptionally) {
            Throwable th = ((CompletedExceptionally) obj).cause;
            if (!DebugKt.RECOVER_STACK_TRACES) {
                throw th;
            }
            throw StackTraceRecoveryKt.access$recoverFromStackFrame(th, this);
        }
        int i2 = this.resumeMode;
        if (!(i2 == 1 || i2 == 2)) {
            z = false;
        }
        if (!z || (job = (Job) this.context.get(Job.Key.$$INSTANCE)) == null || job.isActive()) {
            return getSuccessfulResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(obj);
        }
        CancellationException cancellationException = job.getCancellationException();
        cancelCompletedResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(obj, cancellationException);
        if (!DebugKt.RECOVER_STACK_TRACES) {
            throw cancellationException;
        }
        throw StackTraceRecoveryKt.access$recoverFromStackFrame(cancellationException, this);
    }

    public final void initCancellability() {
        DisposableHandle installParentHandle = installParentHandle();
        if (installParentHandle != null) {
            AtomicRef<Object> atomicRef = this._state;
            Objects.requireNonNull(atomicRef);
            if (!(atomicRef.value instanceof NotCompleted)) {
                installParentHandle.dispose();
                this.parentHandle = NonDisposableHandle.INSTANCE;
            }
        }
    }

    @Override // kotlin.coroutines.Continuation
    public final void resumeWith(Object obj) {
        Throwable th = Result.m181exceptionOrNullimpl(obj);
        if (th != null) {
            if (DebugKt.RECOVER_STACK_TRACES) {
                th = StackTraceRecoveryKt.access$recoverFromStackFrame(th, this);
            }
            obj = new CompletedExceptionally(th, false);
        }
        resumeImpl(obj, this.resumeMode, null);
    }

    @Override // kotlin.coroutines.Continuation
    public final CoroutineContext getContext() {
        return this.context;
    }

    @Override // kotlinx.coroutines.DispatchedTask
    public final Continuation<T> getDelegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return this.delegate;
    }
}
