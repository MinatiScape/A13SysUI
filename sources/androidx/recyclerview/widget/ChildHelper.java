package androidx.recyclerview.widget;

import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class ChildHelper {
    public final Callback mCallback;
    public final Bucket mBucket = new Bucket();
    public final ArrayList mHiddenViews = new ArrayList();

    /* loaded from: classes.dex */
    public static class Bucket {
        public long mData = 0;
        public Bucket mNext;

        public final void clear(int i) {
            if (i >= 64) {
                Bucket bucket = this.mNext;
                if (bucket != null) {
                    bucket.clear(i - 64);
                    return;
                }
                return;
            }
            this.mData &= ~(1 << i);
        }

        public final int countOnesBefore(int i) {
            Bucket bucket = this.mNext;
            if (bucket == null) {
                if (i >= 64) {
                    return Long.bitCount(this.mData);
                }
                return Long.bitCount(((1 << i) - 1) & this.mData);
            } else if (i < 64) {
                return Long.bitCount(((1 << i) - 1) & this.mData);
            } else {
                return Long.bitCount(this.mData) + bucket.countOnesBefore(i - 64);
            }
        }

        public final void ensureNext() {
            if (this.mNext == null) {
                this.mNext = new Bucket();
            }
        }

        public final boolean get(int i) {
            if (i >= 64) {
                ensureNext();
                return this.mNext.get(i - 64);
            }
            if (((1 << i) & this.mData) != 0) {
                return true;
            }
            return false;
        }

        public final void insert(int i, boolean z) {
            boolean z2;
            if (i >= 64) {
                ensureNext();
                this.mNext.insert(i - 64, z);
                return;
            }
            long j = this.mData;
            if ((Long.MIN_VALUE & j) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            long j2 = (1 << i) - 1;
            this.mData = ((j & (~j2)) << 1) | (j & j2);
            if (z) {
                set(i);
            } else {
                clear(i);
            }
            if (z2 || this.mNext != null) {
                ensureNext();
                this.mNext.insert(0, z2);
            }
        }

        public final boolean remove(int i) {
            boolean z;
            if (i >= 64) {
                ensureNext();
                return this.mNext.remove(i - 64);
            }
            long j = 1 << i;
            long j2 = this.mData;
            if ((j2 & j) != 0) {
                z = true;
            } else {
                z = false;
            }
            long j3 = j2 & (~j);
            this.mData = j3;
            long j4 = j - 1;
            this.mData = (j3 & j4) | Long.rotateRight((~j4) & j3, 1);
            Bucket bucket = this.mNext;
            if (bucket != null) {
                if (bucket.get(0)) {
                    set(63);
                }
                this.mNext.remove(0);
            }
            return z;
        }

        public final void reset() {
            this.mData = 0L;
            Bucket bucket = this.mNext;
            if (bucket != null) {
                bucket.reset();
            }
        }

        public final void set(int i) {
            if (i >= 64) {
                ensureNext();
                this.mNext.set(i - 64);
                return;
            }
            this.mData |= 1 << i;
        }

        public final String toString() {
            if (this.mNext == null) {
                return Long.toBinaryString(this.mData);
            }
            return this.mNext.toString() + "xx" + Long.toBinaryString(this.mData);
        }
    }

    /* loaded from: classes.dex */
    public interface Callback {
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0021, code lost:
        if (r4.mBucket.get(r2) == false) goto L_0x0026;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0023, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0026, code lost:
        return r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int getOffset(int r5) {
        /*
            r4 = this;
            r0 = -1
            if (r5 >= 0) goto L_0x0004
            return r0
        L_0x0004:
            androidx.recyclerview.widget.ChildHelper$Callback r1 = r4.mCallback
            androidx.recyclerview.widget.RecyclerView$5 r1 = (androidx.recyclerview.widget.RecyclerView.AnonymousClass5) r1
            int r1 = r1.getChildCount()
            r2 = r5
        L_0x000d:
            if (r2 >= r1) goto L_0x0029
            androidx.recyclerview.widget.ChildHelper$Bucket r3 = r4.mBucket
            int r3 = r3.countOnesBefore(r2)
            int r3 = r2 - r3
            int r3 = r5 - r3
            if (r3 != 0) goto L_0x0027
        L_0x001b:
            androidx.recyclerview.widget.ChildHelper$Bucket r5 = r4.mBucket
            boolean r5 = r5.get(r2)
            if (r5 == 0) goto L_0x0026
            int r2 = r2 + 1
            goto L_0x001b
        L_0x0026:
            return r2
        L_0x0027:
            int r2 = r2 + r3
            goto L_0x000d
        L_0x0029:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.ChildHelper.getOffset(int):int");
    }

    public final void addView(View view, int i, boolean z) {
        int i2;
        if (i < 0) {
            i2 = ((RecyclerView.AnonymousClass5) this.mCallback).getChildCount();
        } else {
            i2 = getOffset(i);
        }
        this.mBucket.insert(i2, z);
        if (z) {
            hideViewInternal(view);
        }
        RecyclerView.AnonymousClass5 r1 = (RecyclerView.AnonymousClass5) this.mCallback;
        Objects.requireNonNull(r1);
        RecyclerView.this.addView(view, i2);
        RecyclerView recyclerView = RecyclerView.this;
        Objects.requireNonNull(recyclerView);
        RecyclerView.ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
        RecyclerView.Adapter adapter = recyclerView.mAdapter;
        if (!(adapter == null || childViewHolderInt == null)) {
            adapter.onViewAttachedToWindow(childViewHolderInt);
        }
        ArrayList arrayList = recyclerView.mOnChildAttachStateListeners;
        if (arrayList != null) {
            int size = arrayList.size();
            while (true) {
                size--;
                if (size >= 0) {
                    ((RecyclerView.OnChildAttachStateChangeListener) recyclerView.mOnChildAttachStateListeners.get(size)).onChildViewAttachedToWindow(view);
                } else {
                    return;
                }
            }
        }
    }

    public final void attachViewToParent(View view, int i, ViewGroup.LayoutParams layoutParams, boolean z) {
        int i2;
        if (i < 0) {
            i2 = ((RecyclerView.AnonymousClass5) this.mCallback).getChildCount();
        } else {
            i2 = getOffset(i);
        }
        this.mBucket.insert(i2, z);
        if (z) {
            hideViewInternal(view);
        }
        RecyclerView.AnonymousClass5 r1 = (RecyclerView.AnonymousClass5) this.mCallback;
        Objects.requireNonNull(r1);
        RecyclerView.ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
        if (childViewHolderInt != null) {
            if (childViewHolderInt.isTmpDetached() || childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.mFlags &= -257;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Called attach on a child which is not detached: ");
                sb.append(childViewHolderInt);
                throw new IllegalArgumentException(ChildHelper$$ExternalSyntheticOutline0.m(RecyclerView.this, sb));
            }
        }
        RecyclerView.this.attachViewToParent(view, i2, layoutParams);
    }

    public final int getChildCount() {
        return ((RecyclerView.AnonymousClass5) this.mCallback).getChildCount() - this.mHiddenViews.size();
    }

    public final View getUnfilteredChildAt(int i) {
        RecyclerView.AnonymousClass5 r0 = (RecyclerView.AnonymousClass5) this.mCallback;
        Objects.requireNonNull(r0);
        return RecyclerView.this.getChildAt(i);
    }

    public final int getUnfilteredChildCount() {
        return ((RecyclerView.AnonymousClass5) this.mCallback).getChildCount();
    }

    public final void hideViewInternal(View view) {
        this.mHiddenViews.add(view);
        RecyclerView.AnonymousClass5 r2 = (RecyclerView.AnonymousClass5) this.mCallback;
        Objects.requireNonNull(r2);
        RecyclerView.ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
        if (childViewHolderInt != null) {
            RecyclerView recyclerView = RecyclerView.this;
            int i = childViewHolderInt.mPendingAccessibilityState;
            if (i != -1) {
                childViewHolderInt.mWasImportantForAccessibilityBeforeHidden = i;
            } else {
                View view2 = childViewHolderInt.itemView;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                childViewHolderInt.mWasImportantForAccessibilityBeforeHidden = ViewCompat.Api16Impl.getImportantForAccessibility(view2);
            }
            recyclerView.setChildImportantForAccessibilityInternal(childViewHolderInt, 4);
        }
    }

    public final int indexOfChild(View view) {
        RecyclerView.AnonymousClass5 r0 = (RecyclerView.AnonymousClass5) this.mCallback;
        Objects.requireNonNull(r0);
        int indexOfChild = RecyclerView.this.indexOfChild(view);
        if (indexOfChild != -1 && !this.mBucket.get(indexOfChild)) {
            return indexOfChild - this.mBucket.countOnesBefore(indexOfChild);
        }
        return -1;
    }

    public final boolean isHidden(View view) {
        return this.mHiddenViews.contains(view);
    }

    public final String toString() {
        return this.mBucket.toString() + ", hidden list:" + this.mHiddenViews.size();
    }

    public final boolean unhideViewInternal(View view) {
        if (!this.mHiddenViews.remove(view)) {
            return false;
        }
        RecyclerView.AnonymousClass5 r2 = (RecyclerView.AnonymousClass5) this.mCallback;
        Objects.requireNonNull(r2);
        RecyclerView.ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
        if (childViewHolderInt == null) {
            return true;
        }
        RecyclerView.this.setChildImportantForAccessibilityInternal(childViewHolderInt, childViewHolderInt.mWasImportantForAccessibilityBeforeHidden);
        childViewHolderInt.mWasImportantForAccessibilityBeforeHidden = 0;
        return true;
    }

    public ChildHelper(RecyclerView.AnonymousClass5 r1) {
        this.mCallback = r1;
    }

    public final void detachViewFromParent(int i) {
        RecyclerView.ViewHolder childViewHolderInt;
        int offset = getOffset(i);
        this.mBucket.remove(offset);
        RecyclerView.AnonymousClass5 r3 = (RecyclerView.AnonymousClass5) this.mCallback;
        Objects.requireNonNull(r3);
        View childAt = RecyclerView.this.getChildAt(offset);
        if (!(childAt == null || (childViewHolderInt = RecyclerView.getChildViewHolderInt(childAt)) == null)) {
            if (!childViewHolderInt.isTmpDetached() || childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.addFlags(256);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("called detach on an already detached child ");
                sb.append(childViewHolderInt);
                throw new IllegalArgumentException(ChildHelper$$ExternalSyntheticOutline0.m(RecyclerView.this, sb));
            }
        }
        RecyclerView.this.detachViewFromParent(offset);
    }

    public final View getChildAt(int i) {
        int offset = getOffset(i);
        RecyclerView.AnonymousClass5 r0 = (RecyclerView.AnonymousClass5) this.mCallback;
        Objects.requireNonNull(r0);
        return RecyclerView.this.getChildAt(offset);
    }
}
