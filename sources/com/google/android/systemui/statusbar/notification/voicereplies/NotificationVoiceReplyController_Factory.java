package com.google.android.systemui.statusbar.notification.voicereplies;

import android.content.Context;
import android.os.PowerManager;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController_Factory implements Factory<NotificationVoiceReplyController> {
    public final Provider<BindEventManager> bindEventManagerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    public final Provider<NotificationVoiceReplyLogger> loggerProvider;
    public final Provider<CommonNotifCollection> notifCollectionProvider;
    public final Provider<NotifLiveDataStore> notifLiveDataStoreProvider;
    public final Provider<NotificationShadeWindowController> notifShadeWindowControllerProvider;
    public final Provider<NotificationRemoteInputManager> notificationRemoteInputManagerProvider;
    public final Provider<PowerManager> powerManagerProvider;
    public final Provider<LockscreenShadeTransitionController> shadeTransitionControllerProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    public final Provider<StatusBar> statusBarProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;

    public static NotificationVoiceReplyController_Factory create(Provider<CommonNotifCollection> provider, Provider<BindEventManager> provider2, Provider<NotifLiveDataStore> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<NotificationRemoteInputManager> provider5, Provider<LockscreenShadeTransitionController> provider6, Provider<NotificationShadeWindowController> provider7, Provider<StatusBarKeyguardViewManager> provider8, Provider<StatusBar> provider9, Provider<SysuiStatusBarStateController> provider10, Provider<HeadsUpManager> provider11, Provider<PowerManager> provider12, Provider<Context> provider13, Provider<NotificationVoiceReplyLogger> provider14) {
        return new NotificationVoiceReplyController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationVoiceReplyController(this.notifCollectionProvider.mo144get(), this.bindEventManagerProvider.mo144get(), this.notifLiveDataStoreProvider.mo144get(), this.lockscreenUserManagerProvider.mo144get(), this.notificationRemoteInputManagerProvider.mo144get(), this.shadeTransitionControllerProvider.mo144get(), this.notifShadeWindowControllerProvider.mo144get(), this.statusBarKeyguardViewManagerProvider.mo144get(), this.statusBarProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.headsUpManagerProvider.mo144get(), this.powerManagerProvider.mo144get(), this.contextProvider.mo144get(), this.loggerProvider.mo144get());
    }

    public NotificationVoiceReplyController_Factory(Provider<CommonNotifCollection> provider, Provider<BindEventManager> provider2, Provider<NotifLiveDataStore> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<NotificationRemoteInputManager> provider5, Provider<LockscreenShadeTransitionController> provider6, Provider<NotificationShadeWindowController> provider7, Provider<StatusBarKeyguardViewManager> provider8, Provider<StatusBar> provider9, Provider<SysuiStatusBarStateController> provider10, Provider<HeadsUpManager> provider11, Provider<PowerManager> provider12, Provider<Context> provider13, Provider<NotificationVoiceReplyLogger> provider14) {
        this.notifCollectionProvider = provider;
        this.bindEventManagerProvider = provider2;
        this.notifLiveDataStoreProvider = provider3;
        this.lockscreenUserManagerProvider = provider4;
        this.notificationRemoteInputManagerProvider = provider5;
        this.shadeTransitionControllerProvider = provider6;
        this.notifShadeWindowControllerProvider = provider7;
        this.statusBarKeyguardViewManagerProvider = provider8;
        this.statusBarProvider = provider9;
        this.statusBarStateControllerProvider = provider10;
        this.headsUpManagerProvider = provider11;
        this.powerManagerProvider = provider12;
        this.contextProvider = provider13;
        this.loggerProvider = provider14;
    }
}
