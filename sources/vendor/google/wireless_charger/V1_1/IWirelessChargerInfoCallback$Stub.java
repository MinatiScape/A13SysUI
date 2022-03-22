package vendor.google.wireless_charger.V1_1;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.RemoteException;
import android.util.Log;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.systemui.dock.DockManager;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda0;
import com.google.android.systemui.dreamliner.DockAlignmentController;
import com.google.android.systemui.dreamliner.DockObserver;
import com.google.android.systemui.dreamliner.WirelessCharger;
import com.google.android.systemui.dreamliner.WirelessChargerImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class IWirelessChargerInfoCallback$Stub extends HwBinder implements IHwInterface {
    public final IHwBinder asBinder() {
        return this;
    }

    public final boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) {
        return true;
    }

    public final void onTransact(int i, HwParcel hwParcel, HwParcel hwParcel2, int i2) throws RemoteException {
        int i3 = 1;
        switch (i) {
            case 1:
                hwParcel.enforceInterface("vendor.google.wireless_charger@1.1::IWirelessChargerInfoCallback");
                HwBlob readBuffer = hwParcel.readBuffer(12L);
                byte int8 = readBuffer.getInt8(0L);
                byte int82 = readBuffer.getInt8(1L);
                readBuffer.getInt32(4L);
                readBuffer.getInt32(8L);
                WirelessCharger.AlignInfoListener alignInfoListener = ((WirelessChargerImpl.WirelessChargerInfoCallback) this).mListener;
                int intValue = Byte.valueOf(int8).intValue();
                int intValue2 = Byte.valueOf(int82).intValue();
                DockAlignmentController.RegisterAlignInfoListener registerAlignInfoListener = (DockAlignmentController.RegisterAlignInfoListener) alignInfoListener;
                Objects.requireNonNull(registerAlignInfoListener);
                DockAlignmentController dockAlignmentController = DockAlignmentController.this;
                Objects.requireNonNull(dockAlignmentController);
                int i4 = dockAlignmentController.mAlignmentState;
                if (DockAlignmentController.DEBUG) {
                    Log.d("DockAlignmentController", "onAlignInfo, state: " + intValue + ", alignPct: " + intValue2);
                }
                int i5 = dockAlignmentController.mAlignmentState;
                if (intValue == 0) {
                    i3 = i5;
                } else if (intValue != 1) {
                    if (intValue != 2) {
                        if (intValue != 3) {
                            GridLayoutManager$$ExternalSyntheticOutline1.m("Unexpected state: ", intValue, "DockAlignmentController");
                        }
                    } else if (intValue2 >= 0) {
                        if (intValue2 >= 100) {
                            i3 = 0;
                        }
                    }
                    i3 = -1;
                } else {
                    i3 = 2;
                }
                dockAlignmentController.mAlignmentState = i3;
                if (i4 != i3) {
                    DockObserver dockObserver = dockAlignmentController.mDockObserver;
                    Objects.requireNonNull(dockObserver);
                    Log.d("DLObserver", "onAlignStateChanged alignState = " + i3);
                    dockObserver.mLastAlignState = i3;
                    Iterator it = dockObserver.mAlignmentStateListeners.iterator();
                    while (it.hasNext()) {
                        ((DockManager.AlignmentStateListener) it.next()).onAlignmentStateChanged(i3);
                    }
                    dockObserver.runPhotoAction();
                    dockObserver.notifyDreamlinerAlignStateChanged(i3);
                    if (DockAlignmentController.DEBUG) {
                        KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(VendorAtomValue$$ExternalSyntheticOutline1.m("onAlignStateChanged, state: "), dockAlignmentController.mAlignmentState, "DockAlignmentController");
                    }
                }
                DockObserver dockObserver2 = dockAlignmentController.mDockObserver;
                Objects.requireNonNull(dockObserver2);
                dockObserver2.refreshFanLevel(new WifiEntry$$ExternalSyntheticLambda0(dockObserver2, 6));
                return;
            case 256067662:
                hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                ArrayList arrayList = new ArrayList(Arrays.asList("vendor.google.wireless_charger@1.1::IWirelessChargerInfoCallback", "android.hidl.base@1.0::IBase"));
                hwParcel2.writeStatus(0);
                hwParcel2.writeStringVector(arrayList);
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
                hwParcel2.writeString("vendor.google.wireless_charger@1.1::IWirelessChargerInfoCallback");
                hwParcel2.send();
                return;
            case 256398152:
                hwParcel.enforceInterface("android.hidl.base@1.0::IBase");
                ArrayList arrayList2 = new ArrayList(Arrays.asList(new byte[]{-95, -73, 125, 36, -45, 43, 115, 27, -95, -68, -4, -57, -72, 69, -99, 83, 65, -13, -50, -82, 38, -52, 66, -58, 40, 85, -74, 113, -76, 16, 117, 112}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
                hwParcel2.writeStatus(0);
                HwBlob hwBlob = new HwBlob(16);
                int size = arrayList2.size();
                hwBlob.putInt32(8L, size);
                hwBlob.putBool(12L, false);
                HwBlob hwBlob2 = new HwBlob(size * 32);
                for (int i6 = 0; i6 < size; i6++) {
                    long j = i6 * 32;
                    byte[] bArr = (byte[]) arrayList2.get(i6);
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

    public final String toString() {
        return "vendor.google.wireless_charger@1.1::IWirelessChargerInfoCallback@Stub";
    }

    public final boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) {
        return true;
    }

    public final IHwInterface queryLocalInterface(String str) {
        if ("vendor.google.wireless_charger@1.1::IWirelessChargerInfoCallback".equals(str)) {
            return this;
        }
        return null;
    }
}
