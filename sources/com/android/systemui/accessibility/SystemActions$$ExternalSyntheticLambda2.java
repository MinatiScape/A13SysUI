package com.android.systemui.accessibility;

import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda17;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SystemActions$$ExternalSyntheticLambda2 implements Consumer {
    public static final /* synthetic */ SystemActions$$ExternalSyntheticLambda2 INSTANCE = new SystemActions$$ExternalSyntheticLambda2(0);
    public static final /* synthetic */ SystemActions$$ExternalSyntheticLambda2 INSTANCE$1 = new SystemActions$$ExternalSyntheticLambda2(1);
    public static final /* synthetic */ SystemActions$$ExternalSyntheticLambda2 INSTANCE$2 = new SystemActions$$ExternalSyntheticLambda2(2);
    public static final /* synthetic */ SystemActions$$ExternalSyntheticLambda2 INSTANCE$3 = new SystemActions$$ExternalSyntheticLambda2(3);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ SystemActions$$ExternalSyntheticLambda2(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                StatusBar statusBar = (StatusBar) obj;
                Objects.requireNonNull(statusBar);
                statusBar.mCommandQueueCallbacks.animateExpandNotificationsPanel();
                return;
            case 1:
                DreamOverlayTouchMonitor.TouchSessionImpl touchSessionImpl = (DreamOverlayTouchMonitor.TouchSessionImpl) obj;
                Objects.requireNonNull(touchSessionImpl);
                touchSessionImpl.mCallbacks.forEach(NavigationBar$$ExternalSyntheticLambda17.INSTANCE$1);
                return;
            case 2:
                ((StatusBar) obj).collapseShade();
                return;
            default:
                ((PhonePipMenuController.Listener) obj).onEnterSplit();
                return;
        }
    }
}
