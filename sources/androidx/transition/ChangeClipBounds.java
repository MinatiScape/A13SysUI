package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class ChangeClipBounds extends Transition {
    public static final String[] sTransitionProperties = {"android:clipBounds:clip"};

    public ChangeClipBounds() {
    }

    @Override // androidx.transition.Transition
    public final Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        boolean z;
        ObjectAnimator objectAnimator = null;
        if (transitionValues != null && transitionValues2 != null && transitionValues.values.containsKey("android:clipBounds:clip") && transitionValues2.values.containsKey("android:clipBounds:clip")) {
            Rect rect = (Rect) transitionValues.values.get("android:clipBounds:clip");
            Rect rect2 = (Rect) transitionValues2.values.get("android:clipBounds:clip");
            if (rect2 == null) {
                z = true;
            } else {
                z = false;
            }
            if (rect == null && rect2 == null) {
                return null;
            }
            if (rect == null) {
                rect = (Rect) transitionValues.values.get("android:clipBounds:bounds");
            } else if (rect2 == null) {
                rect2 = (Rect) transitionValues2.values.get("android:clipBounds:bounds");
            }
            if (rect.equals(rect2)) {
                return null;
            }
            View view = transitionValues2.view;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api18Impl.setClipBounds(view, rect);
            objectAnimator = ObjectAnimator.ofObject(transitionValues2.view, ViewUtils.CLIP_BOUNDS, new RectEvaluator(new Rect()), rect, rect2);
            if (z) {
                final View view2 = transitionValues2.view;
                objectAnimator.addListener(new AnimatorListenerAdapter() { // from class: androidx.transition.ChangeClipBounds.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        View view3 = view2;
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                        ViewCompat.Api18Impl.setClipBounds(view3, null);
                    }
                });
            }
        }
        return objectAnimator;
    }

    public ChangeClipBounds(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public final void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (view.getVisibility() != 8) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            Rect clipBounds = ViewCompat.Api18Impl.getClipBounds(view);
            transitionValues.values.put("android:clipBounds:clip", clipBounds);
            if (clipBounds == null) {
                transitionValues.values.put("android:clipBounds:bounds", new Rect(0, 0, view.getWidth(), view.getHeight()));
            }
        }
    }

    @Override // androidx.transition.Transition
    public final void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Transition
    public final void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Transition
    public final String[] getTransitionProperties() {
        return sTransitionProperties;
    }
}
