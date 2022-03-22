package com.android.systemui.statusbar.notification.init;

import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationClicker;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsControllerImpl_Factory implements Factory<NotificationsControllerImpl> {
    public final Provider<AnimatedImageNotificationManager> animatedImageNotificationManagerProvider;
    public final Provider<BindEventManagerImpl> bindEventManagerImplProvider;
    public final Provider<NotificationClicker.Builder> clickerBuilderProvider;
    public final Provider<CommonNotifCollection> commonNotifCollectionProvider;
    public final Provider<DebugModeFilterProvider> debugModeFilterProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<NotificationEntryManager> entryManagerProvider;
    public final Provider<NotificationGroupAlertTransferHelper> groupAlertTransferHelperProvider;
    public final Provider<NotificationGroupManagerLegacy> groupManagerLegacyProvider;
    public final Provider<HeadsUpController> headsUpControllerProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<HeadsUpViewBinder> headsUpViewBinderProvider;
    public final Provider<NotificationRankingManager> legacyRankerProvider;
    public final Provider<NotifPipelineInitializer> newNotifPipelineInitializerProvider;
    public final Provider<NotifBindPipelineInitializer> notifBindPipelineInitializerProvider;
    public final Provider<NotifLiveDataStore> notifLiveDataStoreProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotifPipeline> notifPipelineProvider;
    public final Provider<NotificationListener> notificationListenerProvider;
    public final Provider<NotificationRowBinderImpl> notificationRowBinderProvider;
    public final Provider<PeopleSpaceWidgetManager> peopleSpaceWidgetManagerProvider;
    public final Provider<RemoteInputUriController> remoteInputUriControllerProvider;
    public final Provider<TargetSdkResolver> targetSdkResolverProvider;

    public NotificationsControllerImpl_Factory(Provider<NotifPipelineFlags> provider, Provider<NotificationListener> provider2, Provider<NotificationEntryManager> provider3, Provider<DebugModeFilterProvider> provider4, Provider<NotificationRankingManager> provider5, Provider<CommonNotifCollection> provider6, Provider<NotifPipeline> provider7, Provider<NotifLiveDataStore> provider8, Provider<TargetSdkResolver> provider9, Provider<NotifPipelineInitializer> provider10, Provider<NotifBindPipelineInitializer> provider11, Provider<DeviceProvisionedController> provider12, Provider<NotificationRowBinderImpl> provider13, Provider<BindEventManagerImpl> provider14, Provider<RemoteInputUriController> provider15, Provider<NotificationGroupManagerLegacy> provider16, Provider<NotificationGroupAlertTransferHelper> provider17, Provider<HeadsUpManager> provider18, Provider<HeadsUpController> provider19, Provider<HeadsUpViewBinder> provider20, Provider<NotificationClicker.Builder> provider21, Provider<AnimatedImageNotificationManager> provider22, Provider<PeopleSpaceWidgetManager> provider23) {
        this.notifPipelineFlagsProvider = provider;
        this.notificationListenerProvider = provider2;
        this.entryManagerProvider = provider3;
        this.debugModeFilterProvider = provider4;
        this.legacyRankerProvider = provider5;
        this.commonNotifCollectionProvider = provider6;
        this.notifPipelineProvider = provider7;
        this.notifLiveDataStoreProvider = provider8;
        this.targetSdkResolverProvider = provider9;
        this.newNotifPipelineInitializerProvider = provider10;
        this.notifBindPipelineInitializerProvider = provider11;
        this.deviceProvisionedControllerProvider = provider12;
        this.notificationRowBinderProvider = provider13;
        this.bindEventManagerImplProvider = provider14;
        this.remoteInputUriControllerProvider = provider15;
        this.groupManagerLegacyProvider = provider16;
        this.groupAlertTransferHelperProvider = provider17;
        this.headsUpManagerProvider = provider18;
        this.headsUpControllerProvider = provider19;
        this.headsUpViewBinderProvider = provider20;
        this.clickerBuilderProvider = provider21;
        this.animatedImageNotificationManagerProvider = provider22;
        this.peopleSpaceWidgetManagerProvider = provider23;
    }

    public static NotificationsControllerImpl_Factory create(Provider<NotifPipelineFlags> provider, Provider<NotificationListener> provider2, Provider<NotificationEntryManager> provider3, Provider<DebugModeFilterProvider> provider4, Provider<NotificationRankingManager> provider5, Provider<CommonNotifCollection> provider6, Provider<NotifPipeline> provider7, Provider<NotifLiveDataStore> provider8, Provider<TargetSdkResolver> provider9, Provider<NotifPipelineInitializer> provider10, Provider<NotifBindPipelineInitializer> provider11, Provider<DeviceProvisionedController> provider12, Provider<NotificationRowBinderImpl> provider13, Provider<BindEventManagerImpl> provider14, Provider<RemoteInputUriController> provider15, Provider<NotificationGroupManagerLegacy> provider16, Provider<NotificationGroupAlertTransferHelper> provider17, Provider<HeadsUpManager> provider18, Provider<HeadsUpController> provider19, Provider<HeadsUpViewBinder> provider20, Provider<NotificationClicker.Builder> provider21, Provider<AnimatedImageNotificationManager> provider22, Provider<PeopleSpaceWidgetManager> provider23) {
        return new NotificationsControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationsControllerImpl(this.notifPipelineFlagsProvider.mo144get(), this.notificationListenerProvider.mo144get(), this.entryManagerProvider.mo144get(), this.debugModeFilterProvider.mo144get(), this.legacyRankerProvider.mo144get(), DoubleCheck.lazy(this.commonNotifCollectionProvider), DoubleCheck.lazy(this.notifPipelineProvider), this.notifLiveDataStoreProvider.mo144get(), this.targetSdkResolverProvider.mo144get(), DoubleCheck.lazy(this.newNotifPipelineInitializerProvider), this.notifBindPipelineInitializerProvider.mo144get(), this.deviceProvisionedControllerProvider.mo144get(), this.notificationRowBinderProvider.mo144get(), this.bindEventManagerImplProvider.mo144get(), this.remoteInputUriControllerProvider.mo144get(), DoubleCheck.lazy(this.groupManagerLegacyProvider), this.groupAlertTransferHelperProvider.mo144get(), this.headsUpManagerProvider.mo144get(), this.headsUpControllerProvider.mo144get(), this.headsUpViewBinderProvider.mo144get(), this.clickerBuilderProvider.mo144get(), this.animatedImageNotificationManagerProvider.mo144get(), this.peopleSpaceWidgetManagerProvider.mo144get());
    }
}
