package com.android.systemui.qs.dagger;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.qs.QSFragment;
import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.MessageRouterImpl;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFragmentModule_ProvideRootViewFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider qsFragmentProvider;

    public /* synthetic */ QSFragmentModule_ProvideRootViewFactory(Provider provider, int i) {
        this.$r8$classId = i;
        this.qsFragmentProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                View view = ((QSFragment) this.qsFragmentProvider.mo144get()).getView();
                Objects.requireNonNull(view, "Cannot return null from a non-@Nullable @Provides method");
                return view;
            case 1:
                BatteryMeterView batteryMeterView = (BatteryMeterView) ((KeyguardStatusBarView) this.qsFragmentProvider.mo144get()).findViewById(2131427571);
                Objects.requireNonNull(batteryMeterView, "Cannot return null from a non-@Nullable @Provides method");
                return batteryMeterView;
            case 2:
                TelephonyManager telephonyManager = (TelephonyManager) ((Context) this.qsFragmentProvider.mo144get()).getSystemService(TelephonyManager.class);
                Objects.requireNonNull(telephonyManager, "Cannot return null from a non-@Nullable @Provides method");
                return telephonyManager;
            case 3:
                return ((LogBufferFactory) this.qsFragmentProvider.mo144get()).create("NotifSectionLog", 1000, 10, false);
            default:
                return new MessageRouterImpl((DelayableExecutor) this.qsFragmentProvider.mo144get());
        }
    }
}
