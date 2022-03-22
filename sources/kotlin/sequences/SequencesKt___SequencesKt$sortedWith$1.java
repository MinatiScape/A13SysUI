package kotlin.sequences;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
/* compiled from: _Sequences.kt */
/* loaded from: classes.dex */
public final class SequencesKt___SequencesKt$sortedWith$1 implements Sequence<Object> {
    public final /* synthetic */ Comparator<Object> $comparator;
    public final /* synthetic */ Sequence<Object> $this_sortedWith;

    public SequencesKt___SequencesKt$sortedWith$1(Sequence<Object> sequence, Comparator<Object> comparator) {
        this.$this_sortedWith = sequence;
        this.$comparator = comparator;
    }

    @Override // kotlin.sequences.Sequence
    public final Iterator<Object> iterator() {
        ArrayList mutableList = SequencesKt___SequencesKt.toMutableList(this.$this_sortedWith);
        CollectionsKt__MutableCollectionsJVMKt.sortWith(mutableList, this.$comparator);
        return mutableList.iterator();
    }
}
