package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Icon;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.ControlStatus;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsModel.kt */
/* loaded from: classes.dex */
public final class ControlStatusWrapper extends ElementWrapper implements ControlInterface {
    public final ControlStatus controlStatus;

    public ControlStatusWrapper(ControlStatus controlStatus) {
        super(0);
        this.controlStatus = controlStatus;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ControlStatusWrapper) && Intrinsics.areEqual(this.controlStatus, ((ControlStatusWrapper) obj).controlStatus);
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final String getControlId() {
        return this.controlStatus.getControlId();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final Icon getCustomIcon() {
        return this.controlStatus.getCustomIcon();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final int getDeviceType() {
        return this.controlStatus.getDeviceType();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final CharSequence getSubtitle() {
        return this.controlStatus.getSubtitle();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final CharSequence getTitle() {
        return this.controlStatus.getTitle();
    }

    public final int hashCode() {
        return this.controlStatus.hashCode();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final ComponentName getComponent() {
        ControlStatus controlStatus = this.controlStatus;
        Objects.requireNonNull(controlStatus);
        return controlStatus.component;
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final boolean getFavorite() {
        ControlStatus controlStatus = this.controlStatus;
        Objects.requireNonNull(controlStatus);
        return controlStatus.favorite;
    }

    @Override // com.android.systemui.controls.ControlInterface
    public final boolean getRemoved() {
        ControlStatus controlStatus = this.controlStatus;
        Objects.requireNonNull(controlStatus);
        return controlStatus.removed;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ControlStatusWrapper(controlStatus=");
        m.append(this.controlStatus);
        m.append(')');
        return m.toString();
    }
}
