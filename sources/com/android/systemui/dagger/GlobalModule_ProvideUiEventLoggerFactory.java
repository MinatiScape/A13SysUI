package com.android.systemui.dagger;

import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class GlobalModule_ProvideUiEventLoggerFactory implements Factory<UiEventLogger> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final GlobalModule_ProvideUiEventLoggerFactory INSTANCE = new GlobalModule_ProvideUiEventLoggerFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new UiEventLoggerImpl();
    }
}
