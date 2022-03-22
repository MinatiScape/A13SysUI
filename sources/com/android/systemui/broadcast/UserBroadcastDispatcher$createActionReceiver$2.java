package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: UserBroadcastDispatcher.kt */
/* loaded from: classes.dex */
public final class UserBroadcastDispatcher$createActionReceiver$2 extends Lambda implements Function1<BroadcastReceiver, Unit> {
    public final /* synthetic */ String $action;
    public final /* synthetic */ UserBroadcastDispatcher this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public UserBroadcastDispatcher$createActionReceiver$2(UserBroadcastDispatcher userBroadcastDispatcher, String str) {
        super(1);
        this.this$0 = userBroadcastDispatcher;
        this.$action = str;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(BroadcastReceiver broadcastReceiver) {
        try {
            this.this$0.context.unregisterReceiver(broadcastReceiver);
            UserBroadcastDispatcher userBroadcastDispatcher = this.this$0;
            userBroadcastDispatcher.logger.logContextReceiverUnregistered(userBroadcastDispatcher.userId, this.$action);
        } catch (IllegalArgumentException e) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Trying to unregister unregistered receiver for user ");
            m.append(this.this$0.userId);
            m.append(", action ");
            m.append(this.$action);
            Log.e("UserBroadcastDispatcher", m.toString(), new IllegalStateException(e));
        }
        return Unit.INSTANCE;
    }
}
