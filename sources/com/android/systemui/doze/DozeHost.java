package com.android.systemui.doze;

import com.android.systemui.ScreenDecorations$2$$ExternalSyntheticLambda0;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.doze.DozeUi;
/* loaded from: classes.dex */
public interface DozeHost {

    /* loaded from: classes.dex */
    public interface Callback {
        default void onDozeSuppressedChanged(boolean z) {
        }

        default void onNotificationAlerted(ScreenDecorations$2$$ExternalSyntheticLambda0 screenDecorations$2$$ExternalSyntheticLambda0) {
        }

        default void onPowerSaveChanged() {
        }
    }

    /* loaded from: classes.dex */
    public interface PulseCallback {
        void onPulseFinished();

        void onPulseStarted();
    }

    void addCallback(DozeTriggers.AnonymousClass1 r1);

    void cancelGentleSleep();

    void dozeTimeTick();

    void extendPulse(int i);

    boolean isBlockingDoze();

    boolean isDozeSuppressed();

    boolean isPowerSaveActive();

    boolean isProvisioned();

    boolean isPulsingBlocked();

    void onIgnoreTouchWhilePulsing(boolean z);

    void onSlpiTap(float f, float f2);

    void prepareForGentleSleep(DozeScreenState$$ExternalSyntheticLambda1 dozeScreenState$$ExternalSyntheticLambda1);

    void pulseWhileDozing(DozeUi.AnonymousClass2 r1, int i);

    void removeCallback(DozeTriggers.AnonymousClass1 r1);

    void setAnimateWakeup(boolean z);

    default void setAodDimmingScrim(float f) {
    }

    void setDozeScreenBrightness(int i);

    void startDozing();

    void stopDozing();
}
