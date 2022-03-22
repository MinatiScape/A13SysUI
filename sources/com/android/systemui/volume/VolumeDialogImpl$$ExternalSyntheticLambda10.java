package com.android.systemui.volume;

import android.animation.Animator;
import android.graphics.Rect;
import android.os.UserHandle;
import android.window.StartingWindowRemovalInfo;
import androidx.emoji2.text.FontRequestEmojiCompatConfig;
import com.android.keyguard.KeyguardVisibilityHelper;
import com.android.systemui.dreams.DreamOverlayContainerViewController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.android.systemui.statusbar.policy.LocationControllerImpl;
import com.android.wm.shell.animation.FloatProperties;
import com.android.wm.shell.animation.FloatProperties$Companion$RECT_Y$1;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PipDismissTargetHandler;
import com.android.wm.shell.pip.phone.PipMotionHelper;
import com.android.wm.shell.pip.tv.TvPipController;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.startingsurface.StartingSurfaceDrawer;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController$$ExternalSyntheticLambda1;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class VolumeDialogImpl$$ExternalSyntheticLambda10 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ VolumeDialogImpl$$ExternalSyntheticLambda10(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        PipBoundsState pipBoundsState;
        switch (this.$r8$classId) {
            case 0:
                VolumeDialogImpl volumeDialogImpl = (VolumeDialogImpl) this.f$0;
                Objects.requireNonNull(volumeDialogImpl);
                int[] locationOnScreen = volumeDialogImpl.mODICaptionsTooltipView.getLocationOnScreen();
                volumeDialogImpl.mODICaptionsTooltipView.setTranslationY((volumeDialogImpl.mODICaptionsIcon.getLocationOnScreen()[1] - locationOnScreen[1]) - ((volumeDialogImpl.mODICaptionsTooltipView.getHeight() - volumeDialogImpl.mODICaptionsIcon.getHeight()) / 2.0f));
                volumeDialogImpl.mODICaptionsTooltipView.animate().alpha(1.0f).setStartDelay(volumeDialogImpl.mDialogShowAnimationDurationMs).withEndAction(new SuggestController$$ExternalSyntheticLambda1(volumeDialogImpl, 7)).start();
                return;
            case 1:
                ((FontRequestEmojiCompatConfig.FontRequestMetadataLoader) this.f$0).loadInternal();
                return;
            case 2:
                KeyguardVisibilityHelper keyguardVisibilityHelper = (KeyguardVisibilityHelper) this.f$0;
                Objects.requireNonNull(keyguardVisibilityHelper);
                keyguardVisibilityHelper.mKeyguardViewVisibilityAnimating = false;
                keyguardVisibilityHelper.mView.setVisibility(4);
                return;
            case 3:
                DreamOverlayContainerViewController.$r8$lambda$Oxvj_GJUc06UJC_m7GrRwIKFrUA((DreamOverlayContainerViewController) this.f$0);
                return;
            case 4:
                ((Animator) this.f$0).start();
                return;
            case 5:
                ((NetworkControllerImpl) this.f$0).recalculateEmergency();
                return;
            case FalsingManager.VERSION /* 6 */:
                ((LocationControllerImpl) this.f$0).areActiveLocationRequests();
                return;
            case 7:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                int i = BubbleStackView.FLYOUT_HIDE_AFTER;
                Objects.requireNonNull(bubbleStackView);
                bubbleStackView.animateFlyoutCollapsed(true, 0.0f);
                return;
            case 8:
                PipDismissTargetHandler.AnonymousClass1 r9 = (PipDismissTargetHandler.AnonymousClass1) this.f$0;
                Objects.requireNonNull(r9);
                PipMotionHelper pipMotionHelper = PipDismissTargetHandler.this.mMotionHelper;
                Objects.requireNonNull(pipMotionHelper);
                pipMotionHelper.mDismissalPending = true;
                PipMotionHelper pipMotionHelper2 = PipDismissTargetHandler.this.mMotionHelper;
                Objects.requireNonNull(pipMotionHelper2);
                PhysicsAnimator<Rect> physicsAnimator = pipMotionHelper2.mTemporaryBoundsPhysicsAnimator;
                FloatProperties$Companion$RECT_Y$1 floatProperties$Companion$RECT_Y$1 = FloatProperties.RECT_Y;
                Objects.requireNonNull(pipMotionHelper2.mPipBoundsState);
                physicsAnimator.spring(floatProperties$Companion$RECT_Y$1, (pipMotionHelper2.getBounds().height() * 2) + pipBoundsState.mMovementBounds.bottom, 0.0f, pipMotionHelper2.mSpringConfig);
                physicsAnimator.withEndActions(new QSTileImpl$$ExternalSyntheticLambda0(pipMotionHelper2, 10));
                pipMotionHelper2.startBoundsAnimator(pipMotionHelper2.getBounds().left, pipMotionHelper2.getBounds().height() + pipMotionHelper2.getBounds().bottom, null);
                pipMotionHelper2.mDismissalPending = false;
                PipDismissTargetHandler.this.hideDismissTargetMaybe();
                PipDismissTargetHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_DRAG_TO_REMOVE);
                return;
            case 9:
                TvPipController.TvPipImpl tvPipImpl = (TvPipController.TvPipImpl) this.f$0;
                Objects.requireNonNull(tvPipImpl);
                TvPipController tvPipController = TvPipController.this;
                Objects.requireNonNull(tvPipController);
                PipMediaController pipMediaController = tvPipController.mPipMediaController;
                Objects.requireNonNull(pipMediaController);
                pipMediaController.mMediaSessionManager.removeOnActiveSessionsChangedListener(pipMediaController.mSessionsChangedListener);
                pipMediaController.mMediaSessionManager.addOnActiveSessionsChangedListener(null, UserHandle.CURRENT, pipMediaController.mHandlerExecutor, pipMediaController.mSessionsChangedListener);
                return;
            default:
                StartingWindowController startingWindowController = (StartingWindowController) this.f$0;
                Objects.requireNonNull(startingWindowController);
                StartingSurfaceDrawer startingSurfaceDrawer = startingWindowController.mStartingSurfaceDrawer;
                Objects.requireNonNull(startingSurfaceDrawer);
                if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, -976436888, 0, null, null);
                }
                int size = startingSurfaceDrawer.mStartingWindowRecords.size();
                int[] iArr = new int[size];
                int i2 = size - 1;
                for (int i3 = i2; i3 >= 0; i3--) {
                    iArr[i3] = startingSurfaceDrawer.mStartingWindowRecords.keyAt(i3);
                }
                while (i2 >= 0) {
                    int i4 = iArr[i2];
                    StartingWindowRemovalInfo startingWindowRemovalInfo = startingSurfaceDrawer.mTmpRemovalInfo;
                    startingWindowRemovalInfo.taskId = i4;
                    startingSurfaceDrawer.removeWindowSynced(startingWindowRemovalInfo, true);
                    i2--;
                }
                synchronized (startingWindowController.mTaskBackgroundColors) {
                    startingWindowController.mTaskBackgroundColors.clear();
                }
                return;
        }
    }
}
