package com.google.android.systemui.assist.uihints;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.metrics.LogMaker;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.util.MathUtils;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
import com.android.systemui.DejankUtils;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.assist.AssistantSessionEvent;
import com.android.systemui.assist.ui.EdgeLight;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda0;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda1;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda2;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda15;
import com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda2;
import com.google.android.systemui.assist.uihints.AssistantPresenceHandler;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import com.google.android.systemui.assist.uihints.edgelights.mode.FullListening;
import com.google.android.systemui.assist.uihints.edgelights.mode.Gone;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NgaUiController implements AssistManager.UiController, ViewTreeObserver.OnComputeInternalInsetsListener, StatusBarStateController.StateListener {
    public static final boolean VERBOSE;
    public static final PathInterpolator mProgressInterpolator;
    public final AssistLogger mAssistLogger;
    public final Lazy<AssistManager> mAssistManager;
    public final AssistantPresenceHandler mAssistantPresenceHandler;
    public final AssistantWarmer mAssistantWarmer;
    public final ColorChangeHandler mColorChangeHandler;
    public final Context mContext;
    public final EdgeLightsController mEdgeLightsController;
    public final FlingVelocityWrapper mFlingVelocity;
    public final GlowController mGlowController;
    public final IconController mIconController;
    public ValueAnimator mInvocationAnimator;
    public AssistantInvocationLightsView mInvocationLightsView;
    public final LightnessProvider mLightnessProvider;
    public final NavBarFader mNavBarFader;
    public Runnable mPendingEdgeLightsModeChange;
    public PromptView mPromptView;
    public final ScrimController mScrimController;
    public final TimeoutManager mTimeoutManager;
    public final TouchInsideHandler mTouchInsideHandler;
    public final TranscriptionController mTranscriptionController;
    public final OverlayUiHost mUiHost;
    public PowerManager.WakeLock mWakeLock;
    public final Handler mUiHandler = new Handler(Looper.getMainLooper());
    public boolean mHasDarkBackground = false;
    public boolean mIsMonitoringColor = false;
    public boolean mInvocationInProgress = false;
    public boolean mShowingAssistUi = false;
    public boolean mShouldKeepWakeLock = false;
    public long mLastInvocationStartTime = 0;
    public float mLastInvocationProgress = 0.0f;
    public long mColorMonitoringStart = 0;

    /* renamed from: com.google.android.systemui.assist.uihints.NgaUiController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        public static final /* synthetic */ int $r8$clinit = 0;
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
            if (!this.mCancelled) {
                NgaUiController ngaUiController = NgaUiController.this;
                Runnable runnable = ngaUiController.mPendingEdgeLightsModeChange;
                if (runnable == null) {
                    EdgeLightsController edgeLightsController = ngaUiController.mEdgeLightsController;
                    Objects.requireNonNull(edgeLightsController);
                    edgeLightsController.getMode().onNewModeRequest(edgeLightsController.mEdgeLightsView, new FullListening(edgeLightsController.mContext, false));
                } else {
                    runnable.run();
                    NgaUiController.this.mPendingEdgeLightsModeChange = null;
                }
            }
            NgaUiController.this.mUiHandler.post(new WifiEntry$$ExternalSyntheticLambda2(this, 13));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0035  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onComputeInternalInsets(android.view.ViewTreeObserver.InternalInsetsInfo r7) {
        /*
            r6 = this;
            r0 = 3
            r7.setTouchableInsets(r0)
            android.graphics.Region r1 = new android.graphics.Region
            r1.<init>()
            com.google.android.systemui.assist.uihints.IconController r2 = r6.mIconController
            java.util.Optional r2 = r2.getTouchActionRegion()
            com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda1 r3 = new com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda1
            r4 = 4
            r3.<init>(r1, r4)
            r2.ifPresent(r3)
            android.graphics.Region r2 = new android.graphics.Region
            r2.<init>()
            com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController r3 = r6.mEdgeLightsController
            com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView$Mode r3 = r3.getMode()
            boolean r5 = r3 instanceof com.google.android.systemui.assist.uihints.edgelights.mode.FullListening
            if (r5 == 0) goto L_0x0032
            com.google.android.systemui.assist.uihints.edgelights.mode.FullListening r3 = (com.google.android.systemui.assist.uihints.edgelights.mode.FullListening) r3
            java.util.Objects.requireNonNull(r3)
            boolean r3 = r3.mFakeForHalfListening
            if (r3 == 0) goto L_0x0032
            r3 = 1
            goto L_0x0033
        L_0x0032:
            r3 = 0
        L_0x0033:
            if (r3 != 0) goto L_0x0043
            com.google.android.systemui.assist.uihints.GlowController r3 = r6.mGlowController
            java.util.Optional r3 = r3.getTouchInsideRegion()
            com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda4 r5 = new com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda4
            r5.<init>(r2, r4)
            r3.ifPresent(r5)
        L_0x0043:
            com.google.android.systemui.assist.uihints.ScrimController r3 = r6.mScrimController
            java.util.Optional r3 = r3.getTouchInsideRegion()
            com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda0 r4 = new com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda0
            r4.<init>(r2, r0)
            r3.ifPresent(r4)
            com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda1 r0 = new com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda1
            r3 = 5
            r0.<init>(r2, r3)
            com.google.android.systemui.assist.uihints.TranscriptionController r3 = r6.mTranscriptionController
            java.util.Optional r3 = r3.getTouchInsideRegion()
            r3.ifPresent(r0)
            com.google.android.systemui.assist.uihints.TranscriptionController r6 = r6.mTranscriptionController
            java.util.Optional r6 = r6.getTouchActionRegion()
            r6.ifPresent(r0)
            android.graphics.Region$Op r6 = android.graphics.Region.Op.UNION
            r1.op(r2, r6)
            android.graphics.Region r6 = r7.touchableRegion
            r6.set(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.assist.uihints.NgaUiController.onComputeInternalInsets(android.view.ViewTreeObserver$InternalInsetsInfo):void");
    }

    static {
        boolean z;
        String str = Build.TYPE;
        Locale locale = Locale.ROOT;
        if (str.toLowerCase(locale).contains("debug") || str.toLowerCase(locale).equals("eng")) {
            z = true;
        } else {
            z = false;
        }
        VERBOSE = z;
        mProgressInterpolator = new PathInterpolator(0.83f, 0.0f, 0.84f, 1.0f);
    }

    public NgaUiController(Context context, TimeoutManager timeoutManager, AssistantPresenceHandler assistantPresenceHandler, TouchInsideHandler touchInsideHandler, ColorChangeHandler colorChangeHandler, OverlayUiHost overlayUiHost, EdgeLightsController edgeLightsController, GlowController glowController, ScrimController scrimController, TranscriptionController transcriptionController, IconController iconController, LightnessProvider lightnessProvider, StatusBarStateController statusBarStateController, Lazy<AssistManager> lazy, FlingVelocityWrapper flingVelocityWrapper, AssistantWarmer assistantWarmer, NavBarFader navBarFader, AssistLogger assistLogger) {
        boolean z = false;
        this.mContext = context;
        this.mAssistLogger = assistLogger;
        this.mColorChangeHandler = colorChangeHandler;
        Objects.requireNonNull(colorChangeHandler);
        colorChangeHandler.mIsDark = false;
        colorChangeHandler.sendColor();
        this.mTimeoutManager = timeoutManager;
        this.mAssistantPresenceHandler = assistantPresenceHandler;
        this.mTouchInsideHandler = touchInsideHandler;
        this.mUiHost = overlayUiHost;
        this.mEdgeLightsController = edgeLightsController;
        this.mGlowController = glowController;
        this.mScrimController = scrimController;
        this.mTranscriptionController = transcriptionController;
        this.mIconController = iconController;
        this.mLightnessProvider = lightnessProvider;
        this.mAssistManager = lazy;
        this.mFlingVelocity = flingVelocityWrapper;
        this.mAssistantWarmer = assistantWarmer;
        this.mNavBarFader = navBarFader;
        NgaUiController$$ExternalSyntheticLambda1 ngaUiController$$ExternalSyntheticLambda1 = new NgaUiController$$ExternalSyntheticLambda1(this);
        Objects.requireNonNull(lightnessProvider);
        lightnessProvider.mListener = ngaUiController$$ExternalSyntheticLambda1;
        AssistantPresenceHandler.SysUiIsNgaUiChangeListener ngaUiController$$ExternalSyntheticLambda4 = new AssistantPresenceHandler.SysUiIsNgaUiChangeListener() { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda4
            @Override // com.google.android.systemui.assist.uihints.AssistantPresenceHandler.SysUiIsNgaUiChangeListener
            public final void onSysUiIsNgaUiChanged(boolean z2) {
                NgaUiController ngaUiController = NgaUiController.this;
                if (!z2) {
                    ngaUiController.hide();
                } else {
                    Objects.requireNonNull(ngaUiController);
                }
            }
        };
        Objects.requireNonNull(assistantPresenceHandler);
        assistantPresenceHandler.mSysUiIsNgaUiChangeListeners.add(ngaUiController$$ExternalSyntheticLambda4);
        CarrierTextManager$$ExternalSyntheticLambda0 carrierTextManager$$ExternalSyntheticLambda0 = new CarrierTextManager$$ExternalSyntheticLambda0(this, 11);
        Objects.requireNonNull(touchInsideHandler);
        touchInsideHandler.mFallback = carrierTextManager$$ExternalSyntheticLambda0;
        NgaUiController$$ExternalSyntheticLambda2 ngaUiController$$ExternalSyntheticLambda2 = new NgaUiController$$ExternalSyntheticLambda2(this);
        Objects.requireNonNull(edgeLightsController);
        edgeLightsController.mThrottler = ngaUiController$$ExternalSyntheticLambda2;
        this.mWakeLock = ((PowerManager) context.getSystemService(GlobalActionsDialogLite.GLOBAL_ACTION_KEY_POWER)).newWakeLock(805306378, "Assist (NGA)");
        PipTouchHandler$$ExternalSyntheticLambda2 pipTouchHandler$$ExternalSyntheticLambda2 = new PipTouchHandler$$ExternalSyntheticLambda2(this);
        Objects.requireNonNull(glowController);
        glowController.mVisibilityListener = pipTouchHandler$$ExternalSyntheticLambda2;
        Objects.requireNonNull(scrimController);
        scrimController.mVisibilityListener = pipTouchHandler$$ExternalSyntheticLambda2;
        Objects.requireNonNull(overlayUiHost);
        AssistUIView assistUIView = overlayUiHost.mRoot;
        AssistantInvocationLightsView assistantInvocationLightsView = (AssistantInvocationLightsView) assistUIView.findViewById(2131428136);
        this.mInvocationLightsView = assistantInvocationLightsView;
        Objects.requireNonNull(assistantInvocationLightsView);
        int i = assistantInvocationLightsView.mColorBlue;
        int i2 = assistantInvocationLightsView.mColorRed;
        int i3 = assistantInvocationLightsView.mColorYellow;
        int i4 = assistantInvocationLightsView.mColorGreen;
        assistantInvocationLightsView.mUseNavBarColor = false;
        assistantInvocationLightsView.attemptUnregisterNavBarListener();
        EdgeLight edgeLight = assistantInvocationLightsView.mAssistInvocationLights.get(0);
        Objects.requireNonNull(edgeLight);
        edgeLight.mColor = i;
        EdgeLight edgeLight2 = assistantInvocationLightsView.mAssistInvocationLights.get(1);
        Objects.requireNonNull(edgeLight2);
        edgeLight2.mColor = i2;
        EdgeLight edgeLight3 = assistantInvocationLightsView.mAssistInvocationLights.get(2);
        Objects.requireNonNull(edgeLight3);
        edgeLight3.mColor = i3;
        EdgeLight edgeLight4 = assistantInvocationLightsView.mAssistInvocationLights.get(3);
        Objects.requireNonNull(edgeLight4);
        edgeLight4.mColor = i4;
        EdgeLightsView edgeLightsView = edgeLightsController.mEdgeLightsView;
        Objects.requireNonNull(edgeLightsView);
        edgeLightsView.mListeners.add(glowController);
        EdgeLightsView edgeLightsView2 = edgeLightsController.mEdgeLightsView;
        Objects.requireNonNull(edgeLightsView2);
        edgeLightsView2.mListeners.add(scrimController);
        Objects.requireNonNull(transcriptionController);
        transcriptionController.mListener = scrimController;
        z = transcriptionController.mCurrentState != TranscriptionController.State.NONE ? true : z;
        if (scrimController.mTranscriptionVisible != z) {
            scrimController.mTranscriptionVisible = z;
            scrimController.refresh();
        }
        this.mPromptView = (PromptView) assistUIView.findViewById(2131428632);
        dispatchHasDarkBackground();
        statusBarStateController.addCallback(this);
        refresh();
        NgaUiController$$ExternalSyntheticLambda3 ngaUiController$$ExternalSyntheticLambda3 = new NgaUiController$$ExternalSyntheticLambda3(new ScrimView$$ExternalSyntheticLambda0(this, 8));
        Objects.requireNonNull(timeoutManager);
        timeoutManager.mTimeoutCallback = ngaUiController$$ExternalSyntheticLambda3;
    }

    public final void closeNgaUi() {
        this.mAssistManager.get().hideAssist();
        hide();
    }

    public final void completeInvocation(final int i) {
        float f;
        AssistantPresenceHandler assistantPresenceHandler = this.mAssistantPresenceHandler;
        Objects.requireNonNull(assistantPresenceHandler);
        if (!assistantPresenceHandler.mSysUiIsNgaUi) {
            setProgress(i, 0.0f);
            this.mInvocationInProgress = false;
            this.mInvocationLightsView.hide();
            this.mLastInvocationProgress = 0.0f;
            ScrimController scrimController = this.mScrimController;
            Objects.requireNonNull(scrimController);
            float constrain = MathUtils.constrain(0.0f, 0.0f, 1.0f);
            if (scrimController.mInvocationProgress != constrain) {
                scrimController.mInvocationProgress = constrain;
                scrimController.refresh();
            }
            refresh();
            return;
        }
        TouchInsideHandler touchInsideHandler = this.mTouchInsideHandler;
        Objects.requireNonNull(touchInsideHandler);
        if (!touchInsideHandler.mInGesturalMode) {
            touchInsideHandler.mGuardLocked = true;
            touchInsideHandler.mGuarded = true;
            touchInsideHandler.mHandler.postDelayed(new WMShell$7$$ExternalSyntheticLambda1(touchInsideHandler, 8), 500L);
        }
        TimeoutManager timeoutManager = this.mTimeoutManager;
        Objects.requireNonNull(timeoutManager);
        timeoutManager.mHandler.removeCallbacks(timeoutManager.mOnTimeout);
        timeoutManager.mHandler.postDelayed(timeoutManager.mOnTimeout, TimeoutManager.SESSION_TIMEOUT_MS);
        this.mPromptView.disable$1();
        ValueAnimator valueAnimator = this.mInvocationAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            this.mInvocationAnimator.cancel();
        }
        FlingVelocityWrapper flingVelocityWrapper = this.mFlingVelocity;
        Objects.requireNonNull(flingVelocityWrapper);
        float f2 = flingVelocityWrapper.mVelocity;
        float f3 = 3.0f;
        if (f2 != 0.0f) {
            f3 = MathUtils.constrain((-f2) / 1.45f, 3.0f, 12.0f);
        }
        final OvershootInterpolator overshootInterpolator = new OvershootInterpolator(f3);
        float f4 = this.mLastInvocationProgress;
        if (i == 2) {
            f = f4 * 0.95f;
        } else {
            f = mProgressInterpolator.getInterpolation(f4 * 0.8f);
        }
        Float valueOf = Float.valueOf(f);
        ArrayList arrayList = new ArrayList((int) 200.0f);
        for (float f5 = 0.0f; f5 < 1.0f; f5 += 0.005f) {
            arrayList.add(Float.valueOf(Math.min(1.0f, overshootInterpolator.getInterpolation(Float.valueOf(f5).floatValue()))));
        }
        int binarySearch = Collections.binarySearch(arrayList, valueOf);
        if (binarySearch < 0) {
            binarySearch = (binarySearch + 1) * (-1);
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(binarySearch * 0.005f, 1.0f);
        ofFloat.setDuration(600L);
        ofFloat.setStartDelay(1L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                NgaUiController ngaUiController = NgaUiController.this;
                int i2 = i;
                OvershootInterpolator overshootInterpolator2 = overshootInterpolator;
                Objects.requireNonNull(ngaUiController);
                ngaUiController.setProgress(i2, overshootInterpolator2.getInterpolation(((Float) valueAnimator2.getAnimatedValue()).floatValue()));
            }
        });
        ofFloat.addListener(new AnonymousClass1());
        this.mInvocationAnimator = ofFloat;
        ofFloat.start();
    }

    public final void dispatchHasDarkBackground() {
        int i;
        int i2;
        int i3;
        TranscriptionController transcriptionController = this.mTranscriptionController;
        boolean z = this.mHasDarkBackground;
        Objects.requireNonNull(transcriptionController);
        for (TranscriptionController.TranscriptionSpaceView transcriptionSpaceView : transcriptionController.mViewMap.values()) {
            transcriptionSpaceView.setHasDarkBackground(z);
        }
        IconController iconController = this.mIconController;
        boolean z2 = this.mHasDarkBackground;
        Objects.requireNonNull(iconController);
        KeyboardIconView keyboardIconView = iconController.mKeyboardIcon;
        Objects.requireNonNull(keyboardIconView);
        ImageView imageView = keyboardIconView.mKeyboardIcon;
        if (z2) {
            i = keyboardIconView.COLOR_DARK_BACKGROUND;
        } else {
            i = keyboardIconView.COLOR_LIGHT_BACKGROUND;
        }
        imageView.setImageTintList(ColorStateList.valueOf(i));
        ZeroStateIconView zeroStateIconView = iconController.mZeroStateIcon;
        Objects.requireNonNull(zeroStateIconView);
        ImageView imageView2 = zeroStateIconView.mZeroStateIcon;
        if (z2) {
            i2 = zeroStateIconView.COLOR_DARK_BACKGROUND;
        } else {
            i2 = zeroStateIconView.COLOR_LIGHT_BACKGROUND;
        }
        imageView2.setImageTintList(ColorStateList.valueOf(i2));
        PromptView promptView = this.mPromptView;
        boolean z3 = this.mHasDarkBackground;
        Objects.requireNonNull(promptView);
        if (z3 != promptView.mHasDarkBackground) {
            if (z3) {
                i3 = promptView.mTextColorDark;
            } else {
                i3 = promptView.mTextColorLight;
            }
            promptView.setTextColor(i3);
            promptView.mHasDarkBackground = z3;
        }
    }

    @Override // com.android.systemui.assist.AssistManager.UiController
    public final void hide() {
        ValueAnimator valueAnimator = this.mInvocationAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            this.mInvocationAnimator.cancel();
        }
        this.mInvocationInProgress = false;
        this.mTranscriptionController.onClear(false);
        EdgeLightsController edgeLightsController = this.mEdgeLightsController;
        Objects.requireNonNull(edgeLightsController);
        edgeLightsController.getMode().onNewModeRequest(edgeLightsController.mEdgeLightsView, new Gone());
        this.mPendingEdgeLightsModeChange = null;
        this.mPromptView.disable$1();
        this.mIconController.onHideKeyboard();
        this.mIconController.onHideZerostate();
        refresh();
    }

    public final void logInvocationProgressMetrics(int i, float f, boolean z) {
        if (f == 1.0f && VERBOSE) {
            Log.v("NgaUiController", "Invocation complete: type=" + i);
        }
        if (!z && f > 0.0f) {
            if (VERBOSE) {
                Log.v("NgaUiController", "Invocation started: type=" + i);
            }
            this.mAssistLogger.reportAssistantInvocationEventFromLegacy(i, false, null, null);
            LogMaker type = new LogMaker(1716).setType(4);
            AssistManager assistManager = this.mAssistManager.get();
            Objects.requireNonNull(assistManager);
            MetricsLogger.action(type.setSubtype((assistManager.mPhoneStateMonitor.getPhoneState() << 4) | 0 | (i << 1)));
        }
        ValueAnimator valueAnimator = this.mInvocationAnimator;
        if ((valueAnimator == null || !valueAnimator.isRunning()) && z && f == 0.0f) {
            if (VERBOSE) {
                Log.v("NgaUiController", "Invocation cancelled: type=" + i);
            }
            this.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_INVOCATION_CANCELLED);
            MetricsLogger.action(new LogMaker(1716).setType(5).setSubtype(1));
        }
    }

    @Override // com.android.systemui.assist.AssistManager.UiController
    public final void onGestureCompletion(float f) {
        if (!this.mEdgeLightsController.getMode().preventsInvocations()) {
            FlingVelocityWrapper flingVelocityWrapper = this.mFlingVelocity;
            Objects.requireNonNull(flingVelocityWrapper);
            flingVelocityWrapper.mVelocity = f;
            flingVelocityWrapper.mGuarded = false;
            completeInvocation(1);
            logInvocationProgressMetrics(1, 1.0f, this.mInvocationInProgress);
        } else if (VERBOSE) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ignoring invocation; mode is ");
            m.append(this.mEdgeLightsController.getMode().getClass().getSimpleName());
            Log.v("NgaUiController", m.toString());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x00f0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // com.android.systemui.assist.AssistManager.UiController
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onInvocationProgress(int r11, float r12) {
        /*
            Method dump skipped, instructions count: 267
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.assist.uihints.NgaUiController.onInvocationProgress(int, float):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x00a0, code lost:
        if (r5 == null) goto L_0x0175;
     */
    /* JADX WARN: Removed duplicated region for block: B:19:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0179  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0214  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x022d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void refresh() {
        /*
            Method dump skipped, instructions count: 568
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.assist.uihints.NgaUiController.refresh():void");
    }

    public final void setProgress(int i, float f) {
        int i2;
        this.mInvocationLightsView.onInvocationProgress(f);
        GlowController glowController = this.mGlowController;
        Objects.requireNonNull(glowController);
        if (glowController.mEdgeLightsMode instanceof Gone) {
            if (f > 0.0f) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            glowController.setVisibility(i2);
            GlowView glowView = glowController.mGlowView;
            int lerp = (int) MathUtils.lerp(glowController.getBlurRadius(), glowController.mContext.getResources().getDimensionPixelSize(2131165787), Math.min(1.0f, 5.0f * f));
            Objects.requireNonNull(glowView);
            if (glowView.mBlurRadius != lerp) {
                glowView.setBlurredImageOnViews(lerp);
            }
            int min = (int) MathUtils.min((int) MathUtils.lerp(glowController.getMinTranslationY(), glowController.mContext.getResources().getDimensionPixelSize(2131165789), f), glowController.mContext.getResources().getDimensionPixelSize(2131165783));
            glowController.mGlowsY = min;
            glowController.mGlowsYDestination = min;
            glowController.mGlowView.setGlowsY(min, min, null);
            glowController.mGlowView.distributeEvenly();
        }
        ScrimController scrimController = this.mScrimController;
        Objects.requireNonNull(scrimController);
        float constrain = MathUtils.constrain(f, 0.0f, 1.0f);
        if (scrimController.mInvocationProgress != constrain) {
            scrimController.mInvocationProgress = constrain;
            scrimController.refresh();
        }
        PromptView promptView = this.mPromptView;
        if (f > 1.0f) {
            Objects.requireNonNull(promptView);
        } else if (f == 0.0f) {
            promptView.setVisibility(8);
            promptView.setAlpha(0.0f);
            promptView.setTranslationY(0.0f);
            promptView.mLastInvocationType = 0;
        } else if (promptView.mEnabled) {
            if (i != 1) {
                if (i != 2) {
                    promptView.mLastInvocationType = 0;
                    promptView.setText("");
                } else if (promptView.mLastInvocationType != i) {
                    promptView.mLastInvocationType = i;
                    promptView.setText(promptView.mSqueezeString);
                    promptView.announceForAccessibility(promptView.mSqueezeString);
                }
            } else if (promptView.mLastInvocationType != i) {
                promptView.mLastInvocationType = i;
                promptView.setText(promptView.mHandleString);
                promptView.announceForAccessibility(promptView.mHandleString);
            }
            promptView.setVisibility(0);
            promptView.setTranslationY((-promptView.mRiseDistance) * f);
            if (i != 2 && f > 0.8f) {
                promptView.setAlpha(0.0f);
            } else if (f > 0.32000002f) {
                promptView.setAlpha(1.0f);
            } else {
                promptView.setAlpha(promptView.mDecelerateInterpolator.getInterpolation(f / 0.32000002f));
            }
        }
        refresh();
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onDozingChanged(final boolean z) {
        if (Looper.myLooper() != this.mUiHandler.getLooper()) {
            this.mUiHandler.post(new Runnable() { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    NgaUiController ngaUiController = NgaUiController.this;
                    boolean z2 = z;
                    Objects.requireNonNull(ngaUiController);
                    ngaUiController.onDozingChanged(z2);
                }
            });
            return;
        }
        ScrimController scrimController = this.mScrimController;
        Objects.requireNonNull(scrimController);
        scrimController.mIsDozing = z;
        scrimController.refresh();
        if (z && this.mShowingAssistUi) {
            DejankUtils.whitelistIpcs(new BubbleStackView$$ExternalSyntheticLambda15(this, 11));
        }
    }
}
