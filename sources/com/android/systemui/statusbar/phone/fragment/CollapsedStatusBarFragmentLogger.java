package com.android.systemui.statusbar.phone.fragment;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.statusbar.DisableFlagsLogger;
/* compiled from: CollapsedStatusBarFragmentLogger.kt */
/* loaded from: classes.dex */
public final class CollapsedStatusBarFragmentLogger {
    public final LogBuffer buffer;
    public final DisableFlagsLogger disableFlagsLogger;

    public CollapsedStatusBarFragmentLogger(LogBuffer logBuffer, DisableFlagsLogger disableFlagsLogger) {
        this.buffer = logBuffer;
        this.disableFlagsLogger = disableFlagsLogger;
    }
}
