package com.android.systemui.statusbar.phone.userswitcher;

import android.os.UserManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.UserInfoController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarUserInfoTracker_Factory implements Factory<StatusBarUserInfoTracker> {
    public final Provider<Executor> backgroundExecutorProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<UserInfoController> userInfoControllerProvider;
    public final Provider<UserManager> userManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new StatusBarUserInfoTracker(this.userInfoControllerProvider.mo144get(), this.userManagerProvider.mo144get(), this.dumpManagerProvider.mo144get(), this.mainExecutorProvider.mo144get(), this.backgroundExecutorProvider.mo144get());
    }

    public StatusBarUserInfoTracker_Factory(Provider<UserInfoController> provider, Provider<UserManager> provider2, Provider<DumpManager> provider3, Provider<Executor> provider4, Provider<Executor> provider5) {
        this.userInfoControllerProvider = provider;
        this.userManagerProvider = provider2;
        this.dumpManagerProvider = provider3;
        this.mainExecutorProvider = provider4;
        this.backgroundExecutorProvider = provider5;
    }
}
