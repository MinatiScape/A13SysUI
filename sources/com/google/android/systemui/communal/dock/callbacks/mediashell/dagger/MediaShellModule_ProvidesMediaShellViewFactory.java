package com.google.android.systemui.communal.dock.callbacks.mediashell.dagger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaShellModule_ProvidesMediaShellViewFactory implements Factory<View> {
    public final Provider<Context> contextProvider;
    public final Provider<String> nameProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        View inflate = LayoutInflater.from(this.contextProvider.mo144get()).inflate(2131624264, (ViewGroup) null);
        ((TextView) inflate.findViewById(2131428359)).setText(this.nameProvider.mo144get());
        return inflate;
    }

    public MediaShellModule_ProvidesMediaShellViewFactory(Provider<Context> provider, Provider<String> provider2) {
        this.contextProvider = provider;
        this.nameProvider = provider2;
    }
}
