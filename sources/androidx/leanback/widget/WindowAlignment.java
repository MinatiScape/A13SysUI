package androidx.leanback.widget;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
/* loaded from: classes.dex */
public final class WindowAlignment {
    public final Axis horizontal;
    public Axis mMainAxis;
    public Axis mSecondAxis;
    public final Axis vertical;

    /* loaded from: classes.dex */
    public static class Axis {
        public int mMaxScroll;
        public int mMinScroll;
        public int mPaddingMax;
        public int mPaddingMin;
        public boolean mReversedFlow;
        public int mSize;
        public int mWindowAlignment = 3;
        public int mMinEdge = Integer.MIN_VALUE;
        public int mMaxEdge = Integer.MAX_VALUE;

        public final int getScroll(int i) {
            int i2;
            boolean z;
            int i3;
            int i4;
            int i5 = this.mSize;
            boolean z2 = this.mReversedFlow;
            boolean z3 = false;
            if (!z2) {
                i2 = ((int) ((i5 * 50.0f) / 100.0f)) + 0;
            } else {
                i2 = (i5 + 0) - ((int) ((i5 * 50.0f) / 100.0f));
            }
            int i6 = this.mMinEdge;
            if (i6 == Integer.MIN_VALUE) {
                z = true;
            } else {
                z = false;
            }
            int i7 = this.mMaxEdge;
            if (i7 == Integer.MAX_VALUE) {
                z3 = true;
            }
            if (!z) {
                int i8 = this.mPaddingMin;
                int i9 = i2 - i8;
                int i10 = this.mWindowAlignment;
                if (z2 ? (i10 & 2) != 0 : (i10 & 1) != 0) {
                    if (i - i6 <= i9) {
                        int i11 = i6 - i8;
                        if (z3 || i11 <= (i4 = this.mMaxScroll)) {
                            return i11;
                        }
                        return i4;
                    }
                }
            }
            if (!z3) {
                int i12 = this.mPaddingMax;
                int i13 = (i5 - i2) - i12;
                if (z2 ? (this.mWindowAlignment & 1) != 0 : (this.mWindowAlignment & 2) != 0) {
                    if (i7 - i <= i13) {
                        int i14 = i7 - (i5 - i12);
                        if (z || i14 >= (i3 = this.mMinScroll)) {
                            return i14;
                        }
                        return i3;
                    }
                }
            }
            return i - i2;
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(" min:");
            m.append(this.mMinEdge);
            m.append(" ");
            m.append(this.mMinScroll);
            m.append(" max:");
            m.append(this.mMaxEdge);
            m.append(" ");
            m.append(this.mMaxScroll);
            return m.toString();
        }

        public final void updateMinMax(int i, int i2, int i3, int i4) {
            int i5;
            boolean z;
            this.mMinEdge = i;
            this.mMaxEdge = i2;
            int i6 = this.mSize;
            int i7 = this.mPaddingMin;
            int i8 = (i6 - i7) - this.mPaddingMax;
            boolean z2 = this.mReversedFlow;
            boolean z3 = false;
            if (!z2) {
                i5 = ((int) ((i6 * 50.0f) / 100.0f)) + 0;
            } else {
                i5 = (i6 + 0) - ((int) ((i6 * 50.0f) / 100.0f));
            }
            if (i == Integer.MIN_VALUE) {
                z = true;
            } else {
                z = false;
            }
            if (i2 == Integer.MAX_VALUE) {
                z3 = true;
            }
            if (!z) {
                int i9 = this.mWindowAlignment;
                if (z2 ? (i9 & 2) == 0 : (i9 & 1) == 0) {
                    this.mMinScroll = i3 - i5;
                } else {
                    this.mMinScroll = i - i7;
                }
            }
            if (!z3) {
                int i10 = this.mWindowAlignment;
                if (z2 ? (i10 & 1) == 0 : (i10 & 2) == 0) {
                    this.mMaxScroll = i4 - i5;
                } else {
                    this.mMaxScroll = (i2 - i7) - i8;
                }
            }
            if (!z3 && !z) {
                if (!z2) {
                    int i11 = this.mWindowAlignment;
                    if ((i11 & 1) != 0) {
                        this.mMaxScroll = Math.max(this.mMinScroll, this.mMaxScroll);
                    } else if ((i11 & 2) != 0) {
                        int max = Math.max(this.mMaxScroll, i3 - i5);
                        this.mMaxScroll = max;
                        this.mMinScroll = Math.min(this.mMinScroll, max);
                    }
                } else {
                    int i12 = this.mWindowAlignment;
                    if ((i12 & 1) != 0) {
                        this.mMinScroll = Math.min(this.mMinScroll, this.mMaxScroll);
                    } else if ((i12 & 2) != 0) {
                        int min = Math.min(this.mMinScroll, i4 - i5);
                        this.mMinScroll = min;
                        this.mMaxScroll = Math.max(min, this.mMaxScroll);
                    }
                }
            }
        }
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("horizontal=");
        m.append(this.horizontal);
        m.append("; vertical=");
        m.append(this.vertical);
        return m.toString();
    }

    public WindowAlignment() {
        Axis axis = new Axis();
        this.vertical = axis;
        Axis axis2 = new Axis();
        this.horizontal = axis2;
        this.mMainAxis = axis2;
        this.mSecondAxis = axis;
    }
}
