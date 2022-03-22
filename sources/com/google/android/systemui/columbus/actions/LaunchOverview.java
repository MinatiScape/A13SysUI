package com.google.android.systemui.columbus.actions;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.recents.Recents;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.sensors.GestureSensor;
/* compiled from: LaunchOverview.kt */
/* loaded from: classes.dex */
public final class LaunchOverview extends UserAction {
    public final Recents recents;
    public final String tag = "Columbus/LaunchOverview";
    public final UiEventLogger uiEventLogger;

    @Override // com.google.android.systemui.columbus.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.recents.toggleRecentApps();
        this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_OVERVIEW);
    }

    public LaunchOverview(Context context, Recents recents, UiEventLogger uiEventLogger) {
        super(context);
        this.recents = recents;
        this.uiEventLogger = uiEventLogger;
        setAvailable(true);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }
}
