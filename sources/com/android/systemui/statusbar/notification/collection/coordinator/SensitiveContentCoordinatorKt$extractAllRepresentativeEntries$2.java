package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Objects;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.FlatteningSequence;
import kotlin.sequences.FlatteningSequence$iterator$1;
import kotlin.sequences.SequenceScope;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: SensitiveContentCoordinator.kt */
@DebugMetadata(c = "com.android.systemui.statusbar.notification.collection.coordinator.SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2", f = "SensitiveContentCoordinator.kt", l = {120, 122}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super NotificationEntry>, Continuation<? super Unit>, Object> {
    public final /* synthetic */ ListEntry $listEntry;
    private /* synthetic */ Object L$0;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2(ListEntry listEntry, Continuation<? super SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2> continuation) {
        super(continuation);
        this.$listEntry = listEntry;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2 sensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2 = new SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2(this.$listEntry, continuation);
        sensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2.L$0 = obj;
        return sensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(SequenceScope<? super NotificationEntry> sequenceScope, Continuation<? super Unit> continuation) {
        return ((SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        SequenceScope sequenceScope;
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            sequenceScope = (SequenceScope) this.L$0;
            NotificationEntry representativeEntry = this.$listEntry.getRepresentativeEntry();
            if (representativeEntry != null) {
                this.L$0 = sequenceScope;
                this.label = 1;
                sequenceScope.yield(representativeEntry, this);
                return coroutineSingletons;
            }
        } else if (i == 1) {
            sequenceScope = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure(obj);
        } else if (i == 2) {
            ResultKt.throwOnFailure(obj);
            return Unit.INSTANCE;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ListEntry listEntry = this.$listEntry;
        if (listEntry instanceof GroupEntry) {
            GroupEntry groupEntry = (GroupEntry) listEntry;
            Objects.requireNonNull(groupEntry);
            FlatteningSequence flatMap = SequencesKt___SequencesKt.flatMap(new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1(groupEntry.mUnmodifiableChildren), SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1.INSTANCE);
            this.L$0 = null;
            this.label = 2;
            Objects.requireNonNull(sequenceScope);
            Object yieldAll = sequenceScope.yieldAll(new FlatteningSequence$iterator$1(flatMap), this);
            if (yieldAll != coroutineSingletons) {
                yieldAll = Unit.INSTANCE;
            }
            if (yieldAll == coroutineSingletons) {
                return coroutineSingletons;
            }
        }
        return Unit.INSTANCE;
    }
}
