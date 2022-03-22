package com.google.android.material.chip;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.graphics.drawable.WrappedDrawable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import androidx.fragment.app.FragmentContainer;
import androidx.mediarouter.R$bool;
import com.google.android.material.R$styleable;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class Chip extends AppCompatCheckBox implements ChipDrawable.Delegate, Shapeable {
    public ChipDrawable chipDrawable;
    public boolean closeIconFocused;
    public boolean closeIconHovered;
    public boolean closeIconPressed;
    public boolean deferredCheckedValue;
    public boolean ensureMinTouchTargetSize;
    public final AnonymousClass1 fontCallback;
    public InsetDrawable insetBackgroundDrawable;
    public int lastLayoutDirection;
    public int minTouchTargetSize;
    public CompoundButton.OnCheckedChangeListener onCheckedChangeListenerInternal;
    public final Rect rect;
    public final RectF rectF;
    public RippleDrawable ripple;
    public static final Rect EMPTY_BOUNDS = new Rect();
    public static final int[] SELECTED_STATE = {16842913};
    public static final int[] CHECKABLE_STATE_SET = {16842911};

    /* loaded from: classes.dex */
    public class ChipTouchHelper extends ExploreByTouchHelper {
        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final void getVisibleVirtualViews(ArrayList arrayList) {
            boolean z = false;
            arrayList.add(0);
            Chip chip = Chip.this;
            Rect rect = Chip.EMPTY_BOUNDS;
            if (chip.hasCloseIcon()) {
                Chip chip2 = Chip.this;
                Objects.requireNonNull(chip2);
                ChipDrawable chipDrawable = chip2.chipDrawable;
                if (chipDrawable != null && chipDrawable.closeIconVisible) {
                    z = true;
                }
                if (z) {
                    Objects.requireNonNull(Chip.this);
                }
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
            if (i2 == 16) {
                if (i == 0) {
                    return Chip.this.performClick();
                }
                if (i == 1) {
                    Chip chip = Chip.this;
                    Objects.requireNonNull(chip);
                    chip.playSoundEffect(0);
                }
            }
            return false;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final void onVirtualViewKeyboardFocusChanged(int i, boolean z) {
            if (i == 1) {
                Chip chip = Chip.this;
                chip.closeIconFocused = z;
                chip.refreshDrawableState();
            }
        }

        public ChipTouchHelper(Chip chip) {
            super(chip);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final int getVirtualViewAt(float f, float f2) {
            Chip chip = Chip.this;
            Rect rect = Chip.EMPTY_BOUNDS;
            if (!chip.hasCloseIcon() || !Chip.this.getCloseIconTouchBounds().contains(f, f2)) {
                return 0;
            }
            return 1;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final void onPopulateNodeForHost(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.mInfo.setCheckable(Chip.this.isCheckable());
            accessibilityNodeInfoCompat.mInfo.setClickable(Chip.this.isClickable());
            accessibilityNodeInfoCompat.setClassName(Chip.this.getAccessibilityClassName());
            accessibilityNodeInfoCompat.mInfo.setText(Chip.this.getText());
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            CharSequence charSequence = "";
            if (i == 1) {
                Objects.requireNonNull(Chip.this);
                CharSequence text = Chip.this.getText();
                Context context = Chip.this.getContext();
                Object[] objArr = new Object[1];
                if (!TextUtils.isEmpty(text)) {
                    charSequence = text;
                }
                objArr[0] = charSequence;
                accessibilityNodeInfoCompat.setContentDescription(context.getString(2131952826, objArr).trim());
                Chip chip = Chip.this;
                Objects.requireNonNull(chip);
                RectF closeIconTouchBounds = chip.getCloseIconTouchBounds();
                chip.rect.set((int) closeIconTouchBounds.left, (int) closeIconTouchBounds.top, (int) closeIconTouchBounds.right, (int) closeIconTouchBounds.bottom);
                accessibilityNodeInfoCompat.setBoundsInParent(chip.rect);
                accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
                accessibilityNodeInfoCompat.mInfo.setEnabled(Chip.this.isEnabled());
                return;
            }
            accessibilityNodeInfoCompat.setContentDescription(charSequence);
            accessibilityNodeInfoCompat.setBoundsInParent(Chip.EMPTY_BOUNDS);
        }
    }

    public Chip(Context context) {
        this(context, null);
    }

    @Override // android.widget.TextView
    public final void setCompoundDrawablesRelativeWithIntrinsicBounds(int i, int i2, int i3, int i4) {
        if (i != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (i3 == 0) {
            super.setCompoundDrawablesRelativeWithIntrinsicBounds(i, i2, i3, i4);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public final void setCompoundDrawablesWithIntrinsicBounds(int i, int i2, int i3, int i4) {
        if (i != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (i3 == 0) {
            super.setCompoundDrawablesWithIntrinsicBounds(i, i2, i3, i4);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public final void setLines(int i) {
        if (i <= 1) {
            super.setLines(i);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public final void setMaxLines(int i) {
        if (i <= 1) {
            super.setMaxLines(i);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public final void setMinLines(int i) {
        if (i <= 1) {
            super.setMinLines(i);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public final void setTextAppearance(Context context, int i) {
        super.setTextAppearance(context, i);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.textDrawableHelper.setTextAppearance(new TextAppearance(chipDrawable.context, i), chipDrawable.context);
        }
        updateTextPaintDrawState();
    }

    public Chip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130968783);
    }

    public final boolean ensureAccessibleTouchTarget(int i) {
        int i2;
        this.minTouchTargetSize = i;
        float f = 0.0f;
        int i3 = 0;
        if (!this.ensureMinTouchTargetSize) {
            InsetDrawable insetDrawable = this.insetBackgroundDrawable;
            if (insetDrawable == null) {
                updateBackgroundDrawable();
            } else if (insetDrawable != null) {
                this.insetBackgroundDrawable = null;
                setMinWidth(0);
                ChipDrawable chipDrawable = this.chipDrawable;
                if (chipDrawable != null) {
                    f = chipDrawable.chipMinHeight;
                }
                setMinHeight((int) f);
                updateBackgroundDrawable();
            }
            return false;
        }
        ChipDrawable chipDrawable2 = this.chipDrawable;
        Objects.requireNonNull(chipDrawable2);
        int max = Math.max(0, i - ((int) chipDrawable2.chipMinHeight));
        int max2 = Math.max(0, i - this.chipDrawable.getIntrinsicWidth());
        if (max2 > 0 || max > 0) {
            if (max2 > 0) {
                i2 = max2 / 2;
            } else {
                i2 = 0;
            }
            if (max > 0) {
                i3 = max / 2;
            }
            if (this.insetBackgroundDrawable != null) {
                Rect rect = new Rect();
                this.insetBackgroundDrawable.getPadding(rect);
                if (rect.top == i3 && rect.bottom == i3 && rect.left == i2 && rect.right == i2) {
                    updateBackgroundDrawable();
                    return true;
                }
            }
            if (getMinHeight() != i) {
                setMinHeight(i);
            }
            if (getMinWidth() != i) {
                setMinWidth(i);
            }
            this.insetBackgroundDrawable = new InsetDrawable((Drawable) this.chipDrawable, i2, i3, i2, i3);
            updateBackgroundDrawable();
            return true;
        }
        InsetDrawable insetDrawable2 = this.insetBackgroundDrawable;
        if (insetDrawable2 == null) {
            updateBackgroundDrawable();
        } else if (insetDrawable2 != null) {
            this.insetBackgroundDrawable = null;
            setMinWidth(0);
            ChipDrawable chipDrawable3 = this.chipDrawable;
            if (chipDrawable3 != null) {
                f = chipDrawable3.chipMinHeight;
            }
            setMinHeight((int) f);
            updateBackgroundDrawable();
        }
        return false;
    }

    public final RectF getCloseIconTouchBounds() {
        this.rectF.setEmpty();
        hasCloseIcon();
        return this.rectF;
    }

    @Override // android.widget.TextView
    public final TextUtils.TruncateAt getEllipsize() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable == null) {
            return null;
        }
        Objects.requireNonNull(chipDrawable);
        return chipDrawable.truncateAt;
    }

    public final boolean hasCloseIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            Drawable drawable = chipDrawable.closeIcon;
            if (drawable == null) {
                drawable = null;
            } else if (drawable instanceof WrappedDrawable) {
                drawable = ((WrappedDrawable) drawable).getWrappedDrawable();
            }
            if (drawable != null) {
                return true;
            }
        }
        return false;
    }

    public final boolean isCheckable() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            Objects.requireNonNull(chipDrawable);
            if (chipDrawable.checkable) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.android.material.chip.ChipDrawable.Delegate
    public final void onChipDrawableSizeChange() {
        ensureAccessibleTouchTarget(this.minTouchTargetSize);
        requestLayout();
        invalidateOutline();
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public final int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 2);
        if (isChecked()) {
            View.mergeDrawableStates(onCreateDrawableState, SELECTED_STATE);
        }
        if (isCheckable()) {
            View.mergeDrawableStates(onCreateDrawableState, CHECKABLE_STATE_SET);
        }
        return onCreateDrawableState;
    }

    @Override // android.view.View
    public final void setBackground(Drawable drawable) {
        Drawable drawable2 = this.insetBackgroundDrawable;
        if (drawable2 == null) {
            drawable2 = this.chipDrawable;
        }
        if (drawable == drawable2 || drawable == this.ripple) {
            super.setBackground(drawable);
        } else {
            Log.w("Chip", "Do not set the background; Chip manages its own background drawable.");
        }
    }

    @Override // android.view.View
    public final void setBackgroundColor(int i) {
        Log.w("Chip", "Do not set the background color; Chip manages its own background drawable.");
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.view.View
    public final void setBackgroundDrawable(Drawable drawable) {
        Drawable drawable2 = this.insetBackgroundDrawable;
        if (drawable2 == null) {
            drawable2 = this.chipDrawable;
        }
        if (drawable == drawable2 || drawable == this.ripple) {
            super.setBackgroundDrawable(drawable);
        } else {
            Log.w("Chip", "Do not set the background drawable; Chip manages its own background drawable.");
        }
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.view.View
    public final void setBackgroundResource(int i) {
        Log.w("Chip", "Do not set the background resource; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public final void setBackgroundTintList(ColorStateList colorStateList) {
        Log.w("Chip", "Do not set the background tint list; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public final void setBackgroundTintMode(PorterDuff.Mode mode) {
        Log.w("Chip", "Do not set the background tint mode; Chip manages its own background drawable.");
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public final void setChecked(boolean z) {
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable == null) {
            this.deferredCheckedValue = z;
            return;
        }
        Objects.requireNonNull(chipDrawable);
        if (chipDrawable.checkable) {
            boolean isChecked = isChecked();
            super.setChecked(z);
            if (isChecked != z && (onCheckedChangeListener = this.onCheckedChangeListenerInternal) != null) {
                onCheckedChangeListener.onCheckedChanged(this, z);
            }
        }
    }

    @Override // android.widget.TextView
    public final void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (drawable3 == null) {
            super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public final void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (drawable3 == null) {
            super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public final void setEllipsize(TextUtils.TruncateAt truncateAt) {
        if (this.chipDrawable != null) {
            if (truncateAt != TextUtils.TruncateAt.MARQUEE) {
                super.setEllipsize(truncateAt);
                ChipDrawable chipDrawable = this.chipDrawable;
                if (chipDrawable != null) {
                    Objects.requireNonNull(chipDrawable);
                    chipDrawable.truncateAt = truncateAt;
                    return;
                }
                return;
            }
            throw new UnsupportedOperationException("Text within a chip are not allowed to scroll.");
        }
    }

    @Override // android.view.View
    public final void setLayoutDirection(int i) {
        if (this.chipDrawable != null) {
            super.setLayoutDirection(i);
        }
    }

    @Override // com.google.android.material.shape.Shapeable
    public final void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.chipDrawable.setShapeAppearanceModel(shapeAppearanceModel);
    }

    @Override // android.widget.TextView
    public final void setSingleLine(boolean z) {
        if (z) {
            super.setSingleLine(z);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public final void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        CharSequence charSequence2;
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            if (charSequence == null) {
                charSequence = "";
            }
            Objects.requireNonNull(chipDrawable);
            if (chipDrawable.shouldDrawText) {
                charSequence2 = null;
            } else {
                charSequence2 = charSequence;
            }
            super.setText(charSequence2, bufferType);
            ChipDrawable chipDrawable2 = this.chipDrawable;
            if (chipDrawable2 != null) {
                chipDrawable2.setText(charSequence);
            }
        }
    }

    public final void updateBackgroundDrawable() {
        ChipDrawable chipDrawable = this.chipDrawable;
        Objects.requireNonNull(chipDrawable);
        ColorStateList sanitizeRippleDrawableColor = RippleUtils.sanitizeRippleDrawableColor(chipDrawable.rippleColor);
        Drawable drawable = this.insetBackgroundDrawable;
        if (drawable == null) {
            drawable = this.chipDrawable;
        }
        this.ripple = new RippleDrawable(sanitizeRippleDrawableColor, drawable, null);
        ChipDrawable chipDrawable2 = this.chipDrawable;
        Objects.requireNonNull(chipDrawable2);
        if (chipDrawable2.useCompatRipple) {
            chipDrawable2.useCompatRipple = false;
            chipDrawable2.compatRippleColor = null;
            chipDrawable2.onStateChange(chipDrawable2.getState());
        }
        RippleDrawable rippleDrawable = this.ripple;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api16Impl.setBackground(this, rippleDrawable);
        updatePaddingInternal();
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [com.google.android.material.chip.Chip$1] */
    public Chip(Context context, AttributeSet attributeSet, int i) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, i, 2132018647), attributeSet, i);
        ChipDrawable chipDrawable;
        ColorStateList colorStateList;
        int resourceId;
        this.rect = new Rect();
        this.rectF = new RectF();
        this.fontCallback = new FragmentContainer() { // from class: com.google.android.material.chip.Chip.1
            @Override // androidx.fragment.app.FragmentContainer
            public final void onFontRetrievalFailed(int i2) {
            }

            @Override // androidx.fragment.app.FragmentContainer
            public final void onFontRetrieved(Typeface typeface, boolean z) {
                CharSequence charSequence;
                Chip chip = Chip.this;
                ChipDrawable chipDrawable2 = chip.chipDrawable;
                Objects.requireNonNull(chipDrawable2);
                if (chipDrawable2.shouldDrawText) {
                    ChipDrawable chipDrawable3 = Chip.this.chipDrawable;
                    Objects.requireNonNull(chipDrawable3);
                    charSequence = chipDrawable3.text;
                } else {
                    charSequence = Chip.this.getText();
                }
                chip.setText(charSequence);
                Chip.this.requestLayout();
                Chip.this.invalidate();
            }
        };
        Context context2 = getContext();
        if (attributeSet != null) {
            if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "background") != null) {
                Log.w("Chip", "Do not set the background; Chip manages its own background drawable.");
            }
            if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableLeft") != null) {
                throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
            } else if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableStart") != null) {
                throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
            } else if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableEnd") != null) {
                throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
            } else if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableRight") != null) {
                throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
            } else if (!attributeSet.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "singleLine", true) || attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "lines", 1) != 1 || attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "minLines", 1) != 1 || attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "maxLines", 1) != 1) {
                throw new UnsupportedOperationException("Chip does not support multi-line text");
            } else if (attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "gravity", 8388627) != 8388627) {
                Log.w("Chip", "Chip text must be vertically center and start aligned");
            }
        }
        ChipDrawable chipDrawable2 = new ChipDrawable(context2, attributeSet, i);
        Context context3 = chipDrawable2.context;
        int[] iArr = R$styleable.Chip;
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context3, attributeSet, iArr, i, 2132018647, new int[0]);
        chipDrawable2.isShapeThemingEnabled = obtainStyledAttributes.hasValue(37);
        ColorStateList colorStateList2 = MaterialResources.getColorStateList(chipDrawable2.context, obtainStyledAttributes, 24);
        if (chipDrawable2.chipSurfaceColor != colorStateList2) {
            chipDrawable2.chipSurfaceColor = colorStateList2;
            chipDrawable2.onStateChange(chipDrawable2.getState());
        }
        ColorStateList colorStateList3 = MaterialResources.getColorStateList(chipDrawable2.context, obtainStyledAttributes, 11);
        if (chipDrawable2.chipBackgroundColor != colorStateList3) {
            chipDrawable2.chipBackgroundColor = colorStateList3;
            chipDrawable2.onStateChange(chipDrawable2.getState());
        }
        float dimension = obtainStyledAttributes.getDimension(19, 0.0f);
        if (chipDrawable2.chipMinHeight != dimension) {
            chipDrawable2.chipMinHeight = dimension;
            chipDrawable2.invalidateSelf();
            chipDrawable2.onSizeChange();
        }
        if (obtainStyledAttributes.hasValue(12)) {
            float dimension2 = obtainStyledAttributes.getDimension(12, 0.0f);
            if (chipDrawable2.chipCornerRadius != dimension2) {
                chipDrawable2.chipCornerRadius = dimension2;
                chipDrawable2.setShapeAppearanceModel(chipDrawable2.drawableState.shapeAppearanceModel.withCornerSize(dimension2));
            }
        }
        ColorStateList colorStateList4 = MaterialResources.getColorStateList(chipDrawable2.context, obtainStyledAttributes, 22);
        if (chipDrawable2.chipStrokeColor != colorStateList4) {
            chipDrawable2.chipStrokeColor = colorStateList4;
            if (chipDrawable2.isShapeThemingEnabled) {
                chipDrawable2.setStrokeColor(colorStateList4);
            }
            chipDrawable2.onStateChange(chipDrawable2.getState());
        }
        float dimension3 = obtainStyledAttributes.getDimension(23, 0.0f);
        if (chipDrawable2.chipStrokeWidth != dimension3) {
            chipDrawable2.chipStrokeWidth = dimension3;
            chipDrawable2.chipPaint.setStrokeWidth(dimension3);
            if (chipDrawable2.isShapeThemingEnabled) {
                chipDrawable2.drawableState.strokeWidth = dimension3;
                chipDrawable2.invalidateSelf();
            }
            chipDrawable2.invalidateSelf();
        }
        ColorStateList colorStateList5 = MaterialResources.getColorStateList(chipDrawable2.context, obtainStyledAttributes, 36);
        if (chipDrawable2.rippleColor != colorStateList5) {
            chipDrawable2.rippleColor = colorStateList5;
            chipDrawable2.compatRippleColor = chipDrawable2.useCompatRipple ? RippleUtils.sanitizeRippleDrawableColor(colorStateList5) : null;
            chipDrawable2.onStateChange(chipDrawable2.getState());
        }
        chipDrawable2.setText(obtainStyledAttributes.getText(5));
        TextAppearance textAppearance = (!obtainStyledAttributes.hasValue(0) || (resourceId = obtainStyledAttributes.getResourceId(0, 0)) == 0) ? null : new TextAppearance(chipDrawable2.context, resourceId);
        Objects.requireNonNull(textAppearance);
        textAppearance.textSize = obtainStyledAttributes.getDimension(1, textAppearance.textSize);
        chipDrawable2.textDrawableHelper.setTextAppearance(textAppearance, chipDrawable2.context);
        int i2 = obtainStyledAttributes.getInt(3, 0);
        if (i2 == 1) {
            chipDrawable2.truncateAt = TextUtils.TruncateAt.START;
        } else if (i2 == 2) {
            chipDrawable2.truncateAt = TextUtils.TruncateAt.MIDDLE;
        } else if (i2 == 3) {
            chipDrawable2.truncateAt = TextUtils.TruncateAt.END;
        }
        chipDrawable2.setChipIconVisible(obtainStyledAttributes.getBoolean(18, false));
        if (!(attributeSet == null || attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "chipIconEnabled") == null || attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "chipIconVisible") != null)) {
            chipDrawable2.setChipIconVisible(obtainStyledAttributes.getBoolean(15, false));
        }
        Drawable drawable = MaterialResources.getDrawable(chipDrawable2.context, obtainStyledAttributes, 14);
        Drawable drawable2 = chipDrawable2.chipIcon;
        if (drawable2 == null) {
            drawable2 = null;
        } else if (drawable2 instanceof WrappedDrawable) {
            drawable2 = ((WrappedDrawable) drawable2).getWrappedDrawable();
        }
        if (drawable2 != drawable) {
            float calculateChipIconWidth = chipDrawable2.calculateChipIconWidth();
            chipDrawable2.chipIcon = drawable != null ? drawable.mutate() : null;
            float calculateChipIconWidth2 = chipDrawable2.calculateChipIconWidth();
            ChipDrawable.unapplyChildDrawable(drawable2);
            if (chipDrawable2.showsChipIcon()) {
                chipDrawable2.applyChildDrawable(chipDrawable2.chipIcon);
            }
            chipDrawable2.invalidateSelf();
            if (calculateChipIconWidth != calculateChipIconWidth2) {
                chipDrawable2.onSizeChange();
            }
        }
        if (obtainStyledAttributes.hasValue(17)) {
            ColorStateList colorStateList6 = MaterialResources.getColorStateList(chipDrawable2.context, obtainStyledAttributes, 17);
            chipDrawable2.hasChipIconTint = true;
            if (chipDrawable2.chipIconTint != colorStateList6) {
                chipDrawable2.chipIconTint = colorStateList6;
                if (chipDrawable2.showsChipIcon()) {
                    chipDrawable2.chipIcon.setTintList(colorStateList6);
                }
                chipDrawable2.onStateChange(chipDrawable2.getState());
            }
        }
        float dimension4 = obtainStyledAttributes.getDimension(16, -1.0f);
        if (chipDrawable2.chipIconSize != dimension4) {
            float calculateChipIconWidth3 = chipDrawable2.calculateChipIconWidth();
            chipDrawable2.chipIconSize = dimension4;
            float calculateChipIconWidth4 = chipDrawable2.calculateChipIconWidth();
            chipDrawable2.invalidateSelf();
            if (calculateChipIconWidth3 != calculateChipIconWidth4) {
                chipDrawable2.onSizeChange();
            }
        }
        chipDrawable2.setCloseIconVisible(obtainStyledAttributes.getBoolean(31, false));
        if (!(attributeSet == null || attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "closeIconEnabled") == null || attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "closeIconVisible") != null)) {
            chipDrawable2.setCloseIconVisible(obtainStyledAttributes.getBoolean(26, false));
        }
        Drawable drawable3 = MaterialResources.getDrawable(chipDrawable2.context, obtainStyledAttributes, 25);
        Drawable drawable4 = chipDrawable2.closeIcon;
        if (drawable4 == null) {
            drawable4 = null;
        } else if (drawable4 instanceof WrappedDrawable) {
            drawable4 = ((WrappedDrawable) drawable4).getWrappedDrawable();
        }
        if (drawable4 != drawable3) {
            float calculateCloseIconWidth = chipDrawable2.calculateCloseIconWidth();
            chipDrawable2.closeIcon = drawable3 != null ? drawable3.mutate() : null;
            chipDrawable2.closeIconRipple = new RippleDrawable(RippleUtils.sanitizeRippleDrawableColor(chipDrawable2.rippleColor), chipDrawable2.closeIcon, ChipDrawable.closeIconRippleMask);
            float calculateCloseIconWidth2 = chipDrawable2.calculateCloseIconWidth();
            ChipDrawable.unapplyChildDrawable(drawable4);
            if (chipDrawable2.showsCloseIcon()) {
                chipDrawable2.applyChildDrawable(chipDrawable2.closeIcon);
            }
            chipDrawable2.invalidateSelf();
            if (calculateCloseIconWidth != calculateCloseIconWidth2) {
                chipDrawable2.onSizeChange();
            }
        }
        ColorStateList colorStateList7 = MaterialResources.getColorStateList(chipDrawable2.context, obtainStyledAttributes, 30);
        if (chipDrawable2.closeIconTint != colorStateList7) {
            chipDrawable2.closeIconTint = colorStateList7;
            if (chipDrawable2.showsCloseIcon()) {
                chipDrawable2.closeIcon.setTintList(colorStateList7);
            }
            chipDrawable2.onStateChange(chipDrawable2.getState());
        }
        float dimension5 = obtainStyledAttributes.getDimension(28, 0.0f);
        if (chipDrawable2.closeIconSize != dimension5) {
            chipDrawable2.closeIconSize = dimension5;
            chipDrawable2.invalidateSelf();
            if (chipDrawable2.showsCloseIcon()) {
                chipDrawable2.onSizeChange();
            }
        }
        boolean z = obtainStyledAttributes.getBoolean(6, false);
        if (chipDrawable2.checkable != z) {
            chipDrawable2.checkable = z;
            float calculateChipIconWidth5 = chipDrawable2.calculateChipIconWidth();
            if (!z && chipDrawable2.currentChecked) {
                chipDrawable2.currentChecked = false;
            }
            float calculateChipIconWidth6 = chipDrawable2.calculateChipIconWidth();
            chipDrawable2.invalidateSelf();
            if (calculateChipIconWidth5 != calculateChipIconWidth6) {
                chipDrawable2.onSizeChange();
            }
        }
        chipDrawable2.setCheckedIconVisible(obtainStyledAttributes.getBoolean(10, false));
        if (!(attributeSet == null || attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "checkedIconEnabled") == null || attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "checkedIconVisible") != null)) {
            chipDrawable2.setCheckedIconVisible(obtainStyledAttributes.getBoolean(8, false));
        }
        Drawable drawable5 = MaterialResources.getDrawable(chipDrawable2.context, obtainStyledAttributes, 7);
        if (chipDrawable2.checkedIcon != drawable5) {
            float calculateChipIconWidth7 = chipDrawable2.calculateChipIconWidth();
            chipDrawable2.checkedIcon = drawable5;
            float calculateChipIconWidth8 = chipDrawable2.calculateChipIconWidth();
            ChipDrawable.unapplyChildDrawable(chipDrawable2.checkedIcon);
            chipDrawable2.applyChildDrawable(chipDrawable2.checkedIcon);
            chipDrawable2.invalidateSelf();
            if (calculateChipIconWidth7 != calculateChipIconWidth8) {
                chipDrawable2.onSizeChange();
            }
        }
        if (obtainStyledAttributes.hasValue(9) && chipDrawable2.checkedIconTint != (colorStateList = MaterialResources.getColorStateList(chipDrawable2.context, obtainStyledAttributes, 9))) {
            chipDrawable2.checkedIconTint = colorStateList;
            if (chipDrawable2.checkedIconVisible && chipDrawable2.checkedIcon != null && chipDrawable2.checkable) {
                chipDrawable2.checkedIcon.setTintList(colorStateList);
            }
            chipDrawable2.onStateChange(chipDrawable2.getState());
        }
        MotionSpec.createFromAttribute(chipDrawable2.context, obtainStyledAttributes, 39);
        MotionSpec.createFromAttribute(chipDrawable2.context, obtainStyledAttributes, 33);
        float dimension6 = obtainStyledAttributes.getDimension(21, 0.0f);
        if (chipDrawable2.chipStartPadding != dimension6) {
            chipDrawable2.chipStartPadding = dimension6;
            chipDrawable2.invalidateSelf();
            chipDrawable2.onSizeChange();
        }
        float dimension7 = obtainStyledAttributes.getDimension(35, 0.0f);
        if (chipDrawable2.iconStartPadding != dimension7) {
            float calculateChipIconWidth9 = chipDrawable2.calculateChipIconWidth();
            chipDrawable2.iconStartPadding = dimension7;
            float calculateChipIconWidth10 = chipDrawable2.calculateChipIconWidth();
            chipDrawable2.invalidateSelf();
            if (calculateChipIconWidth9 != calculateChipIconWidth10) {
                chipDrawable2.onSizeChange();
            }
        }
        float dimension8 = obtainStyledAttributes.getDimension(34, 0.0f);
        if (chipDrawable2.iconEndPadding != dimension8) {
            float calculateChipIconWidth11 = chipDrawable2.calculateChipIconWidth();
            chipDrawable2.iconEndPadding = dimension8;
            float calculateChipIconWidth12 = chipDrawable2.calculateChipIconWidth();
            chipDrawable2.invalidateSelf();
            if (calculateChipIconWidth11 != calculateChipIconWidth12) {
                chipDrawable2.onSizeChange();
            }
        }
        float dimension9 = obtainStyledAttributes.getDimension(41, 0.0f);
        if (chipDrawable2.textStartPadding != dimension9) {
            chipDrawable2.textStartPadding = dimension9;
            chipDrawable2.invalidateSelf();
            chipDrawable2.onSizeChange();
        }
        float dimension10 = obtainStyledAttributes.getDimension(40, 0.0f);
        if (chipDrawable2.textEndPadding != dimension10) {
            chipDrawable2.textEndPadding = dimension10;
            chipDrawable2.invalidateSelf();
            chipDrawable2.onSizeChange();
        }
        float dimension11 = obtainStyledAttributes.getDimension(29, 0.0f);
        if (chipDrawable2.closeIconStartPadding != dimension11) {
            chipDrawable2.closeIconStartPadding = dimension11;
            chipDrawable2.invalidateSelf();
            if (chipDrawable2.showsCloseIcon()) {
                chipDrawable2.onSizeChange();
            }
        }
        float dimension12 = obtainStyledAttributes.getDimension(27, 0.0f);
        if (chipDrawable2.closeIconEndPadding != dimension12) {
            chipDrawable2.closeIconEndPadding = dimension12;
            chipDrawable2.invalidateSelf();
            if (chipDrawable2.showsCloseIcon()) {
                chipDrawable2.onSizeChange();
            }
        }
        float dimension13 = obtainStyledAttributes.getDimension(13, 0.0f);
        if (chipDrawable2.chipEndPadding != dimension13) {
            chipDrawable2.chipEndPadding = dimension13;
            chipDrawable2.invalidateSelf();
            chipDrawable2.onSizeChange();
        }
        chipDrawable2.maxWidth = obtainStyledAttributes.getDimensionPixelSize(4, Integer.MAX_VALUE);
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, iArr, i, 2132018647, new int[0]);
        this.ensureMinTouchTargetSize = obtainStyledAttributes2.getBoolean(32, false);
        this.minTouchTargetSize = (int) Math.ceil(obtainStyledAttributes2.getDimension(20, (float) Math.ceil(ViewUtils.dpToPx(getContext(), 48))));
        obtainStyledAttributes2.recycle();
        ChipDrawable chipDrawable3 = this.chipDrawable;
        if (chipDrawable3 != chipDrawable2) {
            if (chipDrawable3 != null) {
                chipDrawable3.delegate = new WeakReference<>(null);
            }
            this.chipDrawable = chipDrawable2;
            chipDrawable2.shouldDrawText = false;
            chipDrawable2.delegate = new WeakReference<>(this);
            ensureAccessibleTouchTarget(this.minTouchTargetSize);
        }
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        chipDrawable2.setElevation(ViewCompat.Api21Impl.getElevation(this));
        TypedArray obtainStyledAttributes3 = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, iArr, i, 2132018647, new int[0]);
        boolean hasValue = obtainStyledAttributes3.hasValue(37);
        obtainStyledAttributes3.recycle();
        new ChipTouchHelper(this);
        if (hasCloseIcon() && (chipDrawable = this.chipDrawable) != null) {
            boolean z2 = chipDrawable.closeIconVisible;
        }
        ViewCompat.setAccessibilityDelegate(this, null);
        if (!hasValue) {
            setOutlineProvider(new ViewOutlineProvider() { // from class: com.google.android.material.chip.Chip.2
                @Override // android.view.ViewOutlineProvider
                @TargetApi(21)
                public final void getOutline(View view, Outline outline) {
                    ChipDrawable chipDrawable4 = Chip.this.chipDrawable;
                    if (chipDrawable4 != null) {
                        chipDrawable4.getOutline(outline);
                    } else {
                        outline.setAlpha(0.0f);
                    }
                }
            });
        }
        setChecked(this.deferredCheckedValue);
        setText(chipDrawable2.text);
        setEllipsize(chipDrawable2.truncateAt);
        updateTextPaintDrawState();
        ChipDrawable chipDrawable4 = this.chipDrawable;
        Objects.requireNonNull(chipDrawable4);
        if (!chipDrawable4.shouldDrawText) {
            setLines(1);
            setHorizontallyScrolling(true);
        }
        setGravity(8388627);
        updatePaddingInternal();
        if (this.ensureMinTouchTargetSize) {
            setMinHeight(this.minTouchTargetSize);
        }
        this.lastLayoutDirection = ViewCompat.Api17Impl.getLayoutDirection(this);
    }

    @Override // android.view.View
    public final boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public final boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [int, boolean] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.widget.CompoundButton, android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void drawableStateChanged() {
        /*
            r5 = this;
            super.drawableStateChanged()
            com.google.android.material.chip.ChipDrawable r0 = r5.chipDrawable
            r1 = 0
            if (r0 == 0) goto L_0x0088
            android.graphics.drawable.Drawable r0 = r0.closeIcon
            boolean r0 = com.google.android.material.chip.ChipDrawable.isStateful(r0)
            if (r0 == 0) goto L_0x0088
            com.google.android.material.chip.ChipDrawable r0 = r5.chipDrawable
            boolean r2 = r5.isEnabled()
            boolean r3 = r5.closeIconFocused
            if (r3 == 0) goto L_0x001c
            int r2 = r2 + 1
        L_0x001c:
            boolean r3 = r5.closeIconHovered
            if (r3 == 0) goto L_0x0022
            int r2 = r2 + 1
        L_0x0022:
            boolean r3 = r5.closeIconPressed
            if (r3 == 0) goto L_0x0028
            int r2 = r2 + 1
        L_0x0028:
            boolean r3 = r5.isChecked()
            if (r3 == 0) goto L_0x0030
            int r2 = r2 + 1
        L_0x0030:
            int[] r2 = new int[r2]
            boolean r3 = r5.isEnabled()
            if (r3 == 0) goto L_0x003f
            r3 = 16842910(0x101009e, float:2.3694E-38)
            r2[r1] = r3
            r3 = 1
            goto L_0x0040
        L_0x003f:
            r3 = r1
        L_0x0040:
            boolean r4 = r5.closeIconFocused
            if (r4 == 0) goto L_0x004b
            r4 = 16842908(0x101009c, float:2.3693995E-38)
            r2[r3] = r4
            int r3 = r3 + 1
        L_0x004b:
            boolean r4 = r5.closeIconHovered
            if (r4 == 0) goto L_0x0056
            r4 = 16843623(0x1010367, float:2.3696E-38)
            r2[r3] = r4
            int r3 = r3 + 1
        L_0x0056:
            boolean r4 = r5.closeIconPressed
            if (r4 == 0) goto L_0x0061
            r4 = 16842919(0x10100a7, float:2.3694026E-38)
            r2[r3] = r4
            int r3 = r3 + 1
        L_0x0061:
            boolean r4 = r5.isChecked()
            if (r4 == 0) goto L_0x006c
            r4 = 16842913(0x10100a1, float:2.369401E-38)
            r2[r3] = r4
        L_0x006c:
            java.util.Objects.requireNonNull(r0)
            int[] r3 = r0.closeIconStateSet
            boolean r3 = java.util.Arrays.equals(r3, r2)
            if (r3 != 0) goto L_0x0088
            r0.closeIconStateSet = r2
            boolean r3 = r0.showsCloseIcon()
            if (r3 == 0) goto L_0x0088
            int[] r1 = r0.getState()
            boolean r0 = r0.onStateChange(r1, r2)
            r1 = r0
        L_0x0088:
            if (r1 == 0) goto L_0x008d
            r5.invalidate()
        L_0x008d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.chip.Chip.drawableStateChanged():void");
    }

    @Override // android.widget.CheckBox, android.widget.CompoundButton, android.widget.Button, android.widget.TextView, android.view.View
    public final CharSequence getAccessibilityClassName() {
        if (isCheckable()) {
            ViewParent parent = getParent();
            if (!(parent instanceof ChipGroup)) {
                return "android.widget.CompoundButton";
            }
            ChipGroup chipGroup = (ChipGroup) parent;
            Objects.requireNonNull(chipGroup);
            if (chipGroup.singleSelection) {
                return "android.widget.RadioButton";
            }
            return "android.widget.CompoundButton";
        } else if (isClickable()) {
            return "android.widget.Button";
        } else {
            return "android.view.View";
        }
    }

    @Override // android.widget.TextView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        R$bool.setParentAbsoluteElevation(this, this.chipDrawable);
    }

    @Override // android.view.View
    public final boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 7) {
            boolean contains = getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY());
            if (this.closeIconHovered != contains) {
                this.closeIconHovered = contains;
                refreshDrawableState();
            }
        } else if (actionMasked == 10 && this.closeIconHovered) {
            this.closeIconHovered = false;
            refreshDrawableState();
        }
        return super.onHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        int i;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(getAccessibilityClassName());
        accessibilityNodeInfo.setCheckable(isCheckable());
        accessibilityNodeInfo.setClickable(isClickable());
        if (getParent() instanceof ChipGroup) {
            ChipGroup chipGroup = (ChipGroup) getParent();
            Objects.requireNonNull(chipGroup);
            int i2 = -1;
            if (chipGroup.singleLine) {
                i = 0;
                for (int i3 = 0; i3 < chipGroup.getChildCount(); i3++) {
                    if (chipGroup.getChildAt(i3) instanceof Chip) {
                        if (((Chip) chipGroup.getChildAt(i3)) == this) {
                            break;
                        }
                        i++;
                    }
                }
            }
            i = -1;
            Object tag = getTag(2131428724);
            if (tag instanceof Integer) {
                i2 = ((Integer) tag).intValue();
            }
            accessibilityNodeInfo.setCollectionItemInfo((AccessibilityNodeInfo.CollectionItemInfo) AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(i2, 1, i, 1, isChecked()).mInfo);
        }
    }

    @Override // android.widget.Button, android.widget.TextView, android.view.View
    @TargetApi(24)
    public final PointerIcon onResolvePointerIcon(MotionEvent motionEvent, int i) {
        if (!getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY()) || !isEnabled()) {
            return null;
        }
        return PointerIcon.getSystemIcon(getContext(), 1002);
    }

    @Override // android.widget.TextView, android.view.View
    @TargetApi(17)
    public final void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        if (this.lastLayoutDirection != i) {
            this.lastLayoutDirection = i;
            updatePaddingInternal();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x001e, code lost:
        if (r0 != 3) goto L_0x0050;
     */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003d  */
    @Override // android.widget.TextView, android.view.View
    @android.annotation.SuppressLint({"ClickableViewAccessibility"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onTouchEvent(android.view.MotionEvent r6) {
        /*
            r5 = this;
            int r0 = r6.getActionMasked()
            android.graphics.RectF r1 = r5.getCloseIconTouchBounds()
            float r2 = r6.getX()
            float r3 = r6.getY()
            boolean r1 = r1.contains(r2, r3)
            r2 = 0
            r3 = 1
            if (r0 == 0) goto L_0x0043
            if (r0 == r3) goto L_0x002f
            r4 = 2
            if (r0 == r4) goto L_0x0021
            r1 = 3
            if (r0 == r1) goto L_0x0038
            goto L_0x0050
        L_0x0021:
            boolean r0 = r5.closeIconPressed
            if (r0 == 0) goto L_0x0050
            if (r1 != 0) goto L_0x004e
            if (r0 == 0) goto L_0x004e
            r5.closeIconPressed = r2
            r5.refreshDrawableState()
            goto L_0x004e
        L_0x002f:
            boolean r0 = r5.closeIconPressed
            if (r0 == 0) goto L_0x0038
            r5.playSoundEffect(r2)
            r0 = r3
            goto L_0x0039
        L_0x0038:
            r0 = r2
        L_0x0039:
            boolean r1 = r5.closeIconPressed
            if (r1 == 0) goto L_0x0051
            r5.closeIconPressed = r2
            r5.refreshDrawableState()
            goto L_0x0051
        L_0x0043:
            if (r1 == 0) goto L_0x0050
            boolean r0 = r5.closeIconPressed
            if (r0 == r3) goto L_0x004e
            r5.closeIconPressed = r3
            r5.refreshDrawableState()
        L_0x004e:
            r0 = r3
            goto L_0x0051
        L_0x0050:
            r0 = r2
        L_0x0051:
            if (r0 != 0) goto L_0x0059
            boolean r5 = super.onTouchEvent(r6)
            if (r5 == 0) goto L_0x005a
        L_0x0059:
            r2 = r3
        L_0x005a:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.chip.Chip.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View
    public final void setElevation(float f) {
        super.setElevation(f);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setElevation(f);
        }
    }

    @Override // android.widget.TextView
    public final void setGravity(int i) {
        if (i != 8388627) {
            Log.w("Chip", "Chip text must be vertically center and start aligned");
        } else {
            super.setGravity(i);
        }
    }

    @Override // android.widget.TextView
    public final void setMaxWidth(int i) {
        super.setMaxWidth(i);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            Objects.requireNonNull(chipDrawable);
            chipDrawable.maxWidth = i;
        }
    }

    public final void updatePaddingInternal() {
        ChipDrawable chipDrawable;
        if (!TextUtils.isEmpty(getText()) && (chipDrawable = this.chipDrawable) != null) {
            Objects.requireNonNull(chipDrawable);
            float f = chipDrawable.chipEndPadding;
            ChipDrawable chipDrawable2 = this.chipDrawable;
            Objects.requireNonNull(chipDrawable2);
            int calculateCloseIconWidth = (int) (this.chipDrawable.calculateCloseIconWidth() + f + chipDrawable2.textEndPadding);
            ChipDrawable chipDrawable3 = this.chipDrawable;
            Objects.requireNonNull(chipDrawable3);
            float f2 = chipDrawable3.chipStartPadding;
            ChipDrawable chipDrawable4 = this.chipDrawable;
            Objects.requireNonNull(chipDrawable4);
            int calculateChipIconWidth = (int) (this.chipDrawable.calculateChipIconWidth() + f2 + chipDrawable4.textStartPadding);
            if (this.insetBackgroundDrawable != null) {
                Rect rect = new Rect();
                this.insetBackgroundDrawable.getPadding(rect);
                calculateChipIconWidth += rect.left;
                calculateCloseIconWidth += rect.right;
            }
            int paddingTop = getPaddingTop();
            int paddingBottom = getPaddingBottom();
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api17Impl.setPaddingRelative(this, calculateChipIconWidth, paddingTop, calculateCloseIconWidth, paddingBottom);
        }
    }

    public final void updateTextPaintDrawState() {
        TextAppearance textAppearance;
        TextPaint paint = getPaint();
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            paint.drawableState = chipDrawable.getState();
        }
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            TextDrawableHelper textDrawableHelper = chipDrawable2.textDrawableHelper;
            Objects.requireNonNull(textDrawableHelper);
            textAppearance = textDrawableHelper.textAppearance;
        } else {
            textAppearance = null;
        }
        if (textAppearance != null) {
            textAppearance.updateDrawState(getContext(), paint, this.fontCallback);
        }
    }

    @Override // android.widget.TextView
    public final void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (drawable3 == null) {
            super.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public final void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
        } else if (drawable3 == null) {
            super.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        } else {
            throw new UnsupportedOperationException("Please set right drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public final void setTextAppearance(int i) {
        super.setTextAppearance(i);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.textDrawableHelper.setTextAppearance(new TextAppearance(chipDrawable.context, i), chipDrawable.context);
        }
        updateTextPaintDrawState();
    }

    @Override // android.widget.TextView, android.view.View
    public final void getFocusedRect(Rect rect) {
        super.getFocusedRect(rect);
    }

    @Override // android.widget.TextView, android.view.View
    public final void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
    }
}
