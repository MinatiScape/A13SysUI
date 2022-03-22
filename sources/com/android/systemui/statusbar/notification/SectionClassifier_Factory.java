package com.android.systemui.statusbar.notification;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SectionClassifier_Factory implements Factory<SectionClassifier> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final SectionClassifier_Factory INSTANCE = new SectionClassifier_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SectionClassifier();
    }
}
