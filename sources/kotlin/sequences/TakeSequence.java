package kotlin.sequences;

import java.util.Iterator;
/* compiled from: Sequences.kt */
/* loaded from: classes.dex */
public final class TakeSequence<T> implements Sequence<T>, DropTakeSequence<T> {
    public final int count;
    public final Sequence<T> sequence;

    @Override // kotlin.sequences.Sequence
    public final Iterator<T> iterator() {
        return new TakeSequence$iterator$1(this);
    }

    @Override // kotlin.sequences.DropTakeSequence
    public final Sequence<T> take(int i) {
        if (i >= this.count) {
            return this;
        }
        return new TakeSequence(this.sequence, i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public TakeSequence(Sequence<? extends T> sequence, int i) {
        boolean z;
        this.sequence = sequence;
        this.count = i;
        if (i >= 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            throw new IllegalArgumentException(("count must be non-negative, but was " + i + '.').toString());
        }
    }
}
