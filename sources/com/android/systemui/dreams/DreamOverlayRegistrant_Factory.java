package com.android.systemui.dreams;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.ViewGroup;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.wm.shell.back.BackAnimationController;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DreamOverlayRegistrant_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider resourcesProvider;

    public /* synthetic */ DreamOverlayRegistrant_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.resourcesProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new DreamOverlayRegistrant((Context) this.contextProvider.mo144get(), (Resources) this.resourcesProvider.mo144get());
            case 1:
                NotificationShelf notificationShelf = (NotificationShelf) ((LayoutInflater) this.contextProvider.mo144get()).inflate(2131624527, (ViewGroup) ((NotificationStackScrollLayout) this.resourcesProvider.mo144get()), false);
                if (notificationShelf != null) {
                    return notificationShelf;
                }
                throw new IllegalStateException("R.layout.status_bar_notification_shelf could not be properly inflated");
            default:
                Optional of = Optional.of(new BackAnimationController((ShellExecutor) this.resourcesProvider.mo144get(), new SurfaceControl.Transaction(), ActivityTaskManager.getService(), (Context) this.contextProvider.mo144get()));
                Objects.requireNonNull(of, "Cannot return null from a non-@Nullable @Provides method");
                return of;
        }
    }
}
