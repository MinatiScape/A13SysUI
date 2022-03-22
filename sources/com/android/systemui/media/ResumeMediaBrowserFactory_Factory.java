package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.statusbar.phone.ConfigurationControllerImpl;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.TransactionPool;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ResumeMediaBrowserFactory_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object browserFactoryProvider;
    public final Provider contextProvider;

    public /* synthetic */ ResumeMediaBrowserFactory_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.browserFactoryProvider = provider2;
    }

    public ResumeMediaBrowserFactory_Factory(DependencyProvider dependencyProvider, Provider provider) {
        this.$r8$classId = 2;
        this.browserFactoryProvider = dependencyProvider;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ResumeMediaBrowserFactory((Context) this.contextProvider.mo144get(), (MediaBrowserFactory) ((Provider) this.browserFactoryProvider).mo144get());
            case 1:
                return new SyncTransactionQueue((TransactionPool) this.contextProvider.mo144get(), (ShellExecutor) ((Provider) this.browserFactoryProvider).mo144get());
            default:
                Objects.requireNonNull((DependencyProvider) this.browserFactoryProvider);
                return new ConfigurationControllerImpl((Context) this.contextProvider.mo144get());
        }
    }
}
