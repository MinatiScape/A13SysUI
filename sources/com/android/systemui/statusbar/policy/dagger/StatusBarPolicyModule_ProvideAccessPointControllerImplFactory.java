package com.android.systemui.statusbar.policy.dagger;

import android.os.UserManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarPolicyModule_ProvideAccessPointControllerImplFactory implements Factory<AccessPointControllerImpl> {
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<UserTracker> userTrackerProvider;
    public final Provider<AccessPointControllerImpl.WifiPickerTrackerFactory> wifiPickerTrackerFactoryProvider;

    /* JADX WARN: Type inference failed for: r12v0, types: [java.time.Clock, com.android.systemui.statusbar.connectivity.AccessPointControllerImpl$WifiPickerTrackerFactory$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // javax.inject.Provider
    /* renamed from: get */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object mo144get() {
        /*
            r18 = this;
            r0 = r18
            javax.inject.Provider<android.os.UserManager> r1 = r0.userManagerProvider
            java.lang.Object r1 = r1.mo144get()
            android.os.UserManager r1 = (android.os.UserManager) r1
            javax.inject.Provider<com.android.systemui.settings.UserTracker> r2 = r0.userTrackerProvider
            java.lang.Object r2 = r2.mo144get()
            com.android.systemui.settings.UserTracker r2 = (com.android.systemui.settings.UserTracker) r2
            javax.inject.Provider<java.util.concurrent.Executor> r3 = r0.mainExecutorProvider
            java.lang.Object r3 = r3.mo144get()
            java.util.concurrent.Executor r3 = (java.util.concurrent.Executor) r3
            javax.inject.Provider<com.android.systemui.statusbar.connectivity.AccessPointControllerImpl$WifiPickerTrackerFactory> r0 = r0.wifiPickerTrackerFactoryProvider
            java.lang.Object r0 = r0.mo144get()
            com.android.systemui.statusbar.connectivity.AccessPointControllerImpl$WifiPickerTrackerFactory r0 = (com.android.systemui.statusbar.connectivity.AccessPointControllerImpl.WifiPickerTrackerFactory) r0
            com.android.systemui.statusbar.connectivity.AccessPointControllerImpl r15 = new com.android.systemui.statusbar.connectivity.AccessPointControllerImpl
            r15.<init>(r1, r2, r3, r0)
            com.android.wifitrackerlib.WifiPickerTracker r0 = r15.mWifiPickerTracker
            if (r0 != 0) goto L_0x005a
            com.android.systemui.statusbar.connectivity.AccessPointControllerImpl$WifiPickerTrackerFactory r0 = r15.mWifiPickerTrackerFactory
            androidx.lifecycle.LifecycleRegistry r6 = r15.mLifecycle
            java.util.Objects.requireNonNull(r0)
            android.net.wifi.WifiManager r8 = r0.mWifiManager
            if (r8 != 0) goto L_0x003a
            r0 = 0
            r1 = r0
            r0 = r15
            goto L_0x0057
        L_0x003a:
            com.android.wifitrackerlib.WifiPickerTracker r1 = new com.android.wifitrackerlib.WifiPickerTracker
            android.content.Context r7 = r0.mContext
            android.net.ConnectivityManager r9 = r0.mConnectivityManager
            android.os.Handler r10 = r0.mMainHandler
            android.os.Handler r11 = r0.mWorkerHandler
            com.android.systemui.statusbar.connectivity.AccessPointControllerImpl$WifiPickerTrackerFactory$1 r12 = r0.mClock
            r13 = 15000(0x3a98, double:7.411E-320)
            r2 = 10000(0x2710, double:4.9407E-320)
            com.android.wifitrackerlib.WifiTrackerInjector r5 = new com.android.wifitrackerlib.WifiTrackerInjector
            r5.<init>(r7)
            r4 = r1
            r0 = r15
            r15 = r2
            r17 = r0
            r4.<init>(r5, r6, r7, r8, r9, r10, r11, r12, r13, r15, r17)
        L_0x0057:
            r0.mWifiPickerTracker = r1
            goto L_0x005b
        L_0x005a:
            r0 = r15
        L_0x005b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.dagger.StatusBarPolicyModule_ProvideAccessPointControllerImplFactory.mo144get():java.lang.Object");
    }

    public StatusBarPolicyModule_ProvideAccessPointControllerImplFactory(Provider<UserManager> provider, Provider<UserTracker> provider2, Provider<Executor> provider3, Provider<AccessPointControllerImpl.WifiPickerTrackerFactory> provider4) {
        this.userManagerProvider = provider;
        this.userTrackerProvider = provider2;
        this.mainExecutorProvider = provider3;
        this.wifiPickerTrackerFactoryProvider = provider4;
    }
}
