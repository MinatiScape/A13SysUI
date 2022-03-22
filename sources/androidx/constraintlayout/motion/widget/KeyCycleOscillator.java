package androidx.constraintlayout.motion.widget;

import android.annotation.TargetApi;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.motion.utils.CurveFit;
import androidx.constraintlayout.motion.utils.Oscillator;
import androidx.constraintlayout.widget.ConstraintAttribute;
import com.android.systemui.plugins.FalsingManager;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class KeyCycleOscillator {
    public ConstraintAttribute mCustom;
    public CycleOscillator mCycleOscillator;
    public String mType;
    public int mWaveShape = 0;
    public int mVariesBy = 0;
    public ArrayList<WavePoint> mWavePoints = new ArrayList<>();

    /* loaded from: classes.dex */
    public static class CustomSet extends KeyCycleOscillator {
        public float[] value = new float[1];

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public final void setProperty(View view, float f) {
            this.value[0] = get(f);
            this.mCustom.setInterpolatedValue(view, this.value);
        }
    }

    /* loaded from: classes.dex */
    public static class PathRotateSet extends KeyCycleOscillator {
        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public final void setProperty(View view, float f) {
        }
    }

    public abstract void setProperty(View view, float f);

    /* loaded from: classes.dex */
    public static class AlphaSet extends KeyCycleOscillator {
        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public final void setProperty(View view, float f) {
            view.setAlpha(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class CycleOscillator {
        public CurveFit mCurveFit;
        public float[] mOffset;
        public Oscillator mOscillator = new Oscillator();
        public float[] mPeriod;
        public double[] mPosition;
        public double[] mSplineSlopeCache;
        public double[] mSplineValueCache;
        public float[] mValues;

        public CycleOscillator(int i, int i2) {
            new HashMap();
            Oscillator oscillator = this.mOscillator;
            Objects.requireNonNull(oscillator);
            oscillator.mType = i;
            this.mValues = new float[i2];
            this.mPosition = new double[i2];
            this.mPeriod = new float[i2];
            this.mOffset = new float[i2];
            float[] fArr = new float[i2];
        }
    }

    /* loaded from: classes.dex */
    public static class ElevationSet extends KeyCycleOscillator {
        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public final void setProperty(View view, float f) {
            view.setElevation(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class ProgressSet extends KeyCycleOscillator {
        public boolean mNoMethod = false;

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public final void setProperty(View view, float f) {
            if (view instanceof MotionLayout) {
                ((MotionLayout) view).setProgress(get(f));
            } else if (!this.mNoMethod) {
                Method method = null;
                try {
                    method = view.getClass().getMethod("setProgress", Float.TYPE);
                } catch (NoSuchMethodException unused) {
                    this.mNoMethod = true;
                }
                if (method != null) {
                    try {
                        method.invoke(view, Float.valueOf(get(f)));
                    } catch (IllegalAccessException e) {
                        Log.e("KeyCycleOscillator", "unable to setProgress", e);
                    } catch (InvocationTargetException e2) {
                        Log.e("KeyCycleOscillator", "unable to setProgress", e2);
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class RotationSet extends KeyCycleOscillator {
        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public final void setProperty(View view, float f) {
            view.setRotation(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class RotationXset extends KeyCycleOscillator {
        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public final void setProperty(View view, float f) {
            view.setRotationX(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class RotationYset extends KeyCycleOscillator {
        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public final void setProperty(View view, float f) {
            view.setRotationY(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class ScaleXset extends KeyCycleOscillator {
        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public final void setProperty(View view, float f) {
            view.setScaleX(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class ScaleYset extends KeyCycleOscillator {
        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public final void setProperty(View view, float f) {
            view.setScaleY(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class TranslationXset extends KeyCycleOscillator {
        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public final void setProperty(View view, float f) {
            view.setTranslationX(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class TranslationYset extends KeyCycleOscillator {
        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public final void setProperty(View view, float f) {
            view.setTranslationY(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class TranslationZset extends KeyCycleOscillator {
        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public final void setProperty(View view, float f) {
            view.setTranslationZ(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class WavePoint {
        public float mOffset;
        public float mPeriod;
        public int mPosition;
        public float mValue;

        public WavePoint(int i, float f, float f2, float f3) {
            this.mPosition = i;
            this.mValue = f3;
            this.mOffset = f2;
            this.mPeriod = f;
        }
    }

    public final float get(float f) {
        CycleOscillator cycleOscillator = this.mCycleOscillator;
        Objects.requireNonNull(cycleOscillator);
        CurveFit curveFit = cycleOscillator.mCurveFit;
        if (curveFit != null) {
            curveFit.getPos(f, cycleOscillator.mSplineValueCache);
        } else {
            double[] dArr = cycleOscillator.mSplineValueCache;
            dArr[0] = cycleOscillator.mOffset[0];
            dArr[1] = cycleOscillator.mValues[0];
        }
        return (float) ((cycleOscillator.mOscillator.getValue(f) * cycleOscillator.mSplineValueCache[1]) + cycleOscillator.mSplineValueCache[0]);
    }

    public final float getSlope(float f) {
        double d;
        double d2;
        double d3;
        CycleOscillator cycleOscillator = this.mCycleOscillator;
        Objects.requireNonNull(cycleOscillator);
        CurveFit curveFit = cycleOscillator.mCurveFit;
        double d4 = 0.0d;
        if (curveFit != null) {
            double d5 = f;
            curveFit.getSlope(d5, cycleOscillator.mSplineSlopeCache);
            cycleOscillator.mCurveFit.getPos(d5, cycleOscillator.mSplineValueCache);
        } else {
            double[] dArr = cycleOscillator.mSplineSlopeCache;
            dArr[0] = 0.0d;
            dArr[1] = 0.0d;
        }
        double d6 = f;
        double value = cycleOscillator.mOscillator.getValue(d6);
        Oscillator oscillator = cycleOscillator.mOscillator;
        Objects.requireNonNull(oscillator);
        double d7 = 2.0d;
        switch (oscillator.mType) {
            case 1:
                double[] dArr2 = cycleOscillator.mSplineSlopeCache;
                return (float) ((d4 * cycleOscillator.mSplineValueCache[1]) + (value * dArr2[1]) + dArr2[0]);
            case 2:
                d2 = oscillator.getDP(d6) * 4.0d;
                d = Math.signum((((oscillator.getP(d6) * 4.0d) + 3.0d) % 4.0d) - 2.0d);
                d4 = d2 * d;
                double[] dArr22 = cycleOscillator.mSplineSlopeCache;
                return (float) ((d4 * cycleOscillator.mSplineValueCache[1]) + (value * dArr22[1]) + dArr22[0]);
            case 3:
                d3 = oscillator.getDP(d6);
                d4 = d3 * d7;
                double[] dArr222 = cycleOscillator.mSplineSlopeCache;
                return (float) ((d4 * cycleOscillator.mSplineValueCache[1]) + (value * dArr222[1]) + dArr222[0]);
            case 4:
                d3 = -oscillator.getDP(d6);
                d4 = d3 * d7;
                double[] dArr2222 = cycleOscillator.mSplineSlopeCache;
                return (float) ((d4 * cycleOscillator.mSplineValueCache[1]) + (value * dArr2222[1]) + dArr2222[0]);
            case 5:
                d7 = oscillator.getDP(d6) * (-6.283185307179586d);
                d3 = Math.sin(oscillator.getP(d6) * 6.283185307179586d);
                d4 = d3 * d7;
                double[] dArr22222 = cycleOscillator.mSplineSlopeCache;
                return (float) ((d4 * cycleOscillator.mSplineValueCache[1]) + (value * dArr22222[1]) + dArr22222[0]);
            case FalsingManager.VERSION /* 6 */:
                d2 = oscillator.getDP(d6) * 4.0d;
                d = (((oscillator.getP(d6) * 4.0d) + 2.0d) % 4.0d) - 2.0d;
                d4 = d2 * d;
                double[] dArr222222 = cycleOscillator.mSplineSlopeCache;
                return (float) ((d4 * cycleOscillator.mSplineValueCache[1]) + (value * dArr222222[1]) + dArr222222[0]);
            default:
                d2 = oscillator.getDP(d6) * 6.283185307179586d;
                d = Math.cos(oscillator.getP(d6) * 6.283185307179586d);
                d4 = d2 * d;
                double[] dArr2222222 = cycleOscillator.mSplineSlopeCache;
                return (float) ((d4 * cycleOscillator.mSplineValueCache[1]) + (value * dArr2222222[1]) + dArr2222222[0]);
        }
    }

    @TargetApi(19)
    public final void setup() {
        float[] fArr;
        float[] fArr2;
        int i;
        float[] fArr3;
        int size = this.mWavePoints.size();
        if (size != 0) {
            Collections.sort(this.mWavePoints, new Comparator<WavePoint>() { // from class: androidx.constraintlayout.motion.widget.KeyCycleOscillator.1
                @Override // java.util.Comparator
                public final int compare(WavePoint wavePoint, WavePoint wavePoint2) {
                    return Integer.compare(wavePoint.mPosition, wavePoint2.mPosition);
                }
            });
            double[] dArr = new double[size];
            double[][] dArr2 = (double[][]) Array.newInstance(double.class, size, 2);
            this.mCycleOscillator = new CycleOscillator(this.mWaveShape, size);
            Iterator<WavePoint> it = this.mWavePoints.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                WavePoint next = it.next();
                float f = next.mPeriod;
                dArr[i2] = f * 0.01d;
                double[] dArr3 = dArr2[i2];
                float f2 = next.mValue;
                dArr3[0] = f2;
                double[] dArr4 = dArr2[i2];
                float f3 = next.mOffset;
                dArr4[1] = f3;
                CycleOscillator cycleOscillator = this.mCycleOscillator;
                int i3 = next.mPosition;
                Objects.requireNonNull(cycleOscillator);
                cycleOscillator.mPosition[i2] = i3 / 100.0d;
                cycleOscillator.mPeriod[i2] = f;
                cycleOscillator.mOffset[i2] = f3;
                cycleOscillator.mValues[i2] = f2;
                i2++;
                dArr2 = dArr2;
            }
            CycleOscillator cycleOscillator2 = this.mCycleOscillator;
            Objects.requireNonNull(cycleOscillator2);
            double[][] dArr5 = (double[][]) Array.newInstance(double.class, cycleOscillator2.mPosition.length, 2);
            float[] fArr4 = cycleOscillator2.mValues;
            cycleOscillator2.mSplineValueCache = new double[fArr4.length + 1];
            cycleOscillator2.mSplineSlopeCache = new double[fArr4.length + 1];
            if (cycleOscillator2.mPosition[0] > 0.0d) {
                cycleOscillator2.mOscillator.addPoint(0.0d, cycleOscillator2.mPeriod[0]);
            }
            double[] dArr6 = cycleOscillator2.mPosition;
            int length = dArr6.length - 1;
            if (dArr6[length] < 1.0d) {
                cycleOscillator2.mOscillator.addPoint(1.0d, cycleOscillator2.mPeriod[length]);
            }
            for (int i4 = 0; i4 < dArr5.length; i4++) {
                dArr5[i4][0] = cycleOscillator2.mOffset[i4];
                int i5 = 0;
                while (true) {
                    if (i5 < cycleOscillator2.mValues.length) {
                        dArr5[i5][1] = fArr3[i5];
                        i5++;
                    }
                }
                cycleOscillator2.mOscillator.addPoint(cycleOscillator2.mPosition[i4], cycleOscillator2.mPeriod[i4]);
            }
            Oscillator oscillator = cycleOscillator2.mOscillator;
            Objects.requireNonNull(oscillator);
            int i6 = 0;
            double d = 0.0d;
            while (true) {
                if (i6 >= oscillator.mPeriod.length) {
                    break;
                }
                d += fArr[i6];
                i6++;
            }
            int i7 = 1;
            double d2 = 0.0d;
            while (true) {
                float[] fArr5 = oscillator.mPeriod;
                if (i7 >= fArr5.length) {
                    break;
                }
                int i8 = i7 - 1;
                float f4 = (fArr5[i8] + fArr5[i7]) / 2.0f;
                double[] dArr7 = oscillator.mPosition;
                d2 = ((dArr7[i7] - dArr7[i8]) * f4) + d2;
                i7++;
            }
            int i9 = 0;
            while (true) {
                float[] fArr6 = oscillator.mPeriod;
                if (i9 >= fArr6.length) {
                    break;
                }
                fArr6[i9] = (float) (fArr6[i9] * (d / d2));
                i9++;
            }
            oscillator.mArea[0] = 0.0d;
            int i10 = 1;
            while (true) {
                if (i10 >= oscillator.mPeriod.length) {
                    break;
                }
                int i11 = i10 - 1;
                double[] dArr8 = oscillator.mPosition;
                double d3 = dArr8[i10] - dArr8[i11];
                double[] dArr9 = oscillator.mArea;
                dArr9[i10] = (d3 * ((fArr2[i11] + fArr2[i10]) / 2.0f)) + dArr9[i11];
                i10++;
            }
            double[] dArr10 = cycleOscillator2.mPosition;
            if (dArr10.length > 1) {
                i = 0;
                cycleOscillator2.mCurveFit = CurveFit.get(0, dArr10, dArr5);
            } else {
                i = 0;
                cycleOscillator2.mCurveFit = null;
            }
            CurveFit.get(i, dArr, dArr2);
        }
    }

    public final String toString() {
        String str = this.mType;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        Iterator<WavePoint> it = this.mWavePoints.iterator();
        while (it.hasNext()) {
            WavePoint next = it.next();
            StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m(str, "[");
            m.append(next.mPosition);
            m.append(" , ");
            m.append(decimalFormat.format(next.mValue));
            m.append("] ");
            str = m.toString();
        }
        return str;
    }
}
