package com.android.wm.shell;

import android.os.CancellationSignal;
import com.android.wm.shell.recents.RecentTasksController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShellInitImpl$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda4 INSTANCE$1 = new ShellInitImpl$$ExternalSyntheticLambda4(1);
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda4 INSTANCE = new ShellInitImpl$$ExternalSyntheticLambda4(0);

    public /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda4(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                RecentTasksController recentTasksController = (RecentTasksController) obj;
                Objects.requireNonNull(recentTasksController);
                recentTasksController.mTaskStackListener.addListener(recentTasksController);
                return;
            default:
                ((CancellationSignal) obj).cancel();
                return;
        }
    }
}
