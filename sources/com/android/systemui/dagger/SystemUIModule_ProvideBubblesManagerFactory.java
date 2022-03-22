package com.android.systemui.dagger;

import android.app.INotificationManager;
import android.content.Context;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.model.SysUiState;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemUIModule_ProvideBubblesManagerFactory implements Factory<Optional<BubblesManager>> {
    public final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<NotificationEntryManager> entryManagerProvider;
    public final Provider<NotificationGroupManagerLegacy> groupManagerProvider;
    public final Provider<NotificationInterruptStateProvider> interruptionStateProvider;
    public final Provider<CommonNotifCollection> notifCollectionProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotifPipeline> notifPipelineProvider;
    public final Provider<NotificationLockscreenUserManager> notifUserManagerProvider;
    public final Provider<INotificationManager> notificationManagerProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    public final Provider<ShadeController> shadeControllerProvider;
    public final Provider<IStatusBarService> statusBarServiceProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<SysUiState> sysUiStateProvider;
    public final Provider<Executor> sysuiMainExecutorProvider;
    public final Provider<NotificationVisibilityProvider> visibilityProvider;
    public final Provider<ZenModeController> zenModeControllerProvider;

    public SystemUIModule_ProvideBubblesManagerFactory(Provider<Context> provider, Provider<Optional<Bubbles>> provider2, Provider<NotificationShadeWindowController> provider3, Provider<StatusBarStateController> provider4, Provider<ShadeController> provider5, Provider<ConfigurationController> provider6, Provider<IStatusBarService> provider7, Provider<INotificationManager> provider8, Provider<NotificationVisibilityProvider> provider9, Provider<NotificationInterruptStateProvider> provider10, Provider<ZenModeController> provider11, Provider<NotificationLockscreenUserManager> provider12, Provider<NotificationGroupManagerLegacy> provider13, Provider<NotificationEntryManager> provider14, Provider<CommonNotifCollection> provider15, Provider<NotifPipeline> provider16, Provider<SysUiState> provider17, Provider<NotifPipelineFlags> provider18, Provider<DumpManager> provider19, Provider<Executor> provider20) {
        this.contextProvider = provider;
        this.bubblesOptionalProvider = provider2;
        this.notificationShadeWindowControllerProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
        this.shadeControllerProvider = provider5;
        this.configurationControllerProvider = provider6;
        this.statusBarServiceProvider = provider7;
        this.notificationManagerProvider = provider8;
        this.visibilityProvider = provider9;
        this.interruptionStateProvider = provider10;
        this.zenModeControllerProvider = provider11;
        this.notifUserManagerProvider = provider12;
        this.groupManagerProvider = provider13;
        this.entryManagerProvider = provider14;
        this.notifCollectionProvider = provider15;
        this.notifPipelineProvider = provider16;
        this.sysUiStateProvider = provider17;
        this.notifPipelineFlagsProvider = provider18;
        this.dumpManagerProvider = provider19;
        this.sysuiMainExecutorProvider = provider20;
    }

    public static SystemUIModule_ProvideBubblesManagerFactory create(Provider<Context> provider, Provider<Optional<Bubbles>> provider2, Provider<NotificationShadeWindowController> provider3, Provider<StatusBarStateController> provider4, Provider<ShadeController> provider5, Provider<ConfigurationController> provider6, Provider<IStatusBarService> provider7, Provider<INotificationManager> provider8, Provider<NotificationVisibilityProvider> provider9, Provider<NotificationInterruptStateProvider> provider10, Provider<ZenModeController> provider11, Provider<NotificationLockscreenUserManager> provider12, Provider<NotificationGroupManagerLegacy> provider13, Provider<NotificationEntryManager> provider14, Provider<CommonNotifCollection> provider15, Provider<NotifPipeline> provider16, Provider<SysUiState> provider17, Provider<NotifPipelineFlags> provider18, Provider<DumpManager> provider19, Provider<Executor> provider20) {
        return new SystemUIModule_ProvideBubblesManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        BubblesManager bubblesManager;
        Context context = this.contextProvider.mo144get();
        Optional<Bubbles> optional = this.bubblesOptionalProvider.mo144get();
        NotificationShadeWindowController notificationShadeWindowController = this.notificationShadeWindowControllerProvider.mo144get();
        StatusBarStateController statusBarStateController = this.statusBarStateControllerProvider.mo144get();
        ShadeController shadeController = this.shadeControllerProvider.mo144get();
        ConfigurationController configurationController = this.configurationControllerProvider.mo144get();
        IStatusBarService iStatusBarService = this.statusBarServiceProvider.mo144get();
        INotificationManager iNotificationManager = this.notificationManagerProvider.mo144get();
        NotificationVisibilityProvider notificationVisibilityProvider = this.visibilityProvider.mo144get();
        NotificationInterruptStateProvider notificationInterruptStateProvider = this.interruptionStateProvider.mo144get();
        ZenModeController zenModeController = this.zenModeControllerProvider.mo144get();
        NotificationLockscreenUserManager notificationLockscreenUserManager = this.notifUserManagerProvider.mo144get();
        NotificationGroupManagerLegacy notificationGroupManagerLegacy = this.groupManagerProvider.mo144get();
        NotificationEntryManager notificationEntryManager = this.entryManagerProvider.mo144get();
        CommonNotifCollection commonNotifCollection = this.notifCollectionProvider.mo144get();
        NotifPipeline notifPipeline = this.notifPipelineProvider.mo144get();
        SysUiState sysUiState = this.sysUiStateProvider.mo144get();
        NotifPipelineFlags notifPipelineFlags = this.notifPipelineFlagsProvider.mo144get();
        DumpManager dumpManager = this.dumpManagerProvider.mo144get();
        Executor executor = this.sysuiMainExecutorProvider.mo144get();
        if (optional.isPresent()) {
            bubblesManager = new BubblesManager(context, optional.get(), notificationShadeWindowController, statusBarStateController, shadeController, configurationController, iStatusBarService, iNotificationManager, notificationVisibilityProvider, notificationInterruptStateProvider, zenModeController, notificationLockscreenUserManager, notificationGroupManagerLegacy, notificationEntryManager, commonNotifCollection, notifPipeline, sysUiState, notifPipelineFlags, dumpManager, executor);
        } else {
            bubblesManager = null;
        }
        Optional ofNullable = Optional.ofNullable(bubblesManager);
        Objects.requireNonNull(ofNullable, "Cannot return null from a non-@Nullable @Provides method");
        return ofNullable;
    }
}
