package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.markers.KMappedMarker;
/* compiled from: Sequences.kt */
/* loaded from: classes.dex */
public final class TakeSequence$iterator$1 implements Iterator<T>, KMappedMarker {
    public final Iterator<T> iterator;
    public int left;

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        if (this.left <= 0 || !this.iterator.hasNext()) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [T, java.lang.Object] */
    @Override // java.util.Iterator
    public final T next() {
        int i = this.left;
        if (i != 0) {
            this.left = i - 1;
            return this.iterator.next();
        }
        throw new NoSuchElementException();
    }

    public TakeSequence$iterator$1(TakeSequence<T> takeSequence) {
        this.left = takeSequence.count;
        this.iterator = takeSequence.sequence.iterator();
    }
}
