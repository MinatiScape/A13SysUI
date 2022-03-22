package com.android.systemui.statusbar.phone;

import android.os.Bundle;
import android.view.View;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeCommandReceiver;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarTransitions;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.util.ViewController;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class StatusBarDemoMode extends ViewController<View> implements DemoMode {
    public final Clock mClockView;
    public final DemoModeController mDemoModeController;
    public final int mDisplayId;
    public final NavigationBarController mNavigationBarController;
    public final View mOperatorNameView;
    public final PhoneStatusBarTransitions mPhoneStatusBarTransitions;

    @Override // com.android.systemui.demomode.DemoMode
    public final List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("bars");
        arrayList.add("clock");
        arrayList.add("operator");
        return arrayList;
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public final void dispatchDemoCommand(String str, Bundle bundle) {
        int i;
        if (str.equals("clock")) {
            Clock clock = this.mClockView;
            if (clock instanceof DemoModeCommandReceiver) {
                clock.dispatchDemoCommand(str, bundle);
            }
        }
        if (str.equals("operator")) {
            View view = this.mOperatorNameView;
            if (view instanceof DemoModeCommandReceiver) {
                ((DemoModeCommandReceiver) view).dispatchDemoCommand(str, bundle);
            }
        }
        if (str.equals("bars")) {
            String string = bundle.getString("mode");
            if ("opaque".equals(string)) {
                i = 4;
            } else if ("translucent".equals(string)) {
                i = 2;
            } else if ("semi-transparent".equals(string)) {
                i = 1;
            } else if ("transparent".equals(string)) {
                i = 0;
            } else if ("warning".equals(string)) {
                i = 5;
            } else {
                i = -1;
            }
            if (i != -1) {
                PhoneStatusBarTransitions phoneStatusBarTransitions = this.mPhoneStatusBarTransitions;
                Objects.requireNonNull(phoneStatusBarTransitions);
                int i2 = phoneStatusBarTransitions.mMode;
                if (i2 != i) {
                    phoneStatusBarTransitions.mMode = i;
                    phoneStatusBarTransitions.onTransition(i2, i, true);
                }
                NavigationBarController navigationBarController = this.mNavigationBarController;
                int i3 = this.mDisplayId;
                Objects.requireNonNull(navigationBarController);
                NavigationBar navigationBar = navigationBarController.mNavigationBars.get(i3);
                if (navigationBar != null) {
                    NavigationBarTransitions barTransitions = navigationBar.getBarTransitions();
                    Objects.requireNonNull(barTransitions);
                    int i4 = barTransitions.mMode;
                    if (i4 != i) {
                        barTransitions.mMode = i;
                        barTransitions.onTransition(i4, i, true);
                    }
                }
            }
        }
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public final void onDemoModeFinished() {
        Clock clock = this.mClockView;
        if (clock instanceof DemoModeCommandReceiver) {
            clock.onDemoModeFinished();
        }
        View view = this.mOperatorNameView;
        if (view instanceof DemoModeCommandReceiver) {
            ((DemoModeCommandReceiver) view).onDemoModeFinished();
        }
    }

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public final void onDemoModeStarted() {
        Clock clock = this.mClockView;
        if (clock instanceof DemoModeCommandReceiver) {
            clock.onDemoModeStarted();
        }
        View view = this.mOperatorNameView;
        if (view instanceof DemoModeCommandReceiver) {
            ((DemoModeCommandReceiver) view).onDemoModeStarted();
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mDemoModeController.addCallback((DemoMode) this);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mDemoModeController.removeCallback((DemoMode) this);
    }

    public StatusBarDemoMode(Clock clock, View view, DemoModeController demoModeController, PhoneStatusBarTransitions phoneStatusBarTransitions, NavigationBarController navigationBarController, int i) {
        super(clock);
        this.mClockView = clock;
        this.mOperatorNameView = view;
        this.mDemoModeController = demoModeController;
        this.mPhoneStatusBarTransitions = phoneStatusBarTransitions;
        this.mNavigationBarController = navigationBarController;
        this.mDisplayId = i;
    }
}
