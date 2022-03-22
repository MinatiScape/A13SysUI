package com.google.common.collect;

import com.google.common.collect.AbstractMapBasedMultimap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class AbstractSetMultimap<K, V> extends AbstractMapBasedMultimap<K, V> {
    private static final long serialVersionUID = 7431625294878419160L;

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public final AbstractMapBasedMultimap.AsMap asMap() {
        AbstractMapBasedMultimap.AsMap asMap = this.asMap;
        if (asMap != null) {
            return asMap;
        }
        AbstractMapBasedMultimap.AsMap asMap2 = new AbstractMapBasedMultimap.AsMap(this.map);
        this.asMap = asMap2;
        return asMap2;
    }

    @Override // com.google.common.collect.AbstractMultimap
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }
}
