package kotlin.sequences;

import androidx.slice.view.R$id;
import java.util.Iterator;
import kotlin.collections.ArraysKt___ArraysKt;
/* compiled from: Sequences.kt */
/* loaded from: classes.dex */
public class SequencesKt__SequencesKt extends R$id {
    public static final <T> Sequence<T> sequenceOf(T... tArr) {
        boolean z;
        if (tArr.length == 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return EmptySequence.INSTANCE;
        }
        return ArraysKt___ArraysKt.asSequence(tArr);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [kotlin.sequences.SequencesKt__SequencesKt$asSequence$$inlined$Sequence$1, kotlin.sequences.Sequence<T>] */
    public static final <T> Sequence<T> asSequence(final Iterator<? extends T> it) {
        Sequence<T> sequencesKt__SequencesKt$asSequence$$inlined$Sequence$1 = new Sequence<T>() { // from class: kotlin.sequences.SequencesKt__SequencesKt$asSequence$$inlined$Sequence$1
            @Override // kotlin.sequences.Sequence
            public final Iterator<T> iterator() {
                return it;
            }
        };
        if (sequencesKt__SequencesKt$asSequence$$inlined$Sequence$1 instanceof ConstrainedOnceSequence) {
            return sequencesKt__SequencesKt$asSequence$$inlined$Sequence$1;
        }
        return new ConstrainedOnceSequence(sequencesKt__SequencesKt$asSequence$$inlined$Sequence$1);
    }
}
