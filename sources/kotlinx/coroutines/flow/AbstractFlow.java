package kotlinx.coroutines.flow;
/* compiled from: Flow.kt */
/* loaded from: classes.dex */
public abstract class AbstractFlow<T> implements Flow<T> {
    /* JADX WARN: Removed duplicated region for block: B:10:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0035  */
    @Override // kotlinx.coroutines.flow.Flow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> r5, kotlin.coroutines.Continuation<? super kotlin.Unit> r6) {
        /*
            r4 = this;
            boolean r0 = r6 instanceof kotlinx.coroutines.flow.AbstractFlow$collect$1
            if (r0 == 0) goto L_0x0013
            r0 = r6
            kotlinx.coroutines.flow.AbstractFlow$collect$1 r0 = (kotlinx.coroutines.flow.AbstractFlow$collect$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0013
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x0018
        L_0x0013:
            kotlinx.coroutines.flow.AbstractFlow$collect$1 r0 = new kotlinx.coroutines.flow.AbstractFlow$collect$1
            r0.<init>(r4, r6)
        L_0x0018:
            java.lang.Object r6 = r0.result
            kotlin.coroutines.intrinsics.CoroutineSingletons r1 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L_0x0035
            if (r2 != r3) goto L_0x002d
            java.lang.Object r4 = r0.L$0
            kotlinx.coroutines.flow.internal.SafeCollector r4 = (kotlinx.coroutines.flow.internal.SafeCollector) r4
            kotlin.ResultKt.throwOnFailure(r6)     // Catch: all -> 0x002b
            goto L_0x0056
        L_0x002b:
            r5 = move-exception
            goto L_0x005f
        L_0x002d:
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
            java.lang.String r5 = "call to 'resume' before 'invoke' with coroutine"
            r4.<init>(r5)
            throw r4
        L_0x0035:
            kotlin.ResultKt.throwOnFailure(r6)
            kotlinx.coroutines.flow.internal.SafeCollector r6 = new kotlinx.coroutines.flow.internal.SafeCollector
            kotlin.coroutines.CoroutineContext r2 = r0.getContext()
            r6.<init>(r5, r2)
            r0.L$0 = r6     // Catch: all -> 0x005c
            r0.label = r3     // Catch: all -> 0x005c
            kotlinx.coroutines.flow.SafeFlow r4 = (kotlinx.coroutines.flow.SafeFlow) r4     // Catch: all -> 0x005c
            kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super T>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> r4 = r4.block     // Catch: all -> 0x005c
            java.lang.Object r4 = r4.invoke(r6, r0)     // Catch: all -> 0x005c
            if (r4 != r1) goto L_0x0050
            goto L_0x0052
        L_0x0050:
            kotlin.Unit r4 = kotlin.Unit.INSTANCE     // Catch: all -> 0x005c
        L_0x0052:
            if (r4 != r1) goto L_0x0055
            return r1
        L_0x0055:
            r4 = r6
        L_0x0056:
            r4.releaseIntercepted()
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            return r4
        L_0x005c:
            r4 = move-exception
            r5 = r4
            r4 = r6
        L_0x005f:
            r4.releaseIntercepted()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.AbstractFlow.collect(kotlinx.coroutines.flow.FlowCollector, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
