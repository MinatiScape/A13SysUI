package com.google.android.systemui.statusbar.notification.voicereplies;

import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.function.BiFunction;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.ChildHandle;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobNode;
import kotlinx.coroutines.JobSupport;
import kotlinx.coroutines.StandaloneCoroutine;
import kotlinx.coroutines.flow.MutableSharedFlow;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$connect$1 implements NotificationVoiceReplyManager, Job {
    public final /* synthetic */ Job $$delegate_0;
    public final /* synthetic */ NotificationVoiceReplyController.Connection $connection;
    public final /* synthetic */ Job $job;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    @Override // kotlinx.coroutines.Job
    public final ChildHandle attachChild(JobSupport jobSupport) {
        return this.$$delegate_0.attachChild(jobSupport);
    }

    @Override // kotlinx.coroutines.Job
    public final void cancel(CancellationException cancellationException) {
        this.$$delegate_0.cancel(cancellationException);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public final <R> R fold(R r, Function2<? super R, ? super CoroutineContext.Element, ? extends R> function2) {
        return (R) this.$$delegate_0.fold(r, function2);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public final <E extends CoroutineContext.Element> E get(CoroutineContext.Key<E> key) {
        return (E) this.$$delegate_0.get(key);
    }

    @Override // kotlinx.coroutines.Job
    public final CancellationException getCancellationException() {
        return this.$$delegate_0.getCancellationException();
    }

    @Override // kotlin.coroutines.CoroutineContext.Element
    public final CoroutineContext.Key<?> getKey() {
        return this.$$delegate_0.getKey();
    }

    @Override // kotlinx.coroutines.Job
    public final DisposableHandle invokeOnCompletion(boolean z, boolean z2, JobNode jobNode) {
        return this.$$delegate_0.invokeOnCompletion(z, z2, jobNode);
    }

    @Override // kotlinx.coroutines.Job
    public final boolean isActive() {
        return this.$$delegate_0.isActive();
    }

    @Override // kotlinx.coroutines.Job
    public final Object join(Continuation<? super Unit> continuation) {
        return this.$$delegate_0.join(continuation);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public final CoroutineContext minusKey(CoroutineContext.Key<?> key) {
        return this.$$delegate_0.minusKey(key);
    }

    @Override // kotlin.coroutines.CoroutineContext
    public final CoroutineContext plus(CoroutineContext coroutineContext) {
        return this.$$delegate_0.plus(coroutineContext);
    }

    @Override // kotlinx.coroutines.Job
    public final boolean start() {
        return this.$$delegate_0.start();
    }

    public NotificationVoiceReplyController$connect$1(StandaloneCoroutine standaloneCoroutine, NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection) {
        this.$job = standaloneCoroutine;
        this.this$0 = notificationVoiceReplyController;
        this.$connection = connection;
        this.$$delegate_0 = standaloneCoroutine;
    }

    public final void ensureConnected() {
        if (!isActive()) {
            throw new IllegalStateException("Manager is no longer connected".toString());
        }
    }

    @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager
    public final NotificationVoiceReplyController$connect$1$registerHandler$1 registerHandler(NotificationVoiceReplyHandler notificationVoiceReplyHandler) {
        ensureConnected();
        NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
        NotificationVoiceReplyController.Connection connection = this.$connection;
        Objects.requireNonNull(notificationVoiceReplyController);
        Objects.requireNonNull(connection);
        Map<Integer, List<NotificationVoiceReplyHandler>> map = connection.activeHandlersByUser;
        Integer valueOf = Integer.valueOf(notificationVoiceReplyHandler.getUserId());
        final NotificationVoiceReplyController$registerHandler$1 notificationVoiceReplyController$registerHandler$1 = NotificationVoiceReplyController$registerHandler$1.INSTANCE;
        List<NotificationVoiceReplyHandler> compute = map.compute(valueOf, new BiFunction() { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$getOrPut$1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                if (obj2 == null) {
                    return notificationVoiceReplyController$registerHandler$1.invoke();
                }
                return obj2;
            }
        });
        Intrinsics.checkNotNull(compute);
        compute.add(notificationVoiceReplyHandler);
        return new NotificationVoiceReplyController$connect$1$registerHandler$1(new SafeSubscription(new NotificationVoiceReplyController$registerHandler$2(connection, notificationVoiceReplyHandler, notificationVoiceReplyController)), this, this.this$0, this.$connection, notificationVoiceReplyHandler);
    }

    @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager
    public final Object requestHideQuickPhraseCTA(Continuation<? super Unit> continuation) {
        ensureConnected();
        NotificationVoiceReplyController.Connection connection = this.$connection;
        Objects.requireNonNull(connection);
        MutableSharedFlow<Unit> mutableSharedFlow = connection.hideVisibleQuickPhraseCtaRequests;
        Unit unit = Unit.INSTANCE;
        Object emit = mutableSharedFlow.emit(unit, continuation);
        if (emit == CoroutineSingletons.COROUTINE_SUSPENDED) {
            return emit;
        }
        return unit;
    }
}
