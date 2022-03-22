package com.android.settingslib.graph;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.DrawableWrapper;
import android.os.Handler;
import android.telephony.CellSignalStrength;
import android.util.PathParser;
import com.android.settingslib.Utils;
/* loaded from: classes.dex */
public final class SignalDrawable extends DrawableWrapper {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean mAnimating;
    public final Path mAttributionPath;
    public int mCurrentDot;
    public final float mCutoutHeightFraction;
    public final float mCutoutWidthFraction;
    public final int mIntrinsicSize;
    public final Paint mTransparentPaint;
    public final Paint mForegroundPaint = new Paint(1);
    public final Path mCutoutPath = new Path();
    public final Path mForegroundPath = new Path();
    public final Matrix mAttributionScaleMatrix = new Matrix();
    public final Path mScaledAttributionPath = new Path();
    public float mDarkIntensity = -1.0f;
    public final AnonymousClass1 mChangeDot = new AnonymousClass1();
    public final Handler mHandler = new Handler();

    /* renamed from: com.android.settingslib.graph.SignalDrawable$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        public AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SignalDrawable signalDrawable = SignalDrawable.this;
            int i = signalDrawable.mCurrentDot + 1;
            signalDrawable.mCurrentDot = i;
            if (i == 3) {
                signalDrawable.mCurrentDot = 0;
            }
            signalDrawable.invalidateSelf();
            SignalDrawable signalDrawable2 = SignalDrawable.this;
            signalDrawable2.mHandler.postDelayed(signalDrawable2.mChangeDot, 1000L);
        }
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        boolean z;
        boolean z2;
        canvas.saveLayer(null, null);
        float width = getBounds().width();
        float height = getBounds().height();
        boolean z3 = false;
        if (getLayoutDirection() == 1) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            canvas.save();
            canvas.translate(width, 0.0f);
            canvas.scale(-1.0f, 1.0f);
        }
        super.draw(canvas);
        this.mCutoutPath.reset();
        this.mCutoutPath.setFillType(Path.FillType.WINDING);
        float round = Math.round(0.083333336f * width);
        if (((getLevel() & 16711680) >> 16) == 3) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            float f = 0.125f * height;
            float f2 = 0.0625f * height;
            float f3 = f2 + f;
            float f4 = (width - round) - f;
            float f5 = (height - round) - f;
            this.mForegroundPath.reset();
            drawDotAndPadding(f4, f5, f2, f, 2);
            drawDotAndPadding(f4 - f3, f5, f2, f, 1);
            drawDotAndPadding(f4 - (f3 * 2.0f), f5, f2, f, 0);
            canvas.drawPath(this.mCutoutPath, this.mTransparentPaint);
            canvas.drawPath(this.mForegroundPath, this.mForegroundPaint);
        } else {
            if (((getLevel() & 16711680) >> 16) == 2) {
                z3 = true;
            }
            if (z3) {
                float f6 = (this.mCutoutWidthFraction * width) / 24.0f;
                float f7 = (this.mCutoutHeightFraction * height) / 24.0f;
                this.mCutoutPath.moveTo(width, height);
                this.mCutoutPath.rLineTo(-f6, 0.0f);
                this.mCutoutPath.rLineTo(0.0f, -f7);
                this.mCutoutPath.rLineTo(f6, 0.0f);
                this.mCutoutPath.rLineTo(0.0f, f7);
                canvas.drawPath(this.mCutoutPath, this.mTransparentPaint);
                canvas.drawPath(this.mScaledAttributionPath, this.mForegroundPaint);
            }
        }
        if (z) {
            canvas.restore();
        }
        canvas.restore();
    }

    public final void drawDotAndPadding(float f, float f2, float f3, float f4, int i) {
        if (i == this.mCurrentDot) {
            float f5 = f + f4;
            float f6 = f2 + f4;
            this.mForegroundPath.addRect(f, f2, f5, f6, Path.Direction.CW);
            this.mCutoutPath.addRect(f - f3, f2 - f3, f5 + f3, f6 + f3, Path.Direction.CW);
        }
    }

    public SignalDrawable(Context context) {
        super(context.getDrawable(17302845));
        Paint paint = new Paint(1);
        this.mTransparentPaint = paint;
        Path path = new Path();
        this.mAttributionPath = path;
        path.set(PathParser.createPathFromPathData(context.getString(17040023)));
        updateScaledAttributionPath();
        this.mCutoutWidthFraction = context.getResources().getFloat(17105113);
        this.mCutoutHeightFraction = context.getResources().getFloat(17105112);
        int colorStateListDefaultColor = Utils.getColorStateListDefaultColor(context, 2131099796);
        int colorStateListDefaultColor2 = Utils.getColorStateListDefaultColor(context, 2131099958);
        this.mIntrinsicSize = context.getResources().getDimensionPixelSize(2131167016);
        paint.setColor(context.getColor(17170445));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        if (0.0f != this.mDarkIntensity) {
            setTintList(ColorStateList.valueOf(((Integer) ArgbEvaluator.getInstance().evaluate(0.0f, Integer.valueOf(colorStateListDefaultColor2), Integer.valueOf(colorStateListDefaultColor))).intValue()));
        }
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        updateScaledAttributionPath();
        invalidateSelf();
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final boolean onLevelChange(int i) {
        int i2;
        if (((65280 & i) >> 8) == CellSignalStrength.getNumSignalStrengthLevels() + 1) {
            i2 = 10;
        } else {
            i2 = 0;
        }
        super.onLevelChange((i & 255) + i2);
        updateAnimation();
        setTintList(ColorStateList.valueOf(this.mForegroundPaint.getColor()));
        invalidateSelf();
        return true;
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        super.setAlpha(i);
        this.mForegroundPaint.setAlpha(i);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
        super.setColorFilter(colorFilter);
        this.mForegroundPaint.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void setTintList(ColorStateList colorStateList) {
        super.setTintList(colorStateList);
        int color = this.mForegroundPaint.getColor();
        this.mForegroundPaint.setColor(colorStateList.getDefaultColor());
        if (color != this.mForegroundPaint.getColor()) {
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final boolean setVisible(boolean z, boolean z2) {
        boolean visible = super.setVisible(z, z2);
        updateAnimation();
        return visible;
    }

    public final void updateAnimation() {
        boolean z;
        boolean z2 = true;
        if (((getLevel() & 16711680) >> 16) == 3) {
            z = true;
        } else {
            z = false;
        }
        if (!z || !isVisible()) {
            z2 = false;
        }
        if (z2 != this.mAnimating) {
            this.mAnimating = z2;
            if (z2) {
                this.mChangeDot.run();
            } else {
                this.mHandler.removeCallbacks(this.mChangeDot);
            }
        }
    }

    public final void updateScaledAttributionPath() {
        if (getBounds().isEmpty()) {
            this.mAttributionScaleMatrix.setScale(1.0f, 1.0f);
        } else {
            this.mAttributionScaleMatrix.setScale(getBounds().width() / 24.0f, getBounds().height() / 24.0f);
        }
        this.mAttributionPath.transform(this.mAttributionScaleMatrix, this.mScaledAttributionPath);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final int getIntrinsicHeight() {
        return this.mIntrinsicSize;
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final int getIntrinsicWidth() {
        return this.mIntrinsicSize;
    }
}
