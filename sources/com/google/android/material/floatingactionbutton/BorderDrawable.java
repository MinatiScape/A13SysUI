package com.google.android.material.floatingactionbutton;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.ShapeAppearancePathProvider;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BorderDrawable extends Drawable {
    public ColorStateList borderTint;
    public float borderWidth;
    public int bottomInnerStrokeColor;
    public int bottomOuterStrokeColor;
    public int currentBorderTintColor;
    public final Paint paint;
    public ShapeAppearanceModel shapeAppearanceModel;
    public int topInnerStrokeColor;
    public int topOuterStrokeColor;
    public final ShapeAppearancePathProvider pathProvider = ShapeAppearancePathProvider.Lazy.INSTANCE;
    public final Path shapePath = new Path();
    public final Rect rect = new Rect();
    public final RectF rectF = new RectF();
    public final RectF boundsRectF = new RectF();
    public final BorderState state = new BorderState();
    public boolean invalidateShader = true;

    /* loaded from: classes.dex */
    public class BorderState extends Drawable.ConstantState {
        @Override // android.graphics.drawable.Drawable.ConstantState
        public final int getChangingConfigurations() {
            return 0;
        }

        public BorderState() {
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final Drawable newDrawable() {
            return BorderDrawable.this;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final void onBoundsChange(Rect rect) {
        this.invalidateShader = true;
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        if (this.invalidateShader) {
            Paint paint = this.paint;
            Rect rect = this.rect;
            copyBounds(rect);
            float height = this.borderWidth / rect.height();
            paint.setShader(new LinearGradient(0.0f, rect.top, 0.0f, rect.bottom, new int[]{ColorUtils.compositeColors(this.topOuterStrokeColor, this.currentBorderTintColor), ColorUtils.compositeColors(this.topInnerStrokeColor, this.currentBorderTintColor), ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.topInnerStrokeColor, 0), this.currentBorderTintColor), ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.bottomInnerStrokeColor, 0), this.currentBorderTintColor), ColorUtils.compositeColors(this.bottomInnerStrokeColor, this.currentBorderTintColor), ColorUtils.compositeColors(this.bottomOuterStrokeColor, this.currentBorderTintColor)}, new float[]{0.0f, height, 0.5f, 0.5f, 1.0f - height, 1.0f}, Shader.TileMode.CLAMP));
            this.invalidateShader = false;
        }
        float strokeWidth = this.paint.getStrokeWidth() / 2.0f;
        copyBounds(this.rect);
        this.rectF.set(this.rect);
        ShapeAppearanceModel shapeAppearanceModel = this.shapeAppearanceModel;
        Objects.requireNonNull(shapeAppearanceModel);
        float min = Math.min(shapeAppearanceModel.topLeftCornerSize.getCornerSize(getBoundsAsRectF()), this.rectF.width() / 2.0f);
        if (this.shapeAppearanceModel.isRoundRect(getBoundsAsRectF())) {
            this.rectF.inset(strokeWidth, strokeWidth);
            canvas.drawRoundRect(this.rectF, min, min, this.paint);
        }
    }

    public final RectF getBoundsAsRectF() {
        this.boundsRectF.set(getBounds());
        return this.boundsRectF;
    }

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        if (this.borderWidth > 0.0f) {
            return -3;
        }
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    @TargetApi(21)
    public final void getOutline(Outline outline) {
        if (this.shapeAppearanceModel.isRoundRect(getBoundsAsRectF())) {
            ShapeAppearanceModel shapeAppearanceModel = this.shapeAppearanceModel;
            Objects.requireNonNull(shapeAppearanceModel);
            outline.setRoundRect(getBounds(), shapeAppearanceModel.topLeftCornerSize.getCornerSize(getBoundsAsRectF()));
            return;
        }
        copyBounds(this.rect);
        this.rectF.set(this.rect);
        ShapeAppearancePathProvider shapeAppearancePathProvider = this.pathProvider;
        ShapeAppearanceModel shapeAppearanceModel2 = this.shapeAppearanceModel;
        RectF rectF = this.rectF;
        Path path = this.shapePath;
        Objects.requireNonNull(shapeAppearancePathProvider);
        shapeAppearancePathProvider.calculatePath(shapeAppearanceModel2, 1.0f, rectF, null, path);
        if (this.shapePath.isConvex()) {
            outline.setConvexPath(this.shapePath);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean getPadding(Rect rect) {
        if (!this.shapeAppearanceModel.isRoundRect(getBoundsAsRectF())) {
            return true;
        }
        int round = Math.round(this.borderWidth);
        rect.set(round, round, round, round);
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean isStateful() {
        ColorStateList colorStateList = this.borderTint;
        if ((colorStateList == null || !colorStateList.isStateful()) && !super.isStateful()) {
            return false;
        }
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean onStateChange(int[] iArr) {
        int colorForState;
        ColorStateList colorStateList = this.borderTint;
        if (!(colorStateList == null || (colorForState = colorStateList.getColorForState(iArr, this.currentBorderTintColor)) == this.currentBorderTintColor)) {
            this.invalidateShader = true;
            this.currentBorderTintColor = colorForState;
        }
        if (this.invalidateShader) {
            invalidateSelf();
        }
        return this.invalidateShader;
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        this.paint.setAlpha(i);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public BorderDrawable(ShapeAppearanceModel shapeAppearanceModel) {
        this.shapeAppearanceModel = shapeAppearanceModel;
        Paint paint = new Paint(1);
        this.paint = paint;
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override // android.graphics.drawable.Drawable
    public final Drawable.ConstantState getConstantState() {
        return this.state;
    }
}
