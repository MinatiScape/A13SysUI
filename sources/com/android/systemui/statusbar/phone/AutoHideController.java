package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Handler;
import android.view.IWindowManager;
import android.view.MotionEvent;
import com.android.systemui.statusbar.AutoHideUiElement;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda1;
/* loaded from: classes.dex */
public final class AutoHideController {
    public final WifiEntry$$ExternalSyntheticLambda1 mAutoHide = new WifiEntry$$ExternalSyntheticLambda1(this, 2);
    public boolean mAutoHideSuspended;
    public int mDisplayId;
    public final Handler mHandler;
    public AutoHideUiElement mNavigationBar;
    public AutoHideUiElement mStatusBar;
    public final IWindowManager mWindowManagerService;

    /* loaded from: classes.dex */
    public static class Factory {
        public final Handler mHandler;
        public final IWindowManager mIWindowManager;

        public Factory(Handler handler, IWindowManager iWindowManager) {
            this.mHandler = handler;
            this.mIWindowManager = iWindowManager;
        }
    }

    public final boolean isAnyTransientBarShown() {
        AutoHideUiElement autoHideUiElement = this.mStatusBar;
        if (autoHideUiElement != null && autoHideUiElement.isVisible()) {
            return true;
        }
        AutoHideUiElement autoHideUiElement2 = this.mNavigationBar;
        if (autoHideUiElement2 == null || !autoHideUiElement2.isVisible()) {
            return false;
        }
        return true;
    }

    public AutoHideController(Context context, Handler handler, IWindowManager iWindowManager) {
        this.mHandler = handler;
        this.mWindowManagerService = iWindowManager;
        this.mDisplayId = context.getDisplayId();
    }

    public final void checkUserAutoHide(MotionEvent motionEvent) {
        boolean z;
        if (isAnyTransientBarShown() && motionEvent.getAction() == 4 && motionEvent.getX() == 0.0f && motionEvent.getY() == 0.0f) {
            z = true;
        } else {
            z = false;
        }
        AutoHideUiElement autoHideUiElement = this.mStatusBar;
        if (autoHideUiElement != null) {
            z &= autoHideUiElement.shouldHideOnTouch();
        }
        AutoHideUiElement autoHideUiElement2 = this.mNavigationBar;
        if (autoHideUiElement2 != null) {
            z &= autoHideUiElement2.shouldHideOnTouch();
        }
        if (z) {
            this.mAutoHideSuspended = false;
            this.mHandler.removeCallbacks(this.mAutoHide);
            this.mHandler.postDelayed(this.mAutoHide, 350L);
        }
    }

    public final void touchAutoHide() {
        if (isAnyTransientBarShown()) {
            this.mAutoHideSuspended = false;
            this.mHandler.removeCallbacks(this.mAutoHide);
            this.mHandler.postDelayed(this.mAutoHide, 2250L);
            return;
        }
        this.mAutoHideSuspended = false;
        this.mHandler.removeCallbacks(this.mAutoHide);
    }
}
