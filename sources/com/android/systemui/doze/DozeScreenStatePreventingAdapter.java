package com.android.systemui.doze;

import com.android.systemui.doze.DozeMachine;
/* loaded from: classes.dex */
public final class DozeScreenStatePreventingAdapter extends DozeMachine.Service.Delegate {
    @Override // com.android.systemui.doze.DozeMachine.Service.Delegate, com.android.systemui.doze.DozeMachine.Service
    public final void setDozeScreenState(int i) {
        if (i == 3) {
            i = 2;
        } else if (i == 4) {
            i = 6;
        }
        super.setDozeScreenState(i);
    }

    public DozeScreenStatePreventingAdapter(DozeMachine.Service service) {
        super(service);
    }
}
