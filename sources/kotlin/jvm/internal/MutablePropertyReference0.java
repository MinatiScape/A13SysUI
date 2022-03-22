package kotlin.jvm.internal;

import java.util.Objects;
import kotlin.reflect.KCallable;
import kotlin.reflect.KMutableProperty0;
import kotlin.reflect.KProperty0;
/* loaded from: classes.dex */
public abstract class MutablePropertyReference0 extends MutablePropertyReference implements KMutableProperty0 {
    public MutablePropertyReference0() {
    }

    public MutablePropertyReference0(Object obj, Class cls, String str, String str2) {
        super(obj, cls, str, str2);
    }

    @Override // kotlin.jvm.internal.CallableReference
    public final KCallable computeReflected() {
        Objects.requireNonNull(Reflection.factory);
        return this;
    }

    @Override // kotlin.reflect.KProperty0
    public final KProperty0.Getter getGetter() {
        return ((KMutableProperty0) getReflected()).getGetter();
    }

    @Override // kotlin.jvm.functions.Function0
    public final Object invoke() {
        return get();
    }
}
