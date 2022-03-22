package com.android.systemui.util.time;

import android.os.SystemClock;
/* loaded from: classes.dex */
public final class SystemClockImpl implements SystemClock {
    @Override // com.android.systemui.util.time.SystemClock
    public final long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override // com.android.systemui.util.time.SystemClock
    public final long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    @Override // com.android.systemui.util.time.SystemClock
    public final long uptimeMillis() {
        return SystemClock.uptimeMillis();
    }
}
