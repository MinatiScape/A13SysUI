package com.google.protobuf;

import java.util.Objects;
/* loaded from: classes.dex */
public final class MapFieldSchemaLite implements MapFieldSchema {
    @Override // com.google.protobuf.MapFieldSchema
    public final Object toImmutable(Object obj) {
        ((MapFieldLite) obj).makeImmutable();
        return obj;
    }

    @Override // com.google.protobuf.MapFieldSchema
    public final void forMapMetadata(Object obj) {
        Objects.requireNonNull((MapEntryLite) obj);
    }

    @Override // com.google.protobuf.MapFieldSchema
    public final MapFieldLite mergeFrom(Object obj, Object obj2) {
        MapFieldLite mapFieldLite = (MapFieldLite) obj;
        MapFieldLite mapFieldLite2 = (MapFieldLite) obj2;
        if (!mapFieldLite2.isEmpty()) {
            if (!mapFieldLite.isMutable()) {
                mapFieldLite = mapFieldLite.mutableCopy();
            }
            mapFieldLite.ensureMutable();
            if (!mapFieldLite2.isEmpty()) {
                mapFieldLite.putAll(mapFieldLite2);
            }
        }
        return mapFieldLite;
    }

    @Override // com.google.protobuf.MapFieldSchema
    public final MapFieldLite forMapData(Object obj) {
        return (MapFieldLite) obj;
    }
}
