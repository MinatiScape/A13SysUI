package com.android.systemui.dagger;

import android.view.Choreographer;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvidesChoreographerFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object module;

    public /* synthetic */ DependencyProvider_ProvidesChoreographerFactory(Object obj, int i) {
        this.$r8$classId = i;
        this.module = obj;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                Objects.requireNonNull((DependencyProvider) this.module);
                Choreographer instance = Choreographer.getInstance();
                Objects.requireNonNull(instance, "Cannot return null from a non-@Nullable @Provides method");
                return instance;
            case 1:
                return new LifecycleRegistry((LifecycleOwner) ((Provider) this.module).mo144get(), true);
            default:
                NotificationsQuickSettingsContainer notificationsQuickSettingsContainer = (NotificationsQuickSettingsContainer) ((NotificationShadeWindowView) ((Provider) this.module).mo144get()).findViewById(2131428509);
                Objects.requireNonNull(notificationsQuickSettingsContainer, "Cannot return null from a non-@Nullable @Provides method");
                return notificationsQuickSettingsContainer;
        }
    }
}
