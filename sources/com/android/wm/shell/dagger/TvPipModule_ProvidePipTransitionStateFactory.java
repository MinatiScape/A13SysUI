package com.android.wm.shell.dagger;

import com.android.wm.shell.pip.PipTransitionState;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class TvPipModule_ProvidePipTransitionStateFactory implements Factory<PipTransitionState> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final TvPipModule_ProvidePipTransitionStateFactory INSTANCE = new TvPipModule_ProvidePipTransitionStateFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new PipTransitionState();
    }
}
