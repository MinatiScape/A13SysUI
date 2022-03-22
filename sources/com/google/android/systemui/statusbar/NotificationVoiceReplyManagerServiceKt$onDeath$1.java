package com.google.android.systemui.statusbar;

import android.os.IBinder;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerServiceKt$onDeath$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ IBinder.DeathRecipient $recipient;
    public final /* synthetic */ IBinder $this_onDeath;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerServiceKt$onDeath$1(IBinder iBinder, NotificationVoiceReplyManagerServiceKt$onDeath$recipient$1 notificationVoiceReplyManagerServiceKt$onDeath$recipient$1) {
        super(0);
        this.$this_onDeath = iBinder;
        this.$recipient = notificationVoiceReplyManagerServiceKt$onDeath$recipient$1;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        this.$this_onDeath.unlinkToDeath(this.$recipient, 0);
        return Unit.INSTANCE;
    }
}
