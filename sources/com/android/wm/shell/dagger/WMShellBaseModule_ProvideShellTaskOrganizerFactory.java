package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.compatui.CompatUIController;
import com.android.wm.shell.recents.RecentTasksController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideShellTaskOrganizerFactory implements Factory<ShellTaskOrganizer> {
    public final Provider<CompatUIController> compatUIProvider;
    public final Provider<Context> contextProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Optional<RecentTasksController>> recentTasksOptionalProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ShellTaskOrganizer(null, this.mainExecutorProvider.mo144get(), this.contextProvider.mo144get(), this.compatUIProvider.mo144get(), this.recentTasksOptionalProvider.mo144get());
    }

    public WMShellBaseModule_ProvideShellTaskOrganizerFactory(Provider<ShellExecutor> provider, Provider<Context> provider2, Provider<CompatUIController> provider3, Provider<Optional<RecentTasksController>> provider4) {
        this.mainExecutorProvider = provider;
        this.contextProvider = provider2;
        this.compatUIProvider = provider3;
        this.recentTasksOptionalProvider = provider4;
    }
}
