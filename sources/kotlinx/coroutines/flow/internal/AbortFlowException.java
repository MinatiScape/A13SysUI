package kotlinx.coroutines.flow.internal;

import java.util.concurrent.CancellationException;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.FlowKt__ReduceKt$first$$inlined$collectWhile$2;
/* compiled from: FlowExceptions.kt */
/* loaded from: classes.dex */
public final class AbortFlowException extends CancellationException {
    private final FlowCollector<?> owner;

    public AbortFlowException(FlowKt__ReduceKt$first$$inlined$collectWhile$2 flowKt__ReduceKt$first$$inlined$collectWhile$2) {
        super("Flow was aborted, no more elements needed");
        this.owner = flowKt__ReduceKt$first$$inlined$collectWhile$2;
    }

    @Override // java.lang.Throwable
    public final Throwable fillInStackTrace() {
        if (DebugKt.DEBUG) {
            return super.fillInStackTrace();
        }
        setStackTrace(new StackTraceElement[0]);
        return this;
    }

    public final FlowCollector<?> getOwner() {
        return this.owner;
    }
}
