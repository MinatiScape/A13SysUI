package kotlin.collections;

import java.util.AbstractList;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.markers.KMappedMarker;
/* compiled from: AbstractMutableList.kt */
/* loaded from: classes.dex */
public abstract class AbstractMutableList<E> extends AbstractList<E> implements KMappedMarker {
    @Override // java.util.AbstractList, java.util.List
    public final E remove(int i) {
        ArrayDeque arrayDeque = (ArrayDeque) this;
        int i2 = arrayDeque.size;
        if (i < 0 || i >= i2) {
            throw new IndexOutOfBoundsException("index: " + i + ", size: " + i2);
        } else if (i == SetsKt__SetsKt.getLastIndex(arrayDeque)) {
            if (!arrayDeque.isEmpty()) {
                int access$positiveMod = ArrayDeque.access$positiveMod(arrayDeque, arrayDeque.head + SetsKt__SetsKt.getLastIndex(arrayDeque));
                Object[] objArr = arrayDeque.elementData;
                E e = (E) objArr[access$positiveMod];
                objArr[access$positiveMod] = null;
                arrayDeque.size--;
                return e;
            }
            throw new NoSuchElementException("ArrayDeque is empty.");
        } else if (i == 0) {
            return (E) arrayDeque.removeFirst();
        } else {
            int access$positiveMod2 = ArrayDeque.access$positiveMod(arrayDeque, arrayDeque.head + i);
            Object[] objArr2 = arrayDeque.elementData;
            E e2 = (E) objArr2[access$positiveMod2];
            if (i < (arrayDeque.size >> 1)) {
                int i3 = arrayDeque.head;
                if (access$positiveMod2 >= i3) {
                    System.arraycopy(objArr2, i3, objArr2, i3 + 1, access$positiveMod2 - i3);
                } else {
                    System.arraycopy(objArr2, 0, objArr2, 1, access$positiveMod2 - 0);
                    Object[] objArr3 = arrayDeque.elementData;
                    objArr3[0] = objArr3[objArr3.length - 1];
                    int i4 = arrayDeque.head;
                    System.arraycopy(objArr3, i4, objArr3, i4 + 1, (objArr3.length - 1) - i4);
                }
                Object[] objArr4 = arrayDeque.elementData;
                int i5 = arrayDeque.head;
                objArr4[i5] = null;
                arrayDeque.head = arrayDeque.incremented(i5);
            } else {
                int access$positiveMod3 = ArrayDeque.access$positiveMod(arrayDeque, arrayDeque.head + SetsKt__SetsKt.getLastIndex(arrayDeque));
                if (access$positiveMod2 <= access$positiveMod3) {
                    Object[] objArr5 = arrayDeque.elementData;
                    int i6 = access$positiveMod2 + 1;
                    System.arraycopy(objArr5, i6, objArr5, access$positiveMod2, (access$positiveMod3 + 1) - i6);
                } else {
                    Object[] objArr6 = arrayDeque.elementData;
                    int i7 = access$positiveMod2 + 1;
                    System.arraycopy(objArr6, i7, objArr6, access$positiveMod2, objArr6.length - i7);
                    Object[] objArr7 = arrayDeque.elementData;
                    objArr7[objArr7.length - 1] = objArr7[0];
                    System.arraycopy(objArr7, 1, objArr7, 0, (access$positiveMod3 + 1) - 1);
                }
                arrayDeque.elementData[access$positiveMod3] = null;
            }
            arrayDeque.size--;
            return e2;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return ((ArrayDeque) this).size;
    }
}
