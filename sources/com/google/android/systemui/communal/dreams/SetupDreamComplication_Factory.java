package com.google.android.systemui.communal.dreams;

import com.google.android.systemui.communal.dreams.dagger.SetupDreamComponent$Factory;
import com.google.android.systemui.titan.DaggerTitanGlobalRootComponent;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SetupDreamComplication_Factory implements Factory<SetupDreamComplication> {
    public final Provider<SetupDreamComponent$Factory> componentFactoryProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SetupDreamComplication(this.componentFactoryProvider.mo144get());
    }

    public SetupDreamComplication_Factory(DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.AnonymousClass13 r1) {
        this.componentFactoryProvider = r1;
    }
}
