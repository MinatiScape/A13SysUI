package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ChannelEditorDialogController.kt */
/* loaded from: classes.dex */
public final class ChannelEditorDialogController$padToFourChannels$1 extends Lambda implements Function1<NotificationChannel, Boolean> {
    public final /* synthetic */ ChannelEditorDialogController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelEditorDialogController$padToFourChannels$1(ChannelEditorDialogController channelEditorDialogController) {
        super(1);
        this.this$0 = channelEditorDialogController;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(NotificationChannel notificationChannel) {
        ChannelEditorDialogController channelEditorDialogController = this.this$0;
        Objects.requireNonNull(channelEditorDialogController);
        return Boolean.valueOf(channelEditorDialogController.paddedChannels.contains(notificationChannel));
    }
}
