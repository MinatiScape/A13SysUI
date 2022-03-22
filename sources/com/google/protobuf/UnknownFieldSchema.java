package com.google.protobuf;
/* loaded from: classes.dex */
public abstract class UnknownFieldSchema<T, B> {
    public abstract UnknownFieldSetLite getFromMessage(Object obj);

    public abstract void makeImmutable(Object obj);

    public abstract UnknownFieldSetLite merge(Object obj, Object obj2);

    public abstract void setToMessage(Object obj, T t);
}
