package androidx.core.util;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Pair<F, S> {
    public final F first;
    public final S second;

    public final boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) obj;
        if (!Objects.equals(pair.first, this.first) || !Objects.equals(pair.second, this.second)) {
            return false;
        }
        return true;
    }

    public final int hashCode() {
        int i;
        F f = this.first;
        int i2 = 0;
        if (f == null) {
            i = 0;
        } else {
            i = f.hashCode();
        }
        S s = this.second;
        if (s != null) {
            i2 = s.hashCode();
        }
        return i ^ i2;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Pair{");
        m.append(this.first);
        m.append(" ");
        m.append(this.second);
        m.append("}");
        return m.toString();
    }

    public Pair(F f, S s) {
        this.first = f;
        this.second = s;
    }
}
