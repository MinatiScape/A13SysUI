package com.android.systemui.controls.management;

import android.content.ComponentName;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.UserAwareController;
import java.util.ArrayList;
/* compiled from: ControlsListingController.kt */
/* loaded from: classes.dex */
public interface ControlsListingController extends CallbackController<ControlsListingCallback>, UserAwareController {

    /* compiled from: ControlsListingController.kt */
    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface ControlsListingCallback {
        void onServicesUpdated(ArrayList arrayList);
    }

    CharSequence getAppLabel(ComponentName componentName);
}
