package com.android.wm.shell.legacysplitscreen;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceControl;
import android.window.TaskOrganizer;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.policy.DividerSnapAlgorithm;
import com.android.internal.policy.DockedDividerUtils;
import com.android.internal.util.ArrayUtils;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class WindowManagerProxy {
    public final SyncTransactionQueue mSyncTransactionQueue;
    public final TaskOrganizer mTaskOrganizer;
    public static final int[] HOME_AND_RECENTS = {2, 3};
    public static final int[] CONTROLLED_ACTIVITY_TYPES = {1, 2, 3, 0};
    public static final int[] CONTROLLED_WINDOWING_MODES = {1, 4, 0};
    @GuardedBy({"mDockedRect"})
    public final Rect mDockedRect = new Rect();
    @GuardedBy({"mDockedRect"})
    public final Rect mTouchableRegion = new Rect();

    public final boolean getHomeAndRecentsTasks(ArrayList arrayList, WindowContainerToken windowContainerToken) {
        List list;
        if (windowContainerToken == null) {
            list = this.mTaskOrganizer.getRootTasks(0, HOME_AND_RECENTS);
        } else {
            list = this.mTaskOrganizer.getChildTasks(windowContainerToken, HOME_AND_RECENTS);
        }
        int size = list.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) list.get(i);
            arrayList.add(runningTaskInfo);
            if (runningTaskInfo.topActivityType == 2) {
                z = runningTaskInfo.isResizeable;
            }
        }
        return z;
    }

    public static /* synthetic */ boolean $r8$lambda$47w38wy_iigsOkn3kFhRTExSL3k(LegacySplitScreenTaskListener legacySplitScreenTaskListener, ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (runningTaskInfo.token.equals(legacySplitScreenTaskListener.mSecondary.token) || runningTaskInfo.token.equals(legacySplitScreenTaskListener.mPrimary.token)) {
            return true;
        }
        return false;
    }

    public final boolean applyHomeTasksMinimized(LegacySplitDisplayLayout legacySplitDisplayLayout, WindowContainerToken windowContainerToken, WindowContainerTransaction windowContainerTransaction) {
        Rect rect;
        int i;
        int i2;
        ArrayList arrayList = new ArrayList();
        boolean homeAndRecentsTasks = getHomeAndRecentsTasks(arrayList, windowContainerToken);
        if (homeAndRecentsTasks) {
            Objects.requireNonNull(legacySplitDisplayLayout);
            DividerSnapAlgorithm.SnapTarget middleTarget = legacySplitDisplayLayout.getMinimizedSnapAlgorithm(true).getMiddleTarget();
            rect = new Rect();
            int i3 = middleTarget.position;
            int invertDockSide = DockedDividerUtils.invertDockSide(legacySplitDisplayLayout.getPrimarySplitSide());
            DisplayLayout displayLayout = legacySplitDisplayLayout.mDisplayLayout;
            Objects.requireNonNull(displayLayout);
            int i4 = displayLayout.mWidth;
            DisplayLayout displayLayout2 = legacySplitDisplayLayout.mDisplayLayout;
            Objects.requireNonNull(displayLayout2);
            DockedDividerUtils.calculateBoundsForPosition(i3, invertDockSide, rect, i4, displayLayout2.mHeight, legacySplitDisplayLayout.mDividerSize);
        } else {
            boolean z = false;
            rect = new Rect(0, 0, 0, 0);
            int size = arrayList.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                } else if (((ActivityManager.RunningTaskInfo) arrayList.get(size)).topActivityType == 2) {
                    int i5 = ((ActivityManager.RunningTaskInfo) arrayList.get(size)).configuration.orientation;
                    boolean isLandscape = legacySplitDisplayLayout.mDisplayLayout.isLandscape();
                    if (i5 == 2 || (i5 == 0 && isLandscape)) {
                        z = true;
                    }
                    if (z == isLandscape) {
                        DisplayLayout displayLayout3 = legacySplitDisplayLayout.mDisplayLayout;
                        Objects.requireNonNull(displayLayout3);
                        i = displayLayout3.mWidth;
                    } else {
                        DisplayLayout displayLayout4 = legacySplitDisplayLayout.mDisplayLayout;
                        Objects.requireNonNull(displayLayout4);
                        i = displayLayout4.mHeight;
                    }
                    rect.right = i;
                    if (z == isLandscape) {
                        DisplayLayout displayLayout5 = legacySplitDisplayLayout.mDisplayLayout;
                        Objects.requireNonNull(displayLayout5);
                        i2 = displayLayout5.mHeight;
                    } else {
                        DisplayLayout displayLayout6 = legacySplitDisplayLayout.mDisplayLayout;
                        Objects.requireNonNull(displayLayout6);
                        i2 = displayLayout6.mWidth;
                    }
                    rect.bottom = i2;
                } else {
                    size--;
                }
            }
        }
        for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
            if (!homeAndRecentsTasks) {
                if (((ActivityManager.RunningTaskInfo) arrayList.get(size2)).topActivityType != 3) {
                    windowContainerTransaction.setWindowingMode(((ActivityManager.RunningTaskInfo) arrayList.get(size2)).token, 1);
                }
            }
            windowContainerTransaction.setBounds(((ActivityManager.RunningTaskInfo) arrayList.get(size2)).token, rect);
        }
        legacySplitDisplayLayout.mTiles.mHomeBounds.set(rect);
        return homeAndRecentsTasks;
    }

    public final boolean buildEnterSplit(WindowContainerTransaction windowContainerTransaction, LegacySplitScreenTaskListener legacySplitScreenTaskListener, LegacySplitDisplayLayout legacySplitDisplayLayout) {
        List rootTasks = this.mTaskOrganizer.getRootTasks(0, (int[]) null);
        if (rootTasks.isEmpty()) {
            return false;
        }
        ActivityManager.RunningTaskInfo runningTaskInfo = null;
        for (int size = rootTasks.size() - 1; size >= 0; size--) {
            ActivityManager.RunningTaskInfo runningTaskInfo2 = (ActivityManager.RunningTaskInfo) rootTasks.get(size);
            if (runningTaskInfo2.supportsMultiWindow || runningTaskInfo2.topActivityType == 2) {
                int windowingMode = runningTaskInfo2.getWindowingMode();
                if (ArrayUtils.contains(CONTROLLED_WINDOWING_MODES, windowingMode) && ArrayUtils.contains(CONTROLLED_ACTIVITY_TYPES, runningTaskInfo2.getActivityType()) && windowingMode != 4) {
                    if (isHomeOrRecentTask(runningTaskInfo2)) {
                        runningTaskInfo = runningTaskInfo2;
                    } else {
                        runningTaskInfo = null;
                    }
                    windowContainerTransaction.reparent(runningTaskInfo2.token, legacySplitScreenTaskListener.mSecondary.token, true);
                }
            }
        }
        windowContainerTransaction.reorder(legacySplitScreenTaskListener.mSecondary.token, true);
        boolean applyHomeTasksMinimized = applyHomeTasksMinimized(legacySplitDisplayLayout, null, windowContainerTransaction);
        if (runningTaskInfo != null && !Transitions.ENABLE_SHELL_TRANSITIONS) {
            windowContainerTransaction.setBoundsChangeTransaction(runningTaskInfo.token, legacySplitScreenTaskListener.mHomeBounds);
        }
        return applyHomeTasksMinimized;
    }

    public final boolean queueSyncTransactionIfWaiting(WindowContainerTransaction windowContainerTransaction) {
        SyncTransactionQueue syncTransactionQueue = this.mSyncTransactionQueue;
        Objects.requireNonNull(syncTransactionQueue);
        if (!windowContainerTransaction.isEmpty()) {
            synchronized (syncTransactionQueue.mQueue) {
                if (!syncTransactionQueue.mQueue.isEmpty()) {
                    SyncTransactionQueue.SyncCallback syncCallback = new SyncTransactionQueue.SyncCallback(windowContainerTransaction);
                    syncTransactionQueue.mQueue.add(syncCallback);
                    if (syncTransactionQueue.mQueue.size() == 1) {
                        syncCallback.send();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public WindowManagerProxy(SyncTransactionQueue syncTransactionQueue, TaskOrganizer taskOrganizer) {
        new Rect();
        this.mSyncTransactionQueue = syncTransactionQueue;
        this.mTaskOrganizer = taskOrganizer;
    }

    public static void buildDismissSplit(WindowContainerTransaction windowContainerTransaction, LegacySplitScreenTaskListener legacySplitScreenTaskListener, LegacySplitDisplayLayout legacySplitDisplayLayout, boolean z) {
        int i;
        int i2;
        Objects.requireNonNull(legacySplitScreenTaskListener);
        ShellTaskOrganizer shellTaskOrganizer = legacySplitScreenTaskListener.mTaskOrganizer;
        List childTasks = shellTaskOrganizer.getChildTasks(legacySplitScreenTaskListener.mPrimary.token, (int[]) null);
        List childTasks2 = shellTaskOrganizer.getChildTasks(legacySplitScreenTaskListener.mSecondary.token, (int[]) null);
        List rootTasks = shellTaskOrganizer.getRootTasks(0, HOME_AND_RECENTS);
        rootTasks.removeIf(new WindowManagerProxy$$ExternalSyntheticLambda0(legacySplitScreenTaskListener, 0));
        if (!childTasks.isEmpty() || !childTasks2.isEmpty() || !rootTasks.isEmpty()) {
            if (z) {
                for (int size = childTasks.size() - 1; size >= 0; size--) {
                    windowContainerTransaction.reparent(((ActivityManager.RunningTaskInfo) childTasks.get(size)).token, (WindowContainerToken) null, true);
                }
                boolean z2 = false;
                for (int size2 = childTasks2.size() - 1; size2 >= 0; size2--) {
                    ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) childTasks2.get(size2);
                    windowContainerTransaction.reparent(runningTaskInfo.token, (WindowContainerToken) null, true);
                    if (isHomeOrRecentTask(runningTaskInfo)) {
                        windowContainerTransaction.setBounds(runningTaskInfo.token, (Rect) null);
                        windowContainerTransaction.setWindowingMode(runningTaskInfo.token, 0);
                        if (size2 == 0) {
                            z2 = true;
                        }
                    }
                }
                if (z2 && !Transitions.ENABLE_SHELL_TRANSITIONS) {
                    boolean isLandscape = legacySplitDisplayLayout.mDisplayLayout.isLandscape();
                    if (isLandscape) {
                        i = legacySplitDisplayLayout.mSecondary.left - legacySplitScreenTaskListener.mHomeBounds.left;
                    } else {
                        i = legacySplitDisplayLayout.mSecondary.left;
                    }
                    if (isLandscape) {
                        i2 = legacySplitDisplayLayout.mSecondary.top;
                    } else {
                        i2 = legacySplitDisplayLayout.mSecondary.top - legacySplitScreenTaskListener.mHomeBounds.top;
                    }
                    SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                    transaction.setPosition(legacySplitScreenTaskListener.mSecondarySurface, i, i2);
                    DisplayLayout displayLayout = legacySplitDisplayLayout.mDisplayLayout;
                    Objects.requireNonNull(displayLayout);
                    int i3 = displayLayout.mWidth;
                    DisplayLayout displayLayout2 = legacySplitDisplayLayout.mDisplayLayout;
                    Objects.requireNonNull(displayLayout2);
                    Rect rect = new Rect(0, 0, i3, displayLayout2.mHeight);
                    rect.offset(-i, -i2);
                    transaction.setWindowCrop(legacySplitScreenTaskListener.mSecondarySurface, rect);
                    windowContainerTransaction.setBoundsChangeTransaction(legacySplitScreenTaskListener.mSecondary.token, transaction);
                }
            } else {
                for (int size3 = childTasks2.size() - 1; size3 >= 0; size3--) {
                    if (!isHomeOrRecentTask((ActivityManager.RunningTaskInfo) childTasks2.get(size3))) {
                        windowContainerTransaction.reparent(((ActivityManager.RunningTaskInfo) childTasks2.get(size3)).token, (WindowContainerToken) null, true);
                    }
                }
                for (int size4 = childTasks2.size() - 1; size4 >= 0; size4--) {
                    ActivityManager.RunningTaskInfo runningTaskInfo2 = (ActivityManager.RunningTaskInfo) childTasks2.get(size4);
                    if (isHomeOrRecentTask(runningTaskInfo2)) {
                        windowContainerTransaction.reparent(runningTaskInfo2.token, (WindowContainerToken) null, true);
                        windowContainerTransaction.setBounds(runningTaskInfo2.token, (Rect) null);
                        windowContainerTransaction.setWindowingMode(runningTaskInfo2.token, 0);
                    }
                }
                for (int size5 = childTasks.size() - 1; size5 >= 0; size5--) {
                    windowContainerTransaction.reparent(((ActivityManager.RunningTaskInfo) childTasks.get(size5)).token, (WindowContainerToken) null, true);
                }
            }
            for (int size6 = rootTasks.size() - 1; size6 >= 0; size6--) {
                windowContainerTransaction.setBounds(((ActivityManager.RunningTaskInfo) rootTasks.get(size6)).token, (Rect) null);
                windowContainerTransaction.setWindowingMode(((ActivityManager.RunningTaskInfo) rootTasks.get(size6)).token, 0);
            }
            windowContainerTransaction.setFocusable(legacySplitScreenTaskListener.mPrimary.token, true);
        }
    }

    public static boolean isHomeOrRecentTask(ActivityManager.RunningTaskInfo runningTaskInfo) {
        int activityType = runningTaskInfo.getActivityType();
        if (activityType == 2 || activityType == 3) {
            return true;
        }
        return false;
    }

    public static void setResizing(boolean z) {
        try {
            ActivityTaskManager.getService().setSplitScreenResizing(z);
        } catch (RemoteException e) {
            Log.w("WindowManagerProxy", "Error calling setDockedStackResizing: " + e);
        }
    }
}
