package com.android.systemui.navigationbar.gestural;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import com.android.settingslib.Utils;
import com.android.systemui.navigationbar.buttons.ButtonInterface;
/* loaded from: classes.dex */
public class NavigationHandle extends View implements ButtonInterface {
    public final int mBottom;
    public final int mDarkColor;
    public final int mLightColor;
    public final Paint mPaint;
    public final int mRadius;
    public boolean mRequiresInvalidate;

    public NavigationHandle(Context context) {
        this(context, null);
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public final void abortCurrentGesture() {
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public final void setImageDrawable(Drawable drawable) {
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public final void setVertical(boolean z) {
    }

    public NavigationHandle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mPaint = paint;
        Resources resources = context.getResources();
        this.mRadius = resources.getDimensionPixelSize(2131166605);
        this.mBottom = resources.getDimensionPixelSize(2131166604);
        int themeAttr = Utils.getThemeAttr(context, 2130968913);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, Utils.getThemeAttr(context, 2130969364));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, themeAttr);
        this.mLightColor = Utils.getColorAttrDefaultColor(contextThemeWrapper, 2130969200);
        this.mDarkColor = Utils.getColorAttrDefaultColor(contextThemeWrapper2, 2130969200);
        paint.setAntiAlias(true);
        setFocusable(false);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int i = this.mRadius * 2;
        int width = getWidth();
        int i2 = (height - this.mBottom) - i;
        float f = i2 + i;
        int i3 = this.mRadius;
        canvas.drawRoundRect(0.0f, i2, width, f, i3, i3, this.mPaint);
    }

    @Override // android.view.View
    public final void setAlpha(float f) {
        super.setAlpha(f);
        if (f > 0.0f && this.mRequiresInvalidate) {
            this.mRequiresInvalidate = false;
            invalidate();
        }
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public final void setDarkIntensity(float f) {
        int intValue = ((Integer) ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(this.mLightColor), Integer.valueOf(this.mDarkColor))).intValue();
        if (this.mPaint.getColor() != intValue) {
            this.mPaint.setColor(intValue);
            if (getVisibility() != 0 || getAlpha() <= 0.0f) {
                this.mRequiresInvalidate = true;
            } else {
                invalidate();
            }
        }
    }
}
