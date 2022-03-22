package kotlin.sequences;

import java.util.Iterator;
import kotlin.collections.EmptyIterator;
/* compiled from: Sequences.kt */
/* loaded from: classes.dex */
public final class EmptySequence implements Sequence, DropTakeSequence {
    public static final EmptySequence INSTANCE = new EmptySequence();

    @Override // kotlin.sequences.DropTakeSequence
    public final /* bridge */ /* synthetic */ Sequence take(int i) {
        return INSTANCE;
    }

    @Override // kotlin.sequences.Sequence
    public final Iterator iterator() {
        return EmptyIterator.INSTANCE;
    }
}
