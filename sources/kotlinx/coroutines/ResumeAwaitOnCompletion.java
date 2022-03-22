package kotlinx.coroutines;

import kotlin.Result;
import kotlin.Unit;
import kotlinx.coroutines.JobSupport;
/* compiled from: JobSupport.kt */
/* loaded from: classes.dex */
public final class ResumeAwaitOnCompletion<T> extends JobNode {
    public final CancellableContinuationImpl<T> continuation;

    @Override // kotlin.jvm.functions.Function1
    public final /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
        invoke2(th);
        return Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.CompletionHandlerBase
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(Throwable th) {
        Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getJob().getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        boolean z = DebugKt.DEBUG;
        if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof CompletedExceptionally) {
            this.continuation.resumeWith(new Result.Failure(((CompletedExceptionally) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).cause));
        } else {
            this.continuation.resumeWith(JobSupportKt.unboxState(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines));
        }
    }

    public ResumeAwaitOnCompletion(JobSupport.AwaitContinuation awaitContinuation) {
        this.continuation = awaitContinuation;
    }
}
