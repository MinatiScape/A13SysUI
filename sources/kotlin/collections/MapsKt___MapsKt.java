package kotlin.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import kotlin.Pair;
/* compiled from: _Maps.kt */
/* loaded from: classes.dex */
public class MapsKt___MapsKt extends MapsKt__MapsJVMKt {
    public static final Map mapOf(Pair... pairArr) {
        if (pairArr.length <= 0) {
            return EmptyMap.INSTANCE;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap(MapsKt__MapsJVMKt.mapCapacity(pairArr.length));
        int length = pairArr.length;
        int i = 0;
        while (i < length) {
            Pair pair = pairArr[i];
            i++;
            linkedHashMap.put(pair.component1(), pair.component2());
        }
        return linkedHashMap;
    }

    public static final Map toMap(ArrayList arrayList, LinkedHashMap linkedHashMap) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            linkedHashMap.put(pair.component1(), pair.component2());
        }
        return linkedHashMap;
    }

    public static final Object getValue(Map map, Object obj) {
        if (map instanceof MapWithDefault) {
            return ((MapWithDefault) map).getOrImplicitDefault(obj);
        }
        Object obj2 = map.get(obj);
        if (obj2 != null || map.containsKey(obj)) {
            return obj2;
        }
        throw new NoSuchElementException("Key " + obj + " is missing in the map.");
    }

    public static final Map optimizeReadOnlyMap(LinkedHashMap linkedHashMap) {
        int size = linkedHashMap.size();
        if (size == 0) {
            return EmptyMap.INSTANCE;
        }
        if (size != 1) {
            return linkedHashMap;
        }
        Map.Entry entry = (Map.Entry) linkedHashMap.entrySet().iterator().next();
        return Collections.singletonMap(entry.getKey(), entry.getValue());
    }

    public static final Map toMap(ArrayList arrayList) {
        int size = arrayList.size();
        if (size == 0) {
            return EmptyMap.INSTANCE;
        }
        if (size != 1) {
            LinkedHashMap linkedHashMap = new LinkedHashMap(MapsKt__MapsJVMKt.mapCapacity(arrayList.size()));
            toMap(arrayList, linkedHashMap);
            return linkedHashMap;
        }
        Pair pair = (Pair) arrayList.get(0);
        return Collections.singletonMap(pair.getFirst(), pair.getSecond());
    }

    public static final Map emptyMap() {
        return EmptyMap.INSTANCE;
    }
}
