package kotlin.jvm.internal;

import java.io.Serializable;
import java.util.Objects;
/* compiled from: Lambda.kt */
/* loaded from: classes.dex */
public abstract class Lambda<R> implements FunctionBase<R>, Serializable {
    private final int arity;

    public final String toString() {
        Objects.requireNonNull(Reflection.factory);
        return ReflectionFactory.renderLambdaToString(this);
    }

    public Lambda(int i) {
        this.arity = i;
    }

    @Override // kotlin.jvm.internal.FunctionBase
    public final int getArity() {
        return this.arity;
    }
}
