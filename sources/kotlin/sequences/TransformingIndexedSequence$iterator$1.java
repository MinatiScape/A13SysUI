package kotlin.sequences;

import java.util.Iterator;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.markers.KMappedMarker;
/* compiled from: Sequences.kt */
/* loaded from: classes.dex */
public final class TransformingIndexedSequence$iterator$1 implements Iterator<R>, KMappedMarker {
    public int index;
    public final Iterator<T> iterator;
    public final /* synthetic */ TransformingIndexedSequence<T, R> this$0;

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public TransformingIndexedSequence$iterator$1(TransformingIndexedSequence<T, R> transformingIndexedSequence) {
        this.this$0 = transformingIndexedSequence;
        this.iterator = transformingIndexedSequence.sequence.iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.iterator.hasNext();
    }

    /* JADX WARN: Type inference failed for: r3v4, types: [R, java.lang.Object] */
    @Override // java.util.Iterator
    public final R next() {
        Function2<Integer, T, R> function2 = this.this$0.transformer;
        int i = this.index;
        this.index = i + 1;
        if (i >= 0) {
            return function2.invoke(Integer.valueOf(i), this.iterator.next());
        }
        SetsKt__SetsKt.throwIndexOverflow();
        throw null;
    }
}
