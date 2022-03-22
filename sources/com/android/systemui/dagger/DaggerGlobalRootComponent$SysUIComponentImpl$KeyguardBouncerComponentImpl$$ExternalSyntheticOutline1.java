package com.android.systemui.dagger;

import com.android.wm.shell.dagger.WMShellBaseModule_ProvideSplitScreenFactory;
import dagger.internal.DoubleCheck;
import javax.inject.Provider;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DaggerGlobalRootComponent$SysUIComponentImpl$KeyguardBouncerComponentImpl$$ExternalSyntheticOutline1 {
    public static Provider m(Provider provider, int i) {
        return DoubleCheck.provider(new WMShellBaseModule_ProvideSplitScreenFactory(provider, i));
    }
}
