package com.android.systemui.globalactions;

import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.statusbar.phone.StatusBar;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda8 implements Consumer {
    public static final /* synthetic */ GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda8 INSTANCE = new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda8();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        StatusBar statusBar = (StatusBar) obj;
        int i = GlobalActionsDialogLite.ActionsDialogLite.$r8$clinit;
        Objects.requireNonNull(statusBar);
        statusBar.mCommandQueueCallbacks.animateExpandNotificationsPanel();
    }
}
