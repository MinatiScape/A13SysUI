package com.google.android.systemui.dagger;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SystemUIGoogleModule_ProvideLeakReportEmailFactory implements Factory<String> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final SystemUIGoogleModule_ProvideLeakReportEmailFactory INSTANCE = new SystemUIGoogleModule_ProvideLeakReportEmailFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final /* bridge */ /* synthetic */ Object mo144get() {
        return "buganizer-system+187317@google.com";
    }
}
