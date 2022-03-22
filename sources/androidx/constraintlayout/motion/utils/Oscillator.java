package androidx.constraintlayout.motion.utils;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.plugins.FalsingManager;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class Oscillator {
    public double[] mArea;
    public float[] mPeriod = new float[0];
    public double[] mPosition = new double[0];
    public int mType;

    public final void addPoint(double d, float f) {
        int length = this.mPeriod.length + 1;
        int binarySearch = Arrays.binarySearch(this.mPosition, d);
        if (binarySearch < 0) {
            binarySearch = (-binarySearch) - 1;
        }
        this.mPosition = Arrays.copyOf(this.mPosition, length);
        this.mPeriod = Arrays.copyOf(this.mPeriod, length);
        this.mArea = new double[length];
        double[] dArr = this.mPosition;
        System.arraycopy(dArr, binarySearch, dArr, binarySearch + 1, (length - binarySearch) - 1);
        this.mPosition[binarySearch] = d;
        this.mPeriod[binarySearch] = f;
    }

    public final double getDP(double d) {
        if (d <= 0.0d) {
            d = 1.0E-5d;
        } else if (d >= 1.0d) {
            d = 0.999999d;
        }
        int binarySearch = Arrays.binarySearch(this.mPosition, d);
        if (binarySearch > 0 || binarySearch == 0) {
            return 0.0d;
        }
        int i = (-binarySearch) - 1;
        float[] fArr = this.mPeriod;
        int i2 = i - 1;
        double[] dArr = this.mPosition;
        double d2 = (fArr[i] - fArr[i2]) / (dArr[i] - dArr[i2]);
        return (fArr[i2] - (d2 * dArr[i2])) + (d * d2);
    }

    public final double getP(double d) {
        if (d < 0.0d) {
            d = 0.0d;
        } else if (d > 1.0d) {
            d = 1.0d;
        }
        int binarySearch = Arrays.binarySearch(this.mPosition, d);
        if (binarySearch > 0) {
            return 1.0d;
        }
        if (binarySearch == 0) {
            return 0.0d;
        }
        int i = (-binarySearch) - 1;
        float[] fArr = this.mPeriod;
        int i2 = i - 1;
        double d2 = fArr[i] - fArr[i2];
        double[] dArr = this.mPosition;
        double d3 = d2 / (dArr[i] - dArr[i2]);
        return ((((d * d) - (dArr[i2] * dArr[i2])) * d3) / 2.0d) + ((d - dArr[i2]) * (fArr[i2] - (dArr[i2] * d3))) + this.mArea[i2];
    }

    public final double getValue(double d) {
        double d2;
        switch (this.mType) {
            case 1:
                return Math.signum(0.5d - (getP(d) % 1.0d));
            case 2:
                d2 = Math.abs((((getP(d) * 4.0d) + 1.0d) % 4.0d) - 2.0d);
                break;
            case 3:
                return (((getP(d) * 2.0d) + 1.0d) % 2.0d) - 1.0d;
            case 4:
                d2 = ((getP(d) * 2.0d) + 1.0d) % 2.0d;
                break;
            case 5:
                return Math.cos(getP(d) * 6.283185307179586d);
            case FalsingManager.VERSION /* 6 */:
                double abs = 1.0d - Math.abs(((getP(d) * 4.0d) % 4.0d) - 2.0d);
                d2 = abs * abs;
                break;
            default:
                return Math.sin(getP(d) * 6.283185307179586d);
        }
        return 1.0d - d2;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("pos =");
        m.append(Arrays.toString(this.mPosition));
        m.append(" period=");
        m.append(Arrays.toString(this.mPeriod));
        return m.toString();
    }
}
