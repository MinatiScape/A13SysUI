package kotlinx.coroutines.internal;

import java.lang.reflect.Constructor;
import kotlin.Result;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ExceptionsConstuctor.kt */
/* loaded from: classes.dex */
public final class ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$3 extends Lambda implements Function1<Throwable, Throwable> {
    public final /* synthetic */ Constructor $constructor$inlined;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$3(Constructor constructor) {
        super(1);
        this.$constructor$inlined = constructor;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v3, types: [kotlin.Result$Failure] */
    @Override // kotlin.jvm.functions.Function1
    public final Throwable invoke(Throwable th) {
        Throwable th2;
        Object newInstance;
        Throwable th3 = th;
        try {
            newInstance = this.$constructor$inlined.newInstance(th3.getMessage());
        } catch (Throwable th4) {
            th2 = new Result.Failure(th4);
        }
        if (newInstance != null) {
            th2 = (Throwable) newInstance;
            th2.initCause(th3);
            if (th2 instanceof Result.Failure) {
                th2 = null;
            }
            return th2;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Throwable");
    }
}
