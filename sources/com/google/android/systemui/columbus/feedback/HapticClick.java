package com.google.android.systemui.columbus.feedback;

import android.media.AudioAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import dagger.Lazy;
/* compiled from: HapticClick.kt */
/* loaded from: classes.dex */
public final class HapticClick implements FeedbackEffect {
    public static final VibrationEffect GESTURE_DETECTED_VIBRATION_EFFECT = VibrationEffect.get(5);
    public static final AudioAttributes SONIFICATION_AUDIO_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    public final Lazy<Vibrator> vibrator;

    @Override // com.google.android.systemui.columbus.feedback.FeedbackEffect
    public final void onGestureDetected(int i, GestureSensor.DetectionProperties detectionProperties) {
        boolean z;
        if (detectionProperties != null && detectionProperties.isHapticConsumed) {
            z = true;
        } else {
            z = false;
        }
        if (!z && i == 1) {
            this.vibrator.get().vibrate(GESTURE_DETECTED_VIBRATION_EFFECT, SONIFICATION_AUDIO_ATTRIBUTES);
        }
    }

    public HapticClick(Lazy<Vibrator> lazy) {
        this.vibrator = lazy;
    }
}
