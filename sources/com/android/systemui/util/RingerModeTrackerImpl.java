package com.android.systemui.util;

import android.media.AudioManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import java.util.concurrent.Executor;
/* compiled from: RingerModeTrackerImpl.kt */
/* loaded from: classes.dex */
public final class RingerModeTrackerImpl implements RingerModeTracker {
    public final RingerModeLiveData ringerMode;
    public final RingerModeLiveData ringerModeInternal;

    public RingerModeTrackerImpl(AudioManager audioManager, BroadcastDispatcher broadcastDispatcher, Executor executor) {
        this.ringerMode = new RingerModeLiveData(broadcastDispatcher, executor, "android.media.RINGER_MODE_CHANGED", new RingerModeTrackerImpl$ringerMode$1(audioManager));
        this.ringerModeInternal = new RingerModeLiveData(broadcastDispatcher, executor, "android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION", new RingerModeTrackerImpl$ringerModeInternal$1(audioManager));
    }

    @Override // com.android.systemui.util.RingerModeTracker
    public final RingerModeLiveData getRingerMode() {
        return this.ringerMode;
    }

    @Override // com.android.systemui.util.RingerModeTracker
    public final RingerModeLiveData getRingerModeInternal() {
        return this.ringerModeInternal;
    }
}
