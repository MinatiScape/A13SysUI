package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.functions.Function2;
/* compiled from: Sequences.kt */
/* loaded from: classes.dex */
public final class TransformingIndexedSequence<T, R> implements Sequence<R> {
    public final Sequence<T> sequence;
    public final Function2<Integer, T, R> transformer;

    @Override // kotlin.sequences.Sequence
    public final Iterator<R> iterator() {
        return new TransformingIndexedSequence$iterator$1(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public TransformingIndexedSequence(Sequence<? extends T> sequence, Function2<? super Integer, ? super T, ? extends R> function2) {
        this.sequence = sequence;
        this.transformer = function2;
    }
}
