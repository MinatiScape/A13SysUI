package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Trace;
import android.util.Log;
import android.util.MathUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.graphics.ColorUtils;
import com.android.keyguard.KeyguardStatusView$$ExternalSyntheticLambda0;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda1;
import com.android.settingslib.Utils;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.ShadeInterpolation;
import com.android.systemui.dock.DockManager;
import com.android.systemui.scrim.ScrimDrawable;
import com.android.systemui.scrim.ScrimView;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda4;
import com.android.systemui.statusbar.notification.stack.ViewState;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.AlarmTimeout;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda0;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda1;
import com.google.android.systemui.elmyra.actions.Action$$ExternalSyntheticLambda0;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class ScrimController implements ViewTreeObserver.OnPreDrawListener, Dumpable {
    public boolean mAnimateChange;
    public long mAnimationDelay;
    public Animator.AnimatorListener mAnimatorListener;
    public int mBehindTint;
    public boolean mBlankScreen;
    public WMShell$7$$ExternalSyntheticLambda0 mBlankingTransitionRunnable;
    public Callback mCallback;
    public boolean mClipsQsScrim;
    public boolean mDarkenWhileDragging;
    public final DockManager mDockManager;
    public final DozeParameters mDozeParameters;
    public final Handler mHandler;
    public int mInFrontTint;
    public boolean mKeyguardOccluded;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final Executor mMainExecutor;
    public boolean mNeedsDrawableColorUpdate;
    public ScrimView mNotificationsScrim;
    public int mNotificationsTint;
    public float mPanelScrimMinFraction;
    public WMShell$7$$ExternalSyntheticLambda1 mPendingFrameCallback;
    public boolean mQsBottomVisible;
    public float mQsExpansion;
    public float mRawPanelExpansionFraction;
    public boolean mScreenBlankingCallbackCalled;
    public final ScreenOffAnimationController mScreenOffAnimationController;
    public boolean mScreenOn;
    public ScrimView mScrimBehind;
    public Runnable mScrimBehindChangeRunnable;
    public ScrimView mScrimInFront;
    public final ScrimController$$ExternalSyntheticLambda2 mScrimStateListener;
    public Consumer<Integer> mScrimVisibleListener;
    public int mScrimsVisibility;
    public final AlarmTimeout mTimeTicker;
    public boolean mTracking;
    public float mTransitionToFullShadeProgress;
    public boolean mTransitioningToFullShade;
    public boolean mUnOcclusionAnimationRunning;
    public boolean mUpdatePending;
    public final DelayedWakeLock mWakeLock;
    public boolean mWakeLockHeld;
    public boolean mWallpaperSupportsAmbientMode;
    public boolean mWallpaperVisibilityTimedOut;
    public static final boolean DEBUG = Log.isLoggable("ScrimController", 3);
    public static final int TAG_KEY_ANIM = 2131428785;
    public static final int TAG_START_ALPHA = 2131428787;
    public static final int TAG_END_ALPHA = 2131428786;
    public ScrimState mState = ScrimState.UNINITIALIZED;
    public float mAdditionalScrimBehindAlphaKeyguard = 0.0f;
    public float mScrimBehindAlphaKeyguard = 0.2f;
    public float mPanelExpansionFraction = 1.0f;
    public boolean mExpansionAffectsAlpha = true;
    public long mAnimationDuration = -1;
    public final DecelerateInterpolator mInterpolator = new DecelerateInterpolator();
    public float mInFrontAlpha = -1.0f;
    public float mBehindAlpha = -1.0f;
    public float mNotificationsAlpha = -1.0f;
    public final float mDefaultScrimAlpha = 1.0f;
    public final KeyguardVisibilityCallback mKeyguardVisibilityCallback = new KeyguardVisibilityCallback();
    public ColorExtractor.GradientColors mColors = new ColorExtractor.GradientColors();

    /* loaded from: classes.dex */
    public interface Callback {
        default void onCancelled() {
        }

        default void onDisplayBlanked() {
        }

        default void onFinished() {
        }
    }

    /* loaded from: classes.dex */
    public class KeyguardVisibilityCallback extends KeyguardUpdateMonitorCallback {
        public KeyguardVisibilityCallback() {
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardVisibilityChanged(boolean z) {
            ScrimController scrimController = ScrimController.this;
            scrimController.mNeedsDrawableColorUpdate = true;
            scrimController.scheduleUpdate();
        }
    }

    public ScrimController(LightBarController lightBarController, DozeParameters dozeParameters, AlarmManager alarmManager, final KeyguardStateController keyguardStateController, DelayedWakeLock.Builder builder, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, ConfigurationController configurationController, Executor executor, ScreenOffAnimationController screenOffAnimationController, PanelExpansionStateManager panelExpansionStateManager) {
        Objects.requireNonNull(lightBarController);
        this.mScrimStateListener = new ScrimController$$ExternalSyntheticLambda2(lightBarController);
        this.mKeyguardStateController = keyguardStateController;
        this.mDarkenWhileDragging = !keyguardStateController.canDismissLockScreen();
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mHandler = handler;
        this.mMainExecutor = executor;
        this.mScreenOffAnimationController = screenOffAnimationController;
        this.mTimeTicker = new AlarmTimeout(alarmManager, new AlarmManager.OnAlarmListener() { // from class: com.android.systemui.statusbar.phone.ScrimController$$ExternalSyntheticLambda1
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                ScrimController.this.onHideWallpaperTimeout();
            }
        }, "hide_aod_wallpaper", handler);
        Objects.requireNonNull(builder);
        builder.mHandler = handler;
        builder.mTag = "Scrims";
        this.mWakeLock = new DelayedWakeLock(handler, WakeLock.createPartial(builder.mContext, "Scrims"));
        this.mDozeParameters = dozeParameters;
        this.mDockManager = dockManager;
        keyguardStateController.addCallback(new KeyguardStateController.Callback() { // from class: com.android.systemui.statusbar.phone.ScrimController.1
            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public final void onKeyguardFadingAwayChanged() {
                ScrimState[] values;
                ScrimController scrimController = ScrimController.this;
                boolean isKeyguardFadingAway = keyguardStateController.isKeyguardFadingAway();
                long keyguardFadingAwayDuration = keyguardStateController.getKeyguardFadingAwayDuration();
                Objects.requireNonNull(scrimController);
                for (ScrimState scrimState : ScrimState.values()) {
                    Objects.requireNonNull(scrimState);
                    scrimState.mKeyguardFadingAway = isKeyguardFadingAway;
                    scrimState.mKeyguardFadingAwayDuration = keyguardFadingAwayDuration;
                }
            }
        });
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.phone.ScrimController.2
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public final void onThemeChanged() {
                ScrimController scrimController = ScrimController.this;
                Objects.requireNonNull(scrimController);
                scrimController.updateThemeColors();
                scrimController.scheduleUpdate();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public final void onUiModeChanged() {
                ScrimController scrimController = ScrimController.this;
                Objects.requireNonNull(scrimController);
                scrimController.updateThemeColors();
                scrimController.scheduleUpdate();
            }
        });
        panelExpansionStateManager.addExpansionListener(new PanelExpansionListener() { // from class: com.android.systemui.statusbar.phone.ScrimController$$ExternalSyntheticLambda3
            @Override // com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener
            public final void onPanelExpansionChanged(float f, boolean z, boolean z2) {
                ScrimController scrimController = ScrimController.this;
                Objects.requireNonNull(scrimController);
                scrimController.setRawPanelExpansionFraction(f);
            }
        });
    }

    public final void setScrimAlpha(final ScrimView scrimView, float f) {
        boolean z;
        Callback callback;
        boolean z2;
        boolean z3 = true;
        if (f == 0.0f) {
            scrimView.setClickable(false);
        } else {
            if (this.mState != ScrimState.AOD) {
                z2 = true;
            } else {
                z2 = false;
            }
            scrimView.setClickable(z2);
        }
        Objects.requireNonNull(scrimView);
        float f2 = scrimView.mViewAlpha;
        int i = TAG_KEY_ANIM;
        ViewState.AnonymousClass1 r4 = ViewState.NO_NEW_ANIMATIONS;
        ValueAnimator valueAnimator = (ValueAnimator) scrimView.getTag(i);
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (this.mPendingFrameCallback == null) {
            if (this.mBlankScreen) {
                updateScrimColor(this.mScrimInFront, 1.0f, -16777216);
                WMShell$7$$ExternalSyntheticLambda1 wMShell$7$$ExternalSyntheticLambda1 = new WMShell$7$$ExternalSyntheticLambda1(this, 6);
                this.mPendingFrameCallback = wMShell$7$$ExternalSyntheticLambda1;
                doOnTheNextFrame(wMShell$7$$ExternalSyntheticLambda1);
                return;
            }
            if (!this.mScreenBlankingCallbackCalled && (callback = this.mCallback) != null) {
                callback.onDisplayBlanked();
                this.mScreenBlankingCallbackCalled = true;
            }
            if (scrimView == this.mScrimBehind) {
                dispatchBackScrimState(f);
            }
            if (f != f2) {
                z = true;
            } else {
                z = false;
            }
            if (scrimView.mTintColor == getCurrentScrimTint(scrimView)) {
                z3 = false;
            }
            if (!z && !z3) {
                return;
            }
            if (this.mAnimateChange) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                Animator.AnimatorListener animatorListener = this.mAnimatorListener;
                if (animatorListener != null) {
                    ofFloat.addListener(animatorListener);
                }
                final int i2 = scrimView.mTintColor;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.ScrimController$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        ScrimController scrimController = ScrimController.this;
                        View view = scrimView;
                        int i3 = i2;
                        Objects.requireNonNull(scrimController);
                        float floatValue = ((Float) view.getTag(ScrimController.TAG_START_ALPHA)).floatValue();
                        float floatValue2 = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                        scrimController.updateScrimColor(view, MathUtils.constrain(MathUtils.lerp(floatValue, scrimController.getCurrentScrimAlpha(view), floatValue2), 0.0f, 1.0f), ColorUtils.blendARGB(i3, scrimController.getCurrentScrimTint(view), floatValue2));
                        scrimController.dispatchScrimsVisible();
                    }
                });
                ofFloat.setInterpolator(this.mInterpolator);
                ofFloat.setStartDelay(this.mAnimationDelay);
                ofFloat.setDuration(this.mAnimationDuration);
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.ScrimController.3
                    public final Callback mLastCallback;
                    public final ScrimState mLastState;

                    {
                        this.mLastState = ScrimController.this.mState;
                        this.mLastCallback = ScrimController.this.mCallback;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        scrimView.setTag(ScrimController.TAG_KEY_ANIM, null);
                        ScrimController.this.onFinished(this.mLastCallback, this.mLastState);
                        ScrimController.this.dispatchScrimsVisible();
                    }
                });
                scrimView.setTag(TAG_START_ALPHA, Float.valueOf(f2));
                scrimView.setTag(TAG_END_ALPHA, Float.valueOf(getCurrentScrimAlpha(scrimView)));
                scrimView.setTag(i, ofFloat);
                ofFloat.start();
                return;
            }
            updateScrimColor(scrimView, f, getCurrentScrimTint(scrimView));
        }
    }

    public final void applyState() {
        ScrimState scrimState = ScrimState.SHADE_LOCKED;
        ScrimState scrimState2 = this.mState;
        Objects.requireNonNull(scrimState2);
        this.mInFrontTint = scrimState2.mFrontTint;
        this.mBehindTint = this.mState.getBehindTint();
        ScrimState scrimState3 = this.mState;
        Objects.requireNonNull(scrimState3);
        this.mNotificationsTint = scrimState3.mNotifTint;
        ScrimState scrimState4 = this.mState;
        Objects.requireNonNull(scrimState4);
        this.mInFrontAlpha = scrimState4.mFrontAlpha;
        ScrimState scrimState5 = this.mState;
        Objects.requireNonNull(scrimState5);
        this.mBehindAlpha = scrimState5.mBehindAlpha;
        ScrimState scrimState6 = this.mState;
        Objects.requireNonNull(scrimState6);
        this.mNotificationsAlpha = scrimState6.mNotifAlpha;
        assertAlphasValid();
        if (this.mExpansionAffectsAlpha) {
            ScrimState scrimState7 = this.mState;
            if (scrimState7 == ScrimState.UNLOCKED) {
                if (!this.mScreenOffAnimationController.shouldExpandNotifications()) {
                    float pow = (float) Math.pow(getInterpolatedFraction(), 0.800000011920929d);
                    if (this.mClipsQsScrim) {
                        this.mBehindAlpha = 1.0f;
                        this.mNotificationsAlpha = pow * this.mDefaultScrimAlpha;
                    } else {
                        this.mBehindAlpha = pow * this.mDefaultScrimAlpha;
                        this.mNotificationsAlpha = MathUtils.constrainedMap(0.0f, 1.0f, 0.3f, 0.75f, this.mPanelExpansionFraction);
                    }
                    this.mInFrontAlpha = 0.0f;
                }
            } else if (scrimState7 == ScrimState.AUTH_SCRIMMED_SHADE) {
                float pow2 = ((float) Math.pow(getInterpolatedFraction(), 0.800000011920929d)) * this.mDefaultScrimAlpha;
                this.mBehindAlpha = pow2;
                this.mNotificationsAlpha = pow2;
            } else {
                ScrimState scrimState8 = ScrimState.KEYGUARD;
                if (scrimState7 == scrimState8 || scrimState7 == scrimState || scrimState7 == ScrimState.PULSING) {
                    Pair<Integer, Float> calculateBackStateForState = calculateBackStateForState(scrimState7);
                    int intValue = ((Integer) calculateBackStateForState.first).intValue();
                    float floatValue = ((Float) calculateBackStateForState.second).floatValue();
                    if (this.mTransitionToFullShadeProgress > 0.0f) {
                        Pair<Integer, Float> calculateBackStateForState2 = calculateBackStateForState(scrimState);
                        floatValue = MathUtils.lerp(floatValue, ((Float) calculateBackStateForState2.second).floatValue(), this.mTransitionToFullShadeProgress);
                        intValue = ColorUtils.blendARGB(intValue, ((Integer) calculateBackStateForState2.first).intValue(), this.mTransitionToFullShadeProgress);
                    }
                    ScrimState scrimState9 = this.mState;
                    Objects.requireNonNull(scrimState9);
                    this.mInFrontAlpha = scrimState9.mFrontAlpha;
                    if (this.mClipsQsScrim) {
                        this.mNotificationsAlpha = floatValue;
                        this.mNotificationsTint = intValue;
                        this.mBehindAlpha = 1.0f;
                        this.mBehindTint = -16777216;
                    } else {
                        this.mBehindAlpha = floatValue;
                        if (this.mState == scrimState) {
                            this.mNotificationsAlpha = getInterpolatedFraction();
                        } else {
                            this.mNotificationsAlpha = Math.max(1.0f - getInterpolatedFraction(), this.mQsExpansion);
                        }
                        if (this.mState == scrimState8 && this.mTransitionToFullShadeProgress > 0.0f) {
                            this.mNotificationsAlpha = MathUtils.lerp(this.mNotificationsAlpha, getInterpolatedFraction(), this.mTransitionToFullShadeProgress);
                        }
                        ScrimState scrimState10 = this.mState;
                        Objects.requireNonNull(scrimState10);
                        this.mNotificationsTint = scrimState10.mNotifTint;
                        this.mBehindTint = intValue;
                    }
                    if (this.mKeyguardOccluded) {
                        this.mNotificationsAlpha = 0.0f;
                    }
                    if (this.mUnOcclusionAnimationRunning && this.mState == scrimState8) {
                        this.mNotificationsAlpha = this.mScrimBehindAlphaKeyguard;
                        this.mNotificationsTint = scrimState8.mNotifTint;
                    }
                }
            }
            assertAlphasValid();
        }
    }

    public final void assertAlphasValid() {
        if (Float.isNaN(this.mBehindAlpha) || Float.isNaN(this.mInFrontAlpha) || Float.isNaN(this.mNotificationsAlpha)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Scrim opacity is NaN for state: ");
            m.append(this.mState);
            m.append(", front: ");
            m.append(this.mInFrontAlpha);
            m.append(", back: ");
            m.append(this.mBehindAlpha);
            m.append(", notif: ");
            m.append(this.mNotificationsAlpha);
            throw new IllegalStateException(m.toString());
        }
    }

    public final void calculateAndUpdatePanelExpansion() {
        boolean z;
        float f = this.mRawPanelExpansionFraction;
        float f2 = this.mPanelScrimMinFraction;
        if (f2 < 1.0f) {
            f = Math.max((f - f2) / (1.0f - f2), 0.0f);
        }
        if (this.mPanelExpansionFraction != f) {
            this.mPanelExpansionFraction = f;
            ScrimState scrimState = this.mState;
            if (scrimState == ScrimState.UNLOCKED || scrimState == ScrimState.KEYGUARD || scrimState == ScrimState.SHADE_LOCKED || scrimState == ScrimState.PULSING) {
                z = true;
            } else {
                z = false;
            }
            if (z && this.mExpansionAffectsAlpha) {
                applyAndDispatchState();
            }
        }
    }

    public final Pair<Integer, Float> calculateBackStateForState(ScrimState scrimState) {
        float f;
        float f2;
        int i;
        int i2;
        ScrimState scrimState2 = ScrimState.BOUNCER;
        float interpolatedFraction = getInterpolatedFraction();
        boolean z = this.mClipsQsScrim;
        Objects.requireNonNull(scrimState);
        if (z) {
            f = scrimState.mNotifAlpha;
        } else {
            f = scrimState.mBehindAlpha;
        }
        float f3 = 0.0f;
        if (this.mDarkenWhileDragging) {
            f2 = MathUtils.lerp(this.mDefaultScrimAlpha, f, interpolatedFraction);
        } else {
            f2 = MathUtils.lerp(0.0f, f, interpolatedFraction);
        }
        if (this.mClipsQsScrim) {
            int i3 = scrimState2.mNotifTint;
            Objects.requireNonNull(scrimState);
            i = ColorUtils.blendARGB(i3, scrimState.mNotifTint, interpolatedFraction);
        } else {
            i = ColorUtils.blendARGB(scrimState2.mBehindTint, scrimState.getBehindTint(), interpolatedFraction);
        }
        float f4 = this.mQsExpansion;
        if (f4 > 0.0f) {
            f2 = MathUtils.lerp(f2, this.mDefaultScrimAlpha, f4);
            if (this.mClipsQsScrim) {
                i2 = ScrimState.SHADE_LOCKED.mNotifTint;
            } else {
                i2 = -16777216;
            }
            i = ColorUtils.blendARGB(i, i2, this.mQsExpansion);
        }
        if (!this.mKeyguardStateController.isKeyguardGoingAway()) {
            f3 = f2;
        }
        return new Pair<>(Integer.valueOf(i), Float.valueOf(f3));
    }

    public final void dispatchBackScrimState(float f) {
        if (this.mClipsQsScrim && this.mQsBottomVisible) {
            f = this.mNotificationsAlpha;
        }
        ScrimController$$ExternalSyntheticLambda2 scrimController$$ExternalSyntheticLambda2 = this.mScrimStateListener;
        ScrimState scrimState = this.mState;
        Float valueOf = Float.valueOf(f);
        ScrimView scrimView = this.mScrimInFront;
        Objects.requireNonNull(scrimView);
        synchronized (scrimView.mColorLock) {
            scrimView.mTmpColors.set(scrimView.mColors);
        }
        scrimController$$ExternalSyntheticLambda2.accept(scrimState, valueOf, scrimView.mTmpColors);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void dispatchScrimsVisible() {
        /*
            r3 = this;
            boolean r0 = r3.mClipsQsScrim
            if (r0 == 0) goto L_0x0007
            com.android.systemui.scrim.ScrimView r0 = r3.mNotificationsScrim
            goto L_0x0009
        L_0x0007:
            com.android.systemui.scrim.ScrimView r0 = r3.mScrimBehind
        L_0x0009:
            com.android.systemui.scrim.ScrimView r1 = r3.mScrimInFront
            java.util.Objects.requireNonNull(r1)
            float r1 = r1.mViewAlpha
            r2 = 1065353216(0x3f800000, float:1.0)
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 == 0) goto L_0x0036
            java.util.Objects.requireNonNull(r0)
            float r1 = r0.mViewAlpha
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 != 0) goto L_0x0020
            goto L_0x0036
        L_0x0020:
            com.android.systemui.scrim.ScrimView r1 = r3.mScrimInFront
            java.util.Objects.requireNonNull(r1)
            float r1 = r1.mViewAlpha
            r2 = 0
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 != 0) goto L_0x0034
            float r0 = r0.mViewAlpha
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x0034
            r0 = 0
            goto L_0x0037
        L_0x0034:
            r0 = 1
            goto L_0x0037
        L_0x0036:
            r0 = 2
        L_0x0037:
            int r1 = r3.mScrimsVisibility
            if (r1 == r0) goto L_0x0046
            r3.mScrimsVisibility = r0
            java.util.function.Consumer<java.lang.Integer> r3 = r3.mScrimVisibleListener
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r3.accept(r0)
        L_0x0046:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.ScrimController.dispatchScrimsVisible():void");
    }

    @VisibleForTesting
    public void doOnTheNextFrame(Runnable runnable) {
        this.mScrimBehind.postOnAnimationDelayed(runnable, 32L);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(" ScrimController: ");
        printWriter.print("  state: ");
        printWriter.println(this.mState);
        printWriter.println("    mClipQsScrim = " + this.mState.mClipQsScrim);
        printWriter.print("  frontScrim:");
        printWriter.print(" viewAlpha=");
        ScrimView scrimView = this.mScrimInFront;
        Objects.requireNonNull(scrimView);
        printWriter.print(scrimView.mViewAlpha);
        printWriter.print(" alpha=");
        printWriter.print(this.mInFrontAlpha);
        printWriter.print(" tint=0x");
        ScrimView scrimView2 = this.mScrimInFront;
        Objects.requireNonNull(scrimView2);
        printWriter.println(Integer.toHexString(scrimView2.mTintColor));
        printWriter.print("  behindScrim:");
        printWriter.print(" viewAlpha=");
        ScrimView scrimView3 = this.mScrimBehind;
        Objects.requireNonNull(scrimView3);
        printWriter.print(scrimView3.mViewAlpha);
        printWriter.print(" alpha=");
        printWriter.print(this.mBehindAlpha);
        printWriter.print(" tint=0x");
        ScrimView scrimView4 = this.mScrimBehind;
        Objects.requireNonNull(scrimView4);
        printWriter.println(Integer.toHexString(scrimView4.mTintColor));
        printWriter.print("  notificationsScrim:");
        printWriter.print(" viewAlpha=");
        ScrimView scrimView5 = this.mNotificationsScrim;
        Objects.requireNonNull(scrimView5);
        printWriter.print(scrimView5.mViewAlpha);
        printWriter.print(" alpha=");
        printWriter.print(this.mNotificationsAlpha);
        printWriter.print(" tint=0x");
        ScrimView scrimView6 = this.mNotificationsScrim;
        Objects.requireNonNull(scrimView6);
        printWriter.println(Integer.toHexString(scrimView6.mTintColor));
        printWriter.print("  mTracking=");
        printWriter.println(this.mTracking);
        printWriter.print("  mDefaultScrimAlpha=");
        printWriter.println(this.mDefaultScrimAlpha);
        printWriter.print("  mPanelExpansionFraction=");
        printWriter.println(this.mPanelExpansionFraction);
        printWriter.print("  mExpansionAffectsAlpha=");
        printWriter.println(this.mExpansionAffectsAlpha);
        printWriter.print("  mState.getMaxLightRevealScrimAlpha=");
        printWriter.println(this.mState.getMaxLightRevealScrimAlpha());
    }

    public final float getCurrentScrimAlpha(View view) {
        if (view == this.mScrimInFront) {
            return this.mInFrontAlpha;
        }
        if (view == this.mScrimBehind) {
            return this.mBehindAlpha;
        }
        if (view == this.mNotificationsScrim) {
            return this.mNotificationsAlpha;
        }
        throw new IllegalArgumentException("Unknown scrim view");
    }

    public final int getCurrentScrimTint(View view) {
        if (view == this.mScrimInFront) {
            return this.mInFrontTint;
        }
        if (view == this.mScrimBehind) {
            return this.mBehindTint;
        }
        if (view == this.mNotificationsScrim) {
            return this.mNotificationsTint;
        }
        throw new IllegalArgumentException("Unknown scrim view");
    }

    public final float getInterpolatedFraction() {
        return ShadeInterpolation.getNotificationScrimAlpha(this.mPanelExpansionFraction);
    }

    public final boolean isAnimating(View view) {
        if (view == null || view.getTag(TAG_KEY_ANIM) == null) {
            return false;
        }
        return true;
    }

    public final void onFinished(Callback callback, ScrimState scrimState) {
        if (this.mPendingFrameCallback == null) {
            if (!isAnimating(this.mScrimBehind) && !isAnimating(this.mNotificationsScrim) && !isAnimating(this.mScrimInFront)) {
                if (this.mWakeLockHeld) {
                    this.mWakeLock.release("ScrimController");
                    this.mWakeLockHeld = false;
                }
                if (callback != null) {
                    callback.onFinished();
                    if (callback == this.mCallback) {
                        this.mCallback = null;
                    }
                }
                if (scrimState == ScrimState.UNLOCKED) {
                    this.mInFrontTint = 0;
                    this.mBehindTint = this.mState.getBehindTint();
                    ScrimState scrimState2 = this.mState;
                    Objects.requireNonNull(scrimState2);
                    this.mNotificationsTint = scrimState2.mNotifTint;
                    updateScrimColor(this.mScrimInFront, this.mInFrontAlpha, this.mInFrontTint);
                    updateScrimColor(this.mScrimBehind, this.mBehindAlpha, this.mBehindTint);
                    updateScrimColor(this.mNotificationsScrim, this.mNotificationsAlpha, this.mNotificationsTint);
                }
            } else if (callback != null && callback != this.mCallback) {
                callback.onFinished();
            }
        }
    }

    @VisibleForTesting
    public void onHideWallpaperTimeout() {
        ScrimState scrimState = this.mState;
        if (scrimState == ScrimState.AOD || scrimState == ScrimState.PULSING) {
            if (!this.mWakeLockHeld) {
                DelayedWakeLock delayedWakeLock = this.mWakeLock;
                if (delayedWakeLock != null) {
                    this.mWakeLockHeld = true;
                    delayedWakeLock.acquire("ScrimController");
                } else {
                    Log.w("ScrimController", "Cannot hold wake lock, it has not been set yet");
                }
            }
            this.mWallpaperVisibilityTimedOut = true;
            this.mAnimateChange = true;
            DozeParameters dozeParameters = this.mDozeParameters;
            Objects.requireNonNull(dozeParameters);
            this.mAnimationDuration = dozeParameters.mAlwaysOnPolicy.wallpaperFadeOutDuration;
            scheduleUpdate();
        }
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public final boolean onPreDraw() {
        this.mScrimBehind.getViewTreeObserver().removeOnPreDrawListener(this);
        this.mUpdatePending = false;
        Callback callback = this.mCallback;
        if (callback != null) {
            Objects.requireNonNull(callback);
        }
        updateScrims();
        return true;
    }

    public final void scheduleUpdate() {
        ScrimView scrimView;
        if (!this.mUpdatePending && (scrimView = this.mScrimBehind) != null) {
            scrimView.invalidate();
            this.mScrimBehind.getViewTreeObserver().addOnPreDrawListener(this);
            this.mUpdatePending = true;
        }
    }

    public final void setNotificationsBounds(float f, float f2, float f3, float f4) {
        if (this.mClipsQsScrim) {
            ScrimView scrimView = this.mNotificationsScrim;
            float f5 = f - 1.0f;
            float f6 = f3 + 1.0f;
            Objects.requireNonNull(scrimView);
            if (scrimView.mDrawableBounds == null) {
                scrimView.mDrawableBounds = new Rect();
            }
            int i = (int) f2;
            scrimView.mDrawableBounds.set((int) f5, i, (int) f6, (int) f4);
            scrimView.mDrawable.setBounds(scrimView.mDrawableBounds);
            ScrimView scrimView2 = this.mScrimBehind;
            Objects.requireNonNull(scrimView2);
            Drawable drawable = scrimView2.mDrawable;
            if (drawable instanceof ScrimDrawable) {
                ScrimDrawable scrimDrawable = (ScrimDrawable) drawable;
                Objects.requireNonNull(scrimDrawable);
                if (scrimDrawable.mBottomEdgePosition != i) {
                    scrimDrawable.mBottomEdgePosition = i;
                    if (scrimDrawable.mConcaveInfo != null) {
                        scrimDrawable.updatePath();
                        scrimDrawable.invalidateSelf();
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        ScrimView scrimView3 = this.mNotificationsScrim;
        Objects.requireNonNull(scrimView3);
        if (scrimView3.mDrawableBounds == null) {
            scrimView3.mDrawableBounds = new Rect();
        }
        scrimView3.mDrawableBounds.set((int) f, (int) f2, (int) f3, (int) f4);
        scrimView3.mDrawable.setBounds(scrimView3.mDrawableBounds);
    }

    public final void setOrAdaptCurrentAnimation(ScrimView scrimView) {
        boolean z;
        if (scrimView != null) {
            float currentScrimAlpha = getCurrentScrimAlpha(scrimView);
            if (scrimView != this.mScrimBehind || !this.mQsBottomVisible) {
                z = false;
            } else {
                z = true;
            }
            if (!isAnimating(scrimView) || z) {
                updateScrimColor(scrimView, currentScrimAlpha, getCurrentScrimTint(scrimView));
                return;
            }
            ValueAnimator valueAnimator = (ValueAnimator) scrimView.getTag(TAG_KEY_ANIM);
            int i = TAG_END_ALPHA;
            float floatValue = ((Float) scrimView.getTag(i)).floatValue();
            int i2 = TAG_START_ALPHA;
            scrimView.setTag(i2, Float.valueOf((currentScrimAlpha - floatValue) + ((Float) scrimView.getTag(i2)).floatValue()));
            scrimView.setTag(i, Float.valueOf(currentScrimAlpha));
            valueAnimator.setCurrentPlayTime(valueAnimator.getCurrentPlayTime());
        }
    }

    public final void transitionTo(ScrimState scrimState, Callback callback) {
        boolean z;
        ScrimState scrimState2 = ScrimState.UNLOCKED;
        ScrimState scrimState3 = ScrimState.AOD;
        if (scrimState != this.mState) {
            if (DEBUG) {
                Log.d("ScrimController", "State changed to: " + scrimState);
            }
            if (scrimState != ScrimState.UNINITIALIZED) {
                ScrimState scrimState4 = this.mState;
                this.mState = scrimState;
                Trace.traceCounter(4096L, "scrim_state", scrimState.ordinal());
                Callback callback2 = this.mCallback;
                if (callback2 != null) {
                    callback2.onCancelled();
                }
                this.mCallback = callback;
                scrimState.prepare(scrimState4);
                boolean z2 = false;
                this.mScreenBlankingCallbackCalled = false;
                this.mAnimationDelay = 0L;
                this.mBlankScreen = scrimState.mBlankScreen;
                this.mAnimateChange = scrimState.mAnimateChange;
                this.mAnimationDuration = scrimState.mAnimationDuration;
                applyState();
                this.mScrimInFront.setFocusable(!scrimState.isLowPowerState());
                this.mScrimBehind.setFocusable(!scrimState.isLowPowerState());
                this.mNotificationsScrim.setFocusable(!scrimState.isLowPowerState());
                WMShell$7$$ExternalSyntheticLambda1 wMShell$7$$ExternalSyntheticLambda1 = this.mPendingFrameCallback;
                if (wMShell$7$$ExternalSyntheticLambda1 != null) {
                    this.mScrimBehind.removeCallbacks(wMShell$7$$ExternalSyntheticLambda1);
                    this.mPendingFrameCallback = null;
                }
                if (this.mHandler.hasCallbacks(this.mBlankingTransitionRunnable)) {
                    this.mHandler.removeCallbacks(this.mBlankingTransitionRunnable);
                    this.mBlankingTransitionRunnable = null;
                }
                if (scrimState != ScrimState.BRIGHTNESS_MIRROR) {
                    z = true;
                } else {
                    z = false;
                }
                this.mNeedsDrawableColorUpdate = z;
                if (this.mState.isLowPowerState() && !this.mWakeLockHeld) {
                    DelayedWakeLock delayedWakeLock = this.mWakeLock;
                    if (delayedWakeLock != null) {
                        this.mWakeLockHeld = true;
                        delayedWakeLock.acquire("ScrimController");
                    } else {
                        Log.w("ScrimController", "Cannot hold wake lock, it has not been set yet");
                    }
                }
                this.mWallpaperVisibilityTimedOut = false;
                if (this.mWallpaperSupportsAmbientMode && this.mState == scrimState3 && (this.mDozeParameters.getAlwaysOn() || this.mDockManager.isDocked())) {
                    z2 = true;
                }
                if (z2) {
                    DejankUtils.postAfterTraversal(new LockIconViewController$$ExternalSyntheticLambda1(this, 5));
                } else {
                    AlarmTimeout alarmTimeout = this.mTimeTicker;
                    Objects.requireNonNull(alarmTimeout);
                    DejankUtils.postAfterTraversal(new KeyguardStatusView$$ExternalSyntheticLambda0(alarmTimeout, 4));
                }
                KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
                Objects.requireNonNull(keyguardUpdateMonitor);
                if (keyguardUpdateMonitor.mNeedsSlowUnlockTransition && this.mState == scrimState2) {
                    this.mAnimationDelay = 100L;
                    scheduleUpdate();
                } else if (((scrimState4 == scrimState3 || scrimState4 == ScrimState.PULSING) && (!this.mDozeParameters.getAlwaysOn() || this.mState == scrimState2)) || (this.mState == scrimState3 && !this.mDozeParameters.getDisplayNeedsBlanking())) {
                    onPreDraw();
                } else {
                    scheduleUpdate();
                }
                ScrimView scrimView = this.mScrimBehind;
                Objects.requireNonNull(scrimView);
                dispatchBackScrimState(scrimView.mViewAlpha);
                return;
            }
            throw new IllegalArgumentException("Cannot change to UNINITIALIZED.");
        } else if (callback != null && this.mCallback != callback) {
            callback.onFinished();
        }
    }

    public final void updateScrimColor(View view, float f, int i) {
        String str;
        float max = Math.max(0.0f, Math.min(1.0f, f));
        if (view instanceof ScrimView) {
            ScrimView scrimView = (ScrimView) view;
            StringBuilder sb = new StringBuilder();
            String str2 = "notifications_scrim";
            if (scrimView == this.mScrimInFront) {
                str = "front_scrim";
            } else if (scrimView == this.mScrimBehind) {
                str = "behind_scrim";
            } else if (scrimView == this.mNotificationsScrim) {
                str = str2;
            } else {
                str = "unknown_scrim";
            }
            Trace.traceCounter(4096L, MotionController$$ExternalSyntheticOutline1.m(sb, str, "_alpha"), (int) (255.0f * max));
            StringBuilder sb2 = new StringBuilder();
            if (scrimView == this.mScrimInFront) {
                str2 = "front_scrim";
            } else if (scrimView == this.mScrimBehind) {
                str2 = "behind_scrim";
            } else if (scrimView != this.mNotificationsScrim) {
                str2 = "unknown_scrim";
            }
            Trace.traceCounter(4096L, MotionController$$ExternalSyntheticOutline1.m(sb2, str2, "_tint"), Color.alpha(i));
            Objects.requireNonNull(scrimView);
            scrimView.executeOnExecutor(new ScrimView$$ExternalSyntheticLambda4(scrimView, i));
            scrimView.setViewAlpha(max);
        } else {
            view.setAlpha(max);
        }
        dispatchScrimsVisible();
    }

    public final void updateScrims() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        ScrimState scrimState = ScrimState.PULSING;
        boolean z5 = true;
        if (this.mNeedsDrawableColorUpdate) {
            this.mNeedsDrawableColorUpdate = false;
            ScrimView scrimView = this.mScrimInFront;
            Objects.requireNonNull(scrimView);
            if (scrimView.mViewAlpha == 0.0f || this.mBlankScreen) {
                z2 = false;
            } else {
                z2 = true;
            }
            ScrimView scrimView2 = this.mScrimBehind;
            Objects.requireNonNull(scrimView2);
            if (scrimView2.mViewAlpha == 0.0f || this.mBlankScreen) {
                z3 = false;
            } else {
                z3 = true;
            }
            ScrimView scrimView3 = this.mNotificationsScrim;
            Objects.requireNonNull(scrimView3);
            if (scrimView3.mViewAlpha == 0.0f || this.mBlankScreen) {
                z4 = false;
            } else {
                z4 = true;
            }
            this.mScrimInFront.setColors(this.mColors, z2);
            this.mScrimBehind.setColors(this.mColors, z3);
            this.mNotificationsScrim.setColors(this.mColors, z4);
            ScrimView scrimView4 = this.mScrimBehind;
            Objects.requireNonNull(scrimView4);
            dispatchBackScrimState(scrimView4.mViewAlpha);
        }
        ScrimState scrimState2 = this.mState;
        ScrimState scrimState3 = ScrimState.AOD;
        if ((scrimState2 == scrimState3 || scrimState2 == scrimState) && this.mWallpaperVisibilityTimedOut) {
            z = true;
        } else {
            z = false;
        }
        if (!(scrimState2 == scrimState || scrimState2 == scrimState3) || !this.mKeyguardOccluded) {
            z5 = false;
        }
        if (z || z5) {
            this.mBehindAlpha = 1.0f;
        }
        setScrimAlpha(this.mScrimInFront, this.mInFrontAlpha);
        setScrimAlpha(this.mScrimBehind, this.mBehindAlpha);
        setScrimAlpha(this.mNotificationsScrim, this.mNotificationsAlpha);
        onFinished(this.mCallback, this.mState);
        dispatchScrimsVisible();
    }

    public final void updateThemeColors() {
        boolean z;
        ScrimView scrimView = this.mScrimBehind;
        if (scrimView != null) {
            int defaultColor = Utils.getColorAttr(scrimView.getContext(), 16844002).getDefaultColor();
            int defaultColor2 = Utils.getColorAttr(this.mScrimBehind.getContext(), 16843829).getDefaultColor();
            this.mColors.setMainColor(defaultColor);
            this.mColors.setSecondaryColor(defaultColor2);
            ColorExtractor.GradientColors gradientColors = this.mColors;
            if (ColorUtils.calculateContrast(gradientColors.getMainColor(), -1) > 4.5d) {
                z = true;
            } else {
                z = false;
            }
            gradientColors.setSupportsDarkText(z);
            this.mNeedsDrawableColorUpdate = true;
        }
    }

    public final void applyAndDispatchState() {
        applyState();
        if (!this.mUpdatePending) {
            setOrAdaptCurrentAnimation(this.mScrimBehind);
            setOrAdaptCurrentAnimation(this.mNotificationsScrim);
            setOrAdaptCurrentAnimation(this.mScrimInFront);
            ScrimView scrimView = this.mScrimBehind;
            Objects.requireNonNull(scrimView);
            dispatchBackScrimState(scrimView.mViewAlpha);
            if (this.mWallpaperVisibilityTimedOut) {
                this.mWallpaperVisibilityTimedOut = false;
                DejankUtils.postAfterTraversal(new Action$$ExternalSyntheticLambda0(this, 5));
            }
        }
    }

    @VisibleForTesting
    public void setRawPanelExpansionFraction(float f) {
        if (!Float.isNaN(f)) {
            this.mRawPanelExpansionFraction = f;
            calculateAndUpdatePanelExpansion();
            return;
        }
        throw new IllegalArgumentException("rawPanelExpansionFraction should not be NaN");
    }

    public final void setWakeLockScreenSensorActive(boolean z) {
        ScrimState[] values;
        for (ScrimState scrimState : ScrimState.values()) {
            Objects.requireNonNull(scrimState);
            scrimState.mWakeLockScreenSensorActive = z;
        }
        ScrimState scrimState2 = this.mState;
        if (scrimState2 == ScrimState.PULSING) {
            Objects.requireNonNull(scrimState2);
            float f = scrimState2.mBehindAlpha;
            if (this.mBehindAlpha != f) {
                this.mBehindAlpha = f;
                if (!Float.isNaN(f)) {
                    updateScrims();
                    return;
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Scrim opacity is NaN for state: ");
                m.append(this.mState);
                m.append(", back: ");
                m.append(this.mBehindAlpha);
                throw new IllegalStateException(m.toString());
            }
        }
    }

    @VisibleForTesting
    public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.mAnimatorListener = animatorListener;
    }

    @VisibleForTesting
    public boolean getClipQsScrim() {
        return this.mClipsQsScrim;
    }
}
