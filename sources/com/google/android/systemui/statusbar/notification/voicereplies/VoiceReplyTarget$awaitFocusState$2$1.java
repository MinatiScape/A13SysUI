package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.policy.RemoteInputView;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class VoiceReplyTarget$awaitFocusState$2$1 extends Lambda implements Function1<Throwable, Unit> {
    public final /* synthetic */ VoiceReplyTarget$awaitFocusState$2$listener$1 $listener;
    public final /* synthetic */ RemoteInputView $this_awaitFocusState;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public VoiceReplyTarget$awaitFocusState$2$1(RemoteInputView remoteInputView, VoiceReplyTarget$awaitFocusState$2$listener$1 voiceReplyTarget$awaitFocusState$2$listener$1) {
        super(1);
        this.$this_awaitFocusState = remoteInputView;
        this.$listener = voiceReplyTarget$awaitFocusState$2$listener$1;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(Throwable th) {
        RemoteInputView remoteInputView = this.$this_awaitFocusState;
        VoiceReplyTarget$awaitFocusState$2$listener$1 voiceReplyTarget$awaitFocusState$2$listener$1 = this.$listener;
        Objects.requireNonNull(remoteInputView);
        remoteInputView.mEditTextFocusChangeListeners.remove(voiceReplyTarget$awaitFocusState$2$listener$1);
        return Unit.INSTANCE;
    }
}
