package com.google.android.systemui.assist.uihints;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda0;
import com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda2;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsListener;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import com.google.android.systemui.assist.uihints.edgelights.mode.FullListening;
import com.google.android.systemui.assist.uihints.input.TouchInsideRegion;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class ScrimController implements TranscriptionController.TranscriptionSpaceListener, NgaMessageHandler.CardInfoListener, EdgeLightsListener, TouchInsideRegion {
    public static final LinearInterpolator ALPHA_INTERPOLATOR = new LinearInterpolator();
    public final LightnessProvider mLightnessProvider;
    public float mMedianLightness;
    public final OverlappedElementController mOverlappedElement;
    public final View mScrimView;
    public PipTouchHandler$$ExternalSyntheticLambda2 mVisibilityListener;
    public ValueAnimator mAlphaAnimator = new ValueAnimator();
    public float mInvocationProgress = 0.0f;
    public boolean mTranscriptionVisible = false;
    public boolean mCardVisible = false;
    public boolean mHaveAccurateLightness = false;
    public boolean mInFullListening = false;
    public boolean mCardTransitionAnimated = false;
    public boolean mCardForcesScrimGone = false;
    public boolean mIsDozing = false;

    public final ValueAnimator createRelativeAlphaAnimator(float f) {
        ValueAnimator duration = ValueAnimator.ofFloat(this.mScrimView.getAlpha(), f).setDuration(Math.abs(f - this.mScrimView.getAlpha()) * 300.0f);
        duration.setInterpolator(ALPHA_INTERPOLATOR);
        duration.addUpdateListener(new BubbleStackView$$ExternalSyntheticLambda0(this, 1));
        return duration;
    }

    @Override // com.google.android.systemui.assist.uihints.input.TouchInsideRegion
    public final Optional<Region> getTouchInsideRegion() {
        boolean z;
        if (this.mScrimView.getVisibility() == 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            return Optional.empty();
        }
        Rect rect = new Rect();
        this.mScrimView.getHitRect(rect);
        rect.top = rect.bottom - this.mScrimView.getResources().getDimensionPixelSize(2131166971);
        return Optional.of(new Region(rect));
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.CardInfoListener
    public final void onCardInfo(boolean z, int i, boolean z2, boolean z3) {
        this.mCardVisible = z;
        this.mCardTransitionAnimated = z2;
        this.mCardForcesScrimGone = z3;
        refresh();
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsListener
    public final void onModeStarted(EdgeLightsView.Mode mode) {
        this.mInFullListening = mode instanceof FullListening;
        refresh();
    }

    public final void refresh() {
        if (!this.mHaveAccurateLightness || this.mIsDozing) {
            setRelativeAlpha(0.0f, false);
            return;
        }
        boolean z = this.mCardVisible;
        if (!z || !this.mCardForcesScrimGone) {
            boolean z2 = true;
            if (this.mInFullListening || this.mTranscriptionVisible) {
                if (z) {
                    if (this.mScrimView.getVisibility() != 0) {
                        z2 = false;
                    }
                    if (!z2) {
                        return;
                    }
                }
                setRelativeAlpha(1.0f, false);
            } else if (z) {
                setRelativeAlpha(0.0f, this.mCardTransitionAnimated);
            } else {
                float f = this.mInvocationProgress;
                if (f > 0.0f) {
                    setRelativeAlpha(Math.min(1.0f, f), false);
                } else {
                    setRelativeAlpha(0.0f, true);
                }
            }
        } else {
            setRelativeAlpha(0.0f, this.mCardTransitionAnimated);
        }
    }

    public final void setRelativeAlpha(float f, boolean z) {
        int i;
        if (!this.mHaveAccurateLightness && f > 0.0f) {
            return;
        }
        if (f < 0.0f || f > 1.0f) {
            Log.e("ScrimController", "Got unexpected alpha: " + f + ", ignoring");
            return;
        }
        if (this.mAlphaAnimator.isRunning()) {
            this.mAlphaAnimator.cancel();
        }
        if (f > 0.0f) {
            if (this.mScrimView.getVisibility() != 0) {
                View view = this.mScrimView;
                if (this.mMedianLightness <= 0.4f) {
                    i = -16777216;
                } else {
                    i = -1;
                }
                view.setBackgroundTintList(ColorStateList.valueOf(i));
                setVisibility(0);
            }
            if (z) {
                ValueAnimator createRelativeAlphaAnimator = createRelativeAlphaAnimator(f);
                this.mAlphaAnimator = createRelativeAlphaAnimator;
                createRelativeAlphaAnimator.start();
                return;
            }
            this.mScrimView.setAlpha(f);
            this.mOverlappedElement.setAlpha(1.0f - f);
        } else if (z) {
            ValueAnimator createRelativeAlphaAnimator2 = createRelativeAlphaAnimator(f);
            this.mAlphaAnimator = createRelativeAlphaAnimator2;
            createRelativeAlphaAnimator2.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.uihints.ScrimController.1
                public boolean mCancelled = false;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    this.mCancelled = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (!this.mCancelled) {
                        ScrimController.this.setVisibility(8);
                    }
                }
            });
            this.mAlphaAnimator.start();
        } else {
            this.mScrimView.setAlpha(0.0f);
            this.mOverlappedElement.setAlpha(1.0f);
            setVisibility(8);
        }
    }

    public final void setVisibility(int i) {
        boolean z;
        Drawable drawable;
        if (i != this.mScrimView.getVisibility()) {
            this.mScrimView.setVisibility(i);
            PipTouchHandler$$ExternalSyntheticLambda2 pipTouchHandler$$ExternalSyntheticLambda2 = this.mVisibilityListener;
            if (pipTouchHandler$$ExternalSyntheticLambda2 != null) {
                NgaUiController ngaUiController = (NgaUiController) pipTouchHandler$$ExternalSyntheticLambda2.f$0;
                boolean z2 = NgaUiController.VERBOSE;
                Objects.requireNonNull(ngaUiController);
                ngaUiController.refresh();
            }
            LightnessProvider lightnessProvider = this.mLightnessProvider;
            if (i == 0) {
                z = true;
            } else {
                z = false;
            }
            Objects.requireNonNull(lightnessProvider);
            lightnessProvider.mMuted = z;
            View view = this.mScrimView;
            if (i == 0) {
                drawable = view.getContext().getDrawable(2131232638);
            } else {
                drawable = null;
            }
            view.setBackground(drawable);
            if (i != 0) {
                this.mOverlappedElement.setAlpha(1.0f);
                refresh();
            }
        }
    }

    public ScrimController(ViewGroup viewGroup, OverlappedElementController overlappedElementController, LightnessProvider lightnessProvider, TouchInsideHandler touchInsideHandler) {
        View findViewById = viewGroup.findViewById(2131428785);
        this.mScrimView = findViewById;
        findViewById.setBackgroundTintBlendMode(BlendMode.SRC_IN);
        this.mLightnessProvider = lightnessProvider;
        findViewById.setOnClickListener(touchInsideHandler);
        findViewById.setOnTouchListener(touchInsideHandler);
        this.mOverlappedElement = overlappedElementController;
    }
}
