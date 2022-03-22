package com.google.android.systemui.smartspace;

import android.app.AlarmManager;
import com.google.android.systemui.reversecharging.ReverseChargingController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SmartSpaceController$$ExternalSyntheticLambda0 implements AlarmManager.OnAlarmListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ SmartSpaceController$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.app.AlarmManager.OnAlarmListener
    public final void onAlarm() {
        switch (this.$r8$classId) {
            case 0:
                SmartSpaceController smartSpaceController = (SmartSpaceController) this.f$0;
                Objects.requireNonNull(smartSpaceController);
                smartSpaceController.onExpire(false);
                return;
            default:
                ReverseChargingController reverseChargingController = (ReverseChargingController) this.f$0;
                boolean z = ReverseChargingController.DEBUG;
                Objects.requireNonNull(reverseChargingController);
                reverseChargingController.onAlarmRtxFinish(5);
                return;
        }
    }
}
