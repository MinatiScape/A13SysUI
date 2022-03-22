package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.StandaloneCoroutine;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
final class DebugNotificationVoiceReplyClient$startClient$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ Job $job;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DebugNotificationVoiceReplyClient$startClient$1(StandaloneCoroutine standaloneCoroutine) {
        super(0);
        this.$job = standaloneCoroutine;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        this.$job.cancel(null);
        return Unit.INSTANCE;
    }
}
