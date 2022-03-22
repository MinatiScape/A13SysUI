package com.android.systemui.statusbar.notification.collection.render;

import android.view.LayoutInflater;
import com.android.systemui.plugins.ActivityStarter;
import dagger.internal.Factory;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SectionHeaderNodeControllerImpl_Factory implements Factory<SectionHeaderNodeControllerImpl> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<String> clickIntentActionProvider;
    public final Provider<Integer> headerTextResIdProvider;
    public final Provider<LayoutInflater> layoutInflaterProvider;
    public final Provider<String> nodeLabelProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SectionHeaderNodeControllerImpl(this.nodeLabelProvider.mo144get(), this.layoutInflaterProvider.mo144get(), this.headerTextResIdProvider.mo144get().intValue(), this.activityStarterProvider.mo144get(), this.clickIntentActionProvider.mo144get());
    }

    public SectionHeaderNodeControllerImpl_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, InstanceFactory instanceFactory) {
        this.nodeLabelProvider = provider;
        this.layoutInflaterProvider = provider2;
        this.headerTextResIdProvider = provider3;
        this.activityStarterProvider = provider4;
        this.clickIntentActionProvider = instanceFactory;
    }
}
