package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import kotlin.collections.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.FilteringSequence;
import kotlin.sequences.Sequence;
/* compiled from: ChannelEditorDialogController.kt */
/* loaded from: classes.dex */
final class ChannelEditorDialogController$getDisplayableChannels$channels$1 extends Lambda implements Function1<NotificationChannelGroup, Sequence<? extends NotificationChannel>> {
    public static final ChannelEditorDialogController$getDisplayableChannels$channels$1 INSTANCE = new ChannelEditorDialogController$getDisplayableChannels$channels$1();

    /* compiled from: ChannelEditorDialogController.kt */
    /* renamed from: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$getDisplayableChannels$channels$1$1  reason: invalid class name */
    /* loaded from: classes.dex */
    final class AnonymousClass1 extends Lambda implements Function1<NotificationChannel, Boolean> {
        public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

        public AnonymousClass1() {
            super(1);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Boolean invoke(NotificationChannel notificationChannel) {
            boolean z;
            NotificationChannel notificationChannel2 = notificationChannel;
            if (notificationChannel2.isImportanceLockedByOEM() || notificationChannel2.getImportance() == 0 || notificationChannel2.isImportanceLockedByCriticalDeviceFunction()) {
                z = true;
            } else {
                z = false;
            }
            return Boolean.valueOf(z);
        }
    }

    public ChannelEditorDialogController$getDisplayableChannels$channels$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Sequence<? extends NotificationChannel> invoke(NotificationChannelGroup notificationChannelGroup) {
        return new FilteringSequence(new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1(notificationChannelGroup.getChannels()), false, AnonymousClass1.INSTANCE);
    }
}
