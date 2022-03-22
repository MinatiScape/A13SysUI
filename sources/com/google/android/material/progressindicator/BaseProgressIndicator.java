package com.google.android.material.progressindicator;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.progressindicator.BaseProgressIndicatorSpec;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class BaseProgressIndicator<S extends BaseProgressIndicatorSpec> extends ProgressBar {
    public final int minHideDelay;
    public S spec;
    public int storedProgress;
    public boolean storedProgressAnimated;
    public boolean isIndeterminateModeChangeRequested = false;
    public int visibilityAfterHide = 4;
    public final AnonymousClass1 delayedShow = new Runnable() { // from class: com.google.android.material.progressindicator.BaseProgressIndicator.1
        @Override // java.lang.Runnable
        public final void run() {
            BaseProgressIndicator baseProgressIndicator = BaseProgressIndicator.this;
            Objects.requireNonNull(baseProgressIndicator);
            if (baseProgressIndicator.minHideDelay > 0) {
                SystemClock.uptimeMillis();
            }
            baseProgressIndicator.setVisibility(0);
        }
    };
    public final AnonymousClass2 delayedHide = new Runnable() { // from class: com.google.android.material.progressindicator.BaseProgressIndicator.2
        @Override // java.lang.Runnable
        public final void run() {
            BaseProgressIndicator baseProgressIndicator = BaseProgressIndicator.this;
            Objects.requireNonNull(baseProgressIndicator);
            boolean z = false;
            ((DrawableWithAnimatedVisibilityChange) baseProgressIndicator.getCurrentDrawable()).setVisible(false, false, true);
            if ((baseProgressIndicator.getProgressDrawable() == null || !baseProgressIndicator.getProgressDrawable().isVisible()) && (baseProgressIndicator.getIndeterminateDrawable() == null || !baseProgressIndicator.getIndeterminateDrawable().isVisible())) {
                z = true;
            }
            if (z) {
                baseProgressIndicator.setVisibility(4);
            }
            Objects.requireNonNull(BaseProgressIndicator.this);
        }
    };
    public final AnonymousClass3 switchIndeterminateModeCallback = new AnonymousClass3();
    public final AnonymousClass4 hideAnimationCallback = new Animatable2Compat.AnimationCallback() { // from class: com.google.android.material.progressindicator.BaseProgressIndicator.4
        @Override // androidx.vectordrawable.graphics.drawable.Animatable2Compat.AnimationCallback
        public final void onAnimationEnd(Drawable drawable) {
            BaseProgressIndicator baseProgressIndicator = BaseProgressIndicator.this;
            if (!baseProgressIndicator.isIndeterminateModeChangeRequested) {
                baseProgressIndicator.setVisibility(baseProgressIndicator.visibilityAfterHide);
            }
        }
    };
    public AnimatorDurationScaleProvider animatorDurationScaleProvider = new AnimatorDurationScaleProvider();
    public boolean isParentDoneInitializing = true;

    /* renamed from: com.google.android.material.progressindicator.BaseProgressIndicator$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 extends Animatable2Compat.AnimationCallback {
        public AnonymousClass3() {
        }

        @Override // androidx.vectordrawable.graphics.drawable.Animatable2Compat.AnimationCallback
        public final void onAnimationEnd(Drawable drawable) {
            BaseProgressIndicator.this.setIndeterminate(false);
            BaseProgressIndicator baseProgressIndicator = BaseProgressIndicator.this;
            baseProgressIndicator.setProgressCompat(baseProgressIndicator.storedProgress, baseProgressIndicator.storedProgressAnimated);
        }
    }

    public abstract S createSpec(Context context, AttributeSet attributeSet);

    @Override // android.widget.ProgressBar, android.view.View
    public final synchronized void onDraw(Canvas canvas) {
        int save = canvas.save();
        if (!(getPaddingLeft() == 0 && getPaddingTop() == 0)) {
            canvas.translate(getPaddingLeft(), getPaddingTop());
        }
        if (!(getPaddingRight() == 0 && getPaddingBottom() == 0)) {
            canvas.clipRect(0, 0, getWidth() - (getPaddingLeft() + getPaddingRight()), getHeight() - (getPaddingTop() + getPaddingBottom()));
        }
        getCurrentDrawable().draw(canvas);
        canvas.restoreToCount(save);
    }

    @Override // android.widget.ProgressBar, android.view.View
    public final synchronized void onMeasure(int i, int i2) {
        int i3;
        int i4;
        super.onMeasure(i, i2);
        DrawingDelegate<S> drawingDelegate = null;
        if (isIndeterminate()) {
            if (getIndeterminateDrawable() != null) {
                IndeterminateDrawable<S> indeterminateDrawable = getIndeterminateDrawable();
                Objects.requireNonNull(indeterminateDrawable);
                drawingDelegate = indeterminateDrawable.drawingDelegate;
            }
        } else if (getProgressDrawable() != null) {
            DeterminateDrawable<S> progressDrawable = getProgressDrawable();
            Objects.requireNonNull(progressDrawable);
            drawingDelegate = progressDrawable.drawingDelegate;
        }
        if (drawingDelegate != null) {
            int preferredWidth = drawingDelegate.getPreferredWidth();
            int preferredHeight = drawingDelegate.getPreferredHeight();
            if (preferredWidth < 0) {
                i3 = getMeasuredWidth();
            } else {
                i3 = preferredWidth + getPaddingLeft() + getPaddingRight();
            }
            if (preferredHeight < 0) {
                i4 = getMeasuredHeight();
            } else {
                i4 = preferredHeight + getPaddingTop() + getPaddingBottom();
            }
            setMeasuredDimension(i3, i4);
        }
    }

    @Override // android.widget.ProgressBar
    public final synchronized void setIndeterminate(boolean z) {
        if (z != isIndeterminate()) {
            DrawableWithAnimatedVisibilityChange drawableWithAnimatedVisibilityChange = (DrawableWithAnimatedVisibilityChange) getCurrentDrawable();
            if (drawableWithAnimatedVisibilityChange != null) {
                drawableWithAnimatedVisibilityChange.setVisible(false, false, false);
            }
            super.setIndeterminate(z);
            DrawableWithAnimatedVisibilityChange drawableWithAnimatedVisibilityChange2 = (DrawableWithAnimatedVisibilityChange) getCurrentDrawable();
            if (drawableWithAnimatedVisibilityChange2 != null) {
                drawableWithAnimatedVisibilityChange2.setVisible(visibleToUser(), false, false);
            }
            if ((drawableWithAnimatedVisibilityChange2 instanceof IndeterminateDrawable) && visibleToUser()) {
                IndeterminateDrawable indeterminateDrawable = (IndeterminateDrawable) drawableWithAnimatedVisibilityChange2;
                Objects.requireNonNull(indeterminateDrawable);
                indeterminateDrawable.animatorDelegate.startAnimator();
            }
            this.isIndeterminateModeChangeRequested = false;
        }
    }

    @Override // android.widget.ProgressBar
    public final synchronized void setProgress(int i) {
        if (!isIndeterminate()) {
            setProgressCompat(i, false);
        }
    }

    @Override // android.widget.ProgressBar
    public final IndeterminateDrawable<S> getIndeterminateDrawable() {
        return (IndeterminateDrawable) super.getIndeterminateDrawable();
    }

    @Override // android.widget.ProgressBar
    public final DeterminateDrawable<S> getProgressDrawable() {
        return (DeterminateDrawable) super.getProgressDrawable();
    }

    @Override // android.widget.ProgressBar, android.view.View
    public final void onDetachedFromWindow() {
        removeCallbacks(this.delayedHide);
        removeCallbacks(this.delayedShow);
        DrawableWithAnimatedVisibilityChange drawableWithAnimatedVisibilityChange = (DrawableWithAnimatedVisibilityChange) getCurrentDrawable();
        Objects.requireNonNull(drawableWithAnimatedVisibilityChange);
        drawableWithAnimatedVisibilityChange.setVisible(false, false, false);
        if (getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().unregisterAnimationCallback(this.hideAnimationCallback);
            IndeterminateDrawable<S> indeterminateDrawable = getIndeterminateDrawable();
            Objects.requireNonNull(indeterminateDrawable);
            indeterminateDrawable.animatorDelegate.unregisterAnimatorsCompleteCallback();
        }
        if (getProgressDrawable() != null) {
            getProgressDrawable().unregisterAnimationCallback(this.hideAnimationCallback);
        }
        super.onDetachedFromWindow();
    }

    public void setAnimatorDurationScaleProvider(AnimatorDurationScaleProvider animatorDurationScaleProvider) {
        this.animatorDurationScaleProvider = animatorDurationScaleProvider;
        if (getProgressDrawable() != null) {
            getProgressDrawable().animatorDurationScaleProvider = animatorDurationScaleProvider;
        }
        if (getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().animatorDurationScaleProvider = animatorDurationScaleProvider;
        }
    }

    @Override // android.widget.ProgressBar
    public final void setIndeterminateDrawable(Drawable drawable) {
        if (drawable == null) {
            super.setIndeterminateDrawable(null);
        } else if (drawable instanceof IndeterminateDrawable) {
            ((DrawableWithAnimatedVisibilityChange) drawable).setVisible(false, false, false);
            super.setIndeterminateDrawable(drawable);
        } else {
            throw new IllegalArgumentException("Cannot set framework drawable as indeterminate drawable.");
        }
    }

    @Override // android.widget.ProgressBar
    public final void setProgressDrawable(Drawable drawable) {
        if (drawable == null) {
            super.setProgressDrawable(null);
        } else if (drawable instanceof DeterminateDrawable) {
            DeterminateDrawable determinateDrawable = (DeterminateDrawable) drawable;
            determinateDrawable.setVisible(false, false, false);
            super.setProgressDrawable(determinateDrawable);
            determinateDrawable.setLevel((int) ((getProgress() / getMax()) * 10000.0f));
        } else {
            throw new IllegalArgumentException("Cannot set framework drawable as progress drawable.");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0022, code lost:
        if (getWindowVisibility() == 0) goto L_0x0024;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0024, code lost:
        r4 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean visibleToUser() {
        /*
            r4 = this;
            java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r0 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
            boolean r0 = androidx.core.view.ViewCompat.Api19Impl.isAttachedToWindow(r4)
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0033
            int r0 = r4.getWindowVisibility()
            if (r0 != 0) goto L_0x0033
            r0 = r4
        L_0x0011:
            int r3 = r0.getVisibility()
            if (r3 == 0) goto L_0x0018
            goto L_0x0026
        L_0x0018:
            android.view.ViewParent r0 = r0.getParent()
            if (r0 != 0) goto L_0x0028
            int r4 = r4.getWindowVisibility()
            if (r4 != 0) goto L_0x0026
        L_0x0024:
            r4 = r1
            goto L_0x002d
        L_0x0026:
            r4 = r2
            goto L_0x002d
        L_0x0028:
            boolean r3 = r0 instanceof android.view.View
            if (r3 != 0) goto L_0x0030
            goto L_0x0024
        L_0x002d:
            if (r4 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0030:
            android.view.View r0 = (android.view.View) r0
            goto L_0x0011
        L_0x0033:
            r1 = r2
        L_0x0034:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.progressindicator.BaseProgressIndicator.visibleToUser():boolean");
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.google.android.material.progressindicator.BaseProgressIndicator$1] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.google.android.material.progressindicator.BaseProgressIndicator$2] */
    /* JADX WARN: Type inference failed for: r0v5, types: [com.google.android.material.progressindicator.BaseProgressIndicator$4] */
    public BaseProgressIndicator(Context context, AttributeSet attributeSet, int i, int i2) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, i, 2132018700), attributeSet, i);
        Context context2 = getContext();
        this.spec = createSpec(context2, attributeSet);
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.BaseProgressIndicator, i, i2, new int[0]);
        obtainStyledAttributes.getInt(5, -1);
        this.minHideDelay = Math.min(obtainStyledAttributes.getInt(3, -1), 1000);
        obtainStyledAttributes.recycle();
    }

    @Override // android.widget.ProgressBar
    public final Drawable getCurrentDrawable() {
        if (isIndeterminate()) {
            return getIndeterminateDrawable();
        }
        return getProgressDrawable();
    }

    @Override // android.view.View
    public final void invalidate() {
        super.invalidate();
        if (getCurrentDrawable() != null) {
            getCurrentDrawable().invalidateSelf();
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!(getProgressDrawable() == null || getIndeterminateDrawable() == null)) {
            IndeterminateDrawable<S> indeterminateDrawable = getIndeterminateDrawable();
            Objects.requireNonNull(indeterminateDrawable);
            indeterminateDrawable.animatorDelegate.registerAnimatorsCompleteCallback(this.switchIndeterminateModeCallback);
        }
        if (getProgressDrawable() != null) {
            getProgressDrawable().registerAnimationCallback(this.hideAnimationCallback);
        }
        if (getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().registerAnimationCallback(this.hideAnimationCallback);
        }
        if (visibleToUser()) {
            if (this.minHideDelay > 0) {
                SystemClock.uptimeMillis();
            }
            setVisibility(0);
        }
    }

    @Override // android.view.View
    public final void onVisibilityChanged(View view, int i) {
        boolean z;
        super.onVisibilityChanged(view, i);
        if (i == 0) {
            z = true;
        } else {
            z = false;
        }
        if (this.isParentDoneInitializing) {
            ((DrawableWithAnimatedVisibilityChange) getCurrentDrawable()).setVisible(visibleToUser(), false, z);
        }
    }

    @Override // android.view.View
    public final void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (this.isParentDoneInitializing) {
            ((DrawableWithAnimatedVisibilityChange) getCurrentDrawable()).setVisible(visibleToUser(), false, false);
        }
    }

    public void setProgressCompat(int i, boolean z) {
        if (!isIndeterminate()) {
            super.setProgress(i);
            if (getProgressDrawable() != null && !z) {
                getProgressDrawable().jumpToCurrentState();
            }
        } else if (getProgressDrawable() != null) {
            this.storedProgress = i;
            this.storedProgressAnimated = z;
            this.isIndeterminateModeChangeRequested = true;
            if (getIndeterminateDrawable().isVisible()) {
                AnimatorDurationScaleProvider animatorDurationScaleProvider = this.animatorDurationScaleProvider;
                ContentResolver contentResolver = getContext().getContentResolver();
                Objects.requireNonNull(animatorDurationScaleProvider);
                if (Settings.Global.getFloat(contentResolver, "animator_duration_scale", 1.0f) != 0.0f) {
                    IndeterminateDrawable<S> indeterminateDrawable = getIndeterminateDrawable();
                    Objects.requireNonNull(indeterminateDrawable);
                    indeterminateDrawable.animatorDelegate.requestCancelAnimatorAfterCurrentCycle();
                    return;
                }
            }
            this.switchIndeterminateModeCallback.onAnimationEnd(getIndeterminateDrawable());
        }
    }
}
