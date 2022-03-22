package com.android.wm.shell.pip.tv;

import android.view.animation.PathInterpolator;
/* loaded from: classes.dex */
public final class TvPipInterpolators {
    public static final PathInterpolator ENTER = new PathInterpolator(0.12f, 1.0f, 0.4f, 1.0f);
    public static final PathInterpolator EXIT = new PathInterpolator(0.4f, 1.0f, 0.12f, 1.0f);

    static {
        new PathInterpolator(0.2f, 0.1f, 0.0f, 1.0f);
        new PathInterpolator(0.18f, 1.0f, 0.22f, 1.0f);
    }
}
