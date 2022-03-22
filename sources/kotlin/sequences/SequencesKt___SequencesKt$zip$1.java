package kotlin.sequences;

import kotlin.Pair;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;
/* compiled from: _Sequences.kt */
/* loaded from: classes.dex */
final class SequencesKt___SequencesKt$zip$1 extends Lambda implements Function2<Object, Object, Pair<Object, Object>> {
    public static final SequencesKt___SequencesKt$zip$1 INSTANCE = new SequencesKt___SequencesKt$zip$1();

    public SequencesKt___SequencesKt$zip$1() {
        super(2);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Pair<Object, Object> invoke(Object obj, Object obj2) {
        return new Pair<>(obj, obj2);
    }
}
