package com.google.android.systemui.elmyra.feedback;

import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
/* loaded from: classes.dex */
public final class UserActivity implements FeedbackEffect {
    public final PowerManager mPowerManager;
    public int mTriggerCount = 0;
    public int mLastStage = 0;
    public final KeyguardStateController mKeyguardStateController = (KeyguardStateController) Dependency.get(KeyguardStateController.class);

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onRelease() {
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onProgress(float f, int i) {
        PowerManager powerManager;
        if (i != this.mLastStage && i == 2 && !this.mKeyguardStateController.isShowing() && (powerManager = this.mPowerManager) != null) {
            powerManager.userActivity(SystemClock.uptimeMillis(), 0, 0);
            this.mTriggerCount++;
        }
        this.mLastStage = i;
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        this.mTriggerCount--;
    }

    public final String toString() {
        return super.toString() + " [mTriggerCount -> " + this.mTriggerCount + "]";
    }

    public UserActivity(Context context) {
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
    }
}
