package com.android.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.phone.PanelView;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
/* compiled from: ViewGroupFadeHelper.kt */
/* loaded from: classes.dex */
public final class ViewGroupFadeHelper {
    public static final Function1<View, Boolean> visibilityIncluder = ViewGroupFadeHelper$Companion$visibilityIncluder$1.INSTANCE;

    public static final void fadeOutAllChildrenExcept(final PanelView panelView, View view, long j, final Runnable runnable) {
        Function1<View, Boolean> function1 = visibilityIncluder;
        final LinkedHashSet<View> linkedHashSet = new LinkedHashSet();
        ViewParent parent = view.getParent();
        ViewGroup viewGroup = view;
        while (true) {
            ViewGroup viewGroup2 = (ViewGroup) parent;
            if (viewGroup2 == null) {
                break;
            }
            int i = 0;
            int childCount = viewGroup2.getChildCount();
            while (i < childCount) {
                int i2 = i + 1;
                View childAt = viewGroup2.getChildAt(i);
                if (((Boolean) ((ViewGroupFadeHelper$Companion$visibilityIncluder$1) function1).invoke(childAt)).booleanValue() && !Intrinsics.areEqual(viewGroup, childAt)) {
                    linkedHashSet.add(childAt);
                }
                i = i2;
            }
            if (Intrinsics.areEqual(viewGroup2, panelView)) {
                break;
            }
            parent = viewGroup2.getParent();
            viewGroup = viewGroup2;
        }
        for (View view2 : linkedHashSet) {
            if (view2.getHasOverlappingRendering() && view2.getLayerType() == 0) {
                view2.setLayerType(2, null);
                view2.setTag(2131429185, Boolean.TRUE);
            }
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat.setDuration(j);
        ofFloat.setInterpolator(Interpolators.ALPHA_OUT);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                boolean z;
                Float f = (Float) panelView.getTag(2131429187);
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                float floatValue = ((Float) animatedValue).floatValue();
                for (View view3 : linkedHashSet) {
                    float alpha = view3.getAlpha();
                    if (f == null || alpha != f.floatValue()) {
                        z = false;
                    } else {
                        z = true;
                    }
                    if (!z) {
                        view3.setTag(2131429188, Float.valueOf(view3.getAlpha()));
                    }
                    view3.setAlpha(floatValue);
                }
                panelView.setTag(2131429187, Float.valueOf(floatValue));
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                Runnable runnable2 = runnable;
                if (runnable2 != null) {
                    runnable2.run();
                }
            }
        });
        ofFloat.start();
        panelView.setTag(2131429186, linkedHashSet);
        panelView.setTag(2131429184, ofFloat);
    }

    public static final void reset(PanelView panelView) {
        boolean z;
        Object tag = panelView.getTag(2131429186);
        if (!(tag instanceof KMappedMarker)) {
            try {
                Set<View> set = (Set) tag;
                Animator animator = (Animator) panelView.getTag(2131429184);
                if (!(set == null || animator == null)) {
                    animator.cancel();
                    Float f = (Float) panelView.getTag(2131429187);
                    for (View view : set) {
                        Float f2 = (Float) view.getTag(2131429188);
                        if (f2 != null) {
                            float alpha = view.getAlpha();
                            if (f == null || f.floatValue() != alpha) {
                                z = false;
                            } else {
                                z = true;
                            }
                            if (z) {
                                view.setAlpha(f2.floatValue());
                            }
                            if (Intrinsics.areEqual((Boolean) view.getTag(2131429185), Boolean.TRUE)) {
                                view.setLayerType(0, null);
                                view.setTag(2131429185, null);
                            }
                            view.setTag(2131429188, null);
                        }
                    }
                    panelView.setTag(2131429186, null);
                    panelView.setTag(2131429187, null);
                    panelView.setTag(2131429184, null);
                }
            } catch (ClassCastException e) {
                Intrinsics.sanitizeStackTrace(e, TypeIntrinsics.class.getName());
                throw e;
            }
        } else {
            TypeIntrinsics.throwCce(tag, "kotlin.collections.MutableSet");
            throw null;
        }
    }
}
