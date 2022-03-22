package androidx.constraintlayout.motion.widget;

import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.motion.utils.CurveFit;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
/* loaded from: classes.dex */
public abstract class SplineSet {
    public int count;
    public CurveFit mCurveFit;
    public String mType;
    public int[] mTimePoints = new int[10];
    public float[] mValues = new float[10];

    /* loaded from: classes.dex */
    public static class CustomSet extends SplineSet {
        public SparseArray<ConstraintAttribute> mConstraintAttributeList;
        public float[] mTempValues;

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setPoint(int i, float f) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute)");
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setProperty(View view, float f) {
            this.mCurveFit.getPos(f, this.mTempValues);
            this.mConstraintAttributeList.valueAt(0).setInterpolatedValue(view, this.mTempValues);
        }

        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setup(int i) {
            float[] fArr;
            int size = this.mConstraintAttributeList.size();
            int noOfInterpValues = this.mConstraintAttributeList.valueAt(0).noOfInterpValues();
            double[] dArr = new double[size];
            this.mTempValues = new float[noOfInterpValues];
            double[][] dArr2 = (double[][]) Array.newInstance(double.class, size, noOfInterpValues);
            for (int i2 = 0; i2 < size; i2++) {
                dArr[i2] = this.mConstraintAttributeList.keyAt(i2) * 0.01d;
                this.mConstraintAttributeList.valueAt(i2).getValuesToInterpolate(this.mTempValues);
                int i3 = 0;
                while (true) {
                    if (i3 < this.mTempValues.length) {
                        dArr2[i2][i3] = fArr[i3];
                        i3++;
                    }
                }
            }
            this.mCurveFit = CurveFit.get(i, dArr, dArr2);
        }

        public CustomSet(String str, SparseArray<ConstraintAttribute> sparseArray) {
            String str2 = str.split(",")[1];
            this.mConstraintAttributeList = sparseArray;
        }
    }

    /* loaded from: classes.dex */
    public static class PathRotate extends SplineSet {
        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setProperty(View view, float f) {
        }
    }

    public abstract void setProperty(View view, float f);

    /* loaded from: classes.dex */
    public static class AlphaSet extends SplineSet {
        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setProperty(View view, float f) {
            view.setAlpha(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class ElevationSet extends SplineSet {
        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setProperty(View view, float f) {
            view.setElevation(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class ProgressSet extends SplineSet {
        public boolean mNoMethod = false;

        @Override // androidx.constraintlayout.motion.widget.SplineSet
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
                        Log.e("SplineSet", "unable to setProgress", e);
                    } catch (InvocationTargetException e2) {
                        Log.e("SplineSet", "unable to setProgress", e2);
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class RotationSet extends SplineSet {
        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setProperty(View view, float f) {
            view.setRotation(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class RotationXset extends SplineSet {
        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setProperty(View view, float f) {
            view.setRotationX(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class RotationYset extends SplineSet {
        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setProperty(View view, float f) {
            view.setRotationY(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class ScaleXset extends SplineSet {
        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setProperty(View view, float f) {
            view.setScaleX(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class ScaleYset extends SplineSet {
        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setProperty(View view, float f) {
            view.setScaleY(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class TranslationXset extends SplineSet {
        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setProperty(View view, float f) {
            view.setTranslationX(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class TranslationYset extends SplineSet {
        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setProperty(View view, float f) {
            view.setTranslationY(get(f));
        }
    }

    /* loaded from: classes.dex */
    public static class TranslationZset extends SplineSet {
        @Override // androidx.constraintlayout.motion.widget.SplineSet
        public final void setProperty(View view, float f) {
            view.setTranslationZ(get(f));
        }
    }

    public final float get(float f) {
        return (float) this.mCurveFit.getPos(f);
    }

    public void setPoint(int i, float f) {
        int[] iArr = this.mTimePoints;
        if (iArr.length < this.count + 1) {
            this.mTimePoints = Arrays.copyOf(iArr, iArr.length * 2);
            float[] fArr = this.mValues;
            this.mValues = Arrays.copyOf(fArr, fArr.length * 2);
        }
        int[] iArr2 = this.mTimePoints;
        int i2 = this.count;
        iArr2[i2] = i;
        this.mValues[i2] = f;
        this.count = i2 + 1;
    }

    public void setup(int i) {
        int i2 = this.count;
        if (i2 != 0) {
            int[] iArr = this.mTimePoints;
            float[] fArr = this.mValues;
            int[] iArr2 = new int[iArr.length + 10];
            iArr2[0] = i2 - 1;
            iArr2[1] = 0;
            int i3 = 2;
            while (i3 > 0) {
                int i4 = i3 - 1;
                int i5 = iArr2[i4];
                i3 = i4 - 1;
                int i6 = iArr2[i3];
                if (i5 < i6) {
                    int i7 = iArr[i6];
                    int i8 = i5;
                    int i9 = i8;
                    while (i8 < i6) {
                        if (iArr[i8] <= i7) {
                            int i10 = iArr[i9];
                            iArr[i9] = iArr[i8];
                            iArr[i8] = i10;
                            float f = fArr[i9];
                            fArr[i9] = fArr[i8];
                            fArr[i8] = f;
                            i9++;
                        }
                        i8++;
                    }
                    int i11 = iArr[i9];
                    iArr[i9] = iArr[i6];
                    iArr[i6] = i11;
                    float f2 = fArr[i9];
                    fArr[i9] = fArr[i6];
                    fArr[i6] = f2;
                    int i12 = i3 + 1;
                    iArr2[i3] = i9 - 1;
                    int i13 = i12 + 1;
                    iArr2[i12] = i5;
                    int i14 = i13 + 1;
                    iArr2[i13] = i6;
                    i3 = i14 + 1;
                    iArr2[i14] = i9 + 1;
                }
            }
            int i15 = 1;
            for (int i16 = 1; i16 < this.count; i16++) {
                int[] iArr3 = this.mTimePoints;
                if (iArr3[i16 - 1] != iArr3[i16]) {
                    i15++;
                }
            }
            double[] dArr = new double[i15];
            double[][] dArr2 = (double[][]) Array.newInstance(double.class, i15, 1);
            int i17 = 0;
            for (int i18 = 0; i18 < this.count; i18++) {
                if (i18 > 0) {
                    int[] iArr4 = this.mTimePoints;
                    if (iArr4[i18] == iArr4[i18 - 1]) {
                    }
                }
                dArr[i17] = this.mTimePoints[i18] * 0.01d;
                dArr2[i17][0] = this.mValues[i18];
                i17++;
            }
            this.mCurveFit = CurveFit.get(i, dArr, dArr2);
        }
    }

    public final String toString() {
        String str = this.mType;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        for (int i = 0; i < this.count; i++) {
            StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m(str, "[");
            m.append(this.mTimePoints[i]);
            m.append(" , ");
            m.append(decimalFormat.format(this.mValues[i]));
            m.append("] ");
            str = m.toString();
        }
        return str;
    }
}
