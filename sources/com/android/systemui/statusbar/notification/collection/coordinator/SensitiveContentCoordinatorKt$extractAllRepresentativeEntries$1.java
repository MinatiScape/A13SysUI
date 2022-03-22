package com.android.systemui.statusbar.notification.collection.coordinator;

import androidx.savedstate.R$id;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequenceBuilderKt$sequence$$inlined$Sequence$1;
/* compiled from: SensitiveContentCoordinator.kt */
/* loaded from: classes.dex */
final /* synthetic */ class SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1 extends FunctionReferenceImpl implements Function1<ListEntry, Sequence<? extends NotificationEntry>> {
    public static final SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1 INSTANCE = new SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1();

    public SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1() {
        super(1, R$id.class, "extractAllRepresentativeEntries", "extractAllRepresentativeEntries(Lcom/android/systemui/statusbar/notification/collection/ListEntry;)Lkotlin/sequences/Sequence;", 1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Sequence<? extends NotificationEntry> invoke(ListEntry listEntry) {
        return new SequencesKt__SequenceBuilderKt$sequence$$inlined$Sequence$1(new SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2(listEntry, null));
    }
}
