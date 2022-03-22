package com.android.systemui.statusbar;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class CastDrawable extends DrawableWrapper {
    public Drawable mFillDrawable;
    public int mHorizontalPadding;

    public CastDrawable() {
        super(null);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final boolean canApplyTheme() {
        if (this.mFillDrawable.canApplyTheme() || super.canApplyTheme()) {
            return true;
        }
        return false;
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final boolean getPadding(Rect rect) {
        int i = rect.left;
        int i2 = this.mHorizontalPadding;
        rect.left = i + i2;
        rect.right += i2;
        return true;
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final Drawable mutate() {
        this.mFillDrawable.mutate();
        return super.mutate();
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final boolean onLayoutDirectionChanged(int i) {
        this.mFillDrawable.setLayoutDirection(i);
        return super.onLayoutDirectionChanged(i);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final boolean setVisible(boolean z, boolean z2) {
        this.mFillDrawable.setVisible(z, z2);
        return super.setVisible(z, z2);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
        this.mFillDrawable.applyTheme(theme);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        super.draw(canvas);
        this.mFillDrawable.draw(canvas);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        super.inflate(resources, xmlPullParser, attributeSet, theme);
        setDrawable(resources.getDrawable(2131231778, theme).mutate());
        this.mFillDrawable = resources.getDrawable(2131231780, theme).mutate();
        this.mHorizontalPadding = resources.getDimensionPixelSize(2131167061);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mFillDrawable.setBounds(rect);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        super.setAlpha(i);
        this.mFillDrawable.setAlpha(i);
    }
}
