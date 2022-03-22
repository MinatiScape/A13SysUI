package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.MapMakerInternalMap;
import com.google.common.util.concurrent.FuturesGetChecked;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public abstract class Ordering<T> implements Comparator<T> {

    /* loaded from: classes.dex */
    public static class ArbitraryOrdering extends Ordering<Object> {
        public final AtomicInteger counter = new AtomicInteger(0);
        public final AbstractMap uids;

        public final String toString() {
            return "Ordering.arbitrary()";
        }

        @Override // com.google.common.collect.Ordering, java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            if (obj == obj2) {
                return 0;
            }
            if (obj == null) {
                return -1;
            }
            if (obj2 == null) {
                return 1;
            }
            int identityHashCode = System.identityHashCode(obj);
            int identityHashCode2 = System.identityHashCode(obj2);
            if (identityHashCode == identityHashCode2) {
                int compareTo = getUid(obj).compareTo(getUid(obj2));
                if (compareTo != 0) {
                    return compareTo;
                }
                throw new AssertionError();
            } else if (identityHashCode < identityHashCode2) {
                return -1;
            } else {
                return 1;
            }
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [java.util.AbstractMap, java.util.concurrent.ConcurrentMap] */
        public final Integer getUid(Object obj) {
            Integer num = (Integer) this.uids.get(obj);
            if (num != null) {
                return num;
            }
            Integer valueOf = Integer.valueOf(this.counter.getAndIncrement());
            Integer num2 = (Integer) this.uids.putIfAbsent(obj, valueOf);
            if (num2 != null) {
                return num2;
            }
            return valueOf;
        }

        public ArbitraryOrdering() {
            boolean z = false;
            MapMaker mapMaker = new MapMaker();
            MapMakerInternalMap.Strength.AnonymousClass2 r2 = MapMakerInternalMap.Strength.WEAK;
            MapMakerInternalMap.Strength strength = mapMaker.keyStrength;
            Preconditions.checkState(strength == null ? true : z, "Key strength was already set to %s", strength);
            mapMaker.keyStrength = r2;
            mapMaker.useCustomMap = true;
            this.uids = (AbstractMap) mapMaker.makeMap();
        }
    }

    /* loaded from: classes.dex */
    public static class IncomparableValueException extends ClassCastException {
        private static final long serialVersionUID = 0;
        public final Object value;
    }

    @Override // java.util.Comparator
    @CanIgnoreReturnValue
    public abstract int compare(T t, T t2);

    public final Ordering onResultOf(FuturesGetChecked.AnonymousClass1 r2) {
        return new ByFunctionOrdering(r2, this);
    }

    public <S extends T> Ordering<S> reverse() {
        return new ReverseOrdering(this);
    }

    public static <C extends Comparable> Ordering<C> natural() {
        return NaturalOrdering.INSTANCE;
    }
}
