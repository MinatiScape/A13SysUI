package com.android.systemui.statusbar.policy;

import android.os.RemoteException;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import java.util.Objects;
/* loaded from: classes.dex */
public final class RemoteInputUriController {
    public final AnonymousClass1 mInlineUriListener = new NotifCollectionListener() { // from class: com.android.systemui.statusbar.policy.RemoteInputUriController.1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryRemoved(NotificationEntry notificationEntry, int i) {
            try {
                IStatusBarService iStatusBarService = RemoteInputUriController.this.mStatusBarManagerService;
                Objects.requireNonNull(notificationEntry);
                iStatusBarService.clearInlineReplyUriPermissions(notificationEntry.mKey);
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        }
    };
    public final IStatusBarService mStatusBarManagerService;

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.statusbar.policy.RemoteInputUriController$1] */
    public RemoteInputUriController(IStatusBarService iStatusBarService) {
        this.mStatusBarManagerService = iStatusBarService;
    }
}
