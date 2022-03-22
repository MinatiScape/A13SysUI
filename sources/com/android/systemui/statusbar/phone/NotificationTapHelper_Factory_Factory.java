package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationTapHelper_Factory_Factory implements Factory<NotificationTapHelper.Factory> {
    public final Provider<DelayableExecutor> delayableExecutorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationTapHelper.Factory(this.falsingManagerProvider.mo144get(), this.delayableExecutorProvider.mo144get());
    }

    public NotificationTapHelper_Factory_Factory(Provider<FalsingManager> provider, Provider<DelayableExecutor> provider2) {
        this.falsingManagerProvider = provider;
        this.delayableExecutorProvider = provider2;
    }
}
