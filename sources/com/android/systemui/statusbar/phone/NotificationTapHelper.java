package com.android.systemui.statusbar.phone;

import android.view.MotionEvent;
import com.android.internal.annotations.VisibleForTesting;
import com.android.launcher3.icons.ShadowGenerator$Builder$$ExternalSyntheticLambda0;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wm.shell.bubbles.BubbleExpandedView$1$$ExternalSyntheticLambda0;
/* loaded from: classes.dex */
public final class NotificationTapHelper {
    public final ActivationListener mActivationListener;
    public final DoubleTapListener mDoubleTapListener;
    public final DelayableExecutor mExecutor;
    public final FalsingManager mFalsingManager;
    public final SlideBackListener mSlideBackListener;
    public Runnable mTimeoutCancel;
    public boolean mTrackTouch;

    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface ActivationListener {
    }

    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface DoubleTapListener {
        boolean onDoubleTap();
    }

    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface SlideBackListener {
        boolean onSlideBack();
    }

    /* loaded from: classes.dex */
    public static class Factory {
        public final DelayableExecutor mDelayableExecutor;
        public final FalsingManager mFalsingManager;

        public Factory(FalsingManager falsingManager, DelayableExecutor delayableExecutor) {
            this.mFalsingManager = falsingManager;
            this.mDelayableExecutor = delayableExecutor;
        }
    }

    public final void makeInactive() {
        ((ShadowGenerator$Builder$$ExternalSyntheticLambda0) this.mActivationListener).onActiveChanged(false);
        Runnable runnable = this.mTimeoutCancel;
        if (runnable != null) {
            runnable.run();
            this.mTimeoutCancel = null;
        }
    }

    @VisibleForTesting
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        boolean z = false;
        if (actionMasked == 0) {
            if (motionEvent.getY() <= Integer.MAX_VALUE) {
                z = true;
            }
            this.mTrackTouch = z;
        } else if (actionMasked == 1) {
            this.mTrackTouch = false;
            if (!this.mFalsingManager.isFalseTap(0)) {
                makeInactive();
                return this.mDoubleTapListener.onDoubleTap();
            } else if (this.mFalsingManager.isSimpleTap()) {
                SlideBackListener slideBackListener = this.mSlideBackListener;
                if (slideBackListener != null && slideBackListener.onSlideBack()) {
                    return true;
                }
                if (this.mTimeoutCancel == null) {
                    this.mTimeoutCancel = this.mExecutor.executeDelayed(new BubbleExpandedView$1$$ExternalSyntheticLambda0(this, 6), 1200L);
                    ((ShadowGenerator$Builder$$ExternalSyntheticLambda0) this.mActivationListener).onActiveChanged(true);
                    return true;
                }
                makeInactive();
                if (!this.mFalsingManager.isFalseDoubleTap()) {
                    return this.mDoubleTapListener.onDoubleTap();
                }
            } else {
                makeInactive();
            }
        } else if (actionMasked != 2) {
            if (actionMasked == 3) {
                makeInactive();
                this.mTrackTouch = false;
            }
        } else if (this.mTrackTouch && !this.mFalsingManager.isSimpleTap()) {
            makeInactive();
            this.mTrackTouch = false;
        }
        return this.mTrackTouch;
    }
}
