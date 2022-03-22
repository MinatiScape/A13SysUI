package com.android.systemui.tuner;

import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.statusbar.policy.DevicePostureControllerImpl;
import com.android.systemui.tuner.TunerService;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class LockscreenFragment$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ LockscreenFragment$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                LockscreenFragment lockscreenFragment = (LockscreenFragment) this.f$0;
                int i = LockscreenFragment.$r8$clinit;
                Objects.requireNonNull(lockscreenFragment);
                lockscreenFragment.mTunerService.removeTunable((TunerService.Tunable) obj);
                return;
            default:
                DevicePostureControllerImpl devicePostureControllerImpl = (DevicePostureControllerImpl) this.f$0;
                Objects.requireNonNull(devicePostureControllerImpl);
                ((DevicePostureController.Callback) obj).onPostureChanged(devicePostureControllerImpl.mCurrentDevicePosture);
                return;
        }
    }
}
