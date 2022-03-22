package com.android.systemui.wallet.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.cardview.widget.CardView;
import androidx.cardview.widget.RoundRectDrawable;
import java.util.Objects;
/* loaded from: classes.dex */
public class WalletCardView extends CardView {
    public final Paint mBorderPaint;

    public WalletCardView(Context context) {
        this(context, null);
    }

    public WalletCardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mBorderPaint = paint;
        paint.setColor(context.getColor(2131100785));
        paint.setStrokeWidth(context.getResources().getDimension(2131167315));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override // android.view.View
    public final void draw(Canvas canvas) {
        super.draw(canvas);
        CardView.AnonymousClass1 r0 = this.mCardViewDelegate;
        Objects.requireNonNull(r0);
        RoundRectDrawable roundRectDrawable = (RoundRectDrawable) r0.mCardBackground;
        Objects.requireNonNull(roundRectDrawable);
        float f = roundRectDrawable.mRadius;
        canvas.drawRoundRect(0.0f, 0.0f, getWidth(), getHeight(), f, f, this.mBorderPaint);
    }
}
