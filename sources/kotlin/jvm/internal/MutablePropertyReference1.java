package kotlin.jvm.internal;

import java.util.Objects;
import kotlin.reflect.KCallable;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KProperty1;
/* loaded from: classes.dex */
public abstract class MutablePropertyReference1 extends MutablePropertyReference implements KMutableProperty1 {
    public MutablePropertyReference1() {
    }

    public MutablePropertyReference1(Object obj, Class cls, String str, String str2) {
        super(obj, cls, str, str2);
    }

    @Override // kotlin.jvm.internal.CallableReference
    public final KCallable computeReflected() {
        Objects.requireNonNull(Reflection.factory);
        return this;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Object invoke(Object obj) {
        return ((MutablePropertyReference1Impl) this).get(obj);
    }

    @Override // kotlin.reflect.KProperty1
    public final KProperty1.Getter getGetter() {
        return ((KMutableProperty1) getReflected()).getGetter();
    }
}
