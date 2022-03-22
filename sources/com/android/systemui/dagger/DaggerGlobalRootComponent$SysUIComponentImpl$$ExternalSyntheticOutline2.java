package com.android.systemui.dagger;

import com.android.systemui.media.RingtonePlayer_Factory;
import dagger.internal.DoubleCheck;
import javax.inject.Provider;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline2 {
    public static Provider m(Provider provider, int i) {
        return DoubleCheck.provider(new RingtonePlayer_Factory(provider, i));
    }
}
