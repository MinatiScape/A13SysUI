package androidx.drawerlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.RotationUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.graphics.Insets;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.R$styleable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class DrawerLayout extends ViewGroup {
    public Rect mChildHitRect;
    public Matrix mChildInvertedMatrix;
    public boolean mChildrenCanceledTouch;
    public boolean mDrawStatusBarBackground;
    public float mDrawerElevation;
    public int mDrawerState;
    public boolean mInLayout;
    public float mInitialMotionX;
    public float mInitialMotionY;
    public WindowInsetsCompat mLastInsets;
    public final ViewDragCallback mLeftCallback;
    public final ViewDragHelper mLeftDragger;
    public int mMinDrawerMargin;
    public final ArrayList<View> mNonDrawerViews;
    public final ViewDragCallback mRightCallback;
    public final ViewDragHelper mRightDragger;
    public float mScrimOpacity;
    public Drawable mStatusBarBackground;
    public static final int[] THEME_ATTRS = {16843828};
    public static final int[] LAYOUT_ATTRS = {16842931};
    public int mScrimColor = -1728053248;
    public Paint mScrimPaint = new Paint();
    public boolean mFirstLayout = true;
    public int mLockModeLeft = 3;
    public int mLockModeRight = 3;
    public int mLockModeStart = 3;
    public int mLockModeEnd = 3;
    public final AnonymousClass1 mActionDismiss = new AccessibilityViewCommand() { // from class: androidx.drawerlayout.widget.DrawerLayout.1
        @Override // androidx.core.view.accessibility.AccessibilityViewCommand
        public final boolean perform(View view) {
            Objects.requireNonNull(DrawerLayout.this);
            if (!DrawerLayout.isDrawerOpen(view) || DrawerLayout.this.getDrawerLockMode(view) == 2) {
                return false;
            }
            DrawerLayout.this.closeDrawer(view);
            return true;
        }
    };

    /* renamed from: androidx.drawerlayout.widget.DrawerLayout$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass2 implements OnApplyWindowInsetsListener {
        public static void setup(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, int i, float f, float f2) {
            float f3;
            if (i != 0) {
                SurfaceControl build = new SurfaceControl.Builder().setName("Transition Unrotate").setContainerLayer().setParent(surfaceControl).build();
                RotationUtils.rotateSurface(transaction, build, i);
                boolean z = false;
                Point point = new Point(0, 0);
                if (i % 2 != 0) {
                    z = true;
                }
                if (z) {
                    f3 = f2;
                } else {
                    f3 = f;
                }
                if (!z) {
                    f = f2;
                }
                RotationUtils.rotatePoint(point, i, (int) f3, (int) f);
                transaction.setPosition(build, point.x, point.y);
                transaction.show(build);
            }
        }

        @Override // androidx.core.view.OnApplyWindowInsetsListener
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            boolean z;
            DrawerLayout drawerLayout = (DrawerLayout) view;
            boolean z2 = true;
            if (windowInsetsCompat.mImpl.getSystemWindowInsets().top > 0) {
                z = true;
            } else {
                z = false;
            }
            Objects.requireNonNull(drawerLayout);
            drawerLayout.mLastInsets = windowInsetsCompat;
            drawerLayout.mDrawStatusBarBackground = z;
            if (z || drawerLayout.getBackground() != null) {
                z2 = false;
            }
            drawerLayout.setWillNotDraw(z2);
            drawerLayout.requestLayout();
            return windowInsetsCompat.mImpl.consumeSystemWindowInsets();
        }
    }

    /* loaded from: classes.dex */
    public class AccessibilityDelegate extends AccessibilityDelegateCompat {
        public AccessibilityDelegate() {
            new Rect();
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            int[] iArr = DrawerLayout.THEME_ATTRS;
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName("androidx.drawerlayout.widget.DrawerLayout");
            accessibilityNodeInfoCompat.mInfo.setFocusable(false);
            accessibilityNodeInfoCompat.mInfo.setFocused(false);
            accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
            accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            int[] iArr = DrawerLayout.THEME_ATTRS;
            return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            if (accessibilityEvent.getEventType() != 32) {
                return super.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
            }
            accessibilityEvent.getText();
            View findVisibleDrawer = DrawerLayout.this.findVisibleDrawer();
            if (findVisibleDrawer == null) {
                return true;
            }
            int drawerViewAbsoluteGravity = DrawerLayout.this.getDrawerViewAbsoluteGravity(findVisibleDrawer);
            DrawerLayout drawerLayout = DrawerLayout.this;
            Objects.requireNonNull(drawerLayout);
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            Gravity.getAbsoluteGravity(drawerViewAbsoluteGravity, ViewCompat.Api17Impl.getLayoutDirection(drawerLayout));
            return true;
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName("androidx.drawerlayout.widget.DrawerLayout");
        }
    }

    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: androidx.drawerlayout.widget.DrawerLayout.SavedState.1
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
        public int lockModeEnd;
        public int lockModeLeft;
        public int lockModeRight;
        public int lockModeStart;
        public int openDrawerGravity;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.openDrawerGravity = 0;
            this.openDrawerGravity = parcel.readInt();
            this.lockModeLeft = parcel.readInt();
            this.lockModeRight = parcel.readInt();
            this.lockModeStart = parcel.readInt();
            this.lockModeEnd = parcel.readInt();
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mSuperState, i);
            parcel.writeInt(this.openDrawerGravity);
            parcel.writeInt(this.lockModeLeft);
            parcel.writeInt(this.lockModeRight);
            parcel.writeInt(this.lockModeStart);
            parcel.writeInt(this.lockModeEnd);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
            this.openDrawerGravity = 0;
        }
    }

    /* loaded from: classes.dex */
    public class ViewDragCallback extends ViewDragHelper.Callback {
        public final int mAbsGravity;
        public ViewDragHelper mDragger;
        public final AnonymousClass1 mPeekRunnable = new Runnable() { // from class: androidx.drawerlayout.widget.DrawerLayout.ViewDragCallback.1
            @Override // java.lang.Runnable
            public final void run() {
                boolean z;
                int i;
                View view;
                int i2;
                ViewDragCallback viewDragCallback = ViewDragCallback.this;
                Objects.requireNonNull(viewDragCallback);
                ViewDragHelper viewDragHelper = viewDragCallback.mDragger;
                Objects.requireNonNull(viewDragHelper);
                int i3 = viewDragHelper.mEdgeSize;
                int i4 = 3;
                if (viewDragCallback.mAbsGravity == 3) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    view = DrawerLayout.this.findDrawerWithGravity(3);
                    if (view != null) {
                        i2 = -view.getWidth();
                    } else {
                        i2 = 0;
                    }
                    i = i2 + i3;
                } else {
                    view = DrawerLayout.this.findDrawerWithGravity(5);
                    i = DrawerLayout.this.getWidth() - i3;
                }
                if (view == null) {
                    return;
                }
                if (((z && view.getLeft() < i) || (!z && view.getLeft() > i)) && DrawerLayout.this.getDrawerLockMode(view) == 0) {
                    viewDragCallback.mDragger.smoothSlideViewTo(view, i, view.getTop());
                    ((LayoutParams) view.getLayoutParams()).isPeeking = true;
                    DrawerLayout.this.invalidate();
                    if (viewDragCallback.mAbsGravity == 3) {
                        i4 = 5;
                    }
                    View findDrawerWithGravity = DrawerLayout.this.findDrawerWithGravity(i4);
                    if (findDrawerWithGravity != null) {
                        DrawerLayout.this.closeDrawer(findDrawerWithGravity);
                    }
                    DrawerLayout drawerLayout = DrawerLayout.this;
                    Objects.requireNonNull(drawerLayout);
                    if (!drawerLayout.mChildrenCanceledTouch) {
                        long uptimeMillis = SystemClock.uptimeMillis();
                        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                        int childCount = drawerLayout.getChildCount();
                        for (int i5 = 0; i5 < childCount; i5++) {
                            drawerLayout.getChildAt(i5).dispatchTouchEvent(obtain);
                        }
                        obtain.recycle();
                        drawerLayout.mChildrenCanceledTouch = true;
                    }
                }
            }
        };

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final void onEdgeDragStarted(int i, int i2) {
            View view;
            if ((i & 1) == 1) {
                view = DrawerLayout.this.findDrawerWithGravity(3);
            } else {
                view = DrawerLayout.this.findDrawerWithGravity(5);
            }
            if (view != null && DrawerLayout.this.getDrawerLockMode(view) == 0) {
                this.mDragger.captureChildView(view, i2);
            }
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [androidx.drawerlayout.widget.DrawerLayout$ViewDragCallback$1] */
        public ViewDragCallback(int i) {
            this.mAbsGravity = i;
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final int clampViewPositionHorizontal(View view, int i) {
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3)) {
                return Math.max(-view.getWidth(), Math.min(i, 0));
            }
            int width = DrawerLayout.this.getWidth();
            return Math.max(width - view.getWidth(), Math.min(i, width));
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final int getViewHorizontalDragRange(View view) {
            Objects.requireNonNull(DrawerLayout.this);
            if (DrawerLayout.isDrawerView(view)) {
                return view.getWidth();
            }
            return 0;
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final void onEdgeTouched() {
            DrawerLayout.this.postDelayed(this.mPeekRunnable, 160L);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final void onViewDragStateChanged(int i) {
            View rootView;
            DrawerLayout drawerLayout = DrawerLayout.this;
            ViewDragHelper viewDragHelper = this.mDragger;
            Objects.requireNonNull(viewDragHelper);
            View view = viewDragHelper.mCapturedView;
            Objects.requireNonNull(drawerLayout);
            ViewDragHelper viewDragHelper2 = drawerLayout.mLeftDragger;
            Objects.requireNonNull(viewDragHelper2);
            int i2 = viewDragHelper2.mDragState;
            ViewDragHelper viewDragHelper3 = drawerLayout.mRightDragger;
            Objects.requireNonNull(viewDragHelper3);
            int i3 = viewDragHelper3.mDragState;
            int i4 = 2;
            if (i2 == 1 || i3 == 1) {
                i4 = 1;
            } else if (!(i2 == 2 || i3 == 2)) {
                i4 = 0;
            }
            if (view != null && i == 0) {
                float f = ((LayoutParams) view.getLayoutParams()).onScreen;
                if (f == 0.0f) {
                    LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                    if ((layoutParams.openState & 1) == 1) {
                        layoutParams.openState = 0;
                        drawerLayout.updateChildrenImportantForAccessibility(view, false);
                        drawerLayout.updateChildAccessibilityAction(view);
                        if (drawerLayout.hasWindowFocus() && (rootView = drawerLayout.getRootView()) != null) {
                            rootView.sendAccessibilityEvent(32);
                        }
                    }
                } else if (f == 1.0f) {
                    LayoutParams layoutParams2 = (LayoutParams) view.getLayoutParams();
                    if ((layoutParams2.openState & 1) == 0) {
                        layoutParams2.openState = 1;
                        drawerLayout.updateChildrenImportantForAccessibility(view, true);
                        drawerLayout.updateChildAccessibilityAction(view);
                        if (drawerLayout.hasWindowFocus()) {
                            drawerLayout.sendAccessibilityEvent(32);
                        }
                    }
                }
            }
            if (i4 != drawerLayout.mDrawerState) {
                drawerLayout.mDrawerState = i4;
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final void onViewReleased(View view, float f, float f2) {
            int i;
            Objects.requireNonNull(DrawerLayout.this);
            float f3 = ((LayoutParams) view.getLayoutParams()).onScreen;
            int width = view.getWidth();
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3)) {
                int i2 = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
                if (i2 > 0 || (i2 == 0 && f3 > 0.5f)) {
                    i = 0;
                } else {
                    i = -width;
                }
            } else {
                int width2 = DrawerLayout.this.getWidth();
                if (f < 0.0f || (f == 0.0f && f3 > 0.5f)) {
                    width2 -= width;
                }
                i = width2;
            }
            this.mDragger.settleCapturedViewAt(i, view.getTop());
            DrawerLayout.this.invalidate();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final boolean tryCaptureView(View view, int i) {
            Objects.requireNonNull(DrawerLayout.this);
            if (!DrawerLayout.isDrawerView(view) || !DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, this.mAbsGravity) || DrawerLayout.this.getDrawerLockMode(view) != 0) {
                return false;
            }
            return true;
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final int clampViewPositionVertical(View view, int i) {
            return view.getTop();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final void onViewCaptured(View view, int i) {
            ((LayoutParams) view.getLayoutParams()).isPeeking = false;
            int i2 = 3;
            if (this.mAbsGravity == 3) {
                i2 = 5;
            }
            View findDrawerWithGravity = DrawerLayout.this.findDrawerWithGravity(i2);
            if (findDrawerWithGravity != null) {
                DrawerLayout.this.closeDrawer(findDrawerWithGravity);
            }
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public final void onViewPositionChanged(View view, int i, int i2) {
            float f;
            int i3;
            int width = view.getWidth();
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3)) {
                f = i + width;
            } else {
                f = DrawerLayout.this.getWidth() - i;
            }
            float f2 = f / width;
            Objects.requireNonNull(DrawerLayout.this);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (f2 != layoutParams.onScreen) {
                layoutParams.onScreen = f2;
            }
            if (f2 == 0.0f) {
                i3 = 4;
            } else {
                i3 = 0;
            }
            view.setVisibility(i3);
            DrawerLayout.this.invalidate();
        }
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        boolean z;
        if (i == 4) {
            if (findVisibleDrawer() != null) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                keyEvent.startTracking();
                return true;
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public final boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return super.onKeyUp(i, keyEvent);
        }
        View findVisibleDrawer = findVisibleDrawer();
        if (findVisibleDrawer != null && getDrawerLockMode(findVisibleDrawer) == 0) {
            closeDrawers(false);
        }
        if (findVisibleDrawer != null) {
            return true;
        }
        return false;
    }

    @Override // android.view.View
    public final void onRtlPropertiesChanged(int i) {
    }

    /* loaded from: classes.dex */
    public static final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat {
        @Override // androidx.core.view.AccessibilityDelegateCompat
        public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            boolean z;
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            int[] iArr = DrawerLayout.THEME_ATTRS;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            if (ViewCompat.Api16Impl.getImportantForAccessibility(view) == 4 || ViewCompat.Api16Impl.getImportantForAccessibility(view) == 2) {
                z = false;
            } else {
                z = true;
            }
            if (!z) {
                accessibilityNodeInfoCompat.mParentVirtualDescendantId = -1;
                accessibilityNodeInfoCompat.mInfo.setParent(null);
            }
        }
    }

    @Override // android.view.ViewGroup
    public final boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (!(layoutParams instanceof LayoutParams) || !super.checkLayoutParams(layoutParams)) {
            return false;
        }
        return true;
    }

    public final View findDrawerWithGravity(int i) {
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        int absoluteGravity = Gravity.getAbsoluteGravity(i, ViewCompat.Api17Impl.getLayoutDirection(this)) & 7;
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if ((getDrawerViewAbsoluteGravity(childAt) & 7) == absoluteGravity) {
                return childAt;
            }
        }
        return null;
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float f;
        int i5;
        boolean z2;
        int i6;
        int i7;
        boolean z3 = true;
        this.mInLayout = true;
        int i8 = i3 - i;
        int childCount = getChildCount();
        int i9 = 0;
        while (i9 < childCount) {
            View childAt = getChildAt(i9);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (isContentView(childAt)) {
                    int i10 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
                    childAt.layout(i10, ((ViewGroup.MarginLayoutParams) layoutParams).topMargin, childAt.getMeasuredWidth() + i10, childAt.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin);
                } else {
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight = childAt.getMeasuredHeight();
                    if (checkDrawerViewAbsoluteGravity(childAt, 3)) {
                        float f2 = measuredWidth;
                        i5 = (-measuredWidth) + ((int) (layoutParams.onScreen * f2));
                        f = (measuredWidth + i5) / f2;
                    } else {
                        float f3 = measuredWidth;
                        f = (i8 - i7) / f3;
                        i5 = i8 - ((int) (layoutParams.onScreen * f3));
                    }
                    if (f != layoutParams.onScreen) {
                        z2 = z3;
                    } else {
                        z2 = false;
                    }
                    int i11 = layoutParams.gravity & 112;
                    if (i11 == 16) {
                        int i12 = i4 - i2;
                        int i13 = (i12 - measuredHeight) / 2;
                        int i14 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                        if (i13 < i14) {
                            i13 = i14;
                        } else {
                            int i15 = i13 + measuredHeight;
                            int i16 = i12 - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                            if (i15 > i16) {
                                i13 = i16 - measuredHeight;
                            }
                        }
                        childAt.layout(i5, i13, measuredWidth + i5, measuredHeight + i13);
                    } else if (i11 != 80) {
                        int i17 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                        childAt.layout(i5, i17, measuredWidth + i5, measuredHeight + i17);
                    } else {
                        int i18 = i4 - i2;
                        childAt.layout(i5, (i18 - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin) - childAt.getMeasuredHeight(), measuredWidth + i5, i18 - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin);
                    }
                    if (z2) {
                        LayoutParams layoutParams2 = (LayoutParams) childAt.getLayoutParams();
                        if (f != layoutParams2.onScreen) {
                            layoutParams2.onScreen = f;
                        }
                    }
                    if (layoutParams.onScreen > 0.0f) {
                        i6 = 0;
                    } else {
                        i6 = 4;
                    }
                    if (childAt.getVisibility() != i6) {
                        childAt.setVisibility(i6);
                    }
                }
            }
            i9++;
            z3 = true;
        }
        WindowInsets rootWindowInsets = getRootWindowInsets();
        if (rootWindowInsets != null) {
            Insets systemGestureInsets = WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets, null).mImpl.getSystemGestureInsets();
            ViewDragHelper viewDragHelper = this.mLeftDragger;
            Objects.requireNonNull(viewDragHelper);
            viewDragHelper.mEdgeSize = Math.max(viewDragHelper.mDefaultEdgeSize, systemGestureInsets.left);
            ViewDragHelper viewDragHelper2 = this.mRightDragger;
            Objects.requireNonNull(viewDragHelper2);
            viewDragHelper2.mEdgeSize = Math.max(viewDragHelper2.mDefaultEdgeSize, systemGestureInsets.right);
        }
        this.mInLayout = false;
        this.mFirstLayout = false;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0048  */
    @Override // android.view.View
    @android.annotation.SuppressLint({"WrongConstant"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onMeasure(int r17, int r18) {
        /*
            Method dump skipped, instructions count: 433
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.drawerlayout.widget.DrawerLayout.onMeasure(int, int):void");
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        View findDrawerWithGravity;
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        Objects.requireNonNull(savedState);
        super.onRestoreInstanceState(savedState.mSuperState);
        int i = savedState.openDrawerGravity;
        if (!(i == 0 || (findDrawerWithGravity = findDrawerWithGravity(i)) == null)) {
            openDrawer(findDrawerWithGravity);
        }
        int i2 = savedState.lockModeLeft;
        if (i2 != 3) {
            setDrawerLockMode(i2, 3);
        }
        int i3 = savedState.lockModeRight;
        if (i3 != 3) {
            setDrawerLockMode(i3, 5);
        }
        int i4 = savedState.lockModeStart;
        if (i4 != 3) {
            setDrawerLockMode(i4, 8388611);
        }
        int i5 = savedState.lockModeEnd;
        if (i5 != 3) {
            setDrawerLockMode(i5, 8388613);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0059, code lost:
        if (getDrawerLockMode(r7) != 2) goto L_0x005c;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            androidx.customview.widget.ViewDragHelper r0 = r6.mLeftDragger
            r0.processTouchEvent(r7)
            androidx.customview.widget.ViewDragHelper r0 = r6.mRightDragger
            r0.processTouchEvent(r7)
            int r0 = r7.getAction()
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0060
            if (r0 == r2) goto L_0x0020
            r7 = 3
            if (r0 == r7) goto L_0x001a
            goto L_0x006e
        L_0x001a:
            r6.closeDrawers(r2)
            r6.mChildrenCanceledTouch = r1
            goto L_0x006e
        L_0x0020:
            float r0 = r7.getX()
            float r7 = r7.getY()
            androidx.customview.widget.ViewDragHelper r3 = r6.mLeftDragger
            int r4 = (int) r0
            int r5 = (int) r7
            android.view.View r3 = r3.findTopChildUnder(r4, r5)
            if (r3 == 0) goto L_0x005b
            boolean r3 = isContentView(r3)
            if (r3 == 0) goto L_0x005b
            float r3 = r6.mInitialMotionX
            float r0 = r0 - r3
            float r3 = r6.mInitialMotionY
            float r7 = r7 - r3
            androidx.customview.widget.ViewDragHelper r3 = r6.mLeftDragger
            java.util.Objects.requireNonNull(r3)
            int r3 = r3.mTouchSlop
            float r0 = r0 * r0
            float r7 = r7 * r7
            float r7 = r7 + r0
            int r3 = r3 * r3
            float r0 = (float) r3
            int r7 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1))
            if (r7 >= 0) goto L_0x005b
            android.view.View r7 = r6.findOpenDrawer()
            if (r7 == 0) goto L_0x005b
            int r7 = r6.getDrawerLockMode(r7)
            r0 = 2
            if (r7 != r0) goto L_0x005c
        L_0x005b:
            r1 = r2
        L_0x005c:
            r6.closeDrawers(r1)
            goto L_0x006e
        L_0x0060:
            float r0 = r7.getX()
            float r7 = r7.getY()
            r6.mInitialMotionX = r0
            r6.mInitialMotionY = r7
            r6.mChildrenCanceledTouch = r1
        L_0x006e:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.drawerlayout.widget.DrawerLayout.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View, android.view.ViewParent
    public final void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }
    }

    public final void setDrawerLockMode(int i, int i2) {
        View findDrawerWithGravity;
        ViewDragHelper viewDragHelper;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        int absoluteGravity = Gravity.getAbsoluteGravity(i2, ViewCompat.Api17Impl.getLayoutDirection(this));
        if (i2 == 3) {
            this.mLockModeLeft = i;
        } else if (i2 == 5) {
            this.mLockModeRight = i;
        } else if (i2 == 8388611) {
            this.mLockModeStart = i;
        } else if (i2 == 8388613) {
            this.mLockModeEnd = i;
        }
        if (i != 0) {
            if (absoluteGravity == 3) {
                viewDragHelper = this.mLeftDragger;
            } else {
                viewDragHelper = this.mRightDragger;
            }
            viewDragHelper.cancel();
        }
        if (i == 1) {
            View findDrawerWithGravity2 = findDrawerWithGravity(absoluteGravity);
            if (findDrawerWithGravity2 != null) {
                closeDrawer(findDrawerWithGravity2);
            }
        } else if (i == 2 && (findDrawerWithGravity = findDrawerWithGravity(absoluteGravity)) != null) {
            openDrawer(findDrawerWithGravity);
        }
    }

    public final void updateChildAccessibilityAction(View view) {
        AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_DISMISS;
        ViewCompat.removeActionWithId(accessibilityActionCompat.getId(), view);
        ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(view, 0);
        if (isDrawerOpen(view) && getDrawerLockMode(view) != 2) {
            ViewCompat.replaceAccessibilityAction(view, accessibilityActionCompat, null, this.mActionDismiss);
        }
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Type inference failed for: r3v0, types: [androidx.drawerlayout.widget.DrawerLayout$1] */
    public DrawerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 2130968982);
        TypedArray obtainStyledAttributes;
        new ChildAccessibilityDelegate();
        setDescendantFocusability(262144);
        float f = getResources().getDisplayMetrics().density;
        this.mMinDrawerMargin = (int) ((64.0f * f) + 0.5f);
        float f2 = f * 400.0f;
        ViewDragCallback viewDragCallback = new ViewDragCallback(3);
        this.mLeftCallback = viewDragCallback;
        ViewDragCallback viewDragCallback2 = new ViewDragCallback(5);
        this.mRightCallback = viewDragCallback2;
        ViewDragHelper viewDragHelper = new ViewDragHelper(getContext(), this, viewDragCallback);
        viewDragHelper.mTouchSlop = (int) (viewDragHelper.mTouchSlop * 1.0f);
        this.mLeftDragger = viewDragHelper;
        viewDragHelper.mTrackingEdges = 1;
        viewDragHelper.mMinVelocity = f2;
        viewDragCallback.mDragger = viewDragHelper;
        ViewDragHelper viewDragHelper2 = new ViewDragHelper(getContext(), this, viewDragCallback2);
        viewDragHelper2.mTouchSlop = (int) (viewDragHelper2.mTouchSlop * 1.0f);
        this.mRightDragger = viewDragHelper2;
        viewDragHelper2.mTrackingEdges = 2;
        viewDragHelper2.mMinVelocity = f2;
        viewDragCallback2.mDragger = viewDragHelper2;
        setFocusableInTouchMode(true);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api16Impl.setImportantForAccessibility(this, 1);
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
        setMotionEventSplittingEnabled(false);
        if (ViewCompat.Api16Impl.getFitsSystemWindows(this)) {
            ViewCompat.Api21Impl.setOnApplyWindowInsetsListener(this, new AnonymousClass2());
            setSystemUiVisibility(1280);
            obtainStyledAttributes = context.obtainStyledAttributes(THEME_ATTRS);
            try {
                this.mStatusBarBackground = obtainStyledAttributes.getDrawable(0);
                obtainStyledAttributes.recycle();
            } catch (Throwable th) {
                throw th;
            }
        }
        obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.DrawerLayout, 2130968982, 0);
        try {
            if (obtainStyledAttributes.hasValue(0)) {
                this.mDrawerElevation = obtainStyledAttributes.getDimension(0, 0.0f);
            } else {
                this.mDrawerElevation = getResources().getDimension(2131165600);
            }
            obtainStyledAttributes.recycle();
            this.mNonDrawerViews = new ArrayList<>();
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public static boolean isContentView(View view) {
        if (((LayoutParams) view.getLayoutParams()).gravity == 0) {
            return true;
        }
        return false;
    }

    public static boolean isDrawerOpen(View view) {
        if (!isDrawerView(view)) {
            throw new IllegalArgumentException("View " + view + " is not a drawer");
        } else if ((((LayoutParams) view.getLayoutParams()).openState & 1) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDrawerView(View view) {
        int i = ((LayoutParams) view.getLayoutParams()).gravity;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        int absoluteGravity = Gravity.getAbsoluteGravity(i, ViewCompat.Api17Impl.getLayoutDirection(view));
        if ((absoluteGravity & 3) == 0 && (absoluteGravity & 5) == 0) {
            return false;
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void addFocusables(ArrayList<View> arrayList, int i, int i2) {
        if (getDescendantFocusability() != 393216) {
            int childCount = getChildCount();
            boolean z = false;
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (!isDrawerView(childAt)) {
                    this.mNonDrawerViews.add(childAt);
                } else if (isDrawerOpen(childAt)) {
                    childAt.addFocusables(arrayList, i, i2);
                    z = true;
                }
            }
            if (!z) {
                int size = this.mNonDrawerViews.size();
                for (int i4 = 0; i4 < size; i4++) {
                    View view = this.mNonDrawerViews.get(i4);
                    if (view.getVisibility() == 0) {
                        view.addFocusables(arrayList, i, i2);
                    }
                }
            }
            this.mNonDrawerViews.clear();
        }
    }

    @Override // android.view.ViewGroup
    public final void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, i, layoutParams);
        if (findOpenDrawer() != null || isDrawerView(view)) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.setImportantForAccessibility(view, 4);
            return;
        }
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api16Impl.setImportantForAccessibility(view, 1);
    }

    public final boolean checkDrawerViewAbsoluteGravity(View view, int i) {
        if ((getDrawerViewAbsoluteGravity(view) & i) == i) {
            return true;
        }
        return false;
    }

    public final void closeDrawer(View view) {
        if (isDrawerView(view)) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (this.mFirstLayout) {
                layoutParams.onScreen = 0.0f;
                layoutParams.openState = 0;
            } else {
                layoutParams.openState |= 4;
                if (checkDrawerViewAbsoluteGravity(view, 3)) {
                    this.mLeftDragger.smoothSlideViewTo(view, -view.getWidth(), view.getTop());
                } else {
                    this.mRightDragger.smoothSlideViewTo(view, getWidth(), view.getTop());
                }
            }
            invalidate();
            return;
        }
        throw new IllegalArgumentException("View " + view + " is not a sliding drawer");
    }

    public final void closeDrawers(boolean z) {
        boolean z2;
        int childCount = getChildCount();
        boolean z3 = false;
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (isDrawerView(childAt) && (!z || layoutParams.isPeeking)) {
                int width = childAt.getWidth();
                if (checkDrawerViewAbsoluteGravity(childAt, 3)) {
                    z2 = this.mLeftDragger.smoothSlideViewTo(childAt, -width, childAt.getTop());
                } else {
                    z2 = this.mRightDragger.smoothSlideViewTo(childAt, getWidth(), childAt.getTop());
                }
                z3 |= z2;
                layoutParams.isPeeking = false;
            }
        }
        ViewDragCallback viewDragCallback = this.mLeftCallback;
        Objects.requireNonNull(viewDragCallback);
        DrawerLayout.this.removeCallbacks(viewDragCallback.mPeekRunnable);
        ViewDragCallback viewDragCallback2 = this.mRightCallback;
        Objects.requireNonNull(viewDragCallback2);
        DrawerLayout.this.removeCallbacks(viewDragCallback2.mPeekRunnable);
        if (z3) {
            invalidate();
        }
    }

    @Override // android.view.View
    public final void computeScroll() {
        int childCount = getChildCount();
        float f = 0.0f;
        for (int i = 0; i < childCount; i++) {
            f = Math.max(f, ((LayoutParams) getChildAt(i).getLayoutParams()).onScreen);
        }
        this.mScrimOpacity = f;
        boolean continueSettling = this.mLeftDragger.continueSettling();
        boolean continueSettling2 = this.mRightDragger.continueSettling();
        if (continueSettling || continueSettling2) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.postInvalidateOnAnimation(this);
        }
    }

    @Override // android.view.View
    public final boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        boolean z;
        if ((motionEvent.getSource() & 2) == 0 || motionEvent.getAction() == 10 || this.mScrimOpacity <= 0.0f) {
            return super.dispatchGenericMotionEvent(motionEvent);
        }
        int childCount = getChildCount();
        if (childCount == 0) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        for (int i = childCount - 1; i >= 0; i--) {
            View childAt = getChildAt(i);
            if (this.mChildHitRect == null) {
                this.mChildHitRect = new Rect();
            }
            childAt.getHitRect(this.mChildHitRect);
            if (this.mChildHitRect.contains((int) x, (int) y) && !isContentView(childAt)) {
                if (!childAt.getMatrix().isIdentity()) {
                    MotionEvent obtain = MotionEvent.obtain(motionEvent);
                    obtain.offsetLocation(getScrollX() - childAt.getLeft(), getScrollY() - childAt.getTop());
                    Matrix matrix = childAt.getMatrix();
                    if (!matrix.isIdentity()) {
                        if (this.mChildInvertedMatrix == null) {
                            this.mChildInvertedMatrix = new Matrix();
                        }
                        matrix.invert(this.mChildInvertedMatrix);
                        obtain.transform(this.mChildInvertedMatrix);
                    }
                    z = childAt.dispatchGenericMotionEvent(obtain);
                    obtain.recycle();
                } else {
                    float scrollX = getScrollX() - childAt.getLeft();
                    float scrollY = getScrollY() - childAt.getTop();
                    motionEvent.offsetLocation(scrollX, scrollY);
                    z = childAt.dispatchGenericMotionEvent(motionEvent);
                    motionEvent.offsetLocation(-scrollX, -scrollY);
                }
                if (z) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public final boolean drawChild(Canvas canvas, View view, long j) {
        int i;
        boolean z;
        int height = getHeight();
        boolean isContentView = isContentView(view);
        int width = getWidth();
        int save = canvas.save();
        int i2 = 0;
        if (isContentView) {
            int childCount = getChildCount();
            int i3 = 0;
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = getChildAt(i4);
                if (childAt != view && childAt.getVisibility() == 0) {
                    Drawable background = childAt.getBackground();
                    if (background == null || background.getOpacity() != -1) {
                        z = false;
                    } else {
                        z = true;
                    }
                    if (z && isDrawerView(childAt) && childAt.getHeight() >= height) {
                        if (checkDrawerViewAbsoluteGravity(childAt, 3)) {
                            int right = childAt.getRight();
                            if (right > i3) {
                                i3 = right;
                            }
                        } else {
                            int left = childAt.getLeft();
                            if (left < width) {
                                width = left;
                            }
                        }
                    }
                }
            }
            canvas.clipRect(i3, 0, width, getHeight());
            i2 = i3;
        }
        boolean drawChild = super.drawChild(canvas, view, j);
        canvas.restoreToCount(save);
        float f = this.mScrimOpacity;
        if (f > 0.0f && isContentView) {
            this.mScrimPaint.setColor((((int) ((((-16777216) & i) >>> 24) * f)) << 24) | (this.mScrimColor & 16777215));
            canvas.drawRect(i2, 0.0f, width, getHeight(), this.mScrimPaint);
        }
        return drawChild;
    }

    public final View findOpenDrawer() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if ((((LayoutParams) childAt.getLayoutParams()).openState & 1) == 1) {
                return childAt;
            }
        }
        return null;
    }

    public final View findVisibleDrawer() {
        boolean z;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (isDrawerView(childAt)) {
                if (isDrawerView(childAt)) {
                    if (((LayoutParams) childAt.getLayoutParams()).onScreen > 0.0f) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        return childAt;
                    }
                } else {
                    throw new IllegalArgumentException("View " + childAt + " is not a drawer");
                }
            }
        }
        return null;
    }

    public final int getDrawerLockMode(View view) {
        int i;
        int i2;
        int i3;
        int i4;
        if (isDrawerView(view)) {
            int i5 = ((LayoutParams) view.getLayoutParams()).gravity;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            int layoutDirection = ViewCompat.Api17Impl.getLayoutDirection(this);
            if (i5 == 3) {
                int i6 = this.mLockModeLeft;
                if (i6 != 3) {
                    return i6;
                }
                if (layoutDirection == 0) {
                    i = this.mLockModeStart;
                } else {
                    i = this.mLockModeEnd;
                }
                if (i != 3) {
                    return i;
                }
            } else if (i5 == 5) {
                int i7 = this.mLockModeRight;
                if (i7 != 3) {
                    return i7;
                }
                if (layoutDirection == 0) {
                    i2 = this.mLockModeEnd;
                } else {
                    i2 = this.mLockModeStart;
                }
                if (i2 != 3) {
                    return i2;
                }
            } else if (i5 == 8388611) {
                int i8 = this.mLockModeStart;
                if (i8 != 3) {
                    return i8;
                }
                if (layoutDirection == 0) {
                    i3 = this.mLockModeLeft;
                } else {
                    i3 = this.mLockModeRight;
                }
                if (i3 != 3) {
                    return i3;
                }
            } else if (i5 == 8388613) {
                int i9 = this.mLockModeEnd;
                if (i9 != 3) {
                    return i9;
                }
                if (layoutDirection == 0) {
                    i4 = this.mLockModeRight;
                } else {
                    i4 = this.mLockModeLeft;
                }
                if (i4 != 3) {
                    return i4;
                }
            }
            return 0;
        }
        throw new IllegalArgumentException("View " + view + " is not a drawer");
    }

    public final int getDrawerViewAbsoluteGravity(View view) {
        int i = ((LayoutParams) view.getLayoutParams()).gravity;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        return Gravity.getAbsoluteGravity(i, ViewCompat.Api17Impl.getLayoutDirection(this));
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        int i;
        super.onDraw(canvas);
        if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
            WindowInsetsCompat windowInsetsCompat = this.mLastInsets;
            if (windowInsetsCompat != null) {
                i = windowInsetsCompat.getSystemWindowInsetTop();
            } else {
                i = 0;
            }
            if (i > 0) {
                this.mStatusBarBackground.setBounds(0, 0, getWidth(), i);
                this.mStatusBarBackground.draw(canvas);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x001b, code lost:
        if (r0 != 3) goto L_0x0092;
     */
    /* JADX WARN: Removed duplicated region for block: B:33:0x006e A[LOOP:0: B:12:0x002b->B:33:0x006e, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x006c A[SYNTHETIC] */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onInterceptTouchEvent(android.view.MotionEvent r10) {
        /*
            Method dump skipped, instructions count: 227
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.drawerlayout.widget.DrawerLayout.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View
    public final Parcelable onSaveInstanceState() {
        boolean z;
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            LayoutParams layoutParams = (LayoutParams) getChildAt(i).getLayoutParams();
            int i2 = layoutParams.openState;
            boolean z2 = true;
            if (i2 == 1) {
                z = true;
            } else {
                z = false;
            }
            if (i2 != 2) {
                z2 = false;
            }
            if (z || z2) {
                savedState.openDrawerGravity = layoutParams.gravity;
                break;
            }
        }
        savedState.lockModeLeft = this.mLockModeLeft;
        savedState.lockModeRight = this.mLockModeRight;
        savedState.lockModeStart = this.mLockModeStart;
        savedState.lockModeEnd = this.mLockModeEnd;
        return savedState;
    }

    public final void openDrawer(View view) {
        if (isDrawerView(view)) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (this.mFirstLayout) {
                layoutParams.onScreen = 1.0f;
                layoutParams.openState = 1;
                updateChildrenImportantForAccessibility(view, true);
                updateChildAccessibilityAction(view);
            } else {
                layoutParams.openState |= 2;
                if (checkDrawerViewAbsoluteGravity(view, 3)) {
                    this.mLeftDragger.smoothSlideViewTo(view, 0, view.getTop());
                } else {
                    this.mRightDragger.smoothSlideViewTo(view, getWidth() - view.getWidth(), view.getTop());
                }
            }
            invalidate();
            return;
        }
        throw new IllegalArgumentException("View " + view + " is not a sliding drawer");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void requestDisallowInterceptTouchEvent(boolean z) {
        super.requestDisallowInterceptTouchEvent(z);
        if (z) {
            closeDrawers(true);
        }
    }

    public final void updateChildrenImportantForAccessibility(View view, boolean z) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if ((z || isDrawerView(childAt)) && (!z || childAt != view)) {
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                ViewCompat.Api16Impl.setImportantForAccessibility(childAt, 4);
            } else {
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                ViewCompat.Api16Impl.setImportantForAccessibility(childAt, 1);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public int gravity;
        public boolean isPeeking;
        public float onScreen;
        public int openState;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.gravity = 0;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, DrawerLayout.LAYOUT_ATTRS);
            this.gravity = obtainStyledAttributes.getInt(0, 0);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams() {
            super(-1, -1);
            this.gravity = 0;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams) layoutParams);
            this.gravity = 0;
            this.gravity = layoutParams.gravity;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.gravity = 0;
        }
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }
}
