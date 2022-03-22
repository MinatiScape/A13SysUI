package com.android.systemui.qs.carrier;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.android.keyguard.CarrierTextManager;
import com.android.keyguard.CarrierTextManager_Builder_Factory;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainLooperFactory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSCarrierGroupController_Builder_Factory implements Factory<QSCarrierGroupController.Builder> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
    public final Provider<CarrierTextManager.Builder> carrierTextControllerBuilderProvider;
    public final Provider<Context> contextProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<Looper> looperProvider;
    public final Provider<NetworkController> networkControllerProvider;
    public final Provider<QSCarrierGroupController.SlotIndexResolver> slotIndexResolverProvider;

    public QSCarrierGroupController_Builder_Factory(Provider provider, Provider provider2, Provider provider3, CarrierTextManager_Builder_Factory carrierTextManager_Builder_Factory, Provider provider4, Provider provider5, Provider provider6, Provider provider7) {
        GlobalConcurrencyModule_ProvideMainLooperFactory globalConcurrencyModule_ProvideMainLooperFactory = GlobalConcurrencyModule_ProvideMainLooperFactory.InstanceHolder.INSTANCE;
        this.activityStarterProvider = provider;
        this.handlerProvider = provider2;
        this.looperProvider = globalConcurrencyModule_ProvideMainLooperFactory;
        this.networkControllerProvider = provider3;
        this.carrierTextControllerBuilderProvider = carrierTextManager_Builder_Factory;
        this.contextProvider = provider4;
        this.carrierConfigTrackerProvider = provider5;
        this.featureFlagsProvider = provider6;
        this.slotIndexResolverProvider = provider7;
    }

    public static QSCarrierGroupController_Builder_Factory create(Provider provider, Provider provider2, Provider provider3, CarrierTextManager_Builder_Factory carrierTextManager_Builder_Factory, Provider provider4, Provider provider5, Provider provider6, Provider provider7) {
        return new QSCarrierGroupController_Builder_Factory(provider, provider2, provider3, carrierTextManager_Builder_Factory, provider4, provider5, provider6, provider7);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new QSCarrierGroupController.Builder(this.activityStarterProvider.mo144get(), this.handlerProvider.mo144get(), this.looperProvider.mo144get(), this.networkControllerProvider.mo144get(), this.carrierTextControllerBuilderProvider.mo144get(), this.contextProvider.mo144get(), this.carrierConfigTrackerProvider.mo144get(), this.featureFlagsProvider.mo144get(), this.slotIndexResolverProvider.mo144get());
    }
}
