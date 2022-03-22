package com.google.android.systemui.statusbar.phone;

import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.StatusBarManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.devicestate.DeviceStateManager;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.fragment.R$id;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.keyguard.ViewMediatorCallback;
import com.android.settingslib.graph.ThemedBatteryDrawable;
import com.android.systemui.AutoReinflateContainer;
import com.android.systemui.Dependency;
import com.android.systemui.InitController;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda4;
import com.android.systemui.biometrics.UdfpsKeyguardViewController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.camera.CameraIntents;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dagger.SysUIComponent$$ExternalSyntheticLambda3;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.DozeScreenBrightness$$ExternalSyntheticOutline0;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.recents.ScreenPinningRequest;
import com.android.systemui.scrim.ScrimView;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.AlertingNotificationManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.charging.WiredChargingRippleController;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.BarTransitions;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.DozeScrimController;
import com.android.systemui.statusbar.phone.DozeServiceHost;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.KeyguardIndicationTextView;
import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import com.android.systemui.statusbar.phone.KeyguardStatusBarViewController;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.statusbar.phone.PanelViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager;
import com.android.systemui.statusbar.phone.dagger.StatusBarComponent;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.util.WallpaperController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.MessageRouter;
import com.android.systemui.volume.VolumeComponent;
import com.android.systemui.wallet.ui.WalletView$$ExternalSyntheticLambda0;
import com.google.android.systemui.NotificationLockscreenUserManagerGoogle;
import com.google.android.systemui.ambientmusic.AmbientIndicationContainer;
import com.google.android.systemui.ambientmusic.AmbientIndicationService;
import com.google.android.systemui.dreamliner.DockAlignmentController;
import com.google.android.systemui.dreamliner.DockIndicationController;
import com.google.android.systemui.dreamliner.DockObserver;
import com.google.android.systemui.dreamliner.WirelessCharger;
import com.google.android.systemui.reversecharging.ReverseChargingViewController;
import com.google.android.systemui.smartspace.SmartSpaceController;
import com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyClient;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class StatusBarGoogle extends StatusBar {
    public static final boolean DEBUG = Log.isLoggable("StatusBarGoogle", 3);
    public final AlarmManager mAlarmManager;
    public long mAnimStartTime;
    public final AnonymousClass1 mBatteryStateChangeCallback = new BatteryController.BatteryStateChangeCallback() { // from class: com.google.android.systemui.statusbar.phone.StatusBarGoogle.1
        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public final void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            StatusBarGoogle statusBarGoogle = StatusBarGoogle.this;
            statusBarGoogle.mReceivingBatteryLevel = i;
            if (!statusBarGoogle.mBatteryController.isWirelessCharging()) {
                long uptimeMillis = SystemClock.uptimeMillis();
                StatusBarGoogle statusBarGoogle2 = StatusBarGoogle.this;
                if (uptimeMillis - statusBarGoogle2.mAnimStartTime > 1500) {
                    statusBarGoogle2.mChargingAnimShown = false;
                }
                statusBarGoogle2.mReverseChargingAnimShown = false;
            }
            if (StatusBarGoogle.DEBUG) {
                StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("onBatteryLevelChanged(): level=", i, ",wlc=");
                m.append(StatusBarGoogle.this.mBatteryController.isWirelessCharging() ? 1 : 0);
                m.append(",wlcs=");
                m.append(StatusBarGoogle.this.mChargingAnimShown);
                m.append(",rtxs=");
                m.append(StatusBarGoogle.this.mReverseChargingAnimShown);
                m.append(",this=");
                m.append(this);
                Log.d("StatusBarGoogle", m.toString());
            }
        }

        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public final void onReverseChanged(boolean z, int i, String str) {
            long j;
            if (!z && i >= 0 && !TextUtils.isEmpty(str)) {
                StatusBarGoogle statusBarGoogle = StatusBarGoogle.this;
                boolean z2 = StatusBarGoogle.DEBUG;
                if (statusBarGoogle.mBatteryController.isWirelessCharging()) {
                    StatusBarGoogle statusBarGoogle2 = StatusBarGoogle.this;
                    if (statusBarGoogle2.mChargingAnimShown && !statusBarGoogle2.mReverseChargingAnimShown) {
                        statusBarGoogle2.mReverseChargingAnimShown = true;
                        long uptimeMillis = SystemClock.uptimeMillis();
                        StatusBarGoogle statusBarGoogle3 = StatusBarGoogle.this;
                        long j2 = uptimeMillis - statusBarGoogle3.mAnimStartTime;
                        if (j2 > 1500) {
                            j = 0;
                        } else {
                            j = 1500 - j2;
                        }
                        statusBarGoogle3.showChargingAnimation(statusBarGoogle3.mReceivingBatteryLevel, i, j);
                    }
                }
            }
            if (StatusBarGoogle.DEBUG) {
                StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("onReverseChanged(): rtx=", z ? 1 : 0, ",rxlevel=");
                m.append(StatusBarGoogle.this.mReceivingBatteryLevel);
                m.append(",level=");
                m.append(i);
                m.append(",name=");
                m.append(str);
                m.append(",wlc=");
                m.append(StatusBarGoogle.this.mBatteryController.isWirelessCharging() ? 1 : 0);
                m.append(",wlcs=");
                m.append(StatusBarGoogle.this.mChargingAnimShown);
                m.append(",rtxs=");
                m.append(StatusBarGoogle.this.mReverseChargingAnimShown);
                m.append(",this=");
                m.append(this);
                Log.d("StatusBarGoogle", m.toString());
            }
        }
    };
    public boolean mChargingAnimShown;
    public final KeyguardIndicationControllerGoogle mKeyguardIndicationController;
    public int mReceivingBatteryLevel;
    public boolean mReverseChargingAnimShown;
    public Optional<ReverseChargingViewController> mReverseChargingViewControllerOptional;
    public final SmartSpaceController mSmartSpaceController;
    public SysuiStatusBarStateController mStatusBarStateController;
    public final Lazy<Optional<NotificationVoiceReplyClient>> mVoiceReplyClient;
    public WallpaperNotifier mWallpaperNotifier;

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.systemui.statusbar.phone.StatusBarGoogle$1] */
    public StatusBarGoogle(SmartSpaceController smartSpaceController, WallpaperNotifier wallpaperNotifier, Optional optional, Context context, NotificationsController notificationsController, FragmentService fragmentService, LightBarController lightBarController, AutoHideController autoHideController, StatusBarWindowController statusBarWindowController, StatusBarWindowStateController statusBarWindowStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, StatusBarSignalPolicy statusBarSignalPolicy, PulseExpansionHandler pulseExpansionHandler, NotificationWakeUpCoordinator notificationWakeUpCoordinator, KeyguardBypassController keyguardBypassController, KeyguardStateController keyguardStateController, HeadsUpManagerPhone headsUpManagerPhone, DynamicPrivacyController dynamicPrivacyController, FalsingManager falsingManager, FalsingCollector falsingCollector, BroadcastDispatcher broadcastDispatcher, NotifShadeEventSource notifShadeEventSource, NotificationEntryManager notificationEntryManager, NotificationGutsManager notificationGutsManager, NotificationLogger notificationLogger, NotificationInterruptStateProvider notificationInterruptStateProvider, NotificationViewHierarchyManager notificationViewHierarchyManager, PanelExpansionStateManager panelExpansionStateManager, KeyguardViewMediator keyguardViewMediator, DisplayMetrics displayMetrics, MetricsLogger metricsLogger, Executor executor, NotificationMediaManager notificationMediaManager, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationRemoteInputManager notificationRemoteInputManager, UserSwitcherController userSwitcherController, NetworkController networkController, BatteryController batteryController, SysuiColorExtractor sysuiColorExtractor, ScreenLifecycle screenLifecycle, WakefulnessLifecycle wakefulnessLifecycle, SysuiStatusBarStateController sysuiStatusBarStateController, Optional optional2, VisualStabilityManager visualStabilityManager, DeviceProvisionedController deviceProvisionedController, NavigationBarController navigationBarController, AccessibilityFloatingMenuController accessibilityFloatingMenuController, Lazy lazy, ConfigurationController configurationController, NotificationShadeWindowController notificationShadeWindowController, DozeParameters dozeParameters, ScrimController scrimController, Lazy lazy2, LockscreenGestureLogger lockscreenGestureLogger, Lazy lazy3, Lazy lazy4, DozeServiceHost dozeServiceHost, PowerManager powerManager, ScreenPinningRequest screenPinningRequest, DozeScrimController dozeScrimController, VolumeComponent volumeComponent, CommandQueue commandQueue, StatusBarComponent.Factory factory, PluginManager pluginManager, Optional optional3, StatusBarNotificationActivityStarter.Builder builder, ShadeController shadeController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, ViewMediatorCallback viewMediatorCallback, InitController initController, Handler handler, PluginDependencyProvider pluginDependencyProvider, KeyguardDismissUtil keyguardDismissUtil, ExtensionController extensionController, UserInfoControllerImpl userInfoControllerImpl, PhoneStatusBarPolicy phoneStatusBarPolicy, KeyguardIndicationControllerGoogle keyguardIndicationControllerGoogle, DemoModeController demoModeController, StatusBarTouchableRegionManager statusBarTouchableRegionManager, NotificationIconAreaController notificationIconAreaController, BrightnessSliderController.Factory factory2, ScreenOffAnimationController screenOffAnimationController, WallpaperController wallpaperController, OngoingCallController ongoingCallController, StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, FeatureFlags featureFlags, Lazy lazy5, KeyguardUnlockAnimationController keyguardUnlockAnimationController, DelayableExecutor delayableExecutor, MessageRouter messageRouter, WallpaperManager wallpaperManager, Optional optional4, ActivityLaunchAnimator activityLaunchAnimator, AlarmManager alarmManager, NotifPipelineFlags notifPipelineFlags, InteractionJankMonitor interactionJankMonitor, DeviceStateManager deviceStateManager, DreamOverlayStateController dreamOverlayStateController, WiredChargingRippleController wiredChargingRippleController) {
        super(context, notificationsController, fragmentService, lightBarController, autoHideController, statusBarWindowController, statusBarWindowStateController, keyguardUpdateMonitor, statusBarSignalPolicy, pulseExpansionHandler, notificationWakeUpCoordinator, keyguardBypassController, keyguardStateController, headsUpManagerPhone, dynamicPrivacyController, falsingManager, falsingCollector, broadcastDispatcher, notifShadeEventSource, notificationEntryManager, notificationGutsManager, notificationLogger, notificationInterruptStateProvider, notificationViewHierarchyManager, panelExpansionStateManager, keyguardViewMediator, displayMetrics, metricsLogger, executor, notificationMediaManager, notificationLockscreenUserManager, notificationRemoteInputManager, userSwitcherController, networkController, batteryController, sysuiColorExtractor, screenLifecycle, wakefulnessLifecycle, sysuiStatusBarStateController, optional2, visualStabilityManager, deviceProvisionedController, navigationBarController, accessibilityFloatingMenuController, lazy, configurationController, notificationShadeWindowController, dozeParameters, scrimController, lazy2, lockscreenGestureLogger, lazy3, dozeServiceHost, powerManager, screenPinningRequest, dozeScrimController, volumeComponent, commandQueue, factory, pluginManager, optional3, builder, shadeController, statusBarKeyguardViewManager, viewMediatorCallback, initController, handler, pluginDependencyProvider, keyguardDismissUtil, extensionController, userInfoControllerImpl, phoneStatusBarPolicy, keyguardIndicationControllerGoogle, demoModeController, lazy4, statusBarTouchableRegionManager, notificationIconAreaController, factory2, screenOffAnimationController, wallpaperController, ongoingCallController, statusBarHideIconsForBouncerManager, lockscreenShadeTransitionController, featureFlags, keyguardUnlockAnimationController, delayableExecutor, messageRouter, wallpaperManager, optional4, activityLaunchAnimator, notifPipelineFlags, interactionJankMonitor, deviceStateManager, dreamOverlayStateController, wiredChargingRippleController);
        this.mSmartSpaceController = smartSpaceController;
        this.mWallpaperNotifier = wallpaperNotifier;
        this.mReverseChargingViewControllerOptional = optional;
        this.mVoiceReplyClient = lazy5;
        this.mKeyguardIndicationController = keyguardIndicationControllerGoogle;
        this.mAlarmManager = alarmManager;
        Objects.requireNonNull(keyguardIndicationControllerGoogle);
        this.mStatusBarStateController = sysuiStatusBarStateController;
    }

    @Override // com.android.systemui.statusbar.phone.StatusBar
    public final void showWirelessChargingAnimation(int i) {
        if (DEBUG) {
            Log.d("StatusBarGoogle", "showWirelessChargingAnimation()");
        }
        this.mChargingAnimShown = true;
        super.showWirelessChargingAnimation(i);
        this.mAnimStartTime = SystemClock.uptimeMillis();
    }

    @Override // com.android.systemui.statusbar.phone.StatusBar, com.android.systemui.CoreStartable, com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        boolean z;
        CharSequence charSequence;
        boolean z2;
        String str2;
        String str3;
        String str4;
        Rect rect;
        String str5;
        CharSequence charSequence2;
        PrintWriter asIndenting = R$id.asIndenting(printWriter);
        synchronized (this.mQueueLock) {
            asIndenting.println("Current Status Bar state:");
            asIndenting.println("  mExpandedVisible=" + this.mExpandedVisible);
            asIndenting.println("  mDisplayMetrics=" + this.mDisplayMetrics);
            asIndenting.println("  mStackScroller: " + StatusBar.viewInfo(this.mStackScroller));
            asIndenting.println("  mStackScroller: " + StatusBar.viewInfo(this.mStackScroller) + " scroll " + this.mStackScroller.getScrollX() + "," + this.mStackScroller.getScrollY());
        }
        asIndenting.print("  mInteractingWindows=");
        asIndenting.println(this.mInteractingWindows);
        asIndenting.print("  mStatusBarWindowState=");
        asIndenting.println(StatusBarManager.windowStateToString(this.mStatusBarWindowState));
        asIndenting.print("  mStatusBarMode=");
        asIndenting.println(BarTransitions.modeToString(this.mStatusBarMode));
        asIndenting.print("  mDozing=");
        asIndenting.println(this.mDozing);
        asIndenting.print("  mWallpaperSupported= ");
        asIndenting.println(this.mWallpaperSupported);
        asIndenting.println("  ShadeWindowView: ");
        NotificationShadeWindowViewController notificationShadeWindowViewController = this.mNotificationShadeWindowViewController;
        if (notificationShadeWindowViewController != null) {
            asIndenting.print("  mExpandAnimationRunning=");
            asIndenting.println(notificationShadeWindowViewController.mExpandAnimationRunning);
            asIndenting.print("  mTouchCancelled=");
            asIndenting.println(notificationShadeWindowViewController.mTouchCancelled);
            asIndenting.print("  mTouchActive=");
            asIndenting.println(notificationShadeWindowViewController.mTouchActive);
            StatusBar.dumpBarTransitions(asIndenting, "PhoneStatusBarTransitions", this.mStatusBarTransitions);
        }
        asIndenting.println("  mMediaManager: ");
        NotificationMediaManager notificationMediaManager = this.mMediaManager;
        if (notificationMediaManager != null) {
            asIndenting.print("    mMediaNotificationKey=");
            asIndenting.println(notificationMediaManager.mMediaNotificationKey);
            asIndenting.print("    mMediaController=");
            asIndenting.print(notificationMediaManager.mMediaController);
            if (notificationMediaManager.mMediaController != null) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(" state=");
                m.append(notificationMediaManager.mMediaController.getPlaybackState());
                asIndenting.print(m.toString());
            }
            asIndenting.println();
            asIndenting.print("    mMediaMetadata=");
            asIndenting.print(notificationMediaManager.mMediaMetadata);
            if (notificationMediaManager.mMediaMetadata != null) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m(" title=");
                m2.append((Object) notificationMediaManager.mMediaMetadata.getText("android.media.metadata.TITLE"));
                asIndenting.print(m2.toString());
            }
            asIndenting.println();
        }
        asIndenting.println("  Panels: ");
        String str6 = null;
        if (this.mNotificationPanelViewController != null) {
            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("    mNotificationPanel=");
            NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController);
            m3.append(((PanelViewController) notificationPanelViewController).mView);
            m3.append(" params=");
            NotificationPanelViewController notificationPanelViewController2 = this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController2);
            m3.append(((PanelViewController) notificationPanelViewController2).mView.getLayoutParams().debug(""));
            asIndenting.println(m3.toString());
            asIndenting.print("      ");
            NotificationPanelViewController notificationPanelViewController3 = this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController3);
            Object[] objArr = new Object[8];
            objArr[0] = "NotificationPanelViewController";
            objArr[1] = Float.valueOf(notificationPanelViewController3.mExpandedHeight);
            objArr[2] = Integer.valueOf(notificationPanelViewController3.getMaxPanelHeight());
            String str7 = "T";
            if (notificationPanelViewController3.mClosing) {
                str2 = str7;
            } else {
                str2 = "f";
            }
            objArr[3] = str2;
            if (notificationPanelViewController3.mTracking) {
                str3 = str7;
            } else {
                str3 = "f";
            }
            objArr[4] = str3;
            ValueAnimator valueAnimator = notificationPanelViewController3.mHeightAnimator;
            objArr[5] = valueAnimator;
            if (valueAnimator == null || !valueAnimator.isStarted()) {
                str4 = "";
            } else {
                str4 = " (started)";
            }
            objArr[6] = str4;
            if (!notificationPanelViewController3.mTouchDisabled) {
                str7 = "f";
            }
            objArr[7] = str7;
            asIndenting.println(String.format("[PanelView(%s): expandedHeight=%f maxPanelHeight=%d closing=%s tracking=%s timeAnim=%s%s touchDisabled=%s]", objArr));
            StringBuilder sb = new StringBuilder();
            sb.append("    gestureExclusionRect: ");
            Region calculateTouchableRegion = notificationPanelViewController3.mStatusBarTouchableRegionManager.calculateTouchableRegion();
            if (!notificationPanelViewController3.isFullyCollapsed() || calculateTouchableRegion == null) {
                rect = null;
            } else {
                rect = calculateTouchableRegion.getBounds();
            }
            if (rect == null) {
                rect = NotificationPanelViewController.EMPTY_RECT;
            }
            sb.append(rect);
            sb.append(" applyQSClippingImmediately: top(");
            sb.append(notificationPanelViewController3.mQsClipTop);
            sb.append(") bottom(");
            sb.append(notificationPanelViewController3.mQsClipBottom);
            sb.append(") qsVisible(");
            sb.append(notificationPanelViewController3.mQsVisible);
            asIndenting.println(sb.toString());
            KeyguardStatusBarViewController keyguardStatusBarViewController = notificationPanelViewController3.mKeyguardStatusBarViewController;
            if (keyguardStatusBarViewController != null) {
                asIndenting.println("KeyguardStatusBarView:");
                asIndenting.println("  mBatteryListening: " + keyguardStatusBarViewController.mBatteryListening);
                KeyguardStatusBarView keyguardStatusBarView = (KeyguardStatusBarView) keyguardStatusBarViewController.mView;
                Objects.requireNonNull(keyguardStatusBarView);
                asIndenting.println("KeyguardStatusBarView:");
                StringBuilder sb2 = new StringBuilder();
                sb2.append("  mBatteryCharging: ");
                StringBuilder m4 = DozeScreenBrightness$$ExternalSyntheticOutline0.m(sb2, keyguardStatusBarView.mBatteryCharging, asIndenting, "  mLayoutState: ");
                m4.append(keyguardStatusBarView.mLayoutState);
                asIndenting.println(m4.toString());
                asIndenting.println("  mKeyguardUserSwitcherEnabled: " + keyguardStatusBarView.mKeyguardUserSwitcherEnabled);
                BatteryMeterView batteryMeterView = keyguardStatusBarView.mBatteryView;
                if (batteryMeterView != null) {
                    if (batteryMeterView.mDrawable == null) {
                        str5 = null;
                    } else {
                        StringBuilder sb3 = new StringBuilder();
                        ThemedBatteryDrawable themedBatteryDrawable = batteryMeterView.mDrawable;
                        Objects.requireNonNull(themedBatteryDrawable);
                        sb3.append(themedBatteryDrawable.powerSaveEnabled);
                        sb3.append("");
                        str5 = sb3.toString();
                    }
                    TextView textView = batteryMeterView.mBatteryPercentView;
                    if (textView == null) {
                        charSequence2 = null;
                    } else {
                        charSequence2 = textView.getText();
                    }
                    asIndenting.println("  BatteryMeterView:");
                    asIndenting.println("    mDrawable.getPowerSave: " + str5);
                    asIndenting.println("    mBatteryPercentView.getText(): " + ((Object) charSequence2));
                    asIndenting.println("    mTextColor: #" + Integer.toHexString(batteryMeterView.mTextColor));
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("    mBatteryStateUnknown: ");
                    StringBuilder m5 = DozeScreenBrightness$$ExternalSyntheticOutline0.m(sb4, batteryMeterView.mBatteryStateUnknown, asIndenting, "    mLevel: ");
                    m5.append(batteryMeterView.mLevel);
                    asIndenting.println(m5.toString());
                    asIndenting.println("    mMode: " + batteryMeterView.mShowPercentMode);
                }
            }
        }
        asIndenting.println("  mStackScroller: ");
        if (this.mStackScroller != null) {
            asIndenting.increaseIndent();
            asIndenting.increaseIndent();
            this.mStackScroller.dump(fileDescriptor, asIndenting, strArr);
            asIndenting.decreaseIndent();
            asIndenting.decreaseIndent();
        }
        asIndenting.println("  Theme:");
        if (this.mUiModeManager == null) {
            str = "null";
        } else {
            str = this.mUiModeManager.getNightMode() + "";
        }
        asIndenting.println("    dark theme: " + str + " (auto: 0, yes: 2, no: 1)");
        if (this.mContext.getThemeResId() == 2132018190) {
            z = true;
        } else {
            z = false;
        }
        asIndenting.println("    light wallpaper theme: " + z);
        KeyguardIndicationController keyguardIndicationController = super.mKeyguardIndicationController;
        if (keyguardIndicationController != null) {
            asIndenting.println("KeyguardIndicationController:");
            asIndenting.println("  mInitialTextColorState: " + keyguardIndicationController.mInitialTextColorState);
            StringBuilder sb5 = new StringBuilder();
            sb5.append("  mPowerPluggedInWired: ");
            StringBuilder m6 = DozeScreenBrightness$$ExternalSyntheticOutline0.m(DozeScreenBrightness$$ExternalSyntheticOutline0.m(DozeScreenBrightness$$ExternalSyntheticOutline0.m(sb5, keyguardIndicationController.mPowerPluggedInWired, asIndenting, "  mPowerPluggedIn: "), keyguardIndicationController.mPowerPluggedIn, asIndenting, "  mPowerCharged: "), keyguardIndicationController.mPowerCharged, asIndenting, "  mChargingSpeed: ");
            m6.append(keyguardIndicationController.mChargingSpeed);
            asIndenting.println(m6.toString());
            asIndenting.println("  mChargingWattage: " + keyguardIndicationController.mChargingWattage);
            asIndenting.println("  mMessageToShowOnScreenOn: " + keyguardIndicationController.mMessageToShowOnScreenOn);
            StringBuilder sb6 = new StringBuilder();
            sb6.append("  mDozing: ");
            StringBuilder m7 = DozeScreenBrightness$$ExternalSyntheticOutline0.m(sb6, keyguardIndicationController.mDozing, asIndenting, "  mTransientIndication: ");
            m7.append((Object) keyguardIndicationController.mTransientIndication);
            asIndenting.println(m7.toString());
            asIndenting.println("  mBiometricMessage: " + ((Object) keyguardIndicationController.mBiometricMessage));
            asIndenting.println("  mBatteryLevel: " + keyguardIndicationController.mBatteryLevel);
            StringBuilder sb7 = new StringBuilder();
            sb7.append("  mBatteryPresent: ");
            StringBuilder m8 = DozeScreenBrightness$$ExternalSyntheticOutline0.m(sb7, keyguardIndicationController.mBatteryPresent, asIndenting, "  mTextView.getText(): ");
            KeyguardIndicationTextView keyguardIndicationTextView = keyguardIndicationController.mTopIndicationView;
            if (keyguardIndicationTextView == null) {
                charSequence = null;
            } else {
                charSequence = keyguardIndicationTextView.getText();
            }
            m8.append((Object) charSequence);
            asIndenting.println(m8.toString());
            asIndenting.println("  computePowerIndication(): " + keyguardIndicationController.computePowerIndication());
            asIndenting.println("  trustGrantedIndication: " + keyguardIndicationController.getTrustGrantedIndication());
            KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = keyguardIndicationController.mRotateTextViewController;
            Objects.requireNonNull(keyguardIndicationRotateTextViewController);
            asIndenting.println("KeyguardIndicationRotatingTextViewController:");
            asIndenting.println("    currentMessage=" + ((Object) ((KeyguardIndicationTextView) keyguardIndicationRotateTextViewController.mView).getText()));
            StringBuilder sb8 = new StringBuilder();
            sb8.append("    dozing:");
            StringBuilder m9 = DozeScreenBrightness$$ExternalSyntheticOutline0.m(sb8, keyguardIndicationRotateTextViewController.mIsDozing, asIndenting, "    queue:");
            m9.append(keyguardIndicationRotateTextViewController.mIndicationQueue.toString());
            asIndenting.println(m9.toString());
            asIndenting.println("    showNextIndicationRunnable:" + keyguardIndicationRotateTextViewController.mShowNextIndicationRunnable);
            if (keyguardIndicationRotateTextViewController.mIndicationMessages.keySet().size() > 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                asIndenting.println("    All messages:");
                for (Integer num : keyguardIndicationRotateTextViewController.mIndicationMessages.keySet()) {
                    int intValue = num.intValue();
                    StringBuilder m10 = ExifInterface$$ExternalSyntheticOutline0.m("        type=", intValue, " ");
                    m10.append(keyguardIndicationRotateTextViewController.mIndicationMessages.get(Integer.valueOf(intValue)));
                    asIndenting.println(m10.toString());
                }
            }
        }
        ScrimController scrimController = this.mScrimController;
        if (scrimController != null) {
            asIndenting.println(" ScrimController: ");
            asIndenting.print("  state: ");
            asIndenting.println(scrimController.mState);
            asIndenting.println("    mClipQsScrim = " + scrimController.mState.mClipQsScrim);
            asIndenting.print("  frontScrim:");
            asIndenting.print(" viewAlpha=");
            ScrimView scrimView = scrimController.mScrimInFront;
            Objects.requireNonNull(scrimView);
            asIndenting.print(scrimView.mViewAlpha);
            asIndenting.print(" alpha=");
            asIndenting.print(scrimController.mInFrontAlpha);
            asIndenting.print(" tint=0x");
            ScrimView scrimView2 = scrimController.mScrimInFront;
            Objects.requireNonNull(scrimView2);
            asIndenting.println(Integer.toHexString(scrimView2.mTintColor));
            asIndenting.print("  behindScrim:");
            asIndenting.print(" viewAlpha=");
            ScrimView scrimView3 = scrimController.mScrimBehind;
            Objects.requireNonNull(scrimView3);
            asIndenting.print(scrimView3.mViewAlpha);
            asIndenting.print(" alpha=");
            asIndenting.print(scrimController.mBehindAlpha);
            asIndenting.print(" tint=0x");
            ScrimView scrimView4 = scrimController.mScrimBehind;
            Objects.requireNonNull(scrimView4);
            asIndenting.println(Integer.toHexString(scrimView4.mTintColor));
            asIndenting.print("  notificationsScrim:");
            asIndenting.print(" viewAlpha=");
            ScrimView scrimView5 = scrimController.mNotificationsScrim;
            Objects.requireNonNull(scrimView5);
            asIndenting.print(scrimView5.mViewAlpha);
            asIndenting.print(" alpha=");
            asIndenting.print(scrimController.mNotificationsAlpha);
            asIndenting.print(" tint=0x");
            ScrimView scrimView6 = scrimController.mNotificationsScrim;
            Objects.requireNonNull(scrimView6);
            asIndenting.println(Integer.toHexString(scrimView6.mTintColor));
            asIndenting.print("  mTracking=");
            asIndenting.println(scrimController.mTracking);
            asIndenting.print("  mDefaultScrimAlpha=");
            asIndenting.println(scrimController.mDefaultScrimAlpha);
            asIndenting.print("  mPanelExpansionFraction=");
            asIndenting.println(scrimController.mPanelExpansionFraction);
            asIndenting.print("  mExpansionAffectsAlpha=");
            asIndenting.println(scrimController.mExpansionAffectsAlpha);
            asIndenting.print("  mState.getMaxLightRevealScrimAlpha=");
            asIndenting.println(scrimController.mState.getMaxLightRevealScrimAlpha());
        }
        if (this.mLightRevealScrim != null) {
            StringBuilder m11 = VendorAtomValue$$ExternalSyntheticOutline1.m("mLightRevealScrim.getRevealEffect(): ");
            LightRevealScrim lightRevealScrim = this.mLightRevealScrim;
            Objects.requireNonNull(lightRevealScrim);
            m11.append(lightRevealScrim.revealEffect);
            asIndenting.println(m11.toString());
            StringBuilder sb9 = new StringBuilder();
            sb9.append("mLightRevealScrim.getRevealAmount(): ");
            LightRevealScrim lightRevealScrim2 = this.mLightRevealScrim;
            Objects.requireNonNull(lightRevealScrim2);
            sb9.append(lightRevealScrim2.revealAmount);
            asIndenting.println(sb9.toString());
        }
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        if (statusBarKeyguardViewManager != null) {
            asIndenting.println("StatusBarKeyguardViewManager:");
            StringBuilder sb10 = new StringBuilder();
            sb10.append("  mShowing: ");
            StringBuilder m12 = DozeScreenBrightness$$ExternalSyntheticOutline0.m(DozeScreenBrightness$$ExternalSyntheticOutline0.m(DozeScreenBrightness$$ExternalSyntheticOutline0.m(DozeScreenBrightness$$ExternalSyntheticOutline0.m(sb10, statusBarKeyguardViewManager.mShowing, asIndenting, "  mOccluded: "), statusBarKeyguardViewManager.mOccluded, asIndenting, "  mRemoteInputActive: "), statusBarKeyguardViewManager.mRemoteInputActive, asIndenting, "  mDozing: "), statusBarKeyguardViewManager.mDozing, asIndenting, "  mAfterKeyguardGoneAction: ");
            m12.append(statusBarKeyguardViewManager.mAfterKeyguardGoneAction);
            asIndenting.println(m12.toString());
            asIndenting.println("  mAfterKeyguardGoneRunnables: " + statusBarKeyguardViewManager.mAfterKeyguardGoneRunnables);
            asIndenting.println("  mPendingWakeupAction: " + statusBarKeyguardViewManager.mPendingWakeupAction);
            KeyguardBouncer keyguardBouncer = statusBarKeyguardViewManager.mBouncer;
            if (keyguardBouncer != null) {
                asIndenting.println("KeyguardBouncer");
                asIndenting.println("  isShowing(): " + keyguardBouncer.isShowing());
                asIndenting.println("  mStatusBarHeight: " + keyguardBouncer.mStatusBarHeight);
                asIndenting.println("  mExpansion: " + keyguardBouncer.mExpansion);
                asIndenting.println("  mKeyguardViewController; " + keyguardBouncer.mKeyguardViewController);
                StringBuilder sb11 = new StringBuilder();
                sb11.append("  mShowingSoon: ");
                StringBuilder m13 = DozeScreenBrightness$$ExternalSyntheticOutline0.m(sb11, keyguardBouncer.mShowingSoon, asIndenting, "  mBouncerPromptReason: ");
                m13.append(keyguardBouncer.mBouncerPromptReason);
                asIndenting.println(m13.toString());
                StringBuilder sb12 = new StringBuilder();
                sb12.append("  mIsAnimatingAway: ");
                StringBuilder m14 = DozeScreenBrightness$$ExternalSyntheticOutline0.m(sb12, keyguardBouncer.mIsAnimatingAway, asIndenting, "  mInitialized: ");
                m14.append(keyguardBouncer.mInitialized);
                asIndenting.println(m14.toString());
            }
            if (statusBarKeyguardViewManager.mAlternateAuthInterceptor != null) {
                asIndenting.println("AltAuthInterceptor: ");
                UdfpsKeyguardViewController.AnonymousClass2 r1 = (UdfpsKeyguardViewController.AnonymousClass2) statusBarKeyguardViewManager.mAlternateAuthInterceptor;
                Objects.requireNonNull(r1);
                Objects.requireNonNull(UdfpsKeyguardViewController.this);
                asIndenting.println("UdfpsKeyguardViewController");
            }
        }
        this.mNotificationsController.dump(asIndenting);
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        if (headsUpManagerPhone != null) {
            asIndenting.println("HeadsUpManagerPhone state:");
            asIndenting.print("  mTouchAcceptanceDelay=");
            asIndenting.println(headsUpManagerPhone.mTouchAcceptanceDelay);
            asIndenting.print("  mSnoozeLengthMs=");
            asIndenting.println(headsUpManagerPhone.mSnoozeLengthMs);
            asIndenting.print("  now=");
            Objects.requireNonNull(headsUpManagerPhone.mClock);
            asIndenting.println(SystemClock.elapsedRealtime());
            asIndenting.print("  mUser=");
            asIndenting.println(headsUpManagerPhone.mUser);
            for (AlertingNotificationManager.AlertEntry alertEntry : headsUpManagerPhone.mAlertEntries.values()) {
                asIndenting.print("  HeadsUpEntry=");
                asIndenting.println(alertEntry.mEntry);
            }
            int size = headsUpManagerPhone.mSnoozedPackages.size();
            asIndenting.println("  snoozed packages: " + size);
            for (int i = 0; i < size; i++) {
                asIndenting.print("    ");
                asIndenting.print(headsUpManagerPhone.mSnoozedPackages.valueAt(i));
                asIndenting.print(", ");
                asIndenting.println(headsUpManagerPhone.mSnoozedPackages.keyAt(i));
            }
            asIndenting.print("  mBarState=");
            asIndenting.println(headsUpManagerPhone.mStatusBarState);
            asIndenting.print("  mTouchableRegion=");
            asIndenting.println(headsUpManagerPhone.mTouchableRegion);
        } else {
            asIndenting.println("  mHeadsUpManager: null");
        }
        StatusBarTouchableRegionManager statusBarTouchableRegionManager = this.mStatusBarTouchableRegionManager;
        if (statusBarTouchableRegionManager != null) {
            asIndenting.println("StatusBarTouchableRegionManager state:");
            asIndenting.print("  mTouchableRegion=");
            asIndenting.println(statusBarTouchableRegionManager.mTouchableRegion);
        } else {
            asIndenting.println("  mStatusBarTouchableRegionManager: null");
        }
        LightBarController lightBarController = this.mLightBarController;
        if (lightBarController != null) {
            lightBarController.dump(fileDescriptor, asIndenting, strArr);
        }
        asIndenting.println("SharedPreferences:");
        Context context = this.mContext;
        for (Map.Entry<String, ?> entry : context.getSharedPreferences(context.getPackageName(), 0).getAll().entrySet()) {
            asIndenting.print("  ");
            asIndenting.print(entry.getKey());
            asIndenting.print("=");
            asIndenting.println(entry.getValue());
        }
        asIndenting.println("Camera gesture intents:");
        asIndenting.println("   Insecure camera: " + CameraIntents.getInsecureCameraIntent(this.mContext));
        StringBuilder sb13 = new StringBuilder();
        sb13.append("   Secure camera: ");
        Context context2 = this.mContext;
        Intent intent = new Intent("android.media.action.STILL_IMAGE_CAMERA_SECURE");
        String string = context2.getResources().getString(2131952131);
        if (string == null || TextUtils.isEmpty(string)) {
            string = null;
        }
        if (string != null) {
            intent.setPackage(string);
        }
        sb13.append(intent.addFlags(8388608));
        asIndenting.println(sb13.toString());
        StringBuilder sb14 = new StringBuilder();
        sb14.append("   Override package: ");
        String string2 = this.mContext.getResources().getString(2131952131);
        if (string2 != null && !TextUtils.isEmpty(string2)) {
            str6 = string2;
        }
        sb14.append(str6);
        asIndenting.println(sb14.toString());
        SmartSpaceController smartSpaceController = this.mSmartSpaceController;
        Objects.requireNonNull(smartSpaceController);
        printWriter.println();
        printWriter.println("SmartspaceController");
        StringBuilder sb15 = new StringBuilder();
        sb15.append("  initial broadcast: ");
        StringBuilder m15 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb15, smartSpaceController.mSmartSpaceEnabledBroadcastSent, printWriter, "  weather ");
        m15.append(smartSpaceController.mData.mWeatherCard);
        printWriter.println(m15.toString());
        printWriter.println("  current " + smartSpaceController.mData.mCurrentCard);
        printWriter.println("serialized:");
        printWriter.println("  weather " + smartSpaceController.loadSmartSpaceData(false));
        printWriter.println("  current " + smartSpaceController.loadSmartSpaceData(true));
        printWriter.println("disabled by experiment: " + smartSpaceController.isSmartSpaceDisabledByExperiments());
    }

    @Override // com.android.systemui.statusbar.phone.StatusBar
    public final void setLockscreenUser(int i) {
        super.setLockscreenUser(i);
        SmartSpaceController smartSpaceController = this.mSmartSpaceController;
        Objects.requireNonNull(smartSpaceController);
        smartSpaceController.mData.mCurrentCard = smartSpaceController.loadSmartSpaceData(true);
        smartSpaceController.mData.mWeatherCard = smartSpaceController.loadSmartSpaceData(false);
        smartSpaceController.update();
    }

    @Override // com.android.systemui.statusbar.phone.StatusBar, com.android.systemui.CoreStartable
    public final void start() {
        super.start();
        this.mBatteryController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) this.mBatteryStateChangeCallback);
        ((NotificationLockscreenUserManagerGoogle) Dependency.get(NotificationLockscreenUserManager.class)).updateSmartSpaceVisibilitySettings();
        if (!this.mContext.getResources().getBoolean(2131034171)) {
            DockObserver dockObserver = (DockObserver) Dependency.get(DockManager.class);
            Objects.requireNonNull(dockObserver);
            dockObserver.mDreamlinerGear = (ImageView) this.mNotificationShadeWindowView.findViewById(2131427885);
            dockObserver.mPhotoPreview = (FrameLayout) this.mNotificationShadeWindowView.findViewById(2131428583);
            DockIndicationController dockIndicationController = new DockIndicationController(this.mContext, this.mKeyguardIndicationController, this.mStatusBarStateController, this);
            dockObserver.mIndicationController = dockIndicationController;
            dockObserver.mConfigurationController.addCallback(dockIndicationController);
            DockAlignmentController dockAlignmentController = dockObserver.mDockAlignmentController;
            Objects.requireNonNull(dockAlignmentController);
            WirelessCharger wirelessCharger = dockAlignmentController.mWirelessCharger;
            if (wirelessCharger == null) {
                Log.w("DockAlignmentController", "wirelessCharger is null");
            } else {
                wirelessCharger.registerAlignInfo(new DockAlignmentController.RegisterAlignInfoListener());
            }
        }
        if (this.mReverseChargingViewControllerOptional.isPresent()) {
            ReverseChargingViewController reverseChargingViewController = this.mReverseChargingViewControllerOptional.get();
            Objects.requireNonNull(reverseChargingViewController);
            reverseChargingViewController.mBatteryController.observe((Lifecycle) reverseChargingViewController.mLifecycle, (LifecycleRegistry) reverseChargingViewController);
            LifecycleRegistry lifecycleRegistry = reverseChargingViewController.mLifecycle;
            Lifecycle.State state = Lifecycle.State.RESUMED;
            Objects.requireNonNull(lifecycleRegistry);
            lifecycleRegistry.enforceMainThreadIfNeeded("markState");
            lifecycleRegistry.setCurrentState(state);
            StatusBar statusBar = reverseChargingViewController.mStatusBarLazy.get();
            Objects.requireNonNull(statusBar);
            reverseChargingViewController.mAmbientIndicationContainer = (AmbientIndicationContainer) statusBar.mNotificationShadeWindowView.findViewById(2131427484);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
            reverseChargingViewController.mBroadcastDispatcher.registerReceiver(reverseChargingViewController, intentFilter);
        }
        WallpaperNotifier wallpaperNotifier = this.mWallpaperNotifier;
        Objects.requireNonNull(wallpaperNotifier);
        wallpaperNotifier.mNotifCollection.addCollectionListener(wallpaperNotifier.mNotifListener);
        wallpaperNotifier.mContext.registerReceiver(wallpaperNotifier.mWallpaperChangedReceiver, new IntentFilter("android.intent.action.WALLPAPER_CHANGED"));
        wallpaperNotifier.checkNotificationBroadcastSupport();
        this.mVoiceReplyClient.get().ifPresent(SysUIComponent$$ExternalSyntheticLambda3.INSTANCE$2);
        final AmbientIndicationContainer ambientIndicationContainer = (AmbientIndicationContainer) this.mNotificationShadeWindowView.findViewById(2131427484);
        Objects.requireNonNull(ambientIndicationContainer);
        ambientIndicationContainer.mStatusBar = this;
        AutoReinflateContainer.InflateListener ambientIndicationContainer$$ExternalSyntheticLambda1 = new AutoReinflateContainer.InflateListener() { // from class: com.google.android.systemui.ambientmusic.AmbientIndicationContainer$$ExternalSyntheticLambda1
            @Override // com.android.systemui.AutoReinflateContainer.InflateListener
            public final void onInflated() {
                AmbientIndicationContainer ambientIndicationContainer2 = AmbientIndicationContainer.this;
                int i = AmbientIndicationContainer.$r8$clinit;
                Objects.requireNonNull(ambientIndicationContainer2);
                ambientIndicationContainer2.mTextView = (TextView) ambientIndicationContainer2.findViewById(2131427486);
                ambientIndicationContainer2.mIconView = (ImageView) ambientIndicationContainer2.findViewById(2131427485);
                ambientIndicationContainer2.mAmbientMusicAnimation = null;
                ambientIndicationContainer2.mAmbientMusicNoteIcon = null;
                ambientIndicationContainer2.mReverseChargingAnimation = null;
                ambientIndicationContainer2.mTextColor = ambientIndicationContainer2.mTextView.getCurrentTextColor();
                ambientIndicationContainer2.mAmbientIndicationIconSize = ambientIndicationContainer2.getResources().getDimensionPixelSize(2131165323);
                ambientIndicationContainer2.mAmbientMusicNoteIconIconSize = ambientIndicationContainer2.getResources().getDimensionPixelSize(2131165325);
                ambientIndicationContainer2.getResources().getDimensionPixelSize(2131165601);
                ambientIndicationContainer2.mTextView.setEnabled(!ambientIndicationContainer2.mDozing);
                ambientIndicationContainer2.updateColors();
                ambientIndicationContainer2.updatePill();
                ambientIndicationContainer2.mTextView.setOnClickListener(new AuthBiometricView$$ExternalSyntheticLambda4(ambientIndicationContainer2, 1));
                ambientIndicationContainer2.mIconView.setOnClickListener(new WalletView$$ExternalSyntheticLambda0(ambientIndicationContainer2, 3));
            }
        };
        ambientIndicationContainer.mInflateListeners.add(ambientIndicationContainer$$ExternalSyntheticLambda1);
        ambientIndicationContainer.getChildAt(0);
        ambientIndicationContainer$$ExternalSyntheticLambda1.onInflated();
        ambientIndicationContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.google.android.systemui.ambientmusic.AmbientIndicationContainer$$ExternalSyntheticLambda0
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                AmbientIndicationContainer ambientIndicationContainer2 = AmbientIndicationContainer.this;
                int i9 = AmbientIndicationContainer.$r8$clinit;
                Objects.requireNonNull(ambientIndicationContainer2);
                ambientIndicationContainer2.updateBottomSpacing();
            }
        });
        AmbientIndicationService ambientIndicationService = new AmbientIndicationService(this.mContext, ambientIndicationContainer, this.mAlarmManager);
        if (!ambientIndicationService.mStarted) {
            ambientIndicationService.mStarted = true;
            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction("com.google.android.ambientindication.action.AMBIENT_INDICATION_SHOW");
            intentFilter2.addAction("com.google.android.ambientindication.action.AMBIENT_INDICATION_HIDE");
            ambientIndicationService.mContext.registerReceiverAsUser(ambientIndicationService, UserHandle.ALL, intentFilter2, "com.google.android.ambientindication.permission.AMBIENT_INDICATION", null, 2);
            ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(ambientIndicationService.mCallback);
        }
    }
}
