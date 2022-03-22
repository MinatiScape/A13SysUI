package com.google.android.systemui.statusbar.notification.voicereplies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import androidx.preference.R$color;
import com.android.systemui.broadcast.BroadcastDispatcher;
import kotlin.KotlinNothingValueException;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Ref$BooleanRef;
import kotlin.jvm.internal.Ref$IntRef;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowImpl;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient$startClient$job$1", f = "NotificationVoiceReplyManager.kt", l = {1173}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class DebugNotificationVoiceReplyClient$startClient$job$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    private /* synthetic */ Object L$0;
    public Object L$1;
    public Object L$2;
    public int label;
    public final /* synthetic */ DebugNotificationVoiceReplyClient this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DebugNotificationVoiceReplyClient$startClient$job$1(DebugNotificationVoiceReplyClient debugNotificationVoiceReplyClient, Continuation<? super DebugNotificationVoiceReplyClient$startClient$job$1> continuation) {
        super(2, continuation);
        this.this$0 = debugNotificationVoiceReplyClient;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        DebugNotificationVoiceReplyClient$startClient$job$1 debugNotificationVoiceReplyClient$startClient$job$1 = new DebugNotificationVoiceReplyClient$startClient$job$1(this.this$0, continuation);
        debugNotificationVoiceReplyClient$startClient$job$1.L$0 = obj;
        return debugNotificationVoiceReplyClient$startClient$job$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        ((DebugNotificationVoiceReplyClient$startClient$job$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        return CoroutineSingletons.COROUTINE_SUSPENDED;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        DebugNotificationVoiceReplyClient debugNotificationVoiceReplyClient;
        Throwable th;
        BroadcastReceiver broadcastReceiver;
        final NotificationVoiceReplyController$connect$1$registerHandler$1 notificationVoiceReplyController$connect$1$registerHandler$1;
        VoiceReplySubscription voiceReplySubscription;
        BroadcastReceiver broadcastReceiver2;
        DebugNotificationVoiceReplyClient debugNotificationVoiceReplyClient2;
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            final CoroutineScope coroutineScope = (CoroutineScope) this.L$0;
            NotificationVoiceReplyController$connect$1 connect = this.this$0.voiceReplyInitializer.connect(coroutineScope);
            final Ref$BooleanRef ref$BooleanRef = new Ref$BooleanRef();
            final DebugNotificationVoiceReplyClient debugNotificationVoiceReplyClient3 = this.this$0;
            notificationVoiceReplyController$connect$1$registerHandler$1 = connect.registerHandler(new NotificationVoiceReplyHandler(debugNotificationVoiceReplyClient3, ref$BooleanRef) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient$startClient$job$1$subscription$1
                public final /* synthetic */ Ref$BooleanRef $notifAvailable;
                public final StateFlowImpl showCta = new StateFlowImpl(CtaState.HOTWORD);
                public final int userId;

                @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler
                public final void onNotifAvailableForQuickPhraseReplyChanged(boolean z) {
                }

                {
                    this.$notifAvailable = ref$BooleanRef;
                    this.userId = debugNotificationVoiceReplyClient3.lockscreenUserManager.getCurrentUserId();
                }

                @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler
                public final void onNotifAvailableForReplyChanged(boolean z) {
                    this.$notifAvailable.element = z;
                }

                @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler
                public final StateFlow<CtaState> getShowCta() {
                    return this.showCta;
                }

                @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler
                public final int getUserId() {
                    return this.userId;
                }
            });
            final Ref$IntRef ref$IntRef = new Ref$IntRef();
            broadcastReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1
                @Override // android.content.BroadcastReceiver
                public final void onReceive(Context context, Intent intent) {
                    if (!Ref$BooleanRef.this.element) {
                        Log.d("NotifVoiceReplyDebug", "no notification available for voice reply");
                    }
                    BuildersKt.launch$default(coroutineScope, null, null, new DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1(notificationVoiceReplyController$connect$1$registerHandler$1, ref$IntRef, null), 3);
                }
            };
            BroadcastDispatcher broadcastDispatcher = this.this$0.broadcastDispatcher;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.google.android.systemui.START_VOICE_REPLY");
            BroadcastDispatcher.registerReceiver$default(broadcastDispatcher, broadcastReceiver, intentFilter, null, null, 28);
            debugNotificationVoiceReplyClient = this.this$0;
            try {
                this.L$0 = notificationVoiceReplyController$connect$1$registerHandler$1;
                this.L$1 = broadcastReceiver;
                this.L$2 = debugNotificationVoiceReplyClient;
                this.label = 1;
                CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(R$color.intercepted(this), 1);
                cancellableContinuationImpl.initCancellability();
                if (cancellableContinuationImpl.getResult() == coroutineSingletons) {
                    return coroutineSingletons;
                }
                debugNotificationVoiceReplyClient2 = debugNotificationVoiceReplyClient;
                voiceReplySubscription = notificationVoiceReplyController$connect$1$registerHandler$1;
                broadcastReceiver2 = broadcastReceiver;
            } catch (Throwable th2) {
                th = th2;
                debugNotificationVoiceReplyClient.broadcastDispatcher.unregisterReceiver(broadcastReceiver);
                notificationVoiceReplyController$connect$1$registerHandler$1.unsubscribe();
                throw th;
            }
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            debugNotificationVoiceReplyClient2 = (DebugNotificationVoiceReplyClient) this.L$2;
            broadcastReceiver2 = (DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1) this.L$1;
            voiceReplySubscription = (VoiceReplySubscription) this.L$0;
            try {
                ResultKt.throwOnFailure(obj);
            } catch (Throwable th3) {
                broadcastReceiver = broadcastReceiver2;
                notificationVoiceReplyController$connect$1$registerHandler$1 = voiceReplySubscription;
                th = th3;
                debugNotificationVoiceReplyClient = debugNotificationVoiceReplyClient2;
                debugNotificationVoiceReplyClient.broadcastDispatcher.unregisterReceiver(broadcastReceiver);
                notificationVoiceReplyController$connect$1$registerHandler$1.unsubscribe();
                throw th;
            }
        }
        throw new KotlinNothingValueException();
    }
}
