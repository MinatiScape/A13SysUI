package com.android.settingslib.drawable;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
/* loaded from: classes.dex */
public final class UserIconDrawable extends Drawable implements Drawable.Callback {
    public Drawable mBadge;
    public float mBadgeMargin;
    public float mBadgeRadius;
    public Bitmap mBitmap;
    public Paint mClearPaint;
    public float mDisplayRadius;
    public float mFramePadding;
    public Paint mFramePaint;
    public float mFrameWidth;
    public final Paint mIconPaint;
    public float mIntrinsicRadius;
    public final Paint mPaint;
    public int mSize;
    public Drawable mUserDrawable;
    public Bitmap mUserIcon;
    public final Matrix mIconMatrix = new Matrix();
    public float mPadding = 0.0f;
    public boolean mInvalidated = true;
    public ColorStateList mTintColor = null;
    public PorterDuff.Mode mTintMode = PorterDuff.Mode.SRC_ATOP;
    public ColorStateList mFrameColor = null;

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return -3;
    }

    public final void rebake() {
        this.mInvalidated = false;
        if (this.mBitmap == null) {
            return;
        }
        if (this.mUserDrawable != null || this.mUserIcon != null) {
            Canvas canvas = new Canvas(this.mBitmap);
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            Drawable drawable = this.mUserDrawable;
            if (drawable != null) {
                drawable.draw(canvas);
            } else if (this.mUserIcon != null) {
                int save = canvas.save();
                canvas.concat(this.mIconMatrix);
                canvas.drawCircle(this.mUserIcon.getWidth() * 0.5f, this.mUserIcon.getHeight() * 0.5f, this.mIntrinsicRadius, this.mIconPaint);
                canvas.restoreToCount(save);
            }
            ColorStateList colorStateList = this.mFrameColor;
            if (colorStateList != null) {
                this.mFramePaint.setColor(colorStateList.getColorForState(getState(), 0));
            }
            float f = this.mFrameWidth;
            if (this.mFramePadding + f > 0.001f) {
                canvas.drawCircle(getBounds().exactCenterX(), getBounds().exactCenterY(), (this.mDisplayRadius - this.mPadding) - (f * 0.5f), this.mFramePaint);
            }
            if (this.mBadge != null) {
                float f2 = this.mBadgeRadius;
                if (f2 > 0.001f) {
                    float f3 = f2 * 2.0f;
                    float height = this.mBitmap.getHeight() - f3;
                    float width = this.mBitmap.getWidth() - f3;
                    this.mBadge.setBounds((int) width, (int) height, (int) (width + f3), (int) (f3 + height));
                    float width2 = (this.mBadge.getBounds().width() * 0.5f) + this.mBadgeMargin;
                    float f4 = this.mBadgeRadius;
                    canvas.drawCircle(width + f4, height + f4, width2, this.mClearPaint);
                    this.mBadge.draw(canvas);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00a1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.android.settingslib.drawable.UserIconDrawable setBadgeIfManagedUser(final android.content.Context r5, int r6) {
        /*
            r4 = this;
            r0 = 1
            r1 = -10000(0xffffffffffffd8f0, float:NaN)
            if (r6 == r1) goto L_0x0073
            java.lang.Class<android.app.admin.DevicePolicyManager> r1 = android.app.admin.DevicePolicyManager.class
            java.lang.Object r1 = r5.getSystemService(r1)
            android.app.admin.DevicePolicyManager r1 = (android.app.admin.DevicePolicyManager) r1
            android.content.ComponentName r2 = r1.getProfileOwnerAsUser(r6)
            r3 = 0
            if (r2 == 0) goto L_0x0020
            android.os.UserHandle r6 = android.os.UserHandle.of(r6)
            android.content.ComponentName r6 = r1.getProfileOwnerOrDeviceOwnerSupervisionComponent(r6)
            if (r6 != 0) goto L_0x0020
            r6 = r0
            goto L_0x0021
        L_0x0020:
            r6 = r3
        L_0x0021:
            if (r6 == 0) goto L_0x0073
            java.lang.String r6 = android.os.Build.VERSION.CODENAME
            java.lang.String r1 = "REL"
            boolean r1 = r1.equals(r6)
            if (r1 == 0) goto L_0x002e
            goto L_0x0037
        L_0x002e:
            java.lang.String r1 = "T"
            int r6 = r6.compareTo(r1)
            if (r6 < 0) goto L_0x0037
            r3 = r0
        L_0x0037:
            if (r3 == 0) goto L_0x0059
            java.lang.Class<android.app.admin.DevicePolicyManager> r6 = android.app.admin.DevicePolicyManager.class
            java.lang.Object r6 = r5.getSystemService(r6)
            android.app.admin.DevicePolicyManager r6 = (android.app.admin.DevicePolicyManager) r6
            android.content.res.Resources r1 = r5.getResources()
            android.util.DisplayMetrics r1 = r1.getDisplayMetrics()
            int r1 = r1.densityDpi
            com.android.settingslib.drawable.UserIconDrawable$$ExternalSyntheticLambda0 r2 = new com.android.settingslib.drawable.UserIconDrawable$$ExternalSyntheticLambda0
            r2.<init>()
            java.lang.String r5 = "WORK_PROFILE_ICON"
            java.lang.String r3 = "SOLID_COLORED"
            android.graphics.drawable.Drawable r5 = r6.getDrawableForDensity(r5, r3, r1, r2)
            goto L_0x0074
        L_0x0059:
            r6 = 17302402(0x1080382, float:2.4981772E-38)
            android.content.res.Resources r1 = r5.getResources()
            android.util.DisplayMetrics r1 = r1.getDisplayMetrics()
            int r1 = r1.densityDpi
            android.content.res.Resources r2 = r5.getResources()
            android.content.res.Resources$Theme r5 = r5.getTheme()
            android.graphics.drawable.Drawable r5 = r2.getDrawableForDensity(r6, r1, r5)
            goto L_0x0074
        L_0x0073:
            r5 = 0
        L_0x0074:
            r4.mBadge = r5
            if (r5 == 0) goto L_0x00a1
            android.graphics.Paint r5 = r4.mClearPaint
            if (r5 != 0) goto L_0x0099
            android.graphics.Paint r5 = new android.graphics.Paint
            r5.<init>()
            r4.mClearPaint = r5
            r5.setAntiAlias(r0)
            android.graphics.Paint r5 = r4.mClearPaint
            android.graphics.PorterDuffXfermode r6 = new android.graphics.PorterDuffXfermode
            android.graphics.PorterDuff$Mode r0 = android.graphics.PorterDuff.Mode.CLEAR
            r6.<init>(r0)
            r5.setXfermode(r6)
            android.graphics.Paint r5 = r4.mClearPaint
            android.graphics.Paint$Style r6 = android.graphics.Paint.Style.FILL
            r5.setStyle(r6)
        L_0x0099:
            android.graphics.Rect r5 = r4.getBounds()
            r4.onBoundsChange(r5)
            goto L_0x00a4
        L_0x00a1:
            r4.invalidateSelf()
        L_0x00a4:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.drawable.UserIconDrawable.setBadgeIfManagedUser(android.content.Context, int):com.android.settingslib.drawable.UserIconDrawable");
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        if (this.mInvalidated) {
            rebake();
        }
        if (this.mBitmap != null) {
            ColorStateList colorStateList = this.mTintColor;
            if (colorStateList == null) {
                this.mPaint.setColorFilter(null);
            } else {
                int colorForState = colorStateList.getColorForState(getState(), this.mTintColor.getDefaultColor());
                PorterDuff.Mode mode = this.mTintMode;
                ColorFilter colorFilter = this.mPaint.getColorFilter();
                boolean z = true;
                if (colorFilter instanceof PorterDuffColorFilter) {
                    PorterDuffColorFilter porterDuffColorFilter = (PorterDuffColorFilter) colorFilter;
                    int color = porterDuffColorFilter.getColor();
                    PorterDuff.Mode mode2 = porterDuffColorFilter.getMode();
                    if (color == colorForState && mode2 == mode) {
                        z = false;
                    }
                }
                if (z) {
                    this.mPaint.setColorFilter(new PorterDuffColorFilter(colorForState, this.mTintMode));
                }
            }
            canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mPaint);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final Drawable.ConstantState getConstantState() {
        return new BitmapDrawable(this.mBitmap).getConstantState();
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicWidth() {
        int i = this.mSize;
        if (i <= 0) {
            return ((int) this.mIntrinsicRadius) * 2;
        }
        return i;
    }

    public final void initFramePaint() {
        if (this.mFramePaint == null) {
            Paint paint = new Paint();
            this.mFramePaint = paint;
            paint.setStyle(Paint.Style.STROKE);
            this.mFramePaint.setAntiAlias(true);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean isStateful() {
        ColorStateList colorStateList = this.mFrameColor;
        if (colorStateList == null || !colorStateList.isStateful()) {
            return false;
        }
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        this.mPaint.setAlpha(i);
        super.invalidateSelf();
    }

    public final UserIconDrawable setIcon(Bitmap bitmap) {
        Drawable drawable = this.mUserDrawable;
        if (drawable != null) {
            drawable.setCallback(null);
            this.mUserDrawable = null;
        }
        this.mUserIcon = bitmap;
        if (bitmap == null) {
            this.mIconPaint.setShader(null);
            this.mBitmap = null;
        } else {
            Paint paint = this.mIconPaint;
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            paint.setShader(new BitmapShader(bitmap, tileMode, tileMode));
        }
        onBoundsChange(getBounds());
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    public final void setTintList(ColorStateList colorStateList) {
        this.mTintColor = colorStateList;
        super.invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public final void setTintMode(PorterDuff.Mode mode) {
        this.mTintMode = mode;
        super.invalidateSelf();
    }

    public UserIconDrawable(int i) {
        Paint paint = new Paint();
        this.mIconPaint = paint;
        Paint paint2 = new Paint();
        this.mPaint = paint2;
        this.mSize = 0;
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint2.setFilterBitmap(true);
        paint2.setAntiAlias(true);
        if (i > 0) {
            setBounds(0, 0, i, i);
            this.mSize = i;
        }
        setIcon(null);
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicHeight() {
        return getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public final void invalidateSelf() {
        super.invalidateSelf();
        this.mInvalidated = true;
    }

    @Override // android.graphics.drawable.Drawable
    public final void onBoundsChange(Rect rect) {
        if (rect.isEmpty()) {
            return;
        }
        if (this.mUserIcon != null || this.mUserDrawable != null) {
            float min = Math.min(rect.width(), rect.height()) * 0.5f;
            int i = (int) (min * 2.0f);
            Bitmap bitmap = this.mBitmap;
            if (bitmap == null || i != ((int) (this.mDisplayRadius * 2.0f))) {
                this.mDisplayRadius = min;
                if (bitmap != null) {
                    bitmap.recycle();
                }
                this.mBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
            }
            float min2 = Math.min(rect.width(), rect.height()) * 0.5f;
            this.mDisplayRadius = min2;
            float f = ((min2 - this.mFrameWidth) - this.mFramePadding) - this.mPadding;
            RectF rectF = new RectF(rect.exactCenterX() - f, rect.exactCenterY() - f, rect.exactCenterX() + f, rect.exactCenterY() + f);
            if (this.mUserDrawable != null) {
                Rect rect2 = new Rect();
                rectF.round(rect2);
                this.mIntrinsicRadius = Math.min(this.mUserDrawable.getIntrinsicWidth(), this.mUserDrawable.getIntrinsicHeight()) * 0.5f;
                this.mUserDrawable.setBounds(rect2);
            } else {
                Bitmap bitmap2 = this.mUserIcon;
                if (bitmap2 != null) {
                    float width = bitmap2.getWidth() * 0.5f;
                    float height = this.mUserIcon.getHeight() * 0.5f;
                    this.mIntrinsicRadius = Math.min(width, height);
                    float f2 = this.mIntrinsicRadius;
                    this.mIconMatrix.setRectToRect(new RectF(width - f2, height - f2, width + f2, height + f2), rectF, Matrix.ScaleToFit.FILL);
                }
            }
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void invalidateDrawable(Drawable drawable) {
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        unscheduleSelf(runnable);
    }

    public Drawable getBadge() {
        return this.mBadge;
    }

    public Drawable getUserDrawable() {
        return this.mUserDrawable;
    }

    public Bitmap getUserIcon() {
        return this.mUserIcon;
    }

    public boolean isInvalidated() {
        return this.mInvalidated;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        scheduleSelf(runnable, j);
    }
}
