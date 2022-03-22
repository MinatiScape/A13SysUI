package com.android.systemui.dreams.dagger;

import android.content.res.Resources;
import android.hardware.SensorPrivacyManager;
import android.view.View;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyControllerImpl;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DreamOverlayModule_ProvidesMaxBurnInOffsetFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider resourcesProvider;

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return Integer.valueOf(((Resources) this.resourcesProvider.mo144get()).getDimensionPixelSize(2131165601));
            case 1:
                return mo144get();
            case 2:
                OngoingPrivacyChip ongoingPrivacyChip = (OngoingPrivacyChip) ((View) this.resourcesProvider.mo144get()).findViewById(2131428617);
                Objects.requireNonNull(ongoingPrivacyChip, "Cannot return null from a non-@Nullable @Provides method");
                return ongoingPrivacyChip;
            default:
                return mo144get();
        }
    }

    public /* synthetic */ DreamOverlayModule_ProvidesMaxBurnInOffsetFactory(Provider provider, int i) {
        this.$r8$classId = i;
        this.resourcesProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final IndividualSensorPrivacyController mo144get() {
        switch (this.$r8$classId) {
            case 1:
                IndividualSensorPrivacyControllerImpl individualSensorPrivacyControllerImpl = new IndividualSensorPrivacyControllerImpl((SensorPrivacyManager) this.resourcesProvider.mo144get());
                individualSensorPrivacyControllerImpl.init();
                return individualSensorPrivacyControllerImpl;
            default:
                IndividualSensorPrivacyControllerImpl individualSensorPrivacyControllerImpl2 = new IndividualSensorPrivacyControllerImpl((SensorPrivacyManager) this.resourcesProvider.mo144get());
                individualSensorPrivacyControllerImpl2.init();
                return individualSensorPrivacyControllerImpl2;
        }
    }
}
