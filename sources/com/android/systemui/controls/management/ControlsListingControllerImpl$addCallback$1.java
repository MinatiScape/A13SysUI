package com.android.systemui.controls.management;

import android.util.Log;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.ArrayList;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsListingControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsListingControllerImpl$addCallback$1 implements Runnable {
    public final /* synthetic */ ControlsListingController.ControlsListingCallback $listener;
    public final /* synthetic */ ControlsListingControllerImpl this$0;

    public ControlsListingControllerImpl$addCallback$1(ControlsListingControllerImpl controlsListingControllerImpl, ControlsListingController.ControlsListingCallback controlsListingCallback) {
        this.this$0 = controlsListingControllerImpl;
        this.$listener = controlsListingCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.this$0.userChangeInProgress.get() > 0) {
            ControlsListingControllerImpl controlsListingControllerImpl = this.this$0;
            ControlsListingController.ControlsListingCallback controlsListingCallback = this.$listener;
            Objects.requireNonNull(controlsListingControllerImpl);
            controlsListingControllerImpl.backgroundExecutor.execute(new ControlsListingControllerImpl$addCallback$1(controlsListingControllerImpl, controlsListingCallback));
            return;
        }
        ArrayList currentServices = this.this$0.getCurrentServices();
        Log.d("ControlsListingControllerImpl", Intrinsics.stringPlus("Subscribing callback, service count: ", Integer.valueOf(currentServices.size())));
        this.this$0.callbacks.add(this.$listener);
        this.$listener.onServicesUpdated(currentServices);
    }
}
