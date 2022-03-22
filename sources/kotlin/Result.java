package kotlin;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.io.Serializable;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Result.kt */
/* loaded from: classes.dex */
public final class Result<T> implements Serializable {
    private final Object value;

    /* compiled from: Result.kt */
    /* loaded from: classes.dex */
    public static final class Failure implements Serializable {
        public final Throwable exception;

        public final boolean equals(Object obj) {
            if (!(obj instanceof Failure) || !Intrinsics.areEqual(this.exception, ((Failure) obj).exception)) {
                return false;
            }
            return true;
        }

        public final int hashCode() {
            return this.exception.hashCode();
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Failure(");
            m.append(this.exception);
            m.append(')');
            return m.toString();
        }

        public Failure(Throwable th) {
            this.exception = th;
        }
    }

    /* renamed from: exceptionOrNull-impl  reason: not valid java name */
    public static final Throwable m181exceptionOrNullimpl(Object obj) {
        if (obj instanceof Failure) {
            return ((Failure) obj).exception;
        }
        return null;
    }

    public final boolean equals(Object obj) {
        Object obj2 = this.value;
        if (!(obj instanceof Result)) {
            return false;
        }
        Result result = (Result) obj;
        Objects.requireNonNull(result);
        if (!Intrinsics.areEqual(obj2, result.value)) {
            return false;
        }
        return true;
    }

    public final int hashCode() {
        Object obj = this.value;
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    public final String toString() {
        Object obj = this.value;
        if (obj instanceof Failure) {
            return ((Failure) obj).toString();
        }
        return "Success(" + obj + ')';
    }
}
