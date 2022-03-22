package com.google.android.systemui.communal.dreams.dagger;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SetupDreamModule_ProvidesDreamSettingFactory implements Factory<String> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final SetupDreamModule_ProvidesDreamSettingFactory INSTANCE = new SetupDreamModule_ProvidesDreamSettingFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final /* bridge */ /* synthetic */ Object mo144get() {
        return "screensaver_components";
    }
}
