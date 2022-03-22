package com.android.wm.shell;

import android.app.ActivityManager;
import android.app.TaskInfo;
import android.content.Context;
import android.content.LocusId;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.os.Binder;
import android.os.IBinder;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceControl;
import android.window.ITaskOrganizerController;
import android.window.StartingWindowInfo;
import android.window.StartingWindowRemovalInfo;
import android.window.TaskAppearedInfo;
import android.window.TaskOrganizer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda21;
import com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda10;
import com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda0;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.compatui.CompatUIController;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.startingsurface.StartingSurfaceDrawer;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda0;
import com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda1;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public class ShellTaskOrganizer extends TaskOrganizer implements CompatUIController.CompatUICallback {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final CompatUIController mCompatUI;
    public final ArraySet<FocusListener> mFocusListeners;
    public ActivityManager.RunningTaskInfo mLastFocusedTaskInfo;
    public final ArrayMap<IBinder, TaskListener> mLaunchCookieToListener;
    public final Object mLock;
    public final ArraySet<LocusIdListener> mLocusIdListeners;
    public final Optional<RecentTasksController> mRecentTasks;
    public StartingWindowController mStartingWindow;
    public final SparseArray<TaskListener> mTaskListeners;
    public final SparseArray<TaskAppearedInfo> mTasks;
    public final SparseArray<LocusId> mVisibleTasksWithLocusId;

    /* loaded from: classes.dex */
    public interface FocusListener {
        void onFocusTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo);
    }

    /* loaded from: classes.dex */
    public interface LocusIdListener {
        void onVisibilityChanged(int i, LocusId locusId, boolean z);
    }

    /* loaded from: classes.dex */
    public interface TaskListener {
        default void dump(PrintWriter printWriter, String str) {
        }

        default void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        default void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        }

        default void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        default void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        default boolean supportCompatUI() {
            return true;
        }

        default void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
            throw new IllegalStateException("This task listener doesn't support child surface attachment.");
        }
    }

    /* loaded from: classes.dex */
    public @interface TaskListenerType {
    }

    public ShellTaskOrganizer() {
        throw null;
    }

    @VisibleForTesting
    public ShellTaskOrganizer(ITaskOrganizerController iTaskOrganizerController, ShellExecutor shellExecutor, Context context, CompatUIController compatUIController, Optional<RecentTasksController> optional) {
        super(iTaskOrganizerController, shellExecutor);
        this.mTaskListeners = new SparseArray<>();
        this.mTasks = new SparseArray<>();
        this.mLaunchCookieToListener = new ArrayMap<>();
        this.mVisibleTasksWithLocusId = new SparseArray<>();
        this.mLocusIdListeners = new ArraySet<>();
        this.mFocusListeners = new ArraySet<>();
        this.mLock = new Object();
        this.mCompatUI = compatUIController;
        this.mRecentTasks = optional;
        if (compatUIController != null) {
            compatUIController.mCallback = this;
        }
    }

    public static String taskListenerTypeToString(@TaskListenerType int i) {
        if (i == -5) {
            return "TASK_LISTENER_TYPE_FREEFORM";
        }
        if (i == -4) {
            return "TASK_LISTENER_TYPE_PIP";
        }
        if (i == -3) {
            return "TASK_LISTENER_TYPE_MULTI_WINDOW";
        }
        if (i == -2) {
            return "TASK_LISTENER_TYPE_FULLSCREEN";
        }
        if (i != -1) {
            return VendorAtomValue$$ExternalSyntheticOutline0.m("taskId#", i);
        }
        return "TASK_LISTENER_TYPE_UNDEFINED";
    }

    public final void notifyLocusIdChange(int i, LocusId locusId, boolean z) {
        for (int i2 = 0; i2 < this.mLocusIdListeners.size(); i2++) {
            this.mLocusIdListeners.valueAt(i2).onVisibilityChanged(i, locusId, z);
        }
    }

    public final void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        synchronized (this.mLock) {
            onTaskAppeared(new TaskAppearedInfo(runningTaskInfo, surfaceControl));
        }
    }

    public final void addListenerForType(TaskListener taskListener, @TaskListenerType int... iArr) {
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 1990759023, 0, null, String.valueOf(Arrays.toString(iArr)), String.valueOf(taskListener));
            }
            for (int i : iArr) {
                if (this.mTaskListeners.get(i) == null) {
                    this.mTaskListeners.put(i, taskListener);
                } else {
                    throw new IllegalArgumentException("Listener for listenerType=" + i + " already exists");
                }
            }
            for (int size = this.mTasks.size() - 1; size >= 0; size--) {
                TaskAppearedInfo valueAt = this.mTasks.valueAt(size);
                if (getTaskListener(valueAt.getTaskInfo(), false) == taskListener) {
                    taskListener.onTaskAppeared(valueAt.getTaskInfo(), valueAt.getLeash());
                }
            }
        }
    }

    public final void addStartingWindow(StartingWindowInfo startingWindowInfo, IBinder iBinder) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            Objects.requireNonNull(startingWindowController);
            startingWindowController.mSplashScreenExecutor.execute(new StartingWindowController$$ExternalSyntheticLambda0(startingWindowController, startingWindowInfo, iBinder, 0));
        }
    }

    public final void copySplashScreenView(int i) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            Objects.requireNonNull(startingWindowController);
            startingWindowController.mSplashScreenExecutor.execute(new StartingWindowController$$ExternalSyntheticLambda1(startingWindowController, i, 0));
        }
    }

    public final void createRootTask(int i, TaskListener taskListener) {
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1312360667, 5, null, Long.valueOf(0), Long.valueOf(i), String.valueOf(taskListener.toString()));
        }
        Binder binder = new Binder();
        synchronized (this.mLock) {
            this.mLaunchCookieToListener.put(binder, taskListener);
        }
        ShellTaskOrganizer.super.createRootTask(0, i, binder);
    }

    public final ActivityManager.RunningTaskInfo getRunningTaskInfo(int i) {
        ActivityManager.RunningTaskInfo runningTaskInfo;
        synchronized (this.mLock) {
            TaskAppearedInfo taskAppearedInfo = this.mTasks.get(i);
            if (taskAppearedInfo != null) {
                runningTaskInfo = taskAppearedInfo.getTaskInfo();
            } else {
                runningTaskInfo = null;
            }
        }
        return runningTaskInfo;
    }

    public final TaskListener getTaskListener(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z) {
        TaskListener taskListener;
        int i = runningTaskInfo.taskId;
        ArrayList arrayList = runningTaskInfo.launchCookies;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            IBinder iBinder = (IBinder) arrayList.get(size);
            TaskListener taskListener2 = this.mLaunchCookieToListener.get(iBinder);
            if (taskListener2 != null) {
                if (z) {
                    this.mLaunchCookieToListener.remove(iBinder);
                    this.mTaskListeners.put(i, taskListener2);
                }
                return taskListener2;
            }
        }
        TaskListener taskListener3 = this.mTaskListeners.get(i);
        if (taskListener3 != null) {
            return taskListener3;
        }
        if (!runningTaskInfo.hasParentTask() || (taskListener = this.mTaskListeners.get(runningTaskInfo.parentTaskId)) == null) {
            return this.mTaskListeners.get(taskInfoToTaskListenerType(runningTaskInfo));
        }
        return taskListener;
    }

    public final void notifyCompatUI(ActivityManager.RunningTaskInfo runningTaskInfo, TaskListener taskListener) {
        if (this.mCompatUI != null) {
            if (taskListener == null || !taskListener.supportCompatUI() || !runningTaskInfo.hasCompatUI() || !runningTaskInfo.isVisible) {
                this.mCompatUI.onCompatInfoChanged(runningTaskInfo, null);
            } else {
                this.mCompatUI.onCompatInfoChanged(runningTaskInfo, taskListener);
            }
        }
    }

    public final void notifyLocusVisibilityIfNeeded(TaskInfo taskInfo) {
        int i = taskInfo.taskId;
        LocusId locusId = this.mVisibleTasksWithLocusId.get(i);
        boolean equals = Objects.equals(locusId, taskInfo.mTopActivityLocusId);
        if (locusId == null) {
            LocusId locusId2 = taskInfo.mTopActivityLocusId;
            if (locusId2 != null && taskInfo.isVisible) {
                this.mVisibleTasksWithLocusId.put(i, locusId2);
                notifyLocusIdChange(i, taskInfo.mTopActivityLocusId, true);
            }
        } else if (equals && !taskInfo.isVisible) {
            this.mVisibleTasksWithLocusId.remove(i);
            notifyLocusIdChange(i, taskInfo.mTopActivityLocusId, false);
        } else if (equals) {
        } else {
            if (taskInfo.isVisible) {
                this.mVisibleTasksWithLocusId.put(i, taskInfo.mTopActivityLocusId);
                notifyLocusIdChange(i, locusId, false);
                notifyLocusIdChange(i, taskInfo.mTopActivityLocusId, true);
                return;
            }
            this.mVisibleTasksWithLocusId.remove(taskInfo.taskId);
            notifyLocusIdChange(i, locusId, false);
        }
    }

    public final void onAppSplashScreenViewRemoved(final int i) {
        final StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            Objects.requireNonNull(startingWindowController);
            startingWindowController.mSplashScreenExecutor.execute(new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    StartingWindowController startingWindowController2 = StartingWindowController.this;
                    int i2 = i;
                    Objects.requireNonNull(startingWindowController2);
                    StartingSurfaceDrawer startingSurfaceDrawer = startingWindowController2.mStartingSurfaceDrawer;
                    Objects.requireNonNull(startingSurfaceDrawer);
                    startingSurfaceDrawer.onAppSplashScreenViewRemoved(i2, true);
                }
            });
        }
    }

    public final void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 980952660, 1, null, Long.valueOf(runningTaskInfo.taskId));
            }
            TaskListener taskListener = getTaskListener(runningTaskInfo, false);
            if (taskListener != null) {
                taskListener.onBackPressedOnTaskRoot(runningTaskInfo);
            }
        }
    }

    public final void onImeDrawnOnTask(final int i) {
        final StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            Objects.requireNonNull(startingWindowController);
            startingWindowController.mSplashScreenExecutor.execute(new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    TaskSnapshotWindow taskSnapshotWindow;
                    StartingWindowController startingWindowController2 = StartingWindowController.this;
                    int i2 = i;
                    Objects.requireNonNull(startingWindowController2);
                    StartingSurfaceDrawer startingSurfaceDrawer = startingWindowController2.mStartingSurfaceDrawer;
                    Objects.requireNonNull(startingSurfaceDrawer);
                    StartingSurfaceDrawer.StartingWindowRecord startingWindowRecord = startingSurfaceDrawer.mStartingWindowRecords.get(i2);
                    if (startingWindowRecord != null && (taskSnapshotWindow = startingWindowRecord.mTaskSnapshotWindow) != null && taskSnapshotWindow.mHasImeSurface) {
                        StartingWindowRemovalInfo startingWindowRemovalInfo = startingSurfaceDrawer.mTmpRemovalInfo;
                        startingWindowRemovalInfo.taskId = i2;
                        startingSurfaceDrawer.removeWindowSynced(startingWindowRemovalInfo, true);
                    }
                }
            });
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x00a8 A[Catch: all -> 0x00c2, LOOP:0: B:43:0x00a8->B:45:0x00b0, LOOP_START, PHI: r2 
      PHI: (r2v1 int) = (r2v0 int), (r2v2 int) binds: [B:42:0x00a6, B:45:0x00b0] A[DONT_GENERATE, DONT_INLINE], TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0009, B:7:0x001d, B:11:0x004d, B:13:0x0052, B:17:0x005a, B:18:0x005d, B:20:0x0062, B:22:0x006c, B:23:0x006f, B:25:0x007d, B:26:0x0087, B:28:0x008b, B:30:0x0090, B:35:0x0098, B:37:0x009c, B:43:0x00a8, B:45:0x00b0, B:46:0x00be, B:47:0x00c0), top: B:52:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onTaskInfoChanged(android.app.ActivityManager.RunningTaskInfo r11) {
        /*
            r10 = this;
            java.lang.Object r0 = r10.mLock
            monitor-enter(r0)
            boolean r1 = com.android.wm.shell.protolog.ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled     // Catch: all -> 0x00c2
            r2 = 0
            r3 = 1
            if (r1 == 0) goto L_0x001d
            int r1 = r11.taskId     // Catch: all -> 0x00c2
            long r4 = (long) r1     // Catch: all -> 0x00c2
            com.android.wm.shell.protolog.ShellProtoLogGroup r1 = com.android.wm.shell.protolog.ShellProtoLogGroup.WM_SHELL_TASK_ORG     // Catch: all -> 0x00c2
            r6 = 157713005(0x966826d, float:2.7746569E-33)
            r7 = 0
            java.lang.Object[] r8 = new java.lang.Object[r3]     // Catch: all -> 0x00c2
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch: all -> 0x00c2
            r8[r2] = r4     // Catch: all -> 0x00c2
            com.android.wm.shell.protolog.ShellProtoLogImpl.v(r1, r6, r3, r7, r8)     // Catch: all -> 0x00c2
        L_0x001d:
            android.util.SparseArray<android.window.TaskAppearedInfo> r1 = r10.mTasks     // Catch: all -> 0x00c2
            int r4 = r11.taskId     // Catch: all -> 0x00c2
            java.lang.Object r1 = r1.get(r4)     // Catch: all -> 0x00c2
            android.window.TaskAppearedInfo r1 = (android.window.TaskAppearedInfo) r1     // Catch: all -> 0x00c2
            android.app.ActivityManager$RunningTaskInfo r4 = r1.getTaskInfo()     // Catch: all -> 0x00c2
            com.android.wm.shell.ShellTaskOrganizer$TaskListener r4 = r10.getTaskListener(r4, r2)     // Catch: all -> 0x00c2
            com.android.wm.shell.ShellTaskOrganizer$TaskListener r5 = r10.getTaskListener(r11, r2)     // Catch: all -> 0x00c2
            android.util.SparseArray<android.window.TaskAppearedInfo> r6 = r10.mTasks     // Catch: all -> 0x00c2
            int r7 = r11.taskId     // Catch: all -> 0x00c2
            android.window.TaskAppearedInfo r8 = new android.window.TaskAppearedInfo     // Catch: all -> 0x00c2
            android.view.SurfaceControl r9 = r1.getLeash()     // Catch: all -> 0x00c2
            r8.<init>(r11, r9)     // Catch: all -> 0x00c2
            r6.put(r7, r8)     // Catch: all -> 0x00c2
            android.view.SurfaceControl r6 = r1.getLeash()     // Catch: all -> 0x00c2
            if (r4 != r5) goto L_0x004b
            r4 = r2
            goto L_0x0056
        L_0x004b:
            if (r4 == 0) goto L_0x0050
            r4.onTaskVanished(r11)     // Catch: all -> 0x00c2
        L_0x0050:
            if (r5 == 0) goto L_0x0055
            r5.onTaskAppeared(r11, r6)     // Catch: all -> 0x00c2
        L_0x0055:
            r4 = r3
        L_0x0056:
            if (r4 != 0) goto L_0x005d
            if (r5 == 0) goto L_0x005d
            r5.onTaskInfoChanged(r11)     // Catch: all -> 0x00c2
        L_0x005d:
            r10.notifyLocusVisibilityIfNeeded(r11)     // Catch: all -> 0x00c2
            if (r4 != 0) goto L_0x006c
            android.app.ActivityManager$RunningTaskInfo r4 = r1.getTaskInfo()     // Catch: all -> 0x00c2
            boolean r4 = r11.equalsForCompatUi(r4)     // Catch: all -> 0x00c2
            if (r4 != 0) goto L_0x006f
        L_0x006c:
            r10.notifyCompatUI(r11, r5)     // Catch: all -> 0x00c2
        L_0x006f:
            android.app.ActivityManager$RunningTaskInfo r1 = r1.getTaskInfo()     // Catch: all -> 0x00c2
            int r1 = r1.getWindowingMode()     // Catch: all -> 0x00c2
            int r4 = r11.getWindowingMode()     // Catch: all -> 0x00c2
            if (r1 == r4) goto L_0x0087
            java.util.Optional<com.android.wm.shell.recents.RecentTasksController> r1 = r10.mRecentTasks     // Catch: all -> 0x00c2
            com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda0 r4 = new com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda0     // Catch: all -> 0x00c2
            r4.<init>(r11, r2)     // Catch: all -> 0x00c2
            r1.ifPresent(r4)     // Catch: all -> 0x00c2
        L_0x0087:
            boolean r1 = r11.isFocused     // Catch: all -> 0x00c2
            if (r1 != 0) goto L_0x0097
            int r1 = r11.topActivityType     // Catch: all -> 0x00c2
            r4 = 2
            if (r1 != r4) goto L_0x0095
            boolean r1 = r11.isVisible     // Catch: all -> 0x00c2
            if (r1 == 0) goto L_0x0095
            goto L_0x0097
        L_0x0095:
            r1 = r2
            goto L_0x0098
        L_0x0097:
            r1 = r3
        L_0x0098:
            android.app.ActivityManager$RunningTaskInfo r4 = r10.mLastFocusedTaskInfo     // Catch: all -> 0x00c2
            if (r4 == 0) goto L_0x00a2
            int r4 = r4.taskId     // Catch: all -> 0x00c2
            int r5 = r11.taskId     // Catch: all -> 0x00c2
            if (r4 == r5) goto L_0x00a5
        L_0x00a2:
            if (r1 == 0) goto L_0x00a5
            goto L_0x00a6
        L_0x00a5:
            r3 = r2
        L_0x00a6:
            if (r3 == 0) goto L_0x00c0
        L_0x00a8:
            android.util.ArraySet<com.android.wm.shell.ShellTaskOrganizer$FocusListener> r1 = r10.mFocusListeners     // Catch: all -> 0x00c2
            int r1 = r1.size()     // Catch: all -> 0x00c2
            if (r2 >= r1) goto L_0x00be
            android.util.ArraySet<com.android.wm.shell.ShellTaskOrganizer$FocusListener> r1 = r10.mFocusListeners     // Catch: all -> 0x00c2
            java.lang.Object r1 = r1.valueAt(r2)     // Catch: all -> 0x00c2
            com.android.wm.shell.ShellTaskOrganizer$FocusListener r1 = (com.android.wm.shell.ShellTaskOrganizer.FocusListener) r1     // Catch: all -> 0x00c2
            r1.onFocusTaskChanged(r11)     // Catch: all -> 0x00c2
            int r2 = r2 + 1
            goto L_0x00a8
        L_0x00be:
            r10.mLastFocusedTaskInfo = r11     // Catch: all -> 0x00c2
        L_0x00c0:
            monitor-exit(r0)     // Catch: all -> 0x00c2
            return
        L_0x00c2:
            r10 = move-exception
            monitor-exit(r0)     // Catch: all -> 0x00c2
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.ShellTaskOrganizer.onTaskInfoChanged(android.app.ActivityManager$RunningTaskInfo):void");
    }

    public final void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -880817403, 1, null, Long.valueOf(runningTaskInfo.taskId));
            }
            int i = runningTaskInfo.taskId;
            TaskListener taskListener = getTaskListener(this.mTasks.get(i).getTaskInfo(), false);
            this.mTasks.remove(i);
            if (taskListener != null) {
                taskListener.onTaskVanished(runningTaskInfo);
            }
            notifyLocusVisibilityIfNeeded(runningTaskInfo);
            notifyCompatUI(runningTaskInfo, null);
            this.mRecentTasks.ifPresent(new ShellTaskOrganizer$$ExternalSyntheticLambda1(runningTaskInfo, 0));
        }
    }

    public final List<TaskAppearedInfo> registerOrganizer() {
        List<TaskAppearedInfo> registerOrganizer;
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 580605218, 0, null, null);
            }
            registerOrganizer = ShellTaskOrganizer.super.registerOrganizer();
            for (int i = 0; i < registerOrganizer.size(); i++) {
                TaskAppearedInfo taskAppearedInfo = registerOrganizer.get(i);
                if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1683614271, 1, null, Long.valueOf(taskAppearedInfo.getTaskInfo().taskId), String.valueOf(taskAppearedInfo.getTaskInfo().baseIntent));
                }
                onTaskAppeared(taskAppearedInfo);
            }
        }
        return registerOrganizer;
    }

    public final void removeListener(TaskListener taskListener) {
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1340279385, 0, null, String.valueOf(taskListener));
            }
            if (this.mTaskListeners.indexOfValue(taskListener) == -1) {
                Log.w("ShellTaskOrganizer", "No registered listener found");
                return;
            }
            ArrayList arrayList = new ArrayList();
            for (int size = this.mTasks.size() - 1; size >= 0; size--) {
                TaskAppearedInfo valueAt = this.mTasks.valueAt(size);
                if (getTaskListener(valueAt.getTaskInfo(), false) == taskListener) {
                    arrayList.add(valueAt);
                }
            }
            for (int size2 = this.mTaskListeners.size() - 1; size2 >= 0; size2--) {
                if (this.mTaskListeners.valueAt(size2) == taskListener) {
                    this.mTaskListeners.removeAt(size2);
                }
            }
            for (int size3 = arrayList.size() - 1; size3 >= 0; size3--) {
                TaskAppearedInfo taskAppearedInfo = (TaskAppearedInfo) arrayList.get(size3);
                ActivityManager.RunningTaskInfo taskInfo = taskAppearedInfo.getTaskInfo();
                SurfaceControl leash = taskAppearedInfo.getLeash();
                TaskListener taskListener2 = getTaskListener(taskAppearedInfo.getTaskInfo(), false);
                if (!(taskListener2 == null || taskListener2 == null)) {
                    taskListener2.onTaskAppeared(taskInfo, leash);
                }
            }
        }
    }

    public final void removeStartingWindow(StartingWindowRemovalInfo startingWindowRemovalInfo) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            Objects.requireNonNull(startingWindowController);
            startingWindowController.mSplashScreenExecutor.execute(new BubblesManager$5$$ExternalSyntheticLambda0(startingWindowController, startingWindowRemovalInfo, 6));
            startingWindowController.mSplashScreenExecutor.executeDelayed(new StatusBar$$ExternalSyntheticLambda21(startingWindowController, startingWindowRemovalInfo, 2), 5000L);
        }
    }

    @TaskListenerType
    @VisibleForTesting
    public static int taskInfoToTaskListenerType(ActivityManager.RunningTaskInfo runningTaskInfo) {
        int windowingMode = runningTaskInfo.getWindowingMode();
        if (windowingMode == 1) {
            return -2;
        }
        if (windowingMode == 2) {
            return -4;
        }
        if (windowingMode == 5) {
            return -5;
        }
        if (windowingMode != 6) {
            return -1;
        }
        return -3;
    }

    public final void unregisterOrganizer() {
        ShellTaskOrganizer.super.unregisterOrganizer();
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            Objects.requireNonNull(startingWindowController);
            startingWindowController.mSplashScreenExecutor.execute(new VolumeDialogImpl$$ExternalSyntheticLambda10(startingWindowController, 10));
        }
    }

    public final void onTaskAppeared(TaskAppearedInfo taskAppearedInfo) {
        int i = taskAppearedInfo.getTaskInfo().taskId;
        this.mTasks.put(i, taskAppearedInfo);
        TaskListener taskListener = getTaskListener(taskAppearedInfo.getTaskInfo(), true);
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1325223370, 1, null, Long.valueOf(i), String.valueOf(taskListener));
        }
        if (taskListener != null) {
            taskListener.onTaskAppeared(taskAppearedInfo.getTaskInfo(), taskAppearedInfo.getLeash());
        }
        notifyLocusVisibilityIfNeeded(taskAppearedInfo.getTaskInfo());
        notifyCompatUI(taskAppearedInfo.getTaskInfo(), taskListener);
    }
}
