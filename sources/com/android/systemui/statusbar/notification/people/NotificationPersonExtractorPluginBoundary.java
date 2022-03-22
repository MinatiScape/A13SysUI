package com.android.systemui.statusbar.notification.people;

import android.service.notification.StatusBarNotification;
import com.android.systemui.plugins.NotificationPersonExtractorPlugin;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.ExtensionControllerImpl;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: PeopleHubNotificationListener.kt */
/* loaded from: classes.dex */
public final class NotificationPersonExtractorPluginBoundary implements NotificationPersonExtractor {
    public NotificationPersonExtractorPlugin plugin;

    public NotificationPersonExtractorPluginBoundary(ExtensionController extensionController) {
        ExtensionControllerImpl.ExtensionBuilder newExtension = extensionController.newExtension();
        Objects.requireNonNull(newExtension);
        String action = PluginManager.Helper.getAction(NotificationPersonExtractorPlugin.class);
        ExtensionControllerImpl.ExtensionImpl<T> extensionImpl = newExtension.mExtension;
        Objects.requireNonNull(extensionImpl);
        extensionImpl.mProducers.add(new ExtensionControllerImpl.ExtensionImpl.PluginItem(action, NotificationPersonExtractorPlugin.class, null));
        newExtension.mExtension.mCallbacks.add(new Consumer() { // from class: com.android.systemui.statusbar.notification.people.NotificationPersonExtractorPluginBoundary.1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NotificationPersonExtractorPluginBoundary.this.plugin = (NotificationPersonExtractorPlugin) obj;
            }
        });
        ExtensionControllerImpl.ExtensionImpl build = newExtension.build();
        Objects.requireNonNull(build);
        this.plugin = (NotificationPersonExtractorPlugin) build.mItem;
    }

    @Override // com.android.systemui.statusbar.notification.people.NotificationPersonExtractor
    public final boolean isPersonNotification(StatusBarNotification statusBarNotification) {
        NotificationPersonExtractorPlugin notificationPersonExtractorPlugin = this.plugin;
        if (notificationPersonExtractorPlugin == null) {
            return false;
        }
        return notificationPersonExtractorPlugin.isPersonNotification(statusBarNotification);
    }
}
