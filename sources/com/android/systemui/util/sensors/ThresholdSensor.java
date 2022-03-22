package com.android.systemui.util.sensors;
/* loaded from: classes.dex */
public interface ThresholdSensor {

    /* loaded from: classes.dex */
    public interface Listener {
        void onThresholdCrossed(ThresholdSensorEvent thresholdSensorEvent);
    }

    boolean isLoaded();

    void pause();

    void register(Listener listener);

    void resume();

    void setDelay();

    void setTag(String str);

    void unregister(Listener listener);
}
