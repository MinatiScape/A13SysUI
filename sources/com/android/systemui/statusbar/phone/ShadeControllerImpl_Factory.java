package com.android.systemui.statusbar.phone;

import android.view.WindowManager;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ShadeControllerImpl_Factory implements Factory<ShadeControllerImpl> {
    public final Provider<AssistManager> assistManagerLazyProvider;
    public final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    public final Provider<Optional<StatusBar>> statusBarOptionalLazyProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<WindowManager> windowManagerProvider;

    public static ShadeControllerImpl_Factory create(Provider<CommandQueue> provider, Provider<StatusBarStateController> provider2, Provider<NotificationShadeWindowController> provider3, Provider<StatusBarKeyguardViewManager> provider4, Provider<WindowManager> provider5, Provider<Optional<StatusBar>> provider6, Provider<AssistManager> provider7, Provider<Optional<Bubbles>> provider8) {
        return new ShadeControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ShadeControllerImpl(this.commandQueueProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.notificationShadeWindowControllerProvider.mo144get(), this.statusBarKeyguardViewManagerProvider.mo144get(), this.windowManagerProvider.mo144get(), DoubleCheck.lazy(this.statusBarOptionalLazyProvider), DoubleCheck.lazy(this.assistManagerLazyProvider), this.bubblesOptionalProvider.mo144get());
    }

    public ShadeControllerImpl_Factory(Provider<CommandQueue> provider, Provider<StatusBarStateController> provider2, Provider<NotificationShadeWindowController> provider3, Provider<StatusBarKeyguardViewManager> provider4, Provider<WindowManager> provider5, Provider<Optional<StatusBar>> provider6, Provider<AssistManager> provider7, Provider<Optional<Bubbles>> provider8) {
        this.commandQueueProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.notificationShadeWindowControllerProvider = provider3;
        this.statusBarKeyguardViewManagerProvider = provider4;
        this.windowManagerProvider = provider5;
        this.statusBarOptionalLazyProvider = provider6;
        this.assistManagerLazyProvider = provider7;
        this.bubblesOptionalProvider = provider8;
    }
}
