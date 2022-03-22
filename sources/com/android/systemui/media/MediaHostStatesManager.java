package com.android.systemui.media;

import com.android.systemui.media.MediaHost;
import com.android.systemui.util.animation.MeasurementOutput;
import com.android.systemui.util.animation.TransitionViewState;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;
/* compiled from: MediaHostStatesManager.kt */
/* loaded from: classes.dex */
public final class MediaHostStatesManager {
    public final LinkedHashSet callbacks = new LinkedHashSet();
    public final LinkedHashSet controllers = new LinkedHashSet();
    public final LinkedHashMap carouselSizes = new LinkedHashMap();
    public final LinkedHashMap mediaHostStates = new LinkedHashMap();

    /* compiled from: MediaHostStatesManager.kt */
    /* loaded from: classes.dex */
    public interface Callback {
        void onHostStateChanged(int i, MediaHostState mediaHostState);
    }

    public final MeasurementOutput updateCarouselDimensions(int i, MediaHost.MediaHostStateHolder mediaHostStateHolder) {
        MeasurementOutput measurementOutput;
        MeasurementOutput measurementOutput2 = new MeasurementOutput();
        for (MediaViewController mediaViewController : this.controllers) {
            Objects.requireNonNull(mediaViewController);
            TransitionViewState obtainViewState = mediaViewController.obtainViewState(mediaHostStateHolder);
            if (obtainViewState == null) {
                measurementOutput = null;
            } else {
                MeasurementOutput measurementOutput3 = mediaViewController.measurement;
                int i2 = obtainViewState.width;
                Objects.requireNonNull(measurementOutput3);
                measurementOutput3.measuredWidth = i2;
                MeasurementOutput measurementOutput4 = mediaViewController.measurement;
                int i3 = obtainViewState.height;
                Objects.requireNonNull(measurementOutput4);
                measurementOutput4.measuredHeight = i3;
                measurementOutput = mediaViewController.measurement;
            }
            if (measurementOutput != null) {
                int i4 = measurementOutput.measuredHeight;
                if (i4 > measurementOutput2.measuredHeight) {
                    measurementOutput2.measuredHeight = i4;
                }
                int i5 = measurementOutput.measuredWidth;
                if (i5 > measurementOutput2.measuredWidth) {
                    measurementOutput2.measuredWidth = i5;
                }
            }
        }
        this.carouselSizes.put(Integer.valueOf(i), measurementOutput2);
        return measurementOutput2;
    }
}
