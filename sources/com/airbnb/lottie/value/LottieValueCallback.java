package com.airbnb.lottie.value;

import com.airbnb.lottie.SimpleColorFilter;
import java.util.Objects;
/* loaded from: classes.dex */
public class LottieValueCallback<T> {
    public final LottieFrameInfo<T> frameInfo;
    public T value;

    public LottieValueCallback() {
        this.frameInfo = new LottieFrameInfo<>();
        this.value = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final Object getValueInternal(Object obj, Object obj2) {
        LottieFrameInfo<T> lottieFrameInfo = this.frameInfo;
        Objects.requireNonNull(lottieFrameInfo);
        lottieFrameInfo.startValue = obj;
        lottieFrameInfo.endValue = obj2;
        return getValue(lottieFrameInfo);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public LottieValueCallback(SimpleColorFilter simpleColorFilter) {
        this.frameInfo = new LottieFrameInfo<>();
        this.value = simpleColorFilter;
    }

    public T getValue(LottieFrameInfo<T> lottieFrameInfo) {
        return this.value;
    }
}
