package com.airbnb.lottie;

import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LottieResult<V> {
    public final Throwable exception;
    public final V value;

    /* JADX WARN: Multi-variable type inference failed */
    public LottieResult(LottieComposition lottieComposition) {
        this.value = lottieComposition;
        this.exception = null;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LottieResult)) {
            return false;
        }
        LottieResult lottieResult = (LottieResult) obj;
        V v = this.value;
        if (v != null) {
            Objects.requireNonNull(lottieResult);
            if (v.equals(lottieResult.value)) {
                return true;
            }
        }
        if (this.exception != null) {
            Objects.requireNonNull(lottieResult);
            if (lottieResult.exception != null) {
                return this.exception.toString().equals(this.exception.toString());
            }
        }
        return false;
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.value, this.exception});
    }

    public LottieResult(Throwable th) {
        this.exception = th;
        this.value = null;
    }
}
