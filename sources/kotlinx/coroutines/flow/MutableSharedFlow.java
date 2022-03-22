package kotlinx.coroutines.flow;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
/* compiled from: SharedFlow.kt */
/* loaded from: classes.dex */
public interface MutableSharedFlow<T> extends Flow, FlowCollector<T> {
    @Override // kotlinx.coroutines.flow.FlowCollector
    Object emit(T t, Continuation<? super Unit> continuation);

    boolean tryEmit(T t);
}
