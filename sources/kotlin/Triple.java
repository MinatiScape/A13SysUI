package kotlin;

import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import java.io.Serializable;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Tuples.kt */
/* loaded from: classes.dex */
public final class Triple<A, B, C> implements Serializable {
    private final A first;
    private final B second;
    private final C third;

    public final A component1() {
        return this.first;
    }

    public final B component2() {
        return this.second;
    }

    public final C component3() {
        return this.third;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Triple)) {
            return false;
        }
        Triple triple = (Triple) obj;
        return Intrinsics.areEqual(this.first, triple.first) && Intrinsics.areEqual(this.second, triple.second) && Intrinsics.areEqual(this.third, triple.third);
    }

    public final int hashCode() {
        A a = this.first;
        int i = 0;
        int hashCode = (a == null ? 0 : a.hashCode()) * 31;
        B b = this.second;
        int hashCode2 = (hashCode + (b == null ? 0 : b.hashCode())) * 31;
        C c = this.third;
        if (c != null) {
            i = c.hashCode();
        }
        return hashCode2 + i;
    }

    public final String toString() {
        StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('(');
        m.append(this.first);
        m.append(", ");
        m.append(this.second);
        m.append(", ");
        m.append(this.third);
        m.append(')');
        return m.toString();
    }

    public Triple(A a, B b, C c) {
        this.first = a;
        this.second = b;
        this.third = c;
    }
}
