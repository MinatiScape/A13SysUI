package com.android.wm.shell.pip.phone;

import android.app.RemoteAction;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceControl;
import android.view.SyncRtSurfaceTransactionApplier;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda2;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipMenuController;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class PhonePipMenuController implements PipMenuController {
    public ParceledListSlice<RemoteAction> mAppActions;
    public SyncRtSurfaceTransactionApplier mApplier;
    public final Context mContext;
    public final ShellExecutor mMainExecutor;
    public final Handler mMainHandler;
    public ParceledListSlice<RemoteAction> mMediaActions;
    public final PipMediaController mMediaController;
    public int mMenuState;
    public final PipBoundsState mPipBoundsState;
    public PipMenuView mPipMenuView;
    public final PipUiEventLogger mPipUiEventLogger;
    public final Optional<SplitScreenController> mSplitScreenController;
    public final SystemWindows mSystemWindows;
    public final Matrix mMoveTransform = new Matrix();
    public final Rect mTmpSourceBounds = new Rect();
    public final RectF mTmpSourceRectF = new RectF();
    public final RectF mTmpDestinationRectF = new RectF();
    public final ArrayList<Listener> mListeners = new ArrayList<>();
    public AnonymousClass1 mMediaActionListener = new AnonymousClass1();
    public final float[] mTmpValues = new float[9];
    public final WifiEntry$$ExternalSyntheticLambda2 mUpdateEmbeddedMatrix = new WifiEntry$$ExternalSyntheticLambda2(this, 12);

    /* renamed from: com.android.wm.shell.pip.phone.PhonePipMenuController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements PipMediaController.ActionListener {
        public AnonymousClass1() {
        }

        @Override // com.android.wm.shell.pip.PipMediaController.ActionListener
        public final void onMediaActionsChanged(List<RemoteAction> list) {
            PhonePipMenuController.this.mMediaActions = new ParceledListSlice<>(list);
            PhonePipMenuController.this.updateMenuActions();
        }
    }

    /* loaded from: classes.dex */
    public interface Listener {
        void onEnterSplit();

        void onPipDismiss();

        void onPipExpand();

        void onPipMenuStateChangeFinish(int i);

        void onPipMenuStateChangeStart(int i, boolean z, Runnable runnable);

        void onPipShowMenu();
    }

    public final void hideMenu() {
        if (isMenuVisible()) {
            PipMenuView pipMenuView = this.mPipMenuView;
            Objects.requireNonNull(pipMenuView);
            pipMenuView.hideMenu(null, true, pipMenuView.mDidLastShowMenuResize, 1);
        }
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public final void attach(SurfaceControl surfaceControl) {
        PipMenuView pipMenuView = this.mPipMenuView;
        if (!(pipMenuView == null || pipMenuView == null)) {
            this.mApplier = null;
            SystemWindows systemWindows = this.mSystemWindows;
            Objects.requireNonNull(systemWindows);
            systemWindows.mViewRoots.remove(pipMenuView).release();
            this.mPipMenuView = null;
        }
        PipMenuView pipMenuView2 = new PipMenuView(this.mContext, this, this.mMainExecutor, this.mMainHandler, this.mSplitScreenController, this.mPipUiEventLogger);
        this.mPipMenuView = pipMenuView2;
        this.mSystemWindows.addView(pipMenuView2, PipMenuController.getPipMenuLayoutParams(0, 0), 0, 1);
        if (this.mMenuState != 0) {
            SystemWindows systemWindows2 = this.mSystemWindows;
            PipMenuView pipMenuView3 = this.mPipMenuView;
            Objects.requireNonNull(systemWindows2);
            SystemWindows.PerDisplay perDisplay = systemWindows2.mPerDisplay.get(0);
            if (perDisplay != null) {
                perDisplay.setShellRootAccessibilityWindow(1, pipMenuView3);
                return;
            }
            return;
        }
        SystemWindows systemWindows3 = this.mSystemWindows;
        Objects.requireNonNull(systemWindows3);
        SystemWindows.PerDisplay perDisplay2 = systemWindows3.mPerDisplay.get(0);
        if (perDisplay2 != null) {
            perDisplay2.setShellRootAccessibilityWindow(1, null);
        }
    }

    public final Size getEstimatedMinMenuSize() {
        PipMenuView pipMenuView = this.mPipMenuView;
        if (pipMenuView == null) {
            return null;
        }
        int max = Math.max(2, pipMenuView.mActions.size()) * pipMenuView.getResources().getDimensionPixelSize(2131166777);
        int dimensionPixelSize = pipMenuView.getResources().getDimensionPixelSize(2131166783);
        return new Size(max, pipMenuView.getResources().getDimensionPixelSize(2131166784) + pipMenuView.getResources().getDimensionPixelSize(2131166776) + dimensionPixelSize);
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public final boolean isMenuVisible() {
        if (this.mPipMenuView == null || this.mMenuState == 0) {
            return false;
        }
        return true;
    }

    public final boolean maybeCreateSyncApplier() {
        PipMenuView pipMenuView = this.mPipMenuView;
        if (pipMenuView == null || pipMenuView.getViewRootImpl() == null) {
            Log.v("PhonePipMenuController", "Not going to move PiP, either menu or its parent is not created.");
            return false;
        }
        if (this.mApplier == null) {
            this.mApplier = new SyncRtSurfaceTransactionApplier(this.mPipMenuView);
        }
        if (this.mApplier != null) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0046  */
    @Override // com.android.wm.shell.pip.PipMenuController
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onFocusTaskChanged(android.app.ActivityManager.RunningTaskInfo r7) {
        /*
            r6 = this;
            com.android.wm.shell.pip.phone.PipMenuView r6 = r6.mPipMenuView
            if (r6 == 0) goto L_0x005d
            java.util.Objects.requireNonNull(r6)
            java.util.Optional<com.android.wm.shell.splitscreen.SplitScreenController> r0 = r6.mSplitScreenControllerOptional
            boolean r0 = r0.isPresent()
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0048
            java.util.Optional<com.android.wm.shell.splitscreen.SplitScreenController> r0 = r6.mSplitScreenControllerOptional
            java.lang.Object r0 = r0.get()
            com.android.wm.shell.splitscreen.SplitScreenController r0 = (com.android.wm.shell.splitscreen.SplitScreenController) r0
            int r3 = r7.taskId
            java.util.Objects.requireNonNull(r0)
            boolean r4 = r0.isSplitScreenVisible()
            if (r4 == 0) goto L_0x0043
            com.android.wm.shell.splitscreen.StageCoordinator r0 = r0.mStageCoordinator
            java.util.Objects.requireNonNull(r0)
            com.android.wm.shell.splitscreen.MainStage r4 = r0.mMainStage
            boolean r4 = r4.containsTask(r3)
            r5 = -1
            if (r4 == 0) goto L_0x0034
            r0 = r1
            goto L_0x003f
        L_0x0034:
            com.android.wm.shell.splitscreen.SideStage r0 = r0.mSideStage
            boolean r0 = r0.containsTask(r3)
            if (r0 == 0) goto L_0x003e
            r0 = r2
            goto L_0x003f
        L_0x003e:
            r0 = r5
        L_0x003f:
            if (r0 == r5) goto L_0x0043
            r0 = r2
            goto L_0x0044
        L_0x0043:
            r0 = r1
        L_0x0044:
            if (r0 == 0) goto L_0x0048
            r0 = r2
            goto L_0x0049
        L_0x0048:
            r0 = r1
        L_0x0049:
            if (r0 != 0) goto L_0x005a
            int r0 = r7.getWindowingMode()
            if (r0 != r2) goto L_0x005b
            boolean r0 = r7.supportsSplitScreenMultiWindow
            if (r0 == 0) goto L_0x005b
            int r7 = r7.topActivityType
            r0 = 2
            if (r7 == r0) goto L_0x005b
        L_0x005a:
            r1 = r2
        L_0x005b:
            r6.mFocusedTaskAllowSplitScreen = r1
        L_0x005d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.phone.PhonePipMenuController.onFocusTaskChanged(android.app.ActivityManager$RunningTaskInfo):void");
    }

    public final void updateMenuActions() {
        boolean z;
        ParceledListSlice<RemoteAction> parceledListSlice;
        if (this.mPipMenuView != null) {
            ParceledListSlice<RemoteAction> parceledListSlice2 = this.mAppActions;
            if (parceledListSlice2 == null || parceledListSlice2.getList().size() <= 0) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                parceledListSlice = this.mAppActions;
            } else {
                parceledListSlice = this.mMediaActions;
            }
            if (parceledListSlice != null) {
                PipMenuView pipMenuView = this.mPipMenuView;
                Rect bounds = this.mPipBoundsState.getBounds();
                List list = parceledListSlice.getList();
                Objects.requireNonNull(pipMenuView);
                pipMenuView.mActions.clear();
                pipMenuView.mActions.addAll(list);
                int i = pipMenuView.mMenuState;
                if (i == 1) {
                    pipMenuView.updateActionViews(i, bounds);
                }
            }
        }
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public final void updateMenuBounds(Rect rect) {
        this.mSystemWindows.updateViewLayout(this.mPipMenuView, PipMenuController.getPipMenuLayoutParams(rect.width(), rect.height()));
        updateMenuLayout();
    }

    public PhonePipMenuController(Context context, PipBoundsState pipBoundsState, PipMediaController pipMediaController, SystemWindows systemWindows, Optional<SplitScreenController> optional, PipUiEventLogger pipUiEventLogger, ShellExecutor shellExecutor, Handler handler) {
        this.mContext = context;
        this.mPipBoundsState = pipBoundsState;
        this.mMediaController = pipMediaController;
        this.mSystemWindows = systemWindows;
        this.mMainExecutor = shellExecutor;
        this.mMainHandler = handler;
        this.mSplitScreenController = optional;
        this.mPipUiEventLogger = pipUiEventLogger;
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public final void detach() {
        hideMenu();
        PipMenuView pipMenuView = this.mPipMenuView;
        if (pipMenuView != null) {
            this.mApplier = null;
            SystemWindows systemWindows = this.mSystemWindows;
            Objects.requireNonNull(systemWindows);
            systemWindows.mViewRoots.remove(pipMenuView).release();
            this.mPipMenuView = null;
        }
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public final void movePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
        if (!rect.isEmpty() && maybeCreateSyncApplier()) {
            if (surfaceControl == null || transaction == null) {
                this.mTmpSourceBounds.set(0, 0, rect.width(), rect.height());
            } else {
                this.mPipMenuView.getBoundsOnScreen(this.mTmpSourceBounds);
            }
            this.mTmpSourceRectF.set(this.mTmpSourceBounds);
            this.mTmpDestinationRectF.set(rect);
            this.mMoveTransform.setRectToRect(this.mTmpSourceRectF, this.mTmpDestinationRectF, Matrix.ScaleToFit.FILL);
            SyncRtSurfaceTransactionApplier.SurfaceParams build = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(this.mSystemWindows.getViewSurface(this.mPipMenuView)).withMatrix(this.mMoveTransform).build();
            if (surfaceControl == null || transaction == null) {
                this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build});
            } else {
                this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build, new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(surfaceControl).withMergeTransaction(transaction).build()});
            }
            if (this.mPipMenuView.getViewRootImpl() != null) {
                this.mPipMenuView.getHandler().removeCallbacks(this.mUpdateEmbeddedMatrix);
                this.mPipMenuView.getHandler().post(this.mUpdateEmbeddedMatrix);
            }
        }
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public final void resizePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
        if (!rect.isEmpty() && maybeCreateSyncApplier()) {
            SyncRtSurfaceTransactionApplier.SurfaceParams build = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(this.mSystemWindows.getViewSurface(this.mPipMenuView)).withWindowCrop(rect).build();
            if (surfaceControl != null) {
                this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build, new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(surfaceControl).withMergeTransaction(transaction).build()});
                return;
            }
            this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build});
        }
    }

    public final void showMenu(int i, Rect rect, boolean z, boolean z2) {
        if (maybeCreateSyncApplier()) {
            movePipMenu(null, null, rect);
            updateMenuBounds(rect);
            this.mPipMenuView.showMenu(i, rect, z, z2, false);
        }
    }

    public final void updateMenuLayout() {
        if (isMenuVisible()) {
            PipMenuView pipMenuView = this.mPipMenuView;
            Objects.requireNonNull(pipMenuView);
            Objects.requireNonNull(pipMenuView.mPipMenuIconsAlgorithm);
        }
    }

    public final void hideMenu(int i) {
        if (isMenuVisible()) {
            PipMenuView pipMenuView = this.mPipMenuView;
            Objects.requireNonNull(pipMenuView);
            pipMenuView.hideMenu(null, true, false, i);
        }
    }
}
