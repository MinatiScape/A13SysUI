package com.android.systemui.util;

import android.view.ViewGroup;
import kotlin.jvm.functions.Function1;
import kotlin.sequences.FilteringSequence;
import kotlin.sequences.SequencesKt__SequenceBuilderKt$sequence$$inlined$Sequence$1;
/* compiled from: ConvenienceExtensions.kt */
/* loaded from: classes.dex */
public final class ConvenienceExtensionsKt {
    public static final SequencesKt__SequenceBuilderKt$sequence$$inlined$Sequence$1 getChildren(ViewGroup viewGroup) {
        return new SequencesKt__SequenceBuilderKt$sequence$$inlined$Sequence$1(new ConvenienceExtensionsKt$children$1(viewGroup, null));
    }

    public static final SequencesKt__SequenceBuilderKt$sequence$$inlined$Sequence$1 takeUntil(FilteringSequence filteringSequence, Function1 function1) {
        return new SequencesKt__SequenceBuilderKt$sequence$$inlined$Sequence$1(new ConvenienceExtensionsKt$takeUntil$1(filteringSequence, function1, null));
    }
}
