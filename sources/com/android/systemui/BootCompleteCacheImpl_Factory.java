package com.android.systemui;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.classifier.PointerCountClassifier;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.statusbar.notification.collection.coordinator.MediaCoordinator;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.wm.shell.common.HandlerExecutor;
import com.android.wm.shell.dagger.WMShellBaseModule$$ExternalSyntheticLambda4;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BootCompleteCacheImpl_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider dumpManagerProvider;

    public /* synthetic */ BootCompleteCacheImpl_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.dumpManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new BootCompleteCacheImpl((DumpManager) this.dumpManagerProvider.mo144get());
            case 1:
                return new PointerCountClassifier((FalsingDataProvider) this.dumpManagerProvider.mo144get());
            case 2:
                return new MediaCoordinator((MediaFeatureFlag) this.dumpManagerProvider.mo144get());
            case 3:
                NotificationShadeWindowView notificationShadeWindowView = (NotificationShadeWindowView) ((LayoutInflater) this.dumpManagerProvider.mo144get()).inflate(2131624600, (ViewGroup) null);
                if (notificationShadeWindowView != null) {
                    return notificationShadeWindowView;
                }
                throw new IllegalStateException("R.layout.super_notification_shade could not be properly inflated");
            case 4:
                Optional map = ((Optional) this.dumpManagerProvider.mo144get()).map(WMShellBaseModule$$ExternalSyntheticLambda4.INSTANCE);
                Objects.requireNonNull(map, "Cannot return null from a non-@Nullable @Provides method");
                return map;
            default:
                return new HandlerExecutor((Handler) this.dumpManagerProvider.mo144get());
        }
    }
}
