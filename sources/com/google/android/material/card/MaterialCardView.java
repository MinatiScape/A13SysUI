package com.google.android.material.card;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import androidx.cardview.widget.CardView;
import androidx.cardview.widget.RoundRectDrawable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.leanback.R$string;
import androidx.mediarouter.R$bool;
import com.google.android.material.R$styleable;
import com.google.android.material.card.MaterialCardViewHelper;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class MaterialCardView extends CardView implements Checkable, Shapeable {
    public static final int[] CHECKABLE_STATE_SET = {16842911};
    public static final int[] CHECKED_STATE_SET = {16842912};
    public final MaterialCardViewHelper cardViewHelper;
    public boolean checked;
    public boolean isParentCardViewDoneInitializing;

    public MaterialCardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MaterialCardView(Context context, AttributeSet attributeSet, int i) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, 2130969434, 2132018645), attributeSet, 2130969434);
        this.checked = false;
        this.isParentCardViewDoneInitializing = true;
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(getContext(), attributeSet, R$styleable.MaterialCardView, 2130969434, 2132018645, new int[0]);
        MaterialCardViewHelper materialCardViewHelper = new MaterialCardViewHelper(this, attributeSet);
        this.cardViewHelper = materialCardViewHelper;
        CardView.AnonymousClass1 r8 = this.mCardViewDelegate;
        Objects.requireNonNull(r8);
        RoundRectDrawable roundRectDrawable = (RoundRectDrawable) r8.mCardBackground;
        Objects.requireNonNull(roundRectDrawable);
        materialCardViewHelper.bgDrawable.setFillColor(roundRectDrawable.mBackground);
        Rect rect = this.mContentPadding;
        materialCardViewHelper.userContentPadding.set(rect.left, rect.top, rect.right, rect.bottom);
        materialCardViewHelper.updateContentPadding();
        ColorStateList colorStateList = MaterialResources.getColorStateList(materialCardViewHelper.materialCardView.getContext(), obtainStyledAttributes, 10);
        materialCardViewHelper.strokeColor = colorStateList;
        if (colorStateList == null) {
            materialCardViewHelper.strokeColor = ColorStateList.valueOf(-1);
        }
        materialCardViewHelper.strokeWidth = obtainStyledAttributes.getDimensionPixelSize(11, 0);
        boolean z = obtainStyledAttributes.getBoolean(0, false);
        materialCardViewHelper.checkable = z;
        materialCardViewHelper.materialCardView.setLongClickable(z);
        materialCardViewHelper.checkedIconTint = MaterialResources.getColorStateList(materialCardViewHelper.materialCardView.getContext(), obtainStyledAttributes, 5);
        Drawable drawable = MaterialResources.getDrawable(materialCardViewHelper.materialCardView.getContext(), obtainStyledAttributes, 2);
        materialCardViewHelper.checkedIcon = drawable;
        if (drawable != null) {
            Drawable mutate = drawable.mutate();
            materialCardViewHelper.checkedIcon = mutate;
            mutate.setTintList(materialCardViewHelper.checkedIconTint);
            boolean isChecked = materialCardViewHelper.materialCardView.isChecked();
            Drawable drawable2 = materialCardViewHelper.checkedIcon;
            if (drawable2 != null) {
                drawable2.setAlpha(isChecked ? 255 : 0);
            }
        }
        LayerDrawable layerDrawable = materialCardViewHelper.clickableForegroundDrawable;
        if (layerDrawable != null) {
            layerDrawable.setDrawableByLayerId(2131428458, materialCardViewHelper.checkedIcon);
        }
        materialCardViewHelper.checkedIconSize = obtainStyledAttributes.getDimensionPixelSize(4, 0);
        materialCardViewHelper.checkedIconMargin = obtainStyledAttributes.getDimensionPixelSize(3, 0);
        ColorStateList colorStateList2 = MaterialResources.getColorStateList(materialCardViewHelper.materialCardView.getContext(), obtainStyledAttributes, 6);
        materialCardViewHelper.rippleColor = colorStateList2;
        if (colorStateList2 == null) {
            materialCardViewHelper.rippleColor = ColorStateList.valueOf(R$string.getColor(materialCardViewHelper.materialCardView, 2130968819));
        }
        ColorStateList colorStateList3 = MaterialResources.getColorStateList(materialCardViewHelper.materialCardView.getContext(), obtainStyledAttributes, 1);
        materialCardViewHelper.foregroundContentDrawable.setFillColor(colorStateList3 == null ? ColorStateList.valueOf(0) : colorStateList3);
        RippleDrawable rippleDrawable = materialCardViewHelper.rippleDrawable;
        if (rippleDrawable != null) {
            rippleDrawable.setColor(materialCardViewHelper.rippleColor);
        }
        MaterialShapeDrawable materialShapeDrawable = materialCardViewHelper.bgDrawable;
        MaterialCardView materialCardView = materialCardViewHelper.materialCardView;
        Objects.requireNonNull(materialCardView);
        CardView.AnonymousClass1 r7 = materialCardView.mCardViewDelegate;
        Objects.requireNonNull(r7);
        materialShapeDrawable.setElevation(CardView.this.getElevation());
        MaterialShapeDrawable materialShapeDrawable2 = materialCardViewHelper.foregroundContentDrawable;
        ColorStateList colorStateList4 = materialCardViewHelper.strokeColor;
        Objects.requireNonNull(materialShapeDrawable2);
        materialShapeDrawable2.drawableState.strokeWidth = materialCardViewHelper.strokeWidth;
        materialShapeDrawable2.invalidateSelf();
        materialShapeDrawable2.setStrokeColor(colorStateList4);
        MaterialCardView materialCardView2 = materialCardViewHelper.materialCardView;
        MaterialCardViewHelper.AnonymousClass1 insetDrawable = materialCardViewHelper.insetDrawable(materialCardViewHelper.bgDrawable);
        Objects.requireNonNull(materialCardView2);
        super.setBackgroundDrawable(insetDrawable);
        Drawable clickableForeground = materialCardViewHelper.materialCardView.isClickable() ? materialCardViewHelper.getClickableForeground() : materialCardViewHelper.foregroundContentDrawable;
        materialCardViewHelper.fgDrawable = clickableForeground;
        materialCardViewHelper.materialCardView.setForeground(materialCardViewHelper.insetDrawable(clickableForeground));
        obtainStyledAttributes.recycle();
    }

    @Override // android.view.ViewGroup, android.view.View
    public final int[] onCreateDrawableState(int i) {
        boolean z;
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 3);
        MaterialCardViewHelper materialCardViewHelper = this.cardViewHelper;
        if (materialCardViewHelper == null || !materialCardViewHelper.checkable) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            View.mergeDrawableStates(onCreateDrawableState, CHECKABLE_STATE_SET);
        }
        if (isChecked()) {
            View.mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }

    @Override // android.view.View
    public final void setBackgroundDrawable(Drawable drawable) {
        if (this.isParentCardViewDoneInitializing) {
            MaterialCardViewHelper materialCardViewHelper = this.cardViewHelper;
            Objects.requireNonNull(materialCardViewHelper);
            if (!materialCardViewHelper.isBackgroundOverwritten) {
                Log.i("MaterialCardView", "Setting a custom background is not supported.");
                MaterialCardViewHelper materialCardViewHelper2 = this.cardViewHelper;
                Objects.requireNonNull(materialCardViewHelper2);
                materialCardViewHelper2.isBackgroundOverwritten = true;
            }
            super.setBackgroundDrawable(drawable);
        }
    }

    @Override // android.widget.Checkable
    public final void setChecked(boolean z) {
        if (this.checked != z) {
            toggle();
        }
    }

    @Override // com.google.android.material.shape.Shapeable
    public final void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        RectF rectF = new RectF();
        MaterialCardViewHelper materialCardViewHelper = this.cardViewHelper;
        Objects.requireNonNull(materialCardViewHelper);
        rectF.set(materialCardViewHelper.bgDrawable.getBounds());
        setClipToOutline(shapeAppearanceModel.isRoundRect(rectF));
        this.cardViewHelper.setShapeAppearanceModel(shapeAppearanceModel);
    }

    @Override // android.widget.Checkable
    public final void toggle() {
        boolean z;
        MaterialCardViewHelper materialCardViewHelper = this.cardViewHelper;
        int i = 0;
        if (materialCardViewHelper == null || !materialCardViewHelper.checkable) {
            z = false;
        } else {
            z = true;
        }
        if (z && isEnabled()) {
            this.checked = !this.checked;
            refreshDrawableState();
            MaterialCardViewHelper materialCardViewHelper2 = this.cardViewHelper;
            Objects.requireNonNull(materialCardViewHelper2);
            RippleDrawable rippleDrawable = materialCardViewHelper2.rippleDrawable;
            if (rippleDrawable != null) {
                Rect bounds = rippleDrawable.getBounds();
                int i2 = bounds.bottom;
                materialCardViewHelper2.rippleDrawable.setBounds(bounds.left, bounds.top, bounds.right, i2 - 1);
                materialCardViewHelper2.rippleDrawable.setBounds(bounds.left, bounds.top, bounds.right, i2);
            }
            MaterialCardViewHelper materialCardViewHelper3 = this.cardViewHelper;
            boolean z2 = this.checked;
            Objects.requireNonNull(materialCardViewHelper3);
            Drawable drawable = materialCardViewHelper3.checkedIcon;
            if (drawable != null) {
                if (z2) {
                    i = 255;
                }
                drawable.setAlpha(i);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialCardViewHelper materialCardViewHelper = this.cardViewHelper;
        Objects.requireNonNull(materialCardViewHelper);
        R$bool.setParentAbsoluteElevation(this, materialCardViewHelper.bgDrawable);
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName("androidx.cardview.widget.CardView");
        accessibilityEvent.setChecked(isChecked());
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        boolean z;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("androidx.cardview.widget.CardView");
        MaterialCardViewHelper materialCardViewHelper = this.cardViewHelper;
        if (materialCardViewHelper == null || !materialCardViewHelper.checkable) {
            z = false;
        } else {
            z = true;
        }
        accessibilityNodeInfo.setCheckable(z);
        accessibilityNodeInfo.setClickable(isClickable());
        accessibilityNodeInfo.setChecked(isChecked());
    }

    @Override // androidx.cardview.widget.CardView, android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        int i3;
        int i4;
        float f;
        super.onMeasure(i, i2);
        MaterialCardViewHelper materialCardViewHelper = this.cardViewHelper;
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        Objects.requireNonNull(materialCardViewHelper);
        if (materialCardViewHelper.clickableForegroundDrawable != null) {
            int i5 = materialCardViewHelper.checkedIconMargin;
            int i6 = materialCardViewHelper.checkedIconSize;
            int i7 = (measuredWidth - i5) - i6;
            int i8 = (measuredHeight - i5) - i6;
            MaterialCardView materialCardView = materialCardViewHelper.materialCardView;
            Objects.requireNonNull(materialCardView);
            if (materialCardView.mCompatPadding) {
                MaterialCardView materialCardView2 = materialCardViewHelper.materialCardView;
                Objects.requireNonNull(materialCardView2);
                CardView.AnonymousClass1 r0 = materialCardView2.mCardViewDelegate;
                Objects.requireNonNull(r0);
                RoundRectDrawable roundRectDrawable = (RoundRectDrawable) r0.mCardBackground;
                Objects.requireNonNull(roundRectDrawable);
                float f2 = roundRectDrawable.mPadding * 1.5f;
                float f3 = 0.0f;
                if (materialCardViewHelper.shouldAddCornerPaddingOutsideCardBackground()) {
                    f = materialCardViewHelper.calculateActualCornerPadding();
                } else {
                    f = 0.0f;
                }
                i8 -= (int) Math.ceil((f2 + f) * 2.0f);
                MaterialCardView materialCardView3 = materialCardViewHelper.materialCardView;
                Objects.requireNonNull(materialCardView3);
                CardView.AnonymousClass1 r02 = materialCardView3.mCardViewDelegate;
                Objects.requireNonNull(r02);
                RoundRectDrawable roundRectDrawable2 = (RoundRectDrawable) r02.mCardBackground;
                Objects.requireNonNull(roundRectDrawable2);
                float f4 = roundRectDrawable2.mPadding;
                if (materialCardViewHelper.shouldAddCornerPaddingOutsideCardBackground()) {
                    f3 = materialCardViewHelper.calculateActualCornerPadding();
                }
                i7 -= (int) Math.ceil((f4 + f3) * 2.0f);
            }
            int i9 = materialCardViewHelper.checkedIconMargin;
            MaterialCardView materialCardView4 = materialCardViewHelper.materialCardView;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            if (ViewCompat.Api17Impl.getLayoutDirection(materialCardView4) == 1) {
                i4 = i9;
                i3 = i7;
            } else {
                i3 = i9;
                i4 = i7;
            }
            materialCardViewHelper.clickableForegroundDrawable.setLayerInset(2, i4, materialCardViewHelper.checkedIconMargin, i3, i8);
        }
    }

    @Override // android.view.View
    public final void setClickable(boolean z) {
        Drawable drawable;
        super.setClickable(z);
        MaterialCardViewHelper materialCardViewHelper = this.cardViewHelper;
        if (materialCardViewHelper != null) {
            Drawable drawable2 = materialCardViewHelper.fgDrawable;
            if (materialCardViewHelper.materialCardView.isClickable()) {
                drawable = materialCardViewHelper.getClickableForeground();
            } else {
                drawable = materialCardViewHelper.foregroundContentDrawable;
            }
            materialCardViewHelper.fgDrawable = drawable;
            if (drawable2 == drawable) {
                return;
            }
            if (materialCardViewHelper.materialCardView.getForeground() instanceof InsetDrawable) {
                ((InsetDrawable) materialCardViewHelper.materialCardView.getForeground()).setDrawable(drawable);
            } else {
                materialCardViewHelper.materialCardView.setForeground(materialCardViewHelper.insetDrawable(drawable));
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0030, code lost:
        if (r2 != false) goto L_0x0032;
     */
    @Override // androidx.cardview.widget.CardView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setRadius(float r2) {
        /*
            r1 = this;
            super.setRadius(r2)
            com.google.android.material.card.MaterialCardViewHelper r1 = r1.cardViewHelper
            java.util.Objects.requireNonNull(r1)
            com.google.android.material.shape.ShapeAppearanceModel r0 = r1.shapeAppearanceModel
            com.google.android.material.shape.ShapeAppearanceModel r2 = r0.withCornerSize(r2)
            r1.setShapeAppearanceModel(r2)
            android.graphics.drawable.Drawable r2 = r1.fgDrawable
            r2.invalidateSelf()
            boolean r2 = r1.shouldAddCornerPaddingOutsideCardBackground()
            if (r2 != 0) goto L_0x0032
            com.google.android.material.card.MaterialCardView r2 = r1.materialCardView
            java.util.Objects.requireNonNull(r2)
            boolean r2 = r2.mPreventCornerOverlap
            if (r2 == 0) goto L_0x002f
            com.google.android.material.shape.MaterialShapeDrawable r2 = r1.bgDrawable
            boolean r2 = r2.isRoundRect()
            if (r2 != 0) goto L_0x002f
            r2 = 1
            goto L_0x0030
        L_0x002f:
            r2 = 0
        L_0x0030:
            if (r2 == 0) goto L_0x0035
        L_0x0032:
            r1.updateContentPadding()
        L_0x0035:
            boolean r2 = r1.shouldAddCornerPaddingOutsideCardBackground()
            if (r2 == 0) goto L_0x0058
            boolean r2 = r1.isBackgroundOverwritten
            if (r2 != 0) goto L_0x004d
            com.google.android.material.card.MaterialCardView r2 = r1.materialCardView
            com.google.android.material.shape.MaterialShapeDrawable r0 = r1.bgDrawable
            com.google.android.material.card.MaterialCardViewHelper$1 r0 = r1.insetDrawable(r0)
            java.util.Objects.requireNonNull(r2)
            super.setBackgroundDrawable(r0)
        L_0x004d:
            com.google.android.material.card.MaterialCardView r2 = r1.materialCardView
            android.graphics.drawable.Drawable r0 = r1.fgDrawable
            com.google.android.material.card.MaterialCardViewHelper$1 r1 = r1.insetDrawable(r0)
            r2.setForeground(r1)
        L_0x0058:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.card.MaterialCardView.setRadius(float):void");
    }

    @Override // android.view.View
    public final void setBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    @Override // android.widget.Checkable
    public final boolean isChecked() {
        return this.checked;
    }
}
