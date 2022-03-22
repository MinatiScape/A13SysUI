package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.policy.RemoteInputViewController;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import kotlin.Pair;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: Collect.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1 implements FlowCollector<Pair<? extends String, ? extends RemoteInputViewController>> {
    public final /* synthetic */ CoroutineScope $$this$coroutineScope$inlined;
    public final /* synthetic */ Flow $remoteInputViewActivatedForVoiceReply$inlined;
    public final /* synthetic */ NotificationVoiceReplyController.Connection $this_logUiEventsForActivatedVoiceReplyInputs$inlined;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* compiled from: Collect.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1", f = "NotificationVoiceReplyManager.kt", l = {146}, m = "emit")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends ContinuationImpl {
        public Object L$0;
        public Object L$1;
        public Object L$2;
        public int label;
        public /* synthetic */ Object result;

        public AnonymousClass1(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1.this.emit(null, this);
        }
    }

    public NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1(CoroutineScope coroutineScope, NotificationVoiceReplyController.Connection connection, NotificationVoiceReplyController notificationVoiceReplyController, Flow flow) {
        this.$$this$coroutineScope$inlined = coroutineScope;
        this.$this_logUiEventsForActivatedVoiceReplyInputs$inlined = connection;
        this.this$0 = notificationVoiceReplyController;
        this.$remoteInputViewActivatedForVoiceReply$inlined = flow;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0022  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x003d  */
    @Override // kotlinx.coroutines.flow.FlowCollector
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object emit(kotlin.Pair<? extends java.lang.String, ? extends com.android.systemui.statusbar.policy.RemoteInputViewController> r14, kotlin.coroutines.Continuation<? super kotlin.Unit> r15) {
        /*
            Method dump skipped, instructions count: 226
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
