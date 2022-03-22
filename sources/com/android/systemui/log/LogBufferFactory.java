package com.android.systemui.log;

import android.app.ActivityManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.dump.RegisteredDumpable;
import java.util.Objects;
/* compiled from: LogBufferFactory.kt */
/* loaded from: classes.dex */
public final class LogBufferFactory {
    public final DumpManager dumpManager;
    public final LogcatEchoTracker logcatEchoTracker;

    public final LogBuffer create(String str, int i) {
        return create(str, i, 10, true);
    }

    public final LogBuffer create(String str, int i, int i2, boolean z) {
        if (ActivityManager.isLowRamDeviceStatic()) {
            i = Math.min(i, 20);
        }
        LogBuffer logBuffer = new LogBuffer(str, i, i2, this.logcatEchoTracker, z);
        DumpManager dumpManager = this.dumpManager;
        Objects.requireNonNull(dumpManager);
        synchronized (dumpManager) {
            if (dumpManager.canAssignToNameLocked(str, logBuffer)) {
                dumpManager.buffers.put(str, new RegisteredDumpable(str, logBuffer));
            } else {
                throw new IllegalArgumentException('\'' + str + "' is already registered");
            }
        }
        return logBuffer;
    }

    public LogBufferFactory(DumpManager dumpManager, LogcatEchoTracker logcatEchoTracker) {
        this.dumpManager = dumpManager;
        this.logcatEchoTracker = logcatEchoTracker;
    }
}
