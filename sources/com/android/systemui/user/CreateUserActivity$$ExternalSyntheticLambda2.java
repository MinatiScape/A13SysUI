package com.android.systemui.user;

import android.app.ActivityTaskManager;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ImageButton;
import androidx.mediarouter.media.MediaRoute2Provider;
import com.android.systemui.Prefs;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.DndTile$$ExternalSyntheticLambda0;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.phone.ScreenOffAnimation;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.volume.VolumeDialogImpl;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda23;
import com.android.wm.shell.legacysplitscreen.ForcedResizableInfoActivityController;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PipController;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.android.wm.shell.pip.phone.PipTouchState;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CreateUserActivity$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ CreateUserActivity$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ImageButton imageButton;
        switch (this.$r8$classId) {
            case 0:
                CreateUserActivity createUserActivity = (CreateUserActivity) this.f$0;
                int i = CreateUserActivity.$r8$clinit;
                Objects.requireNonNull(createUserActivity);
                Log.e("CreateUserActivity", "Unable to create user");
                if (!createUserActivity.isFinishing() && !createUserActivity.isDestroyed()) {
                    createUserActivity.finish();
                    return;
                }
                return;
            case 1:
                MediaRoute2Provider.GroupRouteController groupRouteController = (MediaRoute2Provider.GroupRouteController) this.f$0;
                Objects.requireNonNull(groupRouteController);
                groupRouteController.mOptimisticVolume = -1;
                return;
            case 2:
                ((OverviewProxyService) this.f$0).internalConnectToCurrentUser();
                return;
            case 3:
                PhoneStatusBarPolicy phoneStatusBarPolicy = (PhoneStatusBarPolicy) this.f$0;
                boolean z = PhoneStatusBarPolicy.DEBUG;
                Objects.requireNonNull(phoneStatusBarPolicy);
                try {
                    phoneStatusBarPolicy.mHandler.post(new BubbleStackView$$ExternalSyntheticLambda23(phoneStatusBarPolicy, phoneStatusBarPolicy.mUserManager.isManagedProfile(ActivityTaskManager.getService().getLastResumedActivityUserId()), 1));
                    return;
                } catch (RemoteException e) {
                    Log.w("PhoneStatusBarPolicy", "updateManagedProfile: ", e);
                    return;
                }
            case 4:
                StatusBar statusBar = (StatusBar) this.f$0;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                NotificationShadeWindowController notificationShadeWindowController = statusBar.mNotificationShadeWindowController;
                LightRevealScrim lightRevealScrim = statusBar.mLightRevealScrim;
                Objects.requireNonNull(lightRevealScrim);
                notificationShadeWindowController.setLightRevealScrimOpaque(lightRevealScrim.isScrimOpaque);
                ScreenOffAnimationController screenOffAnimationController = statusBar.mScreenOffAnimationController;
                LightRevealScrim lightRevealScrim2 = statusBar.mLightRevealScrim;
                Objects.requireNonNull(lightRevealScrim2);
                boolean z2 = lightRevealScrim2.isScrimOpaque;
                Objects.requireNonNull(screenOffAnimationController);
                Iterator it = screenOffAnimationController.animations.iterator();
                while (it.hasNext()) {
                    ((ScreenOffAnimation) it.next()).onScrimOpaqueChanged(z2);
                }
                return;
            case 5:
                VolumeDialogImpl volumeDialogImpl = (VolumeDialogImpl) this.f$0;
                String str = VolumeDialogImpl.TAG;
                Objects.requireNonNull(volumeDialogImpl);
                if (!Prefs.getBoolean(volumeDialogImpl.mContext, "TouchedRingerToggle") && (imageButton = volumeDialogImpl.mRingerIcon) != null) {
                    imageButton.postOnAnimationDelayed(new DndTile$$ExternalSyntheticLambda0(volumeDialogImpl, imageButton, 2), 1500L);
                    return;
                }
                return;
            case FalsingManager.VERSION /* 6 */:
                ((ForcedResizableInfoActivityController) this.f$0).showPending();
                return;
            case 7:
                PipController.PipImpl pipImpl = (PipController.PipImpl) this.f$0;
                Objects.requireNonNull(pipImpl);
                PipController pipController = PipController.this;
                Objects.requireNonNull(pipController);
                PipTouchHandler pipTouchHandler = pipController.mTouchHandler;
                Objects.requireNonNull(pipTouchHandler);
                PipTouchState pipTouchState = pipTouchHandler.mTouchState;
                Objects.requireNonNull(pipTouchState);
                if (!pipTouchState.mIsUserInteracting) {
                    pipTouchHandler.mMenuController.showMenu(1, pipTouchHandler.mPipBoundsState.getBounds(), false, pipTouchHandler.willResizeMenu());
                    return;
                }
                return;
            default:
                PipTouchHandler.DefaultPipTouchGesture defaultPipTouchGesture = (PipTouchHandler.DefaultPipTouchGesture) this.f$0;
                Objects.requireNonNull(defaultPipTouchGesture);
                if (PipTouchHandler.this.mPipBoundsState.getBounds().left < 0) {
                    PipBoundsState pipBoundsState = PipTouchHandler.this.mPipBoundsState;
                    Objects.requireNonNull(pipBoundsState);
                    if (pipBoundsState.mStashedState != 1) {
                        PipTouchHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_STASH_LEFT);
                        PipTouchHandler.this.mPipBoundsState.setStashed(1);
                        PipTouchHandler.this.mMenuController.hideMenu();
                        return;
                    }
                }
                if (PipTouchHandler.this.mPipBoundsState.getBounds().left >= 0) {
                    PipBoundsState pipBoundsState2 = PipTouchHandler.this.mPipBoundsState;
                    Objects.requireNonNull(pipBoundsState2);
                    if (pipBoundsState2.mStashedState != 2) {
                        PipTouchHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_STASH_RIGHT);
                        PipTouchHandler.this.mPipBoundsState.setStashed(2);
                    }
                }
                PipTouchHandler.this.mMenuController.hideMenu();
                return;
        }
    }
}
