package com.android.systemui.classifier;

import android.content.Context;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.doze.AlwaysOnDisplayPolicy;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationInteractionTracker;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DiagonalClassifier_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider dataProvider;
    public final Object deviceConfigProxyProvider;

    public /* synthetic */ DiagonalClassifier_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.dataProvider = provider;
        this.deviceConfigProxyProvider = provider2;
    }

    public DiagonalClassifier_Factory(DependencyProvider dependencyProvider, Provider provider) {
        this.$r8$classId = 2;
        this.deviceConfigProxyProvider = dependencyProvider;
        this.dataProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new DiagonalClassifier((FalsingDataProvider) this.dataProvider.mo144get(), (DeviceConfigProxy) ((Provider) this.deviceConfigProxyProvider).mo144get());
            case 1:
                return new NotificationInteractionTracker((NotificationClickNotifier) this.dataProvider.mo144get(), (NotificationEntryManager) ((Provider) this.deviceConfigProxyProvider).mo144get());
            default:
                Objects.requireNonNull((DependencyProvider) this.deviceConfigProxyProvider);
                return new AlwaysOnDisplayPolicy((Context) this.dataProvider.mo144get());
        }
    }
}
