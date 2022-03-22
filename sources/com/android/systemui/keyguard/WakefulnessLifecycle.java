package com.android.systemui.keyguard;

import android.app.IWallpaperManager;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public final class WakefulnessLifecycle extends Lifecycle<Observer> implements Dumpable {
    public final Context mContext;
    public final DisplayMetrics mDisplayMetrics;
    public final IWallpaperManager mWallpaperManagerService;
    public int mWakefulness = 2;
    public int mLastWakeReason = 0;
    public Point mLastWakeOriginLocation = null;
    public int mLastSleepReason = 0;
    public Point mLastSleepOriginLocation = null;

    /* loaded from: classes.dex */
    public interface Observer {
        default void onFinishedGoingToSleep() {
        }

        default void onFinishedWakingUp() {
        }

        default void onPostFinishedWakingUp() {
        }

        default void onStartedGoingToSleep() {
        }

        default void onStartedWakingUp() {
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "WakefulnessLifecycle:", "  mWakefulness=");
        m.append(this.mWakefulness);
        printWriter.println(m.toString());
    }

    public final Point getPowerButtonOrigin() {
        boolean z = true;
        if (this.mContext.getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        if (z) {
            return new Point(this.mDisplayMetrics.widthPixels, this.mContext.getResources().getDimensionPixelSize(2131166769));
        }
        return new Point(this.mContext.getResources().getDimensionPixelSize(2131166769), this.mDisplayMetrics.heightPixels);
    }

    public WakefulnessLifecycle(Context context, IWallpaperManager iWallpaperManager, DumpManager dumpManager) {
        this.mContext = context;
        this.mDisplayMetrics = context.getResources().getDisplayMetrics();
        this.mWallpaperManagerService = iWallpaperManager;
        dumpManager.registerDumpable("WakefulnessLifecycle", this);
    }
}
