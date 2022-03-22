package com.android.systemui.shared.recents.model;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import android.util.Log;
import android.window.TaskSnapshot;
/* loaded from: classes.dex */
public final class ThumbnailData {
    public ThumbnailData(TaskSnapshot taskSnapshot) {
        Bitmap bitmap = null;
        try {
            HardwareBuffer hardwareBuffer = taskSnapshot.getHardwareBuffer();
            bitmap = hardwareBuffer != null ? Bitmap.wrapHardwareBuffer(hardwareBuffer, taskSnapshot.getColorSpace()) : bitmap;
            if (hardwareBuffer != null) {
                hardwareBuffer.close();
            }
        } catch (IllegalArgumentException e) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unexpected snapshot without USAGE_GPU_SAMPLED_IMAGE: ");
            m.append(taskSnapshot.getHardwareBuffer());
            Log.e("ThumbnailData", m.toString(), e);
        }
        if (bitmap == null) {
            Point taskSize = taskSnapshot.getTaskSize();
            bitmap = Bitmap.createBitmap(taskSize.x, taskSize.y, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(-16777216);
        }
        new Rect(taskSnapshot.getContentInsets());
        new Rect(taskSnapshot.getLetterboxInsets());
        taskSnapshot.getOrientation();
        taskSnapshot.getRotation();
        taskSnapshot.isLowResolution();
        bitmap.getWidth();
        int i = taskSnapshot.getTaskSize().x;
        taskSnapshot.isRealSnapshot();
        taskSnapshot.isTranslucent();
        taskSnapshot.getWindowingMode();
        taskSnapshot.getAppearance();
        taskSnapshot.getId();
    }
}
