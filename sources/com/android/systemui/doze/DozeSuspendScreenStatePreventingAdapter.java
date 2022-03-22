package com.android.systemui.doze;

import com.android.systemui.doze.DozeMachine;
/* loaded from: classes.dex */
public final class DozeSuspendScreenStatePreventingAdapter extends DozeMachine.Service.Delegate {
    @Override // com.android.systemui.doze.DozeMachine.Service.Delegate, com.android.systemui.doze.DozeMachine.Service
    public final void setDozeScreenState(int i) {
        if (i == 4) {
            i = 3;
        }
        super.setDozeScreenState(i);
    }

    public DozeSuspendScreenStatePreventingAdapter(DozeMachine.Service service) {
        super(service);
    }
}
