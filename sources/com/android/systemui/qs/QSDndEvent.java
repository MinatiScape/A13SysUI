package com.android.systemui.qs;

import com.android.internal.logging.UiEventLogger;
/* compiled from: QSEvents.kt */
/* loaded from: classes.dex */
public enum QSDndEvent implements UiEventLogger.UiEventEnum {
    QS_DND_CONDITION_SELECT(420),
    QS_DND_TIME_UP(422),
    QS_DND_TIME_DOWN(423),
    QS_DND_DIALOG_ENABLE_FOREVER(946),
    QS_DND_DIALOG_ENABLE_UNTIL_ALARM(947),
    QS_DND_DIALOG_ENABLE_UNTIL_COUNTDOWN(948);
    
    private final int _id;

    QSDndEvent(int i) {
        this._id = i;
    }

    public final int getId() {
        return this._id;
    }
}
