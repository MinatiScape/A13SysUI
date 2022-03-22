package com.android.systemui.statusbar.phone;

import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.IApplicationThread;
import android.app.IWallpaperManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProfilerInfo;
import android.app.StatusBarManager;
import android.app.TaskStackBuilder;
import android.app.UiModeManager;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.hardware.devicestate.DeviceStateManager;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.dreams.IDreamManager;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.MathUtils;
import android.util.Slog;
import android.view.Display;
import android.view.GestureDetector;
import android.view.IRemoteAnimationRunner;
import android.view.InsetsState;
import android.view.MotionEvent;
import android.view.RemoteAnimationAdapter;
import android.view.SurfaceControl;
import android.view.ThreadedRenderer;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityManager;
import android.widget.DateTimeView;
import android.widget.ImageView;
import android.widget.TextView;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.RemoteTransition;
import android.window.TransitionInfo;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.coordinatorlayout.widget.DirectedAcyclicGraph;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.fragment.R$id;
import androidx.leanback.R$color;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.policy.SystemBarUtils;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.RegisterStatusBarResult;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.keyguard.KeyguardDisplayManager$$ExternalSyntheticLambda1;
import com.android.keyguard.KeyguardHostViewController;
import com.android.keyguard.KeyguardMessageArea;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardPatternView$$ExternalSyntheticLambda0;
import com.android.keyguard.KeyguardStatusView$$ExternalSyntheticLambda0;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda1;
import com.android.keyguard.ViewMediatorCallback;
import com.android.settingslib.Utils;
import com.android.settingslib.graph.ThemedBatteryDrawable;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda1;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.AutoReinflateContainer;
import com.android.systemui.CoreStartable;
import com.android.systemui.DejankUtils;
import com.android.systemui.InitController;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DelegateLaunchAnimatorController;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.biometrics.UdfpsKeyguardViewController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.camera.CameraIntents;
import com.android.systemui.charging.WirelessChargingAnimation;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeScreenBrightness$$ExternalSyntheticOutline0;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.fragments.ExtensionFragmentListener;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController;
import com.android.systemui.keyguard.KeyguardService;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarTransitions;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.OverlayPlugin;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.privacy.PrivacyType;
import com.android.systemui.qs.QSFgsManagerFooter$$ExternalSyntheticLambda0;
import com.android.systemui.qs.QSFragment;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSPanelController;
import com.android.systemui.qs.QSSecurityFooter;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
import com.android.systemui.recents.ScreenPinningRequest;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.scrim.ScrimDrawable;
import com.android.systemui.scrim.ScrimView;
import com.android.systemui.settings.brightness.BrightnessMirrorHandler;
import com.android.systemui.settings.brightness.BrightnessMirrorHandler$brightnessMirrorListener$1;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.AlertingNotificationManager;
import com.android.systemui.statusbar.AutoHideUiElement;
import com.android.systemui.statusbar.BackDropView;
import com.android.systemui.statusbar.CircleReveal;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.KeyboardShortcuts;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.PowerButtonReveal;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.charging.WiredChargingRippleController;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.core.StatusBarInitializer;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import com.android.systemui.statusbar.phone.BarTransitions;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarTransitions;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda7;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.statusbar.phone.dagger.StatusBarComponent;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.ExtensionControllerImpl;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.statusbar.window.StatusBarWindowStateListener;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.WallpaperController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.MessageRouter;
import com.android.systemui.volume.VolumeComponent;
import com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda0;
import com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda2;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda1;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda0;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda1;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda2;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda15;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda16;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda4;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.transition.Transitions;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController$$ExternalSyntheticLambda1;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public class StatusBar extends CoreStartable implements ActivityStarter, LifecycleOwner {
    public static final boolean ONLY_CORE_APPS;
    public final AccessibilityFloatingMenuController mAccessibilityFloatingMenuController;
    public final ActivityLaunchAnimator mActivityLaunchAnimator;
    public View mAmbientIndicationContainer;
    public int mAppearance;
    public final Lazy<AssistManager> mAssistManagerLazy;
    public final AutoHideController mAutoHideController;
    public IStatusBarService mBarService;
    public final BatteryController mBatteryController;
    public BiometricUnlockController mBiometricUnlockController;
    public final Lazy<BiometricUnlockController> mBiometricUnlockControllerLazy;
    public boolean mBouncerShowing;
    public BrightnessMirrorController mBrightnessMirrorController;
    public boolean mBrightnessMirrorVisible;
    public final BrightnessSliderController.Factory mBrightnessSliderFactory;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final Optional<Bubbles> mBubblesOptional;
    public boolean mCloseQsBeforeScreenOff;
    public final SysuiColorExtractor mColorExtractor;
    public final CommandQueue mCommandQueue;
    public StatusBarCommandQueueCallbacks mCommandQueueCallbacks;
    public final ConfigurationController mConfigurationController;
    public final DemoModeController mDemoModeController;
    public boolean mDeviceInteractive;
    public DevicePolicyManager mDevicePolicyManager;
    public final DeviceProvisionedController mDeviceProvisionedController;
    public Display mDisplay;
    public int mDisplayId;
    public final DisplayMetrics mDisplayMetrics;
    public final DozeParameters mDozeParameters;
    public DozeScrimController mDozeScrimController;
    @VisibleForTesting
    public DozeServiceHost mDozeServiceHost;
    public boolean mDozing;
    public IDreamManager mDreamManager;
    public final DreamOverlayStateController mDreamOverlayStateController;
    public final DynamicPrivacyController mDynamicPrivacyController;
    public final NotificationEntryManager mEntryManager;
    public boolean mExpandedVisible;
    public final ExtensionController mExtensionController;
    public final FalsingCollector mFalsingCollector;
    public final FalsingManager mFalsingManager;
    public final FeatureFlags mFeatureFlags;
    public final FragmentService mFragmentService;
    public PowerManager.WakeLock mGestureWakeLock;
    public final NotificationGutsManager mGutsManager;
    public final HeadsUpManagerPhone mHeadsUpManager;
    public final PhoneStatusBarPolicy mIconPolicy;
    public final InitController mInitController;
    public int mInteractingWindows;
    public boolean mIsFullscreen;
    public boolean mIsKeyguard;
    public boolean mIsLaunchingActivityOverLockscreen;
    public boolean mIsOccluded;
    public final InteractionJankMonitor mJankMonitor;
    public final KeyguardBypassController mKeyguardBypassController;
    public final KeyguardDismissUtil mKeyguardDismissUtil;
    public KeyguardIndicationController mKeyguardIndicationController;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final KeyguardViewMediator mKeyguardViewMediator;
    public final ViewMediatorCallback mKeyguardViewMediatorCallback;
    public int mLastCameraLaunchSource;
    public int mLastLoggedStateFingerprint;
    public boolean mLaunchCameraOnFinishedGoingToSleep;
    public boolean mLaunchCameraWhenFinishedWaking;
    public boolean mLaunchEmergencyActionOnFinishedGoingToSleep;
    public boolean mLaunchEmergencyActionWhenFinishedWaking;
    public Runnable mLaunchTransitionEndRunnable;
    public final LightBarController mLightBarController;
    public LightRevealScrim mLightRevealScrim;
    public final LockscreenGestureLogger mLockscreenGestureLogger;
    public final LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    public LockscreenWallpaper mLockscreenWallpaper;
    public final Lazy<LockscreenWallpaper> mLockscreenWallpaperLazy;
    public final DelayableExecutor mMainExecutor;
    public final NotificationMediaManager mMediaManager;
    public final MessageRouter mMessageRouter;
    public final MetricsLogger mMetricsLogger;
    public final NavigationBarController mNavigationBarController;
    public final NetworkController mNetworkController;
    public boolean mNoAnimationOnNextBarModeChange;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public final NotifShadeEventSource mNotifShadeEventSource;
    public StatusBarNotificationActivityStarter mNotificationActivityStarter;
    public DirectedAcyclicGraph mNotificationAnimationProvider;
    public final NotificationIconAreaController mNotificationIconAreaController;
    public final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
    public final NotificationLogger mNotificationLogger;
    public NotificationPanelViewController mNotificationPanelViewController;
    public final Lazy<NotificationShadeDepthController> mNotificationShadeDepthControllerLazy;
    public NotificationShadeWindowController mNotificationShadeWindowController;
    public NotificationShadeWindowView mNotificationShadeWindowView;
    public NotificationShadeWindowViewController mNotificationShadeWindowViewController;
    public NotificationShelfController mNotificationShelfController;
    public final NotificationsController mNotificationsController;
    public final OngoingCallController mOngoingCallController;
    public boolean mPanelExpanded;
    public final PanelExpansionStateManager mPanelExpansionStateManager;
    public PhoneStatusBarViewController mPhoneStatusBarViewController;
    public final PluginDependencyProvider mPluginDependencyProvider;
    public final PluginManager mPluginManager;
    public PowerButtonReveal mPowerButtonReveal;
    public final PowerManager mPowerManager;
    public StatusBarNotificationPresenter mPresenter;
    public final PulseExpansionHandler mPulseExpansionHandler;
    public QSPanelController mQSPanelController;
    public final NotificationRemoteInputManager mRemoteInputManager;
    public View mReportRejectedTouch;
    public final ScreenLifecycle mScreenLifecycle;
    public final ScreenOffAnimationController mScreenOffAnimationController;
    public final ScreenPinningRequest mScreenPinningRequest;
    public final ScrimController mScrimController;
    public final ShadeController mShadeController;
    public final Optional<LegacySplitScreen> mSplitScreenOptional;
    public NotificationStackScrollLayout mStackScroller;
    public NotificationStackScrollLayoutController mStackScrollerController;
    public final Optional<StartingSurface> mStartingSurfaceOptional;
    public int mState;
    public StatusBarComponent mStatusBarComponent;
    public final StatusBarComponent.Factory mStatusBarComponentFactory;
    public final StatusBarHideIconsForBouncerManager mStatusBarHideIconsForBouncerManager;
    public StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public int mStatusBarMode;
    public final StatusBarNotificationActivityStarter.Builder mStatusBarNotificationActivityStarterBuilder;
    public final StatusBarSignalPolicy mStatusBarSignalPolicy;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public LogMaker mStatusBarStateLog;
    public final StatusBarTouchableRegionManager mStatusBarTouchableRegionManager;
    public PhoneStatusBarTransitions mStatusBarTransitions;
    public final StatusBarWindowController mStatusBarWindowController;
    public boolean mTransientShown;
    public final Executor mUiBgExecutor;
    public UiModeManager mUiModeManager;
    public final UserInfoControllerImpl mUserInfoControllerImpl;
    public final UserSwitcherController mUserSwitcherController;
    public final NotificationViewHierarchyManager mViewHierarchyManager;
    public boolean mVisible;
    public boolean mVisibleToUser;
    public final VisualStabilityManager mVisualStabilityManager;
    public final VolumeComponent mVolumeComponent;
    public boolean mWakeUpComingFromTouch;
    public final NotificationWakeUpCoordinator mWakeUpCoordinator;
    public PointF mWakeUpTouchLocation;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final WallpaperController mWallpaperController;
    public final WallpaperManager mWallpaperManager;
    public boolean mWallpaperSupported;
    public static final long[] CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS = {20, 20, 20, 20, 100, 20};
    public static final int[] CAMERA_LAUNCH_GESTURE_VIBRATION_AMPLITUDES = {39, 82, 139, 213, 0, 127};
    public static final UiEventLoggerImpl sUiEventLogger = new UiEventLoggerImpl();
    public float mTransitionToFullShadeProgress = 0.0f;
    public final Point mCurrentDisplaySize = new Point();
    public int mStatusBarWindowState = 0;
    public final Object mQueueLock = new Object();
    public int mDisabled1 = 0;
    public int mDisabled2 = 0;
    @VisibleForTesting
    public boolean mUserSetup = false;
    public final int[] mTmpInt2 = new int[2];
    public final LifecycleRegistry mLifecycle = new LifecycleRegistry(this, true);
    public final StatusBar$$ExternalSyntheticLambda3 mOnColorsChangedListener = new ColorExtractor.OnColorsChangedListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda3
        public final void onColorsChanged(ColorExtractor colorExtractor, int i) {
            StatusBar statusBar = StatusBar.this;
            long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
            Objects.requireNonNull(statusBar);
            statusBar.updateTheme();
        }
    };
    public final WMShell$7$$ExternalSyntheticLambda1 mCheckBarModes = new WMShell$7$$ExternalSyntheticLambda1(this, 7);
    public final AnonymousClass8 mBroadcastReceiver = new AnonymousClass8();
    public final AnonymousClass9 mDemoReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.phone.StatusBar.9
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            "fake_artwork".equals(intent.getAction());
        }
    };
    @VisibleForTesting
    public final WakefulnessLifecycle.Observer mWakefulnessObserver = new AnonymousClass10();
    public final AnonymousClass11 mScreenObserver = new ScreenLifecycle.Observer() { // from class: com.android.systemui.statusbar.phone.StatusBar.11
        {
            StatusBar.this = this;
        }

        @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
        public final void onScreenTurnedOff() {
            Trace.beginSection("StatusBar#onScreenTurnedOff");
            StatusBar.this.mFalsingCollector.onScreenOff();
            ScrimController scrimController = StatusBar.this.mScrimController;
            Objects.requireNonNull(scrimController);
            scrimController.mScreenOn = false;
            StatusBar statusBar = StatusBar.this;
            if (statusBar.mCloseQsBeforeScreenOff) {
                NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
                Objects.requireNonNull(notificationPanelViewController);
                ValueAnimator valueAnimator = notificationPanelViewController.mQsExpansionAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                notificationPanelViewController.setQsExpansion(notificationPanelViewController.mQsMinExpansionHeight);
                StatusBar.this.mCloseQsBeforeScreenOff = false;
            }
            StatusBar.this.updateIsKeyguard();
            Trace.endSection();
        }

        @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
        public final void onScreenTurnedOn() {
            ScrimController scrimController = StatusBar.this.mScrimController;
            Objects.requireNonNull(scrimController);
            scrimController.mScreenOn = true;
            if (scrimController.mHandler.hasCallbacks(scrimController.mBlankingTransitionRunnable)) {
                if (ScrimController.DEBUG) {
                    Log.d("ScrimController", "Shorter blanking because screen turned on. All good.");
                }
                scrimController.mHandler.removeCallbacks(scrimController.mBlankingTransitionRunnable);
                scrimController.mBlankingTransitionRunnable.run();
            }
        }

        @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
        public final void onScreenTurningOn(Runnable runnable) {
            StatusBar.this.mFalsingCollector.onScreenTurningOn();
            NotificationPanelViewController notificationPanelViewController = StatusBar.this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController);
            notificationPanelViewController.mKeyguardStatusViewController.dozeTimeTick();
        }
    };
    public final AnonymousClass13 mBannerActionBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.phone.StatusBar.13
        {
            StatusBar.this = this;
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.android.systemui.statusbar.banner_action_cancel".equals(action) || "com.android.systemui.statusbar.banner_action_setup".equals(action)) {
                StatusBar statusBar = StatusBar.this;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                ((NotificationManager) statusBar.mContext.getSystemService("notification")).cancel(5);
                Settings.Secure.putInt(StatusBar.this.mContext.getContentResolver(), "show_note_about_notification_hiding", 0);
                if ("com.android.systemui.statusbar.banner_action_setup".equals(action)) {
                    StatusBar.this.mShadeController.animateCollapsePanels$1(2);
                    StatusBar.this.mContext.startActivity(new Intent("android.settings.ACTION_APP_NOTIFICATION_REDACTION").addFlags(268435456));
                }
            }
        }
    };
    public final AnonymousClass15 mUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.phone.StatusBar.15
        {
            StatusBar.this = this;
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onDreamingStateChanged(boolean z) {
            if (z) {
                StatusBar.m107$$Nest$mmaybeEscalateHeadsUp(StatusBar.this);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onStrongAuthStateChanged(int i) {
            StatusBar.this.mNotificationsController.requestNotificationUpdate("onStrongAuthStateChanged");
        }
    };
    public final AnonymousClass16 mFalsingBeliefListener = new FalsingManager.FalsingBeliefListener() { // from class: com.android.systemui.statusbar.phone.StatusBar.16
        {
            StatusBar.this = this;
        }

        @Override // com.android.systemui.plugins.FalsingManager.FalsingBeliefListener
        public final void onFalse() {
            StatusBar.this.mStatusBarKeyguardViewManager.reset(true);
        }
    };
    public final AnonymousClass17 mUnlockScrimCallback = new ScrimController.Callback() { // from class: com.android.systemui.statusbar.phone.StatusBar.17
        {
            StatusBar.this = this;
        }

        @Override // com.android.systemui.statusbar.phone.ScrimController.Callback
        public final void onFinished() {
            StatusBar statusBar = StatusBar.this;
            if (statusBar.mStatusBarKeyguardViewManager == null) {
                Log.w("StatusBar", "Tried to notify keyguard visibility when mStatusBarKeyguardViewManager was null");
            } else if (statusBar.mKeyguardStateController.isKeyguardFadingAway()) {
                StatusBar.this.mStatusBarKeyguardViewManager.onKeyguardFadedAway();
            }
        }

        @Override // com.android.systemui.statusbar.phone.ScrimController.Callback
        public final void onCancelled() {
            onFinished();
        }
    };
    public final AnonymousClass18 mUserSetupObserver = new AnonymousClass18();
    public final AnonymousClass19 mWallpaperChangedReceiver = new AnonymousClass19();
    public final AnonymousClass20 mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.phone.StatusBar.20
        {
            StatusBar.this = this;
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onConfigChanged(Configuration configuration) {
            StatusBar.this.updateResources();
            StatusBar statusBar = StatusBar.this;
            Objects.requireNonNull(statusBar);
            statusBar.mDisplay.getMetrics(statusBar.mDisplayMetrics);
            statusBar.mDisplay.getSize(statusBar.mCurrentDisplaySize);
            if (!StatusBar.this.mNotifPipelineFlags.isNewPipelineEnabled()) {
                NotificationViewHierarchyManager notificationViewHierarchyManager = StatusBar.this.mViewHierarchyManager;
                Objects.requireNonNull(notificationViewHierarchyManager);
                Assert.isMainThread();
                if (notificationViewHierarchyManager.mNotifPipelineFlags.checkLegacyPipelineEnabled()) {
                    if (notificationViewHierarchyManager.mPerformingUpdate) {
                        Log.wtf("NotificationViewHierarchyManager", "Re-entrant code during update", new Exception());
                    }
                    notificationViewHierarchyManager.mPerformingUpdate = true;
                    notificationViewHierarchyManager.updateRowStatesInternal();
                    if (!notificationViewHierarchyManager.mPerformingUpdate) {
                        Log.wtf("NotificationViewHierarchyManager", "Manager state has become desynced", new Exception());
                    }
                    notificationViewHierarchyManager.mPerformingUpdate = false;
                }
            }
            ScreenPinningRequest screenPinningRequest = StatusBar.this.mScreenPinningRequest;
            Objects.requireNonNull(screenPinningRequest);
            ScreenPinningRequest.RequestWindowView requestWindowView = screenPinningRequest.mRequestWindow;
            if (requestWindowView != null) {
                requestWindowView.onConfigurationChanged();
            }
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onDensityOrFontScaleChanged() {
            BrightnessMirrorController brightnessMirrorController = StatusBar.this.mBrightnessMirrorController;
            if (brightnessMirrorController != null) {
                Objects.requireNonNull(brightnessMirrorController);
                brightnessMirrorController.reinflate();
            }
            UserInfoControllerImpl userInfoControllerImpl = StatusBar.this.mUserInfoControllerImpl;
            Objects.requireNonNull(userInfoControllerImpl);
            userInfoControllerImpl.reloadUserInfo();
            UserSwitcherController userSwitcherController = StatusBar.this.mUserSwitcherController;
            Objects.requireNonNull(userSwitcherController);
            userSwitcherController.refreshUsers(-1);
            StatusBar statusBar = StatusBar.this;
            NotificationIconAreaController notificationIconAreaController = statusBar.mNotificationIconAreaController;
            Context context = statusBar.mContext;
            Objects.requireNonNull(notificationIconAreaController);
            notificationIconAreaController.updateIconLayoutParams(context);
            Objects.requireNonNull(StatusBar.this.mHeadsUpManager);
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onThemeChanged() {
            BrightnessMirrorController brightnessMirrorController = StatusBar.this.mBrightnessMirrorController;
            if (brightnessMirrorController != null) {
                brightnessMirrorController.reinflate();
            }
            NotificationPanelViewController notificationPanelViewController = StatusBar.this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController);
            notificationPanelViewController.mConfigurationListener.onThemeChanged();
            StatusBarKeyguardViewManager statusBarKeyguardViewManager = StatusBar.this.mStatusBarKeyguardViewManager;
            if (statusBarKeyguardViewManager != null) {
                statusBarKeyguardViewManager.onThemeChanged();
            }
            View view = StatusBar.this.mAmbientIndicationContainer;
            if (view instanceof AutoReinflateContainer) {
                ((AutoReinflateContainer) view).inflateLayout();
            }
            NotificationIconAreaController notificationIconAreaController = StatusBar.this.mNotificationIconAreaController;
            Objects.requireNonNull(notificationIconAreaController);
            notificationIconAreaController.mAodIconTint = Utils.getColorAttrDefaultColor(notificationIconAreaController.mContext, 2130970103);
            notificationIconAreaController.updateAodIconColors();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onUiModeChanged() {
            BrightnessMirrorController brightnessMirrorController = StatusBar.this.mBrightnessMirrorController;
            if (brightnessMirrorController != null) {
                Objects.requireNonNull(brightnessMirrorController);
                brightnessMirrorController.reinflate();
            }
        }
    };
    public AnonymousClass21 mStateListener = new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.phone.StatusBar.21
        {
            StatusBar.this = this;
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onDozeAmountChanged(float f, float f2) {
            if (StatusBar.this.mFeatureFlags.isEnabled(Flags.LOCKSCREEN_ANIMATIONS)) {
                LightRevealScrim lightRevealScrim = StatusBar.this.mLightRevealScrim;
                Objects.requireNonNull(lightRevealScrim);
                if (!(lightRevealScrim.revealEffect instanceof CircleReveal) && !StatusBar.this.mBiometricUnlockController.isWakeAndUnlock()) {
                    StatusBar.this.mLightRevealScrim.setRevealAmount(1.0f - f);
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x003b  */
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onDozingChanged(boolean r4) {
            /*
                r3 = this;
                java.lang.String r0 = "StatusBar#updateDozing"
                android.os.Trace.beginSection(r0)
                com.android.systemui.statusbar.phone.StatusBar r0 = com.android.systemui.statusbar.phone.StatusBar.this
                r0.mDozing = r4
                com.android.systemui.statusbar.phone.DozeServiceHost r4 = r0.mDozeServiceHost
                java.util.Objects.requireNonNull(r4)
                boolean r4 = r4.mDozingRequested
                r0 = 0
                if (r4 == 0) goto L_0x0020
                com.android.systemui.statusbar.phone.StatusBar r4 = com.android.systemui.statusbar.phone.StatusBar.this
                com.android.systemui.statusbar.phone.DozeParameters r4 = r4.mDozeParameters
                java.util.Objects.requireNonNull(r4)
                boolean r4 = r4.mControlScreenOffAnimation
                if (r4 == 0) goto L_0x0020
                r4 = 1
                goto L_0x0021
            L_0x0020:
                r4 = r0
            L_0x0021:
                com.android.systemui.statusbar.phone.StatusBar r1 = com.android.systemui.statusbar.phone.StatusBar.this
                com.android.systemui.statusbar.phone.NotificationPanelViewController r1 = r1.mNotificationPanelViewController
                r1.resetViews(r4)
                com.android.systemui.statusbar.phone.StatusBar r4 = com.android.systemui.statusbar.phone.StatusBar.this
                r4.updateQsExpansionEnabled()
                com.android.systemui.statusbar.phone.StatusBar r4 = com.android.systemui.statusbar.phone.StatusBar.this
                com.android.systemui.keyguard.KeyguardViewMediator r1 = r4.mKeyguardViewMediator
                boolean r4 = r4.mDozing
                java.util.Objects.requireNonNull(r1)
                boolean r2 = r1.mDozing
                if (r4 != r2) goto L_0x003b
                goto L_0x0056
            L_0x003b:
                r1.mDozing = r4
                if (r4 != 0) goto L_0x0041
                r1.mAnimatingScreenOff = r0
            L_0x0041:
                boolean r4 = r1.mShowing
                if (r4 != 0) goto L_0x0051
                boolean r4 = r1.mPendingLock
                if (r4 == 0) goto L_0x0051
                com.android.systemui.statusbar.phone.DozeParameters r4 = r1.mDozeParameters
                boolean r4 = r4.canControlUnlockedScreenOff()
                if (r4 != 0) goto L_0x0056
            L_0x0051:
                boolean r4 = r1.mShowing
                r1.setShowingLocked(r4, r0)
            L_0x0056:
                com.android.systemui.statusbar.phone.StatusBar r4 = com.android.systemui.statusbar.phone.StatusBar.this
                com.android.systemui.statusbar.notification.init.NotificationsController r4 = r4.mNotificationsController
                java.lang.String r0 = "onDozingChanged"
                r4.requestNotificationUpdate(r0)
                com.android.systemui.statusbar.phone.StatusBar r4 = com.android.systemui.statusbar.phone.StatusBar.this
                r4.updateDozingState()
                com.android.systemui.statusbar.phone.StatusBar r4 = com.android.systemui.statusbar.phone.StatusBar.this
                com.android.systemui.statusbar.phone.DozeServiceHost r4 = r4.mDozeServiceHost
                r4.updateDozing()
                com.android.systemui.statusbar.phone.StatusBar r4 = com.android.systemui.statusbar.phone.StatusBar.this
                r4.updateScrimController()
                com.android.systemui.statusbar.phone.StatusBar r3 = com.android.systemui.statusbar.phone.StatusBar.this
                r3.updateReportRejectedTouchVisibility()
                android.os.Trace.endSection()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBar.AnonymousClass21.onDozingChanged(boolean):void");
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onFullscreenStateChanged(boolean z) {
            StatusBar statusBar = StatusBar.this;
            statusBar.mIsFullscreen = z;
            statusBar.maybeUpdateBarMode();
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onStateChanged(int i) {
            StatusBar statusBar = StatusBar.this;
            statusBar.mState = i;
            statusBar.updateReportRejectedTouchVisibility();
            StatusBar.this.mDozeServiceHost.updateDozing();
            StatusBar.this.updateTheme();
            StatusBar statusBar2 = StatusBar.this;
            statusBar2.mNavigationBarController.touchAutoDim(statusBar2.mDisplayId);
            Trace.beginSection("StatusBar#updateKeyguardState");
            StatusBar statusBar3 = StatusBar.this;
            boolean z = true;
            if (statusBar3.mState == 1) {
                NotificationPanelViewController notificationPanelViewController = statusBar3.mNotificationPanelViewController;
                Objects.requireNonNull(notificationPanelViewController);
                notificationPanelViewController.mView.removeCallbacks(notificationPanelViewController.mMaybeHideExpandedRunnable);
            }
            StatusBar.this.updateDozingState();
            StatusBar.this.checkBarModes();
            StatusBar.this.updateScrimController();
            StatusBar statusBar4 = StatusBar.this;
            StatusBarNotificationPresenter statusBarNotificationPresenter = statusBar4.mPresenter;
            if (statusBar4.mState == 1) {
                z = false;
            }
            statusBarNotificationPresenter.updateMediaMetaData(false, z);
            StatusBar statusBar5 = StatusBar.this;
            Objects.requireNonNull(statusBar5);
            KeyguardStateController keyguardStateController = statusBar5.mKeyguardStateController;
            StatusBarKeyguardViewManager statusBarKeyguardViewManager = statusBar5.mStatusBarKeyguardViewManager;
            Objects.requireNonNull(statusBarKeyguardViewManager);
            boolean z2 = statusBarKeyguardViewManager.mShowing;
            StatusBarKeyguardViewManager statusBarKeyguardViewManager2 = statusBar5.mStatusBarKeyguardViewManager;
            Objects.requireNonNull(statusBarKeyguardViewManager2);
            keyguardStateController.notifyKeyguardState(z2, statusBarKeyguardViewManager2.mOccluded);
            Trace.endSection();
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onStatePreChange(int i, int i2) {
            StatusBar statusBar = StatusBar.this;
            if (statusBar.mVisible && (i2 == 2 || statusBar.mStatusBarStateController.goingToFullShade())) {
                StatusBar statusBar2 = StatusBar.this;
                Objects.requireNonNull(statusBar2);
                try {
                    statusBar2.mBarService.clearNotificationEffects();
                } catch (RemoteException unused) {
                }
            }
            if (i2 == 1) {
                NotificationRemoteInputManager notificationRemoteInputManager = StatusBar.this.mRemoteInputManager;
                Objects.requireNonNull(notificationRemoteInputManager);
                NotificationRemoteInputManager.RemoteInputListener remoteInputListener = notificationRemoteInputManager.mRemoteInputListener;
                if (remoteInputListener != null) {
                    remoteInputListener.onPanelCollapsed();
                }
                StatusBar.m107$$Nest$mmaybeEscalateHeadsUp(StatusBar.this);
            }
        }
    };
    public final AnonymousClass22 mBatteryStateChangeCallback = new BatteryController.BatteryStateChangeCallback() { // from class: com.android.systemui.statusbar.phone.StatusBar.22
        {
            StatusBar.this = this;
        }

        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public final void onPowerSaveChanged(boolean z) {
            StatusBar statusBar = StatusBar.this;
            statusBar.mMainExecutor.execute(statusBar.mCheckBarModes);
            DozeServiceHost dozeServiceHost = StatusBar.this.mDozeServiceHost;
            if (dozeServiceHost != null) {
                Iterator<DozeHost.Callback> it = dozeServiceHost.mCallbacks.iterator();
                while (it.hasNext()) {
                    it.next().onPowerSaveChanged();
                }
            }
        }
    };
    public final AnonymousClass23 mActivityLaunchAnimatorCallback = new AnonymousClass23();
    public final AnonymousClass24 mActivityLaunchAnimatorListener = new ActivityLaunchAnimator.Listener() { // from class: com.android.systemui.statusbar.phone.StatusBar.24
        {
            StatusBar.this = this;
        }

        @Override // com.android.systemui.animation.ActivityLaunchAnimator.Listener
        public final void onLaunchAnimationEnd() {
            StatusBar.this.mKeyguardViewMediator.setBlursDisabledForAppLaunch(false);
        }

        @Override // com.android.systemui.animation.ActivityLaunchAnimator.Listener
        public final void onLaunchAnimationStart() {
            StatusBar.this.mKeyguardViewMediator.setBlursDisabledForAppLaunch(true);
        }
    };
    public final AnonymousClass25 mDemoModeCallback = new DemoMode() { // from class: com.android.systemui.statusbar.phone.StatusBar.25
        @Override // com.android.systemui.demomode.DemoModeCommandReceiver
        public final void dispatchDemoCommand(String str, Bundle bundle) {
        }

        {
            StatusBar.this = this;
        }

        @Override // com.android.systemui.demomode.DemoModeCommandReceiver
        public final void onDemoModeFinished() {
            StatusBar.this.checkBarModes();
        }
    };
    public final StatusBar$$ExternalSyntheticLambda2 mBubbleExpandListener = new StatusBar$$ExternalSyntheticLambda2(this);
    public final ActivityIntentHelper mActivityIntentHelper = new ActivityIntentHelper(this.mContext);

    /* renamed from: com.android.systemui.statusbar.phone.StatusBar$10 */
    /* loaded from: classes.dex */
    public class AnonymousClass10 implements WakefulnessLifecycle.Observer {
        public AnonymousClass10() {
            StatusBar.this = r1;
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public final void onFinishedGoingToSleep() {
            NotificationPanelViewController notificationPanelViewController = StatusBar.this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController);
            notificationPanelViewController.setLaunchingAffordance(false);
            StatusBar.this.releaseGestureWakeLock();
            StatusBar statusBar = StatusBar.this;
            statusBar.mLaunchCameraWhenFinishedWaking = false;
            statusBar.mDeviceInteractive = false;
            statusBar.mWakeUpComingFromTouch = false;
            statusBar.mWakeUpTouchLocation = null;
            statusBar.updateVisibleToUser();
            StatusBar.this.updateNotificationPanelTouchState();
            StatusBar.this.mNotificationShadeWindowViewController.cancelCurrentTouch();
            StatusBar statusBar2 = StatusBar.this;
            if (statusBar2.mLaunchCameraOnFinishedGoingToSleep) {
                statusBar2.mLaunchCameraOnFinishedGoingToSleep = false;
                statusBar2.mMainExecutor.execute(new BubbleStackView$$ExternalSyntheticLambda15(this, 6));
            }
            StatusBar statusBar3 = StatusBar.this;
            if (statusBar3.mLaunchEmergencyActionOnFinishedGoingToSleep) {
                statusBar3.mLaunchEmergencyActionOnFinishedGoingToSleep = false;
                statusBar3.mMainExecutor.execute(new WifiEntry$$ExternalSyntheticLambda0(this, 3));
            }
            StatusBar.this.updateIsKeyguard();
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public final void onFinishedWakingUp() {
            NotificationWakeUpCoordinator notificationWakeUpCoordinator = StatusBar.this.mWakeUpCoordinator;
            Objects.requireNonNull(notificationWakeUpCoordinator);
            notificationWakeUpCoordinator.fullyAwake = true;
            StatusBar.this.mWakeUpCoordinator.setWakingUp(false);
            StatusBar statusBar = StatusBar.this;
            if (statusBar.mLaunchCameraWhenFinishedWaking) {
                statusBar.mNotificationPanelViewController.launchCamera(false, statusBar.mLastCameraLaunchSource);
                StatusBar.this.mLaunchCameraWhenFinishedWaking = false;
            }
            StatusBar statusBar2 = StatusBar.this;
            if (statusBar2.mLaunchEmergencyActionWhenFinishedWaking) {
                statusBar2.mLaunchEmergencyActionWhenFinishedWaking = false;
                Intent emergencyActionIntent = statusBar2.getEmergencyActionIntent();
                if (emergencyActionIntent != null) {
                    StatusBar.this.mContext.startActivityAsUser(emergencyActionIntent, UserHandle.CURRENT);
                }
            }
            StatusBar.this.updateScrimController();
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public final void onStartedGoingToSleep() {
            DejankUtils.startDetectingBlockingIpcs("StatusBar#onStartedGoingToSleep");
            StatusBar.m108$$Nest$mupdateRevealEffect(StatusBar.this, false);
            StatusBar.this.updateNotificationPanelTouchState();
            StatusBar.m107$$Nest$mmaybeEscalateHeadsUp(StatusBar.this);
            StatusBar statusBar = StatusBar.this;
            Objects.requireNonNull(statusBar);
            VolumeComponent volumeComponent = statusBar.mVolumeComponent;
            if (volumeComponent != null) {
                volumeComponent.dismissNow();
            }
            NotificationWakeUpCoordinator notificationWakeUpCoordinator = StatusBar.this.mWakeUpCoordinator;
            Objects.requireNonNull(notificationWakeUpCoordinator);
            notificationWakeUpCoordinator.fullyAwake = false;
            KeyguardBypassController keyguardBypassController = StatusBar.this.mKeyguardBypassController;
            Objects.requireNonNull(keyguardBypassController);
            keyguardBypassController.pendingUnlock = null;
            if (StatusBar.this.mDozeParameters.shouldShowLightRevealScrim()) {
                StatusBar.this.makeExpandedVisible(true);
            }
            DejankUtils.stopDetectingBlockingIpcs("StatusBar#onStartedGoingToSleep");
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public final void onStartedWakingUp() {
            DejankUtils.startDetectingBlockingIpcs("StatusBar#onStartedWakingUp");
            StatusBar.this.mNotificationShadeWindowController.batchApplyWindowLayoutParams(new WifiEntry$$ExternalSyntheticLambda1(this, 3));
            DejankUtils.stopDetectingBlockingIpcs("StatusBar#onStartedWakingUp");
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.StatusBar$18 */
    /* loaded from: classes.dex */
    public class AnonymousClass18 implements DeviceProvisionedController.DeviceProvisionedListener {
        public AnonymousClass18() {
            StatusBar.this = r1;
        }

        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public final void onUserSetupChanged() {
            boolean isCurrentUserSetup = StatusBar.this.mDeviceProvisionedController.isCurrentUserSetup();
            Log.d("StatusBar", "mUserSetupObserver - DeviceProvisionedListener called for current user");
            StatusBar statusBar = StatusBar.this;
            if (isCurrentUserSetup != statusBar.mUserSetup) {
                statusBar.mUserSetup = isCurrentUserSetup;
                if (!isCurrentUserSetup && statusBar.mState == 0) {
                    statusBar.mNotificationPanelViewController.collapsePanel(true, false, 1.0f);
                }
                StatusBar statusBar2 = StatusBar.this;
                NotificationPanelViewController notificationPanelViewController = statusBar2.mNotificationPanelViewController;
                if (notificationPanelViewController != null) {
                    boolean z = statusBar2.mUserSetup;
                    notificationPanelViewController.mUserSetupComplete = z;
                    KeyguardBottomAreaView keyguardBottomAreaView = notificationPanelViewController.mKeyguardBottomArea;
                    Objects.requireNonNull(keyguardBottomAreaView);
                    keyguardBottomAreaView.mUserSetupComplete = z;
                    keyguardBottomAreaView.updateCameraVisibility();
                    keyguardBottomAreaView.updateLeftAffordanceIcon();
                }
                StatusBar.this.updateQsExpansionEnabled();
            }
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.StatusBar$19 */
    /* loaded from: classes.dex */
    public class AnonymousClass19 extends BroadcastReceiver {
        public AnonymousClass19() {
            StatusBar.this = r1;
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            boolean z;
            ScrimState[] values;
            StatusBar statusBar = StatusBar.this;
            if (!statusBar.mWallpaperSupported) {
                Log.wtf("StatusBar", "WallpaperManager not supported");
                return;
            }
            WallpaperInfo wallpaperInfo = statusBar.mWallpaperManager.getWallpaperInfo(-2);
            WallpaperController wallpaperController = StatusBar.this.mWallpaperController;
            Objects.requireNonNull(wallpaperController);
            wallpaperController.wallpaperInfo = wallpaperInfo;
            if (!StatusBar.this.mContext.getResources().getBoolean(17891609) || wallpaperInfo == null || !wallpaperInfo.supportsAmbientMode()) {
                z = false;
            } else {
                z = true;
            }
            StatusBar.this.mNotificationShadeWindowController.setWallpaperSupportsAmbientMode();
            ScrimController scrimController = StatusBar.this.mScrimController;
            Objects.requireNonNull(scrimController);
            scrimController.mWallpaperSupportsAmbientMode = z;
            for (ScrimState scrimState : ScrimState.values()) {
                Objects.requireNonNull(scrimState);
                scrimState.mWallpaperSupportsAmbientMode = z;
            }
            KeyguardViewMediator keyguardViewMediator = StatusBar.this.mKeyguardViewMediator;
            Objects.requireNonNull(keyguardViewMediator);
            keyguardViewMediator.mWallpaperSupportsAmbientMode = z;
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.StatusBar$2 */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements PluginListener<OverlayPlugin> {
        public final ArraySet<OverlayPlugin> mOverlays = new ArraySet<>();

        /* renamed from: com.android.systemui.statusbar.phone.StatusBar$2$Callback */
        /* loaded from: classes.dex */
        public class Callback implements OverlayPlugin.Callback {
            public final OverlayPlugin mPlugin;

            public Callback(OverlayPlugin overlayPlugin) {
                AnonymousClass2.this = r1;
                this.mPlugin = overlayPlugin;
            }

            @Override // com.android.systemui.plugins.OverlayPlugin.Callback
            public final void onHoldStatusBarOpenChange() {
                if (this.mPlugin.holdStatusBarOpen()) {
                    AnonymousClass2.this.mOverlays.add(this.mPlugin);
                } else {
                    AnonymousClass2.this.mOverlays.remove(this.mPlugin);
                }
                StatusBar.this.mMainExecutor.execute(new SuggestController$$ExternalSyntheticLambda1(this, 6));
            }
        }

        public AnonymousClass2() {
            StatusBar.this = r1;
        }

        @Override // com.android.systemui.plugins.PluginListener
        public final void onPluginConnected(OverlayPlugin overlayPlugin, Context context) {
            StatusBar.this.mMainExecutor.execute(new BubblesManager$5$$ExternalSyntheticLambda0(this, overlayPlugin, 3));
        }

        @Override // com.android.systemui.plugins.PluginListener
        public final void onPluginDisconnected(OverlayPlugin overlayPlugin) {
            StatusBar.this.mMainExecutor.execute(new BubblesManager$5$$ExternalSyntheticLambda2(this, overlayPlugin, 1));
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.StatusBar$23 */
    /* loaded from: classes.dex */
    public class AnonymousClass23 implements ActivityLaunchAnimator.Callback {
        public AnonymousClass23() {
            StatusBar.this = r1;
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.StatusBar$4 */
    /* loaded from: classes.dex */
    public final class AnonymousClass4 implements BiometricUnlockController.BiometricModeListener {
        public AnonymousClass4() {
            StatusBar.this = r1;
        }

        public final void notifyBiometricAuthModeChanged() {
            StatusBar statusBar = StatusBar.this;
            Objects.requireNonNull(statusBar);
            statusBar.mDozeServiceHost.updateDozing();
            statusBar.updateScrimController();
        }

        public final void setWakeAndUnlocking(boolean z) {
            boolean z2;
            if (StatusBar.this.getNavigationBarView() != null) {
                NavigationBarView navigationBarView = StatusBar.this.getNavigationBarView();
                Objects.requireNonNull(navigationBarView);
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) ((ViewGroup) navigationBarView.getParent()).getLayoutParams();
                if (layoutParams != null) {
                    if (layoutParams.windowAnimations != 0) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (!z2 && z) {
                        layoutParams.windowAnimations = 2132017165;
                    } else if (z2 && !z) {
                        layoutParams.windowAnimations = 0;
                    }
                    ((WindowManager) navigationBarView.getContext().getSystemService(WindowManager.class)).updateViewLayout((View) navigationBarView.getParent(), layoutParams);
                }
                navigationBarView.mWakeAndUnlocking = z;
                navigationBarView.updateLayoutTransitionsEnabled();
            }
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.StatusBar$6 */
    /* loaded from: classes.dex */
    public class AnonymousClass6 implements WirelessChargingAnimation.Callback {
        public AnonymousClass6() {
            StatusBar.this = r1;
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.StatusBar$8 */
    /* loaded from: classes.dex */
    public class AnonymousClass8 extends BroadcastReceiver {
        public static final /* synthetic */ int $r8$clinit = 0;

        public AnonymousClass8() {
            StatusBar.this = r1;
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            Trace.beginSection("StatusBar#onReceive");
            String action = intent.getAction();
            int i = 2;
            int i2 = 0;
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action)) {
                KeyboardShortcuts.dismiss();
                StatusBar.this.mRemoteInputManager.closeRemoteInputs();
                if (StatusBar.this.mBubblesOptional.isPresent() && StatusBar.this.mBubblesOptional.get().isStackExpanded()) {
                    StatusBar.this.mBubblesOptional.get().collapseStack();
                }
                if (StatusBar.this.mLockscreenUserManager.isCurrentProfile(getSendingUserId())) {
                    String stringExtra = intent.getStringExtra("reason");
                    if (stringExtra != null) {
                        if (!stringExtra.equals("recentapps")) {
                            i = 0;
                        }
                        if (!stringExtra.equals("dream") || !StatusBar.this.mScreenOffAnimationController.shouldExpandNotifications()) {
                            i2 = i;
                        } else {
                            i2 = i | 4;
                        }
                    }
                    StatusBar.this.mShadeController.animateCollapsePanels(i2);
                }
            } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
                NotificationShadeWindowController notificationShadeWindowController = StatusBar.this.mNotificationShadeWindowController;
                if (notificationShadeWindowController != null) {
                    notificationShadeWindowController.setNotTouchable();
                }
                if (StatusBar.this.mBubblesOptional.isPresent() && StatusBar.this.mBubblesOptional.get().isStackExpanded()) {
                    StatusBar.this.mMainExecutor.execute(new BubbleStackView$$ExternalSyntheticLambda16(this, 3));
                }
                StatusBar statusBar = StatusBar.this;
                Objects.requireNonNull(statusBar);
                PhoneStatusBarTransitions phoneStatusBarTransitions = statusBar.mStatusBarTransitions;
                if (phoneStatusBarTransitions != null) {
                    BarTransitions.BarBackgroundDrawable barBackgroundDrawable = phoneStatusBarTransitions.mBarBackground;
                    Objects.requireNonNull(barBackgroundDrawable);
                    if (barBackgroundDrawable.mAnimating) {
                        barBackgroundDrawable.mAnimating = false;
                        barBackgroundDrawable.invalidateSelf();
                    }
                }
                NavigationBarController navigationBarController = statusBar.mNavigationBarController;
                int i3 = statusBar.mDisplayId;
                Objects.requireNonNull(navigationBarController);
                NavigationBar navigationBar = navigationBarController.mNavigationBars.get(i3);
                if (navigationBar != null) {
                    NavigationBarView navigationBarView = navigationBar.mNavigationBarView;
                    Objects.requireNonNull(navigationBarView);
                    NavigationBarTransitions navigationBarTransitions = navigationBarView.mBarTransitions;
                    Objects.requireNonNull(navigationBarTransitions);
                    BarTransitions.BarBackgroundDrawable barBackgroundDrawable2 = navigationBarTransitions.mBarBackground;
                    Objects.requireNonNull(barBackgroundDrawable2);
                    if (barBackgroundDrawable2.mAnimating) {
                        barBackgroundDrawable2.mAnimating = false;
                        barBackgroundDrawable2.invalidateSelf();
                    }
                }
                StatusBar statusBar2 = StatusBar.this;
                Objects.requireNonNull(statusBar2);
                statusBar2.mNotificationsController.resetUserExpandedStates();
            } else if ("android.app.action.SHOW_DEVICE_MONITORING_DIALOG".equals(action)) {
                QSPanelController qSPanelController = StatusBar.this.mQSPanelController;
                Objects.requireNonNull(qSPanelController);
                QSSecurityFooter qSSecurityFooter = qSPanelController.mQsSecurityFooter;
                Objects.requireNonNull(qSSecurityFooter);
                qSSecurityFooter.mShouldUseSettingsButton.set(false);
                qSSecurityFooter.mMainHandler.post(new KeyguardPatternView$$ExternalSyntheticLambda0(qSSecurityFooter, qSSecurityFooter.createDialogView(), 2));
            }
            Trace.endSection();
        }
    }

    /* loaded from: classes.dex */
    public static class AnimateExpandSettingsPanelMessage {
    }

    static {
        boolean z;
        IPackageManager asInterface;
        try {
            asInterface = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
        } catch (RemoteException unused) {
        }
        if (asInterface != null) {
            if (asInterface.isOnlyCoreApps()) {
                z = true;
                ONLY_CORE_APPS = z;
            }
        }
        z = false;
        ONLY_CORE_APPS = z;
    }

    /* JADX WARN: Type inference failed for: r7v11, types: [com.android.systemui.statusbar.phone.StatusBar$11] */
    /* JADX WARN: Type inference failed for: r7v12, types: [com.android.systemui.statusbar.phone.StatusBar$13] */
    /* JADX WARN: Type inference failed for: r7v13, types: [com.android.systemui.statusbar.phone.StatusBar$15] */
    /* JADX WARN: Type inference failed for: r7v14, types: [com.android.systemui.statusbar.phone.StatusBar$16] */
    /* JADX WARN: Type inference failed for: r7v15, types: [com.android.systemui.statusbar.phone.StatusBar$17] */
    /* JADX WARN: Type inference failed for: r7v18, types: [com.android.systemui.statusbar.phone.StatusBar$20] */
    /* JADX WARN: Type inference failed for: r7v19, types: [com.android.systemui.statusbar.phone.StatusBar$21] */
    /* JADX WARN: Type inference failed for: r7v20, types: [com.android.systemui.statusbar.phone.StatusBar$22] */
    /* JADX WARN: Type inference failed for: r7v22, types: [com.android.systemui.statusbar.phone.StatusBar$24] */
    /* JADX WARN: Type inference failed for: r7v23, types: [com.android.systemui.statusbar.phone.StatusBar$25] */
    /* JADX WARN: Type inference failed for: r7v9, types: [com.android.systemui.statusbar.phone.StatusBar$9] */
    public StatusBar(Context context, NotificationsController notificationsController, FragmentService fragmentService, LightBarController lightBarController, AutoHideController autoHideController, StatusBarWindowController statusBarWindowController, StatusBarWindowStateController statusBarWindowStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, StatusBarSignalPolicy statusBarSignalPolicy, PulseExpansionHandler pulseExpansionHandler, NotificationWakeUpCoordinator notificationWakeUpCoordinator, KeyguardBypassController keyguardBypassController, KeyguardStateController keyguardStateController, HeadsUpManagerPhone headsUpManagerPhone, DynamicPrivacyController dynamicPrivacyController, FalsingManager falsingManager, FalsingCollector falsingCollector, BroadcastDispatcher broadcastDispatcher, NotifShadeEventSource notifShadeEventSource, NotificationEntryManager notificationEntryManager, NotificationGutsManager notificationGutsManager, NotificationLogger notificationLogger, NotificationInterruptStateProvider notificationInterruptStateProvider, NotificationViewHierarchyManager notificationViewHierarchyManager, PanelExpansionStateManager panelExpansionStateManager, KeyguardViewMediator keyguardViewMediator, DisplayMetrics displayMetrics, MetricsLogger metricsLogger, Executor executor, NotificationMediaManager notificationMediaManager, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationRemoteInputManager notificationRemoteInputManager, UserSwitcherController userSwitcherController, NetworkController networkController, BatteryController batteryController, SysuiColorExtractor sysuiColorExtractor, ScreenLifecycle screenLifecycle, WakefulnessLifecycle wakefulnessLifecycle, SysuiStatusBarStateController sysuiStatusBarStateController, Optional optional, VisualStabilityManager visualStabilityManager, DeviceProvisionedController deviceProvisionedController, NavigationBarController navigationBarController, AccessibilityFloatingMenuController accessibilityFloatingMenuController, Lazy lazy, ConfigurationController configurationController, NotificationShadeWindowController notificationShadeWindowController, DozeParameters dozeParameters, ScrimController scrimController, Lazy lazy2, LockscreenGestureLogger lockscreenGestureLogger, Lazy lazy3, DozeServiceHost dozeServiceHost, PowerManager powerManager, ScreenPinningRequest screenPinningRequest, DozeScrimController dozeScrimController, VolumeComponent volumeComponent, CommandQueue commandQueue, StatusBarComponent.Factory factory, PluginManager pluginManager, Optional optional2, StatusBarNotificationActivityStarter.Builder builder, ShadeController shadeController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, ViewMediatorCallback viewMediatorCallback, InitController initController, Handler handler, PluginDependencyProvider pluginDependencyProvider, KeyguardDismissUtil keyguardDismissUtil, ExtensionController extensionController, UserInfoControllerImpl userInfoControllerImpl, PhoneStatusBarPolicy phoneStatusBarPolicy, KeyguardIndicationController keyguardIndicationController, DemoModeController demoModeController, Lazy lazy4, StatusBarTouchableRegionManager statusBarTouchableRegionManager, NotificationIconAreaController notificationIconAreaController, BrightnessSliderController.Factory factory2, ScreenOffAnimationController screenOffAnimationController, WallpaperController wallpaperController, OngoingCallController ongoingCallController, StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, FeatureFlags featureFlags, KeyguardUnlockAnimationController keyguardUnlockAnimationController, DelayableExecutor delayableExecutor, MessageRouter messageRouter, WallpaperManager wallpaperManager, Optional optional3, ActivityLaunchAnimator activityLaunchAnimator, NotifPipelineFlags notifPipelineFlags, InteractionJankMonitor interactionJankMonitor, DeviceStateManager deviceStateManager, DreamOverlayStateController dreamOverlayStateController, final WiredChargingRippleController wiredChargingRippleController) {
        super(context);
        this.mNotificationsController = notificationsController;
        this.mFragmentService = fragmentService;
        this.mLightBarController = lightBarController;
        this.mAutoHideController = autoHideController;
        this.mStatusBarWindowController = statusBarWindowController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mPulseExpansionHandler = pulseExpansionHandler;
        this.mWakeUpCoordinator = notificationWakeUpCoordinator;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mKeyguardStateController = keyguardStateController;
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mKeyguardIndicationController = keyguardIndicationController;
        this.mStatusBarTouchableRegionManager = statusBarTouchableRegionManager;
        this.mDynamicPrivacyController = dynamicPrivacyController;
        this.mFalsingCollector = falsingCollector;
        this.mFalsingManager = falsingManager;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mNotifShadeEventSource = notifShadeEventSource;
        this.mEntryManager = notificationEntryManager;
        this.mGutsManager = notificationGutsManager;
        this.mNotificationLogger = notificationLogger;
        this.mNotificationInterruptStateProvider = notificationInterruptStateProvider;
        this.mViewHierarchyManager = notificationViewHierarchyManager;
        this.mPanelExpansionStateManager = panelExpansionStateManager;
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mDisplayMetrics = displayMetrics;
        this.mMetricsLogger = metricsLogger;
        this.mUiBgExecutor = executor;
        this.mMediaManager = notificationMediaManager;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mUserSwitcherController = userSwitcherController;
        this.mNetworkController = networkController;
        this.mBatteryController = batteryController;
        this.mColorExtractor = sysuiColorExtractor;
        this.mScreenLifecycle = screenLifecycle;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mBubblesOptional = optional;
        this.mVisualStabilityManager = visualStabilityManager;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mNavigationBarController = navigationBarController;
        this.mAccessibilityFloatingMenuController = accessibilityFloatingMenuController;
        this.mAssistManagerLazy = lazy;
        this.mConfigurationController = configurationController;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mDozeServiceHost = dozeServiceHost;
        this.mPowerManager = powerManager;
        this.mDozeParameters = dozeParameters;
        this.mScrimController = scrimController;
        this.mLockscreenWallpaperLazy = lazy2;
        this.mLockscreenGestureLogger = lockscreenGestureLogger;
        this.mScreenPinningRequest = screenPinningRequest;
        this.mDozeScrimController = dozeScrimController;
        this.mBiometricUnlockControllerLazy = lazy3;
        this.mNotificationShadeDepthControllerLazy = lazy4;
        this.mVolumeComponent = volumeComponent;
        this.mCommandQueue = commandQueue;
        this.mStatusBarComponentFactory = factory;
        this.mPluginManager = pluginManager;
        this.mSplitScreenOptional = optional2;
        this.mStatusBarNotificationActivityStarterBuilder = builder;
        this.mShadeController = shadeController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mKeyguardViewMediatorCallback = viewMediatorCallback;
        this.mInitController = initController;
        this.mPluginDependencyProvider = pluginDependencyProvider;
        this.mKeyguardDismissUtil = keyguardDismissUtil;
        this.mExtensionController = extensionController;
        this.mUserInfoControllerImpl = userInfoControllerImpl;
        this.mIconPolicy = phoneStatusBarPolicy;
        this.mDemoModeController = demoModeController;
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mBrightnessSliderFactory = factory2;
        this.mWallpaperController = wallpaperController;
        this.mOngoingCallController = ongoingCallController;
        this.mStatusBarSignalPolicy = statusBarSignalPolicy;
        this.mStatusBarHideIconsForBouncerManager = statusBarHideIconsForBouncerManager;
        this.mFeatureFlags = featureFlags;
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
        this.mMainExecutor = delayableExecutor;
        this.mMessageRouter = messageRouter;
        this.mWallpaperManager = wallpaperManager;
        this.mJankMonitor = interactionJankMonitor;
        this.mDreamOverlayStateController = dreamOverlayStateController;
        this.mLockscreenShadeTransitionController = lockscreenShadeTransitionController;
        this.mStartingSurfaceOptional = optional3;
        this.mNotifPipelineFlags = notifPipelineFlags;
        Objects.requireNonNull(lockscreenShadeTransitionController);
        lockscreenShadeTransitionController.statusbar = this;
        StatusBarWindowStateListener statusBar$$ExternalSyntheticLambda13 = new StatusBarWindowStateListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda13
            @Override // com.android.systemui.statusbar.window.StatusBarWindowStateListener
            public final void onStatusBarWindowStateChanged(int i) {
                StatusBar statusBar = StatusBar.this;
                Objects.requireNonNull(statusBar);
                statusBar.mBubblesOptional.ifPresent(new PipController$$ExternalSyntheticLambda4(statusBar, 2));
                statusBar.mStatusBarWindowState = i;
            }
        };
        Objects.requireNonNull(statusBarWindowStateController);
        statusBarWindowStateController.listeners.add(statusBar$$ExternalSyntheticLambda13);
        this.mScreenOffAnimationController = screenOffAnimationController;
        panelExpansionStateManager.addExpansionListener(new PanelExpansionListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda12
            /* JADX WARN: Code restructure failed: missing block: B:28:0x0068, code lost:
                if (r8.unlockingWithSmartspaceTransition != false) goto L_0x006a;
             */
            @Override // com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final void onPanelExpansionChanged(float r7, boolean r8, boolean r9) {
                /*
                    r6 = this;
                    com.android.systemui.statusbar.phone.StatusBar r6 = com.android.systemui.statusbar.phone.StatusBar.this
                    long[] r8 = com.android.systemui.statusbar.phone.StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS
                    java.util.Objects.requireNonNull(r6)
                    boolean r8 = r6.isKeyguardShowing()
                    r0 = 1065353216(0x3f800000, float:1.0)
                    if (r8 == 0) goto L_0x0071
                    com.android.systemui.statusbar.policy.KeyguardStateController r8 = r6.mKeyguardStateController
                    boolean r8 = r8.canDismissLockScreen()
                    if (r8 == 0) goto L_0x0071
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = r6.mKeyguardViewMediator
                    java.util.Objects.requireNonNull(r8)
                    r1 = 0
                    r2 = r1
                L_0x001e:
                    android.util.SparseIntArray r3 = r8.mLastSimStates
                    int r3 = r3.size()
                    r4 = 1
                    if (r2 >= r3) goto L_0x0049
                    android.util.SparseIntArray r3 = r8.mLastSimStates
                    int r3 = r3.keyAt(r2)
                    android.util.SparseIntArray r5 = r8.mLastSimStates
                    int r3 = r5.get(r3)
                    boolean r5 = com.android.keyguard.KeyguardUpdateMonitor.DEBUG
                    r5 = 2
                    if (r3 == r5) goto L_0x0041
                    r5 = 3
                    if (r3 == r5) goto L_0x0041
                    r5 = 7
                    if (r3 != r5) goto L_0x003f
                    goto L_0x0041
                L_0x003f:
                    r3 = r1
                    goto L_0x0042
                L_0x0041:
                    r3 = r4
                L_0x0042:
                    if (r3 == 0) goto L_0x0046
                    r1 = r4
                    goto L_0x0049
                L_0x0046:
                    int r2 = r2 + 1
                    goto L_0x001e
                L_0x0049:
                    if (r1 != 0) goto L_0x0071
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r8 = r6.mNotificationPanelViewController
                    java.util.Objects.requireNonNull(r8)
                    boolean r8 = r8.mQsExpanded
                    if (r8 == 0) goto L_0x0057
                    if (r9 == 0) goto L_0x0057
                    goto L_0x0071
                L_0x0057:
                    if (r9 != 0) goto L_0x006a
                    com.android.systemui.keyguard.KeyguardViewMediator r8 = r6.mKeyguardViewMediator
                    boolean r8 = r8.isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe()
                    if (r8 != 0) goto L_0x006a
                    com.android.systemui.keyguard.KeyguardUnlockAnimationController r8 = r6.mKeyguardUnlockAnimationController
                    java.util.Objects.requireNonNull(r8)
                    boolean r8 = r8.unlockingWithSmartspaceTransition
                    if (r8 == 0) goto L_0x0071
                L_0x006a:
                    com.android.systemui.statusbar.policy.KeyguardStateController r8 = r6.mKeyguardStateController
                    float r1 = r0 - r7
                    r8.notifyKeyguardDismissAmountChanged(r1, r9)
                L_0x0071:
                    r8 = 0
                    int r8 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1))
                    if (r8 == 0) goto L_0x007a
                    int r7 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1))
                    if (r7 != 0) goto L_0x0091
                L_0x007a:
                    com.android.systemui.navigationbar.NavigationBarView r7 = r6.getNavigationBarView()
                    if (r7 == 0) goto L_0x008a
                    com.android.systemui.navigationbar.NavigationBarView r7 = r6.getNavigationBarView()
                    java.util.Objects.requireNonNull(r7)
                    r7.updateSlippery()
                L_0x008a:
                    com.android.systemui.statusbar.phone.NotificationPanelViewController r6 = r6.mNotificationPanelViewController
                    if (r6 == 0) goto L_0x0091
                    r6.updateSystemUiStateFlags()
                L_0x0091:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda12.onPanelExpansionChanged(float, boolean, boolean):void");
            }
        });
        this.mActivityLaunchAnimator = activityLaunchAnimator;
        ongoingCallController.addCallback(new OngoingCallListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda11
            @Override // com.android.systemui.statusbar.phone.ongoingcall.OngoingCallListener
            public final void onOngoingCallStateChanged() {
                StatusBar statusBar = StatusBar.this;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.maybeUpdateBarMode();
            }
        });
        DateTimeView.setReceiverHandler(handler);
        messageRouter.subscribeTo(KeyboardShortcutsMessage.class, new MessageRouter.DataMessageListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda15
            @Override // com.android.systemui.util.concurrency.MessageRouter.DataMessageListener
            public final void onMessage(Object obj) {
                boolean z;
                AlertDialog alertDialog;
                StatusBar statusBar = StatusBar.this;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                int i = ((StatusBar.KeyboardShortcutsMessage) obj).mDeviceId;
                Context context2 = statusBar.mContext;
                synchronized (KeyboardShortcuts.sLock) {
                    KeyboardShortcuts keyboardShortcuts = KeyboardShortcuts.sInstance;
                    if (keyboardShortcuts == null || (alertDialog = keyboardShortcuts.mKeyboardShortcutsDialog) == null || !alertDialog.isShowing()) {
                        z = false;
                    } else {
                        z = true;
                    }
                    if (z) {
                        KeyboardShortcuts.dismiss();
                    } else {
                        KeyboardShortcuts.show(context2, i);
                    }
                }
            }
        });
        messageRouter.subscribeTo(1027, new MessageRouter.SimpleMessageListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda16
            @Override // com.android.systemui.util.concurrency.MessageRouter.SimpleMessageListener
            public final void onMessage() {
                StatusBar statusBar = StatusBar.this;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                KeyboardShortcuts.dismiss();
            }
        });
        messageRouter.subscribeTo(AnimateExpandSettingsPanelMessage.class, new MessageRouter.DataMessageListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda14
            @Override // com.android.systemui.util.concurrency.MessageRouter.DataMessageListener
            public final void onMessage(Object obj) {
                StatusBar statusBar = StatusBar.this;
                StatusBar.AnimateExpandSettingsPanelMessage animateExpandSettingsPanelMessage = (StatusBar.AnimateExpandSettingsPanelMessage) obj;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.mCommandQueueCallbacks.animateExpandSettingsPanel(null);
            }
        });
        messageRouter.subscribeTo(1003, new MessageRouter.SimpleMessageListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda17
            @Override // com.android.systemui.util.concurrency.MessageRouter.SimpleMessageListener
            public final void onMessage() {
                StatusBar statusBar = StatusBar.this;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                Log.w("StatusBar", "Launch transition: Timeout!");
                NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
                Objects.requireNonNull(notificationPanelViewController);
                notificationPanelViewController.setLaunchingAffordance(false);
                statusBar.releaseGestureWakeLock();
                statusBar.mNotificationPanelViewController.resetViews(false);
            }
        });
        deviceStateManager.registerCallback(delayableExecutor, new FoldStateListener(this.mContext, new StatusBar$$ExternalSyntheticLambda8(this)));
        Objects.requireNonNull(wiredChargingRippleController);
        wiredChargingRippleController.batteryController.addCallback(new BatteryController.BatteryStateChangeCallback() { // from class: com.android.systemui.statusbar.charging.WiredChargingRippleController$registerCallbacks$batteryStateChangeCallback$1
            @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
            public final void onBatteryLevelChanged(int i, boolean z, boolean z2) {
                if (!WiredChargingRippleController.this.batteryController.isPluggedInWireless()) {
                    WiredChargingRippleController wiredChargingRippleController2 = WiredChargingRippleController.this;
                    Boolean bool = wiredChargingRippleController2.pluggedIn;
                    wiredChargingRippleController2.pluggedIn = Boolean.valueOf(z);
                    if ((bool == null || !bool.booleanValue()) && z) {
                        WiredChargingRippleController wiredChargingRippleController3 = WiredChargingRippleController.this;
                        Objects.requireNonNull(wiredChargingRippleController3);
                        long elapsedRealtime = wiredChargingRippleController3.systemClock.elapsedRealtime();
                        Long l = wiredChargingRippleController3.lastTriggerTime;
                        if (l != null) {
                            if (elapsedRealtime - l.longValue() <= Math.pow(2.0d, wiredChargingRippleController3.debounceLevel) * 2000) {
                                wiredChargingRippleController3.debounceLevel = Math.min(3, wiredChargingRippleController3.debounceLevel + 1);
                                wiredChargingRippleController3.lastTriggerTime = Long.valueOf(elapsedRealtime);
                            }
                        }
                        wiredChargingRippleController3.startRipple();
                        wiredChargingRippleController3.debounceLevel = 0;
                        wiredChargingRippleController3.lastTriggerTime = Long.valueOf(elapsedRealtime);
                    }
                }
            }
        });
        wiredChargingRippleController.configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.charging.WiredChargingRippleController$registerCallbacks$configurationChangedListener$1
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public final void onConfigChanged(Configuration configuration) {
                WiredChargingRippleController wiredChargingRippleController2 = WiredChargingRippleController.this;
                wiredChargingRippleController2.normalizedPortPosX = wiredChargingRippleController2.context.getResources().getFloat(2131166765);
                WiredChargingRippleController wiredChargingRippleController3 = WiredChargingRippleController.this;
                wiredChargingRippleController3.normalizedPortPosY = wiredChargingRippleController3.context.getResources().getFloat(2131166766);
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public final void onThemeChanged() {
                WiredChargingRippleController.this.updateRippleColor();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public final void onUiModeChanged() {
                WiredChargingRippleController.this.updateRippleColor();
            }
        });
    }

    public final void executeRunnableDismissingKeyguard(Runnable runnable, boolean z, boolean z2, boolean z3) {
        executeRunnableDismissingKeyguard(runnable, null, z, z2, z3, false);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void postStartActivityDismissingKeyguard(PendingIntent pendingIntent) {
        postStartActivityDismissingKeyguard(pendingIntent, (ActivityLaunchAnimator.Controller) null);
    }

    public void showWirelessChargingAnimation(int i) {
        showChargingAnimation(i, -1, 0L);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startActivity(Intent intent, boolean z, boolean z2, int i) {
        startActivityDismissingKeyguard(intent, z, z2, i);
    }

    public final void startActivityDismissingKeyguard(Intent intent, boolean z, boolean z2, int i) {
        startActivityDismissingKeyguard(intent, z, z2, false, null, i, null);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent) {
        startPendingIntentDismissingKeyguard(pendingIntent, null);
    }

    public final boolean updateIsKeyguard() {
        return updateIsKeyguard(false);
    }

    /* loaded from: classes.dex */
    public static class KeyboardShortcutsMessage {
        public final int mDeviceId;

        public KeyboardShortcutsMessage(int i) {
            this.mDeviceId = i;
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum StatusBarUiEvent implements UiEventLogger.UiEventEnum {
        /* JADX INFO: Fake field, exist only in values array */
        LOCKSCREEN_OPEN_SECURE(405),
        /* JADX INFO: Fake field, exist only in values array */
        LOCKSCREEN_OPEN_INSECURE(406),
        /* JADX INFO: Fake field, exist only in values array */
        LOCKSCREEN_CLOSE_SECURE(407),
        /* JADX INFO: Fake field, exist only in values array */
        LOCKSCREEN_CLOSE_INSECURE(408),
        /* JADX INFO: Fake field, exist only in values array */
        BOUNCER_OPEN_SECURE(409),
        /* JADX INFO: Fake field, exist only in values array */
        BOUNCER_OPEN_INSECURE(410),
        /* JADX INFO: Fake field, exist only in values array */
        BOUNCER_CLOSE_SECURE(411),
        /* JADX INFO: Fake field, exist only in values array */
        BOUNCER_CLOSE_INSECURE(412);
        
        private final int mId;

        StatusBarUiEvent(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    public static void dumpBarTransitions(PrintWriter printWriter, String str, BarTransitions barTransitions) {
        printWriter.print("  ");
        printWriter.print(str);
        printWriter.print(".BarTransitions.mMode=");
        if (barTransitions != null) {
            printWriter.println(BarTransitions.modeToString(barTransitions.mMode));
        } else {
            printWriter.println("Unknown");
        }
    }

    public static ActivityOptions getDefaultActivityOptions(RemoteAnimationAdapter remoteAnimationAdapter) {
        if (remoteAnimationAdapter == null) {
            return ActivityOptions.makeBasic();
        }
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            return ActivityOptions.makeRemoteAnimation(remoteAnimationAdapter);
        }
        final IRemoteAnimationRunner runner = remoteAnimationAdapter.getRunner();
        return ActivityOptions.makeRemoteTransition(new RemoteTransition(new IRemoteTransition.Stub() { // from class: com.android.systemui.animation.RemoteTransitionAdapter$Companion$adaptRemoteRunner$1
            public final void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
            }

            /* JADX WARN: Removed duplicated region for block: B:28:0x00a4 A[LOOP:0: B:5:0x002b->B:28:0x00a4, LOOP_END] */
            /* JADX WARN: Removed duplicated region for block: B:62:0x00a1 A[EDGE_INSN: B:62:0x00a1->B:27:0x00a1 ?: BREAK  , SYNTHETIC] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final void startAnimation(android.os.IBinder r19, final android.window.TransitionInfo r20, android.view.SurfaceControl.Transaction r21, final android.window.IRemoteTransitionFinishedCallback r22) {
                /*
                    Method dump skipped, instructions count: 396
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.animation.RemoteTransitionAdapter$Companion$adaptRemoteRunner$1.startAnimation(android.os.IBinder, android.window.TransitionInfo, android.view.SurfaceControl$Transaction, android.window.IRemoteTransitionFinishedCallback):void");
            }
        }, remoteAnimationAdapter.getCallingApplication()));
    }

    public static PackageManager getPackageManagerForUser(Context context, int i) {
        if (i >= 0) {
            try {
                context = context.createPackageContextAsUser(context.getPackageName(), 4, new UserHandle(i));
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return context.getPackageManager();
    }

    public static String viewInfo(ViewGroup viewGroup) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("[(");
        m.append(viewGroup.getLeft());
        m.append(",");
        m.append(viewGroup.getTop());
        m.append(")(");
        m.append(viewGroup.getRight());
        m.append(",");
        m.append(viewGroup.getBottom());
        m.append(") ");
        m.append(viewGroup.getWidth());
        m.append("x");
        m.append(viewGroup.getHeight());
        m.append("]");
        return m.toString();
    }

    public final int barMode(boolean z, int i) {
        if ((this.mOngoingCallController.hasOngoingCall() && this.mIsFullscreen) || z) {
            return 1;
        }
        if ((i & 5) == 5) {
            return 3;
        }
        if ((i & 4) != 0) {
            return 6;
        }
        if ((i & 1) != 0) {
            return 4;
        }
        if ((i & 32) != 0) {
            return 1;
        }
        return 0;
    }

    public final void checkBarModes() {
        boolean z;
        DemoModeController demoModeController = this.mDemoModeController;
        Objects.requireNonNull(demoModeController);
        if (!demoModeController.isInDemoMode) {
            PhoneStatusBarTransitions phoneStatusBarTransitions = this.mStatusBarTransitions;
            if (phoneStatusBarTransitions != null) {
                int i = this.mStatusBarMode;
                int i2 = this.mStatusBarWindowState;
                if (this.mNoAnimationOnNextBarModeChange || !this.mDeviceInteractive || i2 == 2) {
                    z = false;
                } else {
                    z = true;
                }
                int i3 = phoneStatusBarTransitions.mMode;
                if (i3 != i) {
                    phoneStatusBarTransitions.mMode = i;
                    phoneStatusBarTransitions.onTransition(i3, i, z);
                }
            }
            NavigationBarController navigationBarController = this.mNavigationBarController;
            int i4 = this.mDisplayId;
            Objects.requireNonNull(navigationBarController);
            NavigationBar navigationBar = navigationBarController.mNavigationBars.get(i4);
            if (navigationBar != null) {
                navigationBar.checkNavBarModes();
            }
            this.mNoAnimationOnNextBarModeChange = false;
        }
    }

    public final void collapseShade() {
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        if (notificationPanelViewController.mTracking) {
            this.mNotificationShadeWindowViewController.cancelCurrentTouch();
        }
        if (this.mPanelExpanded && this.mState == 0) {
            this.mShadeController.animateCollapsePanels();
        }
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void dismissKeyguardThenExecute(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z) {
        WakefulnessLifecycle wakefulnessLifecycle = this.mWakefulnessLifecycle;
        Objects.requireNonNull(wakefulnessLifecycle);
        if (wakefulnessLifecycle.mWakefulness == 0 && this.mKeyguardStateController.canDismissLockScreen() && !this.mStatusBarStateController.leaveOpenOnKeyguardHide()) {
            DozeServiceHost dozeServiceHost = this.mDozeServiceHost;
            Objects.requireNonNull(dozeServiceHost);
            if (dozeServiceHost.mPulsing) {
                this.mBiometricUnlockController.startWakeAndUnlock(2);
            }
        }
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        Objects.requireNonNull(statusBarKeyguardViewManager);
        if (statusBarKeyguardViewManager.mShowing) {
            StatusBarKeyguardViewManager statusBarKeyguardViewManager2 = this.mStatusBarKeyguardViewManager;
            Objects.requireNonNull(statusBarKeyguardViewManager2);
            statusBarKeyguardViewManager2.dismissWithAction(onDismissAction, runnable, z, null);
            return;
        }
        onDismissAction.onDismiss();
    }

    public final void executeRunnableDismissingKeyguard(final Runnable runnable, KeyguardDisplayManager$$ExternalSyntheticLambda1 keyguardDisplayManager$$ExternalSyntheticLambda1, final boolean z, boolean z2, final boolean z3, final boolean z4) {
        dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.statusbar.phone.StatusBar.7
            {
                StatusBar.this = this;
            }

            @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
            public final boolean onDismiss() {
                if (runnable != null) {
                    StatusBarKeyguardViewManager statusBarKeyguardViewManager = StatusBar.this.mStatusBarKeyguardViewManager;
                    Objects.requireNonNull(statusBarKeyguardViewManager);
                    if (statusBarKeyguardViewManager.mShowing) {
                        StatusBarKeyguardViewManager statusBarKeyguardViewManager2 = StatusBar.this.mStatusBarKeyguardViewManager;
                        Objects.requireNonNull(statusBarKeyguardViewManager2);
                        if (statusBarKeyguardViewManager2.mOccluded) {
                            StatusBarKeyguardViewManager statusBarKeyguardViewManager3 = StatusBar.this.mStatusBarKeyguardViewManager;
                            Runnable runnable2 = runnable;
                            Objects.requireNonNull(statusBarKeyguardViewManager3);
                            statusBarKeyguardViewManager3.mAfterKeyguardGoneRunnables.add(runnable2);
                        }
                    }
                    StatusBar.this.mMainExecutor.execute(runnable);
                }
                if (z) {
                    StatusBar statusBar = StatusBar.this;
                    if (!statusBar.mExpandedVisible || statusBar.mBouncerShowing) {
                        DelayableExecutor delayableExecutor = statusBar.mMainExecutor;
                        ShadeController shadeController = statusBar.mShadeController;
                        Objects.requireNonNull(shadeController);
                        delayableExecutor.execute(new LockIconViewController$$ExternalSyntheticLambda1(shadeController, 6));
                    } else {
                        statusBar.mShadeController.animateCollapsePanels$1();
                    }
                } else if (StatusBar.this.isInLaunchTransition()) {
                    NotificationPanelViewController notificationPanelViewController = StatusBar.this.mNotificationPanelViewController;
                    Objects.requireNonNull(notificationPanelViewController);
                    if (notificationPanelViewController.mIsLaunchTransitionFinished) {
                        StatusBar statusBar2 = StatusBar.this;
                        DelayableExecutor delayableExecutor2 = statusBar2.mMainExecutor;
                        StatusBarKeyguardViewManager statusBarKeyguardViewManager4 = statusBar2.mStatusBarKeyguardViewManager;
                        Objects.requireNonNull(statusBarKeyguardViewManager4);
                        delayableExecutor2.execute(new KeyguardStatusView$$ExternalSyntheticLambda0(statusBarKeyguardViewManager4, 5));
                    }
                }
                return z3;
            }

            @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
            public final boolean willRunAnimationOnKeyguard() {
                return z4;
            }
        }, keyguardDisplayManager$$ExternalSyntheticLambda1, z2);
    }

    public final void fadeKeyguardAfterLaunchTransition(StatusBarKeyguardViewManager.AnonymousClass5 r3, Runnable runnable) {
        this.mMessageRouter.cancelMessages(1003);
        this.mLaunchTransitionEndRunnable = runnable;
        StatusBar$$ExternalSyntheticLambda22 statusBar$$ExternalSyntheticLambda22 = new StatusBar$$ExternalSyntheticLambda22(this, r3, 0);
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        if (notificationPanelViewController.mIsLaunchTransitionRunning) {
            NotificationPanelViewController notificationPanelViewController2 = this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController2);
            notificationPanelViewController2.mLaunchAnimationEndRunnable = statusBar$$ExternalSyntheticLambda22;
            return;
        }
        statusBar$$ExternalSyntheticLambda22.run();
    }

    public final ViewGroup getBouncerContainer() {
        NotificationShadeWindowViewController notificationShadeWindowViewController = this.mNotificationShadeWindowViewController;
        Objects.requireNonNull(notificationShadeWindowViewController);
        return (ViewGroup) notificationShadeWindowViewController.mView.findViewById(2131428167);
    }

    public final Intent getEmergencyActionIntent() {
        ResolveInfo resolveInfo;
        Intent intent = new Intent("com.android.systemui.action.LAUNCH_EMERGENCY");
        List<ResolveInfo> queryIntentActivities = this.mContext.getPackageManager().queryIntentActivities(intent, 1048576);
        if (queryIntentActivities != null && !queryIntentActivities.isEmpty()) {
            String string = this.mContext.getString(2131952142);
            if (!TextUtils.isEmpty(string)) {
                Iterator<ResolveInfo> it = queryIntentActivities.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        resolveInfo = queryIntentActivities.get(0);
                        break;
                    }
                    ResolveInfo next = it.next();
                    if (TextUtils.equals(next.activityInfo.packageName, string)) {
                        resolveInfo = next;
                        break;
                    }
                }
            } else {
                resolveInfo = queryIntentActivities.get(0);
            }
        } else {
            resolveInfo = null;
        }
        if (resolveInfo == null) {
            Log.wtf("StatusBar", "Couldn't find an app to process the emergency intent.");
            return null;
        }
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        intent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
        intent.setFlags(268435456);
        return intent;
    }

    public final NavigationBarView getNavigationBarView() {
        return this.mNavigationBarController.getNavigationBarView(this.mDisplayId);
    }

    public final void handleVisibleToUserChangedImpl(boolean z) {
        boolean z2;
        int i;
        if (z) {
            HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
            Objects.requireNonNull(headsUpManagerPhone);
            boolean z3 = headsUpManagerPhone.mHasPinnedNotification;
            int i2 = 1;
            if (this.mPresenter.isPresenterFullyCollapsed() || !((i = this.mState) == 0 || i == 2)) {
                z2 = false;
            } else {
                z2 = true;
            }
            int activeNotificationsCount = this.mNotificationsController.getActiveNotificationsCount();
            if (!z3 || !this.mPresenter.isPresenterFullyCollapsed()) {
                i2 = activeNotificationsCount;
            }
            this.mUiBgExecutor.execute(new StatusBar$$ExternalSyntheticLambda24(this, z2, i2));
            return;
        }
        this.mUiBgExecutor.execute(new QSTileImpl$$ExternalSyntheticLambda0(this, 5));
    }

    public final boolean hideKeyguard() {
        this.mStatusBarStateController.setKeyguardRequested(false);
        return updateIsKeyguard(false);
    }

    public final void instantCollapseNotificationPanel() {
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        notificationPanelViewController.cancelHeightAnimator();
        ((PanelViewController) notificationPanelViewController).mView.removeCallbacks(notificationPanelViewController.mFlingCollapseRunnable);
        notificationPanelViewController.setExpandedHeightInternal(notificationPanelViewController.getMaxPanelHeight() * 0.0f);
        if (notificationPanelViewController.mExpanding) {
            notificationPanelViewController.notifyExpandingFinished();
        }
        if (notificationPanelViewController.mInstantExpanding) {
            notificationPanelViewController.mInstantExpanding = false;
            notificationPanelViewController.updatePanelExpansionAndVisibility();
        }
        this.mShadeController.runPostCollapseRunnables();
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0043, code lost:
        if (r0 == false) goto L_0x0046;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean isCameraAllowedByAdmin() {
        /*
            r6 = this;
            android.app.admin.DevicePolicyManager r0 = r6.mDevicePolicyManager
            com.android.systemui.statusbar.NotificationLockscreenUserManager r1 = r6.mLockscreenUserManager
            int r1 = r1.getCurrentUserId()
            r2 = 0
            boolean r0 = r0.getCameraDisabled(r2, r1)
            r1 = 0
            if (r0 == 0) goto L_0x0011
            return r1
        L_0x0011:
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r0 = r6.mStatusBarKeyguardViewManager
            r3 = 1
            if (r0 == 0) goto L_0x0047
            boolean r0 = r6.isKeyguardShowing()
            if (r0 == 0) goto L_0x0046
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r0 = r6.mStatusBarKeyguardViewManager
            if (r0 != 0) goto L_0x002d
            java.lang.Throwable r0 = new java.lang.Throwable
            r0.<init>()
            java.lang.String r4 = "StatusBar"
            java.lang.String r5 = "isKeyguardSecure() called before startKeyguard(), returning false"
            android.util.Slog.w(r4, r5, r0)
            goto L_0x0042
        L_0x002d:
            com.android.systemui.statusbar.phone.KeyguardBouncer r0 = r0.mBouncer
            java.util.Objects.requireNonNull(r0)
            com.android.keyguard.KeyguardSecurityModel r0 = r0.mKeyguardSecurityModel
            int r4 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r0 = r0.getSecurityMode(r4)
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r4 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.None
            if (r0 == r4) goto L_0x0042
            r0 = r3
            goto L_0x0043
        L_0x0042:
            r0 = r1
        L_0x0043:
            if (r0 == 0) goto L_0x0046
            goto L_0x0047
        L_0x0046:
            return r3
        L_0x0047:
            android.app.admin.DevicePolicyManager r0 = r6.mDevicePolicyManager
            com.android.systemui.statusbar.NotificationLockscreenUserManager r6 = r6.mLockscreenUserManager
            int r6 = r6.getCurrentUserId()
            int r6 = r0.getKeyguardDisabledFeatures(r2, r6)
            r6 = r6 & 2
            if (r6 != 0) goto L_0x0058
            r1 = r3
        L_0x0058:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBar.isCameraAllowedByAdmin():boolean");
    }

    public final boolean isGoingToSleep() {
        WakefulnessLifecycle wakefulnessLifecycle = this.mWakefulnessLifecycle;
        Objects.requireNonNull(wakefulnessLifecycle);
        if (wakefulnessLifecycle.mWakefulness == 3) {
            return true;
        }
        return false;
    }

    public final boolean isInLaunchTransition() {
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        if (!notificationPanelViewController.mIsLaunchTransitionRunning) {
            NotificationPanelViewController notificationPanelViewController2 = this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController2);
            if (!notificationPanelViewController2.mIsLaunchTransitionFinished) {
                return false;
            }
        }
        return true;
    }

    public final boolean isKeyguardShowing() {
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        if (statusBarKeyguardViewManager == null) {
            Slog.i("StatusBar", "isKeyguardShowing() called before startKeyguard(), returning true");
            return true;
        }
        Objects.requireNonNull(statusBarKeyguardViewManager);
        return statusBarKeyguardViewManager.mShowing;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r1v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r2v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r3v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r4v1, types: [int, boolean] */
    /* JADX WARN: Unknown variable types count: 5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void logStateToEventlog() {
        /*
            r12 = this;
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r0 = r12.mStatusBarKeyguardViewManager
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.mShowing
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r1 = r12.mStatusBarKeyguardViewManager
            java.util.Objects.requireNonNull(r1)
            boolean r1 = r1.mOccluded
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r2 = r12.mStatusBarKeyguardViewManager
            boolean r2 = r2.isBouncerShowing()
            com.android.systemui.statusbar.policy.KeyguardStateController r3 = r12.mKeyguardStateController
            boolean r3 = r3.isMethodSecure()
            com.android.systemui.statusbar.policy.KeyguardStateController r4 = r12.mKeyguardStateController
            boolean r4 = r4.canDismissLockScreen()
            int r5 = r12.mState
            r5 = r5 & 255(0xff, float:3.57E-43)
            int r6 = r0 << 8
            r5 = r5 | r6
            int r6 = r1 << 9
            r5 = r5 | r6
            int r6 = r2 << 10
            r5 = r5 | r6
            int r6 = r3 << 11
            r5 = r5 | r6
            int r6 = r4 << 12
            r5 = r5 | r6
            int r6 = r12.mLastLoggedStateFingerprint
            if (r5 == r6) goto L_0x00c8
            android.metrics.LogMaker r6 = r12.mStatusBarStateLog
            r7 = 0
            if (r6 != 0) goto L_0x0043
            android.metrics.LogMaker r6 = new android.metrics.LogMaker
            r6.<init>(r7)
            r12.mStatusBarStateLog = r6
        L_0x0043:
            com.android.internal.logging.MetricsLogger r6 = r12.mMetricsLogger
            android.metrics.LogMaker r8 = r12.mStatusBarStateLog
            if (r2 == 0) goto L_0x004c
            r9 = 197(0xc5, float:2.76E-43)
            goto L_0x004e
        L_0x004c:
            r9 = 196(0xc4, float:2.75E-43)
        L_0x004e:
            android.metrics.LogMaker r8 = r8.setCategory(r9)
            r9 = 1
            r10 = 2
            if (r0 == 0) goto L_0x0058
            r11 = r9
            goto L_0x0059
        L_0x0058:
            r11 = r10
        L_0x0059:
            android.metrics.LogMaker r8 = r8.setType(r11)
            android.metrics.LogMaker r8 = r8.setSubtype(r3)
            r6.write(r8)
            int r6 = r12.mState
            r8 = 6
            java.lang.Object[] r8 = new java.lang.Object[r8]
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
            r8[r7] = r6
            java.lang.Integer r6 = java.lang.Integer.valueOf(r0)
            r8[r9] = r6
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            r8[r10] = r1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)
            r6 = 3
            r8[r6] = r1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r3)
            r6 = 4
            r8[r6] = r1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)
            r4 = 5
            r8[r4] = r1
            r1 = 36004(0x8ca4, float:5.0452E-41)
            android.util.EventLog.writeEvent(r1, r8)
            r12.mLastLoggedStateFingerprint = r5
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            if (r2 == 0) goto L_0x00a2
            java.lang.String r1 = "BOUNCER"
            goto L_0x00a4
        L_0x00a2:
            java.lang.String r1 = "LOCKSCREEN"
        L_0x00a4:
            r12.append(r1)
            if (r0 == 0) goto L_0x00ac
            java.lang.String r0 = "_OPEN"
            goto L_0x00ae
        L_0x00ac:
            java.lang.String r0 = "_CLOSE"
        L_0x00ae:
            r12.append(r0)
            if (r3 == 0) goto L_0x00b6
            java.lang.String r0 = "_SECURE"
            goto L_0x00b8
        L_0x00b6:
            java.lang.String r0 = "_INSECURE"
        L_0x00b8:
            r12.append(r0)
            com.android.internal.logging.UiEventLoggerImpl r0 = com.android.systemui.statusbar.phone.StatusBar.sUiEventLogger
            java.lang.String r12 = r12.toString()
            com.android.systemui.statusbar.phone.StatusBar$StatusBarUiEvent r12 = com.android.systemui.statusbar.phone.StatusBar.StatusBarUiEvent.valueOf(r12)
            r0.log(r12)
        L_0x00c8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBar.logStateToEventlog():void");
    }

    public final void makeExpandedInvisible() {
        if (this.mExpandedVisible && this.mNotificationShadeWindowView != null) {
            this.mNotificationPanelViewController.collapsePanel(false, false, 1.0f);
            NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController);
            ValueAnimator valueAnimator = notificationPanelViewController.mQsExpansionAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            notificationPanelViewController.setQsExpansion(notificationPanelViewController.mQsMinExpansionHeight);
            this.mExpandedVisible = false;
            visibilityChanged(false);
            this.mNotificationShadeWindowController.setPanelVisible(false);
            StatusBarWindowController statusBarWindowController = this.mStatusBarWindowController;
            Objects.requireNonNull(statusBarWindowController);
            StatusBarWindowController.State state = statusBarWindowController.mCurrentState;
            state.mForceStatusBarVisible = false;
            statusBarWindowController.apply(state);
            this.mGutsManager.closeAndSaveGuts(true, true, true, true);
            this.mShadeController.runPostCollapseRunnables();
            setInteracting(1, false);
            StatusBarNotificationActivityStarter statusBarNotificationActivityStarter = this.mNotificationActivityStarter;
            Objects.requireNonNull(statusBarNotificationActivityStarter);
            if (!statusBarNotificationActivityStarter.mIsCollapsingToShowActivityOverLockscreen) {
                KeyguardViewMediator keyguardViewMediator = this.mKeyguardViewMediator;
                Objects.requireNonNull(keyguardViewMediator);
                if (!keyguardViewMediator.mHiding) {
                    KeyguardUnlockAnimationController keyguardUnlockAnimationController = this.mKeyguardUnlockAnimationController;
                    Objects.requireNonNull(keyguardUnlockAnimationController);
                    if (!keyguardUnlockAnimationController.playingCannedUnlockAnimation) {
                        if (this.mState == 2 && this.mKeyguardUpdateMonitor.isUdfpsEnrolled()) {
                            this.mStatusBarKeyguardViewManager.reset(true);
                        } else if ((this.mState == 1 && !this.mStatusBarKeyguardViewManager.bouncerIsOrWillBeShowing()) || this.mState == 2) {
                            this.mStatusBarKeyguardViewManager.showGenericBouncer();
                        }
                    }
                }
            }
            this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, this.mNotificationPanelViewController.hideStatusBarIconsWhenExpanded());
            StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
            Objects.requireNonNull(statusBarKeyguardViewManager);
            if (!statusBarKeyguardViewManager.mShowing) {
                WindowManagerGlobal.getInstance().trimMemory(20);
            }
        }
    }

    public final void makeExpandedVisible(boolean z) {
        if (z || (!this.mExpandedVisible && this.mCommandQueue.panelsEnabled())) {
            this.mExpandedVisible = true;
            this.mNotificationShadeWindowController.setPanelVisible(true);
            visibilityChanged(true);
            this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, !z);
            setInteracting(1, true);
        }
    }

    public final void maybeUpdateBarMode() {
        boolean z;
        int barMode = barMode(this.mTransientShown, this.mAppearance);
        if (this.mStatusBarMode != barMode) {
            this.mStatusBarMode = barMode;
            checkBarModes();
            this.mAutoHideController.touchAutoHide();
            z = true;
        } else {
            z = false;
        }
        if (z) {
            this.mLightBarController.onStatusBarModeChanged(barMode);
            this.mBubblesOptional.ifPresent(new PipController$$ExternalSyntheticLambda4(this, 2));
        }
    }

    public final boolean onBackPressed() {
        boolean z;
        boolean z2;
        boolean z3;
        ScrimController scrimController = this.mScrimController;
        Objects.requireNonNull(scrimController);
        if (scrimController.mState == ScrimState.BOUNCER_SCRIMMED) {
            z = true;
        } else {
            z = false;
        }
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        Objects.requireNonNull(statusBarKeyguardViewManager);
        if (statusBarKeyguardViewManager.mBouncer.isShowing()) {
            StatusBar statusBar = statusBarKeyguardViewManager.mStatusBar;
            Objects.requireNonNull(statusBar);
            statusBar.releaseGestureWakeLock();
            NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController);
            notificationPanelViewController.setLaunchingAffordance(false);
            KeyguardBouncer keyguardBouncer = statusBarKeyguardViewManager.mBouncer;
            Objects.requireNonNull(keyguardBouncer);
            if (!keyguardBouncer.mIsScrimmed || statusBarKeyguardViewManager.mBouncer.needsFullscreenBouncer()) {
                statusBarKeyguardViewManager.reset(z);
            } else {
                statusBarKeyguardViewManager.hideBouncer(false);
                statusBarKeyguardViewManager.updateStates();
            }
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            if (z) {
                this.mStatusBarStateController.setLeaveOpenOnKeyguardHide(false);
            } else {
                NotificationPanelViewController notificationPanelViewController2 = this.mNotificationPanelViewController;
                Objects.requireNonNull(notificationPanelViewController2);
                if (notificationPanelViewController2.mQsExpanded) {
                    notificationPanelViewController2.flingSettings(0.0f, 1, null, false);
                } else {
                    notificationPanelViewController2.expand(true);
                }
            }
            return true;
        }
        NotificationPanelViewController notificationPanelViewController3 = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController3);
        if (notificationPanelViewController3.mQs.isCustomizing()) {
            NotificationPanelViewController notificationPanelViewController4 = this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController4);
            notificationPanelViewController4.mQs.closeCustomizer();
            return true;
        }
        NotificationPanelViewController notificationPanelViewController5 = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController5);
        if (notificationPanelViewController5.mQsExpanded) {
            NotificationPanelViewController notificationPanelViewController6 = this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController6);
            if (notificationPanelViewController6.mQs.isShowingDetail()) {
                NotificationPanelViewController notificationPanelViewController7 = this.mNotificationPanelViewController;
                Objects.requireNonNull(notificationPanelViewController7);
                notificationPanelViewController7.mQs.closeDetail();
            } else {
                NotificationPanelViewController notificationPanelViewController8 = this.mNotificationPanelViewController;
                Objects.requireNonNull(notificationPanelViewController8);
                ValueAnimator valueAnimator = notificationPanelViewController8.mQsExpansionAnimator;
                if (valueAnimator != null) {
                    if (notificationPanelViewController8.mQsAnimatorExpand) {
                        float f = notificationPanelViewController8.mQsExpansionHeight;
                        valueAnimator.cancel();
                        notificationPanelViewController8.setQsExpansion(f);
                    }
                }
                notificationPanelViewController8.flingSettings(0.0f, 1, null, false);
            }
            return true;
        }
        NotificationPanelViewController notificationPanelViewController9 = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController9);
        KeyguardUserSwitcherController keyguardUserSwitcherController = notificationPanelViewController9.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            z3 = keyguardUserSwitcherController.closeSwitcherIfOpenAndNotSimple(true);
        } else {
            z3 = false;
        }
        if (z3) {
            return true;
        }
        int i = this.mState;
        if (i == 1 || i == 2) {
            return false;
        }
        if (this.mNotificationPanelViewController.canPanelBeCollapsed()) {
            this.mShadeController.animateCollapsePanels();
        }
        return true;
    }

    public final void onClosingFinished() {
        this.mShadeController.runPostCollapseRunnables();
        if (!this.mPresenter.isPresenterFullyCollapsed()) {
            this.mNotificationShadeWindowController.setNotificationShadeFocusable(true);
        }
    }

    public final void onInputFocusTransfer(boolean z, boolean z2, float f) {
        float f2;
        if (this.mCommandQueue.panelsEnabled()) {
            if (z) {
                NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
                Objects.requireNonNull(notificationPanelViewController);
                if (notificationPanelViewController.isFullyCollapsed()) {
                    notificationPanelViewController.mExpectingSynthesizedDown = true;
                    notificationPanelViewController.onTrackingStarted();
                    notificationPanelViewController.updatePanelExpanded();
                    return;
                }
                return;
            }
            NotificationPanelViewController notificationPanelViewController2 = this.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController2);
            if (notificationPanelViewController2.mExpectingSynthesizedDown) {
                notificationPanelViewController2.mExpectingSynthesizedDown = false;
                if (z2) {
                    notificationPanelViewController2.collapse(false, 1.0f);
                } else {
                    if (notificationPanelViewController2.mVibrateOnOpening) {
                        ((PanelViewController) notificationPanelViewController2).mVibratorHelper.vibrate(2);
                    }
                    if (f > 1.0f) {
                        f2 = f * 1000.0f;
                    } else {
                        f2 = 0.0f;
                    }
                    Objects.requireNonNull(notificationPanelViewController2.mStatusBar);
                    notificationPanelViewController2.fling(f2, true, 1.0f, false);
                }
                notificationPanelViewController2.onTrackingStopped(false);
            }
        }
    }

    public final void onLaunchTransitionFadingEnded() {
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        notificationPanelViewController.mView.setAlpha(1.0f);
        NotificationPanelViewController notificationPanelViewController2 = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController2);
        notificationPanelViewController2.setLaunchingAffordance(false);
        releaseGestureWakeLock();
        Runnable runnable = this.mLaunchTransitionEndRunnable;
        if (runnable != null) {
            this.mLaunchTransitionEndRunnable = null;
            runnable.run();
        }
        this.mKeyguardStateController.setLaunchTransitionFadingAway(false);
        this.mPresenter.updateMediaMetaData(true, true);
    }

    public final void onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        if (this.mStatusBarWindowState == 0) {
            boolean z2 = false;
            if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                z = true;
            } else {
                z = false;
            }
            if (!z || this.mExpandedVisible) {
                z2 = true;
            }
            setInteracting(1, z2);
        }
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void postQSRunnableDismissingKeyguard(Runnable runnable) {
        this.mMainExecutor.execute(new StatusBar$$ExternalSyntheticLambda21(this, runnable, 0));
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void postStartActivityDismissingKeyguard(final PendingIntent pendingIntent, final ActivityLaunchAnimator.Controller controller) {
        this.mMainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar statusBar = StatusBar.this;
                PendingIntent pendingIntent2 = pendingIntent;
                ActivityLaunchAnimator.Controller controller2 = controller;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.startPendingIntentDismissingKeyguard(pendingIntent2, (Runnable) null, controller2);
            }
        });
    }

    @VisibleForTesting
    public void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.app.action.SHOW_DEVICE_MONITORING_DIALOG");
        this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter, null, UserHandle.ALL);
    }

    public final void releaseGestureWakeLock() {
        if (this.mGestureWakeLock.isHeld()) {
            this.mGestureWakeLock.release();
        }
    }

    public final void setBouncerShowingForStatusBarComponents(boolean z) {
        int i;
        if (z) {
            i = 4;
        } else {
            i = 0;
        }
        PhoneStatusBarViewController phoneStatusBarViewController = this.mPhoneStatusBarViewController;
        if (phoneStatusBarViewController != null) {
            Objects.requireNonNull(phoneStatusBarViewController);
            ((PhoneStatusBarView) phoneStatusBarViewController.mView).setImportantForAccessibility(i);
        }
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        notificationPanelViewController.mView.setImportantForAccessibility(i);
        NotificationPanelViewController notificationPanelViewController2 = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController2);
        notificationPanelViewController2.mBouncerShowing = z;
        notificationPanelViewController2.updateVisibility();
    }

    public final void setInteracting(int i, boolean z) {
        int i2;
        if (z) {
            i2 = i | this.mInteractingWindows;
        } else {
            i2 = (~i) & this.mInteractingWindows;
        }
        this.mInteractingWindows = i2;
        Runnable runnable = null;
        if (i2 != 0) {
            AutoHideController autoHideController = this.mAutoHideController;
            Objects.requireNonNull(autoHideController);
            autoHideController.mHandler.removeCallbacks(autoHideController.mAutoHide);
            if (autoHideController.mStatusBar != null) {
                runnable = new WifiEntry$$ExternalSyntheticLambda2(autoHideController, 6);
            } else if (autoHideController.mNavigationBar != null) {
                runnable = new AccessPoint$$ExternalSyntheticLambda1(autoHideController, 5);
            }
            if (runnable != null) {
                autoHideController.mHandler.removeCallbacks(runnable);
            }
            autoHideController.mAutoHideSuspended = autoHideController.isAnyTransientBarShown();
        } else {
            AutoHideController autoHideController2 = this.mAutoHideController;
            Objects.requireNonNull(autoHideController2);
            if (autoHideController2.mAutoHideSuspended) {
                autoHideController2.mAutoHideSuspended = false;
                autoHideController2.mHandler.removeCallbacks(autoHideController2.mAutoHide);
                autoHideController2.mHandler.postDelayed(autoHideController2.mAutoHide, 2250L);
                if (autoHideController2.mStatusBar != null) {
                    runnable = new WifiEntry$$ExternalSyntheticLambda2(autoHideController2, 6);
                } else if (autoHideController2.mNavigationBar != null) {
                    runnable = new AccessPoint$$ExternalSyntheticLambda1(autoHideController2, 5);
                }
                if (runnable != null) {
                    autoHideController2.mHandler.postDelayed(runnable, 500L);
                }
            }
        }
        checkBarModes();
    }

    public void setLockscreenUser(int i) {
        LockscreenWallpaper lockscreenWallpaper = this.mLockscreenWallpaper;
        if (!(lockscreenWallpaper == null || i == lockscreenWallpaper.mCurrentUserId)) {
            lockscreenWallpaper.mCached = false;
            lockscreenWallpaper.mCurrentUserId = i;
        }
        Objects.requireNonNull(this.mScrimController);
        if (this.mWallpaperSupported) {
            this.mWallpaperChangedReceiver.onReceive(this.mContext, null);
        }
    }

    public final boolean shouldAnimateLaunch(boolean z, boolean z2) {
        if (this.mIsOccluded) {
            return false;
        }
        if (z2 || !this.mKeyguardStateController.isShowing()) {
            return true;
        }
        if (!z || !KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation) {
            return false;
        }
        return true;
    }

    public final void showChargingAnimation(int i, int i2, long j) {
        WirelessChargingAnimation wirelessChargingAnimation = new WirelessChargingAnimation(this.mContext, i2, i, new AnonymousClass6(), sUiEventLogger);
        WirelessChargingAnimation.WirelessChargingView wirelessChargingView = wirelessChargingAnimation.mCurrentWirelessChargingView;
        if (wirelessChargingView == null || wirelessChargingView.mNextView == null) {
            throw new RuntimeException("setView must have been called");
        }
        WirelessChargingAnimation.WirelessChargingView wirelessChargingView2 = WirelessChargingAnimation.mPreviousWirelessChargingView;
        if (wirelessChargingView2 != null) {
            wirelessChargingView2.hide(0L);
        }
        WirelessChargingAnimation.WirelessChargingView wirelessChargingView3 = wirelessChargingAnimation.mCurrentWirelessChargingView;
        WirelessChargingAnimation.mPreviousWirelessChargingView = wirelessChargingView3;
        Objects.requireNonNull(wirelessChargingView3);
        if (WirelessChargingAnimation.DEBUG) {
            Slog.d("WirelessChargingView", "SHOW: " + wirelessChargingView3);
        }
        WirelessChargingAnimation.WirelessChargingView.AnonymousClass1 r7 = wirelessChargingView3.mHandler;
        r7.sendMessageDelayed(Message.obtain(r7, 0), j);
        wirelessChargingAnimation.mCurrentWirelessChargingView.hide(j + 1500);
    }

    public final void showScreenPinningRequest(int i, boolean z) {
        ScreenPinningRequest screenPinningRequest = this.mScreenPinningRequest;
        Objects.requireNonNull(screenPinningRequest);
        try {
            ScreenPinningRequest.RequestWindowView requestWindowView = screenPinningRequest.mRequestWindow;
            if (requestWindowView != null) {
                screenPinningRequest.mWindowManager.removeView(requestWindowView);
                screenPinningRequest.mRequestWindow = null;
            }
        } catch (IllegalArgumentException unused) {
        }
        screenPinningRequest.taskId = i;
        ScreenPinningRequest.RequestWindowView requestWindowView2 = new ScreenPinningRequest.RequestWindowView(screenPinningRequest.mContext, z);
        screenPinningRequest.mRequestWindow = requestWindowView2;
        requestWindowView2.setSystemUiVisibility(256);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2024, 264, -3);
        layoutParams.token = new Binder();
        layoutParams.privateFlags |= 16;
        layoutParams.setTitle("ScreenPinningConfirmation");
        layoutParams.gravity = 119;
        layoutParams.setFitInsetsTypes(0);
        screenPinningRequest.mWindowManager.addView(screenPinningRequest.mRequestWindow, layoutParams);
    }

    @Override // com.android.systemui.CoreStartable
    public void start() {
        RegisterStatusBarResult registerStatusBarResult;
        Display[] displays;
        int i;
        int i2;
        this.mScreenLifecycle.addObserver(this.mScreenObserver);
        this.mWakefulnessLifecycle.addObserver(this.mWakefulnessObserver);
        this.mUiModeManager = (UiModeManager) this.mContext.getSystemService(UiModeManager.class);
        if (this.mBubblesOptional.isPresent()) {
            this.mBubblesOptional.get().setExpandListener(this.mBubbleExpandListener);
        }
        StatusBarSignalPolicy statusBarSignalPolicy = this.mStatusBarSignalPolicy;
        Objects.requireNonNull(statusBarSignalPolicy);
        if (!statusBarSignalPolicy.mInitialized) {
            statusBarSignalPolicy.mInitialized = true;
            statusBarSignalPolicy.mTunerService.addTunable(statusBarSignalPolicy, "icon_blacklist");
            statusBarSignalPolicy.mNetworkController.addCallback(statusBarSignalPolicy);
            statusBarSignalPolicy.mSecurityController.addCallback(statusBarSignalPolicy);
        }
        this.mKeyguardIndicationController.init();
        this.mColorExtractor.addOnColorsChangedListener(this.mOnColorsChangedListener);
        this.mStatusBarStateController.addCallback(this.mStateListener, 0);
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
        this.mDreamManager = IDreamManager.Stub.asInterface(ServiceManager.checkService("dreams"));
        Display display = this.mContext.getDisplay();
        this.mDisplay = display;
        this.mDisplayId = display.getDisplayId();
        this.mDisplay.getMetrics(this.mDisplayMetrics);
        this.mDisplay.getSize(this.mCurrentDisplaySize);
        StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager = this.mStatusBarHideIconsForBouncerManager;
        int i3 = this.mDisplayId;
        Objects.requireNonNull(statusBarHideIconsForBouncerManager);
        statusBarHideIconsForBouncerManager.displayId = i3;
        WindowManagerGlobal.getWindowManagerService();
        this.mDevicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
        KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
        Objects.requireNonNull(keyguardUpdateMonitor);
        keyguardUpdateMonitor.mKeyguardBypassController = keyguardBypassController;
        this.mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        KeyguardManager keyguardManager = (KeyguardManager) this.mContext.getSystemService("keyguard");
        this.mWallpaperSupported = this.mWallpaperManager.isWallpaperSupported();
        try {
            registerStatusBarResult = this.mBarService.registerStatusBar(this.mCommandQueue);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            registerStatusBarResult = null;
        }
        this.mDisplay.getMetrics(this.mDisplayMetrics);
        this.mDisplay.getSize(this.mCurrentDisplaySize);
        updateResources();
        updateTheme();
        StatusBarComponent statusBarComponent = this.mStatusBarComponent;
        if (statusBarComponent != null) {
            for (StatusBarComponent.Startable startable : statusBarComponent.getStartables()) {
                startable.stop();
            }
        }
        StatusBarComponent create = this.mStatusBarComponentFactory.create();
        this.mStatusBarComponent = create;
        this.mFragmentService.addFragmentInstantiationProvider(create);
        this.mNotificationShadeWindowView = this.mStatusBarComponent.getNotificationShadeWindowView();
        this.mNotificationShadeWindowViewController = this.mStatusBarComponent.getNotificationShadeWindowViewController();
        this.mNotificationShadeWindowController.setNotificationShadeView(this.mNotificationShadeWindowView);
        final NotificationShadeWindowViewController notificationShadeWindowViewController = this.mNotificationShadeWindowViewController;
        Objects.requireNonNull(notificationShadeWindowViewController);
        notificationShadeWindowViewController.mStackScrollLayout = (NotificationStackScrollLayout) notificationShadeWindowViewController.mView.findViewById(2131428521);
        notificationShadeWindowViewController.mTunerService.addTunable(new NotificationShadeWindowViewController$$ExternalSyntheticLambda0(notificationShadeWindowViewController, 0), "doze_pulse_on_double_tap", "doze_tap_gesture");
        notificationShadeWindowViewController.mGestureDetector = new GestureDetector(notificationShadeWindowViewController.mView.getContext(), new GestureDetector.SimpleOnGestureListener() { // from class: com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public final boolean onDoubleTap(MotionEvent motionEvent) {
                NotificationShadeWindowViewController notificationShadeWindowViewController2 = notificationShadeWindowViewController;
                if (!notificationShadeWindowViewController2.mDoubleTapEnabled && !notificationShadeWindowViewController2.mSingleTapEnabled) {
                    return false;
                }
                notificationShadeWindowViewController2.mService.wakeUpIfDozing(SystemClock.uptimeMillis(), notificationShadeWindowViewController.mView, "DOUBLE_TAP");
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public final boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                NotificationShadeWindowViewController notificationShadeWindowViewController2 = notificationShadeWindowViewController;
                if (!notificationShadeWindowViewController2.mSingleTapEnabled || notificationShadeWindowViewController2.mDockManager.isDocked()) {
                    return false;
                }
                notificationShadeWindowViewController.mService.wakeUpIfDozing(SystemClock.uptimeMillis(), notificationShadeWindowViewController.mView, "SINGLE_TAP");
                return true;
            }
        });
        notificationShadeWindowViewController.mLowLightClockController.ifPresent(new StatusBar$$ExternalSyntheticLambda34(notificationShadeWindowViewController, 2));
        NotificationShadeWindowView notificationShadeWindowView = notificationShadeWindowViewController.mView;
        NotificationShadeWindowViewController.AnonymousClass2 r4 = new NotificationShadeWindowViewController.AnonymousClass2();
        Objects.requireNonNull(notificationShadeWindowView);
        notificationShadeWindowView.mInteractionEventHandler = r4;
        notificationShadeWindowViewController.mView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() { // from class: com.android.systemui.statusbar.phone.NotificationShadeWindowViewController.3
            @Override // android.view.ViewGroup.OnHierarchyChangeListener
            public final void onChildViewRemoved(View view, View view2) {
            }

            @Override // android.view.ViewGroup.OnHierarchyChangeListener
            public final void onChildViewAdded(View view, View view2) {
                if (view2.getId() == 2131427604) {
                    notificationShadeWindowViewController.mBrightnessMirror = view2;
                }
            }
        });
        LockscreenShadeTransitionController lockscreenShadeTransitionController = notificationShadeWindowViewController.mLockscreenShadeTransitionController;
        Objects.requireNonNull(lockscreenShadeTransitionController);
        notificationShadeWindowViewController.setDragDownHelper(lockscreenShadeTransitionController.touchHelper);
        NotificationShadeDepthController notificationShadeDepthController = notificationShadeWindowViewController.mDepthController;
        NotificationShadeWindowView notificationShadeWindowView2 = notificationShadeWindowViewController.mView;
        Objects.requireNonNull(notificationShadeDepthController);
        notificationShadeDepthController.root = notificationShadeWindowView2;
        notificationShadeWindowViewController.mPanelExpansionStateManager.addExpansionListener(notificationShadeWindowViewController.mDepthController);
        this.mNotificationPanelViewController = this.mStatusBarComponent.getNotificationPanelViewController();
        this.mStatusBarComponent.getLockIconViewController().init();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStatusBarComponent.getNotificationStackScrollLayoutController();
        this.mStackScrollerController = notificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        this.mStackScroller = notificationStackScrollLayoutController.mView;
        this.mNotificationShelfController = this.mStatusBarComponent.getNotificationShelfController();
        this.mStatusBarComponent.getAuthRippleController().init();
        this.mHeadsUpManager.addListener(this.mStatusBarComponent.getStatusBarHeadsUpChangeListener());
        this.mDemoModeController.addCallback((DemoMode) this.mDemoModeCallback);
        StatusBarCommandQueueCallbacks statusBarCommandQueueCallbacks = this.mCommandQueueCallbacks;
        if (statusBarCommandQueueCallbacks != null) {
            this.mCommandQueue.removeCallback((CommandQueue.Callbacks) statusBarCommandQueueCallbacks);
        }
        StatusBarCommandQueueCallbacks statusBarCommandQueueCallbacks2 = this.mStatusBarComponent.getStatusBarCommandQueueCallbacks();
        this.mCommandQueueCallbacks = statusBarCommandQueueCallbacks2;
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) statusBarCommandQueueCallbacks2);
        for (StatusBarComponent.Startable startable2 : this.mStatusBarComponent.getStartables()) {
            startable2.start();
        }
        NotificationShadeWindowViewController notificationShadeWindowViewController2 = this.mNotificationShadeWindowViewController;
        NotificationShadeWindowController notificationShadeWindowController = this.mNotificationShadeWindowController;
        Objects.requireNonNull(notificationShadeWindowViewController2);
        notificationShadeWindowViewController2.mService = this;
        notificationShadeWindowViewController2.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mNotificationShadeWindowView.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                StatusBar statusBar = StatusBar.this;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.mAutoHideController.checkUserAutoHide(motionEvent);
                NotificationRemoteInputManager notificationRemoteInputManager = statusBar.mRemoteInputManager;
                Objects.requireNonNull(notificationRemoteInputManager);
                if (motionEvent.getAction() == 4 && motionEvent.getX() == 0.0f && motionEvent.getY() == 0.0f && notificationRemoteInputManager.isRemoteInputActive()) {
                    notificationRemoteInputManager.closeRemoteInputs();
                }
                if (motionEvent.getAction() == 0 && statusBar.mExpandedVisible) {
                    statusBar.mShadeController.animateCollapsePanels();
                }
                return statusBar.mNotificationShadeWindowView.onTouchEvent(motionEvent);
            }
        });
        WallpaperController wallpaperController = this.mWallpaperController;
        NotificationShadeWindowView notificationShadeWindowView3 = this.mNotificationShadeWindowView;
        Objects.requireNonNull(wallpaperController);
        wallpaperController.rootView = notificationShadeWindowView3;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.mStackScrollerController;
        Objects.requireNonNull(notificationStackScrollLayoutController2);
        NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl = notificationStackScrollLayoutController2.mNotificationListContainer;
        NotificationLogger notificationLogger = this.mNotificationLogger;
        Objects.requireNonNull(notificationLogger);
        notificationLogger.mListContainer = notificationListContainerImpl;
        NotificationIconAreaController notificationIconAreaController = this.mNotificationIconAreaController;
        NotificationShelfController notificationShelfController = this.mNotificationShelfController;
        Objects.requireNonNull(notificationIconAreaController);
        Objects.requireNonNull(notificationShelfController);
        NotificationShelf notificationShelf = notificationShelfController.mView;
        Objects.requireNonNull(notificationShelf);
        notificationIconAreaController.mShelfIcons = notificationShelf.mShelfIcons;
        NotificationIconContainer notificationIconContainer = notificationIconAreaController.mNotificationIcons;
        NotificationShelf notificationShelf2 = notificationShelfController.mView;
        Objects.requireNonNull(notificationShelf2);
        notificationShelf2.mCollapsedIcons = notificationIconContainer;
        notificationIconContainer.addOnLayoutChangeListener(notificationShelf2);
        this.mPanelExpansionStateManager.addExpansionListener(this.mWakeUpCoordinator);
        UserSwitcherController userSwitcherController = this.mUserSwitcherController;
        NotificationShadeWindowView notificationShadeWindowView4 = this.mNotificationShadeWindowView;
        Objects.requireNonNull(userSwitcherController);
        userSwitcherController.mView = notificationShadeWindowView4;
        this.mPluginDependencyProvider.allowPluginDependency(DarkIconDispatcher.class);
        this.mPluginDependencyProvider.allowPluginDependency(StatusBarStateController.class);
        final StatusBarInitializer statusBarInitializer = this.mStatusBarComponent.getStatusBarInitializer();
        StatusBar$$ExternalSyntheticLambda7 statusBar$$ExternalSyntheticLambda7 = new StatusBar$$ExternalSyntheticLambda7(this);
        Objects.requireNonNull(statusBarInitializer);
        statusBarInitializer.statusBarViewUpdatedListener = statusBar$$ExternalSyntheticLambda7;
        StatusBarComponent statusBarComponent2 = this.mStatusBarComponent;
        StatusBarWindowController statusBarWindowController = statusBarInitializer.windowController;
        Objects.requireNonNull(statusBarWindowController);
        FragmentHostManager fragmentHostManager = FragmentHostManager.get(statusBarWindowController.mStatusBarWindowView);
        fragmentHostManager.addTagListener("CollapsedStatusBarFragment", new FragmentHostManager.FragmentListener() { // from class: com.android.systemui.statusbar.core.StatusBarInitializer$initializeStatusBar$1
            @Override // com.android.systemui.fragments.FragmentHostManager.FragmentListener
            public final void onFragmentViewDestroyed(Fragment fragment) {
            }

            @Override // com.android.systemui.fragments.FragmentHostManager.FragmentListener
            public final void onFragmentViewCreated(Fragment fragment) {
                StatusBarFragmentComponent statusBarFragmentComponent = ((CollapsedStatusBarFragment) fragment).mStatusBarFragmentComponent;
                if (statusBarFragmentComponent != null) {
                    StatusBarInitializer statusBarInitializer2 = StatusBarInitializer.this;
                    Objects.requireNonNull(statusBarInitializer2);
                    StatusBarInitializer.OnStatusBarViewUpdatedListener onStatusBarViewUpdatedListener = statusBarInitializer2.statusBarViewUpdatedListener;
                    if (onStatusBarViewUpdatedListener != null) {
                        statusBarFragmentComponent.getPhoneStatusBarView();
                        PhoneStatusBarViewController phoneStatusBarViewController = statusBarFragmentComponent.getPhoneStatusBarViewController();
                        PhoneStatusBarTransitions phoneStatusBarTransitions = statusBarFragmentComponent.getPhoneStatusBarTransitions();
                        StatusBar statusBar = ((StatusBar$$ExternalSyntheticLambda7) onStatusBarViewUpdatedListener).f$0;
                        long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                        Objects.requireNonNull(statusBar);
                        statusBar.mPhoneStatusBarViewController = phoneStatusBarViewController;
                        statusBar.mStatusBarTransitions = phoneStatusBarTransitions;
                        NotificationShadeWindowViewController notificationShadeWindowViewController3 = statusBar.mNotificationShadeWindowViewController;
                        Objects.requireNonNull(notificationShadeWindowViewController3);
                        notificationShadeWindowViewController3.mStatusBarViewController = phoneStatusBarViewController;
                        statusBar.mNotificationPanelViewController.updatePanelExpansionAndVisibility();
                        statusBar.setBouncerShowingForStatusBarComponents(statusBar.mBouncerShowing);
                        statusBar.checkBarModes();
                        return;
                    }
                    return;
                }
                throw new IllegalStateException();
            }
        });
        fragmentHostManager.getFragmentManager().beginTransaction().replace(2131428924, statusBarComponent2.createCollapsedStatusBarFragment(), "CollapsedStatusBarFragment").commit();
        StatusBarTouchableRegionManager statusBarTouchableRegionManager = this.mStatusBarTouchableRegionManager;
        NotificationShadeWindowView notificationShadeWindowView5 = this.mNotificationShadeWindowView;
        Objects.requireNonNull(statusBarTouchableRegionManager);
        statusBarTouchableRegionManager.mStatusBar = this;
        statusBarTouchableRegionManager.mNotificationShadeWindowView = notificationShadeWindowView5;
        statusBarTouchableRegionManager.mNotificationPanelView = notificationShadeWindowView5.findViewById(2131428519);
        HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        headsUpManagerPhone.addListener(notificationPanelViewController.mOnHeadsUpChangedListener);
        if (!this.mNotifPipelineFlags.isNewPipelineEnabled()) {
            this.mHeadsUpManager.addListener(this.mVisualStabilityManager);
        }
        NotificationPanelViewController notificationPanelViewController2 = this.mNotificationPanelViewController;
        HeadsUpManagerPhone headsUpManagerPhone2 = this.mHeadsUpManager;
        Objects.requireNonNull(notificationPanelViewController2);
        notificationPanelViewController2.mHeadsUpManager = headsUpManagerPhone2;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController3 = notificationPanelViewController2.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController3);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController3.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        notificationPanelViewController2.mHeadsUpTouchHelper = new HeadsUpTouchHelper(headsUpManagerPhone2, notificationStackScrollLayout.mHeadsUpCallback, notificationPanelViewController2);
        NavigationBarController navigationBarController = this.mNavigationBarController;
        Objects.requireNonNull(navigationBarController);
        navigationBarController.updateAccessibilityButtonModeIfNeeded();
        boolean z = !navigationBarController.initializeTaskbarIfNecessary();
        for (Display display2 : navigationBarController.mDisplayManager.getDisplays()) {
            if (z || display2.getDisplayId() != 0) {
                navigationBarController.createNavigationBar(display2, null, registerStatusBarResult);
            }
        }
        if (this.mWallpaperSupported) {
            this.mLockscreenWallpaper = this.mLockscreenWallpaperLazy.get();
        }
        NotificationPanelViewController notificationPanelViewController3 = this.mNotificationPanelViewController;
        KeyguardIndicationController keyguardIndicationController = this.mKeyguardIndicationController;
        Objects.requireNonNull(notificationPanelViewController3);
        notificationPanelViewController3.mKeyguardIndicationController = keyguardIndicationController;
        keyguardIndicationController.setIndicationArea(notificationPanelViewController3.mKeyguardBottomArea);
        this.mAmbientIndicationContainer = this.mNotificationShadeWindowView.findViewById(2131427484);
        AutoHideController autoHideController = this.mAutoHideController;
        AutoHideUiElement autoHideUiElement = new AutoHideUiElement() { // from class: com.android.systemui.statusbar.phone.StatusBar.3
            {
                StatusBar.this = this;
            }

            @Override // com.android.systemui.statusbar.AutoHideUiElement
            public final void hide() {
                StatusBar statusBar = StatusBar.this;
                Objects.requireNonNull(statusBar);
                if (statusBar.mTransientShown) {
                    statusBar.mTransientShown = false;
                    statusBar.maybeUpdateBarMode();
                }
            }

            @Override // com.android.systemui.statusbar.AutoHideUiElement
            public final boolean isVisible() {
                StatusBar statusBar = StatusBar.this;
                Objects.requireNonNull(statusBar);
                return statusBar.mTransientShown;
            }

            @Override // com.android.systemui.statusbar.AutoHideUiElement
            public final boolean shouldHideOnTouch() {
                return !StatusBar.this.mRemoteInputManager.isRemoteInputActive();
            }

            @Override // com.android.systemui.statusbar.AutoHideUiElement
            public final void synchronizeState() {
                StatusBar.this.checkBarModes();
            }
        };
        Objects.requireNonNull(autoHideController);
        autoHideController.mStatusBar = autoHideUiElement;
        ScrimView scrimView = (ScrimView) this.mNotificationShadeWindowView.findViewById(2131428788);
        ScrimController scrimController = this.mScrimController;
        StatusBar$$ExternalSyntheticLambda30 statusBar$$ExternalSyntheticLambda30 = new StatusBar$$ExternalSyntheticLambda30(this, 0);
        Objects.requireNonNull(scrimController);
        scrimController.mScrimVisibleListener = statusBar$$ExternalSyntheticLambda30;
        ScrimController scrimController2 = this.mScrimController;
        Objects.requireNonNull(scrimController2);
        scrimController2.mNotificationsScrim = (ScrimView) this.mNotificationShadeWindowView.findViewById(2131428791);
        scrimController2.mScrimBehind = scrimView;
        scrimController2.mScrimInFront = (ScrimView) this.mNotificationShadeWindowView.findViewById(2131428789);
        scrimController2.updateThemeColors();
        scrimView.enableBottomEdgeConcave(scrimController2.mClipsQsScrim);
        ScrimView scrimView2 = scrimController2.mNotificationsScrim;
        Objects.requireNonNull(scrimView2);
        Drawable drawable = scrimView2.mDrawable;
        if (drawable instanceof ScrimDrawable) {
            ScrimDrawable scrimDrawable = (ScrimDrawable) drawable;
            Objects.requireNonNull(scrimDrawable);
            if (!scrimDrawable.mCornerRadiusEnabled) {
                scrimDrawable.mCornerRadiusEnabled = true;
                scrimDrawable.invalidateSelf();
            }
        }
        Runnable runnable = scrimController2.mScrimBehindChangeRunnable;
        if (runnable != null) {
            ScrimView scrimView3 = scrimController2.mScrimBehind;
            Executor executor = scrimController2.mMainExecutor;
            Objects.requireNonNull(scrimView3);
            scrimView3.mChangeRunnable = runnable;
            scrimView3.mChangeRunnableExecutor = executor;
            scrimController2.mScrimBehindChangeRunnable = null;
        }
        ScrimState[] values = ScrimState.values();
        for (int i4 = 0; i4 < values.length; i4++) {
            ScrimState scrimState = values[i4];
            ScrimView scrimView4 = scrimController2.mScrimInFront;
            ScrimView scrimView5 = scrimController2.mScrimBehind;
            DozeParameters dozeParameters = scrimController2.mDozeParameters;
            DockManager dockManager = scrimController2.mDockManager;
            Objects.requireNonNull(scrimState);
            scrimState.mScrimInFront = scrimView4;
            scrimState.mScrimBehind = scrimView5;
            scrimState.mDozeParameters = dozeParameters;
            scrimState.mDockManager = dockManager;
            scrimState.mDisplayRequiresBlanking = dozeParameters.getDisplayNeedsBlanking();
            ScrimState scrimState2 = values[i4];
            float f = scrimController2.mScrimBehindAlphaKeyguard;
            Objects.requireNonNull(scrimState2);
            scrimState2.mScrimBehindAlphaKeyguard = f;
            ScrimState scrimState3 = values[i4];
            float f2 = scrimController2.mDefaultScrimAlpha;
            Objects.requireNonNull(scrimState3);
            scrimState3.mDefaultScrimAlpha = f2;
        }
        scrimController2.mScrimBehind.setDefaultFocusHighlightEnabled(false);
        scrimController2.mNotificationsScrim.setDefaultFocusHighlightEnabled(false);
        scrimController2.mScrimInFront.setDefaultFocusHighlightEnabled(false);
        scrimController2.updateScrims();
        scrimController2.mKeyguardUpdateMonitor.registerCallback(scrimController2.mKeyguardVisibilityCallback);
        LightRevealScrim lightRevealScrim = (LightRevealScrim) this.mNotificationShadeWindowView.findViewById(2131428254);
        this.mLightRevealScrim = lightRevealScrim;
        StatusBar$$ExternalSyntheticLambda31 statusBar$$ExternalSyntheticLambda31 = new StatusBar$$ExternalSyntheticLambda31(this, 0);
        Objects.requireNonNull(lightRevealScrim);
        lightRevealScrim.isScrimOpaqueChangedListener = statusBar$$ExternalSyntheticLambda31;
        ScreenOffAnimationController screenOffAnimationController = this.mScreenOffAnimationController;
        LightRevealScrim lightRevealScrim2 = this.mLightRevealScrim;
        Objects.requireNonNull(screenOffAnimationController);
        Iterator it = screenOffAnimationController.animations.iterator();
        while (it.hasNext()) {
            ((ScreenOffAnimation) it.next()).initialize(this, lightRevealScrim2);
        }
        WakefulnessLifecycle wakefulnessLifecycle = screenOffAnimationController.wakefulnessLifecycle;
        Objects.requireNonNull(wakefulnessLifecycle);
        wakefulnessLifecycle.mObservers.add(screenOffAnimationController);
        updateLightRevealScrimVisibility();
        NotificationPanelViewController notificationPanelViewController4 = this.mNotificationPanelViewController;
        StatusBar$$ExternalSyntheticLambda19 statusBar$$ExternalSyntheticLambda19 = new StatusBar$$ExternalSyntheticLambda19(this, 0);
        NotificationShelfController notificationShelfController2 = this.mNotificationShelfController;
        Objects.requireNonNull(notificationPanelViewController4);
        notificationPanelViewController4.mStatusBar = this;
        KeyguardBottomAreaView keyguardBottomAreaView = notificationPanelViewController4.mKeyguardBottomArea;
        Objects.requireNonNull(keyguardBottomAreaView);
        keyguardBottomAreaView.mStatusBar = this;
        keyguardBottomAreaView.updateCameraVisibility();
        notificationPanelViewController4.mHideExpandedRunnable = statusBar$$ExternalSyntheticLambda19;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController4 = notificationPanelViewController4.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController4);
        NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController4.mView;
        Objects.requireNonNull(notificationStackScrollLayout2);
        NotificationShelf notificationShelf3 = notificationStackScrollLayout2.mShelf;
        if (notificationShelf3 != null) {
            i = notificationStackScrollLayout2.indexOfChild(notificationShelf3);
            notificationStackScrollLayout2.removeView(notificationStackScrollLayout2.mShelf);
        } else {
            i = -1;
        }
        Objects.requireNonNull(notificationShelfController2);
        NotificationShelf notificationShelf4 = notificationShelfController2.mView;
        notificationStackScrollLayout2.mShelf = notificationShelf4;
        notificationStackScrollLayout2.addView(notificationShelf4, i);
        AmbientState ambientState = notificationStackScrollLayout2.mAmbientState;
        NotificationShelf notificationShelf5 = notificationStackScrollLayout2.mShelf;
        Objects.requireNonNull(ambientState);
        ambientState.mShelf = notificationShelf5;
        StackStateAnimator stackStateAnimator = notificationStackScrollLayout2.mStateAnimator;
        NotificationShelf notificationShelf6 = notificationStackScrollLayout2.mShelf;
        Objects.requireNonNull(stackStateAnimator);
        stackStateAnimator.mShelf = notificationShelf6;
        AmbientState ambientState2 = notificationStackScrollLayout2.mAmbientState;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController5 = notificationStackScrollLayout2.mController;
        NotificationShelf notificationShelf7 = notificationShelfController2.mView;
        Objects.requireNonNull(notificationShelf7);
        notificationShelf7.mAmbientState = ambientState2;
        notificationShelf7.mHostLayoutController = notificationStackScrollLayoutController5;
        notificationShelfController2.mAmbientState = ambientState2;
        notificationPanelViewController4.mNotificationShelfController = notificationShelfController2;
        final LockscreenShadeTransitionController lockscreenShadeTransitionController2 = notificationPanelViewController4.mLockscreenShadeTransitionController;
        Objects.requireNonNull(lockscreenShadeTransitionController2);
        notificationShelfController2.mView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$bindController$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                if (LockscreenShadeTransitionController.this.statusBarStateController.getState() == 1) {
                    LockscreenShadeTransitionController lockscreenShadeTransitionController3 = LockscreenShadeTransitionController.this;
                    Objects.requireNonNull(lockscreenShadeTransitionController3);
                    StatusBar statusBar = lockscreenShadeTransitionController3.statusbar;
                    if (statusBar == null) {
                        statusBar = null;
                    }
                    statusBar.wakeUpIfDozing(SystemClock.uptimeMillis(), view, "SHADE_CLICK");
                    LockscreenShadeTransitionController.this.goToLockedShade(view, true);
                }
            }
        });
        notificationPanelViewController4.updateMaxDisplayedNotifications(true);
        final BackDropView backDropView = (BackDropView) this.mNotificationShadeWindowView.findViewById(2131427536);
        NotificationMediaManager notificationMediaManager = this.mMediaManager;
        ScrimController scrimController3 = this.mScrimController;
        LockscreenWallpaper lockscreenWallpaper = this.mLockscreenWallpaper;
        Objects.requireNonNull(notificationMediaManager);
        notificationMediaManager.mBackdrop = backDropView;
        notificationMediaManager.mBackdropFront = (ImageView) backDropView.findViewById(2131427538);
        notificationMediaManager.mBackdropBack = (ImageView) backDropView.findViewById(2131427537);
        notificationMediaManager.mScrimController = scrimController3;
        notificationMediaManager.mLockscreenWallpaper = lockscreenWallpaper;
        final float f3 = this.mContext.getResources().getFloat(17105120);
        NotificationShadeDepthController notificationShadeDepthController2 = this.mNotificationShadeDepthControllerLazy.get();
        NotificationShadeDepthController.DepthListener statusBar$$ExternalSyntheticLambda6 = new NotificationShadeDepthController.DepthListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda6
            @Override // com.android.systemui.statusbar.NotificationShadeDepthController.DepthListener
            public final void onWallpaperZoomOutChanged(float f4) {
                float f5 = f3;
                BackDropView backDropView2 = backDropView;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                float lerp = MathUtils.lerp(f5, 1.0f, f4);
                backDropView2.setPivotX(backDropView2.getWidth() / 2.0f);
                backDropView2.setPivotY(backDropView2.getHeight() / 2.0f);
                backDropView2.setScaleX(lerp);
                backDropView2.setScaleY(lerp);
            }
        };
        Objects.requireNonNull(notificationShadeDepthController2);
        notificationShadeDepthController2.listeners.add(statusBar$$ExternalSyntheticLambda6);
        NotificationPanelViewController notificationPanelViewController5 = this.mNotificationPanelViewController;
        boolean z2 = this.mUserSetup;
        Objects.requireNonNull(notificationPanelViewController5);
        notificationPanelViewController5.mUserSetupComplete = z2;
        KeyguardBottomAreaView keyguardBottomAreaView2 = notificationPanelViewController5.mKeyguardBottomArea;
        Objects.requireNonNull(keyguardBottomAreaView2);
        keyguardBottomAreaView2.mUserSetupComplete = z2;
        keyguardBottomAreaView2.updateCameraVisibility();
        keyguardBottomAreaView2.updateLeftAffordanceIcon();
        View findViewById = this.mNotificationShadeWindowView.findViewById(2131428651);
        if (findViewById != null) {
            FragmentHostManager fragmentHostManager2 = FragmentHostManager.get(findViewById);
            ExtensionControllerImpl.ExtensionBuilder newExtension = this.mExtensionController.newExtension();
            Objects.requireNonNull(newExtension);
            String action = PluginManager.Helper.getAction(QS.class);
            ExtensionControllerImpl.ExtensionImpl<T> extensionImpl = newExtension.mExtension;
            Objects.requireNonNull(extensionImpl);
            extensionImpl.mProducers.add(new ExtensionControllerImpl.ExtensionImpl.PluginItem(action, QS.class, null));
            newExtension.withDefault(new Supplier() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda35
                @Override // java.util.function.Supplier
                public final Object get() {
                    StatusBar statusBar = StatusBar.this;
                    Objects.requireNonNull(statusBar);
                    FragmentHostManager fragmentHostManager3 = FragmentHostManager.get(statusBar.mNotificationShadeWindowView);
                    Objects.requireNonNull(fragmentHostManager3);
                    return (QS) fragmentHostManager3.mPlugins.instantiate(fragmentHostManager3.mContext, QSFragment.class.getName(), null);
                }
            });
            ExtensionControllerImpl.ExtensionImpl build = newExtension.build();
            ExtensionFragmentListener extensionFragmentListener = new ExtensionFragmentListener(findViewById, build);
            Objects.requireNonNull(build);
            build.mCallbacks.add(extensionFragmentListener);
            this.mBrightnessMirrorController = new BrightnessMirrorController(this.mNotificationShadeWindowView, this.mNotificationPanelViewController, this.mNotificationShadeDepthControllerLazy.get(), this.mBrightnessSliderFactory, new StatusBar$$ExternalSyntheticLambda32(this, 0));
            fragmentHostManager2.addTagListener(QS.TAG, new FragmentHostManager.FragmentListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda5
                @Override // com.android.systemui.fragments.FragmentHostManager.FragmentListener
                public final void onFragmentViewCreated(Fragment fragment) {
                    StatusBar statusBar = StatusBar.this;
                    long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                    Objects.requireNonNull(statusBar);
                    QS qs = (QS) fragment;
                    if (qs instanceof QSFragment) {
                        QSFragment qSFragment = (QSFragment) qs;
                        Objects.requireNonNull(qSFragment);
                        QSPanelController qSPanelController = qSFragment.mQSPanelController;
                        statusBar.mQSPanelController = qSPanelController;
                        BrightnessMirrorController brightnessMirrorController = statusBar.mBrightnessMirrorController;
                        Objects.requireNonNull(qSPanelController);
                        BrightnessMirrorHandler brightnessMirrorHandler = qSPanelController.mBrightnessMirrorHandler;
                        Objects.requireNonNull(brightnessMirrorHandler);
                        BrightnessMirrorController brightnessMirrorController2 = brightnessMirrorHandler.mirrorController;
                        if (brightnessMirrorController2 != null) {
                            brightnessMirrorController2.mBrightnessMirrorListeners.remove(brightnessMirrorHandler.brightnessMirrorListener);
                        }
                        brightnessMirrorHandler.mirrorController = brightnessMirrorController;
                        BrightnessMirrorHandler$brightnessMirrorListener$1 brightnessMirrorHandler$brightnessMirrorListener$1 = brightnessMirrorHandler.brightnessMirrorListener;
                        Objects.requireNonNull(brightnessMirrorController);
                        Objects.requireNonNull(brightnessMirrorHandler$brightnessMirrorListener$1);
                        brightnessMirrorController.mBrightnessMirrorListeners.add(brightnessMirrorHandler$brightnessMirrorListener$1);
                        brightnessMirrorHandler.updateBrightnessMirror();
                    }
                }
            });
        }
        View findViewById2 = this.mNotificationShadeWindowView.findViewById(2131428692);
        this.mReportRejectedTouch = findViewById2;
        if (findViewById2 != null) {
            updateReportRejectedTouchVisibility();
            this.mReportRejectedTouch.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StatusBar statusBar = StatusBar.this;
                    long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                    Objects.requireNonNull(statusBar);
                    Uri reportRejectedTouch = statusBar.mFalsingManager.reportRejectedTouch();
                    if (reportRejectedTouch != null) {
                        StringWriter stringWriter = new StringWriter();
                        stringWriter.write("Build info: ");
                        stringWriter.write(SystemProperties.get("ro.build.description"));
                        stringWriter.write("\nSerial number: ");
                        stringWriter.write(SystemProperties.get("ro.serialno"));
                        stringWriter.write("\n");
                        statusBar.startActivityDismissingKeyguard(Intent.createChooser(new Intent("android.intent.action.SEND").setType("*/*").putExtra("android.intent.extra.SUBJECT", "Rejected touch report").putExtra("android.intent.extra.STREAM", reportRejectedTouch).putExtra("android.intent.extra.TEXT", stringWriter.toString()), "Share rejected touch report").addFlags(268435456), true, true, 0);
                    }
                }
            });
        }
        if (!this.mPowerManager.isInteractive()) {
            this.mBroadcastReceiver.onReceive(this.mContext, new Intent("android.intent.action.SCREEN_OFF"));
        }
        this.mGestureWakeLock = this.mPowerManager.newWakeLock(10, "sysui:GestureWakeLock");
        registerBroadcastReceiver();
        this.mContext.registerReceiverAsUser(this.mDemoReceiver, UserHandle.ALL, new IntentFilter(), "android.permission.DUMP", null, 2);
        this.mDeviceProvisionedController.addCallback(this.mUserSetupObserver);
        this.mUserSetupObserver.onUserSetupChanged();
        ThreadedRenderer.overrideProperty("disableProfileBars", "true");
        ThreadedRenderer.overrideProperty("ambientRatio", String.valueOf(1.5f));
        this.mNotificationShadeWindowController.attach$1();
        final StatusBarWindowController statusBarWindowController2 = this.mStatusBarWindowController;
        Objects.requireNonNull(statusBarWindowController2);
        WindowManager.LayoutParams barLayoutParamsForRotation = statusBarWindowController2.getBarLayoutParamsForRotation(statusBarWindowController2.mContext.getDisplay().getRotation());
        barLayoutParamsForRotation.paramsForRotation = new WindowManager.LayoutParams[4];
        for (int i5 = 0; i5 <= 3; i5++) {
            barLayoutParamsForRotation.paramsForRotation[i5] = statusBarWindowController2.getBarLayoutParamsForRotation(i5);
        }
        statusBarWindowController2.mLp = barLayoutParamsForRotation;
        statusBarWindowController2.mWindowManager.addView(statusBarWindowController2.mStatusBarWindowView, barLayoutParamsForRotation);
        statusBarWindowController2.mLpChanged.copyFrom(statusBarWindowController2.mLp);
        StatusBarContentInsetsProvider statusBarContentInsetsProvider = statusBarWindowController2.mContentInsetsProvider;
        StatusBarContentInsetsChangedListener statusBarWindowController$$ExternalSyntheticLambda0 = new StatusBarContentInsetsChangedListener() { // from class: com.android.systemui.statusbar.window.StatusBarWindowController$$ExternalSyntheticLambda0
            @Override // com.android.systemui.statusbar.phone.StatusBarContentInsetsChangedListener
            public final void onStatusBarContentInsetsChanged() {
                StatusBarWindowController.this.calculateStatusBarLocationsForAllRotations();
            }
        };
        Objects.requireNonNull(statusBarContentInsetsProvider);
        statusBarContentInsetsProvider.listeners.add(statusBarWindowController$$ExternalSyntheticLambda0);
        statusBarWindowController2.calculateStatusBarLocationsForAllRotations();
        if (this.mWallpaperSupported) {
            this.mBroadcastDispatcher.registerReceiver(this.mWallpaperChangedReceiver, new IntentFilter("android.intent.action.WALLPAPER_CHANGED"), null, UserHandle.ALL);
            this.mWallpaperChangedReceiver.onReceive(this.mContext, null);
        }
        ActivityLaunchAnimator activityLaunchAnimator = this.mActivityLaunchAnimator;
        AnonymousClass23 r2 = this.mActivityLaunchAnimatorCallback;
        Objects.requireNonNull(activityLaunchAnimator);
        activityLaunchAnimator.callback = r2;
        ActivityLaunchAnimator activityLaunchAnimator2 = this.mActivityLaunchAnimator;
        AnonymousClass24 r22 = this.mActivityLaunchAnimatorListener;
        Objects.requireNonNull(activityLaunchAnimator2);
        activityLaunchAnimator2.listeners.add(r22);
        NotificationShadeWindowViewController notificationShadeWindowViewController3 = this.mNotificationShadeWindowViewController;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController6 = this.mStackScrollerController;
        Objects.requireNonNull(notificationStackScrollLayoutController6);
        NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl2 = notificationStackScrollLayoutController6.mNotificationListContainer;
        HeadsUpManagerPhone headsUpManagerPhone3 = this.mHeadsUpManager;
        this.mNotificationAnimationProvider = new DirectedAcyclicGraph(notificationShadeWindowViewController3, notificationListContainerImpl2, headsUpManagerPhone3, this.mJankMonitor);
        StatusBarNotificationPresenter statusBarNotificationPresenter = new StatusBarNotificationPresenter(this.mContext, this.mNotificationPanelViewController, headsUpManagerPhone3, this.mNotificationShadeWindowView, this.mStackScrollerController, this.mDozeScrimController, this.mScrimController, this.mNotificationShadeWindowController, this.mDynamicPrivacyController, this.mKeyguardStateController, this.mKeyguardIndicationController, this, this.mShadeController, this.mLockscreenShadeTransitionController, this.mCommandQueue, this.mViewHierarchyManager, this.mLockscreenUserManager, this.mStatusBarStateController, this.mNotifShadeEventSource, this.mEntryManager, this.mMediaManager, this.mGutsManager, this.mKeyguardUpdateMonitor, this.mLockscreenGestureLogger, this.mInitController, this.mNotificationInterruptStateProvider, this.mRemoteInputManager, this.mConfigurationController, this.mNotifPipelineFlags);
        this.mPresenter = statusBarNotificationPresenter;
        NotificationShelfController notificationShelfController3 = this.mNotificationShelfController;
        Objects.requireNonNull(notificationShelfController3);
        NotificationShelf notificationShelf8 = notificationShelfController3.mView;
        Objects.requireNonNull(notificationShelf8);
        notificationShelf8.mOnActivatedListener = statusBarNotificationPresenter;
        this.mRemoteInputManager.addControllerCallback(this.mNotificationShadeWindowController);
        StatusBarNotificationActivityStarter.Builder builder = this.mStatusBarNotificationActivityStarterBuilder;
        Objects.requireNonNull(builder);
        StatusBarNotificationActivityStarter statusBarNotificationActivityStarter = new StatusBarNotificationActivityStarter(builder.mContext, builder.mMainThreadHandler, builder.mUiBgExecutor, builder.mEntryManager, builder.mNotifPipeline, builder.mVisibilityProvider, builder.mHeadsUpManager, builder.mActivityStarter, builder.mClickNotifier, builder.mStatusBarStateController, builder.mStatusBarKeyguardViewManager, builder.mKeyguardManager, builder.mDreamManager, builder.mBubblesManagerOptional, builder.mAssistManagerLazy, builder.mRemoteInputManager, builder.mGroupMembershipManager, builder.mLockscreenUserManager, builder.mShadeController, builder.mKeyguardStateController, builder.mNotificationInterruptStateProvider, builder.mLockPatternUtils, builder.mRemoteInputCallback, builder.mActivityIntentHelper, builder.mNotifPipelineFlags, builder.mMetricsLogger, builder.mLogger, builder.mOnUserInteractionCallback, this, this.mPresenter, this.mNotificationPanelViewController, this.mActivityLaunchAnimator, this.mNotificationAnimationProvider);
        this.mNotificationActivityStarter = statusBarNotificationActivityStarter;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController7 = this.mStackScrollerController;
        Objects.requireNonNull(notificationStackScrollLayoutController7);
        notificationStackScrollLayoutController7.mNotificationActivityStarter = statusBarNotificationActivityStarter;
        NotificationGutsManager notificationGutsManager = this.mGutsManager;
        StatusBarNotificationActivityStarter statusBarNotificationActivityStarter2 = this.mNotificationActivityStarter;
        Objects.requireNonNull(notificationGutsManager);
        notificationGutsManager.mNotificationActivityStarter = statusBarNotificationActivityStarter2;
        NotificationsController notificationsController = this.mNotificationsController;
        Optional<Bubbles> optional = this.mBubblesOptional;
        StatusBarNotificationPresenter statusBarNotificationPresenter2 = this.mPresenter;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController8 = this.mStackScrollerController;
        Objects.requireNonNull(notificationStackScrollLayoutController8);
        NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl3 = notificationStackScrollLayoutController8.mNotificationListContainer;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController9 = this.mStackScrollerController;
        Objects.requireNonNull(notificationStackScrollLayoutController9);
        notificationsController.initialize(this, optional, statusBarNotificationPresenter2, notificationListContainerImpl3, notificationStackScrollLayoutController9.mNotifStackController, this.mNotificationActivityStarter, this.mPresenter);
        if (!InsetsState.containsType(registerStatusBarResult.mTransientBarTypes, 0) || this.mTransientShown) {
            i2 = 1;
        } else {
            i2 = 1;
            this.mTransientShown = true;
            this.mNoAnimationOnNextBarModeChange = true;
            maybeUpdateBarMode();
        }
        this.mCommandQueueCallbacks.onSystemBarAttributesChanged(this.mDisplayId, registerStatusBarResult.mAppearance, registerStatusBarResult.mAppearanceRegions, registerStatusBarResult.mNavbarColorManagedByIme, registerStatusBarResult.mBehavior, registerStatusBarResult.mRequestedVisibilities, registerStatusBarResult.mPackageName);
        Objects.requireNonNull(this.mCommandQueueCallbacks);
        int size = registerStatusBarResult.mIcons.size();
        for (int i6 = 0; i6 < size; i6++) {
            this.mCommandQueue.setIcon((String) registerStatusBarResult.mIcons.keyAt(i6), (StatusBarIcon) registerStatusBarResult.mIcons.valueAt(i6));
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.systemui.statusbar.banner_action_cancel");
        intentFilter.addAction("com.android.systemui.statusbar.banner_action_setup");
        this.mContext.registerReceiver(this.mBannerActionBroadcastReceiver, intentFilter, "com.android.systemui.permission.SELF", null, 2);
        if (this.mWallpaperSupported) {
            try {
                IWallpaperManager.Stub.asInterface(ServiceManager.getService("wallpaper")).setInAmbientMode(false, 0L);
            } catch (RemoteException unused) {
            }
        }
        final PhoneStatusBarPolicy phoneStatusBarPolicy = this.mIconPolicy;
        Objects.requireNonNull(phoneStatusBarPolicy);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.HEADSET_PLUG");
        intentFilter2.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter2.addAction("android.telecom.action.CURRENT_TTY_MODE_CHANGED");
        intentFilter2.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter2.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        intentFilter2.addAction("android.intent.action.MANAGED_PROFILE_REMOVED");
        phoneStatusBarPolicy.mBroadcastDispatcher.registerReceiverWithHandler(phoneStatusBarPolicy.mIntentReceiver, intentFilter2, phoneStatusBarPolicy.mHandler);
        Observer phoneStatusBarPolicy$$ExternalSyntheticLambda0 = new Observer() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarPolicy$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                PhoneStatusBarPolicy phoneStatusBarPolicy2 = PhoneStatusBarPolicy.this;
                Integer num = (Integer) obj;
                Objects.requireNonNull(phoneStatusBarPolicy2);
                phoneStatusBarPolicy2.mHandler.post(new WifiEntry$$ExternalSyntheticLambda2(phoneStatusBarPolicy2, 7));
            }
        };
        phoneStatusBarPolicy.mRingerModeTracker.getRingerMode().observeForever(phoneStatusBarPolicy$$ExternalSyntheticLambda0);
        phoneStatusBarPolicy.mRingerModeTracker.getRingerModeInternal().observeForever(phoneStatusBarPolicy$$ExternalSyntheticLambda0);
        try {
            phoneStatusBarPolicy.mIActivityManager.registerUserSwitchObserver(phoneStatusBarPolicy.mUserSwitchListener, "PhoneStatusBarPolicy");
        } catch (RemoteException unused2) {
        }
        TelecomManager telecomManager = phoneStatusBarPolicy.mTelecomManager;
        if (telecomManager == null) {
            phoneStatusBarPolicy.updateTTY(0);
        } else {
            phoneStatusBarPolicy.updateTTY(telecomManager.getCurrentTtyMode());
        }
        phoneStatusBarPolicy.updateBluetooth();
        phoneStatusBarPolicy.mIconController.setIcon(phoneStatusBarPolicy.mSlotAlarmClock, 2131232672, null);
        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotAlarmClock, false);
        phoneStatusBarPolicy.mIconController.setIcon(phoneStatusBarPolicy.mSlotZen, 2131232678, null);
        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotZen, false);
        phoneStatusBarPolicy.mIconController.setIcon(phoneStatusBarPolicy.mSlotVibrate, 2131232686, phoneStatusBarPolicy.mResources.getString(2131951803));
        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotVibrate, false);
        phoneStatusBarPolicy.mIconController.setIcon(phoneStatusBarPolicy.mSlotMute, 2131232685, phoneStatusBarPolicy.mResources.getString(2131951802));
        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotMute, false);
        phoneStatusBarPolicy.updateVolumeZen();
        phoneStatusBarPolicy.mIconController.setIcon(phoneStatusBarPolicy.mSlotCast, 2131232675, null);
        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotCast, false);
        phoneStatusBarPolicy.mIconController.setIcon(phoneStatusBarPolicy.mSlotHotspot, 2131232683, phoneStatusBarPolicy.mResources.getString(2131951814));
        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotHotspot, phoneStatusBarPolicy.mHotspot.isHotspotEnabled());
        phoneStatusBarPolicy.mIconController.setIcon(phoneStatusBarPolicy.mSlotManagedProfile, 2131232684, phoneStatusBarPolicy.mDevicePolicyManager.getString("SystemUi.STATUS_BAR_WORK_ICON_ACCESSIBILITY", new PhoneStatusBarPolicy$$ExternalSyntheticLambda1(phoneStatusBarPolicy)));
        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotManagedProfile, phoneStatusBarPolicy.mManagedProfileIconVisible);
        phoneStatusBarPolicy.mIconController.setIcon(phoneStatusBarPolicy.mSlotDataSaver, 2131232677, phoneStatusBarPolicy.mResources.getString(2131951703));
        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotDataSaver, false);
        Resources resources = phoneStatusBarPolicy.mResources;
        PrivacyType privacyType = PrivacyType.TYPE_MICROPHONE;
        String string = resources.getString(privacyType.getNameId());
        Resources resources2 = phoneStatusBarPolicy.mResources;
        Object[] objArr = new Object[i2];
        objArr[0] = string;
        phoneStatusBarPolicy.mIconController.setIcon(phoneStatusBarPolicy.mSlotMicrophone, privacyType.getIconId(), resources2.getString(2131952940, objArr));
        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotMicrophone, false);
        Resources resources3 = phoneStatusBarPolicy.mResources;
        PrivacyType privacyType2 = PrivacyType.TYPE_CAMERA;
        String string2 = resources3.getString(privacyType2.getNameId());
        Resources resources4 = phoneStatusBarPolicy.mResources;
        Object[] objArr2 = new Object[i2];
        objArr2[0] = string2;
        phoneStatusBarPolicy.mIconController.setIcon(phoneStatusBarPolicy.mSlotCamera, privacyType2.getIconId(), resources4.getString(2131952940, objArr2));
        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotCamera, false);
        phoneStatusBarPolicy.mIconController.setIcon(phoneStatusBarPolicy.mSlotLocation, PhoneStatusBarPolicy.LOCATION_STATUS_ICON_ID, phoneStatusBarPolicy.mResources.getString(2131951752));
        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotLocation, false);
        phoneStatusBarPolicy.mIconController.setIcon(phoneStatusBarPolicy.mSlotSensorsOff, 2131232695, phoneStatusBarPolicy.mResources.getString(2131951809));
        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotSensorsOff, phoneStatusBarPolicy.mSensorPrivacyController.isSensorPrivacyEnabled());
        phoneStatusBarPolicy.mIconController.setIcon(phoneStatusBarPolicy.mSlotScreenRecord, 2131232691, null);
        phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotScreenRecord, false);
        phoneStatusBarPolicy.mRotationLockController.addCallback(phoneStatusBarPolicy);
        phoneStatusBarPolicy.mBluetooth.addCallback(phoneStatusBarPolicy);
        phoneStatusBarPolicy.mProvisionedController.addCallback(phoneStatusBarPolicy);
        phoneStatusBarPolicy.mZenController.addCallback(phoneStatusBarPolicy);
        phoneStatusBarPolicy.mCast.addCallback(phoneStatusBarPolicy.mCastCallback);
        phoneStatusBarPolicy.mHotspot.addCallback(phoneStatusBarPolicy.mHotspotCallback);
        phoneStatusBarPolicy.mNextAlarmController.addCallback(phoneStatusBarPolicy.mNextAlarmCallback);
        phoneStatusBarPolicy.mDataSaver.addCallback(phoneStatusBarPolicy);
        phoneStatusBarPolicy.mKeyguardStateController.addCallback(phoneStatusBarPolicy);
        phoneStatusBarPolicy.mPrivacyItemController.addCallback(phoneStatusBarPolicy);
        phoneStatusBarPolicy.mSensorPrivacyController.addCallback(phoneStatusBarPolicy.mSensorPrivacyListener);
        phoneStatusBarPolicy.mLocationController.addCallback(phoneStatusBarPolicy);
        RecordingController recordingController = phoneStatusBarPolicy.mRecordingController;
        Objects.requireNonNull(recordingController);
        recordingController.mListeners.add(phoneStatusBarPolicy);
        phoneStatusBarPolicy.mCommandQueue.addCallback((CommandQueue.Callbacks) phoneStatusBarPolicy);
        this.mKeyguardStateController.addCallback(new KeyguardStateController.Callback() { // from class: com.android.systemui.statusbar.phone.StatusBar.1
            {
                StatusBar.this = this;
            }

            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public final void onUnlockedChanged() {
                StatusBar statusBar = StatusBar.this;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                KeyguardStateController keyguardStateController = statusBar.mKeyguardStateController;
                StatusBarKeyguardViewManager statusBarKeyguardViewManager = statusBar.mStatusBarKeyguardViewManager;
                Objects.requireNonNull(statusBarKeyguardViewManager);
                boolean z3 = statusBarKeyguardViewManager.mShowing;
                StatusBarKeyguardViewManager statusBarKeyguardViewManager2 = statusBar.mStatusBarKeyguardViewManager;
                Objects.requireNonNull(statusBarKeyguardViewManager2);
                keyguardStateController.notifyKeyguardState(z3, statusBarKeyguardViewManager2.mOccluded);
                StatusBar.this.logStateToEventlog();
            }
        });
        Trace.beginSection("StatusBar#startKeyguard");
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockControllerLazy.get();
        this.mBiometricUnlockController = biometricUnlockController;
        AnonymousClass4 r42 = new AnonymousClass4();
        Objects.requireNonNull(biometricUnlockController);
        biometricUnlockController.mBiometricModeListener = r42;
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        NotificationPanelViewController notificationPanelViewController6 = this.mNotificationPanelViewController;
        PanelExpansionStateManager panelExpansionStateManager = this.mPanelExpansionStateManager;
        BiometricUnlockController biometricUnlockController2 = this.mBiometricUnlockController;
        NotificationStackScrollLayout notificationStackScrollLayout3 = this.mStackScroller;
        KeyguardBypassController keyguardBypassController2 = this.mKeyguardBypassController;
        Objects.requireNonNull(statusBarKeyguardViewManager);
        statusBarKeyguardViewManager.mStatusBar = this;
        statusBarKeyguardViewManager.mBiometricUnlockController = biometricUnlockController2;
        ViewGroup bouncerContainer = getBouncerContainer();
        KeyguardBouncer.Factory factory = statusBarKeyguardViewManager.mKeyguardBouncerFactory;
        StatusBarKeyguardViewManager.AnonymousClass1 r15 = statusBarKeyguardViewManager.mExpansionCallback;
        Objects.requireNonNull(factory);
        statusBarKeyguardViewManager.mBouncer = new KeyguardBouncer(factory.mContext, factory.mCallback, bouncerContainer, factory.mDismissCallbackRegistry, factory.mFalsingCollector, r15, factory.mKeyguardStateController, factory.mKeyguardUpdateMonitor, factory.mKeyguardBypassController, factory.mHandler, factory.mKeyguardSecurityModel, factory.mKeyguardBouncerComponentFactory);
        statusBarKeyguardViewManager.mNotificationPanelViewController = notificationPanelViewController6;
        if (panelExpansionStateManager != null) {
            panelExpansionStateManager.addExpansionListener(statusBarKeyguardViewManager);
        }
        statusBarKeyguardViewManager.mBypassController = keyguardBypassController2;
        statusBarKeyguardViewManager.mNotificationContainer = notificationStackScrollLayout3;
        KeyguardMessageAreaController.Factory factory2 = statusBarKeyguardViewManager.mKeyguardMessageAreaFactory;
        KeyguardMessageArea findSecurityMessageDisplay = KeyguardMessageArea.findSecurityMessageDisplay(bouncerContainer);
        Objects.requireNonNull(factory2);
        statusBarKeyguardViewManager.mKeyguardMessageAreaController = new KeyguardMessageAreaController(findSecurityMessageDisplay, factory2.mKeyguardUpdateMonitor, factory2.mConfigurationController);
        statusBarKeyguardViewManager.mKeyguardUpdateManager.registerCallback(statusBarKeyguardViewManager.mUpdateMonitorCallback);
        statusBarKeyguardViewManager.mStatusBarStateController.addCallback(statusBarKeyguardViewManager);
        statusBarKeyguardViewManager.mConfigurationController.addCallback(statusBarKeyguardViewManager);
        statusBarKeyguardViewManager.mGesturalNav = R$color.isGesturalMode(statusBarKeyguardViewManager.mNavigationModeController.addListener(statusBarKeyguardViewManager));
        FoldAodAnimationController foldAodAnimationController = statusBarKeyguardViewManager.mFoldAodAnimationController;
        if (foldAodAnimationController != null) {
            foldAodAnimationController.statusListeners.add(statusBarKeyguardViewManager);
        }
        DockManager dockManager2 = statusBarKeyguardViewManager.mDockManager;
        if (dockManager2 != null) {
            dockManager2.addListener(statusBarKeyguardViewManager.mDockEventListener);
            statusBarKeyguardViewManager.mIsDocked = statusBarKeyguardViewManager.mDockManager.isDocked();
        }
        KeyguardIndicationController keyguardIndicationController2 = this.mKeyguardIndicationController;
        StatusBarKeyguardViewManager statusBarKeyguardViewManager2 = this.mStatusBarKeyguardViewManager;
        Objects.requireNonNull(keyguardIndicationController2);
        keyguardIndicationController2.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager2;
        BiometricUnlockController biometricUnlockController3 = this.mBiometricUnlockController;
        StatusBarKeyguardViewManager statusBarKeyguardViewManager3 = this.mStatusBarKeyguardViewManager;
        Objects.requireNonNull(biometricUnlockController3);
        biometricUnlockController3.mKeyguardViewController = statusBarKeyguardViewManager3;
        this.mRemoteInputManager.addControllerCallback(this.mStatusBarKeyguardViewManager);
        DynamicPrivacyController dynamicPrivacyController = this.mDynamicPrivacyController;
        StatusBarKeyguardViewManager statusBarKeyguardViewManager4 = this.mStatusBarKeyguardViewManager;
        Objects.requireNonNull(dynamicPrivacyController);
        dynamicPrivacyController.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager4;
        LightBarController lightBarController = this.mLightBarController;
        BiometricUnlockController biometricUnlockController4 = this.mBiometricUnlockController;
        Objects.requireNonNull(lightBarController);
        lightBarController.mBiometricUnlockController = biometricUnlockController4;
        NotificationMediaManager notificationMediaManager2 = this.mMediaManager;
        BiometricUnlockController biometricUnlockController5 = this.mBiometricUnlockController;
        Objects.requireNonNull(notificationMediaManager2);
        notificationMediaManager2.mBiometricUnlockController = biometricUnlockController5;
        KeyguardDismissUtil keyguardDismissUtil = this.mKeyguardDismissUtil;
        KeyguardDismissHandler statusBar$$ExternalSyntheticLambda10 = new KeyguardDismissHandler() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda10
            @Override // com.android.systemui.statusbar.phone.KeyguardDismissHandler
            public final void executeWhenUnlocked(ActivityStarter.OnDismissAction onDismissAction, boolean z3, boolean z4) {
                StatusBar statusBar = StatusBar.this;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                StatusBarKeyguardViewManager statusBarKeyguardViewManager5 = statusBar.mStatusBarKeyguardViewManager;
                Objects.requireNonNull(statusBarKeyguardViewManager5);
                if (statusBarKeyguardViewManager5.mShowing && z3) {
                    statusBar.mStatusBarStateController.setLeaveOpenOnKeyguardHide(true);
                }
                statusBar.dismissKeyguardThenExecute(onDismissAction, null, z4);
            }
        };
        Objects.requireNonNull(keyguardDismissUtil);
        keyguardDismissUtil.mDismissHandler = statusBar$$ExternalSyntheticLambda10;
        Trace.endSection();
        this.mKeyguardUpdateMonitor.registerCallback(this.mUpdateCallback);
        DozeServiceHost dozeServiceHost = this.mDozeServiceHost;
        StatusBarKeyguardViewManager statusBarKeyguardViewManager5 = this.mStatusBarKeyguardViewManager;
        NotificationShadeWindowViewController notificationShadeWindowViewController4 = this.mNotificationShadeWindowViewController;
        NotificationPanelViewController notificationPanelViewController7 = this.mNotificationPanelViewController;
        View view = this.mAmbientIndicationContainer;
        Objects.requireNonNull(dozeServiceHost);
        dozeServiceHost.mStatusBar = this;
        dozeServiceHost.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager5;
        dozeServiceHost.mNotificationPanel = notificationPanelViewController7;
        dozeServiceHost.mNotificationShadeWindowViewController = notificationShadeWindowViewController4;
        dozeServiceHost.mAmbientIndicationContainer = view;
        updateLightRevealScrimVisibility();
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mBatteryController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) this.mBatteryStateChangeCallback);
        this.mLifecycle.setCurrentState(Lifecycle.State.RESUMED);
        AccessibilityFloatingMenuController accessibilityFloatingMenuController = this.mAccessibilityFloatingMenuController;
        Objects.requireNonNull(accessibilityFloatingMenuController);
        AccessibilityButtonModeObserver accessibilityButtonModeObserver = accessibilityFloatingMenuController.mAccessibilityButtonModeObserver;
        Objects.requireNonNull(accessibilityButtonModeObserver);
        accessibilityFloatingMenuController.mBtnMode = AccessibilityButtonModeObserver.parseAccessibilityButtonMode(Settings.Secure.getStringForUser(accessibilityButtonModeObserver.mContentResolver, accessibilityButtonModeObserver.mKey, -2));
        AccessibilityButtonTargetsObserver accessibilityButtonTargetsObserver = accessibilityFloatingMenuController.mAccessibilityButtonTargetsObserver;
        Objects.requireNonNull(accessibilityButtonTargetsObserver);
        accessibilityFloatingMenuController.mBtnTargets = Settings.Secure.getStringForUser(accessibilityButtonTargetsObserver.mContentResolver, accessibilityButtonTargetsObserver.mKey, -2);
        accessibilityFloatingMenuController.mAccessibilityButtonModeObserver.addListener(accessibilityFloatingMenuController);
        accessibilityFloatingMenuController.mAccessibilityButtonTargetsObserver.addListener(accessibilityFloatingMenuController);
        accessibilityFloatingMenuController.mKeyguardUpdateMonitor.registerCallback(accessibilityFloatingMenuController.mKeyguardCallback);
        final int i7 = registerStatusBarResult.mDisabledFlags1;
        final int i8 = registerStatusBarResult.mDisabledFlags2;
        InitController initController = this.mInitController;
        Runnable statusBar$$ExternalSyntheticLambda26 = new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                StatusBar statusBar = StatusBar.this;
                int i9 = i7;
                int i10 = i8;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.mCommandQueue.disable(statusBar.mDisplayId, i9, i10, false);
            }
        };
        Objects.requireNonNull(initController);
        if (!initController.mTasksExecuted) {
            initController.mTasks.add(statusBar$$ExternalSyntheticLambda26);
            this.mFalsingManager.addFalsingBeliefListener(this.mFalsingBeliefListener);
            this.mPluginManager.addPluginListener((PluginListener) new AnonymousClass2(), OverlayPlugin.class, true);
            this.mStartingSurfaceOptional.ifPresent(new StatusBar$$ExternalSyntheticLambda33(this, 0));
            return;
        }
        throw new IllegalStateException("post init tasks have already been executed!");
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startActivity(final Intent intent, boolean z, ActivityLaunchAnimator.Controller controller, boolean z2) {
        if (this.mKeyguardStateController.isUnlocked() || !z2) {
            startActivityDismissingKeyguard(intent, false, z, false, null, 0, controller);
            return;
        }
        boolean z3 = true;
        if (controller == null || !shouldAnimateLaunch(true, z2)) {
            z3 = false;
        }
        DelegateLaunchAnimatorController delegateLaunchAnimatorController = null;
        if (z3) {
            delegateLaunchAnimatorController = new DelegateLaunchAnimatorController(wrapAnimationController(controller, z)) { // from class: com.android.systemui.statusbar.phone.StatusBar.5
                {
                    StatusBar.this = this;
                }

                @Override // com.android.systemui.animation.DelegateLaunchAnimatorController, com.android.systemui.animation.ActivityLaunchAnimator.Controller
                public final void onIntentStarted(boolean z4) {
                    this.delegate.onIntentStarted(z4);
                    if (z4) {
                        StatusBar.this.mIsLaunchingActivityOverLockscreen = true;
                    }
                }

                @Override // com.android.systemui.animation.DelegateLaunchAnimatorController, com.android.systemui.animation.ActivityLaunchAnimator.Controller
                public final void onLaunchAnimationCancelled() {
                    StatusBar.this.mIsLaunchingActivityOverLockscreen = false;
                    this.delegate.onLaunchAnimationCancelled();
                }

                @Override // com.android.systemui.animation.LaunchAnimator.Controller
                public final void onLaunchAnimationEnd(boolean z4) {
                    StatusBar.this.mIsLaunchingActivityOverLockscreen = false;
                    this.delegate.onLaunchAnimationEnd(z4);
                }
            };
        } else if (z) {
            collapseShade();
        }
        this.mActivityLaunchAnimator.startIntentWithAnimation(delegateLaunchAnimatorController, z3, intent.getPackage(), z2, new Function1() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda36
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                StatusBar statusBar = StatusBar.this;
                Intent intent2 = intent;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                return Integer.valueOf(TaskStackBuilder.create(statusBar.mContext).addNextIntent(intent2).startActivities(StatusBar.getActivityOptions(statusBar.mDisplayId, (RemoteAnimationAdapter) obj), UserHandle.CURRENT));
            }
        });
    }

    public final void startActivityDismissingKeyguard(final Intent intent, boolean z, boolean z2, final boolean z3, final ActivityStarter.Callback callback, final int i, ActivityLaunchAnimator.Controller controller) {
        if (!z || this.mDeviceProvisionedController.isDeviceProvisioned()) {
            ActivityIntentHelper activityIntentHelper = this.mActivityIntentHelper;
            int currentUserId = this.mLockscreenUserManager.getCurrentUserId();
            Objects.requireNonNull(activityIntentHelper);
            boolean z4 = activityIntentHelper.getTargetActivityInfo(intent, currentUserId, false) == null;
            final boolean z5 = controller != null && !z4 && shouldAnimateLaunch(true, false);
            final ActivityLaunchAnimator.Controller wrapAnimationController = controller != null ? wrapAnimationController(controller, z2) : null;
            executeRunnableDismissingKeyguard(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda28
                @Override // java.lang.Runnable
                public final void run() {
                    final StatusBar statusBar = StatusBar.this;
                    final Intent intent2 = intent;
                    int i2 = i;
                    ActivityLaunchAnimator.Controller controller2 = wrapAnimationController;
                    boolean z6 = z5;
                    final boolean z7 = z3;
                    ActivityStarter.Callback callback2 = callback;
                    long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                    Objects.requireNonNull(statusBar);
                    statusBar.mAssistManagerLazy.get().hideAssist();
                    intent2.setFlags(335544320);
                    intent2.addFlags(i2);
                    final int[] iArr = {-96};
                    ActivityLaunchAnimator activityLaunchAnimator = statusBar.mActivityLaunchAnimator;
                    String str = intent2.getPackage();
                    Function1<? super RemoteAnimationAdapter, Integer> statusBar$$ExternalSyntheticLambda37 = new Function1() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda37
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            boolean z8;
                            int[] iArr2;
                            RemoteException e;
                            String action;
                            StatusBar statusBar2 = StatusBar.this;
                            boolean z9 = z7;
                            Intent intent3 = intent2;
                            int[] iArr3 = iArr;
                            long[] jArr2 = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                            Objects.requireNonNull(statusBar2);
                            ActivityOptions activityOptions = new ActivityOptions(StatusBar.getActivityOptions(statusBar2.mDisplayId, (RemoteAnimationAdapter) obj));
                            activityOptions.setDisallowEnterPictureInPictureWhileLaunching(z9);
                            if (intent3 == null || (action = intent3.getAction()) == null) {
                                z8 = false;
                            } else {
                                z8 = action.equals("android.media.action.STILL_IMAGE_CAMERA");
                            }
                            if (z8) {
                                activityOptions.setRotationAnimationHint(3);
                            }
                            if ("android.settings.panel.action.VOLUME".equals(intent3.getAction())) {
                                activityOptions.setDisallowEnterPictureInPictureWhileLaunching(true);
                            }
                            try {
                                iArr2 = iArr3;
                            } catch (RemoteException e2) {
                                e = e2;
                                iArr2 = iArr3;
                            }
                            try {
                                iArr2[0] = ActivityTaskManager.getService().startActivityAsUser((IApplicationThread) null, statusBar2.mContext.getBasePackageName(), statusBar2.mContext.getAttributionTag(), intent3, intent3.resolveTypeIfNeeded(statusBar2.mContext.getContentResolver()), (IBinder) null, (String) null, 0, 268435456, (ProfilerInfo) null, activityOptions.toBundle(), UserHandle.CURRENT.getIdentifier());
                            } catch (RemoteException e3) {
                                e = e3;
                                Log.w("StatusBar", "Unable to start activity", e);
                                return Integer.valueOf(iArr2[0]);
                            }
                            return Integer.valueOf(iArr2[0]);
                        }
                    };
                    Objects.requireNonNull(activityLaunchAnimator);
                    activityLaunchAnimator.startIntentWithAnimation(controller2, z6, str, false, statusBar$$ExternalSyntheticLambda37);
                    if (callback2 != null) {
                        callback2.onActivityStarted(iArr[0]);
                    }
                }
            }, new KeyguardDisplayManager$$ExternalSyntheticLambda1(callback, 5), z2 && wrapAnimationController == null, z4, true, z5);
        }
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable) {
        startPendingIntentDismissingKeyguard(pendingIntent, runnable, (ActivityLaunchAnimator.Controller) null);
    }

    public final boolean toggleSplitScreenMode(int i, int i2) {
        if (!this.mSplitScreenOptional.isPresent()) {
            return false;
        }
        LegacySplitScreen legacySplitScreen = this.mSplitScreenOptional.get();
        if (legacySplitScreen.isDividerVisible()) {
            if (legacySplitScreen.isMinimized() && !legacySplitScreen.isHomeStackResizable()) {
                return false;
            }
            legacySplitScreen.onUndockingTask();
            if (i2 != -1) {
                this.mMetricsLogger.action(i2);
            }
            return true;
        } else if (!legacySplitScreen.splitPrimaryTask()) {
            return false;
        } else {
            if (i != -1) {
                this.mMetricsLogger.action(i);
            }
            return true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0050, code lost:
        if (r3 == null) goto L_0x0061;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x005f, code lost:
        if (r0 != null) goto L_0x0061;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0061, code lost:
        r1 = true;
     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0056  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateDozingState() {
        /*
            r5 = this;
            boolean r0 = r5.mDozing
            r1 = 4096(0x1000, double:2.0237E-320)
            java.lang.String r3 = "dozing"
            android.os.Trace.traceCounter(r1, r3, r0)
            java.lang.String r0 = "StatusBar#updateDozingState"
            android.os.Trace.beginSection(r0)
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r0 = r5.mStatusBarKeyguardViewManager
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.mShowing
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0024
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r0 = r5.mStatusBarKeyguardViewManager
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.mOccluded
            if (r0 != 0) goto L_0x0024
            r0 = r2
            goto L_0x0025
        L_0x0024:
            r0 = r1
        L_0x0025:
            if (r0 != 0) goto L_0x0036
            boolean r0 = r5.mDozing
            if (r0 == 0) goto L_0x0034
            com.android.systemui.statusbar.phone.DozeParameters r0 = r5.mDozeParameters
            boolean r0 = r0.shouldDelayKeyguardShow()
            if (r0 == 0) goto L_0x0034
            goto L_0x0036
        L_0x0034:
            r0 = r1
            goto L_0x0037
        L_0x0036:
            r0 = r2
        L_0x0037:
            com.android.systemui.statusbar.phone.BiometricUnlockController r3 = r5.mBiometricUnlockController
            java.util.Objects.requireNonNull(r3)
            int r3 = r3.mMode
            if (r3 != r2) goto L_0x0042
            r3 = r2
            goto L_0x0043
        L_0x0042:
            r3 = r1
        L_0x0043:
            boolean r4 = r5.mDozing
            if (r4 != 0) goto L_0x0052
            com.android.systemui.statusbar.phone.DozeServiceHost r4 = r5.mDozeServiceHost
            java.util.Objects.requireNonNull(r4)
            boolean r4 = r4.mAnimateWakeup
            if (r4 == 0) goto L_0x0052
            if (r3 == 0) goto L_0x0061
        L_0x0052:
            boolean r3 = r5.mDozing
            if (r3 == 0) goto L_0x0062
            com.android.systemui.statusbar.phone.DozeParameters r3 = r5.mDozeParameters
            java.util.Objects.requireNonNull(r3)
            boolean r3 = r3.mControlScreenOffAnimation
            if (r3 == 0) goto L_0x0062
            if (r0 == 0) goto L_0x0062
        L_0x0061:
            r1 = r2
        L_0x0062:
            com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = r5.mNotificationPanelViewController
            boolean r2 = r5.mDozing
            r0.setDozing$1(r2, r1)
            r5.updateQsExpansionEnabled()
            android.os.Trace.endSection()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBar.updateDozingState():void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0031, code lost:
        if (r9.mIsKeyguard == false) goto L_0x0035;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0065, code lost:
        if (r10.mScreenState == 3) goto L_0x01d5;
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00ba  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean updateIsKeyguard(boolean r10) {
        /*
            Method dump skipped, instructions count: 470
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBar.updateIsKeyguard(boolean):boolean");
    }

    public final void updateLightRevealScrimVisibility() {
        LightRevealScrim lightRevealScrim = this.mLightRevealScrim;
        if (lightRevealScrim != null) {
            ScrimController scrimController = this.mScrimController;
            Objects.requireNonNull(scrimController);
            lightRevealScrim.setAlpha(scrimController.mState.getMaxLightRevealScrimAlpha());
        }
    }

    public final void updatePanelExpansionForKeyguard() {
        if (this.mState == 1) {
            BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
            Objects.requireNonNull(biometricUnlockController);
            if (biometricUnlockController.mMode != 1 && !this.mBouncerShowing) {
                this.mShadeController.instantExpandNotificationsPanel();
            }
        }
    }

    public final void updateQsExpansionEnabled() {
        boolean z;
        UserSwitcherController userSwitcherController;
        boolean z2 = false;
        if (this.mDeviceProvisionedController.isDeviceProvisioned() && (this.mUserSetup || (userSwitcherController = this.mUserSwitcherController) == null || !userSwitcherController.mSimpleUserSwitcher)) {
            int i = this.mDisabled2;
            if ((i & 4) != 0) {
                z = true;
            } else {
                z = false;
            }
            if (!z && (i & 1) == 0 && !this.mDozing && !ONLY_CORE_APPS) {
                z2 = true;
            }
        }
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        notificationPanelViewController.mQsExpansionEnabledPolicy = z2;
        QS qs = notificationPanelViewController.mQs;
        if (qs != null) {
            qs.setHeaderClickable(notificationPanelViewController.isQsExpansionEnabled());
        }
        ViewCompat$$ExternalSyntheticLambda0.m("updateQsExpansionEnabled - QS Expand enabled: ", z2, "StatusBar");
    }

    public final void updateReportRejectedTouchVisibility() {
        View view = this.mReportRejectedTouch;
        if (view != null) {
            if (this.mState == 1 && !this.mDozing) {
                this.mFalsingCollector.isReportingEnabled();
            }
            view.setVisibility(4);
        }
    }

    public final void updateResources() {
        KeyguardHostViewController keyguardHostViewController;
        QSPanelController qSPanelController = this.mQSPanelController;
        if (qSPanelController != null) {
            Objects.requireNonNull(qSPanelController);
            ((QSPanel) qSPanelController.mView).updateResources();
        }
        StatusBarWindowController statusBarWindowController = this.mStatusBarWindowController;
        if (statusBarWindowController != null) {
            Objects.requireNonNull(statusBarWindowController);
            int statusBarHeight = SystemBarUtils.getStatusBarHeight(statusBarWindowController.mContext);
            if (statusBarWindowController.mBarHeight != statusBarHeight) {
                statusBarWindowController.mBarHeight = statusBarHeight;
                statusBarWindowController.apply(statusBarWindowController.mCurrentState);
            }
        }
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
        if (notificationPanelViewController != null) {
            notificationPanelViewController.updateResources();
        }
        BrightnessMirrorController brightnessMirrorController = this.mBrightnessMirrorController;
        if (brightnessMirrorController != null) {
            Objects.requireNonNull(brightnessMirrorController);
            int dimensionPixelSize = brightnessMirrorController.mBrightnessMirror.getResources().getDimensionPixelSize(2131166937);
            brightnessMirrorController.mBrightnessMirrorBackgroundPadding = dimensionPixelSize;
            brightnessMirrorController.mBrightnessMirror.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        }
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        if (statusBarKeyguardViewManager != null) {
            Objects.requireNonNull(statusBarKeyguardViewManager);
            KeyguardBouncer keyguardBouncer = statusBarKeyguardViewManager.mBouncer;
            if (!(keyguardBouncer == null || (keyguardHostViewController = keyguardBouncer.mKeyguardViewController) == null)) {
                keyguardHostViewController.updateResources();
            }
        }
        this.mPowerButtonReveal = new PowerButtonReveal(this.mContext.getResources().getDimensionPixelSize(2131166769));
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0039, code lost:
        if (r1.mSurfaceBehindRemoteAnimationRunning != false) goto L_0x003b;
     */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0064 A[LOOP:0: B:23:0x0062->B:24:0x0064, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x009e  */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void updateScrimController() {
        /*
            Method dump skipped, instructions count: 327
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBar.updateScrimController():void");
    }

    public final void updateTheme() {
        ColorExtractor.GradientColors gradientColors;
        int i;
        this.mUiBgExecutor.execute(new QSFgsManagerFooter$$ExternalSyntheticLambda0(this, 4));
        SysuiColorExtractor sysuiColorExtractor = this.mColorExtractor;
        Objects.requireNonNull(sysuiColorExtractor);
        if (sysuiColorExtractor.mHasMediaArtwork) {
            gradientColors = sysuiColorExtractor.mBackdropColors;
        } else {
            gradientColors = sysuiColorExtractor.mNeutralColorsLock;
        }
        if (gradientColors.supportsDarkText()) {
            i = 2132018190;
        } else {
            i = 2132018181;
        }
        if (this.mContext.getThemeResId() != i) {
            this.mContext.setTheme(i);
            this.mConfigurationController.notifyThemeChanged();
        }
    }

    public final void updateVisibleToUser() {
        boolean z;
        boolean z2 = this.mVisibleToUser;
        if (!this.mVisible || !this.mDeviceInteractive) {
            z = false;
        } else {
            z = true;
        }
        this.mVisibleToUser = z;
        if (z2 == z) {
            return;
        }
        if (z) {
            handleVisibleToUserChangedImpl(z);
            NotificationLogger notificationLogger = this.mNotificationLogger;
            Objects.requireNonNull(notificationLogger);
            if (!notificationLogger.mLogging) {
                notificationLogger.mLogging = true;
                notificationLogger.mListContainer.setChildLocationsChangedListener(notificationLogger.mNotificationLocationsChangedListener);
                NotificationLogger.AnonymousClass1 r4 = notificationLogger.mNotificationLocationsChangedListener;
                Objects.requireNonNull(r4);
                NotificationLogger notificationLogger2 = NotificationLogger.this;
                if (!notificationLogger2.mHandler.hasCallbacks(notificationLogger2.mVisibilityReporter)) {
                    NotificationLogger notificationLogger3 = NotificationLogger.this;
                    notificationLogger3.mHandler.postAtTime(notificationLogger3.mVisibilityReporter, notificationLogger3.mLastVisibilityReportUptimeMs + 500);
                    return;
                }
                return;
            }
            return;
        }
        this.mNotificationLogger.stopNotificationLogging();
        handleVisibleToUserChangedImpl(z);
    }

    public final void visibilityChanged(boolean z) {
        if (this.mVisible != z) {
            this.mVisible = z;
            if (!z) {
                this.mGutsManager.closeAndSaveGuts(true, true, true, true);
            }
        }
        updateVisibleToUser();
    }

    public final void wakeUpIfDozing(long j, View view, String str) {
        boolean z;
        if (this.mDozing) {
            ScreenOffAnimationController screenOffAnimationController = this.mScreenOffAnimationController;
            Objects.requireNonNull(screenOffAnimationController);
            ArrayList arrayList = screenOffAnimationController.animations;
            if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    if (!(!((ScreenOffAnimation) it.next()).isAnimationPlaying())) {
                        z = false;
                        break;
                    }
                }
            }
            z = true;
            if (z) {
                this.mPowerManager.wakeUp(j, 4, SupportMenuInflater$$ExternalSyntheticOutline0.m("com.android.systemui:", str));
                this.mWakeUpComingFromTouch = true;
                view.getLocationInWindow(this.mTmpInt2);
                this.mWakeUpTouchLocation = new PointF((view.getWidth() / 2) + this.mTmpInt2[0], (view.getHeight() / 2) + this.mTmpInt2[1]);
                this.mFalsingCollector.onScreenOnFromTouch();
            }
        }
    }

    /* renamed from: -$$Nest$mmaybeEscalateHeadsUp */
    public static void m107$$Nest$mmaybeEscalateHeadsUp(StatusBar statusBar) {
        Objects.requireNonNull(statusBar);
        statusBar.mHeadsUpManager.getAllEntries().forEach(new StatusBar$$ExternalSyntheticLambda34(statusBar, 0));
        statusBar.mHeadsUpManager.releaseAllImmediately();
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0020  */
    /* renamed from: -$$Nest$mupdateRevealEffect */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void m108$$Nest$mupdateRevealEffect(com.android.systemui.statusbar.phone.StatusBar r5, boolean r6) {
        /*
            java.util.Objects.requireNonNull(r5)
            com.android.systemui.statusbar.LightRevealScrim r0 = r5.mLightRevealScrim
            if (r0 != 0) goto L_0x0008
            goto L_0x0066
        L_0x0008:
            r1 = 0
            r2 = 1
            if (r6 == 0) goto L_0x001d
            com.android.systemui.statusbar.LightRevealEffect r0 = r0.revealEffect
            boolean r0 = r0 instanceof com.android.systemui.statusbar.CircleReveal
            if (r0 != 0) goto L_0x001d
            com.android.systemui.keyguard.WakefulnessLifecycle r0 = r5.mWakefulnessLifecycle
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.mLastWakeReason
            if (r0 != r2) goto L_0x001d
            r0 = r2
            goto L_0x001e
        L_0x001d:
            r0 = r1
        L_0x001e:
            if (r6 != 0) goto L_0x002b
            com.android.systemui.keyguard.WakefulnessLifecycle r3 = r5.mWakefulnessLifecycle
            java.util.Objects.requireNonNull(r3)
            int r3 = r3.mLastSleepReason
            r4 = 4
            if (r3 != r4) goto L_0x002b
            r1 = r2
        L_0x002b:
            r2 = 1065353216(0x3f800000, float:1.0)
            if (r0 != 0) goto L_0x0053
            if (r1 == 0) goto L_0x0032
            goto L_0x0053
        L_0x0032:
            if (r6 == 0) goto L_0x003f
            com.android.systemui.statusbar.LightRevealScrim r6 = r5.mLightRevealScrim
            java.util.Objects.requireNonNull(r6)
            com.android.systemui.statusbar.LightRevealEffect r6 = r6.revealEffect
            boolean r6 = r6 instanceof com.android.systemui.statusbar.CircleReveal
            if (r6 != 0) goto L_0x0066
        L_0x003f:
            com.android.systemui.statusbar.LightRevealScrim r6 = r5.mLightRevealScrim
            com.android.systemui.statusbar.LiftReveal r0 = com.android.systemui.statusbar.LiftReveal.INSTANCE
            r6.setRevealEffect(r0)
            com.android.systemui.statusbar.LightRevealScrim r6 = r5.mLightRevealScrim
            com.android.systemui.statusbar.SysuiStatusBarStateController r5 = r5.mStatusBarStateController
            float r5 = r5.getDozeAmount()
            float r2 = r2 - r5
            r6.setRevealAmount(r2)
            goto L_0x0066
        L_0x0053:
            com.android.systemui.statusbar.LightRevealScrim r6 = r5.mLightRevealScrim
            com.android.systemui.statusbar.PowerButtonReveal r0 = r5.mPowerButtonReveal
            r6.setRevealEffect(r0)
            com.android.systemui.statusbar.LightRevealScrim r6 = r5.mLightRevealScrim
            com.android.systemui.statusbar.SysuiStatusBarStateController r5 = r5.mStatusBarStateController
            float r5 = r5.getDozeAmount()
            float r2 = r2 - r5
            r6.setRevealAmount(r2)
        L_0x0066:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBar.m108$$Nest$mupdateRevealEffect(com.android.systemui.statusbar.phone.StatusBar, boolean):void");
    }

    public static Bundle getActivityOptions(int i, RemoteAnimationAdapter remoteAnimationAdapter) {
        ActivityOptions defaultActivityOptions = getDefaultActivityOptions(remoteAnimationAdapter);
        defaultActivityOptions.setLaunchDisplayId(i);
        defaultActivityOptions.setCallerDisplayId(i);
        return defaultActivityOptions.toBundle();
    }

    public final void collapsePanelOnMainThread() {
        if (Looper.getMainLooper().isCurrentThread()) {
            this.mShadeController.collapsePanel();
            return;
        }
        Executor mainExecutor = this.mContext.getMainExecutor();
        ShadeController shadeController = this.mShadeController;
        Objects.requireNonNull(shadeController);
        mainExecutor.execute(new StatusBar$$ExternalSyntheticLambda18(shadeController, 0));
    }

    @Override // com.android.systemui.CoreStartable, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        boolean z;
        CharSequence charSequence;
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
            asIndenting.println("  mStackScroller: " + viewInfo(this.mStackScroller));
            asIndenting.println("  mStackScroller: " + viewInfo(this.mStackScroller) + " scroll " + this.mStackScroller.getScrollX() + "," + this.mStackScroller.getScrollY());
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
            dumpBarTransitions(asIndenting, "PhoneStatusBarTransitions", this.mStatusBarTransitions);
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
        boolean z2 = true;
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
        KeyguardIndicationController keyguardIndicationController = this.mKeyguardIndicationController;
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
            if (keyguardIndicationRotateTextViewController.mIndicationMessages.keySet().size() <= 0) {
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
                UdfpsKeyguardViewController.AnonymousClass2 r0 = (UdfpsKeyguardViewController.AnonymousClass2) statusBarKeyguardViewManager.mAlternateAuthInterceptor;
                Objects.requireNonNull(r0);
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
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void postStartActivityDismissingKeyguard(Intent intent, int i) {
        postStartActivityDismissingKeyguard(intent, i, null);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable, View view) {
        startPendingIntentDismissingKeyguard(pendingIntent, runnable, view instanceof ExpandableNotificationRow ? this.mNotificationAnimationProvider.getAnimatorController((ExpandableNotificationRow) view) : null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x001f, code lost:
        if (r3.mPulsing != false) goto L_0x0021;
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0018  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x003e  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateNotificationPanelTouchState() {
        /*
            r5 = this;
            boolean r0 = r5.isGoingToSleep()
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0013
            com.android.systemui.statusbar.phone.DozeParameters r0 = r5.mDozeParameters
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.mControlScreenOffAnimation
            if (r0 != 0) goto L_0x0013
            r0 = r1
            goto L_0x0014
        L_0x0013:
            r0 = r2
        L_0x0014:
            boolean r3 = r5.mDeviceInteractive
            if (r3 != 0) goto L_0x0021
            com.android.systemui.statusbar.phone.DozeServiceHost r3 = r5.mDozeServiceHost
            java.util.Objects.requireNonNull(r3)
            boolean r3 = r3.mPulsing
            if (r3 == 0) goto L_0x0023
        L_0x0021:
            if (r0 == 0) goto L_0x0025
        L_0x0023:
            r0 = r1
            goto L_0x0026
        L_0x0025:
            r0 = r2
        L_0x0026:
            com.android.systemui.statusbar.phone.NotificationPanelViewController r3 = r5.mNotificationPanelViewController
            java.util.Objects.requireNonNull(r3)
            r3.mTouchDisabled = r0
            if (r0 == 0) goto L_0x003c
            r3.cancelHeightAnimator()
            boolean r4 = r3.mTracking
            if (r4 == 0) goto L_0x0039
            r3.onTrackingStopped(r1)
        L_0x0039:
            r3.notifyExpandingFinished()
        L_0x003c:
            if (r0 == 0) goto L_0x0050
            com.android.systemui.statusbar.phone.KeyguardAffordanceHelper r4 = r3.mAffordanceHelper
            java.util.Objects.requireNonNull(r4)
            boolean r4 = r4.mSwipingInProgress
            if (r4 == 0) goto L_0x0050
            boolean r4 = r3.mIsLaunchTransitionRunning
            if (r4 != 0) goto L_0x0050
            com.android.systemui.statusbar.phone.KeyguardAffordanceHelper r4 = r3.mAffordanceHelper
            r4.reset(r2)
        L_0x0050:
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r2 = r3.mNotificationStackScrollLayoutController
            r0 = r0 ^ r1
            java.util.Objects.requireNonNull(r2)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r1 = r2.mView
            java.util.Objects.requireNonNull(r1)
            r1.mAnimationsEnabled = r0
            r1.updateNotificationAnimationStates()
            if (r0 != 0) goto L_0x006f
            java.util.ArrayList<android.view.View> r2 = r1.mSwipedOutViews
            r2.clear()
            java.util.ArrayList<com.android.systemui.statusbar.notification.row.ExpandableView> r2 = r1.mChildrenToRemoveAnimated
            r2.clear()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.clearTemporaryViewsInGroup(r1)
        L_0x006f:
            com.android.systemui.statusbar.phone.NotificationIconAreaController r5 = r5.mNotificationIconAreaController
            java.util.Objects.requireNonNull(r5)
            r5.mAnimationsEnabled = r0
            r5.updateAnimations()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBar.updateNotificationPanelTouchState():void");
    }

    public final void wakeUpForFullScreenIntent() {
        if (isGoingToSleep() || this.mDozing) {
            this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 2, "com.android.systemui:full_screen_intent");
            this.mWakeUpComingFromTouch = false;
            this.mWakeUpTouchLocation = null;
        }
    }

    public final ActivityLaunchAnimator.Controller wrapAnimationController(ActivityLaunchAnimator.Controller controller, boolean z) {
        Optional optional;
        View rootView = controller.getLaunchContainer().getRootView();
        final StatusBarWindowController statusBarWindowController = this.mStatusBarWindowController;
        Objects.requireNonNull(statusBarWindowController);
        if (rootView != statusBarWindowController.mStatusBarWindowView) {
            optional = Optional.empty();
        } else {
            controller.setLaunchContainer(statusBarWindowController.mLaunchAnimationContainer);
            optional = Optional.of(new DelegateLaunchAnimatorController(controller) { // from class: com.android.systemui.statusbar.window.StatusBarWindowController.1
                @Override // com.android.systemui.animation.LaunchAnimator.Controller
                public final void onLaunchAnimationEnd(boolean z2) {
                    this.delegate.onLaunchAnimationEnd(z2);
                    StatusBarWindowController statusBarWindowController2 = statusBarWindowController;
                    Objects.requireNonNull(statusBarWindowController2);
                    State state = statusBarWindowController2.mCurrentState;
                    if (state.mIsLaunchAnimationRunning) {
                        state.mIsLaunchAnimationRunning = false;
                        statusBarWindowController2.apply(state);
                    }
                }

                @Override // com.android.systemui.animation.DelegateLaunchAnimatorController, com.android.systemui.animation.LaunchAnimator.Controller
                public final void onLaunchAnimationStart(boolean z2) {
                    this.delegate.onLaunchAnimationStart(z2);
                    StatusBarWindowController statusBarWindowController2 = statusBarWindowController;
                    Objects.requireNonNull(statusBarWindowController2);
                    State state = statusBarWindowController2.mCurrentState;
                    if (true != state.mIsLaunchAnimationRunning) {
                        state.mIsLaunchAnimationRunning = true;
                        statusBarWindowController2.apply(state);
                    }
                }
            });
        }
        if (optional.isPresent()) {
            return (ActivityLaunchAnimator.Controller) optional.get();
        }
        if (z) {
            return new StatusBarLaunchAnimatorController(controller, this, true);
        }
        return controller;
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void postStartActivityDismissingKeyguard(Intent intent, int i, ActivityLaunchAnimator.Controller controller) {
        this.mMainExecutor.executeDelayed(new StatusBar$$ExternalSyntheticLambda23(this, intent, controller, 0), i);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r3v0, types: [com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda29] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // com.android.systemui.plugins.ActivityStarter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void startPendingIntentDismissingKeyguard(final android.app.PendingIntent r12, final java.lang.Runnable r13, final com.android.systemui.animation.ActivityLaunchAnimator.Controller r14) {
        /*
            r11 = this;
            boolean r0 = r12.isActivity()
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0024
            com.android.systemui.ActivityIntentHelper r0 = r11.mActivityIntentHelper
            android.content.Intent r3 = r12.getIntent()
            com.android.systemui.statusbar.NotificationLockscreenUserManager r4 = r11.mLockscreenUserManager
            int r4 = r4.getCurrentUserId()
            java.util.Objects.requireNonNull(r0)
            android.content.pm.ActivityInfo r0 = r0.getTargetActivityInfo(r3, r4, r1)
            if (r0 != 0) goto L_0x001f
            r0 = r2
            goto L_0x0020
        L_0x001f:
            r0 = r1
        L_0x0020:
            if (r0 == 0) goto L_0x0024
            r0 = r2
            goto L_0x0025
        L_0x0024:
            r0 = r1
        L_0x0025:
            if (r0 != 0) goto L_0x0034
            if (r14 == 0) goto L_0x0034
            boolean r3 = r12.isActivity()
            boolean r3 = r11.shouldAnimateLaunch(r3, r1)
            if (r3 == 0) goto L_0x0034
            r1 = r2
        L_0x0034:
            r2 = r1 ^ 1
            com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda29 r3 = new com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda29
            r4 = r3
            r5 = r11
            r6 = r14
            r7 = r12
            r8 = r1
            r9 = r2
            r10 = r13
            r4.<init>()
            com.android.systemui.statusbar.policy.DeviceProvisionedController r12 = r11.mDeviceProvisionedController
            boolean r12 = r12.isDeviceProvisioned()
            if (r12 != 0) goto L_0x004b
            goto L_0x0054
        L_0x004b:
            com.android.systemui.statusbar.phone.StatusBar$14 r12 = new com.android.systemui.statusbar.phone.StatusBar$14
            r12.<init>()
            r13 = 0
            r11.dismissKeyguardThenExecute(r12, r13, r0)
        L_0x0054:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBar.startPendingIntentDismissingKeyguard(android.app.PendingIntent, java.lang.Runnable, com.android.systemui.animation.ActivityLaunchAnimator$Controller):void");
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startActivity(Intent intent, boolean z, ActivityStarter.Callback callback) {
        startActivityDismissingKeyguard(intent, false, z, false, callback, 0, null);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startActivity(Intent intent, boolean z) {
        startActivityDismissingKeyguard(intent, false, z, 0);
    }

    @Override // com.android.systemui.plugins.ActivityStarter
    public final void startActivity(Intent intent, boolean z, boolean z2) {
        startActivityDismissingKeyguard(intent, z, z2, 0);
    }

    @VisibleForTesting
    public void setBarStateForTest(int i) {
        this.mState = i;
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public final Lifecycle getLifecycle() {
        return this.mLifecycle;
    }
}
