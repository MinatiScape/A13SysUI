package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StructureInfo.kt */
/* loaded from: classes.dex */
public final class StructureInfo {
    public final ComponentName componentName;
    public final List<ControlInfo> controls;
    public final CharSequence structure;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StructureInfo)) {
            return false;
        }
        StructureInfo structureInfo = (StructureInfo) obj;
        return Intrinsics.areEqual(this.componentName, structureInfo.componentName) && Intrinsics.areEqual(this.structure, structureInfo.structure) && Intrinsics.areEqual(this.controls, structureInfo.controls);
    }

    public final int hashCode() {
        int hashCode = this.structure.hashCode();
        return this.controls.hashCode() + ((hashCode + (this.componentName.hashCode() * 31)) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("StructureInfo(componentName=");
        m.append(this.componentName);
        m.append(", structure=");
        m.append((Object) this.structure);
        m.append(", controls=");
        m.append(this.controls);
        m.append(')');
        return m.toString();
    }

    public StructureInfo(ComponentName componentName, CharSequence charSequence, List<ControlInfo> list) {
        this.componentName = componentName;
        this.structure = charSequence;
        this.controls = list;
    }
}
