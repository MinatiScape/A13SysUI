package com.google.android.systemui.communal.dock.dagger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import com.android.systemui.util.condition.Monitor;
import com.google.android.systemui.communal.dock.callbacks.NudgeToSetupDreamCallback;
import com.google.android.systemui.communal.dock.callbacks.ServiceBinderCallback;
import com.google.android.systemui.communal.dock.callbacks.TimeoutToUserZeroCallback;
import com.google.android.systemui.communal.dock.callbacks.mediashell.MediaShellCallback;
import com.google.android.systemui.titan.DaggerTitanGlobalRootComponent;
import dagger.internal.Factory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DockModule_ProvideDockingCallbacksFactory implements Factory<Set<Monitor.Callback>> {
    public final Provider<ServiceBinderCallbackComponent$Factory> factoryProvider;
    public final Provider<MediaShellCallback> mediaShellCallbackProvider;
    public final Provider<NudgeToSetupDreamCallback> nudgeToSetupDreamCallbackProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<TimeoutToUserZeroCallback> timeoutToUserZeroCallbackProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        String[] stringArray;
        ServiceBinderCallbackComponent$Factory serviceBinderCallbackComponent$Factory = this.factoryProvider.mo144get();
        NudgeToSetupDreamCallback nudgeToSetupDreamCallback = this.nudgeToSetupDreamCallbackProvider.mo144get();
        MediaShellCallback mediaShellCallback = this.mediaShellCallbackProvider.mo144get();
        TimeoutToUserZeroCallback timeoutToUserZeroCallback = this.timeoutToUserZeroCallbackProvider.mo144get();
        HashSet hashSet = new HashSet();
        for (String str : this.resourcesProvider.mo144get().getStringArray(2130903090)) {
            Intent intent = new Intent("android.intent.action.DOCK_EVENT");
            intent.setComponent(ComponentName.unflattenFromString(str));
            DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.ServiceBinderCallbackComponentImpl create = serviceBinderCallbackComponent$Factory.create(intent);
            Objects.requireNonNull(create);
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
            hashSet.add(new ServiceBinderCallback(daggerTitanGlobalRootComponent.context, daggerTitanGlobalRootComponent.provideMainExecutorProvider.mo144get(), create.intent));
        }
        hashSet.addAll(Arrays.asList(nudgeToSetupDreamCallback, mediaShellCallback, timeoutToUserZeroCallback));
        return hashSet;
    }

    public DockModule_ProvideDockingCallbacksFactory(Provider<Resources> provider, Provider<ServiceBinderCallbackComponent$Factory> provider2, Provider<NudgeToSetupDreamCallback> provider3, Provider<MediaShellCallback> provider4, Provider<TimeoutToUserZeroCallback> provider5) {
        this.resourcesProvider = provider;
        this.factoryProvider = provider2;
        this.nudgeToSetupDreamCallbackProvider = provider3;
        this.mediaShellCallbackProvider = provider4;
        this.timeoutToUserZeroCallbackProvider = provider5;
    }
}
