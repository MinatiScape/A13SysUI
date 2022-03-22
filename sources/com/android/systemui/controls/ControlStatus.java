package com.android.systemui.controls;

import android.content.ComponentName;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Icon;
import android.service.controls.Control;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlStatus.kt */
/* loaded from: classes.dex */
public final class ControlStatus implements ControlInterface {
    public final ComponentName component;
    public final Control control;
    public boolean favorite;
    public final boolean removed;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlStatus)) {
            return false;
        }
        ControlStatus controlStatus = (ControlStatus) obj;
        return Intrinsics.areEqual(this.control, controlStatus.control) && Intrinsics.areEqual(this.component, controlStatus.component) && this.favorite == controlStatus.favorite && this.removed == controlStatus.removed;
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final String getControlId() {
        return this.control.getControlId();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final Icon getCustomIcon() {
        return this.control.getCustomIcon();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final int getDeviceType() {
        return this.control.getDeviceType();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final CharSequence getSubtitle() {
        return this.control.getSubtitle();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final CharSequence getTitle() {
        return this.control.getTitle();
    }

    public final int hashCode() {
        int hashCode = (this.component.hashCode() + (this.control.hashCode() * 31)) * 31;
        boolean z = this.favorite;
        int i = 1;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = (hashCode + i2) * 31;
        boolean z2 = this.removed;
        if (!z2) {
            i = z2 ? 1 : 0;
        }
        return i4 + i;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ControlStatus(control=");
        m.append(this.control);
        m.append(", component=");
        m.append(this.component);
        m.append(", favorite=");
        m.append(this.favorite);
        m.append(", removed=");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.removed, ')');
    }

    public ControlStatus(Control control, ComponentName componentName, boolean z, boolean z2) {
        this.control = control;
        this.component = componentName;
        this.favorite = z;
        this.removed = z2;
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final ComponentName getComponent() {
        return this.component;
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final boolean getFavorite() {
        return this.favorite;
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final boolean getRemoved() {
        return this.removed;
    }
}
