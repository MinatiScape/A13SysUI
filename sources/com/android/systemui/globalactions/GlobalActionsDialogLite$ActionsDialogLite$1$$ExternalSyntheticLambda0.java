package com.android.systemui.globalactions;

import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class GlobalActionsDialogLite$ActionsDialogLite$1$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ GlobalActionsDialogLite$ActionsDialogLite$1$$ExternalSyntheticLambda0 INSTANCE = new GlobalActionsDialogLite$ActionsDialogLite$1$$ExternalSyntheticLambda0();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        StatusBar statusBar = (StatusBar) obj;
        Objects.requireNonNull(statusBar);
        StatusBarWindowController statusBarWindowController = statusBar.mStatusBarWindowController;
        Objects.requireNonNull(statusBarWindowController);
        return Integer.valueOf(statusBarWindowController.mBarHeight);
    }
}
