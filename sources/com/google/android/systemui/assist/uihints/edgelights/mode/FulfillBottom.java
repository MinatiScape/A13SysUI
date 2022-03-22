package com.google.android.systemui.assist.uihints.edgelights.mode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import android.util.MathUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import com.android.systemui.assist.ui.EdgeLight;
import com.android.systemui.assist.ui.PerimeterPathGuide;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightUpdateListener;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import java.util.Objects;
import java.util.Random;
/* loaded from: classes.dex */
public final class FulfillBottom implements EdgeLightsView.Mode {
    public EdgeLight mBlueLight;
    public EdgeLight mGreenLight;
    public final boolean mIsListening;
    public EdgeLight[] mLightsArray;
    public EdgeLight mRedLight;
    public final Resources mResources;
    public EdgeLight mYellowLight;
    public static final PathInterpolator CRADLE_INTERPOLATOR = new PathInterpolator(0.4f, 0.0f, 0.6f, 1.0f);
    public static final PathInterpolator EXIT_TO_CORNER_INTERPOLATOR = new PathInterpolator(0.1f, 0.0f, 0.5f, 1.0f);
    public static final LinearInterpolator EXIT_FADE_INTERPOLATOR = new LinearInterpolator();
    public final Random mRandom = new Random();
    public AnimatorSet mExitAnimations = new AnimatorSet();
    public AnimatorSet mCradleAnimations = new AnimatorSet();
    public EdgeLightsView mEdgeLightsView = null;
    public PerimeterPathGuide mGuide = null;
    public EdgeLightsView.Mode mNextMode = null;
    public boolean mSwingLeft = false;

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final int getSubType() {
        return 3;
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final void onConfigurationChanged() {
        if (this.mNextMode == null) {
            start(this.mEdgeLightsView, this.mGuide, this);
            return;
        }
        if (this.mExitAnimations.isRunning()) {
            this.mExitAnimations.cancel();
        }
        onNewModeRequest(this.mEdgeLightsView, this.mNextMode);
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final void onNewModeRequest(EdgeLightsView edgeLightsView, EdgeLightsView.Mode mode) {
        this.mNextMode = mode;
        if (this.mCradleAnimations.isRunning()) {
            this.mCradleAnimations.cancel();
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("got mode ");
        m.append(mode.getClass().getSimpleName());
        Log.v("FulfillBottom", m.toString());
        if (!(mode instanceof Gone)) {
            if (this.mExitAnimations.isRunning()) {
                this.mExitAnimations.cancel();
            }
            this.mEdgeLightsView.commitModeTransition(this.mNextMode);
        } else if (!this.mExitAnimations.isRunning()) {
            EdgeLight[] copy = EdgeLight.copy(this.mLightsArray);
            EdgeLight[] copy2 = EdgeLight.copy(this.mLightsArray);
            float regionWidth = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM_LEFT) * 0.8f;
            float f = (-1.0f) * regionWidth;
            float regionWidth2 = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM);
            copy2[0].setEndpoints(f, f);
            copy2[1].setEndpoints(f, f);
            float f2 = regionWidth2 + regionWidth;
            copy2[2].setEndpoints(f2, f2);
            copy2[3].setEndpoints(f2, f2);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.setInterpolator(EXIT_TO_CORNER_INTERPOLATOR);
            ofFloat.setDuration(350L);
            ofFloat.addUpdateListener(new EdgeLightUpdateListener(copy, copy2, this.mLightsArray, this.mEdgeLightsView));
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(1.0f, 0.0f);
            ofFloat2.setInterpolator(EXIT_FADE_INTERPOLATOR);
            ofFloat2.setDuration(350L);
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FulfillBottom fulfillBottom = FulfillBottom.this;
                    Objects.requireNonNull(fulfillBottom);
                    fulfillBottom.mEdgeLightsView.setAlpha(1.0f - valueAnimator.getAnimatedFraction());
                }
            });
            ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    FulfillBottom.this.mEdgeLightsView.setAssistLights(new EdgeLight[0]);
                    FulfillBottom.this.mEdgeLightsView.setAlpha(1.0f);
                }
            });
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(ofFloat);
            animatorSet.play(ofFloat2);
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom.1
                public boolean mCancelled = false;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    this.mCancelled = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    FulfillBottom.this.mEdgeLightsView.setVisibility(8);
                    FulfillBottom fulfillBottom = FulfillBottom.this;
                    EdgeLightsView.Mode mode2 = fulfillBottom.mNextMode;
                    if (mode2 != null && !this.mCancelled) {
                        fulfillBottom.mEdgeLightsView.commitModeTransition(mode2);
                    }
                }
            });
            this.mExitAnimations = animatorSet;
            animatorSet.start();
        }
    }

    public final void setRelativePoints(float f, float f2, float f3) {
        float regionWidth = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM);
        float f4 = f * regionWidth;
        this.mBlueLight.setEndpoints(0.0f, f4);
        float f5 = f2 * regionWidth;
        this.mRedLight.setEndpoints(f4, f5);
        float f6 = f3 * regionWidth;
        this.mYellowLight.setEndpoints(f5, f6);
        this.mGreenLight.setEndpoints(f6, regionWidth);
        this.mEdgeLightsView.setAssistLights(this.mLightsArray);
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final void start(EdgeLightsView edgeLightsView, PerimeterPathGuide perimeterPathGuide, EdgeLightsView.Mode mode) {
        boolean z;
        boolean z2;
        this.mEdgeLightsView = edgeLightsView;
        this.mGuide = perimeterPathGuide;
        edgeLightsView.setVisibility(0);
        EdgeLight[] assistLights = edgeLightsView.getAssistLights();
        if (((mode instanceof FullListening) || (mode instanceof FulfillBottom)) && assistLights.length == 4) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            this.mBlueLight = assistLights[0];
            this.mRedLight = assistLights[1];
            this.mYellowLight = assistLights[2];
            this.mGreenLight = assistLights[3];
        } else {
            this.mBlueLight = new EdgeLight(this.mResources.getColor(2131099853, null));
            this.mRedLight = new EdgeLight(this.mResources.getColor(2131099855, null));
            this.mYellowLight = new EdgeLight(this.mResources.getColor(2131099856, null));
            this.mGreenLight = new EdgeLight(this.mResources.getColor(2131099854, null));
        }
        this.mLightsArray = new EdgeLight[]{this.mBlueLight, this.mRedLight, this.mYellowLight, this.mGreenLight};
        if (mode instanceof FulfillBottom) {
            FulfillBottom fulfillBottom = (FulfillBottom) mode;
            Objects.requireNonNull(fulfillBottom);
            z2 = fulfillBottom.mSwingLeft;
        } else {
            z2 = this.mRandom.nextBoolean();
        }
        this.mSwingLeft = z2;
        float regionWidth = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        EdgeLight edgeLight = this.mBlueLight;
        Objects.requireNonNull(edgeLight);
        final float f = (edgeLight.mStart + edgeLight.mLength) / regionWidth;
        EdgeLight edgeLight2 = this.mRedLight;
        Objects.requireNonNull(edgeLight2);
        final float f2 = (edgeLight2.mStart + edgeLight2.mLength) / regionWidth;
        EdgeLight edgeLight3 = this.mYellowLight;
        Objects.requireNonNull(edgeLight3);
        final float f3 = (edgeLight3.mStart + edgeLight3.mLength) / regionWidth;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom$$ExternalSyntheticLambda2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                float f4;
                float f5;
                float f6;
                FulfillBottom fulfillBottom2 = FulfillBottom.this;
                float f7 = f;
                float f8 = f2;
                float f9 = f3;
                Objects.requireNonNull(fulfillBottom2);
                float animatedFraction = valueAnimator.getAnimatedFraction();
                if (fulfillBottom2.mSwingLeft) {
                    f4 = 0.69f;
                } else {
                    f4 = 0.035f;
                }
                float lerp = MathUtils.lerp(f7, f4, animatedFraction);
                if (fulfillBottom2.mSwingLeft) {
                    f5 = 0.87f;
                } else {
                    f5 = 0.13f;
                }
                float lerp2 = MathUtils.lerp(f8, f5, animatedFraction);
                if (fulfillBottom2.mSwingLeft) {
                    f6 = 0.965f;
                } else {
                    f6 = 0.31f;
                }
                fulfillBottom2.setRelativePoints(lerp, lerp2, MathUtils.lerp(f9, f6, animatedFraction));
            }
        });
        ofFloat.setDuration(1000L);
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                FulfillBottom fulfillBottom2 = FulfillBottom.this;
                Objects.requireNonNull(fulfillBottom2);
                float animatedFraction = valueAnimator.getAnimatedFraction();
                if (!fulfillBottom2.mSwingLeft) {
                    animatedFraction = 1.0f - animatedFraction;
                }
                fulfillBottom2.setRelativePoints(MathUtils.lerp(0.69f, 0.035f, animatedFraction), MathUtils.lerp(0.87f, 0.13f, animatedFraction), MathUtils.lerp(0.965f, 0.31f, animatedFraction));
            }
        });
        ofFloat2.setDuration(1300L);
        ofFloat2.setInterpolator(CRADLE_INTERPOLATOR);
        ofFloat2.setRepeatMode(2);
        ofFloat2.setRepeatCount(-1);
        AnimatorSet animatorSet = new AnimatorSet();
        this.mCradleAnimations = animatorSet;
        animatorSet.playSequentially(ofFloat, ofFloat2);
        this.mCradleAnimations.start();
    }

    public FulfillBottom(Context context, boolean z) {
        this.mResources = context.getResources();
        this.mIsListening = z;
    }
}
