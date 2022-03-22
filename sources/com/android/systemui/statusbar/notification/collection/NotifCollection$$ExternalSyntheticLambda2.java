package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.dump.LogBufferEulogizer;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.util.Assert;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NotifCollection$$ExternalSyntheticLambda2 implements NotifLifetimeExtender.OnEndLifetimeExtensionCallback {
    public final /* synthetic */ NotifCollection f$0;

    public /* synthetic */ NotifCollection$$ExternalSyntheticLambda2(NotifCollection notifCollection) {
        this.f$0 = notifCollection;
    }

    public final void onEndLifetimeExtension(NotifLifetimeExtender notifLifetimeExtender, NotificationEntry notificationEntry) {
        NotifCollection notifCollection = this.f$0;
        Objects.requireNonNull(notifCollection);
        Assert.isMainThread();
        if (notifCollection.mAttached) {
            notifCollection.checkForReentrantCall();
            boolean z = true;
            if (notificationEntry.mLifetimeExtenders.remove(notifLifetimeExtender)) {
                notifCollection.mLogger.logLifetimeExtensionEnded(notificationEntry.mKey, notifLifetimeExtender, notificationEntry.mLifetimeExtenders.size());
                if (notificationEntry.mLifetimeExtenders.size() <= 0) {
                    z = false;
                }
                if (!z && notifCollection.tryRemoveNotification(notificationEntry)) {
                    notifCollection.dispatchEventsAndRebuildList();
                    return;
                }
                return;
            }
            LogBufferEulogizer logBufferEulogizer = notifCollection.mEulogizer;
            IllegalStateException illegalStateException = new IllegalStateException(String.format("Cannot end lifetime extension for extender \"%s\" (%s)", notifLifetimeExtender.getName(), notifLifetimeExtender));
            logBufferEulogizer.record(illegalStateException);
            throw illegalStateException;
        }
    }
}
