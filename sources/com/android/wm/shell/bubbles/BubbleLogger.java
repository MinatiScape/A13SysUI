package com.android.wm.shell.bubbles;

import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BubbleLogger {
    public final UiEventLogger mUiEventLogger;

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum Event implements UiEventLogger.UiEventEnum {
        BUBBLE_OVERFLOW_ADD_USER_GESTURE(483),
        BUBBLE_OVERFLOW_ADD_AGED(484),
        BUBBLE_OVERFLOW_REMOVE_MAX_REACHED(485),
        BUBBLE_OVERFLOW_REMOVE_CANCEL(486),
        BUBBLE_OVERFLOW_REMOVE_GROUP_CANCEL(487),
        BUBBLE_OVERFLOW_REMOVE_NO_LONGER_BUBBLE(488),
        BUBBLE_OVERFLOW_REMOVE_BACK_TO_STACK(489),
        BUBBLE_OVERFLOW_REMOVE_BLOCKED(490),
        BUBBLE_OVERFLOW_SELECTED(600),
        BUBBLE_OVERFLOW_RECOVER(691);
        
        private final int mId;

        Event(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    public final void log(Bubble bubble, Event event) {
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        Objects.requireNonNull(bubble);
        uiEventLogger.logWithInstanceId(event, bubble.mAppUid, bubble.mPackageName, bubble.mInstanceId);
    }

    public BubbleLogger(UiEventLogger uiEventLogger) {
        this.mUiEventLogger = uiEventLogger;
    }
}
