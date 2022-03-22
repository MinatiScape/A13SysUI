package com.android.systemui.screenshot;

import android.content.Context;
import android.view.IWindowManager;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScrollCaptureClient_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider bgExecutorProvider;
    public final Provider contextProvider;
    public final Provider windowManagerServiceProvider;

    public /* synthetic */ ScrollCaptureClient_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.windowManagerServiceProvider = provider;
        this.bgExecutorProvider = provider2;
        this.contextProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        NotificationsController notificationsController;
        switch (this.$r8$classId) {
            case 0:
                return new ScrollCaptureClient((IWindowManager) this.windowManagerServiceProvider.mo144get(), (Executor) this.bgExecutorProvider.mo144get(), (Context) this.contextProvider.mo144get());
            default:
                Lazy lazy = DoubleCheck.lazy(this.bgExecutorProvider);
                Lazy lazy2 = DoubleCheck.lazy(this.contextProvider);
                if (((Context) this.windowManagerServiceProvider.mo144get()).getResources().getBoolean(2131034156)) {
                    notificationsController = (NotificationsController) lazy.get();
                } else {
                    notificationsController = (NotificationsController) lazy2.get();
                }
                Objects.requireNonNull(notificationsController, "Cannot return null from a non-@Nullable @Provides method");
                return notificationsController;
        }
    }
}
