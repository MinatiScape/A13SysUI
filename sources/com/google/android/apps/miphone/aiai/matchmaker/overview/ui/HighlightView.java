package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.HighlightView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class HighlightView extends FrameLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Paint backgroundPaint;
    public final float highlightCornerRadius;
    public final Paint highlightPaint;
    public final ArrayList<RectF> highlights = new ArrayList<>();
    public final ArrayList listeners = new ArrayList();
    public final RectF tmpRectF = new RectF();
    public final Rect tmpRect = new Rect();
    public float highlightProgress = 0.0f;
    public float pathChangeProgress = 0.0f;

    /* loaded from: classes.dex */
    public interface TapListener {
        void onTap();
    }

    public HighlightView(Context context, AttributeSet attrs) {
        super(context, attrs, 0, 0);
        Paint paint = new Paint();
        this.backgroundPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(context.getColor(2131099802));
        float dimensionPixelSize = context.getResources().getDimensionPixelSize(2131166975);
        this.highlightCornerRadius = dimensionPixelSize;
        Paint paint2 = new Paint();
        Paint paint3 = new Paint();
        this.highlightPaint = paint3;
        paint3.setColor(context.getColor(2131099803));
        paint2.setBlendMode(BlendMode.PLUS);
        paint2.setColor(paint3.getColor());
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        paint2.setStrokeJoin(Paint.Join.ROUND);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStrokeWidth(dimensionPixelSize * 2.0f);
        setWillNotDraw(false);
    }

    static {
        new FloatProperty<HighlightView>() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.HighlightView.1
            @Override // android.util.Property
            public final Float get(Object object) {
                HighlightView highlightView = (HighlightView) object;
                Objects.requireNonNull(highlightView);
                return Float.valueOf(highlightView.highlightProgress);
            }

            @Override // android.util.FloatProperty
            public final void setValue(HighlightView object, float value) {
                HighlightView highlightView = object;
                Objects.requireNonNull(highlightView);
                highlightView.highlightProgress = value;
                highlightView.invalidate();
            }
        };
        new FloatProperty<HighlightView>() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.HighlightView.2
            @Override // android.util.Property
            public final Float get(Object object) {
                HighlightView highlightView = (HighlightView) object;
                int i = HighlightView.$r8$clinit;
                Objects.requireNonNull(highlightView);
                return Float.valueOf(highlightView.pathChangeProgress);
            }

            @Override // android.util.FloatProperty
            public final void setValue(HighlightView object, float value) {
                HighlightView highlightView = object;
                int i = HighlightView.$r8$clinit;
                Objects.requireNonNull(highlightView);
                highlightView.pathChangeProgress = value;
                highlightView.invalidate();
            }
        };
        new PathInterpolator(0.71f, 0.0f, 0.13f, 1.0f);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchHoverEvent(MotionEvent event) {
        return super.dispatchHoverEvent(event);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnTouchListener(new View.OnTouchListener() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.HighlightView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                HighlightView highlightView = HighlightView.this;
                Objects.requireNonNull(highlightView);
                if (motionEvent.getAction() != 0) {
                    return false;
                }
                highlightView.performClick();
                motionEvent.getX();
                motionEvent.getY();
                Iterator it = highlightView.listeners.iterator();
                while (it.hasNext()) {
                    ((HighlightView.TapListener) it.next()).onTap();
                }
                return true;
            }
        });
        setAlpha(0.0f);
        animate().alpha(0.2f);
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        float f;
        super.onDraw(canvas);
        if (getHeight() != 0) {
            getDrawingRect(this.tmpRect);
            this.tmpRectF.set(this.tmpRect);
            canvas.drawRoundRect(this.tmpRectF, 0.0f, 0.0f, this.backgroundPaint);
            float f2 = this.highlightProgress * 1.1f;
            for (int i = 0; i < this.highlights.size(); i++) {
                float height = (f2 - (this.highlights.get(i).top / getHeight())) * 10.0f;
                if (height < 0.0f) {
                    f = 0.0f;
                } else {
                    f = Math.min(1.0f, height);
                }
                this.highlightPaint.setAlpha(Math.round(f * 255.0f));
                float f3 = this.highlightCornerRadius;
                canvas.drawRoundRect(this.highlights.get(i), f3, f3, this.highlightPaint);
            }
        }
    }

    @Override // android.view.View
    public final void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }
}
