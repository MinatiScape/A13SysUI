package com.android.systemui.qs.tiles.dialog;

import android.content.Context;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.settingslib.notification.ZenModeDialogMetricsLogger;
import com.android.systemui.qs.QSEvents;
/* loaded from: classes.dex */
public final class QSZenModeDialogMetricsLogger extends ZenModeDialogMetricsLogger {
    public final UiEventLoggerImpl mUiEventLogger = QSEvents.qsUiEventsLogger;

    public QSZenModeDialogMetricsLogger(Context context) {
        super(context);
    }
}
