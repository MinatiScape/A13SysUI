package com.android.wm.shell.animation;

import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import androidx.dynamicanimation.animation.AnimationHandler;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.wm.shell.animation.PhysicsAnimator;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes.dex */
public final class PhysicsAnimator$startInternal$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ FloatPropertyCompat<Object> $animatedProperty;
    public final /* synthetic */ float $currentValue;
    public final /* synthetic */ PhysicsAnimator.FlingConfig $flingConfig;
    public final /* synthetic */ Object $target;
    public final /* synthetic */ PhysicsAnimator<Object> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PhysicsAnimator$startInternal$1(PhysicsAnimator.FlingConfig flingConfig, PhysicsAnimator<Object> physicsAnimator, FloatPropertyCompat<Object> floatPropertyCompat, Object obj, float f) {
        super(0);
        this.$flingConfig = flingConfig;
        this.this$0 = physicsAnimator;
        this.$animatedProperty = floatPropertyCompat;
        this.$target = obj;
        this.$currentValue = f;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        PhysicsAnimator.FlingConfig flingConfig = this.$flingConfig;
        float f = this.$currentValue;
        Objects.requireNonNull(flingConfig);
        flingConfig.min = Math.min(f, flingConfig.min);
        flingConfig.max = Math.max(f, flingConfig.max);
        this.this$0.cancel(this.$animatedProperty);
        PhysicsAnimator<Object> physicsAnimator = this.this$0;
        FloatPropertyCompat<? super Object> floatPropertyCompat = this.$animatedProperty;
        Object obj = this.$target;
        Objects.requireNonNull(physicsAnimator);
        ArrayMap<FloatPropertyCompat<? super Object>, FlingAnimation> arrayMap = physicsAnimator.flingAnimations;
        FlingAnimation flingAnimation = arrayMap.get(floatPropertyCompat);
        if (flingAnimation == null) {
            flingAnimation = new FlingAnimation(obj, floatPropertyCompat);
            PhysicsAnimator$configureDynamicAnimation$1 physicsAnimator$configureDynamicAnimation$1 = new PhysicsAnimator$configureDynamicAnimation$1(physicsAnimator, floatPropertyCompat);
            if (!flingAnimation.mRunning) {
                if (!flingAnimation.mUpdateListeners.contains(physicsAnimator$configureDynamicAnimation$1)) {
                    flingAnimation.mUpdateListeners.add(physicsAnimator$configureDynamicAnimation$1);
                }
                flingAnimation.addEndListener(new PhysicsAnimator$configureDynamicAnimation$2(physicsAnimator, floatPropertyCompat, flingAnimation));
                arrayMap.put(floatPropertyCompat, flingAnimation);
            } else {
                throw new UnsupportedOperationException("Error: Update listeners must be added beforethe animation.");
            }
        }
        FlingAnimation flingAnimation2 = flingAnimation;
        AnimationHandler animationHandler = this.this$0.customAnimationHandler;
        if (animationHandler == null) {
            animationHandler = flingAnimation2.getAnimationHandler();
        }
        if (!flingAnimation2.mRunning) {
            flingAnimation2.mAnimationHandler = animationHandler;
            PhysicsAnimator.FlingConfig flingConfig2 = this.$flingConfig;
            Objects.requireNonNull(flingConfig2);
            float f2 = flingConfig2.friction;
            if (f2 > 0.0f) {
                FlingAnimation.DragForce dragForce = flingAnimation2.mFlingForce;
                Objects.requireNonNull(dragForce);
                dragForce.mFriction = f2 * (-4.2f);
                flingAnimation2.mMinValue = flingConfig2.min;
                flingAnimation2.mMaxValue = flingConfig2.max;
                flingAnimation2.mVelocity = flingConfig2.startVelocity;
                flingAnimation2.start();
                return Unit.INSTANCE;
            }
            throw new IllegalArgumentException("Friction must be positive");
        }
        throw new AndroidRuntimeException("Animations are still running and the animationhandler should not be set at this timming");
    }
}
