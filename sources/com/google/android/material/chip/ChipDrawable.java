package com.google.android.material.chip;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearancePathProvider;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ChipDrawable extends MaterialShapeDrawable implements Drawable.Callback, TextDrawableHelper.TextDrawableDelegate {
    public static final int[] DEFAULT_STATE = {16842910};
    public static final ShapeDrawable closeIconRippleMask = new ShapeDrawable(new OvalShape());
    public boolean checkable;
    public Drawable checkedIcon;
    public ColorStateList checkedIconTint;
    public boolean checkedIconVisible;
    public ColorStateList chipBackgroundColor;
    public float chipEndPadding;
    public Drawable chipIcon;
    public float chipIconSize;
    public ColorStateList chipIconTint;
    public boolean chipIconVisible;
    public float chipMinHeight;
    public float chipStartPadding;
    public ColorStateList chipStrokeColor;
    public float chipStrokeWidth;
    public ColorStateList chipSurfaceColor;
    public Drawable closeIcon;
    public float closeIconEndPadding;
    public RippleDrawable closeIconRipple;
    public float closeIconSize;
    public float closeIconStartPadding;
    public int[] closeIconStateSet;
    public ColorStateList closeIconTint;
    public boolean closeIconVisible;
    public ColorFilter colorFilter;
    public ColorStateList compatRippleColor;
    public final Context context;
    public boolean currentChecked;
    public int currentChipBackgroundColor;
    public int currentChipStrokeColor;
    public int currentChipSurfaceColor;
    public int currentCompatRippleColor;
    public int currentCompositeSurfaceBackgroundColor;
    public int currentTextColor;
    public int currentTint;
    public boolean hasChipIconTint;
    public float iconEndPadding;
    public float iconStartPadding;
    public boolean isShapeThemingEnabled;
    public int maxWidth;
    public ColorStateList rippleColor;
    public boolean shouldDrawText;
    public final TextDrawableHelper textDrawableHelper;
    public float textEndPadding;
    public float textStartPadding;
    public ColorStateList tint;
    public PorterDuffColorFilter tintFilter;
    public TextUtils.TruncateAt truncateAt;
    public boolean useCompatRipple;
    public float chipCornerRadius = -1.0f;
    public final Paint chipPaint = new Paint(1);
    public final Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
    public final RectF rectF = new RectF();
    public final PointF pointF = new PointF();
    public final Path shapePath = new Path();
    public int alpha = 255;
    public PorterDuff.Mode tintMode = PorterDuff.Mode.SRC_IN;
    public WeakReference<Delegate> delegate = new WeakReference<>(null);
    public CharSequence text = "";

    /* loaded from: classes.dex */
    public interface Delegate {
        void onChipDrawableSizeChange();
    }

    @Override // com.google.android.material.shape.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public final int getOpacity() {
        return -3;
    }

    @Override // com.google.android.material.shape.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public final boolean isStateful() {
        ColorStateList colorStateList;
        if (isStateful(this.chipSurfaceColor) || isStateful(this.chipBackgroundColor) || isStateful(this.chipStrokeColor)) {
            return true;
        }
        if (this.useCompatRipple && isStateful(this.compatRippleColor)) {
            return true;
        }
        TextDrawableHelper textDrawableHelper = this.textDrawableHelper;
        Objects.requireNonNull(textDrawableHelper);
        TextAppearance textAppearance = textDrawableHelper.textAppearance;
        if ((textAppearance == null || (colorStateList = textAppearance.textColor) == null || !colorStateList.isStateful()) ? false : true) {
            return true;
        }
        return (this.checkedIconVisible && this.checkedIcon != null && this.checkable) || isStateful(this.chipIcon) || isStateful(this.checkedIcon) || isStateful(this.tint);
    }

    @Override // com.google.android.material.shape.MaterialShapeDrawable, android.graphics.drawable.Drawable, com.google.android.material.internal.TextDrawableHelper.TextDrawableDelegate
    public final boolean onStateChange(int[] iArr) {
        if (this.isShapeThemingEnabled) {
            super.onStateChange(iArr);
        }
        return onStateChange(iArr, this.closeIconStateSet);
    }

    public static void unapplyChildDrawable(Drawable drawable) {
        if (drawable != null) {
            drawable.setCallback(null);
        }
    }

    public final void applyChildDrawable(Drawable drawable) {
        if (drawable != null) {
            drawable.setCallback(this);
            drawable.setLayoutDirection(getLayoutDirection());
            drawable.setLevel(getLevel());
            drawable.setVisible(isVisible(), false);
            if (drawable == this.closeIcon) {
                if (drawable.isStateful()) {
                    drawable.setState(this.closeIconStateSet);
                }
                drawable.setTintList(this.closeIconTint);
                return;
            }
            Drawable drawable2 = this.chipIcon;
            if (drawable == drawable2 && this.hasChipIconTint) {
                drawable2.setTintList(this.chipIconTint);
            }
            if (drawable.isStateful()) {
                drawable.setState(getState());
            }
        }
    }

    @Override // com.google.android.material.shape.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        boolean z;
        int i6;
        Rect bounds = getBounds();
        if (!bounds.isEmpty() && (i = this.alpha) != 0) {
            if (i < 255) {
                i2 = canvas.saveLayerAlpha(bounds.left, bounds.top, bounds.right, bounds.bottom, i);
            } else {
                i2 = 0;
            }
            if (!this.isShapeThemingEnabled) {
                this.chipPaint.setColor(this.currentChipSurfaceColor);
                this.chipPaint.setStyle(Paint.Style.FILL);
                this.rectF.set(bounds);
                canvas.drawRoundRect(this.rectF, getChipCornerRadius(), getChipCornerRadius(), this.chipPaint);
            }
            if (!this.isShapeThemingEnabled) {
                this.chipPaint.setColor(this.currentChipBackgroundColor);
                this.chipPaint.setStyle(Paint.Style.FILL);
                Paint paint = this.chipPaint;
                ColorFilter colorFilter = this.colorFilter;
                if (colorFilter == null) {
                    colorFilter = this.tintFilter;
                }
                paint.setColorFilter(colorFilter);
                this.rectF.set(bounds);
                canvas.drawRoundRect(this.rectF, getChipCornerRadius(), getChipCornerRadius(), this.chipPaint);
            }
            if (this.isShapeThemingEnabled) {
                super.draw(canvas);
            }
            if (this.chipStrokeWidth > 0.0f && !this.isShapeThemingEnabled) {
                this.chipPaint.setColor(this.currentChipStrokeColor);
                this.chipPaint.setStyle(Paint.Style.STROKE);
                if (!this.isShapeThemingEnabled) {
                    Paint paint2 = this.chipPaint;
                    ColorFilter colorFilter2 = this.colorFilter;
                    if (colorFilter2 == null) {
                        colorFilter2 = this.tintFilter;
                    }
                    paint2.setColorFilter(colorFilter2);
                }
                RectF rectF = this.rectF;
                float f = this.chipStrokeWidth / 2.0f;
                rectF.set(bounds.left + f, bounds.top + f, bounds.right - f, bounds.bottom - f);
                float f2 = this.chipCornerRadius - (this.chipStrokeWidth / 2.0f);
                canvas.drawRoundRect(this.rectF, f2, f2, this.chipPaint);
            }
            this.chipPaint.setColor(this.currentCompatRippleColor);
            this.chipPaint.setStyle(Paint.Style.FILL);
            this.rectF.set(bounds);
            if (!this.isShapeThemingEnabled) {
                canvas.drawRoundRect(this.rectF, getChipCornerRadius(), getChipCornerRadius(), this.chipPaint);
            } else {
                RectF rectF2 = new RectF(bounds);
                Path path = this.shapePath;
                ShapeAppearancePathProvider shapeAppearancePathProvider = this.pathProvider;
                MaterialShapeDrawable.MaterialShapeDrawableState materialShapeDrawableState = this.drawableState;
                shapeAppearancePathProvider.calculatePath(materialShapeDrawableState.shapeAppearanceModel, materialShapeDrawableState.interpolation, rectF2, this.pathShadowListener, path);
                drawShape(canvas, this.chipPaint, this.shapePath, this.drawableState.shapeAppearanceModel, getBoundsAsRectF());
            }
            if (showsChipIcon()) {
                calculateChipIconBounds(bounds, this.rectF);
                RectF rectF3 = this.rectF;
                float f3 = rectF3.left;
                float f4 = rectF3.top;
                canvas.translate(f3, f4);
                this.chipIcon.setBounds(0, 0, (int) this.rectF.width(), (int) this.rectF.height());
                this.chipIcon.draw(canvas);
                canvas.translate(-f3, -f4);
            }
            if (showsCheckedIcon()) {
                calculateChipIconBounds(bounds, this.rectF);
                RectF rectF4 = this.rectF;
                float f5 = rectF4.left;
                float f6 = rectF4.top;
                canvas.translate(f5, f6);
                this.checkedIcon.setBounds(0, 0, (int) this.rectF.width(), (int) this.rectF.height());
                this.checkedIcon.draw(canvas);
                canvas.translate(-f5, -f6);
            }
            if (!this.shouldDrawText || this.text == null) {
                i3 = i2;
                i4 = 0;
                i5 = 255;
            } else {
                PointF pointF = this.pointF;
                pointF.set(0.0f, 0.0f);
                Paint.Align align = Paint.Align.LEFT;
                if (this.text != null) {
                    float calculateChipIconWidth = calculateChipIconWidth() + this.chipStartPadding + this.textStartPadding;
                    if (getLayoutDirection() == 0) {
                        pointF.x = bounds.left + calculateChipIconWidth;
                        align = Paint.Align.LEFT;
                    } else {
                        pointF.x = bounds.right - calculateChipIconWidth;
                        align = Paint.Align.RIGHT;
                    }
                    TextDrawableHelper textDrawableHelper = this.textDrawableHelper;
                    Objects.requireNonNull(textDrawableHelper);
                    textDrawableHelper.textPaint.getFontMetrics(this.fontMetrics);
                    Paint.FontMetrics fontMetrics = this.fontMetrics;
                    pointF.y = bounds.centerY() - ((fontMetrics.descent + fontMetrics.ascent) / 2.0f);
                }
                RectF rectF5 = this.rectF;
                rectF5.setEmpty();
                if (this.text != null) {
                    float calculateChipIconWidth2 = calculateChipIconWidth() + this.chipStartPadding + this.textStartPadding;
                    float calculateCloseIconWidth = calculateCloseIconWidth() + this.chipEndPadding + this.textEndPadding;
                    if (getLayoutDirection() == 0) {
                        rectF5.left = bounds.left + calculateChipIconWidth2;
                        rectF5.right = bounds.right - calculateCloseIconWidth;
                    } else {
                        rectF5.left = bounds.left + calculateCloseIconWidth;
                        rectF5.right = bounds.right - calculateChipIconWidth2;
                    }
                    rectF5.top = bounds.top;
                    rectF5.bottom = bounds.bottom;
                }
                TextDrawableHelper textDrawableHelper2 = this.textDrawableHelper;
                Objects.requireNonNull(textDrawableHelper2);
                if (textDrawableHelper2.textAppearance != null) {
                    TextDrawableHelper textDrawableHelper3 = this.textDrawableHelper;
                    Objects.requireNonNull(textDrawableHelper3);
                    textDrawableHelper3.textPaint.drawableState = getState();
                    TextDrawableHelper textDrawableHelper4 = this.textDrawableHelper;
                    Context context = this.context;
                    Objects.requireNonNull(textDrawableHelper4);
                    textDrawableHelper4.textAppearance.updateDrawState(context, textDrawableHelper4.textPaint, textDrawableHelper4.fontCallback);
                }
                TextDrawableHelper textDrawableHelper5 = this.textDrawableHelper;
                Objects.requireNonNull(textDrawableHelper5);
                textDrawableHelper5.textPaint.setTextAlign(align);
                if (Math.round(this.textDrawableHelper.getTextWidth(this.text.toString())) > Math.round(this.rectF.width())) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    i6 = canvas.save();
                    canvas.clipRect(this.rectF);
                } else {
                    i6 = 0;
                }
                CharSequence charSequence = this.text;
                if (z && this.truncateAt != null) {
                    TextDrawableHelper textDrawableHelper6 = this.textDrawableHelper;
                    Objects.requireNonNull(textDrawableHelper6);
                    charSequence = TextUtils.ellipsize(charSequence, textDrawableHelper6.textPaint, this.rectF.width(), this.truncateAt);
                }
                int length = charSequence.length();
                PointF pointF2 = this.pointF;
                float f7 = pointF2.x;
                float f8 = pointF2.y;
                TextDrawableHelper textDrawableHelper7 = this.textDrawableHelper;
                Objects.requireNonNull(textDrawableHelper7);
                i3 = i2;
                i4 = 0;
                i5 = 255;
                canvas.drawText(charSequence, 0, length, f7, f8, textDrawableHelper7.textPaint);
                if (z) {
                    canvas.restoreToCount(i6);
                }
            }
            if (showsCloseIcon()) {
                RectF rectF6 = this.rectF;
                rectF6.setEmpty();
                if (showsCloseIcon()) {
                    float f9 = this.chipEndPadding + this.closeIconEndPadding;
                    if (getLayoutDirection() == 0) {
                        float f10 = bounds.right - f9;
                        rectF6.right = f10;
                        rectF6.left = f10 - this.closeIconSize;
                    } else {
                        float f11 = bounds.left + f9;
                        rectF6.left = f11;
                        rectF6.right = f11 + this.closeIconSize;
                    }
                    float exactCenterY = bounds.exactCenterY();
                    float f12 = this.closeIconSize;
                    float f13 = exactCenterY - (f12 / 2.0f);
                    rectF6.top = f13;
                    rectF6.bottom = f13 + f12;
                }
                RectF rectF7 = this.rectF;
                float f14 = rectF7.left;
                float f15 = rectF7.top;
                canvas.translate(f14, f15);
                this.closeIcon.setBounds(i4, i4, (int) this.rectF.width(), (int) this.rectF.height());
                this.closeIconRipple.setBounds(this.closeIcon.getBounds());
                this.closeIconRipple.jumpToCurrentState();
                this.closeIconRipple.draw(canvas);
                canvas.translate(-f14, -f15);
            }
            if (this.alpha < i5) {
                canvas.restoreToCount(i3);
            }
        }
    }

    public final float getChipCornerRadius() {
        if (this.isShapeThemingEnabled) {
            return getTopLeftCornerResolvedSize();
        }
        return this.chipCornerRadius;
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicHeight() {
        return (int) this.chipMinHeight;
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicWidth() {
        return Math.min(Math.round(calculateCloseIconWidth() + this.textDrawableHelper.getTextWidth(this.text.toString()) + calculateChipIconWidth() + this.chipStartPadding + this.textStartPadding + this.textEndPadding + this.chipEndPadding), this.maxWidth);
    }

    @Override // com.google.android.material.shape.MaterialShapeDrawable, android.graphics.drawable.Drawable
    @TargetApi(21)
    public final void getOutline(Outline outline) {
        if (this.isShapeThemingEnabled) {
            super.getOutline(outline);
            return;
        }
        Rect bounds = getBounds();
        if (!bounds.isEmpty()) {
            outline.setRoundRect(bounds, this.chipCornerRadius);
        } else {
            outline.setRoundRect(0, 0, getIntrinsicWidth(), (int) this.chipMinHeight, this.chipCornerRadius);
        }
        outline.setAlpha(this.alpha / 255.0f);
    }

    public final void onSizeChange() {
        Delegate delegate = this.delegate.get();
        if (delegate != null) {
            delegate.onChipDrawableSizeChange();
        }
    }

    @Override // com.google.android.material.shape.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        if (this.alpha != i) {
            this.alpha = i;
            invalidateSelf();
        }
    }

    public final void setCheckedIconVisible(boolean z) {
        boolean z2;
        if (this.checkedIconVisible != z) {
            boolean showsCheckedIcon = showsCheckedIcon();
            this.checkedIconVisible = z;
            boolean showsCheckedIcon2 = showsCheckedIcon();
            if (showsCheckedIcon != showsCheckedIcon2) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                if (showsCheckedIcon2) {
                    applyChildDrawable(this.checkedIcon);
                } else {
                    unapplyChildDrawable(this.checkedIcon);
                }
                invalidateSelf();
                onSizeChange();
            }
        }
    }

    public final void setChipIconVisible(boolean z) {
        boolean z2;
        if (this.chipIconVisible != z) {
            boolean showsChipIcon = showsChipIcon();
            this.chipIconVisible = z;
            boolean showsChipIcon2 = showsChipIcon();
            if (showsChipIcon != showsChipIcon2) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                if (showsChipIcon2) {
                    applyChildDrawable(this.chipIcon);
                } else {
                    unapplyChildDrawable(this.chipIcon);
                }
                invalidateSelf();
                onSizeChange();
            }
        }
    }

    public final void setCloseIconVisible(boolean z) {
        boolean z2;
        if (this.closeIconVisible != z) {
            boolean showsCloseIcon = showsCloseIcon();
            this.closeIconVisible = z;
            boolean showsCloseIcon2 = showsCloseIcon();
            if (showsCloseIcon != showsCloseIcon2) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                if (showsCloseIcon2) {
                    applyChildDrawable(this.closeIcon);
                } else {
                    unapplyChildDrawable(this.closeIcon);
                }
                invalidateSelf();
                onSizeChange();
            }
        }
    }

    @Override // com.google.android.material.shape.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
        if (this.colorFilter != colorFilter) {
            this.colorFilter = colorFilter;
            invalidateSelf();
        }
    }

    public final void setText(CharSequence charSequence) {
        if (charSequence == null) {
            charSequence = "";
        }
        if (!TextUtils.equals(this.text, charSequence)) {
            this.text = charSequence;
            TextDrawableHelper textDrawableHelper = this.textDrawableHelper;
            Objects.requireNonNull(textDrawableHelper);
            textDrawableHelper.textWidthDirty = true;
            invalidateSelf();
            onSizeChange();
        }
    }

    @Override // com.google.android.material.shape.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public final void setTintList(ColorStateList colorStateList) {
        if (this.tint != colorStateList) {
            this.tint = colorStateList;
            onStateChange(getState());
        }
    }

    @Override // com.google.android.material.shape.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public final void setTintMode(PorterDuff.Mode mode) {
        PorterDuffColorFilter porterDuffColorFilter;
        if (this.tintMode != mode) {
            this.tintMode = mode;
            ColorStateList colorStateList = this.tint;
            if (colorStateList == null || mode == null) {
                porterDuffColorFilter = null;
            } else {
                porterDuffColorFilter = new PorterDuffColorFilter(colorStateList.getColorForState(getState(), 0), mode);
            }
            this.tintFilter = porterDuffColorFilter;
            invalidateSelf();
        }
    }

    public final boolean showsCheckedIcon() {
        if (!this.checkedIconVisible || this.checkedIcon == null || !this.currentChecked) {
            return false;
        }
        return true;
    }

    public final boolean showsChipIcon() {
        if (!this.chipIconVisible || this.chipIcon == null) {
            return false;
        }
        return true;
    }

    public final boolean showsCloseIcon() {
        if (!this.closeIconVisible || this.closeIcon == null) {
            return false;
        }
        return true;
    }

    public ChipDrawable(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i, 2132018647);
        initializeElevationOverlay(context);
        this.context = context;
        TextDrawableHelper textDrawableHelper = new TextDrawableHelper(this);
        this.textDrawableHelper = textDrawableHelper;
        textDrawableHelper.textPaint.density = context.getResources().getDisplayMetrics().density;
        int[] iArr = DEFAULT_STATE;
        setState(iArr);
        if (!Arrays.equals(this.closeIconStateSet, iArr)) {
            this.closeIconStateSet = iArr;
            if (showsCloseIcon()) {
                onStateChange(getState(), iArr);
            }
        }
        this.shouldDrawText = true;
        closeIconRippleMask.setTint(-1);
    }

    public final void calculateChipIconBounds(Rect rect, RectF rectF) {
        Drawable drawable;
        Drawable drawable2;
        rectF.setEmpty();
        if (showsChipIcon() || showsCheckedIcon()) {
            float f = this.chipStartPadding + this.iconStartPadding;
            if (this.currentChecked) {
                drawable = this.checkedIcon;
            } else {
                drawable = this.chipIcon;
            }
            float f2 = this.chipIconSize;
            if (f2 <= 0.0f && drawable != null) {
                f2 = drawable.getIntrinsicWidth();
            }
            if (getLayoutDirection() == 0) {
                float f3 = rect.left + f;
                rectF.left = f3;
                rectF.right = f3 + f2;
            } else {
                float f4 = rect.right - f;
                rectF.right = f4;
                rectF.left = f4 - f2;
            }
            if (this.currentChecked) {
                drawable2 = this.checkedIcon;
            } else {
                drawable2 = this.chipIcon;
            }
            float f5 = this.chipIconSize;
            if (f5 <= 0.0f && drawable2 != null) {
                float ceil = (float) Math.ceil(ViewUtils.dpToPx(this.context, 24));
                if (drawable2.getIntrinsicHeight() <= ceil) {
                    ceil = drawable2.getIntrinsicHeight();
                }
                f5 = ceil;
            }
            float exactCenterY = rect.exactCenterY() - (f5 / 2.0f);
            rectF.top = exactCenterY;
            rectF.bottom = exactCenterY + f5;
        }
    }

    public final float calculateChipIconWidth() {
        Drawable drawable;
        if (!showsChipIcon() && !showsCheckedIcon()) {
            return 0.0f;
        }
        float f = this.iconStartPadding;
        if (this.currentChecked) {
            drawable = this.checkedIcon;
        } else {
            drawable = this.chipIcon;
        }
        float f2 = this.chipIconSize;
        if (f2 <= 0.0f && drawable != null) {
            f2 = drawable.getIntrinsicWidth();
        }
        return f2 + f + this.iconEndPadding;
    }

    public final float calculateCloseIconWidth() {
        if (showsCloseIcon()) {
            return this.closeIconStartPadding + this.closeIconSize + this.closeIconEndPadding;
        }
        return 0.0f;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void invalidateDrawable(Drawable drawable) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean onLayoutDirectionChanged(int i) {
        boolean onLayoutDirectionChanged = super.onLayoutDirectionChanged(i);
        if (showsChipIcon()) {
            onLayoutDirectionChanged |= this.chipIcon.setLayoutDirection(i);
        }
        if (showsCheckedIcon()) {
            onLayoutDirectionChanged |= this.checkedIcon.setLayoutDirection(i);
        }
        if (showsCloseIcon()) {
            onLayoutDirectionChanged |= this.closeIcon.setLayoutDirection(i);
        }
        if (!onLayoutDirectionChanged) {
            return true;
        }
        invalidateSelf();
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean onLevelChange(int i) {
        boolean onLevelChange = super.onLevelChange(i);
        if (showsChipIcon()) {
            onLevelChange |= this.chipIcon.setLevel(i);
        }
        if (showsCheckedIcon()) {
            onLevelChange |= this.checkedIcon.setLevel(i);
        }
        if (showsCloseIcon()) {
            onLevelChange |= this.closeIcon.setLevel(i);
        }
        if (onLevelChange) {
            invalidateSelf();
        }
        return onLevelChange;
    }

    @Override // com.google.android.material.internal.TextDrawableHelper.TextDrawableDelegate
    public final void onTextSizeChange() {
        onSizeChange();
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, runnable, j);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean setVisible(boolean z, boolean z2) {
        boolean visible = super.setVisible(z, z2);
        if (showsChipIcon()) {
            visible |= this.chipIcon.setVisible(z, z2);
        }
        if (showsCheckedIcon()) {
            visible |= this.checkedIcon.setVisible(z, z2);
        }
        if (showsCloseIcon()) {
            visible |= this.closeIcon.setVisible(z, z2);
        }
        if (visible) {
            invalidateSelf();
        }
        return visible;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, runnable);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x0168  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0171  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0176  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00c3  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0124  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x014b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onStateChange(int[] r9, int[] r10) {
        /*
            Method dump skipped, instructions count: 378
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.chip.ChipDrawable.onStateChange(int[], int[]):boolean");
    }

    public static boolean isStateful(ColorStateList colorStateList) {
        return colorStateList != null && colorStateList.isStateful();
    }

    public static boolean isStateful(Drawable drawable) {
        return drawable != null && drawable.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public final int getAlpha() {
        return this.alpha;
    }

    @Override // android.graphics.drawable.Drawable
    public final ColorFilter getColorFilter() {
        return this.colorFilter;
    }
}
