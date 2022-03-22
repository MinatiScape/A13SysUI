package com.android.systemui.statusbar.window;

import android.content.Context;
import android.graphics.Rect;
import android.os.Binder;
import android.os.RemoteException;
import android.view.IWindowManager;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda34;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import java.util.Optional;
/* loaded from: classes.dex */
public final class StatusBarWindowController {
    public int mBarHeight;
    public final StatusBarContentInsetsProvider mContentInsetsProvider;
    public final Context mContext;
    public final IWindowManager mIWindowManager;
    public final ViewGroup mLaunchAnimationContainer;
    public WindowManager.LayoutParams mLp;
    public final StatusBarWindowView mStatusBarWindowView;
    public final WindowManager mWindowManager;
    public final State mCurrentState = new State();
    public final WindowManager.LayoutParams mLpChanged = new WindowManager.LayoutParams();

    /* loaded from: classes.dex */
    public static class State {
        public boolean mForceStatusBarVisible;
        public boolean mIsLaunchAnimationRunning;
        public boolean mOngoingProcessRequiresStatusBarVisible;
    }

    public final void calculateStatusBarLocationsForAllRotations() {
        try {
            this.mIWindowManager.updateStaticPrivacyIndicatorBounds(this.mContext.getDisplayId(), new Rect[]{this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(0), this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(1), this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(2), this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(3)});
        } catch (RemoteException unused) {
        }
    }

    public final void apply(State state) {
        int i;
        int i2;
        if (state.mForceStatusBarVisible || state.mIsLaunchAnimationRunning || state.mOngoingProcessRequiresStatusBarVisible) {
            this.mLpChanged.privateFlags |= 4096;
        } else {
            this.mLpChanged.privateFlags &= -4097;
        }
        WindowManager.LayoutParams layoutParams = this.mLpChanged;
        if (state.mIsLaunchAnimationRunning) {
            i = -1;
        } else {
            i = this.mBarHeight;
        }
        layoutParams.height = i;
        for (int i3 = 0; i3 <= 3; i3++) {
            WindowManager.LayoutParams layoutParams2 = this.mLpChanged.paramsForRotation[i3];
            if (state.mIsLaunchAnimationRunning) {
                i2 = -1;
            } else {
                i2 = SystemBarUtils.getStatusBarHeightForRotation(this.mContext, i3);
            }
            layoutParams2.height = i2;
        }
        WindowManager.LayoutParams layoutParams3 = this.mLp;
        if (!(layoutParams3 == null || layoutParams3.copyFrom(this.mLpChanged) == 0)) {
            this.mWindowManager.updateViewLayout(this.mStatusBarWindowView, this.mLp);
        }
    }

    public final WindowManager.LayoutParams getBarLayoutParamsForRotation(int i) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, SystemBarUtils.getStatusBarHeightForRotation(this.mContext, i), 2000, -2139095032, -3);
        layoutParams.privateFlags |= 16777216;
        layoutParams.token = new Binder();
        layoutParams.gravity = 48;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTitle("StatusBar");
        layoutParams.packageName = this.mContext.getPackageName();
        layoutParams.layoutInDisplayCutoutMode = 3;
        return layoutParams;
    }

    public StatusBarWindowController(Context context, StatusBarWindowView statusBarWindowView, WindowManager windowManager, IWindowManager iWindowManager, StatusBarContentInsetsProvider statusBarContentInsetsProvider, Optional optional) {
        this.mBarHeight = -1;
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mIWindowManager = iWindowManager;
        this.mContentInsetsProvider = statusBarContentInsetsProvider;
        this.mStatusBarWindowView = statusBarWindowView;
        this.mLaunchAnimationContainer = (ViewGroup) statusBarWindowView.findViewById(2131428927);
        if (this.mBarHeight < 0) {
            this.mBarHeight = SystemBarUtils.getStatusBarHeight(context);
        }
        optional.ifPresent(new StatusBar$$ExternalSyntheticLambda34(this, 3));
    }
}
