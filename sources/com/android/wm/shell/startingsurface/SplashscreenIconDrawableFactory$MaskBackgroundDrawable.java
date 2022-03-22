package com.android.wm.shell.startingsurface;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.PathParser;
/* loaded from: classes.dex */
public class SplashscreenIconDrawableFactory$MaskBackgroundDrawable extends Drawable {
    public static Path sMask;
    public final Paint mBackgroundPaint;
    public final Path mMaskScaleOnly = new Path(new Path(sMask));
    public final Matrix mMaskMatrix = new Matrix();

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return 1;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.clipPath(this.mMaskScaleOnly);
        Paint paint = this.mBackgroundPaint;
        if (paint != null) {
            canvas.drawPath(this.mMaskScaleOnly, paint);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        Paint paint = this.mBackgroundPaint;
        if (paint != null) {
            paint.setAlpha(i);
        }
    }

    public void updateLayerBounds(Rect rect) {
        this.mMaskMatrix.setScale(rect.width() / 100.0f, rect.height() / 100.0f);
        sMask.transform(this.mMaskMatrix, this.mMaskScaleOnly);
    }

    public SplashscreenIconDrawableFactory$MaskBackgroundDrawable(int i) {
        sMask = PathParser.createPathFromPathData(Resources.getSystem().getString(17039972));
        if (i != 0) {
            Paint paint = new Paint(7);
            this.mBackgroundPaint = paint;
            paint.setColor(i);
            paint.setStyle(Paint.Style.FILL);
            return;
        }
        this.mBackgroundPaint = null;
    }

    @Override // android.graphics.drawable.Drawable
    public final void onBoundsChange(Rect rect) {
        if (!rect.isEmpty()) {
            updateLayerBounds(rect);
        }
    }
}
