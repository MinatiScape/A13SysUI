package com.android.wm.shell.bubbles.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.FloatProperty;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.emoji2.text.EmojiCompatInitializer$BackgroundDefaultLoader$$ExternalSyntheticLambda0;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda1;
import com.android.wm.shell.bubbles.BadgedImageView;
import com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PhysicsAnimationLayout extends FrameLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public PhysicsAnimationController mController;
    public final HashMap<DynamicAnimation.ViewProperty, Runnable> mEndActionForProperty = new HashMap<>();

    /* loaded from: classes.dex */
    public class AllAnimationsForPropertyFinishedEndListener implements DynamicAnimation.OnAnimationEndListener {
        public DynamicAnimation.ViewProperty mProperty;

        public AllAnimationsForPropertyFinishedEndListener(DynamicAnimation.ViewProperty viewProperty) {
            this.mProperty = viewProperty;
        }

        @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
        public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
            Runnable runnable;
            PhysicsAnimationLayout physicsAnimationLayout = PhysicsAnimationLayout.this;
            boolean z2 = true;
            DynamicAnimation.ViewProperty[] viewPropertyArr = {this.mProperty};
            Objects.requireNonNull(physicsAnimationLayout);
            int i = 0;
            while (true) {
                if (i >= physicsAnimationLayout.getChildCount()) {
                    z2 = false;
                    break;
                } else if (PhysicsAnimationLayout.arePropertiesAnimatingOnView(physicsAnimationLayout.getChildAt(i), viewPropertyArr)) {
                    break;
                } else {
                    i++;
                }
            }
            if (!z2 && PhysicsAnimationLayout.this.mEndActionForProperty.containsKey(this.mProperty) && (runnable = PhysicsAnimationLayout.this.mEndActionForProperty.get(this.mProperty)) != null) {
                runnable.run();
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class PhysicsAnimationController {
        public PhysicsAnimationLayout mLayout;

        /* loaded from: classes.dex */
        public interface ChildAnimationConfigurator {
            void configureAnimationForChildAtIndex(int i, PhysicsPropertyAnimator physicsPropertyAnimator);
        }

        public abstract HashSet getAnimatedProperties();

        public abstract int getNextAnimationInChain(DynamicAnimation.ViewProperty viewProperty, int i);

        public abstract float getOffsetForChainedPropertyAnimation(DynamicAnimation.ViewProperty viewProperty, int i);

        public abstract SpringForce getSpringForce();

        public abstract void onActiveControllerForLayout(PhysicsAnimationLayout physicsAnimationLayout);

        public abstract void onChildAdded(View view, int i);

        public abstract void onChildRemoved(View view, ExecutorUtils$$ExternalSyntheticLambda1 executorUtils$$ExternalSyntheticLambda1);

        public abstract void onChildReordered();

        public final PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda0 animationsForChildrenFromIndex(ChildAnimationConfigurator childAnimationConfigurator) {
            HashSet hashSet = new HashSet();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.mLayout.getChildCount(); i++) {
                PhysicsPropertyAnimator animationForChild = animationForChild(this.mLayout.getChildAt(i));
                childAnimationConfigurator.configureAnimationForChildAtIndex(i, animationForChild);
                HashSet hashSet2 = new HashSet(animationForChild.mAnimatedProperties.keySet());
                if (animationForChild.mPathAnimator != null) {
                    hashSet2.add(DynamicAnimation.TRANSLATION_X);
                    hashSet2.add(DynamicAnimation.TRANSLATION_Y);
                }
                hashSet.addAll(hashSet2);
                arrayList.add(animationForChild);
            }
            return new PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda0(this, hashSet, arrayList);
        }

        public final boolean isActiveController() {
            PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
            if (physicsAnimationLayout == null || this != physicsAnimationLayout.mController) {
                return false;
            }
            return true;
        }

        public final PhysicsPropertyAnimator animationForChild(View view) {
            PhysicsPropertyAnimator physicsPropertyAnimator = (PhysicsPropertyAnimator) view.getTag(2131428586);
            if (physicsPropertyAnimator == null) {
                PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
                Objects.requireNonNull(physicsAnimationLayout);
                physicsPropertyAnimator = new PhysicsPropertyAnimator(view);
                view.setTag(2131428586, physicsPropertyAnimator);
            }
            physicsPropertyAnimator.clearAnimator();
            physicsPropertyAnimator.mAssociatedController = this;
            return physicsPropertyAnimator;
        }
    }

    /* loaded from: classes.dex */
    public class PhysicsPropertyAnimator {
        public PhysicsAnimationController mAssociatedController;
        public ObjectAnimator mPathAnimator;
        public Runnable[] mPositionEndActions;
        public View mView;
        public float mDefaultStartVelocity = -3.4028235E38f;
        public long mStartDelay = 0;
        public float mDampingRatio = -1.0f;
        public float mStiffness = -1.0f;
        public HashMap mEndActionsForProperty = new HashMap();
        public HashMap mPositionStartVelocities = new HashMap();
        public HashMap mAnimatedProperties = new HashMap();
        public HashMap mInitialPropertyValues = new HashMap();
        public PointF mCurrentPointOnPath = new PointF();
        public final AnonymousClass1 mCurrentPointOnPathXProperty = new FloatProperty<PhysicsPropertyAnimator>() { // from class: com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsPropertyAnimator.1
            @Override // android.util.Property
            public final Float get(Object obj) {
                PhysicsPropertyAnimator physicsPropertyAnimator = (PhysicsPropertyAnimator) obj;
                return Float.valueOf(PhysicsPropertyAnimator.this.mCurrentPointOnPath.x);
            }

            @Override // android.util.FloatProperty
            public final void setValue(PhysicsPropertyAnimator physicsPropertyAnimator, float f) {
                PhysicsPropertyAnimator.this.mCurrentPointOnPath.x = f;
            }
        };
        public final AnonymousClass2 mCurrentPointOnPathYProperty = new FloatProperty<PhysicsPropertyAnimator>() { // from class: com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsPropertyAnimator.2
            @Override // android.util.Property
            public final Float get(Object obj) {
                PhysicsPropertyAnimator physicsPropertyAnimator = (PhysicsPropertyAnimator) obj;
                return Float.valueOf(PhysicsPropertyAnimator.this.mCurrentPointOnPath.y);
            }

            @Override // android.util.FloatProperty
            public final void setValue(PhysicsPropertyAnimator physicsPropertyAnimator, float f) {
                PhysicsPropertyAnimator.this.mCurrentPointOnPath.y = f;
            }
        };

        public final void animateValueForChild(DynamicAnimation.ViewProperty viewProperty, View view, final float f, final float f2, long j, final float f3, final float f4, final Runnable... runnableArr) {
            if (view != null) {
                PhysicsAnimationLayout physicsAnimationLayout = PhysicsAnimationLayout.this;
                int i = PhysicsAnimationLayout.$r8$clinit;
                Objects.requireNonNull(physicsAnimationLayout);
                final SpringAnimation springAnimation = (SpringAnimation) view.getTag(PhysicsAnimationLayout.getTagIdForProperty(viewProperty));
                if (springAnimation != null) {
                    if (runnableArr != null) {
                        springAnimation.addEndListener(new OneTimeEndListener() { // from class: com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsPropertyAnimator.4
                            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                            public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f5, float f6) {
                                Objects.requireNonNull(dynamicAnimation);
                                ArrayList<DynamicAnimation.OnAnimationEndListener> arrayList = dynamicAnimation.mEndListeners;
                                int indexOf = arrayList.indexOf(this);
                                if (indexOf >= 0) {
                                    arrayList.set(indexOf, null);
                                }
                                for (Runnable runnable : runnableArr) {
                                    runnable.run();
                                }
                            }
                        });
                    }
                    final SpringForce springForce = springAnimation.mSpring;
                    if (springForce != null) {
                        Runnable physicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda0 = new Runnable() { // from class: com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                SpringForce springForce2 = SpringForce.this;
                                float f5 = f3;
                                float f6 = f4;
                                float f7 = f2;
                                SpringAnimation springAnimation2 = springAnimation;
                                float f8 = f;
                                springForce2.setStiffness(f5);
                                springForce2.setDampingRatio(f6);
                                if (f7 > -3.4028235E38f) {
                                    Objects.requireNonNull(springAnimation2);
                                    springAnimation2.mVelocity = f7;
                                }
                                springForce2.mFinalPosition = f8;
                                springAnimation2.start();
                            }
                        };
                        if (j > 0) {
                            PhysicsAnimationLayout.this.postDelayed(physicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda0, j);
                        } else {
                            physicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda0.run();
                        }
                    }
                }
            }
        }

        public final PhysicsPropertyAnimator translationY(float f, Runnable... runnableArr) {
            this.mPathAnimator = null;
            property(DynamicAnimation.TRANSLATION_Y, f, runnableArr);
            return this;
        }

        /* JADX WARN: Type inference failed for: r3v8, types: [com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout$PhysicsPropertyAnimator$1] */
        /* JADX WARN: Type inference failed for: r3v9, types: [com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout$PhysicsPropertyAnimator$2] */
        public PhysicsPropertyAnimator(View view) {
            this.mView = view;
        }

        public final void clearAnimator() {
            this.mInitialPropertyValues.clear();
            this.mAnimatedProperties.clear();
            this.mPositionStartVelocities.clear();
            this.mDefaultStartVelocity = -3.4028235E38f;
            this.mStartDelay = 0L;
            this.mStiffness = -1.0f;
            this.mDampingRatio = -1.0f;
            this.mEndActionsForProperty.clear();
            this.mPathAnimator = null;
            this.mPositionEndActions = null;
        }

        public final PhysicsPropertyAnimator property(DynamicAnimation.ViewProperty viewProperty, float f, Runnable... runnableArr) {
            this.mAnimatedProperties.put(viewProperty, Float.valueOf(f));
            this.mEndActionsForProperty.put(viewProperty, runnableArr);
            return this;
        }

        public final void start(Runnable... runnableArr) {
            boolean z;
            float f;
            PhysicsAnimationLayout physicsAnimationLayout = PhysicsAnimationLayout.this;
            PhysicsAnimationController physicsAnimationController = this.mAssociatedController;
            Objects.requireNonNull(physicsAnimationLayout);
            if (physicsAnimationLayout.mController == physicsAnimationController) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                Log.w("Bubbs.PAL", "Only the active animation controller is allowed to start animations. Use PhysicsAnimationLayout#setActiveController to set the active animation controller.");
                return;
            }
            HashSet hashSet = new HashSet(this.mAnimatedProperties.keySet());
            if (this.mPathAnimator != null) {
                hashSet.add(DynamicAnimation.TRANSLATION_X);
                hashSet.add(DynamicAnimation.TRANSLATION_Y);
            }
            if (runnableArr.length > 0) {
                DynamicAnimation.ViewProperty[] viewPropertyArr = (DynamicAnimation.ViewProperty[]) hashSet.toArray(new DynamicAnimation.ViewProperty[0]);
                PhysicsAnimationController physicsAnimationController2 = this.mAssociatedController;
                WifiEntry$$ExternalSyntheticLambda1 wifiEntry$$ExternalSyntheticLambda1 = new WifiEntry$$ExternalSyntheticLambda1(runnableArr, 6);
                Objects.requireNonNull(physicsAnimationController2);
                StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1 statusBarNotificationActivityStarter$$ExternalSyntheticLambda1 = new StatusBarNotificationActivityStarter$$ExternalSyntheticLambda1(physicsAnimationController2, viewPropertyArr, wifiEntry$$ExternalSyntheticLambda1, 1);
                for (DynamicAnimation.ViewProperty viewProperty : viewPropertyArr) {
                    physicsAnimationController2.mLayout.mEndActionForProperty.put(viewProperty, statusBarNotificationActivityStarter$$ExternalSyntheticLambda1);
                }
            }
            if (this.mPositionEndActions != null) {
                PhysicsAnimationLayout physicsAnimationLayout2 = PhysicsAnimationLayout.this;
                DynamicAnimation.AnonymousClass1 r1 = DynamicAnimation.TRANSLATION_X;
                View view = this.mView;
                Objects.requireNonNull(physicsAnimationLayout2);
                SpringAnimation springAnimationFromView = PhysicsAnimationLayout.getSpringAnimationFromView(r1, view);
                PhysicsAnimationLayout physicsAnimationLayout3 = PhysicsAnimationLayout.this;
                DynamicAnimation.AnonymousClass2 r5 = DynamicAnimation.TRANSLATION_Y;
                View view2 = this.mView;
                Objects.requireNonNull(physicsAnimationLayout3);
                EmojiCompatInitializer$BackgroundDefaultLoader$$ExternalSyntheticLambda0 emojiCompatInitializer$BackgroundDefaultLoader$$ExternalSyntheticLambda0 = new EmojiCompatInitializer$BackgroundDefaultLoader$$ExternalSyntheticLambda0(this, springAnimationFromView, PhysicsAnimationLayout.getSpringAnimationFromView(r5, view2), 1);
                this.mEndActionsForProperty.put(r1, new Runnable[]{emojiCompatInitializer$BackgroundDefaultLoader$$ExternalSyntheticLambda0});
                this.mEndActionsForProperty.put(r5, new Runnable[]{emojiCompatInitializer$BackgroundDefaultLoader$$ExternalSyntheticLambda0});
            }
            if (this.mPathAnimator != null) {
                final SpringForce springForce = PhysicsAnimationLayout.this.mController.getSpringForce();
                final SpringForce springForce2 = PhysicsAnimationLayout.this.mController.getSpringForce();
                long j = this.mStartDelay;
                if (j > 0) {
                    this.mPathAnimator.setStartDelay(j);
                }
                final AccessPoint$$ExternalSyntheticLambda1 accessPoint$$ExternalSyntheticLambda1 = new AccessPoint$$ExternalSyntheticLambda1(this, 11);
                this.mPathAnimator.addUpdateListener(new ActivatableNotificationView$$ExternalSyntheticLambda0(accessPoint$$ExternalSyntheticLambda1, 1));
                this.mPathAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsPropertyAnimator.5
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        accessPoint$$ExternalSyntheticLambda1.run();
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationStart(Animator animator) {
                        float f2;
                        PhysicsPropertyAnimator physicsPropertyAnimator = PhysicsPropertyAnimator.this;
                        DynamicAnimation.AnonymousClass1 r2 = DynamicAnimation.TRANSLATION_X;
                        View view3 = physicsPropertyAnimator.mView;
                        float f3 = physicsPropertyAnimator.mCurrentPointOnPath.x;
                        float f4 = physicsPropertyAnimator.mDefaultStartVelocity;
                        float f5 = physicsPropertyAnimator.mStiffness;
                        if (f5 < 0.0f) {
                            SpringForce springForce3 = springForce;
                            Objects.requireNonNull(springForce3);
                            double d = springForce3.mNaturalFreq;
                            f5 = (float) (d * d);
                        }
                        float f6 = PhysicsPropertyAnimator.this.mDampingRatio;
                        if (f6 < 0.0f) {
                            SpringForce springForce4 = springForce;
                            Objects.requireNonNull(springForce4);
                            f6 = (float) springForce4.mDampingRatio;
                        }
                        physicsPropertyAnimator.animateValueForChild(r2, view3, f3, f4, 0L, f5, f6, new Runnable[0]);
                        PhysicsPropertyAnimator physicsPropertyAnimator2 = PhysicsPropertyAnimator.this;
                        DynamicAnimation.AnonymousClass2 r14 = DynamicAnimation.TRANSLATION_Y;
                        View view4 = physicsPropertyAnimator2.mView;
                        float f7 = physicsPropertyAnimator2.mCurrentPointOnPath.y;
                        float f8 = physicsPropertyAnimator2.mDefaultStartVelocity;
                        float f9 = physicsPropertyAnimator2.mStiffness;
                        if (f9 < 0.0f) {
                            SpringForce springForce5 = springForce2;
                            Objects.requireNonNull(springForce5);
                            double d2 = springForce5.mNaturalFreq;
                            f9 = (float) (d2 * d2);
                        }
                        float f10 = PhysicsPropertyAnimator.this.mDampingRatio;
                        if (f10 >= 0.0f) {
                            f2 = f10;
                        } else {
                            SpringForce springForce6 = springForce2;
                            Objects.requireNonNull(springForce6);
                            f2 = (float) springForce6.mDampingRatio;
                        }
                        physicsPropertyAnimator2.animateValueForChild(r14, view4, f7, f8, 0L, f9, f2, new Runnable[0]);
                    }
                });
                PhysicsAnimationLayout physicsAnimationLayout4 = PhysicsAnimationLayout.this;
                View view3 = this.mView;
                Objects.requireNonNull(physicsAnimationLayout4);
                ObjectAnimator objectAnimator = (ObjectAnimator) view3.getTag(2131429018);
                if (objectAnimator != null) {
                    objectAnimator.cancel();
                }
                this.mView.setTag(2131429018, this.mPathAnimator);
                this.mPathAnimator.start();
            }
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                DynamicAnimation.ViewProperty viewProperty2 = (DynamicAnimation.ViewProperty) it.next();
                if (this.mPathAnimator == null || (!viewProperty2.equals(DynamicAnimation.TRANSLATION_X) && !viewProperty2.equals(DynamicAnimation.TRANSLATION_Y))) {
                    if (this.mInitialPropertyValues.containsKey(viewProperty2)) {
                        viewProperty2.setValue(this.mView, ((Float) this.mInitialPropertyValues.get(viewProperty2)).floatValue());
                    }
                    SpringForce springForce3 = PhysicsAnimationLayout.this.mController.getSpringForce();
                    View view4 = this.mView;
                    float floatValue = ((Float) this.mAnimatedProperties.get(viewProperty2)).floatValue();
                    float floatValue2 = ((Float) this.mPositionStartVelocities.getOrDefault(viewProperty2, Float.valueOf(this.mDefaultStartVelocity))).floatValue();
                    long j2 = this.mStartDelay;
                    float f2 = this.mStiffness;
                    if (f2 < 0.0f) {
                        Objects.requireNonNull(springForce3);
                        double d = springForce3.mNaturalFreq;
                        f2 = (float) (d * d);
                    }
                    float f3 = this.mDampingRatio;
                    if (f3 >= 0.0f) {
                        f = f3;
                    } else {
                        Objects.requireNonNull(springForce3);
                        f = (float) springForce3.mDampingRatio;
                    }
                    animateValueForChild(viewProperty2, view4, floatValue, floatValue2, j2, f2, f, (Runnable[]) this.mEndActionsForProperty.get(viewProperty2));
                } else {
                    return;
                }
            }
            clearAnimator();
        }

        public final void updateValueForChild(DynamicAnimation.ViewProperty viewProperty, View view, float f) {
            SpringForce springForce;
            if (view != null) {
                PhysicsAnimationLayout physicsAnimationLayout = PhysicsAnimationLayout.this;
                int i = PhysicsAnimationLayout.$r8$clinit;
                Objects.requireNonNull(physicsAnimationLayout);
                SpringAnimation springAnimation = (SpringAnimation) view.getTag(PhysicsAnimationLayout.getTagIdForProperty(viewProperty));
                if (springAnimation != null && (springForce = springAnimation.mSpring) != null) {
                    springForce.mFinalPosition = f;
                    springAnimation.start();
                }
            }
        }
    }

    @Override // android.view.ViewGroup
    public final void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        addViewInternal(view, i, layoutParams, false);
    }

    public static String getReadablePropertyName(DynamicAnimation.ViewProperty viewProperty) {
        if (viewProperty.equals(DynamicAnimation.TRANSLATION_X)) {
            return "TRANSLATION_X";
        }
        if (viewProperty.equals(DynamicAnimation.TRANSLATION_Y)) {
            return "TRANSLATION_Y";
        }
        if (viewProperty.equals(DynamicAnimation.SCALE_X)) {
            return "SCALE_X";
        }
        if (viewProperty.equals(DynamicAnimation.SCALE_Y)) {
            return "SCALE_Y";
        }
        if (viewProperty.equals(DynamicAnimation.ALPHA)) {
            return "ALPHA";
        }
        return "Unknown animation property.";
    }

    public static int getTagIdForProperty(DynamicAnimation.ViewProperty viewProperty) {
        if (viewProperty.equals(DynamicAnimation.TRANSLATION_X)) {
            return 2131429103;
        }
        if (viewProperty.equals(DynamicAnimation.TRANSLATION_Y)) {
            return 2131429107;
        }
        if (viewProperty.equals(DynamicAnimation.SCALE_X)) {
            return 2131428736;
        }
        if (viewProperty.equals(DynamicAnimation.SCALE_Y)) {
            return 2131428740;
        }
        if (viewProperty.equals(DynamicAnimation.ALPHA)) {
            return 2131427480;
        }
        return -1;
    }

    public final void cancelAllAnimationsOfProperties(DynamicAnimation.ViewProperty... viewPropertyArr) {
        if (this.mController != null) {
            for (int i = 0; i < getChildCount(); i++) {
                for (DynamicAnimation.ViewProperty viewProperty : viewPropertyArr) {
                    SpringAnimation springAnimationFromView = getSpringAnimationFromView(viewProperty, getChildAt(i));
                    if (springAnimationFromView != null) {
                        springAnimationFromView.cancel();
                    }
                }
                ViewPropertyAnimator viewPropertyAnimator = (ViewPropertyAnimator) getChildAt(i).getTag(2131428691);
                if (viewPropertyAnimator != null) {
                    viewPropertyAnimator.cancel();
                }
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public final void removeView(View view) {
        if (this.mController != null) {
            int indexOfChild = indexOfChild(view);
            super.removeView(view);
            addTransientView(view, indexOfChild);
            this.mController.onChildRemoved(view, new ExecutorUtils$$ExternalSyntheticLambda1(this, view, 5));
            return;
        }
        super.removeView(view);
    }

    public final void reorderView(BadgedImageView badgedImageView, int i) {
        if (badgedImageView != null) {
            indexOfChild(badgedImageView);
            super.removeView(badgedImageView);
            if (badgedImageView.getParent() != null) {
                removeTransientView(badgedImageView);
            }
            addViewInternal(badgedImageView, i, badgedImageView.getLayoutParams(), true);
            PhysicsAnimationController physicsAnimationController = this.mController;
            if (physicsAnimationController != null) {
                physicsAnimationController.onChildReordered();
            }
        }
    }

    public final void setActiveController(PhysicsAnimationController physicsAnimationController) {
        PhysicsAnimationController physicsAnimationController2 = this.mController;
        if (physicsAnimationController2 != null) {
            cancelAllAnimationsOfProperties((DynamicAnimation.ViewProperty[]) physicsAnimationController2.getAnimatedProperties().toArray(new DynamicAnimation.ViewProperty[0]));
        }
        this.mEndActionForProperty.clear();
        this.mController = physicsAnimationController;
        Objects.requireNonNull(physicsAnimationController);
        physicsAnimationController.mLayout = this;
        physicsAnimationController.onActiveControllerForLayout(this);
        for (DynamicAnimation.ViewProperty viewProperty : this.mController.getAnimatedProperties()) {
            for (int i = 0; i < getChildCount(); i++) {
                setUpAnimationForChild(viewProperty, getChildAt(i));
            }
        }
    }

    public final void setUpAnimationForChild(final DynamicAnimation.ViewProperty viewProperty, final View view) {
        SpringAnimation springAnimation = new SpringAnimation(view, viewProperty);
        DynamicAnimation.OnAnimationUpdateListener physicsAnimationLayout$$ExternalSyntheticLambda0 = new DynamicAnimation.OnAnimationUpdateListener() { // from class: com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout$$ExternalSyntheticLambda0
            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
            public final void onAnimationUpdate(float f, float f2) {
                SpringAnimation springAnimationFromView;
                PhysicsAnimationLayout physicsAnimationLayout = PhysicsAnimationLayout.this;
                View view2 = view;
                DynamicAnimation.ViewProperty viewProperty2 = viewProperty;
                Objects.requireNonNull(physicsAnimationLayout);
                int indexOfChild = physicsAnimationLayout.indexOfChild(view2);
                int nextAnimationInChain = physicsAnimationLayout.mController.getNextAnimationInChain(viewProperty2, indexOfChild);
                if (nextAnimationInChain != -1 && indexOfChild >= 0) {
                    float offsetForChainedPropertyAnimation = physicsAnimationLayout.mController.getOffsetForChainedPropertyAnimation(viewProperty2, nextAnimationInChain);
                    if (nextAnimationInChain < physicsAnimationLayout.getChildCount() && (springAnimationFromView = PhysicsAnimationLayout.getSpringAnimationFromView(viewProperty2, physicsAnimationLayout.getChildAt(nextAnimationInChain))) != null) {
                        springAnimationFromView.animateToFinalPosition(f + offsetForChainedPropertyAnimation);
                    }
                }
            }
        };
        if (!springAnimation.mRunning) {
            if (!springAnimation.mUpdateListeners.contains(physicsAnimationLayout$$ExternalSyntheticLambda0)) {
                springAnimation.mUpdateListeners.add(physicsAnimationLayout$$ExternalSyntheticLambda0);
            }
            springAnimation.mSpring = this.mController.getSpringForce();
            springAnimation.addEndListener(new AllAnimationsForPropertyFinishedEndListener(viewProperty));
            view.setTag(getTagIdForProperty(viewProperty), springAnimation);
            return;
        }
        throw new UnsupportedOperationException("Error: Update listeners must be added beforethe animation.");
    }

    public PhysicsAnimationLayout(Context context) {
        super(context);
    }

    public static boolean arePropertiesAnimatingOnView(View view, DynamicAnimation.ViewProperty... viewPropertyArr) {
        boolean z;
        ObjectAnimator objectAnimator = (ObjectAnimator) view.getTag(2131429018);
        for (DynamicAnimation.ViewProperty viewProperty : viewPropertyArr) {
            SpringAnimation springAnimationFromView = getSpringAnimationFromView(viewProperty, view);
            if (springAnimationFromView != null && springAnimationFromView.mRunning) {
                return true;
            }
            if (viewProperty.equals(DynamicAnimation.TRANSLATION_X) || viewProperty.equals(DynamicAnimation.TRANSLATION_Y)) {
                z = true;
            } else {
                z = false;
            }
            if (z && objectAnimator != null && objectAnimator.isRunning()) {
                return true;
            }
        }
        return false;
    }

    public static SpringAnimation getSpringAnimationFromView(DynamicAnimation.ViewProperty viewProperty, View view) {
        return (SpringAnimation) view.getTag(getTagIdForProperty(viewProperty));
    }

    public final void addViewInternal(View view, int i, ViewGroup.LayoutParams layoutParams, boolean z) {
        super.addView(view, i, layoutParams);
        PhysicsAnimationController physicsAnimationController = this.mController;
        if (!(physicsAnimationController == null || z)) {
            for (DynamicAnimation.ViewProperty viewProperty : physicsAnimationController.getAnimatedProperties()) {
                setUpAnimationForChild(viewProperty, view);
            }
            this.mController.onChildAdded(view, i);
        }
    }

    public final void cancelAnimationsOnView(View view) {
        ObjectAnimator objectAnimator = (ObjectAnimator) view.getTag(2131429018);
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        for (DynamicAnimation.ViewProperty viewProperty : this.mController.getAnimatedProperties()) {
            SpringAnimation springAnimationFromView = getSpringAnimationFromView(viewProperty, view);
            if (springAnimationFromView != null) {
                springAnimationFromView.cancel();
            }
        }
    }

    public final boolean isFirstChildXLeftOfCenter(float f) {
        if (getChildCount() <= 0 || f + (getChildAt(0).getWidth() / 2) >= getWidth() / 2) {
            return false;
        }
        return true;
    }

    @Override // android.view.ViewGroup
    public final void removeViewAt(int i) {
        removeView(getChildAt(i));
    }
}
