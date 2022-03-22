package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.Log;
import com.android.systemui.log.LogBuffer;
/* compiled from: HeadsUpCoordinatorLogger.kt */
/* loaded from: classes.dex */
public final class HeadsUpCoordinatorLogger {
    public final LogBuffer buffer;
    public final boolean verbose;

    public HeadsUpCoordinatorLogger(LogBuffer logBuffer) {
        boolean isLoggable = Log.isLoggable("HeadsUpCoordinator", 2);
        this.buffer = logBuffer;
        this.verbose = isLoggable;
    }
}
