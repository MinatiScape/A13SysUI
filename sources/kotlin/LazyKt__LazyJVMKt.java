package kotlin;

import kotlin.jvm.functions.Function0;
/* compiled from: LazyJVM.kt */
/* loaded from: classes.dex */
public class LazyKt__LazyJVMKt {
    public static final <T> Lazy<T> lazy(Function0<? extends T> function0) {
        return new SynchronizedLazyImpl(function0);
    }

    public static final Lazy lazy$1(Function0 function0) {
        return new SafePublicationLazyImpl(function0);
    }
}
