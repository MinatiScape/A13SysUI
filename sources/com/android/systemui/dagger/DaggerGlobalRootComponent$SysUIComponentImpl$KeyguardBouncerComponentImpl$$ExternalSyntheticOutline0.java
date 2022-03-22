package com.android.systemui.dagger;

import com.android.systemui.qs.dagger.QSFragmentModule_ProvideQSPanelFactory;
import dagger.internal.DoubleCheck;
import javax.inject.Provider;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DaggerGlobalRootComponent$SysUIComponentImpl$KeyguardBouncerComponentImpl$$ExternalSyntheticOutline0 {
    public static Provider m(Provider provider, int i) {
        return DoubleCheck.provider(new QSFragmentModule_ProvideQSPanelFactory(provider, i));
    }
}
