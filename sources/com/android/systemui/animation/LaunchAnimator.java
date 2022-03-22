package com.android.systemui.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.GradientDrawable;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewOverlay;
import android.view.animation.Interpolator;
import androidx.leanback.R$drawable;
import com.android.systemui.animation.LaunchAnimator;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref$BooleanRef;
import kotlin.jvm.internal.Ref$FloatRef;
import kotlin.jvm.internal.Ref$IntRef;
/* compiled from: LaunchAnimator.kt */
/* loaded from: classes.dex */
public final class LaunchAnimator {
    public static final PorterDuffXfermode SRC_MODE = new PorterDuffXfermode(PorterDuff.Mode.SRC);
    public final Interpolators interpolators;
    public final Timings timings;
    public final int[] launchContainerLocation = new int[2];
    public final float[] cornerRadii = new float[8];

    /* compiled from: LaunchAnimator.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public static float getProgress(Timings timings, float f, long j, long j2) {
            return MathUtils.constrain(((f * ((float) timings.totalDuration)) - ((float) j)) / ((float) j2), 0.0f, 1.0f);
        }
    }

    /* compiled from: LaunchAnimator.kt */
    /* loaded from: classes.dex */
    public interface Controller {
        State createAnimatorState();

        ViewGroup getLaunchContainer();

        default View getOpeningWindowSyncView() {
            return null;
        }

        default void onLaunchAnimationEnd(boolean z) {
        }

        default void onLaunchAnimationProgress(State state, float f, float f2) {
        }

        default void onLaunchAnimationStart(boolean z) {
        }

        void setLaunchContainer(ViewGroup viewGroup);
    }

    /* compiled from: LaunchAnimator.kt */
    /* loaded from: classes.dex */
    public static final class Interpolators {
        public final Interpolator contentAfterFadeInInterpolator;
        public final Interpolator contentBeforeFadeOutInterpolator;
        public final Interpolator positionInterpolator;
        public final Interpolator positionXInterpolator;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Interpolators)) {
                return false;
            }
            Interpolators interpolators = (Interpolators) obj;
            return Intrinsics.areEqual(this.positionInterpolator, interpolators.positionInterpolator) && Intrinsics.areEqual(this.positionXInterpolator, interpolators.positionXInterpolator) && Intrinsics.areEqual(this.contentBeforeFadeOutInterpolator, interpolators.contentBeforeFadeOutInterpolator) && Intrinsics.areEqual(this.contentAfterFadeInInterpolator, interpolators.contentAfterFadeInInterpolator);
        }

        public final int hashCode() {
            int hashCode = this.positionXInterpolator.hashCode();
            int hashCode2 = this.contentBeforeFadeOutInterpolator.hashCode();
            return this.contentAfterFadeInInterpolator.hashCode() + ((hashCode2 + ((hashCode + (this.positionInterpolator.hashCode() * 31)) * 31)) * 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Interpolators(positionInterpolator=");
            m.append(this.positionInterpolator);
            m.append(", positionXInterpolator=");
            m.append(this.positionXInterpolator);
            m.append(", contentBeforeFadeOutInterpolator=");
            m.append(this.contentBeforeFadeOutInterpolator);
            m.append(", contentAfterFadeInInterpolator=");
            m.append(this.contentAfterFadeInInterpolator);
            m.append(')');
            return m.toString();
        }

        public Interpolators(Interpolator interpolator, Interpolator interpolator2, Interpolator interpolator3, Interpolator interpolator4) {
            this.positionInterpolator = interpolator;
            this.positionXInterpolator = interpolator2;
            this.contentBeforeFadeOutInterpolator = interpolator3;
            this.contentAfterFadeInInterpolator = interpolator4;
        }
    }

    /* compiled from: LaunchAnimator.kt */
    /* loaded from: classes.dex */
    public static final class Timings {
        public final long contentAfterFadeInDelay;
        public final long contentAfterFadeInDuration;
        public final long contentBeforeFadeOutDelay;
        public final long contentBeforeFadeOutDuration;
        public final long totalDuration;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Timings)) {
                return false;
            }
            Timings timings = (Timings) obj;
            return this.totalDuration == timings.totalDuration && this.contentBeforeFadeOutDelay == timings.contentBeforeFadeOutDelay && this.contentBeforeFadeOutDuration == timings.contentBeforeFadeOutDuration && this.contentAfterFadeInDelay == timings.contentAfterFadeInDelay && this.contentAfterFadeInDuration == timings.contentAfterFadeInDuration;
        }

        public final int hashCode() {
            int hashCode = Long.hashCode(this.contentBeforeFadeOutDelay);
            int hashCode2 = Long.hashCode(this.contentBeforeFadeOutDuration);
            int hashCode3 = Long.hashCode(this.contentAfterFadeInDelay);
            return Long.hashCode(this.contentAfterFadeInDuration) + ((hashCode3 + ((hashCode2 + ((hashCode + (Long.hashCode(this.totalDuration) * 31)) * 31)) * 31)) * 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Timings(totalDuration=");
            m.append(this.totalDuration);
            m.append(", contentBeforeFadeOutDelay=");
            m.append(this.contentBeforeFadeOutDelay);
            m.append(", contentBeforeFadeOutDuration=");
            m.append(this.contentBeforeFadeOutDuration);
            m.append(", contentAfterFadeInDelay=");
            m.append(this.contentAfterFadeInDelay);
            m.append(", contentAfterFadeInDuration=");
            m.append(this.contentAfterFadeInDuration);
            m.append(')');
            return m.toString();
        }

        public Timings(long j, long j2, long j3, long j4, long j5) {
            this.totalDuration = j;
            this.contentBeforeFadeOutDelay = j2;
            this.contentBeforeFadeOutDuration = j3;
            this.contentAfterFadeInDelay = j4;
            this.contentAfterFadeInDuration = j5;
        }
    }

    public final boolean isExpandingFullyAbove$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib(ViewGroup viewGroup, State state) {
        viewGroup.getLocationOnScreen(this.launchContainerLocation);
        int i = state.top;
        int[] iArr = this.launchContainerLocation;
        if (i <= iArr[1]) {
            if (state.bottom >= viewGroup.getHeight() + iArr[1]) {
                int i2 = state.left;
                int[] iArr2 = this.launchContainerLocation;
                if (i2 <= iArr2[0]) {
                    if (state.right >= viewGroup.getWidth() + iArr2[0]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public final LaunchAnimator$startAnimation$3 startAnimation(final Controller controller, final State state, int i, final boolean z) {
        int i2;
        final ViewOverlay viewOverlay;
        final boolean z2;
        final State createAnimatorState = controller.createAnimatorState();
        Objects.requireNonNull(createAnimatorState);
        final int i3 = createAnimatorState.top;
        final int i4 = createAnimatorState.bottom;
        int i5 = createAnimatorState.left;
        final float f = (i5 + i2) / 2.0f;
        final int i6 = createAnimatorState.right - i5;
        final float f2 = createAnimatorState.topCornerRadius;
        final float f3 = createAnimatorState.bottomCornerRadius;
        final Ref$IntRef ref$IntRef = new Ref$IntRef();
        ref$IntRef.element = state.top;
        final Ref$IntRef ref$IntRef2 = new Ref$IntRef();
        ref$IntRef2.element = state.bottom;
        final Ref$IntRef ref$IntRef3 = new Ref$IntRef();
        ref$IntRef3.element = state.left;
        final Ref$IntRef ref$IntRef4 = new Ref$IntRef();
        ref$IntRef4.element = state.right;
        final Ref$FloatRef ref$FloatRef = new Ref$FloatRef();
        ref$FloatRef.element = (ref$IntRef3.element + ref$IntRef4.element) / 2.0f;
        final Ref$IntRef ref$IntRef5 = new Ref$IntRef();
        ref$IntRef5.element = ref$IntRef4.element - ref$IntRef3.element;
        final float f4 = state.topCornerRadius;
        final float f5 = state.bottomCornerRadius;
        final ViewGroup launchContainer = controller.getLaunchContainer();
        final boolean isExpandingFullyAbove$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib = isExpandingFullyAbove$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib(launchContainer, state);
        final GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(i);
        gradientDrawable.setAlpha(0);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        Timings timings = this.timings;
        Objects.requireNonNull(timings);
        ofFloat.setDuration(timings.totalDuration);
        ofFloat.setInterpolator(com.android.systemui.animation.Interpolators.LINEAR);
        final View openingWindowSyncView = controller.getOpeningWindowSyncView();
        if (openingWindowSyncView == null) {
            viewOverlay = null;
        } else {
            viewOverlay = openingWindowSyncView.getOverlay();
        }
        if (openingWindowSyncView == null || Intrinsics.areEqual(openingWindowSyncView.getViewRootImpl(), controller.getLaunchContainer().getViewRootImpl())) {
            z2 = false;
        } else {
            z2 = true;
        }
        final ViewGroupOverlay overlay = launchContainer.getOverlay();
        final Ref$BooleanRef ref$BooleanRef = new Ref$BooleanRef();
        final Ref$BooleanRef ref$BooleanRef2 = new Ref$BooleanRef();
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.animation.LaunchAnimator$startAnimation$1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                ViewOverlay viewOverlay2;
                LaunchAnimator.Controller.this.onLaunchAnimationEnd(isExpandingFullyAbove$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib);
                overlay.remove(gradientDrawable);
                if (z2 && (viewOverlay2 = viewOverlay) != null) {
                    viewOverlay2.remove(gradientDrawable);
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public final void onAnimationStart(Animator animator, boolean z3) {
                LaunchAnimator.Controller.this.onLaunchAnimationStart(isExpandingFullyAbove$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib);
                overlay.add(gradientDrawable);
            }
        });
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.animation.LaunchAnimator$startAnimation$2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                boolean z3;
                View view;
                if (!Ref$BooleanRef.this.element) {
                    Ref$IntRef ref$IntRef6 = ref$IntRef;
                    LaunchAnimator.State state2 = state;
                    Ref$IntRef ref$IntRef7 = ref$IntRef2;
                    Ref$IntRef ref$IntRef8 = ref$IntRef3;
                    Ref$IntRef ref$IntRef9 = ref$IntRef4;
                    Ref$FloatRef ref$FloatRef2 = ref$FloatRef;
                    Ref$IntRef ref$IntRef10 = ref$IntRef5;
                    int i7 = ref$IntRef6.element;
                    Objects.requireNonNull(state2);
                    int i8 = state2.top;
                    if (!(i7 == i8 && ref$IntRef7.element == state2.bottom && ref$IntRef8.element == state2.left && ref$IntRef9.element == state2.right)) {
                        ref$IntRef6.element = i8;
                        ref$IntRef7.element = state2.bottom;
                        ref$IntRef8.element = state2.left;
                        int i9 = state2.right;
                        ref$IntRef9.element = i9;
                        int i10 = ref$IntRef8.element;
                        ref$FloatRef2.element = (i10 + i9) / 2.0f;
                        ref$IntRef10.element = i9 - i10;
                    }
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    LaunchAnimator.Interpolators interpolators = this.interpolators;
                    Objects.requireNonNull(interpolators);
                    float interpolation = interpolators.positionInterpolator.getInterpolation(animatedFraction);
                    LaunchAnimator.Interpolators interpolators2 = this.interpolators;
                    Objects.requireNonNull(interpolators2);
                    float lerp = MathUtils.lerp(f, ref$FloatRef.element, interpolators2.positionXInterpolator.getInterpolation(animatedFraction));
                    float lerp2 = MathUtils.lerp(i6, ref$IntRef5.element, interpolation) / 2.0f;
                    LaunchAnimator.State state3 = createAnimatorState;
                    int roundToInt = R$drawable.roundToInt(MathUtils.lerp(i3, ref$IntRef.element, interpolation));
                    Objects.requireNonNull(state3);
                    state3.top = roundToInt;
                    LaunchAnimator.State state4 = createAnimatorState;
                    int roundToInt2 = R$drawable.roundToInt(MathUtils.lerp(i4, ref$IntRef2.element, interpolation));
                    Objects.requireNonNull(state4);
                    state4.bottom = roundToInt2;
                    LaunchAnimator.State state5 = createAnimatorState;
                    int roundToInt3 = R$drawable.roundToInt(lerp - lerp2);
                    Objects.requireNonNull(state5);
                    state5.left = roundToInt3;
                    LaunchAnimator.State state6 = createAnimatorState;
                    int roundToInt4 = R$drawable.roundToInt(lerp + lerp2);
                    Objects.requireNonNull(state6);
                    state6.right = roundToInt4;
                    LaunchAnimator.State state7 = createAnimatorState;
                    float lerp3 = MathUtils.lerp(f2, f4, interpolation);
                    Objects.requireNonNull(state7);
                    state7.topCornerRadius = lerp3;
                    LaunchAnimator.State state8 = createAnimatorState;
                    float lerp4 = MathUtils.lerp(f3, f5, interpolation);
                    Objects.requireNonNull(state8);
                    state8.bottomCornerRadius = lerp4;
                    LaunchAnimator.State state9 = createAnimatorState;
                    PorterDuffXfermode porterDuffXfermode = LaunchAnimator.SRC_MODE;
                    LaunchAnimator.Timings timings2 = this.timings;
                    Objects.requireNonNull(timings2);
                    long j = timings2.contentBeforeFadeOutDelay;
                    LaunchAnimator.Timings timings3 = this.timings;
                    Objects.requireNonNull(timings3);
                    if (LaunchAnimator.Companion.getProgress(timings2, animatedFraction, j, timings3.contentBeforeFadeOutDuration) < 1.0f) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    Objects.requireNonNull(state9);
                    state9.visible = z3;
                    if (z2) {
                        LaunchAnimator.State state10 = createAnimatorState;
                        Objects.requireNonNull(state10);
                        if (!state10.visible) {
                            Ref$BooleanRef ref$BooleanRef3 = ref$BooleanRef2;
                            if (!ref$BooleanRef3.element) {
                                ref$BooleanRef3.element = true;
                                overlay.remove(gradientDrawable);
                                ViewOverlay viewOverlay2 = viewOverlay;
                                Intrinsics.checkNotNull(viewOverlay2);
                                viewOverlay2.add(gradientDrawable);
                                boolean z4 = ViewRootSync.forceDisableSynchronization;
                                ViewRootSync.synchronizeNextDraw(launchContainer, openingWindowSyncView, AnonymousClass1.INSTANCE);
                            }
                        }
                    }
                    if (ref$BooleanRef2.element) {
                        view = openingWindowSyncView;
                        Intrinsics.checkNotNull(view);
                    } else {
                        view = controller.getLaunchContainer();
                    }
                    LaunchAnimator launchAnimator = this;
                    GradientDrawable gradientDrawable2 = gradientDrawable;
                    LaunchAnimator.State state11 = createAnimatorState;
                    boolean z5 = z;
                    Objects.requireNonNull(launchAnimator);
                    view.getLocationOnScreen(launchAnimator.launchContainerLocation);
                    Objects.requireNonNull(state11);
                    int i11 = state11.left;
                    int[] iArr = launchAnimator.launchContainerLocation;
                    gradientDrawable2.setBounds(i11 - iArr[0], state11.top - iArr[1], state11.right - iArr[0], state11.bottom - iArr[1]);
                    float[] fArr = launchAnimator.cornerRadii;
                    float f6 = state11.topCornerRadius;
                    fArr[0] = f6;
                    fArr[1] = f6;
                    fArr[2] = f6;
                    fArr[3] = f6;
                    float f7 = state11.bottomCornerRadius;
                    fArr[4] = f7;
                    fArr[5] = f7;
                    fArr[6] = f7;
                    fArr[7] = f7;
                    gradientDrawable2.setCornerRadii(fArr);
                    LaunchAnimator.Timings timings4 = launchAnimator.timings;
                    Objects.requireNonNull(timings4);
                    long j2 = timings4.contentBeforeFadeOutDelay;
                    LaunchAnimator.Timings timings5 = launchAnimator.timings;
                    Objects.requireNonNull(timings5);
                    float progress = LaunchAnimator.Companion.getProgress(timings4, animatedFraction, j2, timings5.contentBeforeFadeOutDuration);
                    if (progress < 1.0f) {
                        LaunchAnimator.Interpolators interpolators3 = launchAnimator.interpolators;
                        Objects.requireNonNull(interpolators3);
                        gradientDrawable2.setAlpha(R$drawable.roundToInt(interpolators3.contentBeforeFadeOutInterpolator.getInterpolation(progress) * 255));
                    } else {
                        LaunchAnimator.Timings timings6 = launchAnimator.timings;
                        Objects.requireNonNull(timings6);
                        long j3 = timings6.contentAfterFadeInDelay;
                        LaunchAnimator.Timings timings7 = launchAnimator.timings;
                        Objects.requireNonNull(timings7);
                        float progress2 = LaunchAnimator.Companion.getProgress(timings6, animatedFraction, j3, timings7.contentAfterFadeInDuration);
                        LaunchAnimator.Interpolators interpolators4 = launchAnimator.interpolators;
                        Objects.requireNonNull(interpolators4);
                        gradientDrawable2.setAlpha(R$drawable.roundToInt((1 - interpolators4.contentAfterFadeInInterpolator.getInterpolation(progress2)) * 255));
                        if (z5) {
                            gradientDrawable2.setXfermode(LaunchAnimator.SRC_MODE);
                        }
                    }
                    controller.onLaunchAnimationProgress(createAnimatorState, interpolation, animatedFraction);
                }
            }

            /* compiled from: LaunchAnimator.kt */
            /* renamed from: com.android.systemui.animation.LaunchAnimator$startAnimation$2$1  reason: invalid class name */
            /* loaded from: classes.dex */
            public static final class AnonymousClass1 extends Lambda implements Function0<Unit> {
                public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

                public AnonymousClass1() {
                    super(0);
                }

                @Override // kotlin.jvm.functions.Function0
                public final /* bridge */ /* synthetic */ Unit invoke() {
                    return Unit.INSTANCE;
                }
            }
        });
        ofFloat.start();
        return new LaunchAnimator$startAnimation$3(ref$BooleanRef, ofFloat);
    }

    public LaunchAnimator(Timings timings, Interpolators interpolators) {
        this.timings = timings;
        this.interpolators = interpolators;
    }

    /* compiled from: LaunchAnimator.kt */
    /* loaded from: classes.dex */
    public static class State {
        public int bottom;
        public float bottomCornerRadius;
        public int left;
        public int right;
        public int top;
        public float topCornerRadius;
        public boolean visible;

        public State(int i, int i2, int i3, int i4, float f, float f2) {
            this.top = i;
            this.bottom = i2;
            this.left = i3;
            this.right = i4;
            this.topCornerRadius = f;
            this.bottomCornerRadius = f2;
            this.visible = true;
        }

        public /* synthetic */ State(int i, int i2, int i3, int i4, float f, float f2, int i5) {
            this((i5 & 1) != 0 ? 0 : i, (i5 & 2) != 0 ? 0 : i2, (i5 & 4) != 0 ? 0 : i3, (i5 & 8) != 0 ? 0 : i4, (i5 & 16) != 0 ? 0.0f : f, (i5 & 32) != 0 ? 0.0f : f2);
        }
    }
}
