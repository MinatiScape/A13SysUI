package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import androidx.leanback.R$styleable;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function1;
/* compiled from: _Sequences.kt */
/* loaded from: classes.dex */
public class SequencesKt___SequencesKt extends SequencesKt___SequencesJvmKt {
    public static String joinToString$default(Sequence sequence, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append((CharSequence) str);
        int i = 0;
        for (Object obj : sequence) {
            i++;
            if (i > 1) {
                sb.append((CharSequence) ",");
            }
            R$styleable.appendElement(sb, obj, null);
        }
        sb.append((CharSequence) ")");
        return sb.toString();
    }

    public static final FlatteningSequence plus(Sequence sequence, Sequence sequence2) {
        Sequence sequenceOf = SequencesKt__SequencesKt.sequenceOf(sequence, sequence2);
        SequencesKt__SequencesKt$flatten$1 sequencesKt__SequencesKt$flatten$1 = SequencesKt__SequencesKt$flatten$1.INSTANCE;
        if (!(sequenceOf instanceof TransformingSequence)) {
            return new FlatteningSequence(sequenceOf, SequencesKt__SequencesKt$flatten$3.INSTANCE, sequencesKt__SequencesKt$flatten$1);
        }
        TransformingSequence transformingSequence = (TransformingSequence) sequenceOf;
        return new FlatteningSequence(transformingSequence.sequence, transformingSequence.transformer, sequencesKt__SequencesKt$flatten$1);
    }

    public static final DistinctSequence distinct(FilteringSequence filteringSequence) {
        return new DistinctSequence(filteringSequence, SequencesKt___SequencesKt$distinct$1.INSTANCE);
    }

    public static final FilteringSequence filter(Sequence sequence, Function1 function1) {
        return new FilteringSequence(sequence, true, function1);
    }

    public static final FilteringSequence filterNotNull(Sequence sequence) {
        return new FilteringSequence(sequence, false, SequencesKt___SequencesKt$filterNotNull$1.INSTANCE);
    }

    public static final FlatteningSequence flatMap(Sequence sequence, Function1 function1) {
        return new FlatteningSequence(sequence, function1, SequencesKt___SequencesKt$flatMap$2.INSTANCE);
    }

    public static final FilteringSequence mapNotNull(Sequence sequence, Function1 function1) {
        return filterNotNull(new TransformingSequence(sequence, function1));
    }

    public static final <T> Sequence<T> take(Sequence<? extends T> sequence, int i) {
        boolean z;
        if (i >= 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            throw new IllegalArgumentException(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Requested element count ", i, " is less than zero.").toString());
        } else if (i == 0) {
            return EmptySequence.INSTANCE;
        } else {
            if (sequence instanceof DropTakeSequence) {
                return ((DropTakeSequence) sequence).take(i);
            }
            return new TakeSequence(sequence, i);
        }
    }

    public static final ArrayList toMutableList(Sequence sequence) {
        ArrayList arrayList = new ArrayList();
        for (Object obj : sequence) {
            arrayList.add(obj);
        }
        return arrayList;
    }

    public static final MergingSequence zip(Sequence sequence, Sequence sequence2) {
        return new MergingSequence(sequence, sequence2, SequencesKt___SequencesKt$zip$1.INSTANCE);
    }

    public static final <T> List<T> toList(Sequence<? extends T> sequence) {
        return SetsKt__SetsKt.optimizeReadOnlyList(toMutableList(sequence));
    }
}
