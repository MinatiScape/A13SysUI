package vendor.google.wireless_charger.V1_1;

import android.os.RemoteException;
import com.google.android.systemui.dreamliner.WirelessChargerImpl;
/* loaded from: classes.dex */
public interface IWirelessCharger extends vendor.google.wireless_charger.V1_0.IWirelessCharger {
    byte registerCallback(WirelessChargerImpl.WirelessChargerInfoCallback wirelessChargerInfoCallback) throws RemoteException;
}
