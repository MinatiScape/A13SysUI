package com.airbnb.lottie.utils;

import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import com.airbnb.lottie.L;
import java.io.Closeable;
/* loaded from: classes.dex */
public final class Utils {
    public static final PathMeasure pathMeasure = new PathMeasure();
    public static final Path tempPath = new Path();
    public static final Path tempPath2 = new Path();
    public static final float[] points = new float[4];
    public static final float INV_SQRT_2 = (float) (Math.sqrt(2.0d) / 2.0d);
    public static float dpScale = -1.0f;

    public static void applyTrimPathIfNeeded(Path path, float f, float f2, float f3) {
        PathMeasure pathMeasure2 = pathMeasure;
        pathMeasure2.setPath(path, false);
        float length = pathMeasure2.getLength();
        if (f == 1.0f && f2 == 0.0f) {
            L.endSection();
        } else if (length < 1.0f || Math.abs((f2 - f) - 1.0f) < 0.01d) {
            L.endSection();
        } else {
            float f4 = f * length;
            float f5 = f2 * length;
            float f6 = f3 * length;
            float min = Math.min(f4, f5) + f6;
            float max = Math.max(f4, f5) + f6;
            if (min >= length && max >= length) {
                min = MiscUtils.floorMod(min, length);
                max = MiscUtils.floorMod(max, length);
            }
            if (min < 0.0f) {
                min = MiscUtils.floorMod(min, length);
            }
            if (max < 0.0f) {
                max = MiscUtils.floorMod(max, length);
            }
            int i = (min > max ? 1 : (min == max ? 0 : -1));
            if (i == 0) {
                path.reset();
                L.endSection();
                return;
            }
            if (i >= 0) {
                min -= length;
            }
            Path path2 = tempPath;
            path2.reset();
            pathMeasure2.getSegment(min, max, path2, true);
            if (max > length) {
                Path path3 = tempPath2;
                path3.reset();
                pathMeasure2.getSegment(0.0f, max % length, path3, true);
                path2.addPath(path3);
            } else if (min < 0.0f) {
                Path path4 = tempPath2;
                path4.reset();
                pathMeasure2.getSegment(min + length, length, path4, true);
                path2.addPath(path4);
            }
            path.set(path2);
            L.endSection();
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }

    public static float dpScale() {
        if (dpScale == -1.0f) {
            dpScale = Resources.getSystem().getDisplayMetrics().density;
        }
        return dpScale;
    }

    public static float getScale(Matrix matrix) {
        float[] fArr = points;
        fArr[0] = 0.0f;
        fArr[1] = 0.0f;
        float f = INV_SQRT_2;
        fArr[2] = f;
        fArr[3] = f;
        matrix.mapPoints(fArr);
        return (float) Math.hypot(fArr[2] - fArr[0], fArr[3] - fArr[1]);
    }
}
