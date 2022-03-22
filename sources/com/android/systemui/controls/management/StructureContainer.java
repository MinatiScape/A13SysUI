package com.android.systemui.controls.management;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsFavoritingActivity.kt */
/* loaded from: classes.dex */
public final class StructureContainer {
    public final ControlsModel model;
    public final CharSequence structureName;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StructureContainer)) {
            return false;
        }
        StructureContainer structureContainer = (StructureContainer) obj;
        return Intrinsics.areEqual(this.structureName, structureContainer.structureName) && Intrinsics.areEqual(this.model, structureContainer.model);
    }

    public final int hashCode() {
        return this.model.hashCode() + (this.structureName.hashCode() * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("StructureContainer(structureName=");
        m.append((Object) this.structureName);
        m.append(", model=");
        m.append(this.model);
        m.append(')');
        return m.toString();
    }

    public StructureContainer(CharSequence charSequence, AllModel allModel) {
        this.structureName = charSequence;
        this.model = allModel;
    }
}
