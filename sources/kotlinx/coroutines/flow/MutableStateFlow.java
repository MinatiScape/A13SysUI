package kotlinx.coroutines.flow;
/* compiled from: StateFlow.kt */
/* loaded from: classes.dex */
public interface MutableStateFlow<T> extends StateFlow<T>, MutableSharedFlow<T> {
    @Override // kotlinx.coroutines.flow.StateFlow
    T getValue();

    void setValue(T t);
}
