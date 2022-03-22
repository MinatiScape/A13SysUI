package com.google.protobuf;

import java.util.List;
/* loaded from: classes.dex */
public interface LazyStringList extends List {
    List<?> getUnderlyingElements();

    LazyStringList getUnmodifiableView();
}
