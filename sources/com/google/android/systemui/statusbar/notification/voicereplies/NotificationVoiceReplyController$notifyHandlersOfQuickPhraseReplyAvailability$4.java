package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$4", f = "NotificationVoiceReplyManager.kt", l = {}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$4 extends SuspendLambda implements Function3<VoiceReplyTarget, List<? extends NotificationEntry>, Continuation<? super VoiceReplyTarget>, Object> {
    public /* synthetic */ Object L$0;
    public /* synthetic */ Object L$1;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$4(NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$4> continuation) {
        super(3, continuation);
        this.this$0 = notificationVoiceReplyController;
    }

    @Override // kotlin.jvm.functions.Function3
    public final Object invoke(VoiceReplyTarget voiceReplyTarget, List<? extends NotificationEntry> list, Continuation<? super VoiceReplyTarget> continuation) {
        NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$4 notificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$4 = new NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$4(this.this$0, continuation);
        notificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$4.L$0 = voiceReplyTarget;
        notificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$4.L$1 = list;
        return notificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$4.invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        boolean z;
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            VoiceReplyTarget voiceReplyTarget = (VoiceReplyTarget) this.L$0;
            List<NotificationEntry> list = (List) this.L$1;
            if (voiceReplyTarget != null) {
                NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
                boolean z2 = true;
                if (!(list instanceof Collection) || !list.isEmpty()) {
                    for (NotificationEntry notificationEntry : list) {
                        Objects.requireNonNull(notificationEntry);
                        if (Intrinsics.areEqual(notificationEntry.mKey, voiceReplyTarget.notifKey)) {
                            z = true;
                            break;
                        }
                    }
                }
                z = false;
                if (!z || !notificationVoiceReplyController.statusBarStateController.isDozing()) {
                    z2 = false;
                }
                if (z2) {
                    return voiceReplyTarget;
                }
            }
            return null;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
