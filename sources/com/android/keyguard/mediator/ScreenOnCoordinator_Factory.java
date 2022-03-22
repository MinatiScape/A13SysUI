package com.android.keyguard.mediator;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.Execution;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScreenOnCoordinator_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider executionProvider;
    public final Provider screenLifecycleProvider;
    public final Provider unfoldComponentProvider;

    public /* synthetic */ ScreenOnCoordinator_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.screenLifecycleProvider = provider;
        this.unfoldComponentProvider = provider2;
        this.executionProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ScreenOnCoordinator((ScreenLifecycle) this.screenLifecycleProvider.mo144get(), (Optional) this.unfoldComponentProvider.mo144get(), (Execution) this.executionProvider.mo144get());
            case 1:
                return new MediaTttCommandLineHelper((CommandRegistry) this.screenLifecycleProvider.mo144get(), (Context) this.unfoldComponentProvider.mo144get(), (Executor) this.executionProvider.mo144get());
            default:
                Context context = (Context) this.unfoldComponentProvider.mo144get();
                return new AssistantFeedbackController((Handler) this.screenLifecycleProvider.mo144get(), (DeviceConfigProxy) this.executionProvider.mo144get());
        }
    }
}
