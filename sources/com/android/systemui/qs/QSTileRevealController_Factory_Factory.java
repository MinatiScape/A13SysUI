package com.android.systemui.qs;

import android.content.Context;
import com.android.systemui.qs.QSTileRevealController;
import com.android.systemui.qs.customize.QSCustomizerController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSTileRevealController_Factory_Factory implements Factory<QSTileRevealController.Factory> {
    public final Provider<Context> contextProvider;
    public final Provider<QSCustomizerController> qsCustomizerControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new QSTileRevealController.Factory(this.contextProvider.mo144get(), this.qsCustomizerControllerProvider.mo144get());
    }

    public QSTileRevealController_Factory_Factory(Provider<Context> provider, Provider<QSCustomizerController> provider2) {
        this.contextProvider = provider;
        this.qsCustomizerControllerProvider = provider2;
    }
}
