package com.google.android.material.card;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import androidx.cardview.widget.CardView;
import androidx.cardview.widget.RoundRectDrawable;
import androidx.leanback.R$drawable;
import com.google.android.material.R$styleable;
import com.google.android.material.shape.CutCornerTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MaterialCardViewHelper {
    public static final double COS_45 = Math.cos(Math.toRadians(45.0d));
    public final MaterialShapeDrawable bgDrawable;
    public boolean checkable;
    public Drawable checkedIcon;
    public int checkedIconMargin;
    public int checkedIconSize;
    public ColorStateList checkedIconTint;
    public LayerDrawable clickableForegroundDrawable;
    public Drawable fgDrawable;
    public final MaterialShapeDrawable foregroundContentDrawable;
    public MaterialShapeDrawable foregroundShapeDrawable;
    public final MaterialCardView materialCardView;
    public ColorStateList rippleColor;
    public RippleDrawable rippleDrawable;
    public ShapeAppearanceModel shapeAppearanceModel;
    public ColorStateList strokeColor;
    public int strokeWidth;
    public final Rect userContentPadding = new Rect();
    public boolean isBackgroundOverwritten = false;

    public static float calculateCornerPaddingForCornerTreatment(R$drawable r$drawable, float f) {
        if (r$drawable instanceof RoundedCornerTreatment) {
            return (float) ((1.0d - COS_45) * f);
        }
        if (r$drawable instanceof CutCornerTreatment) {
            return f / 2.0f;
        }
        return 0.0f;
    }

    public final float calculateActualCornerPadding() {
        ShapeAppearanceModel shapeAppearanceModel = this.shapeAppearanceModel;
        Objects.requireNonNull(shapeAppearanceModel);
        float calculateCornerPaddingForCornerTreatment = calculateCornerPaddingForCornerTreatment(shapeAppearanceModel.topLeftCorner, this.bgDrawable.getTopLeftCornerResolvedSize());
        ShapeAppearanceModel shapeAppearanceModel2 = this.shapeAppearanceModel;
        Objects.requireNonNull(shapeAppearanceModel2);
        R$drawable r$drawable = shapeAppearanceModel2.topRightCorner;
        MaterialShapeDrawable materialShapeDrawable = this.bgDrawable;
        Objects.requireNonNull(materialShapeDrawable);
        ShapeAppearanceModel shapeAppearanceModel3 = materialShapeDrawable.drawableState.shapeAppearanceModel;
        Objects.requireNonNull(shapeAppearanceModel3);
        float max = Math.max(calculateCornerPaddingForCornerTreatment, calculateCornerPaddingForCornerTreatment(r$drawable, shapeAppearanceModel3.topRightCornerSize.getCornerSize(materialShapeDrawable.getBoundsAsRectF())));
        ShapeAppearanceModel shapeAppearanceModel4 = this.shapeAppearanceModel;
        Objects.requireNonNull(shapeAppearanceModel4);
        R$drawable r$drawable2 = shapeAppearanceModel4.bottomRightCorner;
        MaterialShapeDrawable materialShapeDrawable2 = this.bgDrawable;
        Objects.requireNonNull(materialShapeDrawable2);
        ShapeAppearanceModel shapeAppearanceModel5 = materialShapeDrawable2.drawableState.shapeAppearanceModel;
        Objects.requireNonNull(shapeAppearanceModel5);
        float calculateCornerPaddingForCornerTreatment2 = calculateCornerPaddingForCornerTreatment(r$drawable2, shapeAppearanceModel5.bottomRightCornerSize.getCornerSize(materialShapeDrawable2.getBoundsAsRectF()));
        ShapeAppearanceModel shapeAppearanceModel6 = this.shapeAppearanceModel;
        Objects.requireNonNull(shapeAppearanceModel6);
        R$drawable r$drawable3 = shapeAppearanceModel6.bottomLeftCorner;
        MaterialShapeDrawable materialShapeDrawable3 = this.bgDrawable;
        Objects.requireNonNull(materialShapeDrawable3);
        ShapeAppearanceModel shapeAppearanceModel7 = materialShapeDrawable3.drawableState.shapeAppearanceModel;
        Objects.requireNonNull(shapeAppearanceModel7);
        return Math.max(max, Math.max(calculateCornerPaddingForCornerTreatment2, calculateCornerPaddingForCornerTreatment(r$drawable3, shapeAppearanceModel7.bottomLeftCornerSize.getCornerSize(materialShapeDrawable3.getBoundsAsRectF()))));
    }

    public final LayerDrawable getClickableForeground() {
        if (this.rippleDrawable == null) {
            this.foregroundShapeDrawable = new MaterialShapeDrawable(this.shapeAppearanceModel);
            this.rippleDrawable = new RippleDrawable(this.rippleColor, null, this.foregroundShapeDrawable);
        }
        if (this.clickableForegroundDrawable == null) {
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{this.rippleDrawable, this.foregroundContentDrawable, this.checkedIcon});
            this.clickableForegroundDrawable = layerDrawable;
            layerDrawable.setId(2, 2131428458);
        }
        return this.clickableForegroundDrawable;
    }

    /* JADX WARN: Type inference failed for: r8v1, types: [com.google.android.material.card.MaterialCardViewHelper$1] */
    public final AnonymousClass1 insetDrawable(Drawable drawable) {
        int i;
        int i2;
        float f;
        MaterialCardView materialCardView = this.materialCardView;
        Objects.requireNonNull(materialCardView);
        if (materialCardView.mCompatPadding) {
            MaterialCardView materialCardView2 = this.materialCardView;
            Objects.requireNonNull(materialCardView2);
            CardView.AnonymousClass1 r0 = materialCardView2.mCardViewDelegate;
            Objects.requireNonNull(r0);
            RoundRectDrawable roundRectDrawable = (RoundRectDrawable) r0.mCardBackground;
            Objects.requireNonNull(roundRectDrawable);
            float f2 = roundRectDrawable.mPadding * 1.5f;
            float f3 = 0.0f;
            if (shouldAddCornerPaddingOutsideCardBackground()) {
                f = calculateActualCornerPadding();
            } else {
                f = 0.0f;
            }
            int ceil = (int) Math.ceil(f2 + f);
            MaterialCardView materialCardView3 = this.materialCardView;
            Objects.requireNonNull(materialCardView3);
            CardView.AnonymousClass1 r02 = materialCardView3.mCardViewDelegate;
            Objects.requireNonNull(r02);
            RoundRectDrawable roundRectDrawable2 = (RoundRectDrawable) r02.mCardBackground;
            Objects.requireNonNull(roundRectDrawable2);
            float f4 = roundRectDrawable2.mPadding;
            if (shouldAddCornerPaddingOutsideCardBackground()) {
                f3 = calculateActualCornerPadding();
            }
            i2 = (int) Math.ceil(f4 + f3);
            i = ceil;
        } else {
            i2 = 0;
            i = 0;
        }
        return new InsetDrawable(drawable, i2, i, i2, i) { // from class: com.google.android.material.card.MaterialCardViewHelper.1
            @Override // android.graphics.drawable.Drawable
            public final int getMinimumHeight() {
                return -1;
            }

            @Override // android.graphics.drawable.Drawable
            public final int getMinimumWidth() {
                return -1;
            }

            @Override // android.graphics.drawable.InsetDrawable, android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
            public final boolean getPadding(Rect rect) {
                return false;
            }
        };
    }

    public final void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.shapeAppearanceModel = shapeAppearanceModel;
        this.bgDrawable.setShapeAppearanceModel(shapeAppearanceModel);
        MaterialShapeDrawable materialShapeDrawable = this.bgDrawable;
        materialShapeDrawable.shadowBitmapDrawingEnable = !materialShapeDrawable.isRoundRect();
        MaterialShapeDrawable materialShapeDrawable2 = this.foregroundContentDrawable;
        if (materialShapeDrawable2 != null) {
            materialShapeDrawable2.setShapeAppearanceModel(shapeAppearanceModel);
        }
        MaterialShapeDrawable materialShapeDrawable3 = this.foregroundShapeDrawable;
        if (materialShapeDrawable3 != null) {
            materialShapeDrawable3.setShapeAppearanceModel(shapeAppearanceModel);
        }
    }

    public final boolean shouldAddCornerPaddingOutsideCardBackground() {
        MaterialCardView materialCardView = this.materialCardView;
        Objects.requireNonNull(materialCardView);
        if (materialCardView.mPreventCornerOverlap && this.bgDrawable.isRoundRect()) {
            MaterialCardView materialCardView2 = this.materialCardView;
            Objects.requireNonNull(materialCardView2);
            if (materialCardView2.mCompatPadding) {
                return true;
            }
        }
        return false;
    }

    public final void updateContentPadding() {
        boolean z;
        float f;
        RoundRectDrawable roundRectDrawable;
        MaterialCardView materialCardView = this.materialCardView;
        Objects.requireNonNull(materialCardView);
        boolean z2 = true;
        if (!materialCardView.mPreventCornerOverlap || this.bgDrawable.isRoundRect()) {
            z = false;
        } else {
            z = true;
        }
        if (!z && !shouldAddCornerPaddingOutsideCardBackground()) {
            z2 = false;
        }
        float f2 = 0.0f;
        if (z2) {
            f = calculateActualCornerPadding();
        } else {
            f = 0.0f;
        }
        MaterialCardView materialCardView2 = this.materialCardView;
        Objects.requireNonNull(materialCardView2);
        if (materialCardView2.mPreventCornerOverlap) {
            MaterialCardView materialCardView3 = this.materialCardView;
            Objects.requireNonNull(materialCardView3);
            if (materialCardView3.mCompatPadding) {
                MaterialCardView materialCardView4 = this.materialCardView;
                Objects.requireNonNull(materialCardView4);
                CardView.AnonymousClass1 r0 = materialCardView4.mCardViewDelegate;
                Objects.requireNonNull(r0);
                Objects.requireNonNull((RoundRectDrawable) r0.mCardBackground);
                f2 = (float) ((1.0d - COS_45) * roundRectDrawable.mRadius);
            }
        }
        int i = (int) (f - f2);
        MaterialCardView materialCardView5 = this.materialCardView;
        Rect rect = this.userContentPadding;
        int i2 = rect.left + i;
        int i3 = rect.top + i;
        int i4 = rect.right + i;
        int i5 = rect.bottom + i;
        Objects.requireNonNull(materialCardView5);
        materialCardView5.mContentPadding.set(i2, i3, i4, i5);
        CardView.IMPL.updatePadding(materialCardView5.mCardViewDelegate);
    }

    public MaterialCardViewHelper(MaterialCardView materialCardView, AttributeSet attributeSet) {
        this.materialCardView = materialCardView;
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(materialCardView.getContext(), attributeSet, 2130969434, 2132018645);
        this.bgDrawable = materialShapeDrawable;
        materialShapeDrawable.initializeElevationOverlay(materialCardView.getContext());
        materialShapeDrawable.setShadowColor();
        ShapeAppearanceModel shapeAppearanceModel = materialShapeDrawable.drawableState.shapeAppearanceModel;
        Objects.requireNonNull(shapeAppearanceModel);
        ShapeAppearanceModel.Builder builder = new ShapeAppearanceModel.Builder(shapeAppearanceModel);
        TypedArray obtainStyledAttributes = materialCardView.getContext().obtainStyledAttributes(attributeSet, R$styleable.CardView, 2130969434, 2132017462);
        if (obtainStyledAttributes.hasValue(3)) {
            builder.setAllCornerSizes(obtainStyledAttributes.getDimension(3, 0.0f));
        }
        this.foregroundContentDrawable = new MaterialShapeDrawable();
        setShapeAppearanceModel(new ShapeAppearanceModel(builder));
        obtainStyledAttributes.recycle();
    }
}
