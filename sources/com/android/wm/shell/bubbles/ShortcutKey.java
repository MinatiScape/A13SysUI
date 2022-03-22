package com.android.wm.shell.bubbles;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BubbleDataRepository.kt */
/* loaded from: classes.dex */
public final class ShortcutKey {
    public final String pkg;
    public final int userId;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ShortcutKey)) {
            return false;
        }
        ShortcutKey shortcutKey = (ShortcutKey) obj;
        return this.userId == shortcutKey.userId && Intrinsics.areEqual(this.pkg, shortcutKey.pkg);
    }

    public final int hashCode() {
        return this.pkg.hashCode() + (Integer.hashCode(this.userId) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ShortcutKey(userId=");
        m.append(this.userId);
        m.append(", pkg=");
        m.append(this.pkg);
        m.append(')');
        return m.toString();
    }

    public ShortcutKey(int i, String str) {
        this.userId = i;
        this.pkg = str;
    }
}
