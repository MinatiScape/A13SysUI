package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ActionReceiver.kt */
/* loaded from: classes.dex */
final class ActionReceiver$removeReceiver$1 extends Lambda implements Function1<ReceiverData, Boolean> {
    public final /* synthetic */ BroadcastReceiver $receiver;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ActionReceiver$removeReceiver$1(BroadcastReceiver broadcastReceiver) {
        super(1);
        this.$receiver = broadcastReceiver;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(ReceiverData receiverData) {
        ReceiverData receiverData2 = receiverData;
        Objects.requireNonNull(receiverData2);
        return Boolean.valueOf(Intrinsics.areEqual(receiverData2.receiver, this.$receiver));
    }
}
