package com.android.systemui.util.wrapper;

import com.android.internal.view.RotationPolicy;
/* compiled from: RotationPolicyWrapper.kt */
/* loaded from: classes.dex */
public interface RotationPolicyWrapper {
    int getRotationLockOrientation();

    boolean isCameraRotationEnabled();

    boolean isRotationLockToggleVisible();

    boolean isRotationLocked();

    void registerRotationPolicyListener(RotationPolicy.RotationPolicyListener rotationPolicyListener);

    void setRotationLock(boolean z);
}
