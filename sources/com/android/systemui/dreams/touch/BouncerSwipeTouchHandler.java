package com.android.systemui.dreams.touch;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule$$ExternalSyntheticLambda0;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule$$ExternalSyntheticLambda1;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.StatusBarIconView$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda23;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.wm.shell.animation.FlingAnimationUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BouncerSwipeTouchHandler implements DreamTouchHandler {
    public final float mBouncerZoneScreenPercentage;
    public Boolean mCapture;
    public float mCurrentExpansion;
    public final FlingAnimationUtils mFlingAnimationUtils;
    public final FlingAnimationUtils mFlingAnimationUtilsClosing;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public final AnonymousClass1 mOnGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: com.android.systemui.dreams.touch.BouncerSwipeTouchHandler.1
        public boolean mBouncerPresent;
        public boolean mTrack;

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public final boolean onDown(MotionEvent motionEvent) {
            float f;
            boolean z;
            StatusBar statusBar = BouncerSwipeTouchHandler.this.mStatusBar;
            Objects.requireNonNull(statusBar);
            float f2 = statusBar.mDisplayMetrics.heightPixels;
            StatusBar statusBar2 = BouncerSwipeTouchHandler.this.mStatusBar;
            Objects.requireNonNull(statusBar2);
            this.mBouncerPresent = statusBar2.mBouncerShowing;
            float y = motionEvent.getY();
            if (this.mBouncerPresent) {
                f = 0.0f;
            } else {
                f = f2;
            }
            if (Math.abs(y - f) / f2 < BouncerSwipeTouchHandler.this.mBouncerZoneScreenPercentage) {
                z = true;
            } else {
                z = false;
            }
            this.mTrack = z;
            return false;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public final boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            StatusBar statusBar;
            boolean z;
            if (!this.mTrack) {
                return false;
            }
            BouncerSwipeTouchHandler bouncerSwipeTouchHandler = BouncerSwipeTouchHandler.this;
            if (bouncerSwipeTouchHandler.mCapture == null) {
                if (Math.abs(f2) > Math.abs(f)) {
                    z = true;
                } else {
                    z = false;
                }
                bouncerSwipeTouchHandler.mCapture = Boolean.valueOf(z);
                if (BouncerSwipeTouchHandler.this.mCapture.booleanValue()) {
                    BouncerSwipeTouchHandler.this.mStatusBarKeyguardViewManager.showBouncer(false);
                }
            }
            if (!BouncerSwipeTouchHandler.this.mCapture.booleanValue()) {
                return false;
            }
            float y = motionEvent.getY() - motionEvent2.getY();
            Objects.requireNonNull(BouncerSwipeTouchHandler.this.mStatusBar);
            float abs = Math.abs(y / statusBar.mDisplayMetrics.heightPixels);
            BouncerSwipeTouchHandler bouncerSwipeTouchHandler2 = BouncerSwipeTouchHandler.this;
            if (!this.mBouncerPresent) {
                abs = 1.0f - abs;
            }
            Objects.requireNonNull(bouncerSwipeTouchHandler2);
            bouncerSwipeTouchHandler2.mCurrentExpansion = abs;
            bouncerSwipeTouchHandler2.mStatusBarKeyguardViewManager.onPanelExpansionChanged(abs, false, true);
            return true;
        }
    };
    public final StatusBar mStatusBar;
    public StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public DreamTouchHandler.TouchSession mTouchSession;
    public ValueAnimatorCreator mValueAnimatorCreator;
    public VelocityTracker mVelocityTracker;
    public VelocityTrackerFactory mVelocityTrackerFactory;

    /* loaded from: classes.dex */
    public interface ValueAnimatorCreator {
        ValueAnimator create(float f, float f2);
    }

    /* loaded from: classes.dex */
    public interface VelocityTrackerFactory {
        VelocityTracker obtain();
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [com.android.systemui.dreams.touch.BouncerSwipeTouchHandler$1] */
    public BouncerSwipeTouchHandler(StatusBarKeyguardViewManager statusBarKeyguardViewManager, StatusBar statusBar, NotificationShadeWindowController notificationShadeWindowController, FlingAnimationUtils flingAnimationUtils, FlingAnimationUtils flingAnimationUtils2, float f) {
        BouncerSwipeModule$$ExternalSyntheticLambda0 bouncerSwipeModule$$ExternalSyntheticLambda0 = BouncerSwipeModule$$ExternalSyntheticLambda0.INSTANCE;
        BouncerSwipeModule$$ExternalSyntheticLambda1 bouncerSwipeModule$$ExternalSyntheticLambda1 = BouncerSwipeModule$$ExternalSyntheticLambda1.INSTANCE;
        this.mStatusBar = statusBar;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mBouncerZoneScreenPercentage = f;
        this.mFlingAnimationUtils = flingAnimationUtils;
        this.mFlingAnimationUtilsClosing = flingAnimationUtils2;
        this.mValueAnimatorCreator = bouncerSwipeModule$$ExternalSyntheticLambda0;
        this.mVelocityTrackerFactory = bouncerSwipeModule$$ExternalSyntheticLambda1;
    }

    @Override // com.android.systemui.dreams.touch.DreamTouchHandler
    public final void onSessionStart(DreamOverlayTouchMonitor.TouchSessionImpl touchSessionImpl) {
        VelocityTracker obtain = this.mVelocityTrackerFactory.obtain();
        this.mVelocityTracker = obtain;
        this.mTouchSession = touchSessionImpl;
        obtain.clear();
        this.mNotificationShadeWindowController.setForcePluginOpen(true, this);
        touchSessionImpl.mGestureListeners.add(this.mOnGestureListener);
        touchSessionImpl.mEventListeners.add(new InputChannelCompat$InputEventListener() { // from class: com.android.systemui.dreams.touch.BouncerSwipeTouchHandler$$ExternalSyntheticLambda0
            @Override // com.android.systemui.shared.system.InputChannelCompat$InputEventListener
            public final void onInputEvent(InputEvent inputEvent) {
                boolean z;
                ValueAnimator valueAnimator;
                BouncerSwipeTouchHandler bouncerSwipeTouchHandler = BouncerSwipeTouchHandler.this;
                Objects.requireNonNull(bouncerSwipeTouchHandler);
                if (!(inputEvent instanceof MotionEvent)) {
                    Log.e("BouncerSwipeTouchHandler", "non MotionEvent received:" + inputEvent);
                    return;
                }
                MotionEvent motionEvent = (MotionEvent) inputEvent;
                if (motionEvent.getAction() != 1) {
                    bouncerSwipeTouchHandler.mVelocityTracker.addMovement(motionEvent);
                    return;
                }
                Boolean bool = bouncerSwipeTouchHandler.mCapture;
                if (bool != null && bool.booleanValue()) {
                    bouncerSwipeTouchHandler.mVelocityTracker.computeCurrentVelocity(1000);
                    float yVelocity = bouncerSwipeTouchHandler.mVelocityTracker.getYVelocity();
                    float abs = Math.abs((float) Math.hypot(bouncerSwipeTouchHandler.mVelocityTracker.getXVelocity(), yVelocity));
                    FlingAnimationUtils flingAnimationUtils = bouncerSwipeTouchHandler.mFlingAnimationUtils;
                    Objects.requireNonNull(flingAnimationUtils);
                    int i = (abs > flingAnimationUtils.mMinVelocityPxPerSecond ? 1 : (abs == flingAnimationUtils.mMinVelocityPxPerSecond ? 0 : -1));
                    float f = 0.0f;
                    if (i >= 0 ? yVelocity <= 0.0f : bouncerSwipeTouchHandler.mCurrentExpansion <= 0.5f) {
                        z = false;
                    } else {
                        z = true;
                    }
                    if (z) {
                        f = 1.0f;
                    }
                    StatusBar statusBar = bouncerSwipeTouchHandler.mStatusBar;
                    Objects.requireNonNull(statusBar);
                    float f2 = statusBar.mDisplayMetrics.heightPixels;
                    float f3 = bouncerSwipeTouchHandler.mCurrentExpansion;
                    float f4 = f2 * f3;
                    float f5 = f2 * f;
                    ValueAnimator create = bouncerSwipeTouchHandler.mValueAnimatorCreator.create(f3, f);
                    create.addUpdateListener(new StatusBarIconView$$ExternalSyntheticLambda0(bouncerSwipeTouchHandler, 1));
                    int i2 = (f > 1.0f ? 1 : (f == 1.0f ? 0 : -1));
                    if (i2 == 0) {
                        bouncerSwipeTouchHandler.mFlingAnimationUtils.apply(create, f4, f5, yVelocity, f2);
                        valueAnimator = create;
                    } else {
                        valueAnimator = create;
                        bouncerSwipeTouchHandler.mFlingAnimationUtilsClosing.apply(create, bouncerSwipeTouchHandler.mCurrentExpansion, f4, f5, f2);
                    }
                    valueAnimator.start();
                    if (i2 == 0) {
                        bouncerSwipeTouchHandler.mStatusBarKeyguardViewManager.reset(false);
                    }
                    final DreamOverlayTouchMonitor.TouchSessionImpl touchSessionImpl2 = (DreamOverlayTouchMonitor.TouchSessionImpl) bouncerSwipeTouchHandler.mTouchSession;
                    Objects.requireNonNull(touchSessionImpl2);
                    final DreamOverlayTouchMonitor dreamOverlayTouchMonitor = touchSessionImpl2.mTouchMonitor;
                    Objects.requireNonNull(dreamOverlayTouchMonitor);
                    CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: com.android.systemui.dreams.touch.DreamOverlayTouchMonitor$$ExternalSyntheticLambda0
                        @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
                        public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                            DreamOverlayTouchMonitor dreamOverlayTouchMonitor2 = DreamOverlayTouchMonitor.this;
                            DreamOverlayTouchMonitor.TouchSessionImpl touchSessionImpl3 = touchSessionImpl2;
                            Objects.requireNonNull(dreamOverlayTouchMonitor2);
                            dreamOverlayTouchMonitor2.mExecutor.execute(new StatusBar$$ExternalSyntheticLambda23(dreamOverlayTouchMonitor2, touchSessionImpl3, completer, 1));
                            return "DreamOverlayTouchMonitor::pop";
                        }
                    });
                }
            }
        });
    }
}
