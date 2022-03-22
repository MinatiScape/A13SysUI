package com.android.systemui.statusbar.notification.row;

import android.view.accessibility.AccessibilityManager;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import com.google.android.systemui.elmyra.actions.UnpinNotifications_Factory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ActivatableNotificationViewController_Factory implements Factory<ActivatableNotificationViewController> {
    public final Provider<AccessibilityManager> accessibilityManagerProvider;
    public final Provider<ExpandableOutlineViewController> expandableOutlineViewControllerProvider;
    public final Provider<FalsingCollector> falsingCollectorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<NotificationTapHelper.Factory> notificationTapHelpFactoryProvider;
    public final Provider<ActivatableNotificationView> viewProvider;

    public static ActivatableNotificationViewController_Factory create(Provider provider, Provider provider2, UnpinNotifications_Factory unpinNotifications_Factory, Provider provider3, Provider provider4, Provider provider5) {
        return new ActivatableNotificationViewController_Factory(provider, provider2, unpinNotifications_Factory, provider3, provider4, provider5);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ActivatableNotificationViewController(this.viewProvider.mo144get(), this.notificationTapHelpFactoryProvider.mo144get(), this.expandableOutlineViewControllerProvider.mo144get(), this.accessibilityManagerProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.falsingCollectorProvider.mo144get());
    }

    public ActivatableNotificationViewController_Factory(Provider provider, Provider provider2, UnpinNotifications_Factory unpinNotifications_Factory, Provider provider3, Provider provider4, Provider provider5) {
        this.viewProvider = provider;
        this.notificationTapHelpFactoryProvider = provider2;
        this.expandableOutlineViewControllerProvider = unpinNotifications_Factory;
        this.accessibilityManagerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.falsingCollectorProvider = provider5;
    }
}
