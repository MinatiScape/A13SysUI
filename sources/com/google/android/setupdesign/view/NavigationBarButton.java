package com.google.android.setupdesign.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.Button;
import java.util.Objects;
@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes.dex */
public class NavigationBarButton extends Button {

    /* loaded from: classes.dex */
    public static class TintedDrawable extends LayerDrawable {
        public ColorStateList tintList = null;

        public TintedDrawable(Drawable drawable) {
            super(new Drawable[]{drawable});
        }

        @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
        public final boolean isStateful() {
            return true;
        }

        public static TintedDrawable wrap(Drawable drawable) {
            if (drawable instanceof TintedDrawable) {
                return (TintedDrawable) drawable;
            }
            return new TintedDrawable(drawable.mutate());
        }

        @Override // android.graphics.drawable.Drawable
        public final boolean setState(int[] iArr) {
            boolean z;
            boolean state = super.setState(iArr);
            ColorStateList colorStateList = this.tintList;
            if (colorStateList != null) {
                setColorFilter(colorStateList.getColorForState(getState(), 0), PorterDuff.Mode.SRC_IN);
                z = true;
            } else {
                z = false;
            }
            if (state || z) {
                return true;
            }
            return false;
        }
    }

    public NavigationBarButton(Context context) {
        super(context);
        init();
    }

    @Override // android.widget.TextView
    public final void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            drawable = TintedDrawable.wrap(drawable);
        }
        if (drawable2 != null) {
            drawable2 = TintedDrawable.wrap(drawable2);
        }
        if (drawable3 != null) {
            drawable3 = TintedDrawable.wrap(drawable3);
        }
        if (drawable4 != null) {
            drawable4 = TintedDrawable.wrap(drawable4);
        }
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        tintDrawables();
    }

    @Override // android.widget.TextView
    public final void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            drawable = TintedDrawable.wrap(drawable);
        }
        if (drawable2 != null) {
            drawable2 = TintedDrawable.wrap(drawable2);
        }
        if (drawable3 != null) {
            drawable3 = TintedDrawable.wrap(drawable3);
        }
        if (drawable4 != null) {
            drawable4 = TintedDrawable.wrap(drawable4);
        }
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        tintDrawables();
    }

    public NavigationBarButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public final void init() {
        if (!isInEditMode()) {
            Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
            for (int i = 0; i < compoundDrawablesRelative.length; i++) {
                if (compoundDrawablesRelative[i] != null) {
                    compoundDrawablesRelative[i] = TintedDrawable.wrap(compoundDrawablesRelative[i]);
                }
            }
            setCompoundDrawablesRelativeWithIntrinsicBounds(compoundDrawablesRelative[0], compoundDrawablesRelative[1], compoundDrawablesRelative[2], compoundDrawablesRelative[3]);
        }
    }

    @Override // android.widget.TextView
    public final void setTextColor(ColorStateList colorStateList) {
        super.setTextColor(colorStateList);
        tintDrawables();
    }

    public final void tintDrawables() {
        ColorStateList textColors = getTextColors();
        if (textColors != null) {
            Drawable[] compoundDrawables = getCompoundDrawables();
            Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
            Drawable[] drawableArr = {compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], compoundDrawables[3], compoundDrawablesRelative[0], compoundDrawablesRelative[2]};
            for (int i = 0; i < 6; i++) {
                Drawable drawable = drawableArr[i];
                if (drawable instanceof TintedDrawable) {
                    TintedDrawable tintedDrawable = (TintedDrawable) drawable;
                    Objects.requireNonNull(tintedDrawable);
                    tintedDrawable.tintList = textColors;
                    tintedDrawable.setColorFilter(textColors.getColorForState(tintedDrawable.getState(), 0), PorterDuff.Mode.SRC_IN);
                    tintedDrawable.invalidateSelf();
                }
            }
            invalidate();
        }
    }
}
