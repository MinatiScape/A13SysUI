package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import android.content.Intent;
/* loaded from: classes.dex */
public final class SnoozeAlarm extends DeskClockAction {
    @Override // com.google.android.systemui.elmyra.actions.DeskClockAction
    public final String getAlertAction() {
        return "com.google.android.deskclock.action.ALARM_ALERT";
    }

    @Override // com.google.android.systemui.elmyra.actions.DeskClockAction
    public final String getDoneAction() {
        return "com.google.android.deskclock.action.ALARM_DONE";
    }

    @Override // com.google.android.systemui.elmyra.actions.DeskClockAction
    public final Intent createDismissIntent() {
        return new Intent("android.intent.action.SNOOZE_ALARM");
    }

    public SnoozeAlarm(Context context) {
        super(context);
    }
}
