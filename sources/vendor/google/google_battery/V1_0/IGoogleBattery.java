package vendor.google.google_battery.V1_0;

import android.os.IHwInterface;
import android.os.RemoteException;
import com.google.android.systemui.adaptivecharging.AdaptiveChargingManager;
/* loaded from: classes.dex */
public interface IGoogleBattery extends IHwInterface {
    void getChargingStageAndDeadline(AdaptiveChargingManager.AnonymousClass2 r1) throws RemoteException;

    byte setChargingDeadline() throws RemoteException;
}
