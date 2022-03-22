package com.android.wm.shell.draganddrop;

import android.content.pm.ActivityInfo;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
/* loaded from: classes.dex */
public final class DragAndDropEventLogger {
    public ActivityInfo mActivityInfo;
    public final InstanceIdSequence mIdSequence = new InstanceIdSequence(Integer.MAX_VALUE);
    public InstanceId mInstanceId;
    public final UiEventLogger mUiEventLogger;

    /* loaded from: classes.dex */
    public enum DragAndDropUiEventEnum implements UiEventLogger.UiEventEnum {
        GLOBAL_APP_DRAG_START_ACTIVITY(884),
        GLOBAL_APP_DRAG_START_SHORTCUT(885),
        GLOBAL_APP_DRAG_START_TASK(888),
        GLOBAL_APP_DRAG_DROPPED(887),
        GLOBAL_APP_DRAG_END(886);
        
        private final int mId;

        DragAndDropUiEventEnum(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    public final void log(DragAndDropUiEventEnum dragAndDropUiEventEnum, ActivityInfo activityInfo) {
        int i;
        String str;
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        if (activityInfo == null) {
            i = 0;
        } else {
            i = activityInfo.applicationInfo.uid;
        }
        if (activityInfo == null) {
            str = null;
        } else {
            str = activityInfo.applicationInfo.packageName;
        }
        uiEventLogger.logWithInstanceId(dragAndDropUiEventEnum, i, str, this.mInstanceId);
    }

    public DragAndDropEventLogger(UiEventLogger uiEventLogger) {
        this.mUiEventLogger = uiEventLogger;
    }
}
