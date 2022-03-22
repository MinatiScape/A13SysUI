package com.android.systemui.statusbar.connectivity;

import com.android.settingslib.net.DataUsageController;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.policy.DataSaverControllerImpl;
/* loaded from: classes.dex */
public interface NetworkController extends CallbackController<SignalCallback>, DemoMode {

    /* loaded from: classes.dex */
    public interface EmergencyListener {
        void setEmergencyCallsOnly(boolean z);
    }

    DataSaverControllerImpl getDataSaverController();

    DataUsageController getMobileDataController();

    String getMobileDataNetworkName();

    int getNumberSubscriptions();

    boolean hasEmergencyCryptKeeperText();

    boolean hasMobileDataFeature();

    boolean hasVoiceCallingFeature();

    boolean isMobileDataNetworkInService();

    boolean isRadioOn();

    void setWifiEnabled(boolean z);
}
