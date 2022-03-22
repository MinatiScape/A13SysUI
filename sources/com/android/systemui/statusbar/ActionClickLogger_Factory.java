package com.android.systemui.statusbar;

import android.app.StatsManager;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Looper;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.util.concurrency.ExecutorImpl;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ActionClickLogger_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object bufferProvider;

    public /* synthetic */ ActionClickLogger_Factory(Object obj, int i) {
        this.$r8$classId = i;
        this.bufferProvider = obj;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ActionClickLogger((LogBuffer) ((Provider) this.bufferProvider).mo144get());
            case 1:
                StatsManager statsManager = (StatsManager) ((Context) ((Provider) this.bufferProvider).mo144get()).getSystemService(StatsManager.class);
                Objects.requireNonNull(statsManager, "Cannot return null from a non-@Nullable @Provides method");
                return statsManager;
            case 2:
                return new NearbyMediaDevicesManager((CommandQueue) ((Provider) this.bufferProvider).mo144get());
            case 3:
                return new ExecutorImpl((Looper) ((Provider) this.bufferProvider).mo144get());
            case 4:
                Optional ofNullable = Optional.ofNullable((UsbManager) ((Context) ((Provider) this.bufferProvider).mo144get()).getSystemService(UsbManager.class));
                Objects.requireNonNull(ofNullable, "Cannot return null from a non-@Nullable @Provides method");
                return ofNullable;
            default:
                Objects.requireNonNull((DependencyProvider) this.bufferProvider);
                ActivityManagerWrapper activityManagerWrapper = ActivityManagerWrapper.sInstance;
                Objects.requireNonNull(activityManagerWrapper, "Cannot return null from a non-@Nullable @Provides method");
                return activityManagerWrapper;
        }
    }
}
