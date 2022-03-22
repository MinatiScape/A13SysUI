package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RankingCoordinator_Factory implements Factory<RankingCoordinator> {
    public final Provider<NodeController> alertingHeaderControllerProvider;
    public final Provider<HighPriorityProvider> highPriorityProvider;
    public final Provider<SectionClassifier> sectionClassifierProvider;
    public final Provider<SectionHeaderController> silentHeaderControllerProvider;
    public final Provider<NodeController> silentNodeControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public static RankingCoordinator_Factory create(Provider<StatusBarStateController> provider, Provider<HighPriorityProvider> provider2, Provider<SectionClassifier> provider3, Provider<NodeController> provider4, Provider<SectionHeaderController> provider5, Provider<NodeController> provider6) {
        return new RankingCoordinator_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new RankingCoordinator(this.statusBarStateControllerProvider.mo144get(), this.highPriorityProvider.mo144get(), this.sectionClassifierProvider.mo144get(), this.alertingHeaderControllerProvider.mo144get(), this.silentHeaderControllerProvider.mo144get(), this.silentNodeControllerProvider.mo144get());
    }

    public RankingCoordinator_Factory(Provider<StatusBarStateController> provider, Provider<HighPriorityProvider> provider2, Provider<SectionClassifier> provider3, Provider<NodeController> provider4, Provider<SectionHeaderController> provider5, Provider<NodeController> provider6) {
        this.statusBarStateControllerProvider = provider;
        this.highPriorityProvider = provider2;
        this.sectionClassifierProvider = provider3;
        this.alertingHeaderControllerProvider = provider4;
        this.silentHeaderControllerProvider = provider5;
        this.silentNodeControllerProvider = provider6;
    }
}
