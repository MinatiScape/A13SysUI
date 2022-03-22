package com.google.android.systemui.communal.dreams.dagger;

import android.content.Intent;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SetupDreamModule_ProvidesDreamSettingIntentFactory implements Factory<Intent> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final SetupDreamModule_ProvidesDreamSettingIntentFactory INSTANCE = new SetupDreamModule_ProvidesDreamSettingIntentFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new Intent("android.settings.DREAM_SETTINGS");
    }
}
