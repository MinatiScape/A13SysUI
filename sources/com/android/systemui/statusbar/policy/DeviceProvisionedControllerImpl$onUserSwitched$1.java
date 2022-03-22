package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: DeviceProvisionedControllerImpl.kt */
/* loaded from: classes.dex */
public /* synthetic */ class DeviceProvisionedControllerImpl$onUserSwitched$1 extends FunctionReferenceImpl implements Function1<DeviceProvisionedController.DeviceProvisionedListener, Unit> {
    public static final DeviceProvisionedControllerImpl$onUserSwitched$1 INSTANCE = new DeviceProvisionedControllerImpl$onUserSwitched$1();

    public DeviceProvisionedControllerImpl$onUserSwitched$1() {
        super(1, DeviceProvisionedController.DeviceProvisionedListener.class, "onUserSwitched", "onUserSwitched()V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener) {
        deviceProvisionedListener.onUserSwitched();
        return Unit.INSTANCE;
    }
}
