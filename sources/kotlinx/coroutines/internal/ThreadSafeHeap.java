package kotlinx.coroutines.internal;

import java.lang.Comparable;
import java.util.Arrays;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicInt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.EventLoopImplBase;
import kotlinx.coroutines.internal.ThreadSafeHeapNode;
/* compiled from: ThreadSafeHeap.kt */
/* loaded from: classes.dex */
public class ThreadSafeHeap<T extends ThreadSafeHeapNode & Comparable<? super T>> {
    public final AtomicInt _size = new AtomicInt();
    public T[] a;

    public final void addImpl(T t) {
        boolean z = DebugKt.DEBUG;
        t.setHeap((EventLoopImplBase.DelayedTaskQueue) this);
        T[] tArr = this.a;
        if (tArr == null) {
            tArr = (T[]) new ThreadSafeHeapNode[4];
            this.a = tArr;
        } else if (getSize() >= tArr.length) {
            tArr = (T[]) ((ThreadSafeHeapNode[]) Arrays.copyOf(tArr, getSize() * 2));
            this.a = tArr;
        }
        int size = getSize();
        this._size.setValue(size + 1);
        tArr[size] = t;
        t.setIndex(size);
        siftUpFrom(size);
    }

    public final int getSize() {
        AtomicInt atomicInt = this._size;
        Objects.requireNonNull(atomicInt);
        return atomicInt.value;
    }

    public final T removeAtImpl(int i) {
        boolean z = DebugKt.DEBUG;
        T[] tArr = this.a;
        Intrinsics.checkNotNull(tArr);
        this._size.setValue(getSize() - 1);
        if (i < getSize()) {
            swap(i, getSize());
            int i2 = (i - 1) / 2;
            if (i > 0) {
                T t = tArr[i];
                Intrinsics.checkNotNull(t);
                T t2 = tArr[i2];
                Intrinsics.checkNotNull(t2);
                if (((Comparable) t).compareTo(t2) < 0) {
                    swap(i, i2);
                    siftUpFrom(i2);
                }
            }
            while (true) {
                int i3 = (i * 2) + 1;
                if (i3 >= getSize()) {
                    break;
                }
                T[] tArr2 = this.a;
                Intrinsics.checkNotNull(tArr2);
                int i4 = i3 + 1;
                if (i4 < getSize()) {
                    T t3 = tArr2[i4];
                    Intrinsics.checkNotNull(t3);
                    T t4 = tArr2[i3];
                    Intrinsics.checkNotNull(t4);
                    if (((Comparable) t3).compareTo(t4) < 0) {
                        i3 = i4;
                    }
                }
                T t5 = tArr2[i];
                Intrinsics.checkNotNull(t5);
                T t6 = tArr2[i3];
                Intrinsics.checkNotNull(t6);
                if (((Comparable) t5).compareTo(t6) <= 0) {
                    break;
                }
                swap(i, i3);
                i = i3;
            }
        }
        T t7 = tArr[getSize()];
        Intrinsics.checkNotNull(t7);
        boolean z2 = DebugKt.DEBUG;
        t7.setHeap(null);
        t7.setIndex(-1);
        tArr[getSize()] = null;
        return t7;
    }

    public final void siftUpFrom(int i) {
        while (i > 0) {
            T[] tArr = this.a;
            Intrinsics.checkNotNull(tArr);
            int i2 = (i - 1) / 2;
            T t = tArr[i2];
            Intrinsics.checkNotNull(t);
            T t2 = tArr[i];
            Intrinsics.checkNotNull(t2);
            if (((Comparable) t).compareTo(t2) > 0) {
                swap(i, i2);
                i = i2;
            } else {
                return;
            }
        }
    }

    public final void swap(int i, int i2) {
        T[] tArr = this.a;
        Intrinsics.checkNotNull(tArr);
        T t = tArr[i2];
        Intrinsics.checkNotNull(t);
        T t2 = tArr[i];
        Intrinsics.checkNotNull(t2);
        tArr[i] = t;
        tArr[i2] = t2;
        t.setIndex(i);
        t2.setIndex(i2);
    }
}
