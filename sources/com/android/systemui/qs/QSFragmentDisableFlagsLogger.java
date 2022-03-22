package com.android.systemui.qs;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.statusbar.DisableFlagsLogger;
/* compiled from: QSFragmentDisableFlagsLogger.kt */
/* loaded from: classes.dex */
public final class QSFragmentDisableFlagsLogger {
    public final LogBuffer buffer;
    public final DisableFlagsLogger disableFlagsLogger;

    public QSFragmentDisableFlagsLogger(LogBuffer logBuffer, DisableFlagsLogger disableFlagsLogger) {
        this.buffer = logBuffer;
        this.disableFlagsLogger = disableFlagsLogger;
    }
}
