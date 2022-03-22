package com.android.keyguard;

import com.android.keyguard.CarrierTextManager;
import com.android.systemui.util.ViewController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CarrierTextController extends ViewController<CarrierText> {
    public final AnonymousClass1 mCarrierTextCallback = new CarrierTextManager.CarrierTextCallback() { // from class: com.android.keyguard.CarrierTextController.1
        @Override // com.android.keyguard.CarrierTextManager.CarrierTextCallback
        public final void finishedWakingUp() {
            ((CarrierText) CarrierTextController.this.mView).setSelected(true);
        }

        @Override // com.android.keyguard.CarrierTextManager.CarrierTextCallback
        public final void startedGoingToSleep() {
            ((CarrierText) CarrierTextController.this.mView).setSelected(false);
        }

        @Override // com.android.keyguard.CarrierTextManager.CarrierTextCallback
        public final void updateCarrierInfo(CarrierTextManager.CarrierTextCallbackInfo carrierTextCallbackInfo) {
            ((CarrierText) CarrierTextController.this.mView).setText(carrierTextCallbackInfo.carrierText);
        }
    };
    public final CarrierTextManager mCarrierTextManager;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        ((CarrierText) this.mView).setSelected(keyguardUpdateMonitor.mDeviceInteractive);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        CarrierTextManager carrierTextManager = this.mCarrierTextManager;
        AnonymousClass1 r4 = this.mCarrierTextCallback;
        Objects.requireNonNull(carrierTextManager);
        carrierTextManager.mBgExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda2(carrierTextManager, r4, 0));
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        CarrierTextManager carrierTextManager = this.mCarrierTextManager;
        Objects.requireNonNull(carrierTextManager);
        carrierTextManager.mBgExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda2(carrierTextManager, null, 0));
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.keyguard.CarrierTextController$1] */
    public CarrierTextController(CarrierText carrierText, CarrierTextManager.Builder builder, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        super(carrierText);
        Objects.requireNonNull(carrierText);
        boolean z = carrierText.mShowAirplaneMode;
        Objects.requireNonNull(builder);
        builder.mShowAirplaneMode = z;
        builder.mShowMissingSim = carrierText.mShowMissingSim;
        this.mCarrierTextManager = builder.build();
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
    }
}
