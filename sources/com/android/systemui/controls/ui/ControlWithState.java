package com.android.systemui.controls.ui;

import android.content.ComponentName;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.service.controls.Control;
import com.android.systemui.controls.controller.ControlInfo;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlWithState.kt */
/* loaded from: classes.dex */
public final class ControlWithState {
    public final ControlInfo ci;
    public final ComponentName componentName;
    public final Control control;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlWithState)) {
            return false;
        }
        ControlWithState controlWithState = (ControlWithState) obj;
        return Intrinsics.areEqual(this.componentName, controlWithState.componentName) && Intrinsics.areEqual(this.ci, controlWithState.ci) && Intrinsics.areEqual(this.control, controlWithState.control);
    }

    public final int hashCode() {
        int hashCode = (this.ci.hashCode() + (this.componentName.hashCode() * 31)) * 31;
        Control control = this.control;
        return hashCode + (control == null ? 0 : control.hashCode());
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ControlWithState(componentName=");
        m.append(this.componentName);
        m.append(", ci=");
        m.append(this.ci);
        m.append(", control=");
        m.append(this.control);
        m.append(')');
        return m.toString();
    }

    public ControlWithState(ComponentName componentName, ControlInfo controlInfo, Control control) {
        this.componentName = componentName;
        this.ci = controlInfo;
        this.control = control;
    }
}
