package com.android.systemui.doze;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.SystemProperties;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.util.concurrency.ThreadFactory;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutOrganizer;
import com.google.android.systemui.columbus.actions.UnpinNotifications;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeLog_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider dumpManagerProvider;
    public final Provider keyguardUpdateMonitorProvider;
    public final Provider loggerProvider;

    public /* synthetic */ DozeLog_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.keyguardUpdateMonitorProvider = provider;
        this.dumpManagerProvider = provider2;
        this.loggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        HideDisplayCutoutController hideDisplayCutoutController;
        switch (this.$r8$classId) {
            case 0:
                return new DozeLog((KeyguardUpdateMonitor) this.keyguardUpdateMonitorProvider.mo144get(), (DumpManager) this.dumpManagerProvider.mo144get(), (DozeLogger) this.loggerProvider.mo144get());
            case 1:
                return new CommandQueue((Context) this.keyguardUpdateMonitorProvider.mo144get(), (ProtoTracer) this.dumpManagerProvider.mo144get(), (CommandRegistry) this.loggerProvider.mo144get());
            case 2:
                Lazy lazy = DoubleCheck.lazy(this.dumpManagerProvider);
                Object obj = (NotificationEntryManager) this.loggerProvider.mo144get();
                if (((NotifPipelineFlags) this.keyguardUpdateMonitorProvider.mo144get()).isNewPipelineEnabled()) {
                    obj = (CommonNotifCollection) lazy.get();
                }
                Objects.requireNonNull(obj, "Cannot return null from a non-@Nullable @Provides method");
                return obj;
            case 3:
                return new AsyncSensorManager((SensorManager) this.keyguardUpdateMonitorProvider.mo144get(), (ThreadFactory) this.dumpManagerProvider.mo144get(), (PluginManager) this.loggerProvider.mo144get());
            case 4:
                Context context = (Context) this.keyguardUpdateMonitorProvider.mo144get();
                DisplayController displayController = (DisplayController) this.dumpManagerProvider.mo144get();
                ShellExecutor shellExecutor = (ShellExecutor) this.loggerProvider.mo144get();
                if (!SystemProperties.getBoolean("ro.support_hide_display_cutout", false)) {
                    hideDisplayCutoutController = null;
                } else {
                    hideDisplayCutoutController = new HideDisplayCutoutController(context, new HideDisplayCutoutOrganizer(context, displayController, shellExecutor), shellExecutor);
                }
                Optional ofNullable = Optional.ofNullable(hideDisplayCutoutController);
                Objects.requireNonNull(ofNullable, "Cannot return null from a non-@Nullable @Provides method");
                return ofNullable;
            default:
                return new UnpinNotifications((Context) this.keyguardUpdateMonitorProvider.mo144get(), (SilenceAlertsDisabled) this.dumpManagerProvider.mo144get(), (Optional) this.loggerProvider.mo144get());
        }
    }
}
