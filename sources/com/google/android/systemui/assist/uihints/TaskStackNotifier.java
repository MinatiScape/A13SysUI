package com.google.android.systemui.assist.uihints;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.util.Log;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TaskStackNotifier implements NgaMessageHandler.ConfigInfoListener {
    public PendingIntent mIntent;
    public final TaskStackChangeListeners mListeners = TaskStackChangeListeners.INSTANCE;
    public boolean mListenerRegistered = false;
    public final AnonymousClass1 mListener = new TaskStackChangeListener() { // from class: com.google.android.systemui.assist.uihints.TaskStackNotifier.1
        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public final void onTaskCreated(ComponentName componentName) {
            TaskStackNotifier taskStackNotifier = TaskStackNotifier.this;
            Objects.requireNonNull(taskStackNotifier);
            PendingIntent pendingIntent = taskStackNotifier.mIntent;
            if (pendingIntent != null) {
                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    Log.e("TaskStackNotifier", "could not send intent", e);
                }
            }
        }

        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public final void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) {
            TaskStackNotifier taskStackNotifier = TaskStackNotifier.this;
            Objects.requireNonNull(taskStackNotifier);
            PendingIntent pendingIntent = taskStackNotifier.mIntent;
            if (pendingIntent != null) {
                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    Log.e("TaskStackNotifier", "could not send intent", e);
                }
            }
        }
    };

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ConfigInfoListener
    public final void onConfigInfo(NgaMessageHandler.ConfigInfo configInfo) {
        PendingIntent pendingIntent = configInfo.onTaskChange;
        this.mIntent = pendingIntent;
        if (pendingIntent != null && !this.mListenerRegistered) {
            this.mListeners.registerTaskStackListener(this.mListener);
            this.mListenerRegistered = true;
        } else if (pendingIntent == null && this.mListenerRegistered) {
            this.mListeners.unregisterTaskStackListener(this.mListener);
            this.mListenerRegistered = false;
        }
    }
}
