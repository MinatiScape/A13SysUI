package com.android.systemui.dagger;

import android.app.WallpaperManager;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.qs.QSContainerImpl;
import com.android.systemui.statusbar.notification.collection.coordinator.DebugModeCoordinator;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda29;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideHandlerFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object module;

    public /* synthetic */ DependencyProvider_ProvideHandlerFactory(Object obj, int i) {
        this.$r8$classId = i;
        this.module = obj;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                Objects.requireNonNull((DependencyProvider) this.module);
                return new Handler();
            case 1:
                WallpaperManager wallpaperManager = (WallpaperManager) ((Context) ((Provider) this.module).mo144get()).getSystemService(WallpaperManager.class);
                Objects.requireNonNull(wallpaperManager, "Cannot return null from a non-@Nullable @Provides method");
                return wallpaperManager;
            case 2:
                return ((LogBufferFactory) ((Provider) this.module).mo144get()).create("SwipeStatusBarAwayLog", 30);
            case 3:
                QSContainerImpl qSContainerImpl = (QSContainerImpl) ((View) ((Provider) this.module).mo144get()).findViewById(2131428661);
                Objects.requireNonNull(qSContainerImpl, "Cannot return null from a non-@Nullable @Provides method");
                return qSContainerImpl;
            case 4:
                return new DebugModeCoordinator((DebugModeFilterProvider) ((Provider) this.module).mo144get());
            case 5:
                return new PipAnimationController((PipSurfaceTransactionHelper) ((Provider) this.module).mo144get());
            default:
                Optional map = ((Optional) ((Provider) this.module).mo144get()).map(BubbleStackView$$ExternalSyntheticLambda29.INSTANCE$1);
                Objects.requireNonNull(map, "Cannot return null from a non-@Nullable @Provides method");
                return map;
        }
    }
}
