package com.android.wm.shell.animation;

import android.util.ArrayMap;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.wm.shell.animation.PhysicsAnimator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt__ReversedViewsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes.dex */
public final class PhysicsAnimator$configureDynamicAnimation$2 implements DynamicAnimation.OnAnimationEndListener {
    public final /* synthetic */ DynamicAnimation<?> $anim;
    public final /* synthetic */ FloatPropertyCompat<Object> $property;
    public final /* synthetic */ PhysicsAnimator<Object> this$0;

    /* compiled from: PhysicsAnimator.kt */
    /* renamed from: com.android.wm.shell.animation.PhysicsAnimator$configureDynamicAnimation$2$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends Lambda implements Function1<PhysicsAnimator<Object>.InternalListener, Boolean> {
        public final /* synthetic */ DynamicAnimation<?> $anim;
        public final /* synthetic */ boolean $canceled;
        public final /* synthetic */ FloatPropertyCompat<Object> $property;
        public final /* synthetic */ float $value;
        public final /* synthetic */ float $velocity;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(FloatPropertyCompat<Object> floatPropertyCompat, boolean z, float f, float f2, DynamicAnimation<?> dynamicAnimation) {
            super(1);
            this.$property = floatPropertyCompat;
            this.$canceled = z;
            this.$value = f;
            this.$velocity = f2;
            this.$anim = dynamicAnimation;
        }

        @Override // kotlin.jvm.functions.Function1
        public final Boolean invoke(PhysicsAnimator<Object>.InternalListener internalListener) {
            boolean z;
            PhysicsAnimator<Object>.InternalListener internalListener2 = internalListener;
            FloatPropertyCompat<? super Object> floatPropertyCompat = this.$property;
            boolean z2 = this.$canceled;
            float f = this.$value;
            float f2 = this.$velocity;
            boolean z3 = this.$anim instanceof FlingAnimation;
            if (!internalListener2.properties.contains(floatPropertyCompat)) {
                z = false;
                break;
            }
            internalListener2.numPropertiesAnimating--;
            internalListener2.maybeDispatchUpdates();
            if (internalListener2.undispatchedUpdates.containsKey(floatPropertyCompat)) {
                Iterator<T> it = internalListener2.updateListeners.iterator();
                while (it.hasNext()) {
                    Object obj = internalListener2.target;
                    new ArrayMap().put(floatPropertyCompat, internalListener2.undispatchedUpdates.get(floatPropertyCompat));
                    ((PhysicsAnimator.UpdateListener) it.next()).onAnimationUpdateForProperty(obj);
                }
                internalListener2.undispatchedUpdates.remove(floatPropertyCompat);
            }
            z = !PhysicsAnimator.this.arePropertiesAnimating(internalListener2.properties);
            List<? extends PhysicsAnimator.EndListener<Object>> list = internalListener2.endListeners;
            PhysicsAnimator<Object> physicsAnimator = PhysicsAnimator.this;
            Iterator<T> it2 = list.iterator();
            while (it2.hasNext()) {
                ((PhysicsAnimator.EndListener) it2.next()).onAnimationEnd(internalListener2.target, floatPropertyCompat, z3, z2, f, f2);
                if (physicsAnimator.isPropertyAnimating(floatPropertyCompat)) {
                    z = false;
                    break;
                }
            }
            if (z && !z2) {
                Iterator<T> it3 = internalListener2.endActions.iterator();
                while (it3.hasNext()) {
                    ((Function0) it3.next()).invoke();
                }
            }
            return Boolean.valueOf(z);
        }
    }

    public PhysicsAnimator$configureDynamicAnimation$2(PhysicsAnimator<Object> physicsAnimator, FloatPropertyCompat<Object> floatPropertyCompat, DynamicAnimation<?> dynamicAnimation) {
        this.this$0 = physicsAnimator;
        this.$property = floatPropertyCompat;
        this.$anim = dynamicAnimation;
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        PhysicsAnimator<Object> physicsAnimator = this.this$0;
        Objects.requireNonNull(physicsAnimator);
        CollectionsKt__ReversedViewsKt.removeAll(physicsAnimator.internalListeners, new AnonymousClass1(this.$property, z, f, f2, this.$anim));
        if (Intrinsics.areEqual(this.this$0.springAnimations.get(this.$property), this.$anim)) {
            this.this$0.springAnimations.remove(this.$property);
        }
        if (Intrinsics.areEqual(this.this$0.flingAnimations.get(this.$property), this.$anim)) {
            this.this$0.flingAnimations.remove(this.$property);
        }
    }
}
