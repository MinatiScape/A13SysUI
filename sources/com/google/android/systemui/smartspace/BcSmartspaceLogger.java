package com.google.android.systemui.smartspace;

import android.util.Log;
import com.android.systemui.shared.system.SysUiStatsLog;
/* loaded from: classes.dex */
public final class BcSmartspaceLogger {
    public static final boolean IS_VERBOSE = Log.isLoggable("StatsLog", 2);

    public static void log(EventEnum eventEnum, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        BcSmartspaceEvent bcSmartspaceEvent = (BcSmartspaceEvent) eventEnum;
        SysUiStatsLog.write(bcSmartspaceEvent.getId(), bcSmartspaceCardLoggingInfo.mInstanceId, bcSmartspaceCardLoggingInfo.mDisplaySurface, bcSmartspaceCardLoggingInfo.mRank, bcSmartspaceCardLoggingInfo.mCardinality, bcSmartspaceCardLoggingInfo.mFeatureType, -1, 0, 0, bcSmartspaceCardLoggingInfo.mReceivedLatency);
        if (IS_VERBOSE) {
            Log.d("StatsLog", String.format("\nLogged Smartspace event(%s), info(%s):", bcSmartspaceEvent, bcSmartspaceCardLoggingInfo.toString()));
        }
    }
}
