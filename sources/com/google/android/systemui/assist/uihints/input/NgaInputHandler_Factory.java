package com.google.android.systemui.assist.uihints.input;

import com.google.android.systemui.assist.uihints.TouchInsideHandler;
import dagger.internal.Factory;
import dagger.internal.SetFactory;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NgaInputHandler_Factory implements Factory<NgaInputHandler> {
    public final Provider<Set<TouchInsideRegion>> dismissablesProvider;
    public final Provider<TouchInsideHandler> touchInsideHandlerProvider;
    public final Provider<Set<TouchActionRegion>> touchablesProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NgaInputHandler(this.touchInsideHandlerProvider.mo144get(), this.touchablesProvider.mo144get(), this.dismissablesProvider.mo144get());
    }

    public NgaInputHandler_Factory(Provider provider, Provider provider2, SetFactory setFactory) {
        this.touchInsideHandlerProvider = provider;
        this.touchablesProvider = provider2;
        this.dismissablesProvider = setFactory;
    }
}
