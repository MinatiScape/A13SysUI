package com.android.wm.shell.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.wm.shell.animation.PhysicsAnimator;
import java.util.Objects;
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes.dex */
public final class PhysicsAnimator$configureDynamicAnimation$1 implements DynamicAnimation.OnAnimationUpdateListener {
    public final /* synthetic */ FloatPropertyCompat<Object> $property;
    public final /* synthetic */ PhysicsAnimator<Object> this$0;

    public PhysicsAnimator$configureDynamicAnimation$1(PhysicsAnimator<Object> physicsAnimator, FloatPropertyCompat<Object> floatPropertyCompat) {
        this.this$0 = physicsAnimator;
        this.$property = floatPropertyCompat;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
    public final void onAnimationUpdate(float f, float f2) {
        PhysicsAnimator<Object> physicsAnimator = this.this$0;
        Objects.requireNonNull(physicsAnimator);
        int size = physicsAnimator.internalListeners.size();
        int i = 0;
        while (i < size) {
            int i2 = i + 1;
            PhysicsAnimator<Object> physicsAnimator2 = this.this$0;
            Objects.requireNonNull(physicsAnimator2);
            PhysicsAnimator<Object>.InternalListener internalListener = physicsAnimator2.internalListeners.get(i);
            FloatPropertyCompat<? super Object> floatPropertyCompat = this.$property;
            Objects.requireNonNull(internalListener);
            if (internalListener.properties.contains(floatPropertyCompat)) {
                internalListener.undispatchedUpdates.put(floatPropertyCompat, new PhysicsAnimator.AnimationUpdate(f, f2));
                internalListener.maybeDispatchUpdates();
            }
            i = i2;
        }
    }
}
