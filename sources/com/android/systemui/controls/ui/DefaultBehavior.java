package com.android.systemui.controls.ui;

import android.service.controls.Control;
import java.util.Set;
/* compiled from: DefaultBehavior.kt */
/* loaded from: classes.dex */
public final class DefaultBehavior implements Behavior {
    public ControlViewHolder cvh;

    @Override // com.android.systemui.controls.ui.Behavior
    public final void bind(ControlWithState controlWithState, int i) {
        CharSequence charSequence;
        ControlViewHolder controlViewHolder = this.cvh;
        ControlViewHolder controlViewHolder2 = null;
        if (controlViewHolder == null) {
            controlViewHolder = null;
        }
        Control control = controlWithState.control;
        if (control == null || (charSequence = control.getStatusText()) == null) {
            charSequence = "";
        }
        Set<Integer> set = ControlViewHolder.FORCE_PANEL_DEVICES;
        controlViewHolder.setStatusText(charSequence, false);
        ControlViewHolder controlViewHolder3 = this.cvh;
        if (controlViewHolder3 != null) {
            controlViewHolder2 = controlViewHolder3;
        }
        controlViewHolder2.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(false, i, true);
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public final void initialize(ControlViewHolder controlViewHolder) {
        this.cvh = controlViewHolder;
    }
}
