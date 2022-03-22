package com.google.common.collect;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.google.common.collect.Cut;
import java.lang.Comparable;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Range<C extends Comparable> extends RangeGwtSerializationDependencies {
    public static final Range<Comparable> ALL = new Range<>(Cut.BelowAll.INSTANCE, Cut.AboveAll.INSTANCE);
    private static final long serialVersionUID = 0;
    public final Cut<C> lowerBound;
    public final Cut<C> upperBound;

    public final boolean equals(Object obj) {
        if (!(obj instanceof Range)) {
            return false;
        }
        Range range = (Range) obj;
        if (!this.lowerBound.equals(range.lowerBound) || !this.upperBound.equals(range.upperBound)) {
            return false;
        }
        return true;
    }

    public final int hashCode() {
        return (this.lowerBound.hashCode() * 31) + this.upperBound.hashCode();
    }

    public Object readResolve() {
        Range<Comparable> range = ALL;
        if (equals(range)) {
            return range;
        }
        return this;
    }

    public final String toString() {
        Cut<C> cut = this.lowerBound;
        Cut<C> cut2 = this.upperBound;
        StringBuilder sb = new StringBuilder(16);
        cut.describeAsLowerBound(sb);
        sb.append("..");
        cut2.describeAsUpperBound(sb);
        return sb.toString();
    }

    public Range(Cut<C> cut, Cut<C> cut2) {
        Objects.requireNonNull(cut);
        this.lowerBound = cut;
        Objects.requireNonNull(cut2);
        this.upperBound = cut2;
        if (cut.compareTo((Cut) cut2) > 0 || cut == Cut.AboveAll.INSTANCE || cut2 == Cut.BelowAll.INSTANCE) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid range: ");
            StringBuilder sb = new StringBuilder(16);
            cut.describeAsLowerBound(sb);
            sb.append("..");
            cut2.describeAsUpperBound(sb);
            m.append(sb.toString());
            throw new IllegalArgumentException(m.toString());
        }
    }
}
