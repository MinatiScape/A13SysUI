package vendor.google.wireless_charger.V1_0;

import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.HidlSupport;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FirmwareVersion {
    public int major = 0;
    public int minor = 0;
    public String extra = new String();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != FirmwareVersion.class) {
            return false;
        }
        FirmwareVersion firmwareVersion = (FirmwareVersion) obj;
        return this.major == firmwareVersion.major && this.minor == firmwareVersion.minor && HidlSupport.deepEquals(this.extra, firmwareVersion.extra);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.major))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.minor))), Integer.valueOf(HidlSupport.deepHashCode(this.extra)));
    }

    public final String toString() {
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("{", ".major = ");
        m.append(this.major);
        m.append(", .minor = ");
        m.append(this.minor);
        m.append(", .extra = ");
        return MotionController$$ExternalSyntheticOutline1.m(m, this.extra, "}");
    }
}
