package com.android.systemui.statusbar.phone;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class KeyguardDismissUtil_Factory implements Factory<KeyguardDismissUtil> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final KeyguardDismissUtil_Factory INSTANCE = new KeyguardDismissUtil_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new KeyguardDismissUtil();
    }
}
