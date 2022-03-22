package com.android.systemui.assist;

import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.wm.shell.onehanded.OneHandedController;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PhoneStateMonitor$$ExternalSyntheticLambda1 implements Function {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ PhoneStateMonitor$$ExternalSyntheticLambda1 INSTANCE$1 = new PhoneStateMonitor$$ExternalSyntheticLambda1(1);
    public static final /* synthetic */ PhoneStateMonitor$$ExternalSyntheticLambda1 INSTANCE = new PhoneStateMonitor$$ExternalSyntheticLambda1(0);
    public static final /* synthetic */ PhoneStateMonitor$$ExternalSyntheticLambda1 INSTANCE$2 = new PhoneStateMonitor$$ExternalSyntheticLambda1(2);
    public static final /* synthetic */ PhoneStateMonitor$$ExternalSyntheticLambda1 INSTANCE$3 = new PhoneStateMonitor$$ExternalSyntheticLambda1(3);

    public /* synthetic */ PhoneStateMonitor$$ExternalSyntheticLambda1(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                StatusBar statusBar = (StatusBar) obj;
                Objects.requireNonNull(statusBar);
                return Boolean.valueOf(statusBar.mBouncerShowing);
            case 1:
                StatusBar statusBar2 = (StatusBar) obj;
                Objects.requireNonNull(statusBar2);
                return Boolean.valueOf(statusBar2.mPanelExpanded);
            case 2:
                StatusBar statusBar3 = (StatusBar) obj;
                Objects.requireNonNull(statusBar3);
                StatusBarWindowController statusBarWindowController = statusBar3.mStatusBarWindowController;
                Objects.requireNonNull(statusBarWindowController);
                return Integer.valueOf(statusBarWindowController.mBarHeight);
            default:
                OneHandedController oneHandedController = (OneHandedController) obj;
                Objects.requireNonNull(oneHandedController);
                return oneHandedController.mImpl;
        }
    }
}
