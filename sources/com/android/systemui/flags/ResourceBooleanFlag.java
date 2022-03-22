package com.android.systemui.flags;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import java.util.Objects;
/* compiled from: Flag.kt */
/* loaded from: classes.dex */
public final class ResourceBooleanFlag {
    public final int id;
    public final int resourceId;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ResourceBooleanFlag)) {
            return false;
        }
        ResourceBooleanFlag resourceBooleanFlag = (ResourceBooleanFlag) obj;
        int i = this.id;
        Objects.requireNonNull(resourceBooleanFlag);
        return i == resourceBooleanFlag.id && this.resourceId == resourceBooleanFlag.resourceId;
    }

    public final int hashCode() {
        return Integer.hashCode(this.resourceId) + (Integer.hashCode(this.id) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ResourceBooleanFlag(id=");
        m.append(this.id);
        m.append(", resourceId=");
        return Insets$$ExternalSyntheticOutline0.m(m, this.resourceId, ')');
    }

    public ResourceBooleanFlag(int i, int i2) {
        this.id = i;
        this.resourceId = i2;
    }
}
