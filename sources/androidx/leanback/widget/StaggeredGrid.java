package androidx.leanback.widget;

import androidx.collection.CircularArray;
import androidx.collection.CircularIntArray;
import androidx.leanback.widget.Grid;
import androidx.leanback.widget.GridLayoutManager;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class StaggeredGrid extends Grid {
    public Object mPendingItem;
    public int mPendingItemSize;
    public CircularArray<Location> mLocations = new CircularArray<>();
    public int mFirstIndex = -1;

    public abstract boolean appendVisibleItemsWithoutCache(int i, boolean z);

    @Override // androidx.leanback.widget.Grid
    public final CircularIntArray[] getItemPositionsInRows(int i, int i2) {
        for (int i3 = 0; i3 < this.mNumRows; i3++) {
            CircularIntArray circularIntArray = this.mTmpItemPositionsInRows[i3];
            Objects.requireNonNull(circularIntArray);
            circularIntArray.mTail = 0;
        }
        if (i >= 0) {
            while (i <= i2) {
                CircularIntArray circularIntArray2 = this.mTmpItemPositionsInRows[getLocation(i).row];
                Objects.requireNonNull(circularIntArray2);
                int i4 = circularIntArray2.mTail;
                int i5 = circularIntArray2.mCapacityBitmask;
                if (((i4 + 0) & i5) > 0) {
                    if (i4 != 0) {
                        int[] iArr = circularIntArray2.mElements;
                        int i6 = i5 & (i4 - 1);
                        if (iArr[i6] == i - 1) {
                            if (i4 != 0) {
                                int i7 = iArr[i6];
                                circularIntArray2.mTail = i6;
                                circularIntArray2.addLast(i);
                                i++;
                            } else {
                                throw new ArrayIndexOutOfBoundsException();
                            }
                        }
                    } else {
                        throw new ArrayIndexOutOfBoundsException();
                    }
                }
                circularIntArray2.addLast(i);
                circularIntArray2.addLast(i);
                i++;
            }
        }
        return this.mTmpItemPositionsInRows;
    }

    public abstract boolean prependVisibleItemsWithoutCache(int i, boolean z);

    /* loaded from: classes.dex */
    public static class Location extends Grid.Location {
        public int offset;
        public int size = 0;

        public Location(int i, int i2) {
            super(i);
            this.offset = i2;
        }
    }

    public final boolean appendVisbleItemsWithCache(int i, boolean z) {
        int i2;
        int i3;
        if (this.mLocations.size() == 0) {
            return false;
        }
        int count = ((GridLayoutManager.AnonymousClass2) this.mProvider).getCount();
        int i4 = this.mLastVisibleIndex;
        if (i4 >= 0) {
            i2 = i4 + 1;
            i3 = ((GridLayoutManager.AnonymousClass2) this.mProvider).getEdge(i4);
        } else {
            int i5 = this.mStartIndex;
            if (i5 != -1) {
                i2 = i5;
            } else {
                i2 = 0;
            }
            if (i2 > getLastIndex() + 1 || i2 < this.mFirstIndex) {
                CircularArray<Location> circularArray = this.mLocations;
                Objects.requireNonNull(circularArray);
                circularArray.removeFromStart(circularArray.size());
                return false;
            } else if (i2 > getLastIndex()) {
                return false;
            } else {
                i3 = Integer.MAX_VALUE;
            }
        }
        int lastIndex = getLastIndex();
        while (i2 < count && i2 <= lastIndex) {
            Location location = getLocation(i2);
            if (i3 != Integer.MAX_VALUE) {
                i3 += location.offset;
            }
            int i6 = location.row;
            int createItem = ((GridLayoutManager.AnonymousClass2) this.mProvider).createItem(i2, true, this.mTmpItem, false);
            if (createItem != location.size) {
                location.size = createItem;
                this.mLocations.removeFromEnd(lastIndex - i2);
                lastIndex = i2;
            }
            this.mLastVisibleIndex = i2;
            if (this.mFirstVisibleIndex < 0) {
                this.mFirstVisibleIndex = i2;
            }
            ((GridLayoutManager.AnonymousClass2) this.mProvider).addItem(this.mTmpItem[0], createItem, i6, i3);
            if (!z && checkAppendOverLimit(i)) {
                return true;
            }
            if (i3 == Integer.MAX_VALUE) {
                i3 = ((GridLayoutManager.AnonymousClass2) this.mProvider).getEdge(i2);
            }
            if (i6 == this.mNumRows - 1 && z) {
                return true;
            }
            i2++;
        }
        return false;
    }

    public final int appendVisibleItemToRow(int i, int i2, int i3) {
        int i4;
        boolean z;
        int i5 = this.mLastVisibleIndex;
        if (i5 < 0 || (i5 == getLastIndex() && this.mLastVisibleIndex == i - 1)) {
            int i6 = this.mLastVisibleIndex;
            if (i6 >= 0) {
                i4 = i3 - ((GridLayoutManager.AnonymousClass2) this.mProvider).getEdge(i6);
            } else if (this.mLocations.size() <= 0 || i != getLastIndex() + 1) {
                i4 = 0;
            } else {
                int lastIndex = getLastIndex();
                while (true) {
                    if (lastIndex < this.mFirstIndex) {
                        z = false;
                        break;
                    } else if (getLocation(lastIndex).row == i2) {
                        z = true;
                        break;
                    } else {
                        lastIndex--;
                    }
                }
                if (!z) {
                    lastIndex = getLastIndex();
                }
                if (this.mReversedFlow) {
                    i4 = (-getLocation(lastIndex).size) - this.mSpacing;
                } else {
                    i4 = getLocation(lastIndex).size + this.mSpacing;
                }
                for (int i7 = lastIndex + 1; i7 <= getLastIndex(); i7++) {
                    i4 -= getLocation(i7).offset;
                }
            }
            Location location = new Location(i2, i4);
            CircularArray<Location> circularArray = this.mLocations;
            Objects.requireNonNull(circularArray);
            Location[] locationArr = circularArray.mElements;
            int i8 = circularArray.mTail;
            locationArr[i8] = location;
            int i9 = circularArray.mCapacityBitmask & (i8 + 1);
            circularArray.mTail = i9;
            if (i9 == circularArray.mHead) {
                circularArray.doubleCapacity();
            }
            Object obj = this.mPendingItem;
            if (obj != null) {
                location.size = this.mPendingItemSize;
                this.mPendingItem = null;
            } else {
                location.size = ((GridLayoutManager.AnonymousClass2) this.mProvider).createItem(i, true, this.mTmpItem, false);
                obj = this.mTmpItem[0];
            }
            if (this.mLocations.size() == 1) {
                this.mLastVisibleIndex = i;
                this.mFirstVisibleIndex = i;
                this.mFirstIndex = i;
            } else {
                int i10 = this.mLastVisibleIndex;
                if (i10 < 0) {
                    this.mLastVisibleIndex = i;
                    this.mFirstVisibleIndex = i;
                } else {
                    this.mLastVisibleIndex = i10 + 1;
                }
            }
            ((GridLayoutManager.AnonymousClass2) this.mProvider).addItem(obj, location.size, i2, i3);
            return location.size;
        }
        throw new IllegalStateException();
    }

    @Override // androidx.leanback.widget.Grid
    public final boolean appendVisibleItems(int i, boolean z) {
        boolean appendVisibleItemsWithoutCache;
        if (((GridLayoutManager.AnonymousClass2) this.mProvider).getCount() == 0) {
            return false;
        }
        if (!z && checkAppendOverLimit(i)) {
            return false;
        }
        try {
            if (appendVisbleItemsWithCache(i, z)) {
                appendVisibleItemsWithoutCache = true;
                this.mTmpItem[0] = null;
            } else {
                appendVisibleItemsWithoutCache = appendVisibleItemsWithoutCache(i, z);
                this.mTmpItem[0] = null;
            }
            this.mPendingItem = null;
            return appendVisibleItemsWithoutCache;
        } catch (Throwable th) {
            this.mTmpItem[0] = null;
            this.mPendingItem = null;
            throw th;
        }
    }

    public final int getLastIndex() {
        return (this.mLocations.size() + this.mFirstIndex) - 1;
    }

    @Override // androidx.leanback.widget.Grid
    public final Location getLocation(int i) {
        int i2 = i - this.mFirstIndex;
        if (i2 < 0 || i2 >= this.mLocations.size()) {
            return null;
        }
        CircularArray<Location> circularArray = this.mLocations;
        if (i2 < 0) {
            Objects.requireNonNull(circularArray);
        } else if (i2 < circularArray.size()) {
            return circularArray.mElements[circularArray.mCapacityBitmask & (circularArray.mHead + i2)];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public final boolean prependVisbleItemsWithCache(int i, boolean z) {
        int i2;
        int i3;
        int i4;
        if (this.mLocations.size() == 0) {
            return false;
        }
        int i5 = this.mFirstVisibleIndex;
        if (i5 >= 0) {
            i4 = ((GridLayoutManager.AnonymousClass2) this.mProvider).getEdge(i5);
            i3 = getLocation(this.mFirstVisibleIndex).offset;
            i2 = this.mFirstVisibleIndex - 1;
        } else {
            i4 = Integer.MAX_VALUE;
            int i6 = this.mStartIndex;
            if (i6 != -1) {
                i2 = i6;
            } else {
                i2 = 0;
            }
            if (i2 <= getLastIndex()) {
                int i7 = this.mFirstIndex;
                if (i2 >= i7 - 1) {
                    if (i2 < i7) {
                        return false;
                    }
                    i3 = 0;
                }
            }
            CircularArray<Location> circularArray = this.mLocations;
            Objects.requireNonNull(circularArray);
            circularArray.removeFromStart(circularArray.size());
            return false;
        }
        GridLayoutManager.AnonymousClass2 r5 = (GridLayoutManager.AnonymousClass2) this.mProvider;
        Objects.requireNonNull(r5);
        int max = Math.max(GridLayoutManager.this.mPositionDeltaInPreLayout, this.mFirstIndex);
        while (i2 >= max) {
            Location location = getLocation(i2);
            int i8 = location.row;
            int createItem = ((GridLayoutManager.AnonymousClass2) this.mProvider).createItem(i2, false, this.mTmpItem, false);
            if (createItem != location.size) {
                this.mLocations.removeFromStart((i2 + 1) - this.mFirstIndex);
                this.mFirstIndex = this.mFirstVisibleIndex;
                this.mPendingItem = this.mTmpItem[0];
                this.mPendingItemSize = createItem;
                return false;
            }
            this.mFirstVisibleIndex = i2;
            if (this.mLastVisibleIndex < 0) {
                this.mLastVisibleIndex = i2;
            }
            ((GridLayoutManager.AnonymousClass2) this.mProvider).addItem(this.mTmpItem[0], createItem, i8, i4 - i3);
            if (!z && checkPrependOverLimit(i)) {
                return true;
            }
            i4 = ((GridLayoutManager.AnonymousClass2) this.mProvider).getEdge(i2);
            i3 = location.offset;
            if (i8 == 0 && z) {
                return true;
            }
            i2--;
        }
        return false;
    }

    public final int prependVisibleItemToRow(int i, int i2, int i3) {
        Location location;
        int i4;
        int i5 = this.mFirstVisibleIndex;
        if (i5 < 0 || (i5 == this.mFirstIndex && i5 == i + 1)) {
            int i6 = this.mFirstIndex;
            if (i6 >= 0) {
                location = getLocation(i6);
            } else {
                location = null;
            }
            int edge = ((GridLayoutManager.AnonymousClass2) this.mProvider).getEdge(this.mFirstIndex);
            Location location2 = new Location(i2, 0);
            CircularArray<Location> circularArray = this.mLocations;
            Objects.requireNonNull(circularArray);
            int i7 = (circularArray.mHead - 1) & circularArray.mCapacityBitmask;
            circularArray.mHead = i7;
            circularArray.mElements[i7] = location2;
            if (i7 == circularArray.mTail) {
                circularArray.doubleCapacity();
            }
            Object obj = this.mPendingItem;
            if (obj != null) {
                location2.size = this.mPendingItemSize;
                this.mPendingItem = null;
            } else {
                location2.size = ((GridLayoutManager.AnonymousClass2) this.mProvider).createItem(i, false, this.mTmpItem, false);
                obj = this.mTmpItem[0];
            }
            this.mFirstVisibleIndex = i;
            this.mFirstIndex = i;
            if (this.mLastVisibleIndex < 0) {
                this.mLastVisibleIndex = i;
            }
            if (!this.mReversedFlow) {
                i4 = i3 - location2.size;
            } else {
                i4 = i3 + location2.size;
            }
            if (location != null) {
                location.offset = edge - i4;
            }
            ((GridLayoutManager.AnonymousClass2) this.mProvider).addItem(obj, location2.size, i2, i4);
            return location2.size;
        }
        throw new IllegalStateException();
    }

    @Override // androidx.leanback.widget.Grid
    public final boolean prependVisibleItems(int i, boolean z) {
        boolean prependVisibleItemsWithoutCache;
        if (((GridLayoutManager.AnonymousClass2) this.mProvider).getCount() == 0) {
            return false;
        }
        if (!z && checkPrependOverLimit(i)) {
            return false;
        }
        try {
            if (prependVisbleItemsWithCache(i, z)) {
                prependVisibleItemsWithoutCache = true;
                this.mTmpItem[0] = null;
            } else {
                prependVisibleItemsWithoutCache = prependVisibleItemsWithoutCache(i, z);
                this.mTmpItem[0] = null;
            }
            this.mPendingItem = null;
            return prependVisibleItemsWithoutCache;
        } catch (Throwable th) {
            this.mTmpItem[0] = null;
            this.mPendingItem = null;
            throw th;
        }
    }

    @Override // androidx.leanback.widget.Grid
    public final void invalidateItemsAfter(int i) {
        super.invalidateItemsAfter(i);
        this.mLocations.removeFromEnd((getLastIndex() - i) + 1);
        if (this.mLocations.size() == 0) {
            this.mFirstIndex = -1;
        }
    }
}
