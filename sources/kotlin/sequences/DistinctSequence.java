package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.functions.Function1;
/* compiled from: Sequences.kt */
/* loaded from: classes.dex */
public final class DistinctSequence<T, K> implements Sequence<T> {
    public final Function1<T, K> keySelector;
    public final Sequence<T> source;

    @Override // kotlin.sequences.Sequence
    public final Iterator<T> iterator() {
        return new DistinctIterator(this.source.iterator(), this.keySelector);
    }

    public DistinctSequence(FilteringSequence filteringSequence, Function1 function1) {
        this.source = filteringSequence;
        this.keySelector = function1;
    }
}
