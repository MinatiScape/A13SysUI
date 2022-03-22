package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory implements Factory<SectionHeaderControllerSubcomponent> {
    public final Provider<SectionHeaderControllerSubcomponent.Builder> builderProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        SectionHeaderControllerSubcomponent build = this.builderProvider.mo144get().mo142nodeLabel("silent header").headerText(2131952921).mo141clickIntentAction("android.settings.NOTIFICATION_SETTINGS").build();
        Objects.requireNonNull(build, "Cannot return null from a non-@Nullable @Provides method");
        return build;
    }

    public NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        this.builderProvider = provider;
    }
}
