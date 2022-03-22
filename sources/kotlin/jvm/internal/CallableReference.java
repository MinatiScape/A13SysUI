package kotlin.jvm.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Objects;
import kotlin.reflect.KCallable;
/* loaded from: classes.dex */
public abstract class CallableReference implements KCallable, Serializable {
    public static final Object NO_RECEIVER = NoReceiver.INSTANCE;
    private final boolean isTopLevel;
    private final String name;
    private final Class owner;
    public final Object receiver;
    public transient KCallable reflected;
    private final String signature;

    public CallableReference() {
        this(NO_RECEIVER, null, null, null, false);
    }

    public abstract KCallable computeReflected();

    public final KCallable compute() {
        KCallable kCallable = this.reflected;
        if (kCallable != null) {
            return kCallable;
        }
        KCallable computeReflected = computeReflected();
        this.reflected = computeReflected;
        return computeReflected;
    }

    public final ClassBasedDeclarationContainer getOwner() {
        Class cls = this.owner;
        if (cls == null) {
            return null;
        }
        if (!this.isTopLevel) {
            return Reflection.getOrCreateKotlinClass(cls);
        }
        Objects.requireNonNull(Reflection.factory);
        return new PackageReference(cls);
    }

    public CallableReference(Object obj, Class cls, String str, String str2, boolean z) {
        this.receiver = obj;
        this.owner = cls;
        this.name = str;
        this.signature = str2;
        this.isTopLevel = z;
    }

    /* loaded from: classes.dex */
    public static class NoReceiver implements Serializable {
        public static final NoReceiver INSTANCE = new NoReceiver();

        private NoReceiver() {
        }

        private Object readResolve() throws ObjectStreamException {
            return INSTANCE;
        }
    }

    @Override // kotlin.reflect.KCallable
    public final String getName() {
        return this.name;
    }

    public final String getSignature() {
        return this.signature;
    }
}
