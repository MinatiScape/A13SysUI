package com.android.systemui.qs.tiles;

import android.provider.DeviceConfig;
import java.util.function.Supplier;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CameraToggleTile$$ExternalSyntheticLambda0 implements Supplier {
    public static final /* synthetic */ CameraToggleTile$$ExternalSyntheticLambda0 INSTANCE = new CameraToggleTile$$ExternalSyntheticLambda0();

    @Override // java.util.function.Supplier
    public final Object get() {
        int i = CameraToggleTile.$r8$clinit;
        return Boolean.valueOf(DeviceConfig.getBoolean("privacy", "camera_toggle_enabled", true));
    }
}
