package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import kotlin.ExceptionsKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.scheduling.Task;
/* compiled from: DispatchedTask.kt */
/* loaded from: classes.dex */
public abstract class DispatchedTask<T> extends Task {
    public int resumeMode;

    public void cancelCompletedResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(Object obj, CancellationException cancellationException) {
    }

    public abstract Continuation<T> getDelegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines();

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getSuccessfulResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(Object obj) {
        return obj;
    }

    public abstract Object takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();

    public Throwable getExceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(Object obj) {
        CompletedExceptionally completedExceptionally;
        if (obj instanceof CompletedExceptionally) {
            completedExceptionally = (CompletedExceptionally) obj;
        } else {
            completedExceptionally = null;
        }
        if (completedExceptionally == null) {
            return null;
        }
        return completedExceptionally.cause;
    }

    public final void handleFatalException(Throwable th, Throwable th2) {
        if (th != null || th2 != null) {
            if (!(th == null || th2 == null)) {
                ExceptionsKt.addSuppressed(th, th2);
            }
            if (th == null) {
                th = th2;
            }
            Intrinsics.checkNotNull(th);
            CoroutineExceptionHandlerKt.handleCoroutineException(getDelegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines().getContext(), new CoroutinesInternalError("Fatal exception in coroutines machinery for " + this + ". Please read KDoc to 'handleFatalException' method and report this incident to maintainers", th));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x003b, code lost:
        r6 = (kotlinx.coroutines.Job) r6.get(kotlinx.coroutines.Job.Key.$$INSTANCE);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v6, types: [java.lang.Throwable] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void run() {
        /*
            r12 = this;
            boolean r0 = kotlinx.coroutines.DebugKt.DEBUG
            kotlinx.coroutines.scheduling.TaskContext r0 = r12.taskContext
            kotlin.coroutines.Continuation r1 = r12.getDelegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines()     // Catch: all -> 0x00ac
            kotlinx.coroutines.internal.DispatchedContinuation r1 = (kotlinx.coroutines.internal.DispatchedContinuation) r1     // Catch: all -> 0x00ac
            kotlin.coroutines.Continuation<T> r2 = r1.continuation     // Catch: all -> 0x00ac
            java.lang.Object r1 = r1.countOrElement     // Catch: all -> 0x00ac
            kotlin.coroutines.CoroutineContext r3 = r2.getContext()     // Catch: all -> 0x00ac
            java.lang.Object r1 = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(r3, r1)     // Catch: all -> 0x00ac
            kotlinx.coroutines.internal.Symbol r4 = kotlinx.coroutines.internal.ThreadContextKt.NO_THREAD_ELEMENTS     // Catch: all -> 0x00ac
            r5 = 0
            if (r1 == r4) goto L_0x0020
            kotlinx.coroutines.UndispatchedCoroutine r4 = kotlinx.coroutines.CoroutineContextKt.updateUndispatchedCompletion(r2, r3, r1)     // Catch: all -> 0x00ac
            goto L_0x0021
        L_0x0020:
            r4 = r5
        L_0x0021:
            kotlin.coroutines.CoroutineContext r6 = r2.getContext()     // Catch: all -> 0x006d
            java.lang.Object r7 = r12.takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines()     // Catch: all -> 0x006d
            java.lang.Throwable r8 = r12.getExceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(r7)     // Catch: all -> 0x006d
            if (r8 != 0) goto L_0x0044
            int r9 = r12.resumeMode     // Catch: all -> 0x006d
            r10 = 1
            if (r9 == r10) goto L_0x0039
            r11 = 2
            if (r9 != r11) goto L_0x0038
            goto L_0x0039
        L_0x0038:
            r10 = 0
        L_0x0039:
            if (r10 == 0) goto L_0x0044
            kotlinx.coroutines.Job$Key r9 = kotlinx.coroutines.Job.Key.$$INSTANCE     // Catch: all -> 0x006d
            kotlin.coroutines.CoroutineContext$Element r6 = r6.get(r9)     // Catch: all -> 0x006d
            kotlinx.coroutines.Job r6 = (kotlinx.coroutines.Job) r6     // Catch: all -> 0x006d
            goto L_0x0045
        L_0x0044:
            r6 = r5
        L_0x0045:
            if (r6 == 0) goto L_0x006f
            boolean r9 = r6.isActive()     // Catch: all -> 0x006d
            if (r9 != 0) goto L_0x006f
            java.util.concurrent.CancellationException r6 = r6.getCancellationException()     // Catch: all -> 0x006d
            r12.cancelCompletedResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(r7, r6)     // Catch: all -> 0x006d
            boolean r7 = kotlinx.coroutines.DebugKt.RECOVER_STACK_TRACES     // Catch: all -> 0x006d
            if (r7 == 0) goto L_0x0064
            boolean r7 = r2 instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame     // Catch: all -> 0x006d
            if (r7 != 0) goto L_0x005d
            goto L_0x0064
        L_0x005d:
            r7 = r2
            kotlin.coroutines.jvm.internal.CoroutineStackFrame r7 = (kotlin.coroutines.jvm.internal.CoroutineStackFrame) r7     // Catch: all -> 0x006d
            java.lang.Throwable r6 = kotlinx.coroutines.internal.StackTraceRecoveryKt.access$recoverFromStackFrame(r6, r7)     // Catch: all -> 0x006d
        L_0x0064:
            kotlin.Result$Failure r7 = new kotlin.Result$Failure     // Catch: all -> 0x006d
            r7.<init>(r6)     // Catch: all -> 0x006d
            r2.resumeWith(r7)     // Catch: all -> 0x006d
            goto L_0x0081
        L_0x006d:
            r2 = move-exception
            goto L_0x00a0
        L_0x006f:
            if (r8 == 0) goto L_0x007a
            kotlin.Result$Failure r6 = new kotlin.Result$Failure     // Catch: all -> 0x006d
            r6.<init>(r8)     // Catch: all -> 0x006d
            r2.resumeWith(r6)     // Catch: all -> 0x006d
            goto L_0x0081
        L_0x007a:
            java.lang.Object r6 = r12.getSuccessfulResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(r7)     // Catch: all -> 0x006d
            r2.resumeWith(r6)     // Catch: all -> 0x006d
        L_0x0081:
            kotlin.Unit r2 = kotlin.Unit.INSTANCE     // Catch: all -> 0x006d
            if (r4 == 0) goto L_0x008b
            boolean r4 = r4.clearThreadContext()     // Catch: all -> 0x00ac
            if (r4 == 0) goto L_0x008e
        L_0x008b:
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r3, r1)     // Catch: all -> 0x00ac
        L_0x008e:
            r0.afterTask()     // Catch: all -> 0x0092
            goto L_0x0098
        L_0x0092:
            r0 = move-exception
            kotlin.Result$Failure r2 = new kotlin.Result$Failure
            r2.<init>(r0)
        L_0x0098:
            java.lang.Throwable r0 = kotlin.Result.m181exceptionOrNullimpl(r2)
            r12.handleFatalException(r5, r0)
            goto L_0x00c1
        L_0x00a0:
            if (r4 == 0) goto L_0x00a8
            boolean r4 = r4.clearThreadContext()     // Catch: all -> 0x00ac
            if (r4 == 0) goto L_0x00ab
        L_0x00a8:
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r3, r1)     // Catch: all -> 0x00ac
        L_0x00ab:
            throw r2     // Catch: all -> 0x00ac
        L_0x00ac:
            r1 = move-exception
            r0.afterTask()     // Catch: all -> 0x00b3
            kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch: all -> 0x00b3
            goto L_0x00ba
        L_0x00b3:
            r0 = move-exception
            kotlin.Result$Failure r2 = new kotlin.Result$Failure
            r2.<init>(r0)
            r0 = r2
        L_0x00ba:
            java.lang.Throwable r0 = kotlin.Result.m181exceptionOrNullimpl(r0)
            r12.handleFatalException(r1, r0)
        L_0x00c1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.DispatchedTask.run():void");
    }

    public DispatchedTask(int i) {
        this.resumeMode = i;
    }
}
