package kotlinx.coroutines;

import androidx.preference.R$color;
import java.util.Objects;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.internal.DispatchedContinuationKt;
import kotlinx.coroutines.internal.ThreadContextKt;
/* compiled from: AbstractCoroutine.kt */
/* loaded from: classes.dex */
public abstract class AbstractCoroutine<T> extends JobSupport implements Continuation<T>, CoroutineScope {
    public final CoroutineContext context;

    @Override // kotlinx.coroutines.JobSupport
    public final void handleOnCompletionException$external__kotlinx_coroutines__android_common__kotlinx_coroutines(CompletionHandlerException completionHandlerException) {
        CoroutineExceptionHandlerKt.handleCoroutineException(this.context, completionHandlerException);
    }

    @Override // kotlinx.coroutines.JobSupport
    public final String nameString$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        CoroutineId coroutineId;
        CoroutineContext coroutineContext = this.context;
        boolean z = CoroutineContextKt.useCoroutinesScheduler;
        String str = null;
        if (DebugKt.DEBUG && (coroutineId = (CoroutineId) coroutineContext.get(CoroutineId.Key)) != null) {
            CoroutineName coroutineName = (CoroutineName) coroutineContext.get(CoroutineName.Key);
            str = "coroutine#" + coroutineId.id;
        }
        if (str == null) {
            return DebugStringsKt.getClassSimpleName(this);
        }
        return '\"' + str + "\":" + DebugStringsKt.getClassSimpleName(this);
    }

    @Override // kotlinx.coroutines.JobSupport
    public final void onCompletionInternal(Object obj) {
        if (obj instanceof CompletedExceptionally) {
            CompletedExceptionally completedExceptionally = (CompletedExceptionally) obj;
            Throwable th = completedExceptionally.cause;
            Objects.requireNonNull(completedExceptionally._handled);
        }
    }

    public AbstractCoroutine(CoroutineContext coroutineContext, boolean z) {
        super(z);
        initParentJob((Job) coroutineContext.get(Job.Key.$$INSTANCE));
        this.context = coroutineContext.plus(this);
    }

    @Override // kotlinx.coroutines.JobSupport
    public final String cancellationExceptionMessage() {
        return Intrinsics.stringPlus(DebugStringsKt.getClassSimpleName(this), " was cancelled");
    }

    @Override // kotlinx.coroutines.JobSupport, kotlinx.coroutines.Job
    public final boolean isActive() {
        return super.isActive();
    }

    @Override // kotlin.coroutines.Continuation
    public final void resumeWith(Object obj) {
        Throwable th = Result.m181exceptionOrNullimpl(obj);
        if (th != null) {
            obj = new CompletedExceptionally(th, false);
        }
        Object makeCompletingOnce$external__kotlinx_coroutines__android_common__kotlinx_coroutines = makeCompletingOnce$external__kotlinx_coroutines__android_common__kotlinx_coroutines(obj);
        if (makeCompletingOnce$external__kotlinx_coroutines__android_common__kotlinx_coroutines != JobSupportKt.COMPLETING_WAITING_CHILDREN) {
            afterResume(makeCompletingOnce$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
        }
    }

    public final void start(CoroutineStart coroutineStart, AbstractCoroutine abstractCoroutine, Function2 function2) {
        int ordinal = coroutineStart.ordinal();
        if (ordinal == 0) {
            try {
                DispatchedContinuationKt.resumeCancellableWith(R$color.intercepted(R$color.createCoroutineUnintercepted(function2, abstractCoroutine, this)), Unit.INSTANCE, null);
            } finally {
                resumeWith(new Result.Failure(th));
            }
        } else if (ordinal == 1) {
        } else {
            if (ordinal == 2) {
                R$color.intercepted(R$color.createCoroutineUnintercepted(function2, abstractCoroutine, this)).resumeWith(Unit.INSTANCE);
            } else if (ordinal == 3) {
                try {
                    CoroutineContext coroutineContext = this.context;
                    Object updateThreadContext = ThreadContextKt.updateThreadContext(coroutineContext, null);
                    TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2);
                    Object invoke = function2.invoke(abstractCoroutine, this);
                    ThreadContextKt.restoreThreadContext(coroutineContext, updateThreadContext);
                    if (invoke != CoroutineSingletons.COROUTINE_SUSPENDED) {
                        resumeWith(invoke);
                    }
                } catch (Throwable th) {
                }
            } else {
                throw new NoWhenBranchMatchedException();
            }
        }
    }

    public void afterResume(Object obj) {
        afterCompletion(obj);
    }

    @Override // kotlin.coroutines.Continuation
    public final CoroutineContext getContext() {
        return this.context;
    }

    @Override // kotlinx.coroutines.CoroutineScope
    public final CoroutineContext getCoroutineContext() {
        return this.context;
    }
}
