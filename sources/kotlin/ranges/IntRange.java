package kotlin.ranges;

import java.util.Objects;
/* compiled from: Ranges.kt */
/* loaded from: classes.dex */
public final class IntRange extends IntProgression {
    public static final IntRange EMPTY = new IntRange(1, 0);

    public IntRange(int i, int i2) {
        super(i, i2, 1);
    }

    @Override // kotlin.ranges.IntProgression
    public final boolean equals(Object obj) {
        if (obj instanceof IntRange) {
            if (!isEmpty() || !((IntRange) obj).isEmpty()) {
                int i = this.first;
                IntRange intRange = (IntRange) obj;
                Objects.requireNonNull(intRange);
                if (i == intRange.first) {
                    int i2 = this.last;
                    Objects.requireNonNull(intRange);
                    if (i2 == intRange.last) {
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override // kotlin.ranges.IntProgression
    public final boolean isEmpty() {
        if (this.first > this.last) {
            return true;
        }
        return false;
    }

    @Override // kotlin.ranges.IntProgression
    public final String toString() {
        return this.first + ".." + this.last;
    }

    @Override // kotlin.ranges.IntProgression
    public final int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return this.last + (this.first * 31);
    }
}
