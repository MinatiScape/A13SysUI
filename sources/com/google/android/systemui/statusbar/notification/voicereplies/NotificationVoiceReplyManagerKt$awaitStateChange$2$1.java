package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.NotificationShadeWindowController;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyManagerKt$awaitStateChange$2$1 extends Lambda implements Function1<Throwable, Unit> {
    public final /* synthetic */ NotificationVoiceReplyManagerKt$awaitStateChange$2$cb$1 $cb;
    public final /* synthetic */ NotificationShadeWindowController $this_awaitStateChange;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerKt$awaitStateChange$2$1(NotificationShadeWindowController notificationShadeWindowController, NotificationVoiceReplyManagerKt$awaitStateChange$2$cb$1 notificationVoiceReplyManagerKt$awaitStateChange$2$cb$1) {
        super(1);
        this.$this_awaitStateChange = notificationShadeWindowController;
        this.$cb = notificationVoiceReplyManagerKt$awaitStateChange$2$cb$1;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(Throwable th) {
        this.$this_awaitStateChange.unregisterCallback(this.$cb);
        return Unit.INSTANCE;
    }
}
