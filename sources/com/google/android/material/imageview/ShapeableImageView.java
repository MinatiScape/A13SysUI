package com.google.android.material.imageview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.AppCompatImageView;
import com.google.android.material.R$styleable;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.ShapeAppearancePathProvider;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.util.Objects;
/* loaded from: classes.dex */
public class ShapeableImageView extends AppCompatImageView implements Shapeable {
    public final Paint borderPaint;
    public int bottomContentPadding;
    public final Paint clearPaint;
    public final RectF destination;
    public int endContentPadding;
    public boolean hasAdjustedPaddingAfterLayoutDirectionResolved;
    public int leftContentPadding;
    public Path maskPath;
    public final RectF maskRect;
    public final Path path;
    public final ShapeAppearancePathProvider pathProvider;
    public int rightContentPadding;
    public MaterialShapeDrawable shadowDrawable;
    public ShapeAppearanceModel shapeAppearanceModel;
    public int startContentPadding;
    public ColorStateList strokeColor;
    public float strokeWidth;
    public int topContentPadding;

    @TargetApi(21)
    /* loaded from: classes.dex */
    public class OutlineProvider extends ViewOutlineProvider {
        public final Rect rect = new Rect();

        public OutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public final void getOutline(View view, Outline outline) {
            ShapeableImageView shapeableImageView = ShapeableImageView.this;
            if (shapeableImageView.shapeAppearanceModel != null) {
                if (shapeableImageView.shadowDrawable == null) {
                    shapeableImageView.shadowDrawable = new MaterialShapeDrawable(ShapeableImageView.this.shapeAppearanceModel);
                }
                ShapeableImageView.this.destination.round(this.rect);
                ShapeableImageView.this.shadowDrawable.setBounds(this.rect);
                ShapeableImageView.this.shadowDrawable.getOutline(outline);
            }
        }
    }

    public ShapeableImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onDetachedFromWindow() {
        setLayerType(0, null);
        super.onDetachedFromWindow();
    }

    public ShapeableImageView(Context context, AttributeSet attributeSet, int i) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, 0, 2132018701), attributeSet, 0);
        this.pathProvider = ShapeAppearancePathProvider.Lazy.INSTANCE;
        this.path = new Path();
        this.hasAdjustedPaddingAfterLayoutDirectionResolved = false;
        Context context2 = getContext();
        Paint paint = new Paint();
        this.clearPaint = paint;
        paint.setAntiAlias(true);
        paint.setColor(-1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.destination = new RectF();
        this.maskRect = new RectF();
        this.maskPath = new Path();
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, R$styleable.ShapeableImageView, 0, 2132018701);
        this.strokeColor = MaterialResources.getColorStateList(context2, obtainStyledAttributes, 9);
        this.strokeWidth = obtainStyledAttributes.getDimensionPixelSize(10, 0);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(0, 0);
        this.leftContentPadding = dimensionPixelSize;
        this.topContentPadding = dimensionPixelSize;
        this.rightContentPadding = dimensionPixelSize;
        this.bottomContentPadding = dimensionPixelSize;
        this.leftContentPadding = obtainStyledAttributes.getDimensionPixelSize(3, dimensionPixelSize);
        this.topContentPadding = obtainStyledAttributes.getDimensionPixelSize(6, dimensionPixelSize);
        this.rightContentPadding = obtainStyledAttributes.getDimensionPixelSize(4, dimensionPixelSize);
        this.bottomContentPadding = obtainStyledAttributes.getDimensionPixelSize(1, dimensionPixelSize);
        this.startContentPadding = obtainStyledAttributes.getDimensionPixelSize(5, Integer.MIN_VALUE);
        this.endContentPadding = obtainStyledAttributes.getDimensionPixelSize(2, Integer.MIN_VALUE);
        obtainStyledAttributes.recycle();
        Paint paint2 = new Paint();
        this.borderPaint = paint2;
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setAntiAlias(true);
        this.shapeAppearanceModel = new ShapeAppearanceModel(ShapeAppearanceModel.builder(context2, attributeSet, 0, 2132018701));
        setOutlineProvider(new OutlineProvider());
    }

    public final int getContentPaddingLeft() {
        boolean z;
        int i;
        int i2;
        if (this.startContentPadding == Integer.MIN_VALUE && this.endContentPadding == Integer.MIN_VALUE) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            if (isRtl() && (i2 = this.endContentPadding) != Integer.MIN_VALUE) {
                return i2;
            }
            if (!isRtl() && (i = this.startContentPadding) != Integer.MIN_VALUE) {
                return i;
            }
        }
        return this.leftContentPadding;
    }

    public final int getContentPaddingRight() {
        boolean z;
        int i;
        int i2;
        if (this.startContentPadding == Integer.MIN_VALUE && this.endContentPadding == Integer.MIN_VALUE) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            if (isRtl() && (i2 = this.startContentPadding) != Integer.MIN_VALUE) {
                return i2;
            }
            if (!isRtl() && (i = this.endContentPadding) != Integer.MIN_VALUE) {
                return i;
            }
        }
        return this.rightContentPadding;
    }

    @Override // android.view.View
    public final void setPaddingRelative(int i, int i2, int i3, int i4) {
        int i5 = this.startContentPadding;
        if (i5 == Integer.MIN_VALUE) {
            if (isRtl()) {
                i5 = this.rightContentPadding;
            } else {
                i5 = this.leftContentPadding;
            }
        }
        int i6 = i5 + i;
        int i7 = i2 + this.topContentPadding;
        int i8 = this.endContentPadding;
        if (i8 == Integer.MIN_VALUE) {
            if (isRtl()) {
                i8 = this.leftContentPadding;
            } else {
                i8 = this.rightContentPadding;
            }
        }
        super.setPaddingRelative(i6, i7, i8 + i3, i4 + this.bottomContentPadding);
    }

    @Override // com.google.android.material.shape.Shapeable
    public final void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.shapeAppearanceModel = shapeAppearanceModel;
        MaterialShapeDrawable materialShapeDrawable = this.shadowDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setShapeAppearanceModel(shapeAppearanceModel);
        }
        updateShapeMask(getWidth(), getHeight());
        invalidate();
        invalidateOutline();
    }

    public final void updateShapeMask(int i, int i2) {
        this.destination.set(getPaddingLeft(), getPaddingTop(), i - getPaddingRight(), i2 - getPaddingBottom());
        ShapeAppearancePathProvider shapeAppearancePathProvider = this.pathProvider;
        ShapeAppearanceModel shapeAppearanceModel = this.shapeAppearanceModel;
        RectF rectF = this.destination;
        Path path = this.path;
        Objects.requireNonNull(shapeAppearancePathProvider);
        shapeAppearancePathProvider.calculatePath(shapeAppearanceModel, 1.0f, rectF, null, path);
        this.maskPath.rewind();
        this.maskPath.addPath(this.path);
        this.maskRect.set(0.0f, 0.0f, i, i2);
        this.maskPath.addRect(this.maskRect, Path.Direction.CCW);
    }

    @Override // android.view.View
    public final int getPaddingBottom() {
        return super.getPaddingBottom() - this.bottomContentPadding;
    }

    @Override // android.view.View
    public final int getPaddingEnd() {
        int i;
        int paddingEnd = super.getPaddingEnd();
        int i2 = this.endContentPadding;
        if (i2 == Integer.MIN_VALUE) {
            if (isRtl()) {
                i = this.leftContentPadding;
            } else {
                i = this.rightContentPadding;
            }
            i2 = i;
        }
        return paddingEnd - i2;
    }

    @Override // android.view.View
    public final int getPaddingLeft() {
        return super.getPaddingLeft() - getContentPaddingLeft();
    }

    @Override // android.view.View
    public final int getPaddingRight() {
        return super.getPaddingRight() - getContentPaddingRight();
    }

    @Override // android.view.View
    public final int getPaddingStart() {
        int i;
        int paddingStart = super.getPaddingStart();
        int i2 = this.startContentPadding;
        if (i2 == Integer.MIN_VALUE) {
            if (isRtl()) {
                i = this.rightContentPadding;
            } else {
                i = this.leftContentPadding;
            }
            i2 = i;
        }
        return paddingStart - i2;
    }

    @Override // android.view.View
    public final int getPaddingTop() {
        return super.getPaddingTop() - this.topContentPadding;
    }

    public final boolean isRtl() {
        if (getLayoutDirection() == 1) {
            return true;
        }
        return false;
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        setLayerType(2, null);
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(this.maskPath, this.clearPaint);
        if (this.strokeColor != null) {
            this.borderPaint.setStrokeWidth(this.strokeWidth);
            int colorForState = this.strokeColor.getColorForState(getDrawableState(), this.strokeColor.getDefaultColor());
            if (this.strokeWidth > 0.0f && colorForState != 0) {
                this.borderPaint.setColor(colorForState);
                canvas.drawPath(this.path, this.borderPaint);
            }
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (!this.hasAdjustedPaddingAfterLayoutDirectionResolved && isLayoutDirectionResolved()) {
            boolean z = true;
            this.hasAdjustedPaddingAfterLayoutDirectionResolved = true;
            if (!isPaddingRelative()) {
                if (this.startContentPadding == Integer.MIN_VALUE && this.endContentPadding == Integer.MIN_VALUE) {
                    z = false;
                }
                if (!z) {
                    setPadding(super.getPaddingLeft(), super.getPaddingTop(), super.getPaddingRight(), super.getPaddingBottom());
                    return;
                }
            }
            setPaddingRelative(super.getPaddingStart(), super.getPaddingTop(), super.getPaddingEnd(), super.getPaddingBottom());
        }
    }

    @Override // android.view.View
    public final void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        updateShapeMask(i, i2);
    }

    @Override // android.view.View
    public final void setPadding(int i, int i2, int i3, int i4) {
        super.setPadding(getContentPaddingLeft() + i, i2 + this.topContentPadding, getContentPaddingRight() + i3, i4 + this.bottomContentPadding);
    }
}
