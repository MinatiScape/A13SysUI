package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.policy.FlashlightController;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.concurrent.TimeUnit;
/* compiled from: ToggleFlashlight.kt */
/* loaded from: classes.dex */
public final class ToggleFlashlight extends UserAction {
    public static final long FLASHLIGHT_TIMEOUT = TimeUnit.MINUTES.toMillis(2);
    public final FlashlightController flashlightController;
    public final Handler handler;
    public final String tag = "ToggleFlashlight";
    public final ToggleFlashlight$turnOffFlashlight$1 turnOffFlashlight = new Runnable() { // from class: com.google.android.systemui.columbus.actions.ToggleFlashlight$turnOffFlashlight$1
        @Override // java.lang.Runnable
        public final void run() {
            ToggleFlashlight.this.flashlightController.setFlashlight(false);
        }
    };
    public final UiEventLogger uiEventLogger;

    @Override // com.google.android.systemui.columbus.actions.UserAction
    public final boolean availableOnLockscreen() {
        return true;
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.handler.removeCallbacks(this.turnOffFlashlight);
        boolean z = !this.flashlightController.isEnabled();
        this.flashlightController.setFlashlight(z);
        if (z) {
            this.handler.postDelayed(this.turnOffFlashlight, FLASHLIGHT_TIMEOUT);
        }
        this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_FLASHLIGHT_TOGGLE);
    }

    public final void updateAvailable() {
        boolean z;
        if (!this.flashlightController.hasFlashlight() || !this.flashlightController.isAvailable()) {
            z = false;
        } else {
            z = true;
        }
        setAvailable(z);
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.google.android.systemui.columbus.actions.ToggleFlashlight$turnOffFlashlight$1] */
    public ToggleFlashlight(Context context, FlashlightController flashlightController, Handler handler, UiEventLogger uiEventLogger) {
        super(context);
        this.flashlightController = flashlightController;
        this.handler = handler;
        this.uiEventLogger = uiEventLogger;
        FlashlightController.FlashlightListener toggleFlashlight$flashlightListener$1 = new FlashlightController.FlashlightListener() { // from class: com.google.android.systemui.columbus.actions.ToggleFlashlight$flashlightListener$1
            @Override // com.android.systemui.statusbar.policy.FlashlightController.FlashlightListener
            public final void onFlashlightAvailabilityChanged(boolean z) {
                if (!z) {
                    ToggleFlashlight toggleFlashlight = ToggleFlashlight.this;
                    toggleFlashlight.handler.removeCallbacks(toggleFlashlight.turnOffFlashlight);
                }
                ToggleFlashlight.this.updateAvailable();
            }

            @Override // com.android.systemui.statusbar.policy.FlashlightController.FlashlightListener
            public final void onFlashlightChanged(boolean z) {
                if (!z) {
                    ToggleFlashlight toggleFlashlight = ToggleFlashlight.this;
                    toggleFlashlight.handler.removeCallbacks(toggleFlashlight.turnOffFlashlight);
                }
                ToggleFlashlight.this.updateAvailable();
            }

            @Override // com.android.systemui.statusbar.policy.FlashlightController.FlashlightListener
            public final void onFlashlightError() {
                ToggleFlashlight toggleFlashlight = ToggleFlashlight.this;
                toggleFlashlight.handler.removeCallbacks(toggleFlashlight.turnOffFlashlight);
                ToggleFlashlight.this.updateAvailable();
            }
        };
        flashlightController.addCallback(toggleFlashlight$flashlightListener$1);
        updateAvailable();
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }
}
