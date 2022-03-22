package com.google.protobuf;
/* loaded from: classes.dex */
public interface Schema<T> {
    boolean equals(T t, T t2);

    int hashCode(T t);

    boolean isInitialized(T t);

    void makeImmutable(T t);

    void mergeFrom(T t, T t2);
}
