package com.android.wm.shell.splitscreen;

import android.util.Slog;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.util.FrameworkStatsLog;
import com.android.systemui.plugins.FalsingManager;
/* loaded from: classes.dex */
public final class SplitscreenEventLogger {
    public int mDragEnterPosition;
    public InstanceId mDragEnterSessionId;
    public InstanceId mLoggerSessionId;
    public int mLastMainStagePosition = -1;
    public int mLastMainStageUid = -1;
    public int mLastSideStagePosition = -1;
    public int mLastSideStageUid = -1;
    public float mLastSplitRatio = -1.0f;
    public final InstanceIdSequence mIdSequence = new InstanceIdSequence(Integer.MAX_VALUE);

    public static int getMainStagePositionFromSplitPosition(int i, boolean z) {
        if (i == -1) {
            return 0;
        }
        return z ? i == 0 ? 1 : 2 : i == 0 ? 3 : 4;
    }

    public static int getSideStagePositionFromSplitPosition(int i, boolean z) {
        if (i == -1) {
            return 0;
        }
        return z ? i == 0 ? 1 : 2 : i == 0 ? 3 : 4;
    }

    public final void logEnter(float f, int i, int i2, int i3, int i4, boolean z) {
        int i5;
        this.mLoggerSessionId = this.mIdSequence.newInstanceId();
        int i6 = this.mDragEnterPosition;
        boolean z2 = true;
        if (i6 == -1) {
            i5 = 1;
        } else if (z) {
            if (i6 == 0) {
                i5 = 2;
            } else {
                i5 = 4;
            }
        } else if (i6 == 0) {
            i5 = 3;
        } else {
            i5 = 5;
        }
        updateMainStageState(getMainStagePositionFromSplitPosition(i, z), i2);
        updateSideStageState(getSideStagePositionFromSplitPosition(i3, z), i4);
        int i7 = 0;
        if (Float.compare(this.mLastSplitRatio, f) == 0) {
            z2 = false;
        }
        if (z2) {
            this.mLastSplitRatio = f;
        }
        int i8 = this.mLastMainStagePosition;
        int i9 = this.mLastMainStageUid;
        int i10 = this.mLastSideStagePosition;
        int i11 = this.mLastSideStageUid;
        InstanceId instanceId = this.mDragEnterSessionId;
        if (instanceId != null) {
            i7 = instanceId.getId();
        }
        FrameworkStatsLog.write(388, 1, i5, 0, f, i8, i9, i10, i11, i7, this.mLoggerSessionId.getId());
    }

    public final void logExit(int i, int i2, int i3, int i4, int i5, boolean z) {
        int i6;
        if (this.mLoggerSessionId != null) {
            if ((i2 == -1 || i4 == -1) && (i3 == 0 || i5 == 0)) {
                switch (i) {
                    case 1:
                        i6 = 8;
                        break;
                    case 2:
                        i6 = 7;
                        break;
                    case 3:
                        i6 = 5;
                        break;
                    case 4:
                        i6 = 1;
                        break;
                    case 5:
                        i6 = 2;
                        break;
                    case FalsingManager.VERSION /* 6 */:
                        i6 = 6;
                        break;
                    case 7:
                        i6 = 3;
                        break;
                    case 8:
                        i6 = 4;
                        break;
                    default:
                        Slog.e("SplitscreenEventLogger", "Unknown exit reason: " + i);
                        i6 = 0;
                        break;
                }
                FrameworkStatsLog.write(388, 2, 0, i6, 0.0f, getMainStagePositionFromSplitPosition(i2, z), i3, getSideStagePositionFromSplitPosition(i4, z), i5, 0, this.mLoggerSessionId.getId());
                this.mLoggerSessionId = null;
                this.mDragEnterPosition = -1;
                this.mDragEnterSessionId = null;
                this.mLastMainStagePosition = -1;
                this.mLastMainStageUid = -1;
                this.mLastSideStagePosition = -1;
                this.mLastSideStageUid = -1;
                return;
            }
            throw new IllegalArgumentException("Only main or side stage should be set");
        }
    }

    public final boolean updateMainStageState(int i, int i2) {
        boolean z;
        if (this.mLastMainStagePosition == i && this.mLastMainStageUid == i2) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            return false;
        }
        this.mLastMainStagePosition = i;
        this.mLastMainStageUid = i2;
        return true;
    }

    public final boolean updateSideStageState(int i, int i2) {
        boolean z;
        if (this.mLastSideStagePosition == i && this.mLastSideStageUid == i2) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            return false;
        }
        this.mLastSideStagePosition = i;
        this.mLastSideStageUid = i2;
        return true;
    }
}
