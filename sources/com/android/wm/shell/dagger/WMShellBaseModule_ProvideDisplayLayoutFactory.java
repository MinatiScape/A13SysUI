package com.android.wm.shell.dagger;

import com.android.wm.shell.common.DisplayLayout;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideDisplayLayoutFactory implements Factory<DisplayLayout> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final WMShellBaseModule_ProvideDisplayLayoutFactory INSTANCE = new WMShellBaseModule_ProvideDisplayLayoutFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DisplayLayout();
    }
}
