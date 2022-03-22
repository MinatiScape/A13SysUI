package com.android.systemui.statusbar.notification.row;

import android.app.INotificationManager;
import android.content.Context;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialog;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialog_Builder_Factory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ChannelEditorDialogController_Factory implements Factory<ChannelEditorDialogController> {
    public final Provider<Context> cProvider;
    public final Provider<ChannelEditorDialog.Builder> dialogBuilderProvider;
    public final Provider<INotificationManager> noManProvider;

    public ChannelEditorDialogController_Factory(Provider provider, Provider provider2) {
        ChannelEditorDialog_Builder_Factory channelEditorDialog_Builder_Factory = ChannelEditorDialog_Builder_Factory.InstanceHolder.INSTANCE;
        this.cProvider = provider;
        this.noManProvider = provider2;
        this.dialogBuilderProvider = channelEditorDialog_Builder_Factory;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ChannelEditorDialogController(this.cProvider.mo144get(), this.noManProvider.mo144get(), this.dialogBuilderProvider.mo144get());
    }
}
