package com.android.systemui.util;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;
import com.android.systemui.R$styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class AlphaTintDrawableWrapper extends InsetDrawable {
    public int[] mThemeAttrs;
    public ColorStateList mTint;

    /* loaded from: classes.dex */
    public static class AlphaTintState extends Drawable.ConstantState {
        public int mAlpha;
        public ColorStateList mColorStateList;
        public int[] mThemeAttrs;
        public Drawable.ConstantState mWrappedState;

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final boolean canApplyTheme() {
            return true;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final Drawable newDrawable() {
            return newDrawable(null, null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final int getChangingConfigurations() {
            return this.mWrappedState.getChangingConfigurations();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final Drawable newDrawable(Resources resources, Resources.Theme theme) {
            AlphaTintDrawableWrapper alphaTintDrawableWrapper = new AlphaTintDrawableWrapper(((DrawableWrapper) this.mWrappedState.newDrawable(resources, theme)).getDrawable(), this.mThemeAttrs);
            alphaTintDrawableWrapper.setTintList(this.mColorStateList);
            alphaTintDrawableWrapper.setAlpha(this.mAlpha);
            return alphaTintDrawableWrapper;
        }

        public AlphaTintState(Drawable.ConstantState constantState, int[] iArr, int i, ColorStateList colorStateList) {
            this.mWrappedState = constantState;
            this.mThemeAttrs = iArr;
            this.mAlpha = i;
            this.mColorStateList = colorStateList;
        }
    }

    public AlphaTintDrawableWrapper() {
        super((Drawable) null, 0);
    }

    public final void updateStateFromTypedArray(TypedArray typedArray) {
        if (typedArray.hasValue(0)) {
            this.mTint = typedArray.getColorStateList(0);
        }
        if (typedArray.hasValue(1)) {
            setAlpha(Math.round(typedArray.getFloat(1, 1.0f) * 255.0f));
        }
    }

    public AlphaTintDrawableWrapper(Drawable drawable, int[] iArr) {
        super(drawable, 0);
        this.mThemeAttrs = iArr;
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final boolean canApplyTheme() {
        int[] iArr = this.mThemeAttrs;
        if ((iArr == null || iArr.length <= 0) && !super.canApplyTheme()) {
            return false;
        }
        return true;
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final Drawable.ConstantState getConstantState() {
        return new AlphaTintState(super.getConstantState(), this.mThemeAttrs, getAlpha(), this.mTint);
    }

    @Override // android.graphics.drawable.InsetDrawable, android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        TypedArray obtainAttributes = InsetDrawable.obtainAttributes(resources, theme, attributeSet, R$styleable.AlphaTintDrawableWrapper);
        super.inflate(resources, xmlPullParser, attributeSet, theme);
        this.mThemeAttrs = obtainAttributes.extractThemeAttrs();
        updateStateFromTypedArray(obtainAttributes);
        obtainAttributes.recycle();
        if (getDrawable() != null && this.mTint != null) {
            getDrawable().mutate().setTintList(this.mTint);
        }
    }

    @Override // android.graphics.drawable.InsetDrawable, android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
        int[] iArr = this.mThemeAttrs;
        if (!(iArr == null || theme == null)) {
            TypedArray resolveAttributes = theme.resolveAttributes(iArr, R$styleable.AlphaTintDrawableWrapper);
            updateStateFromTypedArray(resolveAttributes);
            resolveAttributes.recycle();
        }
        if (getDrawable() != null && this.mTint != null) {
            getDrawable().mutate().setTintList(this.mTint);
        }
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void setTintList(ColorStateList colorStateList) {
        super.setTintList(colorStateList);
        this.mTint = colorStateList;
    }
}
