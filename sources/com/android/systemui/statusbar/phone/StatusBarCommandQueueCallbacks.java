package com.android.systemui.statusbar.phone;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.InsetsState;
import android.view.InsetsVisibilities;
import android.widget.Toast;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.view.AppearanceRegion;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.assist.AssistDisclosure;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.dreams.DreamOverlayStateController$$ExternalSyntheticLambda7;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.ScreenPinningNotify;
import com.android.systemui.navigationbar.TaskbarDelegate;
import com.android.systemui.qs.QSPanelController;
import com.android.systemui.qs.QSPanelControllerBase;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.DisableFlagsLogger;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda4;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class StatusBarCommandQueueCallbacks implements CommandQueue.Callbacks {
    public static final VibrationAttributes HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(50);
    public final AssistManager mAssistManager;
    public final VibrationEffect mCameraLaunchGestureVibrationEffect;
    public final CommandQueue mCommandQueue;
    public final Context mContext;
    public final DeviceProvisionedController mDeviceProvisionedController;
    public final DisableFlagsLogger mDisableFlagsLogger;
    public final int mDisplayId;
    public final DozeServiceHost mDozeServiceHost;
    public final HeadsUpManager mHeadsUpManager;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final LightBarController mLightBarController;
    public final MetricsLogger mMetricsLogger;
    public final NotificationPanelViewController mNotificationPanelViewController;
    public final NotificationStackScrollLayoutController mNotificationStackScrollLayoutController;
    public final PowerManager mPowerManager;
    public final RemoteInputQuickSettingsDisabler mRemoteInputQuickSettingsDisabler;
    public final ShadeController mShadeController;
    public final Optional<LegacySplitScreen> mSplitScreenOptional;
    public final StatusBar mStatusBar;
    public final StatusBarHideIconsForBouncerManager mStatusBarHideIconsForBouncerManager;
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public final boolean mVibrateOnOpening;
    public final VibratorHelper mVibratorHelper;
    public final Optional<Vibrator> mVibratorOptional;
    public final WakefulnessLifecycle mWakefulnessLifecycle;

    public StatusBarCommandQueueCallbacks(StatusBar statusBar, Context context, Resources resources, ShadeController shadeController, CommandQueue commandQueue, NotificationPanelViewController notificationPanelViewController, Optional optional, RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler, MetricsLogger metricsLogger, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, HeadsUpManager headsUpManager, WakefulnessLifecycle wakefulnessLifecycle, DeviceProvisionedController deviceProvisionedController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, AssistManager assistManager, DozeServiceHost dozeServiceHost, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationStackScrollLayoutController notificationStackScrollLayoutController, StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, PowerManager powerManager, VibratorHelper vibratorHelper, Optional optional2, LightBarController lightBarController, DisableFlagsLogger disableFlagsLogger, int i) {
        VibrationEffect vibrationEffect;
        this.mStatusBar = statusBar;
        this.mContext = context;
        this.mShadeController = shadeController;
        this.mCommandQueue = commandQueue;
        this.mNotificationPanelViewController = notificationPanelViewController;
        this.mSplitScreenOptional = optional;
        this.mRemoteInputQuickSettingsDisabler = remoteInputQuickSettingsDisabler;
        this.mMetricsLogger = metricsLogger;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardStateController = keyguardStateController;
        this.mHeadsUpManager = headsUpManager;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mAssistManager = assistManager;
        this.mDozeServiceHost = dozeServiceHost;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mNotificationStackScrollLayoutController = notificationStackScrollLayoutController;
        this.mStatusBarHideIconsForBouncerManager = statusBarHideIconsForBouncerManager;
        this.mPowerManager = powerManager;
        this.mVibratorHelper = vibratorHelper;
        this.mVibratorOptional = optional2;
        this.mLightBarController = lightBarController;
        this.mDisableFlagsLogger = disableFlagsLogger;
        this.mDisplayId = i;
        this.mVibrateOnOpening = resources.getBoolean(2131034180);
        if (optional2.isPresent() && ((Vibrator) optional2.get()).areAllPrimitivesSupported(4, 1)) {
            vibrationEffect = VibrationEffect.startComposition().addPrimitive(4).addPrimitive(1, 1.0f, 50).compose();
        } else if (!optional2.isPresent() || !((Vibrator) optional2.get()).hasAmplitudeControl()) {
            int[] intArray = resources.getIntArray(2130903088);
            long[] jArr = new long[intArray.length];
            for (int i2 = 0; i2 < intArray.length; i2++) {
                jArr[i2] = intArray[i2];
            }
            vibrationEffect = VibrationEffect.createWaveform(jArr, -1);
        } else {
            vibrationEffect = VibrationEffect.createWaveform(StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS, StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_AMPLITUDES, -1);
        }
        this.mCameraLaunchGestureVibrationEffect = vibrationEffect;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void abortTransient(int i, int[] iArr) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 0)) {
            StatusBar statusBar = this.mStatusBar;
            Objects.requireNonNull(statusBar);
            if (statusBar.mTransientShown) {
                statusBar.mTransientShown = false;
                statusBar.maybeUpdateBarMode();
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void addQsTile(ComponentName componentName) {
        QSTileHost qSTileHost;
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        QSPanelController qSPanelController = statusBar.mQSPanelController;
        if (qSPanelController != null && (qSTileHost = qSPanelController.mHost) != null) {
            Objects.requireNonNull(qSTileHost);
            qSTileHost.addTile(componentName, false);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void animateCollapsePanels(int i, boolean z) {
        this.mShadeController.animateCollapsePanels(i, z, false, 1.0f);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void animateExpandNotificationsPanel() {
        if (this.mCommandQueue.panelsEnabled()) {
            NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController);
            if (notificationPanelViewController.mQsExpanded) {
                notificationPanelViewController.flingSettings(0.0f, 1, null, false);
            } else {
                notificationPanelViewController.expand(true);
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void animateExpandSettingsPanel(String str) {
        if (this.mCommandQueue.panelsEnabled() && this.mDeviceProvisionedController.isCurrentUserSetup()) {
            this.mNotificationPanelViewController.expandWithQs();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void appTransitionCancelled(int i) {
        if (i == this.mDisplayId) {
            this.mSplitScreenOptional.ifPresent(DreamOverlayStateController$$ExternalSyntheticLambda7.INSTANCE$1);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void appTransitionFinished(int i) {
        if (i == this.mDisplayId) {
            this.mSplitScreenOptional.ifPresent(StatusBarCommandQueueCallbacks$$ExternalSyntheticLambda0.INSTANCE);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void clickTile(ComponentName componentName) {
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        QSPanelController qSPanelController = statusBar.mQSPanelController;
        if (qSPanelController != null) {
            String spec = CustomTile.toSpec(componentName);
            Iterator<QSPanelControllerBase.TileRecord> it = qSPanelController.mRecords.iterator();
            while (it.hasNext()) {
                QSPanelControllerBase.TileRecord next = it.next();
                if (next.tile.getTileSpec().equals(spec)) {
                    next.tile.click(null);
                    return;
                }
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void disable(int i, int i2, int i3, boolean z) {
        int i4;
        boolean z2;
        if (i == this.mDisplayId) {
            RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler = this.mRemoteInputQuickSettingsDisabler;
            Objects.requireNonNull(remoteInputQuickSettingsDisabler);
            if (!remoteInputQuickSettingsDisabler.remoteInputActive || !remoteInputQuickSettingsDisabler.isLandscape || remoteInputQuickSettingsDisabler.shouldUseSplitNotificationShade) {
                i4 = i3;
            } else {
                i4 = i3 | 1;
            }
            DisableFlagsLogger disableFlagsLogger = this.mDisableFlagsLogger;
            StatusBar statusBar = this.mStatusBar;
            Objects.requireNonNull(statusBar);
            int i5 = statusBar.mDisabled1;
            StatusBar statusBar2 = this.mStatusBar;
            Objects.requireNonNull(statusBar2);
            Log.d("StatusBar", disableFlagsLogger.getDisableFlagsString(new DisableFlagsLogger.DisableState(i5, statusBar2.mDisabled2), new DisableFlagsLogger.DisableState(i2, i3), new DisableFlagsLogger.DisableState(i2, i4)));
            StatusBar statusBar3 = this.mStatusBar;
            Objects.requireNonNull(statusBar3);
            int i6 = statusBar3.mDisabled1 ^ i2;
            StatusBar statusBar4 = this.mStatusBar;
            Objects.requireNonNull(statusBar4);
            statusBar4.mDisabled1 = i2;
            StatusBar statusBar5 = this.mStatusBar;
            Objects.requireNonNull(statusBar5);
            int i7 = statusBar5.mDisabled2 ^ i4;
            StatusBar statusBar6 = this.mStatusBar;
            Objects.requireNonNull(statusBar6);
            statusBar6.mDisabled2 = i4;
            if (!((i6 & 65536) == 0 || (i2 & 65536) == 0)) {
                this.mShadeController.animateCollapsePanels();
            }
            if ((i6 & 262144) != 0) {
                StatusBar statusBar7 = this.mStatusBar;
                Objects.requireNonNull(statusBar7);
                if ((262144 & statusBar7.mDisabled1) != 0) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2) {
                    this.mHeadsUpManager.releaseAllImmediately();
                }
            }
            if ((i7 & 1) != 0) {
                this.mStatusBar.updateQsExpansionEnabled();
            }
            if ((i7 & 4) != 0) {
                this.mStatusBar.updateQsExpansionEnabled();
                if ((i4 & 4) != 0) {
                    this.mShadeController.animateCollapsePanels();
                }
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void dismissKeyboardShortcutsMenu() {
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        statusBar.mMessageRouter.cancelMessages(1027);
        statusBar.mMessageRouter.sendMessage(1027);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void handleSystemKey(int i) {
        if (this.mCommandQueue.panelsEnabled()) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            if (!keyguardUpdateMonitor.mDeviceInteractive) {
                return;
            }
            if ((this.mKeyguardStateController.isShowing() && !this.mKeyguardStateController.isOccluded()) || !this.mDeviceProvisionedController.isCurrentUserSetup()) {
                return;
            }
            if (280 == i) {
                this.mMetricsLogger.action(493);
                this.mNotificationPanelViewController.collapse(false, 1.0f);
            } else if (281 == i) {
                this.mMetricsLogger.action(494);
                if (this.mNotificationPanelViewController.isFullyCollapsed()) {
                    if (this.mVibrateOnOpening) {
                        this.mVibratorHelper.vibrate(2);
                    }
                    this.mNotificationPanelViewController.expand(true);
                    NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
                    Objects.requireNonNull(notificationStackScrollLayoutController);
                    NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
                    Objects.requireNonNull(notificationStackScrollLayout);
                    notificationStackScrollLayout.mWillExpand = true;
                    this.mHeadsUpManager.unpinAll();
                    this.mMetricsLogger.count("panel_open", 1);
                    return;
                }
                NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
                Objects.requireNonNull(notificationPanelViewController);
                if (!notificationPanelViewController.mQsExpanded) {
                    NotificationPanelViewController notificationPanelViewController2 = this.mNotificationPanelViewController;
                    Objects.requireNonNull(notificationPanelViewController2);
                    if (!notificationPanelViewController2.mIsExpanding) {
                        NotificationPanelViewController notificationPanelViewController3 = this.mNotificationPanelViewController;
                        Objects.requireNonNull(notificationPanelViewController3);
                        notificationPanelViewController3.flingSettings(0.0f, 0, null, false);
                        this.mMetricsLogger.count("panel_open_qs", 1);
                    }
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x005f, code lost:
        if (r2 == false) goto L_0x0061;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00e3, code lost:
        if (r0.mWakefulness == 1) goto L_0x00e5;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x006f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0070  */
    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onCameraLaunchGestureDetected(int r9) {
        /*
            Method dump skipped, instructions count: 271
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBarCommandQueueCallbacks.onCameraLaunchGestureDetected(int):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x0079, code lost:
        if (r0.mWakefulness == 1) goto L_0x007b;
     */
    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onEmergencyActionLaunchGestureDetected() {
        /*
            r9 = this;
            com.android.systemui.statusbar.phone.StatusBar r0 = r9.mStatusBar
            android.content.Intent r2 = r0.getEmergencyActionIntent()
            if (r2 != 0) goto L_0x0010
            java.lang.String r9 = "StatusBar"
            java.lang.String r0 = "Couldn't find an app to process the emergency intent."
            android.util.Log.wtf(r9, r0)
            return
        L_0x0010:
            com.android.systemui.keyguard.WakefulnessLifecycle r0 = r9.mWakefulnessLifecycle
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.mWakefulness
            r1 = 3
            r3 = 0
            r4 = 1
            if (r0 != r1) goto L_0x001e
            r0 = r4
            goto L_0x001f
        L_0x001e:
            r0 = r3
        L_0x001f:
            if (r0 == 0) goto L_0x0029
            com.android.systemui.statusbar.phone.StatusBar r9 = r9.mStatusBar
            java.util.Objects.requireNonNull(r9)
            r9.mLaunchEmergencyActionOnFinishedGoingToSleep = r4
            return
        L_0x0029:
            com.android.systemui.statusbar.phone.StatusBar r0 = r9.mStatusBar
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.mDeviceInteractive
            if (r0 != 0) goto L_0x003e
            android.os.PowerManager r0 = r9.mPowerManager
            long r5 = android.os.SystemClock.uptimeMillis()
            r1 = 4
            java.lang.String r7 = "com.android.systemui:EMERGENCY_GESTURE"
            r0.wakeUp(r5, r1, r7)
        L_0x003e:
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r0 = r9.mStatusBarKeyguardViewManager
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.mShowing
            if (r0 != 0) goto L_0x0053
            com.android.systemui.statusbar.phone.StatusBar r1 = r9.mStatusBar
            r3 = 0
            r4 = 1
            r5 = 1
            r6 = 0
            r7 = 0
            r8 = 0
            r1.startActivityDismissingKeyguard(r2, r3, r4, r5, r6, r7, r8)
            return
        L_0x0053:
            com.android.systemui.statusbar.phone.StatusBar r0 = r9.mStatusBar
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.mDeviceInteractive
            if (r0 != 0) goto L_0x0068
            com.android.systemui.statusbar.phone.StatusBar r0 = r9.mStatusBar
            java.util.Objects.requireNonNull(r0)
            android.os.PowerManager$WakeLock r0 = r0.mGestureWakeLock
            r5 = 6000(0x1770, double:2.9644E-320)
            r0.acquire(r5)
        L_0x0068:
            com.android.systemui.keyguard.WakefulnessLifecycle r0 = r9.mWakefulnessLifecycle
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.mWakefulness
            r1 = 2
            if (r0 == r1) goto L_0x007b
            com.android.systemui.keyguard.WakefulnessLifecycle r0 = r9.mWakefulnessLifecycle
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.mWakefulness
            if (r0 != r4) goto L_0x007c
        L_0x007b:
            r3 = r4
        L_0x007c:
            if (r3 == 0) goto L_0x0093
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r0 = r9.mStatusBarKeyguardViewManager
            boolean r0 = r0.isBouncerShowing()
            if (r0 == 0) goto L_0x008b
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r0 = r9.mStatusBarKeyguardViewManager
            r0.reset(r4)
        L_0x008b:
            android.content.Context r9 = r9.mContext
            android.os.UserHandle r0 = android.os.UserHandle.CURRENT
            r9.startActivityAsUser(r2, r0)
            return
        L_0x0093:
            com.android.systemui.statusbar.phone.StatusBar r9 = r9.mStatusBar
            java.util.Objects.requireNonNull(r9)
            r9.mLaunchEmergencyActionWhenFinishedWaking = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBarCommandQueueCallbacks.onEmergencyActionLaunchGestureDetected():void");
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void onRecentsAnimationStateChanged(boolean z) {
        this.mStatusBar.setInteracting(2, z);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, InsetsVisibilities insetsVisibilities, String str) {
        if (i == this.mDisplayId) {
            StatusBar statusBar = this.mStatusBar;
            Objects.requireNonNull(statusBar);
            boolean z2 = false;
            if (statusBar.mAppearance != i2) {
                statusBar.mAppearance = i2;
                int barMode = statusBar.barMode(statusBar.mTransientShown, i2);
                if (statusBar.mStatusBarMode != barMode) {
                    statusBar.mStatusBarMode = barMode;
                    statusBar.checkBarModes();
                    statusBar.mAutoHideController.touchAutoHide();
                    z2 = true;
                }
            }
            LightBarController lightBarController = this.mLightBarController;
            StatusBar statusBar2 = this.mStatusBar;
            Objects.requireNonNull(statusBar2);
            lightBarController.onStatusBarAppearanceChanged(appearanceRegionArr, z2, statusBar2.mStatusBarMode, z);
            StatusBar statusBar3 = this.mStatusBar;
            Objects.requireNonNull(statusBar3);
            statusBar3.mBubblesOptional.ifPresent(new PipController$$ExternalSyntheticLambda4(statusBar3, 2));
            this.mStatusBarStateController.setSystemBarAttributes(i2, i3, insetsVisibilities, str);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void remQsTile(ComponentName componentName) {
        QSTileHost qSTileHost;
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        QSPanelController qSPanelController = statusBar.mQSPanelController;
        if (qSPanelController != null && (qSTileHost = qSPanelController.mHost) != null) {
            ArrayList arrayList = new ArrayList(qSTileHost.mTileSpecs);
            arrayList.remove(CustomTile.toSpec(componentName));
            qSTileHost.changeTiles(qSTileHost.mTileSpecs, arrayList);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void setTopAppHidesStatusBar(boolean z) {
        StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager = this.mStatusBarHideIconsForBouncerManager;
        Objects.requireNonNull(statusBarHideIconsForBouncerManager);
        statusBarHideIconsForBouncerManager.topAppHidesStatusBar = z;
        if (!z && statusBarHideIconsForBouncerManager.wereIconsJustHidden) {
            statusBarHideIconsForBouncerManager.wereIconsJustHidden = false;
            statusBarHideIconsForBouncerManager.commandQueue.recomputeDisableFlags(statusBarHideIconsForBouncerManager.displayId, true);
        }
        statusBarHideIconsForBouncerManager.updateHideIconsForBouncer(true);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void showAssistDisclosure() {
        AssistManager assistManager = this.mAssistManager;
        Objects.requireNonNull(assistManager);
        AssistDisclosure assistDisclosure = assistManager.mAssistDisclosure;
        Objects.requireNonNull(assistDisclosure);
        assistDisclosure.mHandler.removeCallbacks(assistDisclosure.mShowRunnable);
        assistDisclosure.mHandler.post(assistDisclosure.mShowRunnable);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void showPinningEnterExitToast(boolean z) {
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        NavigationBarController navigationBarController = statusBar.mNavigationBarController;
        int i = statusBar.mDisplayId;
        Objects.requireNonNull(navigationBarController);
        NavigationBarView navigationBarView = navigationBarController.getNavigationBarView(i);
        if (navigationBarView != null) {
            if (z) {
                ScreenPinningNotify screenPinningNotify = navigationBarView.mScreenPinningNotify;
                Objects.requireNonNull(screenPinningNotify);
                Context context = screenPinningNotify.mContext;
                Toast.makeText(context, context.getString(2131953189), 1).show();
                return;
            }
            ScreenPinningNotify screenPinningNotify2 = navigationBarView.mScreenPinningNotify;
            Objects.requireNonNull(screenPinningNotify2);
            Context context2 = screenPinningNotify2.mContext;
            Toast.makeText(context2, context2.getString(2131953185), 1).show();
        } else if (i == 0) {
            TaskbarDelegate taskbarDelegate = navigationBarController.mTaskbarDelegate;
            Objects.requireNonNull(taskbarDelegate);
            if (taskbarDelegate.mInitialized) {
                navigationBarController.mTaskbarDelegate.showPinningEnterExitToast(z);
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void showPinningEscapeToast() {
        boolean z;
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        NavigationBarController navigationBarController = statusBar.mNavigationBarController;
        int i = statusBar.mDisplayId;
        Objects.requireNonNull(navigationBarController);
        NavigationBarView navigationBarView = navigationBarController.getNavigationBarView(i);
        if (navigationBarView != null) {
            ScreenPinningNotify screenPinningNotify = navigationBarView.mScreenPinningNotify;
            if (navigationBarView.mNavBarMode == 2) {
                z = true;
            } else {
                z = false;
            }
            screenPinningNotify.showEscapeToast(z, navigationBarView.isRecentsButtonVisible());
        } else if (i == 0) {
            TaskbarDelegate taskbarDelegate = navigationBarController.mTaskbarDelegate;
            Objects.requireNonNull(taskbarDelegate);
            if (taskbarDelegate.mInitialized) {
                navigationBarController.mTaskbarDelegate.showPinningEscapeToast();
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void showScreenPinningRequest(int i) {
        if (!this.mKeyguardStateController.isShowing()) {
            this.mStatusBar.showScreenPinningRequest(i, true);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void showTransient(int i, int[] iArr, boolean z) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 0)) {
            StatusBar statusBar = this.mStatusBar;
            Objects.requireNonNull(statusBar);
            if (!statusBar.mTransientShown) {
                statusBar.mTransientShown = true;
                statusBar.mNoAnimationOnNextBarModeChange = true;
                statusBar.maybeUpdateBarMode();
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void showWirelessChargingAnimation(int i) {
        this.mStatusBar.showWirelessChargingAnimation(i);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void startAssist(Bundle bundle) {
        this.mAssistManager.startAssist(bundle);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void suppressAmbientDisplay(boolean z) {
        DozeServiceHost dozeServiceHost = this.mDozeServiceHost;
        Objects.requireNonNull(dozeServiceHost);
        if (z != dozeServiceHost.mSuppressed) {
            dozeServiceHost.mSuppressed = z;
            dozeServiceHost.mDozeLog.traceDozingSuppressed(z);
            Iterator<DozeHost.Callback> it = dozeServiceHost.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onDozeSuppressedChanged(z);
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void toggleKeyboardShortcutsMenu(int i) {
        StatusBar statusBar = this.mStatusBar;
        StatusBar.KeyboardShortcutsMessage keyboardShortcutsMessage = new StatusBar.KeyboardShortcutsMessage(i);
        Objects.requireNonNull(statusBar);
        statusBar.mMessageRouter.cancelMessages(StatusBar.KeyboardShortcutsMessage.class);
        statusBar.mMessageRouter.sendMessage(keyboardShortcutsMessage);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void togglePanel() {
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        if (statusBar.mPanelExpanded) {
            this.mShadeController.animateCollapsePanels();
        } else {
            animateExpandNotificationsPanel();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void toggleSplitScreen() {
        this.mStatusBar.toggleSplitScreenMode(-1, -1);
    }
}
