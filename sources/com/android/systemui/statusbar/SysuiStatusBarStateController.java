package com.android.systemui.statusbar;

import android.view.InsetsVisibilities;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.NotificationPanelView;
/* loaded from: classes.dex */
public interface SysuiStatusBarStateController extends StatusBarStateController {
    @Deprecated
    void addCallback(StatusBarStateController.StateListener stateListener, int i);

    boolean fromShadeLocked();

    int getCurrentOrUpcomingState();

    float getInterpolatedDozeAmount();

    boolean goingToFullShade();

    boolean isKeyguardRequested();

    boolean leaveOpenOnKeyguardHide();

    void setAndInstrumentDozeAmount(NotificationPanelView notificationPanelView, float f, boolean z);

    boolean setIsDozing(boolean z);

    void setKeyguardRequested(boolean z);

    void setLeaveOpenOnKeyguardHide(boolean z);

    boolean setPanelExpanded(boolean z);

    void setPulsing(boolean z);

    default boolean setState(int i) {
        return setState(i, false);
    }

    boolean setState(int i, boolean z);

    void setSystemBarAttributes(int i, int i2, InsetsVisibilities insetsVisibilities, String str);

    void setUpcomingState();

    /* loaded from: classes.dex */
    public static class RankedListener {
        public final StatusBarStateController.StateListener mListener;
        public final int mRank;

        public RankedListener(StatusBarStateController.StateListener stateListener, int i) {
            this.mListener = stateListener;
            this.mRank = i;
        }
    }
}
