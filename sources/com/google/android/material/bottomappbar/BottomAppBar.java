package com.google.android.material.bottomappbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.customview.view.AbsSavedState;
import androidx.mediarouter.R$bool;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.animation.TransformationCallback;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButtonImpl;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class BottomAppBar extends Toolbar implements CoordinatorLayout.AttachedBehavior {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Behavior behavior;
    public int bottomInset;
    public int fabAlignmentMode;
    public AnonymousClass1 fabAnimationListener;
    public boolean fabAttached;
    public final int fabOffsetEndMode;
    public AnonymousClass2 fabTransformationCallback;
    public boolean hideOnScroll;
    public int leftInset;
    public final MaterialShapeDrawable materialShapeDrawable;
    public boolean menuAnimatingWithFabAlignmentMode;
    public Animator menuAnimator;
    public Integer navigationIconTint;
    public final boolean paddingBottomSystemWindowInsets;
    public final boolean paddingLeftSystemWindowInsets;
    public final boolean paddingRightSystemWindowInsets;
    public int rightInset;

    /* renamed from: com.google.android.material.bottomappbar.BottomAppBar$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        public AnonymousClass1() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationStart(Animator animator) {
            final ActionMenuView actionMenuView;
            final BottomAppBar bottomAppBar = BottomAppBar.this;
            if (!bottomAppBar.menuAnimatingWithFabAlignmentMode) {
                final int i = bottomAppBar.fabAlignmentMode;
                final boolean z = bottomAppBar.fabAttached;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                if (!ViewCompat.Api19Impl.isLaidOut(bottomAppBar)) {
                    bottomAppBar.menuAnimatingWithFabAlignmentMode = false;
                    return;
                }
                Animator animator2 = bottomAppBar.menuAnimator;
                if (animator2 != null) {
                    animator2.cancel();
                }
                ArrayList arrayList = new ArrayList();
                if (!bottomAppBar.isFabVisibleOrWillBeShown()) {
                    i = 0;
                    z = false;
                }
                int i2 = 0;
                while (true) {
                    if (i2 >= bottomAppBar.getChildCount()) {
                        actionMenuView = null;
                        break;
                    }
                    View childAt = bottomAppBar.getChildAt(i2);
                    if (childAt instanceof ActionMenuView) {
                        actionMenuView = (ActionMenuView) childAt;
                        break;
                    }
                    i2++;
                }
                if (actionMenuView != null) {
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(actionMenuView, "alpha", 1.0f);
                    if (Math.abs(actionMenuView.getTranslationX() - bottomAppBar.getActionMenuViewTranslationX(actionMenuView, i, z)) > 1.0f) {
                        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(actionMenuView, "alpha", 0.0f);
                        ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.bottomappbar.BottomAppBar.7
                            public boolean cancelled;

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public final void onAnimationCancel(Animator animator3) {
                                this.cancelled = true;
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public final void onAnimationEnd(Animator animator3) {
                                if (!this.cancelled) {
                                    Objects.requireNonNull(BottomAppBar.this);
                                    Objects.requireNonNull(BottomAppBar.this);
                                    BottomAppBar.this.translateActionMenuView$1(actionMenuView, i, z);
                                }
                            }
                        });
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.setDuration(150L);
                        animatorSet.playSequentially(ofFloat2, ofFloat);
                        arrayList.add(animatorSet);
                    } else if (actionMenuView.getAlpha() < 1.0f) {
                        arrayList.add(ofFloat);
                    }
                }
                AnimatorSet animatorSet2 = new AnimatorSet();
                animatorSet2.playTogether(arrayList);
                bottomAppBar.menuAnimator = animatorSet2;
                animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.bottomappbar.BottomAppBar.6
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator3) {
                        BottomAppBar bottomAppBar2 = BottomAppBar.this;
                        int i3 = BottomAppBar.$r8$clinit;
                        Objects.requireNonNull(bottomAppBar2);
                        BottomAppBar bottomAppBar3 = BottomAppBar.this;
                        bottomAppBar3.menuAnimatingWithFabAlignmentMode = false;
                        bottomAppBar3.menuAnimator = null;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationStart(Animator animator3) {
                        BottomAppBar bottomAppBar2 = BottomAppBar.this;
                        int i3 = BottomAppBar.$r8$clinit;
                        Objects.requireNonNull(bottomAppBar2);
                    }
                });
                bottomAppBar.menuAnimator.start();
            }
        }
    }

    /* renamed from: com.google.android.material.bottomappbar.BottomAppBar$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements TransformationCallback<FloatingActionButton> {
        public AnonymousClass2() {
        }
    }

    /* loaded from: classes.dex */
    public static class Behavior extends HideBottomViewOnScrollBehavior<BottomAppBar> {
        public int originalBottomMargin;
        public WeakReference<BottomAppBar> viewRef;
        public final AnonymousClass1 fabLayoutListener = new View.OnLayoutChangeListener() { // from class: com.google.android.material.bottomappbar.BottomAppBar.Behavior.1
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                BottomAppBar bottomAppBar = Behavior.this.viewRef.get();
                if (bottomAppBar == null || !(view instanceof FloatingActionButton)) {
                    view.removeOnLayoutChangeListener(this);
                    return;
                }
                FloatingActionButton floatingActionButton = (FloatingActionButton) view;
                Rect rect = Behavior.this.fabContentRect;
                Objects.requireNonNull(floatingActionButton);
                rect.set(0, 0, floatingActionButton.getMeasuredWidth(), floatingActionButton.getMeasuredHeight());
                floatingActionButton.offsetRectWithShadow(rect);
                int height = Behavior.this.fabContentRect.height();
                float f = height;
                BottomAppBarTopEdgeTreatment topEdgeTreatment = bottomAppBar.getTopEdgeTreatment();
                Objects.requireNonNull(topEdgeTreatment);
                if (f != topEdgeTreatment.fabDiameter) {
                    BottomAppBarTopEdgeTreatment topEdgeTreatment2 = bottomAppBar.getTopEdgeTreatment();
                    Objects.requireNonNull(topEdgeTreatment2);
                    topEdgeTreatment2.fabDiameter = f;
                    bottomAppBar.materialShapeDrawable.invalidateSelf();
                }
                FloatingActionButtonImpl impl = floatingActionButton.getImpl();
                Objects.requireNonNull(impl);
                ShapeAppearanceModel shapeAppearanceModel = impl.shapeAppearance;
                Objects.requireNonNull(shapeAppearanceModel);
                float cornerSize = shapeAppearanceModel.topLeftCornerSize.getCornerSize(new RectF(Behavior.this.fabContentRect));
                BottomAppBarTopEdgeTreatment topEdgeTreatment3 = bottomAppBar.getTopEdgeTreatment();
                Objects.requireNonNull(topEdgeTreatment3);
                if (cornerSize != topEdgeTreatment3.fabCornerSize) {
                    BottomAppBarTopEdgeTreatment topEdgeTreatment4 = bottomAppBar.getTopEdgeTreatment();
                    Objects.requireNonNull(topEdgeTreatment4);
                    topEdgeTreatment4.fabCornerSize = cornerSize;
                    bottomAppBar.materialShapeDrawable.invalidateSelf();
                }
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                if (Behavior.this.originalBottomMargin == 0) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = bottomAppBar.bottomInset + (bottomAppBar.getResources().getDimensionPixelOffset(2131166399) - ((floatingActionButton.getMeasuredHeight() - height) / 2));
                    ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = bottomAppBar.leftInset;
                    ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = bottomAppBar.rightInset;
                    if (ViewUtils.isLayoutRtl(floatingActionButton)) {
                        ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin += bottomAppBar.fabOffsetEndMode;
                    } else {
                        ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin += bottomAppBar.fabOffsetEndMode;
                    }
                }
            }
        };
        public final Rect fabContentRect = new Rect();

        /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.material.bottomappbar.BottomAppBar$Behavior$1] */
        public Behavior() {
        }

        @Override // com.google.android.material.behavior.HideBottomViewOnScrollBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public final boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, View view3, int i, int i2) {
            BottomAppBar bottomAppBar = (BottomAppBar) view;
            if (!bottomAppBar.hideOnScroll || !super.onStartNestedScroll(coordinatorLayout, bottomAppBar, view2, view3, i, i2)) {
                return false;
            }
            return true;
        }

        @Override // com.google.android.material.behavior.HideBottomViewOnScrollBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public final boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i) {
            final BottomAppBar bottomAppBar = (BottomAppBar) view;
            this.viewRef = new WeakReference<>(bottomAppBar);
            int i2 = BottomAppBar.$r8$clinit;
            View findDependentView = bottomAppBar.findDependentView();
            if (findDependentView != null) {
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                if (!ViewCompat.Api19Impl.isLaidOut(findDependentView)) {
                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) findDependentView.getLayoutParams();
                    layoutParams.anchorGravity = 49;
                    this.originalBottomMargin = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                    if (findDependentView instanceof FloatingActionButton) {
                        FloatingActionButton floatingActionButton = (FloatingActionButton) findDependentView;
                        FloatingActionButtonImpl impl = floatingActionButton.getImpl();
                        Objects.requireNonNull(impl);
                        if (impl.showMotionSpec == null) {
                            MotionSpec createFromResource = MotionSpec.createFromResource(floatingActionButton.getContext(), 2130837553);
                            FloatingActionButtonImpl impl2 = floatingActionButton.getImpl();
                            Objects.requireNonNull(impl2);
                            impl2.showMotionSpec = createFromResource;
                        }
                        FloatingActionButtonImpl impl3 = floatingActionButton.getImpl();
                        Objects.requireNonNull(impl3);
                        if (impl3.hideMotionSpec == null) {
                            MotionSpec createFromResource2 = MotionSpec.createFromResource(floatingActionButton.getContext(), 2130837552);
                            FloatingActionButtonImpl impl4 = floatingActionButton.getImpl();
                            Objects.requireNonNull(impl4);
                            impl4.hideMotionSpec = createFromResource2;
                        }
                        floatingActionButton.addOnLayoutChangeListener(this.fabLayoutListener);
                        AnonymousClass1 r1 = bottomAppBar.fabAnimationListener;
                        FloatingActionButtonImpl impl5 = floatingActionButton.getImpl();
                        Objects.requireNonNull(impl5);
                        if (impl5.hideListeners == null) {
                            impl5.hideListeners = new ArrayList<>();
                        }
                        impl5.hideListeners.add(r1);
                        AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter() { // from class: com.google.android.material.bottomappbar.BottomAppBar.9
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public final void onAnimationStart(Animator animator) {
                                FloatingActionButton floatingActionButton2;
                                BottomAppBar.this.fabAnimationListener.onAnimationStart(animator);
                                BottomAppBar bottomAppBar2 = BottomAppBar.this;
                                Objects.requireNonNull(bottomAppBar2);
                                View findDependentView2 = bottomAppBar2.findDependentView();
                                if (findDependentView2 instanceof FloatingActionButton) {
                                    floatingActionButton2 = (FloatingActionButton) findDependentView2;
                                } else {
                                    floatingActionButton2 = null;
                                }
                                if (floatingActionButton2 != null) {
                                    floatingActionButton2.setTranslationX(BottomAppBar.this.getFabTranslationX$1());
                                }
                            }
                        };
                        FloatingActionButtonImpl impl6 = floatingActionButton.getImpl();
                        Objects.requireNonNull(impl6);
                        if (impl6.showListeners == null) {
                            impl6.showListeners = new ArrayList<>();
                        }
                        impl6.showListeners.add(animatorListenerAdapter);
                        AnonymousClass2 r12 = bottomAppBar.fabTransformationCallback;
                        FloatingActionButtonImpl impl7 = floatingActionButton.getImpl();
                        FloatingActionButton.TransformationCallbackWrapper transformationCallbackWrapper = new FloatingActionButton.TransformationCallbackWrapper(r12);
                        Objects.requireNonNull(impl7);
                        if (impl7.transformationCallbacks == null) {
                            impl7.transformationCallbacks = new ArrayList<>();
                        }
                        impl7.transformationCallbacks.add(transformationCallbackWrapper);
                    }
                    bottomAppBar.setCutoutState();
                }
            }
            coordinatorLayout.onLayoutChild(bottomAppBar, i);
            super.onLayoutChild(coordinatorLayout, bottomAppBar, i);
            return false;
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.material.bottomappbar.BottomAppBar$Behavior$1] */
        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }
    }

    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: com.google.android.material.bottomappbar.BottomAppBar.SavedState.1
            @Override // android.os.Parcelable.ClassLoaderCreator
            public final SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            public final Object createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.Creator
            public final Object[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public int fabAlignmentMode;
        public boolean fabAttached;

        public SavedState(Toolbar.SavedState savedState) {
            super(savedState);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.fabAlignmentMode = parcel.readInt();
            this.fabAttached = parcel.readInt() != 0;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mSuperState, i);
            parcel.writeInt(this.fabAlignmentMode);
            parcel.writeInt(this.fabAttached ? 1 : 0);
        }
    }

    public BottomAppBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final int getActionMenuViewTranslationX(ActionMenuView actionMenuView, int i, boolean z) {
        int i2;
        int i3;
        int i4;
        boolean z2;
        if (i != 1 || !z) {
            return 0;
        }
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        if (isLayoutRtl) {
            i2 = getMeasuredWidth();
        } else {
            i2 = 0;
        }
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            if (!(childAt.getLayoutParams() instanceof Toolbar.LayoutParams) || (((Toolbar.LayoutParams) childAt.getLayoutParams()).gravity & 8388615) != 8388611) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (z2) {
                if (isLayoutRtl) {
                    i2 = Math.min(i2, childAt.getLeft());
                } else {
                    i2 = Math.max(i2, childAt.getRight());
                }
            }
        }
        if (isLayoutRtl) {
            i3 = actionMenuView.getRight();
        } else {
            i3 = actionMenuView.getLeft();
        }
        if (isLayoutRtl) {
            i4 = this.rightInset;
        } else {
            i4 = -this.leftInset;
        }
        return i2 - (i3 + i4);
    }

    public final void setActionMenuViewPosition() {
        ActionMenuView actionMenuView;
        int i = 0;
        while (true) {
            if (i >= getChildCount()) {
                actionMenuView = null;
                break;
            }
            View childAt = getChildAt(i);
            if (childAt instanceof ActionMenuView) {
                actionMenuView = (ActionMenuView) childAt;
                break;
            }
            i++;
        }
        if (actionMenuView != null && this.menuAnimator == null) {
            actionMenuView.setAlpha(1.0f);
            if (!isFabVisibleOrWillBeShown()) {
                translateActionMenuView$1(actionMenuView, 0, false);
            } else {
                translateActionMenuView$1(actionMenuView, this.fabAlignmentMode, this.fabAttached);
            }
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public final void setSubtitle(CharSequence charSequence) {
    }

    @Override // androidx.appcompat.widget.Toolbar
    public final void setTitle(CharSequence charSequence) {
    }

    /* JADX WARN: Type inference failed for: r13v3, types: [com.google.android.material.bottomappbar.BottomAppBar$3] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public BottomAppBar(android.content.Context r13, android.util.AttributeSet r14, int r15) {
        /*
            r12 = this;
            r15 = 2130968692(0x7f040074, float:1.7546045E38)
            r0 = 2132018625(0x7f1405c1, float:1.9675562E38)
            android.content.Context r13 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r13, r14, r15, r0)
            r12.<init>(r13, r14, r15)
            com.google.android.material.shape.MaterialShapeDrawable r13 = new com.google.android.material.shape.MaterialShapeDrawable
            r13.<init>()
            r12.materialShapeDrawable = r13
            r1 = 0
            r12.menuAnimatingWithFabAlignmentMode = r1
            r2 = 1
            r12.fabAttached = r2
            com.google.android.material.bottomappbar.BottomAppBar$1 r3 = new com.google.android.material.bottomappbar.BottomAppBar$1
            r3.<init>()
            r12.fabAnimationListener = r3
            com.google.android.material.bottomappbar.BottomAppBar$2 r3 = new com.google.android.material.bottomappbar.BottomAppBar$2
            r3.<init>()
            r12.fabTransformationCallback = r3
            android.content.Context r3 = r12.getContext()
            int[] r6 = com.google.android.material.R$styleable.BottomAppBar
            int[] r9 = new int[r1]
            r7 = 2130968692(0x7f040074, float:1.7546045E38)
            r8 = 2132018625(0x7f1405c1, float:1.9675562E38)
            r4 = r3
            r5 = r14
            android.content.res.TypedArray r4 = com.google.android.material.internal.ThemeEnforcement.obtainStyledAttributes(r4, r5, r6, r7, r8, r9)
            android.content.res.ColorStateList r5 = com.google.android.material.resources.MaterialResources.getColorStateList(r3, r4, r1)
            r6 = 8
            boolean r7 = r4.hasValue(r6)
            if (r7 == 0) goto L_0x005c
            r7 = -1
            int r6 = r4.getColor(r6, r7)
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
            r12.navigationIconTint = r6
            android.graphics.drawable.Drawable r6 = r12.getNavigationIcon()
            if (r6 == 0) goto L_0x005c
            r12.setNavigationIcon(r6)
        L_0x005c:
            int r6 = r4.getDimensionPixelSize(r2, r1)
            r7 = 4
            int r7 = r4.getDimensionPixelOffset(r7, r1)
            float r7 = (float) r7
            r8 = 5
            int r8 = r4.getDimensionPixelOffset(r8, r1)
            float r8 = (float) r8
            r9 = 6
            int r9 = r4.getDimensionPixelOffset(r9, r1)
            float r9 = (float) r9
            r10 = 2
            int r11 = r4.getInt(r10, r1)
            r12.fabAlignmentMode = r11
            r11 = 3
            r4.getInt(r11, r1)
            r11 = 7
            boolean r11 = r4.getBoolean(r11, r1)
            r12.hideOnScroll = r11
            r11 = 9
            boolean r11 = r4.getBoolean(r11, r1)
            r12.paddingBottomSystemWindowInsets = r11
            r11 = 10
            boolean r11 = r4.getBoolean(r11, r1)
            r12.paddingLeftSystemWindowInsets = r11
            r11 = 11
            boolean r11 = r4.getBoolean(r11, r1)
            r12.paddingRightSystemWindowInsets = r11
            r4.recycle()
            android.content.res.Resources r4 = r12.getResources()
            r11 = 2131166398(0x7f0704be, float:1.794704E38)
            int r4 = r4.getDimensionPixelOffset(r11)
            r12.fabOffsetEndMode = r4
            com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment r4 = new com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
            r4.<init>(r7, r8, r9)
            com.google.android.material.shape.ShapeAppearanceModel$Builder r7 = new com.google.android.material.shape.ShapeAppearanceModel$Builder
            r7.<init>()
            r7.topEdge = r4
            com.google.android.material.shape.ShapeAppearanceModel r4 = new com.google.android.material.shape.ShapeAppearanceModel
            r4.<init>(r7)
            r13.setShapeAppearanceModel(r4)
            r13.setShadowCompatibilityMode()
            android.graphics.Paint$Style r4 = android.graphics.Paint.Style.FILL
            r13.setPaintStyle(r4)
            r13.initializeElevationOverlay(r3)
            float r3 = (float) r6
            r12.setElevation(r3)
            r13.setTintList(r5)
            java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r3 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
            androidx.core.view.ViewCompat.Api16Impl.setBackground(r12, r13)
            com.google.android.material.bottomappbar.BottomAppBar$3 r13 = new com.google.android.material.bottomappbar.BottomAppBar$3
            r13.<init>()
            android.content.Context r3 = r12.getContext()
            int[] r4 = com.google.android.material.R$styleable.Insets
            android.content.res.TypedArray r14 = r3.obtainStyledAttributes(r14, r4, r15, r0)
            boolean r15 = r14.getBoolean(r1, r1)
            boolean r0 = r14.getBoolean(r2, r1)
            boolean r1 = r14.getBoolean(r10, r1)
            r14.recycle()
            com.google.android.material.internal.ViewUtils$2 r14 = new com.google.android.material.internal.ViewUtils$2
            r14.<init>()
            com.google.android.material.internal.ViewUtils.doOnApplyWindowInsets(r12, r14)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.bottomappbar.BottomAppBar.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.AttachedBehavior
    public final CoordinatorLayout.Behavior getBehavior() {
        if (this.behavior == null) {
            this.behavior = new Behavior();
        }
        return this.behavior;
    }

    public final float getFabTranslationX$1() {
        int i;
        int i2 = this.fabAlignmentMode;
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int i3 = 1;
        if (i2 != 1) {
            return 0.0f;
        }
        if (isLayoutRtl) {
            i = this.leftInset;
        } else {
            i = this.rightInset;
        }
        int measuredWidth = (getMeasuredWidth() / 2) - (this.fabOffsetEndMode + i);
        if (isLayoutRtl) {
            i3 = -1;
        }
        return measuredWidth * i3;
    }

    public final BottomAppBarTopEdgeTreatment getTopEdgeTreatment() {
        MaterialShapeDrawable materialShapeDrawable = this.materialShapeDrawable;
        Objects.requireNonNull(materialShapeDrawable);
        ShapeAppearanceModel shapeAppearanceModel = materialShapeDrawable.drawableState.shapeAppearanceModel;
        Objects.requireNonNull(shapeAppearanceModel);
        return (BottomAppBarTopEdgeTreatment) shapeAppearanceModel.topEdge;
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        Objects.requireNonNull(savedState);
        super.onRestoreInstanceState(savedState.mSuperState);
        this.fabAlignmentMode = savedState.fabAlignmentMode;
        this.fabAttached = savedState.fabAttached;
    }

    @Override // android.view.View
    public final void setElevation(float f) {
        this.materialShapeDrawable.setElevation(f);
        MaterialShapeDrawable materialShapeDrawable = this.materialShapeDrawable;
        Objects.requireNonNull(materialShapeDrawable);
        int shadowOffsetY = materialShapeDrawable.drawableState.shadowCompatRadius - this.materialShapeDrawable.getShadowOffsetY();
        if (this.behavior == null) {
            this.behavior = new Behavior();
        }
        Behavior behavior = this.behavior;
        Objects.requireNonNull(behavior);
        behavior.additionalHiddenOffsetY = shadowOffsetY;
        if (behavior.currentState == 1) {
            setTranslationY(behavior.height + shadowOffsetY);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public final void setNavigationIcon(Drawable drawable) {
        if (!(drawable == null || this.navigationIconTint == null)) {
            drawable = drawable.mutate();
            drawable.setTint(this.navigationIconTint.intValue());
        }
        super.setNavigationIcon(drawable);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.view.View findDependentView() {
        /*
            r3 = this;
            android.view.ViewParent r0 = r3.getParent()
            boolean r0 = r0 instanceof androidx.coordinatorlayout.widget.CoordinatorLayout
            r1 = 0
            if (r0 != 0) goto L_0x000a
            return r1
        L_0x000a:
            android.view.ViewParent r0 = r3.getParent()
            androidx.coordinatorlayout.widget.CoordinatorLayout r0 = (androidx.coordinatorlayout.widget.CoordinatorLayout) r0
            java.util.Objects.requireNonNull(r0)
            androidx.coordinatorlayout.widget.DirectedAcyclicGraph r0 = r0.mChildDag
            java.util.Objects.requireNonNull(r0)
            java.lang.Object r0 = r0.mGraph
            androidx.collection.SimpleArrayMap r0 = (androidx.collection.SimpleArrayMap) r0
            java.util.Objects.requireNonNull(r0)
            java.lang.Object r3 = r0.getOrDefault(r3, r1)
            java.util.ArrayList r3 = (java.util.ArrayList) r3
            if (r3 != 0) goto L_0x0029
            r0 = r1
            goto L_0x002e
        L_0x0029:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>(r3)
        L_0x002e:
            if (r0 != 0) goto L_0x0034
            java.util.List r0 = java.util.Collections.emptyList()
        L_0x0034:
            java.util.Iterator r3 = r0.iterator()
        L_0x0038:
            boolean r0 = r3.hasNext()
            if (r0 == 0) goto L_0x004d
            java.lang.Object r0 = r3.next()
            android.view.View r0 = (android.view.View) r0
            boolean r2 = r0 instanceof com.google.android.material.floatingactionbutton.FloatingActionButton
            if (r2 != 0) goto L_0x004c
            boolean r2 = r0 instanceof com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
            if (r2 == 0) goto L_0x0038
        L_0x004c:
            return r0
        L_0x004d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.bottomappbar.BottomAppBar.findDependentView():android.view.View");
    }

    public final boolean isFabVisibleOrWillBeShown() {
        FloatingActionButton floatingActionButton;
        boolean z;
        View findDependentView = findDependentView();
        if (findDependentView instanceof FloatingActionButton) {
            floatingActionButton = (FloatingActionButton) findDependentView;
        } else {
            floatingActionButton = null;
        }
        if (floatingActionButton != null) {
            FloatingActionButtonImpl impl = floatingActionButton.getImpl();
            Objects.requireNonNull(impl);
            if (impl.view.getVisibility() == 0 ? impl.animState == 1 : impl.animState != 2) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        R$bool.setParentAbsoluteElevation(this, this.materialShapeDrawable);
        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).setClipChildren(false);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            Animator animator = this.menuAnimator;
            if (animator != null) {
                animator.cancel();
            }
            setCutoutState();
        }
        setActionMenuViewPosition();
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public final Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState((Toolbar.SavedState) super.onSaveInstanceState());
        savedState.fabAlignmentMode = this.fabAlignmentMode;
        savedState.fabAttached = this.fabAttached;
        return savedState;
    }

    public final void setCutoutState() {
        float f;
        BottomAppBarTopEdgeTreatment topEdgeTreatment = getTopEdgeTreatment();
        float fabTranslationX$1 = getFabTranslationX$1();
        Objects.requireNonNull(topEdgeTreatment);
        topEdgeTreatment.horizontalOffset = fabTranslationX$1;
        View findDependentView = findDependentView();
        MaterialShapeDrawable materialShapeDrawable = this.materialShapeDrawable;
        if (!this.fabAttached || !isFabVisibleOrWillBeShown()) {
            f = 0.0f;
        } else {
            f = 1.0f;
        }
        materialShapeDrawable.setInterpolation(f);
        if (findDependentView != null) {
            BottomAppBarTopEdgeTreatment topEdgeTreatment2 = getTopEdgeTreatment();
            Objects.requireNonNull(topEdgeTreatment2);
            findDependentView.setTranslationY(-topEdgeTreatment2.cradleVerticalOffset);
            findDependentView.setTranslationX(getFabTranslationX$1());
        }
    }

    public final void translateActionMenuView$1(ActionMenuView actionMenuView, int i, boolean z) {
        actionMenuView.setTranslationX(getActionMenuViewTranslationX(actionMenuView, i, z));
    }
}
