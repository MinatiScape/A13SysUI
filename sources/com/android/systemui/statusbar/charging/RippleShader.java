package com.android.systemui.statusbar.charging;

import android.graphics.PointF;
import android.graphics.RuntimeShader;
import android.util.MathUtils;
/* compiled from: RippleShader.kt */
/* loaded from: classes.dex */
public final class RippleShader extends RuntimeShader {
    public static final Companion Companion = new Companion();
    public float progress;
    public float radius;
    public int color = 16777215;
    public boolean shouldFadeOutRipple = true;

    /* compiled from: RippleShader.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public static final float access$subProgress(float f, float f2, float f3) {
            Companion companion = RippleShader.Companion;
            float min = Math.min(f, f2);
            return (Math.min(Math.max(f3, min), Math.max(f, f2)) - f) / (f2 - f);
        }
    }

    public final void setColor(int i) {
        this.color = i;
        setColorUniform("in_color", i);
    }

    public final void setProgress(float f) {
        float f2;
        this.progress = f;
        setFloatUniform("in_progress", f);
        float f3 = 1;
        float f4 = f3 - f;
        setFloatUniform("in_radius", (f3 - ((f4 * f4) * f4)) * this.radius);
        setFloatUniform("in_blur", MathUtils.lerp(1.25f, 0.5f, f));
        float f5 = 0.0f;
        float access$subProgress = Companion.access$subProgress(0.0f, 0.1f, f);
        float access$subProgress2 = Companion.access$subProgress(0.4f, 1.0f, f);
        if (this.shouldFadeOutRipple) {
            f5 = Companion.access$subProgress(0.0f, 0.2f, f);
            f2 = Companion.access$subProgress(0.3f, 1.0f, f);
        } else {
            f2 = 0.0f;
        }
        setFloatUniform("in_fadeSparkle", Math.min(access$subProgress, f3 - access$subProgress2));
        setFloatUniform("in_fadeCircle", f3 - f5);
        setFloatUniform("in_fadeRing", Math.min(access$subProgress, f3 - f2));
    }

    public RippleShader() {
        super("uniform vec2 in_origin;\n                uniform float in_progress;\n                uniform float in_maxRadius;\n                uniform float in_time;\n                uniform float in_distort_radial;\n                uniform float in_distort_xy;\n                uniform float in_radius;\n                uniform float in_fadeSparkle;\n                uniform float in_fadeCircle;\n                uniform float in_fadeRing;\n                uniform float in_blur;\n                uniform float in_pixelDensity;\n                layout(color) uniform vec4 in_color;\n                uniform float in_sparkle_strength;float triangleNoise(vec2 n) {\n                    n  = fract(n * vec2(5.3987, 5.4421));\n                    n += dot(n.yx, n.xy + vec2(21.5351, 14.3137));\n                    float xy = n.x * n.y;\n                    return fract(xy * 95.4307) + fract(xy * 75.04961) - 1.0;\n                }\n                const float PI = 3.1415926535897932384626;\n\n                float threshold(float v, float l, float h) {\n                  return step(l, v) * (1.0 - step(h, v));\n                }\n\n                float sparkles(vec2 uv, float t) {\n                  float n = triangleNoise(uv);\n                  float s = 0.0;\n                  for (float i = 0; i < 4; i += 1) {\n                    float l = i * 0.01;\n                    float h = l + 0.1;\n                    float o = smoothstep(n - l, h, n);\n                    o *= abs(sin(PI * o * (t + 0.55 * i)));\n                    s += o;\n                  }\n                  return s;\n                }\n\n                float softCircle(vec2 uv, vec2 xy, float radius, float blur) {\n                  float blurHalf = blur * 0.5;\n                  float d = distance(uv, xy);\n                  return 1. - smoothstep(1. - blurHalf, 1. + blurHalf, d / radius);\n                }\n\n                float softRing(vec2 uv, vec2 xy, float radius, float blur) {\n                  float thickness_half = radius * 0.25;\n                  float circle_outer = softCircle(uv, xy, radius + thickness_half, blur);\n                  float circle_inner = softCircle(uv, xy, radius - thickness_half, blur);\n                  return circle_outer - circle_inner;\n                }\n\n                vec2 distort(vec2 p, vec2 origin, float time,\n                    float distort_amount_radial, float distort_amount_xy) {\n                    float2 distance = origin - p;\n                    float angle = atan(distance.y, distance.x);\n                    return p + vec2(sin(angle * 8 + time * 0.003 + 1.641),\n                                    cos(angle * 5 + 2.14 + time * 0.00412)) * distort_amount_radial\n                             + vec2(sin(p.x * 0.01 + time * 0.00215 + 0.8123),\n                                    cos(p.y * 0.01 + time * 0.005931)) * distort_amount_xy;\n                }vec4 main(vec2 p) {\n                    vec2 p_distorted = distort(p, in_origin, in_time, in_distort_radial,\n                        in_distort_xy);\n\n                    // Draw shapes\n                    float sparkleRing = softRing(p_distorted, in_origin, in_radius, in_blur);\n                    float sparkle = sparkles(p - mod(p, in_pixelDensity * 0.8), in_time * 0.00175)\n                        * sparkleRing * in_fadeSparkle;\n                    float circle = softCircle(p_distorted, in_origin, in_radius * 1.2, in_blur);\n                    float rippleAlpha = max(circle * in_fadeCircle,\n                        softRing(p_distorted, in_origin, in_radius, in_blur) * in_fadeRing) * 0.45;\n                    vec4 ripple = in_color * rippleAlpha;\n                    return mix(ripple, vec4(sparkle), sparkle * in_sparkle_strength);\n                }");
        new PointF();
    }
}
