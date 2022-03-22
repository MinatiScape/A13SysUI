package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationRankingManager_Factory implements Factory<NotificationRankingManager> {
    public final Provider<NotificationGroupManagerLegacy> groupManagerProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<HighPriorityProvider> highPriorityProvider;
    public final Provider<NotificationEntryManager.KeyguardEnvironment> keyguardEnvironmentProvider;
    public final Provider<NotificationEntryManagerLogger> loggerProvider;
    public final Provider<NotificationMediaManager> mediaManagerLazyProvider;
    public final Provider<NotificationFilter> notifFilterProvider;
    public final Provider<PeopleNotificationIdentifier> peopleNotificationIdentifierProvider;
    public final Provider<NotificationSectionsFeatureManager> sectionsFeatureManagerProvider;

    public static NotificationRankingManager_Factory create(Provider<NotificationMediaManager> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<HeadsUpManager> provider3, Provider<NotificationFilter> provider4, Provider<NotificationEntryManagerLogger> provider5, Provider<NotificationSectionsFeatureManager> provider6, Provider<PeopleNotificationIdentifier> provider7, Provider<HighPriorityProvider> provider8, Provider<NotificationEntryManager.KeyguardEnvironment> provider9) {
        return new NotificationRankingManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationRankingManager(DoubleCheck.lazy(this.mediaManagerLazyProvider), this.groupManagerProvider.mo144get(), this.headsUpManagerProvider.mo144get(), this.notifFilterProvider.mo144get(), this.loggerProvider.mo144get(), this.sectionsFeatureManagerProvider.mo144get(), this.peopleNotificationIdentifierProvider.mo144get(), this.highPriorityProvider.mo144get(), this.keyguardEnvironmentProvider.mo144get());
    }

    public NotificationRankingManager_Factory(Provider<NotificationMediaManager> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<HeadsUpManager> provider3, Provider<NotificationFilter> provider4, Provider<NotificationEntryManagerLogger> provider5, Provider<NotificationSectionsFeatureManager> provider6, Provider<PeopleNotificationIdentifier> provider7, Provider<HighPriorityProvider> provider8, Provider<NotificationEntryManager.KeyguardEnvironment> provider9) {
        this.mediaManagerLazyProvider = provider;
        this.groupManagerProvider = provider2;
        this.headsUpManagerProvider = provider3;
        this.notifFilterProvider = provider4;
        this.loggerProvider = provider5;
        this.sectionsFeatureManagerProvider = provider6;
        this.peopleNotificationIdentifierProvider = provider7;
        this.highPriorityProvider = provider8;
        this.keyguardEnvironmentProvider = provider9;
    }
}
