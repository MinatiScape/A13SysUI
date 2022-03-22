package com.android.systemui.statusbar.phone;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Handler;
import android.service.dreams.IDreamManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.wmshell.BubblesManager;
import com.google.android.systemui.assist.uihints.ColorChangeHandler_Factory;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarNotificationActivityStarter_Builder_Factory implements Factory<StatusBarNotificationActivityStarter.Builder> {
    public final Provider<ActivityIntentHelper> activityIntentHelperProvider;
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<AssistManager> assistManagerLazyProvider;
    public final Provider<Optional<BubblesManager>> bubblesManagerProvider;
    public final Provider<NotificationClickNotifier> clickNotifierProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<IDreamManager> dreamManagerProvider;
    public final Provider<NotificationEntryManager> entryManagerProvider;
    public final Provider<GroupMembershipManager> groupMembershipManagerProvider;
    public final Provider<HeadsUpManagerPhone> headsUpManagerProvider;
    public final Provider<KeyguardManager> keyguardManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<LockPatternUtils> lockPatternUtilsProvider;
    public final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    public final Provider<StatusBarNotificationActivityStarterLogger> loggerProvider;
    public final Provider<Handler> mainThreadHandlerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotifPipeline> notifPipelineProvider;
    public final Provider<NotificationInterruptStateProvider> notificationInterruptStateProvider;
    public final Provider<OnUserInteractionCallback> onUserInteractionCallbackProvider;
    public final Provider<StatusBarRemoteInputCallback> remoteInputCallbackProvider;
    public final Provider<NotificationRemoteInputManager> remoteInputManagerProvider;
    public final Provider<ShadeController> shadeControllerProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<Executor> uiBgExecutorProvider;
    public final Provider<NotificationVisibilityProvider> visibilityProvider;

    public StatusBarNotificationActivityStarter_Builder_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15, Provider provider16, Provider provider17, Provider provider18, Provider provider19, Provider provider20, Provider provider21, Provider provider22, Provider provider23, Provider provider24, Provider provider25, Provider provider26, Provider provider27, ColorChangeHandler_Factory colorChangeHandler_Factory, Provider provider28) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.mainThreadHandlerProvider = provider3;
        this.uiBgExecutorProvider = provider4;
        this.entryManagerProvider = provider5;
        this.notifPipelineProvider = provider6;
        this.visibilityProvider = provider7;
        this.headsUpManagerProvider = provider8;
        this.activityStarterProvider = provider9;
        this.clickNotifierProvider = provider10;
        this.statusBarStateControllerProvider = provider11;
        this.statusBarKeyguardViewManagerProvider = provider12;
        this.keyguardManagerProvider = provider13;
        this.dreamManagerProvider = provider14;
        this.bubblesManagerProvider = provider15;
        this.assistManagerLazyProvider = provider16;
        this.remoteInputManagerProvider = provider17;
        this.groupMembershipManagerProvider = provider18;
        this.lockscreenUserManagerProvider = provider19;
        this.shadeControllerProvider = provider20;
        this.keyguardStateControllerProvider = provider21;
        this.notificationInterruptStateProvider = provider22;
        this.lockPatternUtilsProvider = provider23;
        this.remoteInputCallbackProvider = provider24;
        this.activityIntentHelperProvider = provider25;
        this.notifPipelineFlagsProvider = provider26;
        this.metricsLoggerProvider = provider27;
        this.loggerProvider = colorChangeHandler_Factory;
        this.onUserInteractionCallbackProvider = provider28;
    }

    public static StatusBarNotificationActivityStarter_Builder_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15, Provider provider16, Provider provider17, Provider provider18, Provider provider19, Provider provider20, Provider provider21, Provider provider22, Provider provider23, Provider provider24, Provider provider25, Provider provider26, Provider provider27, ColorChangeHandler_Factory colorChangeHandler_Factory, Provider provider28) {
        return new StatusBarNotificationActivityStarter_Builder_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, colorChangeHandler_Factory, provider28);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new StatusBarNotificationActivityStarter.Builder(this.contextProvider.mo144get(), this.commandQueueProvider.mo144get(), this.mainThreadHandlerProvider.mo144get(), this.uiBgExecutorProvider.mo144get(), this.entryManagerProvider.mo144get(), this.notifPipelineProvider.mo144get(), this.visibilityProvider.mo144get(), this.headsUpManagerProvider.mo144get(), this.activityStarterProvider.mo144get(), this.clickNotifierProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.statusBarKeyguardViewManagerProvider.mo144get(), this.keyguardManagerProvider.mo144get(), this.dreamManagerProvider.mo144get(), this.bubblesManagerProvider.mo144get(), DoubleCheck.lazy(this.assistManagerLazyProvider), this.remoteInputManagerProvider.mo144get(), this.groupMembershipManagerProvider.mo144get(), this.lockscreenUserManagerProvider.mo144get(), this.shadeControllerProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), this.notificationInterruptStateProvider.mo144get(), this.lockPatternUtilsProvider.mo144get(), this.remoteInputCallbackProvider.mo144get(), this.activityIntentHelperProvider.mo144get(), this.notifPipelineFlagsProvider.mo144get(), this.metricsLoggerProvider.mo144get(), this.loggerProvider.mo144get(), this.onUserInteractionCallbackProvider.mo144get());
    }
}
