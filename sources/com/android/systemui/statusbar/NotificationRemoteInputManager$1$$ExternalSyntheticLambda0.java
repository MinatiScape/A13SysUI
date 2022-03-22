package com.android.systemui.statusbar;

import android.app.PendingIntent;
import android.util.Pair;
import android.view.View;
import android.widget.RemoteViews;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationRemoteInputManager$1$$ExternalSyntheticLambda0 implements NotificationRemoteInputManager.ClickHandler {
    public final /* synthetic */ NotificationRemoteInputManager.AnonymousClass1 f$0;
    public final /* synthetic */ RemoteViews.RemoteResponse f$1;
    public final /* synthetic */ View f$2;
    public final /* synthetic */ NotificationEntry f$3;
    public final /* synthetic */ PendingIntent f$4;

    public /* synthetic */ NotificationRemoteInputManager$1$$ExternalSyntheticLambda0(NotificationRemoteInputManager.AnonymousClass1 r1, RemoteViews.RemoteResponse remoteResponse, View view, NotificationEntry notificationEntry, PendingIntent pendingIntent) {
        this.f$0 = r1;
        this.f$1 = remoteResponse;
        this.f$2 = view;
        this.f$3 = notificationEntry;
        this.f$4 = pendingIntent;
    }

    public final boolean handleClick() {
        String str;
        NotificationRemoteInputManager.AnonymousClass1 r0 = this.f$0;
        RemoteViews.RemoteResponse remoteResponse = this.f$1;
        View view = this.f$2;
        NotificationEntry notificationEntry = this.f$3;
        PendingIntent pendingIntent = this.f$4;
        Objects.requireNonNull(r0);
        Pair launchOptions = remoteResponse.getLaunchOptions(view);
        ActionClickLogger actionClickLogger = NotificationRemoteInputManager.this.mLogger;
        Objects.requireNonNull(actionClickLogger);
        LogBuffer logBuffer = actionClickLogger.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ActionClickLogger$logStartingIntentWithDefaultHandler$2 actionClickLogger$logStartingIntentWithDefaultHandler$2 = ActionClickLogger$logStartingIntentWithDefaultHandler$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", logLevel, actionClickLogger$logStartingIntentWithDefaultHandler$2);
            if (notificationEntry == null) {
                str = null;
            } else {
                str = notificationEntry.mKey;
            }
            obtain.str1 = str;
            obtain.str2 = pendingIntent.getIntent().toString();
            logBuffer.push(obtain);
        }
        boolean startPendingIntent = RemoteViews.startPendingIntent(view, pendingIntent, launchOptions);
        if (startPendingIntent) {
            NotificationRemoteInputManager notificationRemoteInputManager = NotificationRemoteInputManager.this;
            if (notificationEntry == null) {
                Objects.requireNonNull(notificationRemoteInputManager);
            } else {
                NotificationRemoteInputManager.RemoteInputListener remoteInputListener = notificationRemoteInputManager.mRemoteInputListener;
                if (remoteInputListener != null) {
                    remoteInputListener.releaseNotificationIfKeptForRemoteInputHistory(notificationEntry);
                }
            }
        }
        return startPendingIntent;
    }
}
