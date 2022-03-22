package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.leanback.R$string;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LinearDrawingDelegate extends DrawingDelegate<LinearProgressIndicatorSpec> {
    public float displayedCornerRadius;
    public float displayedTrackThickness;
    public float trackLength = 300.0f;

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public final int getPreferredWidth() {
        return -1;
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public final void fillIndicator(Canvas canvas, Paint paint, float f, float f2, int i) {
        if (f != f2) {
            float f3 = this.trackLength;
            float f4 = (-f3) / 2.0f;
            float f5 = this.displayedCornerRadius * 2.0f;
            float f6 = f3 - f5;
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setColor(i);
            float f7 = this.displayedTrackThickness;
            RectF rectF = new RectF((f * f6) + f4, (-f7) / 2.0f, (f6 * f2) + f4 + f5, f7 / 2.0f);
            float f8 = this.displayedCornerRadius;
            canvas.drawRoundRect(rectF, f8, f8, paint);
        }
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public final void fillTrack(Canvas canvas, Paint paint) {
        int i = ((LinearProgressIndicatorSpec) this.spec).trackColor;
        DrawableWithAnimatedVisibilityChange drawableWithAnimatedVisibilityChange = this.drawable;
        Objects.requireNonNull(drawableWithAnimatedVisibilityChange);
        int compositeARGBWithAlpha = R$string.compositeARGBWithAlpha(i, drawableWithAnimatedVisibilityChange.totalAlpha);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(compositeARGBWithAlpha);
        float f = this.trackLength;
        float f2 = this.displayedTrackThickness;
        RectF rectF = new RectF((-f) / 2.0f, (-f2) / 2.0f, f / 2.0f, f2 / 2.0f);
        float f3 = this.displayedCornerRadius;
        canvas.drawRoundRect(rectF, f3, f3, paint);
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public final int getPreferredHeight() {
        return ((LinearProgressIndicatorSpec) this.spec).trackThickness;
    }

    public LinearDrawingDelegate(LinearProgressIndicatorSpec linearProgressIndicatorSpec) {
        super(linearProgressIndicatorSpec);
    }

    @Override // com.google.android.material.progressindicator.DrawingDelegate
    public final void adjustCanvas(Canvas canvas, float f) {
        Rect clipBounds = canvas.getClipBounds();
        this.trackLength = clipBounds.width();
        float f2 = ((LinearProgressIndicatorSpec) this.spec).trackThickness;
        canvas.translate((clipBounds.width() / 2.0f) + clipBounds.left, Math.max(0.0f, (clipBounds.height() - ((LinearProgressIndicatorSpec) this.spec).trackThickness) / 2.0f) + (clipBounds.height() / 2.0f) + clipBounds.top);
        if (((LinearProgressIndicatorSpec) this.spec).drawHorizontallyInverse) {
            canvas.scale(-1.0f, 1.0f);
        }
        if ((this.drawable.isShowing() && ((LinearProgressIndicatorSpec) this.spec).showAnimationBehavior == 1) || (this.drawable.isHiding() && ((LinearProgressIndicatorSpec) this.spec).hideAnimationBehavior == 2)) {
            canvas.scale(1.0f, -1.0f);
        }
        if (this.drawable.isShowing() || this.drawable.isHiding()) {
            canvas.translate(0.0f, ((f - 1.0f) * ((LinearProgressIndicatorSpec) this.spec).trackThickness) / 2.0f);
        }
        float f3 = this.trackLength;
        canvas.clipRect((-f3) / 2.0f, (-f2) / 2.0f, f3 / 2.0f, f2 / 2.0f);
        S s = this.spec;
        this.displayedTrackThickness = ((LinearProgressIndicatorSpec) s).trackThickness * f;
        this.displayedCornerRadius = ((LinearProgressIndicatorSpec) s).trackCornerRadius * f;
    }
}
