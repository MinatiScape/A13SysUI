package com.google.android.systemui.reversecharging;

import android.frameworks.stats.IStats;
import android.frameworks.stats.VendorAtom;
import android.frameworks.stats.VendorAtomValue;
import android.hardware.google.pixel.vendor.PixelAtoms$ReverseDomainNames;
import android.os.IBinder;
import android.os.IInterface;
import android.os.ServiceManager;
import android.util.Log;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import com.google.protobuf.UninitializedMessageException;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class ReverseChargingMetrics {
    public static final boolean DEBUG = Log.isLoggable("ReverseChargingMetrics", 3);
    public static final PixelAtoms$ReverseDomainNames RDN;

    static {
        PixelAtoms$ReverseDomainNames.Builder newBuilder = PixelAtoms$ReverseDomainNames.newBuilder();
        Objects.requireNonNull(newBuilder);
        PixelAtoms$ReverseDomainNames buildPartial = newBuilder.buildPartial();
        if (buildPartial.isInitialized()) {
            RDN = buildPartial;
            return;
        }
        throw new UninitializedMessageException();
    }

    public static VendorAtom createVendorAtom(int i) {
        VendorAtom vendorAtom = new VendorAtom();
        vendorAtom.reverseDomainName = RDN.getPixel();
        vendorAtom.values = new VendorAtomValue[i];
        return vendorAtom;
    }

    public static void reportVendorAtom(VendorAtom vendorAtom) {
        try {
            Optional<IStats> tryConnectingToStatsService = tryConnectingToStatsService();
            if (tryConnectingToStatsService.isPresent()) {
                tryConnectingToStatsService.get().reportVendorAtom(vendorAtom);
                if (DEBUG) {
                    Log.i("ReverseChargingMetrics", "Report vendor atom OK, " + vendorAtom);
                }
            }
        } catch (Exception e) {
            Log.e("ReverseChargingMetrics", "Failed to log atom to IStats service, " + e);
        }
    }

    public static Optional<IStats> tryConnectingToStatsService() {
        IStats iStats;
        StringBuilder sb = new StringBuilder();
        String str = IStats.DESCRIPTOR;
        String m = MotionController$$ExternalSyntheticOutline1.m(sb, str, "/default");
        if (!ServiceManager.isDeclared(m)) {
            Log.e("ReverseChargingMetrics", "IStats is not registered");
            return Optional.empty();
        }
        IBinder waitForDeclaredService = ServiceManager.waitForDeclaredService(m);
        int i = IStats.Stub.$r8$clinit;
        if (waitForDeclaredService == null) {
            iStats = null;
        } else {
            IInterface queryLocalInterface = waitForDeclaredService.queryLocalInterface(str);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IStats)) {
                iStats = new IStats.Stub.Proxy(waitForDeclaredService);
            } else {
                iStats = (IStats) queryLocalInterface;
            }
        }
        return Optional.ofNullable(iStats);
    }
}
