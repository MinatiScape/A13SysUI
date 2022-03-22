package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import android.content.Intent;
/* loaded from: classes.dex */
public final class DismissTimer extends DeskClockAction {
    @Override // com.google.android.systemui.elmyra.actions.DeskClockAction
    public final String getAlertAction() {
        return "com.google.android.deskclock.action.TIMER_ALERT";
    }

    @Override // com.google.android.systemui.elmyra.actions.DeskClockAction
    public final String getDoneAction() {
        return "com.google.android.deskclock.action.TIMER_DONE";
    }

    @Override // com.google.android.systemui.elmyra.actions.DeskClockAction
    public final Intent createDismissIntent() {
        return new Intent("android.intent.action.DISMISS_TIMER");
    }

    public DismissTimer(Context context) {
        super(context);
    }
}
