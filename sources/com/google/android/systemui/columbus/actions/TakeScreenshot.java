package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.ScreenshotHelper;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.function.Consumer;
/* compiled from: TakeScreenshot.kt */
/* loaded from: classes.dex */
public final class TakeScreenshot extends UserAction {
    public final Handler handler;
    public final ScreenshotHelper screenshotHelper;
    public final String tag = "Columbus/TakeScreenshot";
    public final UiEventLogger uiEventLogger;

    @Override // com.google.android.systemui.columbus.actions.UserAction
    public final boolean availableOnLockscreen() {
        return true;
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.screenshotHelper.takeScreenshot(1, true, true, 6, this.handler, (Consumer) null);
        this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_SCREENSHOT);
    }

    public TakeScreenshot(Context context, Handler handler, UiEventLogger uiEventLogger) {
        super(context);
        this.handler = handler;
        this.uiEventLogger = uiEventLogger;
        this.screenshotHelper = new ScreenshotHelper(context);
        setAvailable(true);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }
}
