package kotlinx.coroutines;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.preference.R$color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CancellationException;
import kotlin.ExceptionsKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicBoolean;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.atomicfu.InterceptorKt;
import kotlinx.atomicfu.TraceBase;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.OpDescriptor;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.Symbol;
/* compiled from: JobSupport.kt */
/* loaded from: classes.dex */
public class JobSupport implements Job, ChildJob, ParentJob {
    public final AtomicRef<ChildHandle> _parentHandle;
    public final AtomicRef<Object> _state;

    /* compiled from: JobSupport.kt */
    /* loaded from: classes.dex */
    public static final class AwaitContinuation<T> extends CancellableContinuationImpl<T> {
        public final JobSupport job;

        public AwaitContinuation(Continuation continuation, CompletableDeferredImpl completableDeferredImpl) {
            super(continuation, 1);
            this.job = completableDeferredImpl;
        }

        @Override // kotlinx.coroutines.CancellableContinuationImpl
        public final String nameString() {
            return "AwaitContinuation";
        }

        @Override // kotlinx.coroutines.CancellableContinuationImpl
        public final Throwable getContinuationCancellationCause(JobSupport jobSupport) {
            Throwable rootCause;
            Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = this.job.getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
            if ((state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Finishing) && (rootCause = ((Finishing) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).getRootCause()) != null) {
                return rootCause;
            }
            if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof CompletedExceptionally) {
                return ((CompletedExceptionally) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).cause;
            }
            return jobSupport.getCancellationException();
        }
    }

    /* compiled from: JobSupport.kt */
    /* loaded from: classes.dex */
    public static final class ChildCompletion extends JobNode {
        public final ChildHandleNode child;
        public final JobSupport parent;
        public final Object proposedUpdate;
        public final Finishing state;

        @Override // kotlin.jvm.functions.Function1
        public final /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
            invoke2(th);
            return Unit.INSTANCE;
        }

        @Override // kotlinx.coroutines.CompletionHandlerBase
        /* renamed from: invoke  reason: avoid collision after fix types in other method */
        public final void invoke2(Throwable th) {
            JobSupport jobSupport = this.parent;
            Finishing finishing = this.state;
            ChildHandleNode childHandleNode = this.child;
            Object obj = this.proposedUpdate;
            Objects.requireNonNull(jobSupport);
            boolean z = DebugKt.DEBUG;
            ChildHandleNode nextChild = JobSupport.nextChild(childHandleNode);
            if (nextChild == null || !jobSupport.tryWaitForChild(finishing, nextChild, obj)) {
                jobSupport.afterCompletion(jobSupport.finalizeFinishingState(finishing, obj));
            }
        }

        public ChildCompletion(JobSupport jobSupport, Finishing finishing, ChildHandleNode childHandleNode, Object obj) {
            this.parent = jobSupport;
            this.state = finishing;
            this.child = childHandleNode;
            this.proposedUpdate = obj;
        }
    }

    /* compiled from: JobSupport.kt */
    /* loaded from: classes.dex */
    public static final class Finishing implements Incomplete {
        public final AtomicRef<Throwable> _rootCause;
        public final NodeList list;
        public final AtomicBoolean _isCompleting = new AtomicBoolean(false);
        public final AtomicRef<Object> _exceptionsHolder = new AtomicRef<>(null);

        public final Throwable getRootCause() {
            AtomicRef<Throwable> atomicRef = this._rootCause;
            Objects.requireNonNull(atomicRef);
            return atomicRef.value;
        }

        public final ArrayList sealLocked(Throwable th) {
            ArrayList arrayList;
            AtomicRef<Object> atomicRef = this._exceptionsHolder;
            Objects.requireNonNull(atomicRef);
            Object obj = atomicRef.value;
            if (obj == null) {
                arrayList = new ArrayList(4);
            } else if (obj instanceof Throwable) {
                ArrayList arrayList2 = new ArrayList(4);
                arrayList2.add(obj);
                arrayList = arrayList2;
            } else if (obj instanceof ArrayList) {
                arrayList = (ArrayList) obj;
            } else {
                throw new IllegalStateException(Intrinsics.stringPlus("State is ", obj).toString());
            }
            Throwable rootCause = getRootCause();
            if (rootCause != null) {
                arrayList.add(0, rootCause);
            }
            if (th != null && !Intrinsics.areEqual(th, rootCause)) {
                arrayList.add(th);
            }
            this._exceptionsHolder.setValue(JobSupportKt.SEALED);
            return arrayList;
        }

        public final String toString() {
            boolean z;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Finishing[cancelling=");
            m.append(isCancelling());
            m.append(", completing=");
            AtomicBoolean atomicBoolean = this._isCompleting;
            Objects.requireNonNull(atomicBoolean);
            if (atomicBoolean._value != 0) {
                z = true;
            } else {
                z = false;
            }
            m.append(z);
            m.append(", rootCause=");
            m.append(getRootCause());
            m.append(", exceptions=");
            AtomicRef<Object> atomicRef = this._exceptionsHolder;
            Objects.requireNonNull(atomicRef);
            m.append(atomicRef.value);
            m.append(", list=");
            m.append(this.list);
            m.append(']');
            return m.toString();
        }

        public Finishing(NodeList nodeList, Throwable th) {
            this.list = nodeList;
            this._rootCause = new AtomicRef<>(th);
        }

        public final void addExceptionLocked(Throwable th) {
            Throwable rootCause = getRootCause();
            if (rootCause == null) {
                this._rootCause.setValue(th);
            } else if (th != rootCause) {
                AtomicRef<Object> atomicRef = this._exceptionsHolder;
                Objects.requireNonNull(atomicRef);
                Object obj = atomicRef.value;
                if (obj == null) {
                    this._exceptionsHolder.setValue(th);
                } else if (obj instanceof Throwable) {
                    if (th != obj) {
                        ArrayList arrayList = new ArrayList(4);
                        arrayList.add(obj);
                        arrayList.add(th);
                        this._exceptionsHolder.setValue(arrayList);
                    }
                } else if (obj instanceof ArrayList) {
                    ((ArrayList) obj).add(th);
                } else {
                    throw new IllegalStateException(Intrinsics.stringPlus("State is ", obj).toString());
                }
            }
        }

        @Override // kotlinx.coroutines.Incomplete
        public final boolean isActive() {
            if (getRootCause() == null) {
                return true;
            }
            return false;
        }

        public final boolean isCancelling() {
            if (getRootCause() != null) {
                return true;
            }
            return false;
        }

        @Override // kotlinx.coroutines.Incomplete
        public final NodeList getList() {
            return this.list;
        }
    }

    public void afterCompletion(Object obj) {
    }

    public String cancellationExceptionMessage() {
        return "Job was cancelled";
    }

    public boolean getHandlesException$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return true;
    }

    public boolean getOnCancelComplete$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return this instanceof CompletableDeferredImpl;
    }

    public boolean handleJobException(Throwable th) {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v3, types: [kotlinx.coroutines.InactiveNodeList] */
    @Override // kotlinx.coroutines.Job
    public final DisposableHandle invokeOnCompletion(boolean z, boolean z2, JobNode jobNode) {
        final JobNode jobNode2;
        CompletedExceptionally completedExceptionally;
        Throwable th;
        boolean z3;
        boolean z4;
        Throwable th2 = null;
        if (z) {
            if (jobNode instanceof JobCancellingNode) {
                jobNode2 = (JobCancellingNode) jobNode;
            } else {
                jobNode2 = null;
            }
            if (jobNode2 == null) {
                jobNode2 = new InvokeOnCancelling(jobNode);
            }
        } else {
            boolean z5 = DebugKt.DEBUG;
            jobNode2 = jobNode;
        }
        jobNode2.job = this;
        while (true) {
            final Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
            if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Empty) {
                Empty empty = (Empty) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines;
                Objects.requireNonNull(empty);
                if (!empty.isActive) {
                    NodeList nodeList = new NodeList();
                    if (!empty.isActive) {
                        nodeList = new InactiveNodeList(nodeList);
                    }
                    this._state.compareAndSet(empty, nodeList);
                } else if (this._state.compareAndSet(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines, jobNode2)) {
                    return jobNode2;
                }
            } else if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Incomplete) {
                NodeList list = ((Incomplete) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).getList();
                if (list == null) {
                    Objects.requireNonNull(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines, "null cannot be cast to non-null type kotlinx.coroutines.JobNode");
                    promoteSingleToNodeList((JobNode) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
                } else {
                    DisposableHandle disposableHandle = NonDisposableHandle.INSTANCE;
                    boolean z6 = false;
                    if (!z || !(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Finishing)) {
                        th = null;
                    } else {
                        synchronized (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines) {
                            th = ((Finishing) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).getRootCause();
                            if (th != null) {
                                if (jobNode instanceof ChildHandleNode) {
                                    AtomicBoolean atomicBoolean = ((Finishing) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines)._isCompleting;
                                    Objects.requireNonNull(atomicBoolean);
                                    if (atomicBoolean._value != 0) {
                                        z4 = true;
                                    } else {
                                        z4 = false;
                                    }
                                    if (z4) {
                                    }
                                }
                            }
                            LockFreeLinkedListNode.CondAddOp jobSupport$addLastAtomic$$inlined$addLastIf$1 = new LockFreeLinkedListNode.CondAddOp(jobNode2) { // from class: kotlinx.coroutines.JobSupport$addLastAtomic$$inlined$addLastIf$1
                                @Override // kotlinx.coroutines.internal.AtomicOp
                                public final Symbol prepare(Object obj) {
                                    boolean z7;
                                    LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) obj;
                                    if (this.getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() == state$external__kotlinx_coroutines__android_common__kotlinx_coroutines) {
                                        z7 = true;
                                    } else {
                                        z7 = false;
                                    }
                                    if (z7) {
                                        return null;
                                    }
                                    return LockFreeLinkedListKt.CONDITION_FALSE;
                                }
                            };
                            while (true) {
                                int tryCondAddNext = list.getPrevNode().tryCondAddNext(jobNode2, list, jobSupport$addLastAtomic$$inlined$addLastIf$1);
                                if (tryCondAddNext != 1) {
                                    if (tryCondAddNext == 2) {
                                        z3 = false;
                                        break;
                                    }
                                } else {
                                    z3 = true;
                                    break;
                                }
                            }
                            if (z3) {
                                if (th == null) {
                                    return jobNode2;
                                }
                                disposableHandle = jobNode2;
                            }
                        }
                    }
                    if (th != null) {
                        if (z2) {
                            jobNode.invoke((JobNode) th);
                        }
                        return disposableHandle;
                    }
                    LockFreeLinkedListNode.CondAddOp jobSupport$addLastAtomic$$inlined$addLastIf$12 = new LockFreeLinkedListNode.CondAddOp(jobNode2) { // from class: kotlinx.coroutines.JobSupport$addLastAtomic$$inlined$addLastIf$1
                        @Override // kotlinx.coroutines.internal.AtomicOp
                        public final Symbol prepare(Object obj) {
                            boolean z7;
                            LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) obj;
                            if (this.getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() == state$external__kotlinx_coroutines__android_common__kotlinx_coroutines) {
                                z7 = true;
                            } else {
                                z7 = false;
                            }
                            if (z7) {
                                return null;
                            }
                            return LockFreeLinkedListKt.CONDITION_FALSE;
                        }
                    };
                    while (true) {
                        int tryCondAddNext2 = list.getPrevNode().tryCondAddNext(jobNode2, list, jobSupport$addLastAtomic$$inlined$addLastIf$12);
                        if (tryCondAddNext2 != 1) {
                            if (tryCondAddNext2 == 2) {
                                break;
                            }
                        } else {
                            z6 = true;
                            break;
                        }
                    }
                    if (z6) {
                        return jobNode2;
                    }
                }
            } else {
                if (z2) {
                    if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof CompletedExceptionally) {
                        completedExceptionally = (CompletedExceptionally) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines;
                    } else {
                        completedExceptionally = null;
                    }
                    if (completedExceptionally != null) {
                        th2 = completedExceptionally.cause;
                    }
                    jobNode.invoke((JobNode) th2);
                }
                return NonDisposableHandle.INSTANCE;
            }
        }
    }

    public boolean isScopedCoroutine() {
        return this instanceof BlockingCoroutine;
    }

    public void onCompletionInternal(Object obj) {
    }

    public void onStart() {
    }

    public static String stateString(Object obj) {
        boolean z;
        if (obj instanceof Finishing) {
            Finishing finishing = (Finishing) obj;
            if (finishing.isCancelling()) {
                return "Cancelling";
            }
            AtomicBoolean atomicBoolean = finishing._isCompleting;
            Objects.requireNonNull(atomicBoolean);
            if (atomicBoolean._value != 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                return "Completing";
            }
        } else if (obj instanceof Incomplete) {
            if (!((Incomplete) obj).isActive()) {
                return "New";
            }
        } else if (obj instanceof CompletedExceptionally) {
            return "Cancelled";
        } else {
            return "Completed";
        }
        return "Active";
    }

    @Override // kotlinx.coroutines.Job
    public final ChildHandle attachChild(JobSupport jobSupport) {
        return (ChildHandle) Job.DefaultImpls.invokeOnCompletion$default(this, true, new ChildHandleNode(jobSupport), 2);
    }

    @Override // kotlinx.coroutines.Job
    public final void cancel(CancellationException cancellationException) {
        if (cancellationException == null) {
            cancellationException = new JobCancellationException(cancellationExceptionMessage(), null, this);
        }
        cancelImpl$external__kotlinx_coroutines__android_common__kotlinx_coroutines(cancellationException);
    }

    /* JADX WARN: Code restructure failed: missing block: B:70:0x00fa, code lost:
        r0 = r9;
     */
    /* JADX WARN: Removed duplicated region for block: B:85:0x00d0 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x004a A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean cancelImpl$external__kotlinx_coroutines__android_common__kotlinx_coroutines(java.lang.Object r9) {
        /*
            Method dump skipped, instructions count: 271
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.JobSupport.cancelImpl$external__kotlinx_coroutines__android_common__kotlinx_coroutines(java.lang.Object):boolean");
    }

    public boolean childCancelled(Throwable th) {
        if (th instanceof CancellationException) {
            return true;
        }
        if (!cancelImpl$external__kotlinx_coroutines__android_common__kotlinx_coroutines(th) || !getHandlesException$external__kotlinx_coroutines__android_common__kotlinx_coroutines()) {
            return false;
        }
        return true;
    }

    public final void completeStateFinalization(Incomplete incomplete, Object obj) {
        CompletedExceptionally completedExceptionally;
        Throwable th;
        CompletionHandlerException completionHandlerException;
        AtomicRef<ChildHandle> atomicRef = this._parentHandle;
        Objects.requireNonNull(atomicRef);
        ChildHandle childHandle = atomicRef.value;
        if (childHandle != null) {
            childHandle.dispose();
            this._parentHandle.setValue(NonDisposableHandle.INSTANCE);
        }
        if (obj instanceof CompletedExceptionally) {
            completedExceptionally = (CompletedExceptionally) obj;
        } else {
            completedExceptionally = null;
        }
        if (completedExceptionally == null) {
            th = null;
        } else {
            th = completedExceptionally.cause;
        }
        if (incomplete instanceof JobNode) {
            try {
                ((JobNode) incomplete).invoke(th);
            } catch (Throwable th2) {
                handleOnCompletionException$external__kotlinx_coroutines__android_common__kotlinx_coroutines(new CompletionHandlerException("Exception in completion handler " + incomplete + " for " + this, th2));
            }
        } else {
            NodeList list = incomplete.getList();
            if (list != null) {
                CompletionHandlerException completionHandlerException2 = null;
                for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) list.getNext(); !Intrinsics.areEqual(lockFreeLinkedListNode, list); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
                    if (lockFreeLinkedListNode instanceof JobNode) {
                        JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                        try {
                            jobNode.invoke(th);
                        } catch (Throwable th3) {
                            if (completionHandlerException2 == null) {
                                completionHandlerException = null;
                            } else {
                                ExceptionsKt.addSuppressed(completionHandlerException2, th3);
                                completionHandlerException = completionHandlerException2;
                            }
                            if (completionHandlerException == null) {
                                completionHandlerException2 = new CompletionHandlerException("Exception in completion handler " + jobNode + " for " + this, th3);
                            }
                        }
                    }
                }
                if (completionHandlerException2 != null) {
                    handleOnCompletionException$external__kotlinx_coroutines__android_common__kotlinx_coroutines(completionHandlerException2);
                }
            }
        }
    }

    public final Throwable createCauseException(Object obj) {
        boolean z;
        if (obj == null) {
            z = true;
        } else {
            z = obj instanceof Throwable;
        }
        if (z) {
            Throwable th = (Throwable) obj;
            if (th == null) {
                return new JobCancellationException(cancellationExceptionMessage(), null, this);
            }
            return th;
        }
        Objects.requireNonNull(obj, "null cannot be cast to non-null type kotlinx.coroutines.ParentJob");
        return ((ParentJob) obj).getChildJobCancellationCause();
    }

    public final Object finalizeFinishingState(Finishing finishing, Object obj) {
        CompletedExceptionally completedExceptionally;
        Throwable finalRootCause;
        Object obj2;
        boolean z = DebugKt.DEBUG;
        Throwable th = null;
        if (obj instanceof CompletedExceptionally) {
            completedExceptionally = (CompletedExceptionally) obj;
        } else {
            completedExceptionally = null;
        }
        if (completedExceptionally != null) {
            th = completedExceptionally.cause;
        }
        synchronized (finishing) {
            finishing.isCancelling();
            ArrayList sealLocked = finishing.sealLocked(th);
            finalRootCause = getFinalRootCause(finishing, sealLocked);
            if (finalRootCause != null) {
                addSuppressedExceptions(finalRootCause, sealLocked);
            }
        }
        boolean z2 = false;
        if (!(finalRootCause == null || finalRootCause == th)) {
            obj = new CompletedExceptionally(finalRootCause, false);
        }
        if (finalRootCause != null) {
            if (cancelParent(finalRootCause) || handleJobException(finalRootCause)) {
                z2 = true;
            }
            if (z2) {
                Objects.requireNonNull(obj, "null cannot be cast to non-null type kotlinx.coroutines.CompletedExceptionally");
                ((CompletedExceptionally) obj)._handled.compareAndSet();
            }
        }
        onCompletionInternal(obj);
        AtomicRef<Object> atomicRef = this._state;
        if (obj instanceof Incomplete) {
            obj2 = new IncompleteStateBox((Incomplete) obj);
        } else {
            obj2 = obj;
        }
        atomicRef.compareAndSet(finishing, obj2);
        completeStateFinalization(finishing, obj);
        return obj;
    }

    public final Object getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        AtomicRef<Object> atomicRef = this._state;
        while (true) {
            Objects.requireNonNull(atomicRef);
            Object obj = atomicRef.value;
            if (!(obj instanceof OpDescriptor)) {
                return obj;
            }
            ((OpDescriptor) obj).perform(this);
        }
    }

    public final void initParentJob(Job job) {
        boolean z = DebugKt.DEBUG;
        if (job == null) {
            this._parentHandle.setValue(NonDisposableHandle.INSTANCE);
            return;
        }
        job.start();
        ChildHandle attachChild = job.attachChild(this);
        this._parentHandle.setValue(attachChild);
        if (!(getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() instanceof Incomplete)) {
            attachChild.dispose();
            this._parentHandle.setValue(NonDisposableHandle.INSTANCE);
        }
    }

    public final void promoteSingleToNodeList(JobNode jobNode) {
        NodeList nodeList = new NodeList();
        Objects.requireNonNull(jobNode);
        nodeList._prev.lazySet(jobNode);
        nodeList._next.lazySet(jobNode);
        while (true) {
            if (jobNode.getNext() == jobNode) {
                if (jobNode._next.compareAndSet(jobNode, nodeList)) {
                    nodeList.finishAdd(jobNode);
                    break;
                }
            } else {
                break;
            }
        }
        this._state.compareAndSet(jobNode, jobNode.getNextNode());
    }

    public final int startInternal(Object obj) {
        if (obj instanceof Empty) {
            Empty empty = (Empty) obj;
            Objects.requireNonNull(empty);
            if (empty.isActive) {
                return 0;
            }
            if (!this._state.compareAndSet(obj, JobSupportKt.EMPTY_ACTIVE)) {
                return -1;
            }
            onStart();
            return 1;
        } else if (!(obj instanceof InactiveNodeList)) {
            return 0;
        } else {
            AtomicRef<Object> atomicRef = this._state;
            InactiveNodeList inactiveNodeList = (InactiveNodeList) obj;
            Objects.requireNonNull(inactiveNodeList);
            if (!atomicRef.compareAndSet(obj, inactiveNodeList.list)) {
                return -1;
            }
            onStart();
            return 1;
        }
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nameString$external__kotlinx_coroutines__android_common__kotlinx_coroutines() + '{' + stateString(getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines()) + '}');
        sb.append('@');
        sb.append(DebugStringsKt.getHexAddress(this));
        return sb.toString();
    }

    public final Object tryMakeCompleting(Object obj, Object obj2) {
        Finishing finishing;
        CompletedExceptionally completedExceptionally;
        ChildHandleNode childHandleNode;
        Object obj3;
        if (!(obj instanceof Incomplete)) {
            return JobSupportKt.COMPLETING_ALREADY;
        }
        boolean z = false;
        if (((obj instanceof Empty) || (obj instanceof JobNode)) && !(obj instanceof ChildHandleNode) && !(obj2 instanceof CompletedExceptionally)) {
            Incomplete incomplete = (Incomplete) obj;
            boolean z2 = DebugKt.DEBUG;
            AtomicRef<Object> atomicRef = this._state;
            if (obj2 instanceof Incomplete) {
                obj3 = new IncompleteStateBox((Incomplete) obj2);
            } else {
                obj3 = obj2;
            }
            if (atomicRef.compareAndSet(incomplete, obj3)) {
                onCompletionInternal(obj2);
                completeStateFinalization(incomplete, obj2);
                z = true;
            }
            if (z) {
                return obj2;
            }
            return JobSupportKt.COMPLETING_RETRY;
        }
        Incomplete incomplete2 = (Incomplete) obj;
        NodeList orPromoteCancellingList = getOrPromoteCancellingList(incomplete2);
        if (orPromoteCancellingList == null) {
            return JobSupportKt.COMPLETING_RETRY;
        }
        ChildHandleNode childHandleNode2 = null;
        if (incomplete2 instanceof Finishing) {
            finishing = (Finishing) incomplete2;
        } else {
            finishing = null;
        }
        if (finishing == null) {
            finishing = new Finishing(orPromoteCancellingList, null);
        }
        synchronized (finishing) {
            AtomicBoolean atomicBoolean = finishing._isCompleting;
            Objects.requireNonNull(atomicBoolean);
            if (atomicBoolean._value != 0) {
                z = true;
            }
            if (z) {
                return JobSupportKt.COMPLETING_ALREADY;
            }
            AtomicBoolean atomicBoolean2 = finishing._isCompleting;
            Objects.requireNonNull(atomicBoolean2);
            int i = InterceptorKt.$r8$clinit;
            atomicBoolean2._value = 1;
            TraceBase traceBase = atomicBoolean2.trace;
            if (traceBase != TraceBase.None.INSTANCE) {
                Objects.requireNonNull(traceBase);
            }
            if (finishing == incomplete2 || this._state.compareAndSet(incomplete2, finishing)) {
                boolean z3 = DebugKt.DEBUG;
                boolean isCancelling = finishing.isCancelling();
                if (obj2 instanceof CompletedExceptionally) {
                    completedExceptionally = (CompletedExceptionally) obj2;
                } else {
                    completedExceptionally = null;
                }
                if (completedExceptionally != null) {
                    finishing.addExceptionLocked(completedExceptionally.cause);
                }
                Throwable rootCause = finishing.getRootCause();
                if (!(!isCancelling)) {
                    rootCause = null;
                }
                if (rootCause != null) {
                    notifyCancelling(orPromoteCancellingList, rootCause);
                }
                if (incomplete2 instanceof ChildHandleNode) {
                    childHandleNode = (ChildHandleNode) incomplete2;
                } else {
                    childHandleNode = null;
                }
                if (childHandleNode == null) {
                    NodeList list = incomplete2.getList();
                    if (list != null) {
                        childHandleNode2 = nextChild(list);
                    }
                } else {
                    childHandleNode2 = childHandleNode;
                }
                if (childHandleNode2 == null || !tryWaitForChild(finishing, childHandleNode2, obj2)) {
                    return finalizeFinishingState(finishing, obj2);
                }
                return JobSupportKt.COMPLETING_WAITING_CHILDREN;
            }
            return JobSupportKt.COMPLETING_RETRY;
        }
    }

    public final boolean tryWaitForChild(Finishing finishing, ChildHandleNode childHandleNode, Object obj) {
        while (Job.DefaultImpls.invokeOnCompletion$default(childHandleNode.childJob, false, new ChildCompletion(this, finishing, childHandleNode, obj), 1) == NonDisposableHandle.INSTANCE) {
            childHandleNode = nextChild(childHandleNode);
            if (childHandleNode == null) {
                return false;
            }
        }
        return true;
    }

    public JobSupport(boolean z) {
        Empty empty;
        if (z) {
            empty = JobSupportKt.EMPTY_ACTIVE;
        } else {
            empty = JobSupportKt.EMPTY_NEW;
        }
        this._state = AtomicFU.atomic(empty);
        this._parentHandle = AtomicFU.atomic(null);
    }

    public static void addSuppressedExceptions(Throwable th, ArrayList arrayList) {
        Throwable th2;
        if (arrayList.size() > 1) {
            Set newSetFromMap = Collections.newSetFromMap(new IdentityHashMap(arrayList.size()));
            if (!DebugKt.RECOVER_STACK_TRACES) {
                th2 = th;
            } else {
                th2 = StackTraceRecoveryKt.unwrapImpl(th);
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Throwable th3 = (Throwable) it.next();
                if (DebugKt.RECOVER_STACK_TRACES) {
                    th3 = StackTraceRecoveryKt.unwrapImpl(th3);
                }
                if (th3 != th && th3 != th2 && !(th3 instanceof CancellationException) && newSetFromMap.add(th3)) {
                    ExceptionsKt.addSuppressed(th, th3);
                }
            }
        }
    }

    public static ChildHandleNode nextChild(LockFreeLinkedListNode lockFreeLinkedListNode) {
        while (lockFreeLinkedListNode.isRemoved()) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getPrevNode();
        }
        while (true) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode();
            if (!lockFreeLinkedListNode.isRemoved()) {
                if (lockFreeLinkedListNode instanceof ChildHandleNode) {
                    return (ChildHandleNode) lockFreeLinkedListNode;
                }
                if (lockFreeLinkedListNode instanceof NodeList) {
                    return null;
                }
            }
        }
    }

    public final boolean cancelParent(Throwable th) {
        if (isScopedCoroutine()) {
            return true;
        }
        boolean z = th instanceof CancellationException;
        AtomicRef<ChildHandle> atomicRef = this._parentHandle;
        Objects.requireNonNull(atomicRef);
        ChildHandle childHandle = atomicRef.value;
        if (childHandle == null || childHandle == NonDisposableHandle.INSTANCE) {
            return z;
        }
        if (childHandle.childCancelled(th) || z) {
            return true;
        }
        return false;
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public final <R> R fold(R r, Function2<? super R, ? super CoroutineContext.Element, ? extends R> function2) {
        return (R) function2.invoke(r, this);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public final <E extends CoroutineContext.Element> E get(CoroutineContext.Key<E> key) {
        return (E) CoroutineContext.Element.DefaultImpls.get(this, key);
    }

    @Override // kotlinx.coroutines.Job
    public final CancellationException getCancellationException() {
        Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        CancellationException cancellationException = null;
        if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Finishing) {
            Throwable rootCause = ((Finishing) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).getRootCause();
            if (rootCause != null) {
                String stringPlus = Intrinsics.stringPlus(DebugStringsKt.getClassSimpleName(this), " is cancelling");
                if (rootCause instanceof CancellationException) {
                    cancellationException = (CancellationException) rootCause;
                }
                if (cancellationException == null) {
                    if (stringPlus == null) {
                        stringPlus = cancellationExceptionMessage();
                    }
                    cancellationException = new JobCancellationException(stringPlus, rootCause, this);
                }
            }
            if (cancellationException != null) {
                return cancellationException;
            }
            throw new IllegalStateException(Intrinsics.stringPlus("Job is still new or active: ", this).toString());
        } else if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Incomplete) {
            throw new IllegalStateException(Intrinsics.stringPlus("Job is still new or active: ", this).toString());
        } else if (!(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof CompletedExceptionally)) {
            return new JobCancellationException(Intrinsics.stringPlus(DebugStringsKt.getClassSimpleName(this), " has completed normally"), null, this);
        } else {
            Throwable th = ((CompletedExceptionally) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).cause;
            if (th instanceof CancellationException) {
                cancellationException = (CancellationException) th;
            }
            if (cancellationException == null) {
                return new JobCancellationException(cancellationExceptionMessage(), th, this);
            }
            return cancellationException;
        }
    }

    @Override // kotlinx.coroutines.ParentJob
    public final CancellationException getChildJobCancellationCause() {
        Throwable th;
        Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        CancellationException cancellationException = null;
        if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Finishing) {
            th = ((Finishing) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).getRootCause();
        } else if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof CompletedExceptionally) {
            th = ((CompletedExceptionally) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).cause;
        } else if (!(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Incomplete)) {
            th = null;
        } else {
            throw new IllegalStateException(Intrinsics.stringPlus("Cannot be cancelling child in this state: ", state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).toString());
        }
        if (th instanceof CancellationException) {
            cancellationException = (CancellationException) th;
        }
        if (cancellationException == null) {
            return new JobCancellationException(Intrinsics.stringPlus("Parent job is ", stateString(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines)), th, this);
        }
        return cancellationException;
    }

    public final Throwable getFinalRootCause(Finishing finishing, ArrayList arrayList) {
        Object obj;
        boolean z;
        Object obj2 = null;
        if (!arrayList.isEmpty()) {
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it.next();
                if (!(((Throwable) obj) instanceof CancellationException)) {
                    break;
                }
            }
            Throwable th = (Throwable) obj;
            if (th != null) {
                return th;
            }
            Throwable th2 = (Throwable) arrayList.get(0);
            if (th2 instanceof TimeoutCancellationException) {
                Iterator it2 = arrayList.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    Object next = it2.next();
                    Throwable th3 = (Throwable) next;
                    if (th3 == th2 || !(th3 instanceof TimeoutCancellationException)) {
                        z = false;
                        continue;
                    } else {
                        z = true;
                        continue;
                    }
                    if (z) {
                        obj2 = next;
                        break;
                    }
                }
                Throwable th4 = (Throwable) obj2;
                if (th4 != null) {
                    return th4;
                }
            }
            return th2;
        } else if (finishing.isCancelling()) {
            return new JobCancellationException(cancellationExceptionMessage(), null, this);
        } else {
            return null;
        }
    }

    public final NodeList getOrPromoteCancellingList(Incomplete incomplete) {
        NodeList list = incomplete.getList();
        if (list != null) {
            return list;
        }
        if (incomplete instanceof Empty) {
            return new NodeList();
        }
        if (incomplete instanceof JobNode) {
            promoteSingleToNodeList((JobNode) incomplete);
            return null;
        }
        throw new IllegalStateException(Intrinsics.stringPlus("State should have list: ", incomplete).toString());
    }

    @Override // kotlinx.coroutines.Job
    public boolean isActive() {
        Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        if (!(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Incomplete) || !((Incomplete) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).isActive()) {
            return false;
        }
        return true;
    }

    @Override // kotlinx.coroutines.Job
    public final Object join(Continuation<? super Unit> continuation) {
        boolean z;
        while (true) {
            Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
            if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof Incomplete) {
                if (startInternal(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines) >= 0) {
                    z = true;
                    break;
                }
            } else {
                z = false;
                break;
            }
        }
        if (!z) {
            JobKt.ensureActive(continuation.getContext());
            return Unit.INSTANCE;
        }
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(R$color.intercepted(continuation), 1);
        cancellableContinuationImpl.initCancellability();
        cancellableContinuationImpl.invokeOnCancellation(new DisposeOnCancel(invokeOnCompletion(false, true, new ResumeOnCompletion(cancellableContinuationImpl))));
        Object result = cancellableContinuationImpl.getResult();
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        if (result != coroutineSingletons) {
            result = Unit.INSTANCE;
        }
        if (result == coroutineSingletons) {
            return result;
        }
        return Unit.INSTANCE;
    }

    public final Object makeCompletingOnce$external__kotlinx_coroutines__android_common__kotlinx_coroutines(Object obj) {
        Object tryMakeCompleting;
        CompletedExceptionally completedExceptionally;
        do {
            tryMakeCompleting = tryMakeCompleting(getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines(), obj);
            if (tryMakeCompleting == JobSupportKt.COMPLETING_ALREADY) {
                String str = "Job " + this + " is already complete or completing, but is being completed with " + obj;
                Throwable th = null;
                if (obj instanceof CompletedExceptionally) {
                    completedExceptionally = (CompletedExceptionally) obj;
                } else {
                    completedExceptionally = null;
                }
                if (completedExceptionally != null) {
                    th = completedExceptionally.cause;
                }
                throw new IllegalStateException(str, th);
            }
        } while (tryMakeCompleting == JobSupportKt.COMPLETING_RETRY);
        return tryMakeCompleting;
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public final CoroutineContext minusKey(CoroutineContext.Key<?> key) {
        return CoroutineContext.Element.DefaultImpls.minusKey(this, key);
    }

    public String nameString$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return DebugStringsKt.getClassSimpleName(this);
    }

    public final void notifyCancelling(NodeList nodeList, Throwable th) {
        CompletionHandlerException completionHandlerException;
        CompletionHandlerException completionHandlerException2 = null;
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) nodeList.getNext(); !Intrinsics.areEqual(lockFreeLinkedListNode, nodeList); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            if (lockFreeLinkedListNode instanceof JobCancellingNode) {
                JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                try {
                    jobNode.invoke(th);
                } catch (Throwable th2) {
                    if (completionHandlerException2 == null) {
                        completionHandlerException = null;
                    } else {
                        ExceptionsKt.addSuppressed(completionHandlerException2, th2);
                        completionHandlerException = completionHandlerException2;
                    }
                    if (completionHandlerException == null) {
                        completionHandlerException2 = new CompletionHandlerException("Exception in completion handler " + jobNode + " for " + this, th2);
                    }
                }
            }
        }
        if (completionHandlerException2 != null) {
            handleOnCompletionException$external__kotlinx_coroutines__android_common__kotlinx_coroutines(completionHandlerException2);
        }
        cancelParent(th);
    }

    @Override // kotlin.coroutines.CoroutineContext
    public final CoroutineContext plus(CoroutineContext coroutineContext) {
        return CoroutineContext.DefaultImpls.plus(this, coroutineContext);
    }

    @Override // kotlinx.coroutines.Job
    public final boolean start() {
        int startInternal;
        do {
            startInternal = startInternal(getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines());
            if (startInternal == 0) {
                return false;
            }
        } while (startInternal != 1);
        return true;
    }

    public void handleOnCompletionException$external__kotlinx_coroutines__android_common__kotlinx_coroutines(CompletionHandlerException completionHandlerException) {
        throw completionHandlerException;
    }

    @Override // kotlinx.coroutines.ChildJob
    public final void parentCancelled(JobSupport jobSupport) {
        cancelImpl$external__kotlinx_coroutines__android_common__kotlinx_coroutines(jobSupport);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element
    public final CoroutineContext.Key<?> getKey() {
        return Job.Key.$$INSTANCE;
    }
}
