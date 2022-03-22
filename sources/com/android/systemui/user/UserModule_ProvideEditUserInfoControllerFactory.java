package com.android.systemui.user;

import com.android.settingslib.users.EditUserInfoController;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class UserModule_ProvideEditUserInfoControllerFactory implements Factory<EditUserInfoController> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final UserModule_ProvideEditUserInfoControllerFactory INSTANCE = new UserModule_ProvideEditUserInfoControllerFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new EditUserInfoController();
    }
}
