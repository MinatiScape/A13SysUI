package com.android.wm.shell.startingsurface;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Trace;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SplashscreenIconDrawableFactory$ImmobileIconDrawable extends Drawable {
    public Bitmap mIconBitmap;
    public final Matrix mMatrix;
    public final Paint mPaint = new Paint(7);

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return 1;
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        synchronized (this.mPaint) {
            Bitmap bitmap = this.mIconBitmap;
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, this.mMatrix, this.mPaint);
            } else {
                invalidateSelf();
            }
        }
    }

    public SplashscreenIconDrawableFactory$ImmobileIconDrawable(final Drawable drawable, final int i, int i2, Handler handler) {
        Matrix matrix = new Matrix();
        this.mMatrix = matrix;
        float f = i2 / i;
        matrix.setScale(f, f);
        handler.post(new Runnable() { // from class: com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$ImmobileIconDrawable$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SplashscreenIconDrawableFactory$ImmobileIconDrawable splashscreenIconDrawableFactory$ImmobileIconDrawable = SplashscreenIconDrawableFactory$ImmobileIconDrawable.this;
                Drawable drawable2 = drawable;
                int i3 = i;
                Objects.requireNonNull(splashscreenIconDrawableFactory$ImmobileIconDrawable);
                synchronized (splashscreenIconDrawableFactory$ImmobileIconDrawable.mPaint) {
                    Trace.traceBegin(32L, "preDrawIcon");
                    splashscreenIconDrawableFactory$ImmobileIconDrawable.mIconBitmap = Bitmap.createBitmap(i3, i3, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(splashscreenIconDrawableFactory$ImmobileIconDrawable.mIconBitmap);
                    drawable2.setBounds(0, 0, i3, i3);
                    drawable2.draw(canvas);
                    Trace.traceEnd(32L);
                }
            }
        });
    }
}
