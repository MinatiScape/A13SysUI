package com.google.protobuf;
/* loaded from: classes.dex */
public interface MapFieldSchema {
    MapFieldLite forMapData(Object obj);

    void forMapMetadata(Object obj);

    MapFieldLite mergeFrom(Object obj, Object obj2);

    Object toImmutable(Object obj);
}
