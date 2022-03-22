package com.android.wm.shell.animation;

import android.util.DisplayMetrics;
import com.android.wm.shell.animation.FlingAnimationUtils;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FlingAnimationUtils_Builder_Factory implements Factory<FlingAnimationUtils.Builder> {
    public final Provider<DisplayMetrics> displayMetricsProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new FlingAnimationUtils.Builder(this.displayMetricsProvider.mo144get());
    }

    public FlingAnimationUtils_Builder_Factory(Provider<DisplayMetrics> provider) {
        this.displayMetricsProvider = provider;
    }
}
