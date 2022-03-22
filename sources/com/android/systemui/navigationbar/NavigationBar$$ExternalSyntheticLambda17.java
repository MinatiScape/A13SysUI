package com.android.systemui.navigationbar;

import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.phone.StatusBar;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NavigationBar$$ExternalSyntheticLambda17 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ NavigationBar$$ExternalSyntheticLambda17 INSTANCE$1 = new NavigationBar$$ExternalSyntheticLambda17(1);
    public static final /* synthetic */ NavigationBar$$ExternalSyntheticLambda17 INSTANCE = new NavigationBar$$ExternalSyntheticLambda17(0);

    public /* synthetic */ NavigationBar$$ExternalSyntheticLambda17(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                StatusBar statusBar = (StatusBar) obj;
                Objects.requireNonNull(statusBar);
                statusBar.mUiBgExecutor.execute(new QSTileImpl$$ExternalSyntheticLambda1(statusBar, 3));
                return;
            default:
                ((DreamTouchHandler.TouchSession.Callback) obj).onRemoved();
                return;
        }
    }
}
