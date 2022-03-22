package com.airbnb.lottie.model;

import androidx.collection.LruCache;
import com.airbnb.lottie.LottieComposition;
/* loaded from: classes.dex */
public final class LottieCompositionCache {
    public static final LottieCompositionCache INSTANCE = new LottieCompositionCache();
    public final LruCache<String, LottieComposition> cache = new LruCache<>(20);
}
