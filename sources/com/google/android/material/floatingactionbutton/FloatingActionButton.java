package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatImageHelper;
import androidx.collection.SimpleArrayMap;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.mediarouter.R$bool;
import com.google.android.material.R$styleable;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.animation.TransformationCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.expandable.ExpandableWidget;
import com.google.android.material.expandable.ExpandableWidgetHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButtonImpl;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.internal.VisibilityAwareImageButton;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shadow.ShadowViewDelegate;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.stateful.ExtendableSavedState;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class FloatingActionButton extends VisibilityAwareImageButton implements ExpandableWidget, Shapeable, CoordinatorLayout.AttachedBehavior {
    public ColorStateList backgroundTint;
    public PorterDuff.Mode backgroundTintMode;
    public int borderWidth;
    public boolean compatPadding;
    public int customSize;
    public final ExpandableWidgetHelper expandableWidgetHelper;
    public final AppCompatImageHelper imageHelper;
    public PorterDuff.Mode imageMode;
    public int imagePadding;
    public ColorStateList imageTint;
    public FloatingActionButtonImplLollipop impl;
    public int maxImageSize;
    public ColorStateList rippleColor;
    public int size;
    public final Rect shadowPadding = new Rect();
    public final Rect touchArea = new Rect();

    /* loaded from: classes.dex */
    public static class BaseBehavior<T extends FloatingActionButton> extends CoordinatorLayout.Behavior<T> {
        public boolean autoHideEnabled;
        public Rect tmpRect;

        public BaseBehavior() {
            this.autoHideEnabled = true;
        }

        public void setInternalAutoHideListener(OnVisibilityChangedListener onVisibilityChangedListener) {
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public final boolean getInsetDodgeRect(View view, Rect rect) {
            FloatingActionButton floatingActionButton = (FloatingActionButton) view;
            Rect rect2 = floatingActionButton.shadowPadding;
            rect.set(floatingActionButton.getLeft() + rect2.left, floatingActionButton.getTop() + rect2.top, floatingActionButton.getRight() - rect2.right, floatingActionButton.getBottom() - rect2.bottom);
            return true;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public final void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams layoutParams) {
            if (layoutParams.dodgeInsetEdges == 0) {
                layoutParams.dodgeInsetEdges = 80;
            }
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public final boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
            boolean z;
            FloatingActionButton floatingActionButton = (FloatingActionButton) view;
            if (view2 instanceof AppBarLayout) {
                updateFabVisibilityForAppBarLayout(coordinatorLayout, (AppBarLayout) view2, floatingActionButton);
            } else {
                ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
                if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                    CoordinatorLayout.LayoutParams layoutParams2 = (CoordinatorLayout.LayoutParams) layoutParams;
                    Objects.requireNonNull(layoutParams2);
                    z = layoutParams2.mBehavior instanceof BottomSheetBehavior;
                } else {
                    z = false;
                }
                if (z) {
                    updateFabVisibilityForBottomSheet(view2, floatingActionButton);
                }
            }
            return false;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public final boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i) {
            int i2;
            boolean z;
            FloatingActionButton floatingActionButton = (FloatingActionButton) view;
            List<View> dependencies = coordinatorLayout.getDependencies(floatingActionButton);
            int size = dependencies.size();
            int i3 = 0;
            for (int i4 = 0; i4 < size; i4++) {
                View view2 = dependencies.get(i4);
                if (!(view2 instanceof AppBarLayout)) {
                    ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
                    if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                        CoordinatorLayout.LayoutParams layoutParams2 = (CoordinatorLayout.LayoutParams) layoutParams;
                        Objects.requireNonNull(layoutParams2);
                        z = layoutParams2.mBehavior instanceof BottomSheetBehavior;
                    } else {
                        z = false;
                    }
                    if (z && updateFabVisibilityForBottomSheet(view2, floatingActionButton)) {
                        break;
                    }
                } else if (updateFabVisibilityForAppBarLayout(coordinatorLayout, (AppBarLayout) view2, floatingActionButton)) {
                    break;
                }
            }
            coordinatorLayout.onLayoutChild(floatingActionButton, i);
            Rect rect = floatingActionButton.shadowPadding;
            if (rect == null || rect.centerX() <= 0 || rect.centerY() <= 0) {
                return true;
            }
            CoordinatorLayout.LayoutParams layoutParams3 = (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
            if (floatingActionButton.getRight() >= coordinatorLayout.getWidth() - ((ViewGroup.MarginLayoutParams) layoutParams3).rightMargin) {
                i2 = rect.right;
            } else if (floatingActionButton.getLeft() <= ((ViewGroup.MarginLayoutParams) layoutParams3).leftMargin) {
                i2 = -rect.left;
            } else {
                i2 = 0;
            }
            if (floatingActionButton.getBottom() >= coordinatorLayout.getHeight() - ((ViewGroup.MarginLayoutParams) layoutParams3).bottomMargin) {
                i3 = rect.bottom;
            } else if (floatingActionButton.getTop() <= ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin) {
                i3 = -rect.top;
            }
            if (i3 != 0) {
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                floatingActionButton.offsetTopAndBottom(i3);
            }
            if (i2 == 0) {
                return true;
            }
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
            floatingActionButton.offsetLeftAndRight(i2);
            return true;
        }

        public BaseBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.FloatingActionButton_Behavior_Layout);
            this.autoHideEnabled = obtainStyledAttributes.getBoolean(0, true);
            obtainStyledAttributes.recycle();
        }

        public final boolean shouldUpdateVisibility(View view, FloatingActionButton floatingActionButton) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
            if (!this.autoHideEnabled) {
                return false;
            }
            Objects.requireNonNull(layoutParams);
            if (layoutParams.mAnchorId == view.getId() && floatingActionButton.userSetVisibility == 0) {
                return true;
            }
            return false;
        }

        public final boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, FloatingActionButton floatingActionButton) {
            if (!shouldUpdateVisibility(appBarLayout, floatingActionButton)) {
                return false;
            }
            if (this.tmpRect == null) {
                this.tmpRect = new Rect();
            }
            Rect rect = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect(coordinatorLayout, appBarLayout, rect);
            if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                floatingActionButton.hide$1();
                return true;
            }
            floatingActionButton.show$5();
            return true;
        }

        public final boolean updateFabVisibilityForBottomSheet(View view, FloatingActionButton floatingActionButton) {
            if (!shouldUpdateVisibility(view, floatingActionButton)) {
                return false;
            }
            if (view.getTop() < (floatingActionButton.getHeight() / 2) + ((ViewGroup.MarginLayoutParams) ((CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams())).topMargin) {
                floatingActionButton.hide$1();
                return true;
            }
            floatingActionButton.show$5();
            return true;
        }
    }

    /* loaded from: classes.dex */
    public static class Behavior extends BaseBehavior<FloatingActionButton> {
        public Behavior() {
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.BaseBehavior
        public /* bridge */ /* synthetic */ void setInternalAutoHideListener(OnVisibilityChangedListener onVisibilityChangedListener) {
            super.setInternalAutoHideListener(null);
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class OnVisibilityChangedListener {
    }

    /* loaded from: classes.dex */
    public class ShadowDelegateImpl implements ShadowViewDelegate {
        public ShadowDelegateImpl() {
        }
    }

    /* loaded from: classes.dex */
    public class TransformationCallbackWrapper<T extends FloatingActionButton> implements FloatingActionButtonImpl.InternalTransformationCallback {
        public final TransformationCallback<T> listener;

        public TransformationCallbackWrapper(BottomAppBar.AnonymousClass2 r2) {
            this.listener = r2;
        }

        public final boolean equals(Object obj) {
            if (!(obj instanceof TransformationCallbackWrapper) || !((TransformationCallbackWrapper) obj).listener.equals(this.listener)) {
                return false;
            }
            return true;
        }

        public final int hashCode() {
            return this.listener.hashCode();
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.InternalTransformationCallback
        public final void onScaleChanged() {
            float f;
            TransformationCallback<T> transformationCallback = this.listener;
            FloatingActionButton floatingActionButton = FloatingActionButton.this;
            BottomAppBar.AnonymousClass2 r0 = (BottomAppBar.AnonymousClass2) transformationCallback;
            Objects.requireNonNull(r0);
            MaterialShapeDrawable materialShapeDrawable = BottomAppBar.this.materialShapeDrawable;
            if (floatingActionButton.getVisibility() == 0) {
                f = floatingActionButton.getScaleY();
            } else {
                f = 0.0f;
            }
            materialShapeDrawable.setInterpolation(f);
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.InternalTransformationCallback
        public final void onTranslationChanged() {
            TransformationCallback<T> transformationCallback = this.listener;
            FloatingActionButton floatingActionButton = FloatingActionButton.this;
            BottomAppBar.AnonymousClass2 r0 = (BottomAppBar.AnonymousClass2) transformationCallback;
            Objects.requireNonNull(r0);
            float translationX = floatingActionButton.getTranslationX();
            BottomAppBar bottomAppBar = BottomAppBar.this;
            int i = BottomAppBar.$r8$clinit;
            BottomAppBarTopEdgeTreatment topEdgeTreatment = bottomAppBar.getTopEdgeTreatment();
            Objects.requireNonNull(topEdgeTreatment);
            if (topEdgeTreatment.horizontalOffset != translationX) {
                BottomAppBarTopEdgeTreatment topEdgeTreatment2 = BottomAppBar.this.getTopEdgeTreatment();
                Objects.requireNonNull(topEdgeTreatment2);
                topEdgeTreatment2.horizontalOffset = translationX;
                BottomAppBar.this.materialShapeDrawable.invalidateSelf();
            }
            float f = 0.0f;
            float max = Math.max(0.0f, -floatingActionButton.getTranslationY());
            BottomAppBarTopEdgeTreatment topEdgeTreatment3 = BottomAppBar.this.getTopEdgeTreatment();
            Objects.requireNonNull(topEdgeTreatment3);
            if (topEdgeTreatment3.cradleVerticalOffset != max) {
                BottomAppBarTopEdgeTreatment topEdgeTreatment4 = BottomAppBar.this.getTopEdgeTreatment();
                if (max >= 0.0f) {
                    topEdgeTreatment4.cradleVerticalOffset = max;
                    BottomAppBar.this.materialShapeDrawable.invalidateSelf();
                } else {
                    Objects.requireNonNull(topEdgeTreatment4);
                    throw new IllegalArgumentException("cradleVerticalOffset must be positive.");
                }
            }
            MaterialShapeDrawable materialShapeDrawable = BottomAppBar.this.materialShapeDrawable;
            if (floatingActionButton.getVisibility() == 0) {
                f = floatingActionButton.getScaleY();
            }
            materialShapeDrawable.setInterpolation(f);
        }
    }

    @Override // com.google.android.material.internal.VisibilityAwareImageButton, android.widget.ImageView, android.view.View
    public final void setVisibility(int i) {
        internalSetVisibility(i, true);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.AttachedBehavior
    public final CoordinatorLayout.Behavior<FloatingActionButton> getBehavior() {
        return new Behavior();
    }

    public final FloatingActionButtonImpl getImpl() {
        if (this.impl == null) {
            this.impl = new FloatingActionButtonImplLollipop(this, new ShadowDelegateImpl());
        }
        return this.impl;
    }

    public final int getSizeDimension(int i) {
        int i2 = this.customSize;
        if (i2 != 0) {
            return i2;
        }
        Resources resources = getResources();
        if (i != -1) {
            if (i != 1) {
                return resources.getDimensionPixelSize(2131165627);
            }
            return resources.getDimensionPixelSize(2131165626);
        } else if (Math.max(resources.getConfiguration().screenWidthDp, resources.getConfiguration().screenHeightDp) < 470) {
            return getSizeDimension(1);
        } else {
            return getSizeDimension(0);
        }
    }

    @Override // com.google.android.material.expandable.ExpandableWidget
    public final boolean isExpanded() {
        ExpandableWidgetHelper expandableWidgetHelper = this.expandableWidgetHelper;
        Objects.requireNonNull(expandableWidgetHelper);
        return expandableWidgetHelper.expanded;
    }

    public final void offsetRectWithShadow(Rect rect) {
        int i = rect.left;
        Rect rect2 = this.shadowPadding;
        rect.left = i + rect2.left;
        rect.top += rect2.top;
        rect.right -= rect2.right;
        rect.bottom -= rect2.bottom;
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onMeasure(int i, int i2) {
        int sizeDimension = getSizeDimension(this.size);
        this.imagePadding = (sizeDimension - this.maxImageSize) / 2;
        getImpl().updatePadding();
        int min = Math.min(resolveAdjustedSize(sizeDimension, i), resolveAdjustedSize(sizeDimension, i2));
        Rect rect = this.shadowPadding;
        setMeasuredDimension(rect.left + min + rect.right, min + rect.top + rect.bottom);
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof ExtendableSavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        ExtendableSavedState extendableSavedState = (ExtendableSavedState) parcelable;
        Objects.requireNonNull(extendableSavedState);
        super.onRestoreInstanceState(extendableSavedState.mSuperState);
        ExpandableWidgetHelper expandableWidgetHelper = this.expandableWidgetHelper;
        SimpleArrayMap<String, Bundle> simpleArrayMap = extendableSavedState.extendableStates;
        Objects.requireNonNull(simpleArrayMap);
        Bundle orDefault = simpleArrayMap.getOrDefault("expandableWidgetHelper", null);
        Objects.requireNonNull(orDefault);
        Bundle bundle = orDefault;
        Objects.requireNonNull(expandableWidgetHelper);
        expandableWidgetHelper.expanded = bundle.getBoolean("expanded", false);
        expandableWidgetHelper.expandedComponentIdHint = bundle.getInt("expandedComponentIdHint", 0);
        if (expandableWidgetHelper.expanded) {
            ViewParent parent = expandableWidgetHelper.widget.getParent();
            if (parent instanceof CoordinatorLayout) {
                ((CoordinatorLayout) parent).dispatchDependentViewsChanged(expandableWidgetHelper.widget);
            }
        }
    }

    @Override // android.view.View
    public final void setBackgroundColor(int i) {
        Log.i("FloatingActionButton", "Setting a custom background is not supported.");
    }

    @Override // android.view.View
    public final void setBackgroundDrawable(Drawable drawable) {
        Log.i("FloatingActionButton", "Setting a custom background is not supported.");
    }

    @Override // android.view.View
    public final void setBackgroundResource(int i) {
        Log.i("FloatingActionButton", "Setting a custom background is not supported.");
    }

    @Override // android.view.View
    public final void setBackgroundTintList(ColorStateList colorStateList) {
        if (this.backgroundTint != colorStateList) {
            this.backgroundTint = colorStateList;
            FloatingActionButtonImpl impl = getImpl();
            Objects.requireNonNull(impl);
            MaterialShapeDrawable materialShapeDrawable = impl.shapeDrawable;
            if (materialShapeDrawable != null) {
                materialShapeDrawable.setTintList(colorStateList);
            }
            BorderDrawable borderDrawable = impl.borderDrawable;
            if (borderDrawable != null) {
                if (colorStateList != null) {
                    borderDrawable.currentBorderTintColor = colorStateList.getColorForState(borderDrawable.getState(), borderDrawable.currentBorderTintColor);
                }
                borderDrawable.borderTint = colorStateList;
                borderDrawable.invalidateShader = true;
                borderDrawable.invalidateSelf();
            }
        }
    }

    @Override // android.view.View
    public final void setBackgroundTintMode(PorterDuff.Mode mode) {
        if (this.backgroundTintMode != mode) {
            this.backgroundTintMode = mode;
            FloatingActionButtonImpl impl = getImpl();
            Objects.requireNonNull(impl);
            MaterialShapeDrawable materialShapeDrawable = impl.shapeDrawable;
            if (materialShapeDrawable != null) {
                materialShapeDrawable.setTintMode(mode);
            }
        }
    }

    @Override // android.widget.ImageView
    public final void setImageResource(int i) {
        this.imageHelper.setImageResource(i);
        onApplySupportImageTint();
    }

    public FloatingActionButton(Context context, AttributeSet attributeSet) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, 2130969066, 2132018387), attributeSet, 2130969066);
        Context context2 = getContext();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.FloatingActionButton, 2130969066, 2132018387, new int[0]);
        this.backgroundTint = MaterialResources.getColorStateList(context2, obtainStyledAttributes, 1);
        this.backgroundTintMode = ViewUtils.parseTintMode(obtainStyledAttributes.getInt(2, -1), null);
        this.rippleColor = MaterialResources.getColorStateList(context2, obtainStyledAttributes, 12);
        this.size = obtainStyledAttributes.getInt(7, -1);
        this.customSize = obtainStyledAttributes.getDimensionPixelSize(6, 0);
        this.borderWidth = obtainStyledAttributes.getDimensionPixelSize(3, 0);
        float dimension = obtainStyledAttributes.getDimension(4, 0.0f);
        float dimension2 = obtainStyledAttributes.getDimension(9, 0.0f);
        float dimension3 = obtainStyledAttributes.getDimension(11, 0.0f);
        this.compatPadding = obtainStyledAttributes.getBoolean(16, false);
        int dimensionPixelSize = getResources().getDimensionPixelSize(2131166500);
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(10, 0);
        this.maxImageSize = dimensionPixelSize2;
        FloatingActionButtonImpl impl = getImpl();
        Objects.requireNonNull(impl);
        if (impl.maxImageSize != dimensionPixelSize2) {
            impl.maxImageSize = dimensionPixelSize2;
            float f = impl.imageMatrixScale;
            impl.imageMatrixScale = f;
            Matrix matrix = impl.tmpMatrix;
            impl.calculateImageMatrixFromScale(f, matrix);
            impl.view.setImageMatrix(matrix);
        }
        MotionSpec createFromAttribute = MotionSpec.createFromAttribute(context2, obtainStyledAttributes, 15);
        MotionSpec createFromAttribute2 = MotionSpec.createFromAttribute(context2, obtainStyledAttributes, 8);
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel(ShapeAppearanceModel.builder(context2, attributeSet, 2130969066, 2132018387, ShapeAppearanceModel.PILL));
        boolean z = obtainStyledAttributes.getBoolean(5, false);
        setEnabled(obtainStyledAttributes.getBoolean(0, true));
        obtainStyledAttributes.recycle();
        AppCompatImageHelper appCompatImageHelper = new AppCompatImageHelper(this);
        this.imageHelper = appCompatImageHelper;
        appCompatImageHelper.loadFromAttributes(attributeSet, 2130969066);
        this.expandableWidgetHelper = new ExpandableWidgetHelper(this);
        getImpl().setShapeAppearance(shapeAppearanceModel);
        getImpl().initializeBackgroundDrawable(this.backgroundTint, this.backgroundTintMode, this.rippleColor, this.borderWidth);
        FloatingActionButtonImpl impl2 = getImpl();
        Objects.requireNonNull(impl2);
        impl2.minTouchTargetSize = dimensionPixelSize;
        FloatingActionButtonImpl impl3 = getImpl();
        Objects.requireNonNull(impl3);
        if (impl3.elevation != dimension) {
            impl3.elevation = dimension;
            impl3.onElevationsChanged(dimension, impl3.hoveredFocusedTranslationZ, impl3.pressedTranslationZ);
        }
        FloatingActionButtonImpl impl4 = getImpl();
        Objects.requireNonNull(impl4);
        if (impl4.hoveredFocusedTranslationZ != dimension2) {
            impl4.hoveredFocusedTranslationZ = dimension2;
            impl4.onElevationsChanged(impl4.elevation, dimension2, impl4.pressedTranslationZ);
        }
        FloatingActionButtonImpl impl5 = getImpl();
        Objects.requireNonNull(impl5);
        if (impl5.pressedTranslationZ != dimension3) {
            impl5.pressedTranslationZ = dimension3;
            impl5.onElevationsChanged(impl5.elevation, impl5.hoveredFocusedTranslationZ, dimension3);
        }
        FloatingActionButtonImpl impl6 = getImpl();
        Objects.requireNonNull(impl6);
        impl6.showMotionSpec = createFromAttribute;
        FloatingActionButtonImpl impl7 = getImpl();
        Objects.requireNonNull(impl7);
        impl7.hideMotionSpec = createFromAttribute2;
        FloatingActionButtonImpl impl8 = getImpl();
        Objects.requireNonNull(impl8);
        impl8.ensureMinTouchTargetSize = z;
        setScaleType(ImageView.ScaleType.MATRIX);
    }

    public static int resolveAdjustedSize(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i2);
        if (mode == Integer.MIN_VALUE) {
            return Math.min(i, size);
        }
        if (mode == 0) {
            return i;
        }
        if (mode == 1073741824) {
            return size;
        }
        throw new IllegalArgumentException();
    }

    @Override // android.widget.ImageView, android.view.View
    public final void drawableStateChanged() {
        super.drawableStateChanged();
        getImpl().onDrawableStateChanged(getDrawableState());
    }

    public final void hide$1() {
        boolean z;
        AnimatorSet animatorSet;
        final FloatingActionButtonImpl impl = getImpl();
        Objects.requireNonNull(impl);
        boolean z2 = true;
        if (impl.view.getVisibility() != 0 ? impl.animState == 2 : impl.animState != 1) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            Animator animator = impl.currentAnimator;
            if (animator != null) {
                animator.cancel();
            }
            FloatingActionButton floatingActionButton = impl.view;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            if (!ViewCompat.Api19Impl.isLaidOut(floatingActionButton) || impl.view.isInEditMode()) {
                z2 = false;
            }
            if (z2) {
                MotionSpec motionSpec = impl.hideMotionSpec;
                if (motionSpec != null) {
                    animatorSet = impl.createAnimator(motionSpec, 0.0f, 0.0f, 0.0f);
                } else {
                    animatorSet = impl.createDefaultAnimator(0.0f, 0.4f, 0.4f);
                }
                animatorSet.addListener(
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0054: INVOKE  
                      (r0v8 'animatorSet' android.animation.AnimatorSet)
                      (wrap: android.animation.AnimatorListenerAdapter : 0x0051: CONSTRUCTOR  (r1v3 android.animation.AnimatorListenerAdapter A[REMOVE]) = (r4v1 'impl' com.google.android.material.floatingactionbutton.FloatingActionButtonImpl A[DONT_INLINE]) call: com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.1.<init>(com.google.android.material.floatingactionbutton.FloatingActionButtonImpl):void type: CONSTRUCTOR)
                     type: VIRTUAL call: android.animation.Animator.addListener(android.animation.Animator$AnimatorListener):void in method: com.google.android.material.floatingactionbutton.FloatingActionButton.hide$1():void, file: classes.dex
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:241)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:90)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:79)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:122)
                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:79)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:122)
                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:267)
                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:260)
                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:369)
                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:304)
                    	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:270)
                    	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                    	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                    	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                    Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.google.android.material.floatingactionbutton.FloatingActionButtonImpl, state: NOT_LOADED
                    	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:299)
                    	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:676)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:386)
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:140)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:116)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
                    	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:996)
                    	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:807)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:390)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:271)
                    	... 31 more
                    */
                /*
                    this = this;
                    com.google.android.material.floatingactionbutton.FloatingActionButtonImpl r4 = r4.getImpl()
                    java.util.Objects.requireNonNull(r4)
                    com.google.android.material.floatingactionbutton.FloatingActionButton r0 = r4.view
                    int r0 = r0.getVisibility()
                    r1 = 1
                    r2 = 0
                    if (r0 != 0) goto L_0x0016
                    int r0 = r4.animState
                    if (r0 != r1) goto L_0x001d
                    goto L_0x001b
                L_0x0016:
                    int r0 = r4.animState
                    r3 = 2
                    if (r0 == r3) goto L_0x001d
                L_0x001b:
                    r0 = r1
                    goto L_0x001e
                L_0x001d:
                    r0 = r2
                L_0x001e:
                    if (r0 == 0) goto L_0x0021
                    goto L_0x0079
                L_0x0021:
                    android.animation.Animator r0 = r4.currentAnimator
                    if (r0 == 0) goto L_0x0028
                    r0.cancel()
                L_0x0028:
                    com.google.android.material.floatingactionbutton.FloatingActionButton r0 = r4.view
                    java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r3 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
                    boolean r0 = androidx.core.view.ViewCompat.Api19Impl.isLaidOut(r0)
                    if (r0 == 0) goto L_0x003b
                    com.google.android.material.floatingactionbutton.FloatingActionButton r0 = r4.view
                    boolean r0 = r0.isInEditMode()
                    if (r0 != 0) goto L_0x003b
                    goto L_0x003c
                L_0x003b:
                    r1 = r2
                L_0x003c:
                    if (r1 == 0) goto L_0x0073
                    com.google.android.material.animation.MotionSpec r0 = r4.hideMotionSpec
                    r1 = 0
                    if (r0 == 0) goto L_0x0048
                    android.animation.AnimatorSet r0 = r4.createAnimator(r0, r1, r1, r1)
                    goto L_0x004f
                L_0x0048:
                    r0 = 1053609165(0x3ecccccd, float:0.4)
                    android.animation.AnimatorSet r0 = r4.createDefaultAnimator(r1, r0, r0)
                L_0x004f:
                    com.google.android.material.floatingactionbutton.FloatingActionButtonImpl$1 r1 = new com.google.android.material.floatingactionbutton.FloatingActionButtonImpl$1
                    r1.<init>(r4)
                    r0.addListener(r1)
                    java.util.ArrayList<android.animation.Animator$AnimatorListener> r4 = r4.hideListeners
                    if (r4 == 0) goto L_0x006f
                    java.util.Iterator r4 = r4.iterator()
                L_0x005f:
                    boolean r1 = r4.hasNext()
                    if (r1 == 0) goto L_0x006f
                    java.lang.Object r1 = r4.next()
                    android.animation.Animator$AnimatorListener r1 = (android.animation.Animator.AnimatorListener) r1
                    r0.addListener(r1)
                    goto L_0x005f
                L_0x006f:
                    r0.start()
                    goto L_0x0079
                L_0x0073:
                    com.google.android.material.floatingactionbutton.FloatingActionButton r4 = r4.view
                    r0 = 4
                    r4.internalSetVisibility(r0, r2)
                L_0x0079:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.floatingactionbutton.FloatingActionButton.hide$1():void");
            }

            @Override // android.widget.ImageView, android.view.View
            public final void jumpDrawablesToCurrentState() {
                super.jumpDrawablesToCurrentState();
                getImpl().jumpDrawableToCurrentState();
            }

            public final void onApplySupportImageTint() {
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    ColorStateList colorStateList = this.imageTint;
                    if (colorStateList == null) {
                        drawable.clearColorFilter();
                        return;
                    }
                    int colorForState = colorStateList.getColorForState(getDrawableState(), 0);
                    PorterDuff.Mode mode = this.imageMode;
                    if (mode == null) {
                        mode = PorterDuff.Mode.SRC_IN;
                    }
                    drawable.mutate().setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(colorForState, mode));
                }
            }

            /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.material.floatingactionbutton.FloatingActionButtonImpl$6] */
            @Override // android.widget.ImageView, android.view.View
            public final void onAttachedToWindow() {
                super.onAttachedToWindow();
                final FloatingActionButtonImpl impl = getImpl();
                Objects.requireNonNull(impl);
                MaterialShapeDrawable materialShapeDrawable = impl.shapeDrawable;
                if (materialShapeDrawable != null) {
                    R$bool.setParentAbsoluteElevation(impl.view, materialShapeDrawable);
                }
                if (!(impl instanceof FloatingActionButtonImplLollipop)) {
                    ViewTreeObserver viewTreeObserver = impl.view.getViewTreeObserver();
                    if (impl.preDrawListener == null) {
                        impl.preDrawListener = 
                        /*  JADX ERROR: Method code generation error
                            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0028: IPUT  
                              (wrap: ?? : 0x0025: CONSTRUCTOR  (r1v1 ?? I:com.google.android.material.floatingactionbutton.FloatingActionButtonImpl$6 A[REMOVE]) = (r2v1 'impl' com.google.android.material.floatingactionbutton.FloatingActionButtonImpl A[DONT_INLINE]) call: com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.6.<init>(com.google.android.material.floatingactionbutton.FloatingActionButtonImpl):void type: CONSTRUCTOR)
                              (r2v1 'impl' com.google.android.material.floatingactionbutton.FloatingActionButtonImpl)
                             com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.preDrawListener com.google.android.material.floatingactionbutton.FloatingActionButtonImpl$6 in method: com.google.android.material.floatingactionbutton.FloatingActionButton.onAttachedToWindow():void, file: classes.dex
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:241)
                            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:90)
                            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                            	at jadx.core.dex.regions.Region.generate(Region.java:35)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                            	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:79)
                            	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:122)
                            	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                            	at jadx.core.dex.regions.Region.generate(Region.java:35)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                            	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:79)
                            	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:122)
                            	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                            	at jadx.core.dex.regions.Region.generate(Region.java:35)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                            	at jadx.core.dex.regions.Region.generate(Region.java:35)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:267)
                            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:260)
                            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:369)
                            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:304)
                            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:270)
                            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                            	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                            Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.google.android.material.floatingactionbutton.FloatingActionButtonImpl, state: NOT_LOADED
                            	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:299)
                            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:676)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:386)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:140)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:116)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:455)
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:271)
                            	... 29 more
                            */
                        /*
                            this = this;
                            super.onAttachedToWindow()
                            com.google.android.material.floatingactionbutton.FloatingActionButtonImpl r2 = r2.getImpl()
                            java.util.Objects.requireNonNull(r2)
                            com.google.android.material.shape.MaterialShapeDrawable r0 = r2.shapeDrawable
                            if (r0 == 0) goto L_0x0013
                            com.google.android.material.floatingactionbutton.FloatingActionButton r1 = r2.view
                            androidx.mediarouter.R$bool.setParentAbsoluteElevation(r1, r0)
                        L_0x0013:
                            boolean r0 = r2 instanceof com.google.android.material.floatingactionbutton.FloatingActionButtonImplLollipop
                            r0 = r0 ^ 1
                            if (r0 == 0) goto L_0x002f
                            com.google.android.material.floatingactionbutton.FloatingActionButton r0 = r2.view
                            android.view.ViewTreeObserver r0 = r0.getViewTreeObserver()
                            com.google.android.material.floatingactionbutton.FloatingActionButtonImpl$6 r1 = r2.preDrawListener
                            if (r1 != 0) goto L_0x002a
                            com.google.android.material.floatingactionbutton.FloatingActionButtonImpl$6 r1 = new com.google.android.material.floatingactionbutton.FloatingActionButtonImpl$6
                            r1.<init>(r2)
                            r2.preDrawListener = r1
                        L_0x002a:
                            com.google.android.material.floatingactionbutton.FloatingActionButtonImpl$6 r2 = r2.preDrawListener
                            r0.addOnPreDrawListener(r2)
                        L_0x002f:
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.floatingactionbutton.FloatingActionButton.onAttachedToWindow():void");
                    }

                    @Override // android.widget.ImageView, android.view.View
                    public final void onDetachedFromWindow() {
                        super.onDetachedFromWindow();
                        FloatingActionButtonImpl impl = getImpl();
                        Objects.requireNonNull(impl);
                        ViewTreeObserver viewTreeObserver = impl.view.getViewTreeObserver();
                        FloatingActionButtonImpl.AnonymousClass6 r1 = impl.preDrawListener;
                        if (r1 != null) {
                            viewTreeObserver.removeOnPreDrawListener(r1);
                            impl.preDrawListener = null;
                        }
                    }

                    @Override // android.view.View
                    public final Parcelable onSaveInstanceState() {
                        Parcelable onSaveInstanceState = super.onSaveInstanceState();
                        if (onSaveInstanceState == null) {
                            onSaveInstanceState = new Bundle();
                        }
                        ExtendableSavedState extendableSavedState = new ExtendableSavedState(onSaveInstanceState);
                        SimpleArrayMap<String, Bundle> simpleArrayMap = extendableSavedState.extendableStates;
                        ExpandableWidgetHelper expandableWidgetHelper = this.expandableWidgetHelper;
                        Objects.requireNonNull(expandableWidgetHelper);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("expanded", expandableWidgetHelper.expanded);
                        bundle.putInt("expandedComponentIdHint", expandableWidgetHelper.expandedComponentIdHint);
                        simpleArrayMap.put("expandableWidgetHelper", bundle);
                        return extendableSavedState;
                    }

                    @Override // android.view.View
                    public final boolean onTouchEvent(MotionEvent motionEvent) {
                        boolean z;
                        if (motionEvent.getAction() == 0) {
                            Rect rect = this.touchArea;
                            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                            if (ViewCompat.Api19Impl.isLaidOut(this)) {
                                rect.set(0, 0, getWidth(), getHeight());
                                offsetRectWithShadow(rect);
                                z = true;
                            } else {
                                z = false;
                            }
                            if (z && !this.touchArea.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                                return false;
                            }
                        }
                        return super.onTouchEvent(motionEvent);
                    }

                    @Override // android.view.View
                    public final void setElevation(float f) {
                        super.setElevation(f);
                        FloatingActionButtonImpl impl = getImpl();
                        Objects.requireNonNull(impl);
                        MaterialShapeDrawable materialShapeDrawable = impl.shapeDrawable;
                        if (materialShapeDrawable != null) {
                            materialShapeDrawable.setElevation(f);
                        }
                    }

                    @Override // android.widget.ImageView
                    public final void setImageDrawable(Drawable drawable) {
                        if (getDrawable() != drawable) {
                            super.setImageDrawable(drawable);
                            FloatingActionButtonImpl impl = getImpl();
                            Objects.requireNonNull(impl);
                            float f = impl.imageMatrixScale;
                            impl.imageMatrixScale = f;
                            Matrix matrix = impl.tmpMatrix;
                            impl.calculateImageMatrixFromScale(f, matrix);
                            impl.view.setImageMatrix(matrix);
                            if (this.imageTint != null) {
                                onApplySupportImageTint();
                            }
                        }
                    }

                    @Override // android.view.View
                    public final void setScaleX(float f) {
                        super.setScaleX(f);
                        FloatingActionButtonImpl impl = getImpl();
                        Objects.requireNonNull(impl);
                        ArrayList<FloatingActionButtonImpl.InternalTransformationCallback> arrayList = impl.transformationCallbacks;
                        if (arrayList != null) {
                            Iterator<FloatingActionButtonImpl.InternalTransformationCallback> it = arrayList.iterator();
                            while (it.hasNext()) {
                                it.next().onScaleChanged();
                            }
                        }
                    }

                    @Override // android.view.View
                    public final void setScaleY(float f) {
                        super.setScaleY(f);
                        FloatingActionButtonImpl impl = getImpl();
                        Objects.requireNonNull(impl);
                        ArrayList<FloatingActionButtonImpl.InternalTransformationCallback> arrayList = impl.transformationCallbacks;
                        if (arrayList != null) {
                            Iterator<FloatingActionButtonImpl.InternalTransformationCallback> it = arrayList.iterator();
                            while (it.hasNext()) {
                                it.next().onScaleChanged();
                            }
                        }
                    }

                    public void setShadowPaddingEnabled(boolean z) {
                        FloatingActionButtonImpl impl = getImpl();
                        Objects.requireNonNull(impl);
                        impl.shadowPaddingEnabled = z;
                        impl.updatePadding();
                    }

                    @Override // com.google.android.material.shape.Shapeable
                    public final void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
                        getImpl().setShapeAppearance(shapeAppearanceModel);
                    }

                    @Override // android.view.View
                    public final void setTranslationX(float f) {
                        super.setTranslationX(f);
                        getImpl().onTranslationChanged();
                    }

                    @Override // android.view.View
                    public final void setTranslationY(float f) {
                        super.setTranslationY(f);
                        getImpl().onTranslationChanged();
                    }

                    @Override // android.view.View
                    public final void setTranslationZ(float f) {
                        super.setTranslationZ(f);
                        getImpl().onTranslationChanged();
                    }

                    public final void show$5() {
                        boolean z;
                        boolean z2;
                        AnimatorSet animatorSet;
                        float f;
                        float f2;
                        final FloatingActionButtonImpl impl = getImpl();
                        Objects.requireNonNull(impl);
                        boolean z3 = true;
                        if (impl.view.getVisibility() == 0 ? impl.animState == 1 : impl.animState != 2) {
                            z = false;
                        } else {
                            z = true;
                        }
                        if (!z) {
                            Animator animator = impl.currentAnimator;
                            if (animator != null) {
                                animator.cancel();
                            }
                            if (impl.showMotionSpec == null) {
                                z2 = true;
                            } else {
                                z2 = false;
                            }
                            FloatingActionButton floatingActionButton = impl.view;
                            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                            if (!ViewCompat.Api19Impl.isLaidOut(floatingActionButton) || impl.view.isInEditMode()) {
                                z3 = false;
                            }
                            if (z3) {
                                if (impl.view.getVisibility() != 0) {
                                    float f3 = 0.0f;
                                    impl.view.setAlpha(0.0f);
                                    FloatingActionButton floatingActionButton2 = impl.view;
                                    if (z2) {
                                        f = 0.4f;
                                    } else {
                                        f = 0.0f;
                                    }
                                    floatingActionButton2.setScaleY(f);
                                    FloatingActionButton floatingActionButton3 = impl.view;
                                    if (z2) {
                                        f2 = 0.4f;
                                    } else {
                                        f2 = 0.0f;
                                    }
                                    floatingActionButton3.setScaleX(f2);
                                    if (z2) {
                                        f3 = 0.4f;
                                    }
                                    impl.imageMatrixScale = f3;
                                    Matrix matrix = impl.tmpMatrix;
                                    impl.calculateImageMatrixFromScale(f3, matrix);
                                    impl.view.setImageMatrix(matrix);
                                }
                                MotionSpec motionSpec = impl.showMotionSpec;
                                if (motionSpec != null) {
                                    animatorSet = impl.createAnimator(motionSpec, 1.0f, 1.0f, 1.0f);
                                } else {
                                    animatorSet = impl.createDefaultAnimator(1.0f, 1.0f, 1.0f);
                                }
                                animatorSet.addListener(
                                /*  JADX ERROR: Method code generation error
                                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x008f: INVOKE  
                                      (r0v12 'animatorSet' android.animation.AnimatorSet)
                                      (wrap: android.animation.AnimatorListenerAdapter : 0x008c: CONSTRUCTOR  (r1v4 android.animation.AnimatorListenerAdapter A[REMOVE]) = (r6v1 'impl' com.google.android.material.floatingactionbutton.FloatingActionButtonImpl A[DONT_INLINE]) call: com.google.android.material.floatingactionbutton.FloatingActionButtonImpl.2.<init>(com.google.android.material.floatingactionbutton.FloatingActionButtonImpl):void type: CONSTRUCTOR)
                                     type: VIRTUAL call: android.animation.Animator.addListener(android.animation.Animator$AnimatorListener):void in method: com.google.android.material.floatingactionbutton.FloatingActionButton.show$5():void, file: classes.dex
                                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:241)
                                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:90)
                                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:79)
                                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:122)
                                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:79)
                                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:122)
                                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:267)
                                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:260)
                                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:369)
                                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:304)
                                    	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:270)
                                    	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                                    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                                    	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                                    	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                                    Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.google.android.material.floatingactionbutton.FloatingActionButtonImpl, state: NOT_LOADED
                                    	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:299)
                                    	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:676)
                                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:386)
                                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:140)
                                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:116)
                                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
                                    	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:996)
                                    	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:807)
                                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:390)
                                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:271)
                                    	... 31 more
                                    */
                                /*
                                    this = this;
                                    com.google.android.material.floatingactionbutton.FloatingActionButtonImpl r6 = r6.getImpl()
                                    java.util.Objects.requireNonNull(r6)
                                    com.google.android.material.floatingactionbutton.FloatingActionButton r0 = r6.view
                                    int r0 = r0.getVisibility()
                                    r1 = 1
                                    r2 = 0
                                    if (r0 == 0) goto L_0x001a
                                    int r0 = r6.animState
                                    r3 = 2
                                    if (r0 != r3) goto L_0x0018
                                L_0x0016:
                                    r0 = r1
                                    goto L_0x001f
                                L_0x0018:
                                    r0 = r2
                                    goto L_0x001f
                                L_0x001a:
                                    int r0 = r6.animState
                                    if (r0 == r1) goto L_0x0018
                                    goto L_0x0016
                                L_0x001f:
                                    if (r0 == 0) goto L_0x0023
                                    goto L_0x00ce
                                L_0x0023:
                                    android.animation.Animator r0 = r6.currentAnimator
                                    if (r0 == 0) goto L_0x002a
                                    r0.cancel()
                                L_0x002a:
                                    com.google.android.material.animation.MotionSpec r0 = r6.showMotionSpec
                                    if (r0 != 0) goto L_0x0030
                                    r0 = r1
                                    goto L_0x0031
                                L_0x0030:
                                    r0 = r2
                                L_0x0031:
                                    com.google.android.material.floatingactionbutton.FloatingActionButton r3 = r6.view
                                    java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r4 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
                                    boolean r3 = androidx.core.view.ViewCompat.Api19Impl.isLaidOut(r3)
                                    if (r3 == 0) goto L_0x0044
                                    com.google.android.material.floatingactionbutton.FloatingActionButton r3 = r6.view
                                    boolean r3 = r3.isInEditMode()
                                    if (r3 != 0) goto L_0x0044
                                    goto L_0x0045
                                L_0x0044:
                                    r1 = r2
                                L_0x0045:
                                    r3 = 1065353216(0x3f800000, float:1.0)
                                    if (r1 == 0) goto L_0x00ae
                                    com.google.android.material.floatingactionbutton.FloatingActionButton r1 = r6.view
                                    int r1 = r1.getVisibility()
                                    if (r1 == 0) goto L_0x007d
                                    com.google.android.material.floatingactionbutton.FloatingActionButton r1 = r6.view
                                    r2 = 0
                                    r1.setAlpha(r2)
                                    com.google.android.material.floatingactionbutton.FloatingActionButton r1 = r6.view
                                    r4 = 1053609165(0x3ecccccd, float:0.4)
                                    if (r0 == 0) goto L_0x0060
                                    r5 = r4
                                    goto L_0x0061
                                L_0x0060:
                                    r5 = r2
                                L_0x0061:
                                    r1.setScaleY(r5)
                                    com.google.android.material.floatingactionbutton.FloatingActionButton r1 = r6.view
                                    if (r0 == 0) goto L_0x006a
                                    r5 = r4
                                    goto L_0x006b
                                L_0x006a:
                                    r5 = r2
                                L_0x006b:
                                    r1.setScaleX(r5)
                                    if (r0 == 0) goto L_0x0071
                                    r2 = r4
                                L_0x0071:
                                    r6.imageMatrixScale = r2
                                    android.graphics.Matrix r0 = r6.tmpMatrix
                                    r6.calculateImageMatrixFromScale(r2, r0)
                                    com.google.android.material.floatingactionbutton.FloatingActionButton r1 = r6.view
                                    r1.setImageMatrix(r0)
                                L_0x007d:
                                    com.google.android.material.animation.MotionSpec r0 = r6.showMotionSpec
                                    if (r0 == 0) goto L_0x0086
                                    android.animation.AnimatorSet r0 = r6.createAnimator(r0, r3, r3, r3)
                                    goto L_0x008a
                                L_0x0086:
                                    android.animation.AnimatorSet r0 = r6.createDefaultAnimator(r3, r3, r3)
                                L_0x008a:
                                    com.google.android.material.floatingactionbutton.FloatingActionButtonImpl$2 r1 = new com.google.android.material.floatingactionbutton.FloatingActionButtonImpl$2
                                    r1.<init>(r6)
                                    r0.addListener(r1)
                                    java.util.ArrayList<android.animation.Animator$AnimatorListener> r6 = r6.showListeners
                                    if (r6 == 0) goto L_0x00aa
                                    java.util.Iterator r6 = r6.iterator()
                                L_0x009a:
                                    boolean r1 = r6.hasNext()
                                    if (r1 == 0) goto L_0x00aa
                                    java.lang.Object r1 = r6.next()
                                    android.animation.Animator$AnimatorListener r1 = (android.animation.Animator.AnimatorListener) r1
                                    r0.addListener(r1)
                                    goto L_0x009a
                                L_0x00aa:
                                    r0.start()
                                    goto L_0x00ce
                                L_0x00ae:
                                    com.google.android.material.floatingactionbutton.FloatingActionButton r0 = r6.view
                                    r0.internalSetVisibility(r2, r2)
                                    com.google.android.material.floatingactionbutton.FloatingActionButton r0 = r6.view
                                    r0.setAlpha(r3)
                                    com.google.android.material.floatingactionbutton.FloatingActionButton r0 = r6.view
                                    r0.setScaleY(r3)
                                    com.google.android.material.floatingactionbutton.FloatingActionButton r0 = r6.view
                                    r0.setScaleX(r3)
                                    r6.imageMatrixScale = r3
                                    android.graphics.Matrix r0 = r6.tmpMatrix
                                    r6.calculateImageMatrixFromScale(r3, r0)
                                    com.google.android.material.floatingactionbutton.FloatingActionButton r6 = r6.view
                                    r6.setImageMatrix(r0)
                                L_0x00ce:
                                    return
                                */
                                throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.floatingactionbutton.FloatingActionButton.show$5():void");
                            }

                            @Override // android.view.View
                            public final ColorStateList getBackgroundTintList() {
                                return this.backgroundTint;
                            }

                            @Override // android.view.View
                            public final PorterDuff.Mode getBackgroundTintMode() {
                                return this.backgroundTintMode;
                            }
                        }
