package com.android.launcher3.icons;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class IconNormalizer {
    public final Bitmap mBitmap;
    public final Canvas mCanvas;
    public boolean mEnableShapeDetection;
    public final float[] mLeftBorder;
    public final int mMaxSize;
    public final Paint mPaintMaskShape;
    public final Paint mPaintMaskShapeOutline;
    public final byte[] mPixels;
    public final float[] mRightBorder;
    public final Rect mBounds = new Rect();
    public final RectF mAdaptiveIconBounds = new RectF();
    public final Path mShapePath = new Path();
    public final Matrix mMatrix = new Matrix();
    public float mAdaptiveIconScale = 0.0f;

    public static void convertToConvexArray(float[] fArr, int i, int i2, int i3) {
        float[] fArr2 = new float[fArr.length - 1];
        int i4 = -1;
        float f = Float.MAX_VALUE;
        for (int i5 = i2 + 1; i5 <= i3; i5++) {
            if (fArr[i5] > -1.0f) {
                if (f == Float.MAX_VALUE) {
                    i4 = i2;
                } else {
                    float f2 = ((fArr[i5] - fArr[i4]) / (i5 - i4)) - f;
                    float f3 = i;
                    if (f2 * f3 < 0.0f) {
                        while (i4 > i2) {
                            i4--;
                            if ((((fArr[i5] - fArr[i4]) / (i5 - i4)) - fArr2[i4]) * f3 >= 0.0f) {
                                break;
                            }
                        }
                    }
                }
                f = (fArr[i5] - fArr[i4]) / (i5 - i4);
                for (int i6 = i4; i6 < i5; i6++) {
                    fArr2[i6] = f;
                    fArr[i6] = ((i6 - i4) * f) + fArr[i4];
                }
                i4 = i5;
            }
        }
    }

    public static float getScale(float f, float f2, float f3) {
        float f4 = f / f2;
        float m = f4 < 0.7853982f ? 0.6597222f : MotionController$$ExternalSyntheticOutline0.m(1.0f, f4, 0.040449437f, 0.6510417f);
        float f5 = f / f3;
        if (f5 > m) {
            return (float) Math.sqrt(m / f5);
        }
        return 1.0f;
    }

    @TargetApi(26)
    public static float normalizeAdaptiveIcon(Drawable drawable, int i, RectF rectF) {
        Rect rect = new Rect(drawable.getBounds());
        int i2 = 0;
        drawable.setBounds(0, 0, i, i);
        Path iconMask = ((AdaptiveIconDrawable) drawable).getIconMask();
        Region region = new Region();
        region.setPath(iconMask, new Region(0, 0, i, i));
        Rect bounds = region.getBounds();
        RegionIterator regionIterator = new RegionIterator(region);
        Rect rect2 = new Rect();
        while (regionIterator.next(rect2)) {
            i2 += rect2.height() * rect2.width();
        }
        if (rectF != null) {
            float f = i;
            rectF.set(bounds.left / f, bounds.top / f, 1.0f - (bounds.right / f), 1.0f - (bounds.bottom / f));
        }
        drawable.setBounds(rect);
        float f2 = i2;
        return getScale(f2, f2, i * i);
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x004c, code lost:
        if (r4 <= r16.mMaxSize) goto L_0x0050;
     */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00d4 A[Catch: all -> 0x012c, TryCatch #0 {, blocks: (B:4:0x0009, B:6:0x000e, B:8:0x0014, B:10:0x0020, B:11:0x0025, B:14:0x0029, B:18:0x0036, B:21:0x003c, B:23:0x004a, B:25:0x004e, B:27:0x0052, B:29:0x0056, B:30:0x0058, B:34:0x0085, B:40:0x0094, B:41:0x009b, B:45:0x00ac, B:46:0x00b6, B:51:0x00c5, B:53:0x00d4, B:56:0x00df, B:57:0x00e9, B:58:0x00ec, B:60:0x00f8, B:63:0x010c, B:65:0x0110, B:67:0x0113, B:68:0x011c), top: B:75:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00f8 A[Catch: all -> 0x012c, TryCatch #0 {, blocks: (B:4:0x0009, B:6:0x000e, B:8:0x0014, B:10:0x0020, B:11:0x0025, B:14:0x0029, B:18:0x0036, B:21:0x003c, B:23:0x004a, B:25:0x004e, B:27:0x0052, B:29:0x0056, B:30:0x0058, B:34:0x0085, B:40:0x0094, B:41:0x009b, B:45:0x00ac, B:46:0x00b6, B:51:0x00c5, B:53:0x00d4, B:56:0x00df, B:57:0x00e9, B:58:0x00ec, B:60:0x00f8, B:63:0x010c, B:65:0x0110, B:67:0x0113, B:68:0x011c), top: B:75:0x0009 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final synchronized float getScale(android.graphics.drawable.Drawable r17, android.graphics.RectF r18, android.graphics.Path r19, boolean[] r20) {
        /*
            Method dump skipped, instructions count: 303
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.launcher3.icons.IconNormalizer.getScale(android.graphics.drawable.Drawable, android.graphics.RectF, android.graphics.Path, boolean[]):float");
    }

    public final boolean isShape(Path path) {
        Rect rect;
        if (Math.abs((this.mBounds.width() / this.mBounds.height()) - 1.0f) > 0.05f) {
            return false;
        }
        this.mMatrix.reset();
        this.mMatrix.setScale(this.mBounds.width(), this.mBounds.height());
        Matrix matrix = this.mMatrix;
        Rect rect2 = this.mBounds;
        matrix.postTranslate(rect2.left, rect2.top);
        path.transform(this.mMatrix, this.mShapePath);
        this.mCanvas.drawPath(this.mShapePath, this.mPaintMaskShape);
        this.mCanvas.drawPath(this.mShapePath, this.mPaintMaskShapeOutline);
        ByteBuffer wrap = ByteBuffer.wrap(this.mPixels);
        wrap.rewind();
        this.mBitmap.copyPixelsToBuffer(wrap);
        Rect rect3 = this.mBounds;
        int i = rect3.top;
        int i2 = this.mMaxSize;
        int i3 = i * i2;
        int i4 = i2 - rect3.right;
        int i5 = 0;
        while (true) {
            rect = this.mBounds;
            if (i >= rect.bottom) {
                break;
            }
            int i6 = rect.left;
            int i7 = i3 + i6;
            while (i6 < this.mBounds.right) {
                if ((this.mPixels[i7] & 255) > 40) {
                    i5++;
                }
                i7++;
                i6++;
            }
            i3 = i7 + i4;
            i++;
        }
        if (i5 / (this.mBounds.height() * rect.width()) < 0.005f) {
            return true;
        }
        return false;
    }

    public IconNormalizer(Context context, int i, boolean z) {
        int i2 = i * 2;
        this.mMaxSize = i2;
        Bitmap createBitmap = Bitmap.createBitmap(i2, i2, Bitmap.Config.ALPHA_8);
        this.mBitmap = createBitmap;
        this.mCanvas = new Canvas(createBitmap);
        this.mPixels = new byte[i2 * i2];
        this.mLeftBorder = new float[i2];
        this.mRightBorder = new float[i2];
        Paint paint = new Paint();
        this.mPaintMaskShape = paint;
        paint.setColor(-65536);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        Paint paint2 = new Paint();
        this.mPaintMaskShapeOutline = paint2;
        paint2.setStrokeWidth(context.getResources().getDisplayMetrics().density * 2.0f);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setColor(-16777216);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mEnableShapeDetection = z;
    }
}
