package com.android.systemui.dagger;

import android.os.HandlerThread;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.ShellInit;
import com.android.wm.shell.TaskViewFactory;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.compatui.CompatUI;
import com.android.wm.shell.displayareahelper.DisplayAreaHelper;
import com.android.wm.shell.draganddrop.DragAndDrop;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.recents.RecentTasks;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.wm.shell.transition.ShellTransitions;
import java.util.Optional;
/* loaded from: classes.dex */
public interface WMComponent {

    /* loaded from: classes.dex */
    public interface Builder {
        WMComponent build();

        /* renamed from: setShellMainThread */
        Builder mo143setShellMainThread(HandlerThread handlerThread);
    }

    Optional<Object> getAppPairs();

    Optional<BackAnimation> getBackAnimation();

    Optional<Bubbles> getBubbles();

    Optional<CompatUI> getCompatUI();

    Optional<DisplayAreaHelper> getDisplayAreaHelper();

    Optional<DragAndDrop> getDragAndDrop();

    Optional<HideDisplayCutout> getHideDisplayCutout();

    Optional<LegacySplitScreen> getLegacySplitScreen();

    Optional<OneHanded> getOneHanded();

    Optional<Pip> getPip();

    Optional<RecentTasks> getRecentTasks();

    Optional<ShellCommandHandler> getShellCommandHandler();

    ShellInit getShellInit();

    Optional<SplitScreen> getSplitScreen();

    Optional<StartingSurface> getStartingSurface();

    Optional<TaskSurfaceHelper> getTaskSurfaceHelper();

    Optional<TaskViewFactory> getTaskViewFactory();

    ShellTransitions getTransitions();

    default void init() {
        getShellInit().init();
    }
}
