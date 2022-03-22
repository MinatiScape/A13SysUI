package kotlinx.coroutines;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
/* compiled from: JobSupport.kt */
/* loaded from: classes.dex */
public final class Empty implements Incomplete {
    public final boolean isActive;

    @Override // kotlinx.coroutines.Incomplete
    public final NodeList getList() {
        return null;
    }

    public final String toString() {
        String str;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Empty{");
        if (this.isActive) {
            str = "Active";
        } else {
            str = "New";
        }
        m.append(str);
        m.append('}');
        return m.toString();
    }

    public Empty(boolean z) {
        this.isActive = z;
    }

    @Override // kotlinx.coroutines.Incomplete
    public final boolean isActive() {
        return this.isActive;
    }
}
