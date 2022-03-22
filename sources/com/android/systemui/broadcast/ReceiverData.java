package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.UserHandle;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BroadcastDispatcher.kt */
/* loaded from: classes.dex */
public final class ReceiverData {
    public final Executor executor;
    public final IntentFilter filter;
    public final BroadcastReceiver receiver;
    public final UserHandle user;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ReceiverData)) {
            return false;
        }
        ReceiverData receiverData = (ReceiverData) obj;
        return Intrinsics.areEqual(this.receiver, receiverData.receiver) && Intrinsics.areEqual(this.filter, receiverData.filter) && Intrinsics.areEqual(this.executor, receiverData.executor) && Intrinsics.areEqual(this.user, receiverData.user);
    }

    public final int hashCode() {
        int hashCode = this.filter.hashCode();
        int hashCode2 = this.executor.hashCode();
        return this.user.hashCode() + ((hashCode2 + ((hashCode + (this.receiver.hashCode() * 31)) * 31)) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ReceiverData(receiver=");
        m.append(this.receiver);
        m.append(", filter=");
        m.append(this.filter);
        m.append(", executor=");
        m.append(this.executor);
        m.append(", user=");
        m.append(this.user);
        m.append(')');
        return m.toString();
    }

    public ReceiverData(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor, UserHandle userHandle) {
        this.receiver = broadcastReceiver;
        this.filter = intentFilter;
        this.executor = executor;
        this.user = userHandle;
    }
}
