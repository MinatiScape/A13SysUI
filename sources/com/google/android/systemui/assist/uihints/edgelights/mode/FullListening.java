package com.google.android.systemui.assist.uihints.edgelights.mode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.SystemClock;
import android.util.MathUtils;
import android.view.animation.PathInterpolator;
import com.android.systemui.assist.ui.EdgeLight;
import com.android.systemui.assist.ui.PerimeterPathGuide;
import com.google.android.systemui.assist.uihints.RollingAverage;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightUpdateListener;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FullListening implements EdgeLightsView.Mode {
    public static final PathInterpolator INTERPOLATOR = new PathInterpolator(0.33f, 0.0f, 0.67f, 1.0f);
    public Animator mAnimator;
    public EdgeLightsView mEdgeLightsView;
    public final boolean mFakeForHalfListening;
    public PerimeterPathGuide mGuide;
    public final EdgeLight[] mLights;
    public boolean mLastPerturbationWasEven = false;
    public long mLastSpeechTimestampMs = 0;
    public RollingAverage mRollingConfidence = new RollingAverage();
    public State mState = State.NOT_STARTED;

    /* renamed from: com.google.android.systemui.assist.uihints.edgelights.mode.FullListening$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 extends AnimatorListenerAdapter {
        public boolean mCancelled = false;

        public AnonymousClass1() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationCancel(Animator animator) {
            super.onAnimationCancel(animator);
            this.mCancelled = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            FullListening fullListening = FullListening.this;
            if (fullListening.mAnimator == animator) {
                fullListening.mAnimator = null;
            }
            if (!this.mCancelled) {
                fullListening.updateStateAndAnimation();
            }
        }
    }

    /* loaded from: classes.dex */
    public enum State {
        NOT_STARTED,
        EXPANDING_TO_WIDTH,
        WAITING_FOR_SPEECH,
        LISTENING_TO_SPEECH,
        WAITING_FOR_ENDPOINTER
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final int getSubType() {
        return 1;
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final void onConfigurationChanged() {
        EdgeLight[] edgeLightArr;
        setAnimator(null);
        float f = 0.0f;
        for (EdgeLight edgeLight : this.mLights) {
            Objects.requireNonNull(edgeLight);
            f += edgeLight.mLength;
        }
        float regionWidth = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM);
        EdgeLight edgeLight2 = this.mLights[0];
        Objects.requireNonNull(edgeLight2);
        edgeLight2.mStart = 0.0f;
        EdgeLight[] edgeLightArr2 = this.mLights;
        EdgeLight edgeLight3 = edgeLightArr2[0];
        EdgeLight edgeLight4 = edgeLightArr2[0];
        Objects.requireNonNull(edgeLight4);
        Objects.requireNonNull(edgeLight3);
        edgeLight3.mLength = (edgeLight4.mLength / f) * regionWidth;
        EdgeLight[] edgeLightArr3 = this.mLights;
        EdgeLight edgeLight5 = edgeLightArr3[1];
        EdgeLight edgeLight6 = edgeLightArr3[0];
        Objects.requireNonNull(edgeLight6);
        float f2 = edgeLight6.mStart;
        EdgeLight edgeLight7 = this.mLights[0];
        Objects.requireNonNull(edgeLight7);
        Objects.requireNonNull(edgeLight5);
        edgeLight5.mStart = f2 + edgeLight7.mLength;
        EdgeLight[] edgeLightArr4 = this.mLights;
        EdgeLight edgeLight8 = edgeLightArr4[1];
        EdgeLight edgeLight9 = edgeLightArr4[1];
        Objects.requireNonNull(edgeLight9);
        Objects.requireNonNull(edgeLight8);
        edgeLight8.mLength = (edgeLight9.mLength / f) * regionWidth;
        EdgeLight[] edgeLightArr5 = this.mLights;
        EdgeLight edgeLight10 = edgeLightArr5[2];
        EdgeLight edgeLight11 = edgeLightArr5[1];
        Objects.requireNonNull(edgeLight11);
        float f3 = edgeLight11.mStart;
        EdgeLight edgeLight12 = this.mLights[1];
        Objects.requireNonNull(edgeLight12);
        Objects.requireNonNull(edgeLight10);
        edgeLight10.mStart = f3 + edgeLight12.mLength;
        EdgeLight[] edgeLightArr6 = this.mLights;
        EdgeLight edgeLight13 = edgeLightArr6[2];
        EdgeLight edgeLight14 = edgeLightArr6[2];
        Objects.requireNonNull(edgeLight14);
        Objects.requireNonNull(edgeLight13);
        edgeLight13.mLength = (edgeLight14.mLength / f) * regionWidth;
        EdgeLight[] edgeLightArr7 = this.mLights;
        EdgeLight edgeLight15 = edgeLightArr7[3];
        EdgeLight edgeLight16 = edgeLightArr7[2];
        Objects.requireNonNull(edgeLight16);
        float f4 = edgeLight16.mStart;
        EdgeLight edgeLight17 = this.mLights[2];
        Objects.requireNonNull(edgeLight17);
        Objects.requireNonNull(edgeLight15);
        edgeLight15.mStart = f4 + edgeLight17.mLength;
        EdgeLight[] edgeLightArr8 = this.mLights;
        EdgeLight edgeLight18 = edgeLightArr8[3];
        EdgeLight edgeLight19 = edgeLightArr8[3];
        Objects.requireNonNull(edgeLight19);
        Objects.requireNonNull(edgeLight18);
        edgeLight18.mLength = (edgeLight19.mLength / f) * regionWidth;
        updateStateAndAnimation();
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final boolean preventsInvocations() {
        return true;
    }

    public final EdgeLight[] createPerturbedLights() {
        float f;
        double d;
        double d2;
        float regionWidth = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM);
        State state = this.mState;
        State state2 = State.LISTENING_TO_SPEECH;
        if (state == state2) {
            if (this.mLastPerturbationWasEven) {
                f = 0.39999998f;
            } else {
                f = 0.6f;
            }
        } else if (this.mLastPerturbationWasEven) {
            f = 0.49f;
        } else {
            f = 0.51f;
        }
        float f2 = f * regionWidth;
        float f3 = regionWidth / 2.0f;
        float min = Math.min(f3, f2);
        float max = Math.max(f3, f2);
        RollingAverage rollingAverage = this.mRollingConfidence;
        Objects.requireNonNull(rollingAverage);
        float lerp = MathUtils.lerp(min, max, rollingAverage.mTotal / 3);
        float f4 = regionWidth - lerp;
        this.mLastPerturbationWasEven = !this.mLastPerturbationWasEven;
        State state3 = this.mState;
        if (state3 == state2) {
            d = 0.6d;
        } else {
            d = 0.52d;
        }
        if (state3 == state2) {
            d2 = 0.4d;
        } else {
            d2 = 0.48d;
        }
        double d3 = d - d2;
        float random = ((float) ((Math.random() * d3) + d2)) * lerp;
        float random2 = ((float) ((Math.random() * d3) + d2)) * f4;
        float f5 = f4 - random2;
        EdgeLight[] copy = EdgeLight.copy(this.mLights);
        EdgeLight edgeLight = copy[0];
        Objects.requireNonNull(edgeLight);
        edgeLight.mLength = random;
        EdgeLight edgeLight2 = copy[1];
        Objects.requireNonNull(edgeLight2);
        edgeLight2.mLength = random2;
        EdgeLight edgeLight3 = copy[2];
        Objects.requireNonNull(edgeLight3);
        edgeLight3.mLength = f5;
        EdgeLight edgeLight4 = copy[3];
        Objects.requireNonNull(edgeLight4);
        edgeLight4.mLength = lerp - random;
        EdgeLight edgeLight5 = copy[0];
        Objects.requireNonNull(edgeLight5);
        edgeLight5.mStart = 0.0f;
        EdgeLight edgeLight6 = copy[1];
        Objects.requireNonNull(edgeLight6);
        edgeLight6.mStart = random;
        EdgeLight edgeLight7 = copy[2];
        float f6 = random + random2;
        Objects.requireNonNull(edgeLight7);
        edgeLight7.mStart = f6;
        EdgeLight edgeLight8 = copy[3];
        Objects.requireNonNull(edgeLight8);
        edgeLight8.mStart = f6 + f5;
        return copy;
    }

    public final EdgeLight[] getFinalLights() {
        EdgeLight[] copy = EdgeLight.copy(this.mLights);
        float regionWidth = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM) / 4.0f;
        for (int i = 0; i < copy.length; i++) {
            EdgeLight edgeLight = copy[i];
            Objects.requireNonNull(edgeLight);
            edgeLight.mStart = i * regionWidth;
            EdgeLight edgeLight2 = copy[i];
            Objects.requireNonNull(edgeLight2);
            edgeLight2.mLength = regionWidth;
        }
        return copy;
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final void onAudioLevelUpdate(float f) {
        long j;
        this.mRollingConfidence.add(f);
        if (f > 0.1f) {
            j = SystemClock.uptimeMillis();
        } else {
            j = this.mLastSpeechTimestampMs;
        }
        this.mLastSpeechTimestampMs = j;
        if (this.mState != State.EXPANDING_TO_WIDTH) {
            updateStateAndAnimation();
        }
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public final void onNewModeRequest(EdgeLightsView edgeLightsView, EdgeLightsView.Mode mode) {
        if (!(mode instanceof FullListening) || ((FullListening) mode).mFakeForHalfListening != this.mFakeForHalfListening) {
            setAnimator(null);
            edgeLightsView.commitModeTransition(mode);
        }
    }

    public final void setAnimator(ValueAnimator valueAnimator) {
        Animator animator = this.mAnimator;
        if (animator != null) {
            animator.cancel();
        }
        this.mAnimator = valueAnimator;
        if (valueAnimator != null) {
            valueAnimator.start();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x005f  */
    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void start(com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView r8, com.android.systemui.assist.ui.PerimeterPathGuide r9, com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode r10) {
        /*
            r7 = this;
            r7.mEdgeLightsView = r8
            r7.mGuide = r9
            com.google.android.systemui.assist.uihints.edgelights.mode.FullListening$State r0 = com.google.android.systemui.assist.uihints.edgelights.mode.FullListening.State.EXPANDING_TO_WIDTH
            r7.mState = r0
            r0 = 0
            r8.setVisibility(r0)
            r1 = 2
            float[] r1 = new float[r1]
            r1 = {x009e: FILL_ARRAY_DATA  , data: [0, 1065353216} // fill-array
            android.animation.ValueAnimator r1 = android.animation.ValueAnimator.ofFloat(r1)
            boolean r2 = r10 instanceof com.google.android.systemui.assist.uihints.edgelights.mode.FullListening
            if (r2 == 0) goto L_0x001b
            goto L_0x002a
        L_0x001b:
            boolean r2 = r10 instanceof com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom
            if (r2 == 0) goto L_0x0022
            r2 = 300(0x12c, double:1.48E-321)
            goto L_0x002f
        L_0x0022:
            java.util.ArrayList r2 = r8.mAssistInvocationLights
            boolean r2 = r2.isEmpty()
            if (r2 != 0) goto L_0x002d
        L_0x002a:
            r2 = 0
            goto L_0x002f
        L_0x002d:
            r2 = 500(0x1f4, double:2.47E-321)
        L_0x002f:
            r1.setDuration(r2)
            android.view.animation.PathInterpolator r2 = com.google.android.systemui.assist.uihints.edgelights.mode.FullListening.INTERPOLATOR
            r1.setInterpolator(r2)
            com.google.android.systemui.assist.uihints.edgelights.EdgeLightUpdateListener r2 = new com.google.android.systemui.assist.uihints.edgelights.EdgeLightUpdateListener
            com.android.systemui.assist.ui.EdgeLight[] r3 = r7.mLights
            com.android.systemui.assist.ui.EdgeLight[] r3 = com.android.systemui.assist.ui.EdgeLight.copy(r3)
            com.android.systemui.assist.ui.EdgeLight[] r4 = r8.getAssistLights()
            if (r4 == 0) goto L_0x004e
            com.android.systemui.assist.ui.EdgeLight[] r4 = r8.getAssistLights()
            com.android.systemui.assist.ui.EdgeLight[] r4 = com.android.systemui.assist.ui.EdgeLight.copy(r4)
            goto L_0x004f
        L_0x004e:
            r4 = 0
        L_0x004f:
            boolean r10 = r10 instanceof com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom
            if (r10 == 0) goto L_0x005b
            if (r4 == 0) goto L_0x005b
            int r10 = r3.length
            int r5 = r4.length
            if (r10 != r5) goto L_0x005b
            r10 = 1
            goto L_0x005c
        L_0x005b:
            r10 = r0
        L_0x005c:
            int r5 = r3.length
            if (r0 >= r5) goto L_0x0086
            r5 = r3[r0]
            if (r10 == 0) goto L_0x006b
            r6 = r4[r0]
            java.util.Objects.requireNonNull(r6)
            float r6 = r6.mStart
            goto L_0x0071
        L_0x006b:
            com.android.systemui.assist.ui.PerimeterPathGuide$Region r6 = com.android.systemui.assist.ui.PerimeterPathGuide.Region.BOTTOM
            float r6 = r9.getRegionCenter(r6)
        L_0x0071:
            java.util.Objects.requireNonNull(r5)
            r5.mStart = r6
            if (r10 == 0) goto L_0x0080
            r6 = r4[r0]
            java.util.Objects.requireNonNull(r6)
            float r6 = r6.mLength
            goto L_0x0081
        L_0x0080:
            r6 = 0
        L_0x0081:
            r5.mLength = r6
            int r0 = r0 + 1
            goto L_0x005c
        L_0x0086:
            com.android.systemui.assist.ui.EdgeLight[] r9 = r7.getFinalLights()
            com.android.systemui.assist.ui.EdgeLight[] r10 = r7.mLights
            r2.<init>(r3, r9, r10, r8)
            r1.addUpdateListener(r2)
            com.google.android.systemui.assist.uihints.edgelights.mode.FullListening$1 r8 = new com.google.android.systemui.assist.uihints.edgelights.mode.FullListening$1
            r8.<init>()
            r1.addListener(r8)
            r7.setAnimator(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.assist.uihints.edgelights.mode.FullListening.start(com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView, com.android.systemui.assist.ui.PerimeterPathGuide, com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView$Mode):void");
    }

    public final void updateStateAndAnimation() {
        RollingAverage rollingAverage;
        boolean z;
        int i;
        EdgeLight[] edgeLightArr;
        RollingAverage rollingAverage2;
        State state = State.LISTENING_TO_SPEECH;
        State state2 = State.WAITING_FOR_ENDPOINTER;
        Objects.requireNonNull(this.mRollingConfidence);
        float f = 3;
        if (rollingAverage.mTotal / f > 0.10000000149011612d) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            State state3 = this.mState;
            if (state3 != state && state3 != state2) {
                State state4 = State.WAITING_FOR_SPEECH;
                if (state3 != state4 || this.mAnimator == null) {
                    this.mState = state4;
                    edgeLightArr = createPerturbedLights();
                    i = 1200;
                } else {
                    return;
                }
            } else if (this.mAnimator == null || !(state3 == state2 || state3 == state)) {
                this.mState = state2;
                edgeLightArr = getFinalLights();
                i = 2000;
            } else {
                return;
            }
        } else if (this.mState != state || this.mAnimator == null) {
            this.mState = state;
            edgeLightArr = createPerturbedLights();
            Objects.requireNonNull(this.mRollingConfidence);
            i = (int) MathUtils.lerp(400, 150, rollingAverage2.mTotal / f);
        } else {
            return;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addListener(new AnonymousClass1());
        ofFloat.setDuration(i);
        ofFloat.setInterpolator(INTERPOLATOR);
        ofFloat.addUpdateListener(new EdgeLightUpdateListener(EdgeLight.copy(this.mLights), edgeLightArr, this.mLights, this.mEdgeLightsView));
        setAnimator(ofFloat);
    }

    public FullListening(Context context, boolean z) {
        this.mFakeForHalfListening = z;
        this.mLights = new EdgeLight[]{new EdgeLight(context.getResources().getColor(2131099853, null)), new EdgeLight(context.getResources().getColor(2131099855, null)), new EdgeLight(context.getResources().getColor(2131099856, null)), new EdgeLight(context.getResources().getColor(2131099854, null))};
    }
}
