package androidx.collection;

import androidx.recyclerview.R$dimen;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public class SimpleArrayMap<K, V> {
    public static Object[] mBaseCache;
    public static int mBaseCacheSize;
    public static Object[] mTwiceBaseCache;
    public static int mTwiceBaseCacheSize;
    public Object[] mArray;
    public int[] mHashes;
    public int mSize;

    public SimpleArrayMap() {
        this.mHashes = R$dimen.EMPTY_INTS;
        this.mArray = R$dimen.EMPTY_OBJECTS;
        this.mSize = 0;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SimpleArrayMap) {
            SimpleArrayMap simpleArrayMap = (SimpleArrayMap) obj;
            int i = this.mSize;
            Objects.requireNonNull(simpleArrayMap);
            if (i != simpleArrayMap.mSize) {
                return false;
            }
            for (int i2 = 0; i2 < this.mSize; i2++) {
                K keyAt = keyAt(i2);
                V valueAt = valueAt(i2);
                Object orDefault = simpleArrayMap.getOrDefault(keyAt, null);
                if (valueAt == null) {
                    if (orDefault != null || !simpleArrayMap.containsKey(keyAt)) {
                        return false;
                    }
                } else if (!valueAt.equals(orDefault)) {
                    return false;
                }
            }
            return true;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (this.mSize != map.size()) {
                return false;
            }
            for (int i3 = 0; i3 < this.mSize; i3++) {
                K keyAt2 = keyAt(i3);
                V valueAt2 = valueAt(i3);
                Object obj2 = map.get(keyAt2);
                if (valueAt2 == null) {
                    if (obj2 != null || !map.containsKey(keyAt2)) {
                        return false;
                    }
                } else if (!valueAt2.equals(obj2)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public final V get(Object obj) {
        return getOrDefault(obj, null);
    }

    public final V putIfAbsent(K k, V v) {
        V orDefault = getOrDefault(k, null);
        if (orDefault == null) {
            return put(k, v);
        }
        return orDefault;
    }

    public final V remove(Object obj) {
        int indexOfKey = indexOfKey(obj);
        if (indexOfKey >= 0) {
            return removeAt(indexOfKey);
        }
        return null;
    }

    public final V replace(K k, V v) {
        int indexOfKey = indexOfKey(k);
        if (indexOfKey < 0) {
            return null;
        }
        int i = (indexOfKey << 1) + 1;
        Object[] objArr = this.mArray;
        V v2 = (V) objArr[i];
        objArr[i] = v;
        return v2;
    }

    private void allocArrays(int i) {
        if (i == 8) {
            synchronized (SimpleArrayMap.class) {
                Object[] objArr = mTwiceBaseCache;
                if (objArr != null) {
                    this.mArray = objArr;
                    mTwiceBaseCache = (Object[]) objArr[0];
                    this.mHashes = (int[]) objArr[1];
                    objArr[1] = null;
                    objArr[0] = null;
                    mTwiceBaseCacheSize--;
                    return;
                }
            }
        } else if (i == 4) {
            synchronized (SimpleArrayMap.class) {
                Object[] objArr2 = mBaseCache;
                if (objArr2 != null) {
                    this.mArray = objArr2;
                    mBaseCache = (Object[]) objArr2[0];
                    this.mHashes = (int[]) objArr2[1];
                    objArr2[1] = null;
                    objArr2[0] = null;
                    mBaseCacheSize--;
                    return;
                }
            }
        }
        this.mHashes = new int[i];
        this.mArray = new Object[i << 1];
    }

    public static void freeArrays(int[] iArr, Object[] objArr, int i) {
        if (iArr.length == 8) {
            synchronized (SimpleArrayMap.class) {
                if (mTwiceBaseCacheSize < 10) {
                    objArr[0] = mTwiceBaseCache;
                    objArr[1] = iArr;
                    for (int i2 = (i << 1) - 1; i2 >= 2; i2--) {
                        objArr[i2] = null;
                    }
                    mTwiceBaseCache = objArr;
                    mTwiceBaseCacheSize++;
                }
            }
        } else if (iArr.length == 4) {
            synchronized (SimpleArrayMap.class) {
                if (mBaseCacheSize < 10) {
                    objArr[0] = mBaseCache;
                    objArr[1] = iArr;
                    for (int i3 = (i << 1) - 1; i3 >= 2; i3--) {
                        objArr[i3] = null;
                    }
                    mBaseCache = objArr;
                    mBaseCacheSize++;
                }
            }
        }
    }

    public final void clear() {
        int i = this.mSize;
        if (i > 0) {
            int[] iArr = this.mHashes;
            Object[] objArr = this.mArray;
            this.mHashes = R$dimen.EMPTY_INTS;
            this.mArray = R$dimen.EMPTY_OBJECTS;
            this.mSize = 0;
            freeArrays(iArr, objArr, i);
        }
        if (this.mSize > 0) {
            throw new ConcurrentModificationException();
        }
    }

    public final void ensureCapacity(int i) {
        int i2 = this.mSize;
        int[] iArr = this.mHashes;
        if (iArr.length < i) {
            Object[] objArr = this.mArray;
            allocArrays(i);
            if (this.mSize > 0) {
                System.arraycopy(iArr, 0, this.mHashes, 0, i2);
                System.arraycopy(objArr, 0, this.mArray, 0, i2 << 1);
            }
            freeArrays(iArr, objArr, i2);
        }
        if (this.mSize != i2) {
            throw new ConcurrentModificationException();
        }
    }

    public final int hashCode() {
        int i;
        int[] iArr = this.mHashes;
        Object[] objArr = this.mArray;
        int i2 = this.mSize;
        int i3 = 1;
        int i4 = 0;
        int i5 = 0;
        while (i4 < i2) {
            Object obj = objArr[i3];
            int i6 = iArr[i4];
            if (obj == null) {
                i = 0;
            } else {
                i = obj.hashCode();
            }
            i5 += i ^ i6;
            i4++;
            i3 += 2;
        }
        return i5;
    }

    public final int indexOf(Object obj, int i) {
        int i2 = this.mSize;
        if (i2 == 0) {
            return -1;
        }
        try {
            int binarySearch = R$dimen.binarySearch(this.mHashes, i2, i);
            if (binarySearch < 0) {
                return binarySearch;
            }
            if (obj.equals(this.mArray[binarySearch << 1])) {
                return binarySearch;
            }
            int i3 = binarySearch + 1;
            while (i3 < i2 && this.mHashes[i3] == i) {
                if (obj.equals(this.mArray[i3 << 1])) {
                    return i3;
                }
                i3++;
            }
            for (int i4 = binarySearch - 1; i4 >= 0 && this.mHashes[i4] == i; i4--) {
                if (obj.equals(this.mArray[i4 << 1])) {
                    return i4;
                }
            }
            return ~i3;
        } catch (ArrayIndexOutOfBoundsException unused) {
            throw new ConcurrentModificationException();
        }
    }

    public final int indexOfKey(Object obj) {
        if (obj == null) {
            return indexOfNull();
        }
        return indexOf(obj, obj.hashCode());
    }

    public final int indexOfNull() {
        int i = this.mSize;
        if (i == 0) {
            return -1;
        }
        try {
            int binarySearch = R$dimen.binarySearch(this.mHashes, i, 0);
            if (binarySearch < 0) {
                return binarySearch;
            }
            if (this.mArray[binarySearch << 1] == null) {
                return binarySearch;
            }
            int i2 = binarySearch + 1;
            while (i2 < i && this.mHashes[i2] == 0) {
                if (this.mArray[i2 << 1] == null) {
                    return i2;
                }
                i2++;
            }
            for (int i3 = binarySearch - 1; i3 >= 0 && this.mHashes[i3] == 0; i3--) {
                if (this.mArray[i3 << 1] == null) {
                    return i3;
                }
            }
            return ~i2;
        } catch (ArrayIndexOutOfBoundsException unused) {
            throw new ConcurrentModificationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int indexOfValue(Object obj) {
        int i = this.mSize * 2;
        Object[] objArr = this.mArray;
        if (obj == null) {
            for (int i2 = 1; i2 < i; i2 += 2) {
                if (objArr[i2] == null) {
                    return i2 >> 1;
                }
            }
            return -1;
        }
        for (int i3 = 1; i3 < i; i3 += 2) {
            if (obj.equals(objArr[i3])) {
                return i3 >> 1;
            }
        }
        return -1;
    }

    public final boolean isEmpty() {
        if (this.mSize <= 0) {
            return true;
        }
        return false;
    }

    public final K keyAt(int i) {
        return (K) this.mArray[i << 1];
    }

    public final V put(K k, V v) {
        int i;
        int i2;
        int i3 = this.mSize;
        if (k == null) {
            i2 = indexOfNull();
            i = 0;
        } else {
            int hashCode = k.hashCode();
            i = hashCode;
            i2 = indexOf(k, hashCode);
        }
        if (i2 >= 0) {
            int i4 = (i2 << 1) + 1;
            Object[] objArr = this.mArray;
            V v2 = (V) objArr[i4];
            objArr[i4] = v;
            return v2;
        }
        int i5 = ~i2;
        int[] iArr = this.mHashes;
        if (i3 >= iArr.length) {
            int i6 = 4;
            if (i3 >= 8) {
                i6 = (i3 >> 1) + i3;
            } else if (i3 >= 4) {
                i6 = 8;
            }
            Object[] objArr2 = this.mArray;
            allocArrays(i6);
            if (i3 == this.mSize) {
                int[] iArr2 = this.mHashes;
                if (iArr2.length > 0) {
                    System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                    System.arraycopy(objArr2, 0, this.mArray, 0, objArr2.length);
                }
                freeArrays(iArr, objArr2, i3);
            } else {
                throw new ConcurrentModificationException();
            }
        }
        if (i5 < i3) {
            int[] iArr3 = this.mHashes;
            int i7 = i5 + 1;
            System.arraycopy(iArr3, i5, iArr3, i7, i3 - i5);
            Object[] objArr3 = this.mArray;
            System.arraycopy(objArr3, i5 << 1, objArr3, i7 << 1, (this.mSize - i5) << 1);
        }
        int i8 = this.mSize;
        if (i3 == i8) {
            int[] iArr4 = this.mHashes;
            if (i5 < iArr4.length) {
                iArr4[i5] = i;
                Object[] objArr4 = this.mArray;
                int i9 = i5 << 1;
                objArr4[i9] = k;
                objArr4[i9 + 1] = v;
                this.mSize = i8 + 1;
                return null;
            }
        }
        throw new ConcurrentModificationException();
    }

    public final V removeAt(int i) {
        Object[] objArr = this.mArray;
        int i2 = i << 1;
        V v = (V) objArr[i2 + 1];
        int i3 = this.mSize;
        if (i3 <= 1) {
            clear();
        } else {
            int i4 = i3 - 1;
            int[] iArr = this.mHashes;
            int i5 = 8;
            if (iArr.length <= 8 || i3 >= iArr.length / 3) {
                if (i < i4) {
                    int i6 = i + 1;
                    int i7 = i4 - i;
                    System.arraycopy(iArr, i6, iArr, i, i7);
                    Object[] objArr2 = this.mArray;
                    System.arraycopy(objArr2, i6 << 1, objArr2, i2, i7 << 1);
                }
                Object[] objArr3 = this.mArray;
                int i8 = i4 << 1;
                objArr3[i8] = null;
                objArr3[i8 + 1] = null;
            } else {
                if (i3 > 8) {
                    i5 = i3 + (i3 >> 1);
                }
                allocArrays(i5);
                if (i3 == this.mSize) {
                    if (i > 0) {
                        System.arraycopy(iArr, 0, this.mHashes, 0, i);
                        System.arraycopy(objArr, 0, this.mArray, 0, i2);
                    }
                    if (i < i4) {
                        int i9 = i + 1;
                        int i10 = i4 - i;
                        System.arraycopy(iArr, i9, this.mHashes, i, i10);
                        System.arraycopy(objArr, i9 << 1, this.mArray, i2, i10 << 1);
                    }
                } else {
                    throw new ConcurrentModificationException();
                }
            }
            if (i3 == this.mSize) {
                this.mSize = i4;
            } else {
                throw new ConcurrentModificationException();
            }
        }
        return v;
    }

    public final V valueAt(int i) {
        return (V) this.mArray[(i << 1) + 1];
    }

    public final boolean containsKey(Object obj) {
        if (indexOfKey(obj) >= 0) {
            return true;
        }
        return false;
    }

    public final boolean containsValue(Object obj) {
        if (indexOfValue(obj) >= 0) {
            return true;
        }
        return false;
    }

    public final V getOrDefault(Object obj, V v) {
        int indexOfKey = indexOfKey(obj);
        if (indexOfKey >= 0) {
            return (V) this.mArray[(indexOfKey << 1) + 1];
        }
        return v;
    }

    public final boolean remove(Object obj, Object obj2) {
        int indexOfKey = indexOfKey(obj);
        if (indexOfKey < 0) {
            return false;
        }
        V valueAt = valueAt(indexOfKey);
        if (obj2 != valueAt && (obj2 == null || !obj2.equals(valueAt))) {
            return false;
        }
        removeAt(indexOfKey);
        return true;
    }

    public final String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.mSize * 28);
        sb.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            K keyAt = keyAt(i);
            if (keyAt != this) {
                sb.append(keyAt);
            } else {
                sb.append("(this Map)");
            }
            sb.append('=');
            V valueAt = valueAt(i);
            if (valueAt != this) {
                sb.append(valueAt);
            } else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public final boolean replace(K k, V v, V v2) {
        int indexOfKey = indexOfKey(k);
        if (indexOfKey < 0) {
            return false;
        }
        V valueAt = valueAt(indexOfKey);
        if (valueAt != v && (v == null || !v.equals(valueAt))) {
            return false;
        }
        int i = (indexOfKey << 1) + 1;
        Object[] objArr = this.mArray;
        Object obj = objArr[i];
        objArr[i] = v2;
        return true;
    }

    public SimpleArrayMap(int i) {
        if (i == 0) {
            this.mHashes = R$dimen.EMPTY_INTS;
            this.mArray = R$dimen.EMPTY_OBJECTS;
        } else {
            allocArrays(i);
        }
        this.mSize = 0;
    }

    public final int size() {
        return this.mSize;
    }
}
