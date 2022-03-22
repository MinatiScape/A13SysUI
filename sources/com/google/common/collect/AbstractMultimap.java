package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapBasedMultimap;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public abstract class AbstractMultimap<K, V> implements Multimap<K, V> {
    @LazyInit
    public transient AbstractMapBasedMultimap.AsMap asMap;
    @LazyInit
    public transient Entries entries;
    @LazyInit
    public transient AbstractMapBasedMultimap.KeySet keySet;

    /* loaded from: classes.dex */
    public class Entries extends Multimaps$Entries<K, V> {
        public Entries() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public final Iterator<Map.Entry<K, V>> iterator() {
            final LinkedHashMultimap linkedHashMultimap = (LinkedHashMultimap) AbstractMultimap.this;
            Objects.requireNonNull(linkedHashMultimap);
            return (Iterator<Map.Entry<K, V>>) new Iterator<Map.Entry<Object, Object>>() { // from class: com.google.common.collect.LinkedHashMultimap.1
                public ValueEntry<Object, Object> nextEntry;
                public ValueEntry<Object, Object> toRemove;

                {
                    LinkedHashMultimap.this = linkedHashMultimap;
                    ValueEntry<K, V> valueEntry = linkedHashMultimap.multimapHeaderEntry;
                    Objects.requireNonNull(valueEntry);
                    ValueEntry<K, V> valueEntry2 = valueEntry.successorInMultimap;
                    Objects.requireNonNull(valueEntry2);
                    this.nextEntry = valueEntry2;
                }

                @Override // java.util.Iterator
                public final boolean hasNext() {
                    if (this.nextEntry != LinkedHashMultimap.this.multimapHeaderEntry) {
                        return true;
                    }
                    return false;
                }

                @Override // java.util.Iterator
                public final void remove() {
                    boolean z;
                    if (this.toRemove != null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    Preconditions.checkState(z, "no calls to next() since the last call to remove()");
                    LinkedHashMultimap linkedHashMultimap2 = LinkedHashMultimap.this;
                    ValueEntry<Object, Object> valueEntry = this.toRemove;
                    Objects.requireNonNull(valueEntry);
                    Object obj = valueEntry.key;
                    ValueEntry<Object, Object> valueEntry2 = this.toRemove;
                    Objects.requireNonNull(valueEntry2);
                    Object obj2 = valueEntry2.value;
                    Objects.requireNonNull(linkedHashMultimap2);
                    Collection collection = (Collection) linkedHashMultimap2.asMap().get(obj);
                    if (collection != null) {
                        collection.remove(obj2);
                    }
                    this.toRemove = null;
                }

                @Override // java.util.Iterator
                public final Map.Entry<Object, Object> next() {
                    if (hasNext()) {
                        ValueEntry<Object, Object> valueEntry = this.nextEntry;
                        this.toRemove = valueEntry;
                        Objects.requireNonNull(valueEntry);
                        ValueEntry<Object, Object> valueEntry2 = valueEntry.successorInMultimap;
                        Objects.requireNonNull(valueEntry2);
                        this.nextEntry = valueEntry2;
                        return valueEntry;
                    }
                    throw new NoSuchElementException();
                }
            };
        }
    }

    @Override // com.google.common.collect.Multimap
    public abstract AbstractMapBasedMultimap.AsMap asMap();

    /* loaded from: classes.dex */
    public class EntrySet extends AbstractMultimap<K, V>.Entries implements Set<Map.Entry<K, V>> {
        @Override // java.util.Collection, java.util.Set
        public final boolean equals(Object obj) {
            return Sets.equalsImpl(this, obj);
        }

        @Override // java.util.Collection, java.util.Set
        public final int hashCode() {
            return Sets.hashCodeImpl(this);
        }

        public EntrySet(AbstractMultimap abstractMultimap) {
            super();
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Multimap) {
            return ((AbstractSetMultimap) this).asMap().equals(((Multimap) obj).asMap());
        }
        return false;
    }

    public final int hashCode() {
        return asMap().hashCode();
    }

    public final String toString() {
        return asMap().toString();
    }
}
