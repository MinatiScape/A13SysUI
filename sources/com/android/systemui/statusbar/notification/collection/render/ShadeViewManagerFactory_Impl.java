package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ShadeViewManagerFactory_Impl implements ShadeViewManagerFactory {
    public final ShadeViewManager_Factory delegateFactory;

    @Override // com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory
    public final ShadeViewManager create(NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl, NotificationStackScrollLayoutController.NotifStackControllerImpl notifStackControllerImpl) {
        ShadeViewManager_Factory shadeViewManager_Factory = this.delegateFactory;
        Objects.requireNonNull(shadeViewManager_Factory);
        return new ShadeViewManager(shadeViewManager_Factory.contextProvider.mo144get(), notificationListContainerImpl, notifStackControllerImpl, shadeViewManager_Factory.mediaContainerControllerProvider.mo144get(), shadeViewManager_Factory.featureManagerProvider.mo144get(), shadeViewManager_Factory.sectionHeaderVisibilityProvider.mo144get(), shadeViewManager_Factory.loggerProvider.mo144get(), shadeViewManager_Factory.viewBarnProvider.mo144get());
    }

    public ShadeViewManagerFactory_Impl(ShadeViewManager_Factory shadeViewManager_Factory) {
        this.delegateFactory = shadeViewManager_Factory;
    }
}
