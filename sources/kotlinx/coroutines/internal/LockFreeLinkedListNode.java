package kotlinx.coroutines.internal;

import java.util.Objects;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;
/* compiled from: LockFreeLinkedList.kt */
/* loaded from: classes.dex */
public class LockFreeLinkedListNode {
    public final AtomicRef<Object> _next = AtomicFU.atomic(this);
    public final AtomicRef<LockFreeLinkedListNode> _prev = AtomicFU.atomic(this);
    public final AtomicRef<Removed> _removedRef = AtomicFU.atomic(null);

    /* compiled from: LockFreeLinkedList.kt */
    /* loaded from: classes.dex */
    public static abstract class CondAddOp extends AtomicOp<LockFreeLinkedListNode> {
        public final LockFreeLinkedListNode newNode;
        public LockFreeLinkedListNode oldNext;

        public CondAddOp(LockFreeLinkedListNode lockFreeLinkedListNode) {
            this.newNode = lockFreeLinkedListNode;
        }
    }

    public final boolean addNext(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListHead lockFreeLinkedListHead) {
        lockFreeLinkedListNode._prev.lazySet(this);
        lockFreeLinkedListNode._next.lazySet(lockFreeLinkedListHead);
        if (!this._next.compareAndSet(lockFreeLinkedListHead, lockFreeLinkedListNode)) {
            return false;
        }
        lockFreeLinkedListNode.finishAdd(lockFreeLinkedListHead);
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0046, code lost:
        if (r3._next.compareAndSet(r2, ((kotlinx.coroutines.internal.Removed) r4).ref) != false) goto L_0x0049;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final kotlinx.coroutines.internal.LockFreeLinkedListNode correctPrev() {
        /*
            r7 = this;
        L_0x0000:
            kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeLinkedListNode> r0 = r7._prev
            java.util.Objects.requireNonNull(r0)
            T r0 = r0.value
            kotlinx.coroutines.internal.LockFreeLinkedListNode r0 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r0
            r1 = 0
            r2 = r0
        L_0x000b:
            r3 = r1
        L_0x000c:
            kotlinx.atomicfu.AtomicRef<java.lang.Object> r4 = r2._next
            java.util.Objects.requireNonNull(r4)
            T r4 = r4.value
            if (r4 != r7) goto L_0x0022
            if (r0 != r2) goto L_0x0018
            return r2
        L_0x0018:
            kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeLinkedListNode> r1 = r7._prev
            boolean r0 = r1.compareAndSet(r0, r2)
            if (r0 != 0) goto L_0x0021
            goto L_0x0000
        L_0x0021:
            return r2
        L_0x0022:
            boolean r5 = r7.isRemoved()
            if (r5 == 0) goto L_0x0029
            return r1
        L_0x0029:
            if (r4 != 0) goto L_0x002c
            return r2
        L_0x002c:
            boolean r5 = r4 instanceof kotlinx.coroutines.internal.OpDescriptor
            if (r5 == 0) goto L_0x0036
            kotlinx.coroutines.internal.OpDescriptor r4 = (kotlinx.coroutines.internal.OpDescriptor) r4
            r4.perform(r2)
            goto L_0x0000
        L_0x0036:
            boolean r5 = r4 instanceof kotlinx.coroutines.internal.Removed
            if (r5 == 0) goto L_0x0055
            if (r3 == 0) goto L_0x004b
            kotlinx.atomicfu.AtomicRef<java.lang.Object> r5 = r3._next
            kotlinx.coroutines.internal.Removed r4 = (kotlinx.coroutines.internal.Removed) r4
            kotlinx.coroutines.internal.LockFreeLinkedListNode r4 = r4.ref
            boolean r2 = r5.compareAndSet(r2, r4)
            if (r2 != 0) goto L_0x0049
            goto L_0x0000
        L_0x0049:
            r2 = r3
            goto L_0x000b
        L_0x004b:
            kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeLinkedListNode> r2 = r2._prev
            java.util.Objects.requireNonNull(r2)
            T r2 = r2.value
            kotlinx.coroutines.internal.LockFreeLinkedListNode r2 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r2
            goto L_0x000c
        L_0x0055:
            r3 = r4
            kotlinx.coroutines.internal.LockFreeLinkedListNode r3 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r3
            r6 = r3
            r3 = r2
            r2 = r6
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.LockFreeLinkedListNode.correctPrev():kotlinx.coroutines.internal.LockFreeLinkedListNode");
    }

    public final void finishAdd(LockFreeLinkedListNode lockFreeLinkedListNode) {
        LockFreeLinkedListNode lockFreeLinkedListNode2;
        AtomicRef<LockFreeLinkedListNode> atomicRef = lockFreeLinkedListNode._prev;
        do {
            Objects.requireNonNull(atomicRef);
            lockFreeLinkedListNode2 = atomicRef.value;
            if (getNext() != lockFreeLinkedListNode) {
                return;
            }
        } while (!lockFreeLinkedListNode._prev.compareAndSet(lockFreeLinkedListNode2, this));
        if (isRemoved()) {
            lockFreeLinkedListNode.correctPrev();
        }
    }

    public final Object getNext() {
        AtomicRef<Object> atomicRef = this._next;
        while (true) {
            Objects.requireNonNull(atomicRef);
            Object obj = atomicRef.value;
            if (!(obj instanceof OpDescriptor)) {
                return obj;
            }
            ((OpDescriptor) obj).perform(this);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append((Object) getClass().getSimpleName());
        sb.append('@');
        sb.append((Object) Integer.toHexString(System.identityHashCode(this)));
        return sb.toString();
    }

    public final int tryCondAddNext(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2, CondAddOp condAddOp) {
        lockFreeLinkedListNode._prev.lazySet(this);
        lockFreeLinkedListNode._next.lazySet(lockFreeLinkedListNode2);
        condAddOp.oldNext = lockFreeLinkedListNode2;
        if (!this._next.compareAndSet(lockFreeLinkedListNode2, condAddOp)) {
            return 0;
        }
        if (condAddOp.perform(this) == null) {
            return 1;
        }
        return 2;
    }

    public final LockFreeLinkedListNode getNextNode() {
        Removed removed;
        Object next = getNext();
        LockFreeLinkedListNode lockFreeLinkedListNode = null;
        if (next instanceof Removed) {
            removed = (Removed) next;
        } else {
            removed = null;
        }
        if (removed != null) {
            lockFreeLinkedListNode = removed.ref;
        }
        if (lockFreeLinkedListNode == null) {
            return (LockFreeLinkedListNode) next;
        }
        return lockFreeLinkedListNode;
    }

    public final LockFreeLinkedListNode getPrevNode() {
        LockFreeLinkedListNode correctPrev = correctPrev();
        if (correctPrev == null) {
            AtomicRef<LockFreeLinkedListNode> atomicRef = this._prev;
            Objects.requireNonNull(atomicRef);
            correctPrev = atomicRef.value;
            while (correctPrev.isRemoved()) {
                AtomicRef<LockFreeLinkedListNode> atomicRef2 = correctPrev._prev;
                Objects.requireNonNull(atomicRef2);
                correctPrev = atomicRef2.value;
            }
        }
        return correctPrev;
    }

    public final void helpRemovePrev() {
        while (true) {
            Object next = this.getNext();
            if (!(next instanceof Removed)) {
                this.correctPrev();
                return;
            }
            this = ((Removed) next).ref;
        }
    }

    public boolean isRemoved() {
        return getNext() instanceof Removed;
    }

    public boolean remove() {
        if (removeOrNext() == null) {
            return true;
        }
        return false;
    }

    public final LockFreeLinkedListNode removeOrNext() {
        Object next;
        LockFreeLinkedListNode lockFreeLinkedListNode;
        Removed removed;
        do {
            next = getNext();
            if (next instanceof Removed) {
                return ((Removed) next).ref;
            }
            if (next == this) {
                return (LockFreeLinkedListNode) next;
            }
            lockFreeLinkedListNode = (LockFreeLinkedListNode) next;
            Objects.requireNonNull(lockFreeLinkedListNode);
            AtomicRef<Removed> atomicRef = lockFreeLinkedListNode._removedRef;
            Objects.requireNonNull(atomicRef);
            removed = atomicRef.value;
            if (removed == null) {
                removed = new Removed(lockFreeLinkedListNode);
                lockFreeLinkedListNode._removedRef.lazySet(removed);
            }
        } while (!this._next.compareAndSet(next, removed));
        lockFreeLinkedListNode.correctPrev();
        return null;
    }
}
