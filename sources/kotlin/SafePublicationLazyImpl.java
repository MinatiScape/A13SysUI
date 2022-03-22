package kotlin;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.functions.Function0;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: LazyJVM.kt */
/* loaded from: classes.dex */
public final class SafePublicationLazyImpl<T> implements Lazy<T>, Serializable {
    public static final AtomicReferenceFieldUpdater<SafePublicationLazyImpl<?>, Object> valueUpdater = AtomicReferenceFieldUpdater.newUpdater(SafePublicationLazyImpl.class, Object.class, "_value");
    private volatile Object _value;

    /* renamed from: final  reason: not valid java name */
    private final Object f8final;
    private volatile Function0<? extends T> initializer;

    private final Object writeReplace() {
        return new InitializedLazyImpl(getValue());
    }

    @Override // kotlin.Lazy
    public final T getValue() {
        T t = (T) this._value;
        UNINITIALIZED_VALUE uninitialized_value = UNINITIALIZED_VALUE.INSTANCE;
        if (t != uninitialized_value) {
            return t;
        }
        Function0<? extends T> function0 = this.initializer;
        if (function0 != null) {
            T t2 = (T) function0.invoke();
            if (valueUpdater.compareAndSet(this, uninitialized_value, t2)) {
                this.initializer = null;
                return t2;
            }
        }
        return (T) this._value;
    }

    public final String toString() {
        boolean z;
        if (this._value != UNINITIALIZED_VALUE.INSTANCE) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return String.valueOf(getValue());
        }
        return "Lazy value not initialized yet.";
    }

    public SafePublicationLazyImpl(Function0<? extends T> function0) {
        this.initializer = function0;
        UNINITIALIZED_VALUE uninitialized_value = UNINITIALIZED_VALUE.INSTANCE;
        this._value = uninitialized_value;
        this.f8final = uninitialized_value;
    }
}
