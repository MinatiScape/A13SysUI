package com.android.systemui.qs;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.SecurityController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSSecurityFooter_Factory implements Factory<QSSecurityFooter> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Looper> bgLooperProvider;
    public final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<View> rootViewProvider;
    public final Provider<SecurityController> securityControllerProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public static QSSecurityFooter_Factory create(Provider<View> provider, Provider<UserTracker> provider2, Provider<Handler> provider3, Provider<ActivityStarter> provider4, Provider<SecurityController> provider5, Provider<DialogLaunchAnimator> provider6, Provider<Looper> provider7) {
        return new QSSecurityFooter_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new QSSecurityFooter(this.rootViewProvider.mo144get(), this.userTrackerProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.activityStarterProvider.mo144get(), this.securityControllerProvider.mo144get(), this.dialogLaunchAnimatorProvider.mo144get(), this.bgLooperProvider.mo144get());
    }

    public QSSecurityFooter_Factory(Provider<View> provider, Provider<UserTracker> provider2, Provider<Handler> provider3, Provider<ActivityStarter> provider4, Provider<SecurityController> provider5, Provider<DialogLaunchAnimator> provider6, Provider<Looper> provider7) {
        this.rootViewProvider = provider;
        this.userTrackerProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.activityStarterProvider = provider4;
        this.securityControllerProvider = provider5;
        this.dialogLaunchAnimatorProvider = provider6;
        this.bgLooperProvider = provider7;
    }
}
