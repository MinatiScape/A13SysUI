package com.android.systemui.statusbar.dagger;

import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarDependenciesModule_ProvideSmartReplyControllerFactory implements Factory<SmartReplyController> {
    public final Provider<NotificationClickNotifier> clickNotifierProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<IStatusBarService> statusBarServiceProvider;
    public final Provider<NotificationVisibilityProvider> visibilityProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SmartReplyController(this.dumpManagerProvider.mo144get(), this.visibilityProvider.mo144get(), this.statusBarServiceProvider.mo144get(), this.clickNotifierProvider.mo144get());
    }

    public StatusBarDependenciesModule_ProvideSmartReplyControllerFactory(Provider<DumpManager> provider, Provider<NotificationVisibilityProvider> provider2, Provider<IStatusBarService> provider3, Provider<NotificationClickNotifier> provider4) {
        this.dumpManagerProvider = provider;
        this.visibilityProvider = provider2;
        this.statusBarServiceProvider = provider3;
        this.clickNotifierProvider = provider4;
    }
}
