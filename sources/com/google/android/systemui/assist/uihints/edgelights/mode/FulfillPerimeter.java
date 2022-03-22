package com.google.android.systemui.assist.uihints.edgelights.mode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.animation.PathInterpolator;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda2;
import com.android.systemui.assist.ui.EdgeLight;
import com.android.systemui.assist.ui.PerimeterPathGuide;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FulfillPerimeter implements EdgeLightsView.Mode {
    public static final PathInterpolator FULFILL_PERIMETER_INTERPOLATOR = new PathInterpolator(0.2f, 0.0f, 0.2f, 1.0f);
    public final EdgeLight mBlueLight;
    public boolean mDisappearing = false;
    public final EdgeLight mGreenLight;
    public final EdgeLight[] mLights;
    public EdgeLightsView.Mode mNextMode;
    public final EdgeLight mRedLight;
    public final EdgeLight mYellowLight;

    /* renamed from: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillPerimeter$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        public static final /* synthetic */ int $r8$clinit = 0;
        public final /* synthetic */ AnimatorSet val$set;
        public final /* synthetic */ EdgeLightsView val$view;

        public AnonymousClass1(AnimatorSet animatorSet, EdgeLightsView edgeLightsView) {
            this.val$set = animatorSet;
            this.val$view = edgeLightsView;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            FulfillPerimeter fulfillPerimeter = FulfillPerimeter.this;
            EdgeLightsView.Mode mode = fulfillPerimeter.mNextMode;
            if (mode == null) {
                fulfillPerimeter.mDisappearing = false;
                this.val$set.start();
            } else if (mode != null) {
                new Handler().postDelayed(new CarrierTextManager$$ExternalSyntheticLambda2(this, this.val$view, 4), 500L);
            }
        }
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final int getSubType() {
        return 4;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v3 */
    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final void start(final EdgeLightsView edgeLightsView, final PerimeterPathGuide perimeterPathGuide, EdgeLightsView.Mode mode) {
        boolean z;
        boolean z2;
        float f;
        AnimatorSet animatorSet;
        long j;
        AnimatorSet animatorSet2;
        PerimeterPathGuide perimeterPathGuide2 = perimeterPathGuide;
        PerimeterPathGuide.Region region = PerimeterPathGuide.Region.TOP;
        boolean z3 = false;
        edgeLightsView.setVisibility(0);
        AnimatorSet animatorSet3 = new AnimatorSet();
        EdgeLight[] edgeLightArr = this.mLights;
        int length = edgeLightArr.length;
        int i = 0;
        while (i < length) {
            final EdgeLight edgeLight = edgeLightArr[i];
            if (edgeLight == this.mBlueLight || edgeLight == this.mRedLight) {
                z = true;
            } else {
                z = z3;
            }
            if (edgeLight == this.mRedLight || edgeLight == this.mYellowLight) {
                z2 = true;
            } else {
                z2 = z3;
            }
            PerimeterPathGuide.Region region2 = PerimeterPathGuide.Region.BOTTOM;
            final float regionCenter = perimeterPathGuide2.getRegionCenter(region2);
            if (z) {
                f = perimeterPathGuide2.getRegionCenter(region) - 1.0f;
            } else {
                f = regionCenter;
            }
            final float f2 = f - regionCenter;
            float regionCenter2 = perimeterPathGuide2.getRegionCenter(region) - perimeterPathGuide2.getRegionCenter(region2);
            final float f3 = regionCenter2 - 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            if (z2) {
                animatorSet = animatorSet3;
                j = 100;
            } else {
                animatorSet = animatorSet3;
                j = 0;
            }
            ofFloat.setStartDelay(j);
            ofFloat.setDuration(433L);
            PathInterpolator pathInterpolator = FULFILL_PERIMETER_INTERPOLATOR;
            ofFloat.setInterpolator(pathInterpolator);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillPerimeter$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FulfillPerimeter fulfillPerimeter = FulfillPerimeter.this;
                    EdgeLight edgeLight2 = edgeLight;
                    float f4 = f2;
                    float f5 = regionCenter;
                    float f6 = f3;
                    EdgeLightsView edgeLightsView2 = edgeLightsView;
                    Objects.requireNonNull(fulfillPerimeter);
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    Objects.requireNonNull(edgeLight2);
                    edgeLight2.mStart = (f4 * animatedFraction) + f5;
                    if (!fulfillPerimeter.mDisappearing) {
                        edgeLight2.mLength = (f6 * animatedFraction) + 0.0f;
                    }
                    edgeLightsView2.setAssistLights(fulfillPerimeter.mLights);
                }
            });
            if (!z2) {
                animatorSet2 = animatorSet;
                animatorSet2.play(ofFloat);
            } else {
                animatorSet2 = animatorSet;
                final float interpolation = ofFloat.getInterpolator().getInterpolation(100.0f / ((float) ofFloat.getDuration())) * regionCenter2;
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat2.setStartDelay(ofFloat.getStartDelay() + 100);
                ofFloat2.setDuration(733L);
                ofFloat2.setInterpolator(pathInterpolator);
                ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillPerimeter$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        FulfillPerimeter fulfillPerimeter = FulfillPerimeter.this;
                        EdgeLight edgeLight2 = edgeLight;
                        float f4 = interpolation;
                        PerimeterPathGuide perimeterPathGuide3 = perimeterPathGuide;
                        EdgeLightsView edgeLightsView2 = edgeLightsView;
                        Objects.requireNonNull(fulfillPerimeter);
                        float animatedFraction = valueAnimator.getAnimatedFraction();
                        if (animatedFraction != 0.0f) {
                            fulfillPerimeter.mDisappearing = true;
                            EdgeLight edgeLight3 = fulfillPerimeter.mRedLight;
                            if (edgeLight2 == edgeLight3) {
                                float max = Math.max(((0.0f - f4) * animatedFraction) + f4, 0.0f);
                                Objects.requireNonNull(edgeLight3);
                                edgeLight3.mLength = max;
                                EdgeLight edgeLight4 = fulfillPerimeter.mBlueLight;
                                Objects.requireNonNull(edgeLight4);
                                float abs = Math.abs(edgeLight4.mStart);
                                EdgeLight edgeLight5 = fulfillPerimeter.mRedLight;
                                Objects.requireNonNull(edgeLight5);
                                edgeLight4.mLength = abs - Math.abs(edgeLight5.mStart);
                            } else {
                                EdgeLight edgeLight6 = fulfillPerimeter.mYellowLight;
                                if (edgeLight2 == edgeLight6) {
                                    PerimeterPathGuide.Region region3 = PerimeterPathGuide.Region.BOTTOM;
                                    EdgeLight edgeLight7 = fulfillPerimeter.mRedLight;
                                    Objects.requireNonNull(edgeLight7);
                                    float f5 = edgeLight7.mStart;
                                    EdgeLight edgeLight8 = fulfillPerimeter.mRedLight;
                                    Objects.requireNonNull(edgeLight8);
                                    Objects.requireNonNull(edgeLight6);
                                    edgeLight6.mStart = (perimeterPathGuide3.getRegionCenter(region3) * 2.0f) - (f5 + edgeLight8.mLength);
                                    EdgeLight edgeLight9 = fulfillPerimeter.mYellowLight;
                                    EdgeLight edgeLight10 = fulfillPerimeter.mRedLight;
                                    Objects.requireNonNull(edgeLight10);
                                    float f6 = edgeLight10.mLength;
                                    Objects.requireNonNull(edgeLight9);
                                    edgeLight9.mLength = f6;
                                    EdgeLight edgeLight11 = fulfillPerimeter.mGreenLight;
                                    EdgeLight edgeLight12 = fulfillPerimeter.mBlueLight;
                                    Objects.requireNonNull(edgeLight12);
                                    float f7 = edgeLight12.mStart;
                                    EdgeLight edgeLight13 = fulfillPerimeter.mBlueLight;
                                    Objects.requireNonNull(edgeLight13);
                                    Objects.requireNonNull(edgeLight11);
                                    edgeLight11.mStart = (perimeterPathGuide3.getRegionCenter(region3) * 2.0f) - (f7 + edgeLight13.mLength);
                                    EdgeLight edgeLight14 = fulfillPerimeter.mGreenLight;
                                    EdgeLight edgeLight15 = fulfillPerimeter.mBlueLight;
                                    Objects.requireNonNull(edgeLight15);
                                    float f8 = edgeLight15.mLength;
                                    Objects.requireNonNull(edgeLight14);
                                    edgeLight14.mLength = f8;
                                }
                            }
                            edgeLightsView2.setAssistLights(fulfillPerimeter.mLights);
                        }
                    }
                });
                animatorSet2.play(ofFloat);
                animatorSet2.play(ofFloat2);
            }
            i++;
            perimeterPathGuide2 = perimeterPathGuide;
            animatorSet3 = animatorSet2;
            region = region;
            z3 = false;
        }
        animatorSet3.addListener(new AnonymousClass1(animatorSet3, edgeLightsView));
        animatorSet3.start();
    }

    public FulfillPerimeter(Context context) {
        EdgeLight edgeLight = new EdgeLight(context.getResources().getColor(2131099853, null));
        this.mBlueLight = edgeLight;
        EdgeLight edgeLight2 = new EdgeLight(context.getResources().getColor(2131099855, null));
        this.mRedLight = edgeLight2;
        EdgeLight edgeLight3 = new EdgeLight(context.getResources().getColor(2131099856, null));
        this.mYellowLight = edgeLight3;
        EdgeLight edgeLight4 = new EdgeLight(context.getResources().getColor(2131099854, null));
        this.mGreenLight = edgeLight4;
        this.mLights = new EdgeLight[]{edgeLight, edgeLight2, edgeLight4, edgeLight3};
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final void onNewModeRequest(EdgeLightsView edgeLightsView, EdgeLightsView.Mode mode) {
        this.mNextMode = mode;
    }
}
