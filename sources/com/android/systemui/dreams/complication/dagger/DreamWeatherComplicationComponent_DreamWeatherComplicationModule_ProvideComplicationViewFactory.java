package com.android.systemui.dreams.complication.dagger;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.internal.util.Preconditions;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DreamWeatherComplicationComponent_DreamWeatherComplicationModule_ProvideComplicationViewFactory implements Factory<TextView> {
    public final Provider<LayoutInflater> layoutInflaterProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        TextView textView = (TextView) Preconditions.checkNotNull((TextView) this.layoutInflaterProvider.mo144get().inflate(2131624089, (ViewGroup) null, false), "R.layout.dream_overlay_complication_weather did not properly inflated");
        Objects.requireNonNull(textView, "Cannot return null from a non-@Nullable @Provides method");
        return textView;
    }

    public DreamWeatherComplicationComponent_DreamWeatherComplicationModule_ProvideComplicationViewFactory(Provider<LayoutInflater> provider) {
        this.layoutInflaterProvider = provider;
    }
}
