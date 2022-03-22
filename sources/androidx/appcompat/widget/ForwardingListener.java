package androidx.appcompat.widget;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import androidx.appcompat.view.menu.ShowableListMenu;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class ForwardingListener implements View.OnTouchListener, View.OnAttachStateChangeListener {
    public int mActivePointerId;
    public DisallowIntercept mDisallowIntercept;
    public boolean mForwarding;
    public final int mLongPressTimeout;
    public final float mScaledTouchSlop;
    public final View mSrc;
    public final int mTapTimeout;
    public final int[] mTmpLocation = new int[2];
    public TriggerLongPress mTriggerLongPress;

    /* loaded from: classes.dex */
    public class DisallowIntercept implements Runnable {
        public DisallowIntercept() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            ViewParent parent = ForwardingListener.this.mSrc.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    /* loaded from: classes.dex */
    public class TriggerLongPress implements Runnable {
        public TriggerLongPress() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            ForwardingListener forwardingListener = ForwardingListener.this;
            Objects.requireNonNull(forwardingListener);
            forwardingListener.clearCallbacks();
            View view = forwardingListener.mSrc;
            if (view.isEnabled() && !view.isLongClickable() && forwardingListener.onForwardingStarted()) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                long uptimeMillis = SystemClock.uptimeMillis();
                MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                view.onTouchEvent(obtain);
                obtain.recycle();
                forwardingListener.mForwarding = true;
            }
        }
    }

    public abstract ShowableListMenu getPopup();

    public abstract boolean onForwardingStarted();

    @Override // android.view.View.OnAttachStateChangeListener
    public final void onViewAttachedToWindow(View view) {
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public final void onViewDetachedFromWindow(View view) {
        this.mForwarding = false;
        this.mActivePointerId = -1;
        DisallowIntercept disallowIntercept = this.mDisallowIntercept;
        if (disallowIntercept != null) {
            this.mSrc.removeCallbacks(disallowIntercept);
        }
    }

    public final void clearCallbacks() {
        TriggerLongPress triggerLongPress = this.mTriggerLongPress;
        if (triggerLongPress != null) {
            this.mSrc.removeCallbacks(triggerLongPress);
        }
        DisallowIntercept disallowIntercept = this.mDisallowIntercept;
        if (disallowIntercept != null) {
            this.mSrc.removeCallbacks(disallowIntercept);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0084, code lost:
        if (r4 != 3) goto L_0x0102;
     */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0110  */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onTouch(android.view.View r12, android.view.MotionEvent r13) {
        /*
            Method dump skipped, instructions count: 302
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ForwardingListener.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    public ForwardingListener(View view) {
        this.mSrc = view;
        view.setLongClickable(true);
        view.addOnAttachStateChangeListener(this);
        this.mScaledTouchSlop = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
        int tapTimeout = ViewConfiguration.getTapTimeout();
        this.mTapTimeout = tapTimeout;
        this.mLongPressTimeout = (ViewConfiguration.getLongPressTimeout() + tapTimeout) / 2;
    }

    public boolean onForwardingStopped() {
        ShowableListMenu popup = getPopup();
        if (popup == null || !popup.isShowing()) {
            return true;
        }
        popup.dismiss();
        return true;
    }
}
