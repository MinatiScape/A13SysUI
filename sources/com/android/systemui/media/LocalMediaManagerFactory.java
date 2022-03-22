package com.android.systemui.media;

import android.content.Context;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
/* compiled from: LocalMediaManagerFactory.kt */
/* loaded from: classes.dex */
public final class LocalMediaManagerFactory {
    public final Context context;
    public final LocalBluetoothManager localBluetoothManager;

    public LocalMediaManagerFactory(Context context, LocalBluetoothManager localBluetoothManager) {
        this.context = context;
        this.localBluetoothManager = localBluetoothManager;
    }
}
