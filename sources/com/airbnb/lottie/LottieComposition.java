package com.airbnb.lottie;

import android.graphics.Rect;
import androidx.collection.LongSparseArray;
import androidx.collection.SparseArrayCompat;
import com.airbnb.lottie.model.Font;
import com.airbnb.lottie.model.FontCharacter;
import com.airbnb.lottie.model.Marker;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.utils.Logger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public final class LottieComposition {
    public Rect bounds;
    public SparseArrayCompat<FontCharacter> characters;
    public float endFrame;
    public Map<String, Font> fonts;
    public float frameRate;
    public boolean hasDashPattern;
    public Map<String, LottieImageAsset> images;
    public LongSparseArray<Layer> layerMap;
    public List<Layer> layers;
    public List<Marker> markers;
    public Map<String, List<Layer>> precomps;
    public float startFrame;
    public final PerformanceTracker performanceTracker = new PerformanceTracker();
    public final HashSet<String> warnings = new HashSet<>();
    public int maskAndMatteCount = 0;

    public final float getDuration() {
        return ((this.endFrame - this.startFrame) / this.frameRate) * 1000.0f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x003d, code lost:
        if (r3.substring(0, r3.length() - 1).equalsIgnoreCase(r7) != false) goto L_0x0041;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.airbnb.lottie.model.Marker getMarker(java.lang.String r7) {
        /*
            r6 = this;
            java.util.List<com.airbnb.lottie.model.Marker> r0 = r6.markers
            r0.size()
            r0 = 0
            r1 = r0
        L_0x0007:
            java.util.List<com.airbnb.lottie.model.Marker> r2 = r6.markers
            int r2 = r2.size()
            if (r1 >= r2) goto L_0x0047
            java.util.List<com.airbnb.lottie.model.Marker> r2 = r6.markers
            java.lang.Object r2 = r2.get(r1)
            com.airbnb.lottie.model.Marker r2 = (com.airbnb.lottie.model.Marker) r2
            java.util.Objects.requireNonNull(r2)
            java.lang.String r3 = r2.name
            boolean r3 = r3.equalsIgnoreCase(r7)
            r4 = 1
            if (r3 == 0) goto L_0x0024
            goto L_0x0041
        L_0x0024:
            java.lang.String r3 = r2.name
            java.lang.String r5 = "\r"
            boolean r3 = r3.endsWith(r5)
            if (r3 == 0) goto L_0x0040
            java.lang.String r3 = r2.name
            int r5 = r3.length()
            int r5 = r5 - r4
            java.lang.String r3 = r3.substring(r0, r5)
            boolean r3 = r3.equalsIgnoreCase(r7)
            if (r3 == 0) goto L_0x0040
            goto L_0x0041
        L_0x0040:
            r4 = r0
        L_0x0041:
            if (r4 == 0) goto L_0x0044
            return r2
        L_0x0044:
            int r1 = r1 + 1
            goto L_0x0007
        L_0x0047:
            r6 = 0
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.LottieComposition.getMarker(java.lang.String):com.airbnb.lottie.model.Marker");
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder("LottieComposition:\n");
        for (Layer layer : this.layers) {
            sb.append(layer.toString("\t"));
        }
        return sb.toString();
    }

    public final void addWarning(String str) {
        Logger.warning(str);
        this.warnings.add(str);
    }
}
