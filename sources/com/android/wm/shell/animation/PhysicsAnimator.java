package com.android.wm.shell.animation;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.ArrayMap;
import android.util.Log;
import androidx.dynamicanimation.animation.AnimationHandler;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt__ReversedViewsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes.dex */
public final class PhysicsAnimator<T> {
    public static Function1<Object, ? extends PhysicsAnimator<?>> instanceConstructor = PhysicsAnimator$Companion$instanceConstructor$1.INSTANCE;
    public Function1<? super Set<? extends FloatPropertyCompat<? super T>>, Unit> cancelAction;
    public AnimationHandler customAnimationHandler;
    public SpringConfig defaultSpring;
    public final ArrayList<Function0<Unit>> endActions;
    public final ArrayList<EndListener<T>> endListeners;
    public final ArrayMap<FloatPropertyCompat<? super T>, FlingAnimation> flingAnimations;
    public final ArrayMap<FloatPropertyCompat<? super T>, FlingConfig> flingConfigs;
    public ArrayList<PhysicsAnimator<T>.InternalListener> internalListeners;
    public final ArrayMap<FloatPropertyCompat<? super T>, SpringAnimation> springAnimations;
    public final ArrayMap<FloatPropertyCompat<? super T>, SpringConfig> springConfigs;
    public Function0<Unit> startAction;
    public final ArrayList<UpdateListener<T>> updateListeners;
    public final WeakReference<T> weakTarget;

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes.dex */
    public static final class AnimationUpdate {
        public final float value;
        public final float velocity;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof AnimationUpdate)) {
                return false;
            }
            AnimationUpdate animationUpdate = (AnimationUpdate) obj;
            return Intrinsics.areEqual(Float.valueOf(this.value), Float.valueOf(animationUpdate.value)) && Intrinsics.areEqual(Float.valueOf(this.velocity), Float.valueOf(animationUpdate.velocity));
        }

        public final int hashCode() {
            return Float.hashCode(this.velocity) + (Float.hashCode(this.value) * 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("AnimationUpdate(value=");
            m.append(this.value);
            m.append(", velocity=");
            m.append(this.velocity);
            m.append(')');
            return m.toString();
        }

        public AnimationUpdate(float f, float f2) {
            this.value = f;
            this.velocity = f2;
        }
    }

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public static PhysicsAnimator getInstance(Object obj) {
            WeakHashMap<Object, PhysicsAnimator<?>> weakHashMap = PhysicsAnimatorKt.animators;
            if (!weakHashMap.containsKey(obj)) {
                Objects.requireNonNull((PhysicsAnimator$Companion$instanceConstructor$1) PhysicsAnimator.instanceConstructor);
                weakHashMap.put(obj, new PhysicsAnimator<>(obj));
            }
            PhysicsAnimator<?> physicsAnimator = weakHashMap.get(obj);
            Objects.requireNonNull(physicsAnimator, "null cannot be cast to non-null type com.android.wm.shell.animation.PhysicsAnimator<T of com.android.wm.shell.animation.PhysicsAnimator.Companion.getInstance>");
            return physicsAnimator;
        }
    }

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes.dex */
    public interface EndListener<T> {
        void onAnimationEnd(Object obj, FloatPropertyCompat floatPropertyCompat, boolean z, boolean z2, float f, float f2);
    }

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes.dex */
    public static final class FlingConfig {
        public float friction;
        public float max;
        public float min;
        public float startVelocity;

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public FlingConfig() {
            /*
                r4 = this;
                com.android.wm.shell.animation.PhysicsAnimator$FlingConfig r0 = com.android.wm.shell.animation.PhysicsAnimatorKt.globalDefaultFling
                float r1 = r0.friction
                float r2 = r0.min
                float r0 = r0.max
                r3 = 0
                r4.<init>(r1, r2, r0, r3)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.animation.PhysicsAnimator.FlingConfig.<init>():void");
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FlingConfig)) {
                return false;
            }
            FlingConfig flingConfig = (FlingConfig) obj;
            return Intrinsics.areEqual(Float.valueOf(this.friction), Float.valueOf(flingConfig.friction)) && Intrinsics.areEqual(Float.valueOf(this.min), Float.valueOf(flingConfig.min)) && Intrinsics.areEqual(Float.valueOf(this.max), Float.valueOf(flingConfig.max)) && Intrinsics.areEqual(Float.valueOf(this.startVelocity), Float.valueOf(flingConfig.startVelocity));
        }

        public final int hashCode() {
            int hashCode = Float.hashCode(this.min);
            int hashCode2 = Float.hashCode(this.max);
            return Float.hashCode(this.startVelocity) + ((hashCode2 + ((hashCode + (Float.hashCode(this.friction) * 31)) * 31)) * 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("FlingConfig(friction=");
            m.append(this.friction);
            m.append(", min=");
            m.append(this.min);
            m.append(", max=");
            m.append(this.max);
            m.append(", startVelocity=");
            m.append(this.startVelocity);
            m.append(')');
            return m.toString();
        }

        public FlingConfig(float f, float f2, float f3, float f4) {
            this.friction = f;
            this.min = f2;
            this.max = f3;
            this.startVelocity = f4;
        }
    }

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes.dex */
    public final class InternalListener {
        public List<? extends Function0<Unit>> endActions;
        public List<? extends EndListener<T>> endListeners;
        public int numPropertiesAnimating;
        public Set<? extends FloatPropertyCompat<? super T>> properties;
        public final T target;
        public final ArrayMap<FloatPropertyCompat<? super T>, AnimationUpdate> undispatchedUpdates = new ArrayMap<>();
        public List<? extends UpdateListener<T>> updateListeners;

        /* JADX WARN: Multi-variable type inference failed */
        public InternalListener(Object obj, Set set, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3) {
            this.target = obj;
            this.properties = set;
            this.updateListeners = arrayList;
            this.endListeners = arrayList2;
            this.endActions = arrayList3;
            this.numPropertiesAnimating = set.size();
        }

        public final void maybeDispatchUpdates() {
            if (this.undispatchedUpdates.size() >= this.numPropertiesAnimating && this.undispatchedUpdates.size() > 0) {
                Iterator<T> it = this.updateListeners.iterator();
                while (it.hasNext()) {
                    T t = this.target;
                    new ArrayMap(this.undispatchedUpdates);
                    ((UpdateListener) it.next()).onAnimationUpdateForProperty(t);
                }
                this.undispatchedUpdates.clear();
            }
        }
    }

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes.dex */
    public static final class SpringConfig {
        public float dampingRatio;
        public float finalPosition;
        public float startVelocity;
        public float stiffness;

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public SpringConfig() {
            /*
                r2 = this;
                com.android.wm.shell.animation.PhysicsAnimator$SpringConfig r0 = com.android.wm.shell.animation.PhysicsAnimatorKt.globalDefaultSpring
                float r1 = r0.stiffness
                float r0 = r0.dampingRatio
                r2.<init>(r1, r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.animation.PhysicsAnimator.SpringConfig.<init>():void");
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof SpringConfig)) {
                return false;
            }
            SpringConfig springConfig = (SpringConfig) obj;
            return Intrinsics.areEqual(Float.valueOf(this.stiffness), Float.valueOf(springConfig.stiffness)) && Intrinsics.areEqual(Float.valueOf(this.dampingRatio), Float.valueOf(springConfig.dampingRatio)) && Intrinsics.areEqual(Float.valueOf(this.startVelocity), Float.valueOf(springConfig.startVelocity)) && Intrinsics.areEqual(Float.valueOf(this.finalPosition), Float.valueOf(springConfig.finalPosition));
        }

        public final int hashCode() {
            int hashCode = Float.hashCode(this.dampingRatio);
            int hashCode2 = Float.hashCode(this.startVelocity);
            return Float.hashCode(this.finalPosition) + ((hashCode2 + ((hashCode + (Float.hashCode(this.stiffness) * 31)) * 31)) * 31);
        }

        public final void applyToAnimation$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(SpringAnimation springAnimation) {
            boolean z;
            SpringForce springForce = springAnimation.mSpring;
            if (springForce == null) {
                springForce = new SpringForce();
            }
            springForce.setStiffness(this.stiffness);
            springForce.setDampingRatio(this.dampingRatio);
            springForce.mFinalPosition = this.finalPosition;
            springAnimation.mSpring = springForce;
            float f = this.startVelocity;
            if (f == 0.0f) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                springAnimation.mVelocity = f;
            }
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("SpringConfig(stiffness=");
            m.append(this.stiffness);
            m.append(", dampingRatio=");
            m.append(this.dampingRatio);
            m.append(", startVelocity=");
            m.append(this.startVelocity);
            m.append(", finalPosition=");
            m.append(this.finalPosition);
            m.append(')');
            return m.toString();
        }

        /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
        public SpringConfig(float f, float f2) {
            this(f, f2, 0.0f, -3.4028235E38f);
            WeakHashMap<Object, PhysicsAnimator<?>> weakHashMap = PhysicsAnimatorKt.animators;
        }

        public SpringConfig(float f, float f2, float f3, float f4) {
            this.stiffness = f;
            this.dampingRatio = f2;
            this.startVelocity = f3;
            this.finalPosition = f4;
        }
    }

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes.dex */
    public interface UpdateListener<T> {
        void onAnimationUpdateForProperty(Object obj);
    }

    public PhysicsAnimator() {
        throw null;
    }

    public PhysicsAnimator(Object obj) {
        this.weakTarget = new WeakReference<>(obj);
        this.springAnimations = new ArrayMap<>();
        this.flingAnimations = new ArrayMap<>();
        this.springConfigs = new ArrayMap<>();
        this.flingConfigs = new ArrayMap<>();
        this.updateListeners = new ArrayList<>();
        this.endListeners = new ArrayList<>();
        this.endActions = new ArrayList<>();
        this.defaultSpring = PhysicsAnimatorKt.globalDefaultSpring;
        this.internalListeners = new ArrayList<>();
        this.startAction = new PhysicsAnimator$startAction$1(this);
        this.cancelAction = new PhysicsAnimator$cancelAction$1(this);
    }

    public static final <T> PhysicsAnimator<T> getInstance(T t) {
        return Companion.getInstance(t);
    }

    public final void cancel() {
        ((PhysicsAnimator$cancelAction$1) this.cancelAction).invoke(this.flingAnimations.keySet());
        ((PhysicsAnimator$cancelAction$1) this.cancelAction).invoke(this.springAnimations.keySet());
    }

    public final PhysicsAnimator spring(DynamicAnimation.ViewProperty viewProperty, float f) {
        spring(viewProperty, f, 0.0f, this.defaultSpring);
        return this;
    }

    public final PhysicsAnimator<T> flingThenSpring(FloatPropertyCompat<? super T> floatPropertyCompat, float f, FlingConfig flingConfig, SpringConfig springConfig, boolean z) {
        boolean z2;
        boolean z3;
        float f2 = f;
        Object obj = (T) this.weakTarget.get();
        if (obj == null) {
            Log.w("PhysicsAnimator", "Trying to animate a GC-ed target.");
            return this;
        }
        float f3 = flingConfig.friction;
        float f4 = flingConfig.min;
        float f5 = flingConfig.max;
        FlingConfig flingConfig2 = new FlingConfig(f3, f4, f5, flingConfig.startVelocity);
        SpringConfig springConfig2 = new SpringConfig(springConfig.stiffness, springConfig.dampingRatio, springConfig.startVelocity, springConfig.finalPosition);
        int i = (f2 > 0.0f ? 1 : (f2 == 0.0f ? 0 : -1));
        if (i >= 0) {
            f4 = f5;
        }
        if (z) {
            if (f4 >= Float.MAX_VALUE || f4 <= -3.4028235E38f) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (z2) {
                float value = (f2 / (flingConfig.friction * 4.2f)) + floatPropertyCompat.getValue(obj);
                float f6 = flingConfig.min;
                float f7 = flingConfig.max;
                float f8 = (f6 + f7) / 2;
                if ((i < 0 && value > f8) || (f2 > 0.0f && value < f8)) {
                    if (value >= f8) {
                        f6 = f7;
                    }
                    if (f6 >= Float.MAX_VALUE || f6 <= -3.4028235E38f) {
                        z3 = false;
                    } else {
                        z3 = true;
                    }
                    if (z3) {
                        spring(floatPropertyCompat, f6, f2, springConfig);
                        return this;
                    }
                }
                float value2 = f4 - floatPropertyCompat.getValue(obj);
                float f9 = flingConfig.friction * 4.2f * value2;
                if (value2 > 0.0f && f2 >= 0.0f) {
                    f2 = Math.max(f9, f2);
                } else if (value2 < 0.0f && i <= 0) {
                    f2 = Math.min(f9, f2);
                }
                flingConfig2.startVelocity = f2;
                springConfig2.finalPosition = f4;
                this.flingConfigs.put(floatPropertyCompat, flingConfig2);
                this.springConfigs.put(floatPropertyCompat, springConfig2);
                return this;
            }
        }
        flingConfig2.startVelocity = f2;
        this.flingConfigs.put(floatPropertyCompat, flingConfig2);
        this.springConfigs.put(floatPropertyCompat, springConfig2);
        return this;
    }

    public final boolean isPropertyAnimating(FloatPropertyCompat<? super T> floatPropertyCompat) {
        boolean z;
        boolean z2;
        SpringAnimation springAnimation = this.springAnimations.get(floatPropertyCompat);
        if (springAnimation == null) {
            z = false;
        } else {
            z = springAnimation.mRunning;
        }
        if (!z) {
            FlingAnimation flingAnimation = this.flingAnimations.get(floatPropertyCompat);
            if (flingAnimation == null) {
                z2 = false;
            } else {
                z2 = flingAnimation.mRunning;
            }
            if (!z2) {
                return false;
            }
        }
        return true;
    }

    public final boolean isRunning() {
        Set<FloatPropertyCompat<? super T>> keySet = this.springAnimations.keySet();
        Set<FloatPropertyCompat<? super T>> keySet2 = this.flingAnimations.keySet();
        Set<? extends FloatPropertyCompat<? super T>> mutableSet = CollectionsKt___CollectionsKt.toMutableSet(keySet);
        CollectionsKt__ReversedViewsKt.addAll(mutableSet, keySet2);
        return arePropertiesAnimating(mutableSet);
    }

    public final PhysicsAnimator<T> spring(FloatPropertyCompat<? super T> floatPropertyCompat, float f, float f2, SpringConfig springConfig) {
        float f3 = springConfig.stiffness;
        float f4 = springConfig.dampingRatio;
        WeakHashMap<Object, PhysicsAnimator<?>> weakHashMap = PhysicsAnimatorKt.animators;
        this.springConfigs.put(floatPropertyCompat, new SpringConfig(f3, f4, f2, f));
        return this;
    }

    public final void start() {
        ((PhysicsAnimator$startAction$1) this.startAction).invoke();
    }

    public final PhysicsAnimator<T> withEndActions(Runnable... runnableArr) {
        ArrayList<Function0<Unit>> arrayList = this.endActions;
        ArrayList filterNotNull = ArraysKt___ArraysKt.filterNotNull(runnableArr);
        ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(filterNotNull, 10));
        Iterator it = filterNotNull.iterator();
        while (it.hasNext()) {
            arrayList2.add(new PhysicsAnimator$withEndActions$1$1((Runnable) it.next()));
        }
        arrayList.addAll(arrayList2);
        return this;
    }

    public final boolean arePropertiesAnimating(Set<? extends FloatPropertyCompat<? super T>> set) {
        if (set.isEmpty()) {
            return false;
        }
        for (FloatPropertyCompat<? super T> floatPropertyCompat : set) {
            if (isPropertyAnimating(floatPropertyCompat)) {
                return true;
            }
        }
        return false;
    }

    public final void cancel(FloatPropertyCompat<? super T>... floatPropertyCompatArr) {
        ((PhysicsAnimator$cancelAction$1) this.cancelAction).invoke(ArraysKt___ArraysKt.toSet(floatPropertyCompatArr));
    }
}
