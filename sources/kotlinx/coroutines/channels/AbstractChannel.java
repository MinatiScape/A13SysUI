package kotlinx.coroutines.channels;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import androidx.lifecycle.runtime.R$id;
import androidx.preference.R$color;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BeforeResumeCancelHandler;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CancellableContinuationKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.channels.ChannelResult;
import kotlinx.coroutines.internal.InlineList;
import kotlinx.coroutines.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.OnUndeliveredElementKt;
import kotlinx.coroutines.internal.Removed;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.Symbol;
/* compiled from: AbstractChannel.kt */
/* loaded from: classes.dex */
public abstract class AbstractChannel<E> extends AbstractSendChannel<E> implements Channel<E> {

    /* compiled from: AbstractChannel.kt */
    /* loaded from: classes.dex */
    public static final class Itr<E> implements ChannelIterator<E> {
        public final AbstractChannel<E> channel;
        public Object result = AbstractChannelKt.POLL_FAILED;

        @Override // kotlinx.coroutines.channels.ChannelIterator
        public final Object hasNext(ContinuationImpl continuationImpl) {
            Function1<Throwable, Unit> function1;
            Object obj = this.result;
            Symbol symbol = AbstractChannelKt.POLL_FAILED;
            boolean z = false;
            if (obj != symbol) {
                if (obj instanceof Closed) {
                    Closed closed = (Closed) obj;
                    if (closed.closeCause != null) {
                        throw StackTraceRecoveryKt.recoverStackTrace(closed.getReceiveException());
                    }
                } else {
                    z = true;
                }
                return Boolean.valueOf(z);
            }
            Object pollInternal = this.channel.pollInternal();
            this.result = pollInternal;
            if (pollInternal != symbol) {
                if (pollInternal instanceof Closed) {
                    Closed closed2 = (Closed) pollInternal;
                    if (closed2.closeCause != null) {
                        throw StackTraceRecoveryKt.recoverStackTrace(closed2.getReceiveException());
                    }
                } else {
                    z = true;
                }
                return Boolean.valueOf(z);
            }
            CancellableContinuationImpl orCreateCancellableContinuation = CancellableContinuationKt.getOrCreateCancellableContinuation(R$color.intercepted(continuationImpl));
            ReceiveHasNext receiveHasNext = new ReceiveHasNext(this, orCreateCancellableContinuation);
            while (true) {
                AbstractChannel<E> abstractChannel = this.channel;
                Objects.requireNonNull(abstractChannel);
                if (abstractChannel.enqueueReceiveInternal(receiveHasNext)) {
                    AbstractChannel<E> abstractChannel2 = this.channel;
                    Objects.requireNonNull(abstractChannel2);
                    orCreateCancellableContinuation.invokeOnCancellation(new RemoveReceiveOnCancel(receiveHasNext));
                    break;
                }
                Object pollInternal2 = this.channel.pollInternal();
                this.result = pollInternal2;
                if (pollInternal2 instanceof Closed) {
                    Closed closed3 = (Closed) pollInternal2;
                    if (closed3.closeCause == null) {
                        orCreateCancellableContinuation.resumeWith(Boolean.FALSE);
                    } else {
                        orCreateCancellableContinuation.resumeWith(new Result.Failure(closed3.getReceiveException()));
                    }
                } else if (pollInternal2 != AbstractChannelKt.POLL_FAILED) {
                    Boolean bool = Boolean.TRUE;
                    Function1<E, Unit> function12 = this.channel.onUndeliveredElement;
                    if (function12 == null) {
                        function1 = null;
                    } else {
                        function1 = OnUndeliveredElementKt.bindCancellationFun(function12, pollInternal2, orCreateCancellableContinuation.context);
                    }
                    orCreateCancellableContinuation.resumeImpl(bool, orCreateCancellableContinuation.resumeMode, function1);
                }
            }
            return orCreateCancellableContinuation.getResult();
        }

        @Override // kotlinx.coroutines.channels.ChannelIterator
        public final E next() {
            E e = (E) this.result;
            if (!(e instanceof Closed)) {
                Symbol symbol = AbstractChannelKt.POLL_FAILED;
                if (e != symbol) {
                    this.result = symbol;
                    return e;
                }
                throw new IllegalStateException("'hasNext' should be called prior to 'next' invocation");
            }
            throw StackTraceRecoveryKt.recoverStackTrace(((Closed) e).getReceiveException());
        }

        public Itr(AbstractChannel<E> abstractChannel) {
            this.channel = abstractChannel;
        }
    }

    /* compiled from: AbstractChannel.kt */
    /* loaded from: classes.dex */
    public static class ReceiveElement<E> extends Receive<E> {
        public final CancellableContinuation<Object> cont;
        public final int receiveMode = 1;

        @Override // kotlinx.coroutines.channels.ReceiveOrClosed
        public final void completeResumeReceive(E e) {
            this.cont.completeResume();
        }

        @Override // kotlinx.coroutines.channels.Receive
        public final void resumeReceiveClosed(Closed<?> closed) {
            if (this.receiveMode == 1) {
                this.cont.resumeWith(new ChannelResult(new ChannelResult.Closed(closed.closeCause)));
            } else {
                this.cont.resumeWith(new Result.Failure(closed.getReceiveException()));
            }
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ReceiveElement@");
            m.append(DebugStringsKt.getHexAddress(this));
            m.append("[receiveMode=");
            return Insets$$ExternalSyntheticOutline0.m(m, this.receiveMode, ']');
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlinx.coroutines.channels.ReceiveOrClosed
        public final Symbol tryResumeReceive(Object obj) {
            ChannelResult channelResult;
            CancellableContinuation<Object> cancellableContinuation = this.cont;
            if (this.receiveMode == 1) {
                channelResult = new ChannelResult(obj);
            } else {
                channelResult = obj;
            }
            if (cancellableContinuation.tryResume((Object) channelResult, (Function1) resumeOnCancellationFun(obj)) == null) {
                return null;
            }
            boolean z = DebugKt.DEBUG;
            return R$id.RESUME_TOKEN;
        }

        public ReceiveElement(CancellableContinuationImpl cancellableContinuationImpl) {
            this.cont = cancellableContinuationImpl;
        }
    }

    /* compiled from: AbstractChannel.kt */
    /* loaded from: classes.dex */
    public static final class ReceiveElementWithUndeliveredHandler<E> extends ReceiveElement<E> {
        public final Function1<E, Unit> onUndeliveredElement;

        @Override // kotlinx.coroutines.channels.Receive
        public final Function1<Throwable, Unit> resumeOnCancellationFun(E e) {
            return OnUndeliveredElementKt.bindCancellationFun(this.onUndeliveredElement, e, this.cont.getContext());
        }

        public ReceiveElementWithUndeliveredHandler(CancellableContinuationImpl cancellableContinuationImpl, Function1 function1) {
            super(cancellableContinuationImpl);
            this.onUndeliveredElement = function1;
        }
    }

    /* compiled from: AbstractChannel.kt */
    /* loaded from: classes.dex */
    public static class ReceiveHasNext<E> extends Receive<E> {
        public final CancellableContinuation<Boolean> cont;
        public final Itr<E> iterator;

        @Override // kotlinx.coroutines.channels.ReceiveOrClosed
        public final void completeResumeReceive(E e) {
            Itr<E> itr = this.iterator;
            Objects.requireNonNull(itr);
            itr.result = e;
            this.cont.completeResume();
        }

        @Override // kotlinx.coroutines.channels.Receive
        public final Function1<Throwable, Unit> resumeOnCancellationFun(E e) {
            Function1<E, Unit> function1 = this.iterator.channel.onUndeliveredElement;
            if (function1 == null) {
                return null;
            }
            return OnUndeliveredElementKt.bindCancellationFun(function1, e, this.cont.getContext());
        }

        @Override // kotlinx.coroutines.channels.Receive
        public final void resumeReceiveClosed(Closed<?> closed) {
            Symbol symbol;
            if (closed.closeCause == null) {
                symbol = this.cont.tryResume(Boolean.FALSE, (Object) null);
            } else {
                symbol = this.cont.tryResumeWithException(closed.getReceiveException());
            }
            if (symbol != null) {
                Itr<E> itr = this.iterator;
                Objects.requireNonNull(itr);
                itr.result = closed;
                this.cont.completeResume();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlinx.coroutines.channels.ReceiveOrClosed
        public final Symbol tryResumeReceive(Object obj) {
            if (this.cont.tryResume((Object) Boolean.TRUE, (Function1) resumeOnCancellationFun(obj)) == null) {
                return null;
            }
            boolean z = DebugKt.DEBUG;
            return R$id.RESUME_TOKEN;
        }

        public ReceiveHasNext(Itr itr, CancellableContinuationImpl cancellableContinuationImpl) {
            this.iterator = itr;
            this.cont = cancellableContinuationImpl;
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        public final String toString() {
            return Intrinsics.stringPlus("ReceiveHasNext@", DebugStringsKt.getHexAddress(this));
        }
    }

    /* compiled from: AbstractChannel.kt */
    /* loaded from: classes.dex */
    public final class RemoveReceiveOnCancel extends BeforeResumeCancelHandler {
        public final Receive<?> receive;

        @Override // kotlin.jvm.functions.Function1
        public final /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
            invoke2(th);
            return Unit.INSTANCE;
        }

        public RemoveReceiveOnCancel(Receive<?> receive) {
            this.receive = receive;
        }

        @Override // kotlinx.coroutines.CancelHandlerBase
        /* renamed from: invoke  reason: avoid collision after fix types in other method */
        public final void invoke2(Throwable th) {
            if (this.receive.remove()) {
                Objects.requireNonNull(AbstractChannel.this);
            }
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("RemoveReceiveOnCancel[");
            m.append(this.receive);
            m.append(']');
            return m.toString();
        }
    }

    public abstract boolean isBufferAlwaysEmpty();

    public abstract boolean isBufferEmpty();

    public boolean isClosedForReceive() {
        Closed closed;
        LockFreeLinkedListNode nextNode = this.queue.getNextNode();
        Closed closed2 = null;
        if (nextNode instanceof Closed) {
            closed = (Closed) nextNode;
        } else {
            closed = null;
        }
        if (closed != null) {
            AbstractSendChannel.helpClose(closed);
            closed2 = closed;
        }
        if (closed2 == null || !isBufferEmpty()) {
            return false;
        }
        return true;
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final Itr iterator() {
        return new Itr(this);
    }

    /* renamed from: onCancelIdempotentList-w-w6eGU  reason: not valid java name */
    public void mo182onCancelIdempotentListww6eGU(Object obj, Closed<?> closed) {
        if (obj != null) {
            if (!(obj instanceof ArrayList)) {
                ((Send) obj).resumeSendClosed(closed);
                return;
            }
            ArrayList arrayList = (ArrayList) obj;
            int size = arrayList.size() - 1;
            if (size >= 0) {
                while (true) {
                    int i = size - 1;
                    ((Send) arrayList.get(size)).resumeSendClosed(closed);
                    if (i >= 0) {
                        size = i;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0030  */
    /* JADX WARN: Type inference failed for: r0v6, types: [kotlinx.coroutines.channels.AbstractChannel$ReceiveElement] */
    /* JADX WARN: Type inference failed for: r5v0, types: [kotlinx.coroutines.channels.AbstractSendChannel, kotlinx.coroutines.channels.AbstractChannel<E>, kotlinx.coroutines.channels.AbstractChannel] */
    @Override // kotlinx.coroutines.channels.ReceiveChannel
    /* renamed from: receiveCatching-JP2dKIU  reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object mo183receiveCatchingJP2dKIU(kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelResult<? extends E>> r6) {
        /*
            r5 = this;
            boolean r0 = r6 instanceof kotlinx.coroutines.channels.AbstractChannel$receiveCatching$1
            if (r0 == 0) goto L_0x0013
            r0 = r6
            kotlinx.coroutines.channels.AbstractChannel$receiveCatching$1 r0 = (kotlinx.coroutines.channels.AbstractChannel$receiveCatching$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0013
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x0018
        L_0x0013:
            kotlinx.coroutines.channels.AbstractChannel$receiveCatching$1 r0 = new kotlinx.coroutines.channels.AbstractChannel$receiveCatching$1
            r0.<init>(r5, r6)
        L_0x0018:
            java.lang.Object r6 = r0.result
            kotlin.coroutines.intrinsics.CoroutineSingletons r1 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L_0x0030
            if (r2 != r3) goto L_0x0028
            kotlin.ResultKt.throwOnFailure(r6)
            goto L_0x009f
        L_0x0028:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L_0x0030:
            kotlin.ResultKt.throwOnFailure(r6)
            java.lang.Object r6 = r5.pollInternal()
            kotlinx.coroutines.internal.Symbol r2 = kotlinx.coroutines.channels.AbstractChannelKt.POLL_FAILED
            if (r6 == r2) goto L_0x0049
            boolean r5 = r6 instanceof kotlinx.coroutines.channels.Closed
            if (r5 == 0) goto L_0x0048
            kotlinx.coroutines.channels.Closed r6 = (kotlinx.coroutines.channels.Closed) r6
            java.lang.Throwable r5 = r6.closeCause
            kotlinx.coroutines.channels.ChannelResult$Closed r6 = new kotlinx.coroutines.channels.ChannelResult$Closed
            r6.<init>(r5)
        L_0x0048:
            return r6
        L_0x0049:
            r0.label = r3
            kotlin.coroutines.Continuation r6 = androidx.preference.R$color.intercepted(r0)
            kotlinx.coroutines.CancellableContinuationImpl r6 = kotlinx.coroutines.CancellableContinuationKt.getOrCreateCancellableContinuation(r6)
            kotlin.jvm.functions.Function1<E, kotlin.Unit> r0 = r5.onUndeliveredElement
            if (r0 != 0) goto L_0x005d
            kotlinx.coroutines.channels.AbstractChannel$ReceiveElement r0 = new kotlinx.coroutines.channels.AbstractChannel$ReceiveElement
            r0.<init>(r6)
            goto L_0x0063
        L_0x005d:
            kotlinx.coroutines.channels.AbstractChannel$ReceiveElementWithUndeliveredHandler r2 = new kotlinx.coroutines.channels.AbstractChannel$ReceiveElementWithUndeliveredHandler
            r2.<init>(r6, r0)
            r0 = r2
        L_0x0063:
            boolean r2 = r5.enqueueReceiveInternal(r0)
            if (r2 == 0) goto L_0x0072
            kotlinx.coroutines.channels.AbstractChannel$RemoveReceiveOnCancel r2 = new kotlinx.coroutines.channels.AbstractChannel$RemoveReceiveOnCancel
            r2.<init>(r0)
            r6.invokeOnCancellation(r2)
            goto L_0x0098
        L_0x0072:
            java.lang.Object r2 = r5.pollInternal()
            boolean r4 = r2 instanceof kotlinx.coroutines.channels.Closed
            if (r4 == 0) goto L_0x0080
            kotlinx.coroutines.channels.Closed r2 = (kotlinx.coroutines.channels.Closed) r2
            r0.resumeReceiveClosed(r2)
            goto L_0x0098
        L_0x0080:
            kotlinx.coroutines.internal.Symbol r4 = kotlinx.coroutines.channels.AbstractChannelKt.POLL_FAILED
            if (r2 == r4) goto L_0x0063
            int r5 = r0.receiveMode
            if (r5 != r3) goto L_0x008e
            kotlinx.coroutines.channels.ChannelResult r5 = new kotlinx.coroutines.channels.ChannelResult
            r5.<init>(r2)
            goto L_0x008f
        L_0x008e:
            r5 = r2
        L_0x008f:
            kotlin.jvm.functions.Function1 r0 = r0.resumeOnCancellationFun(r2)
            int r2 = r6.resumeMode
            r6.resumeImpl(r5, r2, r0)
        L_0x0098:
            java.lang.Object r6 = r6.getResult()
            if (r6 != r1) goto L_0x009f
            return r1
        L_0x009f:
            kotlinx.coroutines.channels.ChannelResult r6 = (kotlinx.coroutines.channels.ChannelResult) r6
            java.util.Objects.requireNonNull(r6)
            java.lang.Object r5 = r6.holder
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.AbstractChannel.mo183receiveCatchingJP2dKIU(kotlin.coroutines.Continuation):java.lang.Object");
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed() {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        LockFreeLinkedListNode removeOrNext;
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        while (true) {
            lockFreeLinkedListNode = (LockFreeLinkedListNode) lockFreeLinkedListHead.getNext();
            if (lockFreeLinkedListNode != lockFreeLinkedListHead && (lockFreeLinkedListNode instanceof ReceiveOrClosed)) {
                if (((((ReceiveOrClosed) lockFreeLinkedListNode) instanceof Closed) && !lockFreeLinkedListNode.isRemoved()) || (removeOrNext = lockFreeLinkedListNode.removeOrNext()) == null) {
                    break;
                }
                removeOrNext.helpRemovePrev();
            }
        }
        lockFreeLinkedListNode = null;
        ReceiveOrClosed<E> receiveOrClosed = (ReceiveOrClosed) lockFreeLinkedListNode;
        if (receiveOrClosed != null) {
            boolean z = receiveOrClosed instanceof Closed;
        }
        return receiveOrClosed;
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final void cancel(CancellationException cancellationException) {
        if (!isClosedForReceive()) {
            if (cancellationException == null) {
                cancellationException = new CancellationException(Intrinsics.stringPlus(DebugStringsKt.getClassSimpleName(this), " was cancelled"));
            }
            onCancelIdempotent(close(cancellationException));
        }
    }

    public boolean enqueueReceiveInternal(final Receive<? super E> receive) {
        int tryCondAddNext;
        LockFreeLinkedListNode prevNode;
        if (!isBufferAlwaysEmpty()) {
            LockFreeLinkedListNode lockFreeLinkedListNode = this.queue;
            LockFreeLinkedListNode.CondAddOp abstractChannel$enqueueReceiveInternal$$inlined$addLastIfPrevAndIf$1 = new LockFreeLinkedListNode.CondAddOp(receive) { // from class: kotlinx.coroutines.channels.AbstractChannel$enqueueReceiveInternal$$inlined$addLastIfPrevAndIf$1
                @Override // kotlinx.coroutines.internal.AtomicOp
                public final Symbol prepare(Object obj) {
                    LockFreeLinkedListNode lockFreeLinkedListNode2 = (LockFreeLinkedListNode) obj;
                    if (this.isBufferEmpty()) {
                        return null;
                    }
                    return LockFreeLinkedListKt.CONDITION_FALSE;
                }
            };
            do {
                LockFreeLinkedListNode prevNode2 = lockFreeLinkedListNode.getPrevNode();
                if (!(!(prevNode2 instanceof Send))) {
                    break;
                }
                tryCondAddNext = prevNode2.tryCondAddNext(receive, lockFreeLinkedListNode, abstractChannel$enqueueReceiveInternal$$inlined$addLastIfPrevAndIf$1);
                if (tryCondAddNext == 1) {
                    return true;
                }
            } while (tryCondAddNext != 2);
        } else {
            LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
            do {
                prevNode = lockFreeLinkedListHead.getPrevNode();
                if (!(!(prevNode instanceof Send))) {
                }
            } while (!prevNode.addNext(receive, lockFreeLinkedListHead));
            return true;
        }
        return false;
    }

    public void onCancelIdempotent(boolean z) {
        Closed<?> closedForSend = getClosedForSend();
        if (closedForSend != null) {
            Object obj = null;
            while (true) {
                LockFreeLinkedListNode prevNode = closedForSend.getPrevNode();
                if (prevNode instanceof LockFreeLinkedListHead) {
                    mo182onCancelIdempotentListww6eGU(obj, closedForSend);
                    return;
                }
                boolean z2 = DebugKt.DEBUG;
                if (!prevNode.remove()) {
                    ((Removed) prevNode.getNext()).ref.helpRemovePrev();
                } else {
                    obj = InlineList.m186plusFjFbRPM(obj, (Send) prevNode);
                }
            }
        } else {
            throw new IllegalStateException("Cannot happen".toString());
        }
    }

    public Object pollInternal() {
        while (true) {
            Send takeFirstSendOrPeekClosed = takeFirstSendOrPeekClosed();
            if (takeFirstSendOrPeekClosed == null) {
                return AbstractChannelKt.POLL_FAILED;
            }
            if (takeFirstSendOrPeekClosed.tryResumeSend() != null) {
                boolean z = DebugKt.DEBUG;
                takeFirstSendOrPeekClosed.completeResumeSend();
                return takeFirstSendOrPeekClosed.getPollResult();
            }
            takeFirstSendOrPeekClosed.undeliveredElement();
        }
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    /* renamed from: tryReceive-PtdJZtk  reason: not valid java name */
    public final Object mo184tryReceivePtdJZtk() {
        Object pollInternal = pollInternal();
        if (pollInternal == AbstractChannelKt.POLL_FAILED) {
            return ChannelResult.failed;
        }
        if (pollInternal instanceof Closed) {
            return new ChannelResult.Closed(((Closed) pollInternal).closeCause);
        }
        return pollInternal;
    }

    public AbstractChannel(Function1<? super E, Unit> function1) {
        super(function1);
    }
}
