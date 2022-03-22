package com.android.systemui.dagger;

import com.android.keyguard.LiftToActivateListener_Factory;
import dagger.internal.DoubleCheck;
import javax.inject.Provider;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline1 {
    public static Provider m(Provider provider, int i) {
        return DoubleCheck.provider(new LiftToActivateListener_Factory(provider, i));
    }
}
