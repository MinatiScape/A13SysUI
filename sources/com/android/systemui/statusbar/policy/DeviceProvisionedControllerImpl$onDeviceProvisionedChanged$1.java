package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: DeviceProvisionedControllerImpl.kt */
/* loaded from: classes.dex */
public /* synthetic */ class DeviceProvisionedControllerImpl$onDeviceProvisionedChanged$1 extends FunctionReferenceImpl implements Function1<DeviceProvisionedController.DeviceProvisionedListener, Unit> {
    public static final DeviceProvisionedControllerImpl$onDeviceProvisionedChanged$1 INSTANCE = new DeviceProvisionedControllerImpl$onDeviceProvisionedChanged$1();

    public DeviceProvisionedControllerImpl$onDeviceProvisionedChanged$1() {
        super(1, DeviceProvisionedController.DeviceProvisionedListener.class, "onDeviceProvisionedChanged", "onDeviceProvisionedChanged()V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener) {
        deviceProvisionedListener.onDeviceProvisionedChanged();
        return Unit.INSTANCE;
    }
}
