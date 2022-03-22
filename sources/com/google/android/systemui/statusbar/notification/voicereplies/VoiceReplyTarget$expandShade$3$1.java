package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class VoiceReplyTarget$expandShade$3$1 extends Lambda implements Function1<Throwable, Unit> {
    public final /* synthetic */ VoiceReplyTarget$expandShade$3$callback$1 $callback;
    public final /* synthetic */ VoiceReplyTarget this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public VoiceReplyTarget$expandShade$3$1(VoiceReplyTarget voiceReplyTarget, VoiceReplyTarget$expandShade$3$callback$1 voiceReplyTarget$expandShade$3$callback$1) {
        super(1);
        this.this$0 = voiceReplyTarget;
        this.$callback = voiceReplyTarget$expandShade$3$callback$1;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(Throwable th) {
        this.this$0.statusBarStateController.removeCallback(this.$callback);
        return Unit.INSTANCE;
    }
}
