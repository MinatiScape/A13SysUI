package com.android.systemui.qs.external;

import android.content.ComponentName;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CustomTileStatePersister.kt */
/* loaded from: classes.dex */
public final class TileServiceKey {
    public final ComponentName componentName;
    public final String string;
    public final int user;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TileServiceKey)) {
            return false;
        }
        TileServiceKey tileServiceKey = (TileServiceKey) obj;
        return Intrinsics.areEqual(this.componentName, tileServiceKey.componentName) && this.user == tileServiceKey.user;
    }

    public final int hashCode() {
        return Integer.hashCode(this.user) + (this.componentName.hashCode() * 31);
    }

    public TileServiceKey(ComponentName componentName, int i) {
        this.componentName = componentName;
        this.user = i;
        StringBuilder sb = new StringBuilder();
        sb.append((Object) componentName.flattenToString());
        sb.append(':');
        sb.append(i);
        this.string = sb.toString();
    }

    public final String toString() {
        return this.string;
    }
}
