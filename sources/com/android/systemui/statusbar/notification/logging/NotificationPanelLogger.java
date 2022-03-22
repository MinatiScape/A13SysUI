package com.android.systemui.statusbar.notification.logging;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.List;
/* loaded from: classes.dex */
public interface NotificationPanelLogger {
    void logPanelShown(boolean z, List<NotificationEntry> list);

    /* loaded from: classes.dex */
    public enum NotificationPanelEvent implements UiEventLogger.UiEventEnum {
        NOTIFICATION_PANEL_OPEN_STATUS_BAR(200),
        NOTIFICATION_PANEL_OPEN_LOCKSCREEN(201);
        
        private final int mId;

        NotificationPanelEvent(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }
}
