package com.android.systemui.dump;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DumpManager.kt */
/* loaded from: classes.dex */
public final class RegisteredDumpable<T> {
    public final T dumpable;
    public final String name;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RegisteredDumpable)) {
            return false;
        }
        RegisteredDumpable registeredDumpable = (RegisteredDumpable) obj;
        return Intrinsics.areEqual(this.name, registeredDumpable.name) && Intrinsics.areEqual(this.dumpable, registeredDumpable.dumpable);
    }

    public final int hashCode() {
        int hashCode = this.name.hashCode() * 31;
        T t = this.dumpable;
        return hashCode + (t == null ? 0 : t.hashCode());
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("RegisteredDumpable(name=");
        m.append(this.name);
        m.append(", dumpable=");
        m.append(this.dumpable);
        m.append(')');
        return m.toString();
    }

    public RegisteredDumpable(String str, T t) {
        this.name = str;
        this.dumpable = t;
    }
}
