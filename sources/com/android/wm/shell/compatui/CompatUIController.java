package com.android.wm.shell.compatui;

import android.app.TaskInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.util.ArraySet;
import android.util.SparseArray;
import android.view.Display;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardSliceViewController$$ExternalSyntheticLambda1;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import com.android.systemui.dreams.DreamOverlayStateController$$ExternalSyntheticLambda1;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda2;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda7;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.compatui.CompatUIWindowManager;
import com.android.wm.shell.compatui.letterboxedu.LetterboxEduAnimationController;
import com.android.wm.shell.compatui.letterboxedu.LetterboxEduWindowManager;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController$$ExternalSyntheticLambda1;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final class CompatUIController implements DisplayController.OnDisplaysChangedListener, DisplayImeController.ImePositionProcessor {
    public LetterboxEduWindowManager mActiveLetterboxEduLayout;
    public CompatUICallback mCallback;
    public final Context mContext;
    public final DisplayController mDisplayController;
    public final DisplayInsetsController mDisplayInsetsController;
    public boolean mKeyguardOccluded;
    public final ShellExecutor mMainExecutor;
    public final SyncTransactionQueue mSyncQueue;
    public final ArraySet mDisplaysWithIme = new ArraySet(1);
    public final SparseArray<PerDisplayOnInsetsChangedListener> mOnInsetsChangedListeners = new SparseArray<>(0);
    public final SparseArray<CompatUIWindowManager> mActiveCompatLayouts = new SparseArray<>(0);
    public final SparseArray<WeakReference<Context>> mDisplayContextCache = new SparseArray<>(0);
    public final CompatUIImpl mImpl = new CompatUIImpl();
    public final CompatUIWindowManager.CompatUIHintsState mCompatUIHintsState = new CompatUIWindowManager.CompatUIHintsState();

    /* loaded from: classes.dex */
    public interface CompatUICallback {
    }

    /* loaded from: classes.dex */
    public class CompatUIImpl implements CompatUI {
        public CompatUIImpl() {
        }

        @Override // com.android.wm.shell.compatui.CompatUI
        public final void onKeyguardOccludedChanged(boolean z) {
            CompatUIController.this.mMainExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda1(this, z, 1));
        }
    }

    /* loaded from: classes.dex */
    public class PerDisplayOnInsetsChangedListener implements DisplayInsetsController.OnInsetsChangedListener {
        public final int mDisplayId;
        public final InsetsState mInsetsState = new InsetsState();

        public PerDisplayOnInsetsChangedListener(int i) {
            this.mDisplayId = i;
        }

        @Override // com.android.wm.shell.common.DisplayInsetsController.OnInsetsChangedListener
        public final void insetsChanged(InsetsState insetsState) {
            if (!this.mInsetsState.equals(insetsState)) {
                this.mInsetsState.set(insetsState);
                CompatUIController compatUIController = CompatUIController.this;
                int i = this.mDisplayId;
                Objects.requireNonNull(compatUIController);
                compatUIController.forAllLayouts(new CompatUIController$$ExternalSyntheticLambda2(i), new WMShell$$ExternalSyntheticLambda2(compatUIController.mDisplayController.getDisplayLayout(i), 6));
            }
        }

        @Override // com.android.wm.shell.common.DisplayInsetsController.OnInsetsChangedListener
        public final void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
            insetsChanged(insetsState);
        }
    }

    public final void forAllLayouts(Predicate<CompatUIWindowManagerAbstract> predicate, Consumer<CompatUIWindowManagerAbstract> consumer) {
        for (int i = 0; i < this.mActiveCompatLayouts.size(); i++) {
            CompatUIWindowManager compatUIWindowManager = this.mActiveCompatLayouts.get(this.mActiveCompatLayouts.keyAt(i));
            if (compatUIWindowManager != null && predicate.test(compatUIWindowManager)) {
                consumer.accept(compatUIWindowManager);
            }
        }
        LetterboxEduWindowManager letterboxEduWindowManager = this.mActiveLetterboxEduLayout;
        if (letterboxEduWindowManager != null && predicate.test(letterboxEduWindowManager)) {
            consumer.accept(this.mActiveLetterboxEduLayout);
        }
    }

    @VisibleForTesting
    public CompatUIWindowManager createCompatUiWindowManager(Context context, TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener) {
        return new CompatUIWindowManager(context, taskInfo, this.mSyncQueue, this.mCallback, taskListener, this.mDisplayController.getDisplayLayout(taskInfo.displayId), this.mCompatUIHintsState);
    }

    @VisibleForTesting
    public LetterboxEduWindowManager createLetterboxEduWindowManager(Context context, TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener) {
        return new LetterboxEduWindowManager(context, taskInfo, this.mSyncQueue, taskListener, this.mDisplayController.getDisplayLayout(taskInfo.displayId), new SuggestController$$ExternalSyntheticLambda1(this, 8), new LetterboxEduAnimationController(context));
    }

    public final Context getOrCreateDisplayContext(int i) {
        if (i == 0) {
            return this.mContext;
        }
        Context context = null;
        WeakReference<Context> weakReference = this.mDisplayContextCache.get(i);
        if (weakReference != null) {
            context = weakReference.get();
        }
        if (context != null) {
            return context;
        }
        Display display = ((DisplayManager) this.mContext.getSystemService(DisplayManager.class)).getDisplay(i);
        if (display != null) {
            Context createDisplayContext = this.mContext.createDisplayContext(display);
            this.mDisplayContextCache.put(i, new WeakReference<>(createDisplayContext));
            return createDisplayContext;
        }
        KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Cannot get context for display ", i, "CompatUIController");
        return context;
    }

    public final void onCompatInfoChanged(TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener) {
        if (taskInfo.configuration == null || taskListener == null) {
            removeLayouts(taskInfo.taskId);
            return;
        }
        CompatUIWindowManager compatUIWindowManager = this.mActiveCompatLayouts.get(taskInfo.taskId);
        if (compatUIWindowManager == null) {
            Context orCreateDisplayContext = getOrCreateDisplayContext(taskInfo.displayId);
            if (orCreateDisplayContext != null) {
                CompatUIWindowManager createCompatUiWindowManager = createCompatUiWindowManager(orCreateDisplayContext, taskInfo, taskListener);
                if (createCompatUiWindowManager.createLayout(showOnDisplay(taskInfo.displayId))) {
                    this.mActiveCompatLayouts.put(taskInfo.taskId, createCompatUiWindowManager);
                }
            }
        } else if (!compatUIWindowManager.updateCompatInfo(taskInfo, taskListener, showOnDisplay(compatUIWindowManager.mDisplayId))) {
            this.mActiveCompatLayouts.remove(taskInfo.taskId);
        }
        LetterboxEduWindowManager letterboxEduWindowManager = this.mActiveLetterboxEduLayout;
        if (letterboxEduWindowManager == null || letterboxEduWindowManager.mTaskId != taskInfo.taskId) {
            Context orCreateDisplayContext2 = getOrCreateDisplayContext(taskInfo.displayId);
            if (orCreateDisplayContext2 != null) {
                LetterboxEduWindowManager createLetterboxEduWindowManager = createLetterboxEduWindowManager(orCreateDisplayContext2, taskInfo, taskListener);
                if (createLetterboxEduWindowManager.createLayout(showOnDisplay(taskInfo.displayId))) {
                    LetterboxEduWindowManager letterboxEduWindowManager2 = this.mActiveLetterboxEduLayout;
                    if (letterboxEduWindowManager2 != null) {
                        letterboxEduWindowManager2.release();
                    }
                    this.mActiveLetterboxEduLayout = createLetterboxEduWindowManager;
                }
            }
        } else if (!letterboxEduWindowManager.updateCompatInfo(taskInfo, taskListener, showOnDisplay(letterboxEduWindowManager.mDisplayId))) {
            this.mActiveLetterboxEduLayout = null;
        }
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public final void onDisplayAdded(int i) {
        PerDisplayOnInsetsChangedListener perDisplayOnInsetsChangedListener = new PerDisplayOnInsetsChangedListener(i);
        this.mDisplayInsetsController.addInsetsChangedListener(perDisplayOnInsetsChangedListener.mDisplayId, perDisplayOnInsetsChangedListener);
        this.mOnInsetsChangedListeners.put(i, perDisplayOnInsetsChangedListener);
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public final void onDisplayConfigurationChanged(int i, Configuration configuration) {
        forAllLayouts(new CompatUIController$$ExternalSyntheticLambda2(i), new WMShell$$ExternalSyntheticLambda2(this.mDisplayController.getDisplayLayout(i), 6));
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public final void onDisplayRemoved(int i) {
        this.mDisplayContextCache.remove(i);
        PerDisplayOnInsetsChangedListener perDisplayOnInsetsChangedListener = this.mOnInsetsChangedListeners.get(i);
        if (perDisplayOnInsetsChangedListener != null) {
            DisplayInsetsController displayInsetsController = CompatUIController.this.mDisplayInsetsController;
            int i2 = perDisplayOnInsetsChangedListener.mDisplayId;
            Objects.requireNonNull(displayInsetsController);
            CopyOnWriteArrayList<DisplayInsetsController.OnInsetsChangedListener> copyOnWriteArrayList = displayInsetsController.mListeners.get(i2);
            if (copyOnWriteArrayList != null) {
                copyOnWriteArrayList.remove(perDisplayOnInsetsChangedListener);
            }
            this.mOnInsetsChangedListeners.remove(i);
        }
        ArrayList arrayList = new ArrayList();
        forAllLayouts(new CompatUIController$$ExternalSyntheticLambda2(i), new WMShell$$ExternalSyntheticLambda7(arrayList, 2));
        int size = arrayList.size();
        while (true) {
            size--;
            if (size >= 0) {
                removeLayouts(((Integer) arrayList.get(size)).intValue());
            } else {
                return;
            }
        }
    }

    @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
    public final void onImeVisibilityChanged(int i, boolean z) {
        if (z) {
            this.mDisplaysWithIme.add(Integer.valueOf(i));
        } else {
            this.mDisplaysWithIme.remove(Integer.valueOf(i));
        }
        forAllLayouts(new CompatUIController$$ExternalSyntheticLambda2(i), new CompatUIController$$ExternalSyntheticLambda0(this, i, 0));
    }

    @VisibleForTesting
    public void onKeyguardOccludedChanged(boolean z) {
        this.mKeyguardOccluded = z;
        forAllLayouts(KeyguardSliceViewController$$ExternalSyntheticLambda1.INSTANCE$1, new Consumer() { // from class: com.android.wm.shell.compatui.CompatUIController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CompatUIController compatUIController = CompatUIController.this;
                CompatUIWindowManagerAbstract compatUIWindowManagerAbstract = (CompatUIWindowManagerAbstract) obj;
                Objects.requireNonNull(compatUIController);
                Objects.requireNonNull(compatUIWindowManagerAbstract);
                compatUIWindowManagerAbstract.updateVisibility(compatUIController.showOnDisplay(compatUIWindowManagerAbstract.mDisplayId));
            }
        });
    }

    public final void removeLayouts(int i) {
        CompatUIWindowManager compatUIWindowManager = this.mActiveCompatLayouts.get(i);
        if (compatUIWindowManager != null) {
            compatUIWindowManager.release();
            this.mActiveCompatLayouts.remove(i);
        }
        LetterboxEduWindowManager letterboxEduWindowManager = this.mActiveLetterboxEduLayout;
        if (letterboxEduWindowManager != null) {
            Objects.requireNonNull(letterboxEduWindowManager);
            if (letterboxEduWindowManager.mTaskId == i) {
                this.mActiveLetterboxEduLayout.release();
                this.mActiveLetterboxEduLayout = null;
            }
        }
    }

    public final boolean showOnDisplay(int i) {
        if (this.mKeyguardOccluded || this.mDisplaysWithIme.contains(Integer.valueOf(i))) {
            return false;
        }
        return true;
    }

    public CompatUIController(Context context, DisplayController displayController, DisplayInsetsController displayInsetsController, DisplayImeController displayImeController, SyncTransactionQueue syncTransactionQueue, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mDisplayController = displayController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mSyncQueue = syncTransactionQueue;
        this.mMainExecutor = shellExecutor;
        displayController.addDisplayWindowListener(this);
        displayImeController.addPositionProcessor(this);
    }
}
