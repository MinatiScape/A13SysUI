package com.android.systemui.statusbar.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQuickQSPanelFactory;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.DynamicChildBindController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarDependenciesModule_ProvideNotificationViewHierarchyManagerFactory implements Factory<NotificationViewHierarchyManager> {
    public final Provider<AssistantFeedbackController> assistantFeedbackControllerProvider;
    public final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    public final Provider<KeyguardBypassController> bypassControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DynamicChildBindController> dynamicChildBindControllerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<ForegroundServiceSectionController> fgsSectionControllerProvider;
    public final Provider<NotificationGroupManagerLegacy> groupManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LowPriorityInflationHelper> lowPriorityInflationHelperProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    public final Provider<NotificationLockscreenUserManager> notificationLockscreenUserManagerProvider;
    public final Provider<DynamicPrivacyController> privacyControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<VisualStabilityManager> visualStabilityManagerProvider;

    public StatusBarDependenciesModule_ProvideNotificationViewHierarchyManagerFactory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, QSFragmentModule_ProvidesQuickQSPanelFactory qSFragmentModule_ProvidesQuickQSPanelFactory, Provider provider13, Provider provider14, Provider provider15, Provider provider16, Provider provider17) {
        this.contextProvider = provider;
        this.mainHandlerProvider = provider2;
        this.featureFlagsProvider = provider3;
        this.notificationLockscreenUserManagerProvider = provider4;
        this.groupManagerProvider = provider5;
        this.visualStabilityManagerProvider = provider6;
        this.statusBarStateControllerProvider = provider7;
        this.notificationEntryManagerProvider = provider8;
        this.bypassControllerProvider = provider9;
        this.bubblesOptionalProvider = provider10;
        this.privacyControllerProvider = provider11;
        this.fgsSectionControllerProvider = provider12;
        this.dynamicChildBindControllerProvider = qSFragmentModule_ProvidesQuickQSPanelFactory;
        this.lowPriorityInflationHelperProvider = provider13;
        this.assistantFeedbackControllerProvider = provider14;
        this.notifPipelineFlagsProvider = provider15;
        this.keyguardUpdateMonitorProvider = provider16;
        this.keyguardStateControllerProvider = provider17;
    }

    public static StatusBarDependenciesModule_ProvideNotificationViewHierarchyManagerFactory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, QSFragmentModule_ProvidesQuickQSPanelFactory qSFragmentModule_ProvidesQuickQSPanelFactory, Provider provider13, Provider provider14, Provider provider15, Provider provider16, Provider provider17) {
        return new StatusBarDependenciesModule_ProvideNotificationViewHierarchyManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, qSFragmentModule_ProvidesQuickQSPanelFactory, provider13, provider14, provider15, provider16, provider17);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Context context = this.contextProvider.mo144get();
        Handler handler = this.mainHandlerProvider.mo144get();
        FeatureFlags featureFlags = this.featureFlagsProvider.mo144get();
        NotificationLockscreenUserManager notificationLockscreenUserManager = this.notificationLockscreenUserManagerProvider.mo144get();
        NotificationGroupManagerLegacy notificationGroupManagerLegacy = this.groupManagerProvider.mo144get();
        VisualStabilityManager visualStabilityManager = this.visualStabilityManagerProvider.mo144get();
        StatusBarStateController statusBarStateController = this.statusBarStateControllerProvider.mo144get();
        NotificationEntryManager notificationEntryManager = this.notificationEntryManagerProvider.mo144get();
        this.bypassControllerProvider.mo144get();
        return new NotificationViewHierarchyManager(context, handler, featureFlags, notificationLockscreenUserManager, notificationGroupManagerLegacy, visualStabilityManager, statusBarStateController, notificationEntryManager, this.bubblesOptionalProvider.mo144get(), this.privacyControllerProvider.mo144get(), this.fgsSectionControllerProvider.mo144get(), this.dynamicChildBindControllerProvider.mo144get(), this.lowPriorityInflationHelperProvider.mo144get(), this.assistantFeedbackControllerProvider.mo144get(), this.notifPipelineFlagsProvider.mo144get(), this.keyguardUpdateMonitorProvider.mo144get(), this.keyguardStateControllerProvider.mo144get());
    }
}
