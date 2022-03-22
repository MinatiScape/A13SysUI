package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes.dex */
public final class SuggestParcelables$OnScreenRect {
    public float height;
    public float left;
    public float top;
    public float width;

    public SuggestParcelables$OnScreenRect(Bundle bundle) {
        if (bundle.containsKey("left")) {
            this.left = bundle.getFloat("left");
        }
        if (bundle.containsKey("top")) {
            this.top = bundle.getFloat("top");
        }
        if (bundle.containsKey("width")) {
            this.width = bundle.getFloat("width");
        }
        if (bundle.containsKey("height")) {
            this.height = bundle.getFloat("height");
        }
    }
}
