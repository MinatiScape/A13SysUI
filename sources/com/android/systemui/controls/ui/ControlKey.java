package com.android.systemui.controls.ui;

import android.content.ComponentName;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsUiControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlKey {
    public final ComponentName componentName;
    public final String controlId;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlKey)) {
            return false;
        }
        ControlKey controlKey = (ControlKey) obj;
        return Intrinsics.areEqual(this.componentName, controlKey.componentName) && Intrinsics.areEqual(this.controlId, controlKey.controlId);
    }

    public final int hashCode() {
        return this.controlId.hashCode() + (this.componentName.hashCode() * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ControlKey(componentName=");
        m.append(this.componentName);
        m.append(", controlId=");
        m.append(this.controlId);
        m.append(')');
        return m.toString();
    }

    public ControlKey(ComponentName componentName, String str) {
        this.componentName = componentName;
        this.controlId = str;
    }
}
