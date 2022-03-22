package com.android.systemui.statusbar.notification.row;

import androidx.collection.ArraySet;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class NotifInflationErrorManager {
    public ArraySet mErroredNotifs = new ArraySet(0);
    public ArrayList mListeners = new ArrayList();

    /* loaded from: classes.dex */
    public interface NotifInflationErrorListener {
        void onNotifInflationError(NotificationEntry notificationEntry, Exception exc);

        default void onNotifInflationErrorCleared() {
        }
    }

    public final void clearInflationError(NotificationEntry notificationEntry) {
        if (this.mErroredNotifs.contains(notificationEntry)) {
            this.mErroredNotifs.remove(notificationEntry);
            for (int i = 0; i < this.mListeners.size(); i++) {
                ((NotifInflationErrorListener) this.mListeners.get(i)).onNotifInflationErrorCleared();
            }
        }
    }

    public final void setInflationError(NotificationEntry notificationEntry, Exception exc) {
        this.mErroredNotifs.add(notificationEntry);
        for (int i = 0; i < this.mListeners.size(); i++) {
            ((NotifInflationErrorListener) this.mListeners.get(i)).onNotifInflationError(notificationEntry, exc);
        }
    }
}
