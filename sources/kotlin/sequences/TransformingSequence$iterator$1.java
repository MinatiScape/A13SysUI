package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.internal.markers.KMappedMarker;
/* compiled from: Sequences.kt */
/* loaded from: classes.dex */
public final class TransformingSequence$iterator$1 implements Iterator<R>, KMappedMarker {
    public final Iterator<T> iterator;
    public final /* synthetic */ TransformingSequence<T, R> this$0;

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public TransformingSequence$iterator$1(TransformingSequence<T, R> transformingSequence) {
        this.this$0 = transformingSequence;
        this.iterator = transformingSequence.sequence.iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.iterator.hasNext();
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [R, java.lang.Object] */
    @Override // java.util.Iterator
    public final R next() {
        return this.this$0.transformer.invoke(this.iterator.next());
    }
}
