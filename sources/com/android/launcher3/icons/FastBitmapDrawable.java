package com.android.launcher3.icons;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.FloatProperty;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import java.util.Objects;
/* loaded from: classes.dex */
public class FastBitmapDrawable extends Drawable implements Drawable.Callback {
    public static final AccelerateInterpolator ACCEL = new AccelerateInterpolator();
    public static final DecelerateInterpolator DEACCEL = new DecelerateInterpolator();
    public static final AnonymousClass1 SCALE = new FloatProperty<FastBitmapDrawable>() { // from class: com.android.launcher3.icons.FastBitmapDrawable.1
        @Override // android.util.Property
        public final Float get(Object obj) {
            return Float.valueOf(((FastBitmapDrawable) obj).mScale);
        }

        @Override // android.util.FloatProperty
        public final void setValue(FastBitmapDrawable fastBitmapDrawable, float f) {
            FastBitmapDrawable fastBitmapDrawable2 = fastBitmapDrawable;
            fastBitmapDrawable2.mScale = f;
            fastBitmapDrawable2.invalidateSelf();
        }
    };
    public Drawable mBadge;
    public final Bitmap mBitmap;
    public ColorFilter mColorFilter;
    public final int mIconColor;
    public boolean mIsDisabled;
    public boolean mIsPressed;
    public ObjectAnimator mScaleAnimation;
    public final Paint mPaint = new Paint(3);
    public float mDisabledAlpha = 1.0f;
    public float mScale = 1.0f;
    public int mAlpha = 255;

    /* loaded from: classes.dex */
    public static class FastBitmapConstantState extends Drawable.ConstantState {
        public Drawable.ConstantState mBadgeConstantState;
        public final Bitmap mBitmap;
        public final int mIconColor;
        public boolean mIsDisabled;

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final int getChangingConfigurations() {
            return 0;
        }

        public FastBitmapDrawable createDrawable() {
            return new FastBitmapDrawable(this.mBitmap, this.mIconColor);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final FastBitmapDrawable newDrawable() {
            FastBitmapDrawable createDrawable = createDrawable();
            boolean z = this.mIsDisabled;
            Objects.requireNonNull(createDrawable);
            if (createDrawable.mIsDisabled != z) {
                createDrawable.mIsDisabled = z;
                createDrawable.updateFilter();
            }
            Drawable.ConstantState constantState = this.mBadgeConstantState;
            if (constantState != null) {
                createDrawable.setBadge(constantState.newDrawable());
            }
            return createDrawable;
        }

        public FastBitmapConstantState(Bitmap bitmap, int i) {
            this.mBitmap = bitmap;
            this.mIconColor = i;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean isStateful() {
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean onStateChange(int[] iArr) {
        boolean z;
        int length = iArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            } else if (iArr[i] == 16842919) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (this.mIsPressed == z) {
            return false;
        }
        this.mIsPressed = z;
        ObjectAnimator objectAnimator = this.mScaleAnimation;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.mScaleAnimation = null;
        }
        if (this.mIsPressed) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, SCALE, 1.1f);
            this.mScaleAnimation = ofFloat;
            ofFloat.setDuration(200L);
            this.mScaleAnimation.setInterpolator(ACCEL);
            this.mScaleAnimation.start();
        } else if (isVisible()) {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, SCALE, 1.0f);
            this.mScaleAnimation = ofFloat2;
            ofFloat2.setDuration(200L);
            this.mScaleAnimation.setInterpolator(DEACCEL);
            this.mScaleAnimation.start();
        } else {
            this.mScale = 1.0f;
            invalidateSelf();
        }
        return true;
    }

    public static ColorMatrixColorFilter getDisabledColorFilter(float f) {
        ColorMatrix colorMatrix = new ColorMatrix();
        ColorMatrix colorMatrix2 = new ColorMatrix();
        colorMatrix2.setSaturation(0.0f);
        float[] array = colorMatrix.getArray();
        array[0] = 0.5f;
        array[6] = 0.5f;
        array[12] = 0.5f;
        float f2 = 127;
        array[4] = f2;
        array[9] = f2;
        array[14] = f2;
        array[18] = f;
        colorMatrix2.preConcat(colorMatrix);
        return new ColorMatrixColorFilter(colorMatrix2);
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        if (this.mScale != 1.0f) {
            int save = canvas.save();
            Rect bounds = getBounds();
            float f = this.mScale;
            canvas.scale(f, f, bounds.exactCenterX(), bounds.exactCenterY());
            drawInternal(canvas, bounds);
            Drawable drawable = this.mBadge;
            if (drawable != null) {
                drawable.draw(canvas);
            }
            canvas.restoreToCount(save);
            return;
        }
        drawInternal(canvas, getBounds());
        Drawable drawable2 = this.mBadge;
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
    }

    public void drawInternal(Canvas canvas, Rect rect) {
        canvas.drawBitmap(this.mBitmap, (Rect) null, rect, this.mPaint);
    }

    @Override // android.graphics.drawable.Drawable
    public final ColorFilter getColorFilter() {
        return this.mPaint.getColorFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicHeight() {
        return this.mBitmap.getHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicWidth() {
        return this.mBitmap.getWidth();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void invalidateDrawable(Drawable drawable) {
        if (drawable == this.mBadge) {
            invalidateSelf();
        }
    }

    public FastBitmapConstantState newConstantState() {
        return new FastBitmapConstantState(this.mBitmap, this.mIconColor);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        if (drawable == this.mBadge) {
            scheduleSelf(runnable, j);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        if (this.mAlpha != i) {
            this.mAlpha = i;
            this.mPaint.setAlpha(i);
            invalidateSelf();
        }
    }

    public final void setBadge(Drawable drawable) {
        Drawable drawable2 = this.mBadge;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.mBadge = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        Rect bounds = getBounds();
        Drawable drawable3 = this.mBadge;
        if (drawable3 != null) {
            int width = bounds.width();
            int i = BaseIconFactory.PLACEHOLDER_BACKGROUND_COLOR;
            int i2 = (int) (width * 0.444f);
            int i3 = bounds.right;
            int i4 = bounds.bottom;
            drawable3.setBounds(i3 - i2, i4 - i2, i3, i4);
        }
        updateFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
        updateFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public final void setFilterBitmap(boolean z) {
        this.mPaint.setFilterBitmap(z);
        this.mPaint.setAntiAlias(z);
    }

    public void updateFilter() {
        ColorFilter colorFilter;
        Paint paint = this.mPaint;
        if (this.mIsDisabled) {
            colorFilter = getDisabledColorFilter(this.mDisabledAlpha);
        } else {
            colorFilter = this.mColorFilter;
        }
        paint.setColorFilter(colorFilter);
        Drawable drawable = this.mBadge;
        if (drawable != null) {
            drawable.setColorFilter(getColorFilter());
        }
        invalidateSelf();
    }

    public FastBitmapDrawable(Bitmap bitmap, int i) {
        this.mBitmap = bitmap;
        this.mIconColor = i;
        setFilterBitmap(true);
    }

    @Override // android.graphics.drawable.Drawable
    public final Drawable.ConstantState getConstantState() {
        FastBitmapConstantState newConstantState = newConstantState();
        newConstantState.mIsDisabled = this.mIsDisabled;
        Drawable drawable = this.mBadge;
        if (drawable != null) {
            newConstantState.mBadgeConstantState = drawable.getConstantState();
        }
        return newConstantState;
    }

    @Override // android.graphics.drawable.Drawable
    public final int getMinimumHeight() {
        return getBounds().height();
    }

    @Override // android.graphics.drawable.Drawable
    public final int getMinimumWidth() {
        return getBounds().width();
    }

    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        Drawable drawable = this.mBadge;
        if (drawable != null) {
            int width = rect.width();
            int i = BaseIconFactory.PLACEHOLDER_BACKGROUND_COLOR;
            int i2 = (int) (width * 0.444f);
            int i3 = rect.right;
            int i4 = rect.bottom;
            drawable.setBounds(i3 - i2, i4 - i2, i3, i4);
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        unscheduleSelf(runnable);
    }

    @Override // android.graphics.drawable.Drawable
    public final int getAlpha() {
        return this.mAlpha;
    }
}
