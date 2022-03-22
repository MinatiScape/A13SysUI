package com.google.android.systemui.statusbar.notification.voicereplies;

import androidx.preference.R$color;
import com.android.systemui.statusbar.policy.OnSendRemoteInputListener;
import com.android.systemui.statusbar.policy.RemoteInputViewController;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger;
import java.util.Objects;
import kotlin.KotlinNothingValueException;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1", f = "NotificationVoiceReplyManager.kt", l = {1172}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ CompletableDeferred<Unit> $completion;
    public final /* synthetic */ String $key;
    public final /* synthetic */ RemoteInputViewController $rivc;
    public final /* synthetic */ NotificationVoiceReplyController.Connection $this_logUiEventsForActivatedVoiceReplyInputs;
    public Object L$0;
    public Object L$1;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1(RemoteInputViewController remoteInputViewController, NotificationVoiceReplyController.Connection connection, String str, NotificationVoiceReplyController notificationVoiceReplyController, CompletableDeferred<Unit> completableDeferred, Continuation<? super NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1> continuation) {
        super(2, continuation);
        this.$rivc = remoteInputViewController;
        this.$this_logUiEventsForActivatedVoiceReplyInputs = connection;
        this.$key = str;
        this.this$0 = notificationVoiceReplyController;
        this.$completion = completableDeferred;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1(this.$rivc, this.$this_logUiEventsForActivatedVoiceReplyInputs, this.$key, this.this$0, this.$completion, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        ((NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        return CoroutineSingletons.COROUTINE_SUSPENDED;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v3, types: [java.lang.Object, com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1] */
    /* JADX WARN: Type inference failed for: r8v5 */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1 notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1;
        Throwable th;
        RemoteInputViewController remoteInputViewController;
        NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1 notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$12;
        RemoteInputViewController remoteInputViewController2;
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            final NotificationVoiceReplyController.Connection connection = this.$this_logUiEventsForActivatedVoiceReplyInputs;
            final String str = this.$key;
            final NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
            final CompletableDeferred<Unit> completableDeferred = this.$completion;
            notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1 = new OnSendRemoteInputListener() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1
                @Override // com.android.systemui.statusbar.policy.OnSendRemoteInputListener
                public final void onSendRemoteInput() {
                    InSession inSession;
                    VoiceReplyTarget voiceReplyTarget;
                    NotificationVoiceReplyController.Connection connection2 = NotificationVoiceReplyController.Connection.this;
                    Objects.requireNonNull(connection2);
                    VoiceReplyState value = connection2.stateFlow.getValue();
                    String str2 = null;
                    if (value instanceof InSession) {
                        inSession = (InSession) value;
                    } else {
                        inSession = null;
                    }
                    if (!(inSession == null || (voiceReplyTarget = inSession.target) == null)) {
                        str2 = voiceReplyTarget.notifKey;
                    }
                    if (!Intrinsics.areEqual(str2, str)) {
                        NotificationVoiceReplyLogger notificationVoiceReplyLogger = notificationVoiceReplyController.logger;
                        String str3 = str;
                        Objects.requireNonNull(notificationVoiceReplyLogger);
                        notificationVoiceReplyLogger.eventLogger.log(VoiceReplyEvent.MSG_SEND_DELAYED);
                        notificationVoiceReplyLogger.logMsgSent(str3, NotificationVoiceReplyLogger.SendType.DELAYED);
                    } else if (notificationVoiceReplyController.statusBarStateController.getState() == 2) {
                        NotificationVoiceReplyLogger notificationVoiceReplyLogger2 = notificationVoiceReplyController.logger;
                        String str4 = str;
                        Objects.requireNonNull(notificationVoiceReplyLogger2);
                        notificationVoiceReplyLogger2.eventLogger.log(VoiceReplyEvent.MSG_SEND_AUTH_BYPASSED);
                        notificationVoiceReplyLogger2.logMsgSent(str4, NotificationVoiceReplyLogger.SendType.BYPASS);
                    } else {
                        NotificationVoiceReplyLogger notificationVoiceReplyLogger3 = notificationVoiceReplyController.logger;
                        String str5 = str;
                        Objects.requireNonNull(notificationVoiceReplyLogger3);
                        notificationVoiceReplyLogger3.eventLogger.log(VoiceReplyEvent.MSG_SEND_UNLOCKED);
                        notificationVoiceReplyLogger3.logMsgSent(str5, NotificationVoiceReplyLogger.SendType.UNLOCKED);
                    }
                    completableDeferred.complete();
                }

                @Override // com.android.systemui.statusbar.policy.OnSendRemoteInputListener
                public final void onSendRequestBounced() {
                    NotificationVoiceReplyLogger notificationVoiceReplyLogger = notificationVoiceReplyController.logger;
                    String str2 = str;
                    Objects.requireNonNull(notificationVoiceReplyLogger);
                    notificationVoiceReplyLogger.eventLogger.log(VoiceReplyEvent.MSG_SEND_BOUNCED);
                    notificationVoiceReplyLogger.logMsgSent(str2, NotificationVoiceReplyLogger.SendType.BOUNCED);
                }
            };
            this.$rivc.addOnSendRemoteInputListener(notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1);
            remoteInputViewController = this.$rivc;
            try {
                this.L$0 = notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1;
                this.L$1 = remoteInputViewController;
                this.label = 1;
                CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(R$color.intercepted(this), 1);
                cancellableContinuationImpl.initCancellability();
                if (cancellableContinuationImpl.getResult() == coroutineSingletons) {
                    return coroutineSingletons;
                }
                notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$12 = notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1;
                remoteInputViewController2 = remoteInputViewController;
            } catch (Throwable th2) {
                th = th2;
                remoteInputViewController.removeOnSendRemoteInputListener(notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1);
                throw th;
            }
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            remoteInputViewController2 = (RemoteInputViewController) this.L$1;
            notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$12 = (NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1) this.L$0;
            try {
                ResultKt.throwOnFailure(obj);
            } catch (Throwable th3) {
                remoteInputViewController = remoteInputViewController2;
                notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1 = notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$12;
                th = th3;
                remoteInputViewController.removeOnSendRemoteInputListener(notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1);
                throw th;
            }
        }
        throw new KotlinNothingValueException();
    }
}
