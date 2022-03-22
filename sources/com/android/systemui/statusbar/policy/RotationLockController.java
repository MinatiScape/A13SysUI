package com.android.systemui.statusbar.policy;
/* loaded from: classes.dex */
public interface RotationLockController extends CallbackController<RotationLockControllerCallback> {

    /* loaded from: classes.dex */
    public interface RotationLockControllerCallback {
        void onRotationLockStateChanged(boolean z);
    }

    int getRotationLockOrientation();

    boolean isCameraRotationEnabled();

    boolean isRotationLocked();

    void setRotationLocked(boolean z);
}
