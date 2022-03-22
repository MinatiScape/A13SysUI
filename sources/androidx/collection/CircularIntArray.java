package androidx.collection;
/* loaded from: classes.dex */
public final class CircularIntArray {
    public int mCapacityBitmask;
    public int[] mElements;
    public int mTail;

    public final void addLast(int i) {
        int[] iArr = this.mElements;
        int i2 = this.mTail;
        iArr[i2] = i;
        int i3 = this.mCapacityBitmask & (i2 + 1);
        this.mTail = i3;
        if (i3 == 0) {
            int length = iArr.length;
            int i4 = length + 0;
            int i5 = length << 1;
            if (i5 >= 0) {
                int[] iArr2 = new int[i5];
                System.arraycopy(iArr, 0, iArr2, 0, i4);
                System.arraycopy(this.mElements, 0, iArr2, i4, 0);
                this.mElements = iArr2;
                this.mTail = length;
                this.mCapacityBitmask = i5 - 1;
                return;
            }
            throw new RuntimeException("Max array capacity exceeded");
        }
    }

    public CircularIntArray() {
        int highestOneBit = Integer.bitCount(8) != 1 ? Integer.highestOneBit(7) << 1 : 8;
        this.mCapacityBitmask = highestOneBit - 1;
        this.mElements = new int[highestOneBit];
    }
}
