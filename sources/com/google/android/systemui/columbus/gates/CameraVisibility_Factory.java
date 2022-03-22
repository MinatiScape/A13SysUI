package com.google.android.systemui.columbus.gates;

import android.app.IActivityManager;
import android.content.Context;
import android.os.Handler;
import com.google.android.systemui.columbus.actions.Action;
import dagger.internal.Factory;
import java.util.List;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CameraVisibility_Factory implements Factory<CameraVisibility> {
    public final Provider<IActivityManager> activityManagerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<List<Action>> exceptionsProvider;
    public final Provider<KeyguardVisibility> keyguardGateProvider;
    public final Provider<PowerState> powerStateProvider;
    public final Provider<Handler> updateHandlerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new CameraVisibility(this.contextProvider.mo144get(), this.exceptionsProvider.mo144get(), this.keyguardGateProvider.mo144get(), this.powerStateProvider.mo144get(), this.activityManagerProvider.mo144get(), this.updateHandlerProvider.mo144get());
    }

    public CameraVisibility_Factory(Provider<Context> provider, Provider<List<Action>> provider2, Provider<KeyguardVisibility> provider3, Provider<PowerState> provider4, Provider<IActivityManager> provider5, Provider<Handler> provider6) {
        this.contextProvider = provider;
        this.exceptionsProvider = provider2;
        this.keyguardGateProvider = provider3;
        this.powerStateProvider = provider4;
        this.activityManagerProvider = provider5;
        this.updateHandlerProvider = provider6;
    }
}
