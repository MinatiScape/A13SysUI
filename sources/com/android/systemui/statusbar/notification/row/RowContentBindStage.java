package com.android.systemui.statusbar.notification.row;
/* loaded from: classes.dex */
public final class RowContentBindStage extends BindStage<RowContentBindParams> {
    public final NotificationRowContentBinder mBinder;
    public final RowContentBindStageLogger mLogger;
    public final NotifInflationErrorManager mNotifInflationErrorManager;

    public RowContentBindStage(NotificationRowContentBinder notificationRowContentBinder, NotifInflationErrorManager notifInflationErrorManager, RowContentBindStageLogger rowContentBindStageLogger) {
        this.mBinder = notificationRowContentBinder;
        this.mNotifInflationErrorManager = notifInflationErrorManager;
        this.mLogger = rowContentBindStageLogger;
    }
}
