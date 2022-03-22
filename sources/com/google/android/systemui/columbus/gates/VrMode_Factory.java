package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Vibrator;
import com.android.keyguard.KeyguardClockSwitch;
import com.android.keyguard.KeyguardStatusView;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.qs.QuickStatusBarHeader;
import com.android.systemui.statusbar.QsFrameTranslateImpl;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class VrMode_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;

    public /* synthetic */ VrMode_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new VrMode((Context) this.contextProvider.mo144get());
            case 1:
                return getKeyguardClockSwitch((KeyguardStatusView) this.contextProvider.mo144get());
            case 2:
                return (Vibrator) ((Context) this.contextProvider.mo144get()).getSystemService(Vibrator.class);
            case 3:
                return ((LogBufferFactory) this.contextProvider.mo144get()).create("QSFragmentDisableFlagsLog", 10, 10, false);
            case 4:
                BatteryMeterView batteryMeterView = (BatteryMeterView) ((QuickStatusBarHeader) this.contextProvider.mo144get()).findViewById(2131427572);
                Objects.requireNonNull(batteryMeterView, "Cannot return null from a non-@Nullable @Provides method");
                return batteryMeterView;
            default:
                StatusBar statusBar = (StatusBar) this.contextProvider.mo144get();
                return new QsFrameTranslateImpl();
        }
    }

    public static KeyguardClockSwitch getKeyguardClockSwitch(KeyguardStatusView keyguardStatusView) {
        KeyguardClockSwitch keyguardClockSwitch = (KeyguardClockSwitch) keyguardStatusView.findViewById(2131428170);
        Objects.requireNonNull(keyguardClockSwitch, "Cannot return null from a non-@Nullable @Provides method");
        return keyguardClockSwitch;
    }
}
