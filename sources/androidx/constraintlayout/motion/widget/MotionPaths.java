package androidx.constraintlayout.motion.widget;

import android.view.View;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintSet;
import java.util.LinkedHashMap;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MotionPaths implements Comparable<MotionPaths> {
    public static String[] names = {"position", "x", "y", "width", "height", "pathRotate"};
    public LinkedHashMap<String, ConstraintAttribute> attributes;
    public float height;
    public int mDrawPath;
    public Easing mKeyFrameEasing;
    public int mLastMeasuredHeight;
    public int mLastMeasuredWidth;
    public int mMode;
    public int mPathMotionArc;
    public float mPathRotate;
    public double[] mTempDelta;
    public double[] mTempValue;
    public float position;
    public float time;
    public float width;
    public float x;
    public float y;

    public MotionPaths() {
        this.mDrawPath = 0;
        this.mPathRotate = Float.NaN;
        this.mPathMotionArc = -1;
        this.mLastMeasuredWidth = 0;
        this.mLastMeasuredHeight = 0;
        this.attributes = new LinkedHashMap<>();
        this.mMode = 0;
        this.mTempValue = new double[18];
        this.mTempDelta = new double[18];
    }

    public static void setDpDt(float f, float f2, float[] fArr, int[] iArr, double[] dArr, double[] dArr2) {
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        for (int i = 0; i < iArr.length; i++) {
            float f7 = (float) dArr[i];
            double d = dArr2[i];
            int i2 = iArr[i];
            if (i2 == 1) {
                f4 = f7;
            } else if (i2 == 2) {
                f6 = f7;
            } else if (i2 == 3) {
                f3 = f7;
            } else if (i2 == 4) {
                f5 = f7;
            }
        }
        float f8 = f4 - ((0.0f * f3) / 2.0f);
        float f9 = f6 - ((0.0f * f5) / 2.0f);
        fArr[0] = (((f3 * 1.0f) + f8) * f) + ((1.0f - f) * f8) + 0.0f;
        fArr[1] = (((f5 * 1.0f) + f9) * f2) + ((1.0f - f2) * f9) + 0.0f;
    }

    public final void applyParameters(ConstraintSet.Constraint constraint) {
        this.mKeyFrameEasing = Easing.getInterpolator(constraint.motion.mTransitionEasing);
        ConstraintSet.Motion motion = constraint.motion;
        this.mPathMotionArc = motion.mPathMotionArc;
        this.mPathRotate = motion.mPathRotate;
        this.mDrawPath = motion.mDrawPath;
        float f = constraint.propertySet.mProgress;
        for (String str : constraint.mCustomConstraints.keySet()) {
            ConstraintAttribute constraintAttribute = constraint.mCustomConstraints.get(str);
            Objects.requireNonNull(constraintAttribute);
            if (constraintAttribute.mType != ConstraintAttribute.AttributeType.STRING_TYPE) {
                this.attributes.put(str, constraintAttribute);
            }
        }
    }

    @Override // java.lang.Comparable
    public final int compareTo(MotionPaths motionPaths) {
        return Float.compare(this.position, motionPaths.position);
    }

    public final void getCenter(int[] iArr, double[] dArr, float[] fArr, int i) {
        float f = this.x;
        float f2 = this.y;
        float f3 = this.width;
        float f4 = this.height;
        for (int i2 = 0; i2 < iArr.length; i2++) {
            float f5 = (float) dArr[i2];
            int i3 = iArr[i2];
            if (i3 == 1) {
                f = f5;
            } else if (i3 == 2) {
                f2 = f5;
            } else if (i3 == 3) {
                f3 = f5;
            } else if (i3 == 4) {
                f4 = f5;
            }
        }
        fArr[i] = (f3 / 2.0f) + f + 0.0f;
        fArr[i + 1] = (f4 / 2.0f) + f2 + 0.0f;
    }

    public final void setBounds(float f, float f2, float f3, float f4) {
        this.x = f;
        this.y = f2;
        this.width = f3;
        this.height = f4;
        this.mLastMeasuredWidth = View.MeasureSpec.makeMeasureSpec((int) f3, 1073741824);
        this.mLastMeasuredHeight = View.MeasureSpec.makeMeasureSpec((int) f4, 1073741824);
    }

    public static boolean diff(float f, float f2) {
        if (Float.isNaN(f) || Float.isNaN(f2)) {
            if (Float.isNaN(f) != Float.isNaN(f2)) {
                return true;
            }
            return false;
        } else if (Math.abs(f - f2) > 1.0E-6f) {
            return true;
        } else {
            return false;
        }
    }

    public MotionPaths(int i, int i2, KeyPosition keyPosition, MotionPaths motionPaths, MotionPaths motionPaths2) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        int i3;
        this.mDrawPath = 0;
        this.mPathRotate = Float.NaN;
        this.mPathMotionArc = -1;
        this.mLastMeasuredWidth = 0;
        this.mLastMeasuredHeight = 0;
        this.attributes = new LinkedHashMap<>();
        this.mMode = 0;
        this.mTempValue = new double[18];
        this.mTempDelta = new double[18];
        this.mLastMeasuredHeight = motionPaths2.mLastMeasuredHeight;
        this.mLastMeasuredWidth = motionPaths2.mLastMeasuredWidth;
        int i4 = keyPosition.mPositionType;
        if (i4 == 1) {
            float f8 = keyPosition.mFramePosition / 100.0f;
            this.time = f8;
            this.mDrawPath = keyPosition.mDrawPath;
            float f9 = Float.isNaN(keyPosition.mPercentWidth) ? f8 : keyPosition.mPercentWidth;
            float f10 = Float.isNaN(keyPosition.mPercentHeight) ? f8 : keyPosition.mPercentHeight;
            float f11 = motionPaths2.width - motionPaths.width;
            float f12 = motionPaths2.height - motionPaths.height;
            this.position = this.time;
            f8 = !Float.isNaN(keyPosition.mPercentX) ? keyPosition.mPercentX : f8;
            float f13 = motionPaths.x;
            float f14 = motionPaths.width;
            float f15 = motionPaths.y;
            float f16 = motionPaths.height;
            float f17 = ((motionPaths2.width / 2.0f) + motionPaths2.x) - ((f14 / 2.0f) + f13);
            float f18 = ((motionPaths2.height / 2.0f) + motionPaths2.y) - ((f16 / 2.0f) + f15);
            float f19 = f17 * f8;
            float f20 = (f11 * f9) / 2.0f;
            this.x = (int) ((f13 + f19) - f20);
            float f21 = f8 * f18;
            float f22 = (f12 * f10) / 2.0f;
            this.y = (int) ((f15 + f21) - f22);
            this.width = (int) (f14 + f);
            this.height = (int) (f16 + f2);
            float f23 = Float.isNaN(keyPosition.mPercentY) ? 0.0f : keyPosition.mPercentY;
            this.mMode = 1;
            float f24 = (int) ((motionPaths.x + f19) - f20);
            float f25 = (int) ((motionPaths.y + f21) - f22);
            this.x = f24 + ((-f18) * f23);
            this.y = f25 + (f17 * f23);
            this.mKeyFrameEasing = Easing.getInterpolator(keyPosition.mTransitionEasing);
            this.mPathMotionArc = keyPosition.mPathMotionArc;
        } else if (i4 != 2) {
            float f26 = keyPosition.mFramePosition / 100.0f;
            this.time = f26;
            this.mDrawPath = keyPosition.mDrawPath;
            float f27 = Float.isNaN(keyPosition.mPercentWidth) ? f26 : keyPosition.mPercentWidth;
            float f28 = Float.isNaN(keyPosition.mPercentHeight) ? f26 : keyPosition.mPercentHeight;
            float f29 = motionPaths2.width;
            float f30 = motionPaths.width;
            float f31 = f29 - f30;
            float f32 = motionPaths2.height;
            float f33 = motionPaths.height;
            float f34 = f32 - f33;
            this.position = this.time;
            float f35 = motionPaths.x;
            float f36 = motionPaths.y;
            float f37 = ((f29 / 2.0f) + motionPaths2.x) - ((f30 / 2.0f) + f35);
            float f38 = ((f32 / 2.0f) + motionPaths2.y) - ((f33 / 2.0f) + f36);
            float f39 = (f31 * f27) / 2.0f;
            this.x = (int) (((f37 * f26) + f35) - f39);
            float f40 = (f34 * f28) / 2.0f;
            this.y = (int) (((f38 * f26) + f36) - f40);
            this.width = (int) (f30 + f5);
            this.height = (int) (f33 + f6);
            float f41 = Float.isNaN(keyPosition.mPercentX) ? f26 : keyPosition.mPercentX;
            float f42 = Float.isNaN(Float.NaN) ? 0.0f : Float.NaN;
            f26 = !Float.isNaN(keyPosition.mPercentY) ? keyPosition.mPercentY : f26;
            if (Float.isNaN(Float.NaN)) {
                i3 = 2;
                f7 = 0.0f;
            } else {
                f7 = Float.NaN;
                i3 = 2;
            }
            this.mMode = i3;
            this.x = (int) (((f7 * f38) + ((f41 * f37) + motionPaths.x)) - f39);
            this.y = (int) (((f38 * f26) + ((f37 * f42) + motionPaths.y)) - f40);
            this.mKeyFrameEasing = Easing.getInterpolator(keyPosition.mTransitionEasing);
            this.mPathMotionArc = keyPosition.mPathMotionArc;
        } else {
            float f43 = keyPosition.mFramePosition / 100.0f;
            this.time = f43;
            this.mDrawPath = keyPosition.mDrawPath;
            float f44 = Float.isNaN(keyPosition.mPercentWidth) ? f43 : keyPosition.mPercentWidth;
            float f45 = Float.isNaN(keyPosition.mPercentHeight) ? f43 : keyPosition.mPercentHeight;
            float f46 = motionPaths2.width;
            float f47 = f46 - motionPaths.width;
            float f48 = motionPaths2.height;
            float f49 = f48 - motionPaths.height;
            this.position = this.time;
            float f50 = motionPaths.x;
            float f51 = motionPaths.y;
            float f52 = (f46 / 2.0f) + motionPaths2.x;
            float f53 = (f48 / 2.0f) + motionPaths2.y;
            float f54 = f47 * f44;
            this.x = (int) ((((f52 - ((f3 / 2.0f) + f50)) * f43) + f50) - (f54 / 2.0f));
            float f55 = f49 * f45;
            this.y = (int) ((((f53 - ((f4 / 2.0f) + f51)) * f43) + f51) - (f55 / 2.0f));
            this.width = (int) (f3 + f54);
            this.height = (int) (f4 + f55);
            this.mMode = 3;
            if (!Float.isNaN(keyPosition.mPercentX)) {
                this.x = (int) (keyPosition.mPercentX * ((int) (i - this.width)));
            }
            if (!Float.isNaN(keyPosition.mPercentY)) {
                this.y = (int) (keyPosition.mPercentY * ((int) (i2 - this.height)));
            }
            this.mKeyFrameEasing = Easing.getInterpolator(keyPosition.mTransitionEasing);
            this.mPathMotionArc = keyPosition.mPathMotionArc;
        }
    }
}
