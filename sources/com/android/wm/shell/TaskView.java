package com.android.wm.shell;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Binder;
import android.util.CloseGuard;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.TaskViewTransitions;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class TaskView extends SurfaceView implements SurfaceHolder.Callback, ShellTaskOrganizer.TaskListener, ViewTreeObserver.OnComputeInternalInsetsListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final CloseGuard mGuard;
    public boolean mIsInitialized;
    public Listener mListener;
    public Executor mListenerExecutor;
    public Rect mObscuredTouchRect;
    public final Executor mShellExecutor;
    public boolean mSurfaceCreated;
    public final SyncTransactionQueue mSyncQueue;
    public ActivityManager.RunningTaskInfo mTaskInfo;
    public SurfaceControl mTaskLeash;
    public final ShellTaskOrganizer mTaskOrganizer;
    public WindowContainerToken mTaskToken;
    public final TaskViewTransitions mTaskViewTransitions;
    public final SurfaceControl.Transaction mTransaction = new SurfaceControl.Transaction();
    public final Rect mTmpRect = new Rect();
    public final Rect mTmpRootRect = new Rect();
    public final int[] mTmpLocation = new int[2];

    /* loaded from: classes.dex */
    public interface Listener {
        default void onBackPressedOnTaskRoot(int i) {
        }

        default void onInitialized() {
        }

        default void onReleased() {
        }

        default void onTaskCreated(int i) {
        }

        default void onTaskRemovalStarted() {
        }

        default void onTaskVisibilityChanged(boolean z) {
        }
    }

    public TaskView(Context context, ShellTaskOrganizer shellTaskOrganizer, TaskViewTransitions taskViewTransitions, SyncTransactionQueue syncTransactionQueue) {
        super(context, null, 0, 0, true);
        CloseGuard closeGuard = new CloseGuard();
        this.mGuard = closeGuard;
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mShellExecutor = shellTaskOrganizer.getExecutor();
        this.mSyncQueue = syncTransactionQueue;
        this.mTaskViewTransitions = taskViewTransitions;
        if (taskViewTransitions != null) {
            synchronized (taskViewTransitions.mRegistered) {
                try {
                    boolean[] zArr = taskViewTransitions.mRegistered;
                    if (!zArr[0]) {
                        zArr[0] = true;
                        Transitions transitions = taskViewTransitions.mTransitions;
                        Objects.requireNonNull(transitions);
                        transitions.mHandlers.add(taskViewTransitions);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            taskViewTransitions.mTaskViews.add(this);
        }
        setUseAlpha();
        getHolder().addCallback(this);
        closeGuard.open("release");
    }

    public final void onLocationChanged(WindowContainerTransaction windowContainerTransaction) {
        getBoundsOnScreen(this.mTmpRect);
        getRootView().getBoundsOnScreen(this.mTmpRootRect);
        if (!this.mTmpRootRect.contains(this.mTmpRect)) {
            this.mTmpRect.offsetTo(0, 0);
        }
        windowContainerTransaction.setBounds(this.mTaskToken, this.mTmpRect);
    }

    @Override // android.view.SurfaceHolder.Callback
    public final void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mSurfaceCreated = true;
        if (this.mListener != null && !this.mIsInitialized) {
            this.mIsInitialized = true;
            this.mListenerExecutor.execute(new TaskView$$ExternalSyntheticLambda2(this, 0));
        }
        this.mShellExecutor.execute(new TaskView$$ExternalSyntheticLambda6(this, 0));
    }

    @Override // android.view.SurfaceHolder.Callback
    public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mSurfaceCreated = false;
        this.mShellExecutor.execute(new TaskView$$ExternalSyntheticLambda5(this, 0));
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        if (this.mTaskInfo.taskId == i) {
            builder.setParent(this.mTaskLeash);
            return;
        }
        throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("There is no surface for taskId=", i));
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + this);
    }

    public final void finalize() throws Throwable {
        try {
            CloseGuard closeGuard = this.mGuard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
                performRelease();
            }
        } finally {
            super.finalize();
        }
    }

    public final boolean isUsingShellTransitions() {
        if (this.mTaskViewTransitions == null || !Transitions.ENABLE_SHELL_TRANSITIONS) {
            return false;
        }
        return true;
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
        WindowContainerToken windowContainerToken = this.mTaskToken;
        if (windowContainerToken != null && windowContainerToken.equals(runningTaskInfo.token) && this.mListener != null) {
            final int i = runningTaskInfo.taskId;
            this.mListenerExecutor.execute(new Runnable() { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    TaskView taskView = TaskView.this;
                    int i2 = i;
                    int i3 = TaskView.$r8$clinit;
                    Objects.requireNonNull(taskView);
                    taskView.mListener.onBackPressedOnTaskRoot(i2);
                }
            });
        }
    }

    public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        if (internalInsetsInfo.touchableRegion.isEmpty()) {
            internalInsetsInfo.setTouchableInsets(3);
            View rootView = getRootView();
            rootView.getLocationInWindow(this.mTmpLocation);
            Rect rect = this.mTmpRootRect;
            int[] iArr = this.mTmpLocation;
            rect.set(iArr[0], iArr[1], rootView.getWidth(), rootView.getHeight());
            internalInsetsInfo.touchableRegion.set(this.mTmpRootRect);
        }
        getLocationInWindow(this.mTmpLocation);
        Rect rect2 = this.mTmpRect;
        int[] iArr2 = this.mTmpLocation;
        rect2.set(iArr2[0], iArr2[1], getWidth() + iArr2[0], getHeight() + this.mTmpLocation[1]);
        internalInsetsInfo.touchableRegion.op(this.mTmpRect, Region.Op.DIFFERENCE);
        Rect rect3 = this.mObscuredTouchRect;
        if (rect3 != null) {
            internalInsetsInfo.touchableRegion.union(rect3);
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        ActivityManager.TaskDescription taskDescription = runningTaskInfo.taskDescription;
        if (taskDescription != null) {
            setResizeBackgroundColor(taskDescription.getBackgroundColor());
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        WindowContainerToken windowContainerToken = this.mTaskToken;
        if (windowContainerToken != null && windowContainerToken.equals(runningTaskInfo.token)) {
            if (this.mListener != null) {
                final int i = runningTaskInfo.taskId;
                this.mListenerExecutor.execute(new Runnable() { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        TaskView taskView = TaskView.this;
                        int i2 = TaskView.$r8$clinit;
                        Objects.requireNonNull(taskView);
                        taskView.mListener.onTaskRemovalStarted();
                    }
                });
            }
            this.mTaskOrganizer.setInterceptBackPressedOnTaskRoot(this.mTaskToken, false);
            this.mTransaction.reparent(this.mTaskLeash, null).apply();
            this.mTaskInfo = null;
            this.mTaskToken = null;
            this.mTaskLeash = null;
        }
    }

    public final void prepareActivityOptions(ActivityOptions activityOptions, Rect rect) {
        Binder binder = new Binder();
        this.mShellExecutor.execute(new TaskView$$ExternalSyntheticLambda7(this, binder, 0));
        activityOptions.setLaunchBounds(rect);
        activityOptions.setLaunchCookie(binder);
        activityOptions.setLaunchWindowingMode(6);
        activityOptions.setRemoveWithTaskOrganizer(true);
    }

    @Override // android.view.SurfaceHolder.Callback
    public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (this.mTaskToken != null) {
            onLocationChanged();
        }
    }

    @Override // android.view.View
    public final String toString() {
        Object obj;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("TaskView:");
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo;
        if (runningTaskInfo != null) {
            obj = Integer.valueOf(runningTaskInfo.taskId);
        } else {
            obj = "null";
        }
        m.append(obj);
        return m.toString();
    }

    public final void updateTaskVisibility() {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.setHidden(this.mTaskToken, !this.mSurfaceCreated);
        this.mSyncQueue.queue(windowContainerTransaction);
        if (this.mListener != null) {
            final int i = this.mTaskInfo.taskId;
            this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda0
                @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                    final TaskView taskView = TaskView.this;
                    final int i2 = i;
                    int i3 = TaskView.$r8$clinit;
                    Objects.requireNonNull(taskView);
                    taskView.mListenerExecutor.execute(new Runnable() { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda11
                        @Override // java.lang.Runnable
                        public final void run() {
                            TaskView taskView2 = TaskView.this;
                            int i4 = TaskView.$r8$clinit;
                            Objects.requireNonNull(taskView2);
                            taskView2.mListener.onTaskVisibilityChanged(taskView2.mSurfaceCreated);
                        }
                    });
                }
            });
        }
    }

    public static void $r8$lambda$GN2DCVjyacAWE0TrNN18t7khmiQ(TaskView taskView, ShortcutInfo shortcutInfo, ActivityOptions activityOptions) {
        Objects.requireNonNull(taskView);
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.startShortcut(((SurfaceView) taskView).mContext.getPackageName(), shortcutInfo, activityOptions.toBundle());
        TaskViewTransitions taskViewTransitions = taskView.mTaskViewTransitions;
        Objects.requireNonNull(taskViewTransitions);
        taskViewTransitions.mPending.add(new TaskViewTransitions.PendingTransition(1, windowContainerTransaction, taskView));
        taskViewTransitions.startNextTransition();
    }

    @Override // android.view.SurfaceView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnComputeInternalInsetsListener(this);
    }

    @Override // android.view.SurfaceView, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public final void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (!isUsingShellTransitions()) {
            this.mTaskInfo = runningTaskInfo;
            this.mTaskToken = runningTaskInfo.token;
            this.mTaskLeash = surfaceControl;
            if (this.mSurfaceCreated) {
                this.mTransaction.reparent(surfaceControl, getSurfaceControl()).show(this.mTaskLeash).apply();
            } else {
                updateTaskVisibility();
            }
            this.mTaskOrganizer.setInterceptBackPressedOnTaskRoot(this.mTaskToken, true);
            onLocationChanged();
            ActivityManager.TaskDescription taskDescription = runningTaskInfo.taskDescription;
            if (taskDescription != null) {
                final int backgroundColor = taskDescription.getBackgroundColor();
                this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda1
                    @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                        TaskView taskView = TaskView.this;
                        int i = backgroundColor;
                        int i2 = TaskView.$r8$clinit;
                        Objects.requireNonNull(taskView);
                        taskView.setResizeBackgroundColor(transaction, i);
                    }
                });
            }
            if (this.mListener != null) {
                final int i = runningTaskInfo.taskId;
                final ComponentName componentName = runningTaskInfo.baseActivity;
                this.mListenerExecutor.execute(new Runnable() { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda13
                    @Override // java.lang.Runnable
                    public final void run() {
                        TaskView taskView = TaskView.this;
                        int i2 = i;
                        int i3 = TaskView.$r8$clinit;
                        Objects.requireNonNull(taskView);
                        taskView.mListener.onTaskCreated(i2);
                    }
                });
            }
        }
    }

    public final void performRelease() {
        getHolder().removeCallback(this);
        TaskViewTransitions taskViewTransitions = this.mTaskViewTransitions;
        if (taskViewTransitions != null) {
            Objects.requireNonNull(taskViewTransitions);
            taskViewTransitions.mTaskViews.remove(this);
        }
        this.mShellExecutor.execute(new TaskView$$ExternalSyntheticLambda3(this, 0));
        this.mGuard.close();
        if (this.mListener != null && this.mIsInitialized) {
            this.mListenerExecutor.execute(new TaskView$$ExternalSyntheticLambda4(this, 0));
            this.mIsInitialized = false;
        }
    }

    public final void startActivity(final PendingIntent pendingIntent, final Intent intent, final ActivityOptions activityOptions, Rect rect) {
        prepareActivityOptions(activityOptions, rect);
        if (isUsingShellTransitions()) {
            this.mShellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.TaskView$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    TaskView taskView = TaskView.this;
                    PendingIntent pendingIntent2 = pendingIntent;
                    Intent intent2 = intent;
                    ActivityOptions activityOptions2 = activityOptions;
                    int i = TaskView.$r8$clinit;
                    Objects.requireNonNull(taskView);
                    WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                    windowContainerTransaction.sendPendingIntent(pendingIntent2, intent2, activityOptions2.toBundle());
                    TaskViewTransitions taskViewTransitions = taskView.mTaskViewTransitions;
                    Objects.requireNonNull(taskViewTransitions);
                    taskViewTransitions.mPending.add(new TaskViewTransitions.PendingTransition(1, windowContainerTransaction, taskView));
                    taskViewTransitions.startNextTransition();
                }
            });
            return;
        }
        try {
            pendingIntent.send(((SurfaceView) this).mContext, 0, intent, null, null, null, activityOptions.toBundle());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void startShortcutActivity(ShortcutInfo shortcutInfo, ActivityOptions activityOptions, Rect rect) {
        prepareActivityOptions(activityOptions, rect);
        LauncherApps launcherApps = (LauncherApps) ((SurfaceView) this).mContext.getSystemService(LauncherApps.class);
        if (isUsingShellTransitions()) {
            this.mShellExecutor.execute(new TaskView$$ExternalSyntheticLambda8(this, shortcutInfo, activityOptions, 0));
            return;
        }
        try {
            launcherApps.startShortcut(shortcutInfo, null, activityOptions.toBundle());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void onLocationChanged() {
        if (this.mTaskToken != null) {
            if (isUsingShellTransitions()) {
                TaskViewTransitions taskViewTransitions = this.mTaskViewTransitions;
                Objects.requireNonNull(taskViewTransitions);
                if (!taskViewTransitions.mPending.isEmpty()) {
                    return;
                }
            }
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            onLocationChanged(windowContainerTransaction);
            this.mSyncQueue.queue(windowContainerTransaction);
        }
    }
}
