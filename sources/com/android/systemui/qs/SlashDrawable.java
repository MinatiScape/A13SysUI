package com.android.systemui.qs;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
/* loaded from: classes.dex */
public class SlashDrawable extends Drawable {
    public float mCurrentSlashLength;
    public Drawable mDrawable;
    public final Paint mPaint;
    public final Path mPath;
    public final RectF mSlashRect;
    public ColorStateList mTintList;
    public PorterDuff.Mode mTintMode;

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return 255;
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicHeight() {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicHeight();
        }
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicWidth() {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicWidth();
        }
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        this.mDrawable.setAlpha(i);
        this.mPaint.setAlpha(i);
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
        this.mDrawable.setColorFilter(colorFilter);
        this.mPaint.setColorFilter(colorFilter);
    }

    public void setDrawableTintList(ColorStateList colorStateList) {
        this.mDrawable.setTintList(colorStateList);
    }

    @Override // android.graphics.drawable.Drawable
    public final void setTintList(ColorStateList colorStateList) {
        this.mTintList = colorStateList;
        super.setTintList(colorStateList);
        setDrawableTintList(colorStateList);
        this.mPaint.setColor(colorStateList.getDefaultColor());
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public final void setTintMode(PorterDuff.Mode mode) {
        this.mTintMode = mode;
        super.setTintMode(mode);
        this.mDrawable.setTintMode(mode);
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        canvas.save();
        Matrix matrix = new Matrix();
        int width = getBounds().width();
        int height = getBounds().height();
        float f = width;
        float f2 = f * 1.0f;
        float f3 = height;
        float f4 = 1.0f * f3;
        float f5 = f3 * (-0.088781714f);
        float f6 = (this.mCurrentSlashLength - 0.088781714f) * f3;
        RectF rectF = this.mSlashRect;
        rectF.left = 0.40544835f * f;
        rectF.top = f5;
        rectF.right = f * 0.4820516f;
        rectF.bottom = f6;
        this.mPath.reset();
        this.mPath.addRoundRect(this.mSlashRect, f2, f4, Path.Direction.CW);
        float f7 = width / 2;
        float f8 = height / 2;
        matrix.setRotate(-45.0f, f7, f8);
        this.mPath.transform(matrix);
        canvas.drawPath(this.mPath, this.mPaint);
        matrix.setRotate(45.0f, f7, f8);
        this.mPath.transform(matrix);
        matrix.setTranslate(this.mSlashRect.width(), 0.0f);
        this.mPath.transform(matrix);
        this.mPath.addRoundRect(this.mSlashRect, f2, f4, Path.Direction.CW);
        matrix.setRotate(-45.0f, f7, f8);
        this.mPath.transform(matrix);
        canvas.clipOutPath(this.mPath);
        this.mDrawable.draw(canvas);
        canvas.restore();
    }

    @Override // android.graphics.drawable.Drawable
    public final void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mDrawable.setBounds(rect);
    }

    @Override // android.graphics.drawable.Drawable
    public final void setTint(int i) {
        super.setTint(i);
        this.mDrawable.setTint(i);
        this.mPaint.setColor(i);
    }
}
