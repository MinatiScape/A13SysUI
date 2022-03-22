package com.android.systemui.navigationbar.buttons;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.FloatProperty;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyButtonDrawable extends Drawable {
    public static final AnonymousClass1 KEY_DRAWABLE_ROTATE = new FloatProperty<KeyButtonDrawable>() { // from class: com.android.systemui.navigationbar.buttons.KeyButtonDrawable.1
        @Override // android.util.Property
        public final Float get(Object obj) {
            KeyButtonDrawable keyButtonDrawable = (KeyButtonDrawable) obj;
            Objects.requireNonNull(keyButtonDrawable);
            return Float.valueOf(keyButtonDrawable.mState.mRotateDegrees);
        }

        @Override // android.util.FloatProperty
        public final void setValue(KeyButtonDrawable keyButtonDrawable, float f) {
            keyButtonDrawable.setRotation(f);
        }
    };
    public static final AnonymousClass2 KEY_DRAWABLE_TRANSLATE_Y = new FloatProperty<KeyButtonDrawable>() { // from class: com.android.systemui.navigationbar.buttons.KeyButtonDrawable.2
        @Override // android.util.Property
        public final Float get(Object obj) {
            KeyButtonDrawable keyButtonDrawable = (KeyButtonDrawable) obj;
            Objects.requireNonNull(keyButtonDrawable);
            return Float.valueOf(keyButtonDrawable.mState.mTranslationY);
        }

        @Override // android.util.FloatProperty
        public final void setValue(KeyButtonDrawable keyButtonDrawable, float f) {
            KeyButtonDrawable keyButtonDrawable2 = keyButtonDrawable;
            Objects.requireNonNull(keyButtonDrawable2);
            ShadowDrawableState shadowDrawableState = keyButtonDrawable2.mState;
            float f2 = shadowDrawableState.mTranslationX;
            if (f2 != f2 || shadowDrawableState.mTranslationY != f) {
                shadowDrawableState.mTranslationX = f2;
                shadowDrawableState.mTranslationY = f;
                keyButtonDrawable2.invalidateSelf();
            }
        }
    };
    public AnimatedVectorDrawable mAnimatedDrawable;
    public final AnonymousClass3 mAnimatedDrawableCallback;
    public final Paint mIconPaint = new Paint(3);
    public final Paint mShadowPaint = new Paint(3);
    public final ShadowDrawableState mState;

    /* loaded from: classes.dex */
    public static class ShadowDrawableState extends Drawable.ConstantState {
        public int mBaseHeight;
        public int mBaseWidth;
        public int mChangingConfigurations;
        public Drawable.ConstantState mChildState;
        public final int mDarkColor;
        public float mDarkIntensity;
        public boolean mHorizontalFlip;
        public boolean mIsHardwareBitmap;
        public Bitmap mLastDrawnIcon;
        public Bitmap mLastDrawnShadow;
        public final int mLightColor;
        public float mRotateDegrees;
        public int mShadowColor;
        public int mShadowOffsetX;
        public int mShadowOffsetY;
        public int mShadowSize;
        public final boolean mSupportsAnimation;
        public float mTranslationX;
        public float mTranslationY;
        public int mAlpha = 255;
        public final Color mOvalBackgroundColor = null;

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final boolean canApplyTheme() {
            return true;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final Drawable newDrawable() {
            return new KeyButtonDrawable(null, this);
        }

        public ShadowDrawableState(int i, int i2, boolean z, boolean z2) {
            this.mLightColor = i;
            this.mDarkColor = i2;
            this.mSupportsAnimation = z;
            this.mHorizontalFlip = z2;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean canApplyTheme() {
        Objects.requireNonNull(this.mState);
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicHeight() {
        ShadowDrawableState shadowDrawableState = this.mState;
        return ((Math.abs(shadowDrawableState.mShadowOffsetY) + shadowDrawableState.mShadowSize) * 2) + shadowDrawableState.mBaseHeight;
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicWidth() {
        ShadowDrawableState shadowDrawableState = this.mState;
        return ((Math.abs(shadowDrawableState.mShadowOffsetX) + shadowDrawableState.mShadowSize) * 2) + shadowDrawableState.mBaseWidth;
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        this.mState.mAlpha = i;
        this.mIconPaint.setAlpha(i);
        updateShadowAlpha();
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
        boolean z;
        this.mIconPaint.setColorFilter(colorFilter);
        AnimatedVectorDrawable animatedVectorDrawable = this.mAnimatedDrawable;
        if (animatedVectorDrawable != null) {
            if (this.mState.mOvalBackgroundColor != null) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                animatedVectorDrawable.setColorFilter(new PorterDuffColorFilter(this.mState.mLightColor, PorterDuff.Mode.SRC_IN));
            } else {
                animatedVectorDrawable.setColorFilter(colorFilter);
            }
        }
        invalidateSelf();
    }

    public final void setDarkIntensity(float f) {
        this.mState.mDarkIntensity = f;
        int intValue = ((Integer) ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(this.mState.mLightColor), Integer.valueOf(this.mState.mDarkColor))).intValue();
        updateShadowAlpha();
        setColorFilter(new PorterDuffColorFilter(intValue, PorterDuff.Mode.SRC_ATOP));
    }

    public final void setDrawableBounds(Drawable drawable) {
        ShadowDrawableState shadowDrawableState = this.mState;
        int abs = Math.abs(shadowDrawableState.mShadowOffsetX) + shadowDrawableState.mShadowSize;
        ShadowDrawableState shadowDrawableState2 = this.mState;
        int abs2 = Math.abs(shadowDrawableState2.mShadowOffsetY) + shadowDrawableState2.mShadowSize;
        drawable.setBounds(abs, abs2, getIntrinsicWidth() - abs, getIntrinsicHeight() - abs2);
    }

    public final void setRotation(float f) {
        ShadowDrawableState shadowDrawableState = this.mState;
        if (!shadowDrawableState.mSupportsAnimation && shadowDrawableState.mRotateDegrees != f) {
            shadowDrawableState.mRotateDegrees = f;
            invalidateSelf();
        }
    }

    public final void updateShadowAlpha() {
        int alpha = Color.alpha(this.mState.mShadowColor);
        Paint paint = this.mShadowPaint;
        ShadowDrawableState shadowDrawableState = this.mState;
        paint.setAlpha(Math.round((1.0f - shadowDrawableState.mDarkIntensity) * (shadowDrawableState.mAlpha / 255.0f) * alpha));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [android.graphics.drawable.Drawable$Callback, com.android.systemui.navigationbar.buttons.KeyButtonDrawable$3] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public KeyButtonDrawable(android.graphics.drawable.Drawable r3, com.android.systemui.navigationbar.buttons.KeyButtonDrawable.ShadowDrawableState r4) {
        /*
            r2 = this;
            r2.<init>()
            android.graphics.Paint r0 = new android.graphics.Paint
            r1 = 3
            r0.<init>(r1)
            r2.mIconPaint = r0
            android.graphics.Paint r0 = new android.graphics.Paint
            r0.<init>(r1)
            r2.mShadowPaint = r0
            com.android.systemui.navigationbar.buttons.KeyButtonDrawable$3 r0 = new com.android.systemui.navigationbar.buttons.KeyButtonDrawable$3
            r0.<init>()
            r2.mAnimatedDrawableCallback = r0
            r2.mState = r4
            if (r3 == 0) goto L_0x0035
            int r1 = r3.getIntrinsicHeight()
            r4.mBaseHeight = r1
            int r1 = r3.getIntrinsicWidth()
            r4.mBaseWidth = r1
            int r1 = r3.getChangingConfigurations()
            r4.mChangingConfigurations = r1
            android.graphics.drawable.Drawable$ConstantState r3 = r3.getConstantState()
            r4.mChildState = r3
        L_0x0035:
            boolean r3 = r4.mSupportsAnimation
            if (r3 == 0) goto L_0x004f
            android.graphics.drawable.Drawable$ConstantState r3 = r4.mChildState
            android.graphics.drawable.Drawable r3 = r3.newDrawable()
            android.graphics.drawable.Drawable r3 = r3.mutate()
            android.graphics.drawable.AnimatedVectorDrawable r3 = (android.graphics.drawable.AnimatedVectorDrawable) r3
            r2.mAnimatedDrawable = r3
            r3.setCallback(r0)
            android.graphics.drawable.AnimatedVectorDrawable r3 = r2.mAnimatedDrawable
            r2.setDrawableBounds(r3)
        L_0x004f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.buttons.KeyButtonDrawable.<init>(android.graphics.drawable.Drawable, com.android.systemui.navigationbar.buttons.KeyButtonDrawable$ShadowDrawableState):void");
    }

    public static KeyButtonDrawable create(Context context, int i, int i2, int i3, boolean z) {
        boolean z2;
        Resources resources = context.getResources();
        boolean z3 = false;
        if (resources.getConfiguration().getLayoutDirection() == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        Drawable drawable = context.getDrawable(i3);
        if (z2 && drawable.isAutoMirrored()) {
            z3 = true;
        }
        KeyButtonDrawable keyButtonDrawable = new KeyButtonDrawable(drawable, new ShadowDrawableState(i, i2, drawable instanceof AnimatedVectorDrawable, z3));
        if (z) {
            int dimensionPixelSize = resources.getDimensionPixelSize(2131166587);
            int dimensionPixelSize2 = resources.getDimensionPixelSize(2131166588);
            int dimensionPixelSize3 = resources.getDimensionPixelSize(2131166589);
            int color = context.getColor(2131100464);
            ShadowDrawableState shadowDrawableState = keyButtonDrawable.mState;
            if (!shadowDrawableState.mSupportsAnimation && !(shadowDrawableState.mShadowOffsetX == dimensionPixelSize && shadowDrawableState.mShadowOffsetY == dimensionPixelSize2 && shadowDrawableState.mShadowSize == dimensionPixelSize3 && shadowDrawableState.mShadowColor == color)) {
                shadowDrawableState.mShadowOffsetX = dimensionPixelSize;
                shadowDrawableState.mShadowOffsetY = dimensionPixelSize2;
                shadowDrawableState.mShadowSize = dimensionPixelSize3;
                shadowDrawableState.mShadowColor = color;
                keyButtonDrawable.mShadowPaint.setColorFilter(new PorterDuffColorFilter(keyButtonDrawable.mState.mShadowColor, PorterDuff.Mode.SRC_ATOP));
                keyButtonDrawable.updateShadowAlpha();
                keyButtonDrawable.invalidateSelf();
            }
        }
        return keyButtonDrawable;
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        boolean z;
        ShadowDrawableState shadowDrawableState;
        int[] iArr;
        Rect bounds = getBounds();
        if (!bounds.isEmpty()) {
            AnimatedVectorDrawable animatedVectorDrawable = this.mAnimatedDrawable;
            if (animatedVectorDrawable != null) {
                animatedVectorDrawable.draw(canvas);
                return;
            }
            if (this.mState.mIsHardwareBitmap != canvas.isHardwareAccelerated()) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                this.mState.mIsHardwareBitmap = canvas.isHardwareAccelerated();
            }
            if (this.mState.mLastDrawnIcon == null || z) {
                int intrinsicWidth = getIntrinsicWidth();
                int intrinsicHeight = getIntrinsicHeight();
                Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(createBitmap);
                Drawable mutate = this.mState.mChildState.newDrawable().mutate();
                setDrawableBounds(mutate);
                canvas2.save();
                if (this.mState.mHorizontalFlip) {
                    canvas2.scale(-1.0f, 1.0f, intrinsicWidth * 0.5f, intrinsicHeight * 0.5f);
                }
                mutate.draw(canvas2);
                canvas2.restore();
                if (this.mState.mIsHardwareBitmap) {
                    createBitmap = createBitmap.copy(Bitmap.Config.HARDWARE, false);
                }
                this.mState.mLastDrawnIcon = createBitmap;
            }
            canvas.save();
            ShadowDrawableState shadowDrawableState2 = this.mState;
            canvas.translate(shadowDrawableState2.mTranslationX, shadowDrawableState2.mTranslationY);
            canvas.rotate(this.mState.mRotateDegrees, getIntrinsicWidth() / 2, getIntrinsicHeight() / 2);
            ShadowDrawableState shadowDrawableState3 = this.mState;
            int i = shadowDrawableState3.mShadowSize;
            if (i > 0) {
                if (shadowDrawableState3.mLastDrawnShadow == null || z) {
                    if (i == 0) {
                        shadowDrawableState3.mLastDrawnIcon = null;
                    } else {
                        int intrinsicWidth2 = getIntrinsicWidth();
                        int intrinsicHeight2 = getIntrinsicHeight();
                        Bitmap createBitmap2 = Bitmap.createBitmap(intrinsicWidth2, intrinsicHeight2, Bitmap.Config.ARGB_8888);
                        Canvas canvas3 = new Canvas(createBitmap2);
                        Drawable mutate2 = this.mState.mChildState.newDrawable().mutate();
                        setDrawableBounds(mutate2);
                        canvas3.save();
                        if (this.mState.mHorizontalFlip) {
                            canvas3.scale(-1.0f, 1.0f, intrinsicWidth2 * 0.5f, intrinsicHeight2 * 0.5f);
                        }
                        mutate2.draw(canvas3);
                        canvas3.restore();
                        Paint paint = new Paint(3);
                        paint.setMaskFilter(new BlurMaskFilter(this.mState.mShadowSize, BlurMaskFilter.Blur.NORMAL));
                        Bitmap extractAlpha = createBitmap2.extractAlpha(paint, new int[2]);
                        paint.setMaskFilter(null);
                        createBitmap2.eraseColor(0);
                        canvas3.drawBitmap(extractAlpha, iArr[0], iArr[1], paint);
                        if (this.mState.mIsHardwareBitmap) {
                            createBitmap2 = createBitmap2.copy(Bitmap.Config.HARDWARE, false);
                        }
                        this.mState.mLastDrawnShadow = createBitmap2;
                    }
                }
                double d = (float) ((this.mState.mRotateDegrees * 3.141592653589793d) / 180.0d);
                float cos = ((float) ((Math.cos(d) * shadowDrawableState.mShadowOffsetX) + (Math.sin(d) * this.mState.mShadowOffsetY))) - this.mState.mTranslationX;
                double cos2 = Math.cos(d) * this.mState.mShadowOffsetY;
                double sin = Math.sin(d);
                ShadowDrawableState shadowDrawableState4 = this.mState;
                canvas.drawBitmap(shadowDrawableState4.mLastDrawnShadow, cos, ((float) (cos2 - (sin * shadowDrawableState4.mShadowOffsetX))) - shadowDrawableState4.mTranslationY, this.mShadowPaint);
            }
            canvas.drawBitmap(this.mState.mLastDrawnIcon, (Rect) null, bounds, this.mIconPaint);
            canvas.restore();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final void jumpToCurrentState() {
        super.jumpToCurrentState();
        AnimatedVectorDrawable animatedVectorDrawable = this.mAnimatedDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.jumpToCurrentState();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean setVisible(boolean z, boolean z2) {
        boolean visible = super.setVisible(z, z2);
        if (visible) {
            jumpToCurrentState();
        }
        return visible;
    }

    @Override // android.graphics.drawable.Drawable
    public final Drawable.ConstantState getConstantState() {
        return this.mState;
    }
}
