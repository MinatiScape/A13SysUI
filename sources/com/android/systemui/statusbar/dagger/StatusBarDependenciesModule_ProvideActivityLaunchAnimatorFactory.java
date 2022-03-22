package com.android.systemui.statusbar.dagger;

import com.android.systemui.animation.ActivityLaunchAnimator;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class StatusBarDependenciesModule_ProvideActivityLaunchAnimatorFactory implements Factory<ActivityLaunchAnimator> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final StatusBarDependenciesModule_ProvideActivityLaunchAnimatorFactory INSTANCE = new StatusBarDependenciesModule_ProvideActivityLaunchAnimatorFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ActivityLaunchAnimator();
    }
}
