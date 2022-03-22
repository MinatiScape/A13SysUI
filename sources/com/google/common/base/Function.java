package com.google.common.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
/* loaded from: classes.dex */
public interface Function<F, T> {
    @CanIgnoreReturnValue
    T apply(F f);

    boolean equals(Object obj);
}
