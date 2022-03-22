package androidx.constraintlayout.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import androidx.constraintlayout.motion.utils.ArcCurveFit;
import androidx.constraintlayout.motion.utils.VelocityMatrix;
import androidx.constraintlayout.motion.widget.KeyCycleOscillator;
import androidx.constraintlayout.motion.widget.MotionController;
import androidx.constraintlayout.motion.widget.MotionInterpolator;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionPaths;
import androidx.constraintlayout.motion.widget.SplineSet;
import androidx.constraintlayout.widget.R$styleable;
import java.util.HashMap;
import java.util.Objects;
/* loaded from: classes.dex */
public class MotionTelltales extends MockView {
    public MotionLayout mMotionLayout;
    public int mTailColor;
    public float mTailScale;
    public int mVelocityMode;
    public Paint mPaintTelltales = new Paint();
    public float[] velocity = new float[2];
    public Matrix mInvertMatrix = new Matrix();

    @Override // androidx.constraintlayout.utils.widget.MockView, android.view.View
    public final void onDraw(Canvas canvas) {
        float f;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        float[] fArr;
        float[] fArr2;
        float f2;
        float f3;
        SplineSet splineSet;
        SplineSet splineSet2;
        SplineSet splineSet3;
        SplineSet splineSet4;
        int i6;
        SplineSet splineSet5;
        KeyCycleOscillator keyCycleOscillator;
        KeyCycleOscillator keyCycleOscillator2;
        KeyCycleOscillator keyCycleOscillator3;
        KeyCycleOscillator keyCycleOscillator4;
        float f4;
        float f5;
        float f6;
        double[] dArr;
        int i7;
        float f7;
        float[] fArr3;
        MotionTelltales motionTelltales = this;
        super.onDraw(canvas);
        getMatrix().invert(motionTelltales.mInvertMatrix);
        if (motionTelltales.mMotionLayout == null) {
            ViewParent parent = getParent();
            if (parent instanceof MotionLayout) {
                motionTelltales.mMotionLayout = (MotionLayout) parent;
                return;
            }
            return;
        }
        int width = getWidth();
        int height = getHeight();
        int i8 = 5;
        float[] fArr4 = {0.1f, 0.25f, 0.5f, 0.75f, 0.9f};
        MotionTelltales motionTelltales2 = motionTelltales;
        int i9 = 0;
        while (i9 < i8) {
            float f8 = fArr4[i9];
            int i10 = 0;
            while (i10 < i8) {
                float f9 = fArr4[i10];
                MotionLayout motionLayout = motionTelltales2.mMotionLayout;
                float[] fArr5 = motionTelltales2.velocity;
                int i11 = motionTelltales2.mVelocityMode;
                Objects.requireNonNull(motionLayout);
                float f10 = motionLayout.mLastVelocity;
                float f11 = motionLayout.mTransitionLastPosition;
                if (motionLayout.mInterpolator != null) {
                    float signum = Math.signum(motionLayout.mTransitionGoalPosition - f11);
                    float interpolation = motionLayout.mInterpolator.getInterpolation(motionLayout.mTransitionLastPosition + 1.0E-5f);
                    f11 = motionLayout.mInterpolator.getInterpolation(motionLayout.mTransitionLastPosition);
                    f10 = (((interpolation - f11) / 1.0E-5f) * signum) / motionLayout.mTransitionDuration;
                }
                Interpolator interpolator = motionLayout.mInterpolator;
                if (interpolator instanceof MotionInterpolator) {
                    f10 = ((MotionInterpolator) interpolator).getVelocity$1();
                }
                MotionController motionController = motionLayout.mFrameArrayList.get(motionTelltales2);
                if ((i11 & 1) == 0) {
                    int width2 = getWidth();
                    int height2 = getHeight();
                    Objects.requireNonNull(motionController);
                    float adjustedPosition = motionController.getAdjustedPosition(f11, motionController.mVelocity);
                    HashMap<String, SplineSet> hashMap = motionController.mAttributesMap;
                    KeyCycleOscillator keyCycleOscillator5 = null;
                    if (hashMap == null) {
                        fArr = fArr4;
                        splineSet = null;
                    } else {
                        splineSet = hashMap.get("translationX");
                        fArr = fArr4;
                    }
                    HashMap<String, SplineSet> hashMap2 = motionController.mAttributesMap;
                    i5 = i11;
                    if (hashMap2 == null) {
                        i4 = i9;
                        splineSet2 = null;
                    } else {
                        splineSet2 = hashMap2.get("translationY");
                        i4 = i9;
                    }
                    HashMap<String, SplineSet> hashMap3 = motionController.mAttributesMap;
                    i3 = i10;
                    if (hashMap3 == null) {
                        i2 = height;
                        splineSet3 = null;
                    } else {
                        splineSet3 = hashMap3.get("rotation");
                        i2 = height;
                    }
                    HashMap<String, SplineSet> hashMap4 = motionController.mAttributesMap;
                    i = width;
                    if (hashMap4 == null) {
                        splineSet4 = null;
                    } else {
                        splineSet4 = hashMap4.get("scaleX");
                    }
                    HashMap<String, SplineSet> hashMap5 = motionController.mAttributesMap;
                    f = f10;
                    if (hashMap5 == null) {
                        i6 = width2;
                        splineSet5 = null;
                    } else {
                        splineSet5 = hashMap5.get("scaleY");
                        i6 = width2;
                    }
                    HashMap<String, KeyCycleOscillator> hashMap6 = motionController.mCycleMap;
                    if (hashMap6 == null) {
                        keyCycleOscillator = null;
                    } else {
                        keyCycleOscillator = hashMap6.get("translationX");
                    }
                    HashMap<String, KeyCycleOscillator> hashMap7 = motionController.mCycleMap;
                    if (hashMap7 == null) {
                        keyCycleOscillator2 = null;
                    } else {
                        keyCycleOscillator2 = hashMap7.get("translationY");
                    }
                    HashMap<String, KeyCycleOscillator> hashMap8 = motionController.mCycleMap;
                    if (hashMap8 == null) {
                        keyCycleOscillator3 = null;
                    } else {
                        keyCycleOscillator3 = hashMap8.get("rotation");
                    }
                    HashMap<String, KeyCycleOscillator> hashMap9 = motionController.mCycleMap;
                    if (hashMap9 == null) {
                        keyCycleOscillator4 = null;
                    } else {
                        keyCycleOscillator4 = hashMap9.get("scaleX");
                    }
                    HashMap<String, KeyCycleOscillator> hashMap10 = motionController.mCycleMap;
                    if (hashMap10 != null) {
                        keyCycleOscillator5 = hashMap10.get("scaleY");
                    }
                    VelocityMatrix velocityMatrix = new VelocityMatrix();
                    velocityMatrix.mDRotate = 0.0f;
                    velocityMatrix.mDTranslateY = 0.0f;
                    velocityMatrix.mDTranslateX = 0.0f;
                    velocityMatrix.mDScaleY = 0.0f;
                    velocityMatrix.mDScaleX = 0.0f;
                    if (splineSet3 != null) {
                        f4 = f9;
                        f5 = f8;
                        velocityMatrix.mDRotate = (float) splineSet3.mCurveFit.getSlope(adjustedPosition);
                        velocityMatrix.mRotate = splineSet3.get(adjustedPosition);
                    } else {
                        f4 = f9;
                        f5 = f8;
                    }
                    if (splineSet != null) {
                        velocityMatrix.mDTranslateX = (float) splineSet.mCurveFit.getSlope(adjustedPosition);
                    }
                    if (splineSet2 != null) {
                        velocityMatrix.mDTranslateY = (float) splineSet2.mCurveFit.getSlope(adjustedPosition);
                    }
                    if (splineSet4 != null) {
                        velocityMatrix.mDScaleX = (float) splineSet4.mCurveFit.getSlope(adjustedPosition);
                    }
                    if (splineSet5 != null) {
                        velocityMatrix.mDScaleY = (float) splineSet5.mCurveFit.getSlope(adjustedPosition);
                    }
                    if (keyCycleOscillator3 != null) {
                        velocityMatrix.mDRotate = keyCycleOscillator3.getSlope(adjustedPosition);
                    }
                    if (keyCycleOscillator != null) {
                        velocityMatrix.mDTranslateX = keyCycleOscillator.getSlope(adjustedPosition);
                    }
                    if (keyCycleOscillator2 != null) {
                        velocityMatrix.mDTranslateY = keyCycleOscillator2.getSlope(adjustedPosition);
                    }
                    if (!(keyCycleOscillator4 == null && keyCycleOscillator5 == null)) {
                        if (keyCycleOscillator4 == null) {
                            velocityMatrix.mDScaleX = keyCycleOscillator4.getSlope(adjustedPosition);
                        }
                        if (keyCycleOscillator5 == null) {
                            velocityMatrix.mDScaleY = keyCycleOscillator5.getSlope(adjustedPosition);
                        }
                    }
                    ArcCurveFit arcCurveFit = motionController.mArcSpline;
                    if (arcCurveFit != null) {
                        double[] dArr2 = motionController.mInterpolateData;
                        if (dArr2.length > 0) {
                            double d = adjustedPosition;
                            arcCurveFit.getPos(d, dArr2);
                            motionController.mArcSpline.getSlope(d, motionController.mInterpolateVelocity);
                            MotionPaths motionPaths = motionController.mStartMotionPath;
                            int[] iArr = motionController.mInterpolateVariables;
                            double[] dArr3 = motionController.mInterpolateVelocity;
                            double[] dArr4 = motionController.mInterpolateData;
                            Objects.requireNonNull(motionPaths);
                            i7 = i5;
                            fArr3 = fArr5;
                            f7 = f4;
                            MotionPaths.setDpDt(f4, f5, fArr5, iArr, dArr3, dArr4);
                        } else {
                            fArr3 = fArr5;
                            i7 = i5;
                            f7 = f4;
                        }
                        velocityMatrix.applyTransform(f7, f5, i6, height2, fArr3);
                        i5 = i7;
                        fArr2 = fArr3;
                        f3 = f7;
                    } else if (motionController.mSpline != null) {
                        double adjustedPosition2 = motionController.getAdjustedPosition(adjustedPosition, motionController.mVelocity);
                        motionController.mSpline[0].getSlope(adjustedPosition2, motionController.mInterpolateVelocity);
                        motionController.mSpline[0].getPos(adjustedPosition2, motionController.mInterpolateData);
                        float f12 = motionController.mVelocity[0];
                        int i12 = 0;
                        while (true) {
                            dArr = motionController.mInterpolateVelocity;
                            if (i12 >= dArr.length) {
                                break;
                            }
                            dArr[i12] = dArr[i12] * f12;
                            i12++;
                        }
                        MotionPaths motionPaths2 = motionController.mStartMotionPath;
                        int[] iArr2 = motionController.mInterpolateVariables;
                        double[] dArr5 = motionController.mInterpolateData;
                        Objects.requireNonNull(motionPaths2);
                        fArr2 = fArr5;
                        f3 = f4;
                        MotionPaths.setDpDt(f4, f5, fArr5, iArr2, dArr, dArr5);
                        velocityMatrix.applyTransform(f3, f5, i6, height2, fArr2);
                    } else {
                        fArr2 = fArr5;
                        MotionPaths motionPaths3 = motionController.mEndMotionPath;
                        float f13 = motionPaths3.x;
                        MotionPaths motionPaths4 = motionController.mStartMotionPath;
                        float f14 = f13 - motionPaths4.x;
                        float f15 = motionPaths3.y - motionPaths4.y;
                        fArr2[0] = (((motionPaths3.width - motionPaths4.width) + f14) * f4) + ((1.0f - f4) * f14);
                        fArr2[1] = (((motionPaths3.height - motionPaths4.height) + f15) * f5) + ((1.0f - f5) * f15);
                        velocityMatrix.mDRotate = 0.0f;
                        velocityMatrix.mDTranslateY = 0.0f;
                        velocityMatrix.mDTranslateX = 0.0f;
                        velocityMatrix.mDScaleY = 0.0f;
                        velocityMatrix.mDScaleX = 0.0f;
                        if (splineSet3 != null) {
                            f6 = f4;
                            velocityMatrix.mDRotate = (float) splineSet3.mCurveFit.getSlope(adjustedPosition);
                            velocityMatrix.mRotate = splineSet3.get(adjustedPosition);
                        } else {
                            f6 = f4;
                        }
                        if (splineSet != null) {
                            velocityMatrix.mDTranslateX = (float) splineSet.mCurveFit.getSlope(adjustedPosition);
                        }
                        if (splineSet2 != null) {
                            velocityMatrix.mDTranslateY = (float) splineSet2.mCurveFit.getSlope(adjustedPosition);
                        }
                        if (splineSet4 != null) {
                            velocityMatrix.mDScaleX = (float) splineSet4.mCurveFit.getSlope(adjustedPosition);
                        }
                        if (splineSet5 != null) {
                            velocityMatrix.mDScaleY = (float) splineSet5.mCurveFit.getSlope(adjustedPosition);
                        }
                        if (keyCycleOscillator3 != null) {
                            velocityMatrix.mDRotate = keyCycleOscillator3.getSlope(adjustedPosition);
                        }
                        if (keyCycleOscillator != null) {
                            velocityMatrix.mDTranslateX = keyCycleOscillator.getSlope(adjustedPosition);
                        }
                        if (keyCycleOscillator2 != null) {
                            velocityMatrix.mDTranslateY = keyCycleOscillator2.getSlope(adjustedPosition);
                        }
                        if (!(keyCycleOscillator4 == null && keyCycleOscillator5 == null)) {
                            if (keyCycleOscillator4 == null) {
                                velocityMatrix.mDScaleX = keyCycleOscillator4.getSlope(adjustedPosition);
                            }
                            if (keyCycleOscillator5 == null) {
                                velocityMatrix.mDScaleY = keyCycleOscillator5.getSlope(adjustedPosition);
                            }
                        }
                        f3 = f6;
                        velocityMatrix.applyTransform(f6, f5, i6, height2, fArr2);
                    }
                    f2 = f5;
                } else {
                    i = width;
                    i2 = height;
                    f = f10;
                    fArr = fArr4;
                    i4 = i9;
                    i5 = i11;
                    f3 = f9;
                    f2 = f8;
                    i3 = i10;
                    fArr2 = fArr5;
                    motionController.getDpDt(f11, f3, f2, fArr2);
                }
                if (i5 < 2) {
                    fArr2[0] = fArr2[0] * f;
                    fArr2[1] = fArr2[1] * f;
                }
                this.mInvertMatrix.mapVectors(this.velocity);
                float f16 = i * f3;
                float f17 = i2 * f2;
                float[] fArr6 = this.velocity;
                float f18 = fArr6[0];
                float f19 = this.mTailScale;
                float f20 = f17 - (fArr6[1] * f19);
                this.mInvertMatrix.mapVectors(fArr6);
                canvas.drawLine(f16, f17, f16 - (f18 * f19), f20, this.mPaintTelltales);
                i10 = i3 + 1;
                f8 = f2;
                motionTelltales2 = this;
                width = i;
                fArr4 = fArr;
                i9 = i4;
                i8 = 5;
                height = i2;
                motionTelltales = motionTelltales2;
            }
            i9++;
            i8 = 5;
            height = height;
            motionTelltales = motionTelltales;
        }
    }

    public MotionTelltales(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mVelocityMode = 0;
        this.mTailColor = -65281;
        this.mTailScale = 0.25f;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.MotionTelltales);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == 0) {
                    this.mTailColor = obtainStyledAttributes.getColor(index, this.mTailColor);
                } else if (index == 2) {
                    this.mVelocityMode = obtainStyledAttributes.getInt(index, this.mVelocityMode);
                } else if (index == 1) {
                    this.mTailScale = obtainStyledAttributes.getFloat(index, this.mTailScale);
                }
            }
        }
        this.mPaintTelltales.setColor(this.mTailColor);
        this.mPaintTelltales.setStrokeWidth(5.0f);
    }

    @Override // android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        postInvalidate();
    }

    @Override // android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
