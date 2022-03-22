package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.internal.markers.KMappedMarker;
/* compiled from: Sequences.kt */
/* loaded from: classes.dex */
public final class MergingSequence$iterator$1 implements Iterator<V>, KMappedMarker {
    public final Iterator<T1> iterator1;
    public final Iterator<T2> iterator2;
    public final /* synthetic */ MergingSequence<T1, T2, V> this$0;

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public MergingSequence$iterator$1(MergingSequence<T1, T2, V> mergingSequence) {
        this.this$0 = mergingSequence;
        this.iterator1 = mergingSequence.sequence1.iterator();
        this.iterator2 = mergingSequence.sequence2.iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        if (!this.iterator1.hasNext() || !this.iterator2.hasNext()) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [V, java.lang.Object] */
    @Override // java.util.Iterator
    public final V next() {
        return this.this$0.transform.invoke(this.iterator1.next(), this.iterator2.next());
    }
}
