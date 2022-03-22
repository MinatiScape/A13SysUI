package androidx.leanback.widget;

import androidx.collection.CircularIntArray;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: classes.dex */
public abstract class Grid {
    public int mNumRows;
    public Provider mProvider;
    public boolean mReversedFlow;
    public int mSpacing;
    public CircularIntArray[] mTmpItemPositionsInRows;
    public Object[] mTmpItem = new Object[1];
    public int mFirstVisibleIndex = -1;
    public int mLastVisibleIndex = -1;
    public int mStartIndex = -1;

    /* loaded from: classes.dex */
    public interface Provider {
    }

    public abstract boolean appendVisibleItems(int i, boolean z);

    public void collectAdjacentPrefetchPositions(int i, int i2, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
    }

    public abstract int findRowMax(boolean z, int i, int[] iArr);

    public abstract int findRowMin(boolean z, int i, int[] iArr);

    public abstract CircularIntArray[] getItemPositionsInRows(int i, int i2);

    public abstract Location getLocation(int i);

    public abstract boolean prependVisibleItems(int i, boolean z);

    /* loaded from: classes.dex */
    public static class Location {
        public int row;

        public Location(int i) {
            this.row = i;
        }
    }

    public final boolean checkAppendOverLimit(int i) {
        if (this.mLastVisibleIndex < 0) {
            return false;
        }
        if (this.mReversedFlow) {
            if (findRowMin(true, null) > i + this.mSpacing) {
                return false;
            }
        } else if (findRowMax(false, null) < i - this.mSpacing) {
            return false;
        }
        return true;
    }

    public final boolean checkPrependOverLimit(int i) {
        if (this.mLastVisibleIndex < 0) {
            return false;
        }
        if (this.mReversedFlow) {
            if (findRowMax(false, null) < i - this.mSpacing) {
                return false;
            }
        } else if (findRowMin(true, null) > i + this.mSpacing) {
            return false;
        }
        return true;
    }

    public final int findRowMax(boolean z, int[] iArr) {
        int i;
        if (this.mReversedFlow) {
            i = this.mFirstVisibleIndex;
        } else {
            i = this.mLastVisibleIndex;
        }
        return findRowMax(z, i, iArr);
    }

    public final int findRowMin(boolean z, int[] iArr) {
        int i;
        if (this.mReversedFlow) {
            i = this.mLastVisibleIndex;
        } else {
            i = this.mFirstVisibleIndex;
        }
        return findRowMin(z, i, iArr);
    }

    public void invalidateItemsAfter(int i) {
        int i2;
        if (i >= 0 && (i2 = this.mLastVisibleIndex) >= 0) {
            if (i2 >= i) {
                this.mLastVisibleIndex = i - 1;
            }
            if (this.mLastVisibleIndex < this.mFirstVisibleIndex) {
                this.mLastVisibleIndex = -1;
                this.mFirstVisibleIndex = -1;
            }
            if (this.mFirstVisibleIndex < 0) {
                this.mStartIndex = i;
            }
        }
    }

    public final void setNumRows(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException();
        } else if (this.mNumRows != i) {
            this.mNumRows = i;
            this.mTmpItemPositionsInRows = new CircularIntArray[i];
            for (int i2 = 0; i2 < this.mNumRows; i2++) {
                this.mTmpItemPositionsInRows[i2] = new CircularIntArray();
            }
        }
    }
}
