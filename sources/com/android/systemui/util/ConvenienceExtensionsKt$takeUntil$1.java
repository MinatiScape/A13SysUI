package com.android.systemui.util;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;
/* compiled from: ConvenienceExtensions.kt */
@DebugMetadata(c = "com.android.systemui.util.ConvenienceExtensionsKt$takeUntil$1", f = "ConvenienceExtensions.kt", l = {32}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class ConvenienceExtensionsKt$takeUntil$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<Object>, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Function1<Object, Boolean> $pred;
    public final /* synthetic */ Sequence<Object> $this_takeUntil;
    private /* synthetic */ Object L$0;
    public Object L$1;
    public Object L$2;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ConvenienceExtensionsKt$takeUntil$1(Sequence<Object> sequence, Function1<Object, Boolean> function1, Continuation<? super ConvenienceExtensionsKt$takeUntil$1> continuation) {
        super(continuation);
        this.$this_takeUntil = sequence;
        this.$pred = function1;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        ConvenienceExtensionsKt$takeUntil$1 convenienceExtensionsKt$takeUntil$1 = new ConvenienceExtensionsKt$takeUntil$1(this.$this_takeUntil, this.$pred, continuation);
        convenienceExtensionsKt$takeUntil$1.L$0 = obj;
        return convenienceExtensionsKt$takeUntil$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(SequenceScope<Object> sequenceScope, Continuation<? super Unit> continuation) {
        return ((ConvenienceExtensionsKt$takeUntil$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0054, code lost:
        if (r6.$pred.invoke(r1).booleanValue() == false) goto L_0x0030;
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:12:0x0045 -> B:14:0x0048). Please submit an issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r7) {
        /*
            r6 = this;
            kotlin.coroutines.intrinsics.CoroutineSingletons r0 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r1 = r6.label
            r2 = 1
            if (r1 == 0) goto L_0x0020
            if (r1 != r2) goto L_0x0018
            java.lang.Object r1 = r6.L$2
            java.lang.Object r3 = r6.L$1
            java.util.Iterator r3 = (java.util.Iterator) r3
            java.lang.Object r4 = r6.L$0
            kotlin.sequences.SequenceScope r4 = (kotlin.sequences.SequenceScope) r4
            kotlin.ResultKt.throwOnFailure(r7)
            r7 = r0
            goto L_0x0048
        L_0x0018:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>(r7)
            throw r6
        L_0x0020:
            kotlin.ResultKt.throwOnFailure(r7)
            java.lang.Object r7 = r6.L$0
            kotlin.sequences.SequenceScope r7 = (kotlin.sequences.SequenceScope) r7
            kotlin.sequences.Sequence<java.lang.Object> r1 = r6.$this_takeUntil
            java.util.Iterator r1 = r1.iterator()
            r4 = r7
            r7 = r0
            r3 = r1
        L_0x0030:
            boolean r1 = r3.hasNext()
            if (r1 == 0) goto L_0x0056
            java.lang.Object r1 = r3.next()
            r6.L$0 = r4
            r6.L$1 = r3
            r6.L$2 = r1
            r6.label = r2
            r4.yield(r1, r6)
            if (r0 != r7) goto L_0x0048
            return r7
        L_0x0048:
            kotlin.jvm.functions.Function1<java.lang.Object, java.lang.Boolean> r5 = r6.$pred
            java.lang.Object r1 = r5.invoke(r1)
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x0030
        L_0x0056:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.util.ConvenienceExtensionsKt$takeUntil$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
