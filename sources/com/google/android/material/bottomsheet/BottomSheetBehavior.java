package com.google.android.material.bottomsheet;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.WeakHashMap;
import kotlinx.atomicfu.AtomicFU;
/* loaded from: classes.dex */
public class BottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    public int activePointerId;
    public final ArrayList<BottomSheetCallback> callbacks;
    public int childHeight;
    public int collapsedOffset;
    public final AnonymousClass4 dragCallback;
    public boolean draggable;
    public float elevation;
    public int expandHalfwayActionId;
    public int expandedOffset;
    public boolean fitToContents;
    public int fitToContentsOffset;
    public int gestureInsetBottom;
    public boolean gestureInsetBottomIgnored;
    public int halfExpandedOffset;
    public float halfExpandedRatio;
    public boolean hideable;
    public boolean ignoreEvents;
    public HashMap importantForAccessibilityMap;
    public int initialY;
    public int insetBottom;
    public int insetTop;
    public ValueAnimator interpolatorAnimator;
    public boolean isShapeExpanded;
    public int lastNestedScrollDy;
    public MaterialShapeDrawable materialShapeDrawable;
    public int maxHeight;
    public int maxWidth;
    public float maximumVelocity;
    public boolean nestedScrolled;
    public WeakReference<View> nestedScrollingChildRef;
    public boolean paddingBottomSystemWindowInsets;
    public boolean paddingLeftSystemWindowInsets;
    public boolean paddingRightSystemWindowInsets;
    public boolean paddingTopSystemWindowInsets;
    public int parentHeight;
    public int parentWidth;
    public int peekHeight;
    public boolean peekHeightAuto;
    public int peekHeightGestureInsetBuffer;
    public int peekHeightMin;
    public int saveFlags;
    public BottomSheetBehavior<V>.SettleRunnable settleRunnable;
    public ShapeAppearanceModel shapeAppearanceModelDefault;
    public boolean shapeThemingEnabled;
    public boolean skipCollapsed;
    public int state;
    public boolean touchingScrollingChild;
    public VelocityTracker velocityTracker;
    public ViewDragHelper viewDragHelper;
    public WeakReference<V> viewRef;

    /* loaded from: classes.dex */
    public static abstract class BottomSheetCallback {
        public abstract void onSlide();

        public abstract void onStateChanged();
    }

    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.SavedState.1
            @Override // android.os.Parcelable.ClassLoaderCreator
            public final SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            public final Object createFromParcel(Parcel parcel) {
                return new SavedState(parcel, (ClassLoader) null);
            }

            @Override // android.os.Parcelable.Creator
            public final Object[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public boolean fitToContents;
        public boolean hideable;
        public int peekHeight;
        public boolean skipCollapsed;
        public final int state;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.state = parcel.readInt();
            this.peekHeight = parcel.readInt();
            boolean z = false;
            this.fitToContents = parcel.readInt() == 1;
            this.hideable = parcel.readInt() == 1;
            this.skipCollapsed = parcel.readInt() == 1 ? true : z;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mSuperState, i);
            parcel.writeInt(this.state);
            parcel.writeInt(this.peekHeight);
            parcel.writeInt(this.fitToContents ? 1 : 0);
            parcel.writeInt(this.hideable ? 1 : 0);
            parcel.writeInt(this.skipCollapsed ? 1 : 0);
        }

        public SavedState(android.view.AbsSavedState absSavedState, BottomSheetBehavior bottomSheetBehavior) {
            super(absSavedState);
            this.state = bottomSheetBehavior.state;
            this.peekHeight = bottomSheetBehavior.peekHeight;
            this.fitToContents = bottomSheetBehavior.fitToContents;
            this.hideable = bottomSheetBehavior.hideable;
            this.skipCollapsed = bottomSheetBehavior.skipCollapsed;
        }
    }

    /* loaded from: classes.dex */
    public class SettleRunnable implements Runnable {
        public boolean isPosted;
        public int targetState;
        public final View view;

        public SettleRunnable(View view, int i) {
            this.view = view;
            this.targetState = i;
        }

        @Override // java.lang.Runnable
        public final void run() {
            ViewDragHelper viewDragHelper = BottomSheetBehavior.this.viewDragHelper;
            if (viewDragHelper == null || !viewDragHelper.continueSettling()) {
                BottomSheetBehavior.this.setStateInternal(this.targetState);
            } else {
                View view = this.view;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                ViewCompat.Api16Impl.postOnAnimation(view, this);
            }
            this.isPosted = false;
        }
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [com.google.android.material.bottomsheet.BottomSheetBehavior$4] */
    public BottomSheetBehavior() {
        this.saveFlags = 0;
        this.fitToContents = true;
        this.maxWidth = -1;
        this.maxHeight = -1;
        this.settleRunnable = null;
        this.halfExpandedRatio = 0.5f;
        this.elevation = -1.0f;
        this.draggable = true;
        this.state = 4;
        this.callbacks = new ArrayList<>();
        this.expandHalfwayActionId = -1;
        this.dragCallback = new ViewDragHelper.Callback() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.4
            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final void onViewDragStateChanged(int i) {
                if (i == 1) {
                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
                    if (bottomSheetBehavior.draggable) {
                        bottomSheetBehavior.setStateInternal(1);
                    }
                }
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final void onViewReleased(View view, float f, float f2) {
                int i;
                int i2;
                boolean z;
                int i3 = 4;
                if (f2 < 0.0f) {
                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
                    if (bottomSheetBehavior.fitToContents) {
                        i = bottomSheetBehavior.fitToContentsOffset;
                    } else {
                        int top = view.getTop();
                        System.currentTimeMillis();
                        Objects.requireNonNull(BottomSheetBehavior.this);
                        BottomSheetBehavior bottomSheetBehavior2 = BottomSheetBehavior.this;
                        i2 = bottomSheetBehavior2.halfExpandedOffset;
                        if (top <= i2) {
                            i = bottomSheetBehavior2.getExpandedOffset();
                        }
                        i3 = 6;
                        i = i2;
                    }
                    i3 = 3;
                } else {
                    BottomSheetBehavior bottomSheetBehavior3 = BottomSheetBehavior.this;
                    if (bottomSheetBehavior3.hideable && bottomSheetBehavior3.shouldHide(view, f2)) {
                        if (Math.abs(f) >= Math.abs(f2) || f2 <= 500.0f) {
                            int top2 = view.getTop();
                            BottomSheetBehavior bottomSheetBehavior4 = BottomSheetBehavior.this;
                            if (top2 > (bottomSheetBehavior4.getExpandedOffset() + bottomSheetBehavior4.parentHeight) / 2) {
                                z = true;
                            } else {
                                z = false;
                            }
                            if (!z) {
                                BottomSheetBehavior bottomSheetBehavior5 = BottomSheetBehavior.this;
                                if (bottomSheetBehavior5.fitToContents) {
                                    i = bottomSheetBehavior5.fitToContentsOffset;
                                } else if (Math.abs(view.getTop() - BottomSheetBehavior.this.getExpandedOffset()) < Math.abs(view.getTop() - BottomSheetBehavior.this.halfExpandedOffset)) {
                                    i = BottomSheetBehavior.this.getExpandedOffset();
                                } else {
                                    i2 = BottomSheetBehavior.this.halfExpandedOffset;
                                    i3 = 6;
                                    i = i2;
                                }
                                i3 = 3;
                            }
                        }
                        i = BottomSheetBehavior.this.parentHeight;
                        i3 = 5;
                    } else if (f2 == 0.0f || Math.abs(f) > Math.abs(f2)) {
                        int top3 = view.getTop();
                        BottomSheetBehavior bottomSheetBehavior6 = BottomSheetBehavior.this;
                        if (!bottomSheetBehavior6.fitToContents) {
                            int i4 = bottomSheetBehavior6.halfExpandedOffset;
                            if (top3 < i4) {
                                if (top3 < Math.abs(top3 - bottomSheetBehavior6.collapsedOffset)) {
                                    i = BottomSheetBehavior.this.getExpandedOffset();
                                    i3 = 3;
                                } else {
                                    Objects.requireNonNull(BottomSheetBehavior.this);
                                    i2 = BottomSheetBehavior.this.halfExpandedOffset;
                                }
                            } else if (Math.abs(top3 - i4) < Math.abs(top3 - BottomSheetBehavior.this.collapsedOffset)) {
                                Objects.requireNonNull(BottomSheetBehavior.this);
                                i2 = BottomSheetBehavior.this.halfExpandedOffset;
                            } else {
                                i = BottomSheetBehavior.this.collapsedOffset;
                            }
                            i3 = 6;
                            i = i2;
                        } else if (Math.abs(top3 - bottomSheetBehavior6.fitToContentsOffset) < Math.abs(top3 - BottomSheetBehavior.this.collapsedOffset)) {
                            i = BottomSheetBehavior.this.fitToContentsOffset;
                            i3 = 3;
                        } else {
                            i = BottomSheetBehavior.this.collapsedOffset;
                        }
                    } else {
                        BottomSheetBehavior bottomSheetBehavior7 = BottomSheetBehavior.this;
                        if (bottomSheetBehavior7.fitToContents) {
                            i = bottomSheetBehavior7.collapsedOffset;
                        } else {
                            int top4 = view.getTop();
                            if (Math.abs(top4 - BottomSheetBehavior.this.halfExpandedOffset) < Math.abs(top4 - BottomSheetBehavior.this.collapsedOffset)) {
                                Objects.requireNonNull(BottomSheetBehavior.this);
                                i2 = BottomSheetBehavior.this.halfExpandedOffset;
                                i3 = 6;
                                i = i2;
                            } else {
                                i = BottomSheetBehavior.this.collapsedOffset;
                            }
                        }
                    }
                }
                BottomSheetBehavior bottomSheetBehavior8 = BottomSheetBehavior.this;
                Objects.requireNonNull(bottomSheetBehavior8);
                bottomSheetBehavior8.startSettlingAnimation(view, i3, i, true);
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final int clampViewPositionVertical(View view, int i) {
                int i2;
                int expandedOffset = BottomSheetBehavior.this.getExpandedOffset();
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
                if (bottomSheetBehavior.hideable) {
                    i2 = bottomSheetBehavior.parentHeight;
                } else {
                    i2 = bottomSheetBehavior.collapsedOffset;
                }
                return AtomicFU.clamp(i, expandedOffset, i2);
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final int getViewVerticalDragRange() {
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
                if (bottomSheetBehavior.hideable) {
                    return bottomSheetBehavior.parentHeight;
                }
                return bottomSheetBehavior.collapsedOffset;
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final void onViewPositionChanged(View view, int i, int i2) {
                BottomSheetBehavior.this.dispatchOnSlide(i2);
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final boolean tryCaptureView(View view, int i) {
                View view2;
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
                int i2 = bottomSheetBehavior.state;
                if (i2 == 1 || bottomSheetBehavior.touchingScrollingChild) {
                    return false;
                }
                if (i2 == 3 && bottomSheetBehavior.activePointerId == i) {
                    WeakReference<View> weakReference = bottomSheetBehavior.nestedScrollingChildRef;
                    if (weakReference != null) {
                        view2 = weakReference.get();
                    } else {
                        view2 = null;
                    }
                    if (view2 != null && view2.canScrollVertically(-1)) {
                        return false;
                    }
                }
                System.currentTimeMillis();
                WeakReference<V> weakReference2 = BottomSheetBehavior.this.viewRef;
                if (weakReference2 == null || weakReference2.get() != view) {
                    return false;
                }
                return true;
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final int clampViewPositionHorizontal(View view, int i) {
                return view.getLeft();
            }
        };
    }

    public void disableShapeAnimations() {
        this.interpolatorAnimator = null;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams layoutParams) {
        this.viewRef = null;
        this.viewDragHelper = null;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final void onDetachedFromLayoutParams() {
        this.viewRef = null;
        this.viewDragHelper = null;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int[] iArr, int i3) {
        View view2;
        if (i3 != 1) {
            WeakReference<View> weakReference = this.nestedScrollingChildRef;
            if (weakReference != null) {
                view2 = weakReference.get();
            } else {
                view2 = null;
            }
            if (view == view2) {
                int top = v.getTop();
                int i4 = top - i2;
                if (i2 > 0) {
                    if (i4 < getExpandedOffset()) {
                        iArr[1] = top - getExpandedOffset();
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                        v.offsetTopAndBottom(-iArr[1]);
                        setStateInternal(3);
                    } else if (this.draggable) {
                        iArr[1] = i2;
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                        v.offsetTopAndBottom(-i2);
                        setStateInternal(1);
                    } else {
                        return;
                    }
                } else if (i2 < 0 && !view.canScrollVertically(-1)) {
                    int i5 = this.collapsedOffset;
                    if (i4 > i5 && !this.hideable) {
                        iArr[1] = top - i5;
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap3 = ViewCompat.sViewPropertyAnimatorMap;
                        v.offsetTopAndBottom(-iArr[1]);
                        setStateInternal(4);
                    } else if (this.draggable) {
                        iArr[1] = i2;
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap4 = ViewCompat.sViewPropertyAnimatorMap;
                        v.offsetTopAndBottom(-i2);
                        setStateInternal(1);
                    } else {
                        return;
                    }
                }
                dispatchOnSlide(v.getTop());
                this.lastNestedScrollDy = i2;
                this.nestedScrolled = true;
            }
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final void onNestedScroll(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3, int[] iArr) {
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, View view2, int i, int i2) {
        this.lastNestedScrollDy = 0;
        this.nestedScrolled = false;
        return (i & 2) != 0;
    }

    public final void setPeekHeight(int i) {
        boolean z = true;
        if (i == -1) {
            if (!this.peekHeightAuto) {
                this.peekHeightAuto = true;
            }
            z = false;
        } else {
            if (this.peekHeightAuto || this.peekHeight != i) {
                this.peekHeightAuto = false;
                this.peekHeight = Math.max(0, i);
            }
            z = false;
        }
        if (z) {
            updatePeekHeight();
        }
    }

    public final void settleToState(View view, int i) {
        int i2;
        int i3;
        if (i == 4) {
            i2 = this.collapsedOffset;
        } else if (i == 6) {
            int i4 = this.halfExpandedOffset;
            if (!this.fitToContents || i4 > (i3 = this.fitToContentsOffset)) {
                i2 = i4;
            } else {
                i = 3;
                i2 = i3;
            }
        } else if (i == 3) {
            i2 = getExpandedOffset();
        } else if (!this.hideable || i != 5) {
            Log.w("BottomSheetBehavior", "The bottom sheet may be in an invalid state. Ensure `hideable` is true when using `STATE_HIDDEN`.");
            return;
        } else {
            i2 = this.parentHeight;
        }
        startSettlingAnimation(view, i, i2, false);
    }

    public final void updateDrawableForTargetState(int i) {
        boolean z;
        ValueAnimator valueAnimator;
        float f;
        if (i != 2) {
            if (i == 3) {
                z = true;
            } else {
                z = false;
            }
            if (this.isShapeExpanded != z) {
                this.isShapeExpanded = z;
                if (this.materialShapeDrawable != null && (valueAnimator = this.interpolatorAnimator) != null) {
                    if (valueAnimator.isRunning()) {
                        this.interpolatorAnimator.reverse();
                        return;
                    }
                    if (z) {
                        f = 0.0f;
                    } else {
                        f = 1.0f;
                    }
                    this.interpolatorAnimator.setFloatValues(1.0f - f, f);
                    this.interpolatorAnimator.start();
                }
            }
        }
    }

    public final int calculatePeekHeight() {
        int i;
        int i2;
        int i3;
        if (this.peekHeightAuto) {
            i = Math.min(Math.max(this.peekHeightMin, this.parentHeight - ((this.parentWidth * 9) / 16)), this.childHeight);
            i2 = this.insetBottom;
        } else if (!this.gestureInsetBottomIgnored && !this.paddingBottomSystemWindowInsets && (i3 = this.gestureInsetBottom) > 0) {
            return Math.max(this.peekHeight, i3 + this.peekHeightGestureInsetBuffer);
        } else {
            i = this.peekHeight;
            i2 = this.insetBottom;
        }
        return i + i2;
    }

    public final void createMaterialShapeDrawable(Context context, AttributeSet attributeSet, boolean z, ColorStateList colorStateList) {
        if (this.shapeThemingEnabled) {
            this.shapeAppearanceModelDefault = ShapeAppearanceModel.builder(context, attributeSet, 2130968698, 2132018385).build();
            MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(this.shapeAppearanceModelDefault);
            this.materialShapeDrawable = materialShapeDrawable;
            materialShapeDrawable.initializeElevationOverlay(context);
            if (!z || colorStateList == null) {
                TypedValue typedValue = new TypedValue();
                context.getTheme().resolveAttribute(16842801, typedValue, true);
                this.materialShapeDrawable.setTint(typedValue.data);
                return;
            }
            this.materialShapeDrawable.setFillColor(colorStateList);
        }
    }

    public final void dispatchOnSlide(int i) {
        if (!(this.viewRef.get() == null || this.callbacks.isEmpty())) {
            int i2 = this.collapsedOffset;
            if (i <= i2 && i2 != getExpandedOffset()) {
                getExpandedOffset();
            }
            for (int i3 = 0; i3 < this.callbacks.size(); i3++) {
                this.callbacks.get(i3).onSlide();
            }
        }
    }

    public View findScrollingChild(View view) {
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        if (ViewCompat.Api21Impl.isNestedScrollingEnabled(view)) {
            return view;
        }
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View findScrollingChild = findScrollingChild(viewGroup.getChildAt(i));
            if (findScrollingChild != null) {
                return findScrollingChild;
            }
        }
        return null;
    }

    public final int getExpandedOffset() {
        int i;
        if (this.fitToContents) {
            return this.fitToContentsOffset;
        }
        int i2 = this.expandedOffset;
        if (this.paddingTopSystemWindowInsets) {
            i = 0;
        } else {
            i = this.insetTop;
        }
        return Math.max(i2, i);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v, int i) {
        final boolean z;
        boolean z2;
        float f;
        MaterialShapeDrawable materialShapeDrawable;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        if (ViewCompat.Api16Impl.getFitsSystemWindows(coordinatorLayout) && !ViewCompat.Api16Impl.getFitsSystemWindows(v)) {
            v.setFitsSystemWindows(true);
        }
        if (this.viewRef == null) {
            this.peekHeightMin = coordinatorLayout.getResources().getDimensionPixelSize(2131165622);
            if (this.gestureInsetBottomIgnored || this.peekHeightAuto) {
                z = false;
            } else {
                z = true;
            }
            if (this.paddingBottomSystemWindowInsets || this.paddingLeftSystemWindowInsets || this.paddingRightSystemWindowInsets || z) {
                ViewUtils.doOnApplyWindowInsets(v, new ViewUtils.OnApplyWindowInsetsListener() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.3
                    @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
                    public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
                        int i2;
                        int i3;
                        BottomSheetBehavior.this.insetTop = windowInsetsCompat.getSystemWindowInsetTop();
                        boolean isLayoutRtl = ViewUtils.isLayoutRtl(view);
                        int paddingBottom = view.getPaddingBottom();
                        int paddingLeft = view.getPaddingLeft();
                        int paddingRight = view.getPaddingRight();
                        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
                        if (bottomSheetBehavior.paddingBottomSystemWindowInsets) {
                            bottomSheetBehavior.insetBottom = windowInsetsCompat.getSystemWindowInsetBottom();
                            paddingBottom = relativePadding.bottom + BottomSheetBehavior.this.insetBottom;
                        }
                        if (BottomSheetBehavior.this.paddingLeftSystemWindowInsets) {
                            if (isLayoutRtl) {
                                i3 = relativePadding.end;
                            } else {
                                i3 = relativePadding.start;
                            }
                            paddingLeft = i3 + windowInsetsCompat.getSystemWindowInsetLeft();
                        }
                        if (BottomSheetBehavior.this.paddingRightSystemWindowInsets) {
                            if (isLayoutRtl) {
                                i2 = relativePadding.start;
                            } else {
                                i2 = relativePadding.end;
                            }
                            paddingRight = windowInsetsCompat.getSystemWindowInsetRight() + i2;
                        }
                        view.setPadding(paddingLeft, view.getPaddingTop(), paddingRight, paddingBottom);
                        if (z) {
                            BottomSheetBehavior.this.gestureInsetBottom = windowInsetsCompat.mImpl.getMandatorySystemGestureInsets().bottom;
                        }
                        BottomSheetBehavior bottomSheetBehavior2 = BottomSheetBehavior.this;
                        if (bottomSheetBehavior2.paddingBottomSystemWindowInsets || z) {
                            bottomSheetBehavior2.updatePeekHeight();
                        }
                        return windowInsetsCompat;
                    }
                });
            }
            this.viewRef = new WeakReference<>(v);
            if (this.shapeThemingEnabled && (materialShapeDrawable = this.materialShapeDrawable) != null) {
                ViewCompat.Api16Impl.setBackground(v, materialShapeDrawable);
            }
            MaterialShapeDrawable materialShapeDrawable2 = this.materialShapeDrawable;
            if (materialShapeDrawable2 != null) {
                float f2 = this.elevation;
                if (f2 == -1.0f) {
                    f2 = ViewCompat.Api21Impl.getElevation(v);
                }
                materialShapeDrawable2.setElevation(f2);
                if (this.state == 3) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                this.isShapeExpanded = z2;
                MaterialShapeDrawable materialShapeDrawable3 = this.materialShapeDrawable;
                if (z2) {
                    f = 0.0f;
                } else {
                    f = 1.0f;
                }
                materialShapeDrawable3.setInterpolation(f);
            }
            updateAccessibilityActions$1();
            if (ViewCompat.Api16Impl.getImportantForAccessibility(v) == 0) {
                ViewCompat.Api16Impl.setImportantForAccessibility(v, 1);
            }
        }
        if (this.viewDragHelper == null) {
            this.viewDragHelper = new ViewDragHelper(coordinatorLayout.getContext(), coordinatorLayout, this.dragCallback);
        }
        int top = v.getTop();
        coordinatorLayout.onLayoutChild(v, i);
        this.parentWidth = coordinatorLayout.getWidth();
        this.parentHeight = coordinatorLayout.getHeight();
        int height = v.getHeight();
        this.childHeight = height;
        int i2 = this.parentHeight;
        int i3 = i2 - height;
        int i4 = this.insetTop;
        if (i3 < i4) {
            if (this.paddingTopSystemWindowInsets) {
                this.childHeight = i2;
            } else {
                this.childHeight = i2 - i4;
            }
        }
        this.fitToContentsOffset = Math.max(0, i2 - this.childHeight);
        this.halfExpandedOffset = (int) ((1.0f - this.halfExpandedRatio) * this.parentHeight);
        calculateCollapsedOffset();
        int i5 = this.state;
        if (i5 == 3) {
            v.offsetTopAndBottom(getExpandedOffset());
        } else if (i5 == 6) {
            v.offsetTopAndBottom(this.halfExpandedOffset);
        } else if (this.hideable && i5 == 5) {
            v.offsetTopAndBottom(this.parentHeight);
        } else if (i5 == 4) {
            v.offsetTopAndBottom(this.collapsedOffset);
        } else if (i5 == 1 || i5 == 2) {
            v.offsetTopAndBottom(top - v.getTop());
        }
        this.nestedScrollingChildRef = new WeakReference<>(findScrollingChild(v));
        return true;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final boolean onNestedPreFling(View view) {
        WeakReference<View> weakReference = this.nestedScrollingChildRef;
        if (weakReference == null || view != weakReference.get() || this.state == 3) {
            return false;
        }
        return true;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final void onRestoreInstanceState(View view, Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        int i = this.saveFlags;
        if (i != 0) {
            if (i == -1 || (i & 1) == 1) {
                this.peekHeight = savedState.peekHeight;
            }
            if (i == -1 || (i & 2) == 2) {
                this.fitToContents = savedState.fitToContents;
            }
            if (i == -1 || (i & 4) == 4) {
                this.hideable = savedState.hideable;
            }
            if (i == -1 || (i & 8) == 8) {
                this.skipCollapsed = savedState.skipCollapsed;
            }
        }
        int i2 = savedState.state;
        if (i2 == 1 || i2 == 2) {
            this.state = 4;
        } else {
            this.state = i2;
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final Parcelable onSaveInstanceState(View view) {
        return new SavedState(View.BaseSavedState.EMPTY_STATE, this);
    }

    public final void setState(int i) {
        if (i != this.state) {
            if (this.viewRef != null) {
                settleToStatePendingLayout(i);
            } else if (i == 4 || i == 3 || i == 6 || (this.hideable && i == 5)) {
                this.state = i;
            }
        }
    }

    public final void setStateInternal(int i) {
        if (this.state != i) {
            this.state = i;
            WeakReference<V> weakReference = this.viewRef;
            if (!(weakReference == null || weakReference.get() == null)) {
                if (i == 3) {
                    updateImportantForAccessibility(true);
                } else if (i == 6 || i == 5 || i == 4) {
                    updateImportantForAccessibility(false);
                }
                updateDrawableForTargetState(i);
                for (int i2 = 0; i2 < this.callbacks.size(); i2++) {
                    this.callbacks.get(i2).onStateChanged();
                }
                updateAccessibilityActions$1();
            }
        }
    }

    public final void settleToStatePendingLayout(final int i) {
        final V v = this.viewRef.get();
        if (v != null) {
            ViewParent parent = v.getParent();
            if (parent != null && parent.isLayoutRequested()) {
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                if (ViewCompat.Api19Impl.isAttachedToWindow(v)) {
                    v.post(new Runnable() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            BottomSheetBehavior.this.settleToState(v, i);
                        }
                    });
                    return;
                }
            }
            settleToState(v, i);
        }
    }

    public final boolean shouldHide(View view, float f) {
        if (this.skipCollapsed) {
            return true;
        }
        if (view.getTop() < this.collapsedOffset) {
            return false;
        }
        if (Math.abs(((f * 0.1f) + view.getTop()) - this.collapsedOffset) / calculatePeekHeight() > 0.5f) {
            return true;
        }
        return false;
    }

    public final void startSettlingAnimation(View view, int i, int i2, boolean z) {
        boolean z2;
        ViewDragHelper viewDragHelper = this.viewDragHelper;
        if (viewDragHelper == null || (!z ? !viewDragHelper.smoothSlideViewTo(view, view.getLeft(), i2) : !viewDragHelper.settleCapturedViewAt(view.getLeft(), i2))) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z2) {
            setStateInternal(2);
            updateDrawableForTargetState(i);
            if (this.settleRunnable == null) {
                this.settleRunnable = new SettleRunnable(view, i);
            }
            BottomSheetBehavior<V>.SettleRunnable settleRunnable = this.settleRunnable;
            if (!settleRunnable.isPosted) {
                settleRunnable.targetState = i;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                ViewCompat.Api16Impl.postOnAnimation(view, settleRunnable);
                this.settleRunnable.isPosted = true;
                return;
            }
            settleRunnable.targetState = i;
            return;
        }
        setStateInternal(i);
    }

    public final void updateAccessibilityActions$1() {
        V v;
        int i;
        boolean z;
        AccessibilityDelegateCompat accessibilityDelegateCompat;
        WeakReference<V> weakReference = this.viewRef;
        if (weakReference != null && (v = weakReference.get()) != null) {
            ViewCompat.removeActionWithId(524288, v);
            ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(v, 0);
            ViewCompat.removeActionWithId(262144, v);
            ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(v, 0);
            ViewCompat.removeActionWithId(1048576, v);
            ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(v, 0);
            int i2 = this.expandHalfwayActionId;
            if (i2 != -1) {
                ViewCompat.removeActionWithId(i2, v);
                ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(v, 0);
            }
            final int i3 = 6;
            if (!this.fitToContents && this.state != 6) {
                String string = v.getResources().getString(2131952053);
                AccessibilityViewCommand accessibilityViewCommand = new AccessibilityViewCommand() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.5
                    @Override // androidx.core.view.accessibility.AccessibilityViewCommand
                    public final boolean perform(View view) {
                        BottomSheetBehavior.this.setState(r2);
                        return true;
                    }
                };
                ArrayList actionList = ViewCompat.getActionList(v);
                int i4 = 0;
                while (true) {
                    if (i4 < actionList.size()) {
                        AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat = (AccessibilityNodeInfoCompat.AccessibilityActionCompat) actionList.get(i4);
                        Objects.requireNonNull(accessibilityActionCompat);
                        if (TextUtils.equals(string, ((AccessibilityNodeInfo.AccessibilityAction) accessibilityActionCompat.mAction).getLabel())) {
                            i = ((AccessibilityNodeInfoCompat.AccessibilityActionCompat) actionList.get(i4)).getId();
                            break;
                        }
                        i4++;
                    } else {
                        int i5 = 0;
                        int i6 = -1;
                        while (true) {
                            int[] iArr = ViewCompat.ACCESSIBILITY_ACTIONS_RESOURCE_IDS;
                            if (i5 >= iArr.length || i6 != -1) {
                                break;
                            }
                            int i7 = iArr[i5];
                            boolean z2 = true;
                            for (int i8 = 0; i8 < actionList.size(); i8++) {
                                if (((AccessibilityNodeInfoCompat.AccessibilityActionCompat) actionList.get(i8)).getId() != i7) {
                                    z = true;
                                } else {
                                    z = false;
                                }
                                z2 &= z;
                            }
                            if (z2) {
                                i6 = i7;
                            }
                            i5++;
                        }
                        i = i6;
                    }
                }
                if (i != -1) {
                    AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat2 = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(null, i, string, accessibilityViewCommand, null);
                    View.AccessibilityDelegate accessibilityDelegate = ViewCompat.Api29Impl.getAccessibilityDelegate(v);
                    if (accessibilityDelegate == null) {
                        accessibilityDelegateCompat = null;
                    } else if (accessibilityDelegate instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) {
                        accessibilityDelegateCompat = ((AccessibilityDelegateCompat.AccessibilityDelegateAdapter) accessibilityDelegate).mCompat;
                    } else {
                        accessibilityDelegateCompat = new AccessibilityDelegateCompat(accessibilityDelegate);
                    }
                    if (accessibilityDelegateCompat == null) {
                        accessibilityDelegateCompat = new AccessibilityDelegateCompat();
                    }
                    ViewCompat.setAccessibilityDelegate(v, accessibilityDelegateCompat);
                    ViewCompat.removeActionWithId(accessibilityActionCompat2.getId(), v);
                    ViewCompat.getActionList(v).add(accessibilityActionCompat2);
                    ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(v, 0);
                }
                this.expandHalfwayActionId = i;
            }
            if (this.hideable && this.state != 5) {
                ViewCompat.replaceAccessibilityAction(v, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_DISMISS, null, new AccessibilityViewCommand() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.5
                    @Override // androidx.core.view.accessibility.AccessibilityViewCommand
                    public final boolean perform(View view) {
                        BottomSheetBehavior.this.setState(r2);
                        return true;
                    }
                });
            }
            int i9 = this.state;
            if (i9 == 3) {
                if (this.fitToContents) {
                    i3 = 4;
                }
                ViewCompat.replaceAccessibilityAction(v, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_COLLAPSE, null, new AccessibilityViewCommand() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.5
                    @Override // androidx.core.view.accessibility.AccessibilityViewCommand
                    public final boolean perform(View view) {
                        BottomSheetBehavior.this.setState(i3);
                        return true;
                    }
                });
            } else if (i9 == 4) {
                if (this.fitToContents) {
                    i3 = 3;
                }
                ViewCompat.replaceAccessibilityAction(v, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_EXPAND, null, new AccessibilityViewCommand() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.5
                    @Override // androidx.core.view.accessibility.AccessibilityViewCommand
                    public final boolean perform(View view) {
                        BottomSheetBehavior.this.setState(i3);
                        return true;
                    }
                });
            } else if (i9 == 6) {
                ViewCompat.replaceAccessibilityAction(v, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_COLLAPSE, null, new AccessibilityViewCommand() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.5
                    @Override // androidx.core.view.accessibility.AccessibilityViewCommand
                    public final boolean perform(View view) {
                        BottomSheetBehavior.this.setState(i3);
                        return true;
                    }
                });
                ViewCompat.replaceAccessibilityAction(v, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_EXPAND, null, new AccessibilityViewCommand() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.5
                    @Override // androidx.core.view.accessibility.AccessibilityViewCommand
                    public final boolean perform(View view) {
                        BottomSheetBehavior.this.setState(i3);
                        return true;
                    }
                });
            }
        }
    }

    public final void updateImportantForAccessibility(boolean z) {
        WeakReference<V> weakReference = this.viewRef;
        if (weakReference != null) {
            ViewParent parent = weakReference.get().getParent();
            if (parent instanceof CoordinatorLayout) {
                CoordinatorLayout coordinatorLayout = (CoordinatorLayout) parent;
                int childCount = coordinatorLayout.getChildCount();
                if (z) {
                    if (this.importantForAccessibilityMap == null) {
                        this.importantForAccessibilityMap = new HashMap(childCount);
                    } else {
                        return;
                    }
                }
                for (int i = 0; i < childCount; i++) {
                    View childAt = coordinatorLayout.getChildAt(i);
                    if (childAt != this.viewRef.get() && z) {
                        this.importantForAccessibilityMap.put(childAt, Integer.valueOf(childAt.getImportantForAccessibility()));
                    }
                }
                if (!z) {
                    this.importantForAccessibilityMap = null;
                }
            }
        }
    }

    public final void updatePeekHeight() {
        V v;
        if (this.viewRef != null) {
            calculateCollapsedOffset();
            if (this.state == 4 && (v = this.viewRef.get()) != null) {
                v.requestLayout();
            }
        }
    }

    public static int getChildMeasureSpec(int i, int i2, int i3, int i4) {
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(i, i2, i4);
        if (i3 == -1) {
            return childMeasureSpec;
        }
        int mode = View.MeasureSpec.getMode(childMeasureSpec);
        int size = View.MeasureSpec.getSize(childMeasureSpec);
        if (mode == 1073741824) {
            return View.MeasureSpec.makeMeasureSpec(Math.min(size, i3), 1073741824);
        }
        if (size != 0) {
            i3 = Math.min(size, i3);
        }
        return View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE);
    }

    public final void calculateCollapsedOffset() {
        int calculatePeekHeight = calculatePeekHeight();
        if (this.fitToContents) {
            this.collapsedOffset = Math.max(this.parentHeight - calculatePeekHeight, this.fitToContentsOffset);
        } else {
            this.collapsedOffset = this.parentHeight - calculatePeekHeight;
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        ViewDragHelper viewDragHelper;
        ViewDragHelper viewDragHelper2;
        boolean z;
        View view;
        if (!v.isShown() || !this.draggable) {
            this.ignoreEvents = true;
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        View view2 = null;
        if (actionMasked == 0) {
            this.activePointerId = -1;
            VelocityTracker velocityTracker = this.velocityTracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.velocityTracker = null;
            }
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(motionEvent);
        if (actionMasked == 0) {
            int x = (int) motionEvent.getX();
            this.initialY = (int) motionEvent.getY();
            if (this.state != 2) {
                WeakReference<View> weakReference = this.nestedScrollingChildRef;
                if (weakReference != null) {
                    view = weakReference.get();
                } else {
                    view = null;
                }
                if (view != null && coordinatorLayout.isPointInChildBounds(view, x, this.initialY)) {
                    this.activePointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
                    this.touchingScrollingChild = true;
                }
            }
            if (this.activePointerId != -1 || coordinatorLayout.isPointInChildBounds(v, x, this.initialY)) {
                z = false;
            } else {
                z = true;
            }
            this.ignoreEvents = z;
        } else if (actionMasked == 1 || actionMasked == 3) {
            this.touchingScrollingChild = false;
            this.activePointerId = -1;
            if (this.ignoreEvents) {
                this.ignoreEvents = false;
                return false;
            }
        }
        if (!this.ignoreEvents && (viewDragHelper2 = this.viewDragHelper) != null && viewDragHelper2.shouldInterceptTouchEvent(motionEvent)) {
            return true;
        }
        WeakReference<View> weakReference2 = this.nestedScrollingChildRef;
        if (weakReference2 != null) {
            view2 = weakReference2.get();
        }
        if (actionMasked != 2 || view2 == null || this.ignoreEvents || this.state == 1 || coordinatorLayout.isPointInChildBounds(view2, (int) motionEvent.getX(), (int) motionEvent.getY()) || this.viewDragHelper == null) {
            return false;
        }
        float abs = Math.abs(this.initialY - motionEvent.getY());
        Objects.requireNonNull(this.viewDragHelper);
        if (abs > viewDragHelper.mTouchSlop) {
            return true;
        }
        return false;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final boolean onMeasureChild(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        view.measure(getChildMeasureSpec(i, coordinatorLayout.getPaddingRight() + coordinatorLayout.getPaddingLeft() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i2, this.maxWidth, marginLayoutParams.width), getChildMeasureSpec(i3, coordinatorLayout.getPaddingBottom() + coordinatorLayout.getPaddingTop() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + 0, this.maxHeight, marginLayoutParams.height));
        return true;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i) {
        int i2;
        float f;
        int i3 = 3;
        if (v.getTop() == getExpandedOffset()) {
            setStateInternal(3);
            return;
        }
        WeakReference<View> weakReference = this.nestedScrollingChildRef;
        if (weakReference != null && view == weakReference.get() && this.nestedScrolled) {
            if (this.lastNestedScrollDy > 0) {
                if (this.fitToContents) {
                    i2 = this.fitToContentsOffset;
                } else {
                    int top = v.getTop();
                    int i4 = this.halfExpandedOffset;
                    if (top > i4) {
                        i2 = i4;
                        i3 = 6;
                    } else {
                        i2 = getExpandedOffset();
                    }
                }
                startSettlingAnimation(v, i3, i2, false);
                this.nestedScrolled = false;
            }
            if (this.hideable) {
                VelocityTracker velocityTracker = this.velocityTracker;
                if (velocityTracker == null) {
                    f = 0.0f;
                } else {
                    velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
                    f = this.velocityTracker.getYVelocity(this.activePointerId);
                }
                if (shouldHide(v, f)) {
                    i2 = this.parentHeight;
                    i3 = 5;
                    startSettlingAnimation(v, i3, i2, false);
                    this.nestedScrolled = false;
                }
            }
            if (this.lastNestedScrollDy == 0) {
                int top2 = v.getTop();
                if (!this.fitToContents) {
                    int i5 = this.halfExpandedOffset;
                    if (top2 < i5) {
                        if (top2 < Math.abs(top2 - this.collapsedOffset)) {
                            i2 = getExpandedOffset();
                        } else {
                            i2 = this.halfExpandedOffset;
                        }
                    } else if (Math.abs(top2 - i5) < Math.abs(top2 - this.collapsedOffset)) {
                        i2 = this.halfExpandedOffset;
                    } else {
                        i2 = this.collapsedOffset;
                        i3 = 4;
                    }
                    i3 = 6;
                } else if (Math.abs(top2 - this.fitToContentsOffset) < Math.abs(top2 - this.collapsedOffset)) {
                    i2 = this.fitToContentsOffset;
                } else {
                    i2 = this.collapsedOffset;
                    i3 = 4;
                }
            } else {
                if (this.fitToContents) {
                    i2 = this.collapsedOffset;
                } else {
                    int top3 = v.getTop();
                    if (Math.abs(top3 - this.halfExpandedOffset) < Math.abs(top3 - this.collapsedOffset)) {
                        i2 = this.halfExpandedOffset;
                        i3 = 6;
                    } else {
                        i2 = this.collapsedOffset;
                    }
                }
                i3 = 4;
            }
            startSettlingAnimation(v, i3, i2, false);
            this.nestedScrolled = false;
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public final boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        boolean z;
        ViewDragHelper viewDragHelper;
        boolean z2 = false;
        if (!v.isShown()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        int i = this.state;
        if (i == 1 && actionMasked == 0) {
            return true;
        }
        ViewDragHelper viewDragHelper2 = this.viewDragHelper;
        if (viewDragHelper2 == null || (!this.draggable && i != 1)) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            viewDragHelper2.processTouchEvent(motionEvent);
        }
        if (actionMasked == 0) {
            this.activePointerId = -1;
            VelocityTracker velocityTracker = this.velocityTracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.velocityTracker = null;
            }
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(motionEvent);
        if (this.viewDragHelper != null && (this.draggable || this.state == 1)) {
            z2 = true;
        }
        if (z2 && actionMasked == 2 && !this.ignoreEvents) {
            float abs = Math.abs(this.initialY - motionEvent.getY());
            Objects.requireNonNull(this.viewDragHelper);
            if (abs > viewDragHelper.mTouchSlop) {
                this.viewDragHelper.captureChildView(v, motionEvent.getPointerId(motionEvent.getActionIndex()));
            }
        }
        return !this.ignoreEvents;
    }

    /* JADX WARN: Type inference failed for: r7v1, types: [com.google.android.material.bottomsheet.BottomSheetBehavior$4] */
    public BottomSheetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int i;
        this.saveFlags = 0;
        this.fitToContents = true;
        this.maxWidth = -1;
        this.maxHeight = -1;
        this.settleRunnable = null;
        this.halfExpandedRatio = 0.5f;
        this.elevation = -1.0f;
        this.draggable = true;
        this.state = 4;
        this.callbacks = new ArrayList<>();
        this.expandHalfwayActionId = -1;
        this.dragCallback = new ViewDragHelper.Callback() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.4
            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final void onViewDragStateChanged(int i2) {
                if (i2 == 1) {
                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
                    if (bottomSheetBehavior.draggable) {
                        bottomSheetBehavior.setStateInternal(1);
                    }
                }
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final void onViewReleased(View view, float f, float f2) {
                int i2;
                int i22;
                boolean z;
                int i3 = 4;
                if (f2 < 0.0f) {
                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
                    if (bottomSheetBehavior.fitToContents) {
                        i2 = bottomSheetBehavior.fitToContentsOffset;
                    } else {
                        int top = view.getTop();
                        System.currentTimeMillis();
                        Objects.requireNonNull(BottomSheetBehavior.this);
                        BottomSheetBehavior bottomSheetBehavior2 = BottomSheetBehavior.this;
                        i22 = bottomSheetBehavior2.halfExpandedOffset;
                        if (top <= i22) {
                            i2 = bottomSheetBehavior2.getExpandedOffset();
                        }
                        i3 = 6;
                        i2 = i22;
                    }
                    i3 = 3;
                } else {
                    BottomSheetBehavior bottomSheetBehavior3 = BottomSheetBehavior.this;
                    if (bottomSheetBehavior3.hideable && bottomSheetBehavior3.shouldHide(view, f2)) {
                        if (Math.abs(f) >= Math.abs(f2) || f2 <= 500.0f) {
                            int top2 = view.getTop();
                            BottomSheetBehavior bottomSheetBehavior4 = BottomSheetBehavior.this;
                            if (top2 > (bottomSheetBehavior4.getExpandedOffset() + bottomSheetBehavior4.parentHeight) / 2) {
                                z = true;
                            } else {
                                z = false;
                            }
                            if (!z) {
                                BottomSheetBehavior bottomSheetBehavior5 = BottomSheetBehavior.this;
                                if (bottomSheetBehavior5.fitToContents) {
                                    i2 = bottomSheetBehavior5.fitToContentsOffset;
                                } else if (Math.abs(view.getTop() - BottomSheetBehavior.this.getExpandedOffset()) < Math.abs(view.getTop() - BottomSheetBehavior.this.halfExpandedOffset)) {
                                    i2 = BottomSheetBehavior.this.getExpandedOffset();
                                } else {
                                    i22 = BottomSheetBehavior.this.halfExpandedOffset;
                                    i3 = 6;
                                    i2 = i22;
                                }
                                i3 = 3;
                            }
                        }
                        i2 = BottomSheetBehavior.this.parentHeight;
                        i3 = 5;
                    } else if (f2 == 0.0f || Math.abs(f) > Math.abs(f2)) {
                        int top3 = view.getTop();
                        BottomSheetBehavior bottomSheetBehavior6 = BottomSheetBehavior.this;
                        if (!bottomSheetBehavior6.fitToContents) {
                            int i4 = bottomSheetBehavior6.halfExpandedOffset;
                            if (top3 < i4) {
                                if (top3 < Math.abs(top3 - bottomSheetBehavior6.collapsedOffset)) {
                                    i2 = BottomSheetBehavior.this.getExpandedOffset();
                                    i3 = 3;
                                } else {
                                    Objects.requireNonNull(BottomSheetBehavior.this);
                                    i22 = BottomSheetBehavior.this.halfExpandedOffset;
                                }
                            } else if (Math.abs(top3 - i4) < Math.abs(top3 - BottomSheetBehavior.this.collapsedOffset)) {
                                Objects.requireNonNull(BottomSheetBehavior.this);
                                i22 = BottomSheetBehavior.this.halfExpandedOffset;
                            } else {
                                i2 = BottomSheetBehavior.this.collapsedOffset;
                            }
                            i3 = 6;
                            i2 = i22;
                        } else if (Math.abs(top3 - bottomSheetBehavior6.fitToContentsOffset) < Math.abs(top3 - BottomSheetBehavior.this.collapsedOffset)) {
                            i2 = BottomSheetBehavior.this.fitToContentsOffset;
                            i3 = 3;
                        } else {
                            i2 = BottomSheetBehavior.this.collapsedOffset;
                        }
                    } else {
                        BottomSheetBehavior bottomSheetBehavior7 = BottomSheetBehavior.this;
                        if (bottomSheetBehavior7.fitToContents) {
                            i2 = bottomSheetBehavior7.collapsedOffset;
                        } else {
                            int top4 = view.getTop();
                            if (Math.abs(top4 - BottomSheetBehavior.this.halfExpandedOffset) < Math.abs(top4 - BottomSheetBehavior.this.collapsedOffset)) {
                                Objects.requireNonNull(BottomSheetBehavior.this);
                                i22 = BottomSheetBehavior.this.halfExpandedOffset;
                                i3 = 6;
                                i2 = i22;
                            } else {
                                i2 = BottomSheetBehavior.this.collapsedOffset;
                            }
                        }
                    }
                }
                BottomSheetBehavior bottomSheetBehavior8 = BottomSheetBehavior.this;
                Objects.requireNonNull(bottomSheetBehavior8);
                bottomSheetBehavior8.startSettlingAnimation(view, i3, i2, true);
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final int clampViewPositionVertical(View view, int i2) {
                int i22;
                int expandedOffset = BottomSheetBehavior.this.getExpandedOffset();
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
                if (bottomSheetBehavior.hideable) {
                    i22 = bottomSheetBehavior.parentHeight;
                } else {
                    i22 = bottomSheetBehavior.collapsedOffset;
                }
                return AtomicFU.clamp(i2, expandedOffset, i22);
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final int getViewVerticalDragRange() {
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
                if (bottomSheetBehavior.hideable) {
                    return bottomSheetBehavior.parentHeight;
                }
                return bottomSheetBehavior.collapsedOffset;
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final void onViewPositionChanged(View view, int i2, int i22) {
                BottomSheetBehavior.this.dispatchOnSlide(i22);
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final boolean tryCaptureView(View view, int i2) {
                View view2;
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
                int i22 = bottomSheetBehavior.state;
                if (i22 == 1 || bottomSheetBehavior.touchingScrollingChild) {
                    return false;
                }
                if (i22 == 3 && bottomSheetBehavior.activePointerId == i2) {
                    WeakReference<View> weakReference = bottomSheetBehavior.nestedScrollingChildRef;
                    if (weakReference != null) {
                        view2 = weakReference.get();
                    } else {
                        view2 = null;
                    }
                    if (view2 != null && view2.canScrollVertically(-1)) {
                        return false;
                    }
                }
                System.currentTimeMillis();
                WeakReference<V> weakReference2 = BottomSheetBehavior.this.viewRef;
                if (weakReference2 == null || weakReference2.get() != view) {
                    return false;
                }
                return true;
            }

            @Override // androidx.customview.widget.ViewDragHelper.Callback
            public final int clampViewPositionHorizontal(View view, int i2) {
                return view.getLeft();
            }
        };
        this.peekHeightGestureInsetBuffer = context.getResources().getDimensionPixelSize(2131166512);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.BottomSheetBehavior_Layout);
        this.shapeThemingEnabled = obtainStyledAttributes.hasValue(17);
        int i2 = 3;
        boolean hasValue = obtainStyledAttributes.hasValue(3);
        if (hasValue) {
            createMaterialShapeDrawable(context, attributeSet, hasValue, MaterialResources.getColorStateList(context, obtainStyledAttributes, 3));
        } else {
            createMaterialShapeDrawable(context, attributeSet, hasValue, null);
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.interpolatorAnimator = ofFloat;
        ofFloat.setDuration(500L);
        this.interpolatorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.bottomsheet.BottomSheetBehavior.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                MaterialShapeDrawable materialShapeDrawable = BottomSheetBehavior.this.materialShapeDrawable;
                if (materialShapeDrawable != null) {
                    materialShapeDrawable.setInterpolation(floatValue);
                }
            }
        });
        this.elevation = obtainStyledAttributes.getDimension(2, -1.0f);
        if (obtainStyledAttributes.hasValue(0)) {
            this.maxWidth = obtainStyledAttributes.getDimensionPixelSize(0, -1);
        }
        if (obtainStyledAttributes.hasValue(1)) {
            this.maxHeight = obtainStyledAttributes.getDimensionPixelSize(1, -1);
        }
        TypedValue peekValue = obtainStyledAttributes.peekValue(9);
        if (peekValue == null || (i = peekValue.data) != -1) {
            setPeekHeight(obtainStyledAttributes.getDimensionPixelSize(9, -1));
        } else {
            setPeekHeight(i);
        }
        boolean z = obtainStyledAttributes.getBoolean(8, false);
        if (this.hideable != z) {
            this.hideable = z;
            if (!z && this.state == 5) {
                setState(4);
            }
            updateAccessibilityActions$1();
        }
        this.gestureInsetBottomIgnored = obtainStyledAttributes.getBoolean(12, false);
        boolean z2 = obtainStyledAttributes.getBoolean(6, true);
        if (this.fitToContents != z2) {
            this.fitToContents = z2;
            if (this.viewRef != null) {
                calculateCollapsedOffset();
            }
            setStateInternal((!this.fitToContents || this.state != 6) ? this.state : i2);
            updateAccessibilityActions$1();
        }
        this.skipCollapsed = obtainStyledAttributes.getBoolean(11, false);
        this.draggable = obtainStyledAttributes.getBoolean(4, true);
        this.saveFlags = obtainStyledAttributes.getInt(10, 0);
        float f = obtainStyledAttributes.getFloat(7, 0.5f);
        if (f <= 0.0f || f >= 1.0f) {
            throw new IllegalArgumentException("ratio must be a float value between 0 and 1");
        }
        this.halfExpandedRatio = f;
        if (this.viewRef != null) {
            this.halfExpandedOffset = (int) ((1.0f - f) * this.parentHeight);
        }
        TypedValue peekValue2 = obtainStyledAttributes.peekValue(5);
        if (peekValue2 == null || peekValue2.type != 16) {
            int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(5, 0);
            if (dimensionPixelOffset >= 0) {
                this.expandedOffset = dimensionPixelOffset;
            } else {
                throw new IllegalArgumentException("offset must be greater than or equal to 0");
            }
        } else {
            int i3 = peekValue2.data;
            if (i3 >= 0) {
                this.expandedOffset = i3;
            } else {
                throw new IllegalArgumentException("offset must be greater than or equal to 0");
            }
        }
        this.paddingBottomSystemWindowInsets = obtainStyledAttributes.getBoolean(13, false);
        this.paddingLeftSystemWindowInsets = obtainStyledAttributes.getBoolean(14, false);
        this.paddingRightSystemWindowInsets = obtainStyledAttributes.getBoolean(15, false);
        this.paddingTopSystemWindowInsets = obtainStyledAttributes.getBoolean(16, true);
        obtainStyledAttributes.recycle();
        this.maximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    public int getPeekHeightMin() {
        return this.peekHeightMin;
    }
}
