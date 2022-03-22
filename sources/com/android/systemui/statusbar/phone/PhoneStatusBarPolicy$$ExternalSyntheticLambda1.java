package com.android.systemui.statusbar.phone;

import java.util.Objects;
import java.util.concurrent.Callable;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PhoneStatusBarPolicy$$ExternalSyntheticLambda1 implements Callable {
    public final /* synthetic */ PhoneStatusBarPolicy f$0;

    public /* synthetic */ PhoneStatusBarPolicy$$ExternalSyntheticLambda1(PhoneStatusBarPolicy phoneStatusBarPolicy) {
        this.f$0 = phoneStatusBarPolicy;
    }

    @Override // java.util.concurrent.Callable
    public final Object call() {
        PhoneStatusBarPolicy phoneStatusBarPolicy = this.f$0;
        Objects.requireNonNull(phoneStatusBarPolicy);
        return phoneStatusBarPolicy.mResources.getString(2131951755);
    }
}
