package com.android.systemui.statusbar.notification.row;

import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.controls.ui.ControlsActivity_Factory;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.dagger.RemoteInputViewSubcomponent;
import com.android.systemui.util.time.SystemClock;
import com.android.systemui.wmshell.BubblesManager;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ExpandableNotificationRowController_Factory implements Factory<ExpandableNotificationRowController> {
    public final Provider<ActivatableNotificationViewController> activatableNotificationViewControllerProvider;
    public final Provider<Boolean> allowLongPressProvider;
    public final Provider<String> appNameProvider;
    public final Provider<Optional<BubblesManager>> bubblesManagerOptionalProvider;
    public final Provider<SystemClock> clockProvider;
    public final Provider<ExpandableNotificationRowDragController> dragControllerProvider;
    public final Provider<FalsingCollector> falsingCollectorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<GroupExpansionManager> groupExpansionManagerProvider;
    public final Provider<GroupMembershipManager> groupMembershipManagerProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    public final Provider<NotificationListContainer> listContainerProvider;
    public final Provider<NotificationMediaManager> mediaManagerProvider;
    public final Provider<NotificationGutsManager> notificationGutsManagerProvider;
    public final Provider<String> notificationKeyProvider;
    public final Provider<NotificationLogger> notificationLoggerProvider;
    public final Provider<ExpandableNotificationRow.OnExpandClickListener> onExpandClickListenerProvider;
    public final Provider<OnUserInteractionCallback> onUserInteractionCallbackProvider;
    public final Provider<PeopleNotificationIdentifier> peopleNotificationIdentifierProvider;
    public final Provider<PluginManager> pluginManagerProvider;
    public final Provider<RemoteInputViewSubcomponent.Factory> rivSubcomponentFactoryProvider;
    public final Provider<RowContentBindStage> rowContentBindStageProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<ExpandableNotificationRow> viewProvider;

    public ExpandableNotificationRowController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15, Provider provider16, Provider provider17, Provider provider18, Provider provider19, Provider provider20, Provider provider21, Provider provider22, Provider provider23, Provider provider24, Provider provider25, ControlsActivity_Factory controlsActivity_Factory) {
        this.viewProvider = provider;
        this.listContainerProvider = provider2;
        this.rivSubcomponentFactoryProvider = provider3;
        this.activatableNotificationViewControllerProvider = provider4;
        this.mediaManagerProvider = provider5;
        this.pluginManagerProvider = provider6;
        this.clockProvider = provider7;
        this.appNameProvider = provider8;
        this.notificationKeyProvider = provider9;
        this.keyguardBypassControllerProvider = provider10;
        this.groupMembershipManagerProvider = provider11;
        this.groupExpansionManagerProvider = provider12;
        this.rowContentBindStageProvider = provider13;
        this.notificationLoggerProvider = provider14;
        this.headsUpManagerProvider = provider15;
        this.onExpandClickListenerProvider = provider16;
        this.statusBarStateControllerProvider = provider17;
        this.notificationGutsManagerProvider = provider18;
        this.allowLongPressProvider = provider19;
        this.onUserInteractionCallbackProvider = provider20;
        this.falsingManagerProvider = provider21;
        this.falsingCollectorProvider = provider22;
        this.featureFlagsProvider = provider23;
        this.peopleNotificationIdentifierProvider = provider24;
        this.bubblesManagerOptionalProvider = provider25;
        this.dragControllerProvider = controlsActivity_Factory;
    }

    public static ExpandableNotificationRowController_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15, Provider provider16, Provider provider17, Provider provider18, Provider provider19, Provider provider20, Provider provider21, Provider provider22, Provider provider23, Provider provider24, Provider provider25, ControlsActivity_Factory controlsActivity_Factory) {
        return new ExpandableNotificationRowController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, controlsActivity_Factory);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ExpandableNotificationRowController(this.viewProvider.mo144get(), this.listContainerProvider.mo144get(), this.rivSubcomponentFactoryProvider.mo144get(), this.activatableNotificationViewControllerProvider.mo144get(), this.mediaManagerProvider.mo144get(), this.pluginManagerProvider.mo144get(), this.clockProvider.mo144get(), this.appNameProvider.mo144get(), this.notificationKeyProvider.mo144get(), this.keyguardBypassControllerProvider.mo144get(), this.groupMembershipManagerProvider.mo144get(), this.groupExpansionManagerProvider.mo144get(), this.rowContentBindStageProvider.mo144get(), this.notificationLoggerProvider.mo144get(), this.headsUpManagerProvider.mo144get(), this.onExpandClickListenerProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.notificationGutsManagerProvider.mo144get(), this.allowLongPressProvider.mo144get().booleanValue(), this.onUserInteractionCallbackProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.falsingCollectorProvider.mo144get(), this.featureFlagsProvider.mo144get(), this.peopleNotificationIdentifierProvider.mo144get(), this.bubblesManagerOptionalProvider.mo144get(), this.dragControllerProvider.mo144get());
    }
}
