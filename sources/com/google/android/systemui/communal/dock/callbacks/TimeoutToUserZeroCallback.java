package com.google.android.systemui.communal.dock.callbacks;

import android.util.Log;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda2;
import com.android.keyguard.KeyguardStatusView$$ExternalSyntheticLambda0;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.condition.Monitor;
import com.android.systemui.util.condition.Monitor$$ExternalSyntheticLambda1;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TimeoutToUserZeroCallback implements Monitor.Callback {
    public static final boolean DEBUG = Log.isLoggable("TimeoutToUserZero", 3);
    public Runnable mCancelTimerRunnable;
    public final DelayableExecutor mExecutor;
    public final Monitor mPreconditionsMonitor;
    public final Provider<Integer> mTimeoutDurationSettingProvider;
    public final UserSwitcherController mUserSwitcherController;
    public final UserTracker mUserTracker;
    public boolean mPreconditionsMet = false;
    public boolean mDocked = false;
    public final KeyguardStatusView$$ExternalSyntheticLambda0 mOnTimeout = new KeyguardStatusView$$ExternalSyntheticLambda0(this, 10);
    public final TimeoutToUserZeroCallback$$ExternalSyntheticLambda0 mPreconditionsCallback = new Monitor.Callback() { // from class: com.google.android.systemui.communal.dock.callbacks.TimeoutToUserZeroCallback$$ExternalSyntheticLambda0
        @Override // com.android.systemui.util.condition.Monitor.Callback
        public final void onConditionsChanged(boolean z) {
            TimeoutToUserZeroCallback timeoutToUserZeroCallback = TimeoutToUserZeroCallback.this;
            Objects.requireNonNull(timeoutToUserZeroCallback);
            timeoutToUserZeroCallback.mPreconditionsMet = z;
            timeoutToUserZeroCallback.onStateUpdated();
        }
    };

    @Override // com.android.systemui.util.condition.Monitor.Callback
    public final void onConditionsChanged(boolean z) {
        this.mDocked = z;
        if (z) {
            if (DEBUG) {
                Log.d("TimeoutToUserZero", "docked, start monitoring preconditions");
            }
            Monitor monitor = this.mPreconditionsMonitor;
            TimeoutToUserZeroCallback$$ExternalSyntheticLambda0 timeoutToUserZeroCallback$$ExternalSyntheticLambda0 = this.mPreconditionsCallback;
            Objects.requireNonNull(monitor);
            monitor.mExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda2(monitor, timeoutToUserZeroCallback$$ExternalSyntheticLambda0, 3));
        } else {
            if (DEBUG) {
                Log.d("TimeoutToUserZero", "undocked, stop monitoring preconditions");
            }
            Monitor monitor2 = this.mPreconditionsMonitor;
            TimeoutToUserZeroCallback$$ExternalSyntheticLambda0 timeoutToUserZeroCallback$$ExternalSyntheticLambda02 = this.mPreconditionsCallback;
            Objects.requireNonNull(monitor2);
            monitor2.mExecutor.execute(new Monitor$$ExternalSyntheticLambda1(monitor2, timeoutToUserZeroCallback$$ExternalSyntheticLambda02, 0));
        }
        onStateUpdated();
    }

    public final void onStateUpdated() {
        if (!this.mPreconditionsMet || this.mUserTracker.getUserId() == 0) {
            return;
        }
        if (this.mDocked) {
            if (this.mCancelTimerRunnable == null) {
                int intValue = this.mTimeoutDurationSettingProvider.mo144get().intValue();
                if (intValue > 0) {
                    if (DEBUG) {
                        Log.d("TimeoutToUserZero", "starting a timer of " + intValue + " milliseconds to switch to user 0");
                    }
                    this.mCancelTimerRunnable = this.mExecutor.executeDelayed(this.mOnTimeout, intValue);
                } else if (DEBUG) {
                    Log.w("TimeoutToUserZero", "timeout user setting is invalid");
                }
            }
        } else if (this.mCancelTimerRunnable != null) {
            if (DEBUG) {
                Log.d("TimeoutToUserZero", "canceling timer to switch to user 0");
            }
            this.mCancelTimerRunnable.run();
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.google.android.systemui.communal.dock.callbacks.TimeoutToUserZeroCallback$$ExternalSyntheticLambda0] */
    public TimeoutToUserZeroCallback(DelayableExecutor delayableExecutor, Monitor monitor, Provider<Integer> provider, UserSwitcherController userSwitcherController, UserTracker userTracker) {
        this.mExecutor = delayableExecutor;
        this.mPreconditionsMonitor = monitor;
        this.mTimeoutDurationSettingProvider = provider;
        this.mUserSwitcherController = userSwitcherController;
        this.mUserTracker = userTracker;
    }
}
