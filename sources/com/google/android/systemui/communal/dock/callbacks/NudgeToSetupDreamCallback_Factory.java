package com.google.android.systemui.communal.dock.callbacks;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.net.Uri;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.google.android.systemui.communal.dreams.SetupDreamComplication;
import com.google.android.systemui.communal.dreams.dagger.SetupDreamModule_ProvidesSetupDreamNotificationIdFactory;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NudgeToSetupDreamCallback_Factory implements Factory<NudgeToSetupDreamCallback> {
    public final Provider<SetupDreamComplication> complicationProvider;
    public final Provider<ContentResolver> contentResolverProvider;
    public final Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
    public final Provider<Boolean> dreamSelectedProvider;
    public final Provider<Integer> notificationIdProvider;
    public final Provider<Notification> notificationLazyProvider;
    public final Provider<NotificationManager> notificationManagerProvider;
    public final Provider<Uri> settingUriProvider;

    public NudgeToSetupDreamCallback_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7) {
        SetupDreamModule_ProvidesSetupDreamNotificationIdFactory setupDreamModule_ProvidesSetupDreamNotificationIdFactory = SetupDreamModule_ProvidesSetupDreamNotificationIdFactory.InstanceHolder.INSTANCE;
        this.complicationProvider = provider;
        this.dreamOverlayStateControllerProvider = provider2;
        this.dreamSelectedProvider = provider3;
        this.notificationManagerProvider = provider4;
        this.notificationLazyProvider = provider5;
        this.contentResolverProvider = provider6;
        this.settingUriProvider = provider7;
        this.notificationIdProvider = setupDreamModule_ProvidesSetupDreamNotificationIdFactory;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NudgeToSetupDreamCallback(this.complicationProvider.mo144get(), this.dreamOverlayStateControllerProvider.mo144get(), this.dreamSelectedProvider, this.notificationManagerProvider.mo144get(), DoubleCheck.lazy(this.notificationLazyProvider), this.contentResolverProvider.mo144get(), this.settingUriProvider.mo144get(), this.notificationIdProvider.mo144get().intValue());
    }
}
