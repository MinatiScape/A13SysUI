package com.android.launcher3.icons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.ViewDebug;
/* loaded from: classes.dex */
public final class DotRenderer {
    public final Bitmap mBackgroundWithShadow;
    public final float mBitmapOffset;
    public final Paint mCirclePaint = new Paint(3);
    public final float mCircleRadius;
    public final float[] mLeftDotPosition;
    public final float[] mRightDotPosition;

    /* loaded from: classes.dex */
    public static class DrawParams {
        @ViewDebug.ExportedProperty(category = "notification dot", formatToHexString = true)
        public int color;
        @ViewDebug.ExportedProperty(category = "notification dot")
        public Rect iconBounds = new Rect();
        @ViewDebug.ExportedProperty(category = "notification dot")
        public boolean leftAlign;
        @ViewDebug.ExportedProperty(category = "notification dot")
        public float scale;
    }

    public static float[] getPathPoint(Path path, float f, float f2) {
        float f3 = f / 2.0f;
        float f4 = (f2 * f3) + f3;
        Path path2 = new Path();
        path2.moveTo(f3, f3);
        path2.lineTo((f2 * 1.0f) + f4, 0.0f);
        path2.lineTo(f4, -1.0f);
        path2.close();
        path2.op(path, Path.Op.INTERSECT);
        new PathMeasure(path2, false).getPosTan(0.0f, r3, null);
        float[] fArr = {fArr[0] / f, fArr[1] / f};
        return fArr;
    }

    public DotRenderer(int i, Path path) {
        Bitmap createBitmap;
        int round = Math.round(i * 0.228f);
        round = round <= 0 ? 1 : round;
        RectF rectF = new RectF();
        float f = round;
        float f2 = f * 1.0f;
        float f3 = f2 / 24.0f;
        float f4 = f2 / 16.0f;
        float f5 = f / 2.0f;
        int max = Math.max(Math.round(f3 + f5), Math.round(f5 + f3 + f4));
        rectF.set(0.0f, 0.0f, f, f);
        float f6 = max - f5;
        rectF.offsetTo(f6, f6);
        int i2 = max * 2;
        Picture picture = new Picture();
        Canvas beginRecording = picture.beginRecording(i2, i2);
        Paint paint = new Paint(3);
        paint.setColor(0);
        paint.setShadowLayer(f3, 0.0f, f4, (7 << 24) | 0);
        beginRecording.drawRoundRect(rectF, f5, f5, paint);
        paint.setShadowLayer(f3, 0.0f, 0.0f, (88 << 24) | 0);
        beginRecording.drawRoundRect(rectF, f5, f5, paint);
        if (Color.alpha(0) < 255) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            paint.clearShadowLayer();
            paint.setColor(-16777216);
            beginRecording.drawRoundRect(rectF, f5, f5, paint);
            paint.setXfermode(null);
            paint.setColor(0);
            beginRecording.drawRoundRect(rectF, f5, f5, paint);
        }
        picture.endRecording();
        this.mBackgroundWithShadow = Bitmap.createBitmap(picture);
        this.mCircleRadius = f5;
        this.mBitmapOffset = (-createBitmap.getHeight()) * 0.5f;
        float f7 = 100;
        this.mLeftDotPosition = getPathPoint(path, f7, -1.0f);
        this.mRightDotPosition = getPathPoint(path, f7, 1.0f);
    }
}
