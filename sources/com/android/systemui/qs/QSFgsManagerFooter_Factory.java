package com.android.systemui.qs;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.google.android.systemui.assist.uihints.FlingVelocityWrapper;
import com.google.android.systemui.assist.uihints.TouchInsideHandler;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFgsManagerFooter_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider executorProvider;
    public final Provider fgsManagerControllerProvider;
    public final Provider mainExecutorProvider;
    public final Provider rootViewProvider;

    public /* synthetic */ QSFgsManagerFooter_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, int i) {
        this.$r8$classId = i;
        this.rootViewProvider = provider;
        this.mainExecutorProvider = provider2;
        this.executorProvider = provider3;
        this.fgsManagerControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new QSFgsManagerFooter((View) this.rootViewProvider.mo144get(), (Executor) this.mainExecutorProvider.mo144get(), (Executor) this.executorProvider.mo144get(), (FgsManagerController) this.fgsManagerControllerProvider.mo144get());
            case 1:
                return new StatusBarHideIconsForBouncerManager((CommandQueue) this.rootViewProvider.mo144get(), (DelayableExecutor) this.mainExecutorProvider.mo144get(), (StatusBarWindowStateController) this.executorProvider.mo144get(), (DumpManager) this.fgsManagerControllerProvider.mo144get());
            default:
                return new TranscriptionController((ViewGroup) this.rootViewProvider.mo144get(), (TouchInsideHandler) this.mainExecutorProvider.mo144get(), (FlingVelocityWrapper) this.executorProvider.mo144get(), (ConfigurationController) this.fgsManagerControllerProvider.mo144get());
        }
    }
}
