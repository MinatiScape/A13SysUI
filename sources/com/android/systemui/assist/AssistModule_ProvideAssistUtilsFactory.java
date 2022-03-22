package com.android.systemui.assist;

import android.content.Context;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.app.AssistUtils;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.people.NotificationPersonExtractorPluginBoundary;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.policy.ExtensionController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AssistModule_ProvideAssistUtilsFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;

    public /* synthetic */ AssistModule_ProvideAssistUtilsFactory(Provider provider, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new AssistUtils((Context) this.contextProvider.mo144get());
            case 1:
                final Lazy lazy = DoubleCheck.lazy(this.contextProvider);
                return new LifecycleOwner() { // from class: com.android.systemui.dreams.dagger.DreamOverlayModule$$ExternalSyntheticLambda0
                    @Override // androidx.lifecycle.LifecycleOwner
                    public final Lifecycle getLifecycle() {
                        return (Lifecycle) Lazy.this.get();
                    }
                };
            case 2:
                return new NotifLiveDataStoreImpl((Executor) this.contextProvider.mo144get());
            case 3:
                return new NotificationPersonExtractorPluginBoundary((ExtensionController) this.contextProvider.mo144get());
            default:
                NotificationShadeWindowView notificationShadeWindowView = (NotificationShadeWindowView) this.contextProvider.mo144get();
                Objects.requireNonNull(notificationShadeWindowView);
                NotificationPanelView notificationPanelView = (NotificationPanelView) notificationShadeWindowView.findViewById(2131428519);
                Objects.requireNonNull(notificationPanelView, "Cannot return null from a non-@Nullable @Provides method");
                return notificationPanelView;
        }
    }
}
