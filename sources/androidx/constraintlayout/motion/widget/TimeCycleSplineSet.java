package androidx.constraintlayout.motion.widget;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.motion.utils.CurveFit;
import androidx.constraintlayout.widget.ConstraintAttribute;
import com.android.systemui.plugins.FalsingManager;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class TimeCycleSplineSet {
    public int count;
    public long last_time;
    public CurveFit mCurveFit;
    public String mType;
    public int mWaveShape = 0;
    public int[] mTimePoints = new int[10];
    public float[][] mValues = (float[][]) Array.newInstance(float.class, 10, 3);
    public float[] mCache = new float[3];
    public boolean mContinue = false;
    public float last_cycle = Float.NaN;

    /* loaded from: classes.dex */
    public static class AlphaSet extends TimeCycleSplineSet {
        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setAlpha(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    /* loaded from: classes.dex */
    public static class CustomSet extends TimeCycleSplineSet {
        public float[] mCache;
        public SparseArray<ConstraintAttribute> mConstraintAttributeList;
        public float[] mTempValues;
        public SparseArray<float[]> mWaveProperties = new SparseArray<>();

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final void setPoint(int i, float f, float f2, int i2, float f3) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute,...)");
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            boolean z;
            this.mCurveFit.getPos(f, this.mTempValues);
            float[] fArr = this.mTempValues;
            float f2 = fArr[fArr.length - 2];
            float f3 = fArr[fArr.length - 1];
            float f4 = (float) (((((j - this.last_time) * 1.0E-9d) * f2) + this.last_cycle) % 1.0d);
            this.last_cycle = f4;
            this.last_time = j;
            float calcWave = calcWave(f4);
            this.mContinue = false;
            int i = 0;
            while (true) {
                float[] fArr2 = this.mCache;
                if (i >= fArr2.length) {
                    break;
                }
                boolean z2 = this.mContinue;
                float[] fArr3 = this.mTempValues;
                if (fArr3[i] != 0.0d) {
                    z = true;
                } else {
                    z = false;
                }
                this.mContinue = z2 | z;
                fArr2[i] = (fArr3[i] * calcWave) + f3;
                i++;
            }
            this.mConstraintAttributeList.valueAt(0).setInterpolatedValue(view, this.mCache);
            if (f2 != 0.0f) {
                this.mContinue = true;
            }
            return this.mContinue;
        }

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final void setup(int i) {
            float[] fArr;
            int size = this.mConstraintAttributeList.size();
            int noOfInterpValues = this.mConstraintAttributeList.valueAt(0).noOfInterpValues();
            double[] dArr = new double[size];
            int i2 = noOfInterpValues + 2;
            this.mTempValues = new float[i2];
            this.mCache = new float[noOfInterpValues];
            double[][] dArr2 = (double[][]) Array.newInstance(double.class, size, i2);
            for (int i3 = 0; i3 < size; i3++) {
                int keyAt = this.mConstraintAttributeList.keyAt(i3);
                float[] valueAt = this.mWaveProperties.valueAt(i3);
                dArr[i3] = keyAt * 0.01d;
                this.mConstraintAttributeList.valueAt(i3).getValuesToInterpolate(this.mTempValues);
                int i4 = 0;
                while (true) {
                    if (i4 < this.mTempValues.length) {
                        dArr2[i3][i4] = fArr[i4];
                        i4++;
                    }
                }
                dArr2[i3][noOfInterpValues] = valueAt[0];
                dArr2[i3][noOfInterpValues + 1] = valueAt[1];
            }
            this.mCurveFit = CurveFit.get(i, dArr, dArr2);
        }

        public CustomSet(String str, SparseArray<ConstraintAttribute> sparseArray) {
            String str2 = str.split(",")[1];
            this.mConstraintAttributeList = sparseArray;
        }
    }

    /* loaded from: classes.dex */
    public static class ElevationSet extends TimeCycleSplineSet {
        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setElevation(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    /* loaded from: classes.dex */
    public static class ProgressSet extends TimeCycleSplineSet {
        public boolean mNoMethod = false;

        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            if (view instanceof MotionLayout) {
                ((MotionLayout) view).setProgress(get(f, j, view, keyCache));
            } else if (this.mNoMethod) {
                return false;
            } else {
                Method method = null;
                try {
                    method = view.getClass().getMethod("setProgress", Float.TYPE);
                } catch (NoSuchMethodException unused) {
                    this.mNoMethod = true;
                }
                if (method != null) {
                    try {
                        method.invoke(view, Float.valueOf(get(f, j, view, keyCache)));
                    } catch (IllegalAccessException e) {
                        Log.e("SplineSet", "unable to setProgress", e);
                    } catch (InvocationTargetException e2) {
                        Log.e("SplineSet", "unable to setProgress", e2);
                    }
                }
            }
            return this.mContinue;
        }
    }

    /* loaded from: classes.dex */
    public static class RotationSet extends TimeCycleSplineSet {
        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setRotation(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    /* loaded from: classes.dex */
    public static class RotationXset extends TimeCycleSplineSet {
        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setRotationX(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    /* loaded from: classes.dex */
    public static class RotationYset extends TimeCycleSplineSet {
        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setRotationY(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    /* loaded from: classes.dex */
    public static class ScaleXset extends TimeCycleSplineSet {
        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setScaleX(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    /* loaded from: classes.dex */
    public static class ScaleYset extends TimeCycleSplineSet {
        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setScaleY(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    /* loaded from: classes.dex */
    public static class TranslationXset extends TimeCycleSplineSet {
        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setTranslationX(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    /* loaded from: classes.dex */
    public static class TranslationYset extends TimeCycleSplineSet {
        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setTranslationY(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    /* loaded from: classes.dex */
    public static class TranslationZset extends TimeCycleSplineSet {
        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            view.setTranslationZ(get(f, j, view, keyCache));
            return this.mContinue;
        }
    }

    public abstract boolean setProperty(View view, float f, long j, KeyCache keyCache);

    public final float calcWave(float f) {
        switch (this.mWaveShape) {
            case 1:
                return Math.signum(f * 6.2831855f);
            case 2:
                return 1.0f - Math.abs(f);
            case 3:
                return (((f * 2.0f) + 1.0f) % 2.0f) - 1.0f;
            case 4:
                return 1.0f - (((f * 2.0f) + 1.0f) % 2.0f);
            case 5:
                return (float) Math.cos(f * 6.2831855f);
            case FalsingManager.VERSION /* 6 */:
                float abs = 1.0f - Math.abs(((f * 4.0f) % 4.0f) - 2.0f);
                return 1.0f - (abs * abs);
            default:
                return (float) Math.sin(f * 6.2831855f);
        }
    }

    public final float get(float f, long j, View view, KeyCache keyCache) {
        this.mCurveFit.getPos(f, this.mCache);
        float[] fArr = this.mCache;
        boolean z = true;
        float f2 = fArr[1];
        int i = (f2 > 0.0f ? 1 : (f2 == 0.0f ? 0 : -1));
        if (i == 0) {
            this.mContinue = false;
            return fArr[2];
        }
        if (Float.isNaN(this.last_cycle)) {
            String str = this.mType;
            Objects.requireNonNull(keyCache);
            float f3 = Float.NaN;
            if (keyCache.map.containsKey(view)) {
                HashMap<String, float[]> hashMap = keyCache.map.get(view);
                if (hashMap.containsKey(str)) {
                    float[] fArr2 = hashMap.get(str);
                    if (fArr2.length > 0) {
                        f3 = fArr2[0];
                    }
                }
            }
            this.last_cycle = f3;
            if (Float.isNaN(f3)) {
                this.last_cycle = 0.0f;
            }
        }
        float f4 = (float) (((((j - this.last_time) * 1.0E-9d) * f2) + this.last_cycle) % 1.0d);
        this.last_cycle = f4;
        String str2 = this.mType;
        Objects.requireNonNull(keyCache);
        if (!keyCache.map.containsKey(view)) {
            HashMap<String, float[]> hashMap2 = new HashMap<>();
            hashMap2.put(str2, new float[]{f4});
            keyCache.map.put(view, hashMap2);
        } else {
            HashMap<String, float[]> hashMap3 = keyCache.map.get(view);
            if (!hashMap3.containsKey(str2)) {
                hashMap3.put(str2, new float[]{f4});
                keyCache.map.put(view, hashMap3);
            } else {
                float[] fArr3 = hashMap3.get(str2);
                if (fArr3.length <= 0) {
                    fArr3 = Arrays.copyOf(fArr3, 1);
                }
                fArr3[0] = f4;
                hashMap3.put(str2, fArr3);
            }
        }
        this.last_time = j;
        float f5 = this.mCache[0];
        float calcWave = (calcWave(this.last_cycle) * f5) + this.mCache[2];
        if (f5 == 0.0f && i == 0) {
            z = false;
        }
        this.mContinue = z;
        return calcWave;
    }

    public void setPoint(int i, float f, float f2, int i2, float f3) {
        int[] iArr = this.mTimePoints;
        int i3 = this.count;
        iArr[i3] = i;
        float[][] fArr = this.mValues;
        fArr[i3][0] = f;
        fArr[i3][1] = f2;
        fArr[i3][2] = f3;
        this.mWaveShape = Math.max(this.mWaveShape, i2);
        this.count++;
    }

    public void setup(int i) {
        int i2 = this.count;
        if (i2 == 0) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Error no points added to ");
            m.append(this.mType);
            Log.e("SplineSet", m.toString());
            return;
        }
        int[] iArr = this.mTimePoints;
        float[][] fArr = this.mValues;
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
                        float[] fArr2 = fArr[i9];
                        fArr[i9] = fArr[i8];
                        fArr[i8] = fArr2;
                        i9++;
                    }
                    i8++;
                }
                int i11 = iArr[i9];
                iArr[i9] = iArr[i6];
                iArr[i6] = i11;
                float[] fArr3 = fArr[i9];
                fArr[i9] = fArr[i6];
                fArr[i6] = fArr3;
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
        int i16 = 0;
        while (true) {
            int[] iArr3 = this.mTimePoints;
            if (i15 >= iArr3.length) {
                break;
            }
            if (iArr3[i15] != iArr3[i15 - 1]) {
                i16++;
            }
            i15++;
        }
        double[] dArr = new double[i16];
        double[][] dArr2 = (double[][]) Array.newInstance(double.class, i16, 3);
        int i17 = 0;
        for (int i18 = 0; i18 < this.count; i18++) {
            if (i18 > 0) {
                int[] iArr4 = this.mTimePoints;
                if (iArr4[i18] == iArr4[i18 - 1]) {
                }
            }
            dArr[i17] = this.mTimePoints[i18] * 0.01d;
            double[] dArr3 = dArr2[i17];
            float[][] fArr4 = this.mValues;
            dArr3[0] = fArr4[i18][0];
            dArr2[i17][1] = fArr4[i18][1];
            dArr2[i17][2] = fArr4[i18][2];
            i17++;
        }
        this.mCurveFit = CurveFit.get(i, dArr, dArr2);
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

    /* loaded from: classes.dex */
    public static class PathRotate extends TimeCycleSplineSet {
        @Override // androidx.constraintlayout.motion.widget.TimeCycleSplineSet
        public final boolean setProperty(View view, float f, long j, KeyCache keyCache) {
            return this.mContinue;
        }
    }
}
