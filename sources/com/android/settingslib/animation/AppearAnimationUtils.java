package com.android.settingslib.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.RenderNodeAnimator;
import android.view.View;
import android.view.animation.Interpolator;
import com.android.keyguard.KeyguardInputView;
/* loaded from: classes.dex */
public class AppearAnimationUtils implements AppearAnimationCreator<View> {
    public final float mDelayScale;
    public final long mDuration;
    public final Interpolator mInterpolator;
    public RowTranslationScaler mRowTranslationScaler;
    public final float mStartTranslation;
    public final AppearAnimationProperties mProperties = new AppearAnimationProperties();
    public boolean mAppearing = true;

    /* loaded from: classes.dex */
    public class AppearAnimationProperties {
        public long[][] delays;
        public int maxDelayColIndex;
        public int maxDelayRowIndex;
    }

    /* loaded from: classes.dex */
    public interface RowTranslationScaler {
    }

    @Override // com.android.settingslib.animation.AppearAnimationCreator
    public final /* bridge */ /* synthetic */ void createAnimation(View view, long j, long j2, float f, boolean z, Interpolator interpolator, Runnable runnable) {
        createAnimation2(view, j, j2, f, z, interpolator, runnable);
    }

    /* renamed from: createAnimation  reason: avoid collision after fix types in other method */
    public static void createAnimation2(final View view, long j, long j2, float f, boolean z, Interpolator interpolator, final Runnable runnable) {
        RenderNodeAnimator renderNodeAnimator;
        if (view != null) {
            float f2 = 1.0f;
            view.setAlpha(z ? 0.0f : 1.0f);
            view.setTranslationY(z ? f : 0.0f);
            if (!z) {
                f2 = 0.0f;
            }
            if (view.isHardwareAccelerated()) {
                renderNodeAnimator = new RenderNodeAnimator(11, f2);
                renderNodeAnimator.setTarget(view);
            } else {
                renderNodeAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, view.getAlpha(), f2);
            }
            renderNodeAnimator.setInterpolator(interpolator);
            renderNodeAnimator.setDuration(j2);
            renderNodeAnimator.setStartDelay(j);
            if (view.hasOverlappingRendering()) {
                view.setLayerType(2, null);
                renderNodeAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.settingslib.animation.AppearAnimationUtils.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        view.setLayerType(0, null);
                    }
                });
            }
            if (runnable != null) {
                renderNodeAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.settingslib.animation.AppearAnimationUtils.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        runnable.run();
                    }
                });
            }
            renderNodeAnimator.start();
            startTranslationYAnimation(view, j, j2, z ? 0.0f : f, interpolator, null);
        }
    }

    public long calculateDelay(int i, int i2) {
        return (long) ((((Math.pow(i, 0.4d) + 0.4d) * i2 * 20.0d) + (i * 40)) * this.mDelayScale);
    }

    public final <T> void startAnimation2d(T[][] tArr, Runnable runnable, AppearAnimationCreator<T> appearAnimationCreator) {
        float f;
        Runnable runnable2;
        float f2;
        int length;
        AppearAnimationProperties appearAnimationProperties = this.mProperties;
        appearAnimationProperties.maxDelayColIndex = -1;
        appearAnimationProperties.maxDelayRowIndex = -1;
        appearAnimationProperties.delays = new long[tArr.length];
        long j = -1;
        for (int i = 0; i < tArr.length; i++) {
            T[] tArr2 = tArr[i];
            this.mProperties.delays[i] = new long[tArr2.length];
            for (int i2 = 0; i2 < tArr2.length; i2++) {
                long calculateDelay = calculateDelay(i, i2);
                AppearAnimationProperties appearAnimationProperties2 = this.mProperties;
                appearAnimationProperties2.delays[i][i2] = calculateDelay;
                if (tArr[i][i2] != null && calculateDelay > j) {
                    appearAnimationProperties2.maxDelayColIndex = i2;
                    appearAnimationProperties2.maxDelayRowIndex = i;
                    j = calculateDelay;
                }
            }
        }
        AppearAnimationProperties appearAnimationProperties3 = this.mProperties;
        if (appearAnimationProperties3.maxDelayRowIndex == -1 || appearAnimationProperties3.maxDelayColIndex == -1) {
            runnable.run();
            return;
        }
        int i3 = 0;
        while (true) {
            long[][] jArr = appearAnimationProperties3.delays;
            if (i3 < jArr.length) {
                long[] jArr2 = jArr[i3];
                if (this.mRowTranslationScaler != null) {
                    f = (float) (Math.pow(length - i3, 2.0d) / jArr.length);
                } else {
                    f = 1.0f;
                }
                float f3 = f * this.mStartTranslation;
                for (int i4 = 0; i4 < jArr2.length; i4++) {
                    long j2 = jArr2[i4];
                    if (appearAnimationProperties3.maxDelayRowIndex == i3 && appearAnimationProperties3.maxDelayColIndex == i4) {
                        runnable2 = runnable;
                    } else {
                        runnable2 = null;
                    }
                    T t = tArr[i3][i4];
                    long j3 = this.mDuration;
                    boolean z = this.mAppearing;
                    if (z) {
                        f2 = f3;
                    } else {
                        f2 = -f3;
                    }
                    appearAnimationCreator.createAnimation(t, j2, j3, f2, z, this.mInterpolator, runnable2);
                }
                i3++;
            } else {
                return;
            }
        }
    }

    public AppearAnimationUtils(Context context, long j, float f, float f2, Interpolator interpolator) {
        this.mInterpolator = interpolator;
        this.mStartTranslation = context.getResources().getDimensionPixelOffset(2131165330) * f;
        this.mDelayScale = f2;
        this.mDuration = j;
    }

    public static void startTranslationYAnimation(View view, long j, long j2, float f, Interpolator interpolator, KeyguardInputView.AnonymousClass1 r12) {
        RenderNodeAnimator renderNodeAnimator;
        if (view.isHardwareAccelerated()) {
            renderNodeAnimator = new RenderNodeAnimator(1, f);
            renderNodeAnimator.setTarget(view);
        } else {
            renderNodeAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.getTranslationY(), f);
        }
        renderNodeAnimator.setInterpolator(interpolator);
        renderNodeAnimator.setDuration(j2);
        renderNodeAnimator.setStartDelay(j);
        if (r12 != null) {
            renderNodeAnimator.addListener(r12);
        }
        renderNodeAnimator.start();
    }
}
