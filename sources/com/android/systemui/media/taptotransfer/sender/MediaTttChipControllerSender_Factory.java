package com.android.systemui.media.taptotransfer.sender;

import android.content.Context;
import android.view.WindowManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaTttChipControllerSender_Factory implements Factory<MediaTttChipControllerSender> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DelayableExecutor> mainExecutorProvider;
    public final Provider<WindowManager> windowManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new MediaTttChipControllerSender(this.commandQueueProvider.mo144get(), this.contextProvider.mo144get(), this.windowManagerProvider.mo144get(), this.mainExecutorProvider.mo144get());
    }

    public MediaTttChipControllerSender_Factory(Provider<CommandQueue> provider, Provider<Context> provider2, Provider<WindowManager> provider3, Provider<DelayableExecutor> provider4) {
        this.commandQueueProvider = provider;
        this.contextProvider = provider2;
        this.windowManagerProvider = provider3;
        this.mainExecutorProvider = provider4;
    }
}
