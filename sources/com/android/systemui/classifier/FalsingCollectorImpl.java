package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.FalsingCollectorImpl;
import com.android.systemui.dock.DockManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.sensors.ThresholdSensorEvent;
import com.android.systemui.util.time.SystemClock;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda5;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FalsingCollectorImpl implements FalsingCollector {
    public boolean mAvoidGesture;
    public final BatteryController mBatteryController;
    public final AnonymousClass3 mBatteryListener;
    public final AnonymousClass4 mDockEventListener;
    public final DockManager mDockManager;
    public final FalsingDataProvider mFalsingDataProvider;
    public final FalsingManager mFalsingManager;
    public final HistoryTracker mHistoryTracker;
    public final KeyguardStateController mKeyguardStateController;
    public final AnonymousClass2 mKeyguardUpdateCallback;
    public final DelayableExecutor mMainExecutor;
    public MotionEvent mPendingDownEvent;
    public final ProximitySensor mProximitySensor;
    public boolean mScreenOn;
    public final FalsingCollectorImpl$$ExternalSyntheticLambda0 mSensorEventListener = new ThresholdSensor.Listener() { // from class: com.android.systemui.classifier.FalsingCollectorImpl$$ExternalSyntheticLambda0
        @Override // com.android.systemui.util.sensors.ThresholdSensor.Listener
        public final void onThresholdCrossed(ThresholdSensorEvent thresholdSensorEvent) {
            FalsingCollectorImpl falsingCollectorImpl = FalsingCollectorImpl.this;
            Objects.requireNonNull(falsingCollectorImpl);
            falsingCollectorImpl.mFalsingManager.onProximityEvent(new FalsingCollectorImpl.ProximityEventImpl(thresholdSensorEvent));
        }
    };
    public boolean mSessionStarted;
    public boolean mShowingAod;
    public int mState;
    public final StatusBarStateController mStatusBarStateController;
    public final AnonymousClass1 mStatusBarStateListener;
    public final SystemClock mSystemClock;

    /* loaded from: classes.dex */
    public static class ProximityEventImpl implements FalsingManager.ProximityEvent {
        public ThresholdSensorEvent mThresholdSensorEvent;

        @Override // com.android.systemui.plugins.FalsingManager.ProximityEvent
        public final boolean getCovered() {
            ThresholdSensorEvent thresholdSensorEvent = this.mThresholdSensorEvent;
            Objects.requireNonNull(thresholdSensorEvent);
            return thresholdSensorEvent.mBelow;
        }

        @Override // com.android.systemui.plugins.FalsingManager.ProximityEvent
        public final long getTimestampNs() {
            ThresholdSensorEvent thresholdSensorEvent = this.mThresholdSensorEvent;
            Objects.requireNonNull(thresholdSensorEvent);
            return thresholdSensorEvent.mTimestampNs;
        }

        public ProximityEventImpl(ThresholdSensorEvent thresholdSensorEvent) {
            this.mThresholdSensorEvent = thresholdSensorEvent;
        }
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void avoidGesture() {
        this.mAvoidGesture = true;
        MotionEvent motionEvent = this.mPendingDownEvent;
        if (motionEvent != null) {
            motionEvent.recycle();
            this.mPendingDownEvent = null;
        }
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void isReportingEnabled() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onAffordanceSwipingAborted() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onAffordanceSwipingStarted() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onCameraHintStarted() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onCameraOn() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onExpansionFromPulseStopped() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onLeftAffordanceHintStarted() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onLeftAffordanceOn() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onNotificationDismissed() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onNotificationStartDismissing() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onNotificationStartDraggingDown() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onNotificationStopDismissing() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onNotificationStopDraggingDown() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onQsDown() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onScreenOff() {
        this.mScreenOn = false;
        updateSessionActive();
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onScreenTurningOn() {
        this.mScreenOn = true;
        updateSessionActive();
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onStartExpandingFromPulse() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onTrackingStarted() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onTrackingStopped() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onUnlockHintStarted() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void setNotificationExpanded() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void shouldEnforceBouncer() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onBouncerHidden() {
        if (this.mSessionStarted) {
            this.mProximitySensor.register(this.mSensorEventListener);
        }
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onBouncerShown() {
        this.mProximitySensor.unregister(this.mSensorEventListener);
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onMotionEventComplete() {
        DelayableExecutor delayableExecutor = this.mMainExecutor;
        FalsingDataProvider falsingDataProvider = this.mFalsingDataProvider;
        Objects.requireNonNull(falsingDataProvider);
        delayableExecutor.executeDelayed(new TaskView$$ExternalSyntheticLambda5(falsingDataProvider, 3), 100L);
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onSuccessfulUnlock() {
        this.mFalsingManager.onSuccessfulUnlock();
        sessionEnd();
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onTouchEvent(MotionEvent motionEvent) {
        if (!this.mKeyguardStateController.isShowing() || (this.mStatusBarStateController.isDozing() && !this.mStatusBarStateController.isPulsing())) {
            avoidGesture();
        } else if (motionEvent.getActionMasked() == 0) {
            this.mPendingDownEvent = MotionEvent.obtain(motionEvent);
            this.mAvoidGesture = false;
        } else if (!this.mAvoidGesture) {
            MotionEvent motionEvent2 = this.mPendingDownEvent;
            if (motionEvent2 != null) {
                this.mFalsingDataProvider.onMotionEvent(motionEvent2);
                this.mPendingDownEvent.recycle();
                this.mPendingDownEvent = null;
            }
            this.mFalsingDataProvider.onMotionEvent(motionEvent);
        }
    }

    public final void sessionEnd() {
        if (this.mSessionStarted) {
            this.mSessionStarted = false;
            this.mProximitySensor.unregister(this.mSensorEventListener);
            FalsingDataProvider falsingDataProvider = this.mFalsingDataProvider;
            Objects.requireNonNull(falsingDataProvider);
            Iterator<MotionEvent> it = falsingDataProvider.mRecentMotionEvents.iterator();
            while (it.hasNext()) {
                it.next().recycle();
            }
            falsingDataProvider.mRecentMotionEvents.clear();
            falsingDataProvider.mDirty = true;
            falsingDataProvider.mSessionListeners.forEach(FalsingDataProvider$$ExternalSyntheticLambda1.INSTANCE);
        }
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void setQsExpanded(boolean z) {
        if (z) {
            this.mProximitySensor.unregister(this.mSensorEventListener);
        } else if (this.mSessionStarted) {
            this.mProximitySensor.register(this.mSensorEventListener);
        }
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void setShowingAod(boolean z) {
        this.mShowingAod = z;
        updateSessionActive();
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void updateFalseConfidence(FalsingClassifier.Result result) {
        this.mHistoryTracker.addResults(Collections.singleton(result), this.mSystemClock.uptimeMillis());
    }

    public final void updateSessionActive() {
        boolean z;
        boolean z2;
        boolean z3 = this.mScreenOn;
        if (!z3 || this.mState != 1 || this.mShowingAod) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            sessionEnd();
        } else if (!this.mSessionStarted) {
            if (!z3 || this.mState != 1 || this.mShowingAod) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (z2) {
                this.mSessionStarted = true;
                FalsingDataProvider falsingDataProvider = this.mFalsingDataProvider;
                Objects.requireNonNull(falsingDataProvider);
                falsingDataProvider.mJustUnlockedWithFace = false;
                this.mProximitySensor.register(this.mSensorEventListener);
                FalsingDataProvider falsingDataProvider2 = this.mFalsingDataProvider;
                Objects.requireNonNull(falsingDataProvider2);
                falsingDataProvider2.mSessionListeners.forEach(FalsingDataProvider$$ExternalSyntheticLambda0.INSTANCE);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.classifier.FalsingCollectorImpl$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.classifier.FalsingCollectorImpl$1, com.android.systemui.plugins.statusbar.StatusBarStateController$StateListener] */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.keyguard.KeyguardUpdateMonitorCallback, com.android.systemui.classifier.FalsingCollectorImpl$2] */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.android.systemui.classifier.FalsingCollectorImpl$3, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r3v0, types: [com.android.systemui.dock.DockManager$DockEventListener, com.android.systemui.classifier.FalsingCollectorImpl$4] */
    /* JADX WARN: Unknown variable types count: 4 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public FalsingCollectorImpl(com.android.systemui.classifier.FalsingDataProvider r5, com.android.systemui.plugins.FalsingManager r6, com.android.keyguard.KeyguardUpdateMonitor r7, com.android.systemui.classifier.HistoryTracker r8, com.android.systemui.util.sensors.ProximitySensor r9, com.android.systemui.plugins.statusbar.StatusBarStateController r10, com.android.systemui.statusbar.policy.KeyguardStateController r11, com.android.systemui.statusbar.policy.BatteryController r12, com.android.systemui.dock.DockManager r13, com.android.systemui.util.concurrency.DelayableExecutor r14, com.android.systemui.util.time.SystemClock r15) {
        /*
            r4 = this;
            r4.<init>()
            com.android.systemui.classifier.FalsingCollectorImpl$$ExternalSyntheticLambda0 r0 = new com.android.systemui.classifier.FalsingCollectorImpl$$ExternalSyntheticLambda0
            r0.<init>()
            r4.mSensorEventListener = r0
            com.android.systemui.classifier.FalsingCollectorImpl$1 r0 = new com.android.systemui.classifier.FalsingCollectorImpl$1
            r0.<init>()
            r4.mStatusBarStateListener = r0
            com.android.systemui.classifier.FalsingCollectorImpl$2 r1 = new com.android.systemui.classifier.FalsingCollectorImpl$2
            r1.<init>()
            r4.mKeyguardUpdateCallback = r1
            com.android.systemui.classifier.FalsingCollectorImpl$3 r2 = new com.android.systemui.classifier.FalsingCollectorImpl$3
            r2.<init>()
            r4.mBatteryListener = r2
            com.android.systemui.classifier.FalsingCollectorImpl$4 r3 = new com.android.systemui.classifier.FalsingCollectorImpl$4
            r3.<init>()
            r4.mDockEventListener = r3
            r4.mFalsingDataProvider = r5
            r4.mFalsingManager = r6
            r4.mHistoryTracker = r8
            r4.mProximitySensor = r9
            r4.mStatusBarStateController = r10
            r4.mKeyguardStateController = r11
            r4.mBatteryController = r12
            r4.mDockManager = r13
            r4.mMainExecutor = r14
            r4.mSystemClock = r15
            java.lang.String r5 = "FalsingManager"
            r9.setTag(r5)
            r9.setDelay()
            r10.addCallback(r0)
            int r5 = r10.getState()
            r4.mState = r5
            r7.registerCallback(r1)
            r12.addCallback(r2)
            r13.addListener(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.classifier.FalsingCollectorImpl.<init>(com.android.systemui.classifier.FalsingDataProvider, com.android.systemui.plugins.FalsingManager, com.android.keyguard.KeyguardUpdateMonitor, com.android.systemui.classifier.HistoryTracker, com.android.systemui.util.sensors.ProximitySensor, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.statusbar.policy.KeyguardStateController, com.android.systemui.statusbar.policy.BatteryController, com.android.systemui.dock.DockManager, com.android.systemui.util.concurrency.DelayableExecutor, com.android.systemui.util.time.SystemClock):void");
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public final void onScreenOnFromTouch() {
        onScreenTurningOn();
    }
}
