package com.android.systemui.animation;

import android.graphics.Path;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
/* loaded from: classes.dex */
public final class Interpolators {
    public static final PathInterpolator EMPHASIZED;
    public static final PathInterpolator FAST_OUT_LINEAR_IN;
    public static final PathInterpolator FAST_OUT_SLOW_IN;
    public static final PathInterpolator LEGACY;
    public static final PathInterpolator LEGACY_ACCELERATE;
    public static final PathInterpolator LEGACY_DECELERATE;
    public static final PathInterpolator LINEAR_OUT_SLOW_IN;
    public static final PathInterpolator EMPHASIZED_DECELERATE = new PathInterpolator(0.05f, 0.7f, 0.1f, 1.0f);
    public static final PathInterpolator STANDARD = new PathInterpolator(0.2f, 0.0f, 0.0f, 1.0f);
    public static final PathInterpolator STANDARD_ACCELERATE = new PathInterpolator(0.3f, 0.0f, 1.0f, 1.0f);
    public static final PathInterpolator STANDARD_DECELERATE = new PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f);
    public static final LinearInterpolator LINEAR = new LinearInterpolator();
    public static final PathInterpolator FAST_OUT_SLOW_IN_REVERSE = new PathInterpolator(0.8f, 0.0f, 0.6f, 1.0f);
    public static final PathInterpolator ALPHA_IN = new PathInterpolator(0.4f, 0.0f, 1.0f, 1.0f);
    public static final PathInterpolator ALPHA_OUT = new PathInterpolator(0.0f, 0.0f, 0.8f, 1.0f);
    public static final AccelerateInterpolator ACCELERATE = new AccelerateInterpolator();
    public static final AccelerateDecelerateInterpolator ACCELERATE_DECELERATE = new AccelerateDecelerateInterpolator();
    public static final DecelerateInterpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
    public static final PathInterpolator CUSTOM_40_40 = new PathInterpolator(0.4f, 0.0f, 0.6f, 1.0f);
    public static final PathInterpolator ICON_OVERSHOT = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.4f);
    public static final PathInterpolator ICON_OVERSHOT_LESS = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.1f);
    public static final PathInterpolator PANEL_CLOSE_ACCELERATED = new PathInterpolator(0.3f, 0.0f, 0.5f, 1.0f);
    public static final PathInterpolator CONTROL_STATE = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f);
    public static final PathInterpolator TOUCH_RESPONSE = new PathInterpolator(0.3f, 0.0f, 0.1f, 1.0f);
    public static final PathInterpolator TOUCH_RESPONSE_REVERSE = new PathInterpolator(0.9f, 0.0f, 0.7f, 1.0f);

    static {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(0.05f, 0.0f, 0.133333f, 0.06f, 0.166666f, 0.4f);
        path.cubicTo(0.208333f, 0.82f, 0.25f, 1.0f, 1.0f, 1.0f);
        EMPHASIZED = new PathInterpolator(path);
        new PathInterpolator(0.3f, 0.0f, 0.8f, 0.15f);
        PathInterpolator pathInterpolator = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f);
        LEGACY = pathInterpolator;
        PathInterpolator pathInterpolator2 = new PathInterpolator(0.4f, 0.0f, 1.0f, 1.0f);
        LEGACY_ACCELERATE = pathInterpolator2;
        PathInterpolator pathInterpolator3 = new PathInterpolator(0.0f, 0.0f, 0.2f, 1.0f);
        LEGACY_DECELERATE = pathInterpolator3;
        FAST_OUT_SLOW_IN = pathInterpolator;
        FAST_OUT_LINEAR_IN = pathInterpolator2;
        LINEAR_OUT_SLOW_IN = pathInterpolator3;
        new PathInterpolator(0.8f, 0.0f, 1.0f, 1.0f);
        new BounceInterpolator();
    }
}
