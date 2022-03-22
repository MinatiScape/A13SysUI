package com.android.systemui.dagger;

import android.content.Context;
import android.os.Vibrator;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustmentProvider;
import com.android.systemui.statusbar.tv.notifications.TvNotificationHandler;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanelActivity;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideOptionalVibratorFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;

    public /* synthetic */ FrameworkServicesModule_ProvideOptionalVibratorFactory(Provider provider, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                Optional ofNullable = Optional.ofNullable((Vibrator) ((Context) this.contextProvider.mo144get()).getSystemService(Vibrator.class));
                Objects.requireNonNull(ofNullable, "Cannot return null from a non-@Nullable @Provides method");
                return ofNullable;
            case 1:
                return new NotifUiAdjustmentProvider((SectionClassifier) this.contextProvider.mo144get());
            case 2:
                return new TvNotificationPanelActivity((TvNotificationHandler) this.contextProvider.mo144get());
            default:
                Executor mainExecutor = ((Context) this.contextProvider.mo144get()).getMainExecutor();
                Objects.requireNonNull(mainExecutor, "Cannot return null from a non-@Nullable @Provides method");
                return mainExecutor;
        }
    }
}
