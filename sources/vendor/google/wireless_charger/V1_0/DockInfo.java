package vendor.google.wireless_charger.V1_0;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DockInfo {
    public String manufacturer = new String();
    public String model = new String();
    public String serial = new String();
    public int maxFwSize = 0;
    public boolean isGetInfoSupported = false;
    public FirmwareVersion version = new FirmwareVersion();
    public byte orientation = 0;
    public byte type = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != DockInfo.class) {
            return false;
        }
        DockInfo dockInfo = (DockInfo) obj;
        return HidlSupport.deepEquals(this.manufacturer, dockInfo.manufacturer) && HidlSupport.deepEquals(this.model, dockInfo.model) && HidlSupport.deepEquals(this.serial, dockInfo.serial) && this.maxFwSize == dockInfo.maxFwSize && this.isGetInfoSupported == dockInfo.isGetInfoSupported && HidlSupport.deepEquals(this.version, dockInfo.version) && this.orientation == dockInfo.orientation && this.type == dockInfo.type;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.manufacturer)), Integer.valueOf(HidlSupport.deepHashCode(this.model)), Integer.valueOf(HidlSupport.deepHashCode(this.serial)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.maxFwSize))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isGetInfoSupported))), Integer.valueOf(HidlSupport.deepHashCode(this.version)), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.orientation))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.type))));
    }

    public final void readFromParcel(HwParcel hwParcel) {
        HwBlob readBuffer = hwParcel.readBuffer(88L);
        String string = readBuffer.getString(0L);
        this.manufacturer = string;
        hwParcel.readEmbeddedBuffer(string.getBytes().length + 1, readBuffer.handle(), 0L, false);
        String string2 = readBuffer.getString(16L);
        this.model = string2;
        hwParcel.readEmbeddedBuffer(string2.getBytes().length + 1, readBuffer.handle(), 16L, false);
        String string3 = readBuffer.getString(32L);
        this.serial = string3;
        hwParcel.readEmbeddedBuffer(string3.getBytes().length + 1, readBuffer.handle(), 32L, false);
        this.maxFwSize = readBuffer.getInt32(48L);
        this.isGetInfoSupported = readBuffer.getBool(52L);
        FirmwareVersion firmwareVersion = this.version;
        Objects.requireNonNull(firmwareVersion);
        firmwareVersion.major = readBuffer.getInt32(56L);
        firmwareVersion.minor = readBuffer.getInt32(60L);
        String string4 = readBuffer.getString(64L);
        firmwareVersion.extra = string4;
        hwParcel.readEmbeddedBuffer(string4.getBytes().length + 1, readBuffer.handle(), 64L, false);
        this.orientation = readBuffer.getInt8(80L);
        this.type = readBuffer.getInt8(81L);
    }

    public final String toString() {
        String str;
        String str2;
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("{", ".manufacturer = ");
        m.append(this.manufacturer);
        m.append(", .model = ");
        m.append(this.model);
        m.append(", .serial = ");
        m.append(this.serial);
        m.append(", .maxFwSize = ");
        m.append(this.maxFwSize);
        m.append(", .isGetInfoSupported = ");
        m.append(this.isGetInfoSupported);
        m.append(", .version = ");
        m.append(this.version);
        m.append(", .orientation = ");
        byte b = this.orientation;
        if (b == 0) {
            str = "ARBITRARY";
        } else if (b == 1) {
            str = "LANDSCAPE";
        } else if (b == 2) {
            str = "PORTRAIT";
        } else if (b == 3) {
            str = "BOTH";
        } else {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("0x");
            m2.append(Integer.toHexString(Byte.toUnsignedInt(b)));
            str = m2.toString();
        }
        m.append(str);
        m.append(", .type = ");
        byte b2 = this.type;
        if (b2 == 0) {
            str2 = "DESKTOP_DOCK";
        } else if (b2 == 1) {
            str2 = "DESKTOP_PAD";
        } else if (b2 == 2) {
            str2 = "AUTOMOBILE_DOCK";
        } else if (b2 == 3) {
            str2 = "AUTOMOBILE_PAD";
        } else if (b2 == 4) {
            str2 = "PHONE";
        } else if (b2 == 15) {
            str2 = "UNKNOWN";
        } else {
            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("0x");
            m3.append(Integer.toHexString(Byte.toUnsignedInt(b2)));
            str2 = m3.toString();
        }
        return MotionController$$ExternalSyntheticOutline1.m(m, str2, "}");
    }
}
