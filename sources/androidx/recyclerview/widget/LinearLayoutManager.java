package androidx.recyclerview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.recyclerview.widget.GapWorker;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class LinearLayoutManager extends RecyclerView.LayoutManager implements ItemTouchHelper.ViewDropHandler, RecyclerView.SmoothScroller.ScrollVectorProvider {
    public final AnchorInfo mAnchorInfo;
    public int mInitialPrefetchItemCount;
    public boolean mLastStackFromEnd;
    public final LayoutChunkResult mLayoutChunkResult;
    public LayoutState mLayoutState;
    public int mOrientation;
    public OrientationHelper mOrientationHelper;
    public SavedState mPendingSavedState;
    public int mPendingScrollPosition;
    public int mPendingScrollPositionOffset;
    public int[] mReusableIntPair;
    public boolean mReverseLayout;
    public boolean mShouldReverseLayout;
    public boolean mSmoothScrollbarEnabled;
    public boolean mStackFromEnd;

    /* loaded from: classes.dex */
    public static class AnchorInfo {
        public int mCoordinate;
        public boolean mLayoutFromEnd;
        public OrientationHelper mOrientationHelper;
        public int mPosition;
        public boolean mValid;

        public final void reset() {
            this.mPosition = -1;
            this.mCoordinate = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
            this.mValid = false;
        }

        public final void assignCoordinateFromPadding() {
            int i;
            if (this.mLayoutFromEnd) {
                i = this.mOrientationHelper.getEndAfterPadding();
            } else {
                i = this.mOrientationHelper.getStartAfterPadding();
            }
            this.mCoordinate = i;
        }

        public final void assignFromView(View view, int i) {
            if (this.mLayoutFromEnd) {
                this.mCoordinate = this.mOrientationHelper.getTotalSpaceChange() + this.mOrientationHelper.getDecoratedEnd(view);
            } else {
                this.mCoordinate = this.mOrientationHelper.getDecoratedStart(view);
            }
            this.mPosition = i;
        }

        public final void assignFromViewAndKeepVisibleRect(View view, int i) {
            int totalSpaceChange = this.mOrientationHelper.getTotalSpaceChange();
            if (totalSpaceChange >= 0) {
                assignFromView(view, i);
                return;
            }
            this.mPosition = i;
            if (this.mLayoutFromEnd) {
                int endAfterPadding = (this.mOrientationHelper.getEndAfterPadding() - totalSpaceChange) - this.mOrientationHelper.getDecoratedEnd(view);
                this.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - endAfterPadding;
                if (endAfterPadding > 0) {
                    int decoratedMeasurement = this.mCoordinate - this.mOrientationHelper.getDecoratedMeasurement(view);
                    int startAfterPadding = this.mOrientationHelper.getStartAfterPadding();
                    int min = decoratedMeasurement - (Math.min(this.mOrientationHelper.getDecoratedStart(view) - startAfterPadding, 0) + startAfterPadding);
                    if (min < 0) {
                        this.mCoordinate = Math.min(endAfterPadding, -min) + this.mCoordinate;
                        return;
                    }
                    return;
                }
                return;
            }
            int decoratedStart = this.mOrientationHelper.getDecoratedStart(view);
            int startAfterPadding2 = decoratedStart - this.mOrientationHelper.getStartAfterPadding();
            this.mCoordinate = decoratedStart;
            if (startAfterPadding2 > 0) {
                int endAfterPadding2 = (this.mOrientationHelper.getEndAfterPadding() - Math.min(0, (this.mOrientationHelper.getEndAfterPadding() - totalSpaceChange) - this.mOrientationHelper.getDecoratedEnd(view))) - (this.mOrientationHelper.getDecoratedMeasurement(view) + decoratedStart);
                if (endAfterPadding2 < 0) {
                    this.mCoordinate -= Math.min(startAfterPadding2, -endAfterPadding2);
                }
            }
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("AnchorInfo{mPosition=");
            m.append(this.mPosition);
            m.append(", mCoordinate=");
            m.append(this.mCoordinate);
            m.append(", mLayoutFromEnd=");
            m.append(this.mLayoutFromEnd);
            m.append(", mValid=");
            return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.mValid, '}');
        }

        public AnchorInfo() {
            reset();
        }
    }

    /* loaded from: classes.dex */
    public static class LayoutChunkResult {
        public int mConsumed;
        public boolean mFinished;
        public boolean mFocusable;
        public boolean mIgnoreConsumed;
    }

    /* loaded from: classes.dex */
    public static class LayoutState {
        public int mAvailable;
        public int mCurrentPosition;
        public boolean mInfinite;
        public int mItemDirection;
        public int mLastScrollDelta;
        public int mLayoutDirection;
        public int mOffset;
        public int mScrollingOffset;
        public boolean mRecycle = true;
        public int mExtraFillSpace = 0;
        public int mNoRecycleSpace = 0;
        public List<RecyclerView.ViewHolder> mScrapList = null;

        public final void assignPositionFromScrapList(View view) {
            int viewLayoutPosition;
            int size = this.mScrapList.size();
            View view2 = null;
            int i = Integer.MAX_VALUE;
            for (int i2 = 0; i2 < size; i2++) {
                View view3 = this.mScrapList.get(i2).itemView;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view3.getLayoutParams();
                if (view3 != view && !layoutParams.isItemRemoved() && (viewLayoutPosition = (layoutParams.getViewLayoutPosition() - this.mCurrentPosition) * this.mItemDirection) >= 0 && viewLayoutPosition < i) {
                    view2 = view3;
                    if (viewLayoutPosition == 0) {
                        break;
                    }
                    i = viewLayoutPosition;
                }
            }
            if (view2 == null) {
                this.mCurrentPosition = -1;
            } else {
                this.mCurrentPosition = ((RecyclerView.LayoutParams) view2.getLayoutParams()).getViewLayoutPosition();
            }
        }

        public final View next(RecyclerView.Recycler recycler) {
            List<RecyclerView.ViewHolder> list = this.mScrapList;
            if (list != null) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    View view = this.mScrapList.get(i).itemView;
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                    if (!layoutParams.isItemRemoved() && this.mCurrentPosition == layoutParams.getViewLayoutPosition()) {
                        assignPositionFromScrapList(view);
                        return view;
                    }
                }
                return null;
            }
            View viewForPosition = recycler.getViewForPosition(this.mCurrentPosition);
            this.mCurrentPosition += this.mItemDirection;
            return viewForPosition;
        }
    }

    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: classes.dex */
    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.recyclerview.widget.LinearLayoutManager.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public boolean mAnchorLayoutFromEnd;
        public int mAnchorOffset;
        public int mAnchorPosition;

        public SavedState() {
        }

        @Override // android.os.Parcelable
        public final int describeContents() {
            return 0;
        }

        public SavedState(Parcel parcel) {
            this.mAnchorPosition = parcel.readInt();
            this.mAnchorOffset = parcel.readInt();
            this.mAnchorLayoutFromEnd = parcel.readInt() != 1 ? false : true;
        }

        @Override // android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.mAnchorPosition);
            parcel.writeInt(this.mAnchorOffset);
            parcel.writeInt(this.mAnchorLayoutFromEnd ? 1 : 0);
        }

        public SavedState(SavedState savedState) {
            this.mAnchorPosition = savedState.mAnchorPosition;
            this.mAnchorOffset = savedState.mAnchorOffset;
            this.mAnchorLayoutFromEnd = savedState.mAnchorLayoutFromEnd;
        }
    }

    public LinearLayoutManager() {
        this(1);
    }

    public final int convertFocusDirectionToLayoutDirection(int i) {
        if (i == 1) {
            return (this.mOrientation != 1 && isLayoutRTL()) ? 1 : -1;
        }
        if (i == 2) {
            return (this.mOrientation != 1 && isLayoutRTL()) ? -1 : 1;
        }
        if (i == 17) {
            return this.mOrientation == 0 ? -1 : Integer.MIN_VALUE;
        }
        if (i == 33) {
            return this.mOrientation == 1 ? -1 : Integer.MIN_VALUE;
        }
        if (i != 66) {
            if (i == 130 && this.mOrientation == 1) {
                return 1;
            }
            return Integer.MIN_VALUE;
        } else if (this.mOrientation == 0) {
            return 1;
        } else {
            return Integer.MIN_VALUE;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final boolean isAutoMeasureEnabled() {
        return true;
    }

    public void onAnchorReady(RecyclerView.Recycler recycler, RecyclerView.State state, AnchorInfo anchorInfo, int i) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onDetachedFromWindow(RecyclerView recyclerView) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutCompleted(RecyclerView.State state) {
        this.mPendingSavedState = null;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mAnchorInfo.reset();
    }

    public void setStackFromEnd(boolean z) {
        assertNotInLayoutOrScroll(null);
        if (this.mStackFromEnd != z) {
            this.mStackFromEnd = z;
            requestLayout();
        }
    }

    public LinearLayoutManager(int i) {
        this.mOrientation = 1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mStackFromEnd = false;
        this.mSmoothScrollbarEnabled = true;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
        this.mAnchorInfo = new AnchorInfo();
        this.mLayoutChunkResult = new LayoutChunkResult();
        this.mInitialPrefetchItemCount = 2;
        this.mReusableIntPair = new int[2];
        setOrientation(i);
        assertNotInLayoutOrScroll(null);
        if (this.mReverseLayout) {
            this.mReverseLayout = false;
            requestLayout();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void assertNotInLayoutOrScroll(String str) {
        if (this.mPendingSavedState == null) {
            super.assertNotInLayoutOrScroll(str);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final boolean canScrollHorizontally() {
        if (this.mOrientation == 0) {
            return true;
        }
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final boolean canScrollVertically() {
        if (this.mOrientation == 1) {
            return true;
        }
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void collectAdjacentPrefetchPositions(int i, int i2, RecyclerView.State state, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int i3;
        if (this.mOrientation != 0) {
            i = i2;
        }
        if (getChildCount() != 0 && i != 0) {
            ensureLayoutState();
            if (i > 0) {
                i3 = 1;
            } else {
                i3 = -1;
            }
            updateLayoutState(i3, Math.abs(i), true, state);
            collectPrefetchPositionsForLayoutState(state, this.mLayoutState, layoutPrefetchRegistry);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0025  */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void collectInitialPrefetchPositions(int r7, androidx.recyclerview.widget.RecyclerView.LayoutManager.LayoutPrefetchRegistry r8) {
        /*
            r6 = this;
            androidx.recyclerview.widget.LinearLayoutManager$SavedState r0 = r6.mPendingSavedState
            r1 = 0
            r2 = -1
            r3 = 1
            if (r0 == 0) goto L_0x0013
            int r4 = r0.mAnchorPosition
            if (r4 < 0) goto L_0x000d
            r5 = r3
            goto L_0x000e
        L_0x000d:
            r5 = r1
        L_0x000e:
            if (r5 == 0) goto L_0x0013
            boolean r0 = r0.mAnchorLayoutFromEnd
            goto L_0x0022
        L_0x0013:
            r6.resolveShouldLayoutReverse()
            boolean r0 = r6.mShouldReverseLayout
            int r4 = r6.mPendingScrollPosition
            if (r4 != r2) goto L_0x0022
            if (r0 == 0) goto L_0x0021
            int r4 = r7 + (-1)
            goto L_0x0022
        L_0x0021:
            r4 = r1
        L_0x0022:
            if (r0 == 0) goto L_0x0025
            goto L_0x0026
        L_0x0025:
            r2 = r3
        L_0x0026:
            r0 = r1
        L_0x0027:
            int r3 = r6.mInitialPrefetchItemCount
            if (r0 >= r3) goto L_0x0039
            if (r4 < 0) goto L_0x0039
            if (r4 >= r7) goto L_0x0039
            r3 = r8
            androidx.recyclerview.widget.GapWorker$LayoutPrefetchRegistryImpl r3 = (androidx.recyclerview.widget.GapWorker.LayoutPrefetchRegistryImpl) r3
            r3.addPosition(r4, r1)
            int r4 = r4 + r2
            int r0 = r0 + 1
            goto L_0x0027
        L_0x0039:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.LinearLayoutManager.collectInitialPrefetchPositions(int, androidx.recyclerview.widget.RecyclerView$LayoutManager$LayoutPrefetchRegistry):void");
    }

    public void collectPrefetchPositionsForLayoutState(RecyclerView.State state, LayoutState layoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int i = layoutState.mCurrentPosition;
        if (i >= 0 && i < state.getItemCount()) {
            ((GapWorker.LayoutPrefetchRegistryImpl) layoutPrefetchRegistry).addPosition(i, Math.max(0, layoutState.mScrollingOffset));
        }
    }

    public final void ensureLayoutState() {
        if (this.mLayoutState == null) {
            this.mLayoutState = new LayoutState();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0057, code lost:
        if (r10.mInPreLayout == false) goto L_0x0059;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int fill(androidx.recyclerview.widget.RecyclerView.Recycler r8, androidx.recyclerview.widget.LinearLayoutManager.LayoutState r9, androidx.recyclerview.widget.RecyclerView.State r10, boolean r11) {
        /*
            r7 = this;
            int r0 = r9.mAvailable
            int r1 = r9.mScrollingOffset
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r1 == r2) goto L_0x0010
            if (r0 >= 0) goto L_0x000d
            int r1 = r1 + r0
            r9.mScrollingOffset = r1
        L_0x000d:
            r7.recycleByLayoutState(r8, r9)
        L_0x0010:
            int r1 = r9.mAvailable
            int r3 = r9.mExtraFillSpace
            int r1 = r1 + r3
            androidx.recyclerview.widget.LinearLayoutManager$LayoutChunkResult r3 = r7.mLayoutChunkResult
        L_0x0017:
            boolean r4 = r9.mInfinite
            if (r4 != 0) goto L_0x001d
            if (r1 <= 0) goto L_0x007a
        L_0x001d:
            int r4 = r9.mCurrentPosition
            r5 = 0
            if (r4 < 0) goto L_0x002a
            int r6 = r10.getItemCount()
            if (r4 >= r6) goto L_0x002a
            r4 = 1
            goto L_0x002b
        L_0x002a:
            r4 = r5
        L_0x002b:
            if (r4 == 0) goto L_0x007a
            java.util.Objects.requireNonNull(r3)
            r3.mConsumed = r5
            r3.mFinished = r5
            r3.mIgnoreConsumed = r5
            r3.mFocusable = r5
            r7.layoutChunk(r8, r10, r9, r3)
            boolean r4 = r3.mFinished
            if (r4 == 0) goto L_0x0040
            goto L_0x007a
        L_0x0040:
            int r4 = r9.mOffset
            int r5 = r3.mConsumed
            int r6 = r9.mLayoutDirection
            int r5 = r5 * r6
            int r5 = r5 + r4
            r9.mOffset = r5
            boolean r4 = r3.mIgnoreConsumed
            if (r4 == 0) goto L_0x0059
            java.util.List<androidx.recyclerview.widget.RecyclerView$ViewHolder> r4 = r9.mScrapList
            if (r4 != 0) goto L_0x0059
            java.util.Objects.requireNonNull(r10)
            boolean r4 = r10.mInPreLayout
            if (r4 != 0) goto L_0x0061
        L_0x0059:
            int r4 = r9.mAvailable
            int r5 = r3.mConsumed
            int r4 = r4 - r5
            r9.mAvailable = r4
            int r1 = r1 - r5
        L_0x0061:
            int r4 = r9.mScrollingOffset
            if (r4 == r2) goto L_0x0074
            int r5 = r3.mConsumed
            int r4 = r4 + r5
            r9.mScrollingOffset = r4
            int r5 = r9.mAvailable
            if (r5 >= 0) goto L_0x0071
            int r4 = r4 + r5
            r9.mScrollingOffset = r4
        L_0x0071:
            r7.recycleByLayoutState(r8, r9)
        L_0x0074:
            if (r11 == 0) goto L_0x0017
            boolean r4 = r3.mFocusable
            if (r4 == 0) goto L_0x0017
        L_0x007a:
            int r7 = r9.mAvailable
            int r0 = r0 - r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.LinearLayoutManager.fill(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.LinearLayoutManager$LayoutState, androidx.recyclerview.widget.RecyclerView$State, boolean):int");
    }

    public final View findFirstVisibleChildClosestToEnd(boolean z) {
        if (this.mShouldReverseLayout) {
            return findOneVisibleChild(0, getChildCount(), z);
        }
        return findOneVisibleChild(getChildCount() - 1, -1, z);
    }

    public final View findFirstVisibleChildClosestToStart(boolean z) {
        if (this.mShouldReverseLayout) {
            return findOneVisibleChild(getChildCount() - 1, -1, z);
        }
        return findOneVisibleChild(0, getChildCount(), z);
    }

    public View findReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state, boolean z, boolean z2) {
        int i;
        int i2;
        boolean z3;
        boolean z4;
        ensureLayoutState();
        int childCount = getChildCount();
        int i3 = -1;
        if (z2) {
            i2 = getChildCount() - 1;
            i = -1;
        } else {
            i3 = childCount;
            i2 = 0;
            i = 1;
        }
        int itemCount = state.getItemCount();
        int startAfterPadding = this.mOrientationHelper.getStartAfterPadding();
        int endAfterPadding = this.mOrientationHelper.getEndAfterPadding();
        View view = null;
        View view2 = null;
        View view3 = null;
        while (i2 != i3) {
            View childAt = getChildAt(i2);
            int position = RecyclerView.LayoutManager.getPosition(childAt);
            int decoratedStart = this.mOrientationHelper.getDecoratedStart(childAt);
            int decoratedEnd = this.mOrientationHelper.getDecoratedEnd(childAt);
            if (position >= 0 && position < itemCount) {
                if (!((RecyclerView.LayoutParams) childAt.getLayoutParams()).isItemRemoved()) {
                    if (decoratedEnd > startAfterPadding || decoratedStart >= startAfterPadding) {
                        z3 = false;
                    } else {
                        z3 = true;
                    }
                    if (decoratedStart < endAfterPadding || decoratedEnd <= endAfterPadding) {
                        z4 = false;
                    } else {
                        z4 = true;
                    }
                    if (!z3 && !z4) {
                        return childAt;
                    }
                    if (z) {
                        if (!z4) {
                            if (view != null) {
                            }
                            view = childAt;
                        }
                        view2 = childAt;
                    } else {
                        if (!z3) {
                            if (view != null) {
                            }
                            view = childAt;
                        }
                        view2 = childAt;
                    }
                } else if (view3 == null) {
                    view3 = childAt;
                }
            }
            i2 += i;
        }
        if (view != null) {
            return view;
        }
        if (view2 != null) {
            return view2;
        }
        return view3;
    }

    public final int fixLayoutEndGap(int i, RecyclerView.Recycler recycler, RecyclerView.State state, boolean z) {
        int endAfterPadding;
        int endAfterPadding2 = this.mOrientationHelper.getEndAfterPadding() - i;
        if (endAfterPadding2 <= 0) {
            return 0;
        }
        int i2 = -scrollBy(-endAfterPadding2, recycler, state);
        int i3 = i + i2;
        if (!z || (endAfterPadding = this.mOrientationHelper.getEndAfterPadding() - i3) <= 0) {
            return i2;
        }
        this.mOrientationHelper.offsetChildren(endAfterPadding);
        return endAfterPadding + i2;
    }

    public final int fixLayoutStartGap(int i, RecyclerView.Recycler recycler, RecyclerView.State state, boolean z) {
        int startAfterPadding;
        int startAfterPadding2 = i - this.mOrientationHelper.getStartAfterPadding();
        if (startAfterPadding2 <= 0) {
            return 0;
        }
        int i2 = -scrollBy(startAfterPadding2, recycler, state);
        int i3 = i + i2;
        if (!z || (startAfterPadding = i3 - this.mOrientationHelper.getStartAfterPadding()) <= 0) {
            return i2;
        }
        this.mOrientationHelper.offsetChildren(-startAfterPadding);
        return i2 - startAfterPadding;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-2, -2);
    }

    public final View getChildClosestToEnd() {
        int i;
        if (this.mShouldReverseLayout) {
            i = 0;
        } else {
            i = getChildCount() - 1;
        }
        return getChildAt(i);
    }

    public final View getChildClosestToStart() {
        int i;
        if (this.mShouldReverseLayout) {
            i = getChildCount() - 1;
        } else {
            i = 0;
        }
        return getChildAt(i);
    }

    /* JADX WARN: Removed duplicated region for block: B:139:0x0231  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0192  */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onLayoutChildren(androidx.recyclerview.widget.RecyclerView.Recycler r18, androidx.recyclerview.widget.RecyclerView.State r19) {
        /*
            Method dump skipped, instructions count: 1122
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.LinearLayoutManager.onLayoutChildren(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State):void");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            this.mPendingSavedState = savedState;
            if (this.mPendingScrollPosition != -1) {
                Objects.requireNonNull(savedState);
                savedState.mAnchorPosition = -1;
            }
            requestLayout();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final Parcelable onSaveInstanceState() {
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null) {
            return new SavedState(savedState);
        }
        SavedState savedState2 = new SavedState();
        if (getChildCount() > 0) {
            ensureLayoutState();
            boolean z = this.mLastStackFromEnd ^ this.mShouldReverseLayout;
            savedState2.mAnchorLayoutFromEnd = z;
            if (z) {
                View childClosestToEnd = getChildClosestToEnd();
                savedState2.mAnchorOffset = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(childClosestToEnd);
                savedState2.mAnchorPosition = RecyclerView.LayoutManager.getPosition(childClosestToEnd);
            } else {
                View childClosestToStart = getChildClosestToStart();
                savedState2.mAnchorPosition = RecyclerView.LayoutManager.getPosition(childClosestToStart);
                savedState2.mAnchorOffset = this.mOrientationHelper.getDecoratedStart(childClosestToStart) - this.mOrientationHelper.getStartAfterPadding();
            }
        } else {
            savedState2.mAnchorPosition = -1;
        }
        return savedState2;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.ViewDropHandler
    public final void prepareForDrop(View view, View view2) {
        boolean z;
        assertNotInLayoutOrScroll("Cannot drop a view during a scroll or layout calculation");
        ensureLayoutState();
        resolveShouldLayoutReverse();
        int position = RecyclerView.LayoutManager.getPosition(view);
        int position2 = RecyclerView.LayoutManager.getPosition(view2);
        if (position < position2) {
            z = true;
        } else {
            z = true;
        }
        if (this.mShouldReverseLayout) {
            if (z) {
                scrollToPositionWithOffset(position2, this.mOrientationHelper.getEndAfterPadding() - (this.mOrientationHelper.getDecoratedMeasurement(view) + this.mOrientationHelper.getDecoratedStart(view2)));
                return;
            }
            scrollToPositionWithOffset(position2, this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view2));
        } else if (z) {
            scrollToPositionWithOffset(position2, this.mOrientationHelper.getDecoratedStart(view2));
        } else {
            scrollToPositionWithOffset(position2, this.mOrientationHelper.getDecoratedEnd(view2) - this.mOrientationHelper.getDecoratedMeasurement(view));
        }
    }

    public final void recycleByLayoutState(RecyclerView.Recycler recycler, LayoutState layoutState) {
        if (layoutState.mRecycle && !layoutState.mInfinite) {
            int i = layoutState.mScrollingOffset;
            int i2 = layoutState.mNoRecycleSpace;
            if (layoutState.mLayoutDirection == -1) {
                int childCount = getChildCount();
                if (i >= 0) {
                    int end = (this.mOrientationHelper.getEnd() - i) + i2;
                    if (this.mShouldReverseLayout) {
                        for (int i3 = 0; i3 < childCount; i3++) {
                            View childAt = getChildAt(i3);
                            if (this.mOrientationHelper.getDecoratedStart(childAt) < end || this.mOrientationHelper.getTransformedStartWithDecoration(childAt) < end) {
                                recycleChildren(recycler, 0, i3);
                                return;
                            }
                        }
                        return;
                    }
                    int i4 = childCount - 1;
                    for (int i5 = i4; i5 >= 0; i5--) {
                        View childAt2 = getChildAt(i5);
                        if (this.mOrientationHelper.getDecoratedStart(childAt2) < end || this.mOrientationHelper.getTransformedStartWithDecoration(childAt2) < end) {
                            recycleChildren(recycler, i4, i5);
                            return;
                        }
                    }
                }
            } else if (i >= 0) {
                int i6 = i - i2;
                int childCount2 = getChildCount();
                if (this.mShouldReverseLayout) {
                    int i7 = childCount2 - 1;
                    for (int i8 = i7; i8 >= 0; i8--) {
                        View childAt3 = getChildAt(i8);
                        if (this.mOrientationHelper.getDecoratedEnd(childAt3) > i6 || this.mOrientationHelper.getTransformedEndWithDecoration(childAt3) > i6) {
                            recycleChildren(recycler, i7, i8);
                            return;
                        }
                    }
                    return;
                }
                for (int i9 = 0; i9 < childCount2; i9++) {
                    View childAt4 = getChildAt(i9);
                    if (this.mOrientationHelper.getDecoratedEnd(childAt4) > i6 || this.mOrientationHelper.getTransformedEndWithDecoration(childAt4) > i6) {
                        recycleChildren(recycler, 0, i9);
                        return;
                    }
                }
            }
        }
    }

    public final void recycleChildren(RecyclerView.Recycler recycler, int i, int i2) {
        if (i != i2) {
            if (i2 > i) {
                while (true) {
                    i2--;
                    if (i2 >= i) {
                        View childAt = getChildAt(i2);
                        removeViewAt(i2);
                        recycler.recycleView(childAt);
                    } else {
                        return;
                    }
                }
            } else {
                while (i > i2) {
                    View childAt2 = getChildAt(i);
                    removeViewAt(i);
                    recycler.recycleView(childAt2);
                    i--;
                }
            }
        }
    }

    public final void resolveShouldLayoutReverse() {
        if (this.mOrientation == 1 || !isLayoutRTL()) {
            this.mShouldReverseLayout = this.mReverseLayout;
        } else {
            this.mShouldReverseLayout = !this.mReverseLayout;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 1) {
            return 0;
        }
        return scrollBy(i, recycler, state);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void scrollToPosition(int i) {
        this.mPendingScrollPosition = i;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null) {
            savedState.mAnchorPosition = -1;
        }
        requestLayout();
    }

    public final void scrollToPositionWithOffset(int i, int i2) {
        this.mPendingScrollPosition = i;
        this.mPendingScrollPositionOffset = i2;
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null) {
            Objects.requireNonNull(savedState);
            savedState.mAnchorPosition = -1;
        }
        requestLayout();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 0) {
            return 0;
        }
        return scrollBy(i, recycler, state);
    }

    public final void setOrientation(int i) {
        if (i == 0 || i == 1) {
            assertNotInLayoutOrScroll(null);
            if (i != this.mOrientation || this.mOrientationHelper == null) {
                OrientationHelper createOrientationHelper = OrientationHelper.createOrientationHelper(this, i);
                this.mOrientationHelper = createOrientationHelper;
                this.mAnchorInfo.mOrientationHelper = createOrientationHelper;
                this.mOrientation = i;
                requestLayout();
                return;
            }
            return;
        }
        throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("invalid orientation:", i));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final boolean shouldMeasureTwice() {
        boolean z;
        if (!(this.mHeightMode == 1073741824 || this.mWidthMode == 1073741824)) {
            int childCount = getChildCount();
            int i = 0;
            while (true) {
                if (i >= childCount) {
                    z = false;
                    break;
                }
                ViewGroup.LayoutParams layoutParams = getChildAt(i).getLayoutParams();
                if (layoutParams.width < 0 && layoutParams.height < 0) {
                    z = true;
                    break;
                }
                i++;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void smoothScrollToPosition(RecyclerView recyclerView, int i) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.mTargetPosition = i;
        startSmoothScroll(linearSmoothScroller);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean supportsPredictiveItemAnimations() {
        if (this.mPendingSavedState == null && this.mLastStackFromEnd == this.mStackFromEnd) {
            return true;
        }
        return false;
    }

    public final void updateLayoutState(int i, int i2, boolean z, RecyclerView.State state) {
        boolean z2;
        int i3;
        int i4;
        LayoutState layoutState = this.mLayoutState;
        int i5 = 1;
        boolean z3 = false;
        if (this.mOrientationHelper.getMode() == 0 && this.mOrientationHelper.getEnd() == 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        layoutState.mInfinite = z2;
        this.mLayoutState.mLayoutDirection = i;
        int[] iArr = this.mReusableIntPair;
        iArr[0] = 0;
        iArr[1] = 0;
        calculateExtraLayoutSpace(state, iArr);
        int max = Math.max(0, this.mReusableIntPair[0]);
        int max2 = Math.max(0, this.mReusableIntPair[1]);
        if (i == 1) {
            z3 = true;
        }
        LayoutState layoutState2 = this.mLayoutState;
        if (z3) {
            i3 = max2;
        } else {
            i3 = max;
        }
        layoutState2.mExtraFillSpace = i3;
        if (!z3) {
            max = max2;
        }
        layoutState2.mNoRecycleSpace = max;
        if (z3) {
            layoutState2.mExtraFillSpace = this.mOrientationHelper.getEndPadding() + i3;
            View childClosestToEnd = getChildClosestToEnd();
            LayoutState layoutState3 = this.mLayoutState;
            if (this.mShouldReverseLayout) {
                i5 = -1;
            }
            layoutState3.mItemDirection = i5;
            int position = RecyclerView.LayoutManager.getPosition(childClosestToEnd);
            LayoutState layoutState4 = this.mLayoutState;
            layoutState3.mCurrentPosition = position + layoutState4.mItemDirection;
            layoutState4.mOffset = this.mOrientationHelper.getDecoratedEnd(childClosestToEnd);
            i4 = this.mOrientationHelper.getDecoratedEnd(childClosestToEnd) - this.mOrientationHelper.getEndAfterPadding();
        } else {
            View childClosestToStart = getChildClosestToStart();
            LayoutState layoutState5 = this.mLayoutState;
            layoutState5.mExtraFillSpace = this.mOrientationHelper.getStartAfterPadding() + layoutState5.mExtraFillSpace;
            LayoutState layoutState6 = this.mLayoutState;
            if (!this.mShouldReverseLayout) {
                i5 = -1;
            }
            layoutState6.mItemDirection = i5;
            int position2 = RecyclerView.LayoutManager.getPosition(childClosestToStart);
            LayoutState layoutState7 = this.mLayoutState;
            layoutState6.mCurrentPosition = position2 + layoutState7.mItemDirection;
            layoutState7.mOffset = this.mOrientationHelper.getDecoratedStart(childClosestToStart);
            i4 = (-this.mOrientationHelper.getDecoratedStart(childClosestToStart)) + this.mOrientationHelper.getStartAfterPadding();
        }
        LayoutState layoutState8 = this.mLayoutState;
        layoutState8.mAvailable = i2;
        if (z) {
            layoutState8.mAvailable = i2 - i4;
        }
        layoutState8.mScrollingOffset = i4;
    }

    public final void updateLayoutStateToFillEnd(int i, int i2) {
        int i3;
        this.mLayoutState.mAvailable = this.mOrientationHelper.getEndAfterPadding() - i2;
        LayoutState layoutState = this.mLayoutState;
        if (this.mShouldReverseLayout) {
            i3 = -1;
        } else {
            i3 = 1;
        }
        layoutState.mItemDirection = i3;
        layoutState.mCurrentPosition = i;
        layoutState.mLayoutDirection = 1;
        layoutState.mOffset = i2;
        layoutState.mScrollingOffset = Integer.MIN_VALUE;
    }

    public final void updateLayoutStateToFillStart(int i, int i2) {
        int i3;
        this.mLayoutState.mAvailable = i2 - this.mOrientationHelper.getStartAfterPadding();
        LayoutState layoutState = this.mLayoutState;
        layoutState.mCurrentPosition = i;
        if (this.mShouldReverseLayout) {
            i3 = 1;
        } else {
            i3 = -1;
        }
        layoutState.mItemDirection = i3;
        layoutState.mLayoutDirection = -1;
        layoutState.mOffset = i2;
        layoutState.mScrollingOffset = Integer.MIN_VALUE;
    }

    public void calculateExtraLayoutSpace(RecyclerView.State state, int[] iArr) {
        boolean z;
        int i;
        int i2;
        Objects.requireNonNull(state);
        if (state.mTargetPosition != -1) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            i = this.mOrientationHelper.getTotalSpace();
        } else {
            i = 0;
        }
        if (this.mLayoutState.mLayoutDirection == -1) {
            i2 = 0;
        } else {
            i2 = i;
            i = 0;
        }
        iArr[0] = i;
        iArr[1] = i2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int computeHorizontalScrollExtent(RecyclerView.State state) {
        return computeScrollExtent(state);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeHorizontalScrollOffset(RecyclerView.State state) {
        return computeScrollOffset(state);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeHorizontalScrollRange(RecyclerView.State state) {
        return computeScrollRange(state);
    }

    public final int computeScrollExtent(RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        ensureLayoutState();
        return ScrollbarHelper.computeScrollExtent(state, this.mOrientationHelper, findFirstVisibleChildClosestToStart(!this.mSmoothScrollbarEnabled), findFirstVisibleChildClosestToEnd(!this.mSmoothScrollbarEnabled), this, this.mSmoothScrollbarEnabled);
    }

    public final int computeScrollOffset(RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        ensureLayoutState();
        return ScrollbarHelper.computeScrollOffset(state, this.mOrientationHelper, findFirstVisibleChildClosestToStart(!this.mSmoothScrollbarEnabled), findFirstVisibleChildClosestToEnd(!this.mSmoothScrollbarEnabled), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    }

    public final int computeScrollRange(RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        ensureLayoutState();
        return ScrollbarHelper.computeScrollRange(state, this.mOrientationHelper, findFirstVisibleChildClosestToStart(!this.mSmoothScrollbarEnabled), findFirstVisibleChildClosestToEnd(!this.mSmoothScrollbarEnabled), this, this.mSmoothScrollbarEnabled);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller.ScrollVectorProvider
    public final PointF computeScrollVectorForPosition(int i) {
        if (getChildCount() == 0) {
            return null;
        }
        boolean z = false;
        int i2 = 1;
        if (i < RecyclerView.LayoutManager.getPosition(getChildAt(0))) {
            z = true;
        }
        if (z != this.mShouldReverseLayout) {
            i2 = -1;
        }
        if (this.mOrientation == 0) {
            return new PointF(i2, 0.0f);
        }
        return new PointF(0.0f, i2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int computeVerticalScrollExtent(RecyclerView.State state) {
        return computeScrollExtent(state);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        return computeScrollOffset(state);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeVerticalScrollRange(RecyclerView.State state) {
        return computeScrollRange(state);
    }

    public final int findFirstVisibleItemPosition() {
        View findOneVisibleChild = findOneVisibleChild(0, getChildCount(), false);
        if (findOneVisibleChild == null) {
            return -1;
        }
        return RecyclerView.LayoutManager.getPosition(findOneVisibleChild);
    }

    public final int findLastVisibleItemPosition() {
        View findOneVisibleChild = findOneVisibleChild(getChildCount() - 1, -1, false);
        if (findOneVisibleChild == null) {
            return -1;
        }
        return RecyclerView.LayoutManager.getPosition(findOneVisibleChild);
    }

    public final View findOnePartiallyOrCompletelyInvisibleChild(int i, int i2) {
        char c;
        int i3;
        int i4;
        ensureLayoutState();
        if (i2 > i) {
            c = 1;
        } else if (i2 < i) {
            c = 65535;
        } else {
            c = 0;
        }
        if (c == 0) {
            return getChildAt(i);
        }
        if (this.mOrientationHelper.getDecoratedStart(getChildAt(i)) < this.mOrientationHelper.getStartAfterPadding()) {
            i4 = 16644;
            i3 = 16388;
        } else {
            i4 = 4161;
            i3 = 4097;
        }
        if (this.mOrientation == 0) {
            return this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(i, i2, i4, i3);
        }
        return this.mVerticalBoundCheck.findOneViewWithinBoundFlags(i, i2, i4, i3);
    }

    public final View findOneVisibleChild(int i, int i2, boolean z) {
        int i3;
        ensureLayoutState();
        if (z) {
            i3 = 24579;
        } else {
            i3 = 320;
        }
        if (this.mOrientation == 0) {
            return this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(i, i2, i3, 320);
        }
        return this.mVerticalBoundCheck.findOneViewWithinBoundFlags(i, i2, i3, 320);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final View findViewByPosition(int i) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return null;
        }
        int position = i - RecyclerView.LayoutManager.getPosition(getChildAt(0));
        if (position >= 0 && position < childCount) {
            View childAt = getChildAt(position);
            if (RecyclerView.LayoutManager.getPosition(childAt) == i) {
                return childAt;
            }
        }
        return super.findViewByPosition(i);
    }

    public final boolean isLayoutRTL() {
        if (getLayoutDirection() == 1) {
            return true;
        }
        return false;
    }

    public void layoutChunk(RecyclerView.Recycler recycler, RecyclerView.State state, LayoutState layoutState, LayoutChunkResult layoutChunkResult) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        boolean z;
        boolean z2;
        View next = layoutState.next(recycler);
        if (next == null) {
            layoutChunkResult.mFinished = true;
            return;
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) next.getLayoutParams();
        if (layoutState.mScrapList == null) {
            boolean z3 = this.mShouldReverseLayout;
            if (layoutState.mLayoutDirection == -1) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z3 == z2) {
                addViewInt(next, -1, false);
            } else {
                addViewInt(next, 0, false);
            }
        } else {
            boolean z4 = this.mShouldReverseLayout;
            if (layoutState.mLayoutDirection == -1) {
                z = true;
            } else {
                z = false;
            }
            if (z4 == z) {
                addViewInt(next, -1, true);
            } else {
                addViewInt(next, 0, true);
            }
        }
        RecyclerView.LayoutParams layoutParams2 = (RecyclerView.LayoutParams) next.getLayoutParams();
        Rect itemDecorInsetsForChild = this.mRecyclerView.getItemDecorInsetsForChild(next);
        int childMeasureSpec = RecyclerView.LayoutManager.getChildMeasureSpec(this.mWidth, this.mWidthMode, getPaddingRight() + getPaddingLeft() + ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin + itemDecorInsetsForChild.left + itemDecorInsetsForChild.right + 0, ((ViewGroup.MarginLayoutParams) layoutParams2).width, canScrollHorizontally());
        int childMeasureSpec2 = RecyclerView.LayoutManager.getChildMeasureSpec(this.mHeight, this.mHeightMode, getPaddingBottom() + getPaddingTop() + ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin + itemDecorInsetsForChild.top + itemDecorInsetsForChild.bottom + 0, ((ViewGroup.MarginLayoutParams) layoutParams2).height, canScrollVertically());
        if (shouldMeasureChild(next, childMeasureSpec, childMeasureSpec2, layoutParams2)) {
            next.measure(childMeasureSpec, childMeasureSpec2);
        }
        layoutChunkResult.mConsumed = this.mOrientationHelper.getDecoratedMeasurement(next);
        if (this.mOrientation == 1) {
            if (isLayoutRTL()) {
                i4 = this.mWidth - getPaddingRight();
                i2 = i4 - this.mOrientationHelper.getDecoratedMeasurementInOther(next);
            } else {
                int paddingLeft = getPaddingLeft();
                i4 = this.mOrientationHelper.getDecoratedMeasurementInOther(next) + paddingLeft;
                i2 = paddingLeft;
            }
            if (layoutState.mLayoutDirection == -1) {
                i = layoutState.mOffset;
                i3 = i - layoutChunkResult.mConsumed;
            } else {
                i3 = layoutState.mOffset;
                i = layoutChunkResult.mConsumed + i3;
            }
        } else {
            i3 = getPaddingTop();
            int decoratedMeasurementInOther = this.mOrientationHelper.getDecoratedMeasurementInOther(next) + i3;
            if (layoutState.mLayoutDirection == -1) {
                i4 = layoutState.mOffset;
                i5 = i4 - layoutChunkResult.mConsumed;
            } else {
                i5 = layoutState.mOffset;
                i4 = layoutChunkResult.mConsumed + i5;
            }
            i = decoratedMeasurementInOther;
            i2 = i5;
        }
        RecyclerView.LayoutManager.layoutDecoratedWithMargins(next, i2, i3, i4, i);
        if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
            layoutChunkResult.mIgnoreConsumed = true;
        }
        layoutChunkResult.mFocusable = next.hasFocusable();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public View onFocusSearchFailed(View view, int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int convertFocusDirectionToLayoutDirection;
        View view2;
        View view3;
        resolveShouldLayoutReverse();
        if (getChildCount() == 0 || (convertFocusDirectionToLayoutDirection = convertFocusDirectionToLayoutDirection(i)) == Integer.MIN_VALUE) {
            return null;
        }
        ensureLayoutState();
        updateLayoutState(convertFocusDirectionToLayoutDirection, (int) (this.mOrientationHelper.getTotalSpace() * 0.33333334f), false, state);
        LayoutState layoutState = this.mLayoutState;
        layoutState.mScrollingOffset = Integer.MIN_VALUE;
        layoutState.mRecycle = false;
        fill(recycler, layoutState, state, true);
        if (convertFocusDirectionToLayoutDirection == -1) {
            if (this.mShouldReverseLayout) {
                view2 = findOnePartiallyOrCompletelyInvisibleChild(getChildCount() - 1, -1);
            } else {
                view2 = findOnePartiallyOrCompletelyInvisibleChild(0, getChildCount());
            }
        } else if (this.mShouldReverseLayout) {
            view2 = findOnePartiallyOrCompletelyInvisibleChild(0, getChildCount());
        } else {
            view2 = findOnePartiallyOrCompletelyInvisibleChild(getChildCount() - 1, -1);
        }
        if (convertFocusDirectionToLayoutDirection == -1) {
            view3 = getChildClosestToStart();
        } else {
            view3 = getChildClosestToEnd();
        }
        if (!view3.hasFocusable()) {
            return view2;
        }
        if (view2 == null) {
            return null;
        }
        return view3;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (getChildCount() > 0) {
            accessibilityEvent.setFromIndex(findFirstVisibleItemPosition());
            accessibilityEvent.setToIndex(findLastVisibleItemPosition());
        }
    }

    public final int scrollBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int i2;
        if (getChildCount() == 0 || i == 0) {
            return 0;
        }
        ensureLayoutState();
        this.mLayoutState.mRecycle = true;
        if (i > 0) {
            i2 = 1;
        } else {
            i2 = -1;
        }
        int abs = Math.abs(i);
        updateLayoutState(i2, abs, true, state);
        LayoutState layoutState = this.mLayoutState;
        int fill = fill(recycler, layoutState, state, false) + layoutState.mScrollingOffset;
        if (fill < 0) {
            return 0;
        }
        if (abs > fill) {
            i = i2 * fill;
        }
        this.mOrientationHelper.offsetChildren(-i);
        this.mLayoutState.mLastScrollDelta = i;
        return i;
    }

    public LinearLayoutManager(Context context, AttributeSet attributeSet, int i, int i2) {
        this.mOrientation = 1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mStackFromEnd = false;
        this.mSmoothScrollbarEnabled = true;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
        this.mAnchorInfo = new AnchorInfo();
        this.mLayoutChunkResult = new LayoutChunkResult();
        this.mInitialPrefetchItemCount = 2;
        this.mReusableIntPair = new int[2];
        RecyclerView.LayoutManager.Properties properties = RecyclerView.LayoutManager.getProperties(context, attributeSet, i, i2);
        setOrientation(properties.orientation);
        boolean z = properties.reverseLayout;
        assertNotInLayoutOrScroll(null);
        if (z != this.mReverseLayout) {
            this.mReverseLayout = z;
            requestLayout();
        }
        setStackFromEnd(properties.stackFromEnd);
    }
}
