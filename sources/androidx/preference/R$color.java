package androidx.preference;

import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.RestrictedContinuationImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.TypeIntrinsics;
/* loaded from: classes.dex */
public class R$color {
    public static final int[] CardView = {16843071, 16843072, 2130968737, 2130968738, 2130968739, 2130968742, 2130968743, 2130968745, 2130968871, 2130968872, 2130968874, 2130968875, 2130968877};

    public static final Continuation createCoroutineUnintercepted(final Function2 function2, final Object obj, final Continuation continuation) {
        if (function2 instanceof BaseContinuationImpl) {
            return ((BaseContinuationImpl) function2).create(obj, continuation);
        }
        final CoroutineContext context = continuation.getContext();
        if (context == EmptyCoroutineContext.INSTANCE) {
            return new RestrictedContinuationImpl(function2, obj) { // from class: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$3
                public final /* synthetic */ Object $receiver$inlined;
                public final /* synthetic */ Function2 $this_createCoroutineUnintercepted$inlined;
                private int label;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(Continuation.this);
                    this.$this_createCoroutineUnintercepted$inlined = function2;
                    this.$receiver$inlined = obj;
                }

                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                public final Object invokeSuspend(Object obj2) {
                    int i = this.label;
                    if (i == 0) {
                        this.label = 1;
                        ResultKt.throwOnFailure(obj2);
                        Function2 function22 = this.$this_createCoroutineUnintercepted$inlined;
                        TypeIntrinsics.beforeCheckcastToFunctionOfArity(function22, 2);
                        return function22.invoke(this.$receiver$inlined, this);
                    } else if (i == 1) {
                        this.label = 2;
                        ResultKt.throwOnFailure(obj2);
                        return obj2;
                    } else {
                        throw new IllegalStateException("This coroutine had already completed".toString());
                    }
                }
            };
        }
        return new ContinuationImpl(context, function2, obj) { // from class: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$4
            public final /* synthetic */ CoroutineContext $context;
            public final /* synthetic */ Object $receiver$inlined;
            public final /* synthetic */ Function2 $this_createCoroutineUnintercepted$inlined;
            private int label;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(Continuation.this, context);
                this.$context = context;
                this.$this_createCoroutineUnintercepted$inlined = function2;
                this.$receiver$inlined = obj;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final Object invokeSuspend(Object obj2) {
                int i = this.label;
                if (i == 0) {
                    this.label = 1;
                    ResultKt.throwOnFailure(obj2);
                    Function2 function22 = this.$this_createCoroutineUnintercepted$inlined;
                    TypeIntrinsics.beforeCheckcastToFunctionOfArity(function22, 2);
                    return function22.invoke(this.$receiver$inlined, this);
                } else if (i == 1) {
                    this.label = 2;
                    ResultKt.throwOnFailure(obj2);
                    return obj2;
                } else {
                    throw new IllegalStateException("This coroutine had already completed".toString());
                }
            }
        };
    }

    public static final Continuation intercepted(Continuation continuation) {
        ContinuationImpl continuationImpl;
        if (continuation instanceof ContinuationImpl) {
            continuationImpl = (ContinuationImpl) continuation;
        } else {
            continuationImpl = null;
        }
        if (continuationImpl != null && (continuation = continuationImpl.intercepted) == null) {
            ContinuationInterceptor continuationInterceptor = (ContinuationInterceptor) continuationImpl.getContext().get(ContinuationInterceptor.Key.$$INSTANCE);
            if (continuationInterceptor == null) {
                continuation = continuationImpl;
            } else {
                continuation = continuationInterceptor.interceptContinuation(continuationImpl);
            }
            continuationImpl.intercepted = continuation;
        }
        return continuation;
    }
}
