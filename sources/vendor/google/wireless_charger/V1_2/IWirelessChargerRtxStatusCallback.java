package vendor.google.wireless_charger.V1_2;

import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.RemoteException;
import com.google.android.systemui.reversecharging.ReverseWirelessCharger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
/* loaded from: classes.dex */
public interface IWirelessChargerRtxStatusCallback extends IHwInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends HwBinder implements IWirelessChargerRtxStatusCallback {
        public final IHwBinder asBinder() {
            return this;
        }

        public final boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
            return true;
        }

        public final String toString() {
            return "vendor.google.wireless_charger@1.2::IWirelessChargerRtxStatusCallback@Stub";
        }

        public final boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) {
            return true;
        }

        public final void onTransact(int i, HwParcel hwParcel, HwParcel hwParcel2, int i2) throws RemoteException {
            ArrayList arrayList;
            switch (i) {
                case 1:
                    hwParcel.enforceInterface("vendor.google.wireless_charger@1.2::IWirelessChargerRtxStatusCallback");
                    RtxStatusInfo rtxStatusInfo = new RtxStatusInfo();
                    rtxStatusInfo.readFromParcel(hwParcel);
                    ReverseWirelessCharger reverseWirelessCharger = (ReverseWirelessCharger) this;
                    synchronized (reverseWirelessCharger.mLock) {
                        arrayList = new ArrayList(reverseWirelessCharger.mRtxStatusCallbacks);
                    }
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ((ReverseWirelessCharger.RtxStatusCallback) it.next()).onRtxStatusChanged(rtxStatusInfo);
                    }
                    return;
                case 256067662:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    ArrayList arrayList2 = new ArrayList(Arrays.asList("vendor.google.wireless_charger@1.2::IWirelessChargerRtxStatusCallback", "android.hidl.base@1.0::IBase"));
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeStringVector(arrayList2);
                    hwParcel2.send();
                    return;
                case 256131655:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    hwParcel.readNativeHandle();
                    hwParcel.readStringVector();
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 256136003:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeString("vendor.google.wireless_charger@1.2::IWirelessChargerRtxStatusCallback");
                    hwParcel2.send();
                    return;
                case 256398152:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    ArrayList arrayList3 = new ArrayList(Arrays.asList(new byte[]{31, -61, -5, 52, -75, -23, 43, -97, -81, -10, -34, -35, 6, -29, -18, -126, 47, 34, 80, -50, 99, 33, 17, -14, -20, 25, -117, -52, -40, 93, -62, -120}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
                    hwParcel2.writeStatus(0);
                    HwBlob hwBlob = new HwBlob(16);
                    int size = arrayList3.size();
                    hwBlob.putInt32(8L, size);
                    hwBlob.putBool(12L, false);
                    HwBlob hwBlob2 = new HwBlob(size * 32);
                    for (int i3 = 0; i3 < size; i3++) {
                        long j = i3 * 32;
                        byte[] bArr = (byte[]) arrayList3.get(i3);
                        if (bArr == null || bArr.length != 32) {
                            throw new IllegalArgumentException("Array element is not of the expected length");
                        }
                        hwBlob2.putInt8Array(j, bArr);
                    }
                    hwBlob.putBlob(0L, hwBlob2);
                    hwParcel2.writeBuffer(hwBlob);
                    hwParcel2.send();
                    return;
                case 256462420:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    return;
                case 256921159:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 257049926:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    int pidIfSharable = HidlSupport.getPidIfSharable();
                    hwParcel2.writeStatus(0);
                    HwBlob hwBlob3 = new HwBlob(24);
                    hwBlob3.putInt32(0L, pidIfSharable);
                    hwBlob3.putInt64(8L, 0L);
                    hwBlob3.putInt32(16L, 0);
                    hwParcel2.writeBuffer(hwBlob3);
                    hwParcel2.send();
                    return;
                case 257120595:
                    hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                    HwBinder.enableInstrumentation();
                    return;
                default:
                    return;
            }
        }

        public final IHwInterface queryLocalInterface(String str) {
            if ("vendor.google.wireless_charger@1.2::IWirelessChargerRtxStatusCallback".equals(str)) {
                return this;
            }
            return null;
        }
    }
}
