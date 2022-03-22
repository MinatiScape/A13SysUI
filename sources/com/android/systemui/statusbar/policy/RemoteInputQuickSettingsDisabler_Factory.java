package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.view.IWindowManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RemoteInputQuickSettingsDisabler_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider commandQueueProvider;
    public final Provider configControllerProvider;
    public final Provider contextProvider;

    public /* synthetic */ RemoteInputQuickSettingsDisabler_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.configControllerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new RemoteInputQuickSettingsDisabler((Context) this.contextProvider.mo144get(), (CommandQueue) this.commandQueueProvider.mo144get(), (ConfigurationController) this.configControllerProvider.mo144get());
            default:
                return new DisplayInsetsController((IWindowManager) this.contextProvider.mo144get(), (DisplayController) this.commandQueueProvider.mo144get(), (ShellExecutor) this.configControllerProvider.mo144get());
        }
    }
}
