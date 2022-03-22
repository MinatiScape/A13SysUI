package com.google.android.systemui.columbus.actions;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda18;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import dagger.Lazy;
import java.util.Objects;
/* compiled from: OpenNotificationShade.kt */
/* loaded from: classes.dex */
public final class OpenNotificationShade extends UserAction {
    public final Lazy<NotificationShadeWindowController> notificationShadeWindowController;
    public final Lazy<StatusBar> statusBar;
    public final String tag = "Columbus/OpenNotif";
    public final UiEventLogger uiEventLogger;

    @Override // com.google.android.systemui.columbus.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        if (this.notificationShadeWindowController.get().getPanelExpanded()) {
            StatusBar statusBar = this.statusBar.get();
            Objects.requireNonNull(statusBar);
            DelayableExecutor delayableExecutor = statusBar.mMainExecutor;
            ShadeController shadeController = statusBar.mShadeController;
            Objects.requireNonNull(shadeController);
            delayableExecutor.execute(new BubbleStackView$$ExternalSyntheticLambda18(shadeController, 6));
            this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_NOTIFICATION_SHADE_CLOSE);
            return;
        }
        StatusBar statusBar2 = this.statusBar.get();
        Objects.requireNonNull(statusBar2);
        statusBar2.mCommandQueueCallbacks.animateExpandNotificationsPanel();
        this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_NOTIFICATION_SHADE_OPEN);
    }

    public OpenNotificationShade(Context context, Lazy<NotificationShadeWindowController> lazy, Lazy<StatusBar> lazy2, UiEventLogger uiEventLogger) {
        super(context);
        this.notificationShadeWindowController = lazy;
        this.statusBar = lazy2;
        this.uiEventLogger = uiEventLogger;
        setAvailable(true);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }
}
