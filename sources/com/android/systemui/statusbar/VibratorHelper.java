package com.android.systemui.statusbar;

import android.media.AudioAttributes;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda5;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class VibratorHelper {
    public static final VibrationAttributes TOUCH_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(18);
    public final Executor mExecutor;
    public final Vibrator mVibrator;

    public final void vibrate(int i) {
        if (hasVibrator()) {
            this.mExecutor.execute(new VibratorHelper$$ExternalSyntheticLambda1(this, i, 0));
        }
    }

    public final boolean hasVibrator() {
        Vibrator vibrator = this.mVibrator;
        if (vibrator == null || !vibrator.hasVibrator()) {
            return false;
        }
        return true;
    }

    public VibratorHelper(Vibrator vibrator, Executor executor) {
        this.mExecutor = executor;
        this.mVibrator = vibrator;
    }

    public final void vibrate(final int i, final String str, final VibrationEffect vibrationEffect, final String str2, final VibrationAttributes vibrationAttributes) {
        if (hasVibrator()) {
            this.mExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.VibratorHelper$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    VibratorHelper vibratorHelper = VibratorHelper.this;
                    int i2 = i;
                    String str3 = str;
                    VibrationEffect vibrationEffect2 = vibrationEffect;
                    String str4 = str2;
                    VibrationAttributes vibrationAttributes2 = vibrationAttributes;
                    Objects.requireNonNull(vibratorHelper);
                    vibratorHelper.mVibrator.vibrate(i2, str3, vibrationEffect2, str4, vibrationAttributes2);
                }
            });
        }
    }

    public final void vibrate(VibrationEffect vibrationEffect, AudioAttributes audioAttributes) {
        if (hasVibrator()) {
            this.mExecutor.execute(new PipTaskOrganizer$$ExternalSyntheticLambda5(this, vibrationEffect, audioAttributes, 3));
        }
    }
}
