package com.android.wm.shell.common;

import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.IWindowManager;
import android.view.InsetsSource;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.InsetsVisibilities;
import android.view.SurfaceControl;
import android.view.WindowInsets;
import android.view.animation.PathInterpolator;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayInsetsController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class DisplayImeController implements DisplayController.OnDisplaysChangedListener {
    public static final PathInterpolator INTERPOLATOR = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f);
    public final DisplayController mDisplayController;
    public final DisplayInsetsController mDisplayInsetsController;
    public final Executor mMainExecutor;
    public final TransactionPool mTransactionPool;
    public final IWindowManager mWmService;
    public final SparseArray<PerDisplay> mImePerDisplay = new SparseArray<>();
    public final ArrayList<ImePositionProcessor> mPositionProcessors = new ArrayList<>();

    /* loaded from: classes.dex */
    public interface ImePositionProcessor {
        default void onImeControlTargetChanged(int i, boolean z) {
        }

        default void onImeEndPositioning(int i, boolean z, SurfaceControl.Transaction transaction) {
        }

        default void onImePositionChanged(int i, int i2, SurfaceControl.Transaction transaction) {
        }

        default int onImeStartPositioning(int i, int i2, int i3, boolean z, boolean z2) {
            return 0;
        }

        default void onImeVisibilityChanged(int i, boolean z) {
        }
    }

    /* loaded from: classes.dex */
    public class PerDisplay implements DisplayInsetsController.OnInsetsChangedListener {
        public final int mDisplayId;
        public int mRotation;
        public final InsetsState mInsetsState = new InsetsState();
        public final InsetsVisibilities mRequestedVisibilities = new InsetsVisibilities();
        public InsetsSourceControl mImeSourceControl = null;
        public int mAnimationDirection = 0;
        public ValueAnimator mAnimation = null;
        public boolean mImeShowing = false;
        public final Rect mImeFrame = new Rect();
        public boolean mAnimateAlpha = true;

        @Override // com.android.wm.shell.common.DisplayInsetsController.OnInsetsChangedListener
        public final void topFocusedWindowChanged() {
        }

        public PerDisplay(int i, int i2) {
            this.mRotation = 0;
            this.mDisplayId = i;
            this.mRotation = i2;
        }

        @Override // com.android.wm.shell.common.DisplayInsetsController.OnInsetsChangedListener
        public final void insetsChanged(InsetsState insetsState) {
            if (!this.mInsetsState.equals(insetsState)) {
                updateImeVisibility(insetsState.getSourceOrDefaultVisibility(19));
                InsetsSource source = insetsState.getSource(19);
                Rect frame = source.getFrame();
                Rect frame2 = this.mInsetsState.getSource(19).getFrame();
                this.mInsetsState.set(insetsState, true);
                if (this.mImeShowing && !frame.equals(frame2) && source.isVisible()) {
                    startAnimation(this.mImeShowing, true);
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x0044  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x0063  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x0083  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x009e  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x00bc  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00be  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00c1  */
        /* JADX WARN: Removed duplicated region for block: B:43:0x00c3  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x00d2  */
        /* JADX WARN: Removed duplicated region for block: B:52:0x00d4  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x00e8  */
        /* JADX WARN: Removed duplicated region for block: B:56:0x00eb  */
        /* JADX WARN: Removed duplicated region for block: B:59:0x00f2  */
        /* JADX WARN: Removed duplicated region for block: B:62:0x0126  */
        /* JADX WARN: Removed duplicated region for block: B:66:0x0147  */
        /* JADX WARN: Removed duplicated region for block: B:77:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void startAnimation(boolean r17, boolean r18) {
            /*
                Method dump skipped, instructions count: 354
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.common.DisplayImeController.PerDisplay.startAnimation(boolean, boolean):void");
        }

        public final void updateImeVisibility(boolean z) {
            if (this.mImeShowing != z) {
                this.mImeShowing = z;
                DisplayImeController displayImeController = DisplayImeController.this;
                int i = this.mDisplayId;
                PathInterpolator pathInterpolator = DisplayImeController.INTERPOLATOR;
                Objects.requireNonNull(displayImeController);
                synchronized (displayImeController.mPositionProcessors) {
                    Iterator<ImePositionProcessor> it = displayImeController.mPositionProcessors.iterator();
                    while (it.hasNext()) {
                        it.next().onImeVisibilityChanged(i, z);
                    }
                }
            }
        }

        @Override // com.android.wm.shell.common.DisplayInsetsController.OnInsetsChangedListener
        public final void hideInsets(int i) {
            if ((i & WindowInsets.Type.ime()) != 0) {
                startAnimation(false, false);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:49:0x009b  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x00a3  */
        @Override // com.android.wm.shell.common.DisplayInsetsController.OnInsetsChangedListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void insetsControlChanged(android.view.InsetsState r8, android.view.InsetsSourceControl[] r9) {
            /*
                Method dump skipped, instructions count: 245
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.common.DisplayImeController.PerDisplay.insetsControlChanged(android.view.InsetsState, android.view.InsetsSourceControl[]):void");
        }

        @Override // com.android.wm.shell.common.DisplayInsetsController.OnInsetsChangedListener
        public final void showInsets(int i) {
            if ((i & WindowInsets.Type.ime()) != 0) {
                startAnimation(true, false);
            }
        }
    }

    public final void addPositionProcessor(ImePositionProcessor imePositionProcessor) {
        synchronized (this.mPositionProcessors) {
            if (!this.mPositionProcessors.contains(imePositionProcessor)) {
                this.mPositionProcessors.add(imePositionProcessor);
            }
        }
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public final void onDisplayAdded(int i) {
        DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(i);
        Objects.requireNonNull(displayLayout);
        PerDisplay perDisplay = new PerDisplay(i, displayLayout.mRotation);
        this.mDisplayInsetsController.addInsetsChangedListener(perDisplay.mDisplayId, perDisplay);
        this.mImePerDisplay.put(i, perDisplay);
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public final void onDisplayConfigurationChanged(int i, Configuration configuration) {
        boolean z;
        InsetsSource source;
        PerDisplay perDisplay = this.mImePerDisplay.get(i);
        if (perDisplay != null) {
            DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(i);
            Objects.requireNonNull(displayLayout);
            if (displayLayout.mRotation != perDisplay.mRotation) {
                PerDisplay perDisplay2 = this.mImePerDisplay.get(i);
                if (perDisplay2 == null || (source = perDisplay2.mInsetsState.getSource(19)) == null || perDisplay2.mImeSourceControl == null || !source.isVisible()) {
                    z = false;
                } else {
                    z = true;
                }
                if (z) {
                    perDisplay.startAnimation(true, false);
                }
            }
        }
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public final void onDisplayRemoved(int i) {
        PerDisplay perDisplay = this.mImePerDisplay.get(i);
        if (perDisplay != null) {
            DisplayInsetsController displayInsetsController = DisplayImeController.this.mDisplayInsetsController;
            int i2 = perDisplay.mDisplayId;
            Objects.requireNonNull(displayInsetsController);
            CopyOnWriteArrayList<DisplayInsetsController.OnInsetsChangedListener> copyOnWriteArrayList = displayInsetsController.mListeners.get(i2);
            if (copyOnWriteArrayList != null) {
                copyOnWriteArrayList.remove(perDisplay);
            }
            this.mImePerDisplay.remove(i);
        }
    }

    public DisplayImeController(IWindowManager iWindowManager, DisplayController displayController, DisplayInsetsController displayInsetsController, ShellExecutor shellExecutor, TransactionPool transactionPool) {
        this.mWmService = iWindowManager;
        this.mDisplayController = displayController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mMainExecutor = shellExecutor;
        this.mTransactionPool = transactionPool;
    }
}
