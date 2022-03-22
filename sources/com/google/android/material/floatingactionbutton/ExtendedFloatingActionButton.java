package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import com.android.framework.protobuf.nano.CodedOutputByteBufferNano;
import com.google.android.material.R$styleable;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class ExtendedFloatingActionButton extends MaterialButton implements CoordinatorLayout.AttachedBehavior {
    public int animState;
    public final ExtendedFloatingActionButtonBehavior behavior;
    public final int collapsedSize;
    public final ChangeSizeStrategy extendStrategy;
    public int extendedPaddingEnd;
    public int extendedPaddingStart;
    public final HideStrategy hideStrategy;
    public boolean isExtended;
    public boolean isTransforming;
    public ColorStateList originalTextCsl;
    public final ShowStrategy showStrategy;
    public final ChangeSizeStrategy shrinkStrategy;
    public static final AnonymousClass4 WIDTH = new Property<View, Float>() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.4
        @Override // android.util.Property
        public final Float get(View view) {
            return Float.valueOf(view.getLayoutParams().width);
        }

        @Override // android.util.Property
        public final void set(View view, Float f) {
            View view2 = view;
            view2.getLayoutParams().width = f.intValue();
            view2.requestLayout();
        }
    };
    public static final AnonymousClass5 HEIGHT = new Property<View, Float>() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.5
        @Override // android.util.Property
        public final Float get(View view) {
            return Float.valueOf(view.getLayoutParams().height);
        }

        @Override // android.util.Property
        public final void set(View view, Float f) {
            View view2 = view;
            view2.getLayoutParams().height = f.intValue();
            view2.requestLayout();
        }
    };
    public static final AnonymousClass6 PADDING_START = new Property<View, Float>() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.6
        @Override // android.util.Property
        public final Float get(View view) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            return Float.valueOf(ViewCompat.Api17Impl.getPaddingStart(view));
        }

        @Override // android.util.Property
        public final void set(View view, Float f) {
            View view2 = view;
            int intValue = f.intValue();
            int paddingTop = view2.getPaddingTop();
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api17Impl.setPaddingRelative(view2, intValue, paddingTop, ViewCompat.Api17Impl.getPaddingEnd(view2), view2.getPaddingBottom());
        }
    };
    public static final AnonymousClass7 PADDING_END = new Property<View, Float>() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.7
        @Override // android.util.Property
        public final Float get(View view) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            return Float.valueOf(ViewCompat.Api17Impl.getPaddingEnd(view));
        }

        @Override // android.util.Property
        public final void set(View view, Float f) {
            View view2 = view;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api17Impl.setPaddingRelative(view2, ViewCompat.Api17Impl.getPaddingStart(view2), view2.getPaddingTop(), f.intValue(), view2.getPaddingBottom());
        }
    };

    /* loaded from: classes.dex */
    public class ChangeSizeStrategy extends BaseMotionStrategy {
        public final boolean extending;
        public final Size size;

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final void onChange() {
        }

        public ChangeSizeStrategy(CodedOutputByteBufferNano codedOutputByteBufferNano, Size size, boolean z) {
            super(ExtendedFloatingActionButton.this, codedOutputByteBufferNano);
            this.size = size;
            this.extending = z;
        }

        @Override // com.google.android.material.floatingactionbutton.BaseMotionStrategy, com.google.android.material.floatingactionbutton.MotionStrategy
        public final AnimatorSet createAnimator() {
            float f;
            MotionSpec motionSpec = this.motionSpec;
            if (motionSpec == null) {
                if (this.defaultMotionSpec == null) {
                    this.defaultMotionSpec = MotionSpec.createFromResource(this.context, getDefaultMotionSpecResource());
                }
                motionSpec = this.defaultMotionSpec;
                Objects.requireNonNull(motionSpec);
            }
            if (motionSpec.hasPropertyValues("width")) {
                PropertyValuesHolder[] propertyValues = motionSpec.getPropertyValues("width");
                propertyValues[0].setFloatValues(ExtendedFloatingActionButton.this.getWidth(), this.size.getWidth());
                motionSpec.setPropertyValues("width", propertyValues);
            }
            if (motionSpec.hasPropertyValues("height")) {
                PropertyValuesHolder[] propertyValues2 = motionSpec.getPropertyValues("height");
                propertyValues2[0].setFloatValues(ExtendedFloatingActionButton.this.getHeight(), this.size.getHeight());
                motionSpec.setPropertyValues("height", propertyValues2);
            }
            if (motionSpec.hasPropertyValues("paddingStart")) {
                PropertyValuesHolder[] propertyValues3 = motionSpec.getPropertyValues("paddingStart");
                PropertyValuesHolder propertyValuesHolder = propertyValues3[0];
                ExtendedFloatingActionButton extendedFloatingActionButton = ExtendedFloatingActionButton.this;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                propertyValuesHolder.setFloatValues(ViewCompat.Api17Impl.getPaddingStart(extendedFloatingActionButton), this.size.getPaddingStart());
                motionSpec.setPropertyValues("paddingStart", propertyValues3);
            }
            if (motionSpec.hasPropertyValues("paddingEnd")) {
                PropertyValuesHolder[] propertyValues4 = motionSpec.getPropertyValues("paddingEnd");
                PropertyValuesHolder propertyValuesHolder2 = propertyValues4[0];
                ExtendedFloatingActionButton extendedFloatingActionButton2 = ExtendedFloatingActionButton.this;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                propertyValuesHolder2.setFloatValues(ViewCompat.Api17Impl.getPaddingEnd(extendedFloatingActionButton2), this.size.getPaddingEnd());
                motionSpec.setPropertyValues("paddingEnd", propertyValues4);
            }
            if (motionSpec.hasPropertyValues("labelOpacity")) {
                PropertyValuesHolder[] propertyValues5 = motionSpec.getPropertyValues("labelOpacity");
                boolean z = this.extending;
                float f2 = 0.0f;
                if (z) {
                    f = 0.0f;
                } else {
                    f = 1.0f;
                }
                if (z) {
                    f2 = 1.0f;
                }
                propertyValues5[0].setFloatValues(f, f2);
                motionSpec.setPropertyValues("labelOpacity", propertyValues5);
            }
            return createAnimator(motionSpec);
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final int getDefaultMotionSpecResource() {
            if (this.extending) {
                return 2130837548;
            }
            return 2130837547;
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final void onAnimationEnd() {
            CodedOutputByteBufferNano codedOutputByteBufferNano = this.tracker;
            Objects.requireNonNull(codedOutputByteBufferNano);
            codedOutputByteBufferNano.buffer = null;
            ExtendedFloatingActionButton extendedFloatingActionButton = ExtendedFloatingActionButton.this;
            extendedFloatingActionButton.isTransforming = false;
            extendedFloatingActionButton.setHorizontallyScrolling(false);
            ViewGroup.LayoutParams layoutParams = ExtendedFloatingActionButton.this.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.width = this.size.getLayoutParams().width;
                layoutParams.height = this.size.getLayoutParams().height;
            }
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final void performNow() {
            ExtendedFloatingActionButton extendedFloatingActionButton = ExtendedFloatingActionButton.this;
            extendedFloatingActionButton.isExtended = this.extending;
            ViewGroup.LayoutParams layoutParams = extendedFloatingActionButton.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.width = this.size.getLayoutParams().width;
                layoutParams.height = this.size.getLayoutParams().height;
                ExtendedFloatingActionButton extendedFloatingActionButton2 = ExtendedFloatingActionButton.this;
                int paddingStart = this.size.getPaddingStart();
                int paddingTop = ExtendedFloatingActionButton.this.getPaddingTop();
                int paddingEnd = this.size.getPaddingEnd();
                int paddingBottom = ExtendedFloatingActionButton.this.getPaddingBottom();
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                ViewCompat.Api17Impl.setPaddingRelative(extendedFloatingActionButton2, paddingStart, paddingTop, paddingEnd, paddingBottom);
                ExtendedFloatingActionButton.this.requestLayout();
            }
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final boolean shouldCancel() {
            boolean z = this.extending;
            ExtendedFloatingActionButton extendedFloatingActionButton = ExtendedFloatingActionButton.this;
            if (z != extendedFloatingActionButton.isExtended) {
                Objects.requireNonNull(extendedFloatingActionButton);
                if (extendedFloatingActionButton.icon != null && !TextUtils.isEmpty(ExtendedFloatingActionButton.this.getText())) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.android.material.floatingactionbutton.BaseMotionStrategy, com.google.android.material.floatingactionbutton.MotionStrategy
        public final void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            ExtendedFloatingActionButton extendedFloatingActionButton = ExtendedFloatingActionButton.this;
            extendedFloatingActionButton.isExtended = this.extending;
            extendedFloatingActionButton.isTransforming = true;
            extendedFloatingActionButton.setHorizontallyScrolling(true);
        }
    }

    /* loaded from: classes.dex */
    public static class ExtendedFloatingActionButtonBehavior<T extends ExtendedFloatingActionButton> extends CoordinatorLayout.Behavior<T> {
        public boolean autoHideEnabled;
        public boolean autoShrinkEnabled;
        public Rect tmpRect;

        public ExtendedFloatingActionButtonBehavior() {
            this.autoHideEnabled = false;
            this.autoShrinkEnabled = true;
        }

        public void setInternalAutoHideCallback(OnChangedCallback onChangedCallback) {
        }

        public void setInternalAutoShrinkCallback(OnChangedCallback onChangedCallback) {
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public final /* bridge */ /* synthetic */ boolean getInsetDodgeRect(View view, Rect rect) {
            ExtendedFloatingActionButton extendedFloatingActionButton = (ExtendedFloatingActionButton) view;
            return false;
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
            ExtendedFloatingActionButton extendedFloatingActionButton = (ExtendedFloatingActionButton) view;
            if (view2 instanceof AppBarLayout) {
                updateFabVisibilityForAppBarLayout(coordinatorLayout, (AppBarLayout) view2, extendedFloatingActionButton);
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
                    updateFabVisibilityForBottomSheet(view2, extendedFloatingActionButton);
                }
            }
            return false;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public final boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i) {
            boolean z;
            ExtendedFloatingActionButton extendedFloatingActionButton = (ExtendedFloatingActionButton) view;
            List<View> dependencies = coordinatorLayout.getDependencies(extendedFloatingActionButton);
            int size = dependencies.size();
            for (int i2 = 0; i2 < size; i2++) {
                View view2 = dependencies.get(i2);
                if (!(view2 instanceof AppBarLayout)) {
                    ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
                    if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                        CoordinatorLayout.LayoutParams layoutParams2 = (CoordinatorLayout.LayoutParams) layoutParams;
                        Objects.requireNonNull(layoutParams2);
                        z = layoutParams2.mBehavior instanceof BottomSheetBehavior;
                    } else {
                        z = false;
                    }
                    if (z && updateFabVisibilityForBottomSheet(view2, extendedFloatingActionButton)) {
                        break;
                    }
                } else if (updateFabVisibilityForAppBarLayout(coordinatorLayout, (AppBarLayout) view2, extendedFloatingActionButton)) {
                    break;
                }
            }
            coordinatorLayout.onLayoutChild(extendedFloatingActionButton, i);
            return true;
        }

        public final boolean shouldUpdateVisibility(View view, ExtendedFloatingActionButton extendedFloatingActionButton) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) extendedFloatingActionButton.getLayoutParams();
            if (!this.autoHideEnabled && !this.autoShrinkEnabled) {
                return false;
            }
            Objects.requireNonNull(layoutParams);
            if (layoutParams.mAnchorId != view.getId()) {
                return false;
            }
            return true;
        }

        public final boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, ExtendedFloatingActionButton extendedFloatingActionButton) {
            BaseMotionStrategy baseMotionStrategy;
            BaseMotionStrategy baseMotionStrategy2;
            if (!shouldUpdateVisibility(appBarLayout, extendedFloatingActionButton)) {
                return false;
            }
            if (this.tmpRect == null) {
                this.tmpRect = new Rect();
            }
            Rect rect = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect(coordinatorLayout, appBarLayout, rect);
            if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                if (this.autoShrinkEnabled) {
                    baseMotionStrategy2 = extendedFloatingActionButton.shrinkStrategy;
                } else {
                    baseMotionStrategy2 = extendedFloatingActionButton.hideStrategy;
                }
                ExtendedFloatingActionButton.access$400(extendedFloatingActionButton, baseMotionStrategy2);
                return true;
            }
            if (this.autoShrinkEnabled) {
                baseMotionStrategy = extendedFloatingActionButton.extendStrategy;
            } else {
                baseMotionStrategy = extendedFloatingActionButton.showStrategy;
            }
            ExtendedFloatingActionButton.access$400(extendedFloatingActionButton, baseMotionStrategy);
            return true;
        }

        public final boolean updateFabVisibilityForBottomSheet(View view, ExtendedFloatingActionButton extendedFloatingActionButton) {
            BaseMotionStrategy baseMotionStrategy;
            BaseMotionStrategy baseMotionStrategy2;
            if (!shouldUpdateVisibility(view, extendedFloatingActionButton)) {
                return false;
            }
            if (view.getTop() < (extendedFloatingActionButton.getHeight() / 2) + ((ViewGroup.MarginLayoutParams) ((CoordinatorLayout.LayoutParams) extendedFloatingActionButton.getLayoutParams())).topMargin) {
                if (this.autoShrinkEnabled) {
                    baseMotionStrategy2 = extendedFloatingActionButton.shrinkStrategy;
                } else {
                    baseMotionStrategy2 = extendedFloatingActionButton.hideStrategy;
                }
                ExtendedFloatingActionButton.access$400(extendedFloatingActionButton, baseMotionStrategy2);
                return true;
            }
            if (this.autoShrinkEnabled) {
                baseMotionStrategy = extendedFloatingActionButton.extendStrategy;
            } else {
                baseMotionStrategy = extendedFloatingActionButton.showStrategy;
            }
            ExtendedFloatingActionButton.access$400(extendedFloatingActionButton, baseMotionStrategy);
            return true;
        }

        public ExtendedFloatingActionButtonBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ExtendedFloatingActionButton_Behavior_Layout);
            this.autoHideEnabled = obtainStyledAttributes.getBoolean(0, false);
            this.autoShrinkEnabled = obtainStyledAttributes.getBoolean(1, true);
            obtainStyledAttributes.recycle();
        }
    }

    /* loaded from: classes.dex */
    public class HideStrategy extends BaseMotionStrategy {
        public boolean isCancelled;

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final int getDefaultMotionSpecResource() {
            return 2130837549;
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final void onChange() {
        }

        public HideStrategy(CodedOutputByteBufferNano codedOutputByteBufferNano) {
            super(ExtendedFloatingActionButton.this, codedOutputByteBufferNano);
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final void onAnimationEnd() {
            CodedOutputByteBufferNano codedOutputByteBufferNano = this.tracker;
            Objects.requireNonNull(codedOutputByteBufferNano);
            codedOutputByteBufferNano.buffer = null;
            ExtendedFloatingActionButton extendedFloatingActionButton = ExtendedFloatingActionButton.this;
            extendedFloatingActionButton.animState = 0;
            if (!this.isCancelled) {
                extendedFloatingActionButton.setVisibility(8);
            }
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final void performNow() {
            ExtendedFloatingActionButton.this.setVisibility(8);
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final boolean shouldCancel() {
            ExtendedFloatingActionButton extendedFloatingActionButton = ExtendedFloatingActionButton.this;
            AnonymousClass4 r0 = ExtendedFloatingActionButton.WIDTH;
            Objects.requireNonNull(extendedFloatingActionButton);
            if (extendedFloatingActionButton.getVisibility() == 0) {
                if (extendedFloatingActionButton.animState != 1) {
                    return false;
                }
            } else if (extendedFloatingActionButton.animState == 2) {
                return false;
            }
            return true;
        }

        @Override // com.google.android.material.floatingactionbutton.BaseMotionStrategy, com.google.android.material.floatingactionbutton.MotionStrategy
        public final void onAnimationCancel() {
            super.onAnimationCancel();
            this.isCancelled = true;
        }

        @Override // com.google.android.material.floatingactionbutton.BaseMotionStrategy, com.google.android.material.floatingactionbutton.MotionStrategy
        public final void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            this.isCancelled = false;
            ExtendedFloatingActionButton.this.setVisibility(0);
            ExtendedFloatingActionButton.this.animState = 1;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class OnChangedCallback {
    }

    /* loaded from: classes.dex */
    public class ShowStrategy extends BaseMotionStrategy {
        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final int getDefaultMotionSpecResource() {
            return 2130837550;
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final void onChange() {
        }

        public ShowStrategy(CodedOutputByteBufferNano codedOutputByteBufferNano) {
            super(ExtendedFloatingActionButton.this, codedOutputByteBufferNano);
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final void onAnimationEnd() {
            CodedOutputByteBufferNano codedOutputByteBufferNano = this.tracker;
            Objects.requireNonNull(codedOutputByteBufferNano);
            codedOutputByteBufferNano.buffer = null;
            ExtendedFloatingActionButton.this.animState = 0;
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final void performNow() {
            ExtendedFloatingActionButton.this.setVisibility(0);
            ExtendedFloatingActionButton.this.setAlpha(1.0f);
            ExtendedFloatingActionButton.this.setScaleY(1.0f);
            ExtendedFloatingActionButton.this.setScaleX(1.0f);
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public final boolean shouldCancel() {
            ExtendedFloatingActionButton extendedFloatingActionButton = ExtendedFloatingActionButton.this;
            AnonymousClass4 r0 = ExtendedFloatingActionButton.WIDTH;
            Objects.requireNonNull(extendedFloatingActionButton);
            if (extendedFloatingActionButton.getVisibility() != 0) {
                if (extendedFloatingActionButton.animState != 2) {
                    return false;
                }
            } else if (extendedFloatingActionButton.animState == 1) {
                return false;
            }
            return true;
        }

        @Override // com.google.android.material.floatingactionbutton.BaseMotionStrategy, com.google.android.material.floatingactionbutton.MotionStrategy
        public final void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            ExtendedFloatingActionButton.this.setVisibility(0);
            ExtendedFloatingActionButton.this.animState = 2;
        }
    }

    /* loaded from: classes.dex */
    public interface Size {
        int getHeight();

        ViewGroup.LayoutParams getLayoutParams();

        int getPaddingEnd();

        int getPaddingStart();

        int getWidth();
    }

    public ExtendedFloatingActionButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // android.widget.TextView
    public final void setTextColor(int i) {
        super.setTextColor(i);
        this.originalTextCsl = getTextColors();
    }

    public ExtendedFloatingActionButton(Context context, AttributeSet attributeSet, int i) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, 2130969036, 2132018661), attributeSet, 2130969036);
        this.animState = 0;
        CodedOutputByteBufferNano codedOutputByteBufferNano = new CodedOutputByteBufferNano();
        ShowStrategy showStrategy = new ShowStrategy(codedOutputByteBufferNano);
        this.showStrategy = showStrategy;
        HideStrategy hideStrategy = new HideStrategy(codedOutputByteBufferNano);
        this.hideStrategy = hideStrategy;
        this.isExtended = true;
        this.isTransforming = false;
        Context context2 = getContext();
        this.behavior = new ExtendedFloatingActionButtonBehavior(context2, attributeSet);
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.ExtendedFloatingActionButton, 2130969036, 2132018661, new int[0]);
        MotionSpec createFromAttribute = MotionSpec.createFromAttribute(context2, obtainStyledAttributes, 4);
        MotionSpec createFromAttribute2 = MotionSpec.createFromAttribute(context2, obtainStyledAttributes, 3);
        MotionSpec createFromAttribute3 = MotionSpec.createFromAttribute(context2, obtainStyledAttributes, 2);
        MotionSpec createFromAttribute4 = MotionSpec.createFromAttribute(context2, obtainStyledAttributes, 5);
        this.collapsedSize = obtainStyledAttributes.getDimensionPixelSize(0, -1);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        this.extendedPaddingStart = ViewCompat.Api17Impl.getPaddingStart(this);
        this.extendedPaddingEnd = ViewCompat.Api17Impl.getPaddingEnd(this);
        CodedOutputByteBufferNano codedOutputByteBufferNano2 = new CodedOutputByteBufferNano();
        ChangeSizeStrategy changeSizeStrategy = new ChangeSizeStrategy(codedOutputByteBufferNano2, new Size() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.1
            @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.Size
            public final int getHeight() {
                return ExtendedFloatingActionButton.this.getMeasuredHeight();
            }

            @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.Size
            public final ViewGroup.LayoutParams getLayoutParams() {
                return new ViewGroup.LayoutParams(-2, -2);
            }

            @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.Size
            public final int getPaddingEnd() {
                return ExtendedFloatingActionButton.this.extendedPaddingEnd;
            }

            @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.Size
            public final int getPaddingStart() {
                return ExtendedFloatingActionButton.this.extendedPaddingStart;
            }

            @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.Size
            public final int getWidth() {
                int measuredWidth = ExtendedFloatingActionButton.this.getMeasuredWidth();
                ExtendedFloatingActionButton extendedFloatingActionButton = ExtendedFloatingActionButton.this;
                Objects.requireNonNull(extendedFloatingActionButton);
                ExtendedFloatingActionButton extendedFloatingActionButton2 = ExtendedFloatingActionButton.this;
                return (measuredWidth - (((extendedFloatingActionButton.getCollapsedSize() - extendedFloatingActionButton.iconSize) / 2) * 2)) + extendedFloatingActionButton2.extendedPaddingStart + extendedFloatingActionButton2.extendedPaddingEnd;
            }
        }, true);
        this.extendStrategy = changeSizeStrategy;
        ChangeSizeStrategy changeSizeStrategy2 = new ChangeSizeStrategy(codedOutputByteBufferNano2, new Size() { // from class: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.2
            @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.Size
            public final int getHeight() {
                return ExtendedFloatingActionButton.this.getCollapsedSize();
            }

            @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.Size
            public final ViewGroup.LayoutParams getLayoutParams() {
                return new ViewGroup.LayoutParams(getWidth(), getHeight());
            }

            @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.Size
            public final int getPaddingEnd() {
                ExtendedFloatingActionButton extendedFloatingActionButton = ExtendedFloatingActionButton.this;
                Objects.requireNonNull(extendedFloatingActionButton);
                return (extendedFloatingActionButton.getCollapsedSize() - extendedFloatingActionButton.iconSize) / 2;
            }

            @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.Size
            public final int getPaddingStart() {
                ExtendedFloatingActionButton extendedFloatingActionButton = ExtendedFloatingActionButton.this;
                Objects.requireNonNull(extendedFloatingActionButton);
                return (extendedFloatingActionButton.getCollapsedSize() - extendedFloatingActionButton.iconSize) / 2;
            }

            @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.Size
            public final int getWidth() {
                return ExtendedFloatingActionButton.this.getCollapsedSize();
            }
        }, false);
        this.shrinkStrategy = changeSizeStrategy2;
        showStrategy.motionSpec = createFromAttribute;
        hideStrategy.motionSpec = createFromAttribute2;
        changeSizeStrategy.motionSpec = createFromAttribute3;
        changeSizeStrategy2.motionSpec = createFromAttribute4;
        obtainStyledAttributes.recycle();
        setShapeAppearanceModel(new ShapeAppearanceModel(ShapeAppearanceModel.builder(context2, attributeSet, 2130969036, 2132018661, ShapeAppearanceModel.PILL)));
        this.originalTextCsl = getTextColors();
    }

    public int getCollapsedSize() {
        int i = this.collapsedSize;
        if (i >= 0) {
            return i;
        }
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        return (Math.min(ViewCompat.Api17Impl.getPaddingStart(this), ViewCompat.Api17Impl.getPaddingEnd(this)) * 2) + this.iconSize;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x001f  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void access$400(com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton r2, final com.google.android.material.floatingactionbutton.BaseMotionStrategy r3) {
        /*
            boolean r0 = r3.shouldCancel()
            if (r0 == 0) goto L_0x0007
            goto L_0x004e
        L_0x0007:
            java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r0 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
            boolean r0 = androidx.core.view.ViewCompat.Api19Impl.isLaidOut(r2)
            r1 = 0
            if (r0 != 0) goto L_0x0014
            r2.getVisibility()
            goto L_0x001c
        L_0x0014:
            boolean r0 = r2.isInEditMode()
            if (r0 != 0) goto L_0x001c
            r0 = 1
            goto L_0x001d
        L_0x001c:
            r0 = r1
        L_0x001d:
            if (r0 != 0) goto L_0x0026
            r3.performNow()
            r3.onChange()
            goto L_0x004e
        L_0x0026:
            r2.measure(r1, r1)
            android.animation.AnimatorSet r2 = r3.createAnimator()
            com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$3 r0 = new com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$3
            r0.<init>()
            r2.addListener(r0)
            java.util.ArrayList<android.animation.Animator$AnimatorListener> r3 = r3.listeners
            java.util.Iterator r3 = r3.iterator()
        L_0x003b:
            boolean r0 = r3.hasNext()
            if (r0 == 0) goto L_0x004b
            java.lang.Object r0 = r3.next()
            android.animation.Animator$AnimatorListener r0 = (android.animation.Animator.AnimatorListener) r0
            r2.addListener(r0)
            goto L_0x003b
        L_0x004b:
            r2.start()
        L_0x004e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.access$400(com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton, com.google.android.material.floatingactionbutton.BaseMotionStrategy):void");
    }

    @Override // com.google.android.material.button.MaterialButton, android.widget.TextView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.isExtended && TextUtils.isEmpty(getText()) && this.icon != null) {
            this.isExtended = false;
            this.shrinkStrategy.performNow();
        }
    }

    @Override // android.widget.TextView, android.view.View
    public final void setPadding(int i, int i2, int i3, int i4) {
        super.setPadding(i, i2, i3, i4);
        if (this.isExtended && !this.isTransforming) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            this.extendedPaddingStart = ViewCompat.Api17Impl.getPaddingStart(this);
            this.extendedPaddingEnd = ViewCompat.Api17Impl.getPaddingEnd(this);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public final void setPaddingRelative(int i, int i2, int i3, int i4) {
        super.setPaddingRelative(i, i2, i3, i4);
        if (this.isExtended && !this.isTransforming) {
            this.extendedPaddingStart = i;
            this.extendedPaddingEnd = i3;
        }
    }

    @Override // android.widget.TextView
    public final void setTextColor(ColorStateList colorStateList) {
        super.setTextColor(colorStateList);
        this.originalTextCsl = getTextColors();
    }

    public final void silentlyUpdateTextColor(ColorStateList colorStateList) {
        super.setTextColor(colorStateList);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.AttachedBehavior
    public final CoordinatorLayout.Behavior<ExtendedFloatingActionButton> getBehavior() {
        return this.behavior;
    }
}
