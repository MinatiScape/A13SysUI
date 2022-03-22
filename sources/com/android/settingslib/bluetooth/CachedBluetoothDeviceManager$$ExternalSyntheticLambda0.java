package com.android.settingslib.bluetooth;

import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CachedBluetoothDeviceManager$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ CachedBluetoothDeviceManager$$ExternalSyntheticLambda0 INSTANCE = new CachedBluetoothDeviceManager$$ExternalSyntheticLambda0();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        if (((CachedBluetoothDevice) obj).getBondState() == 10) {
            return true;
        }
        return false;
    }
}
