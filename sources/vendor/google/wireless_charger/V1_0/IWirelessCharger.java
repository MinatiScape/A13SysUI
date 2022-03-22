package vendor.google.wireless_charger.V1_0;

import android.os.IHwInterface;
import android.os.RemoteException;
import com.google.android.systemui.dreamliner.WirelessChargerImpl;
import java.util.ArrayList;
/* loaded from: classes.dex */
public interface IWirelessCharger extends IHwInterface {
    void challenge(byte b, ArrayList arrayList, WirelessChargerImpl.ChallengeCallbackWrapper challengeCallbackWrapper) throws RemoteException;

    void getInformation(WirelessChargerImpl.GetInformationCallbackWrapper getInformationCallbackWrapper) throws RemoteException;

    void isDockPresent(WirelessChargerImpl wirelessChargerImpl) throws RemoteException;

    void keyExchange(ArrayList arrayList, WirelessChargerImpl.KeyExchangeCallbackWrapper keyExchangeCallbackWrapper) throws RemoteException;
}
