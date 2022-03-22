package com.android.systemui.screenshot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.R$styleable;
import com.android.systemui.screenshot.CropView;
/* loaded from: classes.dex */
public class MagnifierView extends View implements CropView.CropInteractionListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final int mBorderColor;
    public final float mBorderPx;
    public Path mCheckerboard;
    public float mCheckerboardBoxSize;
    public Paint mCheckerboardPaint;
    public CropView.CropBoundary mCropBoundary;
    public Drawable mDrawable;
    public final Paint mHandlePaint;
    public Path mInnerCircle;
    public float mLastCenter;
    public float mLastCropPosition;
    public Path mOuterCircle;
    public final Paint mShadePaint;
    public ViewPropertyAnimator mTranslationAnimator;
    public final AnonymousClass1 mTranslationAnimatorListener;

    public MagnifierView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* JADX WARN: Type inference failed for: r5v3, types: [com.android.systemui.screenshot.MagnifierView$1] */
    public MagnifierView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCheckerboardBoxSize = 40.0f;
        this.mLastCenter = 0.5f;
        this.mTranslationAnimatorListener = new AnimatorListenerAdapter() { // from class: com.android.systemui.screenshot.MagnifierView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                MagnifierView.this.mTranslationAnimator = null;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                MagnifierView.this.mTranslationAnimator = null;
            }
        };
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.MagnifierView, 0, 0);
        Paint paint = new Paint();
        this.mShadePaint = paint;
        paint.setColor(ColorUtils.setAlphaComponent(obtainStyledAttributes.getColor(5, 0), obtainStyledAttributes.getInteger(4, 255)));
        Paint paint2 = new Paint();
        this.mHandlePaint = paint2;
        paint2.setColor(obtainStyledAttributes.getColor(2, -16777216));
        paint2.setStrokeWidth(obtainStyledAttributes.getDimensionPixelSize(3, 20));
        this.mBorderPx = obtainStyledAttributes.getDimensionPixelSize(1, 0);
        this.mBorderColor = obtainStyledAttributes.getColor(0, -1);
        obtainStyledAttributes.recycle();
        Paint paint3 = new Paint();
        this.mCheckerboardPaint = paint3;
        paint3.setColor(-7829368);
    }

    public final int getParentWidth() {
        return ((View) getParent()).getWidth();
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.clipPath(this.mOuterCircle);
        canvas.drawColor(this.mBorderColor);
        canvas.clipPath(this.mInnerCircle);
        canvas.drawPath(this.mCheckerboard, this.mCheckerboardPaint);
        if (this.mDrawable != null) {
            canvas.save();
            canvas.translate(((-this.mDrawable.getBounds().width()) * this.mLastCenter) + (getWidth() / 2), ((-this.mDrawable.getBounds().height()) * this.mLastCropPosition) + (getHeight() / 2));
            this.mDrawable.draw(canvas);
            canvas.restore();
        }
        Rect rect = new Rect(0, 0, getWidth(), getHeight() / 2);
        if (this.mCropBoundary == CropView.CropBoundary.BOTTOM) {
            rect.offset(0, getHeight() / 2);
        }
        canvas.drawRect(rect, this.mShadePaint);
        canvas.drawLine(0.0f, getHeight() / 2, getWidth(), getHeight() / 2, this.mHandlePaint);
    }

    @Override // android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        super.onLayout(z, i, i2, i3, i4);
        Path path = new Path();
        this.mOuterCircle = path;
        float width = getWidth() / 2;
        path.addCircle(width, width, width, Path.Direction.CW);
        Path path2 = new Path();
        this.mInnerCircle = path2;
        path2.addCircle(width, width, width - this.mBorderPx, Path.Direction.CW);
        Path path3 = new Path();
        int ceil = (int) Math.ceil(getWidth() / this.mCheckerboardBoxSize);
        int ceil2 = (int) Math.ceil(getHeight() / this.mCheckerboardBoxSize);
        for (int i6 = 0; i6 < ceil2; i6++) {
            if (i6 % 2 == 0) {
                i5 = 0;
            } else {
                i5 = 1;
            }
            for (int i7 = i5; i7 < ceil; i7 += 2) {
                float f = this.mCheckerboardBoxSize;
                path3.addRect(i7 * f, i6 * f, (i7 + 1) * f, (i6 + 1) * f, Path.Direction.CW);
            }
        }
        this.mCheckerboard = path3;
    }
}
