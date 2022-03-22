package com.android.wm.shell;

import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.statusbar.policy.ZenModeControllerImpl;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.splitscreen.StageCoordinator;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShellInitImpl$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda6 INSTANCE$1 = new ShellInitImpl$$ExternalSyntheticLambda6(1);
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda6 INSTANCE$2 = new ShellInitImpl$$ExternalSyntheticLambda6(2);
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda6 INSTANCE = new ShellInitImpl$$ExternalSyntheticLambda6(0);

    public /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda6(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                SplitScreenController splitScreenController = (SplitScreenController) obj;
                Objects.requireNonNull(splitScreenController);
                if (splitScreenController.mStageCoordinator == null) {
                    splitScreenController.mStageCoordinator = new StageCoordinator(splitScreenController.mContext, splitScreenController.mSyncQueue, splitScreenController.mRootTDAOrganizer, splitScreenController.mTaskOrganizer, splitScreenController.mDisplayController, splitScreenController.mDisplayImeController, splitScreenController.mDisplayInsetsController, splitScreenController.mTransitions, splitScreenController.mTransactionPool, splitScreenController.mLogger, splitScreenController.mIconProvider, splitScreenController.mRecentTasksOptional, splitScreenController.mUnfoldControllerProvider);
                    return;
                }
                return;
            case 1:
                ((StatusBar) obj).collapseShade();
                return;
            default:
                int i = ZenModeControllerImpl.$r8$clinit;
                Objects.requireNonNull((ZenModeController.Callback) obj);
                return;
        }
    }
}
