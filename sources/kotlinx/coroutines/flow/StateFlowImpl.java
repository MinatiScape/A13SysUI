package kotlinx.coroutines.flow;

import androidx.slice.compat.SliceProviderCompat;
import java.util.Objects;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.flow.internal.AbstractSharedFlow;
import kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot;
import kotlinx.coroutines.internal.Symbol;
/* compiled from: StateFlow.kt */
/* loaded from: classes.dex */
public final class StateFlowImpl<T> extends AbstractSharedFlow<StateFlowSlot> implements MutableStateFlow<T>, Flow {
    public final AtomicRef<Object> _state;
    public int sequence;

    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlow
    public final AbstractSharedFlowSlot[] createSlotArray() {
        return new StateFlowSlot[2];
    }

    public final boolean updateState(Object obj, Object obj2) {
        int i;
        Object obj3;
        Symbol symbol;
        synchronized (this) {
            AtomicRef<Object> atomicRef = this._state;
            Objects.requireNonNull(atomicRef);
            Object obj4 = atomicRef.value;
            if (obj != null && !Intrinsics.areEqual(obj4, obj)) {
                return false;
            }
            if (Intrinsics.areEqual(obj4, obj2)) {
                return true;
            }
            this._state.setValue(obj2);
            int i2 = this.sequence;
            if ((i2 & 1) == 0) {
                int i3 = i2 + 1;
                this.sequence = i3;
                Object obj5 = this.slots;
                while (true) {
                    StateFlowSlot[] stateFlowSlotArr = (StateFlowSlot[]) obj5;
                    if (stateFlowSlotArr != null) {
                        int length = stateFlowSlotArr.length;
                        int i4 = 0;
                        while (i4 < length) {
                            StateFlowSlot stateFlowSlot = stateFlowSlotArr[i4];
                            i4++;
                            if (stateFlowSlot != null) {
                                AtomicRef<Object> atomicRef2 = stateFlowSlot._state;
                                while (true) {
                                    Objects.requireNonNull(atomicRef2);
                                    Object obj6 = atomicRef2.value;
                                    if (obj6 != null && obj6 != (symbol = StateFlowKt.PENDING)) {
                                        Symbol symbol2 = StateFlowKt.NONE;
                                        if (obj6 == symbol2) {
                                            if (stateFlowSlot._state.compareAndSet(obj6, symbol)) {
                                                break;
                                            }
                                        } else if (stateFlowSlot._state.compareAndSet(obj6, symbol2)) {
                                            ((CancellableContinuationImpl) obj6).resumeWith(Unit.INSTANCE);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    synchronized (this) {
                        i = this.sequence;
                        if (i == i3) {
                            this.sequence = i3 + 1;
                            return true;
                        }
                        obj3 = this.slots;
                    }
                    obj5 = obj3;
                    i3 = i;
                }
            } else {
                this.sequence = i2 + 2;
                return true;
            }
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(10:2|(8:(2:4|(14:6|8|(1:(2:90|(1:(11:13|14|15|16|88|38|(2:72|73)|46|(1:48)(1:49)|50|(1:52)(12:53|54|55|(1:57)(1:58)|(5:60|(1:62)(1:63)|64|(1:66)(1:67)|(1:69))|88|38|(1:40)(3:41|72|73)|46|(0)(0)|50|(0)(0)))(2:17|18))(14:19|20|21|54|55|(0)(0)|(0)|88|38|(0)(0)|46|(0)(0)|50|(0)(0)))(4:23|84|24|25))(5:29|86|30|(2:32|(1:34))|35)|82|36|37|16|88|38|(0)(0)|46|(0)(0)|50|(0)(0)))|88|38|(0)(0)|46|(0)(0)|50|(0)(0))|7|8|(0)(0)|82|36|37|16|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00c9, code lost:
        r8 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00cd, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r14, r9) != false) goto L_0x00f0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0142, code lost:
        r14 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0143, code lost:
        r7 = r2;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0024  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00c0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00c1 A[Catch: all -> 0x013b, TryCatch #3 {all -> 0x013b, blocks: (B:38:0x00b7, B:41:0x00c1, B:44:0x00c9, B:46:0x00cf, B:50:0x00d6, B:55:0x00f0, B:60:0x0109, B:63:0x012a, B:64:0x012f, B:67:0x0136, B:72:0x013d, B:73:0x0141), top: B:88:0x00b7 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00e8 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0109 A[Catch: all -> 0x013b, TryCatch #3 {all -> 0x013b, blocks: (B:38:0x00b7, B:41:0x00c1, B:44:0x00c9, B:46:0x00cf, B:50:0x00d6, B:55:0x00f0, B:60:0x0109, B:63:0x012a, B:64:0x012f, B:67:0x0136, B:72:0x013d, B:73:0x0141), top: B:88:0x00b7 }] */
    /* JADX WARN: Type inference failed for: r2v0, types: [int] */
    /* JADX WARN: Type inference failed for: r2v3, types: [kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot] */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r8v4, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r8v6 */
    /* JADX WARN: Unknown variable types count: 1 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:59:0x0107 -> B:88:0x00b7). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:68:0x0138 -> B:88:0x00b7). Please submit an issue!!! */
    @Override // kotlinx.coroutines.flow.Flow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> r14, kotlin.coroutines.Continuation<? super kotlin.Unit> r15) {
        /*
            Method dump skipped, instructions count: 337
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.StateFlowImpl.collect(kotlinx.coroutines.flow.FlowCollector, kotlin.coroutines.Continuation):java.lang.Object");
    }

    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlow
    public final StateFlowSlot createSlot() {
        return new StateFlowSlot();
    }

    @Override // kotlinx.coroutines.flow.MutableStateFlow, kotlinx.coroutines.flow.StateFlow
    public final T getValue() {
        Symbol symbol = SliceProviderCompat.AnonymousClass2.NULL;
        AtomicRef<Object> atomicRef = this._state;
        Objects.requireNonNull(atomicRef);
        T t = (T) atomicRef.value;
        if (t == symbol) {
            return null;
        }
        return t;
    }

    @Override // kotlinx.coroutines.flow.MutableStateFlow
    public final void setValue(T t) {
        if (t == null) {
            t = (T) SliceProviderCompat.AnonymousClass2.NULL;
        }
        updateState(null, t);
    }

    public StateFlowImpl(Object obj) {
        this._state = new AtomicRef<>(obj);
    }

    @Override // kotlinx.coroutines.flow.MutableSharedFlow, kotlinx.coroutines.flow.FlowCollector
    public final Object emit(T t, Continuation<? super Unit> continuation) {
        setValue(t);
        return Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.flow.MutableSharedFlow
    public final boolean tryEmit(T t) {
        setValue(t);
        return true;
    }
}
