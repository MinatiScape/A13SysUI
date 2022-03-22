package com.google.android.systemui.statusbar.notification.voicereplies;

import androidx.preference.R$color;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import java.util.Objects;
import kotlin.KotlinNothingValueException;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$refreshCandidateOnNotifChanges$2", f = "NotificationVoiceReplyManager.kt", l = {1172}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class NotificationVoiceReplyController$refreshCandidateOnNotifChanges$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ NotificationVoiceReplyController.Connection $this_refreshCandidateOnNotifChanges;
    public Object L$0;
    public Object L$1;
    public Object L$2;
    public int label;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$refreshCandidateOnNotifChanges$2(NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection, Continuation<? super NotificationVoiceReplyController$refreshCandidateOnNotifChanges$2> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyController;
        this.$this_refreshCandidateOnNotifChanges = connection;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new NotificationVoiceReplyController$refreshCandidateOnNotifChanges$2(this.this$0, this.$this_refreshCandidateOnNotifChanges, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        ((NotificationVoiceReplyController$refreshCandidateOnNotifChanges$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        return CoroutineSingletons.COROUTINE_SUSPENDED;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        BindEventManager.Listener listener;
        Throwable th;
        NotifCollectionListener notifCollectionListener;
        NotificationVoiceReplyController notificationVoiceReplyController;
        BindEventManager.Listener listener2;
        NotifCollectionListener notifCollectionListener2;
        NotificationVoiceReplyController notificationVoiceReplyController2;
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            final NotificationVoiceReplyController.Connection connection = this.$this_refreshCandidateOnNotifChanges;
            final NotificationVoiceReplyController notificationVoiceReplyController3 = this.this$0;
            listener = new BindEventManager.Listener() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$refreshCandidateOnNotifChanges$2$bindListener$1
                @Override // com.android.systemui.statusbar.notification.collection.inflation.BindEventManager.Listener
                public final void onViewBound(NotificationEntry notificationEntry) {
                    NotificationVoiceReplyController.Connection connection2 = NotificationVoiceReplyController.Connection.this;
                    NotificationVoiceReplyController notificationVoiceReplyController4 = notificationVoiceReplyController3;
                    Objects.requireNonNull(connection2);
                    if (!connection2.entryReinflations.tryEmit(new Pair<>(notificationEntry, "onViewBound"))) {
                        NotificationVoiceReplyLogger notificationVoiceReplyLogger = notificationVoiceReplyController4.logger;
                        String str = notificationEntry.mKey;
                        Objects.requireNonNull(notificationVoiceReplyLogger);
                        LogBuffer logBuffer = notificationVoiceReplyLogger.logBuffer;
                        LogLevel logLevel = LogLevel.DEBUG;
                        NotificationVoiceReplyLogger$logReinflationDropped$2 notificationVoiceReplyLogger$logReinflationDropped$2 = NotificationVoiceReplyLogger$logReinflationDropped$2.INSTANCE;
                        Objects.requireNonNull(logBuffer);
                        if (!logBuffer.frozen) {
                            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logReinflationDropped$2);
                            obtain.str1 = str;
                            obtain.str2 = "onViewBound";
                            logBuffer.push(obtain);
                        }
                    }
                }
            };
            notifCollectionListener = new NotifCollectionListener() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$refreshCandidateOnNotifChanges$2$notifListener$1
                @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
                public final void onEntryRemoved(NotificationEntry notificationEntry, int i2) {
                    NotificationVoiceReplyController.Connection connection2 = NotificationVoiceReplyController.Connection.this;
                    Objects.requireNonNull(connection2);
                    if (!connection2.entryRemovals.tryEmit(notificationEntry.mKey)) {
                        NotificationVoiceReplyLogger notificationVoiceReplyLogger = notificationVoiceReplyController3.logger;
                        String str = notificationEntry.mKey;
                        Objects.requireNonNull(notificationVoiceReplyLogger);
                        LogBuffer logBuffer = notificationVoiceReplyLogger.logBuffer;
                        LogLevel logLevel = LogLevel.WARNING;
                        NotificationVoiceReplyLogger$logRemovalDropped$2 notificationVoiceReplyLogger$logRemovalDropped$2 = NotificationVoiceReplyLogger$logRemovalDropped$2.INSTANCE;
                        Objects.requireNonNull(logBuffer);
                        if (!logBuffer.frozen) {
                            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logRemovalDropped$2);
                            obtain.str1 = str;
                            logBuffer.push(obtain);
                        }
                    }
                }
            };
            notificationVoiceReplyController3.notifCollection.addCollectionListener(notifCollectionListener);
            BindEventManager bindEventManager = this.this$0.bindEventManager;
            Objects.requireNonNull(bindEventManager);
            bindEventManager.listeners.addIfAbsent(listener);
            notificationVoiceReplyController = this.this$0;
            try {
                this.L$0 = listener;
                this.L$1 = notifCollectionListener;
                this.L$2 = notificationVoiceReplyController;
                this.label = 1;
                CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(R$color.intercepted(this), 1);
                cancellableContinuationImpl.initCancellability();
                if (cancellableContinuationImpl.getResult() == coroutineSingletons) {
                    return coroutineSingletons;
                }
                listener2 = listener;
                notificationVoiceReplyController2 = notificationVoiceReplyController;
                notifCollectionListener2 = notifCollectionListener;
            } catch (Throwable th2) {
                th = th2;
                notificationVoiceReplyController.notifCollection.removeCollectionListener(notifCollectionListener);
                BindEventManager bindEventManager2 = notificationVoiceReplyController.bindEventManager;
                Objects.requireNonNull(bindEventManager2);
                bindEventManager2.listeners.remove(listener);
                throw th;
            }
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            notificationVoiceReplyController2 = (NotificationVoiceReplyController) this.L$2;
            notifCollectionListener2 = (NotificationVoiceReplyController$refreshCandidateOnNotifChanges$2$notifListener$1) this.L$1;
            listener2 = (BindEventManager.Listener) this.L$0;
            try {
                ResultKt.throwOnFailure(obj);
            } catch (Throwable th3) {
                notifCollectionListener = notifCollectionListener2;
                notificationVoiceReplyController = notificationVoiceReplyController2;
                listener = listener2;
                th = th3;
                notificationVoiceReplyController.notifCollection.removeCollectionListener(notifCollectionListener);
                BindEventManager bindEventManager22 = notificationVoiceReplyController.bindEventManager;
                Objects.requireNonNull(bindEventManager22);
                bindEventManager22.listeners.remove(listener);
                throw th;
            }
        }
        throw new KotlinNothingValueException();
    }
}
