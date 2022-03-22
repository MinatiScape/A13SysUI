package com.android.wm.shell.bubbles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.android.launcher3.icons.BaseIconFactory;
import com.android.launcher3.icons.BitmapInfo;
import com.android.launcher3.icons.ShadowGenerator;
/* loaded from: classes.dex */
public final class BubbleBadgeIconFactory extends BaseIconFactory {
    public final BitmapInfo getBadgeBitmap(Drawable drawable, boolean z) {
        int i;
        int i2;
        float f;
        ShadowGenerator shadowGenerator = new ShadowGenerator(this.mIconBitmapSize);
        int i3 = this.mIconBitmapSize;
        Bitmap createBitmap = Bitmap.createBitmap(i3, i3, Bitmap.Config.ARGB_8888);
        if (drawable != null) {
            this.mCanvas.setBitmap(createBitmap);
            this.mOldBounds.set(drawable.getBounds());
            if (drawable instanceof AdaptiveIconDrawable) {
                int max = Math.max((int) Math.ceil(0.035f * f), Math.round((0.0f * i3) / 2.0f));
                int i4 = (i3 - max) - max;
                drawable.setBounds(0, 0, i4, i4);
                int save = this.mCanvas.save();
                float f2 = max;
                this.mCanvas.translate(f2, f2);
                if (drawable instanceof BitmapInfo.Extender) {
                    ((BitmapInfo.Extender) drawable).drawForPersistence(this.mCanvas);
                } else {
                    drawable.draw(this.mCanvas);
                }
                this.mCanvas.restoreToCount(save);
            } else {
                if (drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    if (createBitmap != null && bitmap.getDensity() == 0) {
                        bitmapDrawable.setTargetDensity(this.mContext.getResources().getDisplayMetrics());
                    }
                }
                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();
                if (intrinsicWidth > 0 && intrinsicHeight > 0) {
                    float f3 = intrinsicWidth / intrinsicHeight;
                    if (intrinsicWidth > intrinsicHeight) {
                        i = (int) (i3 / f3);
                        i2 = i3;
                    } else if (intrinsicHeight > intrinsicWidth) {
                        i2 = (int) (i3 * f3);
                        i = i3;
                    }
                    int i5 = (i3 - i2) / 2;
                    int i6 = (i3 - i) / 2;
                    drawable.setBounds(i5, i6, i2 + i5, i + i6);
                    this.mCanvas.save();
                    float f4 = i3 / 2;
                    this.mCanvas.scale(1.0f, 1.0f, f4, f4);
                    drawable.draw(this.mCanvas);
                    this.mCanvas.restore();
                }
                i2 = i3;
                i = i2;
                int i52 = (i3 - i2) / 2;
                int i62 = (i3 - i) / 2;
                drawable.setBounds(i52, i62, i2 + i52, i + i62);
                this.mCanvas.save();
                float f42 = i3 / 2;
                this.mCanvas.scale(1.0f, 1.0f, f42, f42);
                drawable.draw(this.mCanvas);
                this.mCanvas.restore();
            }
            drawable.setBounds(this.mOldBounds);
            this.mCanvas.setBitmap(null);
        }
        if (drawable instanceof AdaptiveIconDrawable) {
            AdaptiveIconDrawable adaptiveIconDrawable = (AdaptiveIconDrawable) drawable;
            int intrinsicWidth2 = drawable.getIntrinsicWidth();
            Drawable foreground = adaptiveIconDrawable.getForeground();
            Drawable background = adaptiveIconDrawable.getBackground();
            Bitmap createBitmap2 = Bitmap.createBitmap(intrinsicWidth2, intrinsicWidth2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas();
            canvas.setBitmap(createBitmap2);
            Path path = new Path();
            float f5 = intrinsicWidth2 / 2.0f;
            path.addCircle(f5, f5, f5, Path.Direction.CW);
            canvas.clipPath(path);
            background.setBounds(0, 0, intrinsicWidth2, intrinsicWidth2);
            background.draw(canvas);
            int i7 = intrinsicWidth2 / 5;
            int i8 = -i7;
            int i9 = intrinsicWidth2 + i7;
            foreground.setBounds(i8, i8, i9, i9);
            foreground.draw(canvas);
            canvas.setBitmap(null);
            int i10 = this.mIconBitmapSize;
            createBitmap = Bitmap.createScaledBitmap(createBitmap2, i10, i10, true);
        }
        if (z) {
            int color = this.mContext.getResources().getColor(2131099884, null);
            Bitmap createBitmap3 = Bitmap.createBitmap(createBitmap.getWidth(), createBitmap.getHeight(), createBitmap.getConfig());
            Canvas canvas2 = new Canvas(createBitmap3);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            paint.setAntiAlias(true);
            canvas2.drawCircle(canvas2.getWidth() / 2, canvas2.getHeight() / 2, canvas2.getWidth() / 2, paint);
            int i11 = r12 * 2;
            float f6 = r12;
            canvas2.drawBitmap(Bitmap.createScaledBitmap(createBitmap, canvas2.getWidth() - i11, canvas2.getHeight() - i11, true), f6, f6, (Paint) null);
            shadowGenerator.recreateIcon(Bitmap.createBitmap(createBitmap3), canvas2);
            return createIconBitmap(createBitmap3);
        }
        Canvas canvas3 = new Canvas();
        canvas3.setBitmap(createBitmap);
        shadowGenerator.recreateIcon(Bitmap.createBitmap(createBitmap), canvas3);
        return createIconBitmap(createBitmap);
    }

    public BubbleBadgeIconFactory(Context context) {
        super(context, context.getResources().getConfiguration().densityDpi, context.getResources().getDimensionPixelSize(2131165412), false);
    }
}
