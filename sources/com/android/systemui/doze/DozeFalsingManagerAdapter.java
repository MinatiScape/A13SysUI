package com.android.systemui.doze;

import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.doze.DozeMachine;
/* loaded from: classes.dex */
public final class DozeFalsingManagerAdapter implements DozeMachine.Part {
    public final FalsingCollector mFalsingCollector;

    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        boolean z;
        FalsingCollector falsingCollector = this.mFalsingCollector;
        int ordinal = state2.ordinal();
        if (ordinal == 3 || ordinal == 9 || ordinal == 10) {
            z = true;
        } else {
            z = false;
        }
        falsingCollector.setShowingAod(z);
    }

    public DozeFalsingManagerAdapter(FalsingCollector falsingCollector) {
        this.mFalsingCollector = falsingCollector;
    }
}
