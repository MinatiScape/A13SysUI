package com.android.systemui.dagger;

import android.view.View;
import android.view.ViewStub;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.statusbar.notification.ForegroundServiceDismissalFeatureController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvidesViewMediatorCallbackFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object module;
    public final Provider viewMediatorProvider;

    public /* synthetic */ DependencyProvider_ProvidesViewMediatorCallbackFactory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.viewMediatorProvider = provider;
        this.module = provider2;
    }

    public DependencyProvider_ProvidesViewMediatorCallbackFactory(DependencyProvider dependencyProvider, Provider provider) {
        this.$r8$classId = 0;
        this.module = dependencyProvider;
        this.viewMediatorProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        int i;
        switch (this.$r8$classId) {
            case 0:
                KeyguardViewMediator keyguardViewMediator = (KeyguardViewMediator) this.viewMediatorProvider.mo144get();
                Objects.requireNonNull((DependencyProvider) this.module);
                Objects.requireNonNull(keyguardViewMediator);
                KeyguardViewMediator.AnonymousClass4 r3 = keyguardViewMediator.mViewMediatorCallback;
                Objects.requireNonNull(r3, "Cannot return null from a non-@Nullable @Provides method");
                return r3;
            case 1:
                return new ForegroundServiceSectionController((NotificationEntryManager) this.viewMediatorProvider.mo144get(), (ForegroundServiceDismissalFeatureController) ((Provider) this.module).mo144get());
            default:
                ViewStub viewStub = (ViewStub) ((NotificationShadeWindowView) this.viewMediatorProvider.mo144get()).findViewById(2131428653);
                if (((FeatureFlags) ((Provider) this.module).mo144get()).isEnabled(Flags.COMBINED_QS_HEADERS)) {
                    i = 2131624036;
                } else {
                    i = 2131624518;
                }
                viewStub.setLayoutResource(i);
                View inflate = viewStub.inflate();
                Objects.requireNonNull(inflate, "Cannot return null from a non-@Nullable @Provides method");
                return inflate;
        }
    }
}
