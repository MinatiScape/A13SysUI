package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.TypedValue;
import android.view.View;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.leanback.R$fraction;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.ImageMatrixProperty;
import com.google.android.material.animation.MatrixEvaluator;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.StateListAnimator;
import com.google.android.material.motion.MotionUtils;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shadow.ShadowViewDelegate;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class FloatingActionButtonImpl {
    public BorderDrawable borderDrawable;
    public LayerDrawable contentBackground;
    public Animator currentAnimator;
    public float elevation;
    public boolean ensureMinTouchTargetSize;
    public ArrayList<Animator.AnimatorListener> hideListeners;
    public MotionSpec hideMotionSpec;
    public float hoveredFocusedTranslationZ;
    public int maxImageSize;
    public int minTouchTargetSize;
    public AnonymousClass6 preDrawListener;
    public float pressedTranslationZ;
    public Drawable rippleDrawable;
    public float rotation;
    public final ShadowViewDelegate shadowViewDelegate;
    public ShapeAppearanceModel shapeAppearance;
    public MaterialShapeDrawable shapeDrawable;
    public ArrayList<Animator.AnimatorListener> showListeners;
    public MotionSpec showMotionSpec;
    public final StateListAnimator stateListAnimator;
    public ArrayList<InternalTransformationCallback> transformationCallbacks;
    public final FloatingActionButton view;
    public static final FastOutLinearInInterpolator ELEVATION_ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
    public static final int[] PRESSED_ENABLED_STATE_SET = {16842919, 16842910};
    public static final int[] HOVERED_FOCUSED_ENABLED_STATE_SET = {16843623, 16842908, 16842910};
    public static final int[] FOCUSED_ENABLED_STATE_SET = {16842908, 16842910};
    public static final int[] HOVERED_ENABLED_STATE_SET = {16843623, 16842910};
    public static final int[] ENABLED_STATE_SET = {16842910};
    public static final int[] EMPTY_STATE_SET = new int[0];
    public boolean shadowPaddingEnabled = true;
    public float imageMatrixScale = 1.0f;
    public int animState = 0;
    public final Rect tmpRect = new Rect();
    public final RectF tmpRectF1 = new RectF();
    public final RectF tmpRectF2 = new RectF();
    public final Matrix tmpMatrix = new Matrix();

    /* loaded from: classes.dex */
    public class ElevateToHoveredFocusedTranslationZAnimation extends ShadowAnimatorImpl {
        public final /* synthetic */ FloatingActionButtonImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ElevateToHoveredFocusedTranslationZAnimation(FloatingActionButtonImplLollipop floatingActionButtonImplLollipop) {
            super(floatingActionButtonImplLollipop);
            this.this$0 = floatingActionButtonImplLollipop;
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.ShadowAnimatorImpl
        public final float getTargetShadowSize() {
            FloatingActionButtonImpl floatingActionButtonImpl = this.this$0;
            return floatingActionButtonImpl.elevation + floatingActionButtonImpl.hoveredFocusedTranslationZ;
        }
    }

    /* loaded from: classes.dex */
    public class ElevateToPressedTranslationZAnimation extends ShadowAnimatorImpl {
        public final /* synthetic */ FloatingActionButtonImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ElevateToPressedTranslationZAnimation(FloatingActionButtonImplLollipop floatingActionButtonImplLollipop) {
            super(floatingActionButtonImplLollipop);
            this.this$0 = floatingActionButtonImplLollipop;
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.ShadowAnimatorImpl
        public final float getTargetShadowSize() {
            FloatingActionButtonImpl floatingActionButtonImpl = this.this$0;
            return floatingActionButtonImpl.elevation + floatingActionButtonImpl.pressedTranslationZ;
        }
    }

    /* loaded from: classes.dex */
    public interface InternalTransformationCallback {
        void onScaleChanged();

        void onTranslationChanged();
    }

    /* loaded from: classes.dex */
    public interface InternalVisibilityChangedListener {
        void onHidden();

        void onShown();
    }

    /* loaded from: classes.dex */
    public class ResetElevationAnimation extends ShadowAnimatorImpl {
        public final /* synthetic */ FloatingActionButtonImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ResetElevationAnimation(FloatingActionButtonImplLollipop floatingActionButtonImplLollipop) {
            super(floatingActionButtonImplLollipop);
            this.this$0 = floatingActionButtonImplLollipop;
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.ShadowAnimatorImpl
        public final float getTargetShadowSize() {
            return this.this$0.elevation;
        }
    }

    /* loaded from: classes.dex */
    public abstract class ShadowAnimatorImpl extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
        public float shadowSizeEnd;
        public float shadowSizeStart;
        public final /* synthetic */ FloatingActionButtonImpl this$0;
        public boolean validValues;

        public abstract float getTargetShadowSize();

        public ShadowAnimatorImpl(FloatingActionButtonImplLollipop floatingActionButtonImplLollipop) {
            this.this$0 = floatingActionButtonImplLollipop;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            FloatingActionButtonImpl floatingActionButtonImpl = this.this$0;
            float f = (int) this.shadowSizeEnd;
            Objects.requireNonNull(floatingActionButtonImpl);
            MaterialShapeDrawable materialShapeDrawable = floatingActionButtonImpl.shapeDrawable;
            if (materialShapeDrawable != null) {
                materialShapeDrawable.setElevation(f);
            }
            this.validValues = false;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            float f;
            if (!this.validValues) {
                MaterialShapeDrawable materialShapeDrawable = this.this$0.shapeDrawable;
                if (materialShapeDrawable == null) {
                    f = 0.0f;
                } else {
                    f = materialShapeDrawable.drawableState.elevation;
                }
                this.shadowSizeStart = f;
                this.shadowSizeEnd = getTargetShadowSize();
                this.validValues = true;
            }
            FloatingActionButtonImpl floatingActionButtonImpl = this.this$0;
            float f2 = this.shadowSizeStart;
            float animatedFraction = (int) ((valueAnimator.getAnimatedFraction() * (this.shadowSizeEnd - f2)) + f2);
            Objects.requireNonNull(floatingActionButtonImpl);
            MaterialShapeDrawable materialShapeDrawable2 = floatingActionButtonImpl.shapeDrawable;
            if (materialShapeDrawable2 != null) {
                materialShapeDrawable2.setElevation(animatedFraction);
            }
        }
    }

    public final AnimatorSet createDefaultAnimator(final float f, final float f2, final float f3) {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        final float alpha = this.view.getAlpha();
        final float scaleX = this.view.getScaleX();
        final float scaleY = this.view.getScaleY();
        final float f4 = this.imageMatrixScale;
        final Matrix matrix = new Matrix(this.tmpMatrix);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                FloatingActionButtonImpl.this.view.setAlpha(AnimationUtils.lerp(alpha, f, 0.0f, 0.2f, floatValue));
                FloatingActionButton floatingActionButton = FloatingActionButtonImpl.this.view;
                float f5 = scaleX;
                floatingActionButton.setScaleX(((f2 - f5) * floatValue) + f5);
                FloatingActionButton floatingActionButton2 = FloatingActionButtonImpl.this.view;
                float f6 = scaleY;
                floatingActionButton2.setScaleY(((f2 - f6) * floatValue) + f6);
                FloatingActionButtonImpl floatingActionButtonImpl = FloatingActionButtonImpl.this;
                float f7 = f4;
                float f8 = f3;
                floatingActionButtonImpl.imageMatrixScale = MotionController$$ExternalSyntheticOutline0.m(f8, f7, floatValue, f7);
                floatingActionButtonImpl.calculateImageMatrixFromScale(MotionController$$ExternalSyntheticOutline0.m(f8, f7, floatValue, f7), matrix);
                FloatingActionButtonImpl.this.view.setImageMatrix(matrix);
            }
        });
        arrayList.add(ofFloat);
        R$fraction.playTogether(animatorSet, arrayList);
        Context context = this.view.getContext();
        int integer = this.view.getContext().getResources().getInteger(2131492994);
        TypedValue resolve = MaterialAttributes.resolve(context, 2130969488);
        if (resolve != null && resolve.type == 16) {
            integer = resolve.data;
        }
        animatorSet.setDuration(integer);
        animatorSet.setInterpolator(MotionUtils.resolveThemeInterpolator(this.view.getContext(), AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR));
        return animatorSet;
    }

    public float getElevation() {
        throw null;
    }

    public void initializeBackgroundDrawable(ColorStateList colorStateList, PorterDuff.Mode mode, ColorStateList colorStateList2, int i) {
        throw null;
    }

    public void jumpDrawableToCurrentState() {
        throw null;
    }

    public void onDrawableStateChanged(int[] iArr) {
        throw null;
    }

    public void onElevationsChanged(float f, float f2, float f3) {
        throw null;
    }

    public boolean shouldAddPadding() {
        throw null;
    }

    public void updateFromViewRotation() {
        throw null;
    }

    public static ValueAnimator createElevationAnimator(ShadowAnimatorImpl shadowAnimatorImpl) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
        valueAnimator.setDuration(100L);
        valueAnimator.addListener(shadowAnimatorImpl);
        valueAnimator.addUpdateListener(shadowAnimatorImpl);
        valueAnimator.setFloatValues(0.0f, 1.0f);
        return valueAnimator;
    }

    public final AnimatorSet createAnimator(MotionSpec motionSpec, float f, float f2, float f3) {
        ArrayList arrayList = new ArrayList();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.view, View.ALPHA, f);
        motionSpec.getTiming("opacity").apply(ofFloat);
        arrayList.add(ofFloat);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.view, View.SCALE_X, f2);
        motionSpec.getTiming("scale").apply(ofFloat2);
        arrayList.add(ofFloat2);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.view, View.SCALE_Y, f2);
        motionSpec.getTiming("scale").apply(ofFloat3);
        arrayList.add(ofFloat3);
        calculateImageMatrixFromScale(f3, this.tmpMatrix);
        ObjectAnimator ofObject = ObjectAnimator.ofObject(this.view, new ImageMatrixProperty(), new MatrixEvaluator() { // from class: com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.3
            @Override // android.animation.TypeEvaluator
            public final Matrix evaluate(float f4, Matrix matrix, Matrix matrix2) {
                FloatingActionButtonImpl.this.imageMatrixScale = f4;
                matrix.getValues(this.tempStartValues);
                matrix2.getValues(this.tempEndValues);
                for (int i = 0; i < 9; i++) {
                    float[] fArr = this.tempEndValues;
                    float f5 = fArr[i];
                    float[] fArr2 = this.tempStartValues;
                    fArr[i] = ((f5 - fArr2[i]) * f4) + fArr2[i];
                }
                this.tempMatrix.setValues(this.tempEndValues);
                return this.tempMatrix;
            }
        }, new Matrix(this.tmpMatrix));
        motionSpec.getTiming("iconScale").apply(ofObject);
        arrayList.add(ofObject);
        AnimatorSet animatorSet = new AnimatorSet();
        R$fraction.playTogether(animatorSet, arrayList);
        return animatorSet;
    }

    public void getPadding(Rect rect) {
        int i;
        float f;
        if (this.ensureMinTouchTargetSize) {
            int i2 = this.minTouchTargetSize;
            FloatingActionButton floatingActionButton = this.view;
            Objects.requireNonNull(floatingActionButton);
            i = (i2 - floatingActionButton.getSizeDimension(floatingActionButton.size)) / 2;
        } else {
            i = 0;
        }
        if (this.shadowPaddingEnabled) {
            f = getElevation() + this.pressedTranslationZ;
        } else {
            f = 0.0f;
        }
        int max = Math.max(i, (int) Math.ceil(f));
        int max2 = Math.max(i, (int) Math.ceil(f * 1.5f));
        rect.set(max, max2, max, max2);
    }

    public final void onTranslationChanged() {
        ArrayList<InternalTransformationCallback> arrayList = this.transformationCallbacks;
        if (arrayList != null) {
            Iterator<InternalTransformationCallback> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().onTranslationChanged();
            }
        }
    }

    public final void setShapeAppearance(ShapeAppearanceModel shapeAppearanceModel) {
        this.shapeAppearance = shapeAppearanceModel;
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setShapeAppearanceModel(shapeAppearanceModel);
        }
        Drawable drawable = this.rippleDrawable;
        if (drawable instanceof Shapeable) {
            ((Shapeable) drawable).setShapeAppearanceModel(shapeAppearanceModel);
        }
        BorderDrawable borderDrawable = this.borderDrawable;
        if (borderDrawable != null) {
            borderDrawable.shapeAppearanceModel = shapeAppearanceModel;
            borderDrawable.invalidateSelf();
        }
    }

    public final void updatePadding() {
        Rect rect = this.tmpRect;
        getPadding(rect);
        Objects.requireNonNull(this.contentBackground, "Didn't initialize content background");
        if (shouldAddPadding()) {
            FloatingActionButtonImpl.super.setBackgroundDrawable(new InsetDrawable((Drawable) this.contentBackground, rect.left, rect.top, rect.right, rect.bottom));
        } else {
            ShadowViewDelegate shadowViewDelegate = this.shadowViewDelegate;
            LayerDrawable layerDrawable = this.contentBackground;
            FloatingActionButton.ShadowDelegateImpl shadowDelegateImpl = (FloatingActionButton.ShadowDelegateImpl) shadowViewDelegate;
            if (layerDrawable != null) {
                FloatingActionButtonImpl.super.setBackgroundDrawable(layerDrawable);
            } else {
                Objects.requireNonNull(shadowDelegateImpl);
            }
        }
        ShadowViewDelegate shadowViewDelegate2 = this.shadowViewDelegate;
        int i = rect.left;
        int i2 = rect.top;
        int i3 = rect.right;
        int i4 = rect.bottom;
        FloatingActionButton.ShadowDelegateImpl shadowDelegateImpl2 = (FloatingActionButton.ShadowDelegateImpl) shadowViewDelegate2;
        Objects.requireNonNull(shadowDelegateImpl2);
        FloatingActionButton.this.shadowPadding.set(i, i2, i3, i4);
        FloatingActionButton floatingActionButton = FloatingActionButton.this;
        int i5 = floatingActionButton.imagePadding;
        floatingActionButton.setPadding(i + i5, i2 + i5, i3 + i5, i4 + i5);
    }

    public FloatingActionButtonImpl(FloatingActionButton floatingActionButton, FloatingActionButton.ShadowDelegateImpl shadowDelegateImpl) {
        this.view = floatingActionButton;
        this.shadowViewDelegate = shadowDelegateImpl;
        StateListAnimator stateListAnimator = new StateListAnimator();
        this.stateListAnimator = stateListAnimator;
        FloatingActionButtonImplLollipop floatingActionButtonImplLollipop = (FloatingActionButtonImplLollipop) this;
        stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToPressedTranslationZAnimation(floatingActionButtonImplLollipop)));
        stateListAnimator.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation(floatingActionButtonImplLollipop)));
        stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation(floatingActionButtonImplLollipop)));
        stateListAnimator.addState(HOVERED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation(floatingActionButtonImplLollipop)));
        stateListAnimator.addState(ENABLED_STATE_SET, createElevationAnimator(new ResetElevationAnimation(floatingActionButtonImplLollipop)));
        stateListAnimator.addState(EMPTY_STATE_SET, createElevationAnimator(new DisabledElevationAnimation(floatingActionButtonImplLollipop)));
        this.rotation = floatingActionButton.getRotation();
    }

    public final void calculateImageMatrixFromScale(float f, Matrix matrix) {
        matrix.reset();
        Drawable drawable = this.view.getDrawable();
        if (drawable != null && this.maxImageSize != 0) {
            RectF rectF = this.tmpRectF1;
            RectF rectF2 = this.tmpRectF2;
            rectF.set(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            int i = this.maxImageSize;
            rectF2.set(0.0f, 0.0f, i, i);
            matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.CENTER);
            int i2 = this.maxImageSize;
            matrix.postScale(f, f, i2 / 2.0f, i2 / 2.0f);
        }
    }

    /* loaded from: classes.dex */
    public class DisabledElevationAnimation extends ShadowAnimatorImpl {
        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.ShadowAnimatorImpl
        public final float getTargetShadowSize() {
            return 0.0f;
        }

        public DisabledElevationAnimation(FloatingActionButtonImplLollipop floatingActionButtonImplLollipop) {
            super(floatingActionButtonImplLollipop);
        }
    }
}
