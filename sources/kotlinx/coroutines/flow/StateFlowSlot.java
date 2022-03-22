package kotlinx.coroutines.flow;

import androidx.slice.SliceSpecs;
import java.util.Objects;
import kotlin.coroutines.Continuation;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.flow.internal.AbstractSharedFlow;
import kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot;
/* compiled from: StateFlow.kt */
/* loaded from: classes.dex */
public final class StateFlowSlot extends AbstractSharedFlowSlot<StateFlowImpl<?>> {
    public final AtomicRef<Object> _state = AtomicFU.atomic(null);

    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot
    public final boolean allocateLocked(AbstractSharedFlow abstractSharedFlow) {
        StateFlowImpl stateFlowImpl = (StateFlowImpl) abstractSharedFlow;
        AtomicRef<Object> atomicRef = this._state;
        Objects.requireNonNull(atomicRef);
        if (atomicRef.value != null) {
            return false;
        }
        this._state.setValue(StateFlowKt.NONE);
        return true;
    }

    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot
    public final Continuation[] freeLocked(AbstractSharedFlow abstractSharedFlow) {
        StateFlowImpl stateFlowImpl = (StateFlowImpl) abstractSharedFlow;
        this._state.setValue(null);
        return SliceSpecs.EMPTY_RESUMES;
    }
}
