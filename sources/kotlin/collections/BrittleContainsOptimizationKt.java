package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
/* compiled from: BrittleContainsOptimization.kt */
/* loaded from: classes.dex */
public final class BrittleContainsOptimizationKt {
    public static final Collection convertToSetForSetOperationWith(Collection collection, Set set) {
        HashSet hashSet;
        boolean z;
        if (collection instanceof Set) {
            return collection;
        }
        if (collection instanceof Collection) {
            if ((set instanceof Collection) && set.size() < 2) {
                return collection;
            }
            if (!CollectionSystemProperties.brittleContainsOptimizationEnabled || collection.size() <= 2 || !(collection instanceof ArrayList)) {
                z = false;
            } else {
                z = true;
            }
            if (!z) {
                return collection;
            }
            hashSet = new HashSet(MapsKt__MapsJVMKt.mapCapacity(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(collection, 12)));
            CollectionsKt___CollectionsKt.toCollection(collection, hashSet);
        } else if (!CollectionSystemProperties.brittleContainsOptimizationEnabled) {
            return CollectionsKt___CollectionsKt.toList(collection);
        } else {
            hashSet = new HashSet(MapsKt__MapsJVMKt.mapCapacity(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(collection, 12)));
            CollectionsKt___CollectionsKt.toCollection(collection, hashSet);
        }
        return hashSet;
    }
}
