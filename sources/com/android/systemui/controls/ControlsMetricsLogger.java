package com.android.systemui.controls;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.controls.ui.ControlViewHolder;
/* compiled from: ControlsMetricsLogger.kt */
/* loaded from: classes.dex */
public interface ControlsMetricsLogger {
    void drag(ControlViewHolder controlViewHolder, boolean z);

    void longPress(ControlViewHolder controlViewHolder, boolean z);

    void refreshBegin(int i, boolean z);

    void refreshEnd(ControlViewHolder controlViewHolder, boolean z);

    void touch(ControlViewHolder controlViewHolder, boolean z);

    /* compiled from: ControlsMetricsLogger.kt */
    /* loaded from: classes.dex */
    public enum ControlsEvents implements UiEventLogger.UiEventEnum {
        CONTROL_TOUCH(714),
        CONTROL_DRAG(713),
        CONTROL_LONG_PRESS(715),
        CONTROL_REFRESH_BEGIN(716),
        CONTROL_REFRESH_END(717);
        
        private final int metricId;

        ControlsEvents(int i) {
            this.metricId = i;
        }

        public final int getId() {
            return this.metricId;
        }
    }
}
