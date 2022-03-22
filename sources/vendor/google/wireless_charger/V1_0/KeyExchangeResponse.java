package vendor.google.wireless_charger.V1_0;

import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.HidlSupport;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyExchangeResponse {
    public byte dockId = 0;
    public ArrayList<Byte> dockPublicKey = new ArrayList<>();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != KeyExchangeResponse.class) {
            return false;
        }
        KeyExchangeResponse keyExchangeResponse = (KeyExchangeResponse) obj;
        return this.dockId == keyExchangeResponse.dockId && HidlSupport.deepEquals(this.dockPublicKey, keyExchangeResponse.dockPublicKey);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.dockId))), Integer.valueOf(HidlSupport.deepHashCode(this.dockPublicKey)));
    }

    public final String toString() {
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("{", ".dockId = ");
        m.append((int) this.dockId);
        m.append(", .dockPublicKey = ");
        m.append(this.dockPublicKey);
        m.append("}");
        return m.toString();
    }
}
