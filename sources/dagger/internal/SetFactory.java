package dagger.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SetFactory<T> implements Factory<Set<T>> {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final List<Provider<Collection<T>>> collectionProviders;
    public final List<Provider<T>> individualProviders;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        int i;
        int size = this.individualProviders.size();
        ArrayList arrayList = new ArrayList(this.collectionProviders.size());
        int size2 = this.collectionProviders.size();
        for (int i2 = 0; i2 < size2; i2++) {
            Collection<T> collection = this.collectionProviders.get(i2).mo144get();
            size += collection.size();
            arrayList.add(collection);
        }
        if (size < 3) {
            i = size + 1;
        } else if (size < 1073741824) {
            i = (int) ((size / 0.75f) + 1.0f);
        } else {
            i = Integer.MAX_VALUE;
        }
        HashSet hashSet = new HashSet(i);
        int size3 = this.individualProviders.size();
        for (int i3 = 0; i3 < size3; i3++) {
            T t = this.individualProviders.get(i3).mo144get();
            Objects.requireNonNull(t);
            hashSet.add(t);
        }
        int size4 = arrayList.size();
        for (int i4 = 0; i4 < size4; i4++) {
            for (Object obj : (Collection) arrayList.get(i4)) {
                Objects.requireNonNull(obj);
                hashSet.add(obj);
            }
        }
        return Collections.unmodifiableSet(hashSet);
    }

    static {
        InstanceFactory.create(Collections.emptySet());
    }

    public SetFactory(List list, List list2) {
        this.individualProviders = list;
        this.collectionProviders = list2;
    }
}
