package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.phone.AutoTileManager;
/* compiled from: DeviceControlsController.kt */
/* loaded from: classes.dex */
public interface DeviceControlsController {

    /* compiled from: DeviceControlsController.kt */
    /* loaded from: classes.dex */
    public interface Callback {
    }

    void removeCallback();

    void setCallback(AutoTileManager.AnonymousClass4 r1);
}
