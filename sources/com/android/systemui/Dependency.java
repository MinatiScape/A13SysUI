package com.android.systemui;

import android.app.AlarmManager;
import android.app.INotificationManager;
import android.app.IWallpaperManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.IWindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.Preconditions;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.clock.ClockManager;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dock.DockManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageHelper;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarOverlayController;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.power.PowerUI;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.AccessibilityController;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.FlashlightController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.statusbar.policy.SecurityController;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.SmartReplyConstants;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tuner.TunablePadding;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.systemui.util.leak.LeakDetector;
import com.android.systemui.util.leak.LeakReporter;
import com.android.systemui.util.sensors.AsyncSensorManager;
import dagger.Lazy;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class Dependency {
    public static Dependency sDependency;
    public Lazy<AccessibilityButtonTargetsObserver> mAccessibilityButtonListController;
    public Lazy<AccessibilityButtonModeObserver> mAccessibilityButtonModeObserver;
    public Lazy<AccessibilityController> mAccessibilityController;
    public Lazy<AccessibilityFloatingMenuController> mAccessibilityFloatingMenuController;
    public Lazy<AccessibilityManagerWrapper> mAccessibilityManagerWrapper;
    public Lazy<ActivityManagerWrapper> mActivityManagerWrapper;
    public Lazy<ActivityStarter> mActivityStarter;
    public Lazy<AlarmManager> mAlarmManager;
    public Lazy<AmbientState> mAmbientStateLazy;
    public Lazy<AppOpsController> mAppOpsController;
    public Lazy<AssistManager> mAssistManager;
    public Lazy<AsyncSensorManager> mAsyncSensorManager;
    public Lazy<AutoHideController> mAutoHideController;
    public Lazy<Executor> mBackgroundExecutor;
    public Lazy<BatteryController> mBatteryController;
    public Lazy<Handler> mBgHandler;
    public Lazy<Looper> mBgLooper;
    public Lazy<BluetoothController> mBluetoothController;
    public Lazy<BroadcastDispatcher> mBroadcastDispatcher;
    public Lazy<CastController> mCastController;
    public Lazy<ClockManager> mClockManager;
    public Lazy<CommandQueue> mCommandQueue;
    public Lazy<ConfigurationController> mConfigurationController;
    public Lazy<StatusBarContentInsetsProvider> mContentInsetsProviderLazy;
    public Lazy<DarkIconDispatcher> mDarkIconDispatcher;
    public Lazy<DataSaverController> mDataSaverController;
    public Lazy<DeviceConfigProxy> mDeviceConfigProxy;
    public Lazy<DevicePolicyManagerWrapper> mDevicePolicyManagerWrapper;
    public Lazy<DeviceProvisionedController> mDeviceProvisionedController;
    public Lazy<DisplayMetrics> mDisplayMetrics;
    public Lazy<DockManager> mDockManager;
    public Lazy<DozeParameters> mDozeParameters;
    public DumpManager mDumpManager;
    public Lazy<EdgeBackGestureHandler.Factory> mEdgeBackGestureHandlerFactoryLazy;
    public Lazy<EnhancedEstimates> mEnhancedEstimates;
    public Lazy<ExtensionController> mExtensionController;
    public Lazy<FeatureFlags> mFeatureFlagsLazy;
    public Lazy<FlashlightController> mFlashlightController;
    public Lazy<ForegroundServiceController> mForegroundServiceController;
    public Lazy<ForegroundServiceNotificationListener> mForegroundServiceNotificationListener;
    public Lazy<FragmentService> mFragmentService;
    public Lazy<GarbageMonitor> mGarbageMonitor;
    public Lazy<GroupExpansionManager> mGroupExpansionManagerLazy;
    public Lazy<GroupMembershipManager> mGroupMembershipManagerLazy;
    public Lazy<HdmiCecSetMenuLanguageHelper> mHdmiCecSetMenuLanguageHelper;
    public Lazy<HotspotController> mHotspotController;
    public Lazy<INotificationManager> mINotificationManager;
    public Lazy<IStatusBarService> mIStatusBarService;
    public Lazy<IWindowManager> mIWindowManager;
    public Lazy<InternetDialogFactory> mInternetDialogFactory;
    public Lazy<KeyguardDismissUtil> mKeyguardDismissUtil;
    public Lazy<NotificationEntryManager.KeyguardEnvironment> mKeyguardEnvironment;
    public Lazy<KeyguardStateController> mKeyguardMonitor;
    public Lazy<KeyguardSecurityModel> mKeyguardSecurityModel;
    public Lazy<KeyguardUpdateMonitor> mKeyguardUpdateMonitor;
    public Lazy<LeakDetector> mLeakDetector;
    public Lazy<String> mLeakReportEmail;
    public Lazy<LeakReporter> mLeakReporter;
    public Lazy<LightBarController> mLightBarController;
    public Lazy<LocalBluetoothManager> mLocalBluetoothManager;
    public Lazy<LocationController> mLocationController;
    public Lazy<LockscreenGestureLogger> mLockscreenGestureLogger;
    public Lazy<Executor> mMainExecutor;
    public Lazy<Handler> mMainHandler;
    public Lazy<Looper> mMainLooper;
    public Lazy<ManagedProfileController> mManagedProfileController;
    public Lazy<MediaOutputDialogFactory> mMediaOutputDialogFactory;
    public Lazy<MetricsLogger> mMetricsLogger;
    public Lazy<NavigationModeController> mNavBarModeController;
    public Lazy<NavigationBarOverlayController> mNavbarButtonsControllerLazy;
    public Lazy<NavigationBarController> mNavigationBarController;
    public Lazy<NextAlarmController> mNextAlarmController;
    public Lazy<NightDisplayListener> mNightDisplayListener;
    public Lazy<NotificationEntryManager> mNotificationEntryManager;
    public Lazy<NotificationFilter> mNotificationFilter;
    public Lazy<NotificationGroupAlertTransferHelper> mNotificationGroupAlertTransferHelper;
    public Lazy<NotificationGroupManagerLegacy> mNotificationGroupManager;
    public Lazy<NotificationGutsManager> mNotificationGutsManager;
    public Lazy<NotificationListener> mNotificationListener;
    public Lazy<NotificationLockscreenUserManager> mNotificationLockscreenUserManager;
    public Lazy<NotificationLogger> mNotificationLogger;
    public Lazy<NotificationMediaManager> mNotificationMediaManager;
    public Lazy<NotificationRemoteInputManager> mNotificationRemoteInputManager;
    public Lazy<NotificationRemoteInputManager.Callback> mNotificationRemoteInputManagerCallback;
    public Lazy<NotificationSectionsManager> mNotificationSectionsManagerLazy;
    public Lazy<NotificationShadeWindowController> mNotificationShadeWindowController;
    public Lazy<NotificationViewHierarchyManager> mNotificationViewHierarchyManager;
    public Lazy<OverviewProxyService> mOverviewProxyService;
    public Lazy<PackageManagerWrapper> mPackageManagerWrapper;
    public Lazy<PluginDependencyProvider> mPluginDependencyProvider;
    public Lazy<PluginManager> mPluginManager;
    public Lazy<PrivacyDotViewController> mPrivacyDotViewControllerLazy;
    public Lazy<PrivacyItemController> mPrivacyItemController;
    public Lazy<ProtoTracer> mProtoTracer;
    public Lazy<RecordingController> mRecordingController;
    public Lazy<ReduceBrightColorsController> mReduceBrightColorsController;
    public Lazy<RemoteInputQuickSettingsDisabler> mRemoteInputQuickSettingsDisabler;
    public Lazy<RotationLockController> mRotationLockController;
    public Lazy<ScreenLifecycle> mScreenLifecycle;
    public Lazy<ScreenOffAnimationController> mScreenOffAnimationController;
    public Lazy<SecurityController> mSecurityController;
    public Lazy<SensorPrivacyController> mSensorPrivacyController;
    public Lazy<SensorPrivacyManager> mSensorPrivacyManager;
    public Lazy<ShadeController> mShadeController;
    public Lazy<SmartReplyConstants> mSmartReplyConstants;
    public Lazy<SmartReplyController> mSmartReplyController;
    public Lazy<StatusBarIconController> mStatusBarIconController;
    public Lazy<StatusBarStateController> mStatusBarStateController;
    public Lazy<SysUiState> mSysUiStateFlagsContainer;
    public Lazy<SystemStatusAnimationScheduler> mSystemStatusAnimationSchedulerLazy;
    public Lazy<SysuiColorExtractor> mSysuiColorExtractor;
    public Lazy<TelephonyListenerManager> mTelephonyListenerManager;
    public Lazy<StatusBarWindowController> mTempStatusBarWindowController;
    public Lazy<Handler> mTimeTickHandler;
    public Lazy<TunablePadding.TunablePaddingService> mTunablePaddingService;
    public Lazy<TunerService> mTunerService;
    public Lazy<UiEventLogger> mUiEventLogger;
    public Lazy<UiOffloadThread> mUiOffloadThread;
    public Lazy<UserInfoController> mUserInfoController;
    public Lazy<UserSwitcherController> mUserSwitcherController;
    public Lazy<VibratorHelper> mVibratorHelper;
    public Lazy<VisualStabilityManager> mVisualStabilityManager;
    public Lazy<VolumeDialogController> mVolumeDialogController;
    public Lazy<WakefulnessLifecycle> mWakefulnessLifecycle;
    public Lazy<IWallpaperManager> mWallpaperManager;
    public Lazy<PowerUI.WarningsUI> mWarningsUI;
    public Lazy<ZenModeController> mZenModeController;
    public static final DependencyKey<Looper> BG_LOOPER = new DependencyKey<>("background_looper");
    public static final DependencyKey<Looper> MAIN_LOOPER = new DependencyKey<>("main_looper");
    public static final DependencyKey<Handler> TIME_TICK_HANDLER = new DependencyKey<>("time_tick_handler");
    public static final DependencyKey<Handler> MAIN_HANDLER = new DependencyKey<>("main_handler");
    public static final DependencyKey<Executor> MAIN_EXECUTOR = new DependencyKey<>("main_executor");
    public static final DependencyKey<Executor> BACKGROUND_EXECUTOR = new DependencyKey<>("background_executor");
    public static final DependencyKey<String> LEAK_REPORT_EMAIL = new DependencyKey<>("leak_report_email");
    public final ArrayMap<Object, Object> mDependencies = new ArrayMap<>();
    public final ArrayMap<Object, LazyDependencyCreator> mProviders = new ArrayMap<>();

    /* loaded from: classes.dex */
    public interface LazyDependencyCreator<T> {
        T createDependency();
    }

    @Deprecated
    public static <T> T get(Class<T> cls) {
        T t;
        Dependency dependency = sDependency;
        Objects.requireNonNull(dependency);
        synchronized (dependency) {
            t = (T) dependency.mDependencies.get(cls);
            if (t == null) {
                t = (T) dependency.createDependency(cls);
                dependency.mDependencies.put(cls, t);
            }
        }
        return t;
    }

    /* loaded from: classes.dex */
    public static final class DependencyKey<V> {
        public final String mDisplayName;

        public DependencyKey(String str) {
            this.mDisplayName = str;
        }

        public final String toString() {
            return this.mDisplayName;
        }
    }

    @VisibleForTesting
    public <T> T createDependency(Object obj) {
        boolean z;
        if ((obj instanceof DependencyKey) || (obj instanceof Class)) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z);
        LazyDependencyCreator lazyDependencyCreator = this.mProviders.get(obj);
        if (lazyDependencyCreator != null) {
            return (T) lazyDependencyCreator.createDependency();
        }
        throw new IllegalArgumentException("Unsupported dependency " + obj + ". " + this.mProviders.size() + " providers known.");
    }

    @Deprecated
    public static <T> T get(DependencyKey<T> dependencyKey) {
        T t;
        Dependency dependency = sDependency;
        Objects.requireNonNull(dependency);
        synchronized (dependency) {
            t = (T) dependency.mDependencies.get(dependencyKey);
            if (t == null) {
                t = (T) dependency.createDependency(dependencyKey);
                dependency.mDependencies.put(dependencyKey, t);
            }
        }
        return t;
    }

    @VisibleForTesting
    public static void setInstance(Dependency dependency) {
        sDependency = dependency;
    }
}
