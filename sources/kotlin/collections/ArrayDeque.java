package kotlin.collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ArrayDeque.kt */
/* loaded from: classes.dex */
public final class ArrayDeque<E> extends AbstractMutableList<E> {
    public static final Object[] emptyElementData = new Object[0];
    public Object[] elementData = emptyElementData;
    public int head;
    public int size;

    @Override // java.util.AbstractList, java.util.List
    public final void add(int i, E e) {
        int i2 = this.size;
        if (i < 0 || i > i2) {
            throw new IndexOutOfBoundsException("index: " + i + ", size: " + i2);
        } else if (i == i2) {
            addLast(e);
        } else if (i == 0) {
            ensureCapacity(i2 + 1);
            int i3 = this.head;
            if (i3 == 0) {
                i3 = this.elementData.length;
            }
            int i4 = i3 - 1;
            this.head = i4;
            this.elementData[i4] = e;
            this.size++;
        } else {
            ensureCapacity(i2 + 1);
            int access$positiveMod = access$positiveMod(this, this.head + i);
            int i5 = this.size;
            if (i < ((i5 + 1) >> 1)) {
                int length = access$positiveMod == 0 ? this.elementData.length - 1 : access$positiveMod - 1;
                int i6 = this.head;
                int length2 = i6 == 0 ? this.elementData.length - 1 : i6 - 1;
                if (length >= i6) {
                    Object[] objArr = this.elementData;
                    objArr[length2] = objArr[i6];
                    int i7 = i6 + 1;
                    System.arraycopy(objArr, i7, objArr, i6, (length + 1) - i7);
                } else {
                    Object[] objArr2 = this.elementData;
                    System.arraycopy(objArr2, i6, objArr2, i6 - 1, objArr2.length - i6);
                    Object[] objArr3 = this.elementData;
                    objArr3[objArr3.length - 1] = objArr3[0];
                    System.arraycopy(objArr3, 1, objArr3, 0, (length + 1) - 1);
                }
                this.elementData[length] = e;
                this.head = length2;
            } else {
                int access$positiveMod2 = access$positiveMod(this, this.head + i5);
                if (access$positiveMod < access$positiveMod2) {
                    Object[] objArr4 = this.elementData;
                    System.arraycopy(objArr4, access$positiveMod, objArr4, access$positiveMod + 1, access$positiveMod2 - access$positiveMod);
                } else {
                    Object[] objArr5 = this.elementData;
                    System.arraycopy(objArr5, 0, objArr5, 1, access$positiveMod2 - 0);
                    Object[] objArr6 = this.elementData;
                    objArr6[0] = objArr6[objArr6.length - 1];
                    System.arraycopy(objArr6, access$positiveMod, objArr6, access$positiveMod + 1, (objArr6.length - 1) - access$positiveMod);
                }
                this.elementData[access$positiveMod] = e;
            }
            this.size++;
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public final boolean addAll(int i, Collection<? extends E> collection) {
        int i2 = this.size;
        if (i < 0 || i > i2) {
            throw new IndexOutOfBoundsException("index: " + i + ", size: " + i2);
        } else if (collection.isEmpty()) {
            return false;
        } else {
            int i3 = this.size;
            if (i == i3) {
                return addAll(collection);
            }
            ensureCapacity(collection.size() + i3);
            int access$positiveMod = access$positiveMod(this, this.head + this.size);
            int access$positiveMod2 = access$positiveMod(this, this.head + i);
            int size = collection.size();
            if (i < ((this.size + 1) >> 1)) {
                int i4 = this.head;
                int i5 = i4 - size;
                if (access$positiveMod2 < i4) {
                    Object[] objArr = this.elementData;
                    System.arraycopy(objArr, i4, objArr, i5, objArr.length - i4);
                    if (size >= access$positiveMod2) {
                        Object[] objArr2 = this.elementData;
                        System.arraycopy(objArr2, 0, objArr2, objArr2.length - size, access$positiveMod2 + 0);
                    } else {
                        Object[] objArr3 = this.elementData;
                        System.arraycopy(objArr3, 0, objArr3, objArr3.length - size, size + 0);
                        Object[] objArr4 = this.elementData;
                        System.arraycopy(objArr4, size, objArr4, 0, access$positiveMod2 - size);
                    }
                } else if (i5 >= 0) {
                    Object[] objArr5 = this.elementData;
                    System.arraycopy(objArr5, i4, objArr5, i5, access$positiveMod2 - i4);
                } else {
                    Object[] objArr6 = this.elementData;
                    i5 += objArr6.length;
                    int i6 = access$positiveMod2 - i4;
                    int length = objArr6.length - i5;
                    if (length >= i6) {
                        System.arraycopy(objArr6, i4, objArr6, i5, i6);
                    } else {
                        System.arraycopy(objArr6, i4, objArr6, i5, (i4 + length) - i4);
                        Object[] objArr7 = this.elementData;
                        int i7 = this.head + length;
                        System.arraycopy(objArr7, i7, objArr7, 0, access$positiveMod2 - i7);
                    }
                }
                this.head = i5;
                int i8 = access$positiveMod2 - size;
                if (i8 < 0) {
                    i8 += this.elementData.length;
                }
                copyCollectionElements(i8, collection);
            } else {
                int i9 = access$positiveMod2 + size;
                if (access$positiveMod2 < access$positiveMod) {
                    int i10 = size + access$positiveMod;
                    Object[] objArr8 = this.elementData;
                    if (i10 <= objArr8.length) {
                        System.arraycopy(objArr8, access$positiveMod2, objArr8, i9, access$positiveMod - access$positiveMod2);
                    } else if (i9 >= objArr8.length) {
                        System.arraycopy(objArr8, access$positiveMod2, objArr8, i9 - objArr8.length, access$positiveMod - access$positiveMod2);
                    } else {
                        int length2 = access$positiveMod - (i10 - objArr8.length);
                        System.arraycopy(objArr8, length2, objArr8, 0, access$positiveMod - length2);
                        Object[] objArr9 = this.elementData;
                        System.arraycopy(objArr9, access$positiveMod2, objArr9, i9, length2 - access$positiveMod2);
                    }
                } else {
                    Object[] objArr10 = this.elementData;
                    System.arraycopy(objArr10, 0, objArr10, size, access$positiveMod - 0);
                    Object[] objArr11 = this.elementData;
                    if (i9 >= objArr11.length) {
                        System.arraycopy(objArr11, access$positiveMod2, objArr11, i9 - objArr11.length, objArr11.length - access$positiveMod2);
                    } else {
                        int length3 = objArr11.length - size;
                        System.arraycopy(objArr11, length3, objArr11, 0, objArr11.length - length3);
                        Object[] objArr12 = this.elementData;
                        System.arraycopy(objArr12, access$positiveMod2, objArr12, i9, (objArr12.length - size) - access$positiveMod2);
                    }
                }
                copyCollectionElements(access$positiveMod2, collection);
            }
            return true;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final Object[] toArray() {
        return toArray(new Object[this.size]);
    }

    public final void addLast(E e) {
        ensureCapacity(this.size + 1);
        this.elementData[access$positiveMod(this, this.head + this.size)] = e;
        this.size++;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final void clear() {
        int access$positiveMod = access$positiveMod(this, this.head + this.size);
        int i = this.head;
        if (i < access$positiveMod) {
            Arrays.fill(this.elementData, i, access$positiveMod, (Object) null);
        } else if (!isEmpty()) {
            Object[] objArr = this.elementData;
            Arrays.fill(objArr, this.head, objArr.length, (Object) null);
            Arrays.fill(this.elementData, 0, access$positiveMod, (Object) null);
        }
        this.head = 0;
        this.size = 0;
    }

    public final void ensureCapacity(int i) {
        if (i >= 0) {
            Object[] objArr = this.elementData;
            if (i > objArr.length) {
                if (objArr == emptyElementData) {
                    if (i < 10) {
                        i = 10;
                    }
                    this.elementData = new Object[i];
                    return;
                }
                int length = objArr.length;
                int i2 = length + (length >> 1);
                if (i2 - i < 0) {
                    i2 = i;
                }
                if (i2 - 2147483639 > 0) {
                    if (i > 2147483639) {
                        i2 = Integer.MAX_VALUE;
                    } else {
                        i2 = 2147483639;
                    }
                }
                Object[] objArr2 = new Object[i2];
                int i3 = this.head;
                System.arraycopy(objArr, i3, objArr2, 0, objArr.length - i3);
                Object[] objArr3 = this.elementData;
                int length2 = objArr3.length;
                int i4 = this.head;
                System.arraycopy(objArr3, 0, objArr2, length2 - i4, i4 - 0);
                this.head = 0;
                this.elementData = objArr2;
                return;
            }
            return;
        }
        throw new IllegalStateException("Deque is too big.");
    }

    @Override // java.util.AbstractList, java.util.List
    public final E get(int i) {
        int i2 = this.size;
        if (i < 0 || i >= i2) {
            throw new IndexOutOfBoundsException("index: " + i + ", size: " + i2);
        }
        return (E) this.elementData[access$positiveMod(this, this.head + i)];
    }

    public final int incremented(int i) {
        if (i == this.elementData.length - 1) {
            return 0;
        }
        return i + 1;
    }

    @Override // java.util.AbstractList, java.util.List
    public final int indexOf(Object obj) {
        int i;
        int access$positiveMod = access$positiveMod(this, this.head + this.size);
        int i2 = this.head;
        if (i2 < access$positiveMod) {
            while (i2 < access$positiveMod) {
                int i3 = i2 + 1;
                if (Intrinsics.areEqual(obj, this.elementData[i2])) {
                    i = this.head;
                } else {
                    i2 = i3;
                }
            }
            return -1;
        } else if (i2 < access$positiveMod) {
            return -1;
        } else {
            int length = this.elementData.length;
            while (true) {
                if (i2 < length) {
                    int i4 = i2 + 1;
                    if (Intrinsics.areEqual(obj, this.elementData[i2])) {
                        i = this.head;
                        break;
                    }
                    i2 = i4;
                } else {
                    int i5 = 0;
                    while (i5 < access$positiveMod) {
                        int i6 = i5 + 1;
                        if (Intrinsics.areEqual(obj, this.elementData[i5])) {
                            i2 = i5 + this.elementData.length;
                            i = this.head;
                        } else {
                            i5 = i6;
                        }
                    }
                    return -1;
                }
            }
        }
        return i2 - i;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean isEmpty() {
        if (this.size == 0) {
            return true;
        }
        return false;
    }

    @Override // java.util.AbstractList, java.util.List
    public final int lastIndexOf(Object obj) {
        int length;
        int i;
        int access$positiveMod = access$positiveMod(this, this.head + this.size);
        int i2 = this.head;
        if (i2 < access$positiveMod) {
            length = access$positiveMod - 1;
            if (i2 <= length) {
                while (true) {
                    int i3 = length - 1;
                    if (Intrinsics.areEqual(obj, this.elementData[length])) {
                        i = this.head;
                        break;
                    } else if (length == i2) {
                        break;
                    } else {
                        length = i3;
                    }
                }
                return length - i;
            }
            return -1;
        }
        if (i2 > access$positiveMod) {
            int i4 = access$positiveMod - 1;
            if (i4 >= 0) {
                while (true) {
                    int i5 = i4 - 1;
                    if (Intrinsics.areEqual(obj, this.elementData[i4])) {
                        length = i4 + this.elementData.length;
                        i = this.head;
                        break;
                    } else if (i5 < 0) {
                        break;
                    } else {
                        i4 = i5;
                    }
                }
                return length - i;
            }
            length = this.elementData.length - 1;
            int i6 = this.head;
            if (i6 <= length) {
                while (true) {
                    int i7 = length - 1;
                    if (Intrinsics.areEqual(obj, this.elementData[length])) {
                        i = this.head;
                        break;
                    } else if (length == i6) {
                        break;
                    } else {
                        length = i7;
                    }
                }
            }
        }
        return -1;
    }

    @Override // java.util.AbstractList, java.util.List
    public final E set(int i, E e) {
        int i2 = this.size;
        if (i < 0 || i >= i2) {
            throw new IndexOutOfBoundsException("index: " + i + ", size: " + i2);
        }
        int access$positiveMod = access$positiveMod(this, this.head + i);
        Object[] objArr = this.elementData;
        E e2 = (E) objArr[access$positiveMod];
        objArr[access$positiveMod] = e;
        return e2;
    }

    public static final int access$positiveMod(ArrayDeque arrayDeque, int i) {
        Objects.requireNonNull(arrayDeque);
        Object[] objArr = arrayDeque.elementData;
        if (i >= objArr.length) {
            return i - objArr.length;
        }
        return i;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean contains(Object obj) {
        if (indexOf(obj) != -1) {
            return true;
        }
        return false;
    }

    public final void copyCollectionElements(int i, Collection<? extends E> collection) {
        Iterator<? extends E> it = collection.iterator();
        int length = this.elementData.length;
        while (i < length) {
            int i2 = i + 1;
            if (!it.hasNext()) {
                break;
            }
            this.elementData[i] = it.next();
            i = i2;
        }
        int i3 = 0;
        int i4 = this.head;
        while (i3 < i4) {
            int i5 = i3 + 1;
            if (!it.hasNext()) {
                break;
            }
            this.elementData[i3] = it.next();
            i3 = i5;
        }
        this.size = collection.size() + this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean remove(Object obj) {
        int indexOf = indexOf(obj);
        if (indexOf == -1) {
            return false;
        }
        remove(indexOf);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean removeAll(Collection<? extends Object> collection) {
        Object[] objArr;
        int i;
        boolean z = false;
        z = false;
        int i2 = 0;
        z = false;
        if (!isEmpty()) {
            if (this.elementData.length == 0) {
                objArr = 1;
            } else {
                objArr = null;
            }
            if (objArr == null) {
                int access$positiveMod = access$positiveMod(this, this.head + this.size);
                int i3 = this.head;
                if (i3 < access$positiveMod) {
                    i = i3;
                    while (i3 < access$positiveMod) {
                        int i4 = i3 + 1;
                        Object obj = this.elementData[i3];
                        if (!collection.contains(obj)) {
                            this.elementData[i] = obj;
                            i3 = i4;
                            i++;
                        } else {
                            z = true;
                            i3 = i4;
                        }
                    }
                    Arrays.fill(this.elementData, i, access$positiveMod, (Object) null);
                } else {
                    int length = this.elementData.length;
                    boolean z2 = false;
                    int i5 = i3;
                    while (i3 < length) {
                        int i6 = i3 + 1;
                        Object[] objArr2 = this.elementData;
                        Object obj2 = objArr2[i3];
                        objArr2[i3] = null;
                        if (!collection.contains(obj2)) {
                            this.elementData[i5] = obj2;
                            i3 = i6;
                            i5++;
                        } else {
                            z2 = true;
                            i3 = i6;
                        }
                    }
                    i = access$positiveMod(this, i5);
                    while (i2 < access$positiveMod) {
                        int i7 = i2 + 1;
                        Object[] objArr3 = this.elementData;
                        Object obj3 = objArr3[i2];
                        objArr3[i2] = null;
                        if (!collection.contains(obj3)) {
                            this.elementData[i] = obj3;
                            i = incremented(i);
                        } else {
                            z2 = true;
                        }
                        i2 = i7;
                    }
                    z = z2;
                }
                if (z) {
                    int i8 = i - this.head;
                    if (i8 < 0) {
                        i8 += this.elementData.length;
                    }
                    this.size = i8;
                }
            }
        }
        return z;
    }

    public final E removeFirst() {
        if (!isEmpty()) {
            int i = this.head;
            Object[] objArr = this.elementData;
            E e = (E) objArr[i];
            objArr[i] = null;
            this.head = incremented(i);
            this.size--;
            return e;
        }
        throw new NoSuchElementException("ArrayDeque is empty.");
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean retainAll(Collection<? extends Object> collection) {
        Object[] objArr;
        int i;
        boolean z = false;
        z = false;
        int i2 = 0;
        z = false;
        if (!isEmpty()) {
            if (this.elementData.length == 0) {
                objArr = 1;
            } else {
                objArr = null;
            }
            if (objArr == null) {
                int access$positiveMod = access$positiveMod(this, this.head + this.size);
                int i3 = this.head;
                if (i3 < access$positiveMod) {
                    i = i3;
                    while (i3 < access$positiveMod) {
                        int i4 = i3 + 1;
                        Object obj = this.elementData[i3];
                        if (collection.contains(obj)) {
                            this.elementData[i] = obj;
                            i3 = i4;
                            i++;
                        } else {
                            z = true;
                            i3 = i4;
                        }
                    }
                    Arrays.fill(this.elementData, i, access$positiveMod, (Object) null);
                } else {
                    int length = this.elementData.length;
                    boolean z2 = false;
                    int i5 = i3;
                    while (i3 < length) {
                        int i6 = i3 + 1;
                        Object[] objArr2 = this.elementData;
                        Object obj2 = objArr2[i3];
                        objArr2[i3] = null;
                        if (collection.contains(obj2)) {
                            this.elementData[i5] = obj2;
                            i3 = i6;
                            i5++;
                        } else {
                            z2 = true;
                            i3 = i6;
                        }
                    }
                    i = access$positiveMod(this, i5);
                    while (i2 < access$positiveMod) {
                        int i7 = i2 + 1;
                        Object[] objArr3 = this.elementData;
                        Object obj3 = objArr3[i2];
                        objArr3[i2] = null;
                        if (collection.contains(obj3)) {
                            this.elementData[i] = obj3;
                            i = incremented(i);
                        } else {
                            z2 = true;
                        }
                        i2 = i7;
                    }
                    z = z2;
                }
                if (z) {
                    int i8 = i - this.head;
                    if (i8 < 0) {
                        i8 += this.elementData.length;
                    }
                    this.size = i8;
                }
            }
        }
        return z;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final <T> T[] toArray(T[] tArr) {
        int length = tArr.length;
        int i = this.size;
        if (length < i) {
            Object newInstance = Array.newInstance(tArr.getClass().getComponentType(), i);
            Objects.requireNonNull(newInstance, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.arrayOfNulls>");
            tArr = (T[]) ((Object[]) newInstance);
        }
        int access$positiveMod = access$positiveMod(this, this.head + this.size);
        int i2 = this.head;
        if (i2 < access$positiveMod) {
            ArraysKt___ArraysKt.copyInto$default(this.elementData, tArr, 0, i2, access$positiveMod, 2);
        } else if (!isEmpty()) {
            Object[] objArr = this.elementData;
            int i3 = this.head;
            System.arraycopy(objArr, i3, tArr, 0, objArr.length - i3);
            Object[] objArr2 = this.elementData;
            System.arraycopy(objArr2, 0, tArr, objArr2.length - this.head, access$positiveMod - 0);
        }
        int length2 = tArr.length;
        int i4 = this.size;
        if (length2 > i4) {
            tArr[i4] = null;
        }
        return tArr;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection<? extends E> collection) {
        if (collection.isEmpty()) {
            return false;
        }
        ensureCapacity(collection.size() + this.size);
        copyCollectionElements(access$positiveMod(this, this.head + this.size), collection);
        return true;
    }
}
