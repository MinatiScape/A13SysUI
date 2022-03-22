package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.app.NotificationChannel;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.UserHandle;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class ChannelChangedEvent extends NotifEvent {
    public final NotificationChannel channel;
    public final int modificationType;
    public final String pkgName;
    public final UserHandle user;

    public ChannelChangedEvent(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        super(0);
        this.pkgName = str;
        this.user = userHandle;
        this.channel = notificationChannel;
        this.modificationType = i;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChannelChangedEvent)) {
            return false;
        }
        ChannelChangedEvent channelChangedEvent = (ChannelChangedEvent) obj;
        return Intrinsics.areEqual(this.pkgName, channelChangedEvent.pkgName) && Intrinsics.areEqual(this.user, channelChangedEvent.user) && Intrinsics.areEqual(this.channel, channelChangedEvent.channel) && this.modificationType == channelChangedEvent.modificationType;
    }

    public final int hashCode() {
        int hashCode = this.user.hashCode();
        int hashCode2 = this.channel.hashCode();
        return Integer.hashCode(this.modificationType) + ((hashCode2 + ((hashCode + (this.pkgName.hashCode() * 31)) * 31)) * 31);
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public final void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onNotificationChannelModified(this.pkgName, this.user, this.channel, this.modificationType);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ChannelChangedEvent(pkgName=");
        m.append(this.pkgName);
        m.append(", user=");
        m.append(this.user);
        m.append(", channel=");
        m.append(this.channel);
        m.append(", modificationType=");
        return Insets$$ExternalSyntheticOutline0.m(m, this.modificationType, ')');
    }
}
