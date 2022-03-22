package androidx.dynamicanimation.animation;

import android.util.AndroidRuntimeException;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.dynamicanimation.animation.AnimationHandler;
import androidx.dynamicanimation.animation.DynamicAnimation;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public abstract class DynamicAnimation<T extends DynamicAnimation<T>> implements AnimationHandler.AnimationFrameCallback {
    public AnimationHandler mAnimationHandler;
    public float mMinVisibleChange;
    public final FloatPropertyCompat mProperty;
    public final Object mTarget;
    public static final AnonymousClass1 TRANSLATION_X = new ViewProperty() { // from class: androidx.dynamicanimation.animation.DynamicAnimation.1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(View view) {
            return view.getTranslationX();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(View view, float f) {
            view.setTranslationX(f);
        }
    };
    public static final AnonymousClass2 TRANSLATION_Y = new ViewProperty() { // from class: androidx.dynamicanimation.animation.DynamicAnimation.2
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(View view) {
            return view.getTranslationY();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(View view, float f) {
            view.setTranslationY(f);
        }
    };
    public static final AnonymousClass3 TRANSLATION_Z = new ViewProperty() { // from class: androidx.dynamicanimation.animation.DynamicAnimation.3
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(View view) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            return ViewCompat.Api21Impl.getTranslationZ(view);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(View view, float f) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api21Impl.setTranslationZ(view, f);
        }
    };
    public static final AnonymousClass4 SCALE_X = new ViewProperty() { // from class: androidx.dynamicanimation.animation.DynamicAnimation.4
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(View view) {
            return view.getScaleX();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(View view, float f) {
            view.setScaleX(f);
        }
    };
    public static final AnonymousClass5 SCALE_Y = new ViewProperty() { // from class: androidx.dynamicanimation.animation.DynamicAnimation.5
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(View view) {
            return view.getScaleY();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(View view, float f) {
            view.setScaleY(f);
        }
    };
    public static final AnonymousClass6 ROTATION = new ViewProperty() { // from class: androidx.dynamicanimation.animation.DynamicAnimation.6
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(View view) {
            return view.getRotation();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(View view, float f) {
            view.setRotation(f);
        }
    };
    public static final AnonymousClass7 ROTATION_X = new ViewProperty() { // from class: androidx.dynamicanimation.animation.DynamicAnimation.7
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(View view) {
            return view.getRotationX();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(View view, float f) {
            view.setRotationX(f);
        }
    };
    public static final AnonymousClass8 ROTATION_Y = new ViewProperty() { // from class: androidx.dynamicanimation.animation.DynamicAnimation.8
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(View view) {
            return view.getRotationY();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(View view, float f) {
            view.setRotationY(f);
        }
    };
    public static final AnonymousClass10 Y = new ViewProperty() { // from class: androidx.dynamicanimation.animation.DynamicAnimation.10
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(View view) {
            return view.getY();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(View view, float f) {
            view.setY(f);
        }
    };
    public static final AnonymousClass12 ALPHA = new ViewProperty() { // from class: androidx.dynamicanimation.animation.DynamicAnimation.12
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(View view) {
            return view.getAlpha();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(View view, float f) {
            view.setAlpha(f);
        }
    };
    public static final AnonymousClass13 SCROLL_X = new ViewProperty() { // from class: androidx.dynamicanimation.animation.DynamicAnimation.13
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(View view) {
            return view.getScrollX();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(View view, float f) {
            view.setScrollX((int) f);
        }
    };
    public static final AnonymousClass14 SCROLL_Y = new ViewProperty() { // from class: androidx.dynamicanimation.animation.DynamicAnimation.14
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(View view) {
            return view.getScrollY();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(View view, float f) {
            view.setScrollY((int) f);
        }
    };
    public float mVelocity = 0.0f;
    public float mValue = Float.MAX_VALUE;
    public boolean mStartValueIsSet = false;
    public boolean mRunning = false;
    public float mMaxValue = Float.MAX_VALUE;
    public float mMinValue = -3.4028235E38f;
    public long mLastFrameTime = 0;
    public final ArrayList<OnAnimationEndListener> mEndListeners = new ArrayList<>();
    public final ArrayList<OnAnimationUpdateListener> mUpdateListeners = new ArrayList<>();

    /* loaded from: classes.dex */
    public static class MassState {
        public float mValue;
        public float mVelocity;
    }

    /* loaded from: classes.dex */
    public interface OnAnimationEndListener {
        void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2);
    }

    /* loaded from: classes.dex */
    public interface OnAnimationUpdateListener {
        void onAnimationUpdate(float f, float f2);
    }

    /* loaded from: classes.dex */
    public static abstract class ViewProperty extends FloatPropertyCompat<View> {
    }

    public final void endAnimationInternal(boolean z) {
        this.mRunning = false;
        AnimationHandler animationHandler = getAnimationHandler();
        Objects.requireNonNull(animationHandler);
        animationHandler.mDelayedCallbackStartTime.remove(this);
        int indexOf = animationHandler.mAnimationCallbacks.indexOf(this);
        if (indexOf >= 0) {
            animationHandler.mAnimationCallbacks.set(indexOf, null);
            animationHandler.mListDirty = true;
        }
        this.mLastFrameTime = 0L;
        this.mStartValueIsSet = false;
        for (int i = 0; i < this.mEndListeners.size(); i++) {
            if (this.mEndListeners.get(i) != null) {
                this.mEndListeners.get(i).onAnimationEnd(this, z, this.mValue, this.mVelocity);
            }
        }
        ArrayList<OnAnimationEndListener> arrayList = this.mEndListeners;
        int size = arrayList.size();
        while (true) {
            size--;
            if (size < 0) {
                return;
            }
            if (arrayList.get(size) == null) {
                arrayList.remove(size);
            }
        }
    }

    public abstract boolean updateValueAndVelocity(long j);

    public final T addEndListener(OnAnimationEndListener onAnimationEndListener) {
        if (!this.mEndListeners.contains(onAnimationEndListener)) {
            this.mEndListeners.add(onAnimationEndListener);
        }
        return this;
    }

    @Override // androidx.dynamicanimation.animation.AnimationHandler.AnimationFrameCallback
    public final boolean doAnimationFrame(long j) {
        long j2 = this.mLastFrameTime;
        if (j2 == 0) {
            this.mLastFrameTime = j;
            setPropertyValue(this.mValue);
            return false;
        }
        this.mLastFrameTime = j;
        boolean updateValueAndVelocity = updateValueAndVelocity(j - j2);
        float min = Math.min(this.mValue, this.mMaxValue);
        this.mValue = min;
        float max = Math.max(min, this.mMinValue);
        this.mValue = max;
        setPropertyValue(max);
        if (updateValueAndVelocity) {
            endAnimationInternal(false);
        }
        return updateValueAndVelocity;
    }

    public final AnimationHandler getAnimationHandler() {
        if (this.mAnimationHandler == null) {
            ThreadLocal<AnimationHandler> threadLocal = AnimationHandler.sAnimatorHandler;
            if (threadLocal.get() == null) {
                threadLocal.set(new AnimationHandler(new AnimationHandler.FrameCallbackScheduler16()));
            }
            this.mAnimationHandler = threadLocal.get();
        }
        return this.mAnimationHandler;
    }

    public final void setPropertyValue(float f) {
        this.mProperty.setValue(this.mTarget, f);
        for (int i = 0; i < this.mUpdateListeners.size(); i++) {
            if (this.mUpdateListeners.get(i) != null) {
                this.mUpdateListeners.get(i).onAnimationUpdate(this.mValue, this.mVelocity);
            }
        }
        ArrayList<OnAnimationUpdateListener> arrayList = this.mUpdateListeners;
        int size = arrayList.size();
        while (true) {
            size--;
            if (size < 0) {
                return;
            }
            if (arrayList.get(size) == null) {
                arrayList.remove(size);
            }
        }
    }

    public <K> DynamicAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat) {
        this.mTarget = k;
        this.mProperty = floatPropertyCompat;
        if (floatPropertyCompat == ROTATION || floatPropertyCompat == ROTATION_X || floatPropertyCompat == ROTATION_Y) {
            this.mMinVisibleChange = 0.1f;
        } else if (floatPropertyCompat == ALPHA) {
            this.mMinVisibleChange = 0.00390625f;
        } else if (floatPropertyCompat == SCALE_X || floatPropertyCompat == SCALE_Y) {
            this.mMinVisibleChange = 0.002f;
        } else {
            this.mMinVisibleChange = 1.0f;
        }
    }

    public void cancel() {
        AnimationHandler animationHandler = getAnimationHandler();
        Objects.requireNonNull(animationHandler);
        if (!animationHandler.mScheduler.isCurrentThread()) {
            throw new AndroidRuntimeException("Animations may only be canceled from the same thread as the animation handler");
        } else if (this.mRunning) {
            endAnimationInternal(true);
        }
    }

    public void start() {
        AnimationHandler animationHandler = getAnimationHandler();
        Objects.requireNonNull(animationHandler);
        if (animationHandler.mScheduler.isCurrentThread()) {
            boolean z = this.mRunning;
            if (!z && !z) {
                this.mRunning = true;
                if (!this.mStartValueIsSet) {
                    this.mValue = this.mProperty.getValue(this.mTarget);
                }
                float f = this.mValue;
                if (f > this.mMaxValue || f < this.mMinValue) {
                    throw new IllegalArgumentException("Starting value need to be in between min value and max value");
                }
                AnimationHandler animationHandler2 = getAnimationHandler();
                Objects.requireNonNull(animationHandler2);
                if (animationHandler2.mAnimationCallbacks.size() == 0) {
                    animationHandler2.mScheduler.postFrameCallback(animationHandler2.mRunnable);
                }
                if (!animationHandler2.mAnimationCallbacks.contains(this)) {
                    animationHandler2.mAnimationCallbacks.add(this);
                    return;
                }
                return;
            }
            return;
        }
        throw new AndroidRuntimeException("Animations may only be started on the same thread as the animation handler");
    }
}
