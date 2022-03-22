package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.recents.RecentTasksController;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideRecentTasksControllerFactory implements Factory<Optional<RecentTasksController>> {
    public final Provider<Context> contextProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<TaskStackListenerImpl> taskStackListenerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        RecentTasksController recentTasksController;
        Context context = this.contextProvider.mo144get();
        TaskStackListenerImpl taskStackListenerImpl = this.taskStackListenerProvider.mo144get();
        ShellExecutor shellExecutor = this.mainExecutorProvider.mo144get();
        if (!context.getResources().getBoolean(17891670)) {
            recentTasksController = null;
        } else {
            recentTasksController = new RecentTasksController(context, taskStackListenerImpl, shellExecutor);
        }
        Optional ofNullable = Optional.ofNullable(recentTasksController);
        Objects.requireNonNull(ofNullable, "Cannot return null from a non-@Nullable @Provides method");
        return ofNullable;
    }

    public WMShellBaseModule_ProvideRecentTasksControllerFactory(Provider<Context> provider, Provider<TaskStackListenerImpl> provider2, Provider<ShellExecutor> provider3) {
        this.contextProvider = provider;
        this.taskStackListenerProvider = provider2;
        this.mainExecutorProvider = provider3;
    }
}
