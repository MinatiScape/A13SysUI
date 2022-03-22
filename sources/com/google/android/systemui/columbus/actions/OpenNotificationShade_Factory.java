package com.google.android.systemui.columbus.actions;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.List;
import java.util.Objects;
import javax.inject.Provider;
import kotlin.collections.SetsKt__SetsKt;
/* loaded from: classes.dex */
public final class OpenNotificationShade_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider notificationShadeWindowControllerProvider;
    public final Provider statusBarProvider;
    public final Provider uiEventLoggerProvider;

    public /* synthetic */ OpenNotificationShade_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.notificationShadeWindowControllerProvider = provider2;
        this.statusBarProvider = provider3;
        this.uiEventLoggerProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new OpenNotificationShade((Context) this.contextProvider.mo144get(), DoubleCheck.lazy(this.notificationShadeWindowControllerProvider), DoubleCheck.lazy(this.statusBarProvider), (UiEventLogger) this.uiEventLoggerProvider.mo144get());
            default:
                List listOf = SetsKt__SetsKt.listOf((DismissTimer) this.contextProvider.mo144get(), (SnoozeAlarm) this.notificationShadeWindowControllerProvider.mo144get(), (SilenceCall) this.statusBarProvider.mo144get(), (SettingsAction) this.uiEventLoggerProvider.mo144get());
                Objects.requireNonNull(listOf, "Cannot return null from a non-@Nullable @Provides method");
                return listOf;
        }
    }
}
