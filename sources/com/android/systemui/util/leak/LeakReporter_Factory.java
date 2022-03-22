package com.android.systemui.util.leak;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.statusbar.policy.SmartReplyConstants;
import com.android.systemui.util.DeviceConfigProxy;
import com.google.android.systemui.columbus.ColumbusSettings;
import com.google.android.systemui.columbus.sensors.config.LowSensitivitySettingAdjustment;
import com.google.android.systemui.columbus.sensors.config.SensorConfiguration;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LeakReporter_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider leakDetectorProvider;
    public final Provider leakReportEmailProvider;

    public /* synthetic */ LeakReporter_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.leakDetectorProvider = provider2;
        this.leakReportEmailProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new LeakReporter((Context) this.contextProvider.mo144get(), (LeakDetector) this.leakDetectorProvider.mo144get(), (String) this.leakReportEmailProvider.mo144get());
            case 1:
                return new SmartReplyConstants((Handler) this.contextProvider.mo144get(), (Context) this.leakDetectorProvider.mo144get(), (DeviceConfigProxy) this.leakReportEmailProvider.mo144get());
            default:
                return new LowSensitivitySettingAdjustment((Context) this.contextProvider.mo144get(), (ColumbusSettings) this.leakDetectorProvider.mo144get(), (SensorConfiguration) this.leakReportEmailProvider.mo144get());
        }
    }
}
