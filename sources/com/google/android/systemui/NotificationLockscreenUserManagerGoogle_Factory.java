package com.google.android.systemui;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Handler;
import android.os.UserManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.google.android.systemui.smartspace.SmartSpaceController;
import dagger.internal.DelegateFactory;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationLockscreenUserManagerGoogle_Factory implements Factory<NotificationLockscreenUserManagerGoogle> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<NotificationClickNotifier> clickNotifierProvider;
    public final Provider<CommonNotifCollection> commonNotifCollectionLazyProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    public final Provider<KeyguardManager> keyguardManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<SmartSpaceController> smartSpaceControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<NotificationVisibilityProvider> visibilityProviderLazyProvider;

    public static NotificationLockscreenUserManagerGoogle_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, DelegateFactory delegateFactory, Provider provider14) {
        return new NotificationLockscreenUserManagerGoogle_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, delegateFactory, provider14);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationLockscreenUserManagerGoogle(this.contextProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), this.devicePolicyManagerProvider.mo144get(), this.userManagerProvider.mo144get(), DoubleCheck.lazy(this.visibilityProviderLazyProvider), DoubleCheck.lazy(this.commonNotifCollectionLazyProvider), this.clickNotifierProvider.mo144get(), this.keyguardManagerProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.deviceProvisionedControllerProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), DoubleCheck.lazy(this.keyguardBypassControllerProvider), this.smartSpaceControllerProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public NotificationLockscreenUserManagerGoogle_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, DelegateFactory delegateFactory, Provider provider14) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.devicePolicyManagerProvider = provider3;
        this.userManagerProvider = provider4;
        this.visibilityProviderLazyProvider = provider5;
        this.commonNotifCollectionLazyProvider = provider6;
        this.clickNotifierProvider = provider7;
        this.keyguardManagerProvider = provider8;
        this.statusBarStateControllerProvider = provider9;
        this.mainHandlerProvider = provider10;
        this.deviceProvisionedControllerProvider = provider11;
        this.keyguardStateControllerProvider = provider12;
        this.keyguardBypassControllerProvider = provider13;
        this.smartSpaceControllerProvider = delegateFactory;
        this.dumpManagerProvider = provider14;
    }
}
