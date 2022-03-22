package com.android.wm.shell.animation;

import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.Log;
import androidx.dynamicanimation.animation.AnimationHandler;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import com.android.wm.shell.animation.PhysicsAnimator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__ReversedViewsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes.dex */
public /* synthetic */ class PhysicsAnimator$startAction$1 extends FunctionReferenceImpl implements Function0<Unit> {
    public PhysicsAnimator$startAction$1(Object obj) {
        super(0, obj, PhysicsAnimator.class, "startInternal", "startInternal$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell()V", 0);
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        final PhysicsAnimator physicsAnimator = (PhysicsAnimator) this.receiver;
        Objects.requireNonNull(physicsAnimator);
        Object obj = physicsAnimator.weakTarget.get();
        if (obj == null) {
            Log.w("PhysicsAnimator", "Trying to animate a GC-ed object.");
        } else {
            ArrayList arrayList = new ArrayList();
            Set keySet = physicsAnimator.springConfigs.keySet();
            Set keySet2 = physicsAnimator.flingConfigs.keySet();
            Set<FloatPropertyCompat> mutableSet = CollectionsKt___CollectionsKt.toMutableSet(keySet);
            CollectionsKt__ReversedViewsKt.addAll(mutableSet, keySet2);
            for (final FloatPropertyCompat floatPropertyCompat : mutableSet) {
                PhysicsAnimator.FlingConfig flingConfig = physicsAnimator.flingConfigs.get(floatPropertyCompat);
                final PhysicsAnimator.SpringConfig springConfig = physicsAnimator.springConfigs.get(floatPropertyCompat);
                float value = floatPropertyCompat.getValue(obj);
                if (flingConfig != null) {
                    arrayList.add(new PhysicsAnimator$startInternal$1(flingConfig, physicsAnimator, floatPropertyCompat, obj, value));
                }
                if (springConfig != null) {
                    if (flingConfig == null) {
                        ArrayMap<FloatPropertyCompat<? super T>, SpringAnimation> arrayMap = physicsAnimator.springAnimations;
                        Object obj2 = arrayMap.get(floatPropertyCompat);
                        Object obj3 = obj2;
                        if (obj2 == null) {
                            SpringAnimation springAnimation = new SpringAnimation(obj, floatPropertyCompat);
                            PhysicsAnimator$configureDynamicAnimation$1 physicsAnimator$configureDynamicAnimation$1 = new PhysicsAnimator$configureDynamicAnimation$1(physicsAnimator, floatPropertyCompat);
                            if (!springAnimation.mRunning) {
                                if (!springAnimation.mUpdateListeners.contains(physicsAnimator$configureDynamicAnimation$1)) {
                                    springAnimation.mUpdateListeners.add(physicsAnimator$configureDynamicAnimation$1);
                                }
                                springAnimation.addEndListener(new PhysicsAnimator$configureDynamicAnimation$2(physicsAnimator, floatPropertyCompat, springAnimation));
                                arrayMap.put(floatPropertyCompat, springAnimation);
                                obj3 = springAnimation;
                            } else {
                                throw new UnsupportedOperationException("Error: Update listeners must be added beforethe animation.");
                            }
                        }
                        SpringAnimation springAnimation2 = (SpringAnimation) obj3;
                        if (physicsAnimator.customAnimationHandler != null && !Intrinsics.areEqual(springAnimation2.getAnimationHandler(), physicsAnimator.customAnimationHandler)) {
                            if (springAnimation2.mRunning) {
                                physicsAnimator.cancel(floatPropertyCompat);
                            }
                            AnimationHandler animationHandler = physicsAnimator.customAnimationHandler;
                            if (animationHandler == null) {
                                animationHandler = springAnimation2.getAnimationHandler();
                            }
                            if (!springAnimation2.mRunning) {
                                springAnimation2.mAnimationHandler = animationHandler;
                            } else {
                                throw new AndroidRuntimeException("Animations are still running and the animationhandler should not be set at this timming");
                            }
                        }
                        springConfig.applyToAnimation$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(springAnimation2);
                        arrayList.add(new PhysicsAnimator$startInternal$2(springAnimation2));
                    } else {
                        final float f = flingConfig.min;
                        final float f2 = flingConfig.max;
                        physicsAnimator.endListeners.add(0, new PhysicsAnimator.EndListener<Object>() { // from class: com.android.wm.shell.animation.PhysicsAnimator$startInternal$3
                            @Override // com.android.wm.shell.animation.PhysicsAnimator.EndListener
                            public final void onAnimationEnd(Object obj4, FloatPropertyCompat floatPropertyCompat2, boolean z, boolean z2, float f3, float f4) {
                                boolean z3;
                                boolean z4;
                                float f5;
                                if (Intrinsics.areEqual(floatPropertyCompat2, floatPropertyCompat) && z && !z2) {
                                    boolean z5 = true;
                                    if (Math.abs(f4) > 0.0f) {
                                        z3 = true;
                                    } else {
                                        z3 = false;
                                    }
                                    if (f > f3 || f3 > f2) {
                                        z4 = false;
                                    } else {
                                        z4 = true;
                                    }
                                    boolean z6 = !z4;
                                    if (z3 || z6) {
                                        PhysicsAnimator.SpringConfig springConfig2 = springConfig;
                                        Objects.requireNonNull(springConfig2);
                                        springConfig2.startVelocity = f4;
                                        PhysicsAnimator.SpringConfig springConfig3 = springConfig;
                                        Objects.requireNonNull(springConfig3);
                                        float f6 = springConfig3.finalPosition;
                                        WeakHashMap<Object, PhysicsAnimator<?>> weakHashMap = PhysicsAnimatorKt.animators;
                                        if (f6 != -3.4028235E38f) {
                                            z5 = false;
                                        }
                                        if (z5) {
                                            if (z3) {
                                                PhysicsAnimator.SpringConfig springConfig4 = springConfig;
                                                if (f4 < 0.0f) {
                                                    f5 = f;
                                                } else {
                                                    f5 = f2;
                                                }
                                                Objects.requireNonNull(springConfig4);
                                                springConfig4.finalPosition = f5;
                                            } else if (z6) {
                                                PhysicsAnimator.SpringConfig springConfig5 = springConfig;
                                                float f7 = f;
                                                if (f3 >= f7) {
                                                    f7 = f2;
                                                }
                                                Objects.requireNonNull(springConfig5);
                                                springConfig5.finalPosition = f7;
                                            }
                                        }
                                        PhysicsAnimator<Object> physicsAnimator2 = physicsAnimator;
                                        FloatPropertyCompat<? super Object> floatPropertyCompat3 = floatPropertyCompat;
                                        Objects.requireNonNull(physicsAnimator2);
                                        ArrayMap<FloatPropertyCompat<? super Object>, SpringAnimation> arrayMap2 = physicsAnimator2.springAnimations;
                                        SpringAnimation springAnimation3 = arrayMap2.get(floatPropertyCompat3);
                                        if (springAnimation3 == null) {
                                            springAnimation3 = new SpringAnimation(obj4, floatPropertyCompat3);
                                            PhysicsAnimator$configureDynamicAnimation$1 physicsAnimator$configureDynamicAnimation$12 = new PhysicsAnimator$configureDynamicAnimation$1(physicsAnimator2, floatPropertyCompat3);
                                            if (!springAnimation3.mRunning) {
                                                if (!springAnimation3.mUpdateListeners.contains(physicsAnimator$configureDynamicAnimation$12)) {
                                                    springAnimation3.mUpdateListeners.add(physicsAnimator$configureDynamicAnimation$12);
                                                }
                                                springAnimation3.addEndListener(new PhysicsAnimator$configureDynamicAnimation$2(physicsAnimator2, floatPropertyCompat3, springAnimation3));
                                                arrayMap2.put(floatPropertyCompat3, springAnimation3);
                                            } else {
                                                throw new UnsupportedOperationException("Error: Update listeners must be added beforethe animation.");
                                            }
                                        }
                                        SpringAnimation springAnimation4 = springAnimation3;
                                        AnimationHandler animationHandler2 = physicsAnimator.customAnimationHandler;
                                        if (animationHandler2 == null) {
                                            animationHandler2 = springAnimation4.getAnimationHandler();
                                        }
                                        if (!springAnimation4.mRunning) {
                                            springAnimation4.mAnimationHandler = animationHandler2;
                                            springConfig.applyToAnimation$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(springAnimation4);
                                            springAnimation4.start();
                                            return;
                                        }
                                        throw new AndroidRuntimeException("Animations are still running and the animationhandler should not be set at this timming");
                                    }
                                }
                            }
                        });
                    }
                }
            }
            ArrayList<PhysicsAnimator<T>.InternalListener> arrayList2 = physicsAnimator.internalListeners;
            Set keySet3 = physicsAnimator.springConfigs.keySet();
            Set keySet4 = physicsAnimator.flingConfigs.keySet();
            Set mutableSet2 = CollectionsKt___CollectionsKt.toMutableSet(keySet3);
            CollectionsKt__ReversedViewsKt.addAll(mutableSet2, keySet4);
            arrayList2.add(new PhysicsAnimator.InternalListener(obj, mutableSet2, new ArrayList(physicsAnimator.updateListeners), new ArrayList(physicsAnimator.endListeners), new ArrayList(physicsAnimator.endActions)));
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((Function0) it.next()).invoke();
            }
            physicsAnimator.springConfigs.clear();
            physicsAnimator.flingConfigs.clear();
            physicsAnimator.updateListeners.clear();
            physicsAnimator.endListeners.clear();
            physicsAnimator.endActions.clear();
        }
        return Unit.INSTANCE;
    }
}
