package com.android.systemui.statusbar.notification.collection.init;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory;
import dagger.internal.Factory;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotifPipelineInitializer_Factory implements Factory<NotifPipelineInitializer> {
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<GroupCoalescer> groupCoalescerProvider;
    public final Provider<ShadeListBuilder> listBuilderProvider;
    public final Provider<NotifCollection> notifCollectionProvider;
    public final Provider<NotifCoordinators> notifCoordinatorsProvider;
    public final Provider<NotifInflaterImpl> notifInflaterProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotifPipeline> pipelineWrapperProvider;
    public final Provider<RenderStageManager> renderStageManagerProvider;
    public final Provider<ShadeViewManagerFactory> shadeViewManagerFactoryProvider;

    public static NotifPipelineInitializer_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, InstanceFactory instanceFactory, Provider provider9) {
        return new NotifPipelineInitializer_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, instanceFactory, provider9);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotifPipelineInitializer(this.pipelineWrapperProvider.mo144get(), this.groupCoalescerProvider.mo144get(), this.notifCollectionProvider.mo144get(), this.listBuilderProvider.mo144get(), this.renderStageManagerProvider.mo144get(), this.notifCoordinatorsProvider.mo144get(), this.notifInflaterProvider.mo144get(), this.dumpManagerProvider.mo144get(), this.shadeViewManagerFactoryProvider.mo144get(), this.notifPipelineFlagsProvider.mo144get());
    }

    public NotifPipelineInitializer_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, InstanceFactory instanceFactory, Provider provider9) {
        this.pipelineWrapperProvider = provider;
        this.groupCoalescerProvider = provider2;
        this.notifCollectionProvider = provider3;
        this.listBuilderProvider = provider4;
        this.renderStageManagerProvider = provider5;
        this.notifCoordinatorsProvider = provider6;
        this.notifInflaterProvider = provider7;
        this.dumpManagerProvider = provider8;
        this.shadeViewManagerFactoryProvider = instanceFactory;
        this.notifPipelineFlagsProvider = provider9;
    }
}
