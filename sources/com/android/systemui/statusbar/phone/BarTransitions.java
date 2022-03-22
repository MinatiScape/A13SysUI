package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.View;
import com.android.settingslib.Utils;
import com.android.systemui.animation.Interpolators;
import java.util.Objects;
/* loaded from: classes.dex */
public class BarTransitions {
    public final BarBackgroundDrawable mBarBackground;
    public int mMode;
    public final View mView;

    /* loaded from: classes.dex */
    public static class BarBackgroundDrawable extends Drawable {
        public boolean mAnimating;
        public int mColor;
        public int mColorStart;
        public long mEndTime;
        public Rect mFrame;
        public final Drawable mGradient;
        public int mGradientAlpha;
        public int mGradientAlphaStart;
        public final int mOpaque;
        public final int mSemiTransparent;
        public long mStartTime;
        public PorterDuffColorFilter mTintFilter;
        public final int mTransparent;
        public final int mWarning;
        public int mMode = -1;
        public float mOverrideAlpha = 1.0f;
        public Paint mPaint = new Paint();

        @Override // android.graphics.drawable.Drawable
        public final int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public final void setAlpha(int i) {
        }

        @Override // android.graphics.drawable.Drawable
        public final void setColorFilter(ColorFilter colorFilter) {
        }

        @Override // android.graphics.drawable.Drawable
        public final void draw(Canvas canvas) {
            int i;
            int i2 = this.mMode;
            if (i2 == 5) {
                i = this.mWarning;
            } else if (i2 == 2) {
                i = this.mSemiTransparent;
            } else if (i2 == 1) {
                i = this.mSemiTransparent;
            } else if (i2 == 0 || i2 == 6) {
                i = this.mTransparent;
            } else {
                i = this.mOpaque;
            }
            if (!this.mAnimating) {
                this.mColor = i;
                this.mGradientAlpha = 0;
            } else {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                long j = this.mEndTime;
                if (elapsedRealtime >= j) {
                    this.mAnimating = false;
                    this.mColor = i;
                    this.mGradientAlpha = 0;
                } else {
                    long j2 = this.mStartTime;
                    float max = Math.max(0.0f, Math.min(Interpolators.LINEAR.getInterpolation(((float) (elapsedRealtime - j2)) / ((float) (j - j2))), 1.0f));
                    float f = 1.0f - max;
                    this.mGradientAlpha = (int) ((this.mGradientAlphaStart * f) + (0 * max));
                    this.mColor = Color.argb((int) ((Color.alpha(this.mColorStart) * f) + (Color.alpha(i) * max)), (int) ((Color.red(this.mColorStart) * f) + (Color.red(i) * max)), (int) ((Color.green(this.mColorStart) * f) + (Color.green(i) * max)), (int) ((Color.blue(this.mColorStart) * f) + (max * Color.blue(i))));
                }
            }
            int i3 = this.mGradientAlpha;
            if (i3 > 0) {
                this.mGradient.setAlpha(i3);
                this.mGradient.draw(canvas);
            }
            if (Color.alpha(this.mColor) > 0) {
                this.mPaint.setColor(this.mColor);
                PorterDuffColorFilter porterDuffColorFilter = this.mTintFilter;
                if (porterDuffColorFilter != null) {
                    this.mPaint.setColorFilter(porterDuffColorFilter);
                }
                this.mPaint.setAlpha((int) (Color.alpha(this.mColor) * this.mOverrideAlpha));
                Rect rect = this.mFrame;
                if (rect != null) {
                    canvas.drawRect(rect, this.mPaint);
                } else {
                    canvas.drawPaint(this.mPaint);
                }
            }
            if (this.mAnimating) {
                invalidateSelf();
            }
        }

        @Override // android.graphics.drawable.Drawable
        public final void setTint(int i) {
            PorterDuff.Mode mode;
            PorterDuffColorFilter porterDuffColorFilter = this.mTintFilter;
            if (porterDuffColorFilter == null) {
                mode = PorterDuff.Mode.SRC_IN;
            } else {
                mode = porterDuffColorFilter.getMode();
            }
            PorterDuffColorFilter porterDuffColorFilter2 = this.mTintFilter;
            if (porterDuffColorFilter2 == null || porterDuffColorFilter2.getColor() != i) {
                this.mTintFilter = new PorterDuffColorFilter(i, mode);
            }
            invalidateSelf();
        }

        @Override // android.graphics.drawable.Drawable
        public final void setTintMode(PorterDuff.Mode mode) {
            int i;
            PorterDuffColorFilter porterDuffColorFilter = this.mTintFilter;
            if (porterDuffColorFilter == null) {
                i = 0;
            } else {
                i = porterDuffColorFilter.getColor();
            }
            PorterDuffColorFilter porterDuffColorFilter2 = this.mTintFilter;
            if (porterDuffColorFilter2 == null || porterDuffColorFilter2.getMode() != mode) {
                this.mTintFilter = new PorterDuffColorFilter(i, mode);
            }
            invalidateSelf();
        }

        public BarBackgroundDrawable(Context context, int i) {
            context.getResources();
            this.mOpaque = context.getColor(2131100740);
            this.mSemiTransparent = context.getColor(17171091);
            this.mTransparent = context.getColor(2131100741);
            this.mWarning = Utils.getColorAttrDefaultColor(context, 16844099);
            this.mGradient = context.getDrawable(i);
        }

        @Override // android.graphics.drawable.Drawable
        public final void onBoundsChange(Rect rect) {
            super.onBoundsChange(rect);
            this.mGradient.setBounds(rect);
        }
    }

    public static String modeToString(int i) {
        if (i == 4) {
            return "MODE_OPAQUE";
        }
        if (i == 1) {
            return "MODE_SEMI_TRANSPARENT";
        }
        if (i == 2) {
            return "MODE_TRANSLUCENT";
        }
        if (i == 3) {
            return "MODE_LIGHTS_OUT";
        }
        if (i == 0) {
            return "MODE_TRANSPARENT";
        }
        if (i == 5) {
            return "MODE_WARNING";
        }
        if (i == 6) {
            return "MODE_LIGHTS_OUT_TRANSPARENT";
        }
        throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("Unknown mode ", i));
    }

    public final void applyModeBackground(int i, boolean z) {
        BarBackgroundDrawable barBackgroundDrawable = this.mBarBackground;
        Objects.requireNonNull(barBackgroundDrawable);
        if (barBackgroundDrawable.mMode != i) {
            barBackgroundDrawable.mMode = i;
            barBackgroundDrawable.mAnimating = z;
            if (z) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                barBackgroundDrawable.mStartTime = elapsedRealtime;
                barBackgroundDrawable.mEndTime = elapsedRealtime + 200;
                barBackgroundDrawable.mGradientAlphaStart = barBackgroundDrawable.mGradientAlpha;
                barBackgroundDrawable.mColorStart = barBackgroundDrawable.mColor;
            }
            barBackgroundDrawable.invalidateSelf();
        }
    }

    public BarTransitions(View view, int i) {
        Objects.requireNonNull(view);
        this.mView = view;
        BarBackgroundDrawable barBackgroundDrawable = new BarBackgroundDrawable(view.getContext(), i);
        this.mBarBackground = barBackgroundDrawable;
        view.setBackground(barBackgroundDrawable);
    }
}
