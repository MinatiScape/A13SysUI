package kotlinx.coroutines.flow;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
/* compiled from: Share.kt */
/* loaded from: classes.dex */
public final class SubscribedFlowCollector<T> implements FlowCollector<T> {
    @Override // kotlinx.coroutines.flow.FlowCollector
    public final Object emit(T t, Continuation<? super Unit> continuation) {
        throw null;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x001f  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0046  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final kotlin.Unit onSubscription(kotlin.coroutines.Continuation r6) {
        /*
            r5 = this;
            boolean r0 = r6 instanceof kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1
            if (r0 == 0) goto L_0x0013
            r0 = r6
            kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1 r0 = (kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0013
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x0018
        L_0x0013:
            kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1 r0 = new kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1
            r0.<init>(r5, r6)
        L_0x0018:
            java.lang.Object r6 = r0.result
            int r1 = r0.label
            r2 = 1
            if (r1 == 0) goto L_0x0046
            if (r1 == r2) goto L_0x0032
            r5 = 2
            if (r1 != r5) goto L_0x002a
            kotlin.ResultKt.throwOnFailure(r6)
        L_0x0027:
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            return r5
        L_0x002a:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L_0x0032:
            java.lang.Object r5 = r0.L$1
            kotlinx.coroutines.flow.internal.SafeCollector r5 = (kotlinx.coroutines.flow.internal.SafeCollector) r5
            java.lang.Object r0 = r0.L$0
            kotlinx.coroutines.flow.SubscribedFlowCollector r0 = (kotlinx.coroutines.flow.SubscribedFlowCollector) r0
            kotlin.ResultKt.throwOnFailure(r6)     // Catch: all -> 0x0044
            r5.releaseIntercepted()
            java.util.Objects.requireNonNull(r0)
            goto L_0x0027
        L_0x0044:
            r6 = move-exception
            goto L_0x005e
        L_0x0046:
            kotlin.ResultKt.throwOnFailure(r6)
            kotlinx.coroutines.flow.internal.SafeCollector r6 = new kotlinx.coroutines.flow.internal.SafeCollector
            kotlin.coroutines.CoroutineContext r1 = r0.getContext()
            r3 = 0
            r6.<init>(r3, r1)
            r0.L$0 = r5     // Catch: all -> 0x005a
            r0.L$1 = r6     // Catch: all -> 0x005a
            r0.label = r2     // Catch: all -> 0x005a
            throw r3     // Catch: all -> 0x005a
        L_0x005a:
            r5 = move-exception
            r4 = r6
            r6 = r5
            r5 = r4
        L_0x005e:
            r5.releaseIntercepted()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.SubscribedFlowCollector.onSubscription(kotlin.coroutines.Continuation):kotlin.Unit");
    }
}
