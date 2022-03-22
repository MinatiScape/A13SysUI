package com.google.android.systemui.statusbar.notification.voicereplies;

import androidx.preference.R$color;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import kotlin.KotlinNothingValueException;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.Channel;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$getDozeStateChanges$1$1", f = "NotificationVoiceReplyManager.kt", l = {1172}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerKt$getDozeStateChanges$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Channel<Boolean> $chan;
    public final /* synthetic */ StatusBarStateController $this_getDozeStateChanges;
    public Object L$0;
    public Object L$1;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerKt$getDozeStateChanges$1$1(StatusBarStateController statusBarStateController, Channel<Boolean> channel, Continuation<? super NotificationVoiceReplyManagerKt$getDozeStateChanges$1$1> continuation) {
        super(2, continuation);
        this.$this_getDozeStateChanges = statusBarStateController;
        this.$chan = channel;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new NotificationVoiceReplyManagerKt$getDozeStateChanges$1$1(this.$this_getDozeStateChanges, this.$chan, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        ((NotificationVoiceReplyManagerKt$getDozeStateChanges$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        return CoroutineSingletons.COROUTINE_SUSPENDED;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Throwable th;
        StatusBarStateController.StateListener stateListener;
        StatusBarStateController statusBarStateController;
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            final Channel<Boolean> channel = this.$chan;
            StatusBarStateController.StateListener notificationVoiceReplyManagerKt$getDozeStateChanges$1$1$listener$1 = new StatusBarStateController.StateListener() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$getDozeStateChanges$1$1$listener$1
                @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
                public final void onDozingChanged(boolean z) {
                    channel.offer(Boolean.valueOf(z));
                }
            };
            this.$this_getDozeStateChanges.addCallback(notificationVoiceReplyManagerKt$getDozeStateChanges$1$1$listener$1);
            StatusBarStateController statusBarStateController2 = this.$this_getDozeStateChanges;
            try {
                this.L$0 = notificationVoiceReplyManagerKt$getDozeStateChanges$1$1$listener$1;
                this.L$1 = statusBarStateController2;
                this.label = 1;
                CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(R$color.intercepted(this), 1);
                cancellableContinuationImpl.initCancellability();
                if (cancellableContinuationImpl.getResult() == coroutineSingletons) {
                    return coroutineSingletons;
                }
                stateListener = notificationVoiceReplyManagerKt$getDozeStateChanges$1$1$listener$1;
                statusBarStateController = statusBarStateController2;
            } catch (Throwable th2) {
                statusBarStateController = statusBarStateController2;
                th = th2;
                stateListener = notificationVoiceReplyManagerKt$getDozeStateChanges$1$1$listener$1;
                statusBarStateController.removeCallback(stateListener);
                throw th;
            }
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            statusBarStateController = (StatusBarStateController) this.L$1;
            stateListener = (NotificationVoiceReplyManagerKt$getDozeStateChanges$1$1$listener$1) this.L$0;
            try {
                ResultKt.throwOnFailure(obj);
            } catch (Throwable th3) {
                th = th3;
                statusBarStateController.removeCallback(stateListener);
                throw th;
            }
        }
        throw new KotlinNothingValueException();
    }
}
