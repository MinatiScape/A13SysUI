package com.google.android.material.circularreveal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.GridLayout;
import com.google.android.material.circularreveal.CircularRevealWidget;
import java.util.Objects;
/* loaded from: classes.dex */
public class CircularRevealGridLayout extends GridLayout implements CircularRevealWidget {
    public final CircularRevealHelper helper = new CircularRevealHelper(this);

    @Override // com.google.android.material.circularreveal.CircularRevealWidget
    public final void buildCircularRevealCache() {
        Objects.requireNonNull(this.helper);
    }

    @Override // com.google.android.material.circularreveal.CircularRevealWidget
    public final void destroyCircularRevealCache() {
        Objects.requireNonNull(this.helper);
    }

    @Override // android.view.View
    public final void draw(Canvas canvas) {
        CircularRevealHelper circularRevealHelper = this.helper;
        if (circularRevealHelper != null) {
            circularRevealHelper.draw(canvas);
        } else {
            super.draw(canvas);
        }
    }

    @Override // com.google.android.material.circularreveal.CircularRevealWidget
    public final int getCircularRevealScrimColor() {
        return this.helper.getCircularRevealScrimColor();
    }

    @Override // com.google.android.material.circularreveal.CircularRevealWidget
    public final CircularRevealWidget.RevealInfo getRevealInfo() {
        return this.helper.getRevealInfo();
    }

    @Override // android.view.View
    public final boolean isOpaque() {
        CircularRevealHelper circularRevealHelper = this.helper;
        if (circularRevealHelper != null) {
            return circularRevealHelper.isOpaque();
        }
        return super.isOpaque();
    }

    @Override // com.google.android.material.circularreveal.CircularRevealWidget
    public final void setCircularRevealOverlayDrawable(Drawable drawable) {
        this.helper.setCircularRevealOverlayDrawable(drawable);
    }

    @Override // com.google.android.material.circularreveal.CircularRevealWidget
    public final void setCircularRevealScrimColor(int i) {
        this.helper.setCircularRevealScrimColor(i);
    }

    @Override // com.google.android.material.circularreveal.CircularRevealWidget
    public final void setRevealInfo(CircularRevealWidget.RevealInfo revealInfo) {
        this.helper.setRevealInfo(revealInfo);
    }

    public CircularRevealGridLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.google.android.material.circularreveal.CircularRevealHelper.Delegate
    public final boolean actualIsOpaque() {
        return super.isOpaque();
    }

    @Override // com.google.android.material.circularreveal.CircularRevealHelper.Delegate
    public final void actualDraw(Canvas canvas) {
        super.draw(canvas);
    }
}
