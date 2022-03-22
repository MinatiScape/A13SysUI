package com.android.wm.shell.onehanded;

import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.os.Looper;
import android.view.InputChannel;
import android.view.InputEventReceiver;
import android.view.InputMonitor;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda2;
import com.android.wm.shell.common.ShellExecutor;
/* loaded from: classes.dex */
public final class OneHandedTouchHandler implements OneHandedTransitionCallback {
    public InputEventReceiver mInputEventReceiver;
    public InputMonitor mInputMonitor;
    public boolean mIsEnabled;
    public boolean mIsInOutsideRegion;
    public boolean mIsOnStopTransitioning;
    public final Rect mLastUpdatedBounds = new Rect();
    public final ShellExecutor mMainExecutor;
    public final OneHandedTimeoutHandler mTimeoutHandler;
    public OneHandedTouchEventCallback mTouchEventCallback;

    /* loaded from: classes.dex */
    public class EventReceiver extends InputEventReceiver {
        public EventReceiver(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper);
        }

        /* JADX WARN: Code restructure failed: missing block: B:14:0x0030, code lost:
            if (r1 != 3) goto L_0x005f;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onInputEvent(android.view.InputEvent r7) {
            /*
                r6 = this;
                com.android.wm.shell.onehanded.OneHandedTouchHandler r0 = com.android.wm.shell.onehanded.OneHandedTouchHandler.this
                java.util.Objects.requireNonNull(r0)
                boolean r1 = r7 instanceof android.view.MotionEvent
                r2 = 1
                if (r1 == 0) goto L_0x005f
                r1 = r7
                android.view.MotionEvent r1 = (android.view.MotionEvent) r1
                r1.getX()
                float r3 = r1.getY()
                int r3 = java.lang.Math.round(r3)
                android.graphics.Rect r4 = r0.mLastUpdatedBounds
                int r4 = r4.top
                r5 = 0
                if (r3 >= r4) goto L_0x0021
                r3 = r2
                goto L_0x0022
            L_0x0021:
                r3 = r5
            L_0x0022:
                r0.mIsInOutsideRegion = r3
                int r1 = r1.getAction()
                if (r1 == 0) goto L_0x0056
                r3 = 2
                if (r1 == r2) goto L_0x0033
                if (r1 == r3) goto L_0x0056
                r4 = 3
                if (r1 == r4) goto L_0x0033
                goto L_0x005f
            L_0x0033:
                com.android.wm.shell.onehanded.OneHandedTimeoutHandler r1 = r0.mTimeoutHandler
                r1.resetTimer()
                boolean r1 = r0.mIsInOutsideRegion
                if (r1 == 0) goto L_0x0053
                boolean r1 = r0.mIsOnStopTransitioning
                if (r1 != 0) goto L_0x0053
                com.android.wm.shell.onehanded.OneHandedTouchHandler$OneHandedTouchEventCallback r1 = r0.mTouchEventCallback
                com.android.systemui.screenshot.ImageLoader$$ExternalSyntheticLambda0 r1 = (com.android.systemui.screenshot.ImageLoader$$ExternalSyntheticLambda0) r1
                java.util.Objects.requireNonNull(r1)
                java.lang.Object r1 = r1.f$0
                com.android.wm.shell.onehanded.OneHandedController r1 = (com.android.wm.shell.onehanded.OneHandedController) r1
                java.util.Objects.requireNonNull(r1)
                r1.stopOneHanded(r3)
                r0.mIsOnStopTransitioning = r2
            L_0x0053:
                r0.mIsInOutsideRegion = r5
                goto L_0x005f
            L_0x0056:
                boolean r1 = r0.mIsInOutsideRegion
                if (r1 != 0) goto L_0x005f
                com.android.wm.shell.onehanded.OneHandedTimeoutHandler r0 = r0.mTimeoutHandler
                r0.resetTimer()
            L_0x005f:
                r6.finishInputEvent(r7, r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.onehanded.OneHandedTouchHandler.EventReceiver.onInputEvent(android.view.InputEvent):void");
        }
    }

    /* loaded from: classes.dex */
    public interface OneHandedTouchEventCallback {
    }

    @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
    public final void onStartFinished(Rect rect) {
        this.mLastUpdatedBounds.set(rect);
    }

    @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
    public final void onStopFinished(Rect rect) {
        this.mLastUpdatedBounds.set(rect);
        this.mIsOnStopTransitioning = false;
    }

    public final void updateIsEnabled() {
        InputEventReceiver inputEventReceiver = this.mInputEventReceiver;
        if (inputEventReceiver != null) {
            inputEventReceiver.dispose();
            this.mInputEventReceiver = null;
        }
        InputMonitor inputMonitor = this.mInputMonitor;
        if (inputMonitor != null) {
            inputMonitor.dispose();
            this.mInputMonitor = null;
        }
        if (this.mIsEnabled) {
            this.mInputMonitor = InputManager.getInstance().monitorGestureInput("onehanded-touch", 0);
            try {
                this.mMainExecutor.executeBlocking$1(new LockIconViewController$$ExternalSyntheticLambda2(this, 8));
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to create input event receiver", e);
            }
        }
    }

    public OneHandedTouchHandler(OneHandedTimeoutHandler oneHandedTimeoutHandler, ShellExecutor shellExecutor) {
        this.mTimeoutHandler = oneHandedTimeoutHandler;
        this.mMainExecutor = shellExecutor;
        updateIsEnabled();
    }
}
