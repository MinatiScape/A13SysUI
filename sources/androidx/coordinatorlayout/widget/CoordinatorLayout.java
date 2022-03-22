package androidx.coordinatorlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.collection.SimpleArrayMap;
import androidx.coordinatorlayout.R$styleable;
import androidx.core.util.Pools$SynchronizedPool;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class CoordinatorLayout extends ViewGroup implements NestedScrollingParent2, NestedScrollingParent3 {
    public static final Class<?>[] CONSTRUCTOR_PARAMS;
    public static final ViewElevationComparator TOP_SORTED_CHILDREN_COMPARATOR;
    public static final String WIDGET_PACKAGE_NAME;
    public static final ThreadLocal<Map<String, Constructor<Behavior>>> sConstructors;
    public static final Pools$SynchronizedPool sRectPool;
    public AnonymousClass1 mApplyWindowInsetsListener;
    public final int[] mBehaviorConsumed;
    public View mBehaviorTouchView;
    public final DirectedAcyclicGraph mChildDag;
    public final ArrayList mDependencySortedChildren;
    public boolean mDisallowInterceptReset;
    public boolean mDrawStatusBarBackground;
    public boolean mIsAttachedToWindow;
    public int[] mKeylines;
    public WindowInsetsCompat mLastInsets;
    public boolean mNeedsPreDrawListener;
    public final NestedScrollingParentHelper mNestedScrollingParentHelper;
    public View mNestedScrollingTarget;
    public final int[] mNestedScrollingV2ConsumedCompat;
    public ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
    public OnPreDrawListener mOnPreDrawListener;
    public Drawable mStatusBarBackground;
    public final ArrayList mTempList1;

    /* loaded from: classes.dex */
    public interface AttachedBehavior {
        Behavior getBehavior();
    }

    /* loaded from: classes.dex */
    public static abstract class Behavior<V extends View> {
        public Behavior() {
        }

        public boolean getInsetDodgeRect(View view, Rect rect) {
            return false;
        }

        public boolean layoutDependsOn(View view, View view2) {
            return false;
        }

        public void onAttachedToLayoutParams(LayoutParams layoutParams) {
        }

        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, V v, View view) {
            return false;
        }

        public void onDependentViewRemoved(CoordinatorLayout coordinatorLayout, View view) {
        }

        public void onDetachedFromLayoutParams() {
        }

        public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
            return false;
        }

        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v, int i) {
            return false;
        }

        public boolean onMeasureChild(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3) {
            return false;
        }

        public boolean onNestedPreFling(View view) {
            return false;
        }

        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int[] iArr, int i3) {
        }

        public void onNestedScroll(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3, int[] iArr) {
            iArr[0] = iArr[0] + i2;
            iArr[1] = iArr[1] + i3;
        }

        public boolean onRequestChildRectangleOnScreen(CoordinatorLayout coordinatorLayout, V v, Rect rect, boolean z) {
            return false;
        }

        public void onRestoreInstanceState(View view, Parcelable parcelable) {
        }

        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, View view2, int i, int i2) {
            return false;
        }

        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i) {
        }

        public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
            return false;
        }

        public Behavior(Context context, AttributeSet attributeSet) {
        }

        public Parcelable onSaveInstanceState(View view) {
            return View.BaseSavedState.EMPTY_STATE;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Deprecated
    /* loaded from: classes.dex */
    public @interface DefaultBehavior {
        Class<? extends Behavior> value();
    }

    /* loaded from: classes.dex */
    public class HierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
        public HierarchyChangeListener() {
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public final void onChildViewAdded(View view, View view2) {
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener = CoordinatorLayout.this.mOnHierarchyChangeListener;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewAdded(view, view2);
            }
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public final void onChildViewRemoved(View view, View view2) {
            CoordinatorLayout.this.onChildViewsChanged(2);
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener = CoordinatorLayout.this.mOnHierarchyChangeListener;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewRemoved(view, view2);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public int anchorGravity;
        public int dodgeInsetEdges;
        public int gravity;
        public int insetEdge;
        public int keyline;
        public View mAnchorDirectChild;
        public int mAnchorId;
        public View mAnchorView;
        public Behavior mBehavior;
        public boolean mBehaviorResolved;
        public boolean mDidAcceptNestedScrollNonTouch;
        public boolean mDidAcceptNestedScrollTouch;
        public boolean mDidBlockInteraction;
        public boolean mDidChangeAfterNestedScroll;
        public int mInsetOffsetX;
        public int mInsetOffsetY;
        public final Rect mLastChildRect;

        public LayoutParams() {
            super(-2, -2);
            this.mBehaviorResolved = false;
            this.gravity = 0;
            this.anchorGravity = 0;
            this.keyline = -1;
            this.mAnchorId = -1;
            this.insetEdge = 0;
            this.dodgeInsetEdges = 0;
            this.mLastChildRect = new Rect();
        }

        public final boolean isNestedScrollAccepted(int i) {
            if (i == 0) {
                return this.mDidAcceptNestedScrollTouch;
            }
            if (i != 1) {
                return false;
            }
            return this.mDidAcceptNestedScrollNonTouch;
        }

        public final void setBehavior(Behavior behavior) {
            Behavior behavior2 = this.mBehavior;
            if (behavior2 != behavior) {
                if (behavior2 != null) {
                    behavior2.onDetachedFromLayoutParams();
                }
                this.mBehavior = behavior;
                this.mBehaviorResolved = true;
                if (behavior != null) {
                    behavior.onAttachedToLayoutParams(this);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            Behavior behavior;
            this.mBehaviorResolved = false;
            this.gravity = 0;
            this.anchorGravity = 0;
            this.keyline = -1;
            this.mAnchorId = -1;
            this.insetEdge = 0;
            this.dodgeInsetEdges = 0;
            this.mLastChildRect = new Rect();
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CoordinatorLayout_Layout);
            this.gravity = obtainStyledAttributes.getInteger(0, 0);
            this.mAnchorId = obtainStyledAttributes.getResourceId(1, -1);
            this.anchorGravity = obtainStyledAttributes.getInteger(2, 0);
            this.keyline = obtainStyledAttributes.getInteger(6, -1);
            this.insetEdge = obtainStyledAttributes.getInt(5, 0);
            this.dodgeInsetEdges = obtainStyledAttributes.getInt(4, 0);
            boolean hasValue = obtainStyledAttributes.hasValue(3);
            this.mBehaviorResolved = hasValue;
            if (hasValue) {
                String string = obtainStyledAttributes.getString(3);
                String str = CoordinatorLayout.WIDGET_PACKAGE_NAME;
                if (TextUtils.isEmpty(string)) {
                    behavior = null;
                } else {
                    if (string.startsWith(".")) {
                        string = context.getPackageName() + string;
                    } else if (string.indexOf(46) < 0) {
                        String str2 = CoordinatorLayout.WIDGET_PACKAGE_NAME;
                        if (!TextUtils.isEmpty(str2)) {
                            string = str2 + '.' + string;
                        }
                    }
                    try {
                        ThreadLocal<Map<String, Constructor<Behavior>>> threadLocal = CoordinatorLayout.sConstructors;
                        Map<String, Constructor<Behavior>> map = threadLocal.get();
                        if (map == null) {
                            map = new HashMap<>();
                            threadLocal.set(map);
                        }
                        Constructor<Behavior> constructor = map.get(string);
                        if (constructor == null) {
                            constructor = Class.forName(string, false, context.getClassLoader()).getConstructor(CoordinatorLayout.CONSTRUCTOR_PARAMS);
                            constructor.setAccessible(true);
                            map.put(string, constructor);
                        }
                        behavior = constructor.newInstance(context, attributeSet);
                    } catch (Exception e) {
                        throw new RuntimeException(SupportMenuInflater$$ExternalSyntheticOutline0.m("Could not inflate Behavior subclass ", string), e);
                    }
                }
                this.mBehavior = behavior;
            }
            obtainStyledAttributes.recycle();
            Behavior behavior2 = this.mBehavior;
            if (behavior2 != null) {
                behavior2.onAttachedToLayoutParams(this);
            }
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams) layoutParams);
            this.mBehaviorResolved = false;
            this.gravity = 0;
            this.anchorGravity = 0;
            this.keyline = -1;
            this.mAnchorId = -1;
            this.insetEdge = 0;
            this.dodgeInsetEdges = 0;
            this.mLastChildRect = new Rect();
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.mBehaviorResolved = false;
            this.gravity = 0;
            this.anchorGravity = 0;
            this.keyline = -1;
            this.mAnchorId = -1;
            this.insetEdge = 0;
            this.dodgeInsetEdges = 0;
            this.mLastChildRect = new Rect();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.mBehaviorResolved = false;
            this.gravity = 0;
            this.anchorGravity = 0;
            this.keyline = -1;
            this.mAnchorId = -1;
            this.insetEdge = 0;
            this.dodgeInsetEdges = 0;
            this.mLastChildRect = new Rect();
        }
    }

    /* loaded from: classes.dex */
    public class OnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
        public OnPreDrawListener() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public final boolean onPreDraw() {
            CoordinatorLayout.this.onChildViewsChanged(0);
            return true;
        }
    }

    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: androidx.coordinatorlayout.widget.CoordinatorLayout.SavedState.1
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
        public SparseArray<Parcelable> behaviorStates;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            int readInt = parcel.readInt();
            int[] iArr = new int[readInt];
            parcel.readIntArray(iArr);
            Parcelable[] readParcelableArray = parcel.readParcelableArray(classLoader);
            this.behaviorStates = new SparseArray<>(readInt);
            for (int i = 0; i < readInt; i++) {
                this.behaviorStates.append(iArr[i], readParcelableArray[i]);
            }
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            int i2;
            parcel.writeParcelable(this.mSuperState, i);
            SparseArray<Parcelable> sparseArray = this.behaviorStates;
            if (sparseArray != null) {
                i2 = sparseArray.size();
            } else {
                i2 = 0;
            }
            parcel.writeInt(i2);
            int[] iArr = new int[i2];
            Parcelable[] parcelableArr = new Parcelable[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                iArr[i3] = this.behaviorStates.keyAt(i3);
                parcelableArr[i3] = this.behaviorStates.valueAt(i3);
            }
            parcel.writeIntArray(iArr);
            parcel.writeParcelableArray(parcelableArr, i);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    /* loaded from: classes.dex */
    public static class ViewElevationComparator implements Comparator<View> {
        @Override // java.util.Comparator
        public final int compare(View view, View view2) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            float z = ViewCompat.Api21Impl.getZ(view);
            float z2 = ViewCompat.Api21Impl.getZ(view2);
            if (z > z2) {
                return -1;
            }
            if (z < z2) {
                return 1;
            }
            return 0;
        }
    }

    public CoordinatorLayout(Context context) {
        this(context, null);
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public final void onMeasureChild(View view, int i, int i2, int i3) {
        measureChildWithMargins(view, i, i2, i3, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void onNestedPreScroll(View view, int i, int i2, int[] iArr) {
        onNestedPreScroll(view, i, i2, iArr, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void onNestedScroll(View view, int i, int i2, int i3, int i4) {
        onNestedScroll(view, i, i2, i3, i4, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void onNestedScrollAccepted(View view, View view2, int i) {
        onNestedScrollAccepted(view, view2, i, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final boolean onStartNestedScroll(View view, View view2, int i) {
        return onStartNestedScroll(view, view2, i, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void onStopNestedScroll(View view) {
        onStopNestedScroll(view, 0);
    }

    static {
        String str;
        Package r0 = CoordinatorLayout.class.getPackage();
        if (r0 != null) {
            str = r0.getName();
        } else {
            str = null;
        }
        WIDGET_PACKAGE_NAME = str;
        TOP_SORTED_CHILDREN_COMPARATOR = new ViewElevationComparator();
        CONSTRUCTOR_PARAMS = new Class[]{Context.class, AttributeSet.class};
        sConstructors = new ThreadLocal<>();
        sRectPool = new Pools$SynchronizedPool(12);
    }

    public CoordinatorLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130968883);
    }

    public static Rect acquireTempRect() {
        Rect rect = (Rect) sRectPool.acquire();
        if (rect == null) {
            return new Rect();
        }
        return rect;
    }

    public static void getDesiredAnchoredChildRectWithoutConstraints(int i, Rect rect, Rect rect2, LayoutParams layoutParams, int i2, int i3) {
        int i4;
        int i5;
        int i6 = layoutParams.gravity;
        if (i6 == 0) {
            i6 = 17;
        }
        int absoluteGravity = Gravity.getAbsoluteGravity(i6, i);
        int i7 = layoutParams.anchorGravity;
        if ((i7 & 7) == 0) {
            i7 |= 8388611;
        }
        if ((i7 & 112) == 0) {
            i7 |= 48;
        }
        int absoluteGravity2 = Gravity.getAbsoluteGravity(i7, i);
        int i8 = absoluteGravity & 7;
        int i9 = absoluteGravity & 112;
        int i10 = absoluteGravity2 & 7;
        int i11 = absoluteGravity2 & 112;
        if (i10 == 1) {
            i4 = rect.left + (rect.width() / 2);
        } else if (i10 != 5) {
            i4 = rect.left;
        } else {
            i4 = rect.right;
        }
        if (i11 == 16) {
            i5 = rect.top + (rect.height() / 2);
        } else if (i11 != 80) {
            i5 = rect.top;
        } else {
            i5 = rect.bottom;
        }
        if (i8 == 1) {
            i4 -= i2 / 2;
        } else if (i8 != 5) {
            i4 -= i2;
        }
        if (i9 == 16) {
            i5 -= i3 / 2;
        } else if (i9 != 80) {
            i5 -= i3;
        }
        rect2.set(i4, i5, i2 + i4, i3 + i5);
    }

    @Override // android.view.ViewGroup
    public final boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (!(layoutParams instanceof LayoutParams) || !super.checkLayoutParams(layoutParams)) {
            return false;
        }
        return true;
    }

    public final void dispatchDependentViewsChanged(View view) {
        DirectedAcyclicGraph directedAcyclicGraph = this.mChildDag;
        Objects.requireNonNull(directedAcyclicGraph);
        SimpleArrayMap simpleArrayMap = (SimpleArrayMap) directedAcyclicGraph.mGraph;
        Objects.requireNonNull(simpleArrayMap);
        ArrayList arrayList = (ArrayList) simpleArrayMap.getOrDefault(view, null);
        if (!(arrayList == null || arrayList.isEmpty())) {
            for (int i = 0; i < arrayList.size(); i++) {
                View view2 = (View) arrayList.get(i);
                LayoutParams layoutParams = (LayoutParams) view2.getLayoutParams();
                Objects.requireNonNull(layoutParams);
                Behavior behavior = layoutParams.mBehavior;
                if (behavior != null) {
                    behavior.onDependentViewChanged(this, view2, view);
                }
            }
        }
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
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

    public final List<View> getDependencies(View view) {
        DirectedAcyclicGraph directedAcyclicGraph = this.mChildDag;
        Objects.requireNonNull(directedAcyclicGraph);
        SimpleArrayMap simpleArrayMap = (SimpleArrayMap) directedAcyclicGraph.mGraph;
        Objects.requireNonNull(simpleArrayMap);
        int i = simpleArrayMap.mSize;
        ArrayList arrayList = null;
        for (int i2 = 0; i2 < i; i2++) {
            ArrayList arrayList2 = (ArrayList) ((SimpleArrayMap) directedAcyclicGraph.mGraph).valueAt(i2);
            if (arrayList2 != null && arrayList2.contains(view)) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(((SimpleArrayMap) directedAcyclicGraph.mGraph).keyAt(i2));
            }
        }
        if (arrayList == null) {
            return Collections.emptyList();
        }
        return arrayList;
    }

    public final void getDescendantRect(View view, Rect rect) {
        ThreadLocal<Matrix> threadLocal = ViewGroupUtils.sMatrix;
        rect.set(0, 0, view.getWidth(), view.getHeight());
        ThreadLocal<Matrix> threadLocal2 = ViewGroupUtils.sMatrix;
        Matrix matrix = threadLocal2.get();
        if (matrix == null) {
            matrix = new Matrix();
            threadLocal2.set(matrix);
        } else {
            matrix.reset();
        }
        ViewGroupUtils.offsetDescendantMatrix(this, view, matrix);
        ThreadLocal<RectF> threadLocal3 = ViewGroupUtils.sRectF;
        RectF rectF = threadLocal3.get();
        if (rectF == null) {
            rectF = new RectF();
            threadLocal3.set(rectF);
        }
        rectF.set(rect);
        matrix.mapRect(rectF);
        rect.set((int) (rectF.left + 0.5f), (int) (rectF.top + 0.5f), (int) (rectF.right + 0.5f), (int) (rectF.bottom + 0.5f));
    }

    public final int getKeyline(int i) {
        int[] iArr = this.mKeylines;
        if (iArr == null) {
            Log.e("CoordinatorLayout", "No keylines defined for " + this + " - attempted index lookup " + i);
            return 0;
        } else if (i >= 0 && i < iArr.length) {
            return iArr[i];
        } else {
            Log.e("CoordinatorLayout", "Keyline index " + i + " out of range for " + this);
            return 0;
        }
    }

    @Override // android.view.ViewGroup
    public final int getNestedScrollAxes() {
        NestedScrollingParentHelper nestedScrollingParentHelper = this.mNestedScrollingParentHelper;
        Objects.requireNonNull(nestedScrollingParentHelper);
        return nestedScrollingParentHelper.mNestedScrollAxesNonTouch | nestedScrollingParentHelper.mNestedScrollAxesTouch;
    }

    public final void onChildViewsChanged(int i) {
        int i2;
        Rect rect;
        int i3;
        boolean z;
        boolean z2;
        boolean z3;
        int width;
        int i4;
        int i5;
        int i6;
        int height;
        int i7;
        int i8;
        int i9;
        int i10;
        Rect rect2;
        int i11;
        int i12;
        Behavior behavior;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        int layoutDirection = ViewCompat.Api17Impl.getLayoutDirection(this);
        int size = this.mDependencySortedChildren.size();
        Rect acquireTempRect = acquireTempRect();
        Rect acquireTempRect2 = acquireTempRect();
        Rect acquireTempRect3 = acquireTempRect();
        int i13 = 0;
        while (i13 < size) {
            View view = (View) this.mDependencySortedChildren.get(i13);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (i == 0 && view.getVisibility() == 8) {
                i3 = size;
                rect = acquireTempRect3;
                i2 = i13;
            } else {
                int i14 = 0;
                while (i14 < i13) {
                    if (layoutParams.mAnchorDirectChild == ((View) this.mDependencySortedChildren.get(i14))) {
                        LayoutParams layoutParams2 = (LayoutParams) view.getLayoutParams();
                        if (layoutParams2.mAnchorView != null) {
                            Rect acquireTempRect4 = acquireTempRect();
                            Rect acquireTempRect5 = acquireTempRect();
                            Rect acquireTempRect6 = acquireTempRect();
                            getDescendantRect(layoutParams2.mAnchorView, acquireTempRect4);
                            getChildRect(view, false, acquireTempRect5);
                            int measuredWidth = view.getMeasuredWidth();
                            i12 = size;
                            int measuredHeight = view.getMeasuredHeight();
                            boolean z4 = true;
                            i11 = i13;
                            rect2 = acquireTempRect3;
                            i10 = i14;
                            getDesiredAnchoredChildRectWithoutConstraints(layoutDirection, acquireTempRect4, acquireTempRect6, layoutParams2, measuredWidth, measuredHeight);
                            if (acquireTempRect6.left == acquireTempRect5.left && acquireTempRect6.top == acquireTempRect5.top) {
                                z4 = false;
                            }
                            constrainChildRect(layoutParams2, acquireTempRect6, measuredWidth, measuredHeight);
                            int i15 = acquireTempRect6.left - acquireTempRect5.left;
                            int i16 = acquireTempRect6.top - acquireTempRect5.top;
                            if (i15 != 0) {
                                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                                view.offsetLeftAndRight(i15);
                            }
                            if (i16 != 0) {
                                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap3 = ViewCompat.sViewPropertyAnimatorMap;
                                view.offsetTopAndBottom(i16);
                            }
                            if (z4 && (behavior = layoutParams2.mBehavior) != null) {
                                behavior.onDependentViewChanged(this, view, layoutParams2.mAnchorView);
                            }
                            acquireTempRect4.setEmpty();
                            Pools$SynchronizedPool pools$SynchronizedPool = sRectPool;
                            pools$SynchronizedPool.release(acquireTempRect4);
                            acquireTempRect5.setEmpty();
                            pools$SynchronizedPool.release(acquireTempRect5);
                            acquireTempRect6.setEmpty();
                            pools$SynchronizedPool.release(acquireTempRect6);
                            i14 = i10 + 1;
                            size = i12;
                            i13 = i11;
                            acquireTempRect3 = rect2;
                        }
                    }
                    i10 = i14;
                    i12 = size;
                    rect2 = acquireTempRect3;
                    i11 = i13;
                    i14 = i10 + 1;
                    size = i12;
                    i13 = i11;
                    acquireTempRect3 = rect2;
                }
                i2 = i13;
                getChildRect(view, true, acquireTempRect2);
                if (layoutParams.insetEdge != 0 && !acquireTempRect2.isEmpty()) {
                    int absoluteGravity = Gravity.getAbsoluteGravity(layoutParams.insetEdge, layoutDirection);
                    int i17 = absoluteGravity & 112;
                    if (i17 == 48) {
                        acquireTempRect.top = Math.max(acquireTempRect.top, acquireTempRect2.bottom);
                    } else if (i17 == 80) {
                        acquireTempRect.bottom = Math.max(acquireTempRect.bottom, getHeight() - acquireTempRect2.top);
                    }
                    int i18 = absoluteGravity & 7;
                    if (i18 == 3) {
                        acquireTempRect.left = Math.max(acquireTempRect.left, acquireTempRect2.right);
                    } else if (i18 == 5) {
                        acquireTempRect.right = Math.max(acquireTempRect.right, getWidth() - acquireTempRect2.left);
                    }
                }
                if (layoutParams.dodgeInsetEdges != 0 && view.getVisibility() == 0) {
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap4 = ViewCompat.sViewPropertyAnimatorMap;
                    if (ViewCompat.Api19Impl.isLaidOut(view) && view.getWidth() > 0 && view.getHeight() > 0) {
                        LayoutParams layoutParams3 = (LayoutParams) view.getLayoutParams();
                        Objects.requireNonNull(layoutParams3);
                        Behavior behavior2 = layoutParams3.mBehavior;
                        Rect acquireTempRect7 = acquireTempRect();
                        Rect acquireTempRect8 = acquireTempRect();
                        acquireTempRect8.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                        if (behavior2 == null || !behavior2.getInsetDodgeRect(view, acquireTempRect7)) {
                            acquireTempRect7.set(acquireTempRect8);
                        } else if (!acquireTempRect8.contains(acquireTempRect7)) {
                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Rect should be within the child's bounds. Rect:");
                            m.append(acquireTempRect7.toShortString());
                            m.append(" | Bounds:");
                            m.append(acquireTempRect8.toShortString());
                            throw new IllegalArgumentException(m.toString());
                        }
                        acquireTempRect8.setEmpty();
                        Pools$SynchronizedPool pools$SynchronizedPool2 = sRectPool;
                        pools$SynchronizedPool2.release(acquireTempRect8);
                        if (acquireTempRect7.isEmpty()) {
                            acquireTempRect7.setEmpty();
                            pools$SynchronizedPool2.release(acquireTempRect7);
                        } else {
                            int absoluteGravity2 = Gravity.getAbsoluteGravity(layoutParams3.dodgeInsetEdges, layoutDirection);
                            if ((absoluteGravity2 & 48) != 48 || (i8 = (acquireTempRect7.top - ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin) - layoutParams3.mInsetOffsetY) >= (i9 = acquireTempRect.top)) {
                                z2 = false;
                            } else {
                                setInsetOffsetY(view, i9 - i8);
                                z2 = true;
                            }
                            if ((absoluteGravity2 & 80) == 80 && (height = ((getHeight() - acquireTempRect7.bottom) - ((ViewGroup.MarginLayoutParams) layoutParams3).bottomMargin) + layoutParams3.mInsetOffsetY) < (i7 = acquireTempRect.bottom)) {
                                setInsetOffsetY(view, height - i7);
                                z2 = true;
                            }
                            if (!z2) {
                                setInsetOffsetY(view, 0);
                            }
                            if ((absoluteGravity2 & 3) != 3 || (i5 = (acquireTempRect7.left - ((ViewGroup.MarginLayoutParams) layoutParams3).leftMargin) - layoutParams3.mInsetOffsetX) >= (i6 = acquireTempRect.left)) {
                                z3 = false;
                            } else {
                                setInsetOffsetX(view, i6 - i5);
                                z3 = true;
                            }
                            if ((absoluteGravity2 & 5) == 5 && (width = ((getWidth() - acquireTempRect7.right) - ((ViewGroup.MarginLayoutParams) layoutParams3).rightMargin) + layoutParams3.mInsetOffsetX) < (i4 = acquireTempRect.right)) {
                                setInsetOffsetX(view, width - i4);
                                z3 = true;
                            }
                            if (!z3) {
                                setInsetOffsetX(view, 0);
                            }
                            acquireTempRect7.setEmpty();
                            pools$SynchronizedPool2.release(acquireTempRect7);
                        }
                    }
                }
                if (i != 2) {
                    LayoutParams layoutParams4 = (LayoutParams) view.getLayoutParams();
                    Objects.requireNonNull(layoutParams4);
                    rect = acquireTempRect3;
                    rect.set(layoutParams4.mLastChildRect);
                    if (rect.equals(acquireTempRect2)) {
                        i3 = size;
                    } else {
                        LayoutParams layoutParams5 = (LayoutParams) view.getLayoutParams();
                        Objects.requireNonNull(layoutParams5);
                        layoutParams5.mLastChildRect.set(acquireTempRect2);
                    }
                } else {
                    rect = acquireTempRect3;
                }
                i3 = size;
                for (int i19 = i2 + 1; i19 < i3; i19++) {
                    View view2 = (View) this.mDependencySortedChildren.get(i19);
                    LayoutParams layoutParams6 = (LayoutParams) view2.getLayoutParams();
                    Objects.requireNonNull(layoutParams6);
                    Behavior behavior3 = layoutParams6.mBehavior;
                    if (behavior3 != null && behavior3.layoutDependsOn(view2, view)) {
                        if (i != 0 || !layoutParams6.mDidChangeAfterNestedScroll) {
                            if (i != 2) {
                                z = behavior3.onDependentViewChanged(this, view2, view);
                            } else {
                                behavior3.onDependentViewRemoved(this, view);
                                z = true;
                            }
                            if (i == 1) {
                                layoutParams6.mDidChangeAfterNestedScroll = z;
                            }
                        } else {
                            layoutParams6.mDidChangeAfterNestedScroll = false;
                        }
                    }
                }
            }
            i13 = i2 + 1;
            size = i3;
            acquireTempRect3 = rect;
        }
        acquireTempRect.setEmpty();
        Pools$SynchronizedPool pools$SynchronizedPool3 = sRectPool;
        pools$SynchronizedPool3.release(acquireTempRect);
        acquireTempRect2.setEmpty();
        pools$SynchronizedPool3.release(acquireTempRect2);
        acquireTempRect3.setEmpty();
        pools$SynchronizedPool3.release(acquireTempRect3);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        int layoutDirection = ViewCompat.Api17Impl.getLayoutDirection(this);
        int size = this.mDependencySortedChildren.size();
        for (int i5 = 0; i5 < size; i5++) {
            View view = (View) this.mDependencySortedChildren.get(i5);
            if (view.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                Objects.requireNonNull(layoutParams);
                Behavior behavior = layoutParams.mBehavior;
                if (behavior == null || !behavior.onLayoutChild(this, view, layoutDirection)) {
                    onLayoutChild(view, layoutDirection);
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:79:0x0196, code lost:
        if (r0.onMeasureChild(r30, r19, r24, r20, r25) == false) goto L_0x01ab;
     */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0177  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0199  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onMeasure(int r31, int r32) {
        /*
            Method dump skipped, instructions count: 522
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.coordinatorlayout.widget.CoordinatorLayout.onMeasure(int, int):void");
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3) {
        Behavior behavior;
        int childCount = getChildCount();
        boolean z = false;
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isNestedScrollAccepted(i3) && (behavior = layoutParams.mBehavior) != null) {
                    int[] iArr2 = this.mBehaviorConsumed;
                    iArr2[0] = 0;
                    iArr2[1] = 0;
                    behavior.onNestedPreScroll(this, childAt, view, i, i2, iArr2, i3);
                    if (i > 0) {
                        i4 = Math.max(i4, this.mBehaviorConsumed[0]);
                    } else {
                        i4 = Math.min(i4, this.mBehaviorConsumed[0]);
                    }
                    if (i2 > 0) {
                        i5 = Math.max(i5, this.mBehaviorConsumed[1]);
                    } else {
                        i5 = Math.min(i5, this.mBehaviorConsumed[1]);
                    }
                    z = true;
                }
            }
        }
        iArr[0] = i4;
        iArr[1] = i5;
        if (z) {
            onChildViewsChanged(1);
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5) {
        onNestedScroll(view, i, i2, i3, i4, 0, this.mNestedScrollingV2ConsumedCompat);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final void onNestedScrollAccepted(View view, View view2, int i, int i2) {
        NestedScrollingParentHelper nestedScrollingParentHelper = this.mNestedScrollingParentHelper;
        Objects.requireNonNull(nestedScrollingParentHelper);
        if (i2 == 1) {
            nestedScrollingParentHelper.mNestedScrollAxesNonTouch = i;
        } else {
            nestedScrollingParentHelper.mNestedScrollAxesTouch = i;
        }
        this.mNestedScrollingTarget = view2;
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            Objects.requireNonNull((LayoutParams) getChildAt(i3).getLayoutParams());
        }
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        Parcelable parcelable2;
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        Objects.requireNonNull(savedState);
        super.onRestoreInstanceState(savedState.mSuperState);
        SparseArray<Parcelable> sparseArray = savedState.behaviorStates;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int id = childAt.getId();
            Behavior behavior = getResolvedLayoutParams(childAt).mBehavior;
            if (!(id == -1 || behavior == null || (parcelable2 = sparseArray.get(id)) == null)) {
                behavior.onRestoreInstanceState(childAt, parcelable2);
            }
        }
    }

    @Override // android.view.View
    public final Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState;
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int id = childAt.getId();
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            Objects.requireNonNull(layoutParams);
            Behavior behavior = layoutParams.mBehavior;
            if (!(id == -1 || behavior == null || (onSaveInstanceState = behavior.onSaveInstanceState(childAt)) == null)) {
                sparseArray.append(id, onSaveInstanceState);
            }
        }
        savedState.behaviorStates = sparseArray;
        return savedState;
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        int childCount = getChildCount();
        boolean z = false;
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                Objects.requireNonNull(layoutParams);
                Behavior behavior = layoutParams.mBehavior;
                if (behavior != null) {
                    boolean onStartNestedScroll = behavior.onStartNestedScroll(this, childAt, view, view2, i, i2);
                    z |= onStartNestedScroll;
                    if (i2 == 0) {
                        layoutParams.mDidAcceptNestedScrollTouch = onStartNestedScroll;
                    } else if (i2 == 1) {
                        layoutParams.mDidAcceptNestedScrollNonTouch = onStartNestedScroll;
                    }
                } else if (i2 == 0) {
                    layoutParams.mDidAcceptNestedScrollTouch = false;
                } else if (i2 == 1) {
                    layoutParams.mDidAcceptNestedScrollNonTouch = false;
                }
            }
        }
        return z;
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final void onStopNestedScroll(View view, int i) {
        NestedScrollingParentHelper nestedScrollingParentHelper = this.mNestedScrollingParentHelper;
        Objects.requireNonNull(nestedScrollingParentHelper);
        if (i == 1) {
            nestedScrollingParentHelper.mNestedScrollAxesNonTouch = 0;
        } else {
            nestedScrollingParentHelper.mNestedScrollAxesTouch = 0;
        }
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (layoutParams.isNestedScrollAccepted(i)) {
                Behavior behavior = layoutParams.mBehavior;
                if (behavior != null) {
                    behavior.onStopNestedScroll(this, childAt, view, i);
                }
                if (i == 0) {
                    layoutParams.mDidAcceptNestedScrollTouch = false;
                } else if (i == 1) {
                    layoutParams.mDidAcceptNestedScrollNonTouch = false;
                }
                layoutParams.mDidChangeAfterNestedScroll = false;
            }
        }
        this.mNestedScrollingTarget = null;
    }

    public final boolean performEvent(Behavior behavior, View view, MotionEvent motionEvent, int i) {
        if (i == 0) {
            return behavior.onInterceptTouchEvent(this, view, motionEvent);
        }
        if (i == 1) {
            return behavior.onTouchEvent(this, view, motionEvent);
        }
        throw new IllegalArgumentException();
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x0082, code lost:
        if (r5 != false) goto L_0x00d9;
     */
    /* JADX WARN: Removed duplicated region for block: B:123:0x01a1 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0130  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void prepareChildren() {
        /*
            Method dump skipped, instructions count: 529
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.coordinatorlayout.widget.CoordinatorLayout.prepareChildren():void");
    }

    public final void resetTouchBehaviors() {
        View view = this.mBehaviorTouchView;
        if (view != null) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            Objects.requireNonNull(layoutParams);
            Behavior behavior = layoutParams.mBehavior;
            if (behavior != null) {
                long uptimeMillis = SystemClock.uptimeMillis();
                MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                behavior.onTouchEvent(this, this.mBehaviorTouchView, obtain);
                obtain.recycle();
            }
            this.mBehaviorTouchView = null;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            LayoutParams layoutParams2 = (LayoutParams) getChildAt(i).getLayoutParams();
            Objects.requireNonNull(layoutParams2);
            layoutParams2.mDidBlockInteraction = false;
        }
        this.mDisallowInterceptReset = false;
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [androidx.coordinatorlayout.widget.CoordinatorLayout$1] */
    public final void setupForInsets() {
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        if (ViewCompat.Api16Impl.getFitsSystemWindows(this)) {
            if (this.mApplyWindowInsetsListener == null) {
                this.mApplyWindowInsetsListener = new OnApplyWindowInsetsListener() { // from class: androidx.coordinatorlayout.widget.CoordinatorLayout.1
                    @Override // androidx.core.view.OnApplyWindowInsetsListener
                    public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                        boolean z;
                        CoordinatorLayout coordinatorLayout = CoordinatorLayout.this;
                        Objects.requireNonNull(coordinatorLayout);
                        if (!Objects.equals(coordinatorLayout.mLastInsets, windowInsetsCompat)) {
                            coordinatorLayout.mLastInsets = windowInsetsCompat;
                            boolean z2 = true;
                            if (windowInsetsCompat.getSystemWindowInsetTop() > 0) {
                                z = true;
                            } else {
                                z = false;
                            }
                            coordinatorLayout.mDrawStatusBarBackground = z;
                            if (z || coordinatorLayout.getBackground() != null) {
                                z2 = false;
                            }
                            coordinatorLayout.setWillNotDraw(z2);
                            if (!windowInsetsCompat.mImpl.isConsumed()) {
                                int childCount = coordinatorLayout.getChildCount();
                                for (int i = 0; i < childCount; i++) {
                                    View childAt = coordinatorLayout.getChildAt(i);
                                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                                    if (ViewCompat.Api16Impl.getFitsSystemWindows(childAt)) {
                                        LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                                        Objects.requireNonNull(layoutParams);
                                        if (layoutParams.mBehavior != null && windowInsetsCompat.mImpl.isConsumed()) {
                                            break;
                                        }
                                    }
                                }
                            }
                            coordinatorLayout.requestLayout();
                        }
                        return windowInsetsCompat;
                    }
                };
            }
            ViewCompat.Api21Impl.setOnApplyWindowInsetsListener(this, this.mApplyWindowInsetsListener);
            setSystemUiVisibility(1280);
            return;
        }
        ViewCompat.Api21Impl.setOnApplyWindowInsetsListener(this, null);
    }

    public CoordinatorLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray typedArray;
        this.mDependencySortedChildren = new ArrayList();
        this.mChildDag = new DirectedAcyclicGraph();
        this.mTempList1 = new ArrayList();
        this.mBehaviorConsumed = new int[2];
        this.mNestedScrollingV2ConsumedCompat = new int[2];
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper();
        if (i == 0) {
            typedArray = context.obtainStyledAttributes(attributeSet, R$styleable.CoordinatorLayout, 0, 2132018741);
        } else {
            typedArray = context.obtainStyledAttributes(attributeSet, R$styleable.CoordinatorLayout, i, 0);
        }
        if (i == 0) {
            int[] iArr = R$styleable.CoordinatorLayout;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr, attributeSet, typedArray, 0, 2132018741);
        } else {
            int[] iArr2 = R$styleable.CoordinatorLayout;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr2, attributeSet, typedArray, i, 0);
        }
        int resourceId = typedArray.getResourceId(0, 0);
        if (resourceId != 0) {
            Resources resources = context.getResources();
            this.mKeylines = resources.getIntArray(resourceId);
            float f = resources.getDisplayMetrics().density;
            int length = this.mKeylines.length;
            for (int i2 = 0; i2 < length; i2++) {
                int[] iArr3 = this.mKeylines;
                iArr3[i2] = (int) (iArr3[i2] * f);
            }
        }
        this.mStatusBarBackground = typedArray.getDrawable(1);
        typedArray.recycle();
        setupForInsets();
        super.setOnHierarchyChangeListener(new HierarchyChangeListener());
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap3 = ViewCompat.sViewPropertyAnimatorMap;
        if (ViewCompat.Api16Impl.getImportantForAccessibility(this) == 0) {
            ViewCompat.Api16Impl.setImportantForAccessibility(this, 1);
        }
    }

    public static LayoutParams getResolvedLayoutParams(View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (!layoutParams.mBehaviorResolved) {
            if (view instanceof AttachedBehavior) {
                Behavior behavior = ((AttachedBehavior) view).getBehavior();
                if (behavior == null) {
                    Log.e("CoordinatorLayout", "Attached behavior class is null");
                }
                layoutParams.setBehavior(behavior);
                layoutParams.mBehaviorResolved = true;
            } else {
                DefaultBehavior defaultBehavior = null;
                for (Class<?> cls = view.getClass(); cls != null; cls = cls.getSuperclass()) {
                    defaultBehavior = (DefaultBehavior) cls.getAnnotation(DefaultBehavior.class);
                    if (defaultBehavior != null) {
                        break;
                    }
                }
                if (defaultBehavior != null) {
                    try {
                        layoutParams.setBehavior((Behavior) defaultBehavior.value().getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
                    } catch (Exception e) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Default behavior class ");
                        m.append(defaultBehavior.value().getName());
                        m.append(" could not be instantiated. Did you forget a default constructor?");
                        Log.e("CoordinatorLayout", m.toString(), e);
                    }
                }
                layoutParams.mBehaviorResolved = true;
            }
        }
        return layoutParams;
    }

    public static void setInsetOffsetX(View view, int i) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i2 = layoutParams.mInsetOffsetX;
        if (i2 != i) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            view.offsetLeftAndRight(i - i2);
            layoutParams.mInsetOffsetX = i;
        }
    }

    public static void setInsetOffsetY(View view, int i) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i2 = layoutParams.mInsetOffsetY;
        if (i2 != i) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            view.offsetTopAndBottom(i - i2);
            layoutParams.mInsetOffsetY = i;
        }
    }

    public final void constrainChildRect(LayoutParams layoutParams, Rect rect, int i, int i2) {
        int width = getWidth();
        int height = getHeight();
        int max = Math.max(getPaddingLeft() + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin, Math.min(rect.left, ((width - getPaddingRight()) - i) - ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin));
        int max2 = Math.max(getPaddingTop() + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin, Math.min(rect.top, ((height - getPaddingBottom()) - i2) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin));
        rect.set(max, max2, i + max, i2 + max2);
    }

    @Override // android.view.ViewGroup
    public final boolean drawChild(Canvas canvas, View view, long j) {
        Behavior behavior = ((LayoutParams) view.getLayoutParams()).mBehavior;
        if (behavior != null) {
            Objects.requireNonNull(behavior);
        }
        return super.drawChild(canvas, view, j);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.mStatusBarBackground;
        boolean z = false;
        if (drawable != null && drawable.isStateful()) {
            z = false | drawable.setState(drawableState);
        }
        if (z) {
            invalidate();
        }
    }

    public final void getChildRect(View view, boolean z, Rect rect) {
        if (view.isLayoutRequested() || view.getVisibility() == 8) {
            rect.setEmpty();
        } else if (z) {
            getDescendantRect(view, rect);
        } else {
            rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        }
    }

    public final List<View> getDependencySortedChildren() {
        prepareChildren();
        return Collections.unmodifiableList(this.mDependencySortedChildren);
    }

    @Override // android.view.View
    public final int getSuggestedMinimumHeight() {
        return Math.max(super.getSuggestedMinimumHeight(), getPaddingBottom() + getPaddingTop());
    }

    @Override // android.view.View
    public final int getSuggestedMinimumWidth() {
        return Math.max(super.getSuggestedMinimumWidth(), getPaddingRight() + getPaddingLeft());
    }

    public final boolean isPointInChildBounds(View view, int i, int i2) {
        Rect acquireTempRect = acquireTempRect();
        getDescendantRect(view, acquireTempRect);
        try {
            return acquireTempRect.contains(i, i2);
        } finally {
            acquireTempRect.setEmpty();
            sRectPool.release(acquireTempRect);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        resetTouchBehaviors();
        if (this.mNeedsPreDrawListener) {
            if (this.mOnPreDrawListener == null) {
                this.mOnPreDrawListener = new OnPreDrawListener();
            }
            getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
        }
        if (this.mLastInsets == null) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            if (ViewCompat.Api16Impl.getFitsSystemWindows(this)) {
                ViewCompat.Api20Impl.requestApplyInsets(this);
            }
        }
        this.mIsAttachedToWindow = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        resetTouchBehaviors();
        if (this.mNeedsPreDrawListener && this.mOnPreDrawListener != null) {
            getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
        }
        View view = this.mNestedScrollingTarget;
        if (view != null) {
            onStopNestedScroll(view);
        }
        this.mIsAttachedToWindow = false;
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

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            resetTouchBehaviors();
        }
        boolean performIntercept = performIntercept(motionEvent, 0);
        if (actionMasked == 1 || actionMasked == 3) {
            this.mBehaviorTouchView = null;
            resetTouchBehaviors();
        }
        return performIntercept;
    }

    public final void onLayoutChild(View view, int i) {
        boolean z;
        Rect acquireTempRect;
        Rect acquireTempRect2;
        Pools$SynchronizedPool pools$SynchronizedPool;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        Objects.requireNonNull(layoutParams);
        View view2 = layoutParams.mAnchorView;
        int i2 = 0;
        if (view2 != null || layoutParams.mAnchorId == -1) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            throw new IllegalStateException("An anchor may not be changed after CoordinatorLayout measurement begins before layout is complete.");
        } else if (view2 != null) {
            acquireTempRect = acquireTempRect();
            acquireTempRect2 = acquireTempRect();
            try {
                getDescendantRect(view2, acquireTempRect);
                LayoutParams layoutParams2 = (LayoutParams) view.getLayoutParams();
                int measuredWidth = view.getMeasuredWidth();
                int measuredHeight = view.getMeasuredHeight();
                getDesiredAnchoredChildRectWithoutConstraints(i, acquireTempRect, acquireTempRect2, layoutParams2, measuredWidth, measuredHeight);
                constrainChildRect(layoutParams2, acquireTempRect2, measuredWidth, measuredHeight);
                view.layout(acquireTempRect2.left, acquireTempRect2.top, acquireTempRect2.right, acquireTempRect2.bottom);
            } finally {
                acquireTempRect.setEmpty();
                pools$SynchronizedPool = sRectPool;
                pools$SynchronizedPool.release(acquireTempRect);
                acquireTempRect2.setEmpty();
                pools$SynchronizedPool.release(acquireTempRect2);
            }
        } else {
            int i3 = layoutParams.keyline;
            if (i3 >= 0) {
                LayoutParams layoutParams3 = (LayoutParams) view.getLayoutParams();
                int i4 = layoutParams3.gravity;
                if (i4 == 0) {
                    i4 = 8388661;
                }
                int absoluteGravity = Gravity.getAbsoluteGravity(i4, i);
                int i5 = absoluteGravity & 7;
                int i6 = absoluteGravity & 112;
                int width = getWidth();
                int height = getHeight();
                int measuredWidth2 = view.getMeasuredWidth();
                int measuredHeight2 = view.getMeasuredHeight();
                if (i == 1) {
                    i3 = width - i3;
                }
                int keyline = getKeyline(i3) - measuredWidth2;
                if (i5 == 1) {
                    keyline += measuredWidth2 / 2;
                } else if (i5 == 5) {
                    keyline += measuredWidth2;
                }
                if (i6 == 16) {
                    i2 = 0 + (measuredHeight2 / 2);
                } else if (i6 == 80) {
                    i2 = measuredHeight2 + 0;
                }
                int max = Math.max(getPaddingLeft() + ((ViewGroup.MarginLayoutParams) layoutParams3).leftMargin, Math.min(keyline, ((width - getPaddingRight()) - measuredWidth2) - ((ViewGroup.MarginLayoutParams) layoutParams3).rightMargin));
                int max2 = Math.max(getPaddingTop() + ((ViewGroup.MarginLayoutParams) layoutParams3).topMargin, Math.min(i2, ((height - getPaddingBottom()) - measuredHeight2) - ((ViewGroup.MarginLayoutParams) layoutParams3).bottomMargin));
                view.layout(max, max2, measuredWidth2 + max, measuredHeight2 + max2);
                return;
            }
            LayoutParams layoutParams4 = (LayoutParams) view.getLayoutParams();
            acquireTempRect = acquireTempRect();
            acquireTempRect.set(getPaddingLeft() + ((ViewGroup.MarginLayoutParams) layoutParams4).leftMargin, getPaddingTop() + ((ViewGroup.MarginLayoutParams) layoutParams4).topMargin, (getWidth() - getPaddingRight()) - ((ViewGroup.MarginLayoutParams) layoutParams4).rightMargin, (getHeight() - getPaddingBottom()) - ((ViewGroup.MarginLayoutParams) layoutParams4).bottomMargin);
            if (this.mLastInsets != null) {
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                if (ViewCompat.Api16Impl.getFitsSystemWindows(this) && !ViewCompat.Api16Impl.getFitsSystemWindows(view)) {
                    acquireTempRect.left = this.mLastInsets.getSystemWindowInsetLeft() + acquireTempRect.left;
                    acquireTempRect.top = this.mLastInsets.getSystemWindowInsetTop() + acquireTempRect.top;
                    acquireTempRect.right -= this.mLastInsets.getSystemWindowInsetRight();
                    acquireTempRect.bottom -= this.mLastInsets.getSystemWindowInsetBottom();
                }
            }
            acquireTempRect2 = acquireTempRect();
            int i7 = layoutParams4.gravity;
            if ((i7 & 7) == 0) {
                i7 |= 8388611;
            }
            if ((i7 & 112) == 0) {
                i7 |= 48;
            }
            Gravity.apply(i7, view.getMeasuredWidth(), view.getMeasuredHeight(), acquireTempRect, acquireTempRect2, i);
            view.layout(acquireTempRect2.left, acquireTempRect2.top, acquireTempRect2.right, acquireTempRect2.bottom);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final boolean onNestedFling(View view, float f, float f2, boolean z) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isNestedScrollAccepted(0)) {
                    Behavior behavior = layoutParams.mBehavior;
                }
            }
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final boolean onNestedPreFling(View view, float f, float f2) {
        Behavior behavior;
        int childCount = getChildCount();
        boolean z = false;
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isNestedScrollAccepted(0) && (behavior = layoutParams.mBehavior) != null) {
                    z |= behavior.onNestedPreFling(view);
                }
            }
        }
        return z;
    }

    @Override // androidx.core.view.NestedScrollingParent3
    public final void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr) {
        Behavior behavior;
        int childCount = getChildCount();
        boolean z = false;
        int i6 = 0;
        int i7 = 0;
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isNestedScrollAccepted(i5) && (behavior = layoutParams.mBehavior) != null) {
                    int[] iArr2 = this.mBehaviorConsumed;
                    iArr2[0] = 0;
                    iArr2[1] = 0;
                    behavior.onNestedScroll(this, childAt, i2, i3, i4, iArr2);
                    if (i3 > 0) {
                        i6 = Math.max(i6, this.mBehaviorConsumed[0]);
                    } else {
                        i6 = Math.min(i6, this.mBehaviorConsumed[0]);
                    }
                    if (i4 > 0) {
                        i7 = Math.max(i7, this.mBehaviorConsumed[1]);
                    } else {
                        i7 = Math.min(i7, this.mBehaviorConsumed[1]);
                    }
                    z = true;
                }
            }
        }
        iArr[0] = iArr[0] + i6;
        iArr[1] = iArr[1] + i7;
        if (z) {
            onChildViewsChanged(1);
        }
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        int actionMasked = motionEvent.getActionMasked();
        View view = this.mBehaviorTouchView;
        boolean z2 = false;
        if (view != null) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            Objects.requireNonNull(layoutParams);
            Behavior behavior = layoutParams.mBehavior;
            z = behavior != null ? behavior.onTouchEvent(this, this.mBehaviorTouchView, motionEvent) : false;
        } else {
            z = performIntercept(motionEvent, 1);
            if (actionMasked != 0 && z) {
                z2 = true;
            }
        }
        if (this.mBehaviorTouchView == null || actionMasked == 3) {
            z |= super.onTouchEvent(motionEvent);
        } else if (z2) {
            MotionEvent obtain = MotionEvent.obtain(motionEvent);
            obtain.setAction(3);
            super.onTouchEvent(obtain);
            obtain.recycle();
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.mBehaviorTouchView = null;
            resetTouchBehaviors();
        }
        return z;
    }

    public final boolean performIntercept(MotionEvent motionEvent, int i) {
        boolean z;
        int i2;
        int actionMasked = motionEvent.getActionMasked();
        ArrayList arrayList = this.mTempList1;
        arrayList.clear();
        boolean isChildrenDrawingOrderEnabled = isChildrenDrawingOrderEnabled();
        int childCount = getChildCount();
        for (int i3 = childCount - 1; i3 >= 0; i3--) {
            if (isChildrenDrawingOrderEnabled) {
                i2 = getChildDrawingOrder(childCount, i3);
            } else {
                i2 = i3;
            }
            arrayList.add(getChildAt(i2));
        }
        ViewElevationComparator viewElevationComparator = TOP_SORTED_CHILDREN_COMPARATOR;
        if (viewElevationComparator != null) {
            Collections.sort(arrayList, viewElevationComparator);
        }
        int size = arrayList.size();
        MotionEvent motionEvent2 = null;
        boolean z2 = false;
        boolean z3 = false;
        for (int i4 = 0; i4 < size; i4++) {
            View view = (View) arrayList.get(i4);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            Objects.requireNonNull(layoutParams);
            Behavior behavior = layoutParams.mBehavior;
            if (!(z2 || z3) || actionMasked == 0) {
                if (!z3 && !z2 && behavior != null && (z2 = performEvent(behavior, view, motionEvent, i))) {
                    this.mBehaviorTouchView = view;
                    if (!(actionMasked == 3 || actionMasked == 1)) {
                        for (int i5 = 0; i5 < i4; i5++) {
                            View view2 = (View) arrayList.get(i5);
                            LayoutParams layoutParams2 = (LayoutParams) view2.getLayoutParams();
                            Objects.requireNonNull(layoutParams2);
                            Behavior behavior2 = layoutParams2.mBehavior;
                            if (behavior2 != null) {
                                if (motionEvent2 == null) {
                                    motionEvent2 = MotionEvent.obtain(motionEvent);
                                    motionEvent2.setAction(3);
                                }
                                performEvent(behavior2, view2, motionEvent2, i);
                            }
                        }
                    }
                }
                if (layoutParams.mBehavior == null) {
                    layoutParams.mDidBlockInteraction = false;
                }
                boolean z4 = layoutParams.mDidBlockInteraction;
                if (z4) {
                    z = true;
                } else {
                    z = z4 | false;
                    layoutParams.mDidBlockInteraction = z;
                }
                if (!z || z4) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                if (z && !z3) {
                    break;
                }
            } else if (behavior != null) {
                if (motionEvent2 == null) {
                    motionEvent2 = MotionEvent.obtain(motionEvent);
                    motionEvent2.setAction(3);
                }
                performEvent(behavior, view, motionEvent2, i);
            }
        }
        arrayList.clear();
        if (motionEvent2 != null) {
            motionEvent2.recycle();
        }
        return z2;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        Objects.requireNonNull(layoutParams);
        Behavior behavior = layoutParams.mBehavior;
        if (behavior == null || !behavior.onRequestChildRectangleOnScreen(this, view, rect, z)) {
            return super.requestChildRectangleOnScreen(view, rect, z);
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void requestDisallowInterceptTouchEvent(boolean z) {
        super.requestDisallowInterceptTouchEvent(z);
        if (z && !this.mDisallowInterceptReset) {
            if (this.mBehaviorTouchView == null) {
                int childCount = getChildCount();
                MotionEvent motionEvent = null;
                for (int i = 0; i < childCount; i++) {
                    View childAt = getChildAt(i);
                    LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                    Objects.requireNonNull(layoutParams);
                    Behavior behavior = layoutParams.mBehavior;
                    if (behavior != null) {
                        if (motionEvent == null) {
                            long uptimeMillis = SystemClock.uptimeMillis();
                            motionEvent = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                        }
                        behavior.onInterceptTouchEvent(this, childAt, motionEvent);
                    }
                }
                if (motionEvent != null) {
                    motionEvent.recycle();
                }
            }
            resetTouchBehaviors();
            this.mDisallowInterceptReset = true;
        }
    }

    @Override // android.view.View
    public final void setFitsSystemWindows(boolean z) {
        super.setFitsSystemWindows(z);
        setupForInsets();
    }

    @Override // android.view.View
    public final void setVisibility(int i) {
        boolean z;
        super.setVisibility(i);
        if (i == 0) {
            z = true;
        } else {
            z = false;
        }
        Drawable drawable = this.mStatusBarBackground;
        if (drawable != null && drawable.isVisible() != z) {
            this.mStatusBarBackground.setVisible(z, false);
        }
    }

    @Override // android.view.View
    public final boolean verifyDrawable(Drawable drawable) {
        if (super.verifyDrawable(drawable) || drawable == this.mStatusBarBackground) {
            return true;
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public final void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener) {
        this.mOnHierarchyChangeListener = onHierarchyChangeListener;
    }
}
