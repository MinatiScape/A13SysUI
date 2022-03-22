package vendor.google.wireless_charger.V1_3;

import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.HidlSupport;
import androidx.leanback.R$layout;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FanInfo {
    public byte fanMode = 0;
    public short currentRpm = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != FanInfo.class) {
            return false;
        }
        FanInfo fanInfo = (FanInfo) obj;
        return this.fanMode == fanInfo.fanMode && this.currentRpm == fanInfo.currentRpm;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.fanMode))), Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(this.currentRpm))));
    }

    public final String toString() {
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("{", ".fanMode = ");
        m.append(R$layout.toString(this.fanMode));
        m.append(", .currentRpm = ");
        m.append((int) this.currentRpm);
        m.append("}");
        return m.toString();
    }
}
