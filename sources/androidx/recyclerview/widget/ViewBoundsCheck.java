package androidx.recyclerview.widget;

import android.view.View;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ViewBoundsCheck {
    public BoundFlags mBoundFlags = new BoundFlags();
    public final Callback mCallback;

    /* loaded from: classes.dex */
    public static class BoundFlags {
        public int mBoundFlags = 0;
        public int mChildEnd;
        public int mChildStart;
        public int mRvEnd;
        public int mRvStart;

        public final boolean boundsMatch() {
            int i;
            int i2;
            int i3;
            int i4 = this.mBoundFlags;
            int i5 = 2;
            if ((i4 & 7) != 0) {
                int i6 = this.mChildStart;
                int i7 = this.mRvStart;
                if (i6 > i7) {
                    i3 = 1;
                } else if (i6 == i7) {
                    i3 = 2;
                } else {
                    i3 = 4;
                }
                if (((i3 << 0) & i4) == 0) {
                    return false;
                }
            }
            if ((i4 & 112) != 0) {
                int i8 = this.mChildStart;
                int i9 = this.mRvEnd;
                if (i8 > i9) {
                    i2 = 1;
                } else if (i8 == i9) {
                    i2 = 2;
                } else {
                    i2 = 4;
                }
                if (((i2 << 4) & i4) == 0) {
                    return false;
                }
            }
            if ((i4 & 1792) != 0) {
                int i10 = this.mChildEnd;
                int i11 = this.mRvStart;
                if (i10 > i11) {
                    i = 1;
                } else if (i10 == i11) {
                    i = 2;
                } else {
                    i = 4;
                }
                if (((i << 8) & i4) == 0) {
                    return false;
                }
            }
            if ((i4 & 28672) != 0) {
                int i12 = this.mChildEnd;
                int i13 = this.mRvEnd;
                if (i12 > i13) {
                    i5 = 1;
                } else if (i12 != i13) {
                    i5 = 4;
                }
                if (((i5 << 12) & i4) == 0) {
                    return false;
                }
            }
            return true;
        }
    }

    /* loaded from: classes.dex */
    public interface Callback {
        View getChildAt(int i);

        int getChildEnd(View view);

        int getChildStart(View view);

        int getParentEnd();

        int getParentStart();
    }

    public final View findOneViewWithinBoundFlags(int i, int i2, int i3, int i4) {
        int i5;
        int parentStart = this.mCallback.getParentStart();
        int parentEnd = this.mCallback.getParentEnd();
        if (i2 > i) {
            i5 = 1;
        } else {
            i5 = -1;
        }
        View view = null;
        while (i != i2) {
            View childAt = this.mCallback.getChildAt(i);
            int childStart = this.mCallback.getChildStart(childAt);
            int childEnd = this.mCallback.getChildEnd(childAt);
            BoundFlags boundFlags = this.mBoundFlags;
            Objects.requireNonNull(boundFlags);
            boundFlags.mRvStart = parentStart;
            boundFlags.mRvEnd = parentEnd;
            boundFlags.mChildStart = childStart;
            boundFlags.mChildEnd = childEnd;
            if (i3 != 0) {
                BoundFlags boundFlags2 = this.mBoundFlags;
                Objects.requireNonNull(boundFlags2);
                boundFlags2.mBoundFlags = 0;
                BoundFlags boundFlags3 = this.mBoundFlags;
                Objects.requireNonNull(boundFlags3);
                boundFlags3.mBoundFlags |= i3;
                if (this.mBoundFlags.boundsMatch()) {
                    return childAt;
                }
            }
            if (i4 != 0) {
                BoundFlags boundFlags4 = this.mBoundFlags;
                Objects.requireNonNull(boundFlags4);
                boundFlags4.mBoundFlags = 0;
                BoundFlags boundFlags5 = this.mBoundFlags;
                Objects.requireNonNull(boundFlags5);
                boundFlags5.mBoundFlags |= i4;
                if (this.mBoundFlags.boundsMatch()) {
                    view = childAt;
                }
            }
            i += i5;
        }
        return view;
    }

    public final boolean isViewWithinBoundFlags(View view) {
        BoundFlags boundFlags = this.mBoundFlags;
        int parentStart = this.mCallback.getParentStart();
        int parentEnd = this.mCallback.getParentEnd();
        int childStart = this.mCallback.getChildStart(view);
        int childEnd = this.mCallback.getChildEnd(view);
        Objects.requireNonNull(boundFlags);
        boundFlags.mRvStart = parentStart;
        boundFlags.mRvEnd = parentEnd;
        boundFlags.mChildStart = childStart;
        boundFlags.mChildEnd = childEnd;
        BoundFlags boundFlags2 = this.mBoundFlags;
        Objects.requireNonNull(boundFlags2);
        boundFlags2.mBoundFlags = 0;
        BoundFlags boundFlags3 = this.mBoundFlags;
        Objects.requireNonNull(boundFlags3);
        boundFlags3.mBoundFlags |= 24579;
        return this.mBoundFlags.boundsMatch();
    }

    public ViewBoundsCheck(Callback callback) {
        this.mCallback = callback;
    }
}
