package com.google.android.systemui.elmyra.feedback;

import android.content.Context;
import android.content.res.Resources;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
/* loaded from: classes.dex */
public final class HapticClick implements FeedbackEffect {
    public static final VibrationAttributes TOUCH_VIBRATION_ATTRIBUTES = new VibrationAttributes.Builder().setUsage(18).build();
    public int mLastGestureStage;
    public final VibrationEffect mProgressVibrationEffect;
    public final VibrationEffect mResolveVibrationEffect = VibrationEffect.get(0);
    public final Vibrator mVibrator;

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onRelease() {
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onProgress(float f, int i) {
        Vibrator vibrator;
        if (!(this.mLastGestureStage == 2 || i != 2 || (vibrator = this.mVibrator) == null)) {
            vibrator.vibrate(this.mProgressVibrationEffect, TOUCH_VIBRATION_ATTRIBUTES);
        }
        this.mLastGestureStage = i;
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        Vibrator vibrator;
        if ((detectionProperties == null || !detectionProperties.mHapticConsumed) && (vibrator = this.mVibrator) != null) {
            vibrator.vibrate(this.mResolveVibrationEffect, TOUCH_VIBRATION_ATTRIBUTES);
        }
    }

    public HapticClick(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        this.mVibrator = vibrator;
        VibrationEffect vibrationEffect = VibrationEffect.get(5);
        this.mProgressVibrationEffect = vibrationEffect;
        if (vibrator != null) {
            try {
                vibrator.setAlwaysOnEffect(context.getResources().getInteger(2131492925), vibrationEffect, TOUCH_VIBRATION_ATTRIBUTES);
            } catch (Resources.NotFoundException unused) {
            }
            try {
                this.mVibrator.setAlwaysOnEffect(context.getResources().getInteger(2131492926), this.mResolveVibrationEffect, TOUCH_VIBRATION_ATTRIBUTES);
            } catch (Resources.NotFoundException unused2) {
            }
        }
    }
}
