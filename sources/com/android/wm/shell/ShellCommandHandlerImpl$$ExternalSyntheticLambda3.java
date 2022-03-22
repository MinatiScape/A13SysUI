package com.android.wm.shell;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Window;
import androidx.activity.result.ActivityResultRegistry$3$$ExternalSyntheticOutline0;
import com.android.keyguard.KeyguardUnfoldTransition;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.tv.TvBottomSheetActivity;
import com.android.wm.shell.common.split.SplitLayout;
import com.android.wm.shell.splitscreen.MainStage;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.splitscreen.StageCoordinator;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShellCommandHandlerImpl$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ShellCommandHandlerImpl$$ExternalSyntheticLambda3(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        Drawable drawable;
        switch (this.$r8$classId) {
            case 0:
                PrintWriter printWriter = (PrintWriter) this.f$0;
                SplitScreenController splitScreenController = (SplitScreenController) obj;
                Objects.requireNonNull(splitScreenController);
                printWriter.println("SplitScreenController");
                StageCoordinator stageCoordinator = splitScreenController.mStageCoordinator;
                if (stageCoordinator != null) {
                    StringBuilder m = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("", "StageCoordinator", " mDisplayId=");
                    m.append(stageCoordinator.mDisplayId);
                    printWriter.println(m.toString());
                    printWriter.println("  mDividerVisible=" + stageCoordinator.mDividerVisible);
                    printWriter.println("  MainStage");
                    printWriter.println("    stagePosition=" + stageCoordinator.getMainStagePosition());
                    StringBuilder sb = new StringBuilder();
                    sb.append("    ");
                    sb.append("isActive=");
                    MainStage mainStage = stageCoordinator.mMainStage;
                    Objects.requireNonNull(mainStage);
                    sb.append(mainStage.mIsActive);
                    printWriter.println(sb.toString());
                    stageCoordinator.mMainStageListener.dump(printWriter, "    ");
                    printWriter.println("  SideStage");
                    printWriter.println("    stagePosition=" + stageCoordinator.mSideStagePosition);
                    stageCoordinator.mSideStageListener.dump(printWriter, "    ");
                    MainStage mainStage2 = stageCoordinator.mMainStage;
                    Objects.requireNonNull(mainStage2);
                    if (mainStage2.mIsActive) {
                        printWriter.println("  SplitLayout");
                        SplitLayout splitLayout = stageCoordinator.mSplitLayout;
                        Objects.requireNonNull(splitLayout);
                        printWriter.println("    bounds1=" + splitLayout.mBounds1.toShortString());
                        printWriter.println("    dividerBounds=" + splitLayout.mDividerBounds.toShortString());
                        printWriter.println("    bounds2=" + splitLayout.mBounds2.toShortString());
                        return;
                    }
                    return;
                }
                return;
            case 1:
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this.f$0;
                KeyguardUnfoldTransition keyguardUnfoldTransition = (KeyguardUnfoldTransition) obj;
                Rect rect = NotificationPanelViewController.M_DUMMY_DIRTY_RECT;
                Objects.requireNonNull(notificationPanelViewController);
                boolean z = notificationPanelViewController.mStatusViewCentered;
                Objects.requireNonNull(keyguardUnfoldTransition);
                keyguardUnfoldTransition.statusViewCentered = z;
                return;
            default:
                TvBottomSheetActivity tvBottomSheetActivity = (TvBottomSheetActivity) this.f$0;
                boolean booleanValue = ((Boolean) obj).booleanValue();
                int i = TvBottomSheetActivity.$r8$clinit;
                Objects.requireNonNull(tvBottomSheetActivity);
                Log.v("TvBottomSheetActivity", "blur enabled: " + booleanValue);
                Window window = tvBottomSheetActivity.getWindow();
                if (booleanValue) {
                    drawable = tvBottomSheetActivity.mBackgroundWithBlur;
                } else {
                    drawable = tvBottomSheetActivity.mBackgroundWithoutBlur;
                }
                window.setBackgroundDrawable(drawable);
                return;
        }
    }
}
