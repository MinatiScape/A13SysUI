package kotlin.collections;

import android.graphics.Rect;
import androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import androidx.leanback.R$styleable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: _Collections.kt */
/* loaded from: classes.dex */
public class CollectionsKt___CollectionsKt extends CollectionsKt___CollectionsJvmKt {
    public static final <T> List<T> drop(Iterable<? extends T> iterable, int i) {
        boolean z;
        ArrayList arrayList;
        Object obj;
        int i2 = 0;
        if (i >= 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            throw new IllegalArgumentException(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Requested element count ", i, " is less than zero.").toString());
        } else if (i == 0) {
            return toList(iterable);
        } else {
            if (iterable instanceof Collection) {
                Collection collection = (Collection) iterable;
                int size = collection.size() - i;
                if (size <= 0) {
                    return EmptyList.INSTANCE;
                }
                if (size == 1) {
                    if (iterable instanceof List) {
                        obj = last((List) iterable);
                    } else {
                        Iterator<? extends T> it = iterable.iterator();
                        if (it.hasNext()) {
                            Object next = it.next();
                            while (it.hasNext()) {
                                next = it.next();
                            }
                            obj = next;
                        } else {
                            throw new NoSuchElementException("Collection is empty.");
                        }
                    }
                    return Collections.singletonList(obj);
                }
                arrayList = new ArrayList(size);
                if (iterable instanceof List) {
                    if (iterable instanceof RandomAccess) {
                        int size2 = collection.size();
                        while (i < size2) {
                            arrayList.add(((List) iterable).get(i));
                            i++;
                        }
                    } else {
                        ListIterator listIterator = ((List) iterable).listIterator(i);
                        while (listIterator.hasNext()) {
                            arrayList.add(listIterator.next());
                        }
                    }
                    return arrayList;
                }
            } else {
                arrayList = new ArrayList();
            }
            for (Object obj2 : iterable) {
                if (i2 >= i) {
                    arrayList.add(obj2);
                } else {
                    i2++;
                }
            }
            return SetsKt__SetsKt.optimizeReadOnlyList(arrayList);
        }
    }

    public static /* synthetic */ Appendable joinTo$default(List list, StringBuilder sb, String str) {
        joinTo(list, sb, str, "", "", -1, "...", null);
        return sb;
    }

    public static final ArrayList plus(Collection collection, Object obj) {
        ArrayList arrayList = new ArrayList(collection.size() + 1);
        arrayList.addAll(collection);
        arrayList.add(obj);
        return arrayList;
    }

    public static final List take(List list, int i) {
        boolean z;
        Object obj;
        int i2 = 0;
        if (i >= 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            throw new IllegalArgumentException(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Requested element count ", i, " is less than zero.").toString());
        } else if (i == 0) {
            return EmptyList.INSTANCE;
        } else {
            if (list instanceof Collection) {
                if (i >= list.size()) {
                    return toList(list);
                }
                if (i == 1) {
                    if (!(list instanceof List)) {
                        Iterator it = list.iterator();
                        if (it.hasNext()) {
                            obj = it.next();
                        } else {
                            throw new NoSuchElementException("Collection is empty.");
                        }
                    } else if (!list.isEmpty()) {
                        obj = list.get(0);
                    } else {
                        throw new NoSuchElementException("List is empty.");
                    }
                    return Collections.singletonList(obj);
                }
            }
            ArrayList arrayList = new ArrayList(i);
            for (Object obj2 : list) {
                arrayList.add(obj2);
                i2++;
                if (i2 == i) {
                    break;
                }
            }
            return SetsKt__SetsKt.optimizeReadOnlyList(arrayList);
        }
    }

    public static final <T> List<T> takeLast(List<? extends T> list, int i) {
        boolean z;
        if (i >= 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            throw new IllegalArgumentException(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Requested element count ", i, " is less than zero.").toString());
        } else if (i == 0) {
            return EmptyList.INSTANCE;
        } else {
            int size = list.size();
            if (i >= size) {
                return toList(list);
            }
            if (i == 1) {
                return Collections.singletonList(last(list));
            }
            ArrayList arrayList = new ArrayList(i);
            if (list instanceof RandomAccess) {
                for (int i2 = size - i; i2 < size; i2++) {
                    arrayList.add(list.get(i2));
                }
            } else {
                ListIterator<? extends T> listIterator = list.listIterator(size - i);
                while (listIterator.hasNext()) {
                    arrayList.add(listIterator.next());
                }
            }
            return arrayList;
        }
    }

    public static final boolean contains(Collection collection, Object obj) {
        int i;
        if (collection instanceof Collection) {
            return collection.contains(obj);
        }
        if (!(collection instanceof List)) {
            Iterator it = collection.iterator();
            int i2 = 0;
            while (true) {
                if (!it.hasNext()) {
                    i = -1;
                    break;
                }
                Object next = it.next();
                if (i2 < 0) {
                    SetsKt__SetsKt.throwIndexOverflow();
                    throw null;
                } else if (Intrinsics.areEqual(obj, next)) {
                    i = i2;
                    break;
                } else {
                    i2++;
                }
            }
        } else {
            i = ((List) collection).indexOf(obj);
        }
        if (i >= 0) {
            return true;
        }
        return false;
    }

    public static final Object elementAt(Collection collection, int i) {
        boolean z = collection instanceof List;
        if (z) {
            return ((List) collection).get(i);
        }
        CollectionsKt___CollectionsKt$elementAt$1 collectionsKt___CollectionsKt$elementAt$1 = new CollectionsKt___CollectionsKt$elementAt$1(i);
        if (z) {
            List list = (List) collection;
            if (i >= 0 && i <= SetsKt__SetsKt.getLastIndex(list)) {
                return list.get(i);
            }
            collectionsKt___CollectionsKt$elementAt$1.invoke(Integer.valueOf(i));
            throw null;
        } else if (i >= 0) {
            int i2 = 0;
            for (Object obj : collection) {
                int i3 = i2 + 1;
                if (i == i2) {
                    return obj;
                }
                i2 = i3;
            }
            collectionsKt___CollectionsKt$elementAt$1.invoke(Integer.valueOf(i));
            throw null;
        } else {
            collectionsKt___CollectionsKt$elementAt$1.invoke(Integer.valueOf(i));
            throw null;
        }
    }

    public static final Object elementAtOrNull(Set set, int i) {
        if (set instanceof List) {
            List list = (List) set;
            if (i < 0 || i > SetsKt__SetsKt.getLastIndex(list)) {
                return null;
            }
            return list.get(i);
        } else if (i < 0) {
            return null;
        } else {
            int i2 = 0;
            for (Object obj : set) {
                int i3 = i2 + 1;
                if (i == i2) {
                    return obj;
                }
                i2 = i3;
            }
            return null;
        }
    }

    public static String joinToString$default(Iterable iterable, String str, String str2, String str3, Function1 function1, int i) {
        String str4;
        String str5;
        int i2;
        CharSequence charSequence;
        Function1 function12;
        if ((i & 1) != 0) {
            str = ", ";
        }
        if ((i & 2) != 0) {
            str4 = "";
        } else {
            str4 = str2;
        }
        if ((i & 4) != 0) {
            str5 = "";
        } else {
            str5 = str3;
        }
        if ((i & 8) != 0) {
            i2 = -1;
        } else {
            i2 = 0;
        }
        if ((i & 16) != 0) {
            charSequence = "...";
        } else {
            charSequence = null;
        }
        if ((i & 32) != 0) {
            function12 = null;
        } else {
            function12 = function1;
        }
        StringBuilder sb = new StringBuilder();
        joinTo(iterable, sb, str, str4, str5, i2, charSequence, function12);
        return sb.toString();
    }

    public static final ArrayList minus(Collection collection, Rect rect) {
        ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(collection, 10));
        boolean z = false;
        for (Object obj : collection) {
            boolean z2 = true;
            if (!z && Intrinsics.areEqual(obj, rect)) {
                z = true;
                z2 = false;
            }
            if (z2) {
                arrayList.add(obj);
            }
        }
        return arrayList;
    }

    public static final List sortedWith(Collection collection, Comparator comparator) {
        if (!(collection instanceof Collection)) {
            List mutableList = toMutableList(collection);
            CollectionsKt__MutableCollectionsJVMKt.sortWith(mutableList, comparator);
            return mutableList;
        } else if (collection.size() <= 1) {
            return toList(collection);
        } else {
            Object[] array = collection.toArray(new Object[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
            if (array.length > 1) {
                Arrays.sort(array, comparator);
            }
            return Arrays.asList(array);
        }
    }

    public static final <T> List<T> toList(Iterable<? extends T> iterable) {
        Object obj;
        if (!(iterable instanceof Collection)) {
            return SetsKt__SetsKt.optimizeReadOnlyList(toMutableList(iterable));
        }
        Collection collection = (Collection) iterable;
        int size = collection.size();
        if (size == 0) {
            return EmptyList.INSTANCE;
        }
        if (size != 1) {
            return new ArrayList(collection);
        }
        if (iterable instanceof List) {
            obj = ((List) iterable).get(0);
        } else {
            obj = iterable.iterator().next();
        }
        return Collections.singletonList(obj);
    }

    public static final <T> List<T> toMutableList(Iterable<? extends T> iterable) {
        if (iterable instanceof Collection) {
            return new ArrayList((Collection) iterable);
        }
        ArrayList arrayList = new ArrayList();
        toCollection(iterable, arrayList);
        return arrayList;
    }

    public static final Set toMutableSet(Collection collection) {
        if (collection instanceof Collection) {
            return new LinkedHashSet(collection);
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        toCollection(collection, linkedHashSet);
        return linkedHashSet;
    }

    public static final Set toSet(Collection collection) {
        Object obj;
        if (collection instanceof Collection) {
            int size = collection.size();
            if (size == 0) {
                return EmptySet.INSTANCE;
            }
            if (size != 1) {
                LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt__MapsJVMKt.mapCapacity(collection.size()));
                toCollection(collection, linkedHashSet);
                return linkedHashSet;
            }
            if (collection instanceof List) {
                obj = ((List) collection).get(0);
            } else {
                obj = collection.iterator().next();
            }
            return Collections.singleton(obj);
        }
        LinkedHashSet linkedHashSet2 = new LinkedHashSet();
        toCollection(collection, linkedHashSet2);
        int size2 = linkedHashSet2.size();
        if (size2 == 0) {
            return EmptySet.INSTANCE;
        }
        if (size2 != 1) {
            return linkedHashSet2;
        }
        return Collections.singleton(linkedHashSet2.iterator().next());
    }

    public static final double averageOfFloat(ArrayList arrayList) {
        Iterator it = arrayList.iterator();
        double d = 0.0d;
        int i = 0;
        while (it.hasNext()) {
            d += ((Number) it.next()).floatValue();
            i++;
            if (i < 0) {
                throw new ArithmeticException("Count overflow has happened.");
            }
        }
        if (i == 0) {
            return Double.NaN;
        }
        return d / i;
    }

    public static final Appendable joinTo(Iterable iterable, StringBuilder sb, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1 function1) {
        sb.append(charSequence2);
        int i2 = 0;
        for (Object obj : iterable) {
            i2++;
            if (i2 > 1) {
                sb.append(charSequence);
            }
            if (i >= 0 && i2 > i) {
                break;
            }
            R$styleable.appendElement(sb, obj, function1);
        }
        if (i >= 0 && i2 > i) {
            sb.append(charSequence4);
        }
        sb.append(charSequence3);
        return sb;
    }

    public static final <T> T last(List<? extends T> list) {
        if (!list.isEmpty()) {
            return (T) list.get(SetsKt__SetsKt.getLastIndex(list));
        }
        throw new NoSuchElementException("List is empty.");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.lang.Comparable, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <T extends java.lang.Comparable<? super T>> T minOrNull(java.lang.Iterable<? extends T> r3) {
        /*
            java.util.Iterator r3 = r3.iterator()
            boolean r0 = r3.hasNext()
            if (r0 != 0) goto L_0x000c
            r3 = 0
            return r3
        L_0x000c:
            java.lang.Object r0 = r3.next()
            java.lang.Comparable r0 = (java.lang.Comparable) r0
        L_0x0012:
            boolean r1 = r3.hasNext()
            if (r1 == 0) goto L_0x0026
            java.lang.Object r1 = r3.next()
            java.lang.Comparable r1 = (java.lang.Comparable) r1
            int r2 = r0.compareTo(r1)
            if (r2 <= 0) goto L_0x0012
            r0 = r1
            goto L_0x0012
        L_0x0026:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.CollectionsKt___CollectionsKt.minOrNull(java.lang.Iterable):java.lang.Comparable");
    }

    public static final Collection toCollection(Iterable iterable, AbstractCollection abstractCollection) {
        for (Object obj : iterable) {
            abstractCollection.add(obj);
        }
        return abstractCollection;
    }

    public static final ArrayList plus(List list, List list2) {
        if (list2 instanceof Collection) {
            ArrayList arrayList = new ArrayList(list2.size() + list.size());
            arrayList.addAll(list);
            arrayList.addAll(list2);
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList(list);
        CollectionsKt__ReversedViewsKt.addAll(arrayList2, list2);
        return arrayList2;
    }
}
