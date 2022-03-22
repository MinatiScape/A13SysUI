package com.android.systemui.dreams.complication.dagger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import com.android.internal.util.Preconditions;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DreamClockTimeComplicationComponent_DreamClockTimeComplicationModule_ProvideComplicationViewFactory implements Factory<View> {
    public final Provider<LayoutInflater> layoutInflaterProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        TextClock textClock = (TextClock) Preconditions.checkNotNull((TextClock) this.layoutInflaterProvider.mo144get().inflate(2131624088, (ViewGroup) null, false), "R.layout.dream_overlay_complication_clock_time did not properly inflated");
        textClock.setFontVariationSettings("'wght' 200");
        return textClock;
    }

    public DreamClockTimeComplicationComponent_DreamClockTimeComplicationModule_ProvideComplicationViewFactory(Provider<LayoutInflater> provider) {
        this.layoutInflaterProvider = provider;
    }
}
