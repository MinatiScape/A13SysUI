package com.android.wm.shell.transition;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Matrix;
import android.hardware.HardwareBuffer;
import android.media.Image;
import android.media.ImageReader;
import android.view.SurfaceControl;
import android.view.animation.Animation;
import com.android.wm.shell.common.TransactionPool;
import java.nio.ByteBuffer;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class ScreenRotationAnimation {
    public final int mAnimHint;
    public SurfaceControl mAnimLeash;
    public SurfaceControl mBackColorSurface;
    public final Context mContext;
    public final int mEndHeight;
    public final int mEndRotation;
    public final int mEndWidth;
    public Animation mRotateAlphaAnimation;
    public Animation mRotateEnterAnimation;
    public Animation mRotateExitAnimation;
    public SurfaceControl mScreenshotLayer;
    public final int mStartHeight;
    public float mStartLuma;
    public final int mStartRotation;
    public final int mStartWidth;
    public final SurfaceControl mSurfaceControl;
    public SurfaceControl.Transaction mTransaction;
    public final TransactionPool mTransactionPool;
    public final float[] mTmpFloats = new float[9];
    public final Matrix mSnapshotInitialMatrix = new Matrix();

    public static float getMedianBorderLuma(HardwareBuffer hardwareBuffer, ColorSpace colorSpace) {
        boolean z;
        if (hardwareBuffer != null && hardwareBuffer.getFormat() == 1) {
            if ((hardwareBuffer.getUsage() & 16384) == 16384) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                ImageReader newInstance = ImageReader.newInstance(hardwareBuffer.getWidth(), hardwareBuffer.getHeight(), hardwareBuffer.getFormat(), 1);
                newInstance.getSurface().attachAndQueueBufferWithColorSpace(hardwareBuffer, colorSpace);
                Image acquireLatestImage = newInstance.acquireLatestImage();
                if (!(acquireLatestImage == null || acquireLatestImage.getPlanes().length == 0)) {
                    Image.Plane plane = acquireLatestImage.getPlanes()[0];
                    ByteBuffer buffer = plane.getBuffer();
                    int width = acquireLatestImage.getWidth();
                    int height = acquireLatestImage.getHeight();
                    int pixelStride = plane.getPixelStride();
                    int rowStride = plane.getRowStride();
                    int i = (height * 2) + (width * 2);
                    float[] fArr = new float[i];
                    int i2 = 0;
                    for (int i3 = 0; i3 < width; i3++) {
                        int i4 = i2 + 1;
                        fArr[i2] = getPixelLuminance(buffer, i3, 0, pixelStride, rowStride);
                        i2 = i4 + 1;
                        fArr[i4] = getPixelLuminance(buffer, i3, height - 1, pixelStride, rowStride);
                    }
                    for (int i5 = 0; i5 < height; i5++) {
                        int i6 = i2 + 1;
                        fArr[i2] = getPixelLuminance(buffer, 0, i5, pixelStride, rowStride);
                        i2 = i6 + 1;
                        fArr[i6] = getPixelLuminance(buffer, width - 1, i5, pixelStride, rowStride);
                    }
                    newInstance.close();
                    Arrays.sort(fArr);
                    return fArr[i / 2];
                }
            }
        }
        return 0.0f;
    }

    public static float getPixelLuminance(ByteBuffer byteBuffer, int i, int i2, int i3, int i4) {
        int i5 = (i * i3) + (i2 * i4);
        return Color.valueOf(((byteBuffer.get(i5 + 3) & 255) << 24) | ((byteBuffer.get(i5) & 255) << 16) | 0 | ((byteBuffer.get(i5 + 1) & 255) << 8) | (byteBuffer.get(i5 + 2) & 255)).luminance();
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x012c A[Catch: OutOfResourcesException -> 0x0178, TRY_LEAVE, TryCatch #0 {OutOfResourcesException -> 0x0178, blocks: (B:3:0x009d, B:5:0x00c3, B:7:0x00c9, B:14:0x012c), top: B:32:0x009d }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x018e  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x01b5  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x01bf  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ScreenRotationAnimation(android.content.Context r17, android.view.SurfaceSession r18, com.android.wm.shell.common.TransactionPool r19, android.view.SurfaceControl.Transaction r20, android.window.TransitionInfo.Change r21, android.view.SurfaceControl r22, int r23) {
        /*
            Method dump skipped, instructions count: 500
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.transition.ScreenRotationAnimation.<init>(android.content.Context, android.view.SurfaceSession, com.android.wm.shell.common.TransactionPool, android.view.SurfaceControl$Transaction, android.window.TransitionInfo$Change, android.view.SurfaceControl, int):void");
    }
}
