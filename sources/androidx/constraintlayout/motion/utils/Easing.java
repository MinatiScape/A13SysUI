package androidx.constraintlayout.motion.utils;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import java.util.Arrays;
/* loaded from: classes.dex */
public class Easing {
    public String str = "identity";
    public static Easing sDefault = new Easing();
    public static String[] NAMED_EASING = {"standard", "accelerate", "decelerate", "linear"};

    /* loaded from: classes.dex */
    public static class CubicEasing extends Easing {
        public double x1;
        public double x2;
        public double y1;
        public double y2;

        @Override // androidx.constraintlayout.motion.utils.Easing
        public final double get(double d) {
            if (d <= 0.0d) {
                return 0.0d;
            }
            if (d >= 1.0d) {
                return 1.0d;
            }
            double d2 = 0.5d;
            double d3 = 0.5d;
            while (d2 > 0.01d) {
                d2 *= 0.5d;
                if (getX(d3) < d) {
                    d3 += d2;
                } else {
                    d3 -= d2;
                }
            }
            double d4 = d3 - d2;
            double x = getX(d4);
            double d5 = d3 + d2;
            double x2 = getX(d5);
            double y = getY(d4);
            return (((d - x) * (getY(d5) - y)) / (x2 - x)) + y;
        }

        @Override // androidx.constraintlayout.motion.utils.Easing
        public final double getDiff(double d) {
            double d2 = 0.5d;
            double d3 = 0.5d;
            while (d2 > 1.0E-4d) {
                d2 *= 0.5d;
                if (getX(d3) < d) {
                    d3 += d2;
                } else {
                    d3 -= d2;
                }
            }
            double d4 = d3 - d2;
            double x = getX(d4);
            double d5 = d3 + d2;
            return (getY(d5) - getY(d4)) / (getX(d5) - x);
        }

        public final double getX(double d) {
            double d2 = 1.0d - d;
            double d3 = 3.0d * d2;
            double d4 = d2 * d3 * d;
            double d5 = d3 * d * d;
            return (this.x2 * d5) + (this.x1 * d4) + (d * d * d);
        }

        public final double getY(double d) {
            double d2 = 1.0d - d;
            double d3 = 3.0d * d2;
            double d4 = d2 * d3 * d;
            double d5 = d3 * d * d;
            return (this.y2 * d5) + (this.y1 * d4) + (d * d * d);
        }

        public CubicEasing(String str) {
            this.str = str;
            int indexOf = str.indexOf(40);
            int indexOf2 = str.indexOf(44, indexOf);
            this.x1 = Double.parseDouble(str.substring(indexOf + 1, indexOf2).trim());
            int i = indexOf2 + 1;
            int indexOf3 = str.indexOf(44, i);
            this.y1 = Double.parseDouble(str.substring(i, indexOf3).trim());
            int i2 = indexOf3 + 1;
            int indexOf4 = str.indexOf(44, i2);
            this.x2 = Double.parseDouble(str.substring(i2, indexOf4).trim());
            int i3 = indexOf4 + 1;
            this.y2 = Double.parseDouble(str.substring(i3, str.indexOf(41, i3)).trim());
        }
    }

    public double get(double d) {
        return d;
    }

    public double getDiff(double d) {
        return 1.0d;
    }

    public static Easing getInterpolator(String str) {
        if (str == null) {
            return null;
        }
        if (str.startsWith("cubic")) {
            return new CubicEasing(str);
        }
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1354466595:
                if (str.equals("accelerate")) {
                    c = 0;
                    break;
                }
                break;
            case -1263948740:
                if (str.equals("decelerate")) {
                    c = 1;
                    break;
                }
                break;
            case -1102672091:
                if (str.equals("linear")) {
                    c = 2;
                    break;
                }
                break;
            case 1312628413:
                if (str.equals("standard")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return new CubicEasing("cubic(0.4, 0.05, 0.8, 0.7)");
            case 1:
                return new CubicEasing("cubic(0.0, 0.0, 0.2, 0.95)");
            case 2:
                return new CubicEasing("cubic(1, 1, 0, 0)");
            case 3:
                return new CubicEasing("cubic(0.4, 0.0, 0.2, 1)");
            default:
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("transitionEasing syntax error syntax:transitionEasing=\"cubic(1.0,0.5,0.0,0.6)\" or ");
                m.append(Arrays.toString(NAMED_EASING));
                Log.e("ConstraintSet", m.toString());
                return sDefault;
        }
    }

    public final String toString() {
        return this.str;
    }
}
