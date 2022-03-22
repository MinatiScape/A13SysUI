package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.media.AudioManager;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ManageMedia_Factory implements Factory<ManageMedia> {
    public final Provider<AudioManager> audioManagerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ManageMedia(this.contextProvider.mo144get(), this.audioManagerProvider.mo144get(), this.uiEventLoggerProvider.mo144get());
    }

    public ManageMedia_Factory(Provider<Context> provider, Provider<AudioManager> provider2, Provider<UiEventLogger> provider3) {
        this.contextProvider = provider;
        this.audioManagerProvider = provider2;
        this.uiEventLoggerProvider = provider3;
    }
}
