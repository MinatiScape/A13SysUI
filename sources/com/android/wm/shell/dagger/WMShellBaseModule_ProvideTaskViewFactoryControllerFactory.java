package com.android.wm.shell.dagger;

import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.TaskViewFactoryController;
import com.android.wm.shell.TaskViewTransitions;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideTaskViewFactoryControllerFactory implements Factory<TaskViewFactoryController> {
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    public final Provider<SyncTransactionQueue> syncQueueProvider;
    public final Provider<TaskViewTransitions> taskViewTransitionsProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new TaskViewFactoryController(this.shellTaskOrganizerProvider.mo144get(), this.mainExecutorProvider.mo144get(), this.syncQueueProvider.mo144get(), this.taskViewTransitionsProvider.mo144get());
    }

    public WMShellBaseModule_ProvideTaskViewFactoryControllerFactory(Provider<ShellTaskOrganizer> provider, Provider<ShellExecutor> provider2, Provider<SyncTransactionQueue> provider3, Provider<TaskViewTransitions> provider4) {
        this.shellTaskOrganizerProvider = provider;
        this.mainExecutorProvider = provider2;
        this.syncQueueProvider = provider3;
        this.taskViewTransitionsProvider = provider4;
    }
}
