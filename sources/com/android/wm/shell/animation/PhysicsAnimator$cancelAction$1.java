package com.android.wm.shell.animation;

import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import java.util.Objects;
import java.util.Set;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes.dex */
public /* synthetic */ class PhysicsAnimator$cancelAction$1 extends FunctionReferenceImpl implements Function1<Set<? extends FloatPropertyCompat<? super T>>, Unit> {
    public PhysicsAnimator$cancelAction$1(Object obj) {
        super(1, obj, PhysicsAnimator.class, "cancelInternal", "cancelInternal$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(Ljava/util/Set;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(Object obj) {
        PhysicsAnimator physicsAnimator = (PhysicsAnimator) this.receiver;
        Objects.requireNonNull(physicsAnimator);
        for (FloatPropertyCompat floatPropertyCompat : (Set) obj) {
            FlingAnimation flingAnimation = physicsAnimator.flingAnimations.get(floatPropertyCompat);
            if (flingAnimation != null) {
                flingAnimation.cancel();
            }
            SpringAnimation springAnimation = physicsAnimator.springAnimations.get(floatPropertyCompat);
            if (springAnimation != null) {
                springAnimation.cancel();
            }
        }
        return Unit.INSTANCE;
    }
}
