package com.google.android.material.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import androidx.collection.SimpleArrayMap;
import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MotionSpec {
    public final SimpleArrayMap<String, MotionTiming> timings = new SimpleArrayMap<>();
    public final SimpleArrayMap<String, PropertyValuesHolder[]> propertyValues = new SimpleArrayMap<>();

    public static MotionSpec createFromResource(Context context, int i) {
        try {
            Animator loadAnimator = AnimatorInflater.loadAnimator(context, i);
            if (loadAnimator instanceof AnimatorSet) {
                return createSpecFromAnimators(((AnimatorSet) loadAnimator).getChildAnimations());
            }
            if (loadAnimator == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(loadAnimator);
            return createSpecFromAnimators(arrayList);
        } catch (Exception e) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Can't load animation resource ID #0x");
            m.append(Integer.toHexString(i));
            Log.w("MotionSpec", m.toString(), e);
            return null;
        }
    }

    public static MotionSpec createSpecFromAnimators(ArrayList arrayList) {
        MotionSpec motionSpec = new MotionSpec();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            Animator animator = (Animator) arrayList.get(i);
            if (animator instanceof ObjectAnimator) {
                ObjectAnimator objectAnimator = (ObjectAnimator) animator;
                motionSpec.setPropertyValues(objectAnimator.getPropertyName(), objectAnimator.getValues());
                String propertyName = objectAnimator.getPropertyName();
                long startDelay = objectAnimator.getStartDelay();
                long duration = objectAnimator.getDuration();
                TimeInterpolator interpolator = objectAnimator.getInterpolator();
                if ((interpolator instanceof AccelerateDecelerateInterpolator) || interpolator == null) {
                    interpolator = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;
                } else if (interpolator instanceof AccelerateInterpolator) {
                    interpolator = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
                } else if (interpolator instanceof DecelerateInterpolator) {
                    interpolator = AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR;
                }
                MotionTiming motionTiming = new MotionTiming(startDelay, duration, interpolator);
                motionTiming.repeatCount = objectAnimator.getRepeatCount();
                motionTiming.repeatMode = objectAnimator.getRepeatMode();
                motionSpec.timings.put(propertyName, motionTiming);
            } else {
                throw new IllegalArgumentException("Animator must be an ObjectAnimator: " + animator);
            }
        }
        return motionSpec;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MotionSpec)) {
            return false;
        }
        return this.timings.equals(((MotionSpec) obj).timings);
    }

    public final MotionTiming getTiming(String str) {
        boolean z;
        SimpleArrayMap<String, MotionTiming> simpleArrayMap = this.timings;
        Objects.requireNonNull(simpleArrayMap);
        if (simpleArrayMap.getOrDefault(str, null) != null) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            SimpleArrayMap<String, MotionTiming> simpleArrayMap2 = this.timings;
            Objects.requireNonNull(simpleArrayMap2);
            return simpleArrayMap2.getOrDefault(str, null);
        }
        throw new IllegalArgumentException();
    }

    public final boolean hasPropertyValues(String str) {
        SimpleArrayMap<String, PropertyValuesHolder[]> simpleArrayMap = this.propertyValues;
        Objects.requireNonNull(simpleArrayMap);
        if (simpleArrayMap.getOrDefault(str, null) != null) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return this.timings.hashCode();
    }

    public final void setPropertyValues(String str, PropertyValuesHolder[] propertyValuesHolderArr) {
        this.propertyValues.put(str, propertyValuesHolderArr);
    }

    public final String toString() {
        StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('\n');
        m.append(MotionSpec.class.getName());
        m.append('{');
        m.append(Integer.toHexString(System.identityHashCode(this)));
        m.append(" timings: ");
        m.append(this.timings);
        m.append("}\n");
        return m.toString();
    }

    public static MotionSpec createFromAttribute(Context context, TypedArray typedArray, int i) {
        int resourceId;
        if (!typedArray.hasValue(i) || (resourceId = typedArray.getResourceId(i, 0)) == 0) {
            return null;
        }
        return createFromResource(context, resourceId);
    }

    public final ObjectAnimator getAnimator(String str, ExtendedFloatingActionButton extendedFloatingActionButton, Property property) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(extendedFloatingActionButton, getPropertyValues(str));
        ofPropertyValuesHolder.setProperty(property);
        getTiming(str).apply(ofPropertyValuesHolder);
        return ofPropertyValuesHolder;
    }

    public final PropertyValuesHolder[] getPropertyValues(String str) {
        if (hasPropertyValues(str)) {
            SimpleArrayMap<String, PropertyValuesHolder[]> simpleArrayMap = this.propertyValues;
            Objects.requireNonNull(simpleArrayMap);
            PropertyValuesHolder[] orDefault = simpleArrayMap.getOrDefault(str, null);
            PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[orDefault.length];
            for (int i = 0; i < orDefault.length; i++) {
                propertyValuesHolderArr[i] = orDefault[i].clone();
            }
            return propertyValuesHolderArr;
        }
        throw new IllegalArgumentException();
    }
}
