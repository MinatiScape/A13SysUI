package com.android.wm.shell.dagger;

import com.android.wm.shell.pip.PipSnapAlgorithm;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class WMShellModule_ProvidePipSnapAlgorithmFactory implements Factory<PipSnapAlgorithm> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final WMShellModule_ProvidePipSnapAlgorithmFactory INSTANCE = new WMShellModule_ProvidePipSnapAlgorithmFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new PipSnapAlgorithm();
    }
}
