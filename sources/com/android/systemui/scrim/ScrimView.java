package com.android.systemui.scrim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.systemui.screenshot.SaveImageInBackgroundTask$$ExternalSyntheticLambda0;
import com.android.systemui.scrim.ScrimDrawable;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class ScrimView extends View {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Runnable mChangeRunnable;
    public Executor mChangeRunnableExecutor;
    public PorterDuffColorFilter mColorFilter;
    public final Object mColorLock;
    @GuardedBy({"mColorLock"})
    public final ColorExtractor.GradientColors mColors;
    public Drawable mDrawable;
    public Rect mDrawableBounds;
    public SaveImageInBackgroundTask$$ExternalSyntheticLambda0 mExecutor;
    public Looper mExecutorLooper;
    public int mTintColor;
    public final ColorExtractor.GradientColors mTmpColors;
    public float mViewAlpha;

    public ScrimView(Context context) {
        this(context, null);
    }

    public final boolean canReceivePointerEvents() {
        return false;
    }

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    public ScrimView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final void enableBottomEdgeConcave(boolean z) {
        Drawable drawable = this.mDrawable;
        if (drawable instanceof ScrimDrawable) {
            ScrimDrawable scrimDrawable = (ScrimDrawable) drawable;
            if (!z || scrimDrawable.mConcaveInfo == null) {
                if (!z) {
                    scrimDrawable.mConcaveInfo = null;
                } else {
                    Objects.requireNonNull(scrimDrawable);
                    ScrimDrawable.ConcaveInfo concaveInfo = new ScrimDrawable.ConcaveInfo();
                    scrimDrawable.mConcaveInfo = concaveInfo;
                    float f = scrimDrawable.mCornerRadius;
                    concaveInfo.mPathOverlap = f;
                    float[] fArr = concaveInfo.mCornerRadii;
                    fArr[0] = f;
                    fArr[1] = f;
                    fArr[2] = f;
                    fArr[3] = f;
                }
                scrimDrawable.invalidateSelf();
            }
        }
    }

    public final void executeOnExecutor(Runnable runnable) {
        if (this.mExecutor == null || Looper.myLooper() == this.mExecutorLooper) {
            runnable.run();
            return;
        }
        Objects.requireNonNull(this.mExecutor);
        runnable.run();
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        if (this.mDrawable.getAlpha() > 0) {
            this.mDrawable.draw(canvas);
        }
    }

    @Override // android.view.View
    public final void setClickable(final boolean z) {
        executeOnExecutor(new Runnable() { // from class: com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ScrimView.$r8$lambda$l1DylaPD_MWF2Cf28dTAo5koSm0(ScrimView.this, z);
            }
        });
    }

    public final void setColors(ColorExtractor.GradientColors gradientColors, boolean z) {
        if (gradientColors != null) {
            executeOnExecutor(new ScrimView$$ExternalSyntheticLambda2(this, gradientColors, z, 0));
            return;
        }
        throw new IllegalArgumentException("Colors cannot be null");
    }

    public final void setCornerRadius(int i) {
        Drawable drawable = this.mDrawable;
        if (drawable instanceof ScrimDrawable) {
            ScrimDrawable scrimDrawable = (ScrimDrawable) drawable;
            float f = i;
            Objects.requireNonNull(scrimDrawable);
            if (f != scrimDrawable.mCornerRadius) {
                scrimDrawable.mCornerRadius = f;
                ScrimDrawable.ConcaveInfo concaveInfo = scrimDrawable.mConcaveInfo;
                if (concaveInfo != null) {
                    concaveInfo.mPathOverlap = f;
                    float[] fArr = concaveInfo.mCornerRadii;
                    fArr[0] = f;
                    fArr[1] = f;
                    fArr[2] = f;
                    fArr[3] = f;
                    scrimDrawable.updatePath();
                }
                scrimDrawable.invalidateSelf();
            }
        }
    }

    @VisibleForTesting
    public void setDrawable(Drawable drawable) {
        executeOnExecutor(new ScrimView$$ExternalSyntheticLambda1(this, drawable, 0));
    }

    public final void updateColorWithTint(boolean z) {
        boolean z2;
        PorterDuff.Mode mode;
        Drawable drawable = this.mDrawable;
        if (drawable instanceof ScrimDrawable) {
            ((ScrimDrawable) drawable).setColor(ColorUtils.blendARGB(this.mColors.getMainColor(), this.mTintColor, Color.alpha(this.mTintColor) / 255.0f), z);
        } else {
            if (Color.alpha(this.mTintColor) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                PorterDuffColorFilter porterDuffColorFilter = this.mColorFilter;
                if (porterDuffColorFilter == null) {
                    mode = PorterDuff.Mode.SRC_OVER;
                } else {
                    mode = porterDuffColorFilter.getMode();
                }
                PorterDuffColorFilter porterDuffColorFilter2 = this.mColorFilter;
                if (porterDuffColorFilter2 == null || porterDuffColorFilter2.getColor() != this.mTintColor) {
                    this.mColorFilter = new PorterDuffColorFilter(this.mTintColor, mode);
                }
            } else {
                this.mColorFilter = null;
            }
            this.mDrawable.setColorFilter(this.mColorFilter);
            this.mDrawable.invalidateSelf();
        }
        Runnable runnable = this.mChangeRunnable;
        if (runnable != null) {
            this.mChangeRunnableExecutor.execute(runnable);
        }
    }

    public static /* synthetic */ void $r8$lambda$0hg1lN64kAlaRiHts8Cd8Pck26U(ScrimView scrimView, ColorExtractor.GradientColors gradientColors, boolean z) {
        Objects.requireNonNull(scrimView);
        synchronized (scrimView.mColorLock) {
            if (!scrimView.mColors.equals(gradientColors)) {
                scrimView.mColors.set(gradientColors);
                scrimView.updateColorWithTint(z);
            }
        }
    }

    public static /* synthetic */ void $r8$lambda$l1DylaPD_MWF2Cf28dTAo5koSm0(ScrimView scrimView, boolean z) {
        Objects.requireNonNull(scrimView);
        super.setClickable(z);
    }

    public ScrimView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.view.View, android.graphics.drawable.Drawable.Callback
    public final void invalidateDrawable(Drawable drawable) {
        super.invalidateDrawable(drawable);
        if (drawable == this.mDrawable) {
            invalidate();
        }
    }

    @Override // android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        Rect rect = this.mDrawableBounds;
        if (rect != null) {
            this.mDrawable.setBounds(rect);
        } else if (z) {
            this.mDrawable.setBounds(i, i2, i3, i4);
            invalidate();
        }
    }

    public final void setViewAlpha(final float f) {
        if (!Float.isNaN(f)) {
            executeOnExecutor(new Runnable() { // from class: com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ScrimView scrimView = ScrimView.this;
                    float f2 = f;
                    int i = ScrimView.$r8$clinit;
                    Objects.requireNonNull(scrimView);
                    if (f2 != scrimView.mViewAlpha) {
                        scrimView.mViewAlpha = f2;
                        scrimView.mDrawable.setAlpha((int) (f2 * 255.0f));
                        Runnable runnable = scrimView.mChangeRunnable;
                        if (runnable != null) {
                            scrimView.mChangeRunnableExecutor.execute(runnable);
                        }
                    }
                }
            });
            return;
        }
        throw new IllegalArgumentException("alpha cannot be NaN: " + f);
    }

    public ScrimView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mColorLock = new Object();
        this.mTmpColors = new ColorExtractor.GradientColors();
        this.mViewAlpha = 1.0f;
        ScrimDrawable scrimDrawable = new ScrimDrawable();
        this.mDrawable = scrimDrawable;
        scrimDrawable.setCallback(this);
        this.mColors = new ColorExtractor.GradientColors();
        this.mExecutorLooper = Looper.myLooper();
        this.mExecutor = SaveImageInBackgroundTask$$ExternalSyntheticLambda0.INSTANCE;
        executeOnExecutor(new ScrimView$$ExternalSyntheticLambda0(this, 0));
    }

    @VisibleForTesting
    public Drawable getDrawable() {
        return this.mDrawable;
    }
}
