package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AccessPointControllerImpl_WifiPickerTrackerFactory_Factory implements Factory<AccessPointControllerImpl.WifiPickerTrackerFactory> {
    public final Provider<ConnectivityManager> connectivityManagerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<WifiManager> wifiManagerProvider;
    public final Provider<Handler> workerHandlerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new AccessPointControllerImpl.WifiPickerTrackerFactory(this.contextProvider.mo144get(), this.wifiManagerProvider.mo144get(), this.connectivityManagerProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.workerHandlerProvider.mo144get());
    }

    public AccessPointControllerImpl_WifiPickerTrackerFactory_Factory(Provider<Context> provider, Provider<WifiManager> provider2, Provider<ConnectivityManager> provider3, Provider<Handler> provider4, Provider<Handler> provider5) {
        this.contextProvider = provider;
        this.wifiManagerProvider = provider2;
        this.connectivityManagerProvider = provider3;
        this.mainHandlerProvider = provider4;
        this.workerHandlerProvider = provider5;
    }
}
