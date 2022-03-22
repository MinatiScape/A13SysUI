package com.android.wm.shell;

import android.content.Context;
import com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showDetail$1;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda1;
import com.android.wm.shell.TaskViewFactoryController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class TaskViewFactoryController {
    public final TaskViewFactory mImpl = new TaskViewFactoryImpl();
    public final ShellExecutor mShellExecutor;
    public final SyncTransactionQueue mSyncQueue;
    public final ShellTaskOrganizer mTaskOrganizer;
    public final TaskViewTransitions mTaskViewTransitions;

    /* loaded from: classes.dex */
    public class TaskViewFactoryImpl implements TaskViewFactory {
        public TaskViewFactoryImpl() {
        }

        @Override // com.android.wm.shell.TaskViewFactory
        public final void create(final Context context, final DelayableExecutor delayableExecutor, final ControlActionCoordinatorImpl$showDetail$1.AnonymousClass1.C00021 r5) {
            TaskViewFactoryController.this.mShellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.TaskViewFactoryController$TaskViewFactoryImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TaskViewFactoryController.TaskViewFactoryImpl taskViewFactoryImpl = TaskViewFactoryController.TaskViewFactoryImpl.this;
                    Context context2 = context;
                    Executor executor = delayableExecutor;
                    Consumer consumer = r5;
                    Objects.requireNonNull(taskViewFactoryImpl);
                    TaskViewFactoryController taskViewFactoryController = TaskViewFactoryController.this;
                    Objects.requireNonNull(taskViewFactoryController);
                    executor.execute(new BubblesManager$5$$ExternalSyntheticLambda1(consumer, new TaskView(context2, taskViewFactoryController.mTaskOrganizer, taskViewFactoryController.mTaskViewTransitions, taskViewFactoryController.mSyncQueue), 3));
                }
            });
        }
    }

    public TaskViewFactoryController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor, SyncTransactionQueue syncTransactionQueue, TaskViewTransitions taskViewTransitions) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mShellExecutor = shellExecutor;
        this.mSyncQueue = syncTransactionQueue;
        this.mTaskViewTransitions = taskViewTransitions;
    }
}
