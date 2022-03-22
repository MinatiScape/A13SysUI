package com.android.wm.shell;

import android.os.RemoteException;
import android.window.TransitionMetrics;
import com.android.wm.shell.ShellInitImpl;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.freeform.FreeformTaskListener;
import com.android.wm.shell.fullscreen.FullscreenTaskListener;
import com.android.wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.transition.Transitions;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public class ShellInitImpl {
    public final Optional<AppPairsController> mAppPairsOptional;
    public final Optional<BubbleController> mBubblesOptional;
    public final DisplayController mDisplayController;
    public final DisplayImeController mDisplayImeController;
    public final DisplayInsetsController mDisplayInsetsController;
    public final DragAndDropController mDragAndDropController;
    public final Optional<FreeformTaskListener> mFreeformTaskListenerOptional;
    public final FullscreenTaskListener mFullscreenTaskListener;
    public final Optional<FullscreenUnfoldController> mFullscreenUnfoldController;
    public final InitImpl mImpl = new InitImpl();
    public final ShellExecutor mMainExecutor;
    public final Optional<PipTouchHandler> mPipTouchHandlerOptional;
    public final Optional<RecentTasksController> mRecentTasks;
    public final ShellTaskOrganizer mShellTaskOrganizer;
    public final Optional<SplitScreenController> mSplitScreenOptional;
    public final StartingWindowController mStartingWindow;
    public final Transitions mTransitions;

    /* loaded from: classes.dex */
    public class InitImpl implements ShellInit {
        public InitImpl() {
        }

        @Override // com.android.wm.shell.ShellInit
        public final void init() {
            try {
                ShellInitImpl.this.mMainExecutor.executeBlocking$1(new Runnable() { // from class: com.android.wm.shell.ShellInitImpl$InitImpl$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ShellInitImpl.InitImpl initImpl = ShellInitImpl.InitImpl.this;
                        Objects.requireNonNull(initImpl);
                        ShellInitImpl shellInitImpl = ShellInitImpl.this;
                        Objects.requireNonNull(shellInitImpl);
                        DisplayController displayController = shellInitImpl.mDisplayController;
                        Objects.requireNonNull(displayController);
                        try {
                            for (int i : displayController.mWmService.registerDisplayWindowListener(displayController.mDisplayContainerListener)) {
                                displayController.onDisplayAdded(i);
                            }
                            DisplayInsetsController displayInsetsController = shellInitImpl.mDisplayInsetsController;
                            Objects.requireNonNull(displayInsetsController);
                            displayInsetsController.mDisplayController.addDisplayWindowListener(displayInsetsController);
                            DisplayImeController displayImeController = shellInitImpl.mDisplayImeController;
                            Objects.requireNonNull(displayImeController);
                            displayImeController.mDisplayController.addDisplayWindowListener(displayImeController);
                            shellInitImpl.mShellTaskOrganizer.addListenerForType(shellInitImpl.mFullscreenTaskListener, -2);
                            ShellTaskOrganizer shellTaskOrganizer = shellInitImpl.mShellTaskOrganizer;
                            StartingWindowController startingWindowController = shellInitImpl.mStartingWindow;
                            Objects.requireNonNull(shellTaskOrganizer);
                            shellTaskOrganizer.mStartingWindow = startingWindowController;
                            shellInitImpl.mShellTaskOrganizer.registerOrganizer();
                            shellInitImpl.mAppPairsOptional.ifPresent(ShellInitImpl$$ExternalSyntheticLambda1.INSTANCE);
                            shellInitImpl.mSplitScreenOptional.ifPresent(ShellInitImpl$$ExternalSyntheticLambda6.INSTANCE);
                            shellInitImpl.mBubblesOptional.ifPresent(ShellInitImpl$$ExternalSyntheticLambda5.INSTANCE);
                            DragAndDropController dragAndDropController = shellInitImpl.mDragAndDropController;
                            Optional<SplitScreenController> optional = shellInitImpl.mSplitScreenOptional;
                            Objects.requireNonNull(dragAndDropController);
                            dragAndDropController.mSplitScreen = optional.orElse(null);
                            dragAndDropController.mDisplayController.addDisplayWindowListener(dragAndDropController);
                            if (Transitions.ENABLE_SHELL_TRANSITIONS) {
                                Transitions transitions = shellInitImpl.mTransitions;
                                ShellTaskOrganizer shellTaskOrganizer2 = shellInitImpl.mShellTaskOrganizer;
                                Objects.requireNonNull(transitions);
                                Transitions.TransitionPlayerImpl transitionPlayerImpl = transitions.mPlayerImpl;
                                if (transitionPlayerImpl != null) {
                                    shellTaskOrganizer2.registerTransitionPlayer(transitionPlayerImpl);
                                    TransitionMetrics.getInstance();
                                }
                            }
                            shellInitImpl.mPipTouchHandlerOptional.ifPresent(ShellInitImpl$$ExternalSyntheticLambda3.INSTANCE);
                            shellInitImpl.mFreeformTaskListenerOptional.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda0(shellInitImpl, 0));
                            shellInitImpl.mFullscreenUnfoldController.ifPresent(ShellInitImpl$$ExternalSyntheticLambda2.INSTANCE);
                            shellInitImpl.mRecentTasks.ifPresent(ShellInitImpl$$ExternalSyntheticLambda4.INSTANCE);
                        } catch (RemoteException unused) {
                            throw new RuntimeException("Unable to register display controller");
                        }
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to initialize the Shell in 2s", e);
            }
        }
    }

    public ShellInitImpl(DisplayController displayController, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, DragAndDropController dragAndDropController, ShellTaskOrganizer shellTaskOrganizer, Optional<BubbleController> optional, Optional<SplitScreenController> optional2, Optional<AppPairsController> optional3, Optional<PipTouchHandler> optional4, FullscreenTaskListener fullscreenTaskListener, Optional<FullscreenUnfoldController> optional5, Optional<FreeformTaskListener> optional6, Optional<RecentTasksController> optional7, Transitions transitions, StartingWindowController startingWindowController, ShellExecutor shellExecutor) {
        this.mDisplayController = displayController;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mDragAndDropController = dragAndDropController;
        this.mShellTaskOrganizer = shellTaskOrganizer;
        this.mBubblesOptional = optional;
        this.mSplitScreenOptional = optional2;
        this.mAppPairsOptional = optional3;
        this.mFullscreenTaskListener = fullscreenTaskListener;
        this.mPipTouchHandlerOptional = optional4;
        this.mFullscreenUnfoldController = optional5;
        this.mFreeformTaskListenerOptional = optional6;
        this.mRecentTasks = optional7;
        this.mTransitions = transitions;
        this.mMainExecutor = shellExecutor;
        this.mStartingWindow = startingWindowController;
    }
}
