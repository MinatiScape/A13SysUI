package com.google.android.material.timepicker;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import com.google.android.material.R$styleable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
class ClockHandView extends View {
    public final float centerDotRadius;
    public boolean changedDuringTouch;
    public int circleRadius;
    public double degRad;
    public float downX;
    public float downY;
    public final ArrayList listeners;
    public float originalDeg;
    public final Paint paint;
    public ValueAnimator rotationAnimator;
    public int scaledTouchSlop;
    public final RectF selectorBox;
    public final int selectorRadius;
    public final int selectorStrokeWidth;

    /* loaded from: classes.dex */
    public interface OnRotateListener {
        void onRotate(float f);
    }

    public ClockHandView(Context context) {
        this(context, null);
    }

    public ClockHandView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130969436);
    }

    public final void setHandRotation$1(float f) {
        ValueAnimator valueAnimator = this.rotationAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        setHandRotationInternal(f, false);
    }

    public final void setHandRotationInternal(float f, boolean z) {
        float f2 = f % 360.0f;
        this.originalDeg = f2;
        this.degRad = Math.toRadians(f2 - 90.0f);
        float cos = (this.circleRadius * ((float) Math.cos(this.degRad))) + (getWidth() / 2);
        float sin = (this.circleRadius * ((float) Math.sin(this.degRad))) + (getHeight() / 2);
        RectF rectF = this.selectorBox;
        float f3 = this.selectorRadius;
        rectF.set(cos - f3, sin - f3, cos + f3, sin + f3);
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((OnRotateListener) it.next()).onRotate(f2);
        }
        invalidate();
    }

    public ClockHandView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.listeners = new ArrayList();
        Paint paint = new Paint();
        this.paint = paint;
        this.selectorBox = new RectF();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ClockHandView, i, 2132018724);
        this.circleRadius = obtainStyledAttributes.getDimensionPixelSize(1, 0);
        this.selectorRadius = obtainStyledAttributes.getDimensionPixelSize(2, 0);
        Resources resources = getResources();
        this.selectorStrokeWidth = resources.getDimensionPixelSize(2131166302);
        this.centerDotRadius = resources.getDimensionPixelSize(2131166300);
        int color = obtainStyledAttributes.getColor(0, 0);
        paint.setAntiAlias(true);
        paint.setColor(color);
        setHandRotation$1(0.0f);
        this.scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api16Impl.setImportantForAccessibility(this, 2);
        obtainStyledAttributes.recycle();
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        int width;
        super.onDraw(canvas);
        int height = getHeight() / 2;
        float width2 = getWidth() / 2;
        float f = height;
        this.paint.setStrokeWidth(0.0f);
        canvas.drawCircle((this.circleRadius * ((float) Math.cos(this.degRad))) + width2, (this.circleRadius * ((float) Math.sin(this.degRad))) + f, this.selectorRadius, this.paint);
        double sin = Math.sin(this.degRad);
        double cos = Math.cos(this.degRad);
        this.paint.setStrokeWidth(this.selectorStrokeWidth);
        canvas.drawLine(width2, f, width + ((int) (cos * r6)), height + ((int) (r6 * sin)), this.paint);
        canvas.drawCircle(width2, f, this.centerDotRadius, this.paint);
    }

    @Override // android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setHandRotation$1(this.originalDeg);
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        boolean z2;
        boolean z3;
        int actionMasked = motionEvent.getActionMasked();
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        boolean z4 = false;
        if (actionMasked == 0) {
            this.downX = x;
            this.downY = y;
            this.changedDuringTouch = false;
            z2 = false;
            z = true;
        } else if (actionMasked == 1 || actionMasked == 2) {
            z2 = this.changedDuringTouch;
            z = false;
        } else {
            z2 = false;
            z = false;
        }
        boolean z5 = this.changedDuringTouch;
        int degrees = ((int) Math.toDegrees(Math.atan2(y - (getHeight() / 2), x - (getWidth() / 2)))) + 90;
        if (degrees < 0) {
            degrees += 360;
        }
        float f = degrees;
        if (this.originalDeg != f) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (!z || !z3) {
            if (z3 || z2) {
                setHandRotation$1(f);
            }
            this.changedDuringTouch = z5 | z4;
            return true;
        }
        z4 = true;
        this.changedDuringTouch = z5 | z4;
        return true;
    }
}
