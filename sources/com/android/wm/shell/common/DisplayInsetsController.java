package com.android.wm.shell.common;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.RemoteException;
import android.util.Slog;
import android.util.SparseArray;
import android.view.IDisplayWindowInsetsController;
import android.view.IWindowManager;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import com.android.systemui.ScreenDecorations$2$$ExternalSyntheticLambda0;
import com.android.systemui.util.condition.Monitor$$ExternalSyntheticLambda1;
import com.android.wm.shell.bubbles.BubbleController$5$$ExternalSyntheticLambda0;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayInsetsController;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public final class DisplayInsetsController implements DisplayController.OnDisplaysChangedListener {
    public final DisplayController mDisplayController;
    public final SparseArray<PerDisplay> mInsetsPerDisplay = new SparseArray<>();
    public final SparseArray<CopyOnWriteArrayList<OnInsetsChangedListener>> mListeners = new SparseArray<>();
    public final ShellExecutor mMainExecutor;
    public final IWindowManager mWmService;

    /* loaded from: classes.dex */
    public interface OnInsetsChangedListener {
        default void hideInsets(int i) {
        }

        default void insetsChanged(InsetsState insetsState) {
        }

        default void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
        }

        default void showInsets(int i) {
        }

        default void topFocusedWindowChanged() {
        }
    }

    /* loaded from: classes.dex */
    public class PerDisplay {
        public final int mDisplayId;
        public final DisplayWindowInsetsControllerImpl mInsetsControllerImpl = new DisplayWindowInsetsControllerImpl();

        /* loaded from: classes.dex */
        public class DisplayWindowInsetsControllerImpl extends IDisplayWindowInsetsController.Stub {
            public static final /* synthetic */ int $r8$clinit = 0;

            public DisplayWindowInsetsControllerImpl() {
            }

            public final void hideInsets(final int i, final boolean z) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.common.DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl = DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl.this;
                        int i2 = i;
                        Objects.requireNonNull(displayWindowInsetsControllerImpl);
                        DisplayInsetsController.PerDisplay perDisplay = DisplayInsetsController.PerDisplay.this;
                        Objects.requireNonNull(perDisplay);
                        CopyOnWriteArrayList<DisplayInsetsController.OnInsetsChangedListener> copyOnWriteArrayList = DisplayInsetsController.this.mListeners.get(perDisplay.mDisplayId);
                        if (copyOnWriteArrayList != null) {
                            Iterator<DisplayInsetsController.OnInsetsChangedListener> it = copyOnWriteArrayList.iterator();
                            while (it.hasNext()) {
                                it.next().hideInsets(i2);
                            }
                        }
                    }
                });
            }

            public final void insetsChanged(InsetsState insetsState) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new ScreenDecorations$2$$ExternalSyntheticLambda0(this, insetsState, 2));
            }

            public final void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new BubbleController$5$$ExternalSyntheticLambda0(this, insetsState, insetsSourceControlArr, 1));
            }

            public final void showInsets(final int i, final boolean z) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.common.DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl = DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl.this;
                        int i2 = i;
                        Objects.requireNonNull(displayWindowInsetsControllerImpl);
                        DisplayInsetsController.PerDisplay perDisplay = DisplayInsetsController.PerDisplay.this;
                        Objects.requireNonNull(perDisplay);
                        CopyOnWriteArrayList<DisplayInsetsController.OnInsetsChangedListener> copyOnWriteArrayList = DisplayInsetsController.this.mListeners.get(perDisplay.mDisplayId);
                        if (copyOnWriteArrayList != null) {
                            Iterator<DisplayInsetsController.OnInsetsChangedListener> it = copyOnWriteArrayList.iterator();
                            while (it.hasNext()) {
                                it.next().showInsets(i2);
                            }
                        }
                    }
                });
            }

            public final void topFocusedWindowChanged(String str) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new Monitor$$ExternalSyntheticLambda1(this, str, 2));
            }
        }

        public PerDisplay(int i) {
            this.mDisplayId = i;
        }
    }

    public final void addInsetsChangedListener(int i, OnInsetsChangedListener onInsetsChangedListener) {
        CopyOnWriteArrayList<OnInsetsChangedListener> copyOnWriteArrayList = this.mListeners.get(i);
        if (copyOnWriteArrayList == null) {
            copyOnWriteArrayList = new CopyOnWriteArrayList<>();
            this.mListeners.put(i, copyOnWriteArrayList);
        }
        if (!copyOnWriteArrayList.contains(onInsetsChangedListener)) {
            copyOnWriteArrayList.add(onInsetsChangedListener);
        }
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public final void onDisplayAdded(int i) {
        PerDisplay perDisplay = new PerDisplay(i);
        try {
            this.mWmService.setDisplayWindowInsetsController(perDisplay.mDisplayId, perDisplay.mInsetsControllerImpl);
        } catch (RemoteException unused) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unable to set insets controller on display ");
            m.append(perDisplay.mDisplayId);
            Slog.w("DisplayInsetsController", m.toString());
        }
        this.mInsetsPerDisplay.put(i, perDisplay);
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public final void onDisplayRemoved(int i) {
        PerDisplay perDisplay = this.mInsetsPerDisplay.get(i);
        if (perDisplay != null) {
            try {
                DisplayInsetsController.this.mWmService.setDisplayWindowInsetsController(perDisplay.mDisplayId, (IDisplayWindowInsetsController) null);
            } catch (RemoteException unused) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unable to remove insets controller on display ");
                m.append(perDisplay.mDisplayId);
                Slog.w("DisplayInsetsController", m.toString());
            }
            this.mInsetsPerDisplay.remove(i);
        }
    }

    public DisplayInsetsController(IWindowManager iWindowManager, DisplayController displayController, ShellExecutor shellExecutor) {
        this.mWmService = iWindowManager;
        this.mDisplayController = displayController;
        this.mMainExecutor = shellExecutor;
    }
}
