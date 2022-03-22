package com.android.systemui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.hardware.graphics.common.DisplayDecorationSupport;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ScreenDecorHwcLayer.kt */
/* loaded from: classes.dex */
public final class ScreenDecorHwcLayer extends DisplayCutoutBaseView {
    public static final boolean DEBUG_COLOR = ScreenDecorations.DEBUG_COLOR;
    public final int bgColor;
    public final Paint clearPaint;
    public final int color;
    public final PorterDuffColorFilter cornerBgFilter;
    public final PorterDuffColorFilter cornerFilter;
    public int roundedCornerBottomSize;
    public Drawable roundedCornerDrawableBottom;
    public Drawable roundedCornerDrawableTop;
    public int roundedCornerTopSize;
    public final boolean useInvertedAlphaColor;

    public final void drawRoundedCorner(Canvas canvas, Drawable drawable, int i) {
        if (this.useInvertedAlphaColor) {
            float f = i;
            canvas.drawRect(0.0f, 0.0f, f, f, this.clearPaint);
            if (drawable != null) {
                drawable.setColorFilter(this.cornerBgFilter);
            }
        } else if (drawable != null) {
            drawable.setColorFilter(this.cornerFilter);
        }
        if (drawable != null) {
            drawable.draw(canvas);
        }
        if (drawable != null) {
            drawable.clearColorFilter();
        }
    }

    @Override // com.android.systemui.DisplayCutoutBaseView, android.view.View
    public final void onDraw(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        if (this.useInvertedAlphaColor) {
            canvas.drawColor(this.bgColor);
        }
        super.onDraw(canvas);
        if (this.roundedCornerTopSize != 0 || this.roundedCornerBottomSize != 0) {
            int i5 = 0;
            while (i5 < 4) {
                int i6 = i5 + 1;
                canvas.save();
                int i7 = (((i5 * 90) - (this.displayRotation * 90)) + 360) % 360;
                canvas.rotate(i7);
                if (i7 == 0 || i7 == 90) {
                    i = 0;
                } else {
                    if (i7 == 180) {
                        i4 = getWidth();
                    } else if (i7 == 270) {
                        i4 = getHeight();
                    } else {
                        throw new IllegalArgumentException(Intrinsics.stringPlus("Incorrect degree: ", Integer.valueOf(i7)));
                    }
                    i = -i4;
                }
                float f = i;
                if (i7 != 0) {
                    if (i7 == 90) {
                        i3 = getWidth();
                    } else if (i7 == 180) {
                        i3 = getHeight();
                    } else if (i7 != 270) {
                        throw new IllegalArgumentException(Intrinsics.stringPlus("Incorrect degree: ", Integer.valueOf(i7)));
                    }
                    i2 = -i3;
                    canvas.translate(f, i2);
                    if (i5 != 0 || i5 == 1) {
                        drawRoundedCorner(canvas, this.roundedCornerDrawableTop, this.roundedCornerTopSize);
                    } else {
                        drawRoundedCorner(canvas, this.roundedCornerDrawableBottom, this.roundedCornerBottomSize);
                    }
                    canvas.restore();
                    i5 = i6;
                }
                i2 = 0;
                canvas.translate(f, i2);
                if (i5 != 0) {
                }
                drawRoundedCorner(canvas, this.roundedCornerDrawableTop, this.roundedCornerTopSize);
                canvas.restore();
                i5 = i6;
            }
        }
    }

    public final void updateRoundedCornerDrawableBounds() {
        Drawable drawable = this.roundedCornerDrawableTop;
        if (!(drawable == null || drawable == null)) {
            int i = this.roundedCornerTopSize;
            drawable.setBounds(0, 0, i, i);
        }
        Drawable drawable2 = this.roundedCornerDrawableBottom;
        if (!(drawable2 == null || drawable2 == null)) {
            int i2 = this.roundedCornerBottomSize;
            drawable2.setBounds(0, 0, i2, i2);
        }
        invalidate();
    }

    public ScreenDecorHwcLayer(Context context, DisplayDecorationSupport displayDecorationSupport) {
        super(context);
        boolean z;
        if (displayDecorationSupport.format == 56) {
            if (DEBUG_COLOR) {
                this.color = -16711936;
                this.bgColor = 0;
                this.useInvertedAlphaColor = false;
            } else {
                if (displayDecorationSupport.alphaInterpretation == 0) {
                    z = true;
                } else {
                    z = false;
                }
                this.useInvertedAlphaColor = z;
                if (z) {
                    this.color = 0;
                    this.bgColor = -16777216;
                } else {
                    this.color = -16777216;
                    this.bgColor = 0;
                }
            }
            this.cornerFilter = new PorterDuffColorFilter(this.color, PorterDuff.Mode.SRC_IN);
            this.cornerBgFilter = new PorterDuffColorFilter(this.bgColor, PorterDuff.Mode.SRC_OUT);
            Paint paint = new Paint();
            this.clearPaint = paint;
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            return;
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("Attempting to use unsupported mode ", PixelFormat.formatToString(displayDecorationSupport.format)));
    }

    @Override // com.android.systemui.DisplayCutoutBaseView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewRootImpl().setDisplayDecoration(true);
        if (this.useInvertedAlphaColor) {
            this.paint.set(this.clearPaint);
            return;
        }
        this.paint.setColor(this.color);
        this.paint.setStyle(Paint.Style.FILL);
    }
}
