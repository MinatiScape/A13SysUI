package androidx.viewpager2.widget;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ScrollEventAdapter extends RecyclerView.OnScrollListener {
    public int mAdapterState;
    public ViewPager2.OnPageChangeCallback mCallback;
    public boolean mDataSetChangeHappened;
    public boolean mDispatchSelected;
    public int mDragStartPosition;
    public boolean mFakeDragging;
    public final LinearLayoutManager mLayoutManager;
    public final RecyclerView mRecyclerView;
    public boolean mScrollHappened;
    public int mScrollState;
    public ScrollEventValues mScrollValues = new ScrollEventValues();
    public int mTarget;
    public final ViewPager2 mViewPager;

    /* loaded from: classes.dex */
    public static final class ScrollEventValues {
        public float mOffset;
        public int mOffsetPx;
        public int mPosition;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0027, code lost:
        if (r6 == r7) goto L_0x002c;
     */
    /* JADX WARN: Removed duplicated region for block: B:18:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0043  */
    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onScrolled(androidx.recyclerview.widget.RecyclerView r5, int r6, int r7) {
        /*
            r4 = this;
            r5 = 1
            r4.mScrollHappened = r5
            r4.updateScrollEventValues()
            boolean r0 = r4.mDispatchSelected
            r1 = 0
            r2 = -1
            if (r0 == 0) goto L_0x004b
            r4.mDispatchSelected = r1
            if (r7 > 0) goto L_0x002c
            if (r7 != 0) goto L_0x002a
            if (r6 >= 0) goto L_0x0016
            r6 = r5
            goto L_0x0017
        L_0x0016:
            r6 = r1
        L_0x0017:
            androidx.viewpager2.widget.ViewPager2 r7 = r4.mViewPager
            java.util.Objects.requireNonNull(r7)
            androidx.viewpager2.widget.ViewPager2$LinearLayoutManagerImpl r7 = r7.mLayoutManager
            int r7 = r7.getLayoutDirection()
            if (r7 != r5) goto L_0x0026
            r7 = r5
            goto L_0x0027
        L_0x0026:
            r7 = r1
        L_0x0027:
            if (r6 != r7) goto L_0x002a
            goto L_0x002c
        L_0x002a:
            r6 = r1
            goto L_0x002d
        L_0x002c:
            r6 = r5
        L_0x002d:
            if (r6 == 0) goto L_0x0039
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r6 = r4.mScrollValues
            int r7 = r6.mOffsetPx
            if (r7 == 0) goto L_0x0039
            int r6 = r6.mPosition
            int r6 = r6 + r5
            goto L_0x003d
        L_0x0039:
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r6 = r4.mScrollValues
            int r6 = r6.mPosition
        L_0x003d:
            r4.mTarget = r6
            int r7 = r4.mDragStartPosition
            if (r7 == r6) goto L_0x005d
            androidx.viewpager2.widget.ViewPager2$OnPageChangeCallback r7 = r4.mCallback
            if (r7 == 0) goto L_0x005d
            r7.onPageSelected(r6)
            goto L_0x005d
        L_0x004b:
            int r6 = r4.mAdapterState
            if (r6 != 0) goto L_0x005d
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r6 = r4.mScrollValues
            int r6 = r6.mPosition
            if (r6 != r2) goto L_0x0056
            r6 = r1
        L_0x0056:
            androidx.viewpager2.widget.ViewPager2$OnPageChangeCallback r7 = r4.mCallback
            if (r7 == 0) goto L_0x005d
            r7.onPageSelected(r6)
        L_0x005d:
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r6 = r4.mScrollValues
            int r7 = r6.mPosition
            if (r7 != r2) goto L_0x0064
            r7 = r1
        L_0x0064:
            float r0 = r6.mOffset
            int r6 = r6.mOffsetPx
            androidx.viewpager2.widget.ViewPager2$OnPageChangeCallback r3 = r4.mCallback
            if (r3 == 0) goto L_0x006f
            r3.onPageScrolled(r7, r0, r6)
        L_0x006f:
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r6 = r4.mScrollValues
            int r7 = r6.mPosition
            int r0 = r4.mTarget
            if (r7 == r0) goto L_0x0079
            if (r0 != r2) goto L_0x0087
        L_0x0079:
            int r6 = r6.mOffsetPx
            if (r6 != 0) goto L_0x0087
            int r6 = r4.mScrollState
            if (r6 == r5) goto L_0x0087
            r4.dispatchStateChanged(r1)
            r4.resetState()
        L_0x0087:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.viewpager2.widget.ScrollEventAdapter.onScrolled(androidx.recyclerview.widget.RecyclerView, int, int):void");
    }

    public final void resetState() {
        this.mAdapterState = 0;
        this.mScrollState = 0;
        ScrollEventValues scrollEventValues = this.mScrollValues;
        Objects.requireNonNull(scrollEventValues);
        scrollEventValues.mPosition = -1;
        scrollEventValues.mOffset = 0.0f;
        scrollEventValues.mOffsetPx = 0;
        this.mDragStartPosition = -1;
        this.mTarget = -1;
        this.mDispatchSelected = false;
        this.mScrollHappened = false;
        this.mFakeDragging = false;
        this.mDataSetChangeHappened = false;
    }

    public final void dispatchStateChanged(int i) {
        if ((this.mAdapterState != 3 || this.mScrollState != 0) && this.mScrollState != i) {
            this.mScrollState = i;
            ViewPager2.OnPageChangeCallback onPageChangeCallback = this.mCallback;
            if (onPageChangeCallback != null) {
                onPageChangeCallback.onPageScrollStateChanged(i);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
    public final void onScrollStateChanged(RecyclerView recyclerView, int i) {
        boolean z;
        boolean z2;
        ViewPager2.OnPageChangeCallback onPageChangeCallback;
        ViewPager2.OnPageChangeCallback onPageChangeCallback2;
        int i2 = this.mAdapterState;
        boolean z3 = true;
        if (!(i2 == 1 && this.mScrollState == 1) && i == 1) {
            this.mFakeDragging = false;
            this.mAdapterState = 1;
            int i3 = this.mTarget;
            if (i3 != -1) {
                this.mDragStartPosition = i3;
                this.mTarget = -1;
            } else if (this.mDragStartPosition == -1) {
                this.mDragStartPosition = this.mLayoutManager.findFirstVisibleItemPosition();
            }
            dispatchStateChanged(1);
            return;
        }
        if (i2 == 1 || i2 == 4) {
            z = true;
        } else {
            z = false;
        }
        if (!z || i != 2) {
            if (i2 == 1 || i2 == 4) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2 && i == 0) {
                updateScrollEventValues();
                if (!this.mScrollHappened) {
                    int i4 = this.mScrollValues.mPosition;
                    if (!(i4 == -1 || (onPageChangeCallback2 = this.mCallback) == null)) {
                        onPageChangeCallback2.onPageScrolled(i4, 0.0f, 0);
                    }
                } else {
                    ScrollEventValues scrollEventValues = this.mScrollValues;
                    if (scrollEventValues.mOffsetPx == 0) {
                        int i5 = this.mDragStartPosition;
                        int i6 = scrollEventValues.mPosition;
                        if (!(i5 == i6 || (onPageChangeCallback = this.mCallback) == null)) {
                            onPageChangeCallback.onPageSelected(i6);
                        }
                    } else {
                        z3 = false;
                    }
                }
                if (z3) {
                    dispatchStateChanged(0);
                    resetState();
                }
            }
            if (this.mAdapterState == 2 && i == 0 && this.mDataSetChangeHappened) {
                updateScrollEventValues();
                ScrollEventValues scrollEventValues2 = this.mScrollValues;
                if (scrollEventValues2.mOffsetPx == 0) {
                    int i7 = this.mTarget;
                    int i8 = scrollEventValues2.mPosition;
                    if (i7 != i8) {
                        if (i8 == -1) {
                            i8 = 0;
                        }
                        ViewPager2.OnPageChangeCallback onPageChangeCallback3 = this.mCallback;
                        if (onPageChangeCallback3 != null) {
                            onPageChangeCallback3.onPageSelected(i8);
                        }
                    }
                    dispatchStateChanged(0);
                    resetState();
                }
            }
        } else if (this.mScrollHappened) {
            dispatchStateChanged(2);
            this.mDispatchSelected = true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:61:0x0168, code lost:
        if (r4[r12 - 1][1] >= r2) goto L_0x016b;
     */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0181  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0195  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x019a  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01a2  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0192 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateScrollEventValues() {
        /*
            Method dump skipped, instructions count: 452
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.viewpager2.widget.ScrollEventAdapter.updateScrollEventValues():void");
    }

    public ScrollEventAdapter(ViewPager2 viewPager2) {
        this.mViewPager = viewPager2;
        ViewPager2.RecyclerViewImpl recyclerViewImpl = viewPager2.mRecyclerView;
        this.mRecyclerView = recyclerViewImpl;
        Objects.requireNonNull(recyclerViewImpl);
        this.mLayoutManager = (LinearLayoutManager) recyclerViewImpl.mLayout;
        resetState();
    }
}
