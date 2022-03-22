package com.google.android.setupcompat.internal;

import com.google.android.setupcompat.internal.ClockProvider;
/* loaded from: classes.dex */
public final class ClockProvider {
    public static Ticker ticker = ClockProvider$$ExternalSyntheticLambda1.INSTANCE;

    /* loaded from: classes.dex */
    public interface Supplier<T> {
        T get();
    }

    public static void resetInstance() {
        ticker = ClockProvider$$ExternalSyntheticLambda1.INSTANCE;
    }

    public static void setInstance(final Supplier<Long> supplier) {
        ticker = new Ticker() { // from class: com.google.android.setupcompat.internal.ClockProvider$$ExternalSyntheticLambda0
            @Override // com.google.android.setupcompat.internal.Ticker
            public final long read() {
                return ((Long) ClockProvider.Supplier.this.get()).longValue();
            }
        };
    }
}
