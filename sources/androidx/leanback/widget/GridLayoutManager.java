package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.CircularIntArray;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.Grid;
import androidx.leanback.widget.ItemAlignment;
import androidx.leanback.widget.ItemAlignmentFacet;
import androidx.leanback.widget.WindowAlignment;
import androidx.recyclerview.widget.GapWorker;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class GridLayoutManager extends RecyclerView.LayoutManager {
    public static final Rect sTempRect = new Rect();
    public static int[] sTwoInts = new int[2];
    public BaseGridView mBaseGridView;
    public ArrayList<OnChildViewHolderSelectedListener> mChildViewHolderSelectedListeners;
    public int mChildVisibility;
    public final ViewsStateBundle mChildrenStates;
    public GridLinearSmoothScroller mCurrentSmoothScroller;
    public int[] mDisappearingPositions;
    public int mExtraLayoutSpaceInPreLayout;
    public FacetProviderAdapter mFacetProviderAdapter;
    public int mFixedRowSizeSecondary;
    public int mFlag;
    public int mFocusPosition;
    public int mFocusPositionOffset;
    public int mGravity;
    public Grid mGrid;
    public AnonymousClass2 mGridProvider;
    public final ItemAlignment mItemAlignment;
    public int mMaxPendingMoves;
    public int mMaxSizeSecondary;
    public int[] mMeasuredDimension;
    public int mNumRows;
    public int mNumRowsRequested;
    public ArrayList<BaseGridView.OnLayoutCompletedListener> mOnLayoutCompletedListeners;
    public int mOrientation;
    public OrientationHelper mOrientationHelper;
    public PendingMoveSmoothScroller mPendingMoveSmoothScroller;
    public int mPositionDeltaInPreLayout;
    public final SparseIntArray mPositionToRowInPostLayout;
    public int mPrimaryScrollExtra;
    public RecyclerView.Recycler mRecycler;
    public final AnonymousClass1 mRequestLayoutRunnable;
    public int[] mRowSizeSecondary;
    public int mRowSizeSecondaryRequested;
    public int mSaveContextLevel;
    public int mScrollOffsetSecondary;
    public int mSizePrimary;
    public float mSmoothScrollSpeedFactor;
    public int mSpacingPrimary;
    public int mSpacingSecondary;
    public RecyclerView.State mState;
    public int mSubFocusPosition;
    public int mVerticalSpacing;
    public final WindowAlignment mWindowAlignment;

    /* renamed from: androidx.leanback.widget.GridLayoutManager$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements Grid.Provider {
        public final void addItem(Object obj, int i, int i2, int i3) {
            int i4;
            int i5;
            PendingMoveSmoothScroller pendingMoveSmoothScroller;
            int i6;
            int i7;
            View view = (View) obj;
            if (i3 == Integer.MIN_VALUE || i3 == Integer.MAX_VALUE) {
                Grid grid = GridLayoutManager.this.mGrid;
                Objects.requireNonNull(grid);
                if (!grid.mReversedFlow) {
                    WindowAlignment windowAlignment = GridLayoutManager.this.mWindowAlignment;
                    Objects.requireNonNull(windowAlignment);
                    WindowAlignment.Axis axis = windowAlignment.mMainAxis;
                    Objects.requireNonNull(axis);
                    i7 = axis.mPaddingMin;
                } else {
                    WindowAlignment windowAlignment2 = GridLayoutManager.this.mWindowAlignment;
                    Objects.requireNonNull(windowAlignment2);
                    WindowAlignment.Axis axis2 = windowAlignment2.mMainAxis;
                    Objects.requireNonNull(axis2);
                    int i8 = axis2.mSize;
                    WindowAlignment windowAlignment3 = GridLayoutManager.this.mWindowAlignment;
                    Objects.requireNonNull(windowAlignment3);
                    WindowAlignment.Axis axis3 = windowAlignment3.mMainAxis;
                    Objects.requireNonNull(axis3);
                    i7 = i8 - axis3.mPaddingMax;
                }
                i3 = i7;
            }
            Grid grid2 = GridLayoutManager.this.mGrid;
            Objects.requireNonNull(grid2);
            if (!grid2.mReversedFlow) {
                i4 = i + i3;
                i5 = i3;
            } else {
                i5 = i3 - i;
                i4 = i3;
            }
            int rowStartSecondary = GridLayoutManager.this.getRowStartSecondary(i2);
            WindowAlignment windowAlignment4 = GridLayoutManager.this.mWindowAlignment;
            Objects.requireNonNull(windowAlignment4);
            WindowAlignment.Axis axis4 = windowAlignment4.mSecondAxis;
            Objects.requireNonNull(axis4);
            int i9 = rowStartSecondary + axis4.mPaddingMin;
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            int i10 = i9 - gridLayoutManager.mScrollOffsetSecondary;
            Objects.requireNonNull(gridLayoutManager.mChildrenStates);
            GridLayoutManager.this.layoutChild(i2, view, i5, i4, i10);
            RecyclerView.State state = GridLayoutManager.this.mState;
            Objects.requireNonNull(state);
            if (!state.mInPreLayout) {
                GridLayoutManager.this.updateScrollLimits();
            }
            GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
            if (!((gridLayoutManager2.mFlag & 3) == 1 || (pendingMoveSmoothScroller = gridLayoutManager2.mPendingMoveSmoothScroller) == null)) {
                if (pendingMoveSmoothScroller.mStaggeredGrid && (i6 = pendingMoveSmoothScroller.mPendingMoves) != 0) {
                    pendingMoveSmoothScroller.mPendingMoves = GridLayoutManager.this.processSelectionMoves(true, i6);
                }
                int i11 = pendingMoveSmoothScroller.mPendingMoves;
                if (i11 == 0 || ((i11 > 0 && GridLayoutManager.this.hasCreatedLastItem()) || (pendingMoveSmoothScroller.mPendingMoves < 0 && GridLayoutManager.this.hasCreatedFirstItem()))) {
                    pendingMoveSmoothScroller.mTargetPosition = GridLayoutManager.this.mFocusPosition;
                    pendingMoveSmoothScroller.stop();
                }
            }
            Objects.requireNonNull(GridLayoutManager.this);
        }

        public AnonymousClass2() {
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x0070  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x00bd  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:25:0x006b -> B:26:0x006c). Please submit an issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:46:0x00b0 -> B:47:0x00b4). Please submit an issue!!! */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final int createItem(int r8, boolean r9, java.lang.Object[] r10, boolean r11) {
            /*
                Method dump skipped, instructions count: 323
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.AnonymousClass2.createItem(int, boolean, java.lang.Object[], boolean):int");
        }

        public final int getCount() {
            return GridLayoutManager.this.mState.getItemCount() + GridLayoutManager.this.mPositionDeltaInPreLayout;
        }

        public final int getEdge(int i) {
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            View findViewByPosition = gridLayoutManager.findViewByPosition(i - gridLayoutManager.mPositionDeltaInPreLayout);
            GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
            if ((gridLayoutManager2.mFlag & 262144) != 0) {
                return gridLayoutManager2.mOrientationHelper.getDecoratedEnd(findViewByPosition);
            }
            return gridLayoutManager2.mOrientationHelper.getDecoratedStart(findViewByPosition);
        }

        public final int getSize(int i) {
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            View findViewByPosition = gridLayoutManager.findViewByPosition(i - gridLayoutManager.mPositionDeltaInPreLayout);
            Rect rect = GridLayoutManager.sTempRect;
            gridLayoutManager.getDecoratedBoundsWithMargins(findViewByPosition, rect);
            if (gridLayoutManager.mOrientation == 0) {
                return rect.width();
            }
            return rect.height();
        }

        public final void removeItem(int i) {
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            View findViewByPosition = gridLayoutManager.findViewByPosition(i - gridLayoutManager.mPositionDeltaInPreLayout);
            GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
            if ((gridLayoutManager2.mFlag & 3) == 1) {
                gridLayoutManager2.scrapOrRecycleView(gridLayoutManager2.mRecycler, gridLayoutManager2.mChildHelper.indexOfChild(findViewByPosition), findViewByPosition);
            } else {
                gridLayoutManager2.removeAndRecycleView(findViewByPosition, gridLayoutManager2.mRecycler);
            }
        }
    }

    /* loaded from: classes.dex */
    public abstract class GridLinearSmoothScroller extends LinearSmoothScroller {
        public boolean mSkipOnStopInternal;

        public GridLinearSmoothScroller() {
            super(GridLayoutManager.this.mBaseGridView.getContext());
        }

        @Override // androidx.recyclerview.widget.LinearSmoothScroller
        public final float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return (25.0f / displayMetrics.densityDpi) * GridLayoutManager.this.mSmoothScrollSpeedFactor;
        }

        public void onStopInternal() {
            View findViewByPosition = this.mRecyclerView.mLayout.findViewByPosition(this.mTargetPosition);
            if (findViewByPosition == null) {
                int i = this.mTargetPosition;
                if (i >= 0) {
                    GridLayoutManager.this.scrollToSelection(i, false);
                    return;
                }
                return;
            }
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            int i2 = gridLayoutManager.mFocusPosition;
            int i3 = this.mTargetPosition;
            if (i2 != i3) {
                gridLayoutManager.mFocusPosition = i3;
            }
            if (gridLayoutManager.hasFocus()) {
                GridLayoutManager.this.mFlag |= 32;
                findViewByPosition.requestFocus();
                GridLayoutManager.this.mFlag &= -33;
            }
            GridLayoutManager.this.dispatchChildSelected();
            GridLayoutManager.this.dispatchChildSelectedAndPositioned();
        }

        @Override // androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.SmoothScroller
        public final void onTargetFound(View view, RecyclerView.SmoothScroller.Action action) {
            int i;
            int i2;
            if (GridLayoutManager.this.getScrollPosition(view, null, GridLayoutManager.sTwoInts)) {
                if (GridLayoutManager.this.mOrientation == 0) {
                    int[] iArr = GridLayoutManager.sTwoInts;
                    i2 = iArr[0];
                    i = iArr[1];
                } else {
                    int[] iArr2 = GridLayoutManager.sTwoInts;
                    int i3 = iArr2[1];
                    i = iArr2[0];
                    i2 = i3;
                }
                action.update(i2, i, calculateTimeForDeceleration((int) Math.sqrt((i * i) + (i2 * i2))), this.mDecelerateInterpolator);
            }
        }

        @Override // androidx.recyclerview.widget.LinearSmoothScroller
        public final int calculateTimeForScrolling(int i) {
            int calculateTimeForScrolling = super.calculateTimeForScrolling(i);
            WindowAlignment windowAlignment = GridLayoutManager.this.mWindowAlignment;
            Objects.requireNonNull(windowAlignment);
            WindowAlignment.Axis axis = windowAlignment.mMainAxis;
            Objects.requireNonNull(axis);
            if (axis.mSize <= 0) {
                return calculateTimeForScrolling;
            }
            WindowAlignment windowAlignment2 = GridLayoutManager.this.mWindowAlignment;
            Objects.requireNonNull(windowAlignment2);
            WindowAlignment.Axis axis2 = windowAlignment2.mMainAxis;
            Objects.requireNonNull(axis2);
            float f = (30.0f / axis2.mSize) * i;
            if (calculateTimeForScrolling < f) {
                return (int) f;
            }
            return calculateTimeForScrolling;
        }

        @Override // androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.SmoothScroller
        public final void onStop() {
            super.onStop();
            if (!this.mSkipOnStopInternal) {
                onStopInternal();
            }
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            if (gridLayoutManager.mCurrentSmoothScroller == this) {
                gridLayoutManager.mCurrentSmoothScroller = null;
            }
            if (gridLayoutManager.mPendingMoveSmoothScroller == this) {
                gridLayoutManager.mPendingMoveSmoothScroller = null;
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class LayoutParams extends RecyclerView.LayoutParams {
        public int[] mAlignMultiple;
        public int mAlignX;
        public int mAlignY;
        public ItemAlignmentFacet mAlignmentFacet;
        public int mBottomInset;
        public int mLeftInset;
        public int mRightInset;
        public int mTopInset;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams() {
            super(-2, -2);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(RecyclerView.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((RecyclerView.LayoutParams) layoutParams);
        }
    }

    /* loaded from: classes.dex */
    public final class PendingMoveSmoothScroller extends GridLinearSmoothScroller {
        public int mPendingMoves;
        public final boolean mStaggeredGrid;

        public PendingMoveSmoothScroller(int i, boolean z) {
            super();
            this.mPendingMoves = i;
            this.mStaggeredGrid = z;
            this.mTargetPosition = -2;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
        public final PointF computeScrollVectorForPosition(int i) {
            int i2;
            int i3 = this.mPendingMoves;
            if (i3 == 0) {
                return null;
            }
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            if ((gridLayoutManager.mFlag & 262144) == 0 ? i3 >= 0 : i3 <= 0) {
                i2 = 1;
            } else {
                i2 = -1;
            }
            if (gridLayoutManager.mOrientation == 0) {
                return new PointF(i2, 0.0f);
            }
            return new PointF(0.0f, i2);
        }

        @Override // androidx.leanback.widget.GridLayoutManager.GridLinearSmoothScroller
        public final void onStopInternal() {
            super.onStopInternal();
            this.mPendingMoves = 0;
            View findViewByPosition = this.mRecyclerView.mLayout.findViewByPosition(this.mTargetPosition);
            if (findViewByPosition != null) {
                GridLayoutManager.this.scrollToView(findViewByPosition, true);
            }
        }
    }

    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: classes.dex */
    public static final class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.leanback.widget.GridLayoutManager.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public Bundle childStates;
        public int index;

        public SavedState(Parcel parcel) {
            this.childStates = Bundle.EMPTY;
            this.index = parcel.readInt();
            this.childStates = parcel.readBundle(GridLayoutManager.class.getClassLoader());
        }

        @Override // android.os.Parcelable
        public final int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.index);
            parcel.writeBundle(this.childStates);
        }

        public SavedState() {
            this.childStates = Bundle.EMPTY;
        }
    }

    public GridLayoutManager() {
        this(null);
    }

    public static int getAdapterPositionByView(View view) {
        LayoutParams layoutParams;
        if (view == null || (layoutParams = (LayoutParams) view.getLayoutParams()) == null || layoutParams.isItemRemoved()) {
            return -1;
        }
        return layoutParams.mViewHolder.getAbsoluteAdapterPosition();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void collectAdjacentPrefetchPositions(int i, int i2, RecyclerView.State state, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        try {
            saveContext(null, state);
            if (this.mOrientation != 0) {
                i = i2;
            }
            if (!(getChildCount() == 0 || i == 0)) {
                int i3 = 0;
                if (i >= 0) {
                    i3 = 0 + this.mSizePrimary;
                }
                this.mGrid.collectAdjacentPrefetchPositions(i3, i, layoutPrefetchRegistry);
            }
        } finally {
            leaveContext();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final RecyclerView.LayoutParams generateLayoutParams(Context context, AttributeSet attributeSet) {
        return new LayoutParams(context, attributeSet);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onAdapterChanged(RecyclerView.Adapter adapter, RecyclerView.Adapter adapter2) {
        if (adapter != null) {
            this.mGrid = null;
            this.mRowSizeSecondary = null;
            this.mFlag &= -1025;
            this.mFocusPosition = -1;
            this.mFocusPositionOffset = 0;
            Objects.requireNonNull(this.mChildrenStates);
        }
        if (adapter2 instanceof FacetProviderAdapter) {
            this.mFacetProviderAdapter = (FacetProviderAdapter) adapter2;
        } else {
            this.mFacetProviderAdapter = null;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onItemsChanged() {
        this.mFocusPositionOffset = 0;
        Objects.requireNonNull(this.mChildrenStates);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onItemsUpdated(int i, int i2) {
        int i3 = i2 + i;
        while (i < i3) {
            Objects.requireNonNull(this.mChildrenStates);
            i++;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final boolean requestChildRectangleOnScreen(RecyclerView recyclerView, View view, Rect rect, boolean z) {
        return false;
    }

    public final int scrollDirectionSecondary(int i) {
        int i2 = 0;
        if (i == 0) {
            return 0;
        }
        int i3 = -i;
        int childCount = getChildCount();
        if (this.mOrientation == 0) {
            while (i2 < childCount) {
                getChildAt(i2).offsetTopAndBottom(i3);
                i2++;
            }
        } else {
            while (i2 < childCount) {
                getChildAt(i2).offsetLeftAndRight(i3);
                i2++;
            }
        }
        this.mScrollOffsetSecondary += i;
        updateSecondaryScrollLimits();
        this.mBaseGridView.invalidate();
        return i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void scrollToPosition(int i) {
        setSelection(i, false);
    }

    public final void scrollToSelection(int i, boolean z) {
        this.mPrimaryScrollExtra = 0;
        View findViewByPosition = findViewByPosition(i);
        boolean z2 = true;
        boolean z3 = !isSmoothScrolling();
        if (!z3 || this.mBaseGridView.isLayoutRequested() || findViewByPosition == null || getAdapterPositionByView(findViewByPosition) != i) {
            int i2 = this.mFlag;
            if ((i2 & 512) == 0 || (i2 & 64) != 0) {
                this.mFocusPosition = i;
                this.mSubFocusPosition = 0;
                this.mFocusPositionOffset = Integer.MIN_VALUE;
            } else if (!z || this.mBaseGridView.isLayoutRequested()) {
                if (!z3) {
                    GridLinearSmoothScroller gridLinearSmoothScroller = this.mCurrentSmoothScroller;
                    if (gridLinearSmoothScroller != null) {
                        gridLinearSmoothScroller.mSkipOnStopInternal = true;
                    }
                    this.mBaseGridView.stopScroll();
                }
                if (this.mBaseGridView.isLayoutRequested() || findViewByPosition == null || getAdapterPositionByView(findViewByPosition) != i) {
                    this.mFocusPosition = i;
                    this.mSubFocusPosition = 0;
                    this.mFocusPositionOffset = Integer.MIN_VALUE;
                    this.mFlag |= 256;
                    requestLayout();
                    return;
                }
                this.mFlag |= 32;
                scrollToView(findViewByPosition, z);
                this.mFlag &= -33;
            } else {
                this.mFocusPosition = i;
                this.mSubFocusPosition = 0;
                this.mFocusPositionOffset = Integer.MIN_VALUE;
                if (this.mGrid == null) {
                    z2 = false;
                }
                if (!z2) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("GridLayoutManager:");
                    m.append(this.mBaseGridView.getId());
                    Log.w(m.toString(), "setSelectionSmooth should not be called before first layout pass");
                    return;
                }
                GridLinearSmoothScroller gridLinearSmoothScroller2 = new GridLinearSmoothScroller() { // from class: androidx.leanback.widget.GridLayoutManager.4
                    @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
                    public final PointF computeScrollVectorForPosition(int i3) {
                        if (this.mRecyclerView.mLayout.getChildCount() == 0) {
                            return null;
                        }
                        boolean z4 = false;
                        int position = RecyclerView.LayoutManager.getPosition(GridLayoutManager.this.getChildAt(0));
                        GridLayoutManager gridLayoutManager = GridLayoutManager.this;
                        int i4 = 1;
                        if ((gridLayoutManager.mFlag & 262144) == 0 ? i3 < position : i3 > position) {
                            z4 = true;
                        }
                        if (z4) {
                            i4 = -1;
                        }
                        if (gridLayoutManager.mOrientation == 0) {
                            return new PointF(i4, 0.0f);
                        }
                        return new PointF(0.0f, i4);
                    }
                };
                gridLinearSmoothScroller2.mTargetPosition = i;
                startSmoothScroll(gridLinearSmoothScroller2);
                int i3 = gridLinearSmoothScroller2.mTargetPosition;
                if (i3 != this.mFocusPosition) {
                    this.mFocusPosition = i3;
                    this.mSubFocusPosition = 0;
                }
            }
        } else {
            this.mFlag |= 32;
            scrollToView(findViewByPosition, z);
            this.mFlag &= -33;
        }
    }

    public final void scrollToView(View view, boolean z) {
        scrollToView(view, view.findFocus(), z, 0, 0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void smoothScrollToPosition(RecyclerView recyclerView, int i) {
        setSelection(i, true);
    }

    /* JADX WARN: Type inference failed for: r2v7, types: [androidx.leanback.widget.GridLayoutManager$1] */
    public GridLayoutManager(BaseGridView baseGridView) {
        this.mSmoothScrollSpeedFactor = 1.0f;
        this.mMaxPendingMoves = 10;
        this.mOrientation = 0;
        this.mOrientationHelper = new OrientationHelper.AnonymousClass1(this);
        this.mPositionToRowInPostLayout = new SparseIntArray();
        this.mFlag = 221696;
        this.mChildViewHolderSelectedListeners = null;
        this.mOnLayoutCompletedListeners = null;
        this.mFocusPosition = -1;
        this.mSubFocusPosition = 0;
        this.mFocusPositionOffset = 0;
        this.mGravity = 8388659;
        this.mNumRowsRequested = 1;
        this.mWindowAlignment = new WindowAlignment();
        this.mItemAlignment = new ItemAlignment();
        this.mMeasuredDimension = new int[2];
        this.mChildrenStates = new ViewsStateBundle();
        this.mRequestLayoutRunnable = new Runnable() { // from class: androidx.leanback.widget.GridLayoutManager.1
            @Override // java.lang.Runnable
            public final void run() {
                GridLayoutManager.this.requestLayout();
            }
        };
        this.mGridProvider = new AnonymousClass2();
        this.mBaseGridView = baseGridView;
        this.mChildVisibility = -1;
        if (this.mItemPrefetchEnabled) {
            this.mItemPrefetchEnabled = false;
            this.mPrefetchMaxCountObserved = 0;
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.mRecycler.updateViewCacheSize();
            }
        }
    }

    public static int getSubPositionByView(View view, View view2) {
        if (view == null || view2 == null) {
            return 0;
        }
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        Objects.requireNonNull(layoutParams);
        ItemAlignmentFacet itemAlignmentFacet = layoutParams.mAlignmentFacet;
        if (itemAlignmentFacet == null) {
            return 0;
        }
        ItemAlignmentFacet.ItemAlignmentDef[] itemAlignmentDefArr = itemAlignmentFacet.mAlignmentDefs;
        if (itemAlignmentDefArr.length <= 1) {
            return 0;
        }
        while (view2 != view) {
            int id = view2.getId();
            if (id != -1) {
                for (int i = 1; i < itemAlignmentDefArr.length; i++) {
                    Objects.requireNonNull(itemAlignmentDefArr[i]);
                    if (-1 == id) {
                        return i;
                    }
                }
                continue;
            }
            view2 = (View) view2.getParent();
        }
        return 0;
    }

    public final void appendVisibleItems() {
        int i;
        Grid grid = this.mGrid;
        if ((this.mFlag & 262144) != 0) {
            i = 0 - this.mExtraLayoutSpaceInPreLayout;
        } else {
            i = this.mExtraLayoutSpaceInPreLayout + this.mSizePrimary + 0;
        }
        Objects.requireNonNull(grid);
        grid.appendVisibleItems(i, false);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final boolean canScrollHorizontally() {
        if (this.mOrientation == 0 || this.mNumRows > 1) {
            return true;
        }
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final boolean canScrollVertically() {
        if (this.mOrientation == 1 || this.mNumRows > 1) {
            return true;
        }
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void collectInitialPrefetchPositions(int i, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int i2 = this.mBaseGridView.mInitialPrefetchItemCount;
        if (!(i == 0 || i2 == 0)) {
            int max = Math.max(0, Math.min(this.mFocusPosition - ((i2 - 1) / 2), i - i2));
            for (int i3 = max; i3 < i && i3 < max + i2; i3++) {
                ((GapWorker.LayoutPrefetchRegistryImpl) layoutPrefetchRegistry).addPosition(i3, 0);
            }
        }
    }

    public final void dispatchChildSelected() {
        boolean z;
        View view;
        ArrayList<OnChildViewHolderSelectedListener> arrayList = this.mChildViewHolderSelectedListeners;
        if (arrayList == null || arrayList.size() <= 0) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            int i = this.mFocusPosition;
            if (i == -1) {
                view = null;
            } else {
                view = findViewByPosition(i);
            }
            if (view != null) {
                RecyclerView.ViewHolder childViewHolder = this.mBaseGridView.getChildViewHolder(view);
                BaseGridView baseGridView = this.mBaseGridView;
                int i2 = this.mFocusPosition;
                ArrayList<OnChildViewHolderSelectedListener> arrayList2 = this.mChildViewHolderSelectedListeners;
                if (arrayList2 != null) {
                    int size = arrayList2.size();
                    while (true) {
                        size--;
                        if (size < 0) {
                            break;
                        }
                        this.mChildViewHolderSelectedListeners.get(size).onChildViewHolderSelected(baseGridView, childViewHolder, i2);
                    }
                }
            } else {
                BaseGridView baseGridView2 = this.mBaseGridView;
                ArrayList<OnChildViewHolderSelectedListener> arrayList3 = this.mChildViewHolderSelectedListeners;
                if (arrayList3 != null) {
                    int size2 = arrayList3.size();
                    while (true) {
                        size2--;
                        if (size2 < 0) {
                            break;
                        }
                        this.mChildViewHolderSelectedListeners.get(size2).onChildViewHolderSelected(baseGridView2, null, -1);
                    }
                }
            }
            if (!((this.mFlag & 3) == 1 || this.mBaseGridView.isLayoutRequested())) {
                int childCount = getChildCount();
                for (int i3 = 0; i3 < childCount; i3++) {
                    if (getChildAt(i3).isLayoutRequested()) {
                        BaseGridView baseGridView3 = this.mBaseGridView;
                        AnonymousClass1 r8 = this.mRequestLayoutRunnable;
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                        ViewCompat.Api16Impl.postOnAnimation(baseGridView3, r8);
                        return;
                    }
                }
            }
        }
    }

    public final void dispatchChildSelectedAndPositioned() {
        boolean z;
        ArrayList<OnChildViewHolderSelectedListener> arrayList = this.mChildViewHolderSelectedListeners;
        if (arrayList == null || arrayList.size() <= 0) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            int i = this.mFocusPosition;
            View view = null;
            if (i != -1) {
                view = findViewByPosition(i);
            }
            if (view != null) {
                this.mBaseGridView.getChildViewHolder(view);
                ArrayList<OnChildViewHolderSelectedListener> arrayList2 = this.mChildViewHolderSelectedListeners;
                if (arrayList2 != null) {
                    int size = arrayList2.size();
                    while (true) {
                        size--;
                        if (size >= 0) {
                            Objects.requireNonNull(this.mChildViewHolderSelectedListeners.get(size));
                        } else {
                            return;
                        }
                    }
                }
            } else {
                ArrayList<OnChildViewHolderSelectedListener> arrayList3 = this.mChildViewHolderSelectedListeners;
                if (arrayList3 != null) {
                    int size2 = arrayList3.size();
                    while (true) {
                        size2--;
                        if (size2 >= 0) {
                            Objects.requireNonNull(this.mChildViewHolderSelectedListeners.get(size2));
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) layoutParams);
        }
        if (layoutParams instanceof RecyclerView.LayoutParams) {
            return new LayoutParams((RecyclerView.LayoutParams) layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int getColumnCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Grid grid;
        if (this.mOrientation != 1 || (grid = this.mGrid) == null) {
            return -1;
        }
        Objects.requireNonNull(grid);
        return grid.mNumRows;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0035, code lost:
        if (r10 != 130) goto L_0x0046;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x003d, code lost:
        if ((r9.mFlag & 524288) == 0) goto L_0x001b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0043, code lost:
        if ((r9.mFlag & 524288) == 0) goto L_0x0023;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:?, code lost:
        return 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0018, code lost:
        if (r10 != 130) goto L_0x0046;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int getMovement(int r10) {
        /*
            r9 = this;
            int r0 = r9.mOrientation
            r1 = 130(0x82, float:1.82E-43)
            r2 = 66
            r3 = 33
            r4 = 0
            r5 = 3
            r6 = 2
            r7 = 17
            r8 = 1
            if (r0 != 0) goto L_0x002b
            r0 = 262144(0x40000, float:3.67342E-40)
            if (r10 == r7) goto L_0x0025
            if (r10 == r3) goto L_0x0023
            if (r10 == r2) goto L_0x001d
            if (r10 == r1) goto L_0x001b
            goto L_0x0046
        L_0x001b:
            r4 = r5
            goto L_0x0047
        L_0x001d:
            int r9 = r9.mFlag
            r9 = r9 & r0
            if (r9 != 0) goto L_0x0047
            goto L_0x0038
        L_0x0023:
            r4 = r6
            goto L_0x0047
        L_0x0025:
            int r9 = r9.mFlag
            r9 = r9 & r0
            if (r9 != 0) goto L_0x0038
            goto L_0x0047
        L_0x002b:
            if (r0 != r8) goto L_0x0046
            r0 = 524288(0x80000, float:7.34684E-40)
            if (r10 == r7) goto L_0x0040
            if (r10 == r3) goto L_0x0047
            if (r10 == r2) goto L_0x003a
            if (r10 == r1) goto L_0x0038
            goto L_0x0046
        L_0x0038:
            r4 = r8
            goto L_0x0047
        L_0x003a:
            int r9 = r9.mFlag
            r9 = r9 & r0
            if (r9 != 0) goto L_0x0023
            goto L_0x001b
        L_0x0040:
            int r9 = r9.mFlag
            r9 = r9 & r0
            if (r9 != 0) goto L_0x001b
            goto L_0x0023
        L_0x0046:
            r4 = r7
        L_0x0047:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.getMovement(int):int");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int getRowCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Grid grid;
        if (this.mOrientation != 0 || (grid = this.mGrid) == null) {
            return -1;
        }
        Objects.requireNonNull(grid);
        return grid.mNumRows;
    }

    public final int getRowSizeSecondary(int i) {
        int i2 = this.mFixedRowSizeSecondary;
        if (i2 != 0) {
            return i2;
        }
        int[] iArr = this.mRowSizeSecondary;
        if (iArr == null) {
            return 0;
        }
        return iArr[i];
    }

    public final int getRowStartSecondary(int i) {
        int i2 = 0;
        if ((this.mFlag & 524288) != 0) {
            for (int i3 = this.mNumRows - 1; i3 > i; i3--) {
                i2 += getRowSizeSecondary(i3) + this.mSpacingSecondary;
            }
            return i2;
        }
        int i4 = 0;
        while (i2 < i) {
            i4 += getRowSizeSecondary(i2) + this.mSpacingSecondary;
            i2++;
        }
        return i4;
    }

    public final boolean getScrollPosition(View view, View view2, int[] iArr) {
        int i;
        int i2;
        int i3;
        int i4;
        int subPositionByView;
        WindowAlignment windowAlignment = this.mWindowAlignment;
        Objects.requireNonNull(windowAlignment);
        WindowAlignment.Axis axis = windowAlignment.mMainAxis;
        if (this.mOrientation == 0) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            Objects.requireNonNull(layoutParams);
            i = view.getLeft() + layoutParams.mLeftInset;
            i2 = layoutParams.mAlignX;
        } else {
            LayoutParams layoutParams2 = (LayoutParams) view.getLayoutParams();
            Objects.requireNonNull(layoutParams2);
            i = view.getTop() + layoutParams2.mTopInset;
            i2 = layoutParams2.mAlignY;
        }
        int scroll = axis.getScroll(i + i2);
        if (!(view2 == null || (subPositionByView = getSubPositionByView(view, view2)) == 0)) {
            LayoutParams layoutParams3 = (LayoutParams) view.getLayoutParams();
            Objects.requireNonNull(layoutParams3);
            int[] iArr2 = layoutParams3.mAlignMultiple;
            scroll += iArr2[subPositionByView] - iArr2[0];
        }
        if (this.mOrientation == 0) {
            LayoutParams layoutParams4 = (LayoutParams) view.getLayoutParams();
            Objects.requireNonNull(layoutParams4);
            i4 = view.getTop() + layoutParams4.mTopInset;
            i3 = layoutParams4.mAlignY;
        } else {
            LayoutParams layoutParams5 = (LayoutParams) view.getLayoutParams();
            Objects.requireNonNull(layoutParams5);
            i4 = view.getLeft() + layoutParams5.mLeftInset;
            i3 = layoutParams5.mAlignX;
        }
        int i5 = i4 + i3;
        WindowAlignment windowAlignment2 = this.mWindowAlignment;
        Objects.requireNonNull(windowAlignment2);
        int scroll2 = windowAlignment2.mSecondAxis.getScroll(i5);
        int i6 = scroll + this.mPrimaryScrollExtra;
        if (i6 == 0 && scroll2 == 0) {
            iArr[0] = 0;
            iArr[1] = 0;
            return false;
        }
        iArr[0] = i6;
        iArr[1] = scroll2;
        return true;
    }

    public final int getSizeSecondary() {
        int i;
        if ((this.mFlag & 524288) != 0) {
            i = 0;
        } else {
            i = this.mNumRows - 1;
        }
        return getRowSizeSecondary(i) + getRowStartSecondary(i);
    }

    public final View getViewForPosition(int i) {
        Object obj;
        FacetProviderAdapter facetProviderAdapter;
        View viewForPosition = this.mRecycler.getViewForPosition(i);
        LayoutParams layoutParams = (LayoutParams) viewForPosition.getLayoutParams();
        RecyclerView.ViewHolder childViewHolder = this.mBaseGridView.getChildViewHolder(viewForPosition);
        if (childViewHolder instanceof FacetProvider) {
            obj = ((FacetProvider) childViewHolder).getFacet();
        } else {
            obj = null;
        }
        if (obj == null && (facetProviderAdapter = this.mFacetProviderAdapter) != null) {
            Objects.requireNonNull(childViewHolder);
            FacetProvider facetProvider = facetProviderAdapter.getFacetProvider();
            if (facetProvider != null) {
                obj = facetProvider.getFacet();
            }
        }
        Objects.requireNonNull(layoutParams);
        layoutParams.mAlignmentFacet = (ItemAlignmentFacet) obj;
        return viewForPosition;
    }

    public final boolean isItemFullyVisible(int i) {
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.mBaseGridView.findViewHolderForAdapterPosition(i);
        if (findViewHolderForAdapterPosition != null && findViewHolderForAdapterPosition.itemView.getLeft() >= 0 && findViewHolderForAdapterPosition.itemView.getRight() <= this.mBaseGridView.getWidth() && findViewHolderForAdapterPosition.itemView.getTop() >= 0 && findViewHolderForAdapterPosition.itemView.getBottom() <= this.mBaseGridView.getHeight()) {
            return true;
        }
        return false;
    }

    public final void layoutChild(int i, View view, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        if (this.mOrientation == 0) {
            i5 = getDecoratedMeasuredHeightWithMargin(view);
        } else {
            i5 = getDecoratedMeasuredWidthWithMargin(view);
        }
        int i8 = this.mFixedRowSizeSecondary;
        if (i8 > 0) {
            i5 = Math.min(i5, i8);
        }
        int i9 = this.mGravity;
        int i10 = i9 & 112;
        if ((this.mFlag & 786432) != 0) {
            i6 = Gravity.getAbsoluteGravity(i9 & 8388615, 1);
        } else {
            i6 = i9 & 7;
        }
        int i11 = this.mOrientation;
        if (!((i11 == 0 && i10 == 48) || (i11 == 1 && i6 == 3))) {
            if ((i11 == 0 && i10 == 80) || (i11 == 1 && i6 == 5)) {
                i7 = getRowSizeSecondary(i) - i5;
            } else if ((i11 == 0 && i10 == 16) || (i11 == 1 && i6 == 1)) {
                i7 = (getRowSizeSecondary(i) - i5) / 2;
            }
            i4 += i7;
        }
        int i12 = i5 + i4;
        if (this.mOrientation != 0) {
            i4 = i2;
            i2 = i4;
            i12 = i3;
            i3 = i12;
        }
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        RecyclerView.LayoutManager.layoutDecoratedWithMargins(view, i2, i4, i3, i12);
        Rect rect = sTempRect;
        super.getDecoratedBoundsWithMargins(view, rect);
        int i13 = i2 - rect.left;
        int i14 = i4 - rect.top;
        int i15 = rect.right - i3;
        Objects.requireNonNull(layoutParams);
        layoutParams.mLeftInset = i13;
        layoutParams.mTopInset = i14;
        layoutParams.mRightInset = i15;
        layoutParams.mBottomInset = rect.bottom - i12;
        LayoutParams layoutParams2 = (LayoutParams) view.getLayoutParams();
        Objects.requireNonNull(layoutParams2);
        ItemAlignmentFacet itemAlignmentFacet = layoutParams2.mAlignmentFacet;
        if (itemAlignmentFacet == null) {
            ItemAlignment.Axis axis = this.mItemAlignment.horizontal;
            Objects.requireNonNull(axis);
            layoutParams2.mAlignX = ItemAlignmentFacetHelper.getAlignmentPosition(view, axis, axis.mOrientation);
            ItemAlignment.Axis axis2 = this.mItemAlignment.vertical;
            Objects.requireNonNull(axis2);
            layoutParams2.mAlignY = ItemAlignmentFacetHelper.getAlignmentPosition(view, axis2, axis2.mOrientation);
            return;
        }
        int i16 = this.mOrientation;
        ItemAlignmentFacet.ItemAlignmentDef[] itemAlignmentDefArr = itemAlignmentFacet.mAlignmentDefs;
        int[] iArr = layoutParams2.mAlignMultiple;
        if (iArr == null || iArr.length != itemAlignmentDefArr.length) {
            layoutParams2.mAlignMultiple = new int[itemAlignmentDefArr.length];
        }
        for (int i17 = 0; i17 < itemAlignmentDefArr.length; i17++) {
            layoutParams2.mAlignMultiple[i17] = ItemAlignmentFacetHelper.getAlignmentPosition(view, itemAlignmentDefArr[i17], i16);
        }
        if (i16 == 0) {
            layoutParams2.mAlignX = layoutParams2.mAlignMultiple[0];
        } else {
            layoutParams2.mAlignY = layoutParams2.mAlignMultiple[0];
        }
        if (this.mOrientation == 0) {
            ItemAlignment.Axis axis3 = this.mItemAlignment.vertical;
            Objects.requireNonNull(axis3);
            layoutParams2.mAlignY = ItemAlignmentFacetHelper.getAlignmentPosition(view, axis3, axis3.mOrientation);
            return;
        }
        ItemAlignment.Axis axis4 = this.mItemAlignment.horizontal;
        Objects.requireNonNull(axis4);
        layoutParams2.mAlignX = ItemAlignmentFacetHelper.getAlignmentPosition(view, axis4, axis4.mOrientation);
    }

    public final void leaveContext() {
        int i = this.mSaveContextLevel - 1;
        this.mSaveContextLevel = i;
        if (i == 0) {
            this.mRecycler = null;
            this.mState = null;
            this.mPositionDeltaInPreLayout = 0;
            this.mExtraLayoutSpaceInPreLayout = 0;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:65:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x00d1  */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onAddFocusables(androidx.recyclerview.widget.RecyclerView r18, java.util.ArrayList<android.view.View> r19, int r20, int r21) {
        /*
            Method dump skipped, instructions count: 331
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.onAddFocusables(androidx.recyclerview.widget.RecyclerView, java.util.ArrayList, int, int):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x00cb A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00cc  */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.view.View onInterceptFocusSearch(android.view.View r8, int r9) {
        /*
            Method dump skipped, instructions count: 223
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.onInterceptFocusSearch(android.view.View, int):android.view.View");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onItemsAdded(int i, int i2) {
        Grid grid;
        int i3;
        int i4 = this.mFocusPosition;
        if (!(i4 == -1 || (grid = this.mGrid) == null || grid.mFirstVisibleIndex < 0 || (i3 = this.mFocusPositionOffset) == Integer.MIN_VALUE || i > i4 + i3)) {
            this.mFocusPositionOffset = i3 + i2;
        }
        Objects.requireNonNull(this.mChildrenStates);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onItemsMoved(int i, int i2) {
        int i3;
        int i4 = this.mFocusPosition;
        if (!(i4 == -1 || (i3 = this.mFocusPositionOffset) == Integer.MIN_VALUE)) {
            int i5 = i4 + i3;
            if (i <= i5 && i5 < i + 1) {
                this.mFocusPositionOffset = (i2 - i) + i3;
            } else if (i < i5 && i2 > i5 - 1) {
                this.mFocusPositionOffset = i3 - 1;
            } else if (i > i5 && i2 < i5) {
                this.mFocusPositionOffset = i3 + 1;
            }
        }
        Objects.requireNonNull(this.mChildrenStates);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onItemsRemoved(int i, int i2) {
        Grid grid;
        int i3;
        int i4;
        int i5 = this.mFocusPosition;
        if (!(i5 == -1 || (grid = this.mGrid) == null || grid.mFirstVisibleIndex < 0 || (i3 = this.mFocusPositionOffset) == Integer.MIN_VALUE || i > (i4 = i5 + i3))) {
            if (i + i2 > i4) {
                this.mFocusPosition = (i - i4) + i3 + i5;
                this.mFocusPositionOffset = Integer.MIN_VALUE;
            } else {
                this.mFocusPositionOffset = i3 - i2;
            }
        }
        Objects.requireNonNull(this.mChildrenStates);
    }

    /* JADX WARN: Code restructure failed: missing block: B:103:0x0216, code lost:
        if (r2 != r11.mReversedFlow) goto L_0x0218;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x06dd, code lost:
        if (r1 < 0) goto L_0x070e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x070c, code lost:
        if (r1 < 0) goto L_0x070e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x070e, code lost:
        r0 = r0 + r1;
     */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onLayoutChildren(androidx.recyclerview.widget.RecyclerView.Recycler r24, androidx.recyclerview.widget.RecyclerView.State r25) {
        /*
            Method dump skipped, instructions count: 1840
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.onLayoutChildren(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State):void");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onLayoutCompleted(RecyclerView.State state) {
        ArrayList<BaseGridView.OnLayoutCompletedListener> arrayList = this.mOnLayoutCompletedListeners;
        if (arrayList != null) {
            int size = arrayList.size();
            while (true) {
                size--;
                if (size >= 0) {
                    this.mOnLayoutCompletedListeners.get(size).onLayoutCompleted();
                } else {
                    return;
                }
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final boolean onRequestChildFocus(RecyclerView recyclerView, View view, View view2) {
        if ((this.mFlag & 32768) == 0 && getAdapterPositionByView(view) != -1 && (this.mFlag & 35) == 0) {
            scrollToView(view, view2, true, 0, 0);
        }
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            this.mFocusPosition = ((SavedState) parcelable).index;
            this.mFocusPositionOffset = 0;
            Objects.requireNonNull(this.mChildrenStates);
            this.mFlag |= 256;
            requestLayout();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState();
        savedState.index = this.mFocusPosition;
        Objects.requireNonNull(this.mChildrenStates);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (getAdapterPositionByView(getChildAt(i)) != -1) {
                Objects.requireNonNull(this.mChildrenStates);
            }
        }
        savedState.childStates = null;
        return savedState;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x002c, code lost:
        if (r5 != false) goto L_0x004c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0037, code lost:
        if (r5 != false) goto L_0x0042;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x004a, code lost:
        if (r7 == androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_DOWN.getId()) goto L_0x004c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x004c, code lost:
        r7 = 4096;
     */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean performAccessibilityAction(androidx.recyclerview.widget.RecyclerView.Recycler r5, androidx.recyclerview.widget.RecyclerView.State r6, int r7, android.os.Bundle r8) {
        /*
            r4 = this;
            int r8 = r4.mFlag
            r0 = 131072(0x20000, float:1.83671E-40)
            r8 = r8 & r0
            r0 = 0
            r1 = 1
            if (r8 == 0) goto L_0x000b
            r8 = r1
            goto L_0x000c
        L_0x000b:
            r8 = r0
        L_0x000c:
            if (r8 != 0) goto L_0x000f
            return r1
        L_0x000f:
            r4.saveContext(r5, r6)
            int r5 = r4.mFlag
            r8 = 262144(0x40000, float:3.67342E-40)
            r5 = r5 & r8
            if (r5 == 0) goto L_0x001b
            r5 = r1
            goto L_0x001c
        L_0x001b:
            r5 = r0
        L_0x001c:
            int r8 = r4.mOrientation
            r2 = 8192(0x2000, float:1.14794E-41)
            r3 = 4096(0x1000, float:5.74E-42)
            if (r8 != 0) goto L_0x003a
            androidx.core.view.accessibility.AccessibilityNodeInfoCompat$AccessibilityActionCompat r8 = androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_LEFT
            int r8 = r8.getId()
            if (r7 != r8) goto L_0x002f
            if (r5 == 0) goto L_0x0042
            goto L_0x004c
        L_0x002f:
            androidx.core.view.accessibility.AccessibilityNodeInfoCompat$AccessibilityActionCompat r8 = androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_RIGHT
            int r8 = r8.getId()
            if (r7 != r8) goto L_0x004d
            if (r5 == 0) goto L_0x004c
            goto L_0x0042
        L_0x003a:
            androidx.core.view.accessibility.AccessibilityNodeInfoCompat$AccessibilityActionCompat r5 = androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_UP
            int r5 = r5.getId()
            if (r7 != r5) goto L_0x0044
        L_0x0042:
            r7 = r2
            goto L_0x004d
        L_0x0044:
            androidx.core.view.accessibility.AccessibilityNodeInfoCompat$AccessibilityActionCompat r5 = androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_DOWN
            int r5 = r5.getId()
            if (r7 != r5) goto L_0x004d
        L_0x004c:
            r7 = r3
        L_0x004d:
            int r5 = r4.mFocusPosition
            if (r5 != 0) goto L_0x0055
            if (r7 != r2) goto L_0x0055
            r8 = r1
            goto L_0x0056
        L_0x0055:
            r8 = r0
        L_0x0056:
            int r6 = r6.getItemCount()
            int r6 = r6 - r1
            if (r5 != r6) goto L_0x0061
            if (r7 != r3) goto L_0x0061
            r5 = r1
            goto L_0x0062
        L_0x0061:
            r5 = r0
        L_0x0062:
            if (r8 != 0) goto L_0x007b
            if (r5 == 0) goto L_0x0067
            goto L_0x007b
        L_0x0067:
            if (r7 == r3) goto L_0x0074
            if (r7 == r2) goto L_0x006c
            goto L_0x0089
        L_0x006c:
            r4.processPendingMovement(r0)
            r5 = -1
            r4.processSelectionMoves(r0, r5)
            goto L_0x0089
        L_0x0074:
            r4.processPendingMovement(r1)
            r4.processSelectionMoves(r0, r1)
            goto L_0x0089
        L_0x007b:
            android.view.accessibility.AccessibilityEvent r5 = android.view.accessibility.AccessibilityEvent.obtain(r3)
            androidx.leanback.widget.BaseGridView r6 = r4.mBaseGridView
            r6.onInitializeAccessibilityEvent(r5)
            androidx.leanback.widget.BaseGridView r6 = r4.mBaseGridView
            r6.requestSendAccessibilityEvent(r6, r5)
        L_0x0089:
            r4.leaveContext()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.performAccessibilityAction(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State, int, android.os.Bundle):boolean");
    }

    public final void prependVisibleItems() {
        int i;
        Grid grid = this.mGrid;
        if ((this.mFlag & 262144) != 0) {
            i = this.mSizePrimary + 0 + this.mExtraLayoutSpaceInPreLayout;
        } else {
            i = 0 - this.mExtraLayoutSpaceInPreLayout;
        }
        Objects.requireNonNull(grid);
        grid.prependVisibleItems(i, false);
    }

    public final void processPendingMovement(boolean z) {
        if (z) {
            if (hasCreatedLastItem()) {
                return;
            }
        } else if (hasCreatedFirstItem()) {
            return;
        }
        PendingMoveSmoothScroller pendingMoveSmoothScroller = this.mPendingMoveSmoothScroller;
        int i = -1;
        boolean z2 = true;
        if (pendingMoveSmoothScroller == null) {
            if (z) {
                i = 1;
            }
            if (this.mNumRows <= 1) {
                z2 = false;
            }
            PendingMoveSmoothScroller pendingMoveSmoothScroller2 = new PendingMoveSmoothScroller(i, z2);
            this.mFocusPositionOffset = 0;
            startSmoothScroll(pendingMoveSmoothScroller2);
        } else if (z) {
            Objects.requireNonNull(pendingMoveSmoothScroller);
            int i2 = pendingMoveSmoothScroller.mPendingMoves;
            if (i2 < GridLayoutManager.this.mMaxPendingMoves) {
                pendingMoveSmoothScroller.mPendingMoves = i2 + 1;
            }
        } else {
            Objects.requireNonNull(pendingMoveSmoothScroller);
            int i3 = pendingMoveSmoothScroller.mPendingMoves;
            if (i3 > (-GridLayoutManager.this.mMaxPendingMoves)) {
                pendingMoveSmoothScroller.mPendingMoves = i3 - 1;
            }
        }
    }

    public final boolean processRowSizeSecondary(boolean z) {
        CircularIntArray[] circularIntArrayArr;
        CircularIntArray circularIntArray;
        int i;
        int i2;
        int i3;
        if (this.mFixedRowSizeSecondary != 0 || this.mRowSizeSecondary == null) {
            return false;
        }
        Grid grid = this.mGrid;
        if (grid == null) {
            circularIntArrayArr = null;
        } else {
            circularIntArrayArr = grid.getItemPositionsInRows(grid.mFirstVisibleIndex, grid.mLastVisibleIndex);
        }
        boolean z2 = false;
        int i4 = -1;
        for (int i5 = 0; i5 < this.mNumRows; i5++) {
            if (circularIntArrayArr == null) {
                circularIntArray = null;
            } else {
                circularIntArray = circularIntArrayArr[i5];
            }
            if (circularIntArray == null) {
                i = 0;
            } else {
                i = (circularIntArray.mTail + 0) & circularIntArray.mCapacityBitmask;
            }
            int i6 = -1;
            for (int i7 = 0; i7 < i; i7 += 2) {
                if (i7 >= 0) {
                    int i8 = circularIntArray.mTail;
                    int i9 = circularIntArray.mCapacityBitmask;
                    if (i7 < ((i8 + 0) & i9)) {
                        int[] iArr = circularIntArray.mElements;
                        int i10 = i7 + 1;
                        if (i10 < 0 || i10 >= ((i8 + 0) & i9)) {
                            throw new ArrayIndexOutOfBoundsException();
                        }
                        int i11 = iArr[(i10 + 0) & i9];
                        for (int i12 = iArr[(i7 + 0) & i9]; i12 <= i11; i12++) {
                            View findViewByPosition = findViewByPosition(i12 - this.mPositionDeltaInPreLayout);
                            if (findViewByPosition != null) {
                                if (z) {
                                    measureChild(findViewByPosition);
                                }
                                if (this.mOrientation == 0) {
                                    i3 = getDecoratedMeasuredHeightWithMargin(findViewByPosition);
                                } else {
                                    i3 = getDecoratedMeasuredWidthWithMargin(findViewByPosition);
                                }
                                if (i3 > i6) {
                                    i6 = i3;
                                }
                            }
                        }
                    }
                } else {
                    Objects.requireNonNull(circularIntArray);
                }
                throw new ArrayIndexOutOfBoundsException();
            }
            int itemCount = this.mState.getItemCount();
            BaseGridView baseGridView = this.mBaseGridView;
            Objects.requireNonNull(baseGridView);
            if (!baseGridView.mHasFixedSize && z && i6 < 0 && itemCount > 0) {
                if (i4 < 0) {
                    int i13 = this.mFocusPosition;
                    if (i13 < 0) {
                        i13 = 0;
                    } else if (i13 >= itemCount) {
                        i13 = itemCount - 1;
                    }
                    if (getChildCount() > 0) {
                        int layoutPosition = this.mBaseGridView.getChildViewHolder(getChildAt(0)).getLayoutPosition();
                        int layoutPosition2 = this.mBaseGridView.getChildViewHolder(getChildAt(getChildCount() - 1)).getLayoutPosition();
                        if (i13 >= layoutPosition && i13 <= layoutPosition2) {
                            i13 = i13 - layoutPosition <= layoutPosition2 - i13 ? layoutPosition - 1 : layoutPosition2 + 1;
                            if (i13 < 0 && layoutPosition2 < itemCount - 1) {
                                i13 = layoutPosition2 + 1;
                            } else if (i13 >= itemCount && layoutPosition > 0) {
                                i13 = layoutPosition - 1;
                            }
                        }
                    }
                    if (i13 >= 0 && i13 < itemCount) {
                        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
                        int[] iArr2 = this.mMeasuredDimension;
                        View viewForPosition = this.mRecycler.getViewForPosition(i13);
                        if (viewForPosition != null) {
                            LayoutParams layoutParams = (LayoutParams) viewForPosition.getLayoutParams();
                            Rect rect = sTempRect;
                            calculateItemDecorationsForChild(viewForPosition, rect);
                            viewForPosition.measure(ViewGroup.getChildMeasureSpec(makeMeasureSpec, getPaddingRight() + getPaddingLeft() + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + rect.left + rect.right, ((ViewGroup.MarginLayoutParams) layoutParams).width), ViewGroup.getChildMeasureSpec(makeMeasureSpec2, getPaddingBottom() + getPaddingTop() + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + rect.top + rect.bottom, ((ViewGroup.MarginLayoutParams) layoutParams).height));
                            iArr2[0] = getDecoratedMeasuredWidthWithMargin(viewForPosition);
                            iArr2[1] = getDecoratedMeasuredHeightWithMargin(viewForPosition);
                            this.mRecycler.recycleView(viewForPosition);
                        }
                        if (this.mOrientation == 0) {
                            i2 = this.mMeasuredDimension[1];
                        } else {
                            i2 = this.mMeasuredDimension[0];
                        }
                        i4 = i2;
                    }
                }
                if (i4 >= 0) {
                    i6 = i4;
                }
            }
            if (i6 < 0) {
                i6 = 0;
            }
            int[] iArr3 = this.mRowSizeSecondary;
            if (iArr3[i5] != i6) {
                iArr3[i5] = i6;
                z2 = true;
            }
        }
        return z2;
    }

    public final int processSelectionMoves(boolean z, int i) {
        int i2;
        int i3;
        int i4;
        Grid.Location location;
        Grid grid = this.mGrid;
        if (grid == null) {
            return i;
        }
        int i5 = this.mFocusPosition;
        if (i5 == -1 || (location = grid.getLocation(i5)) == null) {
            i2 = -1;
        } else {
            i2 = location.row;
        }
        View view = null;
        int childCount = getChildCount();
        int i6 = 0;
        while (true) {
            boolean z2 = true;
            if (i6 >= childCount || i == 0) {
                break;
            }
            if (i > 0) {
                i3 = i6;
            } else {
                i3 = (childCount - 1) - i6;
            }
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() != 0 || (hasFocus() && !childAt.hasFocusable())) {
                z2 = false;
            }
            if (z2) {
                int adapterPositionByView = getAdapterPositionByView(getChildAt(i3));
                Grid grid2 = this.mGrid;
                Objects.requireNonNull(grid2);
                Grid.Location location2 = grid2.getLocation(adapterPositionByView);
                if (location2 == null) {
                    i4 = -1;
                } else {
                    i4 = location2.row;
                }
                if (i2 == -1) {
                    i5 = adapterPositionByView;
                    i2 = i4;
                } else if (i4 == i2 && ((i > 0 && adapterPositionByView > i5) || (i < 0 && adapterPositionByView < i5))) {
                    if (i > 0) {
                        i--;
                    } else {
                        i++;
                    }
                    i5 = adapterPositionByView;
                }
                view = childAt;
            }
            i6++;
        }
        if (view != null) {
            if (z) {
                if (hasFocus()) {
                    this.mFlag |= 32;
                    view.requestFocus();
                    this.mFlag &= -33;
                }
                this.mFocusPosition = i5;
                this.mSubFocusPosition = 0;
            } else {
                scrollToView(view, true);
            }
        }
        return i;
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x003e, code lost:
        r0 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void removeInvisibleViewsAtEnd() {
        /*
            r6 = this;
            int r0 = r6.mFlag
            r1 = 65600(0x10040, float:9.1925E-41)
            r1 = r1 & r0
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r1 != r2) goto L_0x005d
            androidx.leanback.widget.Grid r1 = r6.mGrid
            int r2 = r6.mFocusPosition
            r3 = 262144(0x40000, float:3.67342E-40)
            r0 = r0 & r3
            r3 = 0
            if (r0 == 0) goto L_0x0016
            r6 = r3
            goto L_0x0019
        L_0x0016:
            int r6 = r6.mSizePrimary
            int r6 = r6 + r3
        L_0x0019:
            java.util.Objects.requireNonNull(r1)
        L_0x001c:
            int r0 = r1.mLastVisibleIndex
            int r4 = r1.mFirstVisibleIndex
            if (r0 < r4) goto L_0x0052
            if (r0 <= r2) goto L_0x0052
            boolean r4 = r1.mReversedFlow
            r5 = 1
            if (r4 != 0) goto L_0x0034
            androidx.leanback.widget.Grid$Provider r4 = r1.mProvider
            androidx.leanback.widget.GridLayoutManager$2 r4 = (androidx.leanback.widget.GridLayoutManager.AnonymousClass2) r4
            int r0 = r4.getEdge(r0)
            if (r0 < r6) goto L_0x0040
            goto L_0x003e
        L_0x0034:
            androidx.leanback.widget.Grid$Provider r4 = r1.mProvider
            androidx.leanback.widget.GridLayoutManager$2 r4 = (androidx.leanback.widget.GridLayoutManager.AnonymousClass2) r4
            int r0 = r4.getEdge(r0)
            if (r0 > r6) goto L_0x0040
        L_0x003e:
            r0 = r5
            goto L_0x0041
        L_0x0040:
            r0 = r3
        L_0x0041:
            if (r0 == 0) goto L_0x0052
            androidx.leanback.widget.Grid$Provider r0 = r1.mProvider
            int r4 = r1.mLastVisibleIndex
            androidx.leanback.widget.GridLayoutManager$2 r0 = (androidx.leanback.widget.GridLayoutManager.AnonymousClass2) r0
            r0.removeItem(r4)
            int r0 = r1.mLastVisibleIndex
            int r0 = r0 - r5
            r1.mLastVisibleIndex = r0
            goto L_0x001c
        L_0x0052:
            int r6 = r1.mLastVisibleIndex
            int r0 = r1.mFirstVisibleIndex
            if (r6 >= r0) goto L_0x005d
            r6 = -1
            r1.mLastVisibleIndex = r6
            r1.mFirstVisibleIndex = r6
        L_0x005d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.removeInvisibleViewsAtEnd():void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x003c, code lost:
        if ((((androidx.leanback.widget.GridLayoutManager.AnonymousClass2) r1.mProvider).getEdge(r1.mFirstVisibleIndex) + r0) <= r7) goto L_0x004c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x004a, code lost:
        if ((((androidx.leanback.widget.GridLayoutManager.AnonymousClass2) r1.mProvider).getEdge(r1.mFirstVisibleIndex) - r0) >= r7) goto L_0x004c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004c, code lost:
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x004e, code lost:
        r0 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void removeInvisibleViewsAtFront() {
        /*
            r7 = this;
            int r0 = r7.mFlag
            r1 = 65600(0x10040, float:9.1925E-41)
            r1 = r1 & r0
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r1 != r2) goto L_0x006b
            androidx.leanback.widget.Grid r1 = r7.mGrid
            int r2 = r7.mFocusPosition
            r3 = 262144(0x40000, float:3.67342E-40)
            r0 = r0 & r3
            r3 = 0
            if (r0 == 0) goto L_0x0018
            int r7 = r7.mSizePrimary
            int r7 = r7 + r3
            goto L_0x0019
        L_0x0018:
            r7 = r3
        L_0x0019:
            java.util.Objects.requireNonNull(r1)
        L_0x001c:
            int r0 = r1.mLastVisibleIndex
            int r4 = r1.mFirstVisibleIndex
            if (r0 < r4) goto L_0x0060
            if (r4 >= r2) goto L_0x0060
            androidx.leanback.widget.Grid$Provider r0 = r1.mProvider
            androidx.leanback.widget.GridLayoutManager$2 r0 = (androidx.leanback.widget.GridLayoutManager.AnonymousClass2) r0
            int r0 = r0.getSize(r4)
            boolean r4 = r1.mReversedFlow
            r5 = 1
            if (r4 != 0) goto L_0x003f
            androidx.leanback.widget.Grid$Provider r4 = r1.mProvider
            int r6 = r1.mFirstVisibleIndex
            androidx.leanback.widget.GridLayoutManager$2 r4 = (androidx.leanback.widget.GridLayoutManager.AnonymousClass2) r4
            int r4 = r4.getEdge(r6)
            int r4 = r4 + r0
            if (r4 > r7) goto L_0x004e
            goto L_0x004c
        L_0x003f:
            androidx.leanback.widget.Grid$Provider r4 = r1.mProvider
            int r6 = r1.mFirstVisibleIndex
            androidx.leanback.widget.GridLayoutManager$2 r4 = (androidx.leanback.widget.GridLayoutManager.AnonymousClass2) r4
            int r4 = r4.getEdge(r6)
            int r4 = r4 - r0
            if (r4 < r7) goto L_0x004e
        L_0x004c:
            r0 = r5
            goto L_0x004f
        L_0x004e:
            r0 = r3
        L_0x004f:
            if (r0 == 0) goto L_0x0060
            androidx.leanback.widget.Grid$Provider r0 = r1.mProvider
            int r4 = r1.mFirstVisibleIndex
            androidx.leanback.widget.GridLayoutManager$2 r0 = (androidx.leanback.widget.GridLayoutManager.AnonymousClass2) r0
            r0.removeItem(r4)
            int r0 = r1.mFirstVisibleIndex
            int r0 = r0 + r5
            r1.mFirstVisibleIndex = r0
            goto L_0x001c
        L_0x0060:
            int r7 = r1.mLastVisibleIndex
            int r0 = r1.mFirstVisibleIndex
            if (r7 >= r0) goto L_0x006b
            r7 = -1
            r1.mLastVisibleIndex = r7
            r1.mFirstVisibleIndex = r7
        L_0x006b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.removeInvisibleViewsAtFront():void");
    }

    public final void saveContext(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int i = this.mSaveContextLevel;
        if (i == 0) {
            this.mRecycler = recycler;
            this.mState = state;
            this.mPositionDeltaInPreLayout = 0;
            this.mExtraLayoutSpaceInPreLayout = 0;
        }
        this.mSaveContextLevel = i + 1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0030, code lost:
        if (r7 > r0) goto L_0x0058;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0056, code lost:
        if (r7 < r0) goto L_0x0058;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0058, code lost:
        r7 = r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int scrollDirectionPrimary(int r7) {
        /*
            Method dump skipped, instructions count: 238
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.scrollDirectionPrimary(int):int");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        boolean z;
        int i2;
        if ((this.mFlag & 512) != 0) {
            if (this.mGrid != null) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                saveContext(recycler, state);
                this.mFlag = (this.mFlag & (-4)) | 2;
                if (this.mOrientation == 0) {
                    i2 = scrollDirectionPrimary(i);
                } else {
                    i2 = scrollDirectionSecondary(i);
                }
                leaveContext();
                this.mFlag &= -4;
                return i2;
            }
        }
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        boolean z;
        int i2;
        int i3 = this.mFlag;
        if ((i3 & 512) != 0) {
            if (this.mGrid != null) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                this.mFlag = (i3 & (-4)) | 2;
                saveContext(recycler, state);
                if (this.mOrientation == 1) {
                    i2 = scrollDirectionPrimary(i);
                } else {
                    i2 = scrollDirectionSecondary(i);
                }
                leaveContext();
                this.mFlag &= -4;
                return i2;
            }
        }
        return 0;
    }

    public final void setOrientation(int i) {
        if (i == 0 || i == 1) {
            this.mOrientation = i;
            this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, i);
            WindowAlignment windowAlignment = this.mWindowAlignment;
            Objects.requireNonNull(windowAlignment);
            if (i == 0) {
                windowAlignment.mMainAxis = windowAlignment.horizontal;
                windowAlignment.mSecondAxis = windowAlignment.vertical;
            } else {
                windowAlignment.mMainAxis = windowAlignment.vertical;
                windowAlignment.mSecondAxis = windowAlignment.horizontal;
            }
            Objects.requireNonNull(this.mItemAlignment);
            this.mFlag |= 256;
        }
    }

    public final void setRowHeight(int i) {
        if (i >= 0 || i == -2) {
            this.mRowSizeSecondaryRequested = i;
            return;
        }
        throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("Invalid row height: ", i));
    }

    public final void setSelection(int i, boolean z) {
        if ((this.mFocusPosition != i && i != -1) || this.mSubFocusPosition != 0 || this.mPrimaryScrollExtra != 0) {
            scrollToSelection(i, z);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void startSmoothScroll(RecyclerView.SmoothScroller smoothScroller) {
        GridLinearSmoothScroller gridLinearSmoothScroller = this.mCurrentSmoothScroller;
        if (gridLinearSmoothScroller != null) {
            gridLinearSmoothScroller.mSkipOnStopInternal = true;
        }
        super.startSmoothScroll(smoothScroller);
        if (!smoothScroller.mRunning || !(smoothScroller instanceof GridLinearSmoothScroller)) {
            this.mCurrentSmoothScroller = null;
            this.mPendingMoveSmoothScroller = null;
            return;
        }
        GridLinearSmoothScroller gridLinearSmoothScroller2 = (GridLinearSmoothScroller) smoothScroller;
        this.mCurrentSmoothScroller = gridLinearSmoothScroller2;
        if (gridLinearSmoothScroller2 instanceof PendingMoveSmoothScroller) {
            this.mPendingMoveSmoothScroller = (PendingMoveSmoothScroller) gridLinearSmoothScroller2;
        } else {
            this.mPendingMoveSmoothScroller = null;
        }
    }

    public final void updateScrollLimits() {
        int i;
        int i2;
        int i3;
        int i4;
        boolean z;
        boolean z2;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        boolean z3;
        boolean z4;
        if (this.mState.getItemCount() != 0) {
            if ((this.mFlag & 262144) == 0) {
                Grid grid = this.mGrid;
                Objects.requireNonNull(grid);
                i4 = grid.mLastVisibleIndex;
                i3 = this.mState.getItemCount() - 1;
                Grid grid2 = this.mGrid;
                Objects.requireNonNull(grid2);
                i = grid2.mFirstVisibleIndex;
                i2 = 0;
            } else {
                Grid grid3 = this.mGrid;
                Objects.requireNonNull(grid3);
                i4 = grid3.mFirstVisibleIndex;
                Grid grid4 = this.mGrid;
                Objects.requireNonNull(grid4);
                i = grid4.mLastVisibleIndex;
                i2 = this.mState.getItemCount() - 1;
                i3 = 0;
            }
            if (i4 >= 0 && i >= 0) {
                if (i4 == i3) {
                    z = true;
                } else {
                    z = false;
                }
                if (i == i2) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                int i11 = Integer.MIN_VALUE;
                int i12 = Integer.MAX_VALUE;
                if (!z) {
                    WindowAlignment windowAlignment = this.mWindowAlignment;
                    Objects.requireNonNull(windowAlignment);
                    WindowAlignment.Axis axis = windowAlignment.mMainAxis;
                    Objects.requireNonNull(axis);
                    if (axis.mMaxEdge == Integer.MAX_VALUE) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (z3 && !z2) {
                        WindowAlignment windowAlignment2 = this.mWindowAlignment;
                        Objects.requireNonNull(windowAlignment2);
                        WindowAlignment.Axis axis2 = windowAlignment2.mMainAxis;
                        Objects.requireNonNull(axis2);
                        if (axis2.mMinEdge == Integer.MIN_VALUE) {
                            z4 = true;
                        } else {
                            z4 = false;
                        }
                        if (z4) {
                            return;
                        }
                    }
                }
                if (z) {
                    i12 = this.mGrid.findRowMax(true, sTwoInts);
                    View findViewByPosition = findViewByPosition(sTwoInts[1]);
                    if (this.mOrientation == 0) {
                        LayoutParams layoutParams = (LayoutParams) findViewByPosition.getLayoutParams();
                        Objects.requireNonNull(layoutParams);
                        i9 = findViewByPosition.getLeft() + layoutParams.mLeftInset;
                        i10 = layoutParams.mAlignX;
                    } else {
                        LayoutParams layoutParams2 = (LayoutParams) findViewByPosition.getLayoutParams();
                        Objects.requireNonNull(layoutParams2);
                        i9 = findViewByPosition.getTop() + layoutParams2.mTopInset;
                        i10 = layoutParams2.mAlignY;
                    }
                    int i13 = i10 + i9;
                    LayoutParams layoutParams3 = (LayoutParams) findViewByPosition.getLayoutParams();
                    Objects.requireNonNull(layoutParams3);
                    int[] iArr = layoutParams3.mAlignMultiple;
                    if (iArr == null || iArr.length <= 0) {
                        i5 = i13;
                    } else {
                        i5 = (iArr[iArr.length - 1] - iArr[0]) + i13;
                    }
                } else {
                    i5 = Integer.MAX_VALUE;
                }
                if (z2) {
                    i11 = this.mGrid.findRowMin(false, sTwoInts);
                    View findViewByPosition2 = findViewByPosition(sTwoInts[1]);
                    if (this.mOrientation == 0) {
                        LayoutParams layoutParams4 = (LayoutParams) findViewByPosition2.getLayoutParams();
                        Objects.requireNonNull(layoutParams4);
                        i8 = findViewByPosition2.getLeft() + layoutParams4.mLeftInset;
                        i7 = layoutParams4.mAlignX;
                    } else {
                        LayoutParams layoutParams5 = (LayoutParams) findViewByPosition2.getLayoutParams();
                        Objects.requireNonNull(layoutParams5);
                        i8 = findViewByPosition2.getTop() + layoutParams5.mTopInset;
                        i7 = layoutParams5.mAlignY;
                    }
                    i6 = i8 + i7;
                } else {
                    i6 = Integer.MIN_VALUE;
                }
                WindowAlignment windowAlignment3 = this.mWindowAlignment;
                Objects.requireNonNull(windowAlignment3);
                windowAlignment3.mMainAxis.updateMinMax(i11, i12, i6, i5);
            }
        }
    }

    public final void updateSecondaryScrollLimits() {
        WindowAlignment windowAlignment = this.mWindowAlignment;
        Objects.requireNonNull(windowAlignment);
        WindowAlignment.Axis axis = windowAlignment.mSecondAxis;
        Objects.requireNonNull(axis);
        int i = axis.mPaddingMin - this.mScrollOffsetSecondary;
        int sizeSecondary = getSizeSecondary() + i;
        axis.updateMinMax(i, sizeSecondary, i, sizeSecondary);
    }

    public static int getDecoratedMeasuredHeightWithMargin(View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        return RecyclerView.LayoutManager.getDecoratedMeasuredHeight(view) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
    }

    public static int getDecoratedMeasuredWidthWithMargin(View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        return RecyclerView.LayoutManager.getDecoratedMeasuredWidth(view) + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int getDecoratedBottom(View view) {
        return (((RecyclerView.LayoutParams) view.getLayoutParams()).mDecorInsets.bottom + view.getBottom()) - ((LayoutParams) view.getLayoutParams()).mBottomInset;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void getDecoratedBoundsWithMargins(View view, Rect rect) {
        super.getDecoratedBoundsWithMargins(view, rect);
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        rect.left += layoutParams.mLeftInset;
        rect.top += layoutParams.mTopInset;
        rect.right -= layoutParams.mRightInset;
        rect.bottom -= layoutParams.mBottomInset;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int getDecoratedLeft(View view) {
        return (view.getLeft() - ((RecyclerView.LayoutParams) view.getLayoutParams()).mDecorInsets.left) + ((LayoutParams) view.getLayoutParams()).mLeftInset;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int getDecoratedRight(View view) {
        return (((RecyclerView.LayoutParams) view.getLayoutParams()).mDecorInsets.right + view.getRight()) - ((LayoutParams) view.getLayoutParams()).mRightInset;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int getDecoratedTop(View view) {
        return (view.getTop() - ((RecyclerView.LayoutParams) view.getLayoutParams()).mDecorInsets.top) + ((LayoutParams) view.getLayoutParams()).mTopInset;
    }

    public final boolean hasCreatedFirstItem() {
        if (getItemCount() == 0 || this.mBaseGridView.findViewHolderForAdapterPosition(0) != null) {
            return true;
        }
        return false;
    }

    public final boolean hasCreatedLastItem() {
        int itemCount = getItemCount();
        if (itemCount == 0 || this.mBaseGridView.findViewHolderForAdapterPosition(itemCount - 1) != null) {
            return true;
        }
        return false;
    }

    public final void measureChild(View view) {
        int i;
        int i2;
        int i3;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        Rect rect = sTempRect;
        calculateItemDecorationsForChild(view, rect);
        int i4 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + rect.left + rect.right;
        int i5 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + rect.top + rect.bottom;
        if (this.mRowSizeSecondaryRequested == -2) {
            i = View.MeasureSpec.makeMeasureSpec(0, 0);
        } else {
            i = View.MeasureSpec.makeMeasureSpec(this.mFixedRowSizeSecondary, 1073741824);
        }
        if (this.mOrientation == 0) {
            i2 = ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(0, 0), i4, ((ViewGroup.MarginLayoutParams) layoutParams).width);
            i3 = ViewGroup.getChildMeasureSpec(i, i5, ((ViewGroup.MarginLayoutParams) layoutParams).height);
        } else {
            int childMeasureSpec = ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(0, 0), i5, ((ViewGroup.MarginLayoutParams) layoutParams).height);
            int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(i, i4, ((ViewGroup.MarginLayoutParams) layoutParams).width);
            i3 = childMeasureSpec;
            i2 = childMeasureSpec2;
        }
        view.measure(i2, i3);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler recycler, RecyclerView.State state, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        boolean z;
        AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat;
        AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat2;
        saveContext(recycler, state);
        int itemCount = state.getItemCount();
        int i = this.mFlag;
        if ((262144 & i) != 0) {
            z = true;
        } else {
            z = false;
        }
        if ((i & 2048) == 0 || (itemCount > 1 && !isItemFullyVisible(0))) {
            if (this.mOrientation == 0) {
                if (z) {
                    accessibilityActionCompat2 = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_RIGHT;
                } else {
                    accessibilityActionCompat2 = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_LEFT;
                }
                accessibilityNodeInfoCompat.addAction(accessibilityActionCompat2);
            } else {
                accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_UP);
            }
            accessibilityNodeInfoCompat.setScrollable(true);
        }
        if ((this.mFlag & 4096) == 0 || (itemCount > 1 && !isItemFullyVisible(itemCount - 1))) {
            if (this.mOrientation == 0) {
                if (z) {
                    accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_LEFT;
                } else {
                    accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_RIGHT;
                }
                accessibilityNodeInfoCompat.addAction(accessibilityActionCompat);
            } else {
                accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_DOWN);
            }
            accessibilityNodeInfoCompat.setScrollable(true);
        }
        accessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(recycler, state), getColumnCountForAccessibility(recycler, state), 0));
        leaveContext();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (this.mGrid != null && (layoutParams instanceof LayoutParams)) {
            LayoutParams layoutParams2 = (LayoutParams) layoutParams;
            Objects.requireNonNull(layoutParams2);
            int absoluteAdapterPosition = layoutParams2.mViewHolder.getAbsoluteAdapterPosition();
            int i = -1;
            if (absoluteAdapterPosition >= 0) {
                Grid grid = this.mGrid;
                Objects.requireNonNull(grid);
                Grid.Location location = grid.getLocation(absoluteAdapterPosition);
                if (location != null) {
                    i = location.row;
                }
            }
            if (i >= 0) {
                Grid grid2 = this.mGrid;
                Objects.requireNonNull(grid2);
                int i2 = absoluteAdapterPosition / grid2.mNumRows;
                if (this.mOrientation == 0) {
                    accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(i, 1, i2, 1, false));
                } else {
                    accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(i2, 1, i, 1, false));
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:55:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00ef  */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onMeasure(androidx.recyclerview.widget.RecyclerView.Recycler r7, androidx.recyclerview.widget.RecyclerView.State r8, int r9, int r10) {
        /*
            Method dump skipped, instructions count: 248
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.onMeasure(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State, int, int):void");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void removeAndRecycleAllViews(RecyclerView.Recycler recycler) {
        int childCount = getChildCount();
        while (true) {
            childCount--;
            if (childCount >= 0) {
                View childAt = getChildAt(childCount);
                removeViewAt(childCount);
                recycler.recycleView(childAt);
            } else {
                return;
            }
        }
    }

    public final void scrollToView(View view, View view2, boolean z, int i, int i2) {
        if ((this.mFlag & 64) == 0) {
            int adapterPositionByView = getAdapterPositionByView(view);
            int subPositionByView = getSubPositionByView(view, view2);
            if (!(adapterPositionByView == this.mFocusPosition && subPositionByView == this.mSubFocusPosition)) {
                this.mFocusPosition = adapterPositionByView;
                this.mSubFocusPosition = subPositionByView;
                this.mFocusPositionOffset = 0;
                if ((this.mFlag & 3) != 1) {
                    dispatchChildSelected();
                }
                if (this.mBaseGridView.isChildrenDrawingOrderEnabledInternal()) {
                    this.mBaseGridView.invalidate();
                }
            }
            if (view != null) {
                if (!view.hasFocus() && this.mBaseGridView.hasFocus()) {
                    view.requestFocus();
                }
                if ((this.mFlag & 131072) == 0 && z) {
                    return;
                }
                if (getScrollPosition(view, view2, sTwoInts) || i != 0 || i2 != 0) {
                    int[] iArr = sTwoInts;
                    int i3 = iArr[0] + i;
                    int i4 = iArr[1] + i2;
                    if ((this.mFlag & 3) == 1) {
                        scrollDirectionPrimary(i3);
                        scrollDirectionSecondary(i4);
                        return;
                    }
                    if (this.mOrientation != 0) {
                        i3 = i4;
                        i4 = i3;
                    }
                    if (z) {
                        BaseGridView baseGridView = this.mBaseGridView;
                        Objects.requireNonNull(baseGridView);
                        baseGridView.smoothScrollBy$1(i3, i4, false);
                        return;
                    }
                    this.mBaseGridView.scrollBy(i3, i4);
                    dispatchChildSelectedAndPositioned();
                }
            }
        }
    }

    public final void updatePositionDeltaInPreLayout() {
        if (getChildCount() > 0) {
            Grid grid = this.mGrid;
            Objects.requireNonNull(grid);
            this.mPositionDeltaInPreLayout = grid.mFirstVisibleIndex - ((LayoutParams) getChildAt(0).getLayoutParams()).getViewLayoutPosition();
            return;
        }
        this.mPositionDeltaInPreLayout = 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final boolean checkLayoutParams(RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }
}
