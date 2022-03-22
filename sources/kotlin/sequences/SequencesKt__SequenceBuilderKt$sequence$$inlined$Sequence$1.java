package kotlin.sequences;

import androidx.preference.R$color;
import java.util.Iterator;
import kotlin.jvm.functions.Function2;
/* compiled from: Sequences.kt */
/* loaded from: classes.dex */
public final class SequencesKt__SequenceBuilderKt$sequence$$inlined$Sequence$1 implements Sequence<Object> {
    public final /* synthetic */ Function2 $block$inlined;

    public SequencesKt__SequenceBuilderKt$sequence$$inlined$Sequence$1(Function2 function2) {
        this.$block$inlined = function2;
    }

    @Override // kotlin.sequences.Sequence
    public final Iterator<Object> iterator() {
        Function2 function2 = this.$block$inlined;
        SequenceBuilderIterator sequenceBuilderIterator = new SequenceBuilderIterator();
        sequenceBuilderIterator.nextStep = R$color.createCoroutineUnintercepted(function2, sequenceBuilderIterator, sequenceBuilderIterator);
        return sequenceBuilderIterator;
    }
}
