package com.android.systemui.privacy.logging;

import android.content.Context;
import android.os.SystemProperties;
import com.android.internal.util.Preconditions;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.dreams.DreamOverlayContainerView;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.statusbar.RemoteInputNotificationRebuilder;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionLogger;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PrivacyLogger_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider bufferProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new PrivacyLogger((LogBuffer) this.bufferProvider.mo144get());
            case 1:
                BatteryMeterView batteryMeterView = (BatteryMeterView) Preconditions.checkNotNull((BatteryMeterView) ((DreamOverlayContainerView) this.bufferProvider.mo144get()).findViewById(2131427877), "R.id.battery must not be null");
                Objects.requireNonNull(batteryMeterView, "Cannot return null from a non-@Nullable @Provides method");
                return batteryMeterView;
            case 2:
                return Boolean.valueOf(((Context) this.bufferProvider.mo144get()).getResources().getBoolean(2131034155));
            case 3:
                return new RemoteInputNotificationRebuilder((Context) this.bufferProvider.mo144get());
            case 4:
                return new NotifCollectionLogger((LogBuffer) this.bufferProvider.mo144get());
            case 5:
                return mo144get();
            default:
                return mo144get();
        }
    }

    public /* synthetic */ PrivacyLogger_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.bufferProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public final Optional mo144get() {
        switch (this.$r8$classId) {
            case 5:
                Optional optional = (Optional) this.bufferProvider.mo144get();
                if (!SystemProperties.getBoolean("ro.support_one_handed_mode", false)) {
                    optional = Optional.empty();
                }
                Objects.requireNonNull(optional, "Cannot return null from a non-@Nullable @Provides method");
                return optional;
            default:
                return Optional.empty();
        }
    }
}
