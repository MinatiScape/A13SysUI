package com.google.android.material.tooltip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.shape.MarkerEdgeTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.OffsetEdgeTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TooltipDrawable extends MaterialShapeDrawable implements TextDrawableHelper.TextDrawableDelegate {
    public int arrowSize;
    public final Context context;
    public int layoutMargin;
    public int locationOnScreenX;
    public int minHeight;
    public int minWidth;
    public int padding;
    public CharSequence text;
    public final TextDrawableHelper textDrawableHelper;
    public final Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
    public final AnonymousClass1 attachedViewLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.google.android.material.tooltip.TooltipDrawable.1
        @Override // android.view.View.OnLayoutChangeListener
        public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            TooltipDrawable tooltipDrawable = TooltipDrawable.this;
            Objects.requireNonNull(tooltipDrawable);
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            tooltipDrawable.locationOnScreenX = iArr[0];
            view.getWindowVisibleDisplayFrame(tooltipDrawable.displayFrame);
        }
    };
    public final Rect displayFrame = new Rect();
    public float tooltipScaleX = 1.0f;
    public float tooltipScaleY = 1.0f;
    public float tooltipPivotY = 0.5f;
    public float labelOpacity = 1.0f;

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.material.tooltip.TooltipDrawable$1] */
    public TooltipDrawable(Context context, int i) {
        super(context, null, 0, i);
        TextDrawableHelper textDrawableHelper = new TextDrawableHelper(this);
        this.textDrawableHelper = textDrawableHelper;
        this.context = context;
        textDrawableHelper.textPaint.density = context.getResources().getDisplayMetrics().density;
        textDrawableHelper.textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public final float calculatePointerOffset() {
        int i;
        if (((this.displayFrame.right - getBounds().right) - this.locationOnScreenX) - this.layoutMargin < 0) {
            i = ((this.displayFrame.right - getBounds().right) - this.locationOnScreenX) - this.layoutMargin;
        } else if (((this.displayFrame.left - getBounds().left) - this.locationOnScreenX) + this.layoutMargin <= 0) {
            return 0.0f;
        } else {
            i = ((this.displayFrame.left - getBounds().left) - this.locationOnScreenX) + this.layoutMargin;
        }
        return i;
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicHeight() {
        TextDrawableHelper textDrawableHelper = this.textDrawableHelper;
        Objects.requireNonNull(textDrawableHelper);
        return (int) Math.max(textDrawableHelper.textPaint.getTextSize(), this.minHeight);
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicWidth() {
        float f;
        float f2 = this.padding * 2;
        CharSequence charSequence = this.text;
        if (charSequence == null) {
            f = 0.0f;
        } else {
            f = this.textDrawableHelper.getTextWidth(charSequence.toString());
        }
        return (int) Math.max(f2 + f, this.minWidth);
    }

    public final OffsetEdgeTreatment createMarkerEdge() {
        float width = ((float) (getBounds().width() - (Math.sqrt(2.0d) * this.arrowSize))) / 2.0f;
        return new OffsetEdgeTreatment(new MarkerEdgeTreatment(this.arrowSize), Math.min(Math.max(-calculatePointerOffset(), -width), width));
    }

    @Override // com.google.android.material.shape.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        canvas.save();
        float calculatePointerOffset = calculatePointerOffset();
        double sqrt = Math.sqrt(2.0d);
        canvas.scale(this.tooltipScaleX, this.tooltipScaleY, (getBounds().width() * 0.5f) + getBounds().left, (getBounds().height() * this.tooltipPivotY) + getBounds().top);
        canvas.translate(calculatePointerOffset, (float) (-((sqrt * this.arrowSize) - this.arrowSize)));
        super.draw(canvas);
        if (this.text != null) {
            Rect bounds = getBounds();
            TextDrawableHelper textDrawableHelper = this.textDrawableHelper;
            Objects.requireNonNull(textDrawableHelper);
            textDrawableHelper.textPaint.getFontMetrics(this.fontMetrics);
            Paint.FontMetrics fontMetrics = this.fontMetrics;
            int centerY = (int) (bounds.centerY() - ((fontMetrics.descent + fontMetrics.ascent) / 2.0f));
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
                TextDrawableHelper textDrawableHelper5 = this.textDrawableHelper;
                Objects.requireNonNull(textDrawableHelper5);
                textDrawableHelper5.textPaint.setAlpha((int) (this.labelOpacity * 255.0f));
            }
            CharSequence charSequence = this.text;
            TextDrawableHelper textDrawableHelper6 = this.textDrawableHelper;
            Objects.requireNonNull(textDrawableHelper6);
            canvas.drawText(charSequence, 0, charSequence.length(), bounds.centerX(), centerY, textDrawableHelper6.textPaint);
        }
        canvas.restore();
    }

    @Override // com.google.android.material.shape.MaterialShapeDrawable, android.graphics.drawable.Drawable
    public final void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        ShapeAppearanceModel shapeAppearanceModel = this.drawableState.shapeAppearanceModel;
        Objects.requireNonNull(shapeAppearanceModel);
        ShapeAppearanceModel.Builder builder = new ShapeAppearanceModel.Builder(shapeAppearanceModel);
        builder.bottomEdge = createMarkerEdge();
        setShapeAppearanceModel(new ShapeAppearanceModel(builder));
    }

    @Override // com.google.android.material.shape.MaterialShapeDrawable, android.graphics.drawable.Drawable, com.google.android.material.internal.TextDrawableHelper.TextDrawableDelegate
    public final boolean onStateChange(int[] iArr) {
        return super.onStateChange(iArr);
    }

    @Override // com.google.android.material.internal.TextDrawableHelper.TextDrawableDelegate
    public final void onTextSizeChange() {
        invalidateSelf();
    }
}
