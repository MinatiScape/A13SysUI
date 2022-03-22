package com.google.android.systemui.assist.uihints;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.util.MathUtils;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import com.android.systemui.Dependency;
import com.android.systemui.assist.ui.EdgeLight;
import com.android.systemui.biometrics.AuthPanelController$$ExternalSyntheticLambda0;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda2;
import com.google.android.systemui.assist.uihints.BlurProvider;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsListener;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom;
import com.google.android.systemui.assist.uihints.edgelights.mode.FullListening;
import com.google.android.systemui.assist.uihints.edgelights.mode.Gone;
import com.google.android.systemui.assist.uihints.input.TouchInsideRegion;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class GlowController implements NgaMessageHandler.AudioInfoListener, NgaMessageHandler.CardInfoListener, EdgeLightsListener, TouchInsideRegion {
    public final Context mContext;
    public final GlowView mGlowView;
    public float mMedianLightness;
    public PipTouchHandler$$ExternalSyntheticLambda2 mVisibilityListener;
    public EdgeLight[] mEdgeLights = null;
    public EdgeLightsView.Mode mEdgeLightsMode = null;
    public ValueAnimator mAnimator = null;
    public int mGlowsY = 0;
    public int mGlowsYDestination = 0;
    public RollingAverage mSpeechRolling = new RollingAverage();
    public boolean mInvocationCompleting = false;
    public boolean mCardVisible = false;

    /* loaded from: classes.dex */
    public enum GlowState {
        SHORT_DARK_BACKGROUND,
        SHORT_LIGHT_BACKGROUND,
        TALL_DARK_BACKGROUND,
        TALL_LIGHT_BACKGROUND,
        GONE
    }

    public final void animateGlowTranslationY(int i) {
        EdgeLight[] edgeLightArr;
        long min = Math.min((float) 400, Math.abs(i - this.mGlowsY) / ((float) (Math.abs(getMaxTranslationY() - getMinTranslationY()) / 400)));
        if (i == this.mGlowsYDestination) {
            GlowView glowView = this.mGlowView;
            int i2 = this.mGlowsY;
            int minTranslationY = getMinTranslationY();
            if (this.mEdgeLightsMode instanceof FullListening) {
                edgeLightArr = this.mEdgeLights;
            } else {
                edgeLightArr = null;
            }
            glowView.setGlowsY(i2, minTranslationY, edgeLightArr);
            return;
        }
        this.mGlowsYDestination = i;
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(this.mGlowsY, i);
        this.mAnimator = ofInt;
        ofInt.addUpdateListener(new AuthPanelController$$ExternalSyntheticLambda0(this, 1));
        this.mAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.uihints.GlowController.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                GlowController glowController = GlowController.this;
                glowController.mAnimator = null;
                if (GlowState.GONE.equals(glowController.getState())) {
                    GlowController.this.setVisibility(8);
                } else {
                    GlowController.this.maybeAnimateForSpeechConfidence();
                }
            }
        });
        this.mAnimator.setInterpolator(new LinearInterpolator());
        this.mAnimator.setDuration(min);
        GlowView glowView2 = this.mGlowView;
        Objects.requireNonNull(glowView2);
        final int i3 = glowView2.mBlurRadius;
        final int blurRadius = getBlurRadius();
        this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.assist.uihints.GlowController$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                GlowController glowController = GlowController.this;
                int i4 = i3;
                int i5 = blurRadius;
                Objects.requireNonNull(glowController);
                GlowView glowView3 = glowController.mGlowView;
                int lerp = (int) MathUtils.lerp(i4, i5, valueAnimator2.getAnimatedFraction());
                Objects.requireNonNull(glowView3);
                if (glowView3.mBlurRadius != lerp) {
                    glowView3.setBlurredImageOnViews(lerp);
                }
            }
        });
        GlowView glowView3 = this.mGlowView;
        Objects.requireNonNull(glowView3);
        float f = glowView3.mGlowWidthRatio;
        GlowView glowView4 = this.mGlowView;
        float f2 = ((0.55f - f) * 1.0f) + f;
        Objects.requireNonNull(glowView4);
        if (glowView4.mGlowWidthRatio != f2) {
            glowView4.mGlowWidthRatio = f2;
            glowView4.updateGlowSizes();
            glowView4.distributeEvenly();
        }
        if (this.mGlowView.getVisibility() != 0) {
            setVisibility(0);
        }
        this.mAnimator.start();
    }

    public final GlowState getState() {
        boolean z;
        EdgeLightsView.Mode mode;
        EdgeLightsView.Mode mode2 = this.mEdgeLightsMode;
        boolean z2 = true;
        if (mode2 instanceof FulfillBottom) {
            FulfillBottom fulfillBottom = (FulfillBottom) mode2;
            Objects.requireNonNull(fulfillBottom);
            if (!fulfillBottom.mIsListening) {
                z = true;
                mode = this.mEdgeLightsMode;
                if (!(mode instanceof Gone) || mode == null || z) {
                    return GlowState.GONE;
                }
                boolean z3 = this.mCardVisible;
                if (this.mMedianLightness >= 0.4f) {
                    z2 = false;
                }
                if (z2) {
                    return GlowState.TALL_DARK_BACKGROUND;
                }
                return GlowState.TALL_LIGHT_BACKGROUND;
            }
        }
        z = false;
        mode = this.mEdgeLightsMode;
        if (!(mode instanceof Gone)) {
        }
        return GlowState.GONE;
    }

    @Override // com.google.android.systemui.assist.uihints.input.TouchInsideRegion
    public final Optional<Region> getTouchInsideRegion() {
        if (this.mGlowView.getVisibility() != 0) {
            return Optional.empty();
        }
        Rect rect = new Rect();
        this.mGlowView.getBoundsOnScreen(rect);
        rect.top = rect.bottom - getMaxTranslationY();
        return Optional.of(new Region(rect));
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void maybeAnimateForSpeechConfidence() {
        /*
            r5 = this;
            com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView$Mode r0 = r5.mEdgeLightsMode
            boolean r1 = r0 instanceof com.google.android.systemui.assist.uihints.edgelights.mode.FullListening
            r2 = 3
            if (r1 != 0) goto L_0x000b
            boolean r0 = r0 instanceof com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom
            if (r0 == 0) goto L_0x0027
        L_0x000b:
            com.google.android.systemui.assist.uihints.RollingAverage r0 = r5.mSpeechRolling
            java.util.Objects.requireNonNull(r0)
            float r0 = r0.mTotal
            float r1 = (float) r2
            float r0 = r0 / r1
            double r0 = (double) r0
            r3 = 4599075939685498880(0x3fd3333340000000, double:0.30000001192092896)
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 >= 0) goto L_0x0029
            int r0 = r5.mGlowsYDestination
            int r1 = r5.getMinTranslationY()
            if (r0 <= r1) goto L_0x0027
            goto L_0x0029
        L_0x0027:
            r0 = 0
            goto L_0x002a
        L_0x0029:
            r0 = 1
        L_0x002a:
            if (r0 == 0) goto L_0x0047
            int r0 = r5.getMinTranslationY()
            int r1 = r5.getMaxTranslationY()
            com.google.android.systemui.assist.uihints.RollingAverage r3 = r5.mSpeechRolling
            java.util.Objects.requireNonNull(r3)
            float r3 = r3.mTotal
            float r2 = (float) r2
            float r3 = r3 / r2
            double r2 = (double) r3
            float r2 = (float) r2
            float r0 = android.util.MathUtils.lerp(r0, r1, r2)
            int r0 = (int) r0
            r5.animateGlowTranslationY(r0)
        L_0x0047:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.assist.uihints.GlowController.maybeAnimateForSpeechConfidence():void");
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsListener
    public final void onAssistLightsUpdated(EdgeLightsView.Mode mode, EdgeLight[] edgeLightArr) {
        int i;
        if (!(this.mEdgeLightsMode instanceof FullListening)) {
            this.mEdgeLights = null;
            this.mGlowView.distributeEvenly();
            return;
        }
        this.mEdgeLights = edgeLightArr;
        if ((this.mInvocationCompleting && (mode instanceof Gone)) || !(mode instanceof FullListening)) {
            return;
        }
        if (edgeLightArr == null || edgeLightArr.length != 4) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected 4 lights, have ");
            if (edgeLightArr == null) {
                i = 0;
            } else {
                i = edgeLightArr.length;
            }
            m.append(i);
            Log.e("GlowController", m.toString());
            return;
        }
        maybeAnimateForSpeechConfidence();
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.AudioInfoListener
    public final void onAudioInfo(float f, float f2) {
        this.mSpeechRolling.add(f2);
        maybeAnimateForSpeechConfidence();
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsListener
    public final void onModeStarted(EdgeLightsView.Mode mode) {
        boolean z = mode instanceof Gone;
        if (!z || this.mEdgeLightsMode != null) {
            this.mInvocationCompleting = !z;
            this.mEdgeLightsMode = mode;
            if (z) {
                this.mSpeechRolling = new RollingAverage();
            }
            animateGlowTranslationY(getMinTranslationY());
            return;
        }
        this.mEdgeLightsMode = mode;
    }

    public final void setVisibility(int i) {
        boolean z;
        boolean z2;
        this.mGlowView.setVisibility(i);
        boolean z3 = true;
        if (i == 0) {
            z = true;
        } else {
            z = false;
        }
        if (this.mGlowView.getVisibility() == 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z != z2) {
            PipTouchHandler$$ExternalSyntheticLambda2 pipTouchHandler$$ExternalSyntheticLambda2 = this.mVisibilityListener;
            if (pipTouchHandler$$ExternalSyntheticLambda2 != null) {
                NgaUiController ngaUiController = (NgaUiController) pipTouchHandler$$ExternalSyntheticLambda2.f$0;
                boolean z4 = NgaUiController.VERBOSE;
                Objects.requireNonNull(ngaUiController);
                ngaUiController.refresh();
            }
            if (this.mGlowView.getVisibility() != 0) {
                z3 = false;
            }
            if (!z3) {
                GlowView glowView = this.mGlowView;
                Objects.requireNonNull(glowView);
                Iterator<ImageView> it = glowView.mGlowImageViews.iterator();
                while (it.hasNext()) {
                    it.next().setImageDrawable(null);
                }
                BlurProvider blurProvider = glowView.mBlurProvider;
                Objects.requireNonNull(blurProvider);
                blurProvider.mBuffer = null;
                BlurProvider.BlurKernel blurKernel = blurProvider.mBlurKernel;
                Objects.requireNonNull(blurKernel);
                blurKernel.prepareInputAllocation(null);
                blurKernel.prepareOutputAllocation(null);
            }
        }
    }

    public GlowController(Context context, ViewGroup viewGroup, TouchInsideHandler touchInsideHandler) {
        this.mContext = context;
        ((NavigationModeController) Dependency.get(NavigationModeController.class)).addListener(new NavigationModeController.ModeChangedListener() { // from class: com.google.android.systemui.assist.uihints.GlowController$$ExternalSyntheticLambda1
            @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
            public final void onNavigationModeChanged(int i) {
                Objects.requireNonNull(GlowController.this);
            }
        });
        GlowView glowView = (GlowView) viewGroup.findViewById(2131428031);
        this.mGlowView = glowView;
        int i = this.mGlowsY;
        glowView.setGlowsY(i, i, null);
        glowView.setOnClickListener(touchInsideHandler);
        glowView.setOnTouchListener(touchInsideHandler);
        glowView.setGlowsY(getMinTranslationY(), getMinTranslationY(), null);
        if (glowView.mGlowWidthRatio != 0.55f) {
            glowView.mGlowWidthRatio = 0.55f;
            glowView.updateGlowSizes();
            glowView.distributeEvenly();
        }
    }

    public final int getBlurRadius() {
        if (getState() == GlowState.GONE) {
            GlowView glowView = this.mGlowView;
            Objects.requireNonNull(glowView);
            return glowView.mBlurRadius;
        } else if (getState() == GlowState.SHORT_DARK_BACKGROUND || getState() == GlowState.SHORT_LIGHT_BACKGROUND) {
            return this.mContext.getResources().getDimensionPixelSize(2131165784);
        } else {
            if (getState() == GlowState.TALL_DARK_BACKGROUND || getState() == GlowState.TALL_LIGHT_BACKGROUND) {
                return this.mContext.getResources().getDimensionPixelSize(2131165787);
            }
            return 0;
        }
    }

    public final int getMaxTranslationY() {
        if (getState() == GlowState.SHORT_DARK_BACKGROUND || getState() == GlowState.SHORT_LIGHT_BACKGROUND) {
            return this.mContext.getResources().getDimensionPixelSize(2131165785);
        }
        if (getState() == GlowState.TALL_DARK_BACKGROUND || getState() == GlowState.TALL_LIGHT_BACKGROUND) {
            return this.mContext.getResources().getDimensionPixelSize(2131165788);
        }
        return this.mContext.getResources().getDimensionPixelSize(2131165781);
    }

    public final int getMinTranslationY() {
        if (getState() == GlowState.SHORT_DARK_BACKGROUND || getState() == GlowState.SHORT_LIGHT_BACKGROUND) {
            return this.mContext.getResources().getDimensionPixelSize(2131165786);
        }
        if (getState() == GlowState.TALL_DARK_BACKGROUND || getState() == GlowState.TALL_LIGHT_BACKGROUND) {
            return this.mContext.getResources().getDimensionPixelSize(2131165789);
        }
        return this.mContext.getResources().getDimensionPixelSize(2131165782);
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.CardInfoListener
    public final void onCardInfo(boolean z, int i, boolean z2, boolean z3) {
        this.mCardVisible = z;
    }
}
