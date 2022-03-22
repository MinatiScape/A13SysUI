package com.android.systemui.assist;

import com.android.internal.logging.UiEventLogger;
/* compiled from: AssistantInvocationEvent.kt */
/* loaded from: classes.dex */
public enum AssistantInvocationEvent implements UiEventLogger.UiEventEnum {
    ASSISTANT_INVOCATION_UNKNOWN(442),
    ASSISTANT_INVOCATION_TOUCH_GESTURE(443),
    /* JADX INFO: Fake field, exist only in values array */
    ASSISTANT_INVOCATION_TOUCH_GESTURE_ALT(444),
    ASSISTANT_INVOCATION_HOTWORD(445),
    ASSISTANT_INVOCATION_QUICK_SEARCH_BAR(446),
    ASSISTANT_INVOCATION_HOME_LONG_PRESS(447),
    ASSISTANT_INVOCATION_PHYSICAL_GESTURE(448),
    ASSISTANT_INVOCATION_START_UNKNOWN(530),
    ASSISTANT_INVOCATION_START_TOUCH_GESTURE(531),
    ASSISTANT_INVOCATION_START_PHYSICAL_GESTURE(532),
    ASSISTANT_INVOCATION_POWER_LONG_PRESS(758);
    
    private final int id;

    AssistantInvocationEvent(int i) {
        this.id = i;
    }

    public final int getId() {
        return this.id;
    }
}
