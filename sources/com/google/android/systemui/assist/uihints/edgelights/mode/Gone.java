package com.google.android.systemui.assist.uihints.edgelights.mode;

import android.metrics.LogMaker;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.assist.ui.EdgeLight;
import com.android.systemui.assist.ui.PerimeterPathGuide;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
/* loaded from: classes.dex */
public final class Gone implements EdgeLightsView.Mode {
    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final int getSubType() {
        return 0;
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final void onNewModeRequest(EdgeLightsView edgeLightsView, EdgeLightsView.Mode mode) {
        edgeLightsView.setVisibility(0);
        edgeLightsView.commitModeTransition(mode);
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final void start(EdgeLightsView edgeLightsView, PerimeterPathGuide perimeterPathGuide, EdgeLightsView.Mode mode) {
        edgeLightsView.setAssistLights(new EdgeLight[0]);
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final void logState() {
        MetricsLogger.action(new LogMaker(1716).setType(2));
    }
}
