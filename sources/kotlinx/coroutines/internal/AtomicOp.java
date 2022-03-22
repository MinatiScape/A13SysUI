package kotlinx.coroutines.internal;

import androidx.slice.view.R$id;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
/* compiled from: Atomic.kt */
/* loaded from: classes.dex */
public abstract class AtomicOp<T> extends OpDescriptor {
    public final AtomicRef<Object> _consensus = new AtomicRef<>(R$id.NO_DECISION);

    public abstract Symbol prepare(Object obj);

    @Override // kotlinx.coroutines.internal.OpDescriptor
    public final Object perform(Object obj) {
        boolean z;
        LockFreeLinkedListNode lockFreeLinkedListNode;
        AtomicRef<Object> atomicRef = this._consensus;
        Objects.requireNonNull(atomicRef);
        Object obj2 = atomicRef.value;
        Symbol symbol = R$id.NO_DECISION;
        if (obj2 == symbol) {
            obj2 = prepare(obj);
            boolean z2 = DebugKt.DEBUG;
            AtomicRef<Object> atomicRef2 = this._consensus;
            Objects.requireNonNull(atomicRef2);
            Object obj3 = atomicRef2.value;
            if (obj3 != symbol) {
                obj2 = obj3;
            } else if (!this._consensus.compareAndSet(symbol, obj2)) {
                AtomicRef<Object> atomicRef3 = this._consensus;
                Objects.requireNonNull(atomicRef3);
                obj2 = atomicRef3.value;
            }
        }
        LockFreeLinkedListNode.CondAddOp condAddOp = (LockFreeLinkedListNode.CondAddOp) this;
        LockFreeLinkedListNode lockFreeLinkedListNode2 = (LockFreeLinkedListNode) obj;
        if (obj2 == null) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            lockFreeLinkedListNode = condAddOp.newNode;
        } else {
            lockFreeLinkedListNode = condAddOp.oldNext;
        }
        if (lockFreeLinkedListNode != null && lockFreeLinkedListNode2._next.compareAndSet(condAddOp, lockFreeLinkedListNode) && z) {
            LockFreeLinkedListNode lockFreeLinkedListNode3 = condAddOp.newNode;
            LockFreeLinkedListNode lockFreeLinkedListNode4 = condAddOp.oldNext;
            Intrinsics.checkNotNull(lockFreeLinkedListNode4);
            lockFreeLinkedListNode3.finishAdd(lockFreeLinkedListNode4);
        }
        return obj2;
    }
}
