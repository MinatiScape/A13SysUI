package androidx.core.view;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.view.DisplayCutout;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DisplayCutoutCompat {
    public final Object mDisplayCutout;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || DisplayCutoutCompat.class != obj.getClass()) {
            return false;
        }
        return Objects.equals(this.mDisplayCutout, ((DisplayCutoutCompat) obj).mDisplayCutout);
    }

    public final int hashCode() {
        Object obj = this.mDisplayCutout;
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("DisplayCutoutCompat{");
        m.append(this.mDisplayCutout);
        m.append("}");
        return m.toString();
    }

    public DisplayCutoutCompat(DisplayCutout displayCutout) {
        this.mDisplayCutout = displayCutout;
    }
}
