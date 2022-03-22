package androidx.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.transition.MatrixUtils;
import java.util.HashMap;
/* loaded from: classes.dex */
public class ChangeImageTransform extends Transition {
    public static final String[] sTransitionProperties = {"android:changeImageTransform:matrix", "android:changeImageTransform:bounds"};
    public static final AnonymousClass1 NULL_MATRIX_EVALUATOR = new TypeEvaluator<Matrix>() { // from class: androidx.transition.ChangeImageTransform.1
        @Override // android.animation.TypeEvaluator
        public final /* bridge */ /* synthetic */ Matrix evaluate(float f, Matrix matrix, Matrix matrix2) {
            return null;
        }
    };
    public static final AnonymousClass2 ANIMATED_TRANSFORM_PROPERTY = new AnonymousClass2(Matrix.class);

    /* renamed from: androidx.transition.ChangeImageTransform$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends Property<ImageView, Matrix> {
        public AnonymousClass2(Class cls) {
            super(cls, "animatedTransform");
        }

        @Override // android.util.Property
        public final /* bridge */ /* synthetic */ Matrix get(ImageView imageView) {
            return null;
        }

        @Override // android.util.Property
        public final void set(ImageView imageView, Matrix matrix) {
            imageView.animateTransform(matrix);
        }
    }

    public ChangeImageTransform() {
    }

    @Override // androidx.transition.Transition
    public final Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        boolean z;
        if (transitionValues == null || transitionValues2 == null) {
            return null;
        }
        Rect rect = (Rect) transitionValues.values.get("android:changeImageTransform:bounds");
        Rect rect2 = (Rect) transitionValues2.values.get("android:changeImageTransform:bounds");
        if (rect == null || rect2 == null) {
            return null;
        }
        Matrix matrix = (Matrix) transitionValues.values.get("android:changeImageTransform:matrix");
        Matrix matrix2 = (Matrix) transitionValues2.values.get("android:changeImageTransform:matrix");
        if (!(matrix == null && matrix2 == null) && (matrix == null || !matrix.equals(matrix2))) {
            z = false;
        } else {
            z = true;
        }
        if (rect.equals(rect2) && z) {
            return null;
        }
        ImageView imageView = (ImageView) transitionValues2.view;
        Drawable drawable = imageView.getDrawable();
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            AnonymousClass2 r6 = ANIMATED_TRANSFORM_PROPERTY;
            AnonymousClass1 r7 = NULL_MATRIX_EVALUATOR;
            MatrixUtils.AnonymousClass1 r0 = MatrixUtils.IDENTITY_MATRIX;
            return ObjectAnimator.ofObject(imageView, r6, r7, r0, r0);
        }
        if (matrix == null) {
            matrix = MatrixUtils.IDENTITY_MATRIX;
        }
        if (matrix2 == null) {
            matrix2 = MatrixUtils.IDENTITY_MATRIX;
        }
        AnonymousClass2 r62 = ANIMATED_TRANSFORM_PROPERTY;
        r62.set(imageView, matrix);
        return ObjectAnimator.ofObject(imageView, r62, new TypeEvaluator<Matrix>() { // from class: androidx.transition.TransitionUtils$MatrixEvaluator
            public final float[] mTempStartValues = new float[9];
            public final float[] mTempEndValues = new float[9];
            public final Matrix mTempMatrix = new Matrix();

            @Override // android.animation.TypeEvaluator
            public final Matrix evaluate(float f, Matrix matrix3, Matrix matrix4) {
                matrix3.getValues(this.mTempStartValues);
                matrix4.getValues(this.mTempEndValues);
                for (int i = 0; i < 9; i++) {
                    float[] fArr = this.mTempEndValues;
                    float f2 = fArr[i];
                    float[] fArr2 = this.mTempStartValues;
                    fArr[i] = ((f2 - fArr2[i]) * f) + fArr2[i];
                }
                this.mTempMatrix.setValues(this.mTempEndValues);
                return this.mTempMatrix;
            }
        }, matrix, matrix2);
    }

    /* renamed from: androidx.transition.ChangeImageTransform$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass3 {
        public static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType;

        static {
            int[] iArr = new int[ImageView.ScaleType.values().length];
            $SwitchMap$android$widget$ImageView$ScaleType = iArr;
            try {
                iArr[ImageView.ScaleType.FIT_XY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER_CROP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public ChangeImageTransform(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public final void captureValues(TransitionValues transitionValues) {
        Matrix matrix;
        View view = transitionValues.view;
        if ((view instanceof ImageView) && view.getVisibility() == 0) {
            ImageView imageView = (ImageView) view;
            if (imageView.getDrawable() != null) {
                HashMap hashMap = transitionValues.values;
                hashMap.put("android:changeImageTransform:bounds", new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
                Drawable drawable = imageView.getDrawable();
                if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                    matrix = new Matrix(imageView.getImageMatrix());
                } else {
                    int i = AnonymousClass3.$SwitchMap$android$widget$ImageView$ScaleType[imageView.getScaleType().ordinal()];
                    if (i == 1) {
                        Drawable drawable2 = imageView.getDrawable();
                        Matrix matrix2 = new Matrix();
                        matrix2.postScale(imageView.getWidth() / drawable2.getIntrinsicWidth(), imageView.getHeight() / drawable2.getIntrinsicHeight());
                        matrix = matrix2;
                    } else if (i != 2) {
                        matrix = new Matrix(imageView.getImageMatrix());
                    } else {
                        Drawable drawable3 = imageView.getDrawable();
                        int intrinsicWidth = drawable3.getIntrinsicWidth();
                        float width = imageView.getWidth();
                        float f = intrinsicWidth;
                        int intrinsicHeight = drawable3.getIntrinsicHeight();
                        float height = imageView.getHeight();
                        float f2 = intrinsicHeight;
                        float max = Math.max(width / f, height / f2);
                        int round = Math.round((width - (f * max)) / 2.0f);
                        int round2 = Math.round((height - (f2 * max)) / 2.0f);
                        Matrix matrix3 = new Matrix();
                        matrix3.postScale(max, max);
                        matrix3.postTranslate(round, round2);
                        matrix = matrix3;
                    }
                }
                hashMap.put("android:changeImageTransform:matrix", matrix);
            }
        }
    }

    @Override // androidx.transition.Transition
    public final void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Transition
    public final void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Transition
    public final String[] getTransitionProperties() {
        return sTransitionProperties;
    }
}
