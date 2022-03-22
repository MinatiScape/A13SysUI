package androidx.core.graphics;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Color;
/* loaded from: classes.dex */
public final class ColorUtils {
    public static final ThreadLocal<double[]> TEMP_ARRAY = new ThreadLocal<>();

    public static void RGBToHSL(int i, int i2, int i3, float[] fArr) {
        float f;
        float f2;
        float f3 = i / 255.0f;
        float f4 = i2 / 255.0f;
        float f5 = i3 / 255.0f;
        float max = Math.max(f3, Math.max(f4, f5));
        float min = Math.min(f3, Math.min(f4, f5));
        float f6 = max - min;
        float f7 = (max + min) / 2.0f;
        if (max == min) {
            f = 0.0f;
            f2 = 0.0f;
        } else {
            if (max == f3) {
                f = ((f4 - f5) / f6) % 6.0f;
            } else if (max == f4) {
                f = ((f5 - f3) / f6) + 2.0f;
            } else {
                f = 4.0f + ((f3 - f4) / f6);
            }
            f2 = f6 / (1.0f - Math.abs((2.0f * f7) - 1.0f));
        }
        float f8 = 360.0f;
        float f9 = (f * 60.0f) % 360.0f;
        if (f9 < 0.0f) {
            f9 += 360.0f;
        }
        if (f9 < 0.0f) {
            f8 = 0.0f;
        } else if (f9 <= 360.0f) {
            f8 = f9;
        }
        fArr[0] = f8;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        } else if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        fArr[1] = f2;
        if (f7 < 0.0f) {
            f7 = 0.0f;
        } else if (f7 > 1.0f) {
            f7 = 1.0f;
        }
        fArr[2] = f7;
    }

    public static int blendARGB(int i, int i2, float f) {
        float f2 = 1.0f - f;
        return Color.argb((int) ((Color.alpha(i2) * f) + (Color.alpha(i) * f2)), (int) ((Color.red(i2) * f) + (Color.red(i) * f2)), (int) ((Color.green(i2) * f) + (Color.green(i) * f2)), (int) ((Color.blue(i2) * f) + (Color.blue(i) * f2)));
    }

    public static double calculateLuminance(int i) {
        double d;
        double d2;
        double d3;
        ThreadLocal<double[]> threadLocal = TEMP_ARRAY;
        double[] dArr = threadLocal.get();
        if (dArr == null) {
            dArr = new double[3];
            threadLocal.set(dArr);
        }
        int red = Color.red(i);
        int green = Color.green(i);
        int blue = Color.blue(i);
        if (dArr.length == 3) {
            double d4 = red / 255.0d;
            if (d4 < 0.04045d) {
                d = d4 / 12.92d;
            } else {
                d = Math.pow((d4 + 0.055d) / 1.055d, 2.4d);
            }
            double d5 = green / 255.0d;
            if (d5 < 0.04045d) {
                d2 = d5 / 12.92d;
            } else {
                d2 = Math.pow((d5 + 0.055d) / 1.055d, 2.4d);
            }
            double d6 = blue / 255.0d;
            if (d6 < 0.04045d) {
                d3 = d6 / 12.92d;
            } else {
                d3 = Math.pow((d6 + 0.055d) / 1.055d, 2.4d);
            }
            dArr[0] = ((0.1805d * d3) + (0.3576d * d2) + (0.4124d * d)) * 100.0d;
            dArr[1] = ((0.0722d * d3) + (0.7152d * d2) + (0.2126d * d)) * 100.0d;
            double d7 = d3 * 0.9505d;
            dArr[2] = (d7 + (d2 * 0.1192d) + (d * 0.0193d)) * 100.0d;
            return dArr[1] / 100.0d;
        }
        throw new IllegalArgumentException("outXyz must have a length of 3.");
    }

    public static float circularInterpolate(float f, float f2, float f3) {
        if (Math.abs(f2 - f) > 180.0f) {
            if (f2 > f) {
                f += 360.0f;
            } else {
                f2 += 360.0f;
            }
        }
        return (((f2 - f) * f3) + f) % 360.0f;
    }

    public static int setAlphaComponent(int i, int i2) {
        if (i2 >= 0 && i2 <= 255) {
            return (i & 16777215) | (i2 << 24);
        }
        throw new IllegalArgumentException("alpha must be between 0 and 255.");
    }

    public static double calculateContrast(int i, int i2) {
        if (Color.alpha(i2) == 255) {
            if (Color.alpha(i) < 255) {
                i = compositeColors(i, i2);
            }
            double calculateLuminance = calculateLuminance(i) + 0.05d;
            double calculateLuminance2 = calculateLuminance(i2) + 0.05d;
            return Math.max(calculateLuminance, calculateLuminance2) / Math.min(calculateLuminance, calculateLuminance2);
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("background can not be translucent: #");
        m.append(Integer.toHexString(i2));
        throw new IllegalArgumentException(m.toString());
    }

    public static int calculateMinimumAlpha(int i, int i2, float f) {
        int i3 = 255;
        if (Color.alpha(i2) == 255) {
            double d = f;
            if (calculateContrast(setAlphaComponent(i, 255), i2) < d) {
                return -1;
            }
            int i4 = 0;
            for (int i5 = 0; i5 <= 10 && i3 - i4 > 1; i5++) {
                int i6 = (i4 + i3) / 2;
                if (calculateContrast(setAlphaComponent(i, i6), i2) < d) {
                    i4 = i6;
                } else {
                    i3 = i6;
                }
            }
            return i3;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("background can not be translucent: #");
        m.append(Integer.toHexString(i2));
        throw new IllegalArgumentException(m.toString());
    }

    public static int compositeColors(int i, int i2) {
        int i3;
        int i4;
        int alpha = Color.alpha(i2);
        int alpha2 = Color.alpha(i);
        int i5 = 255 - alpha2;
        int i6 = 255 - (((255 - alpha) * i5) / 255);
        int red = Color.red(i);
        int red2 = Color.red(i2);
        int i7 = 0;
        if (i6 == 0) {
            i3 = 0;
        } else {
            i3 = (((red2 * alpha) * i5) + ((red * 255) * alpha2)) / (i6 * 255);
        }
        int green = Color.green(i);
        int green2 = Color.green(i2);
        if (i6 == 0) {
            i4 = 0;
        } else {
            i4 = (((green2 * alpha) * i5) + ((green * 255) * alpha2)) / (i6 * 255);
        }
        int blue = Color.blue(i);
        int blue2 = Color.blue(i2);
        if (i6 != 0) {
            i7 = (((blue2 * alpha) * i5) + ((blue * 255) * alpha2)) / (i6 * 255);
        }
        return Color.argb(i6, i3, i4, i7);
    }

    public static int XYZToColor(double d, double d2, double d3) {
        double d4;
        double d5;
        double d6;
        double d7 = (((-0.4986d) * d3) + (((-1.5372d) * d2) + (3.2406d * d))) / 100.0d;
        double d8 = ((0.0415d * d3) + ((1.8758d * d2) + ((-0.9689d) * d))) / 100.0d;
        double d9 = ((1.057d * d3) + (((-0.204d) * d2) + (0.0557d * d))) / 100.0d;
        if (d7 > 0.0031308d) {
            d4 = (Math.pow(d7, 0.4166666666666667d) * 1.055d) - 0.055d;
        } else {
            d4 = d7 * 12.92d;
        }
        if (d8 > 0.0031308d) {
            d5 = (Math.pow(d8, 0.4166666666666667d) * 1.055d) - 0.055d;
        } else {
            d5 = d8 * 12.92d;
        }
        if (d9 > 0.0031308d) {
            d6 = (Math.pow(d9, 0.4166666666666667d) * 1.055d) - 0.055d;
        } else {
            d6 = d9 * 12.92d;
        }
        int round = (int) Math.round(d4 * 255.0d);
        int i = 0;
        if (round < 0) {
            round = 0;
        } else if (round > 255) {
            round = 255;
        }
        int round2 = (int) Math.round(d5 * 255.0d);
        if (round2 < 0) {
            round2 = 0;
        } else if (round2 > 255) {
            round2 = 255;
        }
        int round3 = (int) Math.round(d6 * 255.0d);
        if (round3 >= 0) {
            if (round3 > 255) {
                i = 255;
            } else {
                i = round3;
            }
        }
        return Color.rgb(round, round2, i);
    }
}
