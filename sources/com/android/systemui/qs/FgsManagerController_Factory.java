package com.android.systemui.qs;

import android.app.IActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FgsManagerController_Factory implements Factory<FgsManagerController> {
    public final Provider<IActivityManager> activityManagerProvider;
    public final Provider<Executor> backgroundExecutorProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DeviceConfigProxy> deviceConfigProxyProvider;
    public final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<PackageManager> packageManagerProvider;
    public final Provider<SystemClock> systemClockProvider;

    public static FgsManagerController_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<Executor> provider3, Provider<SystemClock> provider4, Provider<IActivityManager> provider5, Provider<PackageManager> provider6, Provider<DeviceConfigProxy> provider7, Provider<DialogLaunchAnimator> provider8, Provider<DumpManager> provider9) {
        return new FgsManagerController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new FgsManagerController(this.contextProvider.mo144get(), this.mainExecutorProvider.mo144get(), this.backgroundExecutorProvider.mo144get(), this.systemClockProvider.mo144get(), this.activityManagerProvider.mo144get(), this.packageManagerProvider.mo144get(), this.deviceConfigProxyProvider.mo144get(), this.dialogLaunchAnimatorProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public FgsManagerController_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<Executor> provider3, Provider<SystemClock> provider4, Provider<IActivityManager> provider5, Provider<PackageManager> provider6, Provider<DeviceConfigProxy> provider7, Provider<DialogLaunchAnimator> provider8, Provider<DumpManager> provider9) {
        this.contextProvider = provider;
        this.mainExecutorProvider = provider2;
        this.backgroundExecutorProvider = provider3;
        this.systemClockProvider = provider4;
        this.activityManagerProvider = provider5;
        this.packageManagerProvider = provider6;
        this.deviceConfigProxyProvider = provider7;
        this.dialogLaunchAnimatorProvider = provider8;
        this.dumpManagerProvider = provider9;
    }
}
