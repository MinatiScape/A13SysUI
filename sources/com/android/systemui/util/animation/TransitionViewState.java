package com.android.systemui.util.animation;

import android.graphics.PointF;
import java.util.LinkedHashMap;
import java.util.Map;
/* compiled from: TransitionLayout.kt */
/* loaded from: classes.dex */
public final class TransitionViewState {
    public int height;
    public int width;
    public LinkedHashMap widgetStates = new LinkedHashMap();
    public float alpha = 1.0f;
    public final PointF translation = new PointF();
    public final PointF contentTranslation = new PointF();

    public final TransitionViewState copy(TransitionViewState transitionViewState) {
        if (transitionViewState == null) {
            transitionViewState = new TransitionViewState();
        }
        transitionViewState.width = this.width;
        transitionViewState.height = this.height;
        transitionViewState.alpha = this.alpha;
        PointF pointF = transitionViewState.translation;
        PointF pointF2 = this.translation;
        pointF.set(pointF2.x, pointF2.y);
        PointF pointF3 = transitionViewState.contentTranslation;
        PointF pointF4 = this.contentTranslation;
        pointF3.set(pointF4.x, pointF4.y);
        for (Map.Entry entry : this.widgetStates.entrySet()) {
            LinkedHashMap linkedHashMap = transitionViewState.widgetStates;
            Object key = entry.getKey();
            WidgetState widgetState = (WidgetState) entry.getValue();
            linkedHashMap.put(key, new WidgetState(widgetState.x, widgetState.y, widgetState.width, widgetState.height, widgetState.measureWidth, widgetState.measureHeight, widgetState.alpha, widgetState.scale, widgetState.gone));
        }
        return transitionViewState;
    }
}
