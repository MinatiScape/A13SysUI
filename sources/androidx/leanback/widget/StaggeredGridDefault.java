package androidx.leanback.widget;

import androidx.leanback.widget.GridLayoutManager;
import androidx.leanback.widget.StaggeredGrid;
/* loaded from: classes.dex */
public final class StaggeredGridDefault extends StaggeredGrid {
    public final int findRowEdgeLimitSearchIndex(boolean z) {
        boolean z2 = false;
        if (z) {
            for (int i = this.mLastVisibleIndex; i >= this.mFirstVisibleIndex; i--) {
                int i2 = getLocation(i).row;
                if (i2 == 0) {
                    z2 = true;
                } else if (z2 && i2 == this.mNumRows - 1) {
                    return i;
                }
            }
            return -1;
        }
        for (int i3 = this.mFirstVisibleIndex; i3 <= this.mLastVisibleIndex; i3++) {
            int i4 = getLocation(i3).row;
            if (i4 == this.mNumRows - 1) {
                z2 = true;
            } else if (z2 && i4 == 0) {
                return i3;
            }
        }
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:106:0x014e, code lost:
        return r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0136, code lost:
        return true;
     */
    @Override // androidx.leanback.widget.StaggeredGrid
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean appendVisibleItemsWithoutCache(int r14, boolean r15) {
        /*
            Method dump skipped, instructions count: 355
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.StaggeredGridDefault.appendVisibleItemsWithoutCache(int, boolean):boolean");
    }

    @Override // androidx.leanback.widget.Grid
    public final int findRowMax(boolean z, int i, int[] iArr) {
        int i2;
        int edge = ((GridLayoutManager.AnonymousClass2) this.mProvider).getEdge(i);
        StaggeredGrid.Location location = getLocation(i);
        int i3 = location.row;
        if (this.mReversedFlow) {
            i2 = i3;
            int i4 = i2;
            int i5 = 1;
            int i6 = edge;
            for (int i7 = i + 1; i5 < this.mNumRows && i7 <= this.mLastVisibleIndex; i7++) {
                StaggeredGrid.Location location2 = getLocation(i7);
                i6 += location2.offset;
                int i8 = location2.row;
                if (i8 != i4) {
                    i5++;
                    if (!z ? i6 >= edge : i6 <= edge) {
                        i4 = i8;
                    } else {
                        edge = i6;
                        i = i7;
                        i2 = i8;
                        i4 = i2;
                    }
                }
            }
        } else {
            int i9 = 1;
            int i10 = i3;
            StaggeredGrid.Location location3 = location;
            int i11 = edge;
            edge = ((GridLayoutManager.AnonymousClass2) this.mProvider).getSize(i) + edge;
            i2 = i10;
            for (int i12 = i - 1; i9 < this.mNumRows && i12 >= this.mFirstVisibleIndex; i12--) {
                i11 -= location3.offset;
                location3 = getLocation(i12);
                int i13 = location3.row;
                if (i13 != i10) {
                    i9++;
                    int size = ((GridLayoutManager.AnonymousClass2) this.mProvider).getSize(i12) + i11;
                    if (!z ? size >= edge : size <= edge) {
                        i10 = i13;
                    } else {
                        edge = size;
                        i = i12;
                        i2 = i13;
                        i10 = i2;
                    }
                }
            }
        }
        if (iArr != null) {
            iArr[0] = i2;
            iArr[1] = i;
        }
        return edge;
    }

    @Override // androidx.leanback.widget.Grid
    public final int findRowMin(boolean z, int i, int[] iArr) {
        int i2;
        int edge = ((GridLayoutManager.AnonymousClass2) this.mProvider).getEdge(i);
        StaggeredGrid.Location location = getLocation(i);
        int i3 = location.row;
        if (this.mReversedFlow) {
            int i4 = 1;
            i2 = edge - ((GridLayoutManager.AnonymousClass2) this.mProvider).getSize(i);
            int i5 = i3;
            for (int i6 = i - 1; i4 < this.mNumRows && i6 >= this.mFirstVisibleIndex; i6--) {
                edge -= location.offset;
                location = getLocation(i6);
                int i7 = location.row;
                if (i7 != i5) {
                    i4++;
                    int size = edge - ((GridLayoutManager.AnonymousClass2) this.mProvider).getSize(i6);
                    if (!z ? size >= i2 : size <= i2) {
                        i5 = i7;
                    } else {
                        i2 = size;
                        i = i6;
                        i3 = i7;
                        i5 = i3;
                    }
                }
            }
        } else {
            int i8 = i3;
            int i9 = i8;
            int i10 = 1;
            int i11 = edge;
            for (int i12 = i + 1; i10 < this.mNumRows && i12 <= this.mLastVisibleIndex; i12++) {
                StaggeredGrid.Location location2 = getLocation(i12);
                i11 += location2.offset;
                int i13 = location2.row;
                if (i13 != i9) {
                    i10++;
                    if (!z ? i11 >= edge : i11 <= edge) {
                        i9 = i13;
                    } else {
                        edge = i11;
                        i = i12;
                        i8 = i13;
                        i9 = i8;
                    }
                }
            }
            i2 = edge;
            i3 = i8;
        }
        if (iArr != null) {
            iArr[0] = i3;
            iArr[1] = i;
        }
        return i2;
    }

    public final int getRowMax(int i) {
        int i2;
        StaggeredGrid.Location location;
        int i3 = this.mFirstVisibleIndex;
        if (i3 < 0) {
            return Integer.MIN_VALUE;
        }
        if (this.mReversedFlow) {
            int edge = ((GridLayoutManager.AnonymousClass2) this.mProvider).getEdge(i3);
            if (getLocation(this.mFirstVisibleIndex).row == i) {
                return edge;
            }
            int i4 = this.mFirstVisibleIndex;
            do {
                i4++;
                if (i4 <= getLastIndex()) {
                    location = getLocation(i4);
                    edge += location.offset;
                }
            } while (location.row != i);
            return edge;
        }
        int edge2 = ((GridLayoutManager.AnonymousClass2) this.mProvider).getEdge(this.mLastVisibleIndex);
        StaggeredGrid.Location location2 = getLocation(this.mLastVisibleIndex);
        if (location2.row != i) {
            int i5 = this.mLastVisibleIndex;
            while (true) {
                i5--;
                if (i5 < this.mFirstIndex) {
                    break;
                }
                edge2 -= location2.offset;
                location2 = getLocation(i5);
                if (location2.row == i) {
                    i2 = location2.size;
                    break;
                }
            }
        } else {
            i2 = location2.size;
        }
        return edge2 + i2;
        return Integer.MIN_VALUE;
    }

    public final int getRowMin(int i) {
        StaggeredGrid.Location location;
        int i2;
        int i3 = this.mFirstVisibleIndex;
        if (i3 < 0) {
            return Integer.MAX_VALUE;
        }
        if (this.mReversedFlow) {
            int edge = ((GridLayoutManager.AnonymousClass2) this.mProvider).getEdge(this.mLastVisibleIndex);
            StaggeredGrid.Location location2 = getLocation(this.mLastVisibleIndex);
            if (location2.row != i) {
                int i4 = this.mLastVisibleIndex;
                while (true) {
                    i4--;
                    if (i4 < this.mFirstIndex) {
                        break;
                    }
                    edge -= location2.offset;
                    location2 = getLocation(i4);
                    if (location2.row == i) {
                        i2 = location2.size;
                        break;
                    }
                }
            } else {
                i2 = location2.size;
            }
            return edge - i2;
        }
        int edge2 = ((GridLayoutManager.AnonymousClass2) this.mProvider).getEdge(i3);
        if (getLocation(this.mFirstVisibleIndex).row == i) {
            return edge2;
        }
        int i5 = this.mFirstVisibleIndex;
        do {
            i5++;
            if (i5 <= getLastIndex()) {
                location = getLocation(i5);
                edge2 += location.offset;
            }
        } while (location.row != i);
        return edge2;
        return Integer.MAX_VALUE;
    }

    /* JADX WARN: Code restructure failed: missing block: B:105:0x0140, code lost:
        return r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0128, code lost:
        return true;
     */
    @Override // androidx.leanback.widget.StaggeredGrid
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean prependVisibleItemsWithoutCache(int r13, boolean r14) {
        /*
            Method dump skipped, instructions count: 343
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.StaggeredGridDefault.prependVisibleItemsWithoutCache(int, boolean):boolean");
    }
}
