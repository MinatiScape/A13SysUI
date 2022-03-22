package com.google.android.material.progressindicator;

import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Property;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.progressindicator.BaseProgressIndicator;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class DrawableWithAnimatedVisibilityChange extends Drawable implements Animatable2Compat {
    public static final AnonymousClass3 GROW_FRACTION = new Property<DrawableWithAnimatedVisibilityChange, Float>() { // from class: com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange.3
        @Override // android.util.Property
        public final Float get(DrawableWithAnimatedVisibilityChange drawableWithAnimatedVisibilityChange) {
            return Float.valueOf(drawableWithAnimatedVisibilityChange.getGrowFraction());
        }

        @Override // android.util.Property
        public final void set(DrawableWithAnimatedVisibilityChange drawableWithAnimatedVisibilityChange, Float f) {
            DrawableWithAnimatedVisibilityChange drawableWithAnimatedVisibilityChange2 = drawableWithAnimatedVisibilityChange;
            float floatValue = f.floatValue();
            Objects.requireNonNull(drawableWithAnimatedVisibilityChange2);
            if (drawableWithAnimatedVisibilityChange2.growFraction != floatValue) {
                drawableWithAnimatedVisibilityChange2.growFraction = floatValue;
                drawableWithAnimatedVisibilityChange2.invalidateSelf();
            }
        }
    };
    public ArrayList animationCallbacks;
    public final BaseProgressIndicatorSpec baseSpec;
    public final Context context;
    public float growFraction;
    public ValueAnimator hideAnimator;
    public boolean ignoreCallbacks;
    public float mockGrowFraction;
    public boolean mockHideAnimationRunning;
    public boolean mockShowAnimationRunning;
    public ValueAnimator showAnimator;
    public int totalAlpha;
    public final Paint paint = new Paint();
    public AnimatorDurationScaleProvider animatorDurationScaleProvider = new AnimatorDurationScaleProvider();

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean setVisible(boolean z, boolean z2) {
        return setVisible(z, z2, true);
    }

    @Override // android.graphics.drawable.Animatable
    public final void start() {
        setVisibleInternal(true, true, false);
    }

    @Override // android.graphics.drawable.Animatable
    public final void stop() {
        setVisibleInternal(false, true, false);
    }

    @Override // androidx.vectordrawable.graphics.drawable.Animatable2Compat
    public final void clearAnimationCallbacks() {
        this.animationCallbacks.clear();
        this.animationCallbacks = null;
    }

    public final float getGrowFraction() {
        boolean z;
        BaseProgressIndicatorSpec baseProgressIndicatorSpec = this.baseSpec;
        Objects.requireNonNull(baseProgressIndicatorSpec);
        boolean z2 = true;
        if (baseProgressIndicatorSpec.showAnimationBehavior != 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            BaseProgressIndicatorSpec baseProgressIndicatorSpec2 = this.baseSpec;
            Objects.requireNonNull(baseProgressIndicatorSpec2);
            if (baseProgressIndicatorSpec2.hideAnimationBehavior == 0) {
                z2 = false;
            }
            if (!z2) {
                return 1.0f;
            }
        }
        if (this.mockHideAnimationRunning || this.mockShowAnimationRunning) {
            return this.mockGrowFraction;
        }
        return this.growFraction;
    }

    public final boolean isHiding() {
        ValueAnimator valueAnimator = this.hideAnimator;
        if ((valueAnimator == null || !valueAnimator.isRunning()) && !this.mockHideAnimationRunning) {
            return false;
        }
        return true;
    }

    public final boolean isShowing() {
        ValueAnimator valueAnimator = this.showAnimator;
        if ((valueAnimator == null || !valueAnimator.isRunning()) && !this.mockShowAnimationRunning) {
            return false;
        }
        return true;
    }

    @Override // androidx.vectordrawable.graphics.drawable.Animatable2Compat
    public final void registerAnimationCallback(Animatable2Compat.AnimationCallback animationCallback) {
        if (this.animationCallbacks == null) {
            this.animationCallbacks = new ArrayList();
        }
        if (!this.animationCallbacks.contains(animationCallback)) {
            this.animationCallbacks.add(animationCallback);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        this.totalAlpha = i;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public void setMockHideAnimationRunning(boolean z, float f) {
        this.mockHideAnimationRunning = z;
        this.mockGrowFraction = f;
    }

    public void setMockShowAnimationRunning(boolean z, float f) {
        this.mockShowAnimationRunning = z;
        this.mockGrowFraction = f;
    }

    public final boolean setVisible(boolean z, boolean z2, boolean z3) {
        AnimatorDurationScaleProvider animatorDurationScaleProvider = this.animatorDurationScaleProvider;
        ContentResolver contentResolver = this.context.getContentResolver();
        Objects.requireNonNull(animatorDurationScaleProvider);
        return setVisibleInternal(z, z2, z3 && Settings.Global.getFloat(contentResolver, "animator_duration_scale", 1.0f) > 0.0f);
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x00c9, code lost:
        if (r6.showAnimationBehavior != 0) goto L_0x00cb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00cb, code lost:
        r6 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00cd, code lost:
        r6 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00d6, code lost:
        if (r6.hideAnimationBehavior != 0) goto L_0x00cb;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean setVisibleInternal(boolean r6, boolean r7, boolean r8) {
        /*
            Method dump skipped, instructions count: 274
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.progressindicator.DrawableWithAnimatedVisibilityChange.setVisibleInternal(boolean, boolean, boolean):boolean");
    }

    public final boolean unregisterAnimationCallback(BaseProgressIndicator.AnonymousClass4 r2) {
        ArrayList arrayList = this.animationCallbacks;
        if (arrayList == null || !arrayList.contains(r2)) {
            return false;
        }
        this.animationCallbacks.remove(r2);
        if (!this.animationCallbacks.isEmpty()) {
            return true;
        }
        this.animationCallbacks = null;
        return true;
    }

    public DrawableWithAnimatedVisibilityChange(Context context, BaseProgressIndicatorSpec baseProgressIndicatorSpec) {
        this.context = context;
        this.baseSpec = baseProgressIndicatorSpec;
        setAlpha(255);
    }

    @Override // android.graphics.drawable.Animatable
    public final boolean isRunning() {
        if (isShowing() || isHiding()) {
            return true;
        }
        return false;
    }

    @Override // android.graphics.drawable.Drawable
    public final int getAlpha() {
        return this.totalAlpha;
    }
}
