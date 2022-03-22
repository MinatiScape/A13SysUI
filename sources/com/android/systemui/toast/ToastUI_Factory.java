package com.android.systemui.toast;

import android.app.INotificationManager;
import android.content.Context;
import android.os.ServiceManager;
import android.view.accessibility.IAccessibilityManager;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ToastUI_Factory implements Factory<ToastUI> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<ToastFactory> toastFactoryProvider;
    public final Provider<ToastLogger> toastLoggerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ToastUI(this.contextProvider.mo144get(), this.commandQueueProvider.mo144get(), INotificationManager.Stub.asInterface(ServiceManager.getService("notification")), IAccessibilityManager.Stub.asInterface(ServiceManager.getService("accessibility")), this.toastFactoryProvider.mo144get(), this.toastLoggerProvider.mo144get());
    }

    public ToastUI_Factory(Provider provider, Provider provider2, Provider provider3, ToastLogger_Factory toastLogger_Factory) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.toastFactoryProvider = provider3;
        this.toastLoggerProvider = toastLogger_Factory;
    }
}
