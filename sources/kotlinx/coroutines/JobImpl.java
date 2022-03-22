package kotlinx.coroutines;

import java.util.Objects;
import kotlinx.atomicfu.AtomicRef;
/* compiled from: JobSupport.kt */
/* loaded from: classes.dex */
public class JobImpl extends JobSupport {
    public final boolean handlesException;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public JobImpl(Job job) {
        super(true);
        ChildHandleNode childHandleNode;
        JobSupport jobSupport;
        ChildHandleNode childHandleNode2;
        boolean z = true;
        initParentJob(job);
        AtomicRef<ChildHandle> atomicRef = this._parentHandle;
        Objects.requireNonNull(atomicRef);
        ChildHandle childHandle = atomicRef.value;
        if (childHandle instanceof ChildHandleNode) {
            childHandleNode = (ChildHandleNode) childHandle;
        } else {
            childHandleNode = null;
        }
        if (childHandleNode == null) {
            jobSupport = null;
        } else {
            jobSupport = childHandleNode.getJob();
        }
        if (jobSupport != null) {
            while (!jobSupport.getHandlesException$external__kotlinx_coroutines__android_common__kotlinx_coroutines()) {
                AtomicRef<ChildHandle> atomicRef2 = jobSupport._parentHandle;
                Objects.requireNonNull(atomicRef2);
                ChildHandle childHandle2 = atomicRef2.value;
                if (childHandle2 instanceof ChildHandleNode) {
                    childHandleNode2 = (ChildHandleNode) childHandle2;
                } else {
                    childHandleNode2 = null;
                }
                if (childHandleNode2 == null) {
                    jobSupport = null;
                    continue;
                } else {
                    jobSupport = childHandleNode2.getJob();
                    continue;
                }
                if (jobSupport == null) {
                    z = false;
                    break;
                }
            }
        } else {
            z = false;
            break;
        }
        this.handlesException = z;
    }

    @Override // kotlinx.coroutines.JobSupport
    public final boolean getOnCancelComplete$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return true;
    }

    @Override // kotlinx.coroutines.JobSupport
    public final boolean getHandlesException$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return this.handlesException;
    }
}
