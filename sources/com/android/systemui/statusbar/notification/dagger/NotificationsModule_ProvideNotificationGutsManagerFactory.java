package com.android.systemui.statusbar.notification.dagger;

import android.app.INotificationManager;
import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutManager;
import android.os.Handler;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.wmshell.BubblesManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsModule_ProvideNotificationGutsManagerFactory implements Factory<NotificationGutsManager> {
    public final Provider<AccessibilityManager> accessibilityManagerProvider;
    public final Provider<AssistantFeedbackController> assistantFeedbackControllerProvider;
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<Optional<BubblesManager>> bubblesManagerOptionalProvider;
    public final Provider<ChannelEditorDialogController> channelEditorDialogControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<UserContextProvider> contextTrackerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<HighPriorityProvider> highPriorityProvider;
    public final Provider<LauncherApps> launcherAppsProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    public final Provider<INotificationManager> notificationManagerProvider;
    public final Provider<OnUserInteractionCallback> onUserInteractionCallbackProvider;
    public final Provider<PeopleSpaceWidgetManager> peopleSpaceWidgetManagerProvider;
    public final Provider<ShadeController> shadeControllerProvider;
    public final Provider<ShortcutManager> shortcutManagerProvider;
    public final Provider<Optional<StatusBar>> statusBarOptionalLazyProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    public NotificationsModule_ProvideNotificationGutsManagerFactory(Provider<Context> provider, Provider<Optional<StatusBar>> provider2, Provider<Handler> provider3, Provider<Handler> provider4, Provider<AccessibilityManager> provider5, Provider<HighPriorityProvider> provider6, Provider<INotificationManager> provider7, Provider<NotificationEntryManager> provider8, Provider<PeopleSpaceWidgetManager> provider9, Provider<LauncherApps> provider10, Provider<ShortcutManager> provider11, Provider<ChannelEditorDialogController> provider12, Provider<UserContextProvider> provider13, Provider<AssistantFeedbackController> provider14, Provider<Optional<BubblesManager>> provider15, Provider<UiEventLogger> provider16, Provider<OnUserInteractionCallback> provider17, Provider<ShadeController> provider18, Provider<DumpManager> provider19) {
        this.contextProvider = provider;
        this.statusBarOptionalLazyProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.bgHandlerProvider = provider4;
        this.accessibilityManagerProvider = provider5;
        this.highPriorityProvider = provider6;
        this.notificationManagerProvider = provider7;
        this.notificationEntryManagerProvider = provider8;
        this.peopleSpaceWidgetManagerProvider = provider9;
        this.launcherAppsProvider = provider10;
        this.shortcutManagerProvider = provider11;
        this.channelEditorDialogControllerProvider = provider12;
        this.contextTrackerProvider = provider13;
        this.assistantFeedbackControllerProvider = provider14;
        this.bubblesManagerOptionalProvider = provider15;
        this.uiEventLoggerProvider = provider16;
        this.onUserInteractionCallbackProvider = provider17;
        this.shadeControllerProvider = provider18;
        this.dumpManagerProvider = provider19;
    }

    public static NotificationsModule_ProvideNotificationGutsManagerFactory create(Provider<Context> provider, Provider<Optional<StatusBar>> provider2, Provider<Handler> provider3, Provider<Handler> provider4, Provider<AccessibilityManager> provider5, Provider<HighPriorityProvider> provider6, Provider<INotificationManager> provider7, Provider<NotificationEntryManager> provider8, Provider<PeopleSpaceWidgetManager> provider9, Provider<LauncherApps> provider10, Provider<ShortcutManager> provider11, Provider<ChannelEditorDialogController> provider12, Provider<UserContextProvider> provider13, Provider<AssistantFeedbackController> provider14, Provider<Optional<BubblesManager>> provider15, Provider<UiEventLogger> provider16, Provider<OnUserInteractionCallback> provider17, Provider<ShadeController> provider18, Provider<DumpManager> provider19) {
        return new NotificationsModule_ProvideNotificationGutsManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Context context = this.contextProvider.mo144get();
        Lazy lazy = DoubleCheck.lazy(this.statusBarOptionalLazyProvider);
        Handler handler = this.mainHandlerProvider.mo144get();
        Handler handler2 = this.bgHandlerProvider.mo144get();
        AccessibilityManager accessibilityManager = this.accessibilityManagerProvider.mo144get();
        HighPriorityProvider highPriorityProvider = this.highPriorityProvider.mo144get();
        INotificationManager iNotificationManager = this.notificationManagerProvider.mo144get();
        this.notificationEntryManagerProvider.mo144get();
        return new NotificationGutsManager(context, lazy, handler, handler2, accessibilityManager, highPriorityProvider, iNotificationManager, this.peopleSpaceWidgetManagerProvider.mo144get(), this.launcherAppsProvider.mo144get(), this.shortcutManagerProvider.mo144get(), this.channelEditorDialogControllerProvider.mo144get(), this.contextTrackerProvider.mo144get(), this.assistantFeedbackControllerProvider.mo144get(), this.bubblesManagerOptionalProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.onUserInteractionCallbackProvider.mo144get(), this.shadeControllerProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }
}
