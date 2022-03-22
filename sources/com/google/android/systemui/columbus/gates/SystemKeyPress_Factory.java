package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import com.android.launcher3.icons.IconProvider;
import com.android.systemui.statusbar.CommandQueue;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import dagger.internal.Factory;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemKeyPress_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider blockingKeysProvider;
    public final Provider commandQueueProvider;
    public final Provider contextProvider;
    public final Provider gateDurationProvider;
    public final Provider handlerProvider;

    public /* synthetic */ SystemKeyPress_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.commandQueueProvider = provider3;
        this.gateDurationProvider = provider4;
        this.blockingKeysProvider = provider5;
    }

    public static SystemKeyPress_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        return new SystemKeyPress_Factory(provider, provider2, provider3, provider4, provider5, 1);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new SystemKeyPress((Context) this.contextProvider.mo144get(), (Handler) this.handlerProvider.mo144get(), (CommandQueue) this.commandQueueProvider.mo144get(), ((Long) this.gateDurationProvider.mo144get()).longValue(), (Set) this.blockingKeysProvider.mo144get());
            default:
                return new StartingWindowController((Context) this.contextProvider.mo144get(), (ShellExecutor) this.handlerProvider.mo144get(), (StartingWindowTypeAlgorithm) this.commandQueueProvider.mo144get(), (IconProvider) this.gateDurationProvider.mo144get(), (TransactionPool) this.blockingKeysProvider.mo144get());
        }
    }
}
