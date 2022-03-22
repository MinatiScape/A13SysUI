package com.google.android.systemui.assist.uihints.edgelights.mode;

import com.android.internal.logging.UiEventLogger;
/* compiled from: AssistantModeChangeEvent.kt */
/* loaded from: classes.dex */
public enum AssistantModeChangeEvent implements UiEventLogger.UiEventEnum {
    ASSISTANT_SESSION_MODE_GONE(585),
    /* JADX INFO: Fake field, exist only in values array */
    ASSISTANT_SESSION_MODE_HALF_LISTENING(586),
    ASSISTANT_SESSION_MODE_FULL_LISTENING(587),
    ASSISTANT_SESSION_MODE_FULFILL_BOTTOM(588),
    ASSISTANT_SESSION_MODE_FULFILL_PERIMETER(589),
    ASSISTANT_SESSION_MODE_UNKNOWN(590);
    
    private final int id;

    AssistantModeChangeEvent(int i) {
        this.id = i;
    }

    public final int getId() {
        return this.id;
    }
}
