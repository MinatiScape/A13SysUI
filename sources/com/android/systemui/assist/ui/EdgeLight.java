package com.android.systemui.assist.ui;

import android.util.Log;
import java.util.Objects;
/* loaded from: classes.dex */
public final class EdgeLight {
    public int mColor;
    public float mLength;
    public float mStart;

    public EdgeLight(int i) {
        this.mColor = i;
        this.mStart = 0.0f;
        this.mLength = 0.0f;
    }

    public static EdgeLight[] copy(EdgeLight[] edgeLightArr) {
        EdgeLight[] edgeLightArr2 = new EdgeLight[edgeLightArr.length];
        for (int i = 0; i < edgeLightArr.length; i++) {
            edgeLightArr2[i] = new EdgeLight(edgeLightArr[i]);
        }
        return edgeLightArr2;
    }

    public final void setEndpoints(float f, float f2) {
        if (f > f2) {
            Log.e("EdgeLight", String.format("Endpoint must be >= start (add 1 if necessary). Got [%f, %f]", Float.valueOf(f), Float.valueOf(f2)));
            return;
        }
        this.mStart = f;
        this.mLength = f2 - f;
    }

    public EdgeLight(EdgeLight edgeLight) {
        Objects.requireNonNull(edgeLight);
        this.mColor = edgeLight.mColor;
        this.mStart = edgeLight.mStart;
        this.mLength = edgeLight.mLength;
    }
}
