package kotlin.sequences;

import java.util.HashSet;
import java.util.Iterator;
import kotlin.collections.AbstractIterator;
import kotlin.jvm.functions.Function1;
/* compiled from: Sequences.kt */
/* loaded from: classes.dex */
public final class DistinctIterator<T, K> extends AbstractIterator<T> {
    public final Function1<T, K> keySelector;
    public final HashSet<K> observed = new HashSet<>();
    public final Iterator<T> source;

    /* JADX WARN: Multi-variable type inference failed */
    public DistinctIterator(Iterator<? extends T> it, Function1<? super T, ? extends K> function1) {
        this.source = it;
        this.keySelector = function1;
    }
}
