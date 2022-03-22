package com.android.wm.shell.common;

import android.graphics.Rect;
import android.view.SurfaceControl;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class ScreenshotUtils {

    /* loaded from: classes.dex */
    public static class BufferConsumer implements Consumer<SurfaceControl.ScreenshotHardwareBuffer> {
        public SurfaceControl mSurfaceControl;
        public SurfaceControl.Transaction mTransaction;
        public SurfaceControl mScreenshot = null;
        public int mLayer = 2147483645;

        @Override // java.util.function.Consumer
        public final void accept(SurfaceControl.ScreenshotHardwareBuffer screenshotHardwareBuffer) {
            SurfaceControl.ScreenshotHardwareBuffer screenshotHardwareBuffer2 = screenshotHardwareBuffer;
            if (screenshotHardwareBuffer2 != null && screenshotHardwareBuffer2.getHardwareBuffer() != null) {
                SurfaceControl build = new SurfaceControl.Builder().setName("ScreenshotUtils screenshot").setFormat(-3).setSecure(screenshotHardwareBuffer2.containsSecureLayers()).setCallsite("ScreenshotUtils.takeScreenshot").setBLASTLayer().build();
                this.mScreenshot = build;
                this.mTransaction.setBuffer(build, screenshotHardwareBuffer2.getHardwareBuffer());
                this.mTransaction.setColorSpace(this.mScreenshot, screenshotHardwareBuffer2.getColorSpace());
                this.mTransaction.reparent(this.mScreenshot, this.mSurfaceControl);
                this.mTransaction.setLayer(this.mScreenshot, this.mLayer);
                this.mTransaction.show(this.mScreenshot);
                this.mTransaction.apply();
            }
        }

        public BufferConsumer(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
            this.mTransaction = transaction;
            this.mSurfaceControl = surfaceControl;
        }
    }

    public static void captureLayer(SurfaceControl surfaceControl, Rect rect, Consumer<SurfaceControl.ScreenshotHardwareBuffer> consumer) {
        consumer.accept(SurfaceControl.captureLayers(new SurfaceControl.LayerCaptureArgs.Builder(surfaceControl).setSourceCrop(rect).setCaptureSecureLayers(true).setAllowProtected(true).build()));
    }
}
