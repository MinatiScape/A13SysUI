package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
/* loaded from: classes.dex */
public final class LockTask extends Gate {
    public boolean mIsBlocked;
    public final AnonymousClass1 mTaskStackListener = new TaskStackChangeListener() { // from class: com.google.android.systemui.elmyra.gates.LockTask.1
        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public final void onLockTaskModeChanged(int i) {
            ExifInterface$$ExternalSyntheticOutline1.m("Mode: ", i, "Elmyra/LockTask");
            if (i == 0) {
                LockTask.this.mIsBlocked = false;
            } else {
                LockTask.this.mIsBlocked = true;
            }
            LockTask.this.notifyListener();
        }
    };

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onActivate() {
        TaskStackChangeListeners.INSTANCE.registerTaskStackListener(this.mTaskStackListener);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onDeactivate() {
        TaskStackChangeListeners.INSTANCE.unregisterTaskStackListener(this.mTaskStackListener);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.systemui.elmyra.gates.LockTask$1] */
    public LockTask(Context context) {
        super(context);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final boolean isBlocked() {
        return this.mIsBlocked;
    }
}
