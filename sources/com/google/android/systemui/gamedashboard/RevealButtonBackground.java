package com.google.android.systemui.gamedashboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
/* loaded from: classes.dex */
public final class RevealButtonBackground extends Drawable {
    public final Paint mOvalBgPaint;

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        this.mOvalBgPaint.setAlpha((i * 153) / 255);
    }

    public RevealButtonBackground() {
        Paint paint = new Paint(3);
        this.mOvalBgPaint = paint;
        paint.setColor(Color.argb(153, 0, 0, 0));
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        Rect bounds = getBounds();
        float width = bounds.width() * 0.5f;
        canvas.drawRoundRect(bounds.left, bounds.top, bounds.right, bounds.bottom, width, width, this.mOvalBgPaint);
    }
}
