package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DimView extends FrameLayout {
    public final Paint backgroundPaint;
    public final RectF tmpRectF = new RectF();
    public final Rect tmpRect = new Rect();

    public DimView(Context context, AttributeSet attrs) {
        super(context, attrs, 0, 0);
        Paint paint = new Paint();
        this.backgroundPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0);
        setWillNotDraw(false);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnTouchListener(new View.OnTouchListener() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.DimView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                Objects.requireNonNull(DimView.this);
                if (motionEvent.getAction() != 0) {
                    return false;
                }
                Log.i("AiAiSuggestUi", "Handle touch for the background scrim.");
                return true;
            }
        });
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getHeight() != 0) {
            getDrawingRect(this.tmpRect);
            this.tmpRectF.set(this.tmpRect);
            canvas.drawRoundRect(this.tmpRectF, 0.0f, 0.0f, this.backgroundPaint);
        }
    }
}
