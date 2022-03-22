package com.google.android.systemui.columbus.feedback;

import android.os.PowerManager;
import android.os.SystemClock;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import dagger.Lazy;
/* compiled from: UserActivity.kt */
/* loaded from: classes.dex */
public final class UserActivity implements FeedbackEffect {
    public final Lazy<PowerManager> powerManager;

    @Override // com.google.android.systemui.columbus.feedback.FeedbackEffect
    public final void onGestureDetected(int i, GestureSensor.DetectionProperties detectionProperties) {
        if (i != 0) {
            this.powerManager.get().userActivity(SystemClock.uptimeMillis(), 0, 0);
        }
    }

    public UserActivity(Lazy<PowerManager> lazy) {
        this.powerManager = lazy;
    }
}
