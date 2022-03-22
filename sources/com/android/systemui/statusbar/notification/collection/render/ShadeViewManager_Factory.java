package com.android.systemui.statusbar.notification.collection.render;

import android.content.Context;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ShadeViewManager_Factory {
    public final Provider<Context> contextProvider;
    public final Provider<NotificationSectionsFeatureManager> featureManagerProvider;
    public final Provider<ShadeViewDifferLogger> loggerProvider;
    public final Provider<MediaContainerController> mediaContainerControllerProvider;
    public final Provider<SectionHeaderVisibilityProvider> sectionHeaderVisibilityProvider;
    public final Provider<NotifViewBarn> viewBarnProvider;

    public static ShadeViewManager_Factory create(Provider<Context> provider, Provider<MediaContainerController> provider2, Provider<NotificationSectionsFeatureManager> provider3, Provider<SectionHeaderVisibilityProvider> provider4, Provider<ShadeViewDifferLogger> provider5, Provider<NotifViewBarn> provider6) {
        return new ShadeViewManager_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public ShadeViewManager_Factory(Provider<Context> provider, Provider<MediaContainerController> provider2, Provider<NotificationSectionsFeatureManager> provider3, Provider<SectionHeaderVisibilityProvider> provider4, Provider<ShadeViewDifferLogger> provider5, Provider<NotifViewBarn> provider6) {
        this.contextProvider = provider;
        this.mediaContainerControllerProvider = provider2;
        this.featureManagerProvider = provider3;
        this.sectionHeaderVisibilityProvider = provider4;
        this.loggerProvider = provider5;
        this.viewBarnProvider = provider6;
    }
}
