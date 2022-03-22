package com.android.wifitrackerlib;

import android.animation.AnimationHandler;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.hardware.biometrics.IBiometricSysuiReceiver;
import android.os.RemoteException;
import android.util.Log;
import android.util.MathUtils;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.settingslib.wifi.AccessPoint;
import com.android.systemui.biometrics.AuthContainerView;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import com.android.systemui.qs.external.CustomTile$$ExternalSyntheticLambda0;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.theme.ThemeOverlayController;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.pip.phone.PipMenuView;
import com.google.android.systemui.assist.uihints.NgaUiController;
import com.google.android.systemui.assist.uihints.ScrimController;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiEntry$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WifiEntry$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        boolean z2 = true;
        switch (this.$r8$classId) {
            case 0:
                Objects.requireNonNull((WifiEntry) this.f$0);
                return;
            case 1:
                int i = AccessPoint.$r8$clinit;
                Objects.requireNonNull((AccessPoint) this.f$0);
                return;
            case 2:
                AuthController authController = (AuthController) this.f$0;
                int i2 = AuthController.BiometricTaskStackListener.$r8$clinit;
                Objects.requireNonNull(authController);
                authController.mExecution.assertIsMainThread();
                AuthDialog authDialog = authController.mCurrentDialog;
                if (authDialog != null) {
                    try {
                        String str = ((AuthContainerView) authDialog).mConfig.mOpPackageName;
                        Log.w("AuthController", "Task stack changed, current client: " + str);
                        List tasks = authController.mActivityTaskManager.getTasks(1);
                        if (!tasks.isEmpty()) {
                            String packageName = ((ActivityManager.RunningTaskInfo) tasks.get(0)).topActivity.getPackageName();
                            if (!packageName.contentEquals(str)) {
                                if (authController.mContext.checkCallingOrSelfPermission("android.permission.USE_BIOMETRIC_INTERNAL") == 0) {
                                    z = true;
                                } else {
                                    z = false;
                                }
                                if (!z || !ThemeOverlayApplier.ANDROID_PACKAGE.equals(str)) {
                                    z2 = false;
                                }
                                if (!z2) {
                                    Log.w("AuthController", "Evicting client due to: " + packageName);
                                    AuthContainerView authContainerView = (AuthContainerView) authController.mCurrentDialog;
                                    Objects.requireNonNull(authContainerView);
                                    authContainerView.animateAway(false, 0);
                                    authController.mCurrentDialog = null;
                                    authController.mOrientationListener.disable();
                                    Iterator it = authController.mCallbacks.iterator();
                                    while (it.hasNext()) {
                                        ((AuthController.Callback) it.next()).onBiometricPromptDismissed();
                                    }
                                    IBiometricSysuiReceiver iBiometricSysuiReceiver = authController.mReceiver;
                                    if (iBiometricSysuiReceiver != null) {
                                        iBiometricSysuiReceiver.onDialogDismissed(3, (byte[]) null);
                                        authController.mReceiver = null;
                                        return;
                                    }
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    } catch (RemoteException e) {
                        Log.e("AuthController", "Remote exception", e);
                        return;
                    }
                } else {
                    return;
                }
            case 3:
                QRCodeScannerController qRCodeScannerController = (QRCodeScannerController) this.f$0;
                Objects.requireNonNull(qRCodeScannerController);
                qRCodeScannerController.updateQRCodeScannerActivityDetails();
                return;
            case 4:
                NotificationLockscreenUserManagerImpl notificationLockscreenUserManagerImpl = (NotificationLockscreenUserManagerImpl) this.f$0;
                Objects.requireNonNull(notificationLockscreenUserManagerImpl);
                Iterator it2 = notificationLockscreenUserManagerImpl.mListeners.iterator();
                while (it2.hasNext()) {
                    ((NotificationLockscreenUserManager.UserChangedListener) it2.next()).onCurrentProfilesChanged(notificationLockscreenUserManagerImpl.mCurrentProfiles);
                }
                return;
            case 5:
                boolean z3 = NetworkControllerImpl.DEBUG;
                ((NetworkControllerImpl) this.f$0).updateConnectivity();
                return;
            case FalsingManager.VERSION /* 6 */:
                AutoHideController autoHideController = (AutoHideController) this.f$0;
                Objects.requireNonNull(autoHideController);
                autoHideController.mStatusBar.synchronizeState();
                return;
            case 7:
                boolean z4 = PhoneStatusBarPolicy.DEBUG;
                ((PhoneStatusBarPolicy) this.f$0).updateVolumeZen();
                return;
            case 8:
                StatusBarNotificationActivityStarter statusBarNotificationActivityStarter = (StatusBarNotificationActivityStarter) this.f$0;
                Objects.requireNonNull(statusBarNotificationActivityStarter);
                try {
                    statusBarNotificationActivityStarter.mDreamManager.awaken();
                    return;
                } catch (RemoteException e2) {
                    e2.printStackTrace();
                    return;
                }
            case 9:
                ((UserSwitcherController) this.f$0).notifyAdapters();
                return;
            case 10:
                ThemeOverlayController themeOverlayController = (ThemeOverlayController) this.f$0;
                Objects.requireNonNull(themeOverlayController);
                WallpaperManager wallpaperManager = themeOverlayController.mWallpaperManager;
                int userId = themeOverlayController.mUserTracker.getUserId();
                int i3 = 2;
                if (themeOverlayController.mWallpaperManager.getWallpaperIdForUser(2, userId) <= themeOverlayController.mWallpaperManager.getWallpaperIdForUser(1, userId)) {
                    i3 = 1;
                }
                CustomTile$$ExternalSyntheticLambda0 customTile$$ExternalSyntheticLambda0 = new CustomTile$$ExternalSyntheticLambda0(themeOverlayController, wallpaperManager.getWallpaperColors(i3), 1);
                if (themeOverlayController.mDeviceProvisionedController.isCurrentUserSetup()) {
                    themeOverlayController.mMainExecutor.execute(customTile$$ExternalSyntheticLambda0);
                    return;
                } else {
                    customTile$$ExternalSyntheticLambda0.run();
                    return;
                }
            case QSTileImpl.H.STALE /* 11 */:
                ((AnimationHandler) this.f$0).setProvider(new SfVsyncFrameCallbackProvider());
                return;
            case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                PhonePipMenuController phonePipMenuController = (PhonePipMenuController) this.f$0;
                Objects.requireNonNull(phonePipMenuController);
                PipMenuView pipMenuView = phonePipMenuController.mPipMenuView;
                if (pipMenuView != null && pipMenuView.getViewRootImpl() != null) {
                    phonePipMenuController.mMoveTransform.getValues(phonePipMenuController.mTmpValues);
                    try {
                        phonePipMenuController.mPipMenuView.getViewRootImpl().getAccessibilityEmbeddedConnection().setScreenMatrix(phonePipMenuController.mTmpValues);
                        return;
                    } catch (RemoteException unused) {
                        return;
                    }
                } else {
                    return;
                }
            default:
                NgaUiController.AnonymousClass1 r7 = (NgaUiController.AnonymousClass1) this.f$0;
                int i4 = NgaUiController.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r7);
                NgaUiController ngaUiController = NgaUiController.this;
                Objects.requireNonNull(ngaUiController);
                ngaUiController.mInvocationInProgress = false;
                ngaUiController.mInvocationLightsView.hide();
                ngaUiController.mLastInvocationProgress = 0.0f;
                ScrimController scrimController = ngaUiController.mScrimController;
                Objects.requireNonNull(scrimController);
                float constrain = MathUtils.constrain(0.0f, 0.0f, 1.0f);
                if (scrimController.mInvocationProgress != constrain) {
                    scrimController.mInvocationProgress = constrain;
                    scrimController.refresh();
                }
                ngaUiController.refresh();
                return;
        }
    }
}
