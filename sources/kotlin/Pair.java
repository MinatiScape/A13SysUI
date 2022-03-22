package kotlin;

import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import java.io.Serializable;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Tuples.kt */
/* loaded from: classes.dex */
public final class Pair<A, B> implements Serializable {
    private final A first;
    private final B second;

    public final A component1() {
        return this.first;
    }

    public final B component2() {
        return this.second;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) obj;
        return Intrinsics.areEqual(this.first, pair.first) && Intrinsics.areEqual(this.second, pair.second);
    }

    public final int hashCode() {
        A a = this.first;
        int i = 0;
        int hashCode = (a == null ? 0 : a.hashCode()) * 31;
        B b = this.second;
        if (b != null) {
            i = b.hashCode();
        }
        return hashCode + i;
    }

    public final String toString() {
        StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('(');
        m.append(this.first);
        m.append(", ");
        m.append(this.second);
        m.append(')');
        return m.toString();
    }

    public Pair(A a, B b) {
        this.first = a;
        this.second = b;
    }

    public final A getFirst() {
        return this.first;
    }

    public final B getSecond() {
        return this.second;
    }
}
