package com.airbnb.lottie.parser;

import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import androidx.collection.SparseArrayCompat;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;
import com.android.systemui.plugins.FalsingManager;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyframeParser {
    public static final LinearInterpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    public static JsonReader.Options NAMES = JsonReader.Options.of("t", "s", "e", "o", "i", "h", "to", "ti");
    public static SparseArrayCompat<WeakReference<Interpolator>> pathInterpolatorCache;

    public static <T> Keyframe<T> parse(JsonReader jsonReader, LottieComposition lottieComposition, float f, ValueParser<T> valueParser, boolean z) throws IOException {
        T t;
        Interpolator interpolator;
        Interpolator interpolator2;
        int i;
        WeakReference weakReference;
        if (!z) {
            return new Keyframe<>(valueParser.parse(jsonReader, f));
        }
        jsonReader.beginObject();
        float f2 = 0.0f;
        boolean z2 = false;
        PointF pointF = null;
        PointF pointF2 = null;
        T t2 = null;
        T t3 = null;
        PointF pointF3 = null;
        PointF pointF4 = null;
        while (jsonReader.hasNext()) {
            switch (jsonReader.selectName(NAMES)) {
                case 0:
                    f2 = (float) jsonReader.nextDouble();
                    break;
                case 1:
                    t3 = valueParser.parse(jsonReader, f);
                    break;
                case 2:
                    t2 = valueParser.parse(jsonReader, f);
                    break;
                case 3:
                    pointF = JsonUtils.jsonToPoint(jsonReader, f);
                    break;
                case 4:
                    pointF2 = JsonUtils.jsonToPoint(jsonReader, f);
                    break;
                case 5:
                    if (jsonReader.nextInt() != 1) {
                        z2 = false;
                        break;
                    } else {
                        z2 = true;
                        break;
                    }
                case FalsingManager.VERSION /* 6 */:
                    pointF4 = JsonUtils.jsonToPoint(jsonReader, f);
                    break;
                case 7:
                    pointF3 = JsonUtils.jsonToPoint(jsonReader, f);
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();
        if (z2) {
            interpolator = LINEAR_INTERPOLATOR;
            t = t3;
        } else {
            if (pointF == null || pointF2 == null) {
                interpolator2 = LINEAR_INTERPOLATOR;
            } else {
                float f3 = -f;
                pointF.x = MiscUtils.clamp(pointF.x, f3, f);
                pointF.y = MiscUtils.clamp(pointF.y, -100.0f, 100.0f);
                pointF2.x = MiscUtils.clamp(pointF2.x, f3, f);
                float clamp = MiscUtils.clamp(pointF2.y, -100.0f, 100.0f);
                pointF2.y = clamp;
                float f4 = pointF.x;
                float f5 = pointF.y;
                float f6 = pointF2.x;
                PathMeasure pathMeasure = Utils.pathMeasure;
                if (f4 != 0.0f) {
                    i = (int) (527 * f4);
                } else {
                    i = 17;
                }
                if (f5 != 0.0f) {
                    i = (int) (i * 31 * f5);
                }
                if (f6 != 0.0f) {
                    i = (int) (i * 31 * f6);
                }
                if (clamp != 0.0f) {
                    i = (int) (i * 31 * clamp);
                }
                synchronized (KeyframeParser.class) {
                    if (pathInterpolatorCache == null) {
                        pathInterpolatorCache = new SparseArrayCompat<>();
                    }
                    SparseArrayCompat<WeakReference<Interpolator>> sparseArrayCompat = pathInterpolatorCache;
                    Objects.requireNonNull(sparseArrayCompat);
                    weakReference = (WeakReference) sparseArrayCompat.get(i, null);
                }
                if (weakReference != null) {
                    interpolator2 = (Interpolator) weakReference.get();
                } else {
                    interpolator2 = null;
                }
                if (weakReference == null || interpolator2 == null) {
                    pointF.x /= f;
                    pointF.y /= f;
                    float f7 = pointF2.x / f;
                    pointF2.x = f7;
                    float f8 = pointF2.y / f;
                    pointF2.y = f8;
                    try {
                        interpolator2 = new PathInterpolator(pointF.x, pointF.y, f7, f8);
                    } catch (IllegalArgumentException e) {
                        if (e.getMessage().equals("The Path cannot loop back on itself.")) {
                            interpolator2 = new PathInterpolator(Math.min(pointF.x, 1.0f), pointF.y, Math.max(pointF2.x, 0.0f), pointF2.y);
                        } else {
                            interpolator2 = new LinearInterpolator();
                        }
                    }
                    try {
                        WeakReference<Interpolator> weakReference2 = new WeakReference<>(interpolator2);
                        synchronized (KeyframeParser.class) {
                            pathInterpolatorCache.put(i, weakReference2);
                        }
                    } catch (ArrayIndexOutOfBoundsException unused) {
                    }
                }
            }
            interpolator = interpolator2;
            t = t2;
        }
        Keyframe<T> keyframe = new Keyframe<>(lottieComposition, t3, t, interpolator, f2, null);
        keyframe.pathCp1 = pointF4;
        keyframe.pathCp2 = pointF3;
        return keyframe;
    }
}
