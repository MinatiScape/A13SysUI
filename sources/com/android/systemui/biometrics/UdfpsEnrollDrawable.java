package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
/* loaded from: classes.dex */
public final class UdfpsEnrollDrawable extends UdfpsDrawable {
    public final Paint mBlueFill;
    public float mCurrentX;
    public float mCurrentY;
    public UdfpsEnrollHelper mEnrollHelper;
    public final Drawable mMovingTargetFpIcon;
    public final Paint mSensorOutlinePaint;
    public RectF mSensorRect;
    public AnimatorSet mTargetAnimatorSet;
    public float mCurrentScale = 1.0f;
    public boolean mShouldShowTipHint = false;
    public boolean mShouldShowEdgeHint = false;
    public final AnonymousClass1 mTargetAnimListener = new Animator.AnimatorListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable.1
        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationStart(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            UdfpsEnrollDrawable.this.updateTipHintVisibility();
        }
    };

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        if (!this.isIlluminationShowing) {
            UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
            if (udfpsEnrollHelper == null || udfpsEnrollHelper.isCenterEnrollmentStage()) {
                RectF rectF = this.mSensorRect;
                if (rectF != null) {
                    canvas.drawOval(rectF, this.mSensorOutlinePaint);
                }
                this.fingerprintDrawable.draw(canvas);
                this.fingerprintDrawable.setAlpha(this._alpha);
                this.mSensorOutlinePaint.setAlpha(this._alpha);
                return;
            }
            canvas.save();
            canvas.translate(this.mCurrentX, this.mCurrentY);
            RectF rectF2 = this.mSensorRect;
            if (rectF2 != null) {
                float f = this.mCurrentScale;
                canvas.scale(f, f, rectF2.centerX(), this.mSensorRect.centerY());
                canvas.drawOval(this.mSensorRect, this.mBlueFill);
            }
            this.mMovingTargetFpIcon.draw(canvas);
            canvas.restore();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0027, code lost:
        if (r0 != false) goto L_0x002b;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateTipHintVisibility() {
        /*
            r6 = this;
            com.android.systemui.biometrics.UdfpsEnrollHelper r0 = r6.mEnrollHelper
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x002a
            java.util.Objects.requireNonNull(r0)
            int r3 = r0.mTotalSteps
            r4 = -1
            if (r3 == r4) goto L_0x0026
            int r5 = r0.mRemainingSteps
            if (r5 != r4) goto L_0x0013
            goto L_0x0026
        L_0x0013:
            int r4 = r3 - r5
            int r3 = r0.getStageThresholdSteps(r3, r1)
            if (r4 < r3) goto L_0x0026
            int r3 = r0.mTotalSteps
            r5 = 2
            int r0 = r0.getStageThresholdSteps(r3, r5)
            if (r4 >= r0) goto L_0x0026
            r0 = r1
            goto L_0x0027
        L_0x0026:
            r0 = r2
        L_0x0027:
            if (r0 == 0) goto L_0x002a
            goto L_0x002b
        L_0x002a:
            r1 = r2
        L_0x002b:
            boolean r0 = r6.mShouldShowTipHint
            if (r0 != r1) goto L_0x0030
            return
        L_0x0030:
            r6.mShouldShowTipHint = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsEnrollDrawable.updateTipHintVisibility():void");
    }

    /* JADX WARN: Type inference failed for: r6v2, types: [com.android.systemui.biometrics.UdfpsEnrollDrawable$1] */
    public UdfpsEnrollDrawable(Context context) {
        super(context);
        new Handler(Looper.getMainLooper());
        Paint paint = new Paint(0);
        this.mSensorOutlinePaint = paint;
        paint.setAntiAlias(true);
        paint.setColor(context.getColor(2131100778));
        paint.setStyle(Paint.Style.FILL);
        Paint paint2 = new Paint(0);
        this.mBlueFill = paint2;
        paint2.setAntiAlias(true);
        paint2.setColor(context.getColor(2131100778));
        paint2.setStyle(Paint.Style.FILL);
        Drawable drawable = context.getResources().getDrawable(2131232017, null);
        this.mMovingTargetFpIcon = drawable;
        drawable.setTint(context.getColor(2131100775));
        drawable.mutate();
        this.fingerprintDrawable.setTint(context.getColor(2131100775));
    }

    @Override // com.android.systemui.biometrics.UdfpsDrawable
    public final void onSensorRectUpdated(RectF rectF) {
        super.onSensorRectUpdated(rectF);
        this.mSensorRect = rectF;
    }

    @Override // com.android.systemui.biometrics.UdfpsDrawable, android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        super.setAlpha(i);
        this.mSensorOutlinePaint.setAlpha(i);
        this.mBlueFill.setAlpha(i);
        this.mMovingTargetFpIcon.setAlpha(i);
        invalidateSelf();
    }

    @Override // com.android.systemui.biometrics.UdfpsDrawable
    public final void updateFingerprintIconBounds(Rect rect) {
        super.updateFingerprintIconBounds(rect);
        this.mMovingTargetFpIcon.setBounds(rect);
        invalidateSelf();
    }
}
