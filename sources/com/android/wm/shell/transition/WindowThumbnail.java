package com.android.wm.shell.transition;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.ColorSpace;
import android.graphics.GraphicBuffer;
import android.hardware.HardwareBuffer;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
/* loaded from: classes.dex */
public final class WindowThumbnail {
    public SurfaceControl mSurfaceControl;

    public static WindowThumbnail createAndAttach(SurfaceSession surfaceSession, SurfaceControl surfaceControl, HardwareBuffer hardwareBuffer, SurfaceControl.Transaction transaction) {
        WindowThumbnail windowThumbnail = new WindowThumbnail();
        SurfaceControl.Builder parent = new SurfaceControl.Builder(surfaceSession).setParent(surfaceControl);
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("WindowThumanil : ");
        m.append(surfaceControl.toString());
        windowThumbnail.mSurfaceControl = parent.setName(m.toString()).setCallsite("WindowThumanil").setFormat(-3).build();
        transaction.setBuffer(windowThumbnail.mSurfaceControl, GraphicBuffer.createFromHardwareBuffer(hardwareBuffer));
        transaction.setColorSpace(windowThumbnail.mSurfaceControl, ColorSpace.get(ColorSpace.Named.SRGB));
        transaction.setLayer(windowThumbnail.mSurfaceControl, Integer.MAX_VALUE);
        transaction.show(windowThumbnail.mSurfaceControl);
        transaction.apply();
        return windowThumbnail;
    }
}
