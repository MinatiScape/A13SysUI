package com.android.systemui.dreams.complication.dagger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.util.Preconditions;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DreamClockDateComplicationComponent_DreamClockDateComplicationModule_ProvideComplicationViewFactory implements Factory<View> {
    public final Provider<LayoutInflater> layoutInflaterProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        View view = (View) Preconditions.checkNotNull(this.layoutInflaterProvider.mo144get().inflate(2131624087, (ViewGroup) null, false), "R.layout.dream_overlay_complication_clock_date did not properly inflated");
        Objects.requireNonNull(view, "Cannot return null from a non-@Nullable @Provides method");
        return view;
    }

    public DreamClockDateComplicationComponent_DreamClockDateComplicationModule_ProvideComplicationViewFactory(Provider<LayoutInflater> provider) {
        this.layoutInflaterProvider = provider;
    }
}
