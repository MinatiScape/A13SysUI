package androidx.recyclerview.widget;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.GapWorker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class GridLayoutManager extends LinearLayoutManager {
    public int[] mCachedBorders;
    public View[] mSet;
    public boolean mPendingSpanCountChange = false;
    public int mSpanCount = -1;
    public final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
    public final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
    public SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();
    public final Rect mDecorInsets = new Rect();

    /* loaded from: classes.dex */
    public static abstract class SpanSizeLookup {
        public final SparseIntArray mSpanIndexCache = new SparseIntArray();
        public final SparseIntArray mSpanGroupIndexCache = new SparseIntArray();
        public boolean mCacheSpanIndices = false;

        public abstract int getSpanSize(int i);

        public final int getCachedSpanIndex(int i, int i2) {
            if (!this.mCacheSpanIndices) {
                return getSpanIndex(i, i2);
            }
            int i3 = this.mSpanIndexCache.get(i, -1);
            if (i3 != -1) {
                return i3;
            }
            int spanIndex = getSpanIndex(i, i2);
            this.mSpanIndexCache.put(i, spanIndex);
            return spanIndex;
        }

        public final void invalidateSpanIndexCache() {
            this.mSpanIndexCache.clear();
        }

        public final int getSpanGroupIndex(int i, int i2) {
            int spanSize = getSpanSize(i);
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < i; i5++) {
                int spanSize2 = getSpanSize(i5);
                i3 += spanSize2;
                if (i3 == i2) {
                    i4++;
                    i3 = 0;
                } else if (i3 > i2) {
                    i4++;
                    i3 = spanSize2;
                }
            }
            if (i3 + spanSize > i2) {
                return i4 + 1;
            }
            return i4;
        }

        /* JADX WARN: Code restructure failed: missing block: B:26:0x0050, code lost:
            if (r2 > r10) goto L_0x0041;
         */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0047  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x0056  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:25:0x004e -> B:27:0x0053). Please submit an issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x0050 -> B:20:0x0041). Please submit an issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x0050 -> B:27:0x0053). Please submit an issue!!! */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public int getSpanIndex(int r9, int r10) {
            /*
                r8 = this;
                int r0 = r8.getSpanSize(r9)
                r1 = 0
                if (r0 != r10) goto L_0x0008
                return r1
            L_0x0008:
                boolean r2 = r8.mCacheSpanIndices
                if (r2 == 0) goto L_0x0043
                android.util.SparseIntArray r2 = r8.mSpanIndexCache
                int r3 = r2.size()
                r4 = -1
                int r3 = r3 + r4
                r5 = r1
            L_0x0015:
                if (r5 > r3) goto L_0x0027
                int r6 = r5 + r3
                int r6 = r6 >>> 1
                int r7 = r2.keyAt(r6)
                if (r7 >= r9) goto L_0x0024
                int r5 = r6 + 1
                goto L_0x0015
            L_0x0024:
                int r3 = r6 + (-1)
                goto L_0x0015
            L_0x0027:
                int r5 = r5 + r4
                if (r5 < 0) goto L_0x0034
                int r3 = r2.size()
                if (r5 >= r3) goto L_0x0034
                int r4 = r2.keyAt(r5)
            L_0x0034:
                if (r4 < 0) goto L_0x0043
                android.util.SparseIntArray r2 = r8.mSpanIndexCache
                int r2 = r2.get(r4)
                int r3 = r8.getSpanSize(r4)
                int r3 = r3 + r2
            L_0x0041:
                r2 = r3
                goto L_0x0053
            L_0x0043:
                r2 = r1
                r4 = r2
            L_0x0045:
                if (r4 >= r9) goto L_0x0056
                int r3 = r8.getSpanSize(r4)
                int r2 = r2 + r3
                if (r2 != r10) goto L_0x0050
                r2 = r1
                goto L_0x0053
            L_0x0050:
                if (r2 <= r10) goto L_0x0053
                goto L_0x0041
            L_0x0053:
                int r4 = r4 + 1
                goto L_0x0045
            L_0x0056:
                int r0 = r0 + r2
                if (r0 > r10) goto L_0x005a
                return r2
            L_0x005a:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup.getSpanIndex(int, int):int");
        }
    }

    public GridLayoutManager(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setSpanCount(RecyclerView.LayoutManager.getProperties(context, attributeSet, i, i2).spanCount);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final RecyclerView.LayoutParams generateLayoutParams(Context context, AttributeSet attributeSet) {
        return new LayoutParams(context, attributeSet);
    }

    public final void calculateItemBorders(int i) {
        int i2;
        int[] iArr = this.mCachedBorders;
        int i3 = this.mSpanCount;
        if (!(iArr != null && iArr.length == i3 + 1 && iArr[iArr.length - 1] == i)) {
            iArr = new int[i3 + 1];
        }
        int i4 = 0;
        iArr[0] = 0;
        int i5 = i / i3;
        int i6 = i % i3;
        int i7 = 0;
        for (int i8 = 1; i8 <= i3; i8++) {
            i4 += i6;
            if (i4 <= 0 || i3 - i4 >= i6) {
                i2 = i5;
            } else {
                i2 = i5 + 1;
                i4 -= i3;
            }
            i7 += i2;
            iArr[i8] = i7;
        }
        this.mCachedBorders = iArr;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    public final void collectPrefetchPositionsForLayoutState(RecyclerView.State state, LinearLayoutManager.LayoutState layoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        boolean z;
        int i = this.mSpanCount;
        for (int i2 = 0; i2 < this.mSpanCount; i2++) {
            Objects.requireNonNull(layoutState);
            int i3 = layoutState.mCurrentPosition;
            if (i3 < 0 || i3 >= state.getItemCount()) {
                z = false;
            } else {
                z = true;
            }
            if (z && i > 0) {
                int i4 = layoutState.mCurrentPosition;
                ((GapWorker.LayoutPrefetchRegistryImpl) layoutPrefetchRegistry).addPosition(i4, Math.max(0, layoutState.mScrollingOffset));
                i -= this.mSpanSizeLookup.getSpanSize(i4);
                layoutState.mCurrentPosition += layoutState.mItemDirection;
            } else {
                return;
            }
        }
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final RecyclerView.LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -1);
        }
        return new LayoutParams(-1, -2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getColumnCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 1) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getRowCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }

    public final int getSpaceForSpanRange(int i, int i2) {
        if (this.mOrientation != 1 || !isLayoutRTL()) {
            int[] iArr = this.mCachedBorders;
            return iArr[i2 + i] - iArr[i];
        }
        int[] iArr2 = this.mCachedBorders;
        int i3 = this.mSpanCount;
        return iArr2[i3 - i] - iArr2[(i3 - i) - i2];
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    public final void layoutChunk(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.LayoutState layoutState, LinearLayoutManager.LayoutChunkResult layoutChunkResult) {
        boolean z;
        int i;
        boolean z2;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        boolean z3;
        boolean z4;
        View next;
        int modeInOther = this.mOrientationHelper.getModeInOther();
        int i13 = 1;
        if (modeInOther != 1073741824) {
            z = true;
        } else {
            z = false;
        }
        if (getChildCount() > 0) {
            i = this.mCachedBorders[this.mSpanCount];
        } else {
            i = 0;
        }
        if (z) {
            updateMeasurements();
        }
        if (layoutState.mItemDirection == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        int i14 = this.mSpanCount;
        if (!z2) {
            i14 = getSpanIndex(recycler, state, layoutState.mCurrentPosition) + getSpanSize(recycler, state, layoutState.mCurrentPosition);
        }
        int i15 = 0;
        while (i15 < this.mSpanCount) {
            int i16 = layoutState.mCurrentPosition;
            if (i16 < 0 || i16 >= state.getItemCount()) {
                z4 = false;
            } else {
                z4 = true;
            }
            if (!z4 || i14 <= 0) {
                break;
            }
            int i17 = layoutState.mCurrentPosition;
            int spanSize = getSpanSize(recycler, state, i17);
            if (spanSize <= this.mSpanCount) {
                i14 -= spanSize;
                if (i14 < 0 || (next = layoutState.next(recycler)) == null) {
                    break;
                }
                this.mSet[i15] = next;
                i15++;
            } else {
                StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("Item at position ", i17, " requires ", spanSize, " spans but GridLayoutManager has only ");
                m.append(this.mSpanCount);
                m.append(" spans.");
                throw new IllegalArgumentException(m.toString());
            }
        }
        if (i15 == 0) {
            layoutChunkResult.mFinished = true;
            return;
        }
        if (z2) {
            i3 = 0;
            i2 = i15;
        } else {
            i3 = i15 - 1;
            i13 = -1;
            i2 = -1;
        }
        int i18 = 0;
        while (i3 != i2) {
            View view = this.mSet[i3];
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            int spanSize2 = getSpanSize(recycler, state, RecyclerView.LayoutManager.getPosition(view));
            layoutParams.mSpanSize = spanSize2;
            layoutParams.mSpanIndex = i18;
            i18 += spanSize2;
            i3 += i13;
        }
        float f = 0.0f;
        int i19 = 0;
        for (int i20 = 0; i20 < i15; i20++) {
            View view2 = this.mSet[i20];
            if (layoutState.mScrapList != null) {
                z3 = false;
                if (z2) {
                    addViewInt(view2, -1, true);
                } else {
                    addViewInt(view2, 0, true);
                }
            } else if (z2) {
                z3 = false;
                addViewInt(view2, -1, false);
            } else {
                z3 = false;
                addViewInt(view2, 0, false);
            }
            calculateItemDecorationsForChild(view2, this.mDecorInsets);
            measureChild(view2, modeInOther, z3);
            int decoratedMeasurement = this.mOrientationHelper.getDecoratedMeasurement(view2);
            if (decoratedMeasurement > i19) {
                i19 = decoratedMeasurement;
            }
            float decoratedMeasurementInOther = (this.mOrientationHelper.getDecoratedMeasurementInOther(view2) * 1.0f) / ((LayoutParams) view2.getLayoutParams()).mSpanSize;
            if (decoratedMeasurementInOther > f) {
                f = decoratedMeasurementInOther;
            }
        }
        if (z) {
            calculateItemBorders(Math.max(Math.round(f * this.mSpanCount), i));
            i19 = 0;
            for (int i21 = 0; i21 < i15; i21++) {
                View view3 = this.mSet[i21];
                measureChild(view3, 1073741824, true);
                int decoratedMeasurement2 = this.mOrientationHelper.getDecoratedMeasurement(view3);
                if (decoratedMeasurement2 > i19) {
                    i19 = decoratedMeasurement2;
                }
            }
        }
        for (int i22 = 0; i22 < i15; i22++) {
            View view4 = this.mSet[i22];
            if (this.mOrientationHelper.getDecoratedMeasurement(view4) != i19) {
                LayoutParams layoutParams2 = (LayoutParams) view4.getLayoutParams();
                Rect rect = layoutParams2.mDecorInsets;
                int i23 = rect.top + rect.bottom + ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin;
                int i24 = rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin;
                int spaceForSpanRange = getSpaceForSpanRange(layoutParams2.mSpanIndex, layoutParams2.mSpanSize);
                if (this.mOrientation == 1) {
                    i12 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, 1073741824, i24, ((ViewGroup.MarginLayoutParams) layoutParams2).width, false);
                    i11 = View.MeasureSpec.makeMeasureSpec(i19 - i23, 1073741824);
                } else {
                    int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i19 - i24, 1073741824);
                    i11 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, 1073741824, i23, ((ViewGroup.MarginLayoutParams) layoutParams2).height, false);
                    i12 = makeMeasureSpec;
                }
                if (shouldReMeasureChild(view4, i12, i11, (RecyclerView.LayoutParams) view4.getLayoutParams())) {
                    view4.measure(i12, i11);
                }
            }
        }
        layoutChunkResult.mConsumed = i19;
        if (this.mOrientation == 1) {
            if (layoutState.mLayoutDirection == -1) {
                i10 = layoutState.mOffset;
                i9 = i10 - i19;
            } else {
                i9 = layoutState.mOffset;
                i10 = i19 + i9;
            }
            i5 = 0;
            i4 = i9;
            i6 = i10;
            i7 = 0;
        } else {
            if (layoutState.mLayoutDirection == -1) {
                i7 = layoutState.mOffset;
                i8 = i7 - i19;
            } else {
                i8 = layoutState.mOffset;
                i7 = i19 + i8;
            }
            i4 = 0;
            i5 = i8;
            i6 = 0;
        }
        for (int i25 = 0; i25 < i15; i25++) {
            View view5 = this.mSet[i25];
            LayoutParams layoutParams3 = (LayoutParams) view5.getLayoutParams();
            if (this.mOrientation != 1) {
                int paddingTop = getPaddingTop() + this.mCachedBorders[layoutParams3.mSpanIndex];
                i4 = paddingTop;
                i6 = this.mOrientationHelper.getDecoratedMeasurementInOther(view5) + paddingTop;
            } else if (isLayoutRTL()) {
                i7 = getPaddingLeft() + this.mCachedBorders[this.mSpanCount - layoutParams3.mSpanIndex];
                i5 = i7 - this.mOrientationHelper.getDecoratedMeasurementInOther(view5);
            } else {
                i5 = this.mCachedBorders[layoutParams3.mSpanIndex] + getPaddingLeft();
                i7 = this.mOrientationHelper.getDecoratedMeasurementInOther(view5) + i5;
            }
            RecyclerView.LayoutManager.layoutDecoratedWithMargins(view5, i5, i4, i7, i6);
            if (layoutParams3.isItemRemoved() || layoutParams3.isItemChanged()) {
                layoutChunkResult.mIgnoreConsumed = true;
            }
            layoutChunkResult.mFocusable = view5.hasFocusable() | layoutChunkResult.mFocusable;
        }
        Arrays.fill(this.mSet, (Object) null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:54:0x00cb, code lost:
        if (r13 == r7) goto L_0x00cd;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00fb, code lost:
        if (r13 == r7) goto L_0x00fd;
     */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0107  */
    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.view.View onFocusSearchFailed(android.view.View r23, int r24, androidx.recyclerview.widget.RecyclerView.Recycler r25, androidx.recyclerview.widget.RecyclerView.State r26) {
        /*
            Method dump skipped, instructions count: 327
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.GridLayoutManager.onFocusSearchFailed(android.view.View, int, androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State):android.view.View");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onItemsAdded(int i, int i2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        SpanSizeLookup spanSizeLookup = this.mSpanSizeLookup;
        Objects.requireNonNull(spanSizeLookup);
        spanSizeLookup.mSpanGroupIndexCache.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onItemsChanged() {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        SpanSizeLookup spanSizeLookup = this.mSpanSizeLookup;
        Objects.requireNonNull(spanSizeLookup);
        spanSizeLookup.mSpanGroupIndexCache.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onItemsMoved(int i, int i2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        SpanSizeLookup spanSizeLookup = this.mSpanSizeLookup;
        Objects.requireNonNull(spanSizeLookup);
        spanSizeLookup.mSpanGroupIndexCache.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onItemsRemoved(int i, int i2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        SpanSizeLookup spanSizeLookup = this.mSpanSizeLookup;
        Objects.requireNonNull(spanSizeLookup);
        spanSizeLookup.mSpanGroupIndexCache.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onItemsUpdated(RecyclerView recyclerView, int i, int i2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        SpanSizeLookup spanSizeLookup = this.mSpanSizeLookup;
        Objects.requireNonNull(spanSizeLookup);
        spanSizeLookup.mSpanGroupIndexCache.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void setMeasuredDimension(Rect rect, int i, int i2) {
        int i3;
        int i4;
        if (this.mCachedBorders == null) {
            super.setMeasuredDimension(rect, i, i2);
        }
        int paddingRight = getPaddingRight() + getPaddingLeft();
        int paddingBottom = getPaddingBottom() + getPaddingTop();
        if (this.mOrientation == 1) {
            int height = rect.height() + paddingBottom;
            RecyclerView recyclerView = this.mRecyclerView;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            i4 = RecyclerView.LayoutManager.chooseSize(i2, height, ViewCompat.Api16Impl.getMinimumHeight(recyclerView));
            int[] iArr = this.mCachedBorders;
            i3 = RecyclerView.LayoutManager.chooseSize(i, iArr[iArr.length - 1] + paddingRight, ViewCompat.Api16Impl.getMinimumWidth(this.mRecyclerView));
        } else {
            int width = rect.width() + paddingRight;
            RecyclerView recyclerView2 = this.mRecyclerView;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
            i3 = RecyclerView.LayoutManager.chooseSize(i, width, ViewCompat.Api16Impl.getMinimumWidth(recyclerView2));
            int[] iArr2 = this.mCachedBorders;
            i4 = RecyclerView.LayoutManager.chooseSize(i2, iArr2[iArr2.length - 1] + paddingBottom, ViewCompat.Api16Impl.getMinimumHeight(this.mRecyclerView));
        }
        this.mRecyclerView.setMeasuredDimension(i3, i4);
    }

    public final void setSpanCount(int i) {
        if (i != this.mSpanCount) {
            this.mPendingSpanCountChange = true;
            if (i >= 1) {
                this.mSpanCount = i;
                this.mSpanSizeLookup.invalidateSpanIndexCache();
                requestLayout();
                return;
            }
            throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("Span count should be at least 1. Provided ", i));
        }
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    public final void setStackFromEnd(boolean z) {
        if (!z) {
            super.setStackFromEnd(false);
            return;
        }
        throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final boolean supportsPredictiveItemAnimations() {
        if (this.mPendingSavedState != null || this.mPendingSpanCountChange) {
            return false;
        }
        return true;
    }

    public final void updateMeasurements() {
        int i;
        int i2;
        if (this.mOrientation == 1) {
            i2 = this.mWidth - getPaddingRight();
            i = getPaddingLeft();
        } else {
            i2 = this.mHeight - getPaddingBottom();
            i = getPaddingTop();
        }
        calculateItemBorders(i2 - i);
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends RecyclerView.LayoutParams {
        public int mSpanIndex = -1;
        public int mSpanSize = 0;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int computeHorizontalScrollOffset(RecyclerView.State state) {
        return computeScrollOffset(state);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int computeHorizontalScrollRange(RecyclerView.State state) {
        return computeScrollRange(state);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int computeVerticalScrollOffset(RecyclerView.State state) {
        return computeScrollOffset(state);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int computeVerticalScrollRange(RecyclerView.State state) {
        return computeScrollRange(state);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    public final View findReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state, boolean z, boolean z2) {
        int i;
        int childCount = getChildCount();
        int i2 = -1;
        if (z2) {
            i = getChildCount() - 1;
            childCount = -1;
        } else {
            i = 0;
            i2 = 1;
        }
        int itemCount = state.getItemCount();
        ensureLayoutState();
        int startAfterPadding = this.mOrientationHelper.getStartAfterPadding();
        int endAfterPadding = this.mOrientationHelper.getEndAfterPadding();
        View view = null;
        View view2 = null;
        while (i != childCount) {
            View childAt = getChildAt(i);
            int position = RecyclerView.LayoutManager.getPosition(childAt);
            if (position >= 0 && position < itemCount && getSpanIndex(recycler, state, position) == 0) {
                if (((RecyclerView.LayoutParams) childAt.getLayoutParams()).isItemRemoved()) {
                    if (view2 == null) {
                        view2 = childAt;
                    }
                } else if (this.mOrientationHelper.getDecoratedStart(childAt) < endAfterPadding && this.mOrientationHelper.getDecoratedEnd(childAt) >= startAfterPadding) {
                    return childAt;
                } else {
                    if (view == null) {
                        view = childAt;
                    }
                }
            }
            i += i2;
        }
        if (view != null) {
            return view;
        }
        return view2;
    }

    public final int getSpanGroupIndex(RecyclerView.Recycler recycler, RecyclerView.State state, int i) {
        Objects.requireNonNull(state);
        if (!state.mInPreLayout) {
            SpanSizeLookup spanSizeLookup = this.mSpanSizeLookup;
            int i2 = this.mSpanCount;
            Objects.requireNonNull(spanSizeLookup);
            return spanSizeLookup.getSpanGroupIndex(i, i2);
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
        if (convertPreLayoutPositionToPostLayout == -1) {
            GridLayoutManager$$ExternalSyntheticOutline1.m("Cannot find span size for pre layout position. ", i, "GridLayoutManager");
            return 0;
        }
        SpanSizeLookup spanSizeLookup2 = this.mSpanSizeLookup;
        int i3 = this.mSpanCount;
        Objects.requireNonNull(spanSizeLookup2);
        return spanSizeLookup2.getSpanGroupIndex(convertPreLayoutPositionToPostLayout, i3);
    }

    public final int getSpanIndex(RecyclerView.Recycler recycler, RecyclerView.State state, int i) {
        Objects.requireNonNull(state);
        if (!state.mInPreLayout) {
            return this.mSpanSizeLookup.getCachedSpanIndex(i, this.mSpanCount);
        }
        int i2 = this.mPreLayoutSpanIndexCache.get(i, -1);
        if (i2 != -1) {
            return i2;
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
        if (convertPreLayoutPositionToPostLayout != -1) {
            return this.mSpanSizeLookup.getCachedSpanIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
        }
        GridLayoutManager$$ExternalSyntheticOutline1.m("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:", i, "GridLayoutManager");
        return 0;
    }

    public final int getSpanSize(RecyclerView.Recycler recycler, RecyclerView.State state, int i) {
        Objects.requireNonNull(state);
        if (!state.mInPreLayout) {
            return this.mSpanSizeLookup.getSpanSize(i);
        }
        int i2 = this.mPreLayoutSpanSizeCache.get(i, -1);
        if (i2 != -1) {
            return i2;
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
        if (convertPreLayoutPositionToPostLayout != -1) {
            return this.mSpanSizeLookup.getSpanSize(convertPreLayoutPositionToPostLayout);
        }
        GridLayoutManager$$ExternalSyntheticOutline1.m("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:", i, "GridLayoutManager");
        return 1;
    }

    public final void measureChild(View view, int i, boolean z) {
        int i2;
        int i3;
        boolean z2;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        int i4 = rect.top + rect.bottom + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        int i5 = rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
        int spaceForSpanRange = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        if (this.mOrientation == 1) {
            i2 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, i, i5, ((ViewGroup.MarginLayoutParams) layoutParams).width, false);
            i3 = RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), this.mHeightMode, i4, ((ViewGroup.MarginLayoutParams) layoutParams).height, true);
        } else {
            int childMeasureSpec = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, i, i4, ((ViewGroup.MarginLayoutParams) layoutParams).height, false);
            int childMeasureSpec2 = RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), this.mWidthMode, i5, ((ViewGroup.MarginLayoutParams) layoutParams).width, true);
            i3 = childMeasureSpec;
            i2 = childMeasureSpec2;
        }
        RecyclerView.LayoutParams layoutParams2 = (RecyclerView.LayoutParams) view.getLayoutParams();
        if (z) {
            z2 = shouldReMeasureChild(view, i2, i3, layoutParams2);
        } else {
            z2 = shouldMeasureChild(view, i2, i3, layoutParams2);
        }
        if (z2) {
            view.measure(i2, i3);
        }
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    public final void onAnchorReady(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.AnchorInfo anchorInfo, int i) {
        boolean z;
        updateMeasurements();
        if (state.getItemCount() > 0 && !state.mInPreLayout) {
            if (i == 1) {
                z = true;
            } else {
                z = false;
            }
            int spanIndex = getSpanIndex(recycler, state, anchorInfo.mPosition);
            if (z) {
                while (spanIndex > 0) {
                    int i2 = anchorInfo.mPosition;
                    if (i2 <= 0) {
                        break;
                    }
                    int i3 = i2 - 1;
                    anchorInfo.mPosition = i3;
                    spanIndex = getSpanIndex(recycler, state, i3);
                }
            } else {
                int itemCount = state.getItemCount() - 1;
                int i4 = anchorInfo.mPosition;
                while (i4 < itemCount) {
                    int i5 = i4 + 1;
                    int spanIndex2 = getSpanIndex(recycler, state, i5);
                    if (spanIndex2 <= spanIndex) {
                        break;
                    }
                    i4 = i5;
                    spanIndex = spanIndex2;
                }
                anchorInfo.mPosition = i4;
            }
        }
        View[] viewArr = this.mSet;
        if (viewArr == null || viewArr.length != this.mSpanCount) {
            this.mSet = new View[this.mSpanCount];
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            onInitializeAccessibilityNodeInfoForItem(view, accessibilityNodeInfoCompat);
            return;
        }
        LayoutParams layoutParams2 = (LayoutParams) layoutParams;
        int spanGroupIndex = getSpanGroupIndex(recycler, state, layoutParams2.getViewLayoutPosition());
        if (this.mOrientation == 0) {
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(layoutParams2.mSpanIndex, layoutParams2.mSpanSize, spanGroupIndex, 1, false));
        } else {
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(spanGroupIndex, 1, layoutParams2.mSpanIndex, layoutParams2.mSpanSize, false));
        }
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Objects.requireNonNull(state);
        if (state.mInPreLayout) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                LayoutParams layoutParams = (LayoutParams) getChildAt(i).getLayoutParams();
                int viewLayoutPosition = layoutParams.getViewLayoutPosition();
                this.mPreLayoutSpanSizeCache.put(viewLayoutPosition, layoutParams.mSpanSize);
                this.mPreLayoutSpanIndexCache.put(viewLayoutPosition, layoutParams.mSpanIndex);
            }
        }
        super.onLayoutChildren(recycler, state);
        this.mPreLayoutSpanSizeCache.clear();
        this.mPreLayoutSpanIndexCache.clear();
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        this.mPendingSpanCountChange = false;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        updateMeasurements();
        View[] viewArr = this.mSet;
        if (viewArr == null || viewArr.length != this.mSpanCount) {
            this.mSet = new View[this.mSpanCount];
        }
        return super.scrollHorizontallyBy(i, recycler, state);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        updateMeasurements();
        View[] viewArr = this.mSet;
        if (viewArr == null || viewArr.length != this.mSpanCount) {
            this.mSet = new View[this.mSpanCount];
        }
        return super.scrollVerticallyBy(i, recycler, state);
    }

    public GridLayoutManager(int i, int i2) {
        super(1);
        setSpanCount(i);
    }

    public GridLayoutManager(int i) {
        super(1);
        setSpanCount(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public final boolean checkLayoutParams(RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    /* loaded from: classes.dex */
    public static final class DefaultSpanSizeLookup extends SpanSizeLookup {
        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public final int getSpanSize(int i) {
            return 1;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public final int getSpanIndex(int i, int i2) {
            return i % i2;
        }
    }
}
