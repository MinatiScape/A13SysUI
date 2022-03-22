package com.google.android.systemui.gamedashboard;

import android.content.Context;
/* loaded from: classes.dex */
public final class YouTubeLiveWidget {
    public final Context mContext;
    public final GameDashboardUiEventLogger mUiEventLogger;
    public final WidgetView mWidgetView;

    public YouTubeLiveWidget(Context context, WidgetView widgetView, GameDashboardUiEventLogger gameDashboardUiEventLogger) {
        this.mContext = context;
        this.mWidgetView = widgetView;
        this.mUiEventLogger = gameDashboardUiEventLogger;
    }
}
