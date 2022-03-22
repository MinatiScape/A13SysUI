package com.android.systemui.statusbar.phone;

import android.os.Handler;
import android.view.IWindowManager;
import com.android.systemui.statusbar.phone.AutoHideController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AutoHideController_Factory_Factory implements Factory<AutoHideController.Factory> {
    public final Provider<Handler> handlerProvider;
    public final Provider<IWindowManager> iWindowManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new AutoHideController.Factory(this.handlerProvider.mo144get(), this.iWindowManagerProvider.mo144get());
    }

    public AutoHideController_Factory_Factory(Provider<Handler> provider, Provider<IWindowManager> provider2) {
        this.handlerProvider = provider;
        this.iWindowManagerProvider = provider2;
    }
}
