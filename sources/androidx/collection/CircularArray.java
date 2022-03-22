package androidx.collection;
/* loaded from: classes.dex */
public final class CircularArray<E> {
    public int mCapacityBitmask;
    public E[] mElements;
    public int mHead;
    public int mTail;

    public final void doubleCapacity() {
        E[] eArr = this.mElements;
        int length = eArr.length;
        int i = this.mHead;
        int i2 = length - i;
        int i3 = length << 1;
        if (i3 >= 0) {
            E[] eArr2 = (E[]) new Object[i3];
            System.arraycopy(eArr, i, eArr2, 0, i2);
            System.arraycopy(this.mElements, 0, eArr2, i2, this.mHead);
            this.mElements = eArr2;
            this.mHead = 0;
            this.mTail = length;
            this.mCapacityBitmask = i3 - 1;
            return;
        }
        throw new RuntimeException("Max array capacity exceeded");
    }

    public final void removeFromEnd(int i) {
        int i2;
        if (i > 0) {
            if (i <= size()) {
                int i3 = 0;
                int i4 = this.mTail;
                if (i < i4) {
                    i3 = i4 - i;
                }
                int i5 = i3;
                while (true) {
                    i2 = this.mTail;
                    if (i5 >= i2) {
                        break;
                    }
                    this.mElements[i5] = null;
                    i5++;
                }
                int i6 = i2 - i3;
                int i7 = i - i6;
                this.mTail = i2 - i6;
                if (i7 > 0) {
                    int length = this.mElements.length;
                    this.mTail = length;
                    int i8 = length - i7;
                    for (int i9 = i8; i9 < this.mTail; i9++) {
                        this.mElements[i9] = null;
                    }
                    this.mTail = i8;
                    return;
                }
                return;
            }
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public final void removeFromStart(int i) {
        if (i > 0) {
            if (i <= size()) {
                int length = this.mElements.length;
                int i2 = this.mHead;
                if (i < length - i2) {
                    length = i2 + i;
                }
                while (i2 < length) {
                    this.mElements[i2] = null;
                    i2++;
                }
                int i3 = this.mHead;
                int i4 = length - i3;
                int i5 = i - i4;
                this.mHead = this.mCapacityBitmask & (i3 + i4);
                if (i5 > 0) {
                    for (int i6 = 0; i6 < i5; i6++) {
                        this.mElements[i6] = null;
                    }
                    this.mHead = i5;
                    return;
                }
                return;
            }
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public final int size() {
        return this.mCapacityBitmask & (this.mTail - this.mHead);
    }

    public CircularArray() {
        int highestOneBit = Integer.bitCount(64) != 1 ? Integer.highestOneBit(63) << 1 : 64;
        this.mCapacityBitmask = highestOneBit - 1;
        this.mElements = (E[]) new Object[highestOneBit];
    }
}
