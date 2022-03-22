package com.android.wm.shell;

import android.app.ActivityManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.window.WindowContainerTransaction;
import com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda0;
import com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda3;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda4;
import com.android.wm.shell.ShellCommandHandlerImpl;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.util.GroupedRecentTaskInfo;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class ShellCommandHandlerImpl {
    public final Optional<AppPairsController> mAppPairsOptional;
    public final Optional<HideDisplayCutoutController> mHideDisplayCutout;
    public final HandlerImpl mImpl = new HandlerImpl();
    public final Optional<LegacySplitScreenController> mLegacySplitScreenOptional;
    public final ShellExecutor mMainExecutor;
    public final Optional<OneHandedController> mOneHandedOptional;
    public final Optional<Pip> mPipOptional;
    public final Optional<RecentTasksController> mRecentTasks;
    public final ShellTaskOrganizer mShellTaskOrganizer;
    public final Optional<SplitScreenController> mSplitScreenOptional;

    /* loaded from: classes.dex */
    public class HandlerImpl implements ShellCommandHandler {
        @Override // com.android.wm.shell.ShellCommandHandler
        public final boolean handleCommand(final String[] strArr, final PrintWriter printWriter) {
            try {
                final boolean[] zArr = new boolean[1];
                ShellCommandHandlerImpl.this.mMainExecutor.executeBlocking$1(new Runnable() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda1
                    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
                    @Override // java.lang.Runnable
                    public final void run() {
                        boolean z;
                        final int i;
                        ShellCommandHandlerImpl.HandlerImpl handlerImpl = ShellCommandHandlerImpl.HandlerImpl.this;
                        boolean[] zArr2 = zArr;
                        String[] strArr2 = strArr;
                        PrintWriter printWriter2 = printWriter;
                        Objects.requireNonNull(handlerImpl);
                        ShellCommandHandlerImpl shellCommandHandlerImpl = ShellCommandHandlerImpl.this;
                        Objects.requireNonNull(shellCommandHandlerImpl);
                        if (strArr2.length >= 2) {
                            z = true;
                            String str = strArr2[1];
                            Objects.requireNonNull(str);
                            char c = 65535;
                            switch (str.hashCode()) {
                                case -968877417:
                                    if (str.equals("setSideStageVisibility")) {
                                        c = 0;
                                        break;
                                    }
                                    break;
                                case -840336141:
                                    if (str.equals("unpair")) {
                                        c = 1;
                                        break;
                                    }
                                    break;
                                case -91197669:
                                    if (str.equals("moveToSideStage")) {
                                        c = 2;
                                        break;
                                    }
                                    break;
                                case 3198785:
                                    if (str.equals("help")) {
                                        c = 3;
                                        break;
                                    }
                                    break;
                                case 3433178:
                                    if (str.equals("pair")) {
                                        c = 4;
                                        break;
                                    }
                                    break;
                                case 295561529:
                                    if (str.equals("removeFromSideStage")) {
                                        c = 5;
                                        break;
                                    }
                                    break;
                                case 1522429422:
                                    if (str.equals("setSideStagePosition")) {
                                        c = 6;
                                        break;
                                    }
                                    break;
                            }
                            switch (c) {
                                case 0:
                                    if (strArr2.length >= 3) {
                                        shellCommandHandlerImpl.mSplitScreenOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda2(new Boolean(strArr2[2]), 0));
                                        break;
                                    } else {
                                        printWriter2.println("Error: side stage visibility should be provided as arguments");
                                        break;
                                    }
                                case 1:
                                    if (strArr2.length >= 3) {
                                        final int intValue = new Integer(strArr2[2]).intValue();
                                        shellCommandHandlerImpl.mAppPairsOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda5
                                            @Override // java.util.function.Consumer
                                            public final void accept(Object obj) {
                                                int i2 = intValue;
                                                AppPairsController appPairsController = (AppPairsController) obj;
                                                Objects.requireNonNull(appPairsController);
                                                appPairsController.unpair(i2, true);
                                            }
                                        });
                                        break;
                                    } else {
                                        printWriter2.println("Error: task id should be provided as an argument");
                                        break;
                                    }
                                case 2:
                                    if (strArr2.length >= 3) {
                                        final int intValue2 = new Integer(strArr2[2]).intValue();
                                        if (strArr2.length > 3) {
                                            i = new Integer(strArr2[3]).intValue();
                                        } else {
                                            i = 1;
                                        }
                                        shellCommandHandlerImpl.mSplitScreenOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda8
                                            @Override // java.util.function.Consumer
                                            public final void accept(Object obj) {
                                                int i2 = intValue2;
                                                int i3 = i;
                                                SplitScreenController splitScreenController = (SplitScreenController) obj;
                                                Objects.requireNonNull(splitScreenController);
                                                splitScreenController.moveToStage(i2, 1, i3, new WindowContainerTransaction());
                                            }
                                        });
                                        break;
                                    } else {
                                        printWriter2.println("Error: task id should be provided as arguments");
                                        break;
                                    }
                                case 3:
                                    printWriter2.println("Window Manager Shell commands:");
                                    printWriter2.println("  help");
                                    printWriter2.println("      Print this help text.");
                                    printWriter2.println("  <no arguments provided>");
                                    printWriter2.println("    Dump Window Manager Shell internal state");
                                    printWriter2.println("  pair <taskId1> <taskId2>");
                                    printWriter2.println("  unpair <taskId>");
                                    printWriter2.println("    Pairs/unpairs tasks with given ids.");
                                    printWriter2.println("  moveToSideStage <taskId> <SideStagePosition>");
                                    printWriter2.println("    Move a task with given id in split-screen mode.");
                                    printWriter2.println("  removeFromSideStage <taskId>");
                                    printWriter2.println("    Remove a task with given id in split-screen mode.");
                                    printWriter2.println("  setSideStageOutline <true/false>");
                                    printWriter2.println("    Enable/Disable outline on the side-stage.");
                                    printWriter2.println("  setSideStagePosition <SideStagePosition>");
                                    printWriter2.println("    Sets the position of the side-stage.");
                                    printWriter2.println("  setSideStageVisibility <true/false>");
                                    printWriter2.println("    Show/hide side-stage.");
                                    break;
                                case 4:
                                    if (strArr2.length >= 4) {
                                        final int intValue3 = new Integer(strArr2[2]).intValue();
                                        final int intValue4 = new Integer(strArr2[3]).intValue();
                                        shellCommandHandlerImpl.mAppPairsOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda7
                                            @Override // java.util.function.Consumer
                                            public final void accept(Object obj) {
                                                int i2 = intValue3;
                                                int i3 = intValue4;
                                                AppPairsController appPairsController = (AppPairsController) obj;
                                                Objects.requireNonNull(appPairsController);
                                                ActivityManager.RunningTaskInfo runningTaskInfo = appPairsController.mTaskOrganizer.getRunningTaskInfo(i2);
                                                ActivityManager.RunningTaskInfo runningTaskInfo2 = appPairsController.mTaskOrganizer.getRunningTaskInfo(i3);
                                                if (runningTaskInfo != null && runningTaskInfo2 != null) {
                                                    appPairsController.pairInner(runningTaskInfo, runningTaskInfo2);
                                                }
                                            }
                                        });
                                        break;
                                    } else {
                                        printWriter2.println("Error: two task ids should be provided as arguments");
                                        break;
                                    }
                                case 5:
                                    if (strArr2.length >= 3) {
                                        shellCommandHandlerImpl.mSplitScreenOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda0(new Integer(strArr2[2]).intValue(), 0));
                                        break;
                                    } else {
                                        printWriter2.println("Error: task id should be provided as arguments");
                                        break;
                                    }
                                case FalsingManager.VERSION /* 6 */:
                                    if (strArr2.length >= 3) {
                                        final int intValue5 = new Integer(strArr2[2]).intValue();
                                        shellCommandHandlerImpl.mSplitScreenOptional.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda6
                                            @Override // java.util.function.Consumer
                                            public final void accept(Object obj) {
                                                int i2 = intValue5;
                                                SplitScreenController splitScreenController = (SplitScreenController) obj;
                                                Objects.requireNonNull(splitScreenController);
                                                splitScreenController.mStageCoordinator.setSideStagePosition(i2, null);
                                            }
                                        });
                                        break;
                                    } else {
                                        printWriter2.println("Error: side stage position should be provided as arguments");
                                        break;
                                    }
                            }
                            zArr2[0] = z;
                        }
                        z = false;
                        zArr2[0] = z;
                    }
                });
                return zArr[0];
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to handle Shell command in 2s", e);
            }
        }

        public HandlerImpl() {
        }

        @Override // com.android.wm.shell.ShellCommandHandler
        public final void dump(final PrintWriter printWriter) {
            try {
                ShellCommandHandlerImpl.this.mMainExecutor.executeBlocking$1(new Runnable() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ShellCommandHandlerImpl.HandlerImpl handlerImpl = ShellCommandHandlerImpl.HandlerImpl.this;
                        final PrintWriter printWriter2 = printWriter;
                        Objects.requireNonNull(handlerImpl);
                        ShellCommandHandlerImpl shellCommandHandlerImpl = ShellCommandHandlerImpl.this;
                        Objects.requireNonNull(shellCommandHandlerImpl);
                        ShellTaskOrganizer shellTaskOrganizer = shellCommandHandlerImpl.mShellTaskOrganizer;
                        Objects.requireNonNull(shellTaskOrganizer);
                        synchronized (shellTaskOrganizer.mLock) {
                            printWriter2.println("ShellTaskOrganizer");
                            printWriter2.println("  " + shellTaskOrganizer.mTaskListeners.size() + " Listeners");
                            int size = shellTaskOrganizer.mTaskListeners.size();
                            while (true) {
                                size--;
                                if (size < 0) {
                                    break;
                                }
                                int keyAt = shellTaskOrganizer.mTaskListeners.keyAt(size);
                                printWriter2.println("  #" + size + " " + ShellTaskOrganizer.taskListenerTypeToString(keyAt));
                                shellTaskOrganizer.mTaskListeners.valueAt(size).dump(printWriter2, "    ");
                            }
                            printWriter2.println();
                            printWriter2.println("  " + shellTaskOrganizer.mTasks.size() + " Tasks");
                            int size2 = shellTaskOrganizer.mTasks.size();
                            while (true) {
                                size2--;
                                if (size2 < 0) {
                                    break;
                                }
                                printWriter2.println("  #" + size2 + " task=" + shellTaskOrganizer.mTasks.keyAt(size2) + " listener=" + shellTaskOrganizer.getTaskListener(shellTaskOrganizer.mTasks.valueAt(size2).getTaskInfo(), false));
                            }
                            printWriter2.println();
                            printWriter2.println("  " + shellTaskOrganizer.mLaunchCookieToListener.size() + " Launch Cookies");
                            int size3 = shellTaskOrganizer.mLaunchCookieToListener.size();
                            while (true) {
                                size3--;
                                if (size3 >= 0) {
                                    printWriter2.println("  #" + size3 + " cookie=" + shellTaskOrganizer.mLaunchCookieToListener.keyAt(size3) + " listener=" + shellTaskOrganizer.mLaunchCookieToListener.valueAt(size3));
                                }
                            }
                        }
                        printWriter2.println();
                        printWriter2.println();
                        shellCommandHandlerImpl.mPipOptional.ifPresent(new CreateUserActivity$$ExternalSyntheticLambda4(printWriter2, 3));
                        shellCommandHandlerImpl.mLegacySplitScreenOptional.ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda0(printWriter2, 2));
                        shellCommandHandlerImpl.mOneHandedOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda1(printWriter2, 0));
                        shellCommandHandlerImpl.mHideDisplayCutout.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda4(printWriter2, 0));
                        printWriter2.println();
                        printWriter2.println();
                        shellCommandHandlerImpl.mAppPairsOptional.ifPresent(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda3(printWriter2, 1));
                        printWriter2.println();
                        printWriter2.println();
                        shellCommandHandlerImpl.mSplitScreenOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda3(printWriter2, 0));
                        printWriter2.println();
                        printWriter2.println();
                        shellCommandHandlerImpl.mRecentTasks.ifPresent(new Consumer() { // from class: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda9
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                PrintWriter printWriter3 = printWriter2;
                                RecentTasksController recentTasksController = (RecentTasksController) obj;
                                Objects.requireNonNull(recentTasksController);
                                printWriter3.println("RecentTasksController");
                                ArrayList<GroupedRecentTaskInfo> recentTasks = recentTasksController.getRecentTasks(Integer.MAX_VALUE, 2, ActivityManager.getCurrentUser());
                                for (int i = 0; i < recentTasks.size(); i++) {
                                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("  ");
                                    m.append(recentTasks.get(i));
                                    printWriter3.println(m.toString());
                                }
                            }
                        });
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to dump the Shell in 2s", e);
            }
        }
    }

    public ShellCommandHandlerImpl(ShellTaskOrganizer shellTaskOrganizer, Optional<LegacySplitScreenController> optional, Optional<SplitScreenController> optional2, Optional<Pip> optional3, Optional<OneHandedController> optional4, Optional<HideDisplayCutoutController> optional5, Optional<AppPairsController> optional6, Optional<RecentTasksController> optional7, ShellExecutor shellExecutor) {
        this.mShellTaskOrganizer = shellTaskOrganizer;
        this.mRecentTasks = optional7;
        this.mLegacySplitScreenOptional = optional;
        this.mSplitScreenOptional = optional2;
        this.mPipOptional = optional3;
        this.mOneHandedOptional = optional4;
        this.mHideDisplayCutout = optional5;
        this.mAppPairsOptional = optional6;
        this.mMainExecutor = shellExecutor;
    }
}
