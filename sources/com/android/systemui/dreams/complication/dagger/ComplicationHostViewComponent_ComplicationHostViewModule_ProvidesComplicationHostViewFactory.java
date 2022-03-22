package com.android.systemui.dreams.complication.dagger;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.internal.util.Preconditions;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ComplicationHostViewComponent_ComplicationHostViewModule_ProvidesComplicationHostViewFactory implements Factory<ConstraintLayout> {
    public final Provider<LayoutInflater> layoutInflaterProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        ConstraintLayout constraintLayout = (ConstraintLayout) Preconditions.checkNotNull((ConstraintLayout) this.layoutInflaterProvider.mo144get().inflate(2131624090, (ViewGroup) null), "R.layout.dream_overlay_complications_layer did not properly inflated");
        Objects.requireNonNull(constraintLayout, "Cannot return null from a non-@Nullable @Provides method");
        return constraintLayout;
    }

    public ComplicationHostViewComponent_ComplicationHostViewModule_ProvidesComplicationHostViewFactory(Provider<LayoutInflater> provider) {
        this.layoutInflaterProvider = provider;
    }
}
