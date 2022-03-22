package com.google.android.systemui.elmyra.feedback;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
/* loaded from: classes.dex */
public class PoodleOrbView extends FrameLayout implements Animator.AnimatorListener, FeedbackEffect {
    public View mBackground;
    public View mBlue;
    public int mFeedbackHeight;
    public View mGreen;
    public View mRed;
    public View mYellow;
    public int mState = 0;
    public ArrayList<ValueAnimator> mAnimations = new ArrayList<>();

    public PoodleOrbView(Context context) {
        super(context);
    }

    @Override // android.animation.Animator.AnimatorListener
    public final void onAnimationCancel(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public final void onAnimationEnd(Animator animator) {
        this.mState = 0;
        onProgress(0.0f, 0);
    }

    @Override // android.animation.Animator.AnimatorListener
    public final void onAnimationRepeat(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public final void onAnimationStart(Animator animator) {
    }

    public final ObjectAnimator[] createDotAnimator(View view, float f, Path path) {
        Keyframe[] keyframeArr = {Keyframe.ofFloat(0.0f, view.getScaleX()), Keyframe.ofFloat(0.75f, view.getScaleX()), Keyframe.ofFloat(0.95f, 0.3f), Keyframe.ofFloat(1.0f, 0.0f)};
        float f2 = 0.25f;
        Keyframe[] keyframeArr2 = {Keyframe.ofFloat(0.0f, 1.0f), Keyframe.ofFloat(0.75f, 1.0f), Keyframe.ofFloat(0.95f, 0.25f), Keyframe.ofFloat(1.0f, 0.0f)};
        float[] approximate = path.approximate(0.5f);
        Keyframe[] keyframeArr3 = new Keyframe[approximate.length / 3];
        Keyframe[] keyframeArr4 = new Keyframe[approximate.length / 3];
        int i = 0;
        int i2 = 0;
        while (i < approximate.length) {
            int i3 = i + 1;
            float f3 = (approximate[i] * f2) + 0.75f;
            int i4 = i3 + 1;
            keyframeArr3[i2] = Keyframe.ofFloat(f3, approximate[i3]);
            keyframeArr4[i2] = Keyframe.ofFloat(f3, approximate[i4]);
            i2++;
            i = i4 + 1;
            f2 = 0.25f;
        }
        Keyframe[][] keyframeArr5 = {keyframeArr3, keyframeArr4};
        Keyframe[] keyframeArr6 = new Keyframe[keyframeArr5[0].length + 2];
        keyframeArr6[0] = Keyframe.ofFloat(0.0f, view.getTranslationX());
        keyframeArr6[1] = Keyframe.ofFloat(0.75f, view.getTranslationX());
        System.arraycopy(keyframeArr5[0], 0, keyframeArr6, 2, keyframeArr5[0].length);
        Keyframe[] keyframeArr7 = new Keyframe[keyframeArr5[1].length + 3];
        keyframeArr7[0] = Keyframe.ofFloat(0.0f, view.getTranslationY());
        keyframeArr7[1] = Keyframe.ofFloat(f, view.getTranslationY());
        keyframeArr7[2] = Keyframe.ofFloat(0.75f, view.getTranslationY() - this.mFeedbackHeight);
        System.arraycopy(keyframeArr5[1], 0, keyframeArr7, 3, keyframeArr5[1].length);
        keyframeArr7[2].setInterpolator(new OvershootInterpolator());
        ObjectAnimator[] objectAnimatorArr = {ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofKeyframe(View.SCALE_X, keyframeArr)), ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofKeyframe(View.SCALE_Y, keyframeArr)), ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X, keyframeArr6)), ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofKeyframe(View.TRANSLATION_Y, keyframeArr7)), ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofKeyframe(View.ALPHA, keyframeArr2))};
        for (int i5 = 0; i5 < 5; i5++) {
            objectAnimatorArr[i5].setDuration(1000L);
        }
        return objectAnimatorArr;
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mBackground = findViewById(2131427900);
        this.mBlue = findViewById(2131427901);
        this.mGreen = findViewById(2131427902);
        this.mRed = findViewById(2131427903);
        this.mYellow = findViewById(2131427905);
        this.mFeedbackHeight = getResources().getDimensionPixelSize(2131166717);
        this.mBackground.setScaleX(0.0f);
        this.mBackground.setScaleY(0.0f);
        View view = this.mBlue;
        view.setTranslationY(view.getTranslationY() + this.mFeedbackHeight);
        View view2 = this.mGreen;
        view2.setTranslationY(view2.getTranslationY() + this.mFeedbackHeight);
        View view3 = this.mRed;
        view3.setTranslationY(view3.getTranslationY() + this.mFeedbackHeight);
        View view4 = this.mYellow;
        view4.setTranslationY(view4.getTranslationY() + this.mFeedbackHeight);
        ArrayList<ValueAnimator> arrayList = this.mAnimations;
        View view5 = this.mBackground;
        Keyframe[] keyframeArr = {Keyframe.ofFloat(0.0f, 0.0f), Keyframe.ofFloat(0.375f, 1.2f), Keyframe.ofFloat(0.75f, 1.2f), Keyframe.ofFloat(0.95f, 0.2f), Keyframe.ofFloat(1.0f, 0.0f)};
        keyframeArr[1].setInterpolator(new OvershootInterpolator());
        ObjectAnimator[] objectAnimatorArr = {ObjectAnimator.ofPropertyValuesHolder(view5, PropertyValuesHolder.ofKeyframe(View.SCALE_X, keyframeArr)), ObjectAnimator.ofPropertyValuesHolder(view5, PropertyValuesHolder.ofKeyframe(View.SCALE_Y, keyframeArr)), ObjectAnimator.ofPropertyValuesHolder(view5, PropertyValuesHolder.ofKeyframe(View.TRANSLATION_Y, Keyframe.ofFloat(0.0f, view5.getTranslationY()), Keyframe.ofFloat(0.375f, px(27.5f)), Keyframe.ofFloat(0.75f, px(27.5f)), Keyframe.ofFloat(0.95f, px(21.75f))))};
        for (int i = 0; i < 3; i++) {
            objectAnimatorArr[i].setDuration(1000L);
        }
        arrayList.addAll(Arrays.asList(objectAnimatorArr));
        this.mAnimations.get(0).addListener(this);
        Path path = new Path();
        path.moveTo(this.mBlue.getTranslationX(), this.mBlue.getTranslationY() - this.mFeedbackHeight);
        path.cubicTo(px(-32.5f), px(-27.5f), px(15.0f), px(-33.75f), px(-2.5f), px(-20.0f));
        this.mAnimations.addAll(Arrays.asList(createDotAnimator(this.mBlue, 0.0f, path)));
        Path path2 = new Path();
        path2.moveTo(this.mRed.getTranslationX(), this.mRed.getTranslationY() - this.mFeedbackHeight);
        path2.cubicTo(px(-25.0f), px(-17.5f), px(-20.0f), px(-27.5f), px(2.5f), px(-20.0f));
        this.mAnimations.addAll(Arrays.asList(createDotAnimator(this.mRed, 0.05f, path2)));
        Path path3 = new Path();
        path3.moveTo(this.mYellow.getTranslationX(), this.mYellow.getTranslationY() - this.mFeedbackHeight);
        path3.cubicTo(px(21.25f), px(-33.75f), px(15.0f), px(-27.5f), px(0.0f), px(-20.0f));
        this.mAnimations.addAll(Arrays.asList(createDotAnimator(this.mYellow, 0.1f, path3)));
        Path path4 = new Path();
        path4.moveTo(this.mGreen.getTranslationX(), this.mGreen.getTranslationY() - this.mFeedbackHeight);
        path4.cubicTo(px(-27.5f), px(-20.0f), px(35.0f), px(-30.0f), px(0.0f), px(-20.0f));
        this.mAnimations.addAll(Arrays.asList(createDotAnimator(this.mGreen, 0.2f, path4)));
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onProgress(float f, int i) {
        if (this.mState != 3) {
            float f2 = (0.75f * f) + 0.0f;
            Iterator<ValueAnimator> it = this.mAnimations.iterator();
            while (it.hasNext()) {
                ValueAnimator next = it.next();
                next.cancel();
                next.setCurrentFraction(f2);
            }
            if (f == 0.0f) {
                this.mState = 0;
            } else if (f == 1.0f) {
                this.mState = 2;
            } else {
                this.mState = 1;
            }
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onRelease() {
        int i = this.mState;
        if (i == 2 || i == 1) {
            Iterator<ValueAnimator> it = this.mAnimations.iterator();
            while (it.hasNext()) {
                it.next().reverse();
            }
            this.mState = 0;
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        Iterator<ValueAnimator> it = this.mAnimations.iterator();
        while (it.hasNext()) {
            it.next().start();
        }
        this.mState = 3;
    }

    public final float px(float f) {
        return TypedValue.applyDimension(1, f, getResources().getDisplayMetrics());
    }

    public PoodleOrbView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PoodleOrbView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public PoodleOrbView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }
}
