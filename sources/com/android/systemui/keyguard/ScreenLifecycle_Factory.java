package com.android.systemui.keyguard;

import android.app.StatsManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import com.android.internal.util.Preconditions;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.dreams.DreamOverlayContainerView;
import com.android.systemui.dreams.DreamOverlayStatusBarView;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.people.widget.PeopleSpaceWidgetPinnedReceiver;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QuickStatusBarHeader;
import com.android.systemui.statusbar.notification.stack.StackStateLogger;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.google.android.systemui.autorotate.DataLogger;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScreenLifecycle_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object dumpManagerProvider;

    public /* synthetic */ ScreenLifecycle_Factory(Object obj, int i) {
        this.$r8$classId = i;
        this.dumpManagerProvider = obj;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ScreenLifecycle((DumpManager) ((Provider) this.dumpManagerProvider).mo144get());
            case 1:
                DreamOverlayStatusBarView dreamOverlayStatusBarView = (DreamOverlayStatusBarView) Preconditions.checkNotNull((DreamOverlayStatusBarView) ((DreamOverlayContainerView) ((Provider) this.dumpManagerProvider).mo144get()).findViewById(2131427881), "R.id.status_bar must not be null");
                Objects.requireNonNull(dreamOverlayStatusBarView, "Cannot return null from a non-@Nullable @Provides method");
                return dreamOverlayStatusBarView;
            case 2:
                return new PeopleSpaceWidgetPinnedReceiver((PeopleSpaceWidgetManager) ((Provider) this.dumpManagerProvider).mo144get());
            case 3:
                StatusIconContainer statusIconContainer = (StatusIconContainer) ((QuickStatusBarHeader) ((Provider) this.dumpManagerProvider).mo144get()).findViewById(2131428922);
                Objects.requireNonNull(statusIconContainer, "Cannot return null from a non-@Nullable @Provides method");
                return statusIconContainer;
            case 4:
                return new StackStateLogger((LogBuffer) ((Provider) this.dumpManagerProvider).mo144get());
            case 5:
                BatteryMeterView batteryMeterView = (BatteryMeterView) ((View) ((Provider) this.dumpManagerProvider).mo144get()).findViewById(2131427572);
                Objects.requireNonNull(batteryMeterView, "Cannot return null from a non-@Nullable @Provides method");
                return batteryMeterView;
            case FalsingManager.VERSION /* 6 */:
                return new DataLogger((StatsManager) ((Provider) this.dumpManagerProvider).mo144get());
            default:
                Objects.requireNonNull((DependencyProvider) this.dumpManagerProvider);
                HandlerThread handlerThread = new HandlerThread("TimeTick");
                handlerThread.start();
                return new Handler(handlerThread.getLooper());
        }
    }
}
