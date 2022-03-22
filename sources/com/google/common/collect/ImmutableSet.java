package com.google.common.collect;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import okio.Okio__OkioKt;
/* loaded from: classes.dex */
public abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E> {
    public static final /* synthetic */ int $r8$clinit = 0;
    @LazyInit
    public transient ImmutableList<E> asList;

    /* loaded from: classes.dex */
    public static class Builder<E> extends ImmutableCollection.ArrayBasedBuilder<E> {
        public Object[] hashTable;
    }

    /* loaded from: classes.dex */
    public static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        public final Object[] elements;

        public Object readResolve() {
            Object[] objArr = this.elements;
            int i = ImmutableSet.$r8$clinit;
            int length = objArr.length;
            if (length == 0) {
                return RegularImmutableSet.EMPTY;
            }
            if (length != 1) {
                return ImmutableSet.construct(objArr.length, (Object[]) objArr.clone());
            }
            return new SingletonImmutableSet(objArr[0]);
        }

        public SerializedForm(Object[] objArr) {
            this.elements = objArr;
        }
    }

    public static int chooseTableSize(int i) {
        int max = Math.max(i, 2);
        boolean z = true;
        if (max < 751619276) {
            int highestOneBit = Integer.highestOneBit(max - 1) << 1;
            while (highestOneBit * 0.7d < max) {
                highestOneBit <<= 1;
            }
            return highestOneBit;
        }
        if (max >= 1073741824) {
            z = false;
        }
        if (z) {
            return 1073741824;
        }
        throw new IllegalArgumentException("collection too large");
    }

    public boolean isHashCodeFast() {
        return this instanceof RegularImmutableSet;
    }

    public static <E> ImmutableSet<E> construct(int i, Object... objArr) {
        if (i == 0) {
            return RegularImmutableSet.EMPTY;
        }
        boolean z = false;
        if (i != 1) {
            int chooseTableSize = chooseTableSize(i);
            Object[] objArr2 = new Object[chooseTableSize];
            int i2 = chooseTableSize - 1;
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < i; i5++) {
                Object obj = objArr[i5];
                if (obj != null) {
                    int hashCode = obj.hashCode();
                    int smear = Okio__OkioKt.smear(hashCode);
                    while (true) {
                        int i6 = smear & i2;
                        Object obj2 = objArr2[i6];
                        if (obj2 == null) {
                            objArr[i4] = obj;
                            objArr2[i6] = obj;
                            i3 += hashCode;
                            i4++;
                            break;
                        } else if (obj2.equals(obj)) {
                            break;
                        } else {
                            smear++;
                        }
                    }
                } else {
                    throw new NullPointerException(VendorAtomValue$$ExternalSyntheticOutline0.m("at index ", i5));
                }
            }
            Arrays.fill(objArr, i4, i, (Object) null);
            if (i4 == 1) {
                Object obj3 = objArr[0];
                Objects.requireNonNull(obj3);
                return new SingletonImmutableSet(obj3);
            } else if (chooseTableSize(i4) < chooseTableSize / 2) {
                return construct(i4, objArr);
            } else {
                int length = objArr.length;
                if (i4 < (length >> 1) + (length >> 2)) {
                    z = true;
                }
                if (z) {
                    objArr = Arrays.copyOf(objArr, i4);
                }
                return new RegularImmutableSet(objArr, i3, objArr2, i2, i4);
            }
        } else {
            Object obj4 = objArr[0];
            Objects.requireNonNull(obj4);
            return new SingletonImmutableSet(obj4);
        }
    }

    public ImmutableList<E> asList() {
        ImmutableList<E> immutableList = this.asList;
        if (immutableList != null) {
            return immutableList;
        }
        ImmutableList<E> createAsList = createAsList();
        this.asList = createAsList;
        return createAsList;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ImmutableSet) || !isHashCodeFast() || !((ImmutableSet) obj).isHashCodeFast() || hashCode() == obj.hashCode()) {
            return Sets.equalsImpl(this, obj);
        }
        return false;
    }

    @Override // com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(toArray());
    }

    public ImmutableList<E> createAsList() {
        Object[] array = toArray();
        ImmutableList.Itr itr = ImmutableList.EMPTY_ITR;
        return ImmutableList.asImmutableList(array, array.length);
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        return Sets.hashCodeImpl(this);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: iterator */
    public /* bridge */ /* synthetic */ Iterator mo174iterator() {
        return iterator();
    }
}
