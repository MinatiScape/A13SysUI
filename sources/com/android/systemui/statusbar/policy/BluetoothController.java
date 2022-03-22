package com.android.systemui.statusbar.policy;

import com.android.systemui.Dumpable;
import java.util.ArrayList;
/* loaded from: classes.dex */
public interface BluetoothController extends CallbackController<Callback>, Dumpable {

    /* loaded from: classes.dex */
    public interface Callback {
        void onBluetoothDevicesChanged();

        void onBluetoothStateChange();
    }

    boolean canConfigBluetooth();

    int getBluetoothState();

    String getConnectedDeviceName();

    ArrayList getConnectedDevices();

    boolean isBluetoothAudioActive();

    boolean isBluetoothAudioProfileOnly();

    boolean isBluetoothConnected();

    boolean isBluetoothConnecting();

    boolean isBluetoothEnabled();

    boolean isBluetoothSupported();

    void setBluetoothEnabled(boolean z);
}
