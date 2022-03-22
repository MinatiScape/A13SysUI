package vendor.google.google_battery.V1_2;

import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.RemoteException;
import com.google.android.systemui.adaptivecharging.AdaptiveChargingManager;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public interface IGoogleBattery extends vendor.google.google_battery.V1_0.IGoogleBattery {

    /* loaded from: classes.dex */
    public static final class Proxy implements IGoogleBattery {
        public IHwBinder mRemote;

        @Override // vendor.google.google_battery.V1_2.IGoogleBattery
        public final byte clearBatteryDefender() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.google_battery@1.2::IGoogleBattery");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(5, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt8();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.google_battery.V1_0.IGoogleBattery
        public final void getChargingStageAndDeadline(AdaptiveChargingManager.AnonymousClass2 r5) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.google_battery@1.0::IGoogleBattery");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(2, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                r5.onValues(hwParcel2.readInt8(), hwParcel2.readString(), hwParcel2.readInt32());
            } finally {
                hwParcel2.release();
            }
        }

        public final int hashCode() {
            return this.mRemote.hashCode();
        }

        @Override // vendor.google.google_battery.V1_2.IGoogleBattery
        public final boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException {
            return this.mRemote.linkToDeath(deathRecipient, 0L);
        }

        @Override // vendor.google.google_battery.V1_0.IGoogleBattery
        public final byte setChargingDeadline() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.google_battery@1.0::IGoogleBattery");
            hwParcel.writeInt32(-3);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(1, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt8();
            } finally {
                hwParcel2.release();
            }
        }

        public final String toString() {
            try {
                StringBuilder sb = new StringBuilder();
                HwParcel hwParcel = new HwParcel();
                hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
                HwParcel hwParcel2 = new HwParcel();
                this.mRemote.transact(256136003, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                String readString = hwParcel2.readString();
                hwParcel2.release();
                sb.append(readString);
                sb.append("@Proxy");
                return sb.toString();
            } catch (RemoteException unused) {
                return "[class or subclass of vendor.google.google_battery@1.2::IGoogleBattery]@Proxy";
            }
        }

        @Override // vendor.google.google_battery.V1_2.IGoogleBattery
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(deathRecipient);
        }

        public Proxy(IHwBinder iHwBinder) {
            this.mRemote = iHwBinder;
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final IHwBinder asBinder() {
            return this.mRemote;
        }
    }

    byte clearBatteryDefender() throws RemoteException;

    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    @Deprecated
    static IGoogleBattery getService() throws RemoteException {
        IHwBinder service = HwBinder.getService("vendor.google.google_battery@1.2::IGoogleBattery", "default");
        if (service == null) {
            return null;
        }
        IGoogleBattery queryLocalInterface = service.queryLocalInterface("vendor.google.google_battery@1.2::IGoogleBattery");
        if (queryLocalInterface != null && (queryLocalInterface instanceof IGoogleBattery)) {
            return queryLocalInterface;
        }
        Proxy proxy = new Proxy(service);
        try {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel hwParcel2 = new HwParcel();
            proxy.mRemote.transact(256067662, hwParcel, hwParcel2, 0);
            hwParcel2.verifySuccess();
            hwParcel.releaseTemporaryStorage();
            ArrayList readStringVector = hwParcel2.readStringVector();
            hwParcel2.release();
            Iterator it = readStringVector.iterator();
            while (it.hasNext()) {
                if (((String) it.next()).equals("vendor.google.google_battery@1.2::IGoogleBattery")) {
                    return proxy;
                }
            }
            return null;
        } catch (RemoteException unused) {
            return null;
        }
    }
}
