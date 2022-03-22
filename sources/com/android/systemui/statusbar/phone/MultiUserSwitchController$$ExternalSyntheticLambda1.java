package com.android.systemui.statusbar.phone;

import java.util.Objects;
import java.util.function.Supplier;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class MultiUserSwitchController$$ExternalSyntheticLambda1 implements Supplier {
    public final /* synthetic */ MultiUserSwitchController f$0;

    public /* synthetic */ MultiUserSwitchController$$ExternalSyntheticLambda1(MultiUserSwitchController multiUserSwitchController) {
        this.f$0 = multiUserSwitchController;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        MultiUserSwitchController multiUserSwitchController = this.f$0;
        Objects.requireNonNull(multiUserSwitchController);
        return Boolean.valueOf(multiUserSwitchController.mUserManager.isUserSwitcherEnabled(multiUserSwitchController.getResources().getBoolean(2131034220)));
    }
}
