package com.android.systemui.theme;

import android.os.UserHandle;
import android.view.SurfaceControl;
import android.view.animation.PathInterpolator;
import android.window.WindowContainerTransaction;
import com.android.internal.policy.DividerSnapAlgorithm;
import com.android.wm.shell.legacysplitscreen.DividerView;
import com.android.wm.shell.legacysplitscreen.ForcedResizableInfoActivityController;
import com.android.wm.shell.legacysplitscreen.LegacySplitDisplayLayout;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenTaskListener;
import com.android.wm.shell.legacysplitscreen.WindowManagerProxy;
import com.android.wm.shell.transition.Transitions;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ThemeOverlayApplier$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda1(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        boolean z;
        boolean z2;
        int i;
        int i2;
        switch (this.$r8$classId) {
            case 0:
                ThemeOverlayApplier themeOverlayApplier = (ThemeOverlayApplier) this.f$0;
                Objects.requireNonNull(themeOverlayApplier);
                ((List) this.f$1).addAll(themeOverlayApplier.mOverlayManager.getOverlayInfosForTarget((String) obj, UserHandle.SYSTEM));
                return;
            default:
                DividerView dividerView = (DividerView) this.f$0;
                DividerSnapAlgorithm.SnapTarget snapTarget = (DividerSnapAlgorithm.SnapTarget) this.f$1;
                PathInterpolator pathInterpolator = DividerView.IME_ADJUST_INTERPOLATOR;
                Objects.requireNonNull(dividerView);
                boolean z3 = dividerView.mIsInMinimizeInteraction;
                if (!((Boolean) obj).booleanValue() && !dividerView.mDockedStackMinimized && dividerView.mIsInMinimizeInteraction) {
                    dividerView.mIsInMinimizeInteraction = false;
                }
                int i3 = snapTarget.flag;
                if (i3 == 0) {
                    z = false;
                } else {
                    z = true;
                    if (i3 != 1 ? (i = dividerView.mDockSide) == 3 || i == 4 : (i2 = dividerView.mDockSide) == 1 || i2 == 2) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    WindowManagerProxy windowManagerProxy = dividerView.mWindowManagerProxy;
                    LegacySplitScreenTaskListener legacySplitScreenTaskListener = dividerView.mTiles;
                    LegacySplitDisplayLayout legacySplitDisplayLayout = dividerView.mSplitLayout;
                    Objects.requireNonNull(windowManagerProxy);
                    if (Transitions.ENABLE_SHELL_TRANSITIONS) {
                        legacySplitScreenTaskListener.mSplitScreenController.startDismissSplit(!z2, true);
                    } else {
                        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                        windowContainerTransaction.setLaunchRoot(legacySplitScreenTaskListener.mSecondary.token, (int[]) null, (int[]) null);
                        WindowManagerProxy.buildDismissSplit(windowContainerTransaction, legacySplitScreenTaskListener, legacySplitDisplayLayout, z2);
                        windowManagerProxy.mSyncTransactionQueue.queue(windowContainerTransaction);
                    }
                    SurfaceControl.Transaction transaction = dividerView.mTiles.getTransaction();
                    dividerView.setResizeDimLayer(transaction, true, 0.0f);
                    dividerView.setResizeDimLayer(transaction, false, 0.0f);
                    transaction.apply();
                    dividerView.mTiles.releaseTransaction(transaction);
                }
                Objects.requireNonNull(dividerView.mWindowManagerProxy);
                WindowManagerProxy.setResizing(false);
                dividerView.updateDockSide();
                dividerView.mCurrentAnimator = null;
                dividerView.mExitAnimationRunning = false;
                if (!z && !z3) {
                    WindowManagerProxy windowManagerProxy2 = dividerView.mWindowManagerProxy;
                    int i4 = snapTarget.position;
                    LegacySplitDisplayLayout legacySplitDisplayLayout2 = dividerView.mSplitLayout;
                    Objects.requireNonNull(windowManagerProxy2);
                    WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
                    legacySplitDisplayLayout2.resizeSplits(i4, windowContainerTransaction2);
                    windowManagerProxy2.mSyncTransactionQueue.queue(windowContainerTransaction2);
                }
                DividerView.DividerCallbacks dividerCallbacks = dividerView.mCallback;
                if (dividerCallbacks != null) {
                    ForcedResizableInfoActivityController forcedResizableInfoActivityController = (ForcedResizableInfoActivityController) dividerCallbacks;
                    forcedResizableInfoActivityController.mDividerDragging = false;
                    forcedResizableInfoActivityController.showPending();
                }
                if (!dividerView.mIsInMinimizeInteraction) {
                    if (snapTarget.position < 0) {
                        snapTarget = dividerView.mSplitLayout.getSnapAlgorithm().getMiddleTarget();
                    }
                    DividerSnapAlgorithm snapAlgorithm = dividerView.mSplitLayout.getSnapAlgorithm();
                    if (!(snapTarget.position == snapAlgorithm.getDismissEndTarget().position || snapTarget.position == snapAlgorithm.getDismissStartTarget().position)) {
                        dividerView.saveSnapTargetBeforeMinimized(snapTarget);
                    }
                }
                dividerView.notifySplitScreenBoundsChanged();
                return;
        }
    }
}
