package com.google.android.systemui.columbus.actions;

import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
/* compiled from: SnoozeAlarm.kt */
/* loaded from: classes.dex */
public final class SnoozeAlarm extends DeskClockAction {
    public final String tag = "Columbus/SnoozeAlarm";

    @Override // com.google.android.systemui.columbus.actions.DeskClockAction
    public final String getAlertAction() {
        return "com.google.android.deskclock.action.ALARM_ALERT";
    }

    @Override // com.google.android.systemui.columbus.actions.DeskClockAction
    public final String getDoneAction() {
        return "com.google.android.deskclock.action.ALARM_DONE";
    }

    @Override // com.google.android.systemui.columbus.actions.DeskClockAction
    public final Intent createDismissIntent() {
        return new Intent("android.intent.action.SNOOZE_ALARM");
    }

    public SnoozeAlarm(Context context, SilenceAlertsDisabled silenceAlertsDisabled, IActivityManager iActivityManager) {
        super(context, silenceAlertsDisabled, iActivityManager);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }
}
