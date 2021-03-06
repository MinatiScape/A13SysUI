package com.android.wm.shell.startingsurface;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
/* loaded from: classes.dex */
public class SplashscreenIconDrawableFactory$AdaptiveForegroundDrawable extends SplashscreenIconDrawableFactory$MaskBackgroundDrawable {
    public final Drawable mForegroundDrawable;
    public final Rect mTmpOutRect = new Rect();

    public SplashscreenIconDrawableFactory$AdaptiveForegroundDrawable(Drawable drawable) {
        super(0);
        this.mForegroundDrawable = drawable;
    }

    @Override // com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$MaskBackgroundDrawable, android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
        this.mForegroundDrawable.setColorFilter(colorFilter);
    }

    @Override // com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$MaskBackgroundDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mForegroundDrawable.draw(canvas);
    }

    @Override // com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory$MaskBackgroundDrawable
    public final void updateLayerBounds(Rect rect) {
        super.updateLayerBounds(rect);
        int width = rect.width() / 2;
        int height = rect.height() / 2;
        int width2 = (int) (rect.width() / 1.3333334f);
        int height2 = (int) (rect.height() / 1.3333334f);
        Rect rect2 = this.mTmpOutRect;
        rect2.set(width - width2, height - height2, width + width2, height + height2);
        Drawable drawable = this.mForegroundDrawable;
        if (drawable != null) {
            drawable.setBounds(rect2);
        }
        invalidateSelf();
    }
}
