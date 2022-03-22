package com.android.systemui.doze;

import android.opengl.EGL14;
import android.os.RemoteException;
import android.os.Trace;
import android.util.Log;
import android.view.ViewGroup;
import androidx.mediarouter.media.MediaRouter;
import com.android.keyguard.EmergencyButtonController;
import com.android.systemui.ImageWallpaper;
import com.android.systemui.glwallpaper.EglHelper;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.privacy.television.PrivacyChipDrawable;
import com.android.systemui.privacy.television.TvOngoingPrivacyChip;
import com.android.systemui.qs.tiles.dialog.InternetDialog;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.NotificationConversationInfo;
import com.android.systemui.volume.VolumeDialogImpl;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DozeUi$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ DozeUi$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ((DozeHost) this.f$0).dozeTimeTick();
                return;
            case 1:
                ((MediaRouter.PrepareTransferNotifier) this.f$0).finishTransfer();
                return;
            case 2:
                ((EmergencyButtonController) this.f$0).updateEmergencyCallButton();
                return;
            case 3:
                ImageWallpaper.GLEngine gLEngine = (ImageWallpaper.GLEngine) this.f$0;
                int i = ImageWallpaper.GLEngine.MIN_SURFACE_WIDTH;
                Objects.requireNonNull(gLEngine);
                Trace.beginSection("ImageWallpaper#finishRendering");
                EglHelper eglHelper = gLEngine.mEglHelper;
                if (eglHelper != null) {
                    eglHelper.destroyEglSurface();
                    EglHelper eglHelper2 = gLEngine.mEglHelper;
                    Objects.requireNonNull(eglHelper2);
                    if (eglHelper2.hasEglContext()) {
                        EGL14.eglDestroyContext(eglHelper2.mEglDisplay, eglHelper2.mEglContext);
                        eglHelper2.mEglContext = EGL14.EGL_NO_CONTEXT;
                    }
                }
                Trace.endSection();
                return;
            case 4:
                TvOngoingPrivacyChip tvOngoingPrivacyChip = (TvOngoingPrivacyChip) this.f$0;
                Objects.requireNonNull(tvOngoingPrivacyChip);
                if (tvOngoingPrivacyChip.mState == 2) {
                    tvOngoingPrivacyChip.mState = 3;
                    PrivacyChipDrawable privacyChipDrawable = tvOngoingPrivacyChip.mChipDrawable;
                    if (privacyChipDrawable != null && privacyChipDrawable.mIsExpanded) {
                        privacyChipDrawable.mIsExpanded = false;
                        privacyChipDrawable.animateToNewTargetWidth(privacyChipDrawable.mDotSize);
                        privacyChipDrawable.mCollapse.start();
                        privacyChipDrawable.mExpand.cancel();
                    }
                    tvOngoingPrivacyChip.animateIconAlphaTo(0.0f);
                    return;
                }
                return;
            case 5:
                InternetDialog internetDialog = (InternetDialog) this.f$0;
                boolean z = InternetDialog.DEBUG;
                Objects.requireNonNull(internetDialog);
                internetDialog.setProgressBarVisible(false);
                return;
            case FalsingManager.VERSION /* 6 */:
                OverviewProxyService overviewProxyService = (OverviewProxyService) this.f$0;
                Objects.requireNonNull(overviewProxyService);
                Log.w("OverviewProxyService", "Binder supposed established connection but actual connection to service timed out, trying again");
                overviewProxyService.retryConnectionWithBackoff();
                return;
            case 7:
                NotificationConversationInfo.UpdateChannelRunnable updateChannelRunnable = (NotificationConversationInfo.UpdateChannelRunnable) this.f$0;
                Objects.requireNonNull(updateChannelRunnable);
                BubblesManager bubblesManager = NotificationConversationInfo.this.mBubblesManagerOptional.get();
                NotificationEntry notificationEntry = NotificationConversationInfo.this.mEntry;
                Objects.requireNonNull(bubblesManager);
                Objects.requireNonNull(notificationEntry);
                if (notificationEntry.mBubbleMetadata != null) {
                    try {
                        bubblesManager.mBarService.onNotificationBubbleChanged(notificationEntry.mKey, true, 2);
                    } catch (RemoteException e) {
                        Log.e("Bubbles", e.getMessage());
                    }
                    bubblesManager.mShadeController.collapsePanel(true);
                    ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                    if (expandableNotificationRow != null) {
                        expandableNotificationRow.updateBubbleButton();
                        return;
                    }
                    return;
                }
                return;
            case 8:
                ((ExpandableView) this.f$0).removeFromTransientContainer();
                return;
            case 9:
                VolumeDialogImpl volumeDialogImpl = (VolumeDialogImpl) this.f$0;
                String str = VolumeDialogImpl.TAG;
                Objects.requireNonNull(volumeDialogImpl);
                volumeDialogImpl.mDialog.dismiss();
                if (volumeDialogImpl.mHasSeenODICaptionsTooltip && volumeDialogImpl.mODICaptionsTooltipView != null) {
                    ((ViewGroup) volumeDialogImpl.mDialog.findViewById(2131429207)).removeView(volumeDialogImpl.mODICaptionsTooltipView);
                    volumeDialogImpl.mODICaptionsTooltipView = null;
                }
                volumeDialogImpl.mIsAnimatingDismiss = false;
                volumeDialogImpl.hideRingerDrawer();
                return;
            default:
                PipTouchHandler.DefaultPipTouchGesture defaultPipTouchGesture = (PipTouchHandler.DefaultPipTouchGesture) this.f$0;
                Objects.requireNonNull(defaultPipTouchGesture);
                if (defaultPipTouchGesture.mShouldHideMenuAfterFling) {
                    PipTouchHandler.this.mMenuController.hideMenu();
                    return;
                }
                return;
        }
    }
}
