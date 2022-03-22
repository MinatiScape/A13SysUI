package com.google.android.systemui.titan;

import android.animation.AnimationHandler;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.IActivityManager;
import android.app.INotificationManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.StatsManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.smartspace.SmartspaceManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.om.OverlayManager;
import android.content.pm.IPackageManager;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.AmbientDisplayConfiguration;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.NightDisplayListener;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.media.IAudioService;
import android.media.MediaRouter2Manager;
import android.media.session.MediaSessionManager;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IThermalService;
import android.os.Looper;
import android.os.PowerManager;
import android.os.UserManager;
import android.os.Vibrator;
import android.permission.PermissionManager;
import android.service.dreams.IDreamManager;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Choreographer;
import android.view.CrossWindowBlurListeners;
import android.view.GestureDetector;
import android.view.IWindowManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.runtime.R$id;
import androidx.mediarouter.R$color;
import com.android.internal.app.AssistUtils;
import com.android.internal.app.IBatteryStats;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.LatencyTracker;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.keyguard.AdminSecondaryLockScreenController_Factory_Factory;
import com.android.keyguard.CarrierText;
import com.android.keyguard.CarrierTextController;
import com.android.keyguard.CarrierTextManager;
import com.android.keyguard.CarrierTextManager_Builder_Factory;
import com.android.keyguard.EmergencyButtonController_Factory_Factory;
import com.android.keyguard.KeyguardBiometricLockoutLogger;
import com.android.keyguard.KeyguardBiometricLockoutLogger_Factory;
import com.android.keyguard.KeyguardClockSwitchController;
import com.android.keyguard.KeyguardDisplayManager;
import com.android.keyguard.KeyguardDisplayManager_Factory;
import com.android.keyguard.KeyguardHostView;
import com.android.keyguard.KeyguardHostViewController;
import com.android.keyguard.KeyguardHostViewController_Factory;
import com.android.keyguard.KeyguardInputViewController_Factory_Factory;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardMessageAreaController_Factory_Factory;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityContainerController_Factory_Factory;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardSecurityModel_Factory;
import com.android.keyguard.KeyguardSecurityViewFlipper;
import com.android.keyguard.KeyguardSecurityViewFlipperController;
import com.android.keyguard.KeyguardSecurityViewFlipperController_Factory;
import com.android.keyguard.KeyguardSliceViewController;
import com.android.keyguard.KeyguardSliceViewController_Factory;
import com.android.keyguard.KeyguardStatusView;
import com.android.keyguard.KeyguardStatusViewController;
import com.android.keyguard.KeyguardUnfoldTransition;
import com.android.keyguard.KeyguardUnfoldTransition_Factory;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitor_Factory;
import com.android.keyguard.LiftToActivateListener_Factory;
import com.android.keyguard.LockIconView;
import com.android.keyguard.LockIconViewController;
import com.android.keyguard.LockIconViewController_Factory;
import com.android.keyguard.ViewMediatorCallback;
import com.android.keyguard.clock.ClockManager;
import com.android.keyguard.clock.ClockManager_Factory;
import com.android.keyguard.clock.ClockOptionsProvider;
import com.android.keyguard.dagger.KeyguardBouncerComponent;
import com.android.keyguard.dagger.KeyguardQsUserSwitchComponent;
import com.android.keyguard.dagger.KeyguardStatusBarViewComponent;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.keyguard.dagger.KeyguardUserSwitcherComponent;
import com.android.keyguard.mediator.ScreenOnCoordinator;
import com.android.keyguard.mediator.ScreenOnCoordinator_Factory;
import com.android.launcher3.icons.IconProvider;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager;
import com.android.settingslib.dream.DreamBackend;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.ActivityIntentHelper_Factory;
import com.android.systemui.ActivityStarterDelegate;
import com.android.systemui.ActivityStarterDelegate_Factory;
import com.android.systemui.BootCompleteCacheImpl;
import com.android.systemui.BootCompleteCacheImpl_Factory;
import com.android.systemui.CoreStartable;
import com.android.systemui.Dependency;
import com.android.systemui.ForegroundServiceController;
import com.android.systemui.ForegroundServiceController_Factory;
import com.android.systemui.ForegroundServiceNotificationListener;
import com.android.systemui.ForegroundServiceNotificationListener_Factory;
import com.android.systemui.ForegroundServicesDialog;
import com.android.systemui.ForegroundServicesDialog_Factory;
import com.android.systemui.InitController;
import com.android.systemui.InitController_Factory;
import com.android.systemui.LatencyTester;
import com.android.systemui.LatencyTester_Factory;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.ScreenDecorations_Factory;
import com.android.systemui.SliceBroadcastRelayHandler;
import com.android.systemui.SliceBroadcastRelayHandler_Factory;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.SystemUIService;
import com.android.systemui.SystemUIService_Factory;
import com.android.systemui.UiOffloadThread;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.ModeSwitchesController;
import com.android.systemui.accessibility.SystemActions;
import com.android.systemui.accessibility.SystemActions_Factory;
import com.android.systemui.accessibility.WindowMagnification;
import com.android.systemui.accessibility.WindowMagnification_Factory;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.appops.AppOpsControllerImpl;
import com.android.systemui.appops.AppOpsControllerImpl_Factory;
import com.android.systemui.assist.AssistLogger_Factory;
import com.android.systemui.assist.AssistModule_ProvideAssistUtilsFactory;
import com.android.systemui.assist.PhoneStateMonitor;
import com.android.systemui.assist.PhoneStateMonitor_Factory;
import com.android.systemui.assist.ui.DefaultUiController;
import com.android.systemui.assist.ui.DefaultUiController_Factory;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.battery.BatteryMeterViewController_Factory;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.AuthController_Factory;
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.biometrics.AuthRippleController_Factory;
import com.android.systemui.biometrics.AuthRippleView;
import com.android.systemui.biometrics.SidefpsController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.biometrics.UdfpsController_Factory;
import com.android.systemui.biometrics.UdfpsHapticsSimulator;
import com.android.systemui.biometrics.UdfpsHapticsSimulator_Factory;
import com.android.systemui.biometrics.UdfpsHbmProvider;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.classifier.BrightLineFalsingManager;
import com.android.systemui.classifier.BrightLineFalsingManager_Factory;
import com.android.systemui.classifier.DiagonalClassifier_Factory;
import com.android.systemui.classifier.DistanceClassifier_Factory;
import com.android.systemui.classifier.DoubleTapClassifier;
import com.android.systemui.classifier.DoubleTapClassifier_Factory;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.classifier.FalsingCollectorImpl_Factory;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.classifier.FalsingManagerProxy;
import com.android.systemui.classifier.FalsingManagerProxy_Factory;
import com.android.systemui.classifier.FalsingModule_ProvidesBrightLineGestureClassifiersFactory;
import com.android.systemui.classifier.FalsingModule_ProvidesDoubleTapTimeoutMsFactory;
import com.android.systemui.classifier.HistoryTracker;
import com.android.systemui.classifier.ProximityClassifier_Factory;
import com.android.systemui.classifier.SingleTapClassifier;
import com.android.systemui.classifier.SingleTapClassifier_Factory;
import com.android.systemui.classifier.TypeClassifier;
import com.android.systemui.classifier.TypeClassifier_Factory;
import com.android.systemui.clipboardoverlay.ClipboardListener;
import com.android.systemui.clipboardoverlay.ClipboardListener_Factory;
import com.android.systemui.clipboardoverlay.ClipboardOverlayControllerFactory;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.colorextraction.SysuiColorExtractor_Factory;
import com.android.systemui.communal.CommunalHostView;
import com.android.systemui.communal.CommunalHostViewController;
import com.android.systemui.communal.CommunalHostViewController_Factory;
import com.android.systemui.communal.CommunalSourceMonitor;
import com.android.systemui.communal.CommunalSourceMonitor_Factory;
import com.android.systemui.communal.CommunalStateController;
import com.android.systemui.communal.conditions.CommunalSettingCondition;
import com.android.systemui.communal.dagger.CommunalModule_ProvideCommunalSourceMonitorFactory;
import com.android.systemui.communal.dagger.CommunalViewComponent;
import com.android.systemui.controls.ControlsMetricsLoggerImpl;
import com.android.systemui.controls.ControlsMetricsLoggerImpl_Factory;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.CustomIconCache_Factory;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.controller.ControlsControllerImpl_Factory;
import com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper;
import com.android.systemui.controls.controller.ControlsTileResourceConfiguration;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.dagger.ControlsComponent_Factory;
import com.android.systemui.controls.management.ControlsEditingActivity;
import com.android.systemui.controls.management.ControlsEditingActivity_Factory;
import com.android.systemui.controls.management.ControlsFavoritingActivity;
import com.android.systemui.controls.management.ControlsListingControllerImpl;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity_Factory;
import com.android.systemui.controls.management.ControlsRequestDialog;
import com.android.systemui.controls.ui.ControlActionCoordinatorImpl;
import com.android.systemui.controls.ui.ControlActionCoordinatorImpl_Factory;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.controls.ui.ControlsActivity_Factory;
import com.android.systemui.controls.ui.ControlsUiControllerImpl;
import com.android.systemui.dagger.ContextComponentHelper;
import com.android.systemui.dagger.ContextComponentResolver;
import com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline0;
import com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline1;
import com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline2;
import com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline3;
import com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentImpl$KeyguardBouncerComponentImpl$$ExternalSyntheticOutline0;
import com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentImpl$KeyguardBouncerComponentImpl$$ExternalSyntheticOutline1;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.dagger.DependencyProvider_ProvideAccessibilityFloatingMenuControllerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideHandlerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvideLeakDetectorFactory;
import com.android.systemui.dagger.DependencyProvider_ProviderLayoutInflaterFactory;
import com.android.systemui.dagger.DependencyProvider_ProvidesChoreographerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvidesModeSwitchesControllerFactory;
import com.android.systemui.dagger.DependencyProvider_ProvidesViewMediatorCallbackFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideActivityTaskManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideFaceManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIActivityManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIAudioServiceFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIBatteryStatsFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIDreamManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIPackageManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIStatusBarServiceFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIWallPaperManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIWindowManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideInteractionJankMonitorFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideOptionalVibratorFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePackageManagerWrapperFactory;
import com.android.systemui.dagger.GlobalModule;
import com.android.systemui.dagger.GlobalModule_ProvideUiEventLoggerFactory;
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.dagger.NightDisplayListenerModule;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.SystemUIModule_ProvideBubblesManagerFactory;
import com.android.systemui.dagger.SystemUIModule_ProvideLowLightClockControllerFactory;
import com.android.systemui.dagger.WMComponent;
import com.android.systemui.decor.PrivacyDotDecorProviderFactory;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.AlwaysOnDisplayPolicy;
import com.android.systemui.doze.DozeAuthRemover;
import com.android.systemui.doze.DozeAuthRemover_Factory;
import com.android.systemui.doze.DozeDockHandler;
import com.android.systemui.doze.DozeFalsingManagerAdapter;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.doze.DozeLog_Factory;
import com.android.systemui.doze.DozeLogger;
import com.android.systemui.doze.DozeLogger_Factory;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.DozeMachine_Factory;
import com.android.systemui.doze.DozePauser;
import com.android.systemui.doze.DozePauser_Factory;
import com.android.systemui.doze.DozeScreenBrightness;
import com.android.systemui.doze.DozeScreenBrightness_Factory;
import com.android.systemui.doze.DozeScreenState;
import com.android.systemui.doze.DozeScreenState_Factory;
import com.android.systemui.doze.DozeService;
import com.android.systemui.doze.DozeService_Factory;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.doze.DozeTriggers_Factory;
import com.android.systemui.doze.DozeUi;
import com.android.systemui.doze.DozeUi_Factory;
import com.android.systemui.doze.DozeWallpaperState;
import com.android.systemui.doze.DozeWallpaperState_Factory;
import com.android.systemui.doze.dagger.DozeComponent;
import com.android.systemui.doze.dagger.DozeModule_ProvidesDozeMachinePartesFactory;
import com.android.systemui.doze.dagger.DozeModule_ProvidesDozeWakeLockFactory;
import com.android.systemui.doze.dagger.DozeModule_ProvidesWrappedServiceFactory;
import com.android.systemui.dreams.DreamOverlayContainerView;
import com.android.systemui.dreams.DreamOverlayContainerViewController;
import com.android.systemui.dreams.DreamOverlayContainerViewController_Factory;
import com.android.systemui.dreams.DreamOverlayRegistrant;
import com.android.systemui.dreams.DreamOverlayRegistrant_Factory;
import com.android.systemui.dreams.DreamOverlayService;
import com.android.systemui.dreams.DreamOverlayService_Factory;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.DreamOverlayStateController_Factory;
import com.android.systemui.dreams.DreamOverlayStatusBarView;
import com.android.systemui.dreams.DreamOverlayStatusBarViewController;
import com.android.systemui.dreams.DreamOverlayStatusBarViewController_Factory;
import com.android.systemui.dreams.SmartSpaceComplication;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationCollectionLiveData;
import com.android.systemui.dreams.complication.ComplicationCollectionViewModel;
import com.android.systemui.dreams.complication.ComplicationHostViewController;
import com.android.systemui.dreams.complication.ComplicationId;
import com.android.systemui.dreams.complication.ComplicationLayoutEngine;
import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import com.android.systemui.dreams.complication.ComplicationTypesUpdater;
import com.android.systemui.dreams.complication.ComplicationViewModel;
import com.android.systemui.dreams.complication.ComplicationViewModelProvider;
import com.android.systemui.dreams.complication.ComplicationViewModelTransformer;
import com.android.systemui.dreams.complication.DreamClockDateComplication;
import com.android.systemui.dreams.complication.DreamClockTimeComplication;
import com.android.systemui.dreams.complication.DreamWeatherComplication;
import com.android.systemui.dreams.complication.dagger.ComplicationHostViewComponent;
import com.android.systemui.dreams.complication.dagger.ComplicationHostViewComponent_ComplicationHostViewModule_ProvidesComplicationHostViewFactory;
import com.android.systemui.dreams.complication.dagger.ComplicationHostViewComponent_ComplicationHostViewModule_ProvidesComplicationPaddingFactory;
import com.android.systemui.dreams.complication.dagger.ComplicationModule_ProvidesComplicationCollectionViewModelFactory;
import com.android.systemui.dreams.complication.dagger.ComplicationViewModelComponent;
import com.android.systemui.dreams.complication.dagger.DreamClockDateComplicationComponent$Factory;
import com.android.systemui.dreams.complication.dagger.DreamClockDateComplicationComponent_DreamClockDateComplicationModule_ProvideComplicationViewFactory;
import com.android.systemui.dreams.complication.dagger.DreamClockDateComplicationComponent_DreamClockDateComplicationModule_ProvideLayoutParamsFactory;
import com.android.systemui.dreams.complication.dagger.DreamClockTimeComplicationComponent$Factory;
import com.android.systemui.dreams.complication.dagger.DreamClockTimeComplicationComponent_DreamClockTimeComplicationModule_ProvideComplicationViewFactory;
import com.android.systemui.dreams.complication.dagger.DreamClockTimeComplicationComponent_DreamClockTimeComplicationModule_ProvideLayoutParamsFactory;
import com.android.systemui.dreams.complication.dagger.DreamWeatherComplicationComponent$Factory;
import com.android.systemui.dreams.complication.dagger.DreamWeatherComplicationComponent_DreamWeatherComplicationModule_ProvideComplicationViewFactory;
import com.android.systemui.dreams.complication.dagger.DreamWeatherComplicationComponent_DreamWeatherComplicationModule_ProvideLayoutParamsFactory;
import com.android.systemui.dreams.dagger.DreamOverlayComponent;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesMaxBurnInOffsetFactory;
import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.dreams.touch.InputSession;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule$$ExternalSyntheticLambda0;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule_ProvidesSwipeToBouncerFlingAnimationUtilsClosingFactory;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule_ProvidesSwipeToBouncerFlingAnimationUtilsOpeningFactory;
import com.android.systemui.dreams.touch.dagger.InputSessionComponent;
import com.android.systemui.dump.DumpHandler;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.dump.DumpManager_Factory;
import com.android.systemui.dump.LogBufferEulogizer;
import com.android.systemui.dump.LogBufferEulogizer_Factory;
import com.android.systemui.dump.LogBufferFreezer;
import com.android.systemui.dump.SystemUIAuxiliaryDumpService;
import com.android.systemui.flags.FeatureFlagsRelease;
import com.android.systemui.flags.FeatureFlagsRelease_Factory;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.fragments.FragmentService_Factory;
import com.android.systemui.globalactions.GlobalActionsComponent;
import com.android.systemui.globalactions.GlobalActionsComponent_Factory;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.globalactions.GlobalActionsDialogLite_Factory;
import com.android.systemui.globalactions.GlobalActionsImpl;
import com.android.systemui.globalactions.GlobalActionsImpl_Factory;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageActivity;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageHelper;
import com.android.systemui.keyboard.KeyboardUI;
import com.android.systemui.keyboard.KeyboardUI_Factory;
import com.android.systemui.keyguard.DismissCallbackRegistry;
import com.android.systemui.keyguard.KeyguardLifecyclesDispatcher;
import com.android.systemui.keyguard.KeyguardLifecyclesDispatcher_Factory;
import com.android.systemui.keyguard.KeyguardService;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.LifecycleScreenStatusProvider;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.ScreenLifecycle_Factory;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle_Factory;
import com.android.systemui.keyguard.WorkLockActivity;
import com.android.systemui.keyguard.dagger.KeyguardModule_NewKeyguardViewMediatorFactory;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.log.LogBufferFactory_Factory;
import com.android.systemui.log.LogcatEchoTracker;
import com.android.systemui.log.SessionTracker;
import com.android.systemui.log.SessionTracker_Factory;
import com.android.systemui.log.dagger.LogModule_ProvidePrivacyLogBufferFactory;
import com.android.systemui.lowlightclock.LowLightClockController;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.media.KeyguardMediaController_Factory;
import com.android.systemui.media.LocalMediaManagerFactory;
import com.android.systemui.media.MediaBrowserFactory;
import com.android.systemui.media.MediaBrowserFactory_Factory;
import com.android.systemui.media.MediaCarouselController;
import com.android.systemui.media.MediaControlPanel;
import com.android.systemui.media.MediaControlPanel_Factory;
import com.android.systemui.media.MediaControllerFactory;
import com.android.systemui.media.MediaControllerFactory_Factory;
import com.android.systemui.media.MediaDataFilter;
import com.android.systemui.media.MediaDataFilter_Factory;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaDataManager_Factory;
import com.android.systemui.media.MediaDeviceManager;
import com.android.systemui.media.MediaDeviceManager_Factory;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.media.MediaFlags;
import com.android.systemui.media.MediaFlags_Factory;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.media.MediaHierarchyManager_Factory;
import com.android.systemui.media.MediaHost;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.media.MediaHostStatesManager_Factory;
import com.android.systemui.media.MediaResumeListener;
import com.android.systemui.media.MediaResumeListener_Factory;
import com.android.systemui.media.MediaSessionBasedFilter;
import com.android.systemui.media.MediaSessionBasedFilter_Factory;
import com.android.systemui.media.MediaTimeoutListener;
import com.android.systemui.media.MediaViewController;
import com.android.systemui.media.MediaViewController_Factory;
import com.android.systemui.media.ResumeMediaBrowserFactory;
import com.android.systemui.media.ResumeMediaBrowserFactory_Factory;
import com.android.systemui.media.RingtonePlayer;
import com.android.systemui.media.RingtonePlayer_Factory;
import com.android.systemui.media.SeekBarViewModel;
import com.android.systemui.media.SeekBarViewModel_Factory;
import com.android.systemui.media.dagger.MediaModule_ProvidesDreamMediaHostFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesKeyguardMediaHostFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttChipControllerSenderFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesQSMediaHostFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesQuickQSMediaHostFactory;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.media.dialog.MediaOutputDialogReceiver;
import com.android.systemui.media.dialog.MediaOutputDialogReceiver_Factory;
import com.android.systemui.media.dream.MediaDreamComplication;
import com.android.systemui.media.dream.MediaDreamSentinel;
import com.android.systemui.media.dream.MediaDreamSentinel_Factory;
import com.android.systemui.media.dream.dagger.MediaComplicationComponent$Factory;
import com.android.systemui.media.dream.dagger.MediaComplicationComponent_MediaComplicationModule_ProvideComplicationContainerFactory;
import com.android.systemui.media.dream.dagger.MediaComplicationComponent_MediaComplicationModule_ProvideLayoutParamsFactory;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManagerFactory;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper;
import com.android.systemui.media.taptotransfer.MediaTttFlags;
import com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver;
import com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavBarHelper;
import com.android.systemui.navigationbar.NavBarHelper_Factory;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarController_Factory;
import com.android.systemui.navigationbar.NavigationBarOverlayController_Factory;
import com.android.systemui.navigationbar.NavigationBar_Factory_Factory;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.NavigationModeController_Factory;
import com.android.systemui.navigationbar.TaskbarDelegate;
import com.android.systemui.navigationbar.TaskbarDelegate_Factory;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.people.PeopleProvider;
import com.android.systemui.people.PeopleSpaceActivity;
import com.android.systemui.people.widget.LaunchConversationActivity;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager_Factory;
import com.android.systemui.people.widget.PeopleSpaceWidgetPinnedReceiver;
import com.android.systemui.people.widget.PeopleSpaceWidgetProvider;
import com.android.systemui.people.widget.PeopleSpaceWidgetProvider_Factory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.PluginDependencyProvider_Factory;
import com.android.systemui.plugins.PluginEnablerImpl;
import com.android.systemui.plugins.PluginEnablerImpl_Factory;
import com.android.systemui.plugins.PluginsModule_ProvidePluginInstanceManagerFactoryFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginDebugFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginExecutorFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginInstanceFactoryFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginManagerFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginPrefsFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPrivilegedPluginsFactory;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.power.PowerNotificationWarnings_Factory;
import com.android.systemui.power.PowerUI;
import com.android.systemui.power.PowerUI_Factory;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.PrivacyDialogController_Factory;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.PrivacyItemController_Factory;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.privacy.logging.PrivacyLogger_Factory;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import com.android.systemui.qs.AutoAddTracker;
import com.android.systemui.qs.AutoAddTracker_Builder_Factory;
import com.android.systemui.qs.FgsManagerController;
import com.android.systemui.qs.FgsManagerController_Factory;
import com.android.systemui.qs.FooterActionsController;
import com.android.systemui.qs.FooterActionsController_Factory;
import com.android.systemui.qs.HeaderPrivacyIconsController_Factory;
import com.android.systemui.qs.QSAnimator;
import com.android.systemui.qs.QSAnimator_Factory;
import com.android.systemui.qs.QSContainerImplController;
import com.android.systemui.qs.QSContainerImplController_Factory;
import com.android.systemui.qs.QSExpansionPathInterpolator;
import com.android.systemui.qs.QSExpansionPathInterpolator_Factory;
import com.android.systemui.qs.QSFgsManagerFooter;
import com.android.systemui.qs.QSFgsManagerFooter_Factory;
import com.android.systemui.qs.QSFooter;
import com.android.systemui.qs.QSFooterViewController;
import com.android.systemui.qs.QSFooterViewController_Factory;
import com.android.systemui.qs.QSFragment;
import com.android.systemui.qs.QSFragmentDisableFlagsLogger;
import com.android.systemui.qs.QSPanelController;
import com.android.systemui.qs.QSPanelController_Factory;
import com.android.systemui.qs.QSSecurityFooter_Factory;
import com.android.systemui.qs.QSSquishinessController;
import com.android.systemui.qs.QSSquishinessController_Factory;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.QSTileHost_Factory;
import com.android.systemui.qs.QSTileRevealController_Factory_Factory;
import com.android.systemui.qs.QuickQSPanelController;
import com.android.systemui.qs.QuickQSPanelController_Factory;
import com.android.systemui.qs.QuickStatusBarHeaderController_Factory;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.qs.carrier.QSCarrierGroupController_Builder_Factory;
import com.android.systemui.qs.carrier.QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory;
import com.android.systemui.qs.customize.QSCustomizer;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.customize.QSCustomizerController_Factory;
import com.android.systemui.qs.customize.TileAdapter;
import com.android.systemui.qs.customize.TileAdapter_Factory;
import com.android.systemui.qs.customize.TileQueryHelper;
import com.android.systemui.qs.customize.TileQueryHelper_Factory;
import com.android.systemui.qs.dagger.QSFlagsModule_IsPMLiteEnabledFactory;
import com.android.systemui.qs.dagger.QSFragmentComponent;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvideQSPanelFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvideRootViewFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvideThemedContextFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQSFooterActionsViewFactory;
import com.android.systemui.qs.dagger.QSFragmentModule_ProvidesQuickQSPanelFactory;
import com.android.systemui.qs.external.C0013TileLifecycleManager_Factory;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.external.CustomTileStatePersister;
import com.android.systemui.qs.external.PackageManagerAdapter;
import com.android.systemui.qs.external.PackageManagerAdapter_Factory;
import com.android.systemui.qs.external.TileLifecycleManager;
import com.android.systemui.qs.external.TileLifecycleManager_Factory_Impl;
import com.android.systemui.qs.external.TileServiceRequestController;
import com.android.systemui.qs.external.TileServiceRequestController_Builder_Factory;
import com.android.systemui.qs.external.TileServices;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.logging.QSLogger_Factory;
import com.android.systemui.qs.tiles.AirplaneModeTile;
import com.android.systemui.qs.tiles.AlarmTile;
import com.android.systemui.qs.tiles.AlarmTile_Factory;
import com.android.systemui.qs.tiles.BluetoothTile;
import com.android.systemui.qs.tiles.BluetoothTile_Factory;
import com.android.systemui.qs.tiles.CameraToggleTile;
import com.android.systemui.qs.tiles.CastTile;
import com.android.systemui.qs.tiles.CastTile_Factory;
import com.android.systemui.qs.tiles.CellularTile;
import com.android.systemui.qs.tiles.ColorCorrectionTile;
import com.android.systemui.qs.tiles.ColorCorrectionTile_Factory;
import com.android.systemui.qs.tiles.ColorInversionTile;
import com.android.systemui.qs.tiles.DataSaverTile;
import com.android.systemui.qs.tiles.DeviceControlsTile;
import com.android.systemui.qs.tiles.DeviceControlsTile_Factory;
import com.android.systemui.qs.tiles.DndTile;
import com.android.systemui.qs.tiles.FlashlightTile;
import com.android.systemui.qs.tiles.HotspotTile;
import com.android.systemui.qs.tiles.InternetTile;
import com.android.systemui.qs.tiles.LocationTile;
import com.android.systemui.qs.tiles.MicrophoneToggleTile;
import com.android.systemui.qs.tiles.MicrophoneToggleTile_Factory;
import com.android.systemui.qs.tiles.NfcTile;
import com.android.systemui.qs.tiles.NfcTile_Factory;
import com.android.systemui.qs.tiles.NightDisplayTile;
import com.android.systemui.qs.tiles.OneHandedModeTile;
import com.android.systemui.qs.tiles.OneHandedModeTile_Factory;
import com.android.systemui.qs.tiles.QRCodeScannerTile;
import com.android.systemui.qs.tiles.QRCodeScannerTile_Factory;
import com.android.systemui.qs.tiles.QuickAccessWalletTile;
import com.android.systemui.qs.tiles.QuickAccessWalletTile_Factory;
import com.android.systemui.qs.tiles.ReduceBrightColorsTile;
import com.android.systemui.qs.tiles.RotationLockTile_Factory;
import com.android.systemui.qs.tiles.ScreenRecordTile;
import com.android.systemui.qs.tiles.UiModeNightTile;
import com.android.systemui.qs.tiles.UserDetailView;
import com.android.systemui.qs.tiles.UserDetailView_Adapter_Factory;
import com.android.systemui.qs.tiles.WifiTile;
import com.android.systemui.qs.tiles.WorkModeTile;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.qs.tiles.dialog.InternetDialogController_Factory;
import com.android.systemui.qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.qs.tiles.dialog.InternetDialogFactory_Factory;
import com.android.systemui.qs.user.UserSwitchDialogController;
import com.android.systemui.qs.user.UserSwitchDialogController_Factory;
import com.android.systemui.recents.OverviewProxyRecentsImpl;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.OverviewProxyService_Factory;
import com.android.systemui.recents.Recents;
import com.android.systemui.recents.RecentsImplementation;
import com.android.systemui.recents.ScreenPinningRequest;
import com.android.systemui.recents.ScreenPinningRequest_Factory;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.screenrecord.RecordingService;
import com.android.systemui.screenrecord.RecordingService_Factory;
import com.android.systemui.screenshot.ActionProxyReceiver;
import com.android.systemui.screenshot.ActionProxyReceiver_Factory;
import com.android.systemui.screenshot.DeleteScreenshotReceiver;
import com.android.systemui.screenshot.ImageExporter_Factory;
import com.android.systemui.screenshot.ImageTileSet_Factory;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.android.systemui.screenshot.LongScreenshotData;
import com.android.systemui.screenshot.LongScreenshotData_Factory;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScreenshotController_Factory;
import com.android.systemui.screenshot.ScreenshotNotificationsController;
import com.android.systemui.screenshot.ScreenshotNotificationsController_Factory;
import com.android.systemui.screenshot.ScreenshotSmartActions;
import com.android.systemui.screenshot.ScreenshotSmartActions_Factory;
import com.android.systemui.screenshot.ScrollCaptureClient;
import com.android.systemui.screenshot.ScrollCaptureClient_Factory;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.android.systemui.screenshot.ScrollCaptureController_Factory;
import com.android.systemui.screenshot.SmartActionsReceiver;
import com.android.systemui.screenshot.SmartActionsReceiver_Factory;
import com.android.systemui.screenshot.TakeScreenshotService;
import com.android.systemui.screenshot.TakeScreenshotService_Factory;
import com.android.systemui.screenshot.TimeoutHandler;
import com.android.systemui.sensorprivacy.SensorUseStartedActivity;
import com.android.systemui.sensorprivacy.SensorUseStartedActivity_Factory;
import com.android.systemui.sensorprivacy.television.TvUnblockSensorActivity;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.settings.brightness.BrightnessController_Factory_Factory;
import com.android.systemui.settings.brightness.BrightnessDialog;
import com.android.systemui.settings.brightness.BrightnessDialog_Factory;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.settings.brightness.BrightnessSliderController_Factory_Factory;
import com.android.systemui.settings.dagger.SettingsModule_ProvideUserTrackerFactory;
import com.android.systemui.shared.plugins.PluginActionManager;
import com.android.systemui.shared.plugins.PluginInstance;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.plugins.PluginPrefs;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.systemui.shortcut.ShortcutKeyDispatcher;
import com.android.systemui.shortcut.ShortcutKeyDispatcher_Factory;
import com.android.systemui.statusbar.ActionClickLogger;
import com.android.systemui.statusbar.ActionClickLogger_Factory;
import com.android.systemui.statusbar.BlurUtils;
import com.android.systemui.statusbar.BlurUtils_Factory;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.DisableFlagsLogger;
import com.android.systemui.statusbar.DisableFlagsLogger_Factory;
import com.android.systemui.statusbar.HeadsUpStatusBarView;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController_Factory;
import com.android.systemui.statusbar.MediaArtworkProcessor;
import com.android.systemui.statusbar.MediaArtworkProcessor_Factory;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationInteractionTracker;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationListener_Factory;
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeDepthController_Factory;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.OperatorNameViewController;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.PulseExpansionHandler_Factory;
import com.android.systemui.statusbar.QsFrameTranslateImpl;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.RemoteInputNotificationRebuilder;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.VibratorHelper_Factory;
import com.android.systemui.statusbar.charging.WiredChargingRippleController;
import com.android.systemui.statusbar.charging.WiredChargingRippleController_Factory;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl_WifiPickerTrackerFactory_Factory;
import com.android.systemui.statusbar.connectivity.CallbackHandler;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl_Factory;
import com.android.systemui.statusbar.core.StatusBarInitializer;
import com.android.systemui.statusbar.core.StatusBarInitializer_Factory;
import com.android.systemui.statusbar.dagger.StatusBarDependenciesModule_ProvideActivityLaunchAnimatorFactory;
import com.android.systemui.statusbar.dagger.StatusBarDependenciesModule_ProvideNotificationMediaManagerFactory;
import com.android.systemui.statusbar.dagger.StatusBarDependenciesModule_ProvideNotificationRemoteInputManagerFactory;
import com.android.systemui.statusbar.dagger.StatusBarDependenciesModule_ProvideNotificationViewHierarchyManagerFactory;
import com.android.systemui.statusbar.dagger.StatusBarDependenciesModule_ProvideOngoingCallControllerFactory;
import com.android.systemui.statusbar.dagger.StatusBarDependenciesModule_ProvideSmartReplyControllerFactory;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.statusbar.events.PrivacyDotViewController_Factory;
import com.android.systemui.statusbar.events.SystemEventChipAnimationController;
import com.android.systemui.statusbar.events.SystemEventChipAnimationController_Factory;
import com.android.systemui.statusbar.events.SystemEventCoordinator;
import com.android.systemui.statusbar.events.SystemEventCoordinator_Factory;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler_Factory;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler_Factory;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureLogger;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.ConversationNotificationManager_Factory;
import com.android.systemui.statusbar.notification.ConversationNotificationProcessor;
import com.android.systemui.statusbar.notification.DynamicChildBindController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.ForegroundServiceDismissalFeatureController;
import com.android.systemui.statusbar.notification.InstantAppNotifier;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationClicker;
import com.android.systemui.statusbar.notification.NotificationClickerLogger;
import com.android.systemui.statusbar.notification.NotificationClicker_Builder_Factory;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.NotificationFilter_Factory;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifCollection_Factory;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotifPipeline_Factory;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager_Factory;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescerLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.AppOpsCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.BubbleCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.CommunalCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.CommunalCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.DataStoreCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.DebugModeCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.DeviceProvisionedCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.DeviceProvisionedCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.GroupCountCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.GroupCountCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.HideLocallyDismissedNotifsCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.HideLocallyDismissedNotifsCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.HideNotifsForOtherUsersCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.KeyguardCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.MediaCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinatorsImpl;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinatorsImpl_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.RowAppearanceCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.StackCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.ViewConfigCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.ViewConfigCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorsModule_NotifCoordinatorsFactory;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorsSubcomponent;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl_Factory;
import com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustmentProvider;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl_Factory;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer_Factory;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationPresenterExtensions;
import com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy_Factory;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionLogger;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.provider.NotificationVisibilityProviderImpl;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider_Factory;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.MediaContainerController;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager;
import com.android.systemui.statusbar.notification.collection.render.NotifPanelEventSource;
import com.android.systemui.statusbar.notification.collection.render.NotifPanelEventSourceModule_ProvideManagerFactory;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import com.android.systemui.statusbar.notification.collection.render.NotifViewBarn;
import com.android.systemui.statusbar.notification.collection.render.NotifViewBarn_Factory;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager_Factory;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl_Factory;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewDifferLogger;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory_Impl;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManager_Factory;
import com.android.systemui.statusbar.notification.collection.render.StatusBarNotifPanelEventSourceModule_BindStartableFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesAlertingHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesIncomingHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesPeopleHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationEntryManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationGutsManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationLoggerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationPanelLoggerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideVisualStabilityManagerFactory;
import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import com.android.systemui.statusbar.notification.icon.IconBuilder;
import com.android.systemui.statusbar.notification.icon.IconManager;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.notification.init.NotificationsControllerImpl;
import com.android.systemui.statusbar.notification.init.NotificationsControllerImpl_Factory;
import com.android.systemui.statusbar.notification.init.NotificationsControllerStub;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController_Factory;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinderLogger;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptLogger;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProviderImpl;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProviderImpl_Factory;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.logging.NotificationLogger_ExpansionStateLogger_Factory;
import com.android.systemui.statusbar.notification.logging.NotificationPanelLogger;
import com.android.systemui.statusbar.notification.people.NotificationPersonExtractorPluginBoundary;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController_Factory;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController_Factory;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController_Factory;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer_Factory;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineLogger;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager_Factory;
import com.android.systemui.statusbar.notification.row.NotifRemoteViewCache;
import com.android.systemui.statusbar.notification.row.NotifRemoteViewCacheImpl;
import com.android.systemui.statusbar.notification.row.NotificationContentInflater;
import com.android.systemui.statusbar.notification.row.NotificationContentInflater_Factory;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.notification.row.RowContentBindStageLogger;
import com.android.systemui.statusbar.notification.row.RowContentBindStage_Factory;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideAppNameFactory;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory;
import com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.AmbientState_Factory;
import com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper_Builder_Factory;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.AutoHideController_Factory_Factory;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.BiometricUnlockController_Factory;
import com.android.systemui.statusbar.phone.C0014LightBarController_Factory;
import com.android.systemui.statusbar.phone.DarkIconDispatcherImpl;
import com.android.systemui.statusbar.phone.DarkIconDispatcherImpl_Factory;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.DozeParameters_Factory;
import com.android.systemui.statusbar.phone.DozeScrimController;
import com.android.systemui.statusbar.phone.DozeServiceHost;
import com.android.systemui.statusbar.phone.DozeServiceHost_Factory;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController_Factory;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.phone.KeyguardBouncer_Factory_Factory;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil_Factory;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl;
import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import com.android.systemui.statusbar.phone.KeyguardStatusBarViewController;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LightBarController_Factory_Factory;
import com.android.systemui.statusbar.phone.LightsOutNotifController;
import com.android.systemui.statusbar.phone.LightsOutNotifController_Factory;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.LockscreenWallpaper;
import com.android.systemui.statusbar.phone.LockscreenWallpaper_Factory;
import com.android.systemui.statusbar.phone.ManagedProfileControllerImpl;
import com.android.systemui.statusbar.phone.ManagedProfileControllerImpl_Factory;
import com.android.systemui.statusbar.phone.MultiUserSwitchController;
import com.android.systemui.statusbar.phone.MultiUserSwitchController_Factory_Factory;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationIconAreaController_Factory;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import com.android.systemui.statusbar.phone.NotificationPanelUnfoldAnimationController;
import com.android.systemui.statusbar.phone.NotificationPanelUnfoldAnimationController_Factory;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController_Factory;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl_Factory;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.statusbar.phone.NotificationTapHelper_Factory_Factory;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy_Factory;
import com.android.systemui.statusbar.phone.PhoneStatusBarTransitions;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController_Factory_Factory;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ScrimController_Factory;
import com.android.systemui.statusbar.phone.ShadeControllerImpl;
import com.android.systemui.statusbar.phone.ShadeControllerImpl_Factory;
import com.android.systemui.statusbar.phone.SplitShadeHeaderController;
import com.android.systemui.statusbar.phone.SplitShadeHeaderController_Factory;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarCommandQueueCallbacks;
import com.android.systemui.statusbar.phone.StatusBarCommandQueueCallbacks_Factory;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider_Factory;
import com.android.systemui.statusbar.phone.StatusBarDemoMode;
import com.android.systemui.statusbar.phone.StatusBarDemoMode_Factory;
import com.android.systemui.statusbar.phone.StatusBarHeadsUpChangeListener;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarIconControllerImpl;
import com.android.systemui.statusbar.phone.StatusBarIconControllerImpl_Factory;
import com.android.systemui.statusbar.phone.StatusBarIconController_TintedIconManager_Factory_Factory;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager_Factory;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher_Factory;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarterLogger;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter_Builder_Factory;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback_Factory;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy_Factory;
import com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager;
import com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager_Factory;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.SystemUIDialogManager_Factory;
import com.android.systemui.statusbar.phone.TapAgainView;
import com.android.systemui.statusbar.phone.TapAgainViewController;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController_Factory;
import com.android.systemui.statusbar.phone.dagger.StatusBarComponent;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_CreateCollapsedStatusBarFragmentFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetBatteryMeterViewControllerFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_ProvidesStatusBarWindowViewFactory;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragmentLogger;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvidePhoneStatusBarViewControllerFactory;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallFlags;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager_Factory;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker_Factory;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherContainer;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherFeatureController;
import com.android.systemui.statusbar.policy.AccessibilityController;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryStateNotifier;
import com.android.systemui.statusbar.policy.BluetoothControllerImpl;
import com.android.systemui.statusbar.policy.BluetoothControllerImpl_Factory;
import com.android.systemui.statusbar.policy.CastControllerImpl;
import com.android.systemui.statusbar.policy.CastControllerImpl_Factory;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsControllerImpl;
import com.android.systemui.statusbar.policy.DevicePostureControllerImpl;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl_Factory;
import com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController;
import com.android.systemui.statusbar.policy.ExtensionControllerImpl;
import com.android.systemui.statusbar.policy.ExtensionControllerImpl_Factory;
import com.android.systemui.statusbar.policy.FlashlightControllerImpl;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.HeadsUpManagerLogger;
import com.android.systemui.statusbar.policy.HotspotControllerImpl;
import com.android.systemui.statusbar.policy.HotspotControllerImpl_Factory;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController_Factory;
import com.android.systemui.statusbar.policy.KeyguardStateControllerImpl;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController_Factory;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherView;
import com.android.systemui.statusbar.policy.LocationControllerImpl;
import com.android.systemui.statusbar.policy.LocationControllerImpl_Factory;
import com.android.systemui.statusbar.policy.NextAlarmControllerImpl;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler_Factory;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import com.android.systemui.statusbar.policy.RemoteInputView;
import com.android.systemui.statusbar.policy.RemoteInputViewController;
import com.android.systemui.statusbar.policy.RemoteInputViewControllerImpl;
import com.android.systemui.statusbar.policy.RotationLockControllerImpl;
import com.android.systemui.statusbar.policy.SecurityControllerImpl;
import com.android.systemui.statusbar.policy.SecurityControllerImpl_Factory;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.SmartActionInflaterImpl;
import com.android.systemui.statusbar.policy.SmartActionInflaterImpl_Factory;
import com.android.systemui.statusbar.policy.SmartReplyConstants;
import com.android.systemui.statusbar.policy.SmartReplyInflaterImpl;
import com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl;
import com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl_Factory;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.statusbar.policy.UserSwitcherController_Factory;
import com.android.systemui.statusbar.policy.VariableDateViewController_Factory_Factory;
import com.android.systemui.statusbar.policy.WalletControllerImpl;
import com.android.systemui.statusbar.policy.ZenModeControllerImpl;
import com.android.systemui.statusbar.policy.dagger.RemoteInputViewSubcomponent;
import com.android.systemui.statusbar.policy.dagger.StatusBarPolicyModule_ProvideAccessPointControllerImplFactory;
import com.android.systemui.statusbar.tv.TvStatusBar_Factory;
import com.android.systemui.statusbar.tv.notifications.TvNotificationHandler;
import com.android.systemui.statusbar.tv.notifications.TvNotificationHandler_Factory;
import com.android.systemui.statusbar.tv.notifications.TvNotificationPanelActivity;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.statusbar.window.StatusBarWindowController_Factory;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.statusbar.window.StatusBarWindowView;
import com.android.systemui.telephony.TelephonyCallback_Factory;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.telephony.TelephonyListenerManager_Factory;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.theme.ThemeOverlayController;
import com.android.systemui.theme.ThemeOverlayController_Factory;
import com.android.systemui.toast.ToastFactory;
import com.android.systemui.toast.ToastFactory_Factory;
import com.android.systemui.toast.ToastLogger;
import com.android.systemui.toast.ToastLogger_Factory;
import com.android.systemui.toast.ToastUI;
import com.android.systemui.toast.ToastUI_Factory;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tracing.ProtoTracer_Factory;
import com.android.systemui.tuner.TunablePadding;
import com.android.systemui.tuner.TunerActivity;
import com.android.systemui.tuner.TunerActivity_Factory;
import com.android.systemui.tuner.TunerServiceImpl;
import com.android.systemui.tuner.TunerServiceImpl_Factory;
import com.android.systemui.tv.TvSystemUIModule_ProvideBatteryControllerFactory;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.unfold.FoldStateLogger;
import com.android.systemui.unfold.FoldStateLoggingProvider;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.SysUIUnfoldModule;
import com.android.systemui.unfold.SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory;
import com.android.systemui.unfold.UnfoldLatencyTracker;
import com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation;
import com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation_Factory;
import com.android.systemui.unfold.UnfoldSharedModule;
import com.android.systemui.unfold.UnfoldSharedModule_HingeAngleProviderFactory;
import com.android.systemui.unfold.UnfoldSharedModule_UnfoldTransitionProgressProviderFactory;
import com.android.systemui.unfold.UnfoldTransitionModule;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.UnfoldTransitionWallpaperController;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider_Factory;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.unfold.updates.hinge.HingeAngleProvider;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener;
import com.android.systemui.unfold.util.C0015ScaleAwareTransitionProgressProvider_Factory;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider_Factory_Impl;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import com.android.systemui.usb.StorageNotification;
import com.android.systemui.usb.UsbDebuggingActivity;
import com.android.systemui.usb.UsbDebuggingActivity_Factory;
import com.android.systemui.usb.UsbDebuggingSecondaryUserActivity;
import com.android.systemui.usb.UsbDebuggingSecondaryUserActivity_Factory;
import com.android.systemui.user.CreateUserActivity;
import com.android.systemui.user.CreateUserActivity_Factory;
import com.android.systemui.user.UserCreator;
import com.android.systemui.user.UserCreator_Factory;
import com.android.systemui.user.UserSwitcherActivity;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.CarrierConfigTracker_Factory;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.DeviceConfigProxy_Factory;
import com.android.systemui.util.NotificationChannels;
import com.android.systemui.util.RingerModeTrackerImpl;
import com.android.systemui.util.RingerModeTrackerImpl_Factory;
import com.android.systemui.util.WallpaperController;
import com.android.systemui.util.WallpaperController_Factory;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.concurrency.ExecutionImpl_Factory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainLooperFactory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvidesUncaughtExceptionHandlerFactory;
import com.android.systemui.util.concurrency.MessageRouter;
import com.android.systemui.util.concurrency.RepeatableExecutor;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBgLooperFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory;
import com.android.systemui.util.concurrency.ThreadFactoryImpl_Factory;
import com.android.systemui.util.condition.Condition;
import com.android.systemui.util.condition.Monitor;
import com.android.systemui.util.condition.dagger.MonitorComponent;
import com.android.systemui.util.io.Files;
import com.android.systemui.util.io.Files_Factory;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.systemui.util.leak.GarbageMonitor_Factory;
import com.android.systemui.util.leak.GarbageMonitor_Service_Factory;
import com.android.systemui.util.leak.LeakDetector;
import com.android.systemui.util.leak.LeakReporter;
import com.android.systemui.util.leak.LeakReporter_Factory;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.PostureDependentProximitySensor_Factory;
import com.android.systemui.util.sensors.ProximityCheck;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.SensorModule_ProvidePostureToProximitySensorMappingFactory;
import com.android.systemui.util.sensors.SensorModule_ProvidePostureToSecondaryProximitySensorMappingFactory;
import com.android.systemui.util.sensors.SensorModule_ProvidePrimaryProximitySensorFactory;
import com.android.systemui.util.sensors.SensorModule_ProvideSecondaryProximitySensorFactory;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import com.android.systemui.util.sensors.ThresholdSensorImpl_BuilderFactory_Factory;
import com.android.systemui.util.sensors.ThresholdSensorImpl_Builder_Factory;
import com.android.systemui.util.service.ObservableServiceConnection;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.settings.SecureSettingsImpl;
import com.android.systemui.util.settings.SecureSettingsImpl_Factory;
import com.android.systemui.util.time.DateFormatUtil;
import com.android.systemui.util.time.DateFormatUtil_Factory;
import com.android.systemui.util.time.SystemClock;
import com.android.systemui.util.time.SystemClockImpl_Factory;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.DelayedWakeLock_Builder_Factory;
import com.android.systemui.util.wakelock.WakeLock;
import com.android.systemui.util.wakelock.WakeLock_Builder_Factory;
import com.android.systemui.util.wrapper.RotationPolicyWrapper;
import com.android.systemui.util.wrapper.RotationPolicyWrapperImpl;
import com.android.systemui.util.wrapper.RotationPolicyWrapperImpl_Factory;
import com.android.systemui.volume.VolumeDialogComponent;
import com.android.systemui.volume.VolumeDialogComponent_Factory;
import com.android.systemui.volume.VolumeDialogControllerImpl;
import com.android.systemui.volume.VolumeDialogControllerImpl_Factory;
import com.android.systemui.volume.VolumeUI;
import com.android.systemui.volume.VolumeUI_Factory;
import com.android.systemui.volume.dagger.VolumeModule_ProvideVolumeDialogFactory;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import com.android.systemui.wallet.controller.QuickAccessWalletController_Factory;
import com.android.systemui.wallet.ui.WalletActivity;
import com.android.systemui.wallet.ui.WalletActivity_Factory;
import com.android.systemui.wmshell.BubblesManager;
import com.android.systemui.wmshell.WMShell;
import com.android.systemui.wmshell.WMShell_Factory;
import com.android.wm.shell.RootDisplayAreaOrganizer;
import com.android.wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.ShellCommandHandlerImpl;
import com.android.wm.shell.ShellInit;
import com.android.wm.shell.ShellInitImpl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.TaskViewFactory;
import com.android.wm.shell.TaskViewFactoryController;
import com.android.wm.shell.TaskViewTransitions;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.animation.FlingAnimationUtils;
import com.android.wm.shell.animation.FlingAnimationUtils_Builder_Factory;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.back.BackAnimationController;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.compatui.CompatUI;
import com.android.wm.shell.compatui.CompatUIController;
import com.android.wm.shell.dagger.TvPipModule_ProvideTvPipBoundsStateFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideBubblesFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideDisplayControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideDisplayLayoutFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideDragAndDropControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideFloatingContentCoordinatorFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideHideDisplayCutoutFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvidePipUiEventLoggerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideRecentTasksControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideRecentTasksFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideShellCommandHandlerImplFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideShellInitImplFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideShellTaskOrganizerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideSplitScreenFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideSystemWindowsFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideTaskViewFactoryControllerFactory;
import com.android.wm.shell.dagger.WMShellBaseModule_ProvideTransactionPoolFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideMainHandlerFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideShellMainExecutorFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideShellMainHandlerFactory;
import com.android.wm.shell.dagger.WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideAppPairsFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideBubbleControllerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideLegacySplitScreenFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideOneHandedControllerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipMotionHelperFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipSnapAlgorithmFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipTaskOrganizerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipTouchHandlerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipTransitionControllerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidePipTransitionStateFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideSplitScreenControllerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvideUnfoldBackgroundControllerFactory;
import com.android.wm.shell.dagger.WMShellModule_ProvidesPipBoundsAlgorithmFactory;
import com.android.wm.shell.displayareahelper.DisplayAreaHelper;
import com.android.wm.shell.draganddrop.DragAndDrop;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.freeform.FreeformTaskListener;
import com.android.wm.shell.fullscreen.FullscreenTaskListener;
import com.android.wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipTransitionState;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.pip.phone.PipAppOpsListener;
import com.android.wm.shell.pip.phone.PipMotionHelper;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.android.wm.shell.recents.RecentTasks;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.wm.shell.transition.ShellTransitions;
import com.android.wm.shell.transition.Transitions;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.wm.shell.unfold.UnfoldBackgroundController;
import com.google.android.systemui.GoogleServices;
import com.google.android.systemui.NotificationLockscreenUserManagerGoogle;
import com.google.android.systemui.NotificationLockscreenUserManagerGoogle_Factory;
import com.google.android.systemui.assist.AssistManagerGoogle;
import com.google.android.systemui.assist.AssistManagerGoogle_Factory;
import com.google.android.systemui.assist.GoogleAssistLogger;
import com.google.android.systemui.assist.GoogleAssistLogger_Factory;
import com.google.android.systemui.assist.OpaEnabledDispatcher;
import com.google.android.systemui.assist.OpaEnabledReceiver;
import com.google.android.systemui.assist.OpaEnabledReceiver_Factory;
import com.google.android.systemui.assist.OpaEnabledSettings;
import com.google.android.systemui.assist.uihints.AssistantPresenceHandler;
import com.google.android.systemui.assist.uihints.AssistantUIHintsModule_BindEdgeLightsInfoListenersFactory;
import com.google.android.systemui.assist.uihints.AssistantUIHintsModule_ProvideActivityStarterFactory;
import com.google.android.systemui.assist.uihints.AssistantUIHintsModule_ProvideAudioInfoListenersFactory;
import com.google.android.systemui.assist.uihints.AssistantUIHintsModule_ProvideCardInfoListenersFactory;
import com.google.android.systemui.assist.uihints.AssistantUIHintsModule_ProvideConfigInfoListenersFactory;
import com.google.android.systemui.assist.uihints.AssistantWarmer;
import com.google.android.systemui.assist.uihints.AssistantWarmer_Factory;
import com.google.android.systemui.assist.uihints.ColorChangeHandler;
import com.google.android.systemui.assist.uihints.ColorChangeHandler_Factory;
import com.google.android.systemui.assist.uihints.ConfigurationHandler;
import com.google.android.systemui.assist.uihints.FlingVelocityWrapper_Factory;
import com.google.android.systemui.assist.uihints.GlowController;
import com.google.android.systemui.assist.uihints.GoBackHandler_Factory;
import com.google.android.systemui.assist.uihints.GoogleDefaultUiController;
import com.google.android.systemui.assist.uihints.IconController;
import com.google.android.systemui.assist.uihints.IconController_Factory;
import com.google.android.systemui.assist.uihints.LightnessProvider_Factory;
import com.google.android.systemui.assist.uihints.NavBarFader;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.NgaMessageHandler_Factory;
import com.google.android.systemui.assist.uihints.NgaUiController;
import com.google.android.systemui.assist.uihints.NgaUiController_Factory;
import com.google.android.systemui.assist.uihints.OverlayUiHost_Factory;
import com.google.android.systemui.assist.uihints.SwipeHandler_Factory;
import com.google.android.systemui.assist.uihints.TaskStackNotifier_Factory;
import com.google.android.systemui.assist.uihints.TimeoutManager_Factory;
import com.google.android.systemui.assist.uihints.TouchInsideHandler;
import com.google.android.systemui.assist.uihints.TouchInsideHandler_Factory;
import com.google.android.systemui.assist.uihints.TouchOutsideHandler_Factory;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController;
import com.google.android.systemui.assist.uihints.input.InputModule_ProvideTouchActionRegionsFactory;
import com.google.android.systemui.assist.uihints.input.NgaInputHandler;
import com.google.android.systemui.assist.uihints.input.NgaInputHandler_Factory;
import com.google.android.systemui.assist.uihints.input.TouchActionRegion;
import com.google.android.systemui.assist.uihints.input.TouchInsideRegion;
import com.google.android.systemui.autorotate.AutorotateDataService;
import com.google.android.systemui.columbus.ColumbusContentObserver;
import com.google.android.systemui.columbus.ColumbusContentObserver_Factory_Factory;
import com.google.android.systemui.columbus.ColumbusService;
import com.google.android.systemui.columbus.ColumbusServiceWrapper;
import com.google.android.systemui.columbus.ColumbusServiceWrapper_Factory;
import com.google.android.systemui.columbus.ColumbusSettings;
import com.google.android.systemui.columbus.ColumbusSettings_Factory;
import com.google.android.systemui.columbus.ColumbusStructuredDataManager;
import com.google.android.systemui.columbus.ColumbusStructuredDataManager_Factory;
import com.google.android.systemui.columbus.ColumbusTargetRequestService;
import com.google.android.systemui.columbus.ContentResolverWrapper;
import com.google.android.systemui.columbus.PowerManagerWrapper;
import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.actions.DismissTimer;
import com.google.android.systemui.columbus.actions.DismissTimer_Factory;
import com.google.android.systemui.columbus.actions.LaunchApp;
import com.google.android.systemui.columbus.actions.LaunchOverview;
import com.google.android.systemui.columbus.actions.LaunchOverview_Factory;
import com.google.android.systemui.columbus.actions.ManageMedia;
import com.google.android.systemui.columbus.actions.OpenNotificationShade;
import com.google.android.systemui.columbus.actions.SnoozeAlarm;
import com.google.android.systemui.columbus.actions.TakeScreenshot;
import com.google.android.systemui.columbus.actions.ToggleFlashlight;
import com.google.android.systemui.columbus.actions.UserAction;
import com.google.android.systemui.columbus.actions.UserSelectedAction;
import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import com.google.android.systemui.columbus.feedback.HapticClick;
import com.google.android.systemui.columbus.feedback.UserActivity;
import com.google.android.systemui.columbus.gates.CameraVisibility;
import com.google.android.systemui.columbus.gates.ChargingState;
import com.google.android.systemui.columbus.gates.ChargingState_Factory;
import com.google.android.systemui.columbus.gates.FlagEnabled;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.gates.KeyguardProximity;
import com.google.android.systemui.columbus.gates.KeyguardVisibility;
import com.google.android.systemui.columbus.gates.KeyguardVisibility_Factory;
import com.google.android.systemui.columbus.gates.PowerSaveState;
import com.google.android.systemui.columbus.gates.PowerSaveState_Factory;
import com.google.android.systemui.columbus.gates.PowerState;
import com.google.android.systemui.columbus.gates.Proximity;
import com.google.android.systemui.columbus.gates.ScreenTouch;
import com.google.android.systemui.columbus.gates.SetupWizard;
import com.google.android.systemui.columbus.gates.SetupWizard_Factory;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import com.google.android.systemui.columbus.gates.SystemKeyPress;
import com.google.android.systemui.columbus.gates.SystemKeyPress_Factory;
import com.google.android.systemui.columbus.gates.UsbState;
import com.google.android.systemui.columbus.gates.VrMode;
import com.google.android.systemui.columbus.gates.VrMode_Factory;
import com.google.android.systemui.columbus.sensors.CHREGestureSensor;
import com.google.android.systemui.columbus.sensors.GestureController;
import com.google.android.systemui.columbus.sensors.GestureController_Factory;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import com.google.android.systemui.columbus.sensors.GestureSensorImpl;
import com.google.android.systemui.columbus.sensors.config.Adjustment;
import com.google.android.systemui.columbus.sensors.config.GestureConfiguration;
import com.google.android.systemui.columbus.sensors.config.LowSensitivitySettingAdjustment;
import com.google.android.systemui.columbus.sensors.config.SensorConfiguration;
import com.google.android.systemui.communal.dock.DockEventSimulator;
import com.google.android.systemui.communal.dock.DockMonitor;
import com.google.android.systemui.communal.dock.callbacks.NudgeToSetupDreamCallback;
import com.google.android.systemui.communal.dock.callbacks.TimeoutToUserZeroCallback;
import com.google.android.systemui.communal.dock.callbacks.mediashell.MediaShellCallback;
import com.google.android.systemui.communal.dock.callbacks.mediashell.MediaShellComplication;
import com.google.android.systemui.communal.dock.callbacks.mediashell.dagger.MediaShellComponent$Factory;
import com.google.android.systemui.communal.dock.conditions.TimeoutToUserZeroFeatureCondition;
import com.google.android.systemui.communal.dock.conditions.TimeoutToUserZeroSettingCondition;
import com.google.android.systemui.communal.dock.dagger.ServiceBinderCallbackComponent$Factory;
import com.google.android.systemui.communal.dreams.SetupDreamComplication;
import com.google.android.systemui.communal.dreams.dagger.SetupDreamComponent$Factory;
import com.google.android.systemui.controls.GoogleControlsTileResourceConfigurationImpl;
import com.google.android.systemui.dagger.SysUIGoogleSysUIComponent;
import com.google.android.systemui.dagger.SystemUIGoogleModule_ProvideHeadsUpManagerPhoneFactory;
import com.google.android.systemui.dagger.SystemUIGoogleModule_ProvideIThermalServiceFactory;
import com.google.android.systemui.dagger.SystemUIGoogleModule_ProvideLeakReportEmailFactory;
import com.google.android.systemui.elmyra.ServiceConfigurationGoogle;
import com.google.android.systemui.elmyra.ServiceConfigurationGoogle_Factory;
import com.google.android.systemui.elmyra.actions.CameraAction;
import com.google.android.systemui.elmyra.actions.CameraAction_Builder_Factory;
import com.google.android.systemui.elmyra.actions.LaunchOpa;
import com.google.android.systemui.elmyra.actions.LaunchOpa_Builder_Factory;
import com.google.android.systemui.elmyra.actions.SettingsAction;
import com.google.android.systemui.elmyra.actions.SettingsAction_Builder_Factory;
import com.google.android.systemui.elmyra.actions.SetupWizardAction;
import com.google.android.systemui.elmyra.actions.SetupWizardAction_Builder_Factory;
import com.google.android.systemui.elmyra.actions.SilenceCall;
import com.google.android.systemui.elmyra.actions.UnpinNotifications;
import com.google.android.systemui.elmyra.actions.UnpinNotifications_Factory;
import com.google.android.systemui.elmyra.feedback.AssistInvocationEffect;
import com.google.android.systemui.elmyra.feedback.OpaHomeButton;
import com.google.android.systemui.elmyra.feedback.OpaHomeButton_Factory;
import com.google.android.systemui.elmyra.feedback.OpaLockscreen;
import com.google.android.systemui.elmyra.feedback.OpaLockscreen_Factory;
import com.google.android.systemui.elmyra.feedback.SquishyNavigationButtons;
import com.google.android.systemui.elmyra.gates.TelephonyActivity;
import com.google.android.systemui.fingerprint.UdfpsHbmController;
import com.google.android.systemui.fingerprint.UdfpsHbmController_Factory;
import com.google.android.systemui.gamedashboard.EntryPointController;
import com.google.android.systemui.gamedashboard.EntryPointController_Factory;
import com.google.android.systemui.gamedashboard.FpsController;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger_Factory;
import com.google.android.systemui.gamedashboard.GameMenuActivity;
import com.google.android.systemui.gamedashboard.GameMenuActivity_Factory;
import com.google.android.systemui.gamedashboard.GameModeDndController;
import com.google.android.systemui.gamedashboard.ScreenRecordController;
import com.google.android.systemui.gamedashboard.ScreenRecordController_Factory;
import com.google.android.systemui.gamedashboard.ShortcutBarController;
import com.google.android.systemui.gamedashboard.ShortcutBarController_Factory;
import com.google.android.systemui.gamedashboard.ToastController;
import com.google.android.systemui.gamedashboard.ToastController_Factory;
import com.google.android.systemui.keyguard.KeyguardSliceProviderGoogle;
import com.google.android.systemui.lowlightclock.AmbientLightModeMonitor;
import com.google.android.systemui.lowlightclock.AmbientLightModeMonitor_Factory;
import com.google.android.systemui.lowlightclock.LightSensorEventsDebounceAlgorithm;
import com.google.android.systemui.lowlightclock.LowLightClockControllerImpl;
import com.google.android.systemui.power.EnhancedEstimatesGoogleImpl;
import com.google.android.systemui.power.EnhancedEstimatesGoogleImpl_Factory;
import com.google.android.systemui.power.PowerModuleGoogle_ProvideWarningsUiFactory;
import com.google.android.systemui.qs.dagger.QSModuleGoogle_ProvideAutoTileManagerFactory;
import com.google.android.systemui.qs.tileimpl.QSFactoryImplGoogle;
import com.google.android.systemui.qs.tileimpl.QSFactoryImplGoogle_Factory;
import com.google.android.systemui.qs.tiles.BatterySaverTileGoogle;
import com.google.android.systemui.qs.tiles.OverlayToggleTile;
import com.google.android.systemui.qs.tiles.OverlayToggleTile_Factory;
import com.google.android.systemui.qs.tiles.ReverseChargingTile;
import com.google.android.systemui.qs.tiles.RotationLockTileGoogle;
import com.google.android.systemui.reversecharging.ReverseChargingController;
import com.google.android.systemui.reversecharging.ReverseChargingViewController;
import com.google.android.systemui.reversecharging.ReverseChargingViewController_Factory;
import com.google.android.systemui.reversecharging.ReverseWirelessCharger;
import com.google.android.systemui.smartspace.KeyguardMediaViewController;
import com.google.android.systemui.smartspace.KeyguardSmartspaceController;
import com.google.android.systemui.smartspace.KeyguardSmartspaceController_Factory;
import com.google.android.systemui.smartspace.KeyguardZenAlarmViewController;
import com.google.android.systemui.smartspace.SmartSpaceController;
import com.google.android.systemui.smartspace.SmartSpaceController_Factory;
import com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle;
import com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle_Factory;
import com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService;
import com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient;
import com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient_Factory;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyClient;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController_Factory;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger;
import com.google.android.systemui.statusbar.phone.StatusBarGoogle;
import com.google.android.systemui.statusbar.phone.StatusBarGoogle_Factory;
import com.google.android.systemui.statusbar.phone.WallpaperNotifier;
import dagger.internal.DelegateFactory;
import dagger.internal.DoubleCheck;
import dagger.internal.InstanceFactory;
import dagger.internal.MapProviderFactory;
import dagger.internal.SetFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DaggerTitanGlobalRootComponent implements GlobalRootComponent {
    public static final Provider ABSENT_JDK_OPTIONAL_PROVIDER = InstanceFactory.create(Optional.empty());
    public Provider<ATraceLoggerTransitionProgressListener> aTraceLoggerTransitionProgressListenerProvider;
    public final Context context;
    public Provider<Context> contextProvider;
    public Provider<DeviceFoldStateProvider> deviceFoldStateProvider;
    public Provider<DumpManager> dumpManagerProvider;
    public Provider<ScaleAwareTransitionProgressProvider.Factory> factoryProvider;
    public Provider<HingeAngleProvider> hingeAngleProvider;
    public Provider<LifecycleScreenStatusProvider> lifecycleScreenStatusProvider;
    public Provider<OpaEnabledSettings> opaEnabledSettingsProvider;
    public Provider<PluginDependencyProvider> pluginDependencyProvider;
    public Provider<PluginEnablerImpl> pluginEnablerImplProvider;
    public Provider<AccessibilityManager> provideAccessibilityManagerProvider;
    public Provider<ActivityManager> provideActivityManagerProvider;
    public Provider<ActivityTaskManager> provideActivityTaskManagerProvider;
    public Provider<AlarmManager> provideAlarmManagerProvider;
    public Provider<AudioManager> provideAudioManagerProvider;
    public Provider<ClipboardManager> provideClipboardManagerProvider;
    public Provider<ColorDisplayManager> provideColorDisplayManagerProvider;
    public Provider<ConnectivityManager> provideConnectivityManagagerProvider;
    public Provider<ContentResolver> provideContentResolverProvider;
    public Provider<CrossWindowBlurListeners> provideCrossWindowBlurListenersProvider;
    public Provider<DevicePolicyManager> provideDevicePolicyManagerProvider;
    public Provider<DeviceStateManager> provideDeviceStateManagerProvider;
    public Provider<Integer> provideDisplayIdProvider;
    public Provider<DisplayManager> provideDisplayManagerProvider;
    public Provider<DisplayMetrics> provideDisplayMetricsProvider;
    public Provider<Execution> provideExecutionProvider;
    public Provider<FaceManager> provideFaceManagerProvider;
    public Provider<FoldStateProvider> provideFoldStateProvider;
    public Provider<IActivityManager> provideIActivityManagerProvider;
    public Provider<IAudioService> provideIAudioServiceProvider;
    public Provider<IBatteryStats> provideIBatteryStatsProvider;
    public Provider<IDreamManager> provideIDreamManagerProvider;
    public Provider<IPackageManager> provideIPackageManagerProvider;
    public Provider<IStatusBarService> provideIStatusBarServiceProvider;
    public Provider<IWindowManager> provideIWindowManagerProvider;
    public Provider<InputMethodManager> provideInputMethodManagerProvider;
    public Provider<InteractionJankMonitor> provideInteractionJankMonitorProvider;
    public Provider<KeyguardManager> provideKeyguardManagerProvider;
    public Provider<LatencyTracker> provideLatencyTrackerProvider;
    public Provider<LauncherApps> provideLauncherAppsProvider;
    public Provider<DelayableExecutor> provideMainDelayableExecutorProvider;
    public Provider<Executor> provideMainExecutorProvider;
    public Provider<Handler> provideMainHandlerProvider;
    public Provider<MediaRouter2Manager> provideMediaRouter2ManagerProvider;
    public Provider<MediaSessionManager> provideMediaSessionManagerProvider;
    public Provider<Optional<NaturalRotationUnfoldProgressProvider>> provideNaturalRotationProgressProvider;
    public Provider<NetworkScoreManager> provideNetworkScoreManagerProvider;
    public Provider<NotificationManager> provideNotificationManagerProvider;
    public Provider<Optional<TelecomManager>> provideOptionalTelecomManagerProvider;
    public Provider<Optional<Vibrator>> provideOptionalVibratorProvider;
    public Provider<OverlayManager> provideOverlayManagerProvider;
    public Provider<PackageManager> providePackageManagerProvider;
    public Provider<PackageManagerWrapper> providePackageManagerWrapperProvider;
    public Provider<PermissionManager> providePermissionManagerProvider;
    public Provider<PluginActionManager.Factory> providePluginInstanceManagerFactoryProvider;
    public Provider<PowerManager> providePowerManagerProvider;
    public Provider<Resources> provideResourcesProvider;
    public Provider<SensorPrivacyManager> provideSensorPrivacyManagerProvider;
    public Provider<ShellUnfoldProgressProvider> provideShellProgressProvider;
    public Provider<ShortcutManager> provideShortcutManagerProvider;
    public Provider<SmartspaceManager> provideSmartspaceManagerProvider;
    public Provider<StatsManager> provideStatsManagerProvider;
    public Provider<Optional<ScopedUnfoldTransitionProgressProvider>> provideStatusBarScopedTransitionProvider;
    public Provider<SubscriptionManager> provideSubcriptionManagerProvider;
    public Provider<TelecomManager> provideTelecomManagerProvider;
    public Provider<TelephonyManager> provideTelephonyManagerProvider;
    public Provider<TrustManager> provideTrustManagerProvider;
    public Provider<UiEventLogger> provideUiEventLoggerProvider;
    public Provider<UnfoldTransitionConfig> provideUnfoldTransitionConfigProvider;
    public Provider<UserManager> provideUserManagerProvider;
    public Provider<Vibrator> provideVibratorProvider;
    public Provider<ViewConfiguration> provideViewConfigurationProvider;
    public Provider<WallpaperManager> provideWallpaperManagerProvider;
    public Provider<WifiManager> provideWifiManagerProvider;
    public Provider<WindowManager> provideWindowManagerProvider;
    public Provider<FingerprintManager> providesFingerprintManagerProvider;
    public Provider<Optional<FoldStateLogger>> providesFoldStateLoggerProvider;
    public Provider<Optional<FoldStateLoggingProvider>> providesFoldStateLoggingProvider;
    public Provider<Executor> providesPluginExecutorProvider;
    public Provider<PluginInstance.Factory> providesPluginInstanceFactoryProvider;
    public Provider<PluginManager> providesPluginManagerProvider;
    public Provider<PluginPrefs> providesPluginPrefsProvider;
    public Provider<List<String>> providesPrivilegedPluginsProvider;
    public Provider<SensorManager> providesSensorManagerProvider;
    public Provider<QSExpansionPathInterpolator> qSExpansionPathInterpolatorProvider;
    public C0015ScaleAwareTransitionProgressProvider_Factory scaleAwareTransitionProgressProvider;
    public Provider<ScreenLifecycle> screenLifecycleProvider;
    public Provider<ScreenStatusProvider> screenStatusProvider;
    public Provider<TaskbarDelegate> taskbarDelegateProvider;
    public Provider<String> tracingTagPrefixProvider;
    public Provider<Optional<UnfoldTransitionProgressProvider>> unfoldTransitionProgressProvider;

    /* loaded from: classes.dex */
    public static final class Builder implements TitanGlobalRootComponent$Builder {
        public Context context;

        @Override // com.android.systemui.dagger.GlobalRootComponent.Builder
        public final GlobalRootComponent build() {
            R$color.checkBuilderRequirement(this.context, Context.class);
            return new DaggerTitanGlobalRootComponent(new GlobalModule(), new UnfoldTransitionModule(), new UnfoldSharedModule(), this.context);
        }

        @Override // com.android.systemui.dagger.GlobalRootComponent.Builder
        /* renamed from: context */
        public final GlobalRootComponent.Builder mo119context(Context context) {
            Objects.requireNonNull(context);
            this.context = context;
            return this;
        }
    }

    /* loaded from: classes.dex */
    public static final class PresentJdkOptionalInstanceProvider<T> implements Provider<Optional<T>> {
        public final Provider<T> delegate;

        @Override // javax.inject.Provider
        /* renamed from: get */
        public final Object mo144get() {
            return Optional.of(this.delegate.mo144get());
        }

        public PresentJdkOptionalInstanceProvider(Provider<T> provider) {
            Objects.requireNonNull(provider);
            this.delegate = provider;
        }
    }

    /* loaded from: classes.dex */
    public final class TitanSysUIComponentBuilder implements TitanSysUIComponent$Builder {
        public Optional<Object> setAppPairs;
        public Optional<BackAnimation> setBackAnimation;
        public Optional<Bubbles> setBubbles;
        public Optional<CompatUI> setCompatUI;
        public Optional<DisplayAreaHelper> setDisplayAreaHelper;
        public Optional<DragAndDrop> setDragAndDrop;
        public Optional<HideDisplayCutout> setHideDisplayCutout;
        public Optional<LegacySplitScreen> setLegacySplitScreen;
        public Optional<OneHanded> setOneHanded;
        public Optional<Pip> setPip;
        public Optional<RecentTasks> setRecentTasks;
        public Optional<ShellCommandHandler> setShellCommandHandler;
        public Optional<SplitScreen> setSplitScreen;
        public Optional<StartingSurface> setStartingSurface;
        public Optional<TaskSurfaceHelper> setTaskSurfaceHelper;
        public Optional<TaskViewFactory> setTaskViewFactory;
        public ShellTransitions setTransitions;

        public TitanSysUIComponentBuilder() {
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        public final SysUIComponent build() {
            R$color.checkBuilderRequirement(this.setPip, Optional.class);
            R$color.checkBuilderRequirement(this.setLegacySplitScreen, Optional.class);
            R$color.checkBuilderRequirement(this.setSplitScreen, Optional.class);
            R$color.checkBuilderRequirement(this.setAppPairs, Optional.class);
            R$color.checkBuilderRequirement(this.setOneHanded, Optional.class);
            R$color.checkBuilderRequirement(this.setBubbles, Optional.class);
            R$color.checkBuilderRequirement(this.setTaskViewFactory, Optional.class);
            R$color.checkBuilderRequirement(this.setHideDisplayCutout, Optional.class);
            R$color.checkBuilderRequirement(this.setShellCommandHandler, Optional.class);
            R$color.checkBuilderRequirement(this.setTransitions, ShellTransitions.class);
            R$color.checkBuilderRequirement(this.setStartingSurface, Optional.class);
            R$color.checkBuilderRequirement(this.setDisplayAreaHelper, Optional.class);
            R$color.checkBuilderRequirement(this.setTaskSurfaceHelper, Optional.class);
            R$color.checkBuilderRequirement(this.setRecentTasks, Optional.class);
            R$color.checkBuilderRequirement(this.setCompatUI, Optional.class);
            R$color.checkBuilderRequirement(this.setDragAndDrop, Optional.class);
            R$color.checkBuilderRequirement(this.setBackAnimation, Optional.class);
            return new TitanSysUIComponentImpl(new DependencyProvider(), new NightDisplayListenerModule(), new SysUIUnfoldModule(), this.setPip, this.setLegacySplitScreen, this.setSplitScreen, this.setAppPairs, this.setOneHanded, this.setBubbles, this.setTaskViewFactory, this.setHideDisplayCutout, this.setShellCommandHandler, this.setTransitions, this.setStartingSurface, this.setDisplayAreaHelper, this.setTaskSurfaceHelper, this.setRecentTasks, this.setCompatUI, this.setDragAndDrop, this.setBackAnimation);
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setAppPairs */
        public final SysUIComponent.Builder mo120setAppPairs(Optional optional) {
            Objects.requireNonNull(optional);
            this.setAppPairs = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setBackAnimation */
        public final SysUIComponent.Builder mo121setBackAnimation(Optional optional) {
            Objects.requireNonNull(optional);
            this.setBackAnimation = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setBubbles */
        public final SysUIComponent.Builder mo122setBubbles(Optional optional) {
            Objects.requireNonNull(optional);
            this.setBubbles = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setCompatUI */
        public final SysUIComponent.Builder mo123setCompatUI(Optional optional) {
            Objects.requireNonNull(optional);
            this.setCompatUI = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setDisplayAreaHelper */
        public final SysUIComponent.Builder mo124setDisplayAreaHelper(Optional optional) {
            Objects.requireNonNull(optional);
            this.setDisplayAreaHelper = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setDragAndDrop */
        public final SysUIComponent.Builder mo125setDragAndDrop(Optional optional) {
            Objects.requireNonNull(optional);
            this.setDragAndDrop = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setHideDisplayCutout */
        public final SysUIComponent.Builder mo126setHideDisplayCutout(Optional optional) {
            Objects.requireNonNull(optional);
            this.setHideDisplayCutout = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setLegacySplitScreen */
        public final SysUIComponent.Builder mo127setLegacySplitScreen(Optional optional) {
            Objects.requireNonNull(optional);
            this.setLegacySplitScreen = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setOneHanded */
        public final SysUIComponent.Builder mo128setOneHanded(Optional optional) {
            Objects.requireNonNull(optional);
            this.setOneHanded = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setPip */
        public final SysUIComponent.Builder mo129setPip(Optional optional) {
            Objects.requireNonNull(optional);
            this.setPip = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setRecentTasks */
        public final SysUIComponent.Builder mo130setRecentTasks(Optional optional) {
            Objects.requireNonNull(optional);
            this.setRecentTasks = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setShellCommandHandler */
        public final SysUIComponent.Builder mo131setShellCommandHandler(Optional optional) {
            Objects.requireNonNull(optional);
            this.setShellCommandHandler = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setSplitScreen */
        public final SysUIComponent.Builder mo132setSplitScreen(Optional optional) {
            Objects.requireNonNull(optional);
            this.setSplitScreen = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setStartingSurface */
        public final SysUIComponent.Builder mo133setStartingSurface(Optional optional) {
            Objects.requireNonNull(optional);
            this.setStartingSurface = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setTaskSurfaceHelper */
        public final SysUIComponent.Builder mo134setTaskSurfaceHelper(Optional optional) {
            Objects.requireNonNull(optional);
            this.setTaskSurfaceHelper = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setTaskViewFactory */
        public final SysUIComponent.Builder mo135setTaskViewFactory(Optional optional) {
            Objects.requireNonNull(optional);
            this.setTaskViewFactory = optional;
            return this;
        }

        @Override // com.android.systemui.dagger.SysUIComponent.Builder
        /* renamed from: setTransitions */
        public final SysUIComponent.Builder mo136setTransitions(ShellTransitions shellTransitions) {
            Objects.requireNonNull(shellTransitions);
            this.setTransitions = shellTransitions;
            return this;
        }
    }

    /* loaded from: classes.dex */
    public final class TitanSysUIComponentImpl implements SysUIGoogleSysUIComponent {
        public Provider<AccessibilityButtonModeObserver> accessibilityButtonModeObserverProvider;
        public Provider<AccessibilityButtonTargetsObserver> accessibilityButtonTargetsObserverProvider;
        public Provider<AccessibilityController> accessibilityControllerProvider;
        public Provider<AccessibilityManagerWrapper> accessibilityManagerWrapperProvider;
        public Provider<ActionClickLogger> actionClickLoggerProvider;
        public Provider<ActionProxyReceiver> actionProxyReceiverProvider;
        public Provider<ActivityIntentHelper> activityIntentHelperProvider;
        public Provider<ActivityStarterDelegate> activityStarterDelegateProvider;
        public Provider<UserDetailView.Adapter> adapterProvider;
        public Provider<AirplaneModeTile> airplaneModeTileProvider;
        public Provider<AlarmTile> alarmTileProvider;
        public Provider<AmbientLightModeMonitor> ambientLightModeMonitorProvider;
        public Provider<AmbientState> ambientStateProvider;
        public Provider<AnimatedImageNotificationManager> animatedImageNotificationManagerProvider;
        public Provider<AppOpsControllerImpl> appOpsControllerImplProvider;
        public Provider<AssistInvocationEffect> assistInvocationEffectProvider;
        public Provider<com.google.android.systemui.columbus.feedback.AssistInvocationEffect> assistInvocationEffectProvider2;
        public Provider<AssistManagerGoogle> assistManagerGoogleProvider;
        public Provider<AssistantFeedbackController> assistantFeedbackControllerProvider;
        public Provider<AssistantPresenceHandler> assistantPresenceHandlerProvider;
        public Provider<AssistantWarmer> assistantWarmerProvider;
        public Provider<AsyncSensorManager> asyncSensorManagerProvider;
        public Provider<AuthController> authControllerProvider;
        public Provider<AutorotateDataService> autorotateDataServiceProvider;
        public Provider<BatterySaverTileGoogle> batterySaverTileGoogleProvider;
        public Provider<BatteryStateNotifier> batteryStateNotifierProvider;
        public Provider<Set<NgaMessageHandler.EdgeLightsInfoListener>> bindEdgeLightsInfoListenersProvider;
        public Provider<BindEventManagerImpl> bindEventManagerImplProvider;
        public Provider<NotifPanelEventSource> bindEventSourceProvider;
        public Provider<RotationPolicyWrapper> bindRotationPolicyWrapperProvider;
        public Provider<SystemClock> bindSystemClockProvider;
        public Provider<BiometricUnlockController> biometricUnlockControllerProvider;
        public Provider<BluetoothControllerImpl> bluetoothControllerImplProvider;
        public Provider<BluetoothTile> bluetoothTileProvider;
        public Provider<BlurUtils> blurUtilsProvider;
        public Provider<BootCompleteCacheImpl> bootCompleteCacheImplProvider;
        public Provider<BrightLineFalsingManager> brightLineFalsingManagerProvider;
        public Provider<BrightnessDialog> brightnessDialogProvider;
        public Provider<BroadcastDispatcherLogger> broadcastDispatcherLoggerProvider;
        public Provider<ThresholdSensorImpl.BuilderFactory> builderFactoryProvider;
        public Provider<DelayedWakeLock.Builder> builderProvider;
        public Provider<CustomTile.Builder> builderProvider10;
        public Provider<NightDisplayListenerModule.Builder> builderProvider11;
        public Provider<AutoAddTracker.Builder> builderProvider12;
        public Provider<TileServiceRequestController.Builder> builderProvider13;
        public Provider<FlingAnimationUtils.Builder> builderProvider14;
        public Provider<ThresholdSensorImpl.Builder> builderProvider2;
        public Provider<WakeLock.Builder> builderProvider3;
        public Provider<NotificationClicker.Builder> builderProvider4;
        public Provider<StatusBarNotificationActivityStarter.Builder> builderProvider5;
        public Provider<LaunchOpa.Builder> builderProvider6;
        public Provider<SettingsAction.Builder> builderProvider7;
        public Provider<CameraAction.Builder> builderProvider8;
        public Provider<SetupWizardAction.Builder> builderProvider9;
        public Provider<CHREGestureSensor> cHREGestureSensorProvider;
        public Provider<CallbackHandler> callbackHandlerProvider;
        public Provider<CameraToggleTile> cameraToggleTileProvider;
        public Provider<CameraVisibility> cameraVisibilityProvider;
        public Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
        public Provider<CastControllerImpl> castControllerImplProvider;
        public Provider<CastTile> castTileProvider;
        public Provider<CellularTile> cellularTileProvider;
        public Provider<ChannelEditorDialogController> channelEditorDialogControllerProvider;
        public Provider<ChargingState> chargingStateProvider;
        public Provider<ClipboardListener> clipboardListenerProvider;
        public Provider<ClockManager> clockManagerProvider;
        public Provider<ColorChangeHandler> colorChangeHandlerProvider;
        public Provider<ColorCorrectionTile> colorCorrectionTileProvider;
        public Provider<ColorInversionTile> colorInversionTileProvider;
        public Provider<ColumbusService> columbusServiceProvider;
        public Provider<ColumbusServiceWrapper> columbusServiceWrapperProvider;
        public Provider<ColumbusSettings> columbusSettingsProvider;
        public Provider<ColumbusStructuredDataManager> columbusStructuredDataManagerProvider;
        public Provider<ColumbusTargetRequestService> columbusTargetRequestServiceProvider;
        public Provider<CommandRegistry> commandRegistryProvider;
        public Provider<CommunalSettingCondition> communalSettingConditionProvider;
        public Provider<CommunalSourceMonitor> communalSourceMonitorProvider;
        public Provider<CommunalStateController> communalStateControllerProvider;
        public Provider<CommunalViewComponent.Factory> communalViewComponentFactoryProvider;
        public Provider<ComplicationTypesUpdater> complicationTypesUpdaterProvider;
        public Provider<ConfigurationHandler> configurationHandlerProvider;
        public Provider<ContentResolverWrapper> contentResolverWrapperProvider;
        public Provider<ContextComponentResolver> contextComponentResolverProvider;
        public Provider<ControlActionCoordinatorImpl> controlActionCoordinatorImplProvider;
        public Provider<ControlsActivity> controlsActivityProvider;
        public Provider<ControlsBindingControllerImpl> controlsBindingControllerImplProvider;
        public Provider<ControlsComponent> controlsComponentProvider;
        public Provider<ControlsControllerImpl> controlsControllerImplProvider;
        public Provider<ControlsEditingActivity> controlsEditingActivityProvider;
        public Provider<ControlsFavoritingActivity> controlsFavoritingActivityProvider;
        public Provider<ControlsListingControllerImpl> controlsListingControllerImplProvider;
        public Provider<ControlsMetricsLoggerImpl> controlsMetricsLoggerImplProvider;
        public Provider<ControlsProviderSelectorActivity> controlsProviderSelectorActivityProvider;
        public Provider<ControlsRequestDialog> controlsRequestDialogProvider;
        public Provider<ControlsUiControllerImpl> controlsUiControllerImplProvider;
        public Provider<ConversationNotificationManager> conversationNotificationManagerProvider;
        public Provider<ConversationNotificationProcessor> conversationNotificationProcessorProvider;
        public Provider<CoordinatorsSubcomponent.Factory> coordinatorsSubcomponentFactoryProvider;
        public Provider<CreateUserActivity> createUserActivityProvider;
        public Provider<CustomIconCache> customIconCacheProvider;
        public Provider<CustomTileStatePersister> customTileStatePersisterProvider;
        public Provider<DarkIconDispatcherImpl> darkIconDispatcherImplProvider;
        public Provider dataLoggerProvider;
        public Provider<DataSaverTile> dataSaverTileProvider;
        public Provider<DateFormatUtil> dateFormatUtilProvider;
        public Provider<DebugModeFilterProvider> debugModeFilterProvider;
        public Provider<DebugNotificationVoiceReplyClient> debugNotificationVoiceReplyClientProvider;
        public Provider<DefaultUiController> defaultUiControllerProvider;
        public Provider<DeleteScreenshotReceiver> deleteScreenshotReceiverProvider;
        public Provider<Dependency> dependencyProvider2;
        public Provider<DeviceConfigProxy> deviceConfigProxyProvider;
        public Provider<DeviceControlsControllerImpl> deviceControlsControllerImplProvider;
        public Provider<DeviceControlsTile> deviceControlsTileProvider;
        public Provider<DevicePostureControllerImpl> devicePostureControllerImplProvider;
        public Provider<DeviceProvisionedControllerImpl> deviceProvisionedControllerImplProvider;
        public Provider<DeviceStateRotationLockSettingController> deviceStateRotationLockSettingControllerProvider;
        public Provider diagonalClassifierProvider;
        public Provider<DisableFlagsLogger> disableFlagsLoggerProvider;
        public Provider<DismissCallbackRegistry> dismissCallbackRegistryProvider;
        public Provider<DismissTimer> dismissTimerProvider;
        public Provider distanceClassifierProvider;
        public Provider<DndTile> dndTileProvider;
        public Provider<DockEventSimulator> dockEventSimulatorProvider;
        public Provider<DockMonitor> dockMonitorProvider;
        public Provider<DockEventSimulator.DockingCondition> dockingConditionProvider;
        public Provider<DoubleTapClassifier> doubleTapClassifierProvider;
        public Provider<DozeComponent.Builder> dozeComponentBuilderProvider;
        public Provider<DozeLog> dozeLogProvider;
        public Provider<DozeLogger> dozeLoggerProvider;
        public Provider<DozeParameters> dozeParametersProvider;
        public Provider<DozeScrimController> dozeScrimControllerProvider;
        public Provider<DozeServiceHost> dozeServiceHostProvider;
        public Provider<DozeService> dozeServiceProvider;
        public Provider<DreamClockDateComplicationComponent$Factory> dreamClockDateComplicationComponentFactoryProvider;
        public Provider<DreamClockDateComplication> dreamClockDateComplicationProvider;
        public Provider<DreamClockTimeComplicationComponent$Factory> dreamClockTimeComplicationComponentFactoryProvider;
        public Provider<DreamClockTimeComplication> dreamClockTimeComplicationProvider;
        public Provider<DreamOverlayComponent.Factory> dreamOverlayComponentFactoryProvider;
        public Provider<DreamOverlayRegistrant> dreamOverlayRegistrantProvider;
        public Provider<DreamOverlayService> dreamOverlayServiceProvider;
        public Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
        public Provider<DreamWeatherComplicationComponent$Factory> dreamWeatherComplicationComponentFactoryProvider;
        public Provider<DreamWeatherComplication> dreamWeatherComplicationProvider;
        public Provider<DumpHandler> dumpHandlerProvider;
        public Provider<DynamicChildBindController> dynamicChildBindControllerProvider;
        public Provider<Optional<LowLightClockController>> dynamicOverrideOptionalOfLowLightClockControllerProvider;
        public Provider<DynamicPrivacyController> dynamicPrivacyControllerProvider;
        public Provider<EdgeLightsController> edgeLightsControllerProvider;
        public Provider<EnhancedEstimatesGoogleImpl> enhancedEstimatesGoogleImplProvider;
        public Provider<EntryPointController> entryPointControllerProvider;
        public Provider<ExpandableNotificationRowComponent.Builder> expandableNotificationRowComponentBuilderProvider;
        public Provider<NotificationLogger.ExpansionStateLogger> expansionStateLoggerProvider;
        public Provider<ExtensionControllerImpl> extensionControllerImplProvider;
        public Provider<LightBarController.Factory> factoryProvider;
        public Provider<StatusBarIconController.TintedIconManager.Factory> factoryProvider10;
        public Provider<AutoHideController.Factory> factoryProvider2;
        public Provider<NavigationBar.Factory> factoryProvider3;
        public Provider<KeyguardBouncer.Factory> factoryProvider4;
        public Provider<KeyguardMessageAreaController.Factory> factoryProvider5;
        public Provider<BrightnessSliderController.Factory> factoryProvider6;
        public Provider<ColumbusContentObserver.Factory> factoryProvider7;
        public Provider<EdgeBackGestureHandler.Factory> factoryProvider8;
        public Provider<TileLifecycleManager.Factory> factoryProvider9;
        public Provider falsingCollectorImplProvider;
        public Provider<FalsingDataProvider> falsingDataProvider;
        public Provider<FalsingManagerProxy> falsingManagerProxyProvider;
        public Provider<FeatureFlagsRelease> featureFlagsReleaseProvider;
        public Provider<FgsManagerController> fgsManagerControllerProvider;
        public Provider<Files> filesProvider;
        public Provider<FlagEnabled> flagEnabledProvider;
        public Provider<FlashlightControllerImpl> flashlightControllerImplProvider;
        public Provider<FlashlightTile> flashlightTileProvider;
        public Provider flingVelocityWrapperProvider;
        public Provider<ForegroundServiceController> foregroundServiceControllerProvider;
        public Provider<ForegroundServiceDismissalFeatureController> foregroundServiceDismissalFeatureControllerProvider;
        public Provider<ForegroundServiceNotificationListener> foregroundServiceNotificationListenerProvider;
        public Provider<ForegroundServiceSectionController> foregroundServiceSectionControllerProvider;
        public Provider<ForegroundServicesDialog> foregroundServicesDialogProvider;
        public Provider<FpsController> fpsControllerProvider;
        public Provider<FragmentService.FragmentCreator.Factory> fragmentCreatorFactoryProvider;
        public Provider<FragmentService> fragmentServiceProvider;
        public Provider<GameDashboardUiEventLogger> gameDashboardUiEventLoggerProvider;
        public Provider<GameMenuActivity> gameMenuActivityProvider;
        public Provider<GameModeDndController> gameModeDndControllerProvider;
        public Provider<GarbageMonitor> garbageMonitorProvider;
        public Provider<GestureConfiguration> gestureConfigurationProvider;
        public Provider<GestureController> gestureControllerProvider;
        public Provider<GestureSensorImpl> gestureSensorImplProvider;
        public Provider<GlobalActionsComponent> globalActionsComponentProvider;
        public Provider<GlobalActionsDialogLite> globalActionsDialogLiteProvider;
        public Provider<GlobalActionsImpl> globalActionsImplProvider;
        public Provider globalSettingsImplProvider;
        public Provider<GlowController> glowControllerProvider;
        public Provider goBackHandlerProvider;
        public Provider<GoogleAssistLogger> googleAssistLoggerProvider;
        public Provider<GoogleControlsTileResourceConfigurationImpl> googleControlsTileResourceConfigurationImplProvider;
        public Provider<GoogleDefaultUiController> googleDefaultUiControllerProvider;
        public Provider<GoogleServices> googleServicesProvider;
        public Provider<GroupCoalescerLogger> groupCoalescerLoggerProvider;
        public Provider<GroupCoalescer> groupCoalescerProvider;
        public Provider<HapticClick> hapticClickProvider;
        public Provider<HdmiCecSetMenuLanguageActivity> hdmiCecSetMenuLanguageActivityProvider;
        public Provider<HdmiCecSetMenuLanguageHelper> hdmiCecSetMenuLanguageHelperProvider;
        public Provider<HeadsUpController> headsUpControllerProvider;
        public Provider<HeadsUpManagerLogger> headsUpManagerLoggerProvider;
        public Provider<HeadsUpViewBinderLogger> headsUpViewBinderLoggerProvider;
        public Provider<HeadsUpViewBinder> headsUpViewBinderProvider;
        public Provider<HighPriorityProvider> highPriorityProvider;
        public Provider<HistoryTracker> historyTrackerProvider;
        public Provider<HotspotControllerImpl> hotspotControllerImplProvider;
        public Provider<HotspotTile> hotspotTileProvider;
        public Provider<IconBuilder> iconBuilderProvider;
        public Provider<IconController> iconControllerProvider;
        public Provider<IconManager> iconManagerProvider;
        public Provider imageExporterProvider;
        public Provider imageTileSetProvider;
        public Provider<InitController> initControllerProvider;
        public Provider<InstantAppNotifier> instantAppNotifierProvider;
        public Provider<InternetDialogController> internetDialogControllerProvider;
        public Provider<InternetDialogFactory> internetDialogFactoryProvider;
        public Provider<InternetTile> internetTileProvider;
        public Provider<Boolean> isPMLiteEnabledProvider;
        public Provider<Boolean> isReduceBrightColorsAvailableProvider;
        public Provider keyboardMonitorProvider;
        public Provider<KeyboardUI> keyboardUIProvider;
        public Provider<KeyguardBiometricLockoutLogger> keyguardBiometricLockoutLoggerProvider;
        public Provider<KeyguardBouncerComponent.Factory> keyguardBouncerComponentFactoryProvider;
        public Provider<KeyguardBypassController> keyguardBypassControllerProvider;
        public Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
        public Provider<KeyguardDisplayManager> keyguardDisplayManagerProvider;
        public Provider<KeyguardEnvironmentImpl> keyguardEnvironmentImplProvider;
        public Provider<KeyguardIndicationControllerGoogle> keyguardIndicationControllerGoogleProvider;
        public Provider<KeyguardLifecyclesDispatcher> keyguardLifecyclesDispatcherProvider;
        public Provider<KeyguardMediaController> keyguardMediaControllerProvider;
        public Provider<KeyguardMediaViewController> keyguardMediaViewControllerProvider;
        public Provider<KeyguardProximity> keyguardProximityProvider;
        public Provider<KeyguardQsUserSwitchComponent.Factory> keyguardQsUserSwitchComponentFactoryProvider;
        public Provider<KeyguardSecurityModel> keyguardSecurityModelProvider;
        public Provider<KeyguardService> keyguardServiceProvider;
        public Provider<KeyguardSmartspaceController> keyguardSmartspaceControllerProvider;
        public Provider<KeyguardStateControllerImpl> keyguardStateControllerImplProvider;
        public Provider<KeyguardStatusBarViewComponent.Factory> keyguardStatusBarViewComponentFactoryProvider;
        public Provider<KeyguardStatusViewComponent.Factory> keyguardStatusViewComponentFactoryProvider;
        public Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
        public Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
        public Provider<KeyguardUserSwitcherComponent.Factory> keyguardUserSwitcherComponentFactoryProvider;
        public Provider<KeyguardVisibility> keyguardVisibilityProvider;
        public Provider<KeyguardZenAlarmViewController> keyguardZenAlarmViewControllerProvider;
        public Provider<LSShadeTransitionLogger> lSShadeTransitionLoggerProvider;
        public Provider<LatencyTester> latencyTesterProvider;
        public Provider<LaunchApp> launchAppProvider;
        public Provider<LaunchConversationActivity> launchConversationActivityProvider;
        public Provider<com.google.android.systemui.columbus.actions.LaunchOpa> launchOpaProvider;
        public Provider<LaunchOverview> launchOverviewProvider;
        public Provider<LeakReporter> leakReporterProvider;
        public Provider<LegacyNotificationPresenterExtensions> legacyNotificationPresenterExtensionsProvider;
        public Provider<LightBarController> lightBarControllerProvider;
        public Provider<LightSensorEventsDebounceAlgorithm> lightSensorEventsDebounceAlgorithmProvider;
        public Provider lightnessProvider;
        public Provider<LocalMediaManagerFactory> localMediaManagerFactoryProvider;
        public Provider<LocationControllerImpl> locationControllerImplProvider;
        public Provider<LocationTile> locationTileProvider;
        public Provider<LockscreenGestureLogger> lockscreenGestureLoggerProvider;
        public Provider<LockscreenShadeTransitionController> lockscreenShadeTransitionControllerProvider;
        public Provider<LockscreenSmartspaceController> lockscreenSmartspaceControllerProvider;
        public Provider<LockscreenWallpaper> lockscreenWallpaperProvider;
        public Provider<LogBufferEulogizer> logBufferEulogizerProvider;
        public Provider<LogBufferFactory> logBufferFactoryProvider;
        public Provider<LogBufferFreezer> logBufferFreezerProvider;
        public Provider<LongScreenshotActivity> longScreenshotActivityProvider;
        public Provider<LongScreenshotData> longScreenshotDataProvider;
        public Provider<LowLightClockControllerImpl> lowLightClockControllerImplProvider;
        public Provider<LowPriorityInflationHelper> lowPriorityInflationHelperProvider;
        public Provider<LowSensitivitySettingAdjustment> lowSensitivitySettingAdjustmentProvider;
        public Provider<ManageMedia> manageMediaProvider;
        public Provider<ManagedProfileControllerImpl> managedProfileControllerImplProvider;
        public Provider<Map<Class<?>, Provider<Activity>>> mapOfClassOfAndProviderOfActivityProvider;
        public Provider<Map<Class<?>, Provider<BroadcastReceiver>>> mapOfClassOfAndProviderOfBroadcastReceiverProvider;
        public Provider<Map<Class<?>, Provider<CoreStartable>>> mapOfClassOfAndProviderOfCoreStartableProvider;
        public Provider<Map<Class<?>, Provider<RecentsImplementation>>> mapOfClassOfAndProviderOfRecentsImplementationProvider;
        public Provider<Map<Class<?>, Provider<Service>>> mapOfClassOfAndProviderOfServiceProvider;
        public Provider<MediaArtworkProcessor> mediaArtworkProcessorProvider;
        public Provider<MediaBrowserFactory> mediaBrowserFactoryProvider;
        public Provider<MediaCarouselController> mediaCarouselControllerProvider;
        public Provider<MediaComplicationComponent$Factory> mediaComplicationComponentFactoryProvider;
        public Provider<MediaContainerController> mediaContainerControllerProvider;
        public Provider<MediaControlPanel> mediaControlPanelProvider;
        public Provider<MediaControllerFactory> mediaControllerFactoryProvider;
        public Provider<MediaDataFilter> mediaDataFilterProvider;
        public Provider<MediaDataManager> mediaDataManagerProvider;
        public Provider<MediaDeviceManager> mediaDeviceManagerProvider;
        public Provider<MediaDreamComplication> mediaDreamComplicationProvider;
        public Provider<MediaDreamSentinel> mediaDreamSentinelProvider;
        public Provider<MediaFeatureFlag> mediaFeatureFlagProvider;
        public Provider<MediaFlags> mediaFlagsProvider;
        public Provider<MediaHierarchyManager> mediaHierarchyManagerProvider;
        public Provider<MediaHostStatesManager> mediaHostStatesManagerProvider;
        public Provider<MediaMuteAwaitConnectionCli> mediaMuteAwaitConnectionCliProvider;
        public Provider<MediaMuteAwaitConnectionManagerFactory> mediaMuteAwaitConnectionManagerFactoryProvider;
        public Provider<MediaOutputDialogFactory> mediaOutputDialogFactoryProvider;
        public Provider<MediaOutputDialogReceiver> mediaOutputDialogReceiverProvider;
        public Provider<MediaResumeListener> mediaResumeListenerProvider;
        public Provider<MediaSessionBasedFilter> mediaSessionBasedFilterProvider;
        public Provider<MediaShellCallback> mediaShellCallbackProvider;
        public Provider<MediaShellComplication> mediaShellComplicationProvider;
        public Provider<MediaShellComponent$Factory> mediaShellComponentFactoryProvider;
        public Provider<MediaTimeoutListener> mediaTimeoutListenerProvider;
        public Provider<MediaTttChipControllerReceiver> mediaTttChipControllerReceiverProvider;
        public Provider<MediaTttChipControllerSender> mediaTttChipControllerSenderProvider;
        public Provider<MediaTttCommandLineHelper> mediaTttCommandLineHelperProvider;
        public Provider<MediaTttFlags> mediaTttFlagsProvider;
        public Provider<MediaViewController> mediaViewControllerProvider;
        public Provider<GarbageMonitor.MemoryTile> memoryTileProvider;
        public Provider<MicrophoneToggleTile> microphoneToggleTileProvider;
        public Provider<MonitorComponent.Factory> monitorComponentFactoryProvider;
        public Provider<Set<Action>> namedSetOfActionProvider;
        public Provider<Set<Condition>> namedSetOfConditionProvider;
        public Provider<Set<FalsingClassifier>> namedSetOfFalsingClassifierProvider;
        public Provider<Set<FeedbackEffect>> namedSetOfFeedbackEffectProvider;
        public Provider<Set<FeedbackEffect>> namedSetOfFeedbackEffectProvider2;
        public Provider<Set<Gate>> namedSetOfGateProvider;
        public Provider<Set<Gate>> namedSetOfGateProvider2;
        public Provider<Set<Integer>> namedSetOfIntegerProvider;
        public Provider<NavBarFader> navBarFaderProvider;
        public Provider<NavBarHelper> navBarHelperProvider;
        public Provider<NavigationBarController> navigationBarControllerProvider;
        public Provider<NavigationModeController> navigationModeControllerProvider;
        public Provider<NearbyMediaDevicesManager> nearbyMediaDevicesManagerProvider;
        public Provider<NetworkControllerImpl> networkControllerImplProvider;
        public Provider<KeyguardViewMediator> newKeyguardViewMediatorProvider;
        public Provider<NextAlarmControllerImpl> nextAlarmControllerImplProvider;
        public Provider<NfcTile> nfcTileProvider;
        public Provider<NgaInputHandler> ngaInputHandlerProvider;
        public Provider<NgaMessageHandler> ngaMessageHandlerProvider;
        public Provider<NgaUiController> ngaUiControllerProvider;
        public Provider<NightDisplayTile> nightDisplayTileProvider;
        public Provider<NotifBindPipelineInitializer> notifBindPipelineInitializerProvider;
        public Provider<NotifBindPipelineLogger> notifBindPipelineLoggerProvider;
        public Provider<NotifBindPipeline> notifBindPipelineProvider;
        public Provider<NotifCollectionLogger> notifCollectionLoggerProvider;
        public Provider<NotifCollection> notifCollectionProvider;
        public Provider<NotifCoordinators> notifCoordinatorsProvider;
        public Provider<NotifInflaterImpl> notifInflaterImplProvider;
        public Provider<NotifInflationErrorManager> notifInflationErrorManagerProvider;
        public Provider<NotifLiveDataStoreImpl> notifLiveDataStoreImplProvider;
        public Provider notifPipelineChoreographerImplProvider;
        public Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
        public Provider<NotifPipelineInitializer> notifPipelineInitializerProvider;
        public Provider<NotifPipeline> notifPipelineProvider;
        public Provider<NotifRemoteViewCacheImpl> notifRemoteViewCacheImplProvider;
        public Provider<NotifUiAdjustmentProvider> notifUiAdjustmentProvider;
        public Provider<NotifViewBarn> notifViewBarnProvider;
        public Provider<NotificationChannels> notificationChannelsProvider;
        public Provider<NotificationClickNotifier> notificationClickNotifierProvider;
        public Provider<NotificationClickerLogger> notificationClickerLoggerProvider;
        public Provider<NotificationContentInflater> notificationContentInflaterProvider;
        public Provider<NotificationEntryManagerLogger> notificationEntryManagerLoggerProvider;
        public Provider<NotificationFilter> notificationFilterProvider;
        public Provider<NotificationGroupAlertTransferHelper> notificationGroupAlertTransferHelperProvider;
        public Provider<NotificationGroupManagerLegacy> notificationGroupManagerLegacyProvider;
        public Provider<NotificationIconAreaController> notificationIconAreaControllerProvider;
        public Provider<NotificationInteractionTracker> notificationInteractionTrackerProvider;
        public Provider<NotificationInterruptLogger> notificationInterruptLoggerProvider;
        public Provider<NotificationInterruptStateProviderImpl> notificationInterruptStateProviderImplProvider;
        public Provider<NotificationListener> notificationListenerProvider;
        public Provider<NotificationListenerWithPlugins> notificationListenerWithPluginsProvider;
        public Provider<NotificationLockscreenUserManagerGoogle> notificationLockscreenUserManagerGoogleProvider;
        public Provider<NotificationLockscreenUserManagerImpl> notificationLockscreenUserManagerImplProvider;
        public Provider<NotificationPersonExtractorPluginBoundary> notificationPersonExtractorPluginBoundaryProvider;
        public Provider<NotificationRankingManager> notificationRankingManagerProvider;
        public Provider<NotificationRoundnessManager> notificationRoundnessManagerProvider;
        public Provider<NotificationRowBinderImpl> notificationRowBinderImplProvider;
        public Provider<NotificationSectionsFeatureManager> notificationSectionsFeatureManagerProvider;
        public Provider<NotificationSectionsLogger> notificationSectionsLoggerProvider;
        public Provider<NotificationSectionsManager> notificationSectionsManagerProvider;
        public Provider<NotificationShadeDepthController> notificationShadeDepthControllerProvider;
        public Provider<NotificationShadeWindowControllerImpl> notificationShadeWindowControllerImplProvider;
        public Provider<NotificationShelfComponent.Builder> notificationShelfComponentBuilderProvider;
        public Provider<NotificationVisibilityProviderImpl> notificationVisibilityProviderImplProvider;
        public Provider<NotificationVoiceReplyController> notificationVoiceReplyControllerProvider;
        public Provider<NotificationVoiceReplyLogger> notificationVoiceReplyLoggerProvider;
        public Provider<NotificationVoiceReplyManagerService> notificationVoiceReplyManagerServiceProvider;
        public Provider<NotificationWakeUpCoordinator> notificationWakeUpCoordinatorProvider;
        public Provider<NotificationsControllerImpl> notificationsControllerImplProvider;
        public Provider<NotificationsControllerStub> notificationsControllerStubProvider;
        public Provider<NudgeToSetupDreamCallback> nudgeToSetupDreamCallbackProvider;
        public Provider<OneHandedModeTile> oneHandedModeTileProvider;
        public Provider<OngoingCallFlags> ongoingCallFlagsProvider;
        public Provider<OngoingCallLogger> ongoingCallLoggerProvider;
        public Provider<OpaEnabledDispatcher> opaEnabledDispatcherProvider;
        public Provider<OpaEnabledReceiver> opaEnabledReceiverProvider;
        public Provider<OpaHomeButton> opaHomeButtonProvider;
        public Provider<OpaLockscreen> opaLockscreenProvider;
        public Provider<OpenNotificationShade> openNotificationShadeProvider;
        public Provider<Optional<BcSmartspaceDataPlugin>> optionalOfBcSmartspaceDataPluginProvider;
        public Provider<Optional<CommandQueue>> optionalOfCommandQueueProvider;
        public Provider<Optional<ControlsFavoritePersistenceWrapper>> optionalOfControlsFavoritePersistenceWrapperProvider;
        public Provider<Optional<ControlsTileResourceConfiguration>> optionalOfControlsTileResourceConfigurationProvider;
        public Provider<Optional<HeadsUpManager>> optionalOfHeadsUpManagerProvider;
        public Provider<Optional<Recents>> optionalOfRecentsProvider;
        public Provider<Optional<StatusBar>> optionalOfStatusBarProvider;
        public Provider<Optional<UdfpsHbmProvider>> optionalOfUdfpsHbmProvider;
        public Provider overlappedElementControllerProvider;
        public Provider<OverlayToggleTile> overlayToggleTileProvider;
        public Provider overlayUiHostProvider;
        public Provider<OverviewProxyRecentsImpl> overviewProxyRecentsImplProvider;
        public Provider<OverviewProxyService> overviewProxyServiceProvider;
        public Provider<PackageManagerAdapter> packageManagerAdapterProvider;
        public Provider<PanelExpansionStateManager> panelExpansionStateManagerProvider;
        public Provider<PeopleNotificationIdentifierImpl> peopleNotificationIdentifierImplProvider;
        public Provider<PeopleSpaceActivity> peopleSpaceActivityProvider;
        public Provider<PeopleSpaceWidgetManager> peopleSpaceWidgetManagerProvider;
        public Provider<PeopleSpaceWidgetPinnedReceiver> peopleSpaceWidgetPinnedReceiverProvider;
        public Provider<PeopleSpaceWidgetProvider> peopleSpaceWidgetProvider;
        public Provider<PhoneStateMonitor> phoneStateMonitorProvider;
        public Provider<PhoneStatusBarPolicy> phoneStatusBarPolicyProvider;
        public Provider pointerCountClassifierProvider;
        public Provider postureDependentProximitySensorProvider;
        public Provider<PowerManagerWrapper> powerManagerWrapperProvider;
        public Provider<PowerSaveState> powerSaveStateProvider;
        public Provider<PowerState> powerStateProvider;
        public Provider<PowerUI> powerUIProvider;
        public Provider<PrivacyDialogController> privacyDialogControllerProvider;
        public Provider<PrivacyDotDecorProviderFactory> privacyDotDecorProviderFactoryProvider;
        public Provider<PrivacyDotViewController> privacyDotViewControllerProvider;
        public Provider<PrivacyItemController> privacyItemControllerProvider;
        public Provider<PrivacyLogger> privacyLoggerProvider;
        public Provider<ProtoTracer> protoTracerProvider;
        public Provider<AccessPointControllerImpl> provideAccessPointControllerImplProvider;
        public Provider<AccessibilityFloatingMenuController> provideAccessibilityFloatingMenuControllerProvider;
        public Provider<ActivityLaunchAnimator> provideActivityLaunchAnimatorProvider;
        public Provider<ActivityManagerWrapper> provideActivityManagerWrapperProvider;
        public Provider<ActivityStarter> provideActivityStarterProvider;
        public Provider<NgaMessageHandler.StartActivityInfoListener> provideActivityStarterProvider2;
        public Provider<Boolean> provideAllowNotificationLongPressProvider;
        public Provider<AlwaysOnDisplayPolicy> provideAlwaysOnDisplayPolicyProvider;
        public Provider<AmbientDisplayConfiguration> provideAmbientDisplayConfigurationProvider;
        public Provider<AssistUtils> provideAssistUtilsProvider;
        public Provider<Set<NgaMessageHandler.AudioInfoListener>> provideAudioInfoListenersProvider;
        public Provider<AutoHideController> provideAutoHideControllerProvider;
        public Provider<DeviceStateRotationLockSettingsManager> provideAutoRotateSettingsManagerProvider;
        public Provider<AutoTileManager> provideAutoTileManagerProvider;
        public Provider<DelayableExecutor> provideBackgroundDelayableExecutorProvider;
        public Provider<Executor> provideBackgroundExecutorProvider;
        public Provider<RepeatableExecutor> provideBackgroundRepeatableExecutorProvider;
        public Provider<BatteryController> provideBatteryControllerProvider;
        public Provider<BcSmartspaceDataPlugin> provideBcSmartspaceDataPluginProvider;
        public Provider<Handler> provideBgHandlerProvider;
        public Provider<Looper> provideBgLooperProvider;
        public Provider<LogBuffer> provideBroadcastDispatcherLogBufferProvider;
        public Provider<Optional<BubblesManager>> provideBubblesManagerProvider;
        public Provider<Set<NgaMessageHandler.CardInfoListener>> provideCardInfoListenersProvider;
        public Provider<ClipboardOverlayControllerFactory> provideClipboardOverlayControllerFactoryProvider;
        public Provider provideClockInfoListProvider;
        public Provider<LogBuffer> provideCollapsedSbFragmentLogBufferProvider;
        public Provider<List<Action>> provideColumbusActionsProvider;
        public Provider<Set<FeedbackEffect>> provideColumbusEffectsProvider;
        public Provider<Set<Gate>> provideColumbusGatesProvider;
        public Provider<Set<Gate>> provideColumbusSoftGatesProvider;
        public Provider<CommandQueue> provideCommandQueueProvider;
        public Provider<CommonNotifCollection> provideCommonNotifCollectionProvider;
        public Provider<Set<Condition>> provideCommunalConditionsProvider;
        public Provider<Monitor> provideCommunalSourceMonitorProvider;
        public Provider<Monitor> provideConditionsMonitorProvider;
        public Provider<Set<NgaMessageHandler.ConfigInfoListener>> provideConfigInfoListenersProvider;
        public Provider<ConfigurationController> provideConfigurationControllerProvider;
        public Provider<DataSaverController> provideDataSaverControllerProvider;
        public Provider<DelayableExecutor> provideDelayableExecutorProvider;
        public Provider<DemoModeController> provideDemoModeControllerProvider;
        public Provider<DevicePolicyManagerWrapper> provideDevicePolicyManagerWrapperProvider;
        public Provider<DeviceProvisionedController> provideDeviceProvisionedControllerProvider;
        public Provider<DialogLaunchAnimator> provideDialogLaunchAnimatorProvider;
        public Provider<DockManager> provideDockManagerProvider;
        public Provider<Set<Monitor.Callback>> provideDockingCallbacksProvider;
        public Provider<Set<Condition>> provideDockingConditionsProvider;
        public Provider<LogBuffer> provideDozeLogBufferProvider;
        public Provider<Executor> provideExecutorProvider;
        public Provider<List<Action>> provideFullscreenActionsProvider;
        public Provider<List<Adjustment>> provideGestureAdjustmentsProvider;
        public Provider<GestureSensor> provideGestureSensorProvider;
        public Provider<GroupExpansionManager> provideGroupExpansionManagerProvider;
        public Provider<GroupMembershipManager> provideGroupMembershipManagerProvider;
        public Provider<Handler> provideHandlerProvider;
        public Provider<HeadsUpManagerPhone> provideHeadsUpManagerPhoneProvider;
        public Provider<INotificationManager> provideINotificationManagerProvider;
        public Provider<IThermalService> provideIThermalServiceProvider;
        public Provider<IndividualSensorPrivacyController> provideIndividualSensorPrivacyControllerProvider;
        public Provider<LogBuffer> provideLSShadeTransitionControllerBufferProvider;
        public Provider<LeakDetector> provideLeakDetectorProvider;
        public Provider<String> provideLeakReportEmailProvider;
        public Provider<LocalBluetoothManager> provideLocalBluetoothControllerProvider;
        public Provider<LockPatternUtils> provideLockPatternUtilsProvider;
        public Provider<LogcatEchoTracker> provideLogcatEchoTrackerProvider;
        public Provider<Executor> provideLongRunningExecutorProvider;
        public Provider<Looper> provideLongRunningLooperProvider;
        public Provider<Optional<LowLightClockController>> provideLowLightClockControllerProvider;
        public Provider<MetricsLogger> provideMetricsLoggerProvider;
        public Provider<NightDisplayListener> provideNightDisplayListenerProvider;
        public Provider<NotifGutsViewManager> provideNotifGutsViewManagerProvider;
        public Provider<LogBuffer> provideNotifInteractionLogBufferProvider;
        public Provider<NotifRemoteViewCache> provideNotifRemoteViewCacheProvider;
        public Provider<NotifShadeEventSource> provideNotifShadeEventSourceProvider;
        public Provider<LogBuffer> provideNotifVoiceReplyLogBufferProvider;
        public Provider<NotificationEntryManager> provideNotificationEntryManagerProvider;
        public Provider<NotificationGutsManager> provideNotificationGutsManagerProvider;
        public Provider<LogBuffer> provideNotificationHeadsUpLogBufferProvider;
        public Provider<NotificationLogger> provideNotificationLoggerProvider;
        public Provider<NotificationMediaManager> provideNotificationMediaManagerProvider;
        public Provider<NotificationMessagingUtil> provideNotificationMessagingUtilProvider;
        public Provider<NotificationPanelLogger> provideNotificationPanelLoggerProvider;
        public Provider<NotificationRemoteInputManager> provideNotificationRemoteInputManagerProvider;
        public Provider<LogBuffer> provideNotificationSectionLogBufferProvider;
        public Provider<NotificationViewHierarchyManager> provideNotificationViewHierarchyManagerProvider;
        public Provider<NotificationVisibilityProvider> provideNotificationVisibilityProvider;
        public Provider<Optional<NotificationVoiceReplyClient>> provideNotificationVoiceReplyClientProvider;
        public Provider<NotificationsController> provideNotificationsControllerProvider;
        public Provider<LogBuffer> provideNotificationsLogBufferProvider;
        public Provider<OnUserInteractionCallback> provideOnUserInteractionCallbackProvider;
        public Provider<OngoingCallController> provideOngoingCallControllerProvider;
        public Provider<ViewGroup> provideParentViewGroupProvider;
        public Provider<ThresholdSensor[]> providePostureToProximitySensorMappingProvider;
        public Provider<ThresholdSensor[]> providePostureToSecondaryProximitySensorMappingProvider;
        public Provider<ThresholdSensor> providePrimaryProximitySensorProvider;
        public Provider<LogBuffer> providePrivacyLogBufferProvider;
        public Provider<ProximityCheck> provideProximityCheckProvider;
        public Provider<ProximitySensor> provideProximitySensorProvider;
        public Provider<LogBuffer> provideQSFragmentDisableLogBufferProvider;
        public Provider<QuickAccessWalletClient> provideQuickAccessWalletClientProvider;
        public Provider<LogBuffer> provideQuickSettingsLogBufferProvider;
        public Provider<RecentsImplementation> provideRecentsImplProvider;
        public Provider<Recents> provideRecentsProvider;
        public Provider<ReduceBrightColorsController> provideReduceBrightColorsListenerProvider;
        public Provider<Optional<ReverseChargingViewController>> provideReverseChargingViewControllerOptionalProvider;
        public Provider<Optional<ReverseWirelessCharger>> provideReverseWirelessChargerProvider;
        public Provider<ThresholdSensor> provideSecondaryProximitySensorProvider;
        public Provider<SensorPrivacyController> provideSensorPrivacyControllerProvider;
        public Provider<SharedPreferences> provideSharePreferencesProvider;
        public Provider<SmartReplyController> provideSmartReplyControllerProvider;
        public Provider<LogBuffer> provideSwipeAwayGestureLogBufferProvider;
        public Provider<Optional<SysUIUnfoldComponent>> provideSysUIUnfoldComponentProvider;
        public Provider<SysUiState> provideSysUiStateProvider;
        public Provider<ThemeOverlayApplier> provideThemeOverlayManagerProvider;
        public Provider<Handler> provideTimeTickHandlerProvider;
        public Provider<Boolean> provideTimeoutToUserZeroFeatureEnabledProvider;
        public Provider<Monitor> provideTimeoutToUserZeroPreconditionsMonitorProvider;
        public Provider<Set<Condition>> provideTimeoutToUserZeroPreconditionsProvider;
        public Provider<Integer> provideTimeoutToUserZeroUserSettingDurationProvider;
        public Provider<LogBuffer> provideToastLogBufferProvider;
        public Provider<Set<TouchActionRegion>> provideTouchActionRegionsProvider;
        public Provider<Set<TouchInsideRegion>> provideTouchInsideRegionsProvider;
        public Provider<Executor> provideUiBackgroundExecutorProvider;
        public Provider<Optional<UsbManager>> provideUsbManagerProvider;
        public Provider<Integer> provideUserIdProvider;
        public Provider<Map<String, UserAction>> provideUserSelectedActionsProvider;
        public Provider<UserTracker> provideUserTrackerProvider;
        public Provider<VisualStabilityManager> provideVisualStabilityManagerProvider;
        public Provider<VolumeDialog> provideVolumeDialogProvider;
        public Provider<PowerUI.WarningsUI> provideWarningsUiProvider;
        public Provider<LayoutInflater> providerLayoutInflaterProvider;
        public Provider<SectionHeaderController> providesAlertingHeaderControllerProvider;
        public Provider<NodeController> providesAlertingHeaderNodeControllerProvider;
        public Provider<SectionHeaderControllerSubcomponent> providesAlertingHeaderSubcomponentProvider;
        public Provider<MessageRouter> providesBackgroundMessageRouterProvider;
        public Provider<Set<FalsingClassifier>> providesBrightLineGestureClassifiersProvider;
        public Provider<BroadcastDispatcher> providesBroadcastDispatcherProvider;
        public Provider<Choreographer> providesChoreographerProvider;
        public Provider<Boolean> providesControlsFeatureEnabledProvider;
        public Provider<String> providesDeviceNameProvider;
        public Provider<String[]> providesDeviceStateRotationLockDefaultsProvider;
        public Provider<Float> providesDoubleTapTouchSlopProvider;
        public Provider<DreamBackend> providesDreamBackendProvider;
        public Provider<MediaHost> providesDreamMediaHostProvider;
        public Provider<Boolean> providesDreamSelectedProvider;
        public Provider<ContentResolver> providesDreamSettingContentObserverProvider;
        public Provider<PendingIntent> providesDreamSettingPendingIntentProvider;
        public Provider<Uri> providesDreamsSettingUriProvider;
        public Provider<SectionHeaderController> providesIncomingHeaderControllerProvider;
        public Provider<NodeController> providesIncomingHeaderNodeControllerProvider;
        public Provider<SectionHeaderControllerSubcomponent> providesIncomingHeaderSubcomponentProvider;
        public Provider<MediaHost> providesKeyguardMediaHostProvider;
        public Provider<MessageRouter> providesMainMessageRouterProvider;
        public Provider<Optional<MediaMuteAwaitConnectionCli>> providesMediaMuteAwaitConnectionCliProvider;
        public Provider<View> providesMediaShellViewProvider;
        public Provider<Optional<MediaTttChipControllerReceiver>> providesMediaTttChipControllerReceiverProvider;
        public Provider<Optional<MediaTttChipControllerSender>> providesMediaTttChipControllerSenderProvider;
        public Provider<Optional<MediaTttCommandLineHelper>> providesMediaTttCommandLineHelperProvider;
        public Provider<ModeSwitchesController> providesModeSwitchesControllerProvider;
        public Provider<Optional<NearbyMediaDevicesManager>> providesNearbyMediaDevicesManagerProvider;
        public Provider<NotificationManager> providesNotificationManagerProvider;
        public Provider<SectionHeaderController> providesPeopleHeaderControllerProvider;
        public Provider<NodeController> providesPeopleHeaderNodeControllerProvider;
        public Provider<SectionHeaderControllerSubcomponent> providesPeopleHeaderSubcomponentProvider;
        public Provider<Executor> providesPluginExecutorProvider;
        public Provider<MediaHost> providesQSMediaHostProvider;
        public Provider<MediaHost> providesQuickQSMediaHostProvider;
        public Provider<Notification> providesSetupDreamNotificationProvider;
        public Provider<SectionHeaderController> providesSilentHeaderControllerProvider;
        public Provider<NodeController> providesSilentHeaderNodeControllerProvider;
        public Provider<SectionHeaderControllerSubcomponent> providesSilentHeaderSubcomponentProvider;
        public Provider<Float> providesSingleTapTouchSlopProvider;
        public Provider<StatusBarWindowView> providesStatusBarWindowViewProvider;
        public Provider<ViewMediatorCallback> providesViewMediatorCallbackProvider;
        public Provider proximityClassifierProvider;
        public Provider<Proximity> proximityProvider;
        public Provider proximitySensorImplProvider;
        public Provider<PulseExpansionHandler> pulseExpansionHandlerProvider;
        public Provider<QRCodeScannerController> qRCodeScannerControllerProvider;
        public Provider<QRCodeScannerTile> qRCodeScannerTileProvider;
        public Provider<QSFactoryImplGoogle> qSFactoryImplGoogleProvider;
        public Provider<QSLogger> qSLoggerProvider;
        public Provider<QSTileHost> qSTileHostProvider;
        public Provider<QsFrameTranslateImpl> qsFrameTranslateImplProvider;
        public Provider<QuickAccessWalletController> quickAccessWalletControllerProvider;
        public Provider<QuickAccessWalletTile> quickAccessWalletTileProvider;
        public Provider<RecordingController> recordingControllerProvider;
        public Provider<RecordingService> recordingServiceProvider;
        public Provider<ReduceBrightColorsTile> reduceBrightColorsTileProvider;
        public Provider<DreamClockTimeComplication.Registrant> registrantProvider;
        public Provider<DreamClockDateComplication.Registrant> registrantProvider2;
        public Provider<DreamWeatherComplication.Registrant> registrantProvider3;
        public Provider<SmartSpaceComplication.Registrant> registrantProvider4;
        public Provider<RemoteInputNotificationRebuilder> remoteInputNotificationRebuilderProvider;
        public Provider<RemoteInputQuickSettingsDisabler> remoteInputQuickSettingsDisablerProvider;
        public Provider<RemoteInputUriController> remoteInputUriControllerProvider;
        public Provider<RenderStageManager> renderStageManagerProvider;
        public Provider<ResumeMediaBrowserFactory> resumeMediaBrowserFactoryProvider;
        public Provider<ReverseChargingController> reverseChargingControllerProvider;
        public Provider<ReverseChargingTile> reverseChargingTileProvider;
        public Provider<ReverseChargingViewController> reverseChargingViewControllerProvider;
        public Provider<RingerModeTrackerImpl> ringerModeTrackerImplProvider;
        public Provider<RingtonePlayer> ringtonePlayerProvider;
        public Provider<RotationLockControllerImpl> rotationLockControllerImplProvider;
        public Provider<RotationLockTileGoogle> rotationLockTileGoogleProvider;
        public Provider<RotationPolicyWrapperImpl> rotationPolicyWrapperImplProvider;
        public Provider<RowContentBindStageLogger> rowContentBindStageLoggerProvider;
        public Provider<RowContentBindStage> rowContentBindStageProvider;
        public Provider<ScreenDecorations> screenDecorationsProvider;
        public Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
        public Provider<ScreenOnCoordinator> screenOnCoordinatorProvider;
        public Provider<ScreenPinningRequest> screenPinningRequestProvider;
        public Provider<ScreenRecordController> screenRecordControllerProvider;
        public Provider<ScreenRecordTile> screenRecordTileProvider;
        public Provider<ScreenTouch> screenTouchProvider;
        public Provider<ScreenshotController> screenshotControllerProvider;
        public Provider<ScreenshotNotificationsController> screenshotNotificationsControllerProvider;
        public Provider<ScreenshotSmartActions> screenshotSmartActionsProvider;
        public Provider<ScrimController> scrimControllerProvider;
        public Provider<com.google.android.systemui.assist.uihints.ScrimController> scrimControllerProvider2;
        public Provider<ScrollCaptureClient> scrollCaptureClientProvider;
        public Provider<ScrollCaptureController> scrollCaptureControllerProvider;
        public Provider<SectionClassifier> sectionClassifierProvider;
        public Provider<SectionHeaderControllerSubcomponent.Builder> sectionHeaderControllerSubcomponentBuilderProvider;
        public Provider<SectionHeaderVisibilityProvider> sectionHeaderVisibilityProvider;
        public Provider secureSettingsImplProvider;
        public Provider<SecurityControllerImpl> securityControllerImplProvider;
        public Provider<SeekBarViewModel> seekBarViewModelProvider;
        public Provider<SensorConfiguration> sensorConfigurationProvider;
        public Provider<SensorUseStartedActivity> sensorUseStartedActivityProvider;
        public Provider<ServiceBinderCallbackComponent$Factory> serviceBinderCallbackComponentFactoryProvider;
        public Provider<ServiceConfigurationGoogle> serviceConfigurationGoogleProvider;
        public Provider<GarbageMonitor.Service> serviceProvider;
        public Provider<SessionTracker> sessionTrackerProvider;
        public Provider<Optional<BackAnimation>> setBackAnimationProvider;
        public Provider<Optional<Bubbles>> setBubblesProvider;
        public Provider<Optional<CompatUI>> setCompatUIProvider;
        public Provider<Optional<DisplayAreaHelper>> setDisplayAreaHelperProvider;
        public Provider<Optional<DragAndDrop>> setDragAndDropProvider;
        public Provider<Optional<HideDisplayCutout>> setHideDisplayCutoutProvider;
        public Provider<Optional<LegacySplitScreen>> setLegacySplitScreenProvider;
        public Provider<Set<NgaMessageHandler.AudioInfoListener>> setOfAudioInfoListenerProvider;
        public Provider<Set<NgaMessageHandler.CardInfoListener>> setOfCardInfoListenerProvider;
        public Provider<Set<NgaMessageHandler.ChipsInfoListener>> setOfChipsInfoListenerProvider;
        public Provider<Set<NgaMessageHandler.ClearListener>> setOfClearListenerProvider;
        public Provider<Set<NgaMessageHandler.ConfigInfoListener>> setOfConfigInfoListenerProvider;
        public Provider<Set<NgaMessageHandler.EdgeLightsInfoListener>> setOfEdgeLightsInfoListenerProvider;
        public Provider<Set<NgaMessageHandler.GoBackListener>> setOfGoBackListenerProvider;
        public Provider<Set<NgaMessageHandler.GreetingInfoListener>> setOfGreetingInfoListenerProvider;
        public Provider<Set<NgaMessageHandler.KeepAliveListener>> setOfKeepAliveListenerProvider;
        public Provider<Set<NgaMessageHandler.KeyboardInfoListener>> setOfKeyboardInfoListenerProvider;
        public Provider<Set<NgaMessageHandler.NavBarVisibilityListener>> setOfNavBarVisibilityListenerProvider;
        public Provider<Set<NgaMessageHandler.StartActivityInfoListener>> setOfStartActivityInfoListenerProvider;
        public Provider<Set<NgaMessageHandler.SwipeListener>> setOfSwipeListenerProvider;
        public Provider<Set<NgaMessageHandler.TakeScreenshotListener>> setOfTakeScreenshotListenerProvider;
        public Provider<Set<TouchActionRegion>> setOfTouchActionRegionProvider;
        public Provider<Set<TouchInsideRegion>> setOfTouchInsideRegionProvider;
        public Provider<Set<NgaMessageHandler.TranscriptionInfoListener>> setOfTranscriptionInfoListenerProvider;
        public Provider<Set<NgaMessageHandler.WarmingListener>> setOfWarmingListenerProvider;
        public Provider<Set<NgaMessageHandler.ZerostateInfoListener>> setOfZerostateInfoListenerProvider;
        public Provider<Optional<OneHanded>> setOneHandedProvider;
        public Provider<Optional<Pip>> setPipProvider;
        public Provider<Optional<RecentTasks>> setRecentTasksProvider;
        public Provider<Optional<ShellCommandHandler>> setShellCommandHandlerProvider;
        public Provider<Optional<SplitScreen>> setSplitScreenProvider;
        public Provider<Optional<StartingSurface>> setStartingSurfaceProvider;
        public Provider<Optional<TaskSurfaceHelper>> setTaskSurfaceHelperProvider;
        public Provider<Optional<TaskViewFactory>> setTaskViewFactoryProvider;
        public Provider<ShellTransitions> setTransitionsProvider;
        public Provider<com.google.android.systemui.columbus.actions.SettingsAction> settingsActionProvider;
        public Provider<SetupDreamComplication> setupDreamComplicationProvider;
        public Provider<SetupDreamComponent$Factory> setupDreamComponentFactoryProvider;
        public Provider<SetupWizard> setupWizardProvider;
        public Provider<ShadeControllerImpl> shadeControllerImplProvider;
        public Provider<ShadeEventCoordinatorLogger> shadeEventCoordinatorLoggerProvider;
        public Provider<ShadeEventCoordinator> shadeEventCoordinatorProvider;
        public Provider<ShadeListBuilderLogger> shadeListBuilderLoggerProvider;
        public Provider<ShadeListBuilder> shadeListBuilderProvider;
        public Provider<ShadeViewDifferLogger> shadeViewDifferLoggerProvider;
        public Provider<ShadeViewManagerFactory> shadeViewManagerFactoryProvider;
        public ShadeViewManager_Factory shadeViewManagerProvider;
        public Provider<ShortcutBarController> shortcutBarControllerProvider;
        public Provider<ShortcutKeyDispatcher> shortcutKeyDispatcherProvider;
        public Provider<SidefpsController> sidefpsControllerProvider;
        public Provider<SilenceAlertsDisabled> silenceAlertsDisabledProvider;
        public Provider<SilenceCall> silenceCallProvider;
        public Provider<com.google.android.systemui.columbus.actions.SilenceCall> silenceCallProvider2;
        public Provider<SingleTapClassifier> singleTapClassifierProvider;
        public Provider<SliceBroadcastRelayHandler> sliceBroadcastRelayHandlerProvider;
        public Provider<SmartActionInflaterImpl> smartActionInflaterImplProvider;
        public Provider<SmartActionsReceiver> smartActionsReceiverProvider;
        public Provider<SmartReplyConstants> smartReplyConstantsProvider;
        public Provider<SmartReplyInflaterImpl> smartReplyInflaterImplProvider;
        public Provider<SmartReplyStateInflaterImpl> smartReplyStateInflaterImplProvider;
        public Provider<SmartSpaceComplication> smartSpaceComplicationProvider;
        public Provider<SmartSpaceController> smartSpaceControllerProvider;
        public Provider<SnoozeAlarm> snoozeAlarmProvider;
        public Provider<SquishyNavigationButtons> squishyNavigationButtonsProvider;
        public Provider<StatusBarComponent.Factory> statusBarComponentFactoryProvider;
        public Provider<StatusBarContentInsetsProvider> statusBarContentInsetsProvider;
        public Provider<StatusBarGoogle> statusBarGoogleProvider;
        public Provider<StatusBarHideIconsForBouncerManager> statusBarHideIconsForBouncerManagerProvider;
        public Provider<StatusBarIconControllerImpl> statusBarIconControllerImplProvider;
        public Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
        public Provider<StatusBarLocationPublisher> statusBarLocationPublisherProvider;
        public Provider<StatusBarNotificationActivityStarterLogger> statusBarNotificationActivityStarterLoggerProvider;
        public Provider<StatusBarRemoteInputCallback> statusBarRemoteInputCallbackProvider;
        public Provider<StatusBarSignalPolicy> statusBarSignalPolicyProvider;
        public Provider<StatusBarStateControllerImpl> statusBarStateControllerImplProvider;
        public Provider<StatusBarTouchableRegionManager> statusBarTouchableRegionManagerProvider;
        public Provider<StatusBarUserInfoTracker> statusBarUserInfoTrackerProvider;
        public Provider<StatusBarUserSwitcherFeatureController> statusBarUserSwitcherFeatureControllerProvider;
        public Provider<StatusBarWindowController> statusBarWindowControllerProvider;
        public Provider<StatusBarWindowStateController> statusBarWindowStateControllerProvider;
        public Provider<StorageNotification> storageNotificationProvider;
        public Provider<QSCarrierGroupController.SubscriptionManagerSlotIndexResolver> subscriptionManagerSlotIndexResolverProvider;
        public Provider swipeHandlerProvider;
        public Provider<SwipeStatusBarAwayGestureHandler> swipeStatusBarAwayGestureHandlerProvider;
        public Provider<SwipeStatusBarAwayGestureLogger> swipeStatusBarAwayGestureLoggerProvider;
        public Provider<SysUIUnfoldComponent.Factory> sysUIUnfoldComponentFactoryProvider;
        public Provider<SystemActions> systemActionsProvider;
        public Provider<SystemEventChipAnimationController> systemEventChipAnimationControllerProvider;
        public Provider<SystemEventCoordinator> systemEventCoordinatorProvider;
        public Provider<SystemKeyPress> systemKeyPressProvider;
        public Provider<SystemStatusAnimationScheduler> systemStatusAnimationSchedulerProvider;
        public Provider<SystemUIAuxiliaryDumpService> systemUIAuxiliaryDumpServiceProvider;
        public Provider<SystemUIDialogManager> systemUIDialogManagerProvider;
        public Provider<SystemUIService> systemUIServiceProvider;
        public Provider<SysuiColorExtractor> sysuiColorExtractorProvider;
        public Provider takeScreenshotHandlerProvider;
        public Provider<TakeScreenshot> takeScreenshotProvider;
        public Provider<TakeScreenshotService> takeScreenshotServiceProvider;
        public Provider<TargetSdkResolver> targetSdkResolverProvider;
        public Provider taskStackNotifierProvider;
        public Provider<TelephonyActivity> telephonyActivityProvider;
        public Provider<com.google.android.systemui.columbus.gates.TelephonyActivity> telephonyActivityProvider2;
        public Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
        public Provider<ThemeOverlayController> themeOverlayControllerProvider;
        public C0013TileLifecycleManager_Factory tileLifecycleManagerProvider;
        public Provider<TileServices> tileServicesProvider;
        public Provider<TimeoutHandler> timeoutHandlerProvider;
        public Provider timeoutManagerProvider;
        public Provider<TimeoutToUserZeroCallback> timeoutToUserZeroCallbackProvider;
        public Provider<TimeoutToUserZeroFeatureCondition> timeoutToUserZeroFeatureConditionProvider;
        public Provider<TimeoutToUserZeroSettingCondition> timeoutToUserZeroSettingConditionProvider;
        public Provider<ToastController> toastControllerProvider;
        public Provider<ToastFactory> toastFactoryProvider;
        public Provider<ToastLogger> toastLoggerProvider;
        public Provider<ToastUI> toastUIProvider;
        public Provider<ToggleFlashlight> toggleFlashlightProvider;
        public Provider<TouchInsideHandler> touchInsideHandlerProvider;
        public Provider touchOutsideHandlerProvider;
        public Provider<TranscriptionController> transcriptionControllerProvider;
        public Provider<TunablePadding.TunablePaddingService> tunablePaddingServiceProvider;
        public Provider<TunerActivity> tunerActivityProvider;
        public Provider<TunerServiceImpl> tunerServiceImplProvider;
        public Provider<TvNotificationHandler> tvNotificationHandlerProvider;
        public Provider<TvNotificationPanelActivity> tvNotificationPanelActivityProvider;
        public Provider<TvUnblockSensorActivity> tvUnblockSensorActivityProvider;
        public Provider<TypeClassifier> typeClassifierProvider;
        public Provider<UdfpsController> udfpsControllerProvider;
        public Provider<UdfpsHapticsSimulator> udfpsHapticsSimulatorProvider;
        public Provider<UdfpsHbmController> udfpsHbmControllerProvider;
        public Provider<UiModeNightTile> uiModeNightTileProvider;
        public Provider<UiOffloadThread> uiOffloadThreadProvider;
        public Provider<UnfoldLatencyTracker> unfoldLatencyTrackerProvider;
        public Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;
        public Provider<UnpinNotifications> unpinNotificationsProvider;
        public Provider<com.google.android.systemui.columbus.actions.UnpinNotifications> unpinNotificationsProvider2;
        public Provider<UsbDebuggingActivity> usbDebuggingActivityProvider;
        public Provider<UsbDebuggingSecondaryUserActivity> usbDebuggingSecondaryUserActivityProvider;
        public Provider<UsbState> usbStateProvider;
        public Provider<UserActivity> userActivityProvider;
        public Provider<UserCreator> userCreatorProvider;
        public Provider<UserInfoControllerImpl> userInfoControllerImplProvider;
        public Provider<UserSelectedAction> userSelectedActionProvider;
        public Provider<UserSwitchDialogController> userSwitchDialogControllerProvider;
        public Provider<UserSwitcherActivity> userSwitcherActivityProvider;
        public Provider<UserSwitcherController> userSwitcherControllerProvider;
        public Provider<VibratorHelper> vibratorHelperProvider;
        public Provider<VisualStabilityCoordinator> visualStabilityCoordinatorProvider;
        public Provider<VisualStabilityProvider> visualStabilityProvider;
        public Provider<VolumeDialogComponent> volumeDialogComponentProvider;
        public Provider<VolumeDialogControllerImpl> volumeDialogControllerImplProvider;
        public Provider<VolumeUI> volumeUIProvider;
        public Provider<VrMode> vrModeProvider;
        public Provider<WMShell> wMShellProvider;
        public Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;
        public Provider<WalletActivity> walletActivityProvider;
        public Provider<WalletControllerImpl> walletControllerImplProvider;
        public Provider<WallpaperController> wallpaperControllerProvider;
        public Provider<WallpaperNotifier> wallpaperNotifierProvider;
        public Provider<AccessPointControllerImpl.WifiPickerTrackerFactory> wifiPickerTrackerFactoryProvider;
        public Provider<WifiTile> wifiTileProvider;
        public Provider<WindowMagnification> windowMagnificationProvider;
        public Provider<WiredChargingRippleController> wiredChargingRippleControllerProvider;
        public Provider<WorkLockActivity> workLockActivityProvider;
        public Provider<WorkModeTile> workModeTileProvider;
        public Provider<ZenModeControllerImpl> zenModeControllerImplProvider;
        public Provider zigZagClassifierProvider;

        /* loaded from: classes.dex */
        public final class CommunalViewComponentFactory implements CommunalViewComponent.Factory {
            public CommunalViewComponentFactory() {
            }

            @Override // com.android.systemui.communal.dagger.CommunalViewComponent.Factory
            public final CommunalViewComponent build(CommunalHostView communalHostView) {
                Objects.requireNonNull(communalHostView);
                return new CommunalViewComponentImpl(communalHostView);
            }
        }

        /* loaded from: classes.dex */
        public final class CommunalViewComponentImpl implements CommunalViewComponent {
            public final CommunalHostView arg0;

            public CommunalViewComponentImpl(CommunalHostView communalHostView) {
                this.arg0 = communalHostView;
            }

            @Override // com.android.systemui.communal.dagger.CommunalViewComponent
            public final CommunalHostViewController getCommunalHostViewController() {
                Executor executor = DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider.mo144get();
                CommunalStateController communalStateController = TitanSysUIComponentImpl.this.communalStateControllerProvider.mo144get();
                KeyguardUpdateMonitor keyguardUpdateMonitor = TitanSysUIComponentImpl.this.keyguardUpdateMonitorProvider.mo144get();
                KeyguardStateControllerImpl keyguardStateControllerImpl = TitanSysUIComponentImpl.this.keyguardStateControllerImplProvider.mo144get();
                TitanSysUIComponentImpl.this.dozeParametersProvider.mo144get();
                return CommunalHostViewController_Factory.newInstance(executor, communalStateController, keyguardUpdateMonitor, keyguardStateControllerImpl, TitanSysUIComponentImpl.this.screenOffAnimationControllerProvider.mo144get(), TitanSysUIComponentImpl.this.statusBarStateControllerImplProvider.mo144get(), this.arg0);
            }
        }

        /* loaded from: classes.dex */
        public final class CoordinatorsSubcomponentFactory implements CoordinatorsSubcomponent.Factory {
            public CoordinatorsSubcomponentFactory() {
            }

            @Override // com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorsSubcomponent.Factory
            public final CoordinatorsSubcomponent create() {
                return new CoordinatorsSubcomponentImpl(TitanSysUIComponentImpl.this);
            }
        }

        /* loaded from: classes.dex */
        public final class CoordinatorsSubcomponentImpl implements CoordinatorsSubcomponent {
            public Provider<AppOpsCoordinator> appOpsCoordinatorProvider;
            public Provider<BubbleCoordinator> bubbleCoordinatorProvider;
            public Provider<CommunalCoordinator> communalCoordinatorProvider;
            public Provider<ConversationCoordinator> conversationCoordinatorProvider;
            public Provider<DataStoreCoordinator> dataStoreCoordinatorProvider;
            public Provider<DebugModeCoordinator> debugModeCoordinatorProvider;
            public Provider<DeviceProvisionedCoordinator> deviceProvisionedCoordinatorProvider;
            public RingtonePlayer_Factory gutsCoordinatorLoggerProvider;
            public Provider<GutsCoordinator> gutsCoordinatorProvider;
            public MediaBrowserFactory_Factory headsUpCoordinatorLoggerProvider;
            public Provider<HeadsUpCoordinator> headsUpCoordinatorProvider;
            public Provider<HideNotifsForOtherUsersCoordinator> hideNotifsForOtherUsersCoordinatorProvider;
            public Provider<KeyguardCoordinator> keyguardCoordinatorProvider;
            public Provider<MediaCoordinator> mediaCoordinatorProvider;
            public Provider<NotifCoordinatorsImpl> notifCoordinatorsImplProvider;
            public TypeClassifier_Factory preparationCoordinatorLoggerProvider;
            public Provider<PreparationCoordinator> preparationCoordinatorProvider;
            public Provider<RankingCoordinator> rankingCoordinatorProvider;
            public Provider<RemoteInputCoordinator> remoteInputCoordinatorProvider;
            public Provider<RowAppearanceCoordinator> rowAppearanceCoordinatorProvider;
            public Provider sensitiveContentCoordinatorImplProvider;
            public WMShellBaseModule_ProvideHideDisplayCutoutFactory sharedCoordinatorLoggerProvider;
            public Provider<SmartspaceDedupingCoordinator> smartspaceDedupingCoordinatorProvider;
            public Provider<StackCoordinator> stackCoordinatorProvider;
            public Provider<ViewConfigCoordinator> viewConfigCoordinatorProvider;
            public Provider<HideLocallyDismissedNotifsCoordinator> hideLocallyDismissedNotifsCoordinatorProvider = DoubleCheck.provider(HideLocallyDismissedNotifsCoordinator_Factory.InstanceHolder.INSTANCE);
            public Provider<GroupCountCoordinator> groupCountCoordinatorProvider = DoubleCheck.provider(GroupCountCoordinator_Factory.InstanceHolder.INSTANCE);

            public CoordinatorsSubcomponentImpl(TitanSysUIComponentImpl titanSysUIComponentImpl) {
                this.dataStoreCoordinatorProvider = DoubleCheck.provider(new KeyboardUI_Factory(titanSysUIComponentImpl.notifLiveDataStoreImplProvider, 7));
                WMShellBaseModule_ProvideHideDisplayCutoutFactory wMShellBaseModule_ProvideHideDisplayCutoutFactory = new WMShellBaseModule_ProvideHideDisplayCutoutFactory(titanSysUIComponentImpl.provideNotificationsLogBufferProvider, 3);
                this.sharedCoordinatorLoggerProvider = wMShellBaseModule_ProvideHideDisplayCutoutFactory;
                this.hideNotifsForOtherUsersCoordinatorProvider = DoubleCheck.provider(new ScreenPinningRequest_Factory(titanSysUIComponentImpl.notificationLockscreenUserManagerGoogleProvider, wMShellBaseModule_ProvideHideDisplayCutoutFactory, 3));
                this.keyguardCoordinatorProvider = DoubleCheck.provider(ControlsControllerImpl_Factory.create$1(DaggerTitanGlobalRootComponent.this.contextProvider, titanSysUIComponentImpl.provideHandlerProvider, titanSysUIComponentImpl.keyguardStateControllerImplProvider, titanSysUIComponentImpl.notificationLockscreenUserManagerGoogleProvider, titanSysUIComponentImpl.providesBroadcastDispatcherProvider, titanSysUIComponentImpl.statusBarStateControllerImplProvider, titanSysUIComponentImpl.keyguardUpdateMonitorProvider, titanSysUIComponentImpl.highPriorityProvider, titanSysUIComponentImpl.sectionHeaderVisibilityProvider));
                this.rankingCoordinatorProvider = DoubleCheck.provider(RankingCoordinator_Factory.create(titanSysUIComponentImpl.statusBarStateControllerImplProvider, titanSysUIComponentImpl.highPriorityProvider, titanSysUIComponentImpl.sectionClassifierProvider, titanSysUIComponentImpl.providesAlertingHeaderNodeControllerProvider, titanSysUIComponentImpl.providesSilentHeaderControllerProvider, titanSysUIComponentImpl.providesSilentHeaderNodeControllerProvider));
                this.appOpsCoordinatorProvider = DoubleCheck.provider(new MediaViewController_Factory(titanSysUIComponentImpl.foregroundServiceControllerProvider, titanSysUIComponentImpl.appOpsControllerImplProvider, DaggerTitanGlobalRootComponent.this.provideMainDelayableExecutorProvider, 1));
                this.deviceProvisionedCoordinatorProvider = DoubleCheck.provider(new DeviceProvisionedCoordinator_Factory(titanSysUIComponentImpl.provideDeviceProvisionedControllerProvider, DaggerTitanGlobalRootComponent.this.provideIPackageManagerProvider));
                this.bubbleCoordinatorProvider = DoubleCheck.provider(new RowContentBindStage_Factory(titanSysUIComponentImpl.provideBubblesManagerProvider, titanSysUIComponentImpl.setBubblesProvider, titanSysUIComponentImpl.notifCollectionProvider, 1));
                MediaBrowserFactory_Factory mediaBrowserFactory_Factory = new MediaBrowserFactory_Factory(titanSysUIComponentImpl.provideNotificationHeadsUpLogBufferProvider, 4);
                this.headsUpCoordinatorLoggerProvider = mediaBrowserFactory_Factory;
                this.headsUpCoordinatorProvider = DoubleCheck.provider(HeadsUpCoordinator_Factory.create(mediaBrowserFactory_Factory, titanSysUIComponentImpl.bindSystemClockProvider, titanSysUIComponentImpl.provideHeadsUpManagerPhoneProvider, titanSysUIComponentImpl.headsUpViewBinderProvider, titanSysUIComponentImpl.notificationInterruptStateProviderImplProvider, titanSysUIComponentImpl.provideNotificationRemoteInputManagerProvider, titanSysUIComponentImpl.providesIncomingHeaderNodeControllerProvider, DaggerTitanGlobalRootComponent.this.provideMainDelayableExecutorProvider));
                RingtonePlayer_Factory ringtonePlayer_Factory = new RingtonePlayer_Factory(titanSysUIComponentImpl.provideNotificationsLogBufferProvider, 3);
                this.gutsCoordinatorLoggerProvider = ringtonePlayer_Factory;
                this.gutsCoordinatorProvider = DoubleCheck.provider(new OpaHomeButton_Factory(titanSysUIComponentImpl.provideNotifGutsViewManagerProvider, ringtonePlayer_Factory, DaggerTitanGlobalRootComponent.this.dumpManagerProvider, 1));
                this.communalCoordinatorProvider = DoubleCheck.provider(new CommunalCoordinator_Factory(DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider, titanSysUIComponentImpl.provideNotificationEntryManagerProvider, titanSysUIComponentImpl.notificationLockscreenUserManagerGoogleProvider, titanSysUIComponentImpl.communalStateControllerProvider));
                this.conversationCoordinatorProvider = DoubleCheck.provider(new WMShellBaseModule_ProvideSystemWindowsFactory(titanSysUIComponentImpl.peopleNotificationIdentifierImplProvider, titanSysUIComponentImpl.providesPeopleHeaderNodeControllerProvider, 1));
                this.debugModeCoordinatorProvider = DoubleCheck.provider(new DependencyProvider_ProvideHandlerFactory(titanSysUIComponentImpl.debugModeFilterProvider, 4));
                this.mediaCoordinatorProvider = DoubleCheck.provider(new BootCompleteCacheImpl_Factory(titanSysUIComponentImpl.mediaFeatureFlagProvider, 2));
                TypeClassifier_Factory typeClassifier_Factory = new TypeClassifier_Factory(titanSysUIComponentImpl.provideNotificationsLogBufferProvider, 4);
                this.preparationCoordinatorLoggerProvider = typeClassifier_Factory;
                this.preparationCoordinatorProvider = DoubleCheck.provider(PreparationCoordinator_Factory.create(typeClassifier_Factory, titanSysUIComponentImpl.notifInflaterImplProvider, titanSysUIComponentImpl.notifInflationErrorManagerProvider, titanSysUIComponentImpl.notifViewBarnProvider, titanSysUIComponentImpl.notifUiAdjustmentProvider, DaggerTitanGlobalRootComponent.this.provideIStatusBarServiceProvider, titanSysUIComponentImpl.bindEventManagerImplProvider));
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
                this.remoteInputCoordinatorProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDragAndDropControllerFactory.create(daggerTitanGlobalRootComponent.dumpManagerProvider, titanSysUIComponentImpl.remoteInputNotificationRebuilderProvider, titanSysUIComponentImpl.provideNotificationRemoteInputManagerProvider, daggerTitanGlobalRootComponent.provideMainHandlerProvider, titanSysUIComponentImpl.provideSmartReplyControllerProvider));
                this.rowAppearanceCoordinatorProvider = DoubleCheck.provider(new RingerModeTrackerImpl_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, titanSysUIComponentImpl.assistantFeedbackControllerProvider, titanSysUIComponentImpl.sectionClassifierProvider, 2));
                this.stackCoordinatorProvider = DoubleCheck.provider(new DozeLogger_Factory(titanSysUIComponentImpl.notificationIconAreaControllerProvider, 3));
                this.smartspaceDedupingCoordinatorProvider = DoubleCheck.provider(SmartspaceDedupingCoordinator_Factory.create(titanSysUIComponentImpl.statusBarStateControllerImplProvider, titanSysUIComponentImpl.lockscreenSmartspaceControllerProvider, titanSysUIComponentImpl.provideNotificationEntryManagerProvider, titanSysUIComponentImpl.notificationLockscreenUserManagerGoogleProvider, titanSysUIComponentImpl.notifPipelineProvider, DaggerTitanGlobalRootComponent.this.provideMainDelayableExecutorProvider, titanSysUIComponentImpl.bindSystemClockProvider));
                this.viewConfigCoordinatorProvider = DoubleCheck.provider(new ViewConfigCoordinator_Factory(titanSysUIComponentImpl.provideConfigurationControllerProvider, titanSysUIComponentImpl.notificationLockscreenUserManagerImplProvider, titanSysUIComponentImpl.provideNotificationGutsManagerProvider, titanSysUIComponentImpl.keyguardUpdateMonitorProvider));
                Provider provider = DoubleCheck.provider(MediaDataFilter_Factory.create$1(titanSysUIComponentImpl.dynamicPrivacyControllerProvider, titanSysUIComponentImpl.notificationLockscreenUserManagerGoogleProvider, titanSysUIComponentImpl.keyguardUpdateMonitorProvider, titanSysUIComponentImpl.statusBarStateControllerImplProvider, titanSysUIComponentImpl.keyguardStateControllerImplProvider));
                this.sensitiveContentCoordinatorImplProvider = provider;
                this.notifCoordinatorsImplProvider = DoubleCheck.provider(NotifCoordinatorsImpl_Factory.create(DaggerTitanGlobalRootComponent.this.dumpManagerProvider, titanSysUIComponentImpl.notifPipelineFlagsProvider, this.dataStoreCoordinatorProvider, this.hideLocallyDismissedNotifsCoordinatorProvider, this.hideNotifsForOtherUsersCoordinatorProvider, this.keyguardCoordinatorProvider, this.rankingCoordinatorProvider, this.appOpsCoordinatorProvider, this.deviceProvisionedCoordinatorProvider, this.bubbleCoordinatorProvider, this.headsUpCoordinatorProvider, this.gutsCoordinatorProvider, this.communalCoordinatorProvider, this.conversationCoordinatorProvider, this.debugModeCoordinatorProvider, this.groupCountCoordinatorProvider, this.mediaCoordinatorProvider, this.preparationCoordinatorProvider, this.remoteInputCoordinatorProvider, this.rowAppearanceCoordinatorProvider, this.stackCoordinatorProvider, titanSysUIComponentImpl.shadeEventCoordinatorProvider, this.smartspaceDedupingCoordinatorProvider, this.viewConfigCoordinatorProvider, titanSysUIComponentImpl.visualStabilityCoordinatorProvider, provider));
            }

            @Override // com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorsSubcomponent
            public final NotifCoordinators getNotifCoordinators() {
                return this.notifCoordinatorsImplProvider.mo144get();
            }
        }

        /* loaded from: classes.dex */
        public final class DozeComponentFactory implements DozeComponent.Builder {
            public DozeComponentFactory() {
            }

            @Override // com.android.systemui.doze.dagger.DozeComponent.Builder
            public final DozeComponent build(DozeMachine.Service service) {
                Objects.requireNonNull(service);
                return new DozeComponentImpl(TitanSysUIComponentImpl.this, service);
            }
        }

        /* loaded from: classes.dex */
        public final class DozeComponentImpl implements DozeComponent {
            public InstanceFactory arg0Provider;
            public Provider<DozeAuthRemover> dozeAuthRemoverProvider;
            public Provider<DozeDockHandler> dozeDockHandlerProvider;
            public Provider<DozeFalsingManagerAdapter> dozeFalsingManagerAdapterProvider;
            public Provider<DozeMachine> dozeMachineProvider;
            public Provider<DozePauser> dozePauserProvider;
            public Provider<DozeScreenBrightness> dozeScreenBrightnessProvider;
            public Provider<DozeScreenState> dozeScreenStateProvider;
            public Provider<DozeTriggers> dozeTriggersProvider;
            public Provider<DozeUi> dozeUiProvider;
            public Provider<DozeWallpaperState> dozeWallpaperStateProvider;
            public LatencyTester_Factory providesBrightnessSensorsProvider;
            public DozeModule_ProvidesDozeMachinePartesFactory providesDozeMachinePartesProvider;
            public Provider<WakeLock> providesDozeWakeLockProvider;
            public Provider<DozeMachine.Service> providesWrappedServiceProvider;

            public DozeComponentImpl(TitanSysUIComponentImpl titanSysUIComponentImpl, DozeMachine.Service service) {
                InstanceFactory create = InstanceFactory.create(service);
                this.arg0Provider = create;
                this.providesWrappedServiceProvider = DoubleCheck.provider(new DozeModule_ProvidesWrappedServiceFactory(create, titanSysUIComponentImpl.dozeServiceHostProvider, titanSysUIComponentImpl.dozeParametersProvider));
                this.providesDozeWakeLockProvider = DoubleCheck.provider(new DozeModule_ProvidesDozeWakeLockFactory(titanSysUIComponentImpl.builderProvider, DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider));
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
                this.dozePauserProvider = DoubleCheck.provider(new DozePauser_Factory(daggerTitanGlobalRootComponent.provideMainHandlerProvider, daggerTitanGlobalRootComponent.provideAlarmManagerProvider, titanSysUIComponentImpl.provideAlwaysOnDisplayPolicyProvider));
                this.dozeFalsingManagerAdapterProvider = DoubleCheck.provider(new MediaControllerFactory_Factory(titanSysUIComponentImpl.falsingCollectorImplProvider, 3));
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent2 = DaggerTitanGlobalRootComponent.this;
                this.dozeTriggersProvider = DoubleCheck.provider(DozeTriggers_Factory.create(daggerTitanGlobalRootComponent2.contextProvider, titanSysUIComponentImpl.dozeServiceHostProvider, titanSysUIComponentImpl.provideAmbientDisplayConfigurationProvider, titanSysUIComponentImpl.dozeParametersProvider, titanSysUIComponentImpl.asyncSensorManagerProvider, this.providesDozeWakeLockProvider, titanSysUIComponentImpl.provideDockManagerProvider, titanSysUIComponentImpl.provideProximitySensorProvider, titanSysUIComponentImpl.provideProximityCheckProvider, titanSysUIComponentImpl.dozeLogProvider, titanSysUIComponentImpl.providesBroadcastDispatcherProvider, titanSysUIComponentImpl.secureSettingsImplProvider, titanSysUIComponentImpl.authControllerProvider, daggerTitanGlobalRootComponent2.provideMainDelayableExecutorProvider, daggerTitanGlobalRootComponent2.provideUiEventLoggerProvider, titanSysUIComponentImpl.keyguardStateControllerImplProvider, titanSysUIComponentImpl.devicePostureControllerImplProvider, titanSysUIComponentImpl.provideBatteryControllerProvider));
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent3 = DaggerTitanGlobalRootComponent.this;
                this.dozeUiProvider = DoubleCheck.provider(DozeUi_Factory.create(daggerTitanGlobalRootComponent3.contextProvider, daggerTitanGlobalRootComponent3.provideAlarmManagerProvider, this.providesDozeWakeLockProvider, titanSysUIComponentImpl.dozeServiceHostProvider, daggerTitanGlobalRootComponent3.provideMainHandlerProvider, titanSysUIComponentImpl.dozeParametersProvider, titanSysUIComponentImpl.keyguardUpdateMonitorProvider, titanSysUIComponentImpl.statusBarStateControllerImplProvider, titanSysUIComponentImpl.dozeLogProvider));
                Provider<AsyncSensorManager> provider = titanSysUIComponentImpl.asyncSensorManagerProvider;
                Provider<Context> provider2 = DaggerTitanGlobalRootComponent.this.contextProvider;
                Provider<DozeParameters> provider3 = titanSysUIComponentImpl.dozeParametersProvider;
                LatencyTester_Factory latencyTester_Factory = new LatencyTester_Factory(provider, provider2, provider3, 1);
                this.providesBrightnessSensorsProvider = latencyTester_Factory;
                Provider<DozeScreenBrightness> provider4 = DoubleCheck.provider(DozeScreenBrightness_Factory.create(provider2, this.providesWrappedServiceProvider, provider, latencyTester_Factory, titanSysUIComponentImpl.dozeServiceHostProvider, titanSysUIComponentImpl.provideHandlerProvider, titanSysUIComponentImpl.provideAlwaysOnDisplayPolicyProvider, titanSysUIComponentImpl.wakefulnessLifecycleProvider, provider3, titanSysUIComponentImpl.devicePostureControllerImplProvider, titanSysUIComponentImpl.dozeLogProvider));
                this.dozeScreenBrightnessProvider = provider4;
                this.dozeScreenStateProvider = DoubleCheck.provider(DozeScreenState_Factory.create(this.providesWrappedServiceProvider, DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider, titanSysUIComponentImpl.dozeServiceHostProvider, titanSysUIComponentImpl.dozeParametersProvider, this.providesDozeWakeLockProvider, titanSysUIComponentImpl.authControllerProvider, titanSysUIComponentImpl.udfpsControllerProvider, titanSysUIComponentImpl.dozeLogProvider, provider4));
                this.dozeWallpaperStateProvider = DoubleCheck.provider(new DozeWallpaperState_Factory(FrameworkServicesModule_ProvideIWallPaperManagerFactory.InstanceHolder.INSTANCE, titanSysUIComponentImpl.biometricUnlockControllerProvider, titanSysUIComponentImpl.dozeParametersProvider, 0));
                this.dozeDockHandlerProvider = DoubleCheck.provider(new ProtoTracer_Factory(titanSysUIComponentImpl.provideAmbientDisplayConfigurationProvider, titanSysUIComponentImpl.provideDockManagerProvider, 1));
                Provider<DozeAuthRemover> provider5 = DoubleCheck.provider(new DozeAuthRemover_Factory(titanSysUIComponentImpl.keyguardUpdateMonitorProvider, 0));
                this.dozeAuthRemoverProvider = provider5;
                DozeModule_ProvidesDozeMachinePartesFactory create2 = DozeModule_ProvidesDozeMachinePartesFactory.create(this.dozePauserProvider, this.dozeFalsingManagerAdapterProvider, this.dozeTriggersProvider, this.dozeUiProvider, this.dozeScreenStateProvider, this.dozeScreenBrightnessProvider, this.dozeWallpaperStateProvider, this.dozeDockHandlerProvider, provider5);
                this.providesDozeMachinePartesProvider = create2;
                this.dozeMachineProvider = DoubleCheck.provider(DozeMachine_Factory.create(this.providesWrappedServiceProvider, titanSysUIComponentImpl.provideAmbientDisplayConfigurationProvider, this.providesDozeWakeLockProvider, titanSysUIComponentImpl.wakefulnessLifecycleProvider, titanSysUIComponentImpl.provideBatteryControllerProvider, titanSysUIComponentImpl.dozeLogProvider, titanSysUIComponentImpl.provideDockManagerProvider, titanSysUIComponentImpl.dozeServiceHostProvider, create2));
            }

            @Override // com.android.systemui.doze.dagger.DozeComponent
            public final DozeMachine getDozeMachine() {
                return this.dozeMachineProvider.mo144get();
            }
        }

        /* loaded from: classes.dex */
        public final class DreamClockDateComplicationComponentFactory implements DreamClockDateComplicationComponent$Factory {
            public DreamClockDateComplicationComponentFactory() {
            }

            @Override // com.android.systemui.dreams.complication.dagger.DreamClockDateComplicationComponent$Factory
            public final DreamClockDateComplicationComponentImpl create() {
                return new DreamClockDateComplicationComponentImpl(TitanSysUIComponentImpl.this);
            }
        }

        /* loaded from: classes.dex */
        public final class DreamClockTimeComplicationComponentFactory implements DreamClockTimeComplicationComponent$Factory {
            public DreamClockTimeComplicationComponentFactory() {
            }

            @Override // com.android.systemui.dreams.complication.dagger.DreamClockTimeComplicationComponent$Factory
            public final DreamClockTimeComplicationComponentImpl create() {
                return new DreamClockTimeComplicationComponentImpl(TitanSysUIComponentImpl.this);
            }
        }

        /* loaded from: classes.dex */
        public final class DreamOverlayComponentFactory implements DreamOverlayComponent.Factory {
            public DreamOverlayComponentFactory() {
            }

            @Override // com.android.systemui.dreams.dagger.DreamOverlayComponent.Factory
            public final DreamOverlayComponent create(ViewModelStore viewModelStore, Complication.Host host) {
                Objects.requireNonNull(viewModelStore);
                return new DreamOverlayComponentImpl(viewModelStore, (DreamOverlayService.AnonymousClass1) host);
            }
        }

        /* loaded from: classes.dex */
        public final class DreamOverlayComponentImpl implements DreamOverlayComponent {
            public final ViewModelStore arg0;
            public final Complication.Host arg1;
            public AnonymousClass1 complicationHostViewComponentFactoryProvider = new Provider<ComplicationHostViewComponent.Factory>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.DreamOverlayComponentImpl.1
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final ComplicationHostViewComponent.Factory mo144get() {
                    return new ComplicationHostViewComponentFactory();
                }
            };
            public Provider<DreamOverlayContainerViewController> dreamOverlayContainerViewControllerProvider;
            public Provider<DreamOverlayStatusBarViewController> dreamOverlayStatusBarViewControllerProvider;
            public Provider<BatteryMeterViewController> providesBatteryMeterViewControllerProvider;
            public Provider<BatteryMeterView> providesBatteryMeterViewProvider;
            public MediaBrowserFactory_Factory providesBurnInProtectionUpdateIntervalProvider;
            public Provider<DreamOverlayContainerView> providesDreamOverlayContainerViewProvider;
            public Provider<ViewGroup> providesDreamOverlayContentViewProvider;
            public Provider<DreamOverlayStatusBarView> providesDreamOverlayStatusBarViewProvider;
            public Provider<LifecycleOwner> providesLifecycleOwnerProvider;
            public Provider<Lifecycle> providesLifecycleProvider;
            public DelegateFactory providesLifecycleRegistryProvider;
            public Provider<Integer> providesMaxBurnInOffsetProvider;

            /* loaded from: classes.dex */
            public final class ComplicationHostViewComponentFactory implements ComplicationHostViewComponent.Factory {
                public ComplicationHostViewComponentFactory() {
                }

                @Override // com.android.systemui.dreams.complication.dagger.ComplicationHostViewComponent.Factory
                public final ComplicationHostViewComponent create() {
                    return new ComplicationHostViewComponentI();
                }
            }

            /* loaded from: classes.dex */
            public final class ComplicationHostViewComponentI implements ComplicationHostViewComponent {
                public Provider<ConstraintLayout> providesComplicationHostViewProvider;
                public Provider<Integer> providesComplicationPaddingProvider;

                public ComplicationHostViewComponentI() {
                    this.providesComplicationHostViewProvider = DoubleCheck.provider(new ComplicationHostViewComponent_ComplicationHostViewModule_ProvidesComplicationHostViewFactory(TitanSysUIComponentImpl.this.providerLayoutInflaterProvider));
                    this.providesComplicationPaddingProvider = DoubleCheck.provider(new ComplicationHostViewComponent_ComplicationHostViewModule_ProvidesComplicationPaddingFactory(DaggerTitanGlobalRootComponent.this.provideResourcesProvider));
                }

                @Override // com.android.systemui.dreams.complication.dagger.ComplicationHostViewComponent
                public final ComplicationHostViewController getController() {
                    DreamOverlayComponentImpl dreamOverlayComponentImpl = DreamOverlayComponentImpl.this;
                    Objects.requireNonNull(dreamOverlayComponentImpl);
                    return new ComplicationHostViewController(this.providesComplicationHostViewProvider.mo144get(), new ComplicationLayoutEngine(this.providesComplicationHostViewProvider.mo144get(), this.providesComplicationPaddingProvider.mo144get().intValue()), DreamOverlayComponentImpl.this.providesLifecycleOwnerProvider.mo144get(), ComplicationModule_ProvidesComplicationCollectionViewModelFactory.providesComplicationCollectionViewModel(dreamOverlayComponentImpl.arg0, new ComplicationCollectionViewModel(new ComplicationCollectionLiveData(TitanSysUIComponentImpl.this.dreamOverlayStateControllerProvider.mo144get()), new ComplicationViewModelTransformer(new ComplicationViewModelComponentFactory()))));
                }
            }

            /* loaded from: classes.dex */
            public final class ComplicationViewModelComponentFactory implements ComplicationViewModelComponent.Factory {
                public ComplicationViewModelComponentFactory() {
                }

                @Override // com.android.systemui.dreams.complication.dagger.ComplicationViewModelComponent.Factory
                public final ComplicationViewModelComponent create(Complication complication, ComplicationId complicationId) {
                    Objects.requireNonNull(complication);
                    Objects.requireNonNull(complicationId);
                    return new ComplicationViewModelComponentI(complication, complicationId);
                }
            }

            /* loaded from: classes.dex */
            public final class ComplicationViewModelComponentI implements ComplicationViewModelComponent {
                public final Complication arg0;
                public final ComplicationId arg1;

                public ComplicationViewModelComponentI(Complication complication, ComplicationId complicationId) {
                    this.arg0 = complication;
                    this.arg1 = complicationId;
                }

                @Override // com.android.systemui.dreams.complication.dagger.ComplicationViewModelComponent
                public final ComplicationViewModelProvider getViewModelProvider() {
                    DreamOverlayComponentImpl dreamOverlayComponentImpl = DreamOverlayComponentImpl.this;
                    return new ComplicationViewModelProvider(dreamOverlayComponentImpl.arg0, new ComplicationViewModel(this.arg0, this.arg1, dreamOverlayComponentImpl.arg1));
                }
            }

            /* JADX WARN: Type inference failed for: r12v2, types: [com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl$DreamOverlayComponentImpl$1] */
            public DreamOverlayComponentImpl(ViewModelStore viewModelStore, DreamOverlayService.AnonymousClass1 r12) {
                this.arg0 = viewModelStore;
                this.arg1 = r12;
                Provider<DreamOverlayContainerView> provider = DoubleCheck.provider(new QSFragmentModule_ProvidesQuickQSPanelFactory(TitanSysUIComponentImpl.this.providerLayoutInflaterProvider, 3));
                this.providesDreamOverlayContainerViewProvider = provider;
                this.providesDreamOverlayContentViewProvider = DoubleCheck.provider(new DateFormatUtil_Factory(provider, 1));
                this.providesDreamOverlayStatusBarViewProvider = DoubleCheck.provider(new ScreenLifecycle_Factory(this.providesDreamOverlayContainerViewProvider, 1));
                Provider<BatteryMeterView> provider2 = DoubleCheck.provider(new PrivacyLogger_Factory(this.providesDreamOverlayContainerViewProvider, 1));
                this.providesBatteryMeterViewProvider = provider2;
                Provider<ConfigurationController> provider3 = TitanSysUIComponentImpl.this.provideConfigurationControllerProvider;
                Provider<TunerServiceImpl> provider4 = TitanSysUIComponentImpl.this.tunerServiceImplProvider;
                Provider<BroadcastDispatcher> provider5 = TitanSysUIComponentImpl.this.providesBroadcastDispatcherProvider;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
                Provider<BatteryMeterViewController> provider6 = DoubleCheck.provider(AppOpsControllerImpl_Factory.create$1(provider2, provider3, provider4, provider5, daggerTitanGlobalRootComponent.provideMainHandlerProvider, daggerTitanGlobalRootComponent.provideContentResolverProvider, TitanSysUIComponentImpl.this.provideBatteryControllerProvider));
                this.providesBatteryMeterViewControllerProvider = provider6;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent2 = DaggerTitanGlobalRootComponent.this;
                this.dreamOverlayStatusBarViewControllerProvider = DoubleCheck.provider(new DreamOverlayStatusBarViewController_Factory(daggerTitanGlobalRootComponent2.contextProvider, this.providesDreamOverlayStatusBarViewProvider, TitanSysUIComponentImpl.this.provideBatteryControllerProvider, provider6, daggerTitanGlobalRootComponent2.provideConnectivityManagagerProvider));
                Provider<Integer> provider7 = DoubleCheck.provider(new DreamOverlayModule_ProvidesMaxBurnInOffsetFactory(DaggerTitanGlobalRootComponent.this.provideResourcesProvider, 0));
                this.providesMaxBurnInOffsetProvider = provider7;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent3 = DaggerTitanGlobalRootComponent.this;
                MediaBrowserFactory_Factory mediaBrowserFactory_Factory = new MediaBrowserFactory_Factory(daggerTitanGlobalRootComponent3.provideResourcesProvider, 2);
                this.providesBurnInProtectionUpdateIntervalProvider = mediaBrowserFactory_Factory;
                this.dreamOverlayContainerViewControllerProvider = DoubleCheck.provider(DreamOverlayContainerViewController_Factory.create(this.providesDreamOverlayContainerViewProvider, this.complicationHostViewComponentFactoryProvider, this.providesDreamOverlayContentViewProvider, this.dreamOverlayStatusBarViewControllerProvider, daggerTitanGlobalRootComponent3.provideMainHandlerProvider, provider7, mediaBrowserFactory_Factory));
                DelegateFactory delegateFactory = new DelegateFactory();
                this.providesLifecycleRegistryProvider = delegateFactory;
                Provider<LifecycleOwner> provider8 = DoubleCheck.provider(new AssistModule_ProvideAssistUtilsFactory(delegateFactory, 1));
                this.providesLifecycleOwnerProvider = provider8;
                DelegateFactory.setDelegate(this.providesLifecycleRegistryProvider, DoubleCheck.provider(new DependencyProvider_ProvidesChoreographerFactory(provider8, 1)));
                this.providesLifecycleProvider = DoubleCheck.provider(new PeopleSpaceWidgetProvider_Factory(this.providesLifecycleOwnerProvider, 2));
            }

            @Override // com.android.systemui.dreams.dagger.DreamOverlayComponent
            public final DreamOverlayContainerViewController getDreamOverlayContainerViewController() {
                return this.dreamOverlayContainerViewControllerProvider.mo144get();
            }

            @Override // com.android.systemui.dreams.dagger.DreamOverlayComponent
            public final DreamOverlayTouchMonitor getDreamOverlayTouchMonitor() {
                TitanSysUIComponentImpl titanSysUIComponentImpl = TitanSysUIComponentImpl.this;
                return new DreamOverlayTouchMonitor(DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider.mo144get(), this.providesLifecycleProvider.mo144get(), new InputSessionComponentFactory(), Collections.singleton(titanSysUIComponentImpl.providesBouncerSwipeTouchHandler()));
            }

            @Override // com.android.systemui.dreams.dagger.DreamOverlayComponent
            public final LifecycleRegistry getLifecycleRegistry() {
                return (LifecycleRegistry) this.providesLifecycleRegistryProvider.mo144get();
            }
        }

        /* loaded from: classes.dex */
        public final class DreamWeatherComplicationComponentFactory implements DreamWeatherComplicationComponent$Factory {
            public DreamWeatherComplicationComponentFactory() {
            }

            @Override // com.android.systemui.dreams.complication.dagger.DreamWeatherComplicationComponent$Factory
            public final DreamWeatherComplicationComponentImpl create() {
                return new DreamWeatherComplicationComponentImpl();
            }
        }

        /* loaded from: classes.dex */
        public final class DreamWeatherComplicationComponentImpl {
            public Provider<TextView> provideComplicationViewProvider;
            public Provider<ComplicationLayoutParams> provideLayoutParamsProvider = DoubleCheck.provider(DreamWeatherComplicationComponent_DreamWeatherComplicationModule_ProvideLayoutParamsFactory.InstanceHolder.INSTANCE);

            public DreamWeatherComplicationComponentImpl() {
                this.provideComplicationViewProvider = DoubleCheck.provider(new DreamWeatherComplicationComponent_DreamWeatherComplicationModule_ProvideComplicationViewFactory(TitanSysUIComponentImpl.this.providerLayoutInflaterProvider));
            }
        }

        /* loaded from: classes.dex */
        public final class ExpandableNotificationRowComponentBuilder implements ExpandableNotificationRowComponent.Builder {
            public ExpandableNotificationRow expandableNotificationRow;
            public NotificationListContainer listContainer;
            public NotificationEntry notificationEntry;
            public ExpandableNotificationRow.OnExpandClickListener onExpandClickListener;

            public ExpandableNotificationRowComponentBuilder() {
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent.Builder
            public final ExpandableNotificationRowComponent build() {
                R$color.checkBuilderRequirement(this.expandableNotificationRow, ExpandableNotificationRow.class);
                R$color.checkBuilderRequirement(this.notificationEntry, NotificationEntry.class);
                R$color.checkBuilderRequirement(this.onExpandClickListener, ExpandableNotificationRow.OnExpandClickListener.class);
                R$color.checkBuilderRequirement(this.listContainer, NotificationListContainer.class);
                return new ExpandableNotificationRowComponentImpl(this.expandableNotificationRow, this.notificationEntry, this.onExpandClickListener, this.listContainer);
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent.Builder
            /* renamed from: expandableNotificationRow */
            public final ExpandableNotificationRowComponent.Builder mo137expandableNotificationRow(ExpandableNotificationRow expandableNotificationRow) {
                Objects.requireNonNull(expandableNotificationRow);
                this.expandableNotificationRow = expandableNotificationRow;
                return this;
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent.Builder
            /* renamed from: listContainer */
            public final ExpandableNotificationRowComponent.Builder mo138listContainer(NotificationListContainer notificationListContainer) {
                Objects.requireNonNull(notificationListContainer);
                this.listContainer = notificationListContainer;
                return this;
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent.Builder
            /* renamed from: notificationEntry */
            public final ExpandableNotificationRowComponent.Builder mo139notificationEntry(NotificationEntry notificationEntry) {
                Objects.requireNonNull(notificationEntry);
                this.notificationEntry = notificationEntry;
                return this;
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent.Builder
            public final ExpandableNotificationRowComponent.Builder onExpandClickListener(NotificationPresenter notificationPresenter) {
                Objects.requireNonNull(notificationPresenter);
                this.onExpandClickListener = notificationPresenter;
                return this;
            }
        }

        /* loaded from: classes.dex */
        public final class ExpandableNotificationRowComponentImpl implements ExpandableNotificationRowComponent {
            public ActivatableNotificationViewController_Factory activatableNotificationViewControllerProvider;
            public Provider<ExpandableNotificationRowController> expandableNotificationRowControllerProvider;
            public ControlsActivity_Factory expandableNotificationRowDragControllerProvider;
            public InstanceFactory expandableNotificationRowProvider;
            public UnpinNotifications_Factory expandableOutlineViewControllerProvider;
            public KeyboardUI_Factory expandableViewControllerProvider;
            public NotificationTapHelper_Factory_Factory factoryProvider;
            public InstanceFactory listContainerProvider;
            public final NotificationEntry notificationEntry;
            public InstanceFactory notificationEntryProvider;
            public InstanceFactory onExpandClickListenerProvider;
            public ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideAppNameFactory provideAppNameProvider;
            public ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory provideNotificationKeyProvider;
            public ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory provideStatusBarNotificationProvider;
            public AnonymousClass1 remoteInputViewSubcomponentFactoryProvider = new Provider<RemoteInputViewSubcomponent.Factory>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.ExpandableNotificationRowComponentImpl.1
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final RemoteInputViewSubcomponent.Factory mo144get() {
                    return new RemoteInputViewSubcomponentFactory();
                }
            };

            /* loaded from: classes.dex */
            public final class RemoteInputViewSubcomponentFactory implements RemoteInputViewSubcomponent.Factory {
                public RemoteInputViewSubcomponentFactory() {
                }

                @Override // com.android.systemui.statusbar.policy.dagger.RemoteInputViewSubcomponent.Factory
                public final RemoteInputViewSubcomponent create(RemoteInputView remoteInputView, RemoteInputController remoteInputController) {
                    Objects.requireNonNull(remoteInputController);
                    return new RemoteInputViewSubcomponentI(remoteInputView, remoteInputController);
                }
            }

            /* loaded from: classes.dex */
            public final class RemoteInputViewSubcomponentI implements RemoteInputViewSubcomponent {
                public final RemoteInputView arg0;
                public final RemoteInputController arg1;

                public RemoteInputViewSubcomponentI(RemoteInputView remoteInputView, RemoteInputController remoteInputController) {
                    this.arg0 = remoteInputView;
                    this.arg1 = remoteInputController;
                }

                @Override // com.android.systemui.statusbar.policy.dagger.RemoteInputViewSubcomponent
                public final RemoteInputViewController getController() {
                    RemoteInputView remoteInputView = this.arg0;
                    ExpandableNotificationRowComponentImpl expandableNotificationRowComponentImpl = ExpandableNotificationRowComponentImpl.this;
                    return new RemoteInputViewControllerImpl(remoteInputView, expandableNotificationRowComponentImpl.notificationEntry, TitanSysUIComponentImpl.this.remoteInputQuickSettingsDisablerProvider.mo144get(), this.arg1, DaggerTitanGlobalRootComponent.this.provideShortcutManagerProvider.mo144get(), DaggerTitanGlobalRootComponent.this.provideUiEventLoggerProvider.mo144get());
                }
            }

            /* JADX WARN: Type inference failed for: r3v2, types: [com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl$ExpandableNotificationRowComponentImpl$1] */
            public ExpandableNotificationRowComponentImpl(ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry, ExpandableNotificationRow.OnExpandClickListener onExpandClickListener, NotificationListContainer notificationListContainer) {
                this.notificationEntry = notificationEntry;
                this.expandableNotificationRowProvider = InstanceFactory.create(expandableNotificationRow);
                this.listContainerProvider = InstanceFactory.create(notificationListContainer);
                this.factoryProvider = new NotificationTapHelper_Factory_Factory(TitanSysUIComponentImpl.this.falsingManagerProxyProvider, DaggerTitanGlobalRootComponent.this.provideMainDelayableExecutorProvider);
                KeyboardUI_Factory create$8 = KeyboardUI_Factory.create$8(this.expandableNotificationRowProvider);
                this.expandableViewControllerProvider = create$8;
                UnpinNotifications_Factory create = UnpinNotifications_Factory.create(this.expandableNotificationRowProvider, create$8);
                this.expandableOutlineViewControllerProvider = create;
                this.activatableNotificationViewControllerProvider = ActivatableNotificationViewController_Factory.create(this.expandableNotificationRowProvider, this.factoryProvider, create, DaggerTitanGlobalRootComponent.this.provideAccessibilityManagerProvider, TitanSysUIComponentImpl.this.falsingManagerProxyProvider, TitanSysUIComponentImpl.this.falsingCollectorImplProvider);
                InstanceFactory create2 = InstanceFactory.create(notificationEntry);
                this.notificationEntryProvider = create2;
                ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory expandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory = new ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory(create2);
                this.provideStatusBarNotificationProvider = expandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory;
                this.provideAppNameProvider = new ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideAppNameFactory(DaggerTitanGlobalRootComponent.this.contextProvider, expandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory);
                this.provideNotificationKeyProvider = new ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideNotificationKeyFactory(expandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory);
                InstanceFactory create3 = InstanceFactory.create(onExpandClickListener);
                this.onExpandClickListenerProvider = create3;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
                Provider<Context> provider = daggerTitanGlobalRootComponent.contextProvider;
                Provider<HeadsUpManagerPhone> provider2 = TitanSysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider;
                ControlsActivity_Factory controlsActivity_Factory = new ControlsActivity_Factory(provider, provider2, 2);
                this.expandableNotificationRowDragControllerProvider = controlsActivity_Factory;
                this.expandableNotificationRowControllerProvider = DoubleCheck.provider(ExpandableNotificationRowController_Factory.create(this.expandableNotificationRowProvider, this.listContainerProvider, this.remoteInputViewSubcomponentFactoryProvider, this.activatableNotificationViewControllerProvider, TitanSysUIComponentImpl.this.provideNotificationMediaManagerProvider, daggerTitanGlobalRootComponent.providesPluginManagerProvider, TitanSysUIComponentImpl.this.bindSystemClockProvider, this.provideAppNameProvider, this.provideNotificationKeyProvider, TitanSysUIComponentImpl.this.keyguardBypassControllerProvider, TitanSysUIComponentImpl.this.provideGroupMembershipManagerProvider, TitanSysUIComponentImpl.this.provideGroupExpansionManagerProvider, TitanSysUIComponentImpl.this.rowContentBindStageProvider, TitanSysUIComponentImpl.this.provideNotificationLoggerProvider, provider2, create3, TitanSysUIComponentImpl.this.statusBarStateControllerImplProvider, TitanSysUIComponentImpl.this.provideNotificationGutsManagerProvider, TitanSysUIComponentImpl.this.provideAllowNotificationLongPressProvider, TitanSysUIComponentImpl.this.provideOnUserInteractionCallbackProvider, TitanSysUIComponentImpl.this.falsingManagerProxyProvider, TitanSysUIComponentImpl.this.falsingCollectorImplProvider, TitanSysUIComponentImpl.this.featureFlagsReleaseProvider, TitanSysUIComponentImpl.this.peopleNotificationIdentifierImplProvider, TitanSysUIComponentImpl.this.provideBubblesManagerProvider, controlsActivity_Factory));
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent
            public final ExpandableNotificationRowController getExpandableNotificationRowController() {
                return this.expandableNotificationRowControllerProvider.mo144get();
            }
        }

        /* loaded from: classes.dex */
        public final class FragmentCreatorFactory implements FragmentService.FragmentCreator.Factory {
            public FragmentCreatorFactory() {
            }

            @Override // com.android.systemui.fragments.FragmentService.FragmentCreator.Factory
            public final FragmentService.FragmentCreator build() {
                return new FragmentCreatorImpl();
            }
        }

        /* loaded from: classes.dex */
        public final class FragmentCreatorImpl implements FragmentService.FragmentCreator {
            public FragmentCreatorImpl() {
            }

            @Override // com.android.systemui.fragments.FragmentService.FragmentCreator
            public final QSFragment createQSFragment() {
                RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler = TitanSysUIComponentImpl.this.remoteInputQuickSettingsDisablerProvider.mo144get();
                TitanSysUIComponentImpl.this.qSTileHostProvider.mo144get();
                StatusBarStateControllerImpl statusBarStateControllerImpl = TitanSysUIComponentImpl.this.statusBarStateControllerImplProvider.mo144get();
                CommandQueue commandQueue = TitanSysUIComponentImpl.this.provideCommandQueueProvider.mo144get();
                MediaHost mediaHost = TitanSysUIComponentImpl.this.providesQSMediaHostProvider.mo144get();
                MediaHost mediaHost2 = TitanSysUIComponentImpl.this.providesQuickQSMediaHostProvider.mo144get();
                KeyguardBypassController keyguardBypassController = TitanSysUIComponentImpl.this.keyguardBypassControllerProvider.mo144get();
                TitanSysUIComponentImpl titanSysUIComponentImpl = TitanSysUIComponentImpl.this;
                return new QSFragment(remoteInputQuickSettingsDisabler, statusBarStateControllerImpl, commandQueue, mediaHost, mediaHost2, keyguardBypassController, new QSFragmentComponentFactory(), new QSFragmentDisableFlagsLogger(titanSysUIComponentImpl.provideQSFragmentDisableLogBufferProvider.mo144get(), TitanSysUIComponentImpl.this.disableFlagsLoggerProvider.mo144get()), TitanSysUIComponentImpl.this.falsingManagerProxyProvider.mo144get(), DaggerTitanGlobalRootComponent.this.dumpManagerProvider.mo144get());
            }
        }

        /* loaded from: classes.dex */
        public final class InputSessionComponentFactory implements InputSessionComponent.Factory {
            public InputSessionComponentFactory() {
            }

            @Override // com.android.systemui.dreams.touch.dagger.InputSessionComponent.Factory
            public final InputSessionComponent create(String str, InputChannelCompat$InputEventListener inputChannelCompat$InputEventListener, GestureDetector.OnGestureListener onGestureListener, boolean z) {
                Objects.requireNonNull(inputChannelCompat$InputEventListener);
                Objects.requireNonNull(onGestureListener);
                Boolean bool = Boolean.TRUE;
                Objects.requireNonNull(bool);
                return new InputSessionComponentImpl((DreamOverlayTouchMonitor.AnonymousClass2) inputChannelCompat$InputEventListener, (DreamOverlayTouchMonitor.AnonymousClass3) onGestureListener, bool);
            }
        }

        /* loaded from: classes.dex */
        public final class InputSessionComponentImpl implements InputSessionComponent {
            public final String arg0 = "dreamOverlay";
            public final InputChannelCompat$InputEventListener arg1;
            public final GestureDetector.OnGestureListener arg2;
            public final Boolean arg3;

            @Override // com.android.systemui.dreams.touch.dagger.InputSessionComponent
            public final InputSession getInputSession() {
                return new InputSession(this.arg0, this.arg1, this.arg2, this.arg3.booleanValue());
            }

            public InputSessionComponentImpl(DreamOverlayTouchMonitor.AnonymousClass2 r2, DreamOverlayTouchMonitor.AnonymousClass3 r3, Boolean bool) {
                this.arg1 = r2;
                this.arg2 = r3;
                this.arg3 = bool;
            }
        }

        /* loaded from: classes.dex */
        public final class KeyguardBouncerComponentFactory implements KeyguardBouncerComponent.Factory {
            public KeyguardBouncerComponentFactory() {
            }

            @Override // com.android.keyguard.dagger.KeyguardBouncerComponent.Factory
            public final KeyguardBouncerComponent create(ViewGroup viewGroup) {
                Objects.requireNonNull(viewGroup);
                return new KeyguardBouncerComponentImpl(TitanSysUIComponentImpl.this, viewGroup);
            }
        }

        /* loaded from: classes.dex */
        public final class KeyguardBouncerComponentImpl implements KeyguardBouncerComponent {
            public InstanceFactory arg0Provider;
            public Provider<AdminSecondaryLockScreenController.Factory> factoryProvider;
            public EmergencyButtonController_Factory_Factory factoryProvider2;
            public KeyguardInputViewController_Factory_Factory factoryProvider3;
            public KeyguardSecurityContainerController_Factory_Factory factoryProvider4;
            public Provider<KeyguardHostViewController> keyguardHostViewControllerProvider;
            public Provider<KeyguardSecurityViewFlipperController> keyguardSecurityViewFlipperControllerProvider;
            public LiftToActivateListener_Factory liftToActivateListenerProvider;
            public Provider<KeyguardHostView> providesKeyguardHostViewProvider;
            public Provider<KeyguardSecurityContainer> providesKeyguardSecurityContainerProvider;
            public Provider<KeyguardSecurityViewFlipper> providesKeyguardSecurityViewFlipperProvider;

            public KeyguardBouncerComponentImpl(TitanSysUIComponentImpl titanSysUIComponentImpl, ViewGroup viewGroup) {
                InstanceFactory create = InstanceFactory.create(viewGroup);
                this.arg0Provider = create;
                Provider<KeyguardHostView> provider = DoubleCheck.provider(new QSFlagsModule_IsPMLiteEnabledFactory(create, titanSysUIComponentImpl.providerLayoutInflaterProvider, 1));
                this.providesKeyguardHostViewProvider = provider;
                Provider<KeyguardSecurityContainer> m = DaggerGlobalRootComponent$SysUIComponentImpl$KeyguardBouncerComponentImpl$$ExternalSyntheticOutline1.m(provider, 1);
                this.providesKeyguardSecurityContainerProvider = m;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
                this.factoryProvider = DoubleCheck.provider(new AdminSecondaryLockScreenController_Factory_Factory(daggerTitanGlobalRootComponent.contextProvider, m, titanSysUIComponentImpl.keyguardUpdateMonitorProvider, daggerTitanGlobalRootComponent.provideMainHandlerProvider));
                this.providesKeyguardSecurityViewFlipperProvider = DaggerGlobalRootComponent$SysUIComponentImpl$KeyguardBouncerComponentImpl$$ExternalSyntheticOutline0.m(this.providesKeyguardSecurityContainerProvider, 1);
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent2 = DaggerTitanGlobalRootComponent.this;
                this.liftToActivateListenerProvider = new LiftToActivateListener_Factory(daggerTitanGlobalRootComponent2.provideAccessibilityManagerProvider, 0);
                EmergencyButtonController_Factory_Factory create2 = EmergencyButtonController_Factory_Factory.create(titanSysUIComponentImpl.provideConfigurationControllerProvider, titanSysUIComponentImpl.keyguardUpdateMonitorProvider, daggerTitanGlobalRootComponent2.provideTelephonyManagerProvider, daggerTitanGlobalRootComponent2.providePowerManagerProvider, daggerTitanGlobalRootComponent2.provideActivityTaskManagerProvider, titanSysUIComponentImpl.shadeControllerImplProvider, daggerTitanGlobalRootComponent2.provideTelecomManagerProvider, titanSysUIComponentImpl.provideMetricsLoggerProvider);
                this.factoryProvider2 = create2;
                Provider<KeyguardUpdateMonitor> provider2 = titanSysUIComponentImpl.keyguardUpdateMonitorProvider;
                Provider<LockPatternUtils> provider3 = titanSysUIComponentImpl.provideLockPatternUtilsProvider;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent3 = DaggerTitanGlobalRootComponent.this;
                KeyguardInputViewController_Factory_Factory create3 = KeyguardInputViewController_Factory_Factory.create(provider2, provider3, daggerTitanGlobalRootComponent3.provideLatencyTrackerProvider, titanSysUIComponentImpl.factoryProvider5, daggerTitanGlobalRootComponent3.provideInputMethodManagerProvider, daggerTitanGlobalRootComponent3.provideMainDelayableExecutorProvider, daggerTitanGlobalRootComponent3.provideResourcesProvider, this.liftToActivateListenerProvider, daggerTitanGlobalRootComponent3.provideTelephonyManagerProvider, titanSysUIComponentImpl.falsingCollectorImplProvider, create2, titanSysUIComponentImpl.devicePostureControllerImplProvider);
                this.factoryProvider3 = create3;
                Provider<KeyguardSecurityViewFlipperController> provider4 = DoubleCheck.provider(new KeyguardSecurityViewFlipperController_Factory(this.providesKeyguardSecurityViewFlipperProvider, titanSysUIComponentImpl.providerLayoutInflaterProvider, create3, this.factoryProvider2));
                this.keyguardSecurityViewFlipperControllerProvider = provider4;
                KeyguardSecurityContainerController_Factory_Factory create4 = KeyguardSecurityContainerController_Factory_Factory.create(this.providesKeyguardSecurityContainerProvider, this.factoryProvider, titanSysUIComponentImpl.provideLockPatternUtilsProvider, titanSysUIComponentImpl.keyguardUpdateMonitorProvider, titanSysUIComponentImpl.keyguardSecurityModelProvider, titanSysUIComponentImpl.provideMetricsLoggerProvider, DaggerTitanGlobalRootComponent.this.provideUiEventLoggerProvider, titanSysUIComponentImpl.keyguardStateControllerImplProvider, provider4, titanSysUIComponentImpl.provideConfigurationControllerProvider, titanSysUIComponentImpl.falsingCollectorImplProvider, titanSysUIComponentImpl.falsingManagerProxyProvider, titanSysUIComponentImpl.userSwitcherControllerProvider, titanSysUIComponentImpl.featureFlagsReleaseProvider, titanSysUIComponentImpl.globalSettingsImplProvider, titanSysUIComponentImpl.sessionTrackerProvider);
                this.factoryProvider4 = create4;
                Provider<KeyguardHostView> provider5 = this.providesKeyguardHostViewProvider;
                Provider<KeyguardUpdateMonitor> provider6 = titanSysUIComponentImpl.keyguardUpdateMonitorProvider;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent4 = DaggerTitanGlobalRootComponent.this;
                this.keyguardHostViewControllerProvider = DoubleCheck.provider(KeyguardHostViewController_Factory.create(provider5, provider6, daggerTitanGlobalRootComponent4.provideAudioManagerProvider, daggerTitanGlobalRootComponent4.provideTelephonyManagerProvider, titanSysUIComponentImpl.providesViewMediatorCallbackProvider, create4));
            }

            @Override // com.android.keyguard.dagger.KeyguardBouncerComponent
            public final KeyguardHostViewController getKeyguardHostViewController() {
                return this.keyguardHostViewControllerProvider.mo144get();
            }
        }

        /* loaded from: classes.dex */
        public final class KeyguardQsUserSwitchComponentFactory implements KeyguardQsUserSwitchComponent.Factory {
            public KeyguardQsUserSwitchComponentFactory() {
            }

            @Override // com.android.keyguard.dagger.KeyguardQsUserSwitchComponent.Factory
            public final KeyguardQsUserSwitchComponent build(FrameLayout frameLayout) {
                Objects.requireNonNull(frameLayout);
                return new KeyguardQsUserSwitchComponentImpl(TitanSysUIComponentImpl.this, frameLayout);
            }
        }

        /* loaded from: classes.dex */
        public final class KeyguardQsUserSwitchComponentImpl implements KeyguardQsUserSwitchComponent {
            public InstanceFactory arg0Provider;
            public Provider<KeyguardQsUserSwitchController> keyguardQsUserSwitchControllerProvider;

            public KeyguardQsUserSwitchComponentImpl(TitanSysUIComponentImpl titanSysUIComponentImpl, FrameLayout frameLayout) {
                InstanceFactory create = InstanceFactory.create(frameLayout);
                this.arg0Provider = create;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
                this.keyguardQsUserSwitchControllerProvider = DoubleCheck.provider(KeyguardQsUserSwitchController_Factory.create(create, daggerTitanGlobalRootComponent.contextProvider, daggerTitanGlobalRootComponent.provideResourcesProvider, daggerTitanGlobalRootComponent.screenLifecycleProvider, titanSysUIComponentImpl.userSwitcherControllerProvider, titanSysUIComponentImpl.communalStateControllerProvider, titanSysUIComponentImpl.keyguardStateControllerImplProvider, titanSysUIComponentImpl.falsingManagerProxyProvider, titanSysUIComponentImpl.provideConfigurationControllerProvider, titanSysUIComponentImpl.statusBarStateControllerImplProvider, titanSysUIComponentImpl.dozeParametersProvider, titanSysUIComponentImpl.screenOffAnimationControllerProvider, titanSysUIComponentImpl.userSwitchDialogControllerProvider, daggerTitanGlobalRootComponent.provideUiEventLoggerProvider));
            }

            @Override // com.android.keyguard.dagger.KeyguardQsUserSwitchComponent
            public final KeyguardQsUserSwitchController getKeyguardQsUserSwitchController() {
                return this.keyguardQsUserSwitchControllerProvider.mo144get();
            }
        }

        /* loaded from: classes.dex */
        public final class KeyguardStatusBarViewComponentFactory implements KeyguardStatusBarViewComponent.Factory {
            public KeyguardStatusBarViewComponentFactory() {
            }

            @Override // com.android.keyguard.dagger.KeyguardStatusBarViewComponent.Factory
            public final KeyguardStatusBarViewComponent build(KeyguardStatusBarView keyguardStatusBarView, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider) {
                Objects.requireNonNull(keyguardStatusBarView);
                Objects.requireNonNull(notificationPanelViewStateProvider);
                return new KeyguardStatusBarViewComponentImpl(keyguardStatusBarView, notificationPanelViewStateProvider);
            }
        }

        /* loaded from: classes.dex */
        public final class KeyguardStatusBarViewComponentImpl implements KeyguardStatusBarViewComponent {
            public final KeyguardStatusBarView arg0;
            public InstanceFactory arg0Provider;
            public final NotificationPanelViewController.NotificationPanelViewStateProvider arg1;
            public Provider<StatusBarUserSwitcherController> bindStatusBarUserSwitcherControllerProvider;
            public Provider<BatteryMeterView> getBatteryMeterViewProvider;
            public Provider<CarrierText> getCarrierTextProvider;
            public Provider<StatusBarUserSwitcherContainer> getUserSwitcherContainerProvider;
            public ClockManager_Factory statusBarUserSwitcherControllerImplProvider;

            public KeyguardStatusBarViewComponentImpl(KeyguardStatusBarView keyguardStatusBarView, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider) {
                this.arg0 = keyguardStatusBarView;
                this.arg1 = notificationPanelViewStateProvider;
                InstanceFactory create = InstanceFactory.create(keyguardStatusBarView);
                this.arg0Provider = create;
                this.getCarrierTextProvider = DoubleCheck.provider(new QSFragmentModule_ProvideThemedContextFactory(create, 1));
                this.getBatteryMeterViewProvider = DoubleCheck.provider(new QSFragmentModule_ProvideRootViewFactory(this.arg0Provider, 1));
                Provider<StatusBarUserSwitcherContainer> provider = DoubleCheck.provider(new LogModule_ProvidePrivacyLogBufferFactory(this.arg0Provider, 1));
                this.getUserSwitcherContainerProvider = provider;
                ClockManager_Factory create$1 = ClockManager_Factory.create$1(provider, TitanSysUIComponentImpl.this.statusBarUserInfoTrackerProvider, TitanSysUIComponentImpl.this.statusBarUserSwitcherFeatureControllerProvider, TitanSysUIComponentImpl.this.userSwitchDialogControllerProvider, TitanSysUIComponentImpl.this.featureFlagsReleaseProvider, TitanSysUIComponentImpl.this.provideActivityStarterProvider);
                this.statusBarUserSwitcherControllerImplProvider = create$1;
                this.bindStatusBarUserSwitcherControllerProvider = DoubleCheck.provider(create$1);
            }

            @Override // com.android.keyguard.dagger.KeyguardStatusBarViewComponent
            public final KeyguardStatusBarViewController getKeyguardStatusBarViewController() {
                KeyguardStatusBarView keyguardStatusBarView = this.arg0;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
                BatteryMeterView batteryMeterView = this.getBatteryMeterViewProvider.mo144get();
                ConfigurationController configurationController = TitanSysUIComponentImpl.this.provideConfigurationControllerProvider.mo144get();
                TunerServiceImpl tunerServiceImpl = TitanSysUIComponentImpl.this.tunerServiceImplProvider.mo144get();
                BroadcastDispatcher broadcastDispatcher = TitanSysUIComponentImpl.this.providesBroadcastDispatcherProvider.mo144get();
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent2 = DaggerTitanGlobalRootComponent.this;
                Provider provider = DaggerTitanGlobalRootComponent.ABSENT_JDK_OPTIONAL_PROVIDER;
                return new KeyguardStatusBarViewController(keyguardStatusBarView, new CarrierTextController(this.getCarrierTextProvider.mo144get(), new CarrierTextManager.Builder(daggerTitanGlobalRootComponent.context, daggerTitanGlobalRootComponent.mainResources(), DaggerTitanGlobalRootComponent.this.provideWifiManagerProvider.mo144get(), DaggerTitanGlobalRootComponent.this.provideTelephonyManagerProvider.mo144get(), TitanSysUIComponentImpl.this.telephonyListenerManagerProvider.mo144get(), TitanSysUIComponentImpl.this.wakefulnessLifecycleProvider.mo144get(), DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider.mo144get(), TitanSysUIComponentImpl.this.provideBackgroundExecutorProvider.mo144get(), TitanSysUIComponentImpl.this.keyguardUpdateMonitorProvider.mo144get()), TitanSysUIComponentImpl.this.keyguardUpdateMonitorProvider.mo144get()), TitanSysUIComponentImpl.this.provideConfigurationControllerProvider.mo144get(), TitanSysUIComponentImpl.this.systemStatusAnimationSchedulerProvider.mo144get(), TitanSysUIComponentImpl.this.provideBatteryControllerProvider.mo144get(), TitanSysUIComponentImpl.this.userInfoControllerImplProvider.mo144get(), TitanSysUIComponentImpl.this.statusBarIconControllerImplProvider.mo144get(), TitanSysUIComponentImpl.this.factoryProvider10.mo144get(), new BatteryMeterViewController(batteryMeterView, configurationController, tunerServiceImpl, broadcastDispatcher, daggerTitanGlobalRootComponent2.mainHandler(), DaggerTitanGlobalRootComponent.this.provideContentResolverProvider.mo144get(), TitanSysUIComponentImpl.this.provideBatteryControllerProvider.mo144get()), this.arg1, TitanSysUIComponentImpl.this.keyguardStateControllerImplProvider.mo144get(), TitanSysUIComponentImpl.this.keyguardBypassControllerProvider.mo144get(), TitanSysUIComponentImpl.this.keyguardUpdateMonitorProvider.mo144get(), TitanSysUIComponentImpl.this.biometricUnlockControllerProvider.mo144get(), TitanSysUIComponentImpl.this.statusBarStateControllerImplProvider.mo144get(), TitanSysUIComponentImpl.this.statusBarContentInsetsProvider.mo144get(), DaggerTitanGlobalRootComponent.this.provideUserManagerProvider.mo144get(), TitanSysUIComponentImpl.this.statusBarUserSwitcherFeatureControllerProvider.mo144get(), this.bindStatusBarUserSwitcherControllerProvider.mo144get(), TitanSysUIComponentImpl.this.statusBarUserInfoTrackerProvider.mo144get());
            }
        }

        /* loaded from: classes.dex */
        public final class KeyguardStatusViewComponentFactory implements KeyguardStatusViewComponent.Factory {
            public KeyguardStatusViewComponentFactory() {
            }

            @Override // com.android.keyguard.dagger.KeyguardStatusViewComponent.Factory
            public final KeyguardStatusViewComponent build(KeyguardStatusView keyguardStatusView) {
                Objects.requireNonNull(keyguardStatusView);
                return new KeyguardStatusViewComponentImpl(keyguardStatusView);
            }
        }

        /* loaded from: classes.dex */
        public final class KeyguardStatusViewComponentImpl implements KeyguardStatusViewComponent {
            public final KeyguardStatusView arg0;
            public InstanceFactory arg0Provider;
            public VrMode_Factory getKeyguardClockSwitchProvider;
            public KeyboardUI_Factory getKeyguardSliceViewProvider;
            public Provider<KeyguardSliceViewController> keyguardSliceViewControllerProvider;

            public KeyguardStatusViewComponentImpl(KeyguardStatusView keyguardStatusView) {
                this.arg0 = keyguardStatusView;
                InstanceFactory create = InstanceFactory.create(keyguardStatusView);
                this.arg0Provider = create;
                VrMode_Factory vrMode_Factory = new VrMode_Factory(create, 1);
                this.getKeyguardClockSwitchProvider = vrMode_Factory;
                KeyboardUI_Factory keyboardUI_Factory = new KeyboardUI_Factory(vrMode_Factory, 1);
                this.getKeyguardSliceViewProvider = keyboardUI_Factory;
                this.keyguardSliceViewControllerProvider = DoubleCheck.provider(KeyguardSliceViewController_Factory.create(keyboardUI_Factory, TitanSysUIComponentImpl.this.provideActivityStarterProvider, TitanSysUIComponentImpl.this.provideConfigurationControllerProvider, TitanSysUIComponentImpl.this.tunerServiceImplProvider, DaggerTitanGlobalRootComponent.this.dumpManagerProvider));
            }

            @Override // com.android.keyguard.dagger.KeyguardStatusViewComponent
            public final KeyguardClockSwitchController getKeyguardClockSwitchController() {
                TitanSysUIComponentImpl.this.keyguardBypassControllerProvider.mo144get();
                return new KeyguardClockSwitchController(VrMode_Factory.getKeyguardClockSwitch(this.arg0), TitanSysUIComponentImpl.this.statusBarStateControllerImplProvider.mo144get(), TitanSysUIComponentImpl.this.sysuiColorExtractorProvider.mo144get(), TitanSysUIComponentImpl.this.clockManagerProvider.mo144get(), this.keyguardSliceViewControllerProvider.mo144get(), TitanSysUIComponentImpl.this.notificationIconAreaControllerProvider.mo144get(), TitanSysUIComponentImpl.this.providesBroadcastDispatcherProvider.mo144get(), TitanSysUIComponentImpl.this.provideBatteryControllerProvider.mo144get(), TitanSysUIComponentImpl.this.keyguardUpdateMonitorProvider.mo144get(), TitanSysUIComponentImpl.this.lockscreenSmartspaceControllerProvider.mo144get(), TitanSysUIComponentImpl.this.keyguardUnlockAnimationControllerProvider.mo144get(), (SecureSettings) TitanSysUIComponentImpl.this.secureSettingsImpl(), DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider.mo144get(), DaggerTitanGlobalRootComponent.this.mainResources());
            }

            @Override // com.android.keyguard.dagger.KeyguardStatusViewComponent
            public final KeyguardStatusViewController getKeyguardStatusViewController() {
                KeyguardStatusView keyguardStatusView = this.arg0;
                KeyguardSliceViewController keyguardSliceViewController = this.keyguardSliceViewControllerProvider.mo144get();
                KeyguardClockSwitchController keyguardClockSwitchController = getKeyguardClockSwitchController();
                KeyguardStateControllerImpl keyguardStateControllerImpl = TitanSysUIComponentImpl.this.keyguardStateControllerImplProvider.mo144get();
                KeyguardUpdateMonitor keyguardUpdateMonitor = TitanSysUIComponentImpl.this.keyguardUpdateMonitorProvider.mo144get();
                CommunalStateController communalStateController = TitanSysUIComponentImpl.this.communalStateControllerProvider.mo144get();
                ConfigurationController configurationController = TitanSysUIComponentImpl.this.provideConfigurationControllerProvider.mo144get();
                TitanSysUIComponentImpl.this.dozeParametersProvider.mo144get();
                return new KeyguardStatusViewController(keyguardStatusView, keyguardSliceViewController, keyguardClockSwitchController, keyguardStateControllerImpl, keyguardUpdateMonitor, communalStateController, configurationController, TitanSysUIComponentImpl.this.keyguardUnlockAnimationControllerProvider.mo144get(), TitanSysUIComponentImpl.this.screenOffAnimationControllerProvider.mo144get());
            }
        }

        /* loaded from: classes.dex */
        public final class KeyguardUserSwitcherComponentFactory implements KeyguardUserSwitcherComponent.Factory {
            public KeyguardUserSwitcherComponentFactory() {
            }

            @Override // com.android.keyguard.dagger.KeyguardUserSwitcherComponent.Factory
            public final KeyguardUserSwitcherComponent build(KeyguardUserSwitcherView keyguardUserSwitcherView) {
                Objects.requireNonNull(keyguardUserSwitcherView);
                return new KeyguardUserSwitcherComponentImpl(TitanSysUIComponentImpl.this, keyguardUserSwitcherView);
            }
        }

        /* loaded from: classes.dex */
        public final class KeyguardUserSwitcherComponentImpl implements KeyguardUserSwitcherComponent {
            public InstanceFactory arg0Provider;
            public Provider<KeyguardUserSwitcherController> keyguardUserSwitcherControllerProvider;

            @Override // com.android.keyguard.dagger.KeyguardUserSwitcherComponent
            public final KeyguardUserSwitcherController getKeyguardUserSwitcherController() {
                return this.keyguardUserSwitcherControllerProvider.mo144get();
            }

            public KeyguardUserSwitcherComponentImpl(TitanSysUIComponentImpl titanSysUIComponentImpl, KeyguardUserSwitcherView keyguardUserSwitcherView) {
                InstanceFactory create = InstanceFactory.create(keyguardUserSwitcherView);
                this.arg0Provider = create;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
                this.keyguardUserSwitcherControllerProvider = DoubleCheck.provider(KeyguardUserSwitcherController_Factory.create(create, daggerTitanGlobalRootComponent.contextProvider, daggerTitanGlobalRootComponent.provideResourcesProvider, titanSysUIComponentImpl.providerLayoutInflaterProvider, daggerTitanGlobalRootComponent.screenLifecycleProvider, titanSysUIComponentImpl.userSwitcherControllerProvider, titanSysUIComponentImpl.communalStateControllerProvider, titanSysUIComponentImpl.keyguardStateControllerImplProvider, titanSysUIComponentImpl.statusBarStateControllerImplProvider, titanSysUIComponentImpl.keyguardUpdateMonitorProvider, titanSysUIComponentImpl.dozeParametersProvider, titanSysUIComponentImpl.screenOffAnimationControllerProvider));
            }
        }

        /* loaded from: classes.dex */
        public final class MediaComplicationComponentFactory implements MediaComplicationComponent$Factory {
            public MediaComplicationComponentFactory() {
            }

            @Override // com.android.systemui.media.dream.dagger.MediaComplicationComponent$Factory
            public final MediaComplicationComponentImpl create() {
                return new MediaComplicationComponentImpl();
            }
        }

        /* loaded from: classes.dex */
        public final class MediaComplicationComponentImpl {
            public Provider<FrameLayout> provideComplicationContainerProvider;
            public Provider<ComplicationLayoutParams> provideLayoutParamsProvider = DoubleCheck.provider(MediaComplicationComponent_MediaComplicationModule_ProvideLayoutParamsFactory.InstanceHolder.INSTANCE);

            public MediaComplicationComponentImpl() {
                this.provideComplicationContainerProvider = DoubleCheck.provider(new MediaComplicationComponent_MediaComplicationModule_ProvideComplicationContainerFactory(DaggerTitanGlobalRootComponent.this.contextProvider));
            }
        }

        /* loaded from: classes.dex */
        public final class MediaShellComponentFactory implements MediaShellComponent$Factory {
            public MediaShellComponentFactory() {
            }

            @Override // com.google.android.systemui.communal.dock.callbacks.mediashell.dagger.MediaShellComponent$Factory
            public final MediaShellComponentImpl create(MediaShellCallback.AnonymousClass1 r2) {
                return new MediaShellComponentImpl(r2);
            }
        }

        /* loaded from: classes.dex */
        public final class MediaShellComponentImpl {
            public final ObservableServiceConnection.Callback<IBinder> callback;

            public MediaShellComponentImpl(MediaShellCallback.AnonymousClass1 r2) {
                this.callback = r2;
            }
        }

        /* loaded from: classes.dex */
        public final class MonitorComponentFactory implements MonitorComponent.Factory {
            public MonitorComponentFactory() {
            }

            @Override // com.android.systemui.util.condition.dagger.MonitorComponent.Factory
            public final MonitorComponent create(Set<Condition> set, Set<Monitor.Callback> set2) {
                Objects.requireNonNull(set);
                Objects.requireNonNull(set2);
                return new MonitorComponentImpl(set, set2);
            }
        }

        /* loaded from: classes.dex */
        public final class MonitorComponentImpl implements MonitorComponent {
            public final Set<Condition> arg0;
            public final Set<Monitor.Callback> arg1;

            public MonitorComponentImpl(Set set, Set set2) {
                this.arg0 = set;
                this.arg1 = set2;
            }

            @Override // com.android.systemui.util.condition.dagger.MonitorComponent
            public final Monitor getMonitor() {
                return new Monitor(TitanSysUIComponentImpl.this.provideExecutorProvider.mo144get(), this.arg0, this.arg1);
            }
        }

        /* loaded from: classes.dex */
        public final class NotificationShelfComponentBuilder implements NotificationShelfComponent.Builder {
            public NotificationShelf notificationShelf;

            public NotificationShelfComponentBuilder() {
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent.Builder
            public final NotificationShelfComponent build() {
                R$color.checkBuilderRequirement(this.notificationShelf, NotificationShelf.class);
                return new NotificationShelfComponentImpl(TitanSysUIComponentImpl.this, this.notificationShelf);
            }

            @Override // com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent.Builder
            /* renamed from: notificationShelf */
            public final NotificationShelfComponent.Builder mo140notificationShelf(NotificationShelf notificationShelf) {
                Objects.requireNonNull(notificationShelf);
                this.notificationShelf = notificationShelf;
                return this;
            }
        }

        /* loaded from: classes.dex */
        public final class NotificationShelfComponentImpl implements NotificationShelfComponent {
            public ActivatableNotificationViewController_Factory activatableNotificationViewControllerProvider;
            public UnpinNotifications_Factory expandableOutlineViewControllerProvider;
            public KeyboardUI_Factory expandableViewControllerProvider;
            public NotificationTapHelper_Factory_Factory factoryProvider;
            public Provider<NotificationShelfController> notificationShelfControllerProvider;
            public InstanceFactory notificationShelfProvider;

            @Override // com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent
            public final NotificationShelfController getNotificationShelfController() {
                return this.notificationShelfControllerProvider.mo144get();
            }

            public NotificationShelfComponentImpl(TitanSysUIComponentImpl titanSysUIComponentImpl, NotificationShelf notificationShelf) {
                InstanceFactory create = InstanceFactory.create(notificationShelf);
                this.notificationShelfProvider = create;
                this.factoryProvider = new NotificationTapHelper_Factory_Factory(titanSysUIComponentImpl.falsingManagerProxyProvider, DaggerTitanGlobalRootComponent.this.provideMainDelayableExecutorProvider);
                KeyboardUI_Factory create$8 = KeyboardUI_Factory.create$8(create);
                this.expandableViewControllerProvider = create$8;
                UnpinNotifications_Factory create2 = UnpinNotifications_Factory.create(this.notificationShelfProvider, create$8);
                this.expandableOutlineViewControllerProvider = create2;
                ActivatableNotificationViewController_Factory create3 = ActivatableNotificationViewController_Factory.create(this.notificationShelfProvider, this.factoryProvider, create2, DaggerTitanGlobalRootComponent.this.provideAccessibilityManagerProvider, titanSysUIComponentImpl.falsingManagerProxyProvider, titanSysUIComponentImpl.falsingCollectorImplProvider);
                this.activatableNotificationViewControllerProvider = create3;
                this.notificationShelfControllerProvider = DoubleCheck.provider(new LogBufferEulogizer_Factory(this.notificationShelfProvider, create3, titanSysUIComponentImpl.keyguardBypassControllerProvider, titanSysUIComponentImpl.statusBarStateControllerImplProvider, 1));
            }
        }

        /* loaded from: classes.dex */
        public final class QSFragmentComponentFactory implements QSFragmentComponent.Factory {
            public QSFragmentComponentFactory() {
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent.Factory
            public final QSFragmentComponent create(QSFragment qSFragment) {
                Objects.requireNonNull(qSFragment);
                return new QSFragmentComponentImpl(TitanSysUIComponentImpl.this, qSFragment);
            }
        }

        /* loaded from: classes.dex */
        public final class QSFragmentComponentImpl implements QSFragmentComponent {
            public InstanceFactory arg0Provider;
            public BatteryMeterViewController_Factory batteryMeterViewControllerProvider;
            public CarrierTextManager_Builder_Factory builderProvider;
            public QSCarrierGroupController_Builder_Factory builderProvider2;
            public Provider factoryProvider;
            public BrightnessController_Factory_Factory factoryProvider2;
            public VariableDateViewController_Factory_Factory factoryProvider3;
            public Provider<MultiUserSwitchController.Factory> factoryProvider4;
            public Provider<FooterActionsController> footerActionsControllerProvider;
            public HeaderPrivacyIconsController_Factory headerPrivacyIconsControllerProvider;
            public QSFragmentModule_ProvideQSPanelFactory provideQSPanelProvider;
            public QSFragmentModule_ProvideRootViewFactory provideRootViewProvider;
            public QSFragmentModule_ProvideThemedContextFactory provideThemedContextProvider;
            public LogModule_ProvidePrivacyLogBufferFactory provideThemedLayoutInflaterProvider;
            public VrMode_Factory providesBatteryMeterViewProvider;
            public Provider<OngoingPrivacyChip> providesPrivacyChipProvider;
            public DependencyProvider_ProvideHandlerFactory providesQSContainerImplProvider;
            public Provider<QSCustomizer> providesQSCutomizerProvider;
            public Provider<View> providesQSFgsManagerFooterViewProvider;
            public QSFragmentModule_ProvidesQSFooterActionsViewFactory providesQSFooterActionsViewProvider;
            public Provider<QSFooter> providesQSFooterProvider;
            public SecureSettingsImpl_Factory providesQSFooterViewProvider;
            public Provider<View> providesQSSecurityFooterViewProvider;
            public PrivacyLogger_Factory providesQSUsingCollapsedLandscapeMediaProvider;
            public MediaBrowserFactory_Factory providesQSUsingMediaPlayerProvider;
            public QSFragmentModule_ProvidesQuickQSPanelFactory providesQuickQSPanelProvider;
            public DateFormatUtil_Factory providesQuickStatusBarHeaderProvider;
            public Provider<StatusIconContainer> providesStatusIconContainerProvider;
            public Provider<QSAnimator> qSAnimatorProvider;
            public Provider<QSContainerImplController> qSContainerImplControllerProvider;
            public Provider<QSCustomizerController> qSCustomizerControllerProvider;
            public Provider<QSFgsManagerFooter> qSFgsManagerFooterProvider;
            public Provider<QSFooterViewController> qSFooterViewControllerProvider;
            public Provider<QSPanelController> qSPanelControllerProvider;
            public Provider qSSecurityFooterProvider;
            public Provider<QSSquishinessController> qSSquishinessControllerProvider;
            public Provider<QuickQSPanelController> quickQSPanelControllerProvider;
            public Provider quickStatusBarHeaderControllerProvider;
            public Provider<TileAdapter> tileAdapterProvider;
            public Provider<TileQueryHelper> tileQueryHelperProvider;

            public QSFragmentComponentImpl(TitanSysUIComponentImpl titanSysUIComponentImpl, QSFragment qSFragment) {
                InstanceFactory create = InstanceFactory.create(qSFragment);
                this.arg0Provider = create;
                QSFragmentModule_ProvideRootViewFactory qSFragmentModule_ProvideRootViewFactory = new QSFragmentModule_ProvideRootViewFactory(create, 0);
                this.provideRootViewProvider = qSFragmentModule_ProvideRootViewFactory;
                QSFragmentModule_ProvideQSPanelFactory qSFragmentModule_ProvideQSPanelFactory = new QSFragmentModule_ProvideQSPanelFactory(qSFragmentModule_ProvideRootViewFactory, 0);
                this.provideQSPanelProvider = qSFragmentModule_ProvideQSPanelFactory;
                QSFragmentModule_ProvideThemedContextFactory qSFragmentModule_ProvideThemedContextFactory = new QSFragmentModule_ProvideThemedContextFactory(qSFragmentModule_ProvideRootViewFactory, 0);
                this.provideThemedContextProvider = qSFragmentModule_ProvideThemedContextFactory;
                LogModule_ProvidePrivacyLogBufferFactory logModule_ProvidePrivacyLogBufferFactory = new LogModule_ProvidePrivacyLogBufferFactory(qSFragmentModule_ProvideThemedContextFactory, 3);
                this.provideThemedLayoutInflaterProvider = logModule_ProvidePrivacyLogBufferFactory;
                Provider<View> provider = DoubleCheck.provider(new DependencyProvider_ProvideLeakDetectorFactory(logModule_ProvidePrivacyLogBufferFactory, qSFragmentModule_ProvideQSPanelFactory));
                this.providesQSFgsManagerFooterViewProvider = provider;
                this.qSFgsManagerFooterProvider = DoubleCheck.provider(new QSFgsManagerFooter_Factory(provider, DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider, titanSysUIComponentImpl.provideBackgroundExecutorProvider, titanSysUIComponentImpl.fgsManagerControllerProvider, 0));
                Provider<View> provider2 = DoubleCheck.provider(new SliceBroadcastRelayHandler_Factory(this.provideThemedLayoutInflaterProvider, this.provideQSPanelProvider, 1));
                this.providesQSSecurityFooterViewProvider = provider2;
                this.qSSecurityFooterProvider = DoubleCheck.provider(QSSecurityFooter_Factory.create(provider2, titanSysUIComponentImpl.provideUserTrackerProvider, DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider, titanSysUIComponentImpl.provideActivityStarterProvider, titanSysUIComponentImpl.securityControllerImplProvider, titanSysUIComponentImpl.provideDialogLaunchAnimatorProvider, titanSysUIComponentImpl.provideBgLooperProvider));
                this.providesQSCutomizerProvider = DoubleCheck.provider(new ActivityIntentHelper_Factory(this.provideRootViewProvider, 4));
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
                this.tileQueryHelperProvider = DoubleCheck.provider(new TileQueryHelper_Factory(daggerTitanGlobalRootComponent.contextProvider, titanSysUIComponentImpl.provideUserTrackerProvider, daggerTitanGlobalRootComponent.provideMainExecutorProvider, titanSysUIComponentImpl.provideBackgroundExecutorProvider));
                Provider<TileAdapter> provider3 = DoubleCheck.provider(new TileAdapter_Factory(this.provideThemedContextProvider, titanSysUIComponentImpl.qSTileHostProvider, DaggerTitanGlobalRootComponent.this.provideUiEventLoggerProvider, 0));
                this.tileAdapterProvider = provider3;
                Provider<QSCustomizer> provider4 = this.providesQSCutomizerProvider;
                Provider<TileQueryHelper> provider5 = this.tileQueryHelperProvider;
                Provider<QSTileHost> provider6 = titanSysUIComponentImpl.qSTileHostProvider;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent2 = DaggerTitanGlobalRootComponent.this;
                Provider<QSCustomizerController> provider7 = DoubleCheck.provider(QSCustomizerController_Factory.create$1(provider4, provider5, provider6, provider3, daggerTitanGlobalRootComponent2.screenLifecycleProvider, titanSysUIComponentImpl.keyguardStateControllerImplProvider, titanSysUIComponentImpl.lightBarControllerProvider, titanSysUIComponentImpl.provideConfigurationControllerProvider, daggerTitanGlobalRootComponent2.provideUiEventLoggerProvider));
                this.qSCustomizerControllerProvider = provider7;
                Provider<Context> provider8 = DaggerTitanGlobalRootComponent.this.contextProvider;
                this.providesQSUsingMediaPlayerProvider = new MediaBrowserFactory_Factory(provider8, 3);
                Provider provider9 = DoubleCheck.provider(new QSTileRevealController_Factory_Factory(provider8, provider7));
                this.factoryProvider = provider9;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent3 = DaggerTitanGlobalRootComponent.this;
                BrightnessController_Factory_Factory brightnessController_Factory_Factory = new BrightnessController_Factory_Factory(daggerTitanGlobalRootComponent3.contextProvider, titanSysUIComponentImpl.providesBroadcastDispatcherProvider, titanSysUIComponentImpl.provideBgHandlerProvider);
                this.factoryProvider2 = brightnessController_Factory_Factory;
                this.qSPanelControllerProvider = DoubleCheck.provider(QSPanelController_Factory.create(this.provideQSPanelProvider, this.qSFgsManagerFooterProvider, this.qSSecurityFooterProvider, titanSysUIComponentImpl.tunerServiceImplProvider, titanSysUIComponentImpl.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, titanSysUIComponentImpl.providesQSMediaHostProvider, provider9, daggerTitanGlobalRootComponent3.dumpManagerProvider, titanSysUIComponentImpl.provideMetricsLoggerProvider, daggerTitanGlobalRootComponent3.provideUiEventLoggerProvider, titanSysUIComponentImpl.qSLoggerProvider, brightnessController_Factory_Factory, titanSysUIComponentImpl.factoryProvider6, titanSysUIComponentImpl.falsingManagerProxyProvider, titanSysUIComponentImpl.featureFlagsReleaseProvider));
                DateFormatUtil_Factory dateFormatUtil_Factory = new DateFormatUtil_Factory(this.provideRootViewProvider, 2);
                this.providesQuickStatusBarHeaderProvider = dateFormatUtil_Factory;
                QSFragmentModule_ProvidesQuickQSPanelFactory qSFragmentModule_ProvidesQuickQSPanelFactory = new QSFragmentModule_ProvidesQuickQSPanelFactory(dateFormatUtil_Factory, 0);
                this.providesQuickQSPanelProvider = qSFragmentModule_ProvidesQuickQSPanelFactory;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent4 = DaggerTitanGlobalRootComponent.this;
                PrivacyLogger_Factory privacyLogger_Factory = new PrivacyLogger_Factory(daggerTitanGlobalRootComponent4.contextProvider, 2);
                this.providesQSUsingCollapsedLandscapeMediaProvider = privacyLogger_Factory;
                Provider<QuickQSPanelController> provider10 = DoubleCheck.provider(QuickQSPanelController_Factory.create(qSFragmentModule_ProvidesQuickQSPanelFactory, titanSysUIComponentImpl.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, titanSysUIComponentImpl.providesQuickQSMediaHostProvider, privacyLogger_Factory, titanSysUIComponentImpl.mediaFlagsProvider, titanSysUIComponentImpl.provideMetricsLoggerProvider, daggerTitanGlobalRootComponent4.provideUiEventLoggerProvider, titanSysUIComponentImpl.qSLoggerProvider, daggerTitanGlobalRootComponent4.dumpManagerProvider));
                this.quickQSPanelControllerProvider = provider10;
                InstanceFactory instanceFactory = this.arg0Provider;
                QSFragmentModule_ProvidesQuickQSPanelFactory qSFragmentModule_ProvidesQuickQSPanelFactory2 = this.providesQuickQSPanelProvider;
                DateFormatUtil_Factory dateFormatUtil_Factory2 = this.providesQuickStatusBarHeaderProvider;
                Provider<QSPanelController> provider11 = this.qSPanelControllerProvider;
                Provider<QSTileHost> provider12 = titanSysUIComponentImpl.qSTileHostProvider;
                Provider<QSFgsManagerFooter> provider13 = this.qSFgsManagerFooterProvider;
                Provider provider14 = this.qSSecurityFooterProvider;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent5 = DaggerTitanGlobalRootComponent.this;
                this.qSAnimatorProvider = DoubleCheck.provider(QSAnimator_Factory.create(instanceFactory, qSFragmentModule_ProvidesQuickQSPanelFactory2, dateFormatUtil_Factory2, provider11, provider10, provider12, provider13, provider14, daggerTitanGlobalRootComponent5.provideMainExecutorProvider, titanSysUIComponentImpl.tunerServiceImplProvider, daggerTitanGlobalRootComponent5.qSExpansionPathInterpolatorProvider));
                this.providesQSContainerImplProvider = new DependencyProvider_ProvideHandlerFactory(this.provideRootViewProvider, 3);
                this.providesPrivacyChipProvider = DoubleCheck.provider(new KeyboardUI_Factory(this.providesQuickStatusBarHeaderProvider, 4));
                Provider<StatusIconContainer> provider15 = DoubleCheck.provider(new ScreenLifecycle_Factory(this.providesQuickStatusBarHeaderProvider, 3));
                this.providesStatusIconContainerProvider = provider15;
                Provider<PrivacyItemController> provider16 = titanSysUIComponentImpl.privacyItemControllerProvider;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent6 = DaggerTitanGlobalRootComponent.this;
                this.headerPrivacyIconsControllerProvider = HeaderPrivacyIconsController_Factory.create(provider16, daggerTitanGlobalRootComponent6.provideUiEventLoggerProvider, this.providesPrivacyChipProvider, titanSysUIComponentImpl.privacyDialogControllerProvider, titanSysUIComponentImpl.privacyLoggerProvider, provider15, daggerTitanGlobalRootComponent6.providePermissionManagerProvider, titanSysUIComponentImpl.provideBackgroundExecutorProvider, daggerTitanGlobalRootComponent6.provideMainExecutorProvider, titanSysUIComponentImpl.provideActivityStarterProvider, titanSysUIComponentImpl.appOpsControllerImplProvider, titanSysUIComponentImpl.deviceConfigProxyProvider);
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent7 = DaggerTitanGlobalRootComponent.this;
                CarrierTextManager_Builder_Factory create2 = CarrierTextManager_Builder_Factory.create(daggerTitanGlobalRootComponent7.contextProvider, daggerTitanGlobalRootComponent7.provideResourcesProvider, daggerTitanGlobalRootComponent7.provideWifiManagerProvider, daggerTitanGlobalRootComponent7.provideTelephonyManagerProvider, titanSysUIComponentImpl.telephonyListenerManagerProvider, titanSysUIComponentImpl.wakefulnessLifecycleProvider, daggerTitanGlobalRootComponent7.provideMainExecutorProvider, titanSysUIComponentImpl.provideBackgroundExecutorProvider, titanSysUIComponentImpl.keyguardUpdateMonitorProvider);
                this.builderProvider = create2;
                this.builderProvider2 = QSCarrierGroupController_Builder_Factory.create(titanSysUIComponentImpl.provideActivityStarterProvider, titanSysUIComponentImpl.provideBgHandlerProvider, titanSysUIComponentImpl.networkControllerImplProvider, create2, DaggerTitanGlobalRootComponent.this.contextProvider, titanSysUIComponentImpl.carrierConfigTrackerProvider, titanSysUIComponentImpl.featureFlagsReleaseProvider, titanSysUIComponentImpl.subscriptionManagerSlotIndexResolverProvider);
                Provider<SystemClock> provider17 = titanSysUIComponentImpl.bindSystemClockProvider;
                Provider<BroadcastDispatcher> provider18 = titanSysUIComponentImpl.providesBroadcastDispatcherProvider;
                this.factoryProvider3 = new VariableDateViewController_Factory_Factory(provider17, provider18, titanSysUIComponentImpl.provideTimeTickHandlerProvider);
                VrMode_Factory vrMode_Factory = new VrMode_Factory(this.providesQuickStatusBarHeaderProvider, 4);
                this.providesBatteryMeterViewProvider = vrMode_Factory;
                Provider<ConfigurationController> provider19 = titanSysUIComponentImpl.provideConfigurationControllerProvider;
                Provider<TunerServiceImpl> provider20 = titanSysUIComponentImpl.tunerServiceImplProvider;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent8 = DaggerTitanGlobalRootComponent.this;
                BatteryMeterViewController_Factory create3 = BatteryMeterViewController_Factory.create(vrMode_Factory, provider19, provider20, provider18, daggerTitanGlobalRootComponent8.provideMainHandlerProvider, daggerTitanGlobalRootComponent8.provideContentResolverProvider, titanSysUIComponentImpl.provideBatteryControllerProvider);
                this.batteryMeterViewControllerProvider = create3;
                Provider provider21 = DoubleCheck.provider(QuickStatusBarHeaderController_Factory.create(this.providesQuickStatusBarHeaderProvider, this.headerPrivacyIconsControllerProvider, titanSysUIComponentImpl.statusBarIconControllerImplProvider, titanSysUIComponentImpl.provideDemoModeControllerProvider, this.quickQSPanelControllerProvider, this.builderProvider2, titanSysUIComponentImpl.sysuiColorExtractorProvider, DaggerTitanGlobalRootComponent.this.qSExpansionPathInterpolatorProvider, titanSysUIComponentImpl.featureFlagsReleaseProvider, this.factoryProvider3, create3, titanSysUIComponentImpl.statusBarContentInsetsProvider));
                this.quickStatusBarHeaderControllerProvider = provider21;
                this.qSContainerImplControllerProvider = DoubleCheck.provider(new QSContainerImplController_Factory(this.providesQSContainerImplProvider, this.qSPanelControllerProvider, provider21, titanSysUIComponentImpl.provideConfigurationControllerProvider));
                SecureSettingsImpl_Factory secureSettingsImpl_Factory = new SecureSettingsImpl_Factory(this.provideRootViewProvider, 2);
                this.providesQSFooterViewProvider = secureSettingsImpl_Factory;
                Provider<QSFooterViewController> provider22 = DoubleCheck.provider(new QSFooterViewController_Factory(secureSettingsImpl_Factory, titanSysUIComponentImpl.provideUserTrackerProvider, titanSysUIComponentImpl.falsingManagerProxyProvider, titanSysUIComponentImpl.provideActivityStarterProvider, this.qSPanelControllerProvider));
                this.qSFooterViewControllerProvider = provider22;
                this.providesQSFooterProvider = DoubleCheck.provider(new TvPipModule_ProvideTvPipBoundsStateFactory(provider22, 3));
                this.qSSquishinessControllerProvider = DoubleCheck.provider(new QSSquishinessController_Factory(this.qSAnimatorProvider, this.qSPanelControllerProvider, this.quickQSPanelControllerProvider, 0));
                QSFragmentModule_ProvideRootViewFactory qSFragmentModule_ProvideRootViewFactory2 = this.provideRootViewProvider;
                Provider<FeatureFlagsRelease> provider23 = titanSysUIComponentImpl.featureFlagsReleaseProvider;
                this.providesQSFooterActionsViewProvider = new QSFragmentModule_ProvidesQSFooterActionsViewFactory(qSFragmentModule_ProvideRootViewFactory2, provider23, 0);
                Provider<MultiUserSwitchController.Factory> provider24 = DoubleCheck.provider(MultiUserSwitchController_Factory_Factory.create(DaggerTitanGlobalRootComponent.this.provideUserManagerProvider, titanSysUIComponentImpl.userSwitcherControllerProvider, titanSysUIComponentImpl.falsingManagerProxyProvider, titanSysUIComponentImpl.userSwitchDialogControllerProvider, provider23, titanSysUIComponentImpl.provideActivityStarterProvider));
                this.factoryProvider4 = provider24;
                QSFragmentModule_ProvidesQSFooterActionsViewFactory qSFragmentModule_ProvidesQSFooterActionsViewFactory = this.providesQSFooterActionsViewProvider;
                Provider<ActivityStarter> provider25 = titanSysUIComponentImpl.provideActivityStarterProvider;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent9 = DaggerTitanGlobalRootComponent.this;
                this.footerActionsControllerProvider = DoubleCheck.provider(FooterActionsController_Factory.create(qSFragmentModule_ProvidesQSFooterActionsViewFactory, provider24, provider25, daggerTitanGlobalRootComponent9.provideUserManagerProvider, titanSysUIComponentImpl.provideUserTrackerProvider, titanSysUIComponentImpl.userInfoControllerImplProvider, titanSysUIComponentImpl.provideDeviceProvisionedControllerProvider, this.qSSecurityFooterProvider, this.qSFgsManagerFooterProvider, titanSysUIComponentImpl.falsingManagerProxyProvider, titanSysUIComponentImpl.provideMetricsLoggerProvider, titanSysUIComponentImpl.globalActionsDialogLiteProvider, daggerTitanGlobalRootComponent9.provideUiEventLoggerProvider, titanSysUIComponentImpl.isPMLiteEnabledProvider, titanSysUIComponentImpl.globalSettingsImplProvider, titanSysUIComponentImpl.provideHandlerProvider, titanSysUIComponentImpl.featureFlagsReleaseProvider));
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public final QSAnimator getQSAnimator() {
                return this.qSAnimatorProvider.mo144get();
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public final QSContainerImplController getQSContainerImplController() {
                return this.qSContainerImplControllerProvider.mo144get();
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public final QSCustomizerController getQSCustomizerController() {
                return this.qSCustomizerControllerProvider.mo144get();
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public final QSFooter getQSFooter() {
                return this.providesQSFooterProvider.mo144get();
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public final FooterActionsController getQSFooterActionController() {
                return this.footerActionsControllerProvider.mo144get();
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public final QSPanelController getQSPanelController() {
                return this.qSPanelControllerProvider.mo144get();
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public final QSSquishinessController getQSSquishinessController() {
                return this.qSSquishinessControllerProvider.mo144get();
            }

            @Override // com.android.systemui.qs.dagger.QSFragmentComponent
            public final QuickQSPanelController getQuickQSPanelController() {
                return this.quickQSPanelControllerProvider.mo144get();
            }
        }

        /* loaded from: classes.dex */
        public final class SectionHeaderControllerSubcomponentBuilder implements SectionHeaderControllerSubcomponent.Builder {
            public String clickIntentAction;
            public Integer headerText;
            public String nodeLabel;

            public SectionHeaderControllerSubcomponentBuilder() {
            }

            @Override // com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent.Builder
            public final SectionHeaderControllerSubcomponent build() {
                R$color.checkBuilderRequirement(this.nodeLabel, String.class);
                R$color.checkBuilderRequirement(this.headerText, Integer.class);
                R$color.checkBuilderRequirement(this.clickIntentAction, String.class);
                return new SectionHeaderControllerSubcomponentImpl(TitanSysUIComponentImpl.this, this.nodeLabel, this.headerText, this.clickIntentAction);
            }

            @Override // com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent.Builder
            public final SectionHeaderControllerSubcomponent.Builder headerText(int i) {
                Integer valueOf = Integer.valueOf(i);
                Objects.requireNonNull(valueOf);
                this.headerText = valueOf;
                return this;
            }

            @Override // com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent.Builder
            /* renamed from: clickIntentAction */
            public final SectionHeaderControllerSubcomponent.Builder mo141clickIntentAction(String str) {
                this.clickIntentAction = str;
                return this;
            }

            @Override // com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent.Builder
            /* renamed from: nodeLabel */
            public final SectionHeaderControllerSubcomponent.Builder mo142nodeLabel(String str) {
                this.nodeLabel = str;
                return this;
            }
        }

        /* loaded from: classes.dex */
        public final class SectionHeaderControllerSubcomponentImpl implements SectionHeaderControllerSubcomponent {
            public InstanceFactory clickIntentActionProvider;
            public InstanceFactory headerTextProvider;
            public InstanceFactory nodeLabelProvider;
            public Provider<SectionHeaderNodeControllerImpl> sectionHeaderNodeControllerImplProvider;

            @Override // com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent
            public final SectionHeaderController getHeaderController() {
                return this.sectionHeaderNodeControllerImplProvider.mo144get();
            }

            @Override // com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent
            public final NodeController getNodeController() {
                return this.sectionHeaderNodeControllerImplProvider.mo144get();
            }

            public SectionHeaderControllerSubcomponentImpl(TitanSysUIComponentImpl titanSysUIComponentImpl, String str, Integer num, String str2) {
                this.nodeLabelProvider = InstanceFactory.create(str);
                this.headerTextProvider = InstanceFactory.create(num);
                InstanceFactory create = InstanceFactory.create(str2);
                this.clickIntentActionProvider = create;
                this.sectionHeaderNodeControllerImplProvider = DoubleCheck.provider(new SectionHeaderNodeControllerImpl_Factory(this.nodeLabelProvider, titanSysUIComponentImpl.providerLayoutInflaterProvider, this.headerTextProvider, titanSysUIComponentImpl.provideActivityStarterProvider, create));
            }
        }

        /* loaded from: classes.dex */
        public final class ServiceBinderCallbackComponentFactory implements ServiceBinderCallbackComponent$Factory {
            public ServiceBinderCallbackComponentFactory() {
            }

            @Override // com.google.android.systemui.communal.dock.dagger.ServiceBinderCallbackComponent$Factory
            public final ServiceBinderCallbackComponentImpl create(Intent intent) {
                return new ServiceBinderCallbackComponentImpl(intent);
            }
        }

        /* loaded from: classes.dex */
        public final class ServiceBinderCallbackComponentImpl {
            public final Intent intent;

            public ServiceBinderCallbackComponentImpl(Intent intent) {
                this.intent = intent;
            }
        }

        /* loaded from: classes.dex */
        public final class SetupDreamComponentFactory implements SetupDreamComponent$Factory {
            public SetupDreamComponentFactory() {
            }

            @Override // com.google.android.systemui.communal.dreams.dagger.SetupDreamComponent$Factory
            public final SetupDreamComponentImpl create(ComplicationViewModel complicationViewModel) {
                Objects.requireNonNull(complicationViewModel);
                return new SetupDreamComponentImpl(complicationViewModel);
            }
        }

        /* loaded from: classes.dex */
        public final class SetupDreamComponentImpl {
            public final ComplicationViewModel model;

            public SetupDreamComponentImpl(ComplicationViewModel complicationViewModel) {
                this.model = complicationViewModel;
            }
        }

        /* loaded from: classes.dex */
        public final class StatusBarComponentFactory implements StatusBarComponent.Factory {
            public StatusBarComponentFactory() {
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent.Factory
            public final StatusBarComponent create() {
                return new StatusBarComponentImpl();
            }
        }

        /* loaded from: classes.dex */
        public final class StatusBarComponentImpl implements StatusBarComponent {
            public Provider<AuthRippleController> authRippleControllerProvider;
            public Provider<StatusBarComponent.Startable> bindStartableProvider;
            public NotificationSwipeHelper_Builder_Factory builderProvider;
            public FlingAnimationUtils_Builder_Factory builderProvider2;
            public CarrierTextManager_Builder_Factory builderProvider3;
            public QSCarrierGroupController_Builder_Factory builderProvider4;
            public Provider<AuthRippleView> getAuthRippleViewProvider;
            public Provider<BatteryMeterViewController> getBatteryMeterViewControllerProvider;
            public Provider<BatteryMeterView> getBatteryMeterViewProvider;
            public Provider<LockIconView> getLockIconViewProvider;
            public Provider<NotificationPanelView> getNotificationPanelViewProvider;
            public Provider<NotificationsQuickSettingsContainer> getNotificationsQuickSettingsContainerProvider;
            public Provider<OngoingPrivacyChip> getSplitShadeOngoingPrivacyChipProvider;
            public Provider<View> getSplitShadeStatusBarViewProvider;
            public Provider<TapAgainView> getTapAgainViewProvider;
            public HeaderPrivacyIconsController_Factory headerPrivacyIconsControllerProvider;
            public Provider<LockIconViewController> lockIconViewControllerProvider;
            public Provider<NotificationPanelViewController> notificationPanelViewControllerProvider;
            public Provider<NotificationStackScrollLayoutController> notificationStackScrollLayoutControllerProvider;
            public TvPipModule_ProvideTvPipBoundsStateFactory notificationStackScrollLoggerProvider;
            public MediaSessionBasedFilter_Factory notificationsQSContainerControllerProvider;
            public Provider<NotificationShadeWindowView> providesNotificationShadeWindowViewProvider;
            public Provider<NotificationShelf> providesNotificationShelfProvider;
            public Provider<NotificationStackScrollLayout> providesNotificationStackScrollLayoutProvider;
            public Provider<NotificationShelfController> providesStatusBarWindowViewProvider;
            public Provider<StatusIconContainer> providesStatusIconContainerProvider;
            public Provider<SplitShadeHeaderController> splitShadeHeaderControllerProvider;
            public ScreenLifecycle_Factory stackStateLoggerProvider;
            public Provider<StatusBarCommandQueueCallbacks> statusBarCommandQueueCallbacksProvider;
            public Provider<StatusBarHeadsUpChangeListener> statusBarHeadsUpChangeListenerProvider;
            public Provider<StatusBarInitializer> statusBarInitializerProvider;
            public Provider<TapAgainViewController> tapAgainViewControllerProvider;

            /* loaded from: classes.dex */
            public final class StatusBarFragmentComponentFactory implements StatusBarFragmentComponent.Factory {
                public StatusBarFragmentComponentFactory() {
                }

                @Override // com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent.Factory
                public final StatusBarFragmentComponent create(CollapsedStatusBarFragment collapsedStatusBarFragment) {
                    Objects.requireNonNull(collapsedStatusBarFragment);
                    return new StatusBarFragmentComponentI(collapsedStatusBarFragment);
                }
            }

            /* loaded from: classes.dex */
            public final class StatusBarFragmentComponentI implements StatusBarFragmentComponent {
                public InstanceFactory arg0Provider;
                public Provider<StatusBarUserSwitcherController> bindStatusBarUserSwitcherControllerProvider;
                public PhoneStatusBarViewController_Factory_Factory factoryProvider;
                public Provider<HeadsUpAppearanceController> headsUpAppearanceControllerProvider;
                public Provider<LightsOutNotifController> lightsOutNotifControllerProvider;
                public Provider<BatteryMeterView> provideBatteryMeterViewProvider;
                public Provider<Clock> provideClockProvider;
                public Provider<View> provideLightsOutNotifViewProvider;
                public Provider<Optional<View>> provideOperatorFrameNameViewProvider;
                public Provider<View> provideOperatorNameViewProvider;
                public Provider<PhoneStatusBarTransitions> providePhoneStatusBarTransitionsProvider;
                public Provider<PhoneStatusBarViewController> providePhoneStatusBarViewControllerProvider;
                public Provider<PhoneStatusBarView> providePhoneStatusBarViewProvider;
                public Provider<StatusBarUserSwitcherContainer> provideStatusBarUserSwitcherContainerProvider;
                public Provider<HeadsUpStatusBarView> providesHeasdUpStatusBarViewProvider;
                public Provider<StatusBarDemoMode> statusBarDemoModeProvider;
                public ClockManager_Factory statusBarUserSwitcherControllerImplProvider;

                public StatusBarFragmentComponentI(CollapsedStatusBarFragment collapsedStatusBarFragment) {
                    InstanceFactory create = InstanceFactory.create(collapsedStatusBarFragment);
                    this.arg0Provider = create;
                    Provider<PhoneStatusBarView> provider = DoubleCheck.provider(new WMShellBaseModule_ProvideHideDisplayCutoutFactory(create, 4));
                    this.providePhoneStatusBarViewProvider = provider;
                    this.provideBatteryMeterViewProvider = DoubleCheck.provider(new TimeoutManager_Factory(provider, 3));
                    Provider<StatusBarUserSwitcherContainer> provider2 = DoubleCheck.provider(new EnhancedEstimatesGoogleImpl_Factory(this.providePhoneStatusBarViewProvider, 2));
                    this.provideStatusBarUserSwitcherContainerProvider = provider2;
                    TitanSysUIComponentImpl titanSysUIComponentImpl = TitanSysUIComponentImpl.this;
                    ClockManager_Factory create$1 = ClockManager_Factory.create$1(provider2, titanSysUIComponentImpl.statusBarUserInfoTrackerProvider, titanSysUIComponentImpl.statusBarUserSwitcherFeatureControllerProvider, titanSysUIComponentImpl.userSwitchDialogControllerProvider, titanSysUIComponentImpl.featureFlagsReleaseProvider, titanSysUIComponentImpl.provideActivityStarterProvider);
                    this.statusBarUserSwitcherControllerImplProvider = create$1;
                    Provider<StatusBarUserSwitcherController> provider3 = DoubleCheck.provider(create$1);
                    this.bindStatusBarUserSwitcherControllerProvider = provider3;
                    TitanSysUIComponentImpl titanSysUIComponentImpl2 = TitanSysUIComponentImpl.this;
                    PhoneStatusBarViewController_Factory_Factory phoneStatusBarViewController_Factory_Factory = new PhoneStatusBarViewController_Factory_Factory(titanSysUIComponentImpl2.provideSysUIUnfoldComponentProvider, DaggerTitanGlobalRootComponent.this.provideStatusBarScopedTransitionProvider, provider3, titanSysUIComponentImpl2.provideConfigurationControllerProvider);
                    this.factoryProvider = phoneStatusBarViewController_Factory_Factory;
                    this.providePhoneStatusBarViewControllerProvider = DoubleCheck.provider(new StatusBarFragmentModule_ProvidePhoneStatusBarViewControllerFactory(phoneStatusBarViewController_Factory_Factory, this.providePhoneStatusBarViewProvider, StatusBarComponentImpl.this.notificationPanelViewControllerProvider));
                    this.providesHeasdUpStatusBarViewProvider = DoubleCheck.provider(new DozeLogger_Factory(this.providePhoneStatusBarViewProvider, 5));
                    this.provideClockProvider = DoubleCheck.provider(new ColorChangeHandler_Factory(this.providePhoneStatusBarViewProvider, 5));
                    Provider<Optional<View>> provider4 = DoubleCheck.provider(new FrameworkServicesModule_ProvideFaceManagerFactory(this.providePhoneStatusBarViewProvider, 2));
                    this.provideOperatorFrameNameViewProvider = provider4;
                    TitanSysUIComponentImpl titanSysUIComponentImpl3 = TitanSysUIComponentImpl.this;
                    this.headsUpAppearanceControllerProvider = DoubleCheck.provider(HeadsUpAppearanceController_Factory.create(titanSysUIComponentImpl3.notificationIconAreaControllerProvider, titanSysUIComponentImpl3.provideHeadsUpManagerPhoneProvider, titanSysUIComponentImpl3.statusBarStateControllerImplProvider, titanSysUIComponentImpl3.keyguardBypassControllerProvider, titanSysUIComponentImpl3.notificationWakeUpCoordinatorProvider, titanSysUIComponentImpl3.darkIconDispatcherImplProvider, titanSysUIComponentImpl3.keyguardStateControllerImplProvider, titanSysUIComponentImpl3.provideCommandQueueProvider, StatusBarComponentImpl.this.notificationStackScrollLayoutControllerProvider, StatusBarComponentImpl.this.notificationPanelViewControllerProvider, this.providesHeasdUpStatusBarViewProvider, this.provideClockProvider, provider4));
                    Provider<View> provider5 = DoubleCheck.provider(new MediaControllerFactory_Factory(this.providePhoneStatusBarViewProvider, 5));
                    this.provideLightsOutNotifViewProvider = provider5;
                    TitanSysUIComponentImpl titanSysUIComponentImpl4 = TitanSysUIComponentImpl.this;
                    this.lightsOutNotifControllerProvider = DoubleCheck.provider(new LightsOutNotifController_Factory(provider5, DaggerTitanGlobalRootComponent.this.provideWindowManagerProvider, titanSysUIComponentImpl4.notifLiveDataStoreImplProvider, titanSysUIComponentImpl4.provideCommandQueueProvider));
                    this.provideOperatorNameViewProvider = DoubleCheck.provider(new StatusBarInitializer_Factory(this.providePhoneStatusBarViewProvider, 4));
                    Provider<PhoneStatusBarTransitions> provider6 = DoubleCheck.provider(new ShadeEventCoordinator_Factory(this.providePhoneStatusBarViewProvider, TitanSysUIComponentImpl.this.statusBarWindowControllerProvider, 1));
                    this.providePhoneStatusBarTransitionsProvider = provider6;
                    Provider<Clock> provider7 = this.provideClockProvider;
                    Provider<View> provider8 = this.provideOperatorNameViewProvider;
                    TitanSysUIComponentImpl titanSysUIComponentImpl5 = TitanSysUIComponentImpl.this;
                    this.statusBarDemoModeProvider = DoubleCheck.provider(StatusBarDemoMode_Factory.create$1(provider7, provider8, titanSysUIComponentImpl5.provideDemoModeControllerProvider, provider6, titanSysUIComponentImpl5.navigationBarControllerProvider, DaggerTitanGlobalRootComponent.this.provideDisplayIdProvider));
                }

                @Override // com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent
                public final BatteryMeterViewController getBatteryMeterViewController() {
                    BatteryMeterView batteryMeterView = this.provideBatteryMeterViewProvider.mo144get();
                    ConfigurationController configurationController = TitanSysUIComponentImpl.this.provideConfigurationControllerProvider.mo144get();
                    TunerServiceImpl tunerServiceImpl = TitanSysUIComponentImpl.this.tunerServiceImplProvider.mo144get();
                    BroadcastDispatcher broadcastDispatcher = TitanSysUIComponentImpl.this.providesBroadcastDispatcherProvider.mo144get();
                    DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
                    Provider provider = DaggerTitanGlobalRootComponent.ABSENT_JDK_OPTIONAL_PROVIDER;
                    return new BatteryMeterViewController(batteryMeterView, configurationController, tunerServiceImpl, broadcastDispatcher, daggerTitanGlobalRootComponent.mainHandler(), DaggerTitanGlobalRootComponent.this.provideContentResolverProvider.mo144get(), TitanSysUIComponentImpl.this.provideBatteryControllerProvider.mo144get());
                }

                @Override // com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent
                public final HeadsUpAppearanceController getHeadsUpAppearanceController() {
                    return this.headsUpAppearanceControllerProvider.mo144get();
                }

                @Override // com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent
                public final LightsOutNotifController getLightsOutNotifController() {
                    return this.lightsOutNotifControllerProvider.mo144get();
                }

                @Override // com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent
                public final PhoneStatusBarTransitions getPhoneStatusBarTransitions() {
                    return this.providePhoneStatusBarTransitionsProvider.mo144get();
                }

                @Override // com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent
                public final PhoneStatusBarView getPhoneStatusBarView() {
                    return this.providePhoneStatusBarViewProvider.mo144get();
                }

                @Override // com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent
                public final PhoneStatusBarViewController getPhoneStatusBarViewController() {
                    return this.providePhoneStatusBarViewControllerProvider.mo144get();
                }

                @Override // com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent
                public final StatusBarDemoMode getStatusBarDemoMode() {
                    return this.statusBarDemoModeProvider.mo144get();
                }
            }

            public StatusBarComponentImpl() {
                Provider<NotificationShadeWindowView> provider = DoubleCheck.provider(new BootCompleteCacheImpl_Factory(TitanSysUIComponentImpl.this.providerLayoutInflaterProvider, 3));
                this.providesNotificationShadeWindowViewProvider = provider;
                Provider<NotificationStackScrollLayout> provider2 = DoubleCheck.provider(new LiftToActivateListener_Factory(provider, 5));
                this.providesNotificationStackScrollLayoutProvider = provider2;
                Provider<NotificationShelf> provider3 = DoubleCheck.provider(new DreamOverlayRegistrant_Factory(TitanSysUIComponentImpl.this.providerLayoutInflaterProvider, provider2, 1));
                this.providesNotificationShelfProvider = provider3;
                this.providesStatusBarWindowViewProvider = DoubleCheck.provider(new StatusBarViewModule_ProvidesStatusBarWindowViewFactory(TitanSysUIComponentImpl.this.notificationShelfComponentBuilderProvider, provider3));
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
                Provider<Resources> provider4 = daggerTitanGlobalRootComponent.provideResourcesProvider;
                Provider<ViewConfiguration> provider5 = daggerTitanGlobalRootComponent.provideViewConfigurationProvider;
                Provider<FalsingManagerProxy> provider6 = TitanSysUIComponentImpl.this.falsingManagerProxyProvider;
                NotificationSwipeHelper_Builder_Factory notificationSwipeHelper_Builder_Factory = new NotificationSwipeHelper_Builder_Factory(provider4, provider5, provider6, TitanSysUIComponentImpl.this.featureFlagsReleaseProvider);
                this.builderProvider = notificationSwipeHelper_Builder_Factory;
                Provider<LogBuffer> provider7 = TitanSysUIComponentImpl.this.provideNotificationHeadsUpLogBufferProvider;
                ScreenLifecycle_Factory screenLifecycle_Factory = new ScreenLifecycle_Factory(provider7, 4);
                this.stackStateLoggerProvider = screenLifecycle_Factory;
                TvPipModule_ProvideTvPipBoundsStateFactory tvPipModule_ProvideTvPipBoundsStateFactory = new TvPipModule_ProvideTvPipBoundsStateFactory(provider7, 4);
                this.notificationStackScrollLoggerProvider = tvPipModule_ProvideTvPipBoundsStateFactory;
                this.notificationStackScrollLayoutControllerProvider = DoubleCheck.provider(NotificationStackScrollLayoutController_Factory.create(TitanSysUIComponentImpl.this.provideAllowNotificationLongPressProvider, TitanSysUIComponentImpl.this.provideNotificationGutsManagerProvider, TitanSysUIComponentImpl.this.provideNotificationVisibilityProvider, TitanSysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, TitanSysUIComponentImpl.this.notificationRoundnessManagerProvider, TitanSysUIComponentImpl.this.tunerServiceImplProvider, TitanSysUIComponentImpl.this.provideDeviceProvisionedControllerProvider, TitanSysUIComponentImpl.this.dynamicPrivacyControllerProvider, TitanSysUIComponentImpl.this.provideConfigurationControllerProvider, TitanSysUIComponentImpl.this.statusBarStateControllerImplProvider, TitanSysUIComponentImpl.this.keyguardMediaControllerProvider, TitanSysUIComponentImpl.this.keyguardBypassControllerProvider, TitanSysUIComponentImpl.this.zenModeControllerImplProvider, TitanSysUIComponentImpl.this.sysuiColorExtractorProvider, TitanSysUIComponentImpl.this.notificationLockscreenUserManagerGoogleProvider, TitanSysUIComponentImpl.this.provideMetricsLoggerProvider, TitanSysUIComponentImpl.this.falsingCollectorImplProvider, provider6, provider4, notificationSwipeHelper_Builder_Factory, TitanSysUIComponentImpl.this.statusBarGoogleProvider, TitanSysUIComponentImpl.this.scrimControllerProvider, TitanSysUIComponentImpl.this.notificationGroupManagerLegacyProvider, TitanSysUIComponentImpl.this.provideGroupExpansionManagerProvider, TitanSysUIComponentImpl.this.providesSilentHeaderControllerProvider, TitanSysUIComponentImpl.this.notifPipelineFlagsProvider, TitanSysUIComponentImpl.this.notifPipelineProvider, TitanSysUIComponentImpl.this.notifCollectionProvider, TitanSysUIComponentImpl.this.provideNotificationEntryManagerProvider, TitanSysUIComponentImpl.this.lockscreenShadeTransitionControllerProvider, daggerTitanGlobalRootComponent.provideIStatusBarServiceProvider, daggerTitanGlobalRootComponent.provideUiEventLoggerProvider, TitanSysUIComponentImpl.this.foregroundServiceDismissalFeatureControllerProvider, TitanSysUIComponentImpl.this.foregroundServiceSectionControllerProvider, TitanSysUIComponentImpl.this.providerLayoutInflaterProvider, TitanSysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, TitanSysUIComponentImpl.this.provideVisualStabilityManagerProvider, TitanSysUIComponentImpl.this.shadeControllerImplProvider, daggerTitanGlobalRootComponent.provideInteractionJankMonitorProvider, screenLifecycle_Factory, tvPipModule_ProvideTvPipBoundsStateFactory));
                this.getNotificationPanelViewProvider = DoubleCheck.provider(new AssistModule_ProvideAssistUtilsFactory(this.providesNotificationShadeWindowViewProvider, 4));
                this.builderProvider2 = new FlingAnimationUtils_Builder_Factory(DaggerTitanGlobalRootComponent.this.provideDisplayMetricsProvider);
                Provider<NotificationsQuickSettingsContainer> provider8 = DoubleCheck.provider(new DependencyProvider_ProvidesChoreographerFactory(this.providesNotificationShadeWindowViewProvider, 2));
                this.getNotificationsQuickSettingsContainerProvider = provider8;
                this.notificationsQSContainerControllerProvider = new MediaSessionBasedFilter_Factory(provider8, TitanSysUIComponentImpl.this.navigationModeControllerProvider, TitanSysUIComponentImpl.this.overviewProxyServiceProvider, TitanSysUIComponentImpl.this.featureFlagsReleaseProvider, 1);
                this.getLockIconViewProvider = DoubleCheck.provider(new PeopleSpaceWidgetProvider_Factory(this.providesNotificationShadeWindowViewProvider, 5));
                Provider<AuthRippleView> provider9 = DoubleCheck.provider(new QSFragmentModule_ProvidesQuickQSPanelFactory(this.providesNotificationShadeWindowViewProvider, 5));
                this.getAuthRippleViewProvider = provider9;
                Provider<AuthRippleController> provider10 = DoubleCheck.provider(AuthRippleController_Factory.create(TitanSysUIComponentImpl.this.statusBarGoogleProvider, DaggerTitanGlobalRootComponent.this.contextProvider, TitanSysUIComponentImpl.this.authControllerProvider, TitanSysUIComponentImpl.this.provideConfigurationControllerProvider, TitanSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TitanSysUIComponentImpl.this.keyguardStateControllerImplProvider, TitanSysUIComponentImpl.this.wakefulnessLifecycleProvider, TitanSysUIComponentImpl.this.commandRegistryProvider, TitanSysUIComponentImpl.this.notificationShadeWindowControllerImplProvider, TitanSysUIComponentImpl.this.keyguardBypassControllerProvider, TitanSysUIComponentImpl.this.biometricUnlockControllerProvider, TitanSysUIComponentImpl.this.udfpsControllerProvider, TitanSysUIComponentImpl.this.statusBarStateControllerImplProvider, provider9));
                this.authRippleControllerProvider = provider10;
                Provider<LockIconView> provider11 = this.getLockIconViewProvider;
                Provider<StatusBarStateControllerImpl> provider12 = TitanSysUIComponentImpl.this.statusBarStateControllerImplProvider;
                Provider<KeyguardUpdateMonitor> provider13 = TitanSysUIComponentImpl.this.keyguardUpdateMonitorProvider;
                Provider<StatusBarKeyguardViewManager> provider14 = TitanSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider;
                Provider<KeyguardStateControllerImpl> provider15 = TitanSysUIComponentImpl.this.keyguardStateControllerImplProvider;
                Provider<FalsingManagerProxy> provider16 = TitanSysUIComponentImpl.this.falsingManagerProxyProvider;
                Provider<AuthController> provider17 = TitanSysUIComponentImpl.this.authControllerProvider;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent2 = DaggerTitanGlobalRootComponent.this;
                this.lockIconViewControllerProvider = DoubleCheck.provider(LockIconViewController_Factory.create(provider11, provider12, provider13, provider14, provider15, provider16, provider17, daggerTitanGlobalRootComponent2.dumpManagerProvider, daggerTitanGlobalRootComponent2.provideAccessibilityManagerProvider, TitanSysUIComponentImpl.this.provideConfigurationControllerProvider, daggerTitanGlobalRootComponent2.provideMainDelayableExecutorProvider, TitanSysUIComponentImpl.this.vibratorHelperProvider, provider10, daggerTitanGlobalRootComponent2.provideResourcesProvider));
                Provider<TapAgainView> provider18 = DoubleCheck.provider(new ActivityStarterDelegate_Factory(this.getNotificationPanelViewProvider, 4));
                this.getTapAgainViewProvider = provider18;
                this.tapAgainViewControllerProvider = DoubleCheck.provider(new AssistLogger_Factory(provider18, DaggerTitanGlobalRootComponent.this.provideMainDelayableExecutorProvider, TitanSysUIComponentImpl.this.provideConfigurationControllerProvider, FalsingModule_ProvidesDoubleTapTimeoutMsFactory.InstanceHolder.INSTANCE, 1));
                Provider<View> provider19 = DoubleCheck.provider(new DependencyProvider_ProvidesViewMediatorCallbackFactory(this.providesNotificationShadeWindowViewProvider, TitanSysUIComponentImpl.this.featureFlagsReleaseProvider, 2));
                this.getSplitShadeStatusBarViewProvider = provider19;
                this.getSplitShadeOngoingPrivacyChipProvider = DoubleCheck.provider(new DreamOverlayModule_ProvidesMaxBurnInOffsetFactory(provider19, 2));
                Provider<StatusIconContainer> provider20 = DoubleCheck.provider(new WMShellBaseModule_ProvideBubblesFactory(this.getSplitShadeStatusBarViewProvider, 3));
                this.providesStatusIconContainerProvider = provider20;
                Provider<PrivacyItemController> provider21 = TitanSysUIComponentImpl.this.privacyItemControllerProvider;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent3 = DaggerTitanGlobalRootComponent.this;
                this.headerPrivacyIconsControllerProvider = HeaderPrivacyIconsController_Factory.create(provider21, daggerTitanGlobalRootComponent3.provideUiEventLoggerProvider, this.getSplitShadeOngoingPrivacyChipProvider, TitanSysUIComponentImpl.this.privacyDialogControllerProvider, TitanSysUIComponentImpl.this.privacyLoggerProvider, provider20, daggerTitanGlobalRootComponent3.providePermissionManagerProvider, TitanSysUIComponentImpl.this.provideBackgroundExecutorProvider, daggerTitanGlobalRootComponent3.provideMainExecutorProvider, TitanSysUIComponentImpl.this.provideActivityStarterProvider, TitanSysUIComponentImpl.this.appOpsControllerImplProvider, TitanSysUIComponentImpl.this.deviceConfigProxyProvider);
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent4 = DaggerTitanGlobalRootComponent.this;
                CarrierTextManager_Builder_Factory create = CarrierTextManager_Builder_Factory.create(daggerTitanGlobalRootComponent4.contextProvider, daggerTitanGlobalRootComponent4.provideResourcesProvider, daggerTitanGlobalRootComponent4.provideWifiManagerProvider, daggerTitanGlobalRootComponent4.provideTelephonyManagerProvider, TitanSysUIComponentImpl.this.telephonyListenerManagerProvider, TitanSysUIComponentImpl.this.wakefulnessLifecycleProvider, daggerTitanGlobalRootComponent4.provideMainExecutorProvider, TitanSysUIComponentImpl.this.provideBackgroundExecutorProvider, TitanSysUIComponentImpl.this.keyguardUpdateMonitorProvider);
                this.builderProvider3 = create;
                this.builderProvider4 = QSCarrierGroupController_Builder_Factory.create(TitanSysUIComponentImpl.this.provideActivityStarterProvider, TitanSysUIComponentImpl.this.provideBgHandlerProvider, TitanSysUIComponentImpl.this.networkControllerImplProvider, create, DaggerTitanGlobalRootComponent.this.contextProvider, TitanSysUIComponentImpl.this.carrierConfigTrackerProvider, TitanSysUIComponentImpl.this.featureFlagsReleaseProvider, TitanSysUIComponentImpl.this.subscriptionManagerSlotIndexResolverProvider);
                Provider<BatteryMeterView> provider22 = DoubleCheck.provider(new ScreenLifecycle_Factory(this.getSplitShadeStatusBarViewProvider, 5));
                this.getBatteryMeterViewProvider = provider22;
                Provider<ConfigurationController> provider23 = TitanSysUIComponentImpl.this.provideConfigurationControllerProvider;
                Provider<TunerServiceImpl> provider24 = TitanSysUIComponentImpl.this.tunerServiceImplProvider;
                Provider<BroadcastDispatcher> provider25 = TitanSysUIComponentImpl.this.providesBroadcastDispatcherProvider;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent5 = DaggerTitanGlobalRootComponent.this;
                Provider<BatteryMeterViewController> provider26 = DoubleCheck.provider(StatusBarViewModule_GetBatteryMeterViewControllerFactory.create(provider22, provider23, provider24, provider25, daggerTitanGlobalRootComponent5.provideMainHandlerProvider, daggerTitanGlobalRootComponent5.provideContentResolverProvider, TitanSysUIComponentImpl.this.provideBatteryControllerProvider));
                this.getBatteryMeterViewControllerProvider = provider26;
                Provider<SplitShadeHeaderController> provider27 = DoubleCheck.provider(SplitShadeHeaderController_Factory.create(this.getSplitShadeStatusBarViewProvider, TitanSysUIComponentImpl.this.statusBarIconControllerImplProvider, this.headerPrivacyIconsControllerProvider, this.builderProvider4, TitanSysUIComponentImpl.this.featureFlagsReleaseProvider, provider26, DaggerTitanGlobalRootComponent.this.dumpManagerProvider));
                this.splitShadeHeaderControllerProvider = provider27;
                Provider<NotificationPanelView> provider28 = this.getNotificationPanelViewProvider;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent6 = DaggerTitanGlobalRootComponent.this;
                Provider<NotificationPanelViewController> provider29 = DoubleCheck.provider(NotificationPanelViewController_Factory.create(provider28, daggerTitanGlobalRootComponent6.provideResourcesProvider, daggerTitanGlobalRootComponent6.provideMainHandlerProvider, TitanSysUIComponentImpl.this.providerLayoutInflaterProvider, TitanSysUIComponentImpl.this.featureFlagsReleaseProvider, TitanSysUIComponentImpl.this.notificationWakeUpCoordinatorProvider, TitanSysUIComponentImpl.this.pulseExpansionHandlerProvider, TitanSysUIComponentImpl.this.dynamicPrivacyControllerProvider, TitanSysUIComponentImpl.this.keyguardBypassControllerProvider, TitanSysUIComponentImpl.this.falsingManagerProxyProvider, TitanSysUIComponentImpl.this.falsingCollectorImplProvider, TitanSysUIComponentImpl.this.notificationLockscreenUserManagerGoogleProvider, TitanSysUIComponentImpl.this.provideNotificationEntryManagerProvider, TitanSysUIComponentImpl.this.communalStateControllerProvider, TitanSysUIComponentImpl.this.keyguardStateControllerImplProvider, TitanSysUIComponentImpl.this.statusBarStateControllerImplProvider, TitanSysUIComponentImpl.this.statusBarWindowStateControllerProvider, TitanSysUIComponentImpl.this.notificationShadeWindowControllerImplProvider, TitanSysUIComponentImpl.this.dozeLogProvider, TitanSysUIComponentImpl.this.dozeParametersProvider, TitanSysUIComponentImpl.this.provideCommandQueueProvider, TitanSysUIComponentImpl.this.vibratorHelperProvider, daggerTitanGlobalRootComponent6.provideLatencyTrackerProvider, daggerTitanGlobalRootComponent6.providePowerManagerProvider, daggerTitanGlobalRootComponent6.provideAccessibilityManagerProvider, daggerTitanGlobalRootComponent6.provideDisplayIdProvider, TitanSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TitanSysUIComponentImpl.this.communalSourceMonitorProvider, TitanSysUIComponentImpl.this.provideMetricsLoggerProvider, daggerTitanGlobalRootComponent6.provideActivityManagerProvider, TitanSysUIComponentImpl.this.provideConfigurationControllerProvider, this.builderProvider2, TitanSysUIComponentImpl.this.statusBarTouchableRegionManagerProvider, TitanSysUIComponentImpl.this.conversationNotificationManagerProvider, TitanSysUIComponentImpl.this.mediaHierarchyManagerProvider, TitanSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, this.notificationsQSContainerControllerProvider, this.notificationStackScrollLayoutControllerProvider, TitanSysUIComponentImpl.this.keyguardStatusViewComponentFactoryProvider, TitanSysUIComponentImpl.this.keyguardQsUserSwitchComponentFactoryProvider, TitanSysUIComponentImpl.this.keyguardUserSwitcherComponentFactoryProvider, TitanSysUIComponentImpl.this.keyguardStatusBarViewComponentFactoryProvider, TitanSysUIComponentImpl.this.communalViewComponentFactoryProvider, TitanSysUIComponentImpl.this.lockscreenShadeTransitionControllerProvider, TitanSysUIComponentImpl.this.notificationGroupManagerLegacyProvider, TitanSysUIComponentImpl.this.notificationIconAreaControllerProvider, TitanSysUIComponentImpl.this.authControllerProvider, TitanSysUIComponentImpl.this.scrimControllerProvider, daggerTitanGlobalRootComponent6.provideUserManagerProvider, TitanSysUIComponentImpl.this.mediaDataManagerProvider, TitanSysUIComponentImpl.this.notificationShadeDepthControllerProvider, TitanSysUIComponentImpl.this.ambientStateProvider, this.lockIconViewControllerProvider, TitanSysUIComponentImpl.this.keyguardMediaControllerProvider, TitanSysUIComponentImpl.this.privacyDotViewControllerProvider, this.tapAgainViewControllerProvider, TitanSysUIComponentImpl.this.navigationModeControllerProvider, TitanSysUIComponentImpl.this.fragmentServiceProvider, daggerTitanGlobalRootComponent6.provideContentResolverProvider, TitanSysUIComponentImpl.this.quickAccessWalletControllerProvider, TitanSysUIComponentImpl.this.qRCodeScannerControllerProvider, TitanSysUIComponentImpl.this.recordingControllerProvider, daggerTitanGlobalRootComponent6.provideMainExecutorProvider, TitanSysUIComponentImpl.this.secureSettingsImplProvider, provider27, TitanSysUIComponentImpl.this.screenOffAnimationControllerProvider, TitanSysUIComponentImpl.this.lockscreenGestureLoggerProvider, TitanSysUIComponentImpl.this.panelExpansionStateManagerProvider, TitanSysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, TitanSysUIComponentImpl.this.provideSysUIUnfoldComponentProvider, TitanSysUIComponentImpl.this.controlsComponentProvider, daggerTitanGlobalRootComponent6.provideInteractionJankMonitorProvider, TitanSysUIComponentImpl.this.qsFrameTranslateImplProvider, TitanSysUIComponentImpl.this.provideSysUiStateProvider, TitanSysUIComponentImpl.this.keyguardUnlockAnimationControllerProvider));
                this.notificationPanelViewControllerProvider = provider29;
                this.statusBarHeadsUpChangeListenerProvider = DoubleCheck.provider(MediaHierarchyManager_Factory.create$1(TitanSysUIComponentImpl.this.notificationShadeWindowControllerImplProvider, TitanSysUIComponentImpl.this.statusBarWindowControllerProvider, provider29, TitanSysUIComponentImpl.this.keyguardBypassControllerProvider, TitanSysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, TitanSysUIComponentImpl.this.statusBarStateControllerImplProvider, TitanSysUIComponentImpl.this.provideNotificationRemoteInputManagerProvider, TitanSysUIComponentImpl.this.provideNotificationsControllerProvider, TitanSysUIComponentImpl.this.dozeServiceHostProvider, TitanSysUIComponentImpl.this.dozeScrimControllerProvider));
                Provider<StatusBarGoogle> provider30 = TitanSysUIComponentImpl.this.statusBarGoogleProvider;
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent7 = DaggerTitanGlobalRootComponent.this;
                this.statusBarCommandQueueCallbacksProvider = DoubleCheck.provider(StatusBarCommandQueueCallbacks_Factory.create(provider30, daggerTitanGlobalRootComponent7.contextProvider, daggerTitanGlobalRootComponent7.provideResourcesProvider, TitanSysUIComponentImpl.this.shadeControllerImplProvider, TitanSysUIComponentImpl.this.provideCommandQueueProvider, this.notificationPanelViewControllerProvider, TitanSysUIComponentImpl.this.setLegacySplitScreenProvider, TitanSysUIComponentImpl.this.remoteInputQuickSettingsDisablerProvider, TitanSysUIComponentImpl.this.provideMetricsLoggerProvider, TitanSysUIComponentImpl.this.keyguardUpdateMonitorProvider, TitanSysUIComponentImpl.this.keyguardStateControllerImplProvider, TitanSysUIComponentImpl.this.provideHeadsUpManagerPhoneProvider, TitanSysUIComponentImpl.this.wakefulnessLifecycleProvider, TitanSysUIComponentImpl.this.provideDeviceProvisionedControllerProvider, TitanSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider, TitanSysUIComponentImpl.this.assistManagerGoogleProvider, TitanSysUIComponentImpl.this.dozeServiceHostProvider, TitanSysUIComponentImpl.this.statusBarStateControllerImplProvider, this.providesNotificationShadeWindowViewProvider, this.notificationStackScrollLayoutControllerProvider, TitanSysUIComponentImpl.this.statusBarHideIconsForBouncerManagerProvider, daggerTitanGlobalRootComponent7.providePowerManagerProvider, TitanSysUIComponentImpl.this.vibratorHelperProvider, daggerTitanGlobalRootComponent7.provideOptionalVibratorProvider, TitanSysUIComponentImpl.this.lightBarControllerProvider, TitanSysUIComponentImpl.this.disableFlagsLoggerProvider, daggerTitanGlobalRootComponent7.provideDisplayIdProvider));
                this.statusBarInitializerProvider = DoubleCheck.provider(new StatusBarInitializer_Factory(TitanSysUIComponentImpl.this.statusBarWindowControllerProvider, 0));
                this.bindStartableProvider = DoubleCheck.provider(new StatusBarNotifPanelEventSourceModule_BindStartableFactory(this.notificationPanelViewControllerProvider));
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public final CollapsedStatusBarFragment createCollapsedStatusBarFragment() {
                return StatusBarViewModule_CreateCollapsedStatusBarFragmentFactory.createCollapsedStatusBarFragment(new StatusBarFragmentComponentFactory(), TitanSysUIComponentImpl.this.provideOngoingCallControllerProvider.mo144get(), TitanSysUIComponentImpl.this.systemStatusAnimationSchedulerProvider.mo144get(), TitanSysUIComponentImpl.this.statusBarLocationPublisherProvider.mo144get(), TitanSysUIComponentImpl.this.notificationIconAreaControllerProvider.mo144get(), TitanSysUIComponentImpl.this.panelExpansionStateManagerProvider.mo144get(), TitanSysUIComponentImpl.this.featureFlagsReleaseProvider.mo144get(), TitanSysUIComponentImpl.this.statusBarIconControllerImplProvider.mo144get(), TitanSysUIComponentImpl.this.statusBarHideIconsForBouncerManagerProvider.mo144get(), TitanSysUIComponentImpl.this.keyguardStateControllerImplProvider.mo144get(), this.notificationPanelViewControllerProvider.mo144get(), TitanSysUIComponentImpl.this.networkControllerImplProvider.mo144get(), TitanSysUIComponentImpl.this.statusBarStateControllerImplProvider.mo144get(), TitanSysUIComponentImpl.this.provideCommandQueueProvider.mo144get(), new CollapsedStatusBarFragmentLogger(TitanSysUIComponentImpl.this.provideCollapsedSbFragmentLogBufferProvider.mo144get(), TitanSysUIComponentImpl.this.disableFlagsLoggerProvider.mo144get()), new OperatorNameViewController.Factory(TitanSysUIComponentImpl.this.darkIconDispatcherImplProvider.mo144get(), TitanSysUIComponentImpl.this.networkControllerImplProvider.mo144get(), TitanSysUIComponentImpl.this.tunerServiceImplProvider.mo144get(), DaggerTitanGlobalRootComponent.this.provideTelephonyManagerProvider.mo144get(), TitanSysUIComponentImpl.this.keyguardUpdateMonitorProvider.mo144get()));
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public final AuthRippleController getAuthRippleController() {
                return this.authRippleControllerProvider.mo144get();
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public final LockIconViewController getLockIconViewController() {
                return this.lockIconViewControllerProvider.mo144get();
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public final NotificationPanelViewController getNotificationPanelViewController() {
                return this.notificationPanelViewControllerProvider.mo144get();
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public final NotificationShadeWindowView getNotificationShadeWindowView() {
                return this.providesNotificationShadeWindowViewProvider.mo144get();
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public final NotificationShadeWindowViewController getNotificationShadeWindowViewController() {
                return new NotificationShadeWindowViewController(TitanSysUIComponentImpl.this.lockscreenShadeTransitionControllerProvider.mo144get(), (FalsingCollector) TitanSysUIComponentImpl.this.falsingCollectorImplProvider.mo144get(), TitanSysUIComponentImpl.this.tunerServiceImplProvider.mo144get(), TitanSysUIComponentImpl.this.statusBarStateControllerImplProvider.mo144get(), TitanSysUIComponentImpl.this.provideDockManagerProvider.mo144get(), TitanSysUIComponentImpl.this.notificationShadeDepthControllerProvider.mo144get(), this.providesNotificationShadeWindowViewProvider.mo144get(), this.notificationPanelViewControllerProvider.mo144get(), TitanSysUIComponentImpl.this.panelExpansionStateManagerProvider.mo144get(), this.notificationStackScrollLayoutControllerProvider.mo144get(), TitanSysUIComponentImpl.this.statusBarKeyguardViewManagerProvider.mo144get(), TitanSysUIComponentImpl.this.statusBarWindowStateControllerProvider.mo144get(), this.lockIconViewControllerProvider.mo144get(), TitanSysUIComponentImpl.this.provideLowLightClockControllerProvider.mo144get());
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public final NotificationShelfController getNotificationShelfController() {
                return this.providesStatusBarWindowViewProvider.mo144get();
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public final NotificationStackScrollLayoutController getNotificationStackScrollLayoutController() {
                return this.notificationStackScrollLayoutControllerProvider.mo144get();
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public final Set<StatusBarComponent.Startable> getStartables() {
                return Collections.singleton(this.bindStartableProvider.mo144get());
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public final StatusBarCommandQueueCallbacks getStatusBarCommandQueueCallbacks() {
                return this.statusBarCommandQueueCallbacksProvider.mo144get();
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public final StatusBarHeadsUpChangeListener getStatusBarHeadsUpChangeListener() {
                return this.statusBarHeadsUpChangeListenerProvider.mo144get();
            }

            @Override // com.android.systemui.statusbar.phone.dagger.StatusBarComponent
            public final StatusBarInitializer getStatusBarInitializer() {
                return this.statusBarInitializerProvider.mo144get();
            }
        }

        /* loaded from: classes.dex */
        public final class SysUIUnfoldComponentFactory implements SysUIUnfoldComponent.Factory {
            public SysUIUnfoldComponentFactory() {
            }

            @Override // com.android.systemui.unfold.SysUIUnfoldComponent.Factory
            public final SysUIUnfoldComponent create(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider) {
                return new SysUIUnfoldComponentImpl(TitanSysUIComponentImpl.this, unfoldTransitionProgressProvider, naturalRotationUnfoldProgressProvider, scopedUnfoldTransitionProgressProvider);
            }
        }

        /* loaded from: classes.dex */
        public final class SysUIUnfoldComponentImpl implements SysUIUnfoldComponent {
            public InstanceFactory arg0Provider;
            public InstanceFactory arg1Provider;
            public InstanceFactory arg2Provider;
            public Provider<FoldAodAnimationController> foldAodAnimationControllerProvider;
            public Provider<KeyguardUnfoldTransition> keyguardUnfoldTransitionProvider;
            public Provider<NotificationPanelUnfoldAnimationController> notificationPanelUnfoldAnimationControllerProvider;
            public Provider<StatusBarMoveFromCenterAnimationController> statusBarMoveFromCenterAnimationControllerProvider;
            public Provider<UnfoldLightRevealOverlayAnimation> unfoldLightRevealOverlayAnimationProvider;
            public Provider<UnfoldTransitionWallpaperController> unfoldTransitionWallpaperControllerProvider;

            @Override // com.android.systemui.unfold.SysUIUnfoldComponent
            public final FoldAodAnimationController getFoldAodAnimationController() {
                return this.foldAodAnimationControllerProvider.mo144get();
            }

            @Override // com.android.systemui.unfold.SysUIUnfoldComponent
            public final KeyguardUnfoldTransition getKeyguardUnfoldTransition() {
                return this.keyguardUnfoldTransitionProvider.mo144get();
            }

            @Override // com.android.systemui.unfold.SysUIUnfoldComponent
            public final NotificationPanelUnfoldAnimationController getNotificationPanelUnfoldAnimationController() {
                return this.notificationPanelUnfoldAnimationControllerProvider.mo144get();
            }

            @Override // com.android.systemui.unfold.SysUIUnfoldComponent
            public final StatusBarMoveFromCenterAnimationController getStatusBarMoveFromCenterAnimationController() {
                return this.statusBarMoveFromCenterAnimationControllerProvider.mo144get();
            }

            @Override // com.android.systemui.unfold.SysUIUnfoldComponent
            public final UnfoldLightRevealOverlayAnimation getUnfoldLightRevealOverlayAnimation() {
                return this.unfoldLightRevealOverlayAnimationProvider.mo144get();
            }

            @Override // com.android.systemui.unfold.SysUIUnfoldComponent
            public final UnfoldTransitionWallpaperController getUnfoldTransitionWallpaperController() {
                return this.unfoldTransitionWallpaperControllerProvider.mo144get();
            }

            public SysUIUnfoldComponentImpl(TitanSysUIComponentImpl titanSysUIComponentImpl, UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider) {
                InstanceFactory create = InstanceFactory.create(naturalRotationUnfoldProgressProvider);
                this.arg1Provider = create;
                this.keyguardUnfoldTransitionProvider = DoubleCheck.provider(new KeyguardUnfoldTransition_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, create, 0));
                InstanceFactory create2 = InstanceFactory.create(scopedUnfoldTransitionProgressProvider);
                this.arg2Provider = create2;
                this.statusBarMoveFromCenterAnimationControllerProvider = DoubleCheck.provider(new QSFragmentModule_ProvidesQSFooterActionsViewFactory(create2, DaggerTitanGlobalRootComponent.this.provideWindowManagerProvider, 2));
                this.notificationPanelUnfoldAnimationControllerProvider = DoubleCheck.provider(new NotificationPanelUnfoldAnimationController_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.arg1Provider));
                this.foldAodAnimationControllerProvider = DoubleCheck.provider(new UdfpsHapticsSimulator_Factory(DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider, titanSysUIComponentImpl.wakefulnessLifecycleProvider, titanSysUIComponentImpl.globalSettingsImplProvider, 2));
                InstanceFactory create3 = InstanceFactory.create(unfoldTransitionProgressProvider);
                this.arg0Provider = create3;
                this.unfoldTransitionWallpaperControllerProvider = DoubleCheck.provider(new FeatureFlagsRelease_Factory(create3, titanSysUIComponentImpl.wallpaperControllerProvider, 2));
                DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
                this.unfoldLightRevealOverlayAnimationProvider = DoubleCheck.provider(UnfoldLightRevealOverlayAnimation_Factory.create(daggerTitanGlobalRootComponent.contextProvider, daggerTitanGlobalRootComponent.provideDeviceStateManagerProvider, daggerTitanGlobalRootComponent.provideDisplayManagerProvider, this.arg0Provider, titanSysUIComponentImpl.setDisplayAreaHelperProvider, daggerTitanGlobalRootComponent.provideMainExecutorProvider, titanSysUIComponentImpl.provideUiBackgroundExecutorProvider, daggerTitanGlobalRootComponent.provideIWindowManagerProvider));
            }
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final /* bridge */ /* synthetic */ void init() {
            super.init();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final void inject(SystemUIAppComponentFactory systemUIAppComponentFactory) {
            injectSystemUIAppComponentFactory(systemUIAppComponentFactory);
        }

        /* loaded from: classes.dex */
        public final class DreamClockDateComplicationComponentImpl {
            public Provider<View> provideComplicationViewProvider;
            public Provider<ComplicationLayoutParams> provideLayoutParamsProvider = DoubleCheck.provider(DreamClockDateComplicationComponent_DreamClockDateComplicationModule_ProvideLayoutParamsFactory.InstanceHolder.INSTANCE);

            public DreamClockDateComplicationComponentImpl(TitanSysUIComponentImpl titanSysUIComponentImpl) {
                this.provideComplicationViewProvider = DoubleCheck.provider(new DreamClockDateComplicationComponent_DreamClockDateComplicationModule_ProvideComplicationViewFactory(titanSysUIComponentImpl.providerLayoutInflaterProvider));
            }
        }

        /* loaded from: classes.dex */
        public final class DreamClockTimeComplicationComponentImpl {
            public Provider<View> provideComplicationViewProvider;
            public Provider<ComplicationLayoutParams> provideLayoutParamsProvider = DoubleCheck.provider(DreamClockTimeComplicationComponent_DreamClockTimeComplicationModule_ProvideLayoutParamsFactory.InstanceHolder.INSTANCE);

            public DreamClockTimeComplicationComponentImpl(TitanSysUIComponentImpl titanSysUIComponentImpl) {
                this.provideComplicationViewProvider = DoubleCheck.provider(new DreamClockTimeComplicationComponent_DreamClockTimeComplicationModule_ProvideComplicationViewFactory(titanSysUIComponentImpl.providerLayoutInflaterProvider));
            }
        }

        public TitanSysUIComponentImpl(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<Object> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            initialize(dependencyProvider, nightDisplayListenerModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize2(dependencyProvider, nightDisplayListenerModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize3(dependencyProvider, nightDisplayListenerModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize4(dependencyProvider, nightDisplayListenerModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize5(dependencyProvider, nightDisplayListenerModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize6(dependencyProvider, nightDisplayListenerModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize7(dependencyProvider, nightDisplayListenerModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize8(dependencyProvider, nightDisplayListenerModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize9(dependencyProvider, nightDisplayListenerModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
        }

        public final ActivityStarter activityStarter() {
            ActivityStarterDelegate activityStarterDelegate = this.activityStarterDelegateProvider.mo144get();
            DaggerTitanGlobalRootComponent.this.pluginDependencyProvider.mo144get().allowPluginDependency(ActivityStarter.class, activityStarterDelegate);
            Objects.requireNonNull(activityStarterDelegate, "Cannot return null from a non-@Nullable @Provides method");
            return activityStarterDelegate;
        }

        public final BouncerSwipeTouchHandler bouncerSwipeTouchHandler() {
            return new BouncerSwipeTouchHandler(this.statusBarKeyguardViewManagerProvider.mo144get(), this.statusBarGoogleProvider.mo144get(), this.notificationShadeWindowControllerImplProvider.mo144get(), namedFlingAnimationUtils(), namedFlingAnimationUtils2(), namedFloat());
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final Dependency createDependency() {
            return this.dependencyProvider2.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final DumpManager createDumpManager() {
            return DaggerTitanGlobalRootComponent.this.dumpManagerProvider.mo144get();
        }

        @Override // com.google.android.systemui.dagger.SysUIGoogleSysUIComponent
        public final KeyguardSmartspaceController createKeyguardSmartspaceController() {
            return this.keyguardSmartspaceControllerProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final ConfigurationController getConfigurationController() {
            return this.provideConfigurationControllerProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final ContextComponentHelper getContextComponentHelper() {
            return this.contextComponentResolverProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final Optional<FoldStateLogger> getFoldStateLogger() {
            return DaggerTitanGlobalRootComponent.this.providesFoldStateLoggerProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final Optional<FoldStateLoggingProvider> getFoldStateLoggingProvider() {
            return DaggerTitanGlobalRootComponent.this.providesFoldStateLoggingProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final InitController getInitController() {
            return this.initControllerProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final Optional<MediaMuteAwaitConnectionCli> getMediaMuteAwaitConnectionCli() {
            return this.providesMediaMuteAwaitConnectionCliProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final Optional<MediaTttChipControllerReceiver> getMediaTttChipControllerReceiver() {
            return this.providesMediaTttChipControllerReceiverProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final Optional<MediaTttChipControllerSender> getMediaTttChipControllerSender() {
            return this.providesMediaTttChipControllerSenderProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final Optional<MediaTttCommandLineHelper> getMediaTttCommandLineHelper() {
            return this.providesMediaTttCommandLineHelperProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final Optional<NaturalRotationUnfoldProgressProvider> getNaturalRotationUnfoldProgressProvider() {
            return DaggerTitanGlobalRootComponent.this.provideNaturalRotationProgressProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final Optional<NearbyMediaDevicesManager> getNearbyMediaDevicesManager() {
            return this.providesNearbyMediaDevicesManagerProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final Map<Class<?>, Provider<CoreStartable>> getPerUserStartables() {
            return Collections.singletonMap(NotificationChannels.class, this.notificationChannelsProvider);
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final Map<Class<?>, Provider<CoreStartable>> getStartables() {
            LinkedHashMap newLinkedHashMapWithExpectedSize = R$id.newLinkedHashMapWithExpectedSize(34);
            newLinkedHashMapWithExpectedSize.put(AuthController.class, this.authControllerProvider);
            newLinkedHashMapWithExpectedSize.put(ClipboardListener.class, this.clipboardListenerProvider);
            newLinkedHashMapWithExpectedSize.put(GarbageMonitor.class, this.serviceProvider);
            newLinkedHashMapWithExpectedSize.put(GlobalActionsComponent.class, this.globalActionsComponentProvider);
            newLinkedHashMapWithExpectedSize.put(InstantAppNotifier.class, this.instantAppNotifierProvider);
            newLinkedHashMapWithExpectedSize.put(KeyboardUI.class, this.keyboardUIProvider);
            newLinkedHashMapWithExpectedSize.put(KeyguardBiometricLockoutLogger.class, this.keyguardBiometricLockoutLoggerProvider);
            newLinkedHashMapWithExpectedSize.put(KeyguardViewMediator.class, this.newKeyguardViewMediatorProvider);
            newLinkedHashMapWithExpectedSize.put(LatencyTester.class, this.latencyTesterProvider);
            newLinkedHashMapWithExpectedSize.put(PowerUI.class, this.powerUIProvider);
            newLinkedHashMapWithExpectedSize.put(Recents.class, this.provideRecentsProvider);
            newLinkedHashMapWithExpectedSize.put(RingtonePlayer.class, this.ringtonePlayerProvider);
            newLinkedHashMapWithExpectedSize.put(ScreenDecorations.class, this.screenDecorationsProvider);
            newLinkedHashMapWithExpectedSize.put(SessionTracker.class, this.sessionTrackerProvider);
            newLinkedHashMapWithExpectedSize.put(ShortcutKeyDispatcher.class, this.shortcutKeyDispatcherProvider);
            newLinkedHashMapWithExpectedSize.put(SliceBroadcastRelayHandler.class, this.sliceBroadcastRelayHandlerProvider);
            newLinkedHashMapWithExpectedSize.put(StorageNotification.class, this.storageNotificationProvider);
            newLinkedHashMapWithExpectedSize.put(SystemActions.class, this.systemActionsProvider);
            newLinkedHashMapWithExpectedSize.put(ThemeOverlayController.class, this.themeOverlayControllerProvider);
            newLinkedHashMapWithExpectedSize.put(ToastUI.class, this.toastUIProvider);
            newLinkedHashMapWithExpectedSize.put(VolumeUI.class, this.volumeUIProvider);
            newLinkedHashMapWithExpectedSize.put(WindowMagnification.class, this.windowMagnificationProvider);
            newLinkedHashMapWithExpectedSize.put(WMShell.class, this.wMShellProvider);
            newLinkedHashMapWithExpectedSize.put(GoogleServices.class, this.googleServicesProvider);
            newLinkedHashMapWithExpectedSize.put(StatusBar.class, this.statusBarGoogleProvider);
            newLinkedHashMapWithExpectedSize.put(DockMonitor.class, this.dockMonitorProvider);
            newLinkedHashMapWithExpectedSize.put(DockEventSimulator.class, this.dockEventSimulatorProvider);
            newLinkedHashMapWithExpectedSize.put(DreamClockTimeComplication.Registrant.class, this.registrantProvider);
            newLinkedHashMapWithExpectedSize.put(DreamClockDateComplication.Registrant.class, this.registrantProvider2);
            newLinkedHashMapWithExpectedSize.put(DreamOverlayRegistrant.class, this.dreamOverlayRegistrantProvider);
            newLinkedHashMapWithExpectedSize.put(DreamWeatherComplication.Registrant.class, this.registrantProvider3);
            newLinkedHashMapWithExpectedSize.put(MediaDreamSentinel.class, this.mediaDreamSentinelProvider);
            newLinkedHashMapWithExpectedSize.put(SmartSpaceComplication.Registrant.class, this.registrantProvider4);
            newLinkedHashMapWithExpectedSize.put(ComplicationTypesUpdater.class, this.complicationTypesUpdaterProvider);
            if (newLinkedHashMapWithExpectedSize.isEmpty()) {
                return Collections.emptyMap();
            }
            return Collections.unmodifiableMap(newLinkedHashMapWithExpectedSize);
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final Optional<SysUIUnfoldComponent> getSysUIUnfoldComponent() {
            return this.provideSysUIUnfoldComponentProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final UnfoldLatencyTracker getUnfoldLatencyTracker() {
            return this.unfoldLatencyTrackerProvider.mo144get();
        }

        public final void initialize(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<Object> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.bootCompleteCacheImplProvider = DoubleCheck.provider(new BootCompleteCacheImpl_Factory(DaggerTitanGlobalRootComponent.this.dumpManagerProvider, 0));
            this.provideConfigurationControllerProvider = DoubleCheck.provider(new ResumeMediaBrowserFactory_Factory(dependencyProvider, DaggerTitanGlobalRootComponent.this.contextProvider));
            Provider<Looper> provider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBgLooperFactory.InstanceHolder.INSTANCE);
            this.provideBgLooperProvider = provider;
            this.provideBackgroundExecutorProvider = DoubleCheck.provider(new PowerSaveState_Factory(provider, 4));
            this.provideExecutorProvider = DaggerGlobalRootComponent$SysUIComponentImpl$KeyguardBouncerComponentImpl$$ExternalSyntheticOutline1.m(this.provideBgLooperProvider, 6);
            SmartActionsReceiver_Factory smartActionsReceiver_Factory = new SmartActionsReceiver_Factory(this.provideBgLooperProvider, 6);
            this.provideBgHandlerProvider = smartActionsReceiver_Factory;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
            Provider<UserTracker> provider2 = DoubleCheck.provider(new SettingsModule_ProvideUserTrackerFactory(daggerTitanGlobalRootComponent.contextProvider, daggerTitanGlobalRootComponent.provideUserManagerProvider, daggerTitanGlobalRootComponent.dumpManagerProvider, smartActionsReceiver_Factory));
            this.provideUserTrackerProvider = provider2;
            this.controlsListingControllerImplProvider = DoubleCheck.provider(new TouchInsideHandler_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideExecutorProvider, provider2, 1));
            this.provideBackgroundDelayableExecutorProvider = DoubleCheck.provider(new ForegroundServicesDialog_Factory(this.provideBgLooperProvider, 5));
            this.controlsControllerImplProvider = new DelegateFactory();
            this.provideSharePreferencesProvider = new KeyguardLifecyclesDispatcher_Factory(dependencyProvider, DaggerTitanGlobalRootComponent.this.contextProvider);
            this.provideDelayableExecutorProvider = DoubleCheck.provider(new ActionClickLogger_Factory(this.provideBgLooperProvider, 3));
            Provider<ContentResolver> provider3 = DaggerTitanGlobalRootComponent.this.provideContentResolverProvider;
            GlobalConcurrencyModule_ProvideMainLooperFactory globalConcurrencyModule_ProvideMainLooperFactory = GlobalConcurrencyModule_ProvideMainLooperFactory.InstanceHolder.INSTANCE;
            Provider<LogcatEchoTracker> provider4 = DoubleCheck.provider(new QSFlagsModule_IsPMLiteEnabledFactory(provider3, globalConcurrencyModule_ProvideMainLooperFactory, 2));
            this.provideLogcatEchoTrackerProvider = provider4;
            Provider<LogBufferFactory> provider5 = DoubleCheck.provider(new LogBufferFactory_Factory(DaggerTitanGlobalRootComponent.this.dumpManagerProvider, provider4, 0));
            this.logBufferFactoryProvider = provider5;
            Provider<LogBuffer> provider6 = DoubleCheck.provider(new ForegroundServicesDialog_Factory(provider5, 3));
            this.provideBroadcastDispatcherLogBufferProvider = provider6;
            WallpaperController_Factory wallpaperController_Factory = new WallpaperController_Factory(provider6, 1);
            this.broadcastDispatcherLoggerProvider = wallpaperController_Factory;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent2 = DaggerTitanGlobalRootComponent.this;
            this.providesBroadcastDispatcherProvider = DoubleCheck.provider(TvSystemUIModule_ProvideBatteryControllerFactory.create(dependencyProvider, daggerTitanGlobalRootComponent2.contextProvider, this.provideBgLooperProvider, this.provideBackgroundExecutorProvider, daggerTitanGlobalRootComponent2.dumpManagerProvider, wallpaperController_Factory, this.provideUserTrackerProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent3 = DaggerTitanGlobalRootComponent.this;
            this.statusBarStateControllerImplProvider = DoubleCheck.provider(new RingerModeTrackerImpl_Factory(daggerTitanGlobalRootComponent3.provideUiEventLoggerProvider, daggerTitanGlobalRootComponent3.dumpManagerProvider, daggerTitanGlobalRootComponent3.provideInteractionJankMonitorProvider, 1));
            this.provideLockPatternUtilsProvider = DoubleCheck.provider(new OpaLockscreen_Factory(dependencyProvider, DaggerTitanGlobalRootComponent.this.contextProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent4 = DaggerTitanGlobalRootComponent.this;
            this.protoTracerProvider = DoubleCheck.provider(new ProtoTracer_Factory(daggerTitanGlobalRootComponent4.contextProvider, daggerTitanGlobalRootComponent4.dumpManagerProvider, 0));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent5 = DaggerTitanGlobalRootComponent.this;
            Provider<CommandRegistry> provider7 = DoubleCheck.provider(new ShortcutKeyDispatcher_Factory(daggerTitanGlobalRootComponent5.contextProvider, daggerTitanGlobalRootComponent5.provideMainExecutorProvider, 1));
            this.commandRegistryProvider = provider7;
            this.provideCommandQueueProvider = DoubleCheck.provider(new DozeLog_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.protoTracerProvider, provider7, 1));
            this.providerLayoutInflaterProvider = DoubleCheck.provider(new DependencyProvider_ProviderLayoutInflaterFactory(dependencyProvider, DaggerTitanGlobalRootComponent.this.contextProvider));
            this.panelExpansionStateManagerProvider = DoubleCheck.provider(PanelExpansionStateManager_Factory.InstanceHolder.INSTANCE);
            this.enhancedEstimatesGoogleImplProvider = DoubleCheck.provider(new EnhancedEstimatesGoogleImpl_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 0));
            ActivityIntentHelper_Factory activityIntentHelper_Factory = new ActivityIntentHelper_Factory(DaggerTitanGlobalRootComponent.this.provideContentResolverProvider, 5);
            this.globalSettingsImplProvider = activityIntentHelper_Factory;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent6 = DaggerTitanGlobalRootComponent.this;
            this.provideDemoModeControllerProvider = DoubleCheck.provider(new LaunchOverview_Factory(daggerTitanGlobalRootComponent6.contextProvider, daggerTitanGlobalRootComponent6.dumpManagerProvider, activityIntentHelper_Factory, 1));
            this.provideReverseWirelessChargerProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline3.m(DaggerTitanGlobalRootComponent.this.contextProvider, 9);
            this.provideUsbManagerProvider = DoubleCheck.provider(new ActionClickLogger_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 4));
            Provider<IThermalService> provider8 = DoubleCheck.provider(SystemUIGoogleModule_ProvideIThermalServiceFactory.InstanceHolder.INSTANCE);
            this.provideIThermalServiceProvider = provider8;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent7 = DaggerTitanGlobalRootComponent.this;
            Provider<ReverseChargingController> provider9 = DoubleCheck.provider(new BluetoothTile_Factory(daggerTitanGlobalRootComponent7.contextProvider, this.providesBroadcastDispatcherProvider, this.provideReverseWirelessChargerProvider, daggerTitanGlobalRootComponent7.provideAlarmManagerProvider, this.provideUsbManagerProvider, daggerTitanGlobalRootComponent7.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.bootCompleteCacheImplProvider, provider8, 1));
            this.reverseChargingControllerProvider = provider9;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent8 = DaggerTitanGlobalRootComponent.this;
            this.provideBatteryControllerProvider = DoubleCheck.provider(new OverlayToggleTile_Factory(daggerTitanGlobalRootComponent8.contextProvider, this.enhancedEstimatesGoogleImplProvider, daggerTitanGlobalRootComponent8.providePowerManagerProvider, this.providesBroadcastDispatcherProvider, this.provideDemoModeControllerProvider, daggerTitanGlobalRootComponent8.provideMainHandlerProvider, this.provideBgHandlerProvider, this.provideUserTrackerProvider, provider9, 2));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent9 = DaggerTitanGlobalRootComponent.this;
            Provider<Context> provider10 = daggerTitanGlobalRootComponent9.contextProvider;
            this.provideAmbientDisplayConfigurationProvider = new DistanceClassifier_Factory(dependencyProvider, provider10);
            this.debugModeFilterProvider = DoubleCheck.provider(new MediaModule_ProvidesMediaTttChipControllerSenderFactory(provider10, daggerTitanGlobalRootComponent9.dumpManagerProvider, 1));
            this.notifLiveDataStoreImplProvider = DoubleCheck.provider(new AssistModule_ProvideAssistUtilsFactory(DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider, 2));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent10 = DaggerTitanGlobalRootComponent.this;
            Provider<FeatureFlagsRelease> provider11 = DoubleCheck.provider(new FeatureFlagsRelease_Factory(daggerTitanGlobalRootComponent10.provideResourcesProvider, daggerTitanGlobalRootComponent10.dumpManagerProvider, 0));
            this.featureFlagsReleaseProvider = provider11;
            this.notifPipelineFlagsProvider = new VolumeUI_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, provider11, 1);
            this.bindSystemClockProvider = DoubleCheck.provider(SystemClockImpl_Factory.InstanceHolder.INSTANCE);
            Provider<LogBuffer> provider12 = DoubleCheck.provider(new QSFragmentModule_ProvideThemedContextFactory(this.logBufferFactoryProvider, 3));
            this.provideNotificationsLogBufferProvider = provider12;
            this.notifCollectionLoggerProvider = new PrivacyLogger_Factory(provider12, 4);
            Provider<Files> provider13 = DoubleCheck.provider(Files_Factory.InstanceHolder.INSTANCE);
            this.filesProvider = provider13;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent11 = DaggerTitanGlobalRootComponent.this;
            Provider<LogBufferEulogizer> provider14 = DoubleCheck.provider(new LogBufferEulogizer_Factory(daggerTitanGlobalRootComponent11.contextProvider, daggerTitanGlobalRootComponent11.dumpManagerProvider, this.bindSystemClockProvider, provider13, 0));
            this.logBufferEulogizerProvider = provider14;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent12 = DaggerTitanGlobalRootComponent.this;
            this.notifCollectionProvider = DoubleCheck.provider(NotifCollection_Factory.create(daggerTitanGlobalRootComponent12.provideIStatusBarServiceProvider, this.bindSystemClockProvider, this.notifPipelineFlagsProvider, this.notifCollectionLoggerProvider, daggerTitanGlobalRootComponent12.provideMainHandlerProvider, provider14, daggerTitanGlobalRootComponent12.dumpManagerProvider));
            Provider<Choreographer> provider15 = DoubleCheck.provider(new DependencyProvider_ProvidesChoreographerFactory(dependencyProvider, 0));
            this.providesChoreographerProvider = provider15;
            this.notifPipelineChoreographerImplProvider = DoubleCheck.provider(new RotationPolicyWrapperImpl_Factory(provider15, DaggerTitanGlobalRootComponent.this.provideMainDelayableExecutorProvider, 1));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent13 = DaggerTitanGlobalRootComponent.this;
            this.notificationClickNotifierProvider = DoubleCheck.provider(new LogBufferFactory_Factory(daggerTitanGlobalRootComponent13.provideIStatusBarServiceProvider, daggerTitanGlobalRootComponent13.provideMainExecutorProvider, 1));
            this.notificationEntryManagerLoggerProvider = new WMShellBaseModule_ProvideRecentTasksFactory(this.provideNotificationsLogBufferProvider, 2);
            Provider<LeakDetector> provider16 = DoubleCheck.provider(new DependencyProvider_ProvideLeakDetectorFactory(dependencyProvider, DaggerTitanGlobalRootComponent.this.dumpManagerProvider));
            this.provideLeakDetectorProvider = provider16;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent14 = DaggerTitanGlobalRootComponent.this;
            Provider<TunerServiceImpl> provider17 = DoubleCheck.provider(new TunerServiceImpl_Factory(daggerTitanGlobalRootComponent14.contextProvider, daggerTitanGlobalRootComponent14.provideMainHandlerProvider, provider16, this.provideDemoModeControllerProvider, this.provideUserTrackerProvider));
            this.tunerServiceImplProvider = provider17;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent15 = DaggerTitanGlobalRootComponent.this;
            Provider<ExtensionControllerImpl> provider18 = DoubleCheck.provider(new ExtensionControllerImpl_Factory(daggerTitanGlobalRootComponent15.contextProvider, this.provideLeakDetectorProvider, daggerTitanGlobalRootComponent15.providesPluginManagerProvider, provider17, this.provideConfigurationControllerProvider));
            this.extensionControllerImplProvider = provider18;
            this.notificationPersonExtractorPluginBoundaryProvider = DoubleCheck.provider(new AssistModule_ProvideAssistUtilsFactory(provider18, 3));
            DelegateFactory delegateFactory = new DelegateFactory();
            this.notificationGroupManagerLegacyProvider = delegateFactory;
            Provider<GroupMembershipManager> provider19 = DoubleCheck.provider(new WMShellModule_ProvideUnfoldBackgroundControllerFactory(this.notifPipelineFlagsProvider, delegateFactory, 1));
            this.provideGroupMembershipManagerProvider = provider19;
            this.peopleNotificationIdentifierImplProvider = DoubleCheck.provider(new RotationPolicyWrapperImpl_Factory(this.notificationPersonExtractorPluginBoundaryProvider, provider19, 3));
            InstanceFactory create = InstanceFactory.create(optional6);
            this.setBubblesProvider = create;
            DelegateFactory.setDelegate(this.notificationGroupManagerLegacyProvider, DoubleCheck.provider(new NotificationGroupManagerLegacy_Factory(this.statusBarStateControllerImplProvider, this.peopleNotificationIdentifierImplProvider, create, DaggerTitanGlobalRootComponent.this.dumpManagerProvider)));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent16 = DaggerTitanGlobalRootComponent.this;
            this.provideNotificationMessagingUtilProvider = new SliceBroadcastRelayHandler_Factory(dependencyProvider, daggerTitanGlobalRootComponent16.contextProvider);
            this.notificationLockscreenUserManagerGoogleProvider = new DelegateFactory();
            DelegateFactory delegateFactory2 = new DelegateFactory();
            this.provideNotificationVisibilityProvider = delegateFactory2;
            this.provideSmartReplyControllerProvider = DoubleCheck.provider(new StatusBarDependenciesModule_ProvideSmartReplyControllerFactory(daggerTitanGlobalRootComponent16.dumpManagerProvider, delegateFactory2, daggerTitanGlobalRootComponent16.provideIStatusBarServiceProvider, this.notificationClickNotifierProvider));
            this.provideNotificationEntryManagerProvider = new DelegateFactory();
            this.remoteInputNotificationRebuilderProvider = DoubleCheck.provider(new PrivacyLogger_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 3));
            this.optionalOfStatusBarProvider = new DelegateFactory();
            this.provideHandlerProvider = new DependencyProvider_ProvideHandlerFactory(dependencyProvider, 0);
            this.remoteInputUriControllerProvider = DoubleCheck.provider(new TimeoutManager_Factory(DaggerTitanGlobalRootComponent.this.provideIStatusBarServiceProvider, 4));
            Provider<LogBuffer> m = DaggerGlobalRootComponent$SysUIComponentImpl$KeyguardBouncerComponentImpl$$ExternalSyntheticOutline1.m(this.logBufferFactoryProvider, 3);
            this.provideNotifInteractionLogBufferProvider = m;
            this.actionClickLoggerProvider = new ActionClickLogger_Factory(m, 0);
            this.provideNotificationRemoteInputManagerProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideNotificationRemoteInputManagerFactory.create(DaggerTitanGlobalRootComponent.this.contextProvider, this.notifPipelineFlagsProvider, this.notificationLockscreenUserManagerGoogleProvider, this.provideSmartReplyControllerProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.remoteInputNotificationRebuilderProvider, this.optionalOfStatusBarProvider, this.statusBarStateControllerImplProvider, this.provideHandlerProvider, this.remoteInputUriControllerProvider, this.notificationClickNotifierProvider, this.actionClickLoggerProvider, DaggerTitanGlobalRootComponent.this.dumpManagerProvider));
            this.provideCommonNotifCollectionProvider = new DelegateFactory();
            DateFormatUtil_Factory dateFormatUtil_Factory = new DateFormatUtil_Factory(this.provideNotificationsLogBufferProvider, 3);
            this.notifBindPipelineLoggerProvider = dateFormatUtil_Factory;
            this.notifBindPipelineProvider = DoubleCheck.provider(new DozeWallpaperState_Factory(this.provideCommonNotifCollectionProvider, dateFormatUtil_Factory, globalConcurrencyModule_ProvideMainLooperFactory, 1));
            PeopleSpaceWidgetProvider_Factory peopleSpaceWidgetProvider_Factory = new PeopleSpaceWidgetProvider_Factory(this.provideCommonNotifCollectionProvider, 4);
            this.notifRemoteViewCacheImplProvider = peopleSpaceWidgetProvider_Factory;
            this.provideNotifRemoteViewCacheProvider = DoubleCheck.provider(peopleSpaceWidgetProvider_Factory);
            Provider<BindEventManagerImpl> provider20 = DoubleCheck.provider(BindEventManagerImpl_Factory.InstanceHolder.INSTANCE);
            this.bindEventManagerImplProvider = provider20;
            Provider<NotificationGroupManagerLegacy> provider21 = this.notificationGroupManagerLegacyProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent17 = DaggerTitanGlobalRootComponent.this;
            Provider<ConversationNotificationManager> provider22 = DoubleCheck.provider(ConversationNotificationManager_Factory.create(provider20, provider21, daggerTitanGlobalRootComponent17.contextProvider, this.provideCommonNotifCollectionProvider, this.notifPipelineFlagsProvider, daggerTitanGlobalRootComponent17.provideMainHandlerProvider));
            this.conversationNotificationManagerProvider = provider22;
            this.conversationNotificationProcessorProvider = new KeyguardLifecyclesDispatcher_Factory(DaggerTitanGlobalRootComponent.this.provideLauncherAppsProvider, provider22, 3);
            this.mediaFeatureFlagProvider = new QSLogger_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 2);
            Provider<DeviceConfigProxy> provider23 = DoubleCheck.provider(DeviceConfigProxy_Factory.InstanceHolder.INSTANCE);
            this.deviceConfigProxyProvider = provider23;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent18 = DaggerTitanGlobalRootComponent.this;
            this.smartReplyConstantsProvider = DoubleCheck.provider(new LeakReporter_Factory(daggerTitanGlobalRootComponent18.provideMainHandlerProvider, daggerTitanGlobalRootComponent18.contextProvider, provider23, 1));
            this.provideActivityManagerWrapperProvider = DoubleCheck.provider(new ActionClickLogger_Factory(dependencyProvider, 5));
            this.provideDevicePolicyManagerWrapperProvider = DoubleCheck.provider(new KeyboardUI_Factory(dependencyProvider, 9));
            Provider<KeyguardDismissUtil> provider24 = DoubleCheck.provider(KeyguardDismissUtil_Factory.InstanceHolder.INSTANCE);
            this.keyguardDismissUtilProvider = provider24;
            this.smartReplyInflaterImplProvider = DefaultUiController_Factory.create$2(this.smartReplyConstantsProvider, provider24, this.provideNotificationRemoteInputManagerProvider, this.provideSmartReplyControllerProvider, DaggerTitanGlobalRootComponent.this.contextProvider);
            this.provideActivityStarterProvider = new DelegateFactory();
            Provider<LogBuffer> m2 = DaggerGlobalRootComponent$SysUIComponentImpl$KeyguardBouncerComponentImpl$$ExternalSyntheticOutline0.m(this.logBufferFactoryProvider, 3);
            this.provideNotificationHeadsUpLogBufferProvider = m2;
            this.headsUpManagerLoggerProvider = new ImageExporter_Factory(m2, 5);
            this.keyguardUpdateMonitorProvider = new DelegateFactory();
            this.keyguardStateControllerImplProvider = new DelegateFactory();
            this.newKeyguardViewMediatorProvider = new DelegateFactory();
            this.statusBarKeyguardViewManagerProvider = new DelegateFactory();
            this.provideAlwaysOnDisplayPolicyProvider = DoubleCheck.provider(new DiagonalClassifier_Factory(dependencyProvider, DaggerTitanGlobalRootComponent.this.contextProvider));
            this.sysUIUnfoldComponentFactoryProvider = new Provider<SysUIUnfoldComponent.Factory>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.1
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final SysUIUnfoldComponent.Factory mo144get() {
                    return new SysUIUnfoldComponentFactory();
                }
            };
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent19 = DaggerTitanGlobalRootComponent.this;
            this.provideSysUIUnfoldComponentProvider = DoubleCheck.provider(new SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory(sysUIUnfoldModule, daggerTitanGlobalRootComponent19.unfoldTransitionProgressProvider, daggerTitanGlobalRootComponent19.provideNaturalRotationProgressProvider, daggerTitanGlobalRootComponent19.provideStatusBarScopedTransitionProvider, this.sysUIUnfoldComponentFactoryProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent20 = DaggerTitanGlobalRootComponent.this;
            this.wakefulnessLifecycleProvider = DoubleCheck.provider(new WakefulnessLifecycle_Factory(daggerTitanGlobalRootComponent20.contextProvider, daggerTitanGlobalRootComponent20.dumpManagerProvider));
            this.dozeParametersProvider = new DelegateFactory();
            Provider<Context> provider25 = DaggerTitanGlobalRootComponent.this.contextProvider;
            Provider<WakefulnessLifecycle> provider26 = this.wakefulnessLifecycleProvider;
            Provider<StatusBarStateControllerImpl> provider27 = this.statusBarStateControllerImplProvider;
            Provider<KeyguardViewMediator> provider28 = this.newKeyguardViewMediatorProvider;
            Provider<KeyguardStateControllerImpl> provider29 = this.keyguardStateControllerImplProvider;
            Provider<DozeParameters> provider30 = this.dozeParametersProvider;
            Provider provider31 = this.globalSettingsImplProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent21 = DaggerTitanGlobalRootComponent.this;
            Provider<UnlockedScreenOffAnimationController> provider32 = DoubleCheck.provider(UnlockedScreenOffAnimationController_Factory.create(provider25, provider26, provider27, provider28, provider29, provider30, provider31, daggerTitanGlobalRootComponent21.provideInteractionJankMonitorProvider, daggerTitanGlobalRootComponent21.providePowerManagerProvider, this.provideHandlerProvider));
            this.unlockedScreenOffAnimationControllerProvider = provider32;
            this.screenOffAnimationControllerProvider = DoubleCheck.provider(new UdfpsHapticsSimulator_Factory(this.provideSysUIUnfoldComponentProvider, provider32, this.wakefulnessLifecycleProvider, 1));
            DelegateFactory.setDelegate(this.dozeParametersProvider, DoubleCheck.provider(DozeParameters_Factory.create(DaggerTitanGlobalRootComponent.this.provideResourcesProvider, this.provideAmbientDisplayConfigurationProvider, this.provideAlwaysOnDisplayPolicyProvider, DaggerTitanGlobalRootComponent.this.providePowerManagerProvider, this.provideBatteryControllerProvider, this.tunerServiceImplProvider, DaggerTitanGlobalRootComponent.this.dumpManagerProvider, this.featureFlagsReleaseProvider, this.screenOffAnimationControllerProvider, this.provideSysUIUnfoldComponentProvider, this.unlockedScreenOffAnimationControllerProvider, this.keyguardUpdateMonitorProvider, this.provideConfigurationControllerProvider, this.statusBarStateControllerImplProvider)));
            Provider<LogBuffer> m3 = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline3.m(this.logBufferFactoryProvider, 3);
            this.provideDozeLogBufferProvider = m3;
            this.dozeLoggerProvider = new DozeLogger_Factory(m3, 0);
        }

        public final void initialize2(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<Object> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            Provider<DozeLog> provider = DoubleCheck.provider(new DozeLog_Factory(this.keyguardUpdateMonitorProvider, DaggerTitanGlobalRootComponent.this.dumpManagerProvider, this.dozeLoggerProvider, 0));
            this.dozeLogProvider = provider;
            this.dozeScrimControllerProvider = DoubleCheck.provider(new SetupWizard_Factory(this.dozeParametersProvider, provider, this.statusBarStateControllerImplProvider, 1));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
            this.darkIconDispatcherImplProvider = DoubleCheck.provider(new DarkIconDispatcherImpl_Factory(daggerTitanGlobalRootComponent.contextProvider, this.provideCommandQueueProvider, daggerTitanGlobalRootComponent.dumpManagerProvider, 0));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent2 = DaggerTitanGlobalRootComponent.this;
            SecureSettingsImpl_Factory secureSettingsImpl_Factory = new SecureSettingsImpl_Factory(daggerTitanGlobalRootComponent2.provideContentResolverProvider, 0);
            this.secureSettingsImplProvider = secureSettingsImpl_Factory;
            Provider<DeviceProvisionedControllerImpl> provider2 = DoubleCheck.provider(DeviceProvisionedControllerImpl_Factory.create(secureSettingsImpl_Factory, this.globalSettingsImplProvider, this.provideUserTrackerProvider, daggerTitanGlobalRootComponent2.dumpManagerProvider, this.provideBgHandlerProvider, daggerTitanGlobalRootComponent2.provideMainExecutorProvider));
            this.deviceProvisionedControllerImplProvider = provider2;
            this.provideDeviceProvisionedControllerProvider = DoubleCheck.provider(new WMShellBaseModule_ProvideRecentTasksFactory(provider2, 5));
            Provider<Executor> provider3 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory.InstanceHolder.INSTANCE);
            this.provideUiBackgroundExecutorProvider = provider3;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent3 = DaggerTitanGlobalRootComponent.this;
            Provider<NavigationModeController> provider4 = DoubleCheck.provider(NavigationModeController_Factory.create$1(daggerTitanGlobalRootComponent3.contextProvider, this.provideDeviceProvisionedControllerProvider, this.provideConfigurationControllerProvider, provider3, daggerTitanGlobalRootComponent3.dumpManagerProvider));
            this.navigationModeControllerProvider = provider4;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent4 = DaggerTitanGlobalRootComponent.this;
            Provider<LightBarController> provider5 = DoubleCheck.provider(new C0014LightBarController_Factory(daggerTitanGlobalRootComponent4.contextProvider, this.darkIconDispatcherImplProvider, this.provideBatteryControllerProvider, provider4, daggerTitanGlobalRootComponent4.dumpManagerProvider));
            this.lightBarControllerProvider = provider5;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent5 = DaggerTitanGlobalRootComponent.this;
            DelayedWakeLock_Builder_Factory delayedWakeLock_Builder_Factory = new DelayedWakeLock_Builder_Factory(daggerTitanGlobalRootComponent5.contextProvider);
            this.builderProvider = delayedWakeLock_Builder_Factory;
            DelegateFactory delegateFactory = new DelegateFactory();
            this.provideDockManagerProvider = delegateFactory;
            this.scrimControllerProvider = DoubleCheck.provider(ScrimController_Factory.create(provider5, this.dozeParametersProvider, daggerTitanGlobalRootComponent5.provideAlarmManagerProvider, this.keyguardStateControllerImplProvider, delayedWakeLock_Builder_Factory, this.provideHandlerProvider, this.keyguardUpdateMonitorProvider, delegateFactory, this.provideConfigurationControllerProvider, daggerTitanGlobalRootComponent5.provideMainExecutorProvider, this.screenOffAnimationControllerProvider, this.panelExpansionStateManagerProvider));
            this.keyguardBypassControllerProvider = new DelegateFactory();
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent6 = DaggerTitanGlobalRootComponent.this;
            Provider<SysuiColorExtractor> provider6 = DoubleCheck.provider(new SysuiColorExtractor_Factory(daggerTitanGlobalRootComponent6.contextProvider, this.provideConfigurationControllerProvider, daggerTitanGlobalRootComponent6.dumpManagerProvider, 0));
            this.sysuiColorExtractorProvider = provider6;
            DelegateFactory delegateFactory2 = new DelegateFactory();
            this.authControllerProvider = delegateFactory2;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent7 = DaggerTitanGlobalRootComponent.this;
            this.notificationShadeWindowControllerImplProvider = DoubleCheck.provider(NotificationShadeWindowControllerImpl_Factory.create(daggerTitanGlobalRootComponent7.contextProvider, daggerTitanGlobalRootComponent7.provideWindowManagerProvider, daggerTitanGlobalRootComponent7.provideIActivityManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.provideConfigurationControllerProvider, this.newKeyguardViewMediatorProvider, this.keyguardBypassControllerProvider, provider6, daggerTitanGlobalRootComponent7.dumpManagerProvider, this.keyguardStateControllerImplProvider, this.screenOffAnimationControllerProvider, delegateFactory2));
            this.provideAssistUtilsProvider = DoubleCheck.provider(new AssistModule_ProvideAssistUtilsFactory(DaggerTitanGlobalRootComponent.this.contextProvider, 0));
            DelegateFactory delegateFactory3 = new DelegateFactory();
            this.assistManagerGoogleProvider = delegateFactory3;
            this.timeoutManagerProvider = DoubleCheck.provider(new TimeoutManager_Factory(delegateFactory3, 0));
            this.assistantPresenceHandlerProvider = DoubleCheck.provider(new SingleTapClassifier_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideAssistUtilsProvider, 2));
            Provider<PhoneStateMonitor> provider7 = DoubleCheck.provider(new PhoneStateMonitor_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, this.optionalOfStatusBarProvider, this.bootCompleteCacheImplProvider, this.statusBarStateControllerImplProvider));
            this.phoneStateMonitorProvider = provider7;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent8 = DaggerTitanGlobalRootComponent.this;
            Provider<GoogleAssistLogger> provider8 = DoubleCheck.provider(new GoogleAssistLogger_Factory(daggerTitanGlobalRootComponent8.contextProvider, daggerTitanGlobalRootComponent8.provideUiEventLoggerProvider, this.provideAssistUtilsProvider, provider7, this.assistantPresenceHandlerProvider, 0));
            this.googleAssistLoggerProvider = provider8;
            this.touchInsideHandlerProvider = DoubleCheck.provider(new TouchInsideHandler_Factory(this.assistManagerGoogleProvider, this.navigationModeControllerProvider, provider8, 0));
            this.colorChangeHandlerProvider = DoubleCheck.provider(new ColorChangeHandler_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 0));
            Provider provider9 = DoubleCheck.provider(TouchOutsideHandler_Factory.InstanceHolder.INSTANCE);
            this.touchOutsideHandlerProvider = provider9;
            Provider provider10 = DoubleCheck.provider(new OverlayUiHost_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, provider9, 0));
            this.overlayUiHostProvider = provider10;
            Provider<ViewGroup> provider11 = DoubleCheck.provider(new TypeClassifier_Factory(provider10, 6));
            this.provideParentViewGroupProvider = provider11;
            this.edgeLightsControllerProvider = DoubleCheck.provider(new IconController_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, provider11, this.googleAssistLoggerProvider, 2));
            this.glowControllerProvider = DoubleCheck.provider(new ActionProxyReceiver_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideParentViewGroupProvider, this.touchInsideHandlerProvider, 2));
            DelegateFactory delegateFactory4 = new DelegateFactory();
            this.statusBarGoogleProvider = delegateFactory4;
            this.overlappedElementControllerProvider = DoubleCheck.provider(new GameDashboardUiEventLogger_Factory(delegateFactory4, 2));
            Provider provider12 = DoubleCheck.provider(LightnessProvider_Factory.InstanceHolder.INSTANCE);
            this.lightnessProvider = provider12;
            this.scrimControllerProvider2 = DoubleCheck.provider(new com.google.android.systemui.assist.uihints.ScrimController_Factory(this.provideParentViewGroupProvider, this.overlappedElementControllerProvider, provider12, this.touchInsideHandlerProvider));
            Provider provider13 = DoubleCheck.provider(FlingVelocityWrapper_Factory.InstanceHolder.INSTANCE);
            this.flingVelocityWrapperProvider = provider13;
            this.transcriptionControllerProvider = DoubleCheck.provider(new QSFgsManagerFooter_Factory(this.provideParentViewGroupProvider, this.touchInsideHandlerProvider, provider13, this.provideConfigurationControllerProvider, 2));
            this.iconControllerProvider = DoubleCheck.provider(new IconController_Factory(this.providerLayoutInflaterProvider, this.provideParentViewGroupProvider, this.provideConfigurationControllerProvider, 0));
            this.assistantWarmerProvider = DoubleCheck.provider(new AssistantWarmer_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 0));
            this.navigationBarControllerProvider = new DelegateFactory();
            this.provideSysUiStateProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline1.m(DaggerTitanGlobalRootComponent.this.dumpManagerProvider, 2);
            this.setPipProvider = InstanceFactory.create(optional);
            this.setLegacySplitScreenProvider = InstanceFactory.create(optional2);
            this.setSplitScreenProvider = InstanceFactory.create(optional3);
            this.setOneHandedProvider = InstanceFactory.create(optional5);
            this.setRecentTasksProvider = InstanceFactory.create(optional13);
            this.setBackAnimationProvider = InstanceFactory.create(optional16);
            this.setStartingSurfaceProvider = InstanceFactory.create(optional10);
            InstanceFactory create = InstanceFactory.create(shellTransitions);
            this.setTransitionsProvider = create;
            DelegateFactory delegateFactory5 = new DelegateFactory();
            this.keyguardUnlockAnimationControllerProvider = delegateFactory5;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent9 = DaggerTitanGlobalRootComponent.this;
            this.overviewProxyServiceProvider = DoubleCheck.provider(OverviewProxyService_Factory.create(daggerTitanGlobalRootComponent9.contextProvider, this.provideCommandQueueProvider, this.navigationBarControllerProvider, this.optionalOfStatusBarProvider, this.navigationModeControllerProvider, this.notificationShadeWindowControllerImplProvider, this.provideSysUiStateProvider, this.setPipProvider, this.setLegacySplitScreenProvider, this.setSplitScreenProvider, this.setOneHandedProvider, this.setRecentTasksProvider, this.setBackAnimationProvider, this.setStartingSurfaceProvider, this.providesBroadcastDispatcherProvider, create, daggerTitanGlobalRootComponent9.screenLifecycleProvider, daggerTitanGlobalRootComponent9.provideUiEventLoggerProvider, delegateFactory5, daggerTitanGlobalRootComponent9.dumpManagerProvider));
            this.accessibilityButtonModeObserverProvider = DoubleCheck.provider(new ColorChangeHandler_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 1));
            this.accessibilityButtonTargetsObserverProvider = DoubleCheck.provider(new MediaControllerFactory_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 1));
            DelegateFactory delegateFactory6 = new DelegateFactory();
            this.contextComponentResolverProvider = delegateFactory6;
            CommunalSourceMonitor_Factory communalSourceMonitor_Factory = new CommunalSourceMonitor_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, delegateFactory6, 3);
            this.provideRecentsImplProvider = communalSourceMonitor_Factory;
            Provider<Recents> provider14 = DoubleCheck.provider(new IconController_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, communalSourceMonitor_Factory, this.provideCommandQueueProvider, 3));
            this.provideRecentsProvider = provider14;
            PresentJdkOptionalInstanceProvider presentJdkOptionalInstanceProvider = new PresentJdkOptionalInstanceProvider(provider14);
            this.optionalOfRecentsProvider = presentJdkOptionalInstanceProvider;
            Provider<SystemActions> provider15 = DoubleCheck.provider(new SystemActions_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.notificationShadeWindowControllerImplProvider, this.optionalOfStatusBarProvider, presentJdkOptionalInstanceProvider));
            this.systemActionsProvider = provider15;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent10 = DaggerTitanGlobalRootComponent.this;
            this.navBarHelperProvider = DoubleCheck.provider(NavBarHelper_Factory.create(daggerTitanGlobalRootComponent10.contextProvider, daggerTitanGlobalRootComponent10.provideAccessibilityManagerProvider, this.accessibilityButtonModeObserverProvider, this.accessibilityButtonTargetsObserverProvider, provider15, this.overviewProxyServiceProvider, this.assistManagerGoogleProvider, this.optionalOfStatusBarProvider, this.navigationModeControllerProvider, this.provideUserTrackerProvider, daggerTitanGlobalRootComponent10.dumpManagerProvider));
            this.provideMetricsLoggerProvider = DoubleCheck.provider(new SecureSettingsImpl_Factory(dependencyProvider, 5));
            this.shadeControllerImplProvider = new DelegateFactory();
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent11 = DaggerTitanGlobalRootComponent.this;
            this.blurUtilsProvider = DoubleCheck.provider(new BlurUtils_Factory(daggerTitanGlobalRootComponent11.provideResourcesProvider, daggerTitanGlobalRootComponent11.provideCrossWindowBlurListenersProvider, daggerTitanGlobalRootComponent11.dumpManagerProvider, 0));
            this.biometricUnlockControllerProvider = new DelegateFactory();
            Provider<WallpaperController> provider16 = DoubleCheck.provider(new WallpaperController_Factory(DaggerTitanGlobalRootComponent.this.provideWallpaperManagerProvider, 0));
            this.wallpaperControllerProvider = provider16;
            this.notificationShadeDepthControllerProvider = DoubleCheck.provider(NotificationShadeDepthController_Factory.create(this.statusBarStateControllerImplProvider, this.blurUtilsProvider, this.biometricUnlockControllerProvider, this.keyguardStateControllerImplProvider, this.providesChoreographerProvider, provider16, this.notificationShadeWindowControllerImplProvider, this.dozeParametersProvider, DaggerTitanGlobalRootComponent.this.dumpManagerProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent12 = DaggerTitanGlobalRootComponent.this;
            this.gameModeDndControllerProvider = DoubleCheck.provider(new TileAdapter_Factory(daggerTitanGlobalRootComponent12.contextProvider, daggerTitanGlobalRootComponent12.provideNotificationManagerProvider, this.providesBroadcastDispatcherProvider, 3));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent13 = DaggerTitanGlobalRootComponent.this;
            this.fpsControllerProvider = DoubleCheck.provider(new UserCreator_Factory(daggerTitanGlobalRootComponent13.provideMainExecutorProvider, daggerTitanGlobalRootComponent13.provideWindowManagerProvider, 3));
            this.recordingControllerProvider = DoubleCheck.provider(new UserCreator_Factory(this.providesBroadcastDispatcherProvider, this.provideUserTrackerProvider, 1));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent14 = DaggerTitanGlobalRootComponent.this;
            Provider<ToastController> provider17 = DoubleCheck.provider(new ToastController_Factory(daggerTitanGlobalRootComponent14.contextProvider, this.provideConfigurationControllerProvider, daggerTitanGlobalRootComponent14.provideWindowManagerProvider, daggerTitanGlobalRootComponent14.provideUiEventLoggerProvider, this.navigationModeControllerProvider, 0));
            this.toastControllerProvider = provider17;
            this.screenRecordControllerProvider = DoubleCheck.provider(new ScreenRecordController_Factory(this.recordingControllerProvider, this.provideHandlerProvider, this.keyguardDismissUtilProvider, DaggerTitanGlobalRootComponent.this.contextProvider, provider17, 0));
            this.setTaskSurfaceHelperProvider = InstanceFactory.create(optional12);
            GameDashboardUiEventLogger_Factory gameDashboardUiEventLogger_Factory = new GameDashboardUiEventLogger_Factory(DaggerTitanGlobalRootComponent.this.provideUiEventLoggerProvider, 0);
            this.gameDashboardUiEventLoggerProvider = gameDashboardUiEventLogger_Factory;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent15 = DaggerTitanGlobalRootComponent.this;
            Provider<ShortcutBarController> provider18 = DoubleCheck.provider(new ShortcutBarController_Factory(daggerTitanGlobalRootComponent15.contextProvider, daggerTitanGlobalRootComponent15.provideWindowManagerProvider, this.fpsControllerProvider, this.provideConfigurationControllerProvider, this.provideHandlerProvider, this.screenRecordControllerProvider, this.setTaskSurfaceHelperProvider, gameDashboardUiEventLogger_Factory, this.toastControllerProvider));
            this.shortcutBarControllerProvider = provider18;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent16 = DaggerTitanGlobalRootComponent.this;
            this.entryPointControllerProvider = DoubleCheck.provider(EntryPointController_Factory.create(daggerTitanGlobalRootComponent16.contextProvider, daggerTitanGlobalRootComponent16.provideAccessibilityManagerProvider, this.providesBroadcastDispatcherProvider, this.provideCommandQueueProvider, this.gameModeDndControllerProvider, daggerTitanGlobalRootComponent16.provideMainHandlerProvider, this.navigationModeControllerProvider, this.setLegacySplitScreenProvider, this.overviewProxyServiceProvider, daggerTitanGlobalRootComponent16.providePackageManagerProvider, provider18, this.toastControllerProvider, this.gameDashboardUiEventLoggerProvider, this.setTaskSurfaceHelperProvider));
            Provider<DarkIconDispatcherImpl> provider19 = this.darkIconDispatcherImplProvider;
            Provider<BatteryController> provider20 = this.provideBatteryControllerProvider;
            Provider<NavigationModeController> provider21 = this.navigationModeControllerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent17 = DaggerTitanGlobalRootComponent.this;
            this.factoryProvider = new LightBarController_Factory_Factory(provider19, provider20, provider21, daggerTitanGlobalRootComponent17.dumpManagerProvider);
            this.provideAutoHideControllerProvider = DoubleCheck.provider(new DoubleTapClassifier_Factory(dependencyProvider, daggerTitanGlobalRootComponent17.contextProvider, daggerTitanGlobalRootComponent17.provideMainHandlerProvider, daggerTitanGlobalRootComponent17.provideIWindowManagerProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent18 = DaggerTitanGlobalRootComponent.this;
            Provider<Handler> provider22 = daggerTitanGlobalRootComponent18.provideMainHandlerProvider;
            AutoHideController_Factory_Factory autoHideController_Factory_Factory = new AutoHideController_Factory_Factory(provider22, daggerTitanGlobalRootComponent18.provideIWindowManagerProvider);
            this.factoryProvider2 = autoHideController_Factory_Factory;
            Provider<AssistManagerGoogle> provider23 = this.assistManagerGoogleProvider;
            Provider<AccessibilityManager> provider24 = daggerTitanGlobalRootComponent18.provideAccessibilityManagerProvider;
            Provider<DeviceProvisionedController> provider25 = this.provideDeviceProvisionedControllerProvider;
            Provider<MetricsLogger> provider26 = this.provideMetricsLoggerProvider;
            Provider<OverviewProxyService> provider27 = this.overviewProxyServiceProvider;
            Provider<NavigationModeController> provider28 = this.navigationModeControllerProvider;
            Provider<AccessibilityButtonModeObserver> provider29 = this.accessibilityButtonModeObserverProvider;
            Provider<StatusBarStateControllerImpl> provider30 = this.statusBarStateControllerImplProvider;
            Provider<SysUiState> provider31 = this.provideSysUiStateProvider;
            Provider<BroadcastDispatcher> provider32 = this.providesBroadcastDispatcherProvider;
            Provider<CommandQueue> provider33 = this.provideCommandQueueProvider;
            Provider<Optional<Pip>> provider34 = this.setPipProvider;
            Provider<Optional<LegacySplitScreen>> provider35 = this.setLegacySplitScreenProvider;
            Provider<Optional<Recents>> provider36 = this.optionalOfRecentsProvider;
            Provider<Optional<StatusBar>> provider37 = this.optionalOfStatusBarProvider;
            Provider<ShadeControllerImpl> provider38 = this.shadeControllerImplProvider;
            Provider<NotificationRemoteInputManager> provider39 = this.provideNotificationRemoteInputManagerProvider;
            Provider<NotificationShadeDepthController> provider40 = this.notificationShadeDepthControllerProvider;
            Provider<EntryPointController> provider41 = this.entryPointControllerProvider;
            Provider<UiEventLogger> provider42 = daggerTitanGlobalRootComponent18.provideUiEventLoggerProvider;
            Provider<NavBarHelper> provider43 = this.navBarHelperProvider;
            Provider<LightBarController> provider44 = this.lightBarControllerProvider;
            Provider<LightBarController.Factory> provider45 = this.factoryProvider;
            Provider<AutoHideController> provider46 = this.provideAutoHideControllerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent19 = DaggerTitanGlobalRootComponent.this;
            NavigationBar_Factory_Factory create2 = NavigationBar_Factory_Factory.create(provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32, provider33, provider34, provider35, provider36, provider37, provider38, provider39, provider40, provider22, provider41, provider42, provider43, provider44, provider45, provider46, autoHideController_Factory_Factory, daggerTitanGlobalRootComponent19.provideOptionalTelecomManagerProvider, daggerTitanGlobalRootComponent19.provideInputMethodManagerProvider, this.setBackAnimationProvider);
            this.factoryProvider3 = create2;
            Provider<NavigationBarController> provider47 = this.navigationBarControllerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent20 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider47, DoubleCheck.provider(NavigationBarController_Factory.create(daggerTitanGlobalRootComponent20.contextProvider, this.overviewProxyServiceProvider, this.navigationModeControllerProvider, this.provideSysUiStateProvider, this.provideCommandQueueProvider, daggerTitanGlobalRootComponent20.provideMainHandlerProvider, this.provideConfigurationControllerProvider, this.navBarHelperProvider, daggerTitanGlobalRootComponent20.taskbarDelegateProvider, create2, this.statusBarKeyguardViewManagerProvider, daggerTitanGlobalRootComponent20.dumpManagerProvider, this.provideAutoHideControllerProvider, this.lightBarControllerProvider, this.setPipProvider, this.setBackAnimationProvider)));
            Provider<NavBarFader> provider48 = DoubleCheck.provider(new DistanceClassifier_Factory(this.navigationBarControllerProvider, DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider, 1));
            this.navBarFaderProvider = provider48;
            this.ngaUiControllerProvider = DoubleCheck.provider(NgaUiController_Factory.create(DaggerTitanGlobalRootComponent.this.contextProvider, this.timeoutManagerProvider, this.assistantPresenceHandlerProvider, this.touchInsideHandlerProvider, this.colorChangeHandlerProvider, this.overlayUiHostProvider, this.edgeLightsControllerProvider, this.glowControllerProvider, this.scrimControllerProvider2, this.transcriptionControllerProvider, this.iconControllerProvider, this.lightnessProvider, this.statusBarStateControllerImplProvider, this.assistManagerGoogleProvider, this.flingVelocityWrapperProvider, this.assistantWarmerProvider, provider48, this.googleAssistLoggerProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent21 = DaggerTitanGlobalRootComponent.this;
            this.opaEnabledReceiverProvider = DoubleCheck.provider(new OpaEnabledReceiver_Factory(daggerTitanGlobalRootComponent21.contextProvider, this.providesBroadcastDispatcherProvider, daggerTitanGlobalRootComponent21.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, daggerTitanGlobalRootComponent21.opaEnabledSettingsProvider, 0));
            this.opaEnabledDispatcherProvider = new SeekBarViewModel_Factory(this.statusBarGoogleProvider, 2);
            int i = SetFactory.$r8$clinit;
            ArrayList arrayList = new ArrayList(1);
            this.setOfKeepAliveListenerProvider = BouncerSwipeModule$$ExternalSyntheticLambda0.m(arrayList, this.timeoutManagerProvider, arrayList, Collections.emptyList());
            this.provideAudioInfoListenersProvider = new AssistantUIHintsModule_ProvideAudioInfoListenersFactory(this.edgeLightsControllerProvider, this.glowControllerProvider);
            List emptyList = Collections.emptyList();
            ArrayList arrayList2 = new ArrayList(1);
            this.setOfAudioInfoListenerProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline0.m(arrayList2, this.provideAudioInfoListenersProvider, emptyList, arrayList2);
            this.provideCardInfoListenersProvider = new AssistantUIHintsModule_ProvideCardInfoListenersFactory(this.glowControllerProvider, this.scrimControllerProvider2, this.transcriptionControllerProvider, this.lightnessProvider);
            List emptyList2 = Collections.emptyList();
            ArrayList arrayList3 = new ArrayList(1);
            this.setOfCardInfoListenerProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline0.m(arrayList3, this.provideCardInfoListenersProvider, emptyList2, arrayList3);
            this.taskStackNotifierProvider = DoubleCheck.provider(TaskStackNotifier_Factory.InstanceHolder.INSTANCE);
            PresentJdkOptionalInstanceProvider presentJdkOptionalInstanceProvider2 = new PresentJdkOptionalInstanceProvider(this.provideCommandQueueProvider);
            this.optionalOfCommandQueueProvider = presentJdkOptionalInstanceProvider2;
            this.keyboardMonitorProvider = DoubleCheck.provider(new FeatureFlagsRelease_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, presentJdkOptionalInstanceProvider2, 3));
            Provider<ConfigurationHandler> provider49 = DoubleCheck.provider(new MediaControllerFactory_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 6));
            this.configurationHandlerProvider = provider49;
            this.provideConfigInfoListenersProvider = new AssistantUIHintsModule_ProvideConfigInfoListenersFactory(this.assistantPresenceHandlerProvider, this.touchInsideHandlerProvider, this.touchOutsideHandlerProvider, this.taskStackNotifierProvider, this.keyboardMonitorProvider, this.colorChangeHandlerProvider, provider49);
            List emptyList3 = Collections.emptyList();
            ArrayList arrayList4 = new ArrayList(1);
            this.setOfConfigInfoListenerProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline0.m(arrayList4, this.provideConfigInfoListenersProvider, emptyList3, arrayList4);
            this.provideTouchActionRegionsProvider = new InputModule_ProvideTouchActionRegionsFactory(this.iconControllerProvider, this.transcriptionControllerProvider);
            List emptyList4 = Collections.emptyList();
            ArrayList arrayList5 = new ArrayList(1);
            this.setOfTouchActionRegionProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline0.m(arrayList5, this.provideTouchActionRegionsProvider, emptyList4, arrayList5);
            this.provideTouchInsideRegionsProvider = new BlurUtils_Factory(this.glowControllerProvider, this.scrimControllerProvider2, this.transcriptionControllerProvider, 1);
            List emptyList5 = Collections.emptyList();
            ArrayList arrayList6 = new ArrayList(1);
            SetFactory m = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline0.m(arrayList6, this.provideTouchInsideRegionsProvider, emptyList5, arrayList6);
            this.setOfTouchInsideRegionProvider = m;
            Provider<NgaInputHandler> provider50 = DoubleCheck.provider(new NgaInputHandler_Factory(this.touchInsideHandlerProvider, this.setOfTouchActionRegionProvider, m));
            this.ngaInputHandlerProvider = provider50;
            this.bindEdgeLightsInfoListenersProvider = new AssistantUIHintsModule_BindEdgeLightsInfoListenersFactory(this.edgeLightsControllerProvider, provider50);
            List emptyList6 = Collections.emptyList();
            ArrayList arrayList7 = new ArrayList(1);
            this.setOfEdgeLightsInfoListenerProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline0.m(arrayList7, this.bindEdgeLightsInfoListenersProvider, emptyList6, arrayList7);
            ArrayList arrayList8 = new ArrayList(1);
            this.setOfTranscriptionInfoListenerProvider = BouncerSwipeModule$$ExternalSyntheticLambda0.m(arrayList8, this.transcriptionControllerProvider, arrayList8, Collections.emptyList());
        }

        public final void initialize3(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<Object> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            int i = SetFactory.$r8$clinit;
            ArrayList arrayList = new ArrayList(1);
            this.setOfGreetingInfoListenerProvider = BouncerSwipeModule$$ExternalSyntheticLambda0.m(arrayList, this.transcriptionControllerProvider, arrayList, Collections.emptyList());
            ArrayList arrayList2 = new ArrayList(1);
            this.setOfChipsInfoListenerProvider = BouncerSwipeModule$$ExternalSyntheticLambda0.m(arrayList2, this.transcriptionControllerProvider, arrayList2, Collections.emptyList());
            ArrayList arrayList3 = new ArrayList(1);
            this.setOfClearListenerProvider = BouncerSwipeModule$$ExternalSyntheticLambda0.m(arrayList3, this.transcriptionControllerProvider, arrayList3, Collections.emptyList());
            this.provideActivityStarterProvider2 = new AssistantUIHintsModule_ProvideActivityStarterFactory(this.statusBarGoogleProvider);
            ArrayList arrayList4 = new ArrayList(1);
            this.setOfStartActivityInfoListenerProvider = BouncerSwipeModule$$ExternalSyntheticLambda0.m(arrayList4, this.provideActivityStarterProvider2, arrayList4, Collections.emptyList());
            ArrayList arrayList5 = new ArrayList(1);
            this.setOfKeyboardInfoListenerProvider = BouncerSwipeModule$$ExternalSyntheticLambda0.m(arrayList5, this.iconControllerProvider, arrayList5, Collections.emptyList());
            ArrayList arrayList6 = new ArrayList(1);
            this.setOfZerostateInfoListenerProvider = BouncerSwipeModule$$ExternalSyntheticLambda0.m(arrayList6, this.iconControllerProvider, arrayList6, Collections.emptyList());
            this.goBackHandlerProvider = DoubleCheck.provider(GoBackHandler_Factory.InstanceHolder.INSTANCE);
            ArrayList arrayList7 = new ArrayList(1);
            this.setOfGoBackListenerProvider = BouncerSwipeModule$$ExternalSyntheticLambda0.m(arrayList7, this.goBackHandlerProvider, arrayList7, Collections.emptyList());
            this.swipeHandlerProvider = DoubleCheck.provider(SwipeHandler_Factory.InstanceHolder.INSTANCE);
            ArrayList arrayList8 = new ArrayList(1);
            this.setOfSwipeListenerProvider = BouncerSwipeModule$$ExternalSyntheticLambda0.m(arrayList8, this.swipeHandlerProvider, arrayList8, Collections.emptyList());
            this.takeScreenshotHandlerProvider = DoubleCheck.provider(new AssistantWarmer_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 4));
            ArrayList arrayList9 = new ArrayList(1);
            this.setOfTakeScreenshotListenerProvider = BouncerSwipeModule$$ExternalSyntheticLambda0.m(arrayList9, this.takeScreenshotHandlerProvider, arrayList9, Collections.emptyList());
            ArrayList arrayList10 = new ArrayList(1);
            this.setOfWarmingListenerProvider = BouncerSwipeModule$$ExternalSyntheticLambda0.m(arrayList10, this.assistantWarmerProvider, arrayList10, Collections.emptyList());
            ArrayList arrayList11 = new ArrayList(1);
            SetFactory m = BouncerSwipeModule$$ExternalSyntheticLambda0.m(arrayList11, this.navBarFaderProvider, arrayList11, Collections.emptyList());
            this.setOfNavBarVisibilityListenerProvider = m;
            this.ngaMessageHandlerProvider = DoubleCheck.provider(NgaMessageHandler_Factory.create(this.ngaUiControllerProvider, this.assistantPresenceHandlerProvider, this.navigationModeControllerProvider, this.setOfKeepAliveListenerProvider, this.setOfAudioInfoListenerProvider, this.setOfCardInfoListenerProvider, this.setOfConfigInfoListenerProvider, this.setOfEdgeLightsInfoListenerProvider, this.setOfTranscriptionInfoListenerProvider, this.setOfGreetingInfoListenerProvider, this.setOfChipsInfoListenerProvider, this.setOfClearListenerProvider, this.setOfStartActivityInfoListenerProvider, this.setOfKeyboardInfoListenerProvider, this.setOfZerostateInfoListenerProvider, this.setOfGoBackListenerProvider, this.setOfSwipeListenerProvider, this.setOfTakeScreenshotListenerProvider, this.setOfWarmingListenerProvider, m, DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
            this.defaultUiControllerProvider = DoubleCheck.provider(DefaultUiController_Factory.create(daggerTitanGlobalRootComponent.contextProvider, this.googleAssistLoggerProvider, daggerTitanGlobalRootComponent.provideWindowManagerProvider, this.provideMetricsLoggerProvider, this.assistManagerGoogleProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent2 = DaggerTitanGlobalRootComponent.this;
            Provider<GoogleDefaultUiController> provider = DoubleCheck.provider(new PrivacyDotViewController_Factory(daggerTitanGlobalRootComponent2.contextProvider, this.googleAssistLoggerProvider, daggerTitanGlobalRootComponent2.provideWindowManagerProvider, this.provideMetricsLoggerProvider, this.assistManagerGoogleProvider, 1));
            this.googleDefaultUiControllerProvider = provider;
            Provider<AssistManagerGoogle> provider2 = this.assistManagerGoogleProvider;
            Provider<DeviceProvisionedController> provider3 = this.provideDeviceProvisionedControllerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent3 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider2, DoubleCheck.provider(AssistManagerGoogle_Factory.create(provider3, daggerTitanGlobalRootComponent3.contextProvider, this.provideAssistUtilsProvider, this.ngaUiControllerProvider, this.provideCommandQueueProvider, this.opaEnabledReceiverProvider, this.phoneStateMonitorProvider, this.overviewProxyServiceProvider, this.opaEnabledDispatcherProvider, this.keyguardUpdateMonitorProvider, this.navigationModeControllerProvider, this.assistantPresenceHandlerProvider, this.ngaMessageHandlerProvider, this.provideSysUiStateProvider, daggerTitanGlobalRootComponent3.provideMainHandlerProvider, this.defaultUiControllerProvider, provider, daggerTitanGlobalRootComponent3.provideIWindowManagerProvider, this.googleAssistLoggerProvider)));
            DelegateFactory.setDelegate(this.shadeControllerImplProvider, DoubleCheck.provider(ShadeControllerImpl_Factory.create(this.provideCommandQueueProvider, this.statusBarStateControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarKeyguardViewManagerProvider, DaggerTitanGlobalRootComponent.this.provideWindowManagerProvider, this.optionalOfStatusBarProvider, this.assistManagerGoogleProvider, this.setBubblesProvider)));
            this.mediaArtworkProcessorProvider = DoubleCheck.provider(MediaArtworkProcessor_Factory.InstanceHolder.INSTANCE);
            this.notifPipelineProvider = new DelegateFactory();
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent4 = DaggerTitanGlobalRootComponent.this;
            MediaControllerFactory_Factory mediaControllerFactory_Factory = new MediaControllerFactory_Factory(daggerTitanGlobalRootComponent4.contextProvider, 0);
            this.mediaControllerFactoryProvider = mediaControllerFactory_Factory;
            this.mediaTimeoutListenerProvider = DoubleCheck.provider(new FeatureFlagsRelease_Factory(mediaControllerFactory_Factory, daggerTitanGlobalRootComponent4.provideMainDelayableExecutorProvider, 1));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent5 = DaggerTitanGlobalRootComponent.this;
            Provider<Context> provider4 = daggerTitanGlobalRootComponent5.contextProvider;
            MediaBrowserFactory_Factory mediaBrowserFactory_Factory = new MediaBrowserFactory_Factory(provider4, 0);
            this.mediaBrowserFactoryProvider = mediaBrowserFactory_Factory;
            ResumeMediaBrowserFactory_Factory resumeMediaBrowserFactory_Factory = new ResumeMediaBrowserFactory_Factory(provider4, mediaBrowserFactory_Factory, 0);
            this.resumeMediaBrowserFactoryProvider = resumeMediaBrowserFactory_Factory;
            this.mediaResumeListenerProvider = DoubleCheck.provider(MediaResumeListener_Factory.create(provider4, this.providesBroadcastDispatcherProvider, this.provideBackgroundExecutorProvider, this.tunerServiceImplProvider, resumeMediaBrowserFactory_Factory, daggerTitanGlobalRootComponent5.dumpManagerProvider, this.bindSystemClockProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent6 = DaggerTitanGlobalRootComponent.this;
            Provider<Context> provider5 = daggerTitanGlobalRootComponent6.contextProvider;
            this.mediaSessionBasedFilterProvider = new MediaSessionBasedFilter_Factory(provider5, daggerTitanGlobalRootComponent6.provideMediaSessionManagerProvider, daggerTitanGlobalRootComponent6.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, 0);
            Provider<LocalBluetoothManager> provider6 = DoubleCheck.provider(new QSFragmentModule_ProvidesQSFooterActionsViewFactory(provider5, this.provideBgHandlerProvider, 1));
            this.provideLocalBluetoothControllerProvider = provider6;
            this.localMediaManagerFactoryProvider = new CommunalSourceMonitor_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, provider6, 2);
            Provider<MediaFlags> provider7 = DoubleCheck.provider(new MediaFlags_Factory(this.featureFlagsReleaseProvider, 0));
            this.mediaFlagsProvider = provider7;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent7 = DaggerTitanGlobalRootComponent.this;
            Provider<MediaMuteAwaitConnectionManagerFactory> provider8 = DoubleCheck.provider(new ColumbusStructuredDataManager_Factory(provider7, daggerTitanGlobalRootComponent7.contextProvider, daggerTitanGlobalRootComponent7.provideMainExecutorProvider, 1));
            this.mediaMuteAwaitConnectionManagerFactoryProvider = provider8;
            Provider<MediaControllerFactory> provider9 = this.mediaControllerFactoryProvider;
            Provider<LocalMediaManagerFactory> provider10 = this.localMediaManagerFactoryProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent8 = DaggerTitanGlobalRootComponent.this;
            this.mediaDeviceManagerProvider = MediaDeviceManager_Factory.create(provider9, provider10, daggerTitanGlobalRootComponent8.provideMediaRouter2ManagerProvider, provider8, daggerTitanGlobalRootComponent8.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, daggerTitanGlobalRootComponent8.dumpManagerProvider);
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent9 = DaggerTitanGlobalRootComponent.this;
            MediaDataFilter_Factory create = MediaDataFilter_Factory.create(daggerTitanGlobalRootComponent9.contextProvider, this.providesBroadcastDispatcherProvider, this.notificationLockscreenUserManagerGoogleProvider, daggerTitanGlobalRootComponent9.provideMainExecutorProvider, this.bindSystemClockProvider);
            this.mediaDataFilterProvider = create;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent10 = DaggerTitanGlobalRootComponent.this;
            Provider<MediaDataManager> provider11 = DoubleCheck.provider(MediaDataManager_Factory.create(daggerTitanGlobalRootComponent10.contextProvider, this.provideBackgroundExecutorProvider, daggerTitanGlobalRootComponent10.provideMainDelayableExecutorProvider, this.mediaControllerFactoryProvider, daggerTitanGlobalRootComponent10.dumpManagerProvider, this.providesBroadcastDispatcherProvider, this.mediaTimeoutListenerProvider, this.mediaResumeListenerProvider, this.mediaSessionBasedFilterProvider, this.mediaDeviceManagerProvider, create, this.provideActivityStarterProvider, this.bindSystemClockProvider, this.tunerServiceImplProvider, this.mediaFlagsProvider));
            this.mediaDataManagerProvider = provider11;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent11 = DaggerTitanGlobalRootComponent.this;
            this.provideNotificationMediaManagerProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideNotificationMediaManagerFactory.create(daggerTitanGlobalRootComponent11.contextProvider, this.optionalOfStatusBarProvider, this.notificationShadeWindowControllerImplProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.mediaArtworkProcessorProvider, this.keyguardBypassControllerProvider, this.notifPipelineProvider, this.notifCollectionProvider, this.notifPipelineFlagsProvider, daggerTitanGlobalRootComponent11.provideMainDelayableExecutorProvider, provider11, daggerTitanGlobalRootComponent11.dumpManagerProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent12 = DaggerTitanGlobalRootComponent.this;
            Provider<SessionTracker> provider12 = DoubleCheck.provider(new SessionTracker_Factory(daggerTitanGlobalRootComponent12.contextProvider, daggerTitanGlobalRootComponent12.provideIStatusBarServiceProvider, this.authControllerProvider, this.keyguardUpdateMonitorProvider, this.keyguardStateControllerImplProvider));
            this.sessionTrackerProvider = provider12;
            Provider<BiometricUnlockController> provider13 = this.biometricUnlockControllerProvider;
            Provider<DozeScrimController> provider14 = this.dozeScrimControllerProvider;
            Provider<KeyguardViewMediator> provider15 = this.newKeyguardViewMediatorProvider;
            Provider<ScrimController> provider16 = this.scrimControllerProvider;
            Provider<ShadeControllerImpl> provider17 = this.shadeControllerImplProvider;
            Provider<NotificationShadeWindowControllerImpl> provider18 = this.notificationShadeWindowControllerImplProvider;
            Provider<KeyguardStateControllerImpl> provider19 = this.keyguardStateControllerImplProvider;
            Provider<Handler> provider20 = this.provideHandlerProvider;
            Provider<KeyguardUpdateMonitor> provider21 = this.keyguardUpdateMonitorProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent13 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider13, DoubleCheck.provider(BiometricUnlockController_Factory.create(provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, daggerTitanGlobalRootComponent13.provideResourcesProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.provideMetricsLoggerProvider, daggerTitanGlobalRootComponent13.dumpManagerProvider, daggerTitanGlobalRootComponent13.providePowerManagerProvider, this.provideNotificationMediaManagerProvider, this.wakefulnessLifecycleProvider, daggerTitanGlobalRootComponent13.screenLifecycleProvider, this.authControllerProvider, this.statusBarStateControllerImplProvider, this.keyguardUnlockAnimationControllerProvider, provider12, daggerTitanGlobalRootComponent13.provideLatencyTrackerProvider)));
            DelegateFactory.setDelegate(this.keyguardUnlockAnimationControllerProvider, DoubleCheck.provider(WMShellModule_ProvideAppPairsFactory.create(DaggerTitanGlobalRootComponent.this.contextProvider, this.keyguardStateControllerImplProvider, this.newKeyguardViewMediatorProvider, this.statusBarKeyguardViewManagerProvider, this.featureFlagsReleaseProvider, this.biometricUnlockControllerProvider)));
            Provider<KeyguardStateControllerImpl> provider22 = this.keyguardStateControllerImplProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent14 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider22, DoubleCheck.provider(GoogleAssistLogger_Factory.create$1(daggerTitanGlobalRootComponent14.contextProvider, this.keyguardUpdateMonitorProvider, this.provideLockPatternUtilsProvider, this.keyguardUnlockAnimationControllerProvider, daggerTitanGlobalRootComponent14.dumpManagerProvider)));
            Provider<KeyguardBypassController> provider23 = this.keyguardBypassControllerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent15 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider23, DoubleCheck.provider(StatusBarDemoMode_Factory.create(daggerTitanGlobalRootComponent15.contextProvider, this.tunerServiceImplProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerGoogleProvider, this.keyguardStateControllerImplProvider, daggerTitanGlobalRootComponent15.dumpManagerProvider)));
            Provider<VisualStabilityProvider> provider24 = DoubleCheck.provider(VisualStabilityProvider_Factory.InstanceHolder.INSTANCE);
            this.visualStabilityProvider = provider24;
            Provider<HeadsUpManagerPhone> provider25 = DoubleCheck.provider(new SystemUIGoogleModule_ProvideHeadsUpManagerPhoneFactory(DaggerTitanGlobalRootComponent.this.contextProvider, this.headsUpManagerLoggerProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.provideGroupMembershipManagerProvider, provider24, this.provideConfigurationControllerProvider));
            this.provideHeadsUpManagerPhoneProvider = provider25;
            Provider<SmartReplyConstants> provider26 = this.smartReplyConstantsProvider;
            SmartActionInflaterImpl_Factory smartActionInflaterImpl_Factory = new SmartActionInflaterImpl_Factory(provider26, this.provideActivityStarterProvider, this.provideSmartReplyControllerProvider, provider25);
            this.smartActionInflaterImplProvider = smartActionInflaterImpl_Factory;
            SmartReplyStateInflaterImpl_Factory create2 = SmartReplyStateInflaterImpl_Factory.create(provider26, this.provideActivityManagerWrapperProvider, DaggerTitanGlobalRootComponent.this.providePackageManagerWrapperProvider, this.provideDevicePolicyManagerWrapperProvider, this.smartReplyInflaterImplProvider, smartActionInflaterImpl_Factory);
            this.smartReplyStateInflaterImplProvider = create2;
            this.notificationContentInflaterProvider = DoubleCheck.provider(NotificationContentInflater_Factory.create(this.provideNotifRemoteViewCacheProvider, this.provideNotificationRemoteInputManagerProvider, this.conversationNotificationProcessorProvider, this.mediaFeatureFlagProvider, this.provideBackgroundExecutorProvider, create2));
            this.notifInflationErrorManagerProvider = DoubleCheck.provider(NotifInflationErrorManager_Factory.InstanceHolder.INSTANCE);
            WMShellBaseModule_ProvideSplitScreenFactory wMShellBaseModule_ProvideSplitScreenFactory = new WMShellBaseModule_ProvideSplitScreenFactory(this.provideNotificationsLogBufferProvider, 5);
            this.rowContentBindStageLoggerProvider = wMShellBaseModule_ProvideSplitScreenFactory;
            this.rowContentBindStageProvider = DoubleCheck.provider(new RowContentBindStage_Factory(this.notificationContentInflaterProvider, this.notifInflationErrorManagerProvider, wMShellBaseModule_ProvideSplitScreenFactory, 0));
            this.expandableNotificationRowComponentBuilderProvider = new Provider<ExpandableNotificationRowComponent.Builder>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.2
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final ExpandableNotificationRowComponent.Builder mo144get() {
                    return new ExpandableNotificationRowComponentBuilder();
                }
            };
            UsbDebuggingActivity_Factory usbDebuggingActivity_Factory = new UsbDebuggingActivity_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 2);
            this.iconBuilderProvider = usbDebuggingActivity_Factory;
            this.iconManagerProvider = new DarkIconDispatcherImpl_Factory(this.provideCommonNotifCollectionProvider, DaggerTitanGlobalRootComponent.this.provideLauncherAppsProvider, usbDebuggingActivity_Factory, 1);
            Provider<LowPriorityInflationHelper> provider27 = DoubleCheck.provider(new ColumbusStructuredDataManager_Factory(this.notificationGroupManagerLegacyProvider, this.rowContentBindStageProvider, this.notifPipelineFlagsProvider, 2));
            this.lowPriorityInflationHelperProvider = provider27;
            this.notificationRowBinderImplProvider = DoubleCheck.provider(NotificationRowBinderImpl_Factory.create(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideNotificationMessagingUtilProvider, this.provideNotificationRemoteInputManagerProvider, this.notificationLockscreenUserManagerGoogleProvider, this.notifBindPipelineProvider, this.rowContentBindStageProvider, this.expandableNotificationRowComponentBuilderProvider, this.iconManagerProvider, provider27, this.notifPipelineFlagsProvider));
            Provider<ForegroundServiceDismissalFeatureController> provider28 = DoubleCheck.provider(new ScreenPinningRequest_Factory(this.deviceConfigProxyProvider, DaggerTitanGlobalRootComponent.this.contextProvider, 2));
            this.foregroundServiceDismissalFeatureControllerProvider = provider28;
            Provider<NotificationEntryManager> provider29 = this.provideNotificationEntryManagerProvider;
            Provider<NotificationEntryManagerLogger> provider30 = this.notificationEntryManagerLoggerProvider;
            Provider<NotificationGroupManagerLegacy> provider31 = this.notificationGroupManagerLegacyProvider;
            Provider<NotifPipelineFlags> provider32 = this.notifPipelineFlagsProvider;
            Provider<NotificationRowBinderImpl> provider33 = this.notificationRowBinderImplProvider;
            Provider<NotificationRemoteInputManager> provider34 = this.provideNotificationRemoteInputManagerProvider;
            Provider<LeakDetector> provider35 = this.provideLeakDetectorProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent16 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider29, DoubleCheck.provider(NotificationsModule_ProvideNotificationEntryManagerFactory.create(provider30, provider31, provider32, provider33, provider34, provider35, provider28, daggerTitanGlobalRootComponent16.provideIStatusBarServiceProvider, this.notifLiveDataStoreImplProvider, daggerTitanGlobalRootComponent16.dumpManagerProvider)));
            this.notificationInteractionTrackerProvider = DoubleCheck.provider(new DiagonalClassifier_Factory(this.notificationClickNotifierProvider, this.provideNotificationEntryManagerProvider, 1));
            SecureSettingsImpl_Factory secureSettingsImpl_Factory = new SecureSettingsImpl_Factory(this.provideNotificationsLogBufferProvider, 3);
            this.shadeListBuilderLoggerProvider = secureSettingsImpl_Factory;
            this.shadeListBuilderProvider = DoubleCheck.provider(GlobalActionsImpl_Factory.create$1(DaggerTitanGlobalRootComponent.this.dumpManagerProvider, this.notifPipelineChoreographerImplProvider, this.notifPipelineFlagsProvider, this.notificationInteractionTrackerProvider, secureSettingsImpl_Factory, this.bindSystemClockProvider));
            Provider<RenderStageManager> provider36 = DoubleCheck.provider(RenderStageManager_Factory.InstanceHolder.INSTANCE);
            this.renderStageManagerProvider = provider36;
            DelegateFactory.setDelegate(this.notifPipelineProvider, DoubleCheck.provider(new NotifPipeline_Factory(this.notifPipelineFlagsProvider, this.notifCollectionProvider, this.shadeListBuilderProvider, provider36)));
            DelegateFactory.setDelegate(this.provideCommonNotifCollectionProvider, DoubleCheck.provider(new DozeLog_Factory(this.notifPipelineFlagsProvider, this.notifPipelineProvider, this.provideNotificationEntryManagerProvider, 2)));
            RotationPolicyWrapperImpl_Factory rotationPolicyWrapperImpl_Factory = new RotationPolicyWrapperImpl_Factory(this.notifLiveDataStoreImplProvider, this.provideCommonNotifCollectionProvider, 2);
            this.notificationVisibilityProviderImplProvider = rotationPolicyWrapperImpl_Factory;
            DelegateFactory.setDelegate(this.provideNotificationVisibilityProvider, DoubleCheck.provider(rotationPolicyWrapperImpl_Factory));
            DelegateFactory delegateFactory = new DelegateFactory();
            this.smartSpaceControllerProvider = delegateFactory;
            Provider<NotificationLockscreenUserManagerGoogle> provider37 = this.notificationLockscreenUserManagerGoogleProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent17 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider37, DoubleCheck.provider(NotificationLockscreenUserManagerGoogle_Factory.create(daggerTitanGlobalRootComponent17.contextProvider, this.providesBroadcastDispatcherProvider, daggerTitanGlobalRootComponent17.provideDevicePolicyManagerProvider, daggerTitanGlobalRootComponent17.provideUserManagerProvider, this.provideNotificationVisibilityProvider, this.provideCommonNotifCollectionProvider, this.notificationClickNotifierProvider, daggerTitanGlobalRootComponent17.provideKeyguardManagerProvider, this.statusBarStateControllerImplProvider, daggerTitanGlobalRootComponent17.provideMainHandlerProvider, this.provideDeviceProvisionedControllerProvider, this.keyguardStateControllerImplProvider, this.keyguardBypassControllerProvider, delegateFactory, daggerTitanGlobalRootComponent17.dumpManagerProvider)));
            this.keyguardEnvironmentImplProvider = DoubleCheck.provider(new VibratorHelper_Factory(this.notificationLockscreenUserManagerGoogleProvider, this.provideDeviceProvisionedControllerProvider, 3));
            Provider<IndividualSensorPrivacyController> provider38 = DoubleCheck.provider(new UsbDebuggingSecondaryUserActivity_Factory(DaggerTitanGlobalRootComponent.this.provideSensorPrivacyManagerProvider, 4));
            this.provideIndividualSensorPrivacyControllerProvider = provider38;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent18 = DaggerTitanGlobalRootComponent.this;
            Provider<AppOpsControllerImpl> provider39 = DoubleCheck.provider(AppOpsControllerImpl_Factory.create(daggerTitanGlobalRootComponent18.contextProvider, this.provideBgLooperProvider, daggerTitanGlobalRootComponent18.dumpManagerProvider, daggerTitanGlobalRootComponent18.provideAudioManagerProvider, provider38, this.providesBroadcastDispatcherProvider, this.bindSystemClockProvider));
            this.appOpsControllerImplProvider = provider39;
            Provider<ForegroundServiceController> provider40 = DoubleCheck.provider(new ForegroundServiceController_Factory(provider39, DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider, 0));
            this.foregroundServiceControllerProvider = provider40;
            this.notificationFilterProvider = DoubleCheck.provider(NotificationFilter_Factory.create(this.debugModeFilterProvider, this.statusBarStateControllerImplProvider, this.keyguardEnvironmentImplProvider, provider40, this.notificationLockscreenUserManagerGoogleProvider, this.mediaFeatureFlagProvider));
            WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory wMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory = new WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory(this.provideNotificationsLogBufferProvider, this.provideNotificationHeadsUpLogBufferProvider, 1);
            this.notificationInterruptLoggerProvider = wMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent19 = DaggerTitanGlobalRootComponent.this;
            this.notificationInterruptStateProviderImplProvider = DoubleCheck.provider(NotificationInterruptStateProviderImpl_Factory.create(daggerTitanGlobalRootComponent19.provideContentResolverProvider, daggerTitanGlobalRootComponent19.providePowerManagerProvider, daggerTitanGlobalRootComponent19.provideIDreamManagerProvider, this.provideAmbientDisplayConfigurationProvider, this.notificationFilterProvider, this.provideBatteryControllerProvider, this.statusBarStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, wMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory, daggerTitanGlobalRootComponent19.provideMainHandlerProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent20 = DaggerTitanGlobalRootComponent.this;
            this.lightSensorEventsDebounceAlgorithmProvider = new DependencyProvider_ProvidesModeSwitchesControllerFactory(daggerTitanGlobalRootComponent20.provideMainDelayableExecutorProvider, daggerTitanGlobalRootComponent20.provideResourcesProvider);
            Provider<AsyncSensorManager> provider41 = DoubleCheck.provider(new DozeLog_Factory(daggerTitanGlobalRootComponent20.providesSensorManagerProvider, ThreadFactoryImpl_Factory.InstanceHolder.INSTANCE, daggerTitanGlobalRootComponent20.providesPluginManagerProvider, 3));
            this.asyncSensorManagerProvider = provider41;
            AmbientLightModeMonitor_Factory ambientLightModeMonitor_Factory = new AmbientLightModeMonitor_Factory(this.lightSensorEventsDebounceAlgorithmProvider, provider41);
            this.ambientLightModeMonitorProvider = ambientLightModeMonitor_Factory;
            Provider<DockManager> provider42 = this.provideDockManagerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent21 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider42, DoubleCheck.provider(new WiredChargingRippleController_Factory(daggerTitanGlobalRootComponent21.contextProvider, this.providesBroadcastDispatcherProvider, this.statusBarStateControllerImplProvider, this.notificationInterruptStateProviderImplProvider, this.provideConfigurationControllerProvider, ambientLightModeMonitor_Factory, daggerTitanGlobalRootComponent21.provideMainDelayableExecutorProvider, daggerTitanGlobalRootComponent21.providePowerManagerProvider, 1)));
            Provider<FalsingDataProvider> provider43 = DoubleCheck.provider(new QSSquishinessController_Factory(DaggerTitanGlobalRootComponent.this.provideDisplayMetricsProvider, this.provideBatteryControllerProvider, this.provideDockManagerProvider, 1));
            this.falsingDataProvider = provider43;
            DistanceClassifier_Factory distanceClassifier_Factory = new DistanceClassifier_Factory(provider43, this.deviceConfigProxyProvider, 0);
            this.distanceClassifierProvider = distanceClassifier_Factory;
            this.proximityClassifierProvider = new ProximityClassifier_Factory(distanceClassifier_Factory, this.falsingDataProvider, this.deviceConfigProxyProvider, 0);
            this.pointerCountClassifierProvider = new BootCompleteCacheImpl_Factory(this.falsingDataProvider, 1);
            this.typeClassifierProvider = new TypeClassifier_Factory(this.falsingDataProvider, 0);
            this.diagonalClassifierProvider = new DiagonalClassifier_Factory(this.falsingDataProvider, this.deviceConfigProxyProvider, 0);
            KeyguardUnfoldTransition_Factory keyguardUnfoldTransition_Factory = new KeyguardUnfoldTransition_Factory(this.falsingDataProvider, this.deviceConfigProxyProvider, 1);
            this.zigZagClassifierProvider = keyguardUnfoldTransition_Factory;
            this.providesBrightLineGestureClassifiersProvider = FalsingModule_ProvidesBrightLineGestureClassifiersFactory.create(this.distanceClassifierProvider, this.proximityClassifierProvider, this.pointerCountClassifierProvider, this.typeClassifierProvider, this.diagonalClassifierProvider, keyguardUnfoldTransition_Factory);
            List emptyList = Collections.emptyList();
            ArrayList arrayList12 = new ArrayList(1);
            this.namedSetOfFalsingClassifierProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline0.m(arrayList12, this.providesBrightLineGestureClassifiersProvider, emptyList, arrayList12);
            QSFragmentModule_ProvidesQuickQSPanelFactory qSFragmentModule_ProvidesQuickQSPanelFactory = new QSFragmentModule_ProvidesQuickQSPanelFactory(DaggerTitanGlobalRootComponent.this.provideViewConfigurationProvider, 1);
            this.providesSingleTapTouchSlopProvider = qSFragmentModule_ProvidesQuickQSPanelFactory;
            this.singleTapClassifierProvider = new SingleTapClassifier_Factory(this.falsingDataProvider, qSFragmentModule_ProvidesQuickQSPanelFactory, 0);
            MediaBrowserFactory_Factory mediaBrowserFactory_Factory2 = new MediaBrowserFactory_Factory(DaggerTitanGlobalRootComponent.this.provideResourcesProvider, 1);
            this.providesDoubleTapTouchSlopProvider = mediaBrowserFactory_Factory2;
            this.doubleTapClassifierProvider = new DoubleTapClassifier_Factory(this.falsingDataProvider, this.singleTapClassifierProvider, mediaBrowserFactory_Factory2);
            Provider<HistoryTracker> provider44 = DoubleCheck.provider(new ActivityStarterDelegate_Factory(this.bindSystemClockProvider, 1));
            this.historyTrackerProvider = provider44;
            BrightLineFalsingManager_Factory create3 = BrightLineFalsingManager_Factory.create(this.falsingDataProvider, this.provideMetricsLoggerProvider, this.namedSetOfFalsingClassifierProvider, this.singleTapClassifierProvider, this.doubleTapClassifierProvider, provider44, this.keyguardStateControllerImplProvider, DaggerTitanGlobalRootComponent.this.provideAccessibilityManagerProvider);
            this.brightLineFalsingManagerProvider = create3;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent22 = DaggerTitanGlobalRootComponent.this;
            this.falsingManagerProxyProvider = DoubleCheck.provider(new FalsingManagerProxy_Factory(daggerTitanGlobalRootComponent22.providesPluginManagerProvider, daggerTitanGlobalRootComponent22.provideMainExecutorProvider, this.deviceConfigProxyProvider, daggerTitanGlobalRootComponent22.dumpManagerProvider, create3));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent23 = DaggerTitanGlobalRootComponent.this;
            Provider<Resources> provider45 = daggerTitanGlobalRootComponent23.provideResourcesProvider;
            ThresholdSensorImpl_BuilderFactory_Factory thresholdSensorImpl_BuilderFactory_Factory = new ThresholdSensorImpl_BuilderFactory_Factory(provider45, this.asyncSensorManagerProvider, daggerTitanGlobalRootComponent23.provideExecutionProvider);
            this.builderFactoryProvider = thresholdSensorImpl_BuilderFactory_Factory;
            this.providePostureToProximitySensorMappingProvider = new SensorModule_ProvidePostureToProximitySensorMappingFactory(thresholdSensorImpl_BuilderFactory_Factory, provider45);
            this.providePostureToSecondaryProximitySensorMappingProvider = new SensorModule_ProvidePostureToSecondaryProximitySensorMappingFactory(thresholdSensorImpl_BuilderFactory_Factory, provider45);
            Provider<DevicePostureControllerImpl> provider46 = DoubleCheck.provider(new TouchInsideHandler_Factory(daggerTitanGlobalRootComponent23.contextProvider, daggerTitanGlobalRootComponent23.provideDeviceStateManagerProvider, daggerTitanGlobalRootComponent23.provideMainExecutorProvider, 2));
            this.devicePostureControllerImplProvider = provider46;
            Provider<ThresholdSensor[]> provider47 = this.providePostureToProximitySensorMappingProvider;
            Provider<ThresholdSensor[]> provider48 = this.providePostureToSecondaryProximitySensorMappingProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent24 = DaggerTitanGlobalRootComponent.this;
            this.postureDependentProximitySensorProvider = PostureDependentProximitySensor_Factory.create(provider47, provider48, daggerTitanGlobalRootComponent24.provideMainDelayableExecutorProvider, daggerTitanGlobalRootComponent24.provideExecutionProvider, provider46);
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent25 = DaggerTitanGlobalRootComponent.this;
            ThresholdSensorImpl_Builder_Factory thresholdSensorImpl_Builder_Factory = new ThresholdSensorImpl_Builder_Factory(daggerTitanGlobalRootComponent25.provideResourcesProvider, this.asyncSensorManagerProvider, daggerTitanGlobalRootComponent25.provideExecutionProvider);
            this.builderProvider2 = thresholdSensorImpl_Builder_Factory;
            this.providePrimaryProximitySensorProvider = new SensorModule_ProvidePrimaryProximitySensorFactory(daggerTitanGlobalRootComponent25.providesSensorManagerProvider, thresholdSensorImpl_Builder_Factory);
        }

        public final void initialize4(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<Object> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            SensorModule_ProvideSecondaryProximitySensorFactory sensorModule_ProvideSecondaryProximitySensorFactory = new SensorModule_ProvideSecondaryProximitySensorFactory(this.builderProvider2);
            this.provideSecondaryProximitySensorProvider = sensorModule_ProvideSecondaryProximitySensorFactory;
            Provider<ThresholdSensor> provider = this.providePrimaryProximitySensorProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
            Provider<DelayableExecutor> provider2 = daggerTitanGlobalRootComponent.provideMainDelayableExecutorProvider;
            MediaDreamSentinel_Factory mediaDreamSentinel_Factory = new MediaDreamSentinel_Factory(provider, sensorModule_ProvideSecondaryProximitySensorFactory, provider2, daggerTitanGlobalRootComponent.provideExecutionProvider, 1);
            this.proximitySensorImplProvider = mediaDreamSentinel_Factory;
            TileAdapter_Factory tileAdapter_Factory = new TileAdapter_Factory(daggerTitanGlobalRootComponent.provideResourcesProvider, this.postureDependentProximitySensorProvider, mediaDreamSentinel_Factory, 1);
            this.provideProximitySensorProvider = tileAdapter_Factory;
            this.falsingCollectorImplProvider = DoubleCheck.provider(FalsingCollectorImpl_Factory.create(this.falsingDataProvider, this.falsingManagerProxyProvider, this.keyguardUpdateMonitorProvider, this.historyTrackerProvider, tileAdapter_Factory, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.provideBatteryControllerProvider, this.provideDockManagerProvider, provider2, this.bindSystemClockProvider));
            this.dismissCallbackRegistryProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline2.m(this.provideUiBackgroundExecutorProvider, 2);
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent2 = DaggerTitanGlobalRootComponent.this;
            this.telephonyListenerManagerProvider = DoubleCheck.provider(new TelephonyListenerManager_Factory(daggerTitanGlobalRootComponent2.provideTelephonyManagerProvider, daggerTitanGlobalRootComponent2.provideMainExecutorProvider, TelephonyCallback_Factory.InstanceHolder.INSTANCE, 0));
            Provider<DialogLaunchAnimator> provider3 = DoubleCheck.provider(new WMShellBaseModule_ProvideHideDisplayCutoutFactory(DaggerTitanGlobalRootComponent.this.provideIDreamManagerProvider, 2));
            this.provideDialogLaunchAnimatorProvider = provider3;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent3 = DaggerTitanGlobalRootComponent.this;
            this.userSwitcherControllerProvider = DoubleCheck.provider(UserSwitcherController_Factory.create(daggerTitanGlobalRootComponent3.contextProvider, daggerTitanGlobalRootComponent3.provideIActivityManagerProvider, daggerTitanGlobalRootComponent3.provideUserManagerProvider, this.provideUserTrackerProvider, this.keyguardStateControllerImplProvider, this.provideDeviceProvisionedControllerProvider, daggerTitanGlobalRootComponent3.provideDevicePolicyManagerProvider, daggerTitanGlobalRootComponent3.provideMainHandlerProvider, this.provideActivityStarterProvider, this.providesBroadcastDispatcherProvider, daggerTitanGlobalRootComponent3.provideUiEventLoggerProvider, this.falsingManagerProxyProvider, this.telephonyListenerManagerProvider, this.secureSettingsImplProvider, this.provideBackgroundExecutorProvider, daggerTitanGlobalRootComponent3.provideMainExecutorProvider, daggerTitanGlobalRootComponent3.provideInteractionJankMonitorProvider, daggerTitanGlobalRootComponent3.provideLatencyTrackerProvider, daggerTitanGlobalRootComponent3.dumpManagerProvider, this.shadeControllerImplProvider, provider3));
            Provider<KeyguardStatusViewComponent.Factory> provider4 = new Provider<KeyguardStatusViewComponent.Factory>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.3
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final KeyguardStatusViewComponent.Factory mo144get() {
                    return new KeyguardStatusViewComponentFactory();
                }
            };
            this.keyguardStatusViewComponentFactoryProvider = provider4;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent4 = DaggerTitanGlobalRootComponent.this;
            this.keyguardDisplayManagerProvider = new KeyguardDisplayManager_Factory(daggerTitanGlobalRootComponent4.contextProvider, this.navigationBarControllerProvider, provider4, this.provideUiBackgroundExecutorProvider);
            this.screenOnCoordinatorProvider = DoubleCheck.provider(new ScreenOnCoordinator_Factory(daggerTitanGlobalRootComponent4.screenLifecycleProvider, this.provideSysUIUnfoldComponentProvider, daggerTitanGlobalRootComponent4.provideExecutionProvider, 0));
            this.dreamOverlayStateControllerProvider = DoubleCheck.provider(new DreamOverlayStateController_Factory(DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider, 0));
            Provider<ActivityLaunchAnimator> provider5 = DoubleCheck.provider(StatusBarDependenciesModule_ProvideActivityLaunchAnimatorFactory.InstanceHolder.INSTANCE);
            this.provideActivityLaunchAnimatorProvider = provider5;
            Provider<KeyguardViewMediator> provider6 = this.newKeyguardViewMediatorProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent5 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider6, DoubleCheck.provider(KeyguardModule_NewKeyguardViewMediatorFactory.create(daggerTitanGlobalRootComponent5.contextProvider, this.falsingCollectorImplProvider, this.provideLockPatternUtilsProvider, this.providesBroadcastDispatcherProvider, this.statusBarKeyguardViewManagerProvider, this.dismissCallbackRegistryProvider, this.keyguardUpdateMonitorProvider, daggerTitanGlobalRootComponent5.dumpManagerProvider, daggerTitanGlobalRootComponent5.providePowerManagerProvider, daggerTitanGlobalRootComponent5.provideTrustManagerProvider, this.userSwitcherControllerProvider, this.provideUiBackgroundExecutorProvider, this.deviceConfigProxyProvider, this.navigationModeControllerProvider, this.keyguardDisplayManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUnlockAnimationControllerProvider, this.screenOffAnimationControllerProvider, this.notificationShadeDepthControllerProvider, this.screenOnCoordinatorProvider, daggerTitanGlobalRootComponent5.provideInteractionJankMonitorProvider, this.dreamOverlayStateControllerProvider, this.notificationShadeWindowControllerImplProvider, provider5)));
            this.providesViewMediatorCallbackProvider = new DependencyProvider_ProvidesViewMediatorCallbackFactory(dependencyProvider, this.newKeyguardViewMediatorProvider);
            Provider<KeyguardSecurityModel> provider7 = DoubleCheck.provider(new KeyguardSecurityModel_Factory(DaggerTitanGlobalRootComponent.this.provideResourcesProvider, this.provideLockPatternUtilsProvider, this.keyguardUpdateMonitorProvider, 0));
            this.keyguardSecurityModelProvider = provider7;
            Provider<KeyguardBouncerComponent.Factory> provider8 = new Provider<KeyguardBouncerComponent.Factory>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.4
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final KeyguardBouncerComponent.Factory mo144get() {
                    return new KeyguardBouncerComponentFactory();
                }
            };
            this.keyguardBouncerComponentFactoryProvider = provider8;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent6 = DaggerTitanGlobalRootComponent.this;
            KeyguardBouncer_Factory_Factory create = KeyguardBouncer_Factory_Factory.create(daggerTitanGlobalRootComponent6.contextProvider, this.providesViewMediatorCallbackProvider, this.dismissCallbackRegistryProvider, this.falsingCollectorImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.keyguardBypassControllerProvider, daggerTitanGlobalRootComponent6.provideMainHandlerProvider, provider7, provider8);
            this.factoryProvider4 = create;
            Provider<KeyguardUpdateMonitor> provider9 = this.keyguardUpdateMonitorProvider;
            Provider<ConfigurationController> provider10 = this.provideConfigurationControllerProvider;
            KeyguardMessageAreaController_Factory_Factory keyguardMessageAreaController_Factory_Factory = new KeyguardMessageAreaController_Factory_Factory(provider9, provider10);
            this.factoryProvider5 = keyguardMessageAreaController_Factory_Factory;
            Provider<StatusBarKeyguardViewManager> provider11 = this.statusBarKeyguardViewManagerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent7 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider11, DoubleCheck.provider(StatusBarKeyguardViewManager_Factory.create(daggerTitanGlobalRootComponent7.contextProvider, this.providesViewMediatorCallbackProvider, this.provideLockPatternUtilsProvider, this.statusBarStateControllerImplProvider, provider10, provider9, this.dreamOverlayStateControllerProvider, this.navigationModeControllerProvider, this.provideDockManagerProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, this.provideNotificationMediaManagerProvider, create, keyguardMessageAreaController_Factory_Factory, this.provideSysUIUnfoldComponentProvider, this.shadeControllerImplProvider, daggerTitanGlobalRootComponent7.provideLatencyTrackerProvider)));
            this.provideLSShadeTransitionControllerBufferProvider = DoubleCheck.provider(new SmartActionsReceiver_Factory(this.logBufferFactoryProvider, 2));
            Provider<LockscreenGestureLogger> m = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline2.m(this.provideMetricsLoggerProvider, 4);
            this.lockscreenGestureLoggerProvider = m;
            this.lSShadeTransitionLoggerProvider = new IconController_Factory(this.provideLSShadeTransitionControllerBufferProvider, m, DaggerTitanGlobalRootComponent.this.provideDisplayMetricsProvider, 1);
            Provider<MediaHostStatesManager> provider12 = DoubleCheck.provider(MediaHostStatesManager_Factory.InstanceHolder.INSTANCE);
            this.mediaHostStatesManagerProvider = provider12;
            this.mediaViewControllerProvider = new MediaViewController_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider, provider12, 0);
            Provider<RepeatableExecutor> m2 = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline3.m(this.provideBackgroundDelayableExecutorProvider, 6);
            this.provideBackgroundRepeatableExecutorProvider = m2;
            this.seekBarViewModelProvider = new SeekBarViewModel_Factory(m2, 0);
            Provider<SystemUIDialogManager> provider13 = DoubleCheck.provider(new SystemUIDialogManager_Factory(DaggerTitanGlobalRootComponent.this.dumpManagerProvider, this.statusBarKeyguardViewManagerProvider, 0));
            this.systemUIDialogManagerProvider = provider13;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent8 = DaggerTitanGlobalRootComponent.this;
            QSCustomizerController_Factory create2 = QSCustomizerController_Factory.create(daggerTitanGlobalRootComponent8.contextProvider, daggerTitanGlobalRootComponent8.provideMediaSessionManagerProvider, this.provideLocalBluetoothControllerProvider, this.shadeControllerImplProvider, this.provideActivityStarterProvider, this.provideCommonNotifCollectionProvider, daggerTitanGlobalRootComponent8.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, provider13);
            this.mediaOutputDialogFactoryProvider = create2;
            DelegateFactory delegateFactory = new DelegateFactory();
            this.mediaCarouselControllerProvider = delegateFactory;
            MediaControlPanel_Factory create3 = MediaControlPanel_Factory.create(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideBackgroundExecutorProvider, this.provideActivityStarterProvider, this.mediaViewControllerProvider, this.seekBarViewModelProvider, this.mediaDataManagerProvider, create2, delegateFactory, this.falsingManagerProxyProvider, this.mediaFlagsProvider, this.bindSystemClockProvider);
            this.mediaControlPanelProvider = create3;
            Provider<MediaCarouselController> provider14 = this.mediaCarouselControllerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent9 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider14, DoubleCheck.provider(CastTile_Factory.create(daggerTitanGlobalRootComponent9.contextProvider, create3, this.visualStabilityProvider, this.mediaHostStatesManagerProvider, this.provideActivityStarterProvider, this.bindSystemClockProvider, daggerTitanGlobalRootComponent9.provideMainDelayableExecutorProvider, this.mediaDataManagerProvider, this.provideConfigurationControllerProvider, this.falsingCollectorImplProvider, this.falsingManagerProxyProvider, daggerTitanGlobalRootComponent9.dumpManagerProvider, this.mediaFlagsProvider)));
            Provider<MediaHierarchyManager> provider15 = DoubleCheck.provider(MediaHierarchyManager_Factory.create(DaggerTitanGlobalRootComponent.this.contextProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardBypassControllerProvider, this.mediaCarouselControllerProvider, this.notificationLockscreenUserManagerGoogleProvider, this.provideConfigurationControllerProvider, this.wakefulnessLifecycleProvider, this.statusBarKeyguardViewManagerProvider, this.dreamOverlayStateControllerProvider));
            this.mediaHierarchyManagerProvider = provider15;
            Provider<MediaHost> provider16 = DoubleCheck.provider(new MediaModule_ProvidesKeyguardMediaHostFactory(provider15, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesKeyguardMediaHostProvider = provider16;
            this.keyguardMediaControllerProvider = DoubleCheck.provider(KeyguardMediaController_Factory.create(provider16, this.keyguardBypassControllerProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerGoogleProvider, DaggerTitanGlobalRootComponent.this.contextProvider, this.provideConfigurationControllerProvider, this.mediaFlagsProvider));
            this.notificationSectionsFeatureManagerProvider = DoubleCheck.provider(new ShortcutKeyDispatcher_Factory(this.deviceConfigProxyProvider, DaggerTitanGlobalRootComponent.this.contextProvider, 2));
            Provider<LogBuffer> provider17 = DoubleCheck.provider(new QSFragmentModule_ProvideRootViewFactory(this.logBufferFactoryProvider, 3));
            this.provideNotificationSectionLogBufferProvider = provider17;
            this.notificationSectionsLoggerProvider = DoubleCheck.provider(new WMShellBaseModule_ProvideBubblesFactory(provider17, 2));
            this.mediaContainerControllerProvider = DoubleCheck.provider(new SystemUIModule_ProvideLowLightClockControllerFactory(this.providerLayoutInflaterProvider, 2));
            Provider<SectionHeaderControllerSubcomponent.Builder> provider18 = new Provider<SectionHeaderControllerSubcomponent.Builder>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.5
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final SectionHeaderControllerSubcomponent.Builder mo144get() {
                    return new SectionHeaderControllerSubcomponentBuilder();
                }
            };
            this.sectionHeaderControllerSubcomponentBuilderProvider = provider18;
            Provider<SectionHeaderControllerSubcomponent> provider19 = DoubleCheck.provider(new NotificationSectionHeadersModule_ProvidesIncomingHeaderSubcomponentFactory(provider18));
            this.providesIncomingHeaderSubcomponentProvider = provider19;
            this.providesIncomingHeaderControllerProvider = new DozeAuthRemover_Factory(provider19, 2);
            Provider<SectionHeaderControllerSubcomponent> provider20 = DoubleCheck.provider(new NotificationSectionHeadersModule_ProvidesPeopleHeaderSubcomponentFactory(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesPeopleHeaderSubcomponentProvider = provider20;
            this.providesPeopleHeaderControllerProvider = new ColorChangeHandler_Factory(provider20, 3);
            Provider<SectionHeaderControllerSubcomponent> provider21 = DoubleCheck.provider(new NotificationSectionHeadersModule_ProvidesAlertingHeaderSubcomponentFactory(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesAlertingHeaderSubcomponentProvider = provider21;
            this.providesAlertingHeaderControllerProvider = new TypeClassifier_Factory(provider21, 5);
            Provider<SectionHeaderControllerSubcomponent> provider22 = DoubleCheck.provider(new NotificationSectionHeadersModule_ProvidesSilentHeaderSubcomponentFactory(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesSilentHeaderSubcomponentProvider = provider22;
            FrameworkServicesModule_ProvideFaceManagerFactory frameworkServicesModule_ProvideFaceManagerFactory = new FrameworkServicesModule_ProvideFaceManagerFactory(provider22, 1);
            this.providesSilentHeaderControllerProvider = frameworkServicesModule_ProvideFaceManagerFactory;
            QuickQSPanelController_Factory create$1 = QuickQSPanelController_Factory.create$1(this.statusBarStateControllerImplProvider, this.provideConfigurationControllerProvider, this.keyguardMediaControllerProvider, this.notificationSectionsFeatureManagerProvider, this.notificationSectionsLoggerProvider, this.notifPipelineFlagsProvider, this.mediaContainerControllerProvider, this.providesIncomingHeaderControllerProvider, this.providesPeopleHeaderControllerProvider, this.providesAlertingHeaderControllerProvider, frameworkServicesModule_ProvideFaceManagerFactory);
            this.notificationSectionsManagerProvider = create$1;
            Provider<AmbientState> provider23 = DoubleCheck.provider(new AmbientState_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, create$1, this.keyguardBypassControllerProvider));
            this.ambientStateProvider = provider23;
            Provider<StatusBarStateControllerImpl> provider24 = this.statusBarStateControllerImplProvider;
            Provider<LSShadeTransitionLogger> provider25 = this.lSShadeTransitionLoggerProvider;
            Provider<KeyguardBypassController> provider26 = this.keyguardBypassControllerProvider;
            Provider<NotificationLockscreenUserManagerGoogle> provider27 = this.notificationLockscreenUserManagerGoogleProvider;
            Provider provider28 = this.falsingCollectorImplProvider;
            Provider<MediaHierarchyManager> provider29 = this.mediaHierarchyManagerProvider;
            Provider<ScrimController> provider30 = this.scrimControllerProvider;
            Provider<NotificationShadeDepthController> provider31 = this.notificationShadeDepthControllerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent10 = DaggerTitanGlobalRootComponent.this;
            this.lockscreenShadeTransitionControllerProvider = DoubleCheck.provider(LockscreenShadeTransitionController_Factory.create(provider24, provider25, provider26, provider27, provider28, provider23, provider29, provider30, provider31, daggerTitanGlobalRootComponent10.contextProvider, this.wakefulnessLifecycleProvider, this.provideConfigurationControllerProvider, this.falsingManagerProxyProvider, daggerTitanGlobalRootComponent10.dumpManagerProvider));
            Provider<VibratorHelper> provider32 = DoubleCheck.provider(new VibratorHelper_Factory(DaggerTitanGlobalRootComponent.this.provideVibratorProvider, this.provideBackgroundExecutorProvider, 0));
            this.vibratorHelperProvider = provider32;
            this.udfpsHapticsSimulatorProvider = DoubleCheck.provider(new UdfpsHapticsSimulator_Factory(this.commandRegistryProvider, provider32, this.keyguardUpdateMonitorProvider, 0));
            Provider<Executor> provider33 = DoubleCheck.provider(new TypeClassifier_Factory(ThreadFactoryImpl_Factory.InstanceHolder.INSTANCE, 1));
            this.providesPluginExecutorProvider = provider33;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent11 = DaggerTitanGlobalRootComponent.this;
            Provider<UdfpsHbmController> provider34 = DoubleCheck.provider(new UdfpsHbmController_Factory(daggerTitanGlobalRootComponent11.contextProvider, daggerTitanGlobalRootComponent11.provideExecutionProvider, daggerTitanGlobalRootComponent11.provideMainHandlerProvider, provider33, this.authControllerProvider, daggerTitanGlobalRootComponent11.provideDisplayManagerProvider));
            this.udfpsHbmControllerProvider = provider34;
            this.optionalOfUdfpsHbmProvider = new PresentJdkOptionalInstanceProvider(provider34);
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent12 = DaggerTitanGlobalRootComponent.this;
            Provider<Context> provider35 = daggerTitanGlobalRootComponent12.contextProvider;
            Provider<Execution> provider36 = daggerTitanGlobalRootComponent12.provideExecutionProvider;
            Provider<LayoutInflater> provider37 = this.providerLayoutInflaterProvider;
            Provider<FingerprintManager> provider38 = daggerTitanGlobalRootComponent12.providesFingerprintManagerProvider;
            Provider<WindowManager> provider39 = daggerTitanGlobalRootComponent12.provideWindowManagerProvider;
            Provider<StatusBarStateControllerImpl> provider40 = this.statusBarStateControllerImplProvider;
            Provider<DelayableExecutor> provider41 = daggerTitanGlobalRootComponent12.provideMainDelayableExecutorProvider;
            Provider<PanelExpansionStateManager> provider42 = this.panelExpansionStateManagerProvider;
            Provider<StatusBarKeyguardViewManager> provider43 = this.statusBarKeyguardViewManagerProvider;
            Provider<DumpManager> provider44 = daggerTitanGlobalRootComponent12.dumpManagerProvider;
            Provider<KeyguardUpdateMonitor> provider45 = this.keyguardUpdateMonitorProvider;
            Provider<FalsingManagerProxy> provider46 = this.falsingManagerProxyProvider;
            Provider<PowerManager> provider47 = daggerTitanGlobalRootComponent12.providePowerManagerProvider;
            Provider<AccessibilityManager> provider48 = daggerTitanGlobalRootComponent12.provideAccessibilityManagerProvider;
            Provider<LockscreenShadeTransitionController> provider49 = this.lockscreenShadeTransitionControllerProvider;
            Provider<ScreenLifecycle> provider50 = daggerTitanGlobalRootComponent12.screenLifecycleProvider;
            Provider<VibratorHelper> provider51 = this.vibratorHelperProvider;
            Provider<UdfpsHapticsSimulator> provider52 = this.udfpsHapticsSimulatorProvider;
            Provider<Optional<UdfpsHbmProvider>> provider53 = this.optionalOfUdfpsHbmProvider;
            Provider<KeyguardStateControllerImpl> provider54 = this.keyguardStateControllerImplProvider;
            Provider<KeyguardBypassController> provider55 = this.keyguardBypassControllerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent13 = DaggerTitanGlobalRootComponent.this;
            this.udfpsControllerProvider = DoubleCheck.provider(UdfpsController_Factory.create(provider35, provider36, provider37, provider38, provider39, provider40, provider41, provider42, provider43, provider44, provider45, provider46, provider47, provider48, provider49, provider50, provider51, provider52, provider53, provider54, provider55, daggerTitanGlobalRootComponent13.provideDisplayManagerProvider, daggerTitanGlobalRootComponent13.provideMainHandlerProvider, this.provideConfigurationControllerProvider, this.bindSystemClockProvider, this.unlockedScreenOffAnimationControllerProvider, this.systemUIDialogManagerProvider, daggerTitanGlobalRootComponent13.provideLatencyTrackerProvider, this.provideActivityLaunchAnimatorProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent14 = DaggerTitanGlobalRootComponent.this;
            Provider<SidefpsController> provider56 = DoubleCheck.provider(NfcTile_Factory.create(daggerTitanGlobalRootComponent14.contextProvider, this.providerLayoutInflaterProvider, daggerTitanGlobalRootComponent14.providesFingerprintManagerProvider, daggerTitanGlobalRootComponent14.provideWindowManagerProvider, daggerTitanGlobalRootComponent14.provideActivityTaskManagerProvider, this.overviewProxyServiceProvider, daggerTitanGlobalRootComponent14.provideDisplayManagerProvider, daggerTitanGlobalRootComponent14.provideMainDelayableExecutorProvider, daggerTitanGlobalRootComponent14.provideMainHandlerProvider));
            this.sidefpsControllerProvider = provider56;
            Provider<AuthController> provider57 = this.authControllerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent15 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider57, DoubleCheck.provider(AuthController_Factory.create(daggerTitanGlobalRootComponent15.contextProvider, daggerTitanGlobalRootComponent15.provideExecutionProvider, this.provideCommandQueueProvider, daggerTitanGlobalRootComponent15.provideActivityTaskManagerProvider, daggerTitanGlobalRootComponent15.provideWindowManagerProvider, daggerTitanGlobalRootComponent15.providesFingerprintManagerProvider, daggerTitanGlobalRootComponent15.provideFaceManagerProvider, this.udfpsControllerProvider, provider56, daggerTitanGlobalRootComponent15.provideDisplayManagerProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, daggerTitanGlobalRootComponent15.provideMainHandlerProvider)));
            Provider<KeyguardUpdateMonitor> provider58 = this.keyguardUpdateMonitorProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent16 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider58, DoubleCheck.provider(KeyguardUpdateMonitor_Factory.create(daggerTitanGlobalRootComponent16.contextProvider, this.providesBroadcastDispatcherProvider, daggerTitanGlobalRootComponent16.dumpManagerProvider, this.provideBackgroundExecutorProvider, daggerTitanGlobalRootComponent16.provideMainExecutorProvider, this.statusBarStateControllerImplProvider, this.provideLockPatternUtilsProvider, this.authControllerProvider, this.telephonyListenerManagerProvider, daggerTitanGlobalRootComponent16.provideInteractionJankMonitorProvider, daggerTitanGlobalRootComponent16.provideLatencyTrackerProvider)));
            Provider<SmartSpaceController> provider59 = this.smartSpaceControllerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent17 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider59, DoubleCheck.provider(new SmartSpaceController_Factory(daggerTitanGlobalRootComponent17.contextProvider, this.keyguardUpdateMonitorProvider, daggerTitanGlobalRootComponent17.provideMainHandlerProvider, daggerTitanGlobalRootComponent17.provideAlarmManagerProvider, daggerTitanGlobalRootComponent17.dumpManagerProvider)));
            this.wallpaperNotifierProvider = new KeyguardSecurityModel_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideCommonNotifCollectionProvider, this.providesBroadcastDispatcherProvider, 1);
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent18 = DaggerTitanGlobalRootComponent.this;
            this.statusBarIconControllerImplProvider = DoubleCheck.provider(StatusBarIconControllerImpl_Factory.create(daggerTitanGlobalRootComponent18.contextProvider, this.provideCommandQueueProvider, this.provideDemoModeControllerProvider, this.provideConfigurationControllerProvider, this.tunerServiceImplProvider, daggerTitanGlobalRootComponent18.dumpManagerProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent19 = DaggerTitanGlobalRootComponent.this;
            Provider<Context> provider60 = daggerTitanGlobalRootComponent19.contextProvider;
            WakeLock_Builder_Factory wakeLock_Builder_Factory = new WakeLock_Builder_Factory(provider60);
            this.builderProvider3 = wakeLock_Builder_Factory;
            Provider<KeyguardIndicationControllerGoogle> provider61 = DoubleCheck.provider(KeyguardIndicationControllerGoogle_Factory.create(provider60, wakeLock_Builder_Factory, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.provideDockManagerProvider, this.providesBroadcastDispatcherProvider, daggerTitanGlobalRootComponent19.provideDevicePolicyManagerProvider, daggerTitanGlobalRootComponent19.provideIBatteryStatsProvider, daggerTitanGlobalRootComponent19.provideUserManagerProvider, this.tunerServiceImplProvider, this.deviceConfigProxyProvider, daggerTitanGlobalRootComponent19.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.falsingManagerProxyProvider, this.provideLockPatternUtilsProvider, daggerTitanGlobalRootComponent19.screenLifecycleProvider, DaggerTitanGlobalRootComponent.this.provideIActivityManagerProvider, this.keyguardBypassControllerProvider));
            this.keyguardIndicationControllerGoogleProvider = provider61;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent20 = DaggerTitanGlobalRootComponent.this;
            Provider<ReverseChargingViewController> provider62 = DoubleCheck.provider(new ReverseChargingViewController_Factory(daggerTitanGlobalRootComponent20.contextProvider, this.provideBatteryControllerProvider, this.statusBarGoogleProvider, this.statusBarIconControllerImplProvider, this.providesBroadcastDispatcherProvider, daggerTitanGlobalRootComponent20.provideMainExecutorProvider, provider61));
            this.reverseChargingViewControllerProvider = provider62;
            this.provideReverseChargingViewControllerOptionalProvider = DoubleCheck.provider(new DependencyProvider_ProviderLayoutInflaterFactory(this.provideBatteryControllerProvider, provider62, 2));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent21 = DaggerTitanGlobalRootComponent.this;
            this.notificationListenerProvider = DoubleCheck.provider(new NotificationListener_Factory(daggerTitanGlobalRootComponent21.contextProvider, daggerTitanGlobalRootComponent21.provideNotificationManagerProvider, this.bindSystemClockProvider, daggerTitanGlobalRootComponent21.provideMainExecutorProvider, daggerTitanGlobalRootComponent21.providesPluginManagerProvider));
            Provider<HighPriorityProvider> provider63 = DoubleCheck.provider(new UserCreator_Factory(this.peopleNotificationIdentifierImplProvider, this.provideGroupMembershipManagerProvider, 2));
            this.highPriorityProvider = provider63;
            this.notificationRankingManagerProvider = NotificationRankingManager_Factory.create(this.provideNotificationMediaManagerProvider, this.notificationGroupManagerLegacyProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationFilterProvider, this.notificationEntryManagerLoggerProvider, this.notificationSectionsFeatureManagerProvider, this.peopleNotificationIdentifierImplProvider, provider63, this.keyguardEnvironmentImplProvider);
            this.targetSdkResolverProvider = DoubleCheck.provider(new MediaFlags_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 2));
            ImageExporter_Factory imageExporter_Factory = new ImageExporter_Factory(this.provideNotificationsLogBufferProvider, 3);
            this.groupCoalescerLoggerProvider = imageExporter_Factory;
            this.groupCoalescerProvider = new SystemEventChipAnimationController_Factory(DaggerTitanGlobalRootComponent.this.provideMainDelayableExecutorProvider, this.bindSystemClockProvider, imageExporter_Factory, 1);
            Provider<CoordinatorsSubcomponent.Factory> provider64 = new Provider<CoordinatorsSubcomponent.Factory>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.6
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final CoordinatorsSubcomponent.Factory mo144get() {
                    return new CoordinatorsSubcomponentFactory();
                }
            };
            this.coordinatorsSubcomponentFactoryProvider = provider64;
            this.notifCoordinatorsProvider = DoubleCheck.provider(new CoordinatorsModule_NotifCoordinatorsFactory(provider64));
            this.notifInflaterImplProvider = DoubleCheck.provider(new PeopleSpaceWidgetProvider_Factory(this.notifInflationErrorManagerProvider, 3));
            this.sectionHeaderVisibilityProvider = DoubleCheck.provider(new PowerSaveState_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 3));
            this.shadeViewDifferLoggerProvider = new ToastLogger_Factory(this.provideNotificationsLogBufferProvider, 2);
            Provider<NotifViewBarn> provider65 = DoubleCheck.provider(NotifViewBarn_Factory.InstanceHolder.INSTANCE);
            this.notifViewBarnProvider = provider65;
            ShadeViewManager_Factory create4 = ShadeViewManager_Factory.create(DaggerTitanGlobalRootComponent.this.contextProvider, this.mediaContainerControllerProvider, this.notificationSectionsFeatureManagerProvider, this.sectionHeaderVisibilityProvider, this.shadeViewDifferLoggerProvider, provider65);
            this.shadeViewManagerProvider = create4;
            InstanceFactory create5 = InstanceFactory.create(new ShadeViewManagerFactory_Impl(create4));
            this.shadeViewManagerFactoryProvider = create5;
            this.notifPipelineInitializerProvider = DoubleCheck.provider(NotifPipelineInitializer_Factory.create(this.notifPipelineProvider, this.groupCoalescerProvider, this.notifCollectionProvider, this.shadeListBuilderProvider, this.renderStageManagerProvider, this.notifCoordinatorsProvider, this.notifInflaterImplProvider, DaggerTitanGlobalRootComponent.this.dumpManagerProvider, create5, this.notifPipelineFlagsProvider));
            this.notifBindPipelineInitializerProvider = new NotifBindPipelineInitializer_Factory(this.notifBindPipelineProvider, this.rowContentBindStageProvider, 0);
            this.notificationGroupAlertTransferHelperProvider = DoubleCheck.provider(new RingerModeTrackerImpl_Factory(this.rowContentBindStageProvider, this.statusBarStateControllerImplProvider, this.notificationGroupManagerLegacyProvider, 3));
            LogModule_ProvidePrivacyLogBufferFactory logModule_ProvidePrivacyLogBufferFactory = new LogModule_ProvidePrivacyLogBufferFactory(this.provideNotificationHeadsUpLogBufferProvider, 4);
            this.headsUpViewBinderLoggerProvider = logModule_ProvidePrivacyLogBufferFactory;
            this.headsUpViewBinderProvider = DoubleCheck.provider(new SysuiColorExtractor_Factory(this.provideNotificationMessagingUtilProvider, this.rowContentBindStageProvider, logModule_ProvidePrivacyLogBufferFactory, 1));
            Provider<NotificationEntryManager> provider66 = this.provideNotificationEntryManagerProvider;
            Provider<VisualStabilityProvider> provider67 = this.visualStabilityProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent22 = DaggerTitanGlobalRootComponent.this;
            Provider<VisualStabilityManager> provider68 = DoubleCheck.provider(NotificationsModule_ProvideVisualStabilityManagerFactory.create(provider66, provider67, daggerTitanGlobalRootComponent22.provideMainHandlerProvider, this.statusBarStateControllerImplProvider, this.wakefulnessLifecycleProvider, daggerTitanGlobalRootComponent22.dumpManagerProvider));
            this.provideVisualStabilityManagerProvider = provider68;
            this.headsUpControllerProvider = DoubleCheck.provider(HeadsUpController_Factory.create(this.headsUpViewBinderProvider, this.notificationInterruptStateProviderImplProvider, this.provideHeadsUpManagerPhoneProvider, this.provideNotificationRemoteInputManagerProvider, this.statusBarStateControllerImplProvider, provider68, this.notificationListenerProvider));
            QSLogger_Factory qSLogger_Factory = new QSLogger_Factory(this.provideNotifInteractionLogBufferProvider, 3);
            this.notificationClickerLoggerProvider = qSLogger_Factory;
            this.builderProvider4 = new NotificationClicker_Builder_Factory(qSLogger_Factory);
            this.animatedImageNotificationManagerProvider = DoubleCheck.provider(new TakeScreenshotService_Factory(this.provideCommonNotifCollectionProvider, this.bindEventManagerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, 1));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent23 = DaggerTitanGlobalRootComponent.this;
            this.peopleSpaceWidgetManagerProvider = DoubleCheck.provider(PeopleSpaceWidgetManager_Factory.create(daggerTitanGlobalRootComponent23.contextProvider, daggerTitanGlobalRootComponent23.provideLauncherAppsProvider, this.provideCommonNotifCollectionProvider, daggerTitanGlobalRootComponent23.providePackageManagerProvider, this.setBubblesProvider, daggerTitanGlobalRootComponent23.provideUserManagerProvider, daggerTitanGlobalRootComponent23.provideNotificationManagerProvider, this.providesBroadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.notificationsControllerImplProvider = DoubleCheck.provider(NotificationsControllerImpl_Factory.create(this.notifPipelineFlagsProvider, this.notificationListenerProvider, this.provideNotificationEntryManagerProvider, this.debugModeFilterProvider, this.notificationRankingManagerProvider, this.provideCommonNotifCollectionProvider, this.notifPipelineProvider, this.notifLiveDataStoreImplProvider, this.targetSdkResolverProvider, this.notifPipelineInitializerProvider, this.notifBindPipelineInitializerProvider, this.provideDeviceProvisionedControllerProvider, this.notificationRowBinderImplProvider, this.bindEventManagerImplProvider, this.remoteInputUriControllerProvider, this.notificationGroupManagerLegacyProvider, this.notificationGroupAlertTransferHelperProvider, this.provideHeadsUpManagerPhoneProvider, this.headsUpControllerProvider, this.headsUpViewBinderProvider, this.builderProvider4, this.animatedImageNotificationManagerProvider, this.peopleSpaceWidgetManagerProvider));
            SmartActionsReceiver_Factory smartActionsReceiver_Factory = new SmartActionsReceiver_Factory(this.notificationListenerProvider, 4);
            this.notificationsControllerStubProvider = smartActionsReceiver_Factory;
            this.provideNotificationsControllerProvider = DoubleCheck.provider(new ScrollCaptureClient_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.notificationsControllerImplProvider, smartActionsReceiver_Factory, 1));
            Provider<FragmentService.FragmentCreator.Factory> provider69 = new Provider<FragmentService.FragmentCreator.Factory>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.7
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final FragmentService.FragmentCreator.Factory mo144get() {
                    return new FragmentCreatorFactory();
                }
            };
            this.fragmentCreatorFactoryProvider = provider69;
            this.fragmentServiceProvider = DoubleCheck.provider(new FragmentService_Factory(provider69, this.provideConfigurationControllerProvider, DaggerTitanGlobalRootComponent.this.dumpManagerProvider));
            this.providesStatusBarWindowViewProvider = DoubleCheck.provider(new SmartActionsReceiver_Factory(this.providerLayoutInflaterProvider, 5));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent24 = DaggerTitanGlobalRootComponent.this;
            Provider<StatusBarContentInsetsProvider> provider70 = DoubleCheck.provider(new StatusBarContentInsetsProvider_Factory(daggerTitanGlobalRootComponent24.contextProvider, this.provideConfigurationControllerProvider, daggerTitanGlobalRootComponent24.dumpManagerProvider, 0));
            this.statusBarContentInsetsProvider = provider70;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent25 = DaggerTitanGlobalRootComponent.this;
            this.statusBarWindowControllerProvider = DoubleCheck.provider(StatusBarWindowController_Factory.create(daggerTitanGlobalRootComponent25.contextProvider, this.providesStatusBarWindowViewProvider, daggerTitanGlobalRootComponent25.provideWindowManagerProvider, daggerTitanGlobalRootComponent25.provideIWindowManagerProvider, provider70, daggerTitanGlobalRootComponent25.provideResourcesProvider, daggerTitanGlobalRootComponent25.unfoldTransitionProgressProvider));
        }

        public final void initialize5(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<Object> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.statusBarWindowStateControllerProvider = DoubleCheck.provider(new QSFlagsModule_IsPMLiteEnabledFactory(DaggerTitanGlobalRootComponent.this.provideDisplayIdProvider, this.provideCommandQueueProvider, 3));
            this.carrierConfigTrackerProvider = DoubleCheck.provider(new CarrierConfigTracker_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider));
            this.callbackHandlerProvider = new KeyboardUI_Factory(GlobalConcurrencyModule_ProvideMainLooperFactory.InstanceHolder.INSTANCE, 6);
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
            Provider<AccessPointControllerImpl.WifiPickerTrackerFactory> provider = DoubleCheck.provider(new AccessPointControllerImpl_WifiPickerTrackerFactory_Factory(daggerTitanGlobalRootComponent.contextProvider, daggerTitanGlobalRootComponent.provideWifiManagerProvider, daggerTitanGlobalRootComponent.provideConnectivityManagagerProvider, daggerTitanGlobalRootComponent.provideMainHandlerProvider, this.provideBgHandlerProvider));
            this.wifiPickerTrackerFactoryProvider = provider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent2 = DaggerTitanGlobalRootComponent.this;
            this.provideAccessPointControllerImplProvider = DoubleCheck.provider(new StatusBarPolicyModule_ProvideAccessPointControllerImplFactory(daggerTitanGlobalRootComponent2.provideUserManagerProvider, this.provideUserTrackerProvider, daggerTitanGlobalRootComponent2.provideMainExecutorProvider, provider));
            Provider<LayoutInflater> provider2 = this.providerLayoutInflaterProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent3 = DaggerTitanGlobalRootComponent.this;
            this.toastFactoryProvider = DoubleCheck.provider(new ToastFactory_Factory(provider2, daggerTitanGlobalRootComponent3.providesPluginManagerProvider, daggerTitanGlobalRootComponent3.dumpManagerProvider, 0));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent4 = DaggerTitanGlobalRootComponent.this;
            Provider<LocationControllerImpl> provider3 = DoubleCheck.provider(LocationControllerImpl_Factory.create(daggerTitanGlobalRootComponent4.contextProvider, this.appOpsControllerImplProvider, this.deviceConfigProxyProvider, this.provideBgHandlerProvider, this.providesBroadcastDispatcherProvider, this.bootCompleteCacheImplProvider, this.provideUserTrackerProvider, daggerTitanGlobalRootComponent4.providePackageManagerProvider, daggerTitanGlobalRootComponent4.provideUiEventLoggerProvider, this.secureSettingsImplProvider));
            this.locationControllerImplProvider = provider3;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent5 = DaggerTitanGlobalRootComponent.this;
            InternetDialogController_Factory create = InternetDialogController_Factory.create(daggerTitanGlobalRootComponent5.contextProvider, daggerTitanGlobalRootComponent5.provideUiEventLoggerProvider, this.provideActivityStarterProvider, this.provideAccessPointControllerImplProvider, daggerTitanGlobalRootComponent5.provideSubcriptionManagerProvider, daggerTitanGlobalRootComponent5.provideTelephonyManagerProvider, daggerTitanGlobalRootComponent5.provideWifiManagerProvider, daggerTitanGlobalRootComponent5.provideConnectivityManagagerProvider, daggerTitanGlobalRootComponent5.provideMainHandlerProvider, daggerTitanGlobalRootComponent5.provideMainExecutorProvider, this.providesBroadcastDispatcherProvider, this.keyguardUpdateMonitorProvider, this.globalSettingsImplProvider, this.keyguardStateControllerImplProvider, daggerTitanGlobalRootComponent5.provideWindowManagerProvider, this.toastFactoryProvider, this.provideBgHandlerProvider, this.carrierConfigTrackerProvider, provider3, this.provideDialogLaunchAnimatorProvider);
            this.internetDialogControllerProvider = create;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent6 = DaggerTitanGlobalRootComponent.this;
            Provider<InternetDialogFactory> provider4 = DoubleCheck.provider(InternetDialogFactory_Factory.create(daggerTitanGlobalRootComponent6.provideMainHandlerProvider, this.provideBackgroundExecutorProvider, create, daggerTitanGlobalRootComponent6.contextProvider, daggerTitanGlobalRootComponent6.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.keyguardStateControllerImplProvider));
            this.internetDialogFactoryProvider = provider4;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent7 = DaggerTitanGlobalRootComponent.this;
            this.networkControllerImplProvider = DoubleCheck.provider(NetworkControllerImpl_Factory.create(daggerTitanGlobalRootComponent7.contextProvider, this.provideBgLooperProvider, this.provideBackgroundExecutorProvider, daggerTitanGlobalRootComponent7.provideSubcriptionManagerProvider, this.callbackHandlerProvider, this.provideDeviceProvisionedControllerProvider, this.providesBroadcastDispatcherProvider, daggerTitanGlobalRootComponent7.provideConnectivityManagagerProvider, daggerTitanGlobalRootComponent7.provideTelephonyManagerProvider, this.telephonyListenerManagerProvider, daggerTitanGlobalRootComponent7.provideWifiManagerProvider, daggerTitanGlobalRootComponent7.provideNetworkScoreManagerProvider, this.provideAccessPointControllerImplProvider, this.provideDemoModeControllerProvider, this.carrierConfigTrackerProvider, daggerTitanGlobalRootComponent7.provideMainHandlerProvider, provider4, this.featureFlagsReleaseProvider, daggerTitanGlobalRootComponent7.dumpManagerProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent8 = DaggerTitanGlobalRootComponent.this;
            Provider<SecurityControllerImpl> provider5 = DoubleCheck.provider(new SecurityControllerImpl_Factory(daggerTitanGlobalRootComponent8.contextProvider, this.provideBgHandlerProvider, this.providesBroadcastDispatcherProvider, this.provideBackgroundExecutorProvider, daggerTitanGlobalRootComponent8.dumpManagerProvider));
            this.securityControllerImplProvider = provider5;
            this.statusBarSignalPolicyProvider = DoubleCheck.provider(StatusBarSignalPolicy_Factory.create(DaggerTitanGlobalRootComponent.this.contextProvider, this.statusBarIconControllerImplProvider, this.carrierConfigTrackerProvider, this.networkControllerImplProvider, provider5, this.tunerServiceImplProvider, this.featureFlagsReleaseProvider));
            this.notificationWakeUpCoordinatorProvider = DoubleCheck.provider(ForegroundServiceNotificationListener_Factory.create$1(this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.screenOffAnimationControllerProvider));
            Provider<NotificationRoundnessManager> m = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline1.m(this.notificationSectionsFeatureManagerProvider, 4);
            this.notificationRoundnessManagerProvider = m;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent9 = DaggerTitanGlobalRootComponent.this;
            this.pulseExpansionHandlerProvider = DoubleCheck.provider(PulseExpansionHandler_Factory.create(daggerTitanGlobalRootComponent9.contextProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideHeadsUpManagerPhoneProvider, m, this.provideConfigurationControllerProvider, this.statusBarStateControllerImplProvider, this.falsingManagerProxyProvider, this.lockscreenShadeTransitionControllerProvider, this.falsingCollectorImplProvider, daggerTitanGlobalRootComponent9.dumpManagerProvider));
            this.dynamicPrivacyControllerProvider = DoubleCheck.provider(new DismissTimer_Factory(this.notificationLockscreenUserManagerGoogleProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider, 1));
            StatusBarInitializer_Factory statusBarInitializer_Factory = new StatusBarInitializer_Factory(this.provideNotificationsLogBufferProvider, 2);
            this.shadeEventCoordinatorLoggerProvider = statusBarInitializer_Factory;
            Provider<ShadeEventCoordinator> provider6 = DoubleCheck.provider(new ShadeEventCoordinator_Factory(DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider, statusBarInitializer_Factory, 0));
            this.shadeEventCoordinatorProvider = provider6;
            ForegroundServicesDialog_Factory foregroundServicesDialog_Factory = new ForegroundServicesDialog_Factory(this.provideNotificationEntryManagerProvider, 4);
            this.legacyNotificationPresenterExtensionsProvider = foregroundServicesDialog_Factory;
            this.provideNotifShadeEventSourceProvider = DoubleCheck.provider(new StatusBarContentInsetsProvider_Factory(this.notifPipelineFlagsProvider, provider6, foregroundServicesDialog_Factory, 1));
            Provider<INotificationManager> provider7 = DoubleCheck.provider(new ActivityIntentHelper_Factory(dependencyProvider, 6));
            this.provideINotificationManagerProvider = provider7;
            this.channelEditorDialogControllerProvider = DoubleCheck.provider(new ChannelEditorDialogController_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, provider7));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent10 = DaggerTitanGlobalRootComponent.this;
            this.assistantFeedbackControllerProvider = DoubleCheck.provider(new ScreenOnCoordinator_Factory(daggerTitanGlobalRootComponent10.provideMainHandlerProvider, daggerTitanGlobalRootComponent10.contextProvider, this.deviceConfigProxyProvider, 2));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent11 = DaggerTitanGlobalRootComponent.this;
            Provider<ZenModeControllerImpl> provider8 = DoubleCheck.provider(MediaDataFilter_Factory.create$2(daggerTitanGlobalRootComponent11.contextProvider, daggerTitanGlobalRootComponent11.provideMainHandlerProvider, this.providesBroadcastDispatcherProvider, daggerTitanGlobalRootComponent11.dumpManagerProvider, this.globalSettingsImplProvider));
            this.zenModeControllerImplProvider = provider8;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent12 = DaggerTitanGlobalRootComponent.this;
            this.provideBubblesManagerProvider = DoubleCheck.provider(SystemUIModule_ProvideBubblesManagerFactory.create(daggerTitanGlobalRootComponent12.contextProvider, this.setBubblesProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarStateControllerImplProvider, this.shadeControllerImplProvider, this.provideConfigurationControllerProvider, daggerTitanGlobalRootComponent12.provideIStatusBarServiceProvider, this.provideINotificationManagerProvider, this.provideNotificationVisibilityProvider, this.notificationInterruptStateProviderImplProvider, provider8, this.notificationLockscreenUserManagerGoogleProvider, this.notificationGroupManagerLegacyProvider, this.provideNotificationEntryManagerProvider, this.provideCommonNotifCollectionProvider, this.notifPipelineProvider, this.provideSysUiStateProvider, this.notifPipelineFlagsProvider, daggerTitanGlobalRootComponent12.dumpManagerProvider, daggerTitanGlobalRootComponent12.provideMainExecutorProvider));
            Provider<NotifPanelEventSource> provider9 = DoubleCheck.provider(NotifPanelEventSourceModule_ProvideManagerFactory.InstanceHolder.INSTANCE);
            this.bindEventSourceProvider = provider9;
            Provider<VisualStabilityCoordinator> provider10 = DoubleCheck.provider(VisualStabilityCoordinator_Factory.create(this.provideDelayableExecutorProvider, DaggerTitanGlobalRootComponent.this.dumpManagerProvider, this.provideHeadsUpManagerPhoneProvider, provider9, this.statusBarStateControllerImplProvider, this.visualStabilityProvider, this.wakefulnessLifecycleProvider));
            this.visualStabilityCoordinatorProvider = provider10;
            Provider<OnUserInteractionCallback> provider11 = DoubleCheck.provider(OverlayToggleTile_Factory.create(this.notifPipelineFlagsProvider, this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.notifCollectionProvider, this.provideNotificationVisibilityProvider, provider10, this.provideNotificationEntryManagerProvider, this.provideVisualStabilityManagerProvider, this.provideGroupMembershipManagerProvider));
            this.provideOnUserInteractionCallbackProvider = provider11;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent13 = DaggerTitanGlobalRootComponent.this;
            this.provideNotificationGutsManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationGutsManagerFactory.create(daggerTitanGlobalRootComponent13.contextProvider, this.optionalOfStatusBarProvider, daggerTitanGlobalRootComponent13.provideMainHandlerProvider, this.provideBgHandlerProvider, daggerTitanGlobalRootComponent13.provideAccessibilityManagerProvider, this.highPriorityProvider, this.provideINotificationManagerProvider, this.provideNotificationEntryManagerProvider, this.peopleSpaceWidgetManagerProvider, daggerTitanGlobalRootComponent13.provideLauncherAppsProvider, daggerTitanGlobalRootComponent13.provideShortcutManagerProvider, this.channelEditorDialogControllerProvider, this.provideUserTrackerProvider, this.assistantFeedbackControllerProvider, this.provideBubblesManagerProvider, daggerTitanGlobalRootComponent13.provideUiEventLoggerProvider, provider11, this.shadeControllerImplProvider, daggerTitanGlobalRootComponent13.dumpManagerProvider));
            this.expansionStateLoggerProvider = new NotificationLogger_ExpansionStateLogger_Factory(this.provideUiBackgroundExecutorProvider);
            Provider<NotificationPanelLogger> provider12 = DoubleCheck.provider(NotificationsModule_ProvideNotificationPanelLoggerFactory.InstanceHolder.INSTANCE);
            this.provideNotificationPanelLoggerProvider = provider12;
            this.provideNotificationLoggerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationLoggerFactory.create(this.notificationListenerProvider, this.provideUiBackgroundExecutorProvider, this.notifPipelineFlagsProvider, this.notifLiveDataStoreImplProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.statusBarStateControllerImplProvider, this.expansionStateLoggerProvider, provider12));
            this.foregroundServiceSectionControllerProvider = DoubleCheck.provider(new DependencyProvider_ProvidesViewMediatorCallbackFactory(this.provideNotificationEntryManagerProvider, this.foregroundServiceDismissalFeatureControllerProvider, 1));
            QSFragmentModule_ProvidesQuickQSPanelFactory qSFragmentModule_ProvidesQuickQSPanelFactory = new QSFragmentModule_ProvidesQuickQSPanelFactory(this.rowContentBindStageProvider, 4);
            this.dynamicChildBindControllerProvider = qSFragmentModule_ProvidesQuickQSPanelFactory;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent14 = DaggerTitanGlobalRootComponent.this;
            this.provideNotificationViewHierarchyManagerProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideNotificationViewHierarchyManagerFactory.create(daggerTitanGlobalRootComponent14.contextProvider, daggerTitanGlobalRootComponent14.provideMainHandlerProvider, this.featureFlagsReleaseProvider, this.notificationLockscreenUserManagerGoogleProvider, this.notificationGroupManagerLegacyProvider, this.provideVisualStabilityManagerProvider, this.statusBarStateControllerImplProvider, this.provideNotificationEntryManagerProvider, this.keyguardBypassControllerProvider, this.setBubblesProvider, this.dynamicPrivacyControllerProvider, this.foregroundServiceSectionControllerProvider, qSFragmentModule_ProvidesQuickQSPanelFactory, this.lowPriorityInflationHelperProvider, this.assistantFeedbackControllerProvider, this.notifPipelineFlagsProvider, this.keyguardUpdateMonitorProvider, this.keyguardStateControllerImplProvider));
            this.provideAccessibilityFloatingMenuControllerProvider = DoubleCheck.provider(new DependencyProvider_ProvideAccessibilityFloatingMenuControllerFactory(dependencyProvider, DaggerTitanGlobalRootComponent.this.contextProvider, this.accessibilityButtonTargetsObserverProvider, this.accessibilityButtonModeObserverProvider, this.keyguardUpdateMonitorProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent15 = DaggerTitanGlobalRootComponent.this;
            this.lockscreenWallpaperProvider = DoubleCheck.provider(LockscreenWallpaper_Factory.create(daggerTitanGlobalRootComponent15.provideWallpaperManagerProvider, this.keyguardUpdateMonitorProvider, daggerTitanGlobalRootComponent15.dumpManagerProvider, this.provideNotificationMediaManagerProvider, daggerTitanGlobalRootComponent15.provideMainHandlerProvider));
            Provider<NotificationIconAreaController> provider13 = DoubleCheck.provider(NotificationIconAreaController_Factory.create(DaggerTitanGlobalRootComponent.this.contextProvider, this.statusBarStateControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideNotificationMediaManagerProvider, this.notificationListenerProvider, this.dozeParametersProvider, this.setBubblesProvider, this.provideDemoModeControllerProvider, this.darkIconDispatcherImplProvider, this.statusBarWindowControllerProvider, this.screenOffAnimationControllerProvider));
            this.notificationIconAreaControllerProvider = provider13;
            this.dozeServiceHostProvider = DoubleCheck.provider(DozeServiceHost_Factory.create(this.dozeLogProvider, DaggerTitanGlobalRootComponent.this.providePowerManagerProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.provideDeviceProvisionedControllerProvider, this.provideHeadsUpManagerPhoneProvider, this.provideBatteryControllerProvider, this.scrimControllerProvider, this.biometricUnlockControllerProvider, this.newKeyguardViewMediatorProvider, this.assistManagerGoogleProvider, this.dozeScrimControllerProvider, this.keyguardUpdateMonitorProvider, this.pulseExpansionHandlerProvider, this.notificationShadeWindowControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.authControllerProvider, provider13));
            this.screenPinningRequestProvider = new ScreenPinningRequest_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.optionalOfStatusBarProvider, 0);
            Provider<RingerModeTrackerImpl> provider14 = DoubleCheck.provider(new RingerModeTrackerImpl_Factory(DaggerTitanGlobalRootComponent.this.provideAudioManagerProvider, this.providesBroadcastDispatcherProvider, this.provideBackgroundExecutorProvider, 0));
            this.ringerModeTrackerImplProvider = provider14;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent16 = DaggerTitanGlobalRootComponent.this;
            this.volumeDialogControllerImplProvider = DoubleCheck.provider(VolumeDialogControllerImpl_Factory.create(daggerTitanGlobalRootComponent16.contextProvider, this.providesBroadcastDispatcherProvider, provider14, daggerTitanGlobalRootComponent16.provideAudioManagerProvider, daggerTitanGlobalRootComponent16.provideNotificationManagerProvider, this.vibratorHelperProvider, daggerTitanGlobalRootComponent16.provideIAudioServiceProvider, daggerTitanGlobalRootComponent16.provideAccessibilityManagerProvider, daggerTitanGlobalRootComponent16.providePackageManagerProvider, this.wakefulnessLifecycleProvider));
            Provider<AccessibilityManagerWrapper> m2 = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline3.m(DaggerTitanGlobalRootComponent.this.provideAccessibilityManagerProvider, 5);
            this.accessibilityManagerWrapperProvider = m2;
            VolumeModule_ProvideVolumeDialogFactory create2 = VolumeModule_ProvideVolumeDialogFactory.create(DaggerTitanGlobalRootComponent.this.contextProvider, this.volumeDialogControllerImplProvider, m2, this.provideDeviceProvisionedControllerProvider, this.provideConfigurationControllerProvider, this.mediaOutputDialogFactoryProvider, this.provideActivityStarterProvider);
            this.provideVolumeDialogProvider = create2;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent17 = DaggerTitanGlobalRootComponent.this;
            this.volumeDialogComponentProvider = DoubleCheck.provider(VolumeDialogComponent_Factory.create(daggerTitanGlobalRootComponent17.contextProvider, this.newKeyguardViewMediatorProvider, this.provideActivityStarterProvider, this.volumeDialogControllerImplProvider, this.provideDemoModeControllerProvider, daggerTitanGlobalRootComponent17.pluginDependencyProvider, this.extensionControllerImplProvider, this.tunerServiceImplProvider, create2));
            this.statusBarComponentFactoryProvider = new Provider<StatusBarComponent.Factory>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.8
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final StatusBarComponent.Factory mo144get() {
                    return new StatusBarComponentFactory();
                }
            };
            Provider<GroupExpansionManager> provider15 = DoubleCheck.provider(new TvStatusBar_Factory(this.notifPipelineFlagsProvider, this.provideGroupMembershipManagerProvider, this.notificationGroupManagerLegacyProvider, 1));
            this.provideGroupExpansionManagerProvider = provider15;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent18 = DaggerTitanGlobalRootComponent.this;
            this.statusBarRemoteInputCallbackProvider = DoubleCheck.provider(StatusBarRemoteInputCallback_Factory.create(daggerTitanGlobalRootComponent18.contextProvider, provider15, this.notificationLockscreenUserManagerGoogleProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.provideActivityStarterProvider, this.shadeControllerImplProvider, this.provideCommandQueueProvider, this.actionClickLoggerProvider, daggerTitanGlobalRootComponent18.provideMainExecutorProvider));
            this.activityIntentHelperProvider = DoubleCheck.provider(new ActivityIntentHelper_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 0));
            ColorChangeHandler_Factory colorChangeHandler_Factory = new ColorChangeHandler_Factory(this.provideNotifInteractionLogBufferProvider, 4);
            this.statusBarNotificationActivityStarterLoggerProvider = colorChangeHandler_Factory;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent19 = DaggerTitanGlobalRootComponent.this;
            this.builderProvider5 = DoubleCheck.provider(StatusBarNotificationActivityStarter_Builder_Factory.create(daggerTitanGlobalRootComponent19.contextProvider, this.provideCommandQueueProvider, daggerTitanGlobalRootComponent19.provideMainHandlerProvider, this.provideUiBackgroundExecutorProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.provideNotificationVisibilityProvider, this.provideHeadsUpManagerPhoneProvider, this.provideActivityStarterProvider, this.notificationClickNotifierProvider, this.statusBarStateControllerImplProvider, this.statusBarKeyguardViewManagerProvider, daggerTitanGlobalRootComponent19.provideKeyguardManagerProvider, daggerTitanGlobalRootComponent19.provideIDreamManagerProvider, this.provideBubblesManagerProvider, this.assistManagerGoogleProvider, this.provideNotificationRemoteInputManagerProvider, this.provideGroupMembershipManagerProvider, this.notificationLockscreenUserManagerGoogleProvider, this.shadeControllerImplProvider, this.keyguardStateControllerImplProvider, this.notificationInterruptStateProviderImplProvider, this.provideLockPatternUtilsProvider, this.statusBarRemoteInputCallbackProvider, this.activityIntentHelperProvider, this.notifPipelineFlagsProvider, this.provideMetricsLoggerProvider, colorChangeHandler_Factory, this.provideOnUserInteractionCallbackProvider));
            this.initControllerProvider = DoubleCheck.provider(InitController_Factory.InstanceHolder.INSTANCE);
            this.provideTimeTickHandlerProvider = DoubleCheck.provider(new ScreenLifecycle_Factory(dependencyProvider, 7));
            this.userInfoControllerImplProvider = DoubleCheck.provider(new UsbDebuggingActivity_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 3));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent20 = DaggerTitanGlobalRootComponent.this;
            this.castControllerImplProvider = DoubleCheck.provider(new CastControllerImpl_Factory(daggerTitanGlobalRootComponent20.contextProvider, daggerTitanGlobalRootComponent20.dumpManagerProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent21 = DaggerTitanGlobalRootComponent.this;
            this.hotspotControllerImplProvider = DoubleCheck.provider(new HotspotControllerImpl_Factory(daggerTitanGlobalRootComponent21.contextProvider, daggerTitanGlobalRootComponent21.provideMainHandlerProvider, this.provideBgHandlerProvider, daggerTitanGlobalRootComponent21.dumpManagerProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent22 = DaggerTitanGlobalRootComponent.this;
            this.bluetoothControllerImplProvider = DoubleCheck.provider(new BluetoothControllerImpl_Factory(daggerTitanGlobalRootComponent22.contextProvider, daggerTitanGlobalRootComponent22.dumpManagerProvider, this.provideBgLooperProvider, this.provideLocalBluetoothControllerProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent23 = DaggerTitanGlobalRootComponent.this;
            this.nextAlarmControllerImplProvider = DoubleCheck.provider(new ToastFactory_Factory(daggerTitanGlobalRootComponent23.provideAlarmManagerProvider, this.providesBroadcastDispatcherProvider, daggerTitanGlobalRootComponent23.dumpManagerProvider, 1));
            RotationPolicyWrapperImpl_Factory rotationPolicyWrapperImpl_Factory = new RotationPolicyWrapperImpl_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.secureSettingsImplProvider, 0);
            this.rotationPolicyWrapperImplProvider = rotationPolicyWrapperImpl_Factory;
            this.bindRotationPolicyWrapperProvider = DoubleCheck.provider(rotationPolicyWrapperImpl_Factory);
            Provider<DeviceStateRotationLockSettingsManager> provider16 = DoubleCheck.provider(new StatusBarInitializer_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 5));
            this.provideAutoRotateSettingsManagerProvider = provider16;
            Provider<RotationPolicyWrapper> provider17 = this.bindRotationPolicyWrapperProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent24 = DaggerTitanGlobalRootComponent.this;
            this.deviceStateRotationLockSettingControllerProvider = DoubleCheck.provider(new KeyguardSmartspaceController_Factory(provider17, daggerTitanGlobalRootComponent24.provideDeviceStateManagerProvider, daggerTitanGlobalRootComponent24.provideMainExecutorProvider, provider16, 1));
            WallpaperController_Factory wallpaperController_Factory = new WallpaperController_Factory(DaggerTitanGlobalRootComponent.this.provideResourcesProvider, 3);
            this.providesDeviceStateRotationLockDefaultsProvider = wallpaperController_Factory;
            this.rotationLockControllerImplProvider = DoubleCheck.provider(new SetupWizard_Factory(this.bindRotationPolicyWrapperProvider, this.deviceStateRotationLockSettingControllerProvider, wallpaperController_Factory, 2));
            this.provideDataSaverControllerProvider = DoubleCheck.provider(new WMShellBaseModule_ProvideSystemWindowsFactory(dependencyProvider, this.networkControllerImplProvider));
            this.provideSensorPrivacyControllerProvider = DoubleCheck.provider(new SmartActionsReceiver_Factory(DaggerTitanGlobalRootComponent.this.provideSensorPrivacyManagerProvider, 7));
            this.dateFormatUtilProvider = new DateFormatUtil_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 0);
            Provider<LogBuffer> provider18 = DoubleCheck.provider(new LogModule_ProvidePrivacyLogBufferFactory(this.logBufferFactoryProvider, 0));
            this.providePrivacyLogBufferProvider = provider18;
            PrivacyLogger_Factory privacyLogger_Factory = new PrivacyLogger_Factory(provider18, 0);
            this.privacyLoggerProvider = privacyLogger_Factory;
            Provider<AppOpsControllerImpl> provider19 = this.appOpsControllerImplProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent25 = DaggerTitanGlobalRootComponent.this;
            this.privacyItemControllerProvider = DoubleCheck.provider(PrivacyItemController_Factory.create(provider19, daggerTitanGlobalRootComponent25.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.deviceConfigProxyProvider, this.provideUserTrackerProvider, privacyLogger_Factory, this.bindSystemClockProvider, daggerTitanGlobalRootComponent25.dumpManagerProvider));
            Provider<StatusBarIconControllerImpl> provider20 = this.statusBarIconControllerImplProvider;
            Provider<CommandQueue> provider21 = this.provideCommandQueueProvider;
            Provider<BroadcastDispatcher> provider22 = this.providesBroadcastDispatcherProvider;
            Provider<Executor> provider23 = this.provideUiBackgroundExecutorProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent26 = DaggerTitanGlobalRootComponent.this;
            this.phoneStatusBarPolicyProvider = PhoneStatusBarPolicy_Factory.create(provider20, provider21, provider22, provider23, daggerTitanGlobalRootComponent26.provideResourcesProvider, this.castControllerImplProvider, this.hotspotControllerImplProvider, this.bluetoothControllerImplProvider, this.nextAlarmControllerImplProvider, this.userInfoControllerImplProvider, this.rotationLockControllerImplProvider, this.provideDataSaverControllerProvider, this.zenModeControllerImplProvider, this.provideDeviceProvisionedControllerProvider, this.keyguardStateControllerImplProvider, this.locationControllerImplProvider, this.provideSensorPrivacyControllerProvider, daggerTitanGlobalRootComponent26.provideIActivityManagerProvider, daggerTitanGlobalRootComponent26.provideAlarmManagerProvider, daggerTitanGlobalRootComponent26.provideUserManagerProvider, daggerTitanGlobalRootComponent26.provideDevicePolicyManagerProvider, this.recordingControllerProvider, daggerTitanGlobalRootComponent26.provideTelecomManagerProvider, daggerTitanGlobalRootComponent26.provideDisplayIdProvider, this.provideSharePreferencesProvider, this.dateFormatUtilProvider, this.ringerModeTrackerImplProvider, this.privacyItemControllerProvider, this.privacyLoggerProvider);
            this.statusBarTouchableRegionManagerProvider = DoubleCheck.provider(new StatusBarTouchableRegionManager_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.notificationShadeWindowControllerImplProvider, this.provideConfigurationControllerProvider, this.provideHeadsUpManagerPhoneProvider));
            this.factoryProvider6 = new BrightnessSliderController_Factory_Factory(this.falsingManagerProxyProvider);
            this.ongoingCallLoggerProvider = DoubleCheck.provider(new MediaOutputDialogReceiver_Factory(DaggerTitanGlobalRootComponent.this.provideUiEventLoggerProvider, 2));
            Provider<LogBuffer> provider24 = DoubleCheck.provider(new DependencyProvider_ProvideHandlerFactory(this.logBufferFactoryProvider, 2));
            this.provideSwipeAwayGestureLogBufferProvider = provider24;
            SmartActionsReceiver_Factory smartActionsReceiver_Factory = new SmartActionsReceiver_Factory(provider24, 3);
            this.swipeStatusBarAwayGestureLoggerProvider = smartActionsReceiver_Factory;
            this.swipeStatusBarAwayGestureHandlerProvider = DoubleCheck.provider(new SwipeStatusBarAwayGestureHandler_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.statusBarWindowControllerProvider, smartActionsReceiver_Factory));
            Provider<OngoingCallFlags> provider25 = DoubleCheck.provider(new MediaFlags_Factory(this.featureFlagsReleaseProvider, 3));
            this.ongoingCallFlagsProvider = provider25;
            Provider<CommonNotifCollection> provider26 = this.provideCommonNotifCollectionProvider;
            Provider<SystemClock> provider27 = this.bindSystemClockProvider;
            Provider<ActivityStarter> provider28 = this.provideActivityStarterProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent27 = DaggerTitanGlobalRootComponent.this;
            this.provideOngoingCallControllerProvider = DoubleCheck.provider(StatusBarDependenciesModule_ProvideOngoingCallControllerFactory.create(provider26, provider27, provider28, daggerTitanGlobalRootComponent27.provideMainExecutorProvider, daggerTitanGlobalRootComponent27.provideIActivityManagerProvider, this.ongoingCallLoggerProvider, daggerTitanGlobalRootComponent27.dumpManagerProvider, this.statusBarWindowControllerProvider, this.swipeStatusBarAwayGestureHandlerProvider, this.statusBarStateControllerImplProvider, provider25));
            Provider<CommandQueue> provider29 = this.provideCommandQueueProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent28 = DaggerTitanGlobalRootComponent.this;
            this.statusBarHideIconsForBouncerManagerProvider = DoubleCheck.provider(new QSFgsManagerFooter_Factory(provider29, daggerTitanGlobalRootComponent28.provideMainDelayableExecutorProvider, this.statusBarWindowStateControllerProvider, daggerTitanGlobalRootComponent28.dumpManagerProvider, 1));
            Provider<LogBuffer> provider30 = DoubleCheck.provider(new ForegroundServicesDialog_Factory(this.logBufferFactoryProvider, 8));
            this.provideNotifVoiceReplyLogBufferProvider = provider30;
            Provider<NotificationVoiceReplyLogger> provider31 = DoubleCheck.provider(new SystemUIDialogManager_Factory(provider30, DaggerTitanGlobalRootComponent.this.provideUiEventLoggerProvider, 1));
            this.notificationVoiceReplyLoggerProvider = provider31;
            Provider<CommonNotifCollection> provider32 = this.provideCommonNotifCollectionProvider;
            Provider<BindEventManagerImpl> provider33 = this.bindEventManagerImplProvider;
            Provider<NotifLiveDataStoreImpl> provider34 = this.notifLiveDataStoreImplProvider;
            Provider<NotificationLockscreenUserManagerGoogle> provider35 = this.notificationLockscreenUserManagerGoogleProvider;
            Provider<NotificationRemoteInputManager> provider36 = this.provideNotificationRemoteInputManagerProvider;
            Provider<LockscreenShadeTransitionController> provider37 = this.lockscreenShadeTransitionControllerProvider;
            Provider<NotificationShadeWindowControllerImpl> provider38 = this.notificationShadeWindowControllerImplProvider;
            Provider<StatusBarKeyguardViewManager> provider39 = this.statusBarKeyguardViewManagerProvider;
            Provider<StatusBarGoogle> provider40 = this.statusBarGoogleProvider;
            Provider<StatusBarStateControllerImpl> provider41 = this.statusBarStateControllerImplProvider;
            Provider<HeadsUpManagerPhone> provider42 = this.provideHeadsUpManagerPhoneProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent29 = DaggerTitanGlobalRootComponent.this;
            Provider<NotificationVoiceReplyController> provider43 = DoubleCheck.provider(NotificationVoiceReplyController_Factory.create(provider32, provider33, provider34, provider35, provider36, provider37, provider38, provider39, provider40, provider41, provider42, daggerTitanGlobalRootComponent29.providePowerManagerProvider, daggerTitanGlobalRootComponent29.contextProvider, provider31));
            this.notificationVoiceReplyControllerProvider = provider43;
            Provider<DebugNotificationVoiceReplyClient> provider44 = DoubleCheck.provider(new DebugNotificationVoiceReplyClient_Factory(this.providesBroadcastDispatcherProvider, this.notificationLockscreenUserManagerGoogleProvider, provider43));
            this.debugNotificationVoiceReplyClientProvider = provider44;
            this.provideNotificationVoiceReplyClientProvider = DoubleCheck.provider(new PrivacyLogger_Factory(provider44, 6));
            this.providesMainMessageRouterProvider = new QSFragmentModule_ProvideThemedContextFactory(DaggerTitanGlobalRootComponent.this.provideMainDelayableExecutorProvider, 4);
            Provider<CommandRegistry> provider45 = this.commandRegistryProvider;
            Provider<BatteryController> provider46 = this.provideBatteryControllerProvider;
            Provider<ConfigurationController> provider47 = this.provideConfigurationControllerProvider;
            Provider<FeatureFlagsRelease> provider48 = this.featureFlagsReleaseProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent30 = DaggerTitanGlobalRootComponent.this;
            this.wiredChargingRippleControllerProvider = DoubleCheck.provider(WiredChargingRippleController_Factory.create(provider45, provider46, provider47, provider48, daggerTitanGlobalRootComponent30.contextProvider, daggerTitanGlobalRootComponent30.provideWindowManagerProvider, this.bindSystemClockProvider, daggerTitanGlobalRootComponent30.provideUiEventLoggerProvider));
            Provider<StatusBarGoogle> provider49 = this.statusBarGoogleProvider;
            Provider<SmartSpaceController> provider50 = this.smartSpaceControllerProvider;
            Provider<WallpaperNotifier> provider51 = this.wallpaperNotifierProvider;
            Provider<Optional<ReverseChargingViewController>> provider52 = this.provideReverseChargingViewControllerOptionalProvider;
            Provider<Context> provider53 = DaggerTitanGlobalRootComponent.this.contextProvider;
            Provider<NotificationsController> provider54 = this.provideNotificationsControllerProvider;
            Provider<FragmentService> provider55 = this.fragmentServiceProvider;
            Provider<LightBarController> provider56 = this.lightBarControllerProvider;
            Provider<AutoHideController> provider57 = this.provideAutoHideControllerProvider;
            Provider<StatusBarWindowController> provider58 = this.statusBarWindowControllerProvider;
            Provider<StatusBarWindowStateController> provider59 = this.statusBarWindowStateControllerProvider;
            Provider<KeyguardUpdateMonitor> provider60 = this.keyguardUpdateMonitorProvider;
            Provider<StatusBarSignalPolicy> provider61 = this.statusBarSignalPolicyProvider;
            Provider<PulseExpansionHandler> provider62 = this.pulseExpansionHandlerProvider;
            Provider<NotificationWakeUpCoordinator> provider63 = this.notificationWakeUpCoordinatorProvider;
            Provider<KeyguardBypassController> provider64 = this.keyguardBypassControllerProvider;
            Provider<KeyguardStateControllerImpl> provider65 = this.keyguardStateControllerImplProvider;
            Provider<HeadsUpManagerPhone> provider66 = this.provideHeadsUpManagerPhoneProvider;
            Provider<DynamicPrivacyController> provider67 = this.dynamicPrivacyControllerProvider;
            Provider<FalsingManagerProxy> provider68 = this.falsingManagerProxyProvider;
            Provider provider69 = this.falsingCollectorImplProvider;
            Provider<BroadcastDispatcher> provider70 = this.providesBroadcastDispatcherProvider;
            Provider<NotifShadeEventSource> provider71 = this.provideNotifShadeEventSourceProvider;
            Provider<NotificationEntryManager> provider72 = this.provideNotificationEntryManagerProvider;
            Provider<NotificationGutsManager> provider73 = this.provideNotificationGutsManagerProvider;
            Provider<NotificationLogger> provider74 = this.provideNotificationLoggerProvider;
            Provider<NotificationInterruptStateProviderImpl> provider75 = this.notificationInterruptStateProviderImplProvider;
            Provider<NotificationViewHierarchyManager> provider76 = this.provideNotificationViewHierarchyManagerProvider;
            Provider<PanelExpansionStateManager> provider77 = this.panelExpansionStateManagerProvider;
            Provider<KeyguardViewMediator> provider78 = this.newKeyguardViewMediatorProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent31 = DaggerTitanGlobalRootComponent.this;
            Provider<DisplayMetrics> provider79 = daggerTitanGlobalRootComponent31.provideDisplayMetricsProvider;
            Provider<MetricsLogger> provider80 = this.provideMetricsLoggerProvider;
            Provider<Executor> provider81 = this.provideUiBackgroundExecutorProvider;
            Provider<NotificationMediaManager> provider82 = this.provideNotificationMediaManagerProvider;
            Provider<NotificationLockscreenUserManagerGoogle> provider83 = this.notificationLockscreenUserManagerGoogleProvider;
            Provider<NotificationRemoteInputManager> provider84 = this.provideNotificationRemoteInputManagerProvider;
            Provider<UserSwitcherController> provider85 = this.userSwitcherControllerProvider;
            Provider<NetworkControllerImpl> provider86 = this.networkControllerImplProvider;
            Provider<BatteryController> provider87 = this.provideBatteryControllerProvider;
            Provider<SysuiColorExtractor> provider88 = this.sysuiColorExtractorProvider;
            Provider<ScreenLifecycle> provider89 = daggerTitanGlobalRootComponent31.screenLifecycleProvider;
            Provider<WakefulnessLifecycle> provider90 = this.wakefulnessLifecycleProvider;
            Provider<StatusBarStateControllerImpl> provider91 = this.statusBarStateControllerImplProvider;
            Provider<Optional<BubblesManager>> provider92 = this.provideBubblesManagerProvider;
            Provider<Optional<Bubbles>> provider93 = this.setBubblesProvider;
            Provider<VisualStabilityManager> provider94 = this.provideVisualStabilityManagerProvider;
            Provider<DeviceProvisionedController> provider95 = this.provideDeviceProvisionedControllerProvider;
            Provider<NavigationBarController> provider96 = this.navigationBarControllerProvider;
            Provider<AccessibilityFloatingMenuController> provider97 = this.provideAccessibilityFloatingMenuControllerProvider;
            Provider<AssistManagerGoogle> provider98 = this.assistManagerGoogleProvider;
            Provider<ConfigurationController> provider99 = this.provideConfigurationControllerProvider;
            Provider<NotificationShadeWindowControllerImpl> provider100 = this.notificationShadeWindowControllerImplProvider;
            Provider<DozeParameters> provider101 = this.dozeParametersProvider;
            Provider<ScrimController> provider102 = this.scrimControllerProvider;
            Provider<LockscreenWallpaper> provider103 = this.lockscreenWallpaperProvider;
            Provider<LockscreenGestureLogger> provider104 = this.lockscreenGestureLoggerProvider;
            Provider<BiometricUnlockController> provider105 = this.biometricUnlockControllerProvider;
            Provider<NotificationShadeDepthController> provider106 = this.notificationShadeDepthControllerProvider;
            Provider<DozeServiceHost> provider107 = this.dozeServiceHostProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent32 = DaggerTitanGlobalRootComponent.this;
            Provider<PowerManager> provider108 = daggerTitanGlobalRootComponent32.providePowerManagerProvider;
            Provider<ScreenPinningRequest> provider109 = this.screenPinningRequestProvider;
            Provider<DozeScrimController> provider110 = this.dozeScrimControllerProvider;
            Provider<VolumeDialogComponent> provider111 = this.volumeDialogComponentProvider;
            Provider<CommandQueue> provider112 = this.provideCommandQueueProvider;
            Provider<StatusBarComponent.Factory> provider113 = this.statusBarComponentFactoryProvider;
            Provider<PluginManager> provider114 = daggerTitanGlobalRootComponent32.providesPluginManagerProvider;
            Provider<Optional<LegacySplitScreen>> provider115 = this.setLegacySplitScreenProvider;
            Provider<StatusBarNotificationActivityStarter.Builder> provider116 = this.builderProvider5;
            Provider<ShadeControllerImpl> provider117 = this.shadeControllerImplProvider;
            Provider<StatusBarKeyguardViewManager> provider118 = this.statusBarKeyguardViewManagerProvider;
            Provider<ViewMediatorCallback> provider119 = this.providesViewMediatorCallbackProvider;
            Provider<InitController> provider120 = this.initControllerProvider;
            Provider<Handler> provider121 = this.provideTimeTickHandlerProvider;
            Provider<PluginDependencyProvider> provider122 = DaggerTitanGlobalRootComponent.this.pluginDependencyProvider;
            Provider<KeyguardDismissUtil> provider123 = this.keyguardDismissUtilProvider;
            Provider<ExtensionControllerImpl> provider124 = this.extensionControllerImplProvider;
            Provider<UserInfoControllerImpl> provider125 = this.userInfoControllerImplProvider;
            Provider<PhoneStatusBarPolicy> provider126 = this.phoneStatusBarPolicyProvider;
            Provider<KeyguardIndicationControllerGoogle> provider127 = this.keyguardIndicationControllerGoogleProvider;
            Provider<DemoModeController> provider128 = this.provideDemoModeControllerProvider;
            Provider<StatusBarTouchableRegionManager> provider129 = this.statusBarTouchableRegionManagerProvider;
            Provider<NotificationIconAreaController> provider130 = this.notificationIconAreaControllerProvider;
            Provider<BrightnessSliderController.Factory> provider131 = this.factoryProvider6;
            Provider<ScreenOffAnimationController> provider132 = this.screenOffAnimationControllerProvider;
            Provider<WallpaperController> provider133 = this.wallpaperControllerProvider;
            Provider<OngoingCallController> provider134 = this.provideOngoingCallControllerProvider;
            Provider<StatusBarHideIconsForBouncerManager> provider135 = this.statusBarHideIconsForBouncerManagerProvider;
            Provider<LockscreenShadeTransitionController> provider136 = this.lockscreenShadeTransitionControllerProvider;
            Provider<FeatureFlagsRelease> provider137 = this.featureFlagsReleaseProvider;
            Provider<Optional<NotificationVoiceReplyClient>> provider138 = this.provideNotificationVoiceReplyClientProvider;
            Provider<KeyguardUnlockAnimationController> provider139 = this.keyguardUnlockAnimationControllerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent33 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider49, DoubleCheck.provider(StatusBarGoogle_Factory.create(provider50, provider51, provider52, provider53, provider54, provider55, provider56, provider57, provider58, provider59, provider60, provider61, provider62, provider63, provider64, provider65, provider66, provider67, provider68, provider69, provider70, provider71, provider72, provider73, provider74, provider75, provider76, provider77, provider78, provider79, provider80, provider81, provider82, provider83, provider84, provider85, provider86, provider87, provider88, provider89, provider90, provider91, provider92, provider93, provider94, provider95, provider96, provider97, provider98, provider99, provider100, provider101, provider102, provider103, provider104, provider105, provider106, provider107, provider108, provider109, provider110, provider111, provider112, provider113, provider114, provider115, provider116, provider117, provider118, provider119, provider120, provider121, provider122, provider123, provider124, provider125, provider126, provider127, provider128, provider129, provider130, provider131, provider132, provider133, provider134, provider135, provider136, provider137, provider138, provider139, daggerTitanGlobalRootComponent33.provideMainHandlerProvider, daggerTitanGlobalRootComponent33.provideMainDelayableExecutorProvider, this.providesMainMessageRouterProvider, daggerTitanGlobalRootComponent33.provideWallpaperManagerProvider, this.setStartingSurfaceProvider, this.provideActivityLaunchAnimatorProvider, daggerTitanGlobalRootComponent33.provideAlarmManagerProvider, this.notifPipelineFlagsProvider, daggerTitanGlobalRootComponent33.provideInteractionJankMonitorProvider, daggerTitanGlobalRootComponent33.provideDeviceStateManagerProvider, this.dreamOverlayStateControllerProvider, this.wiredChargingRippleControllerProvider)));
            DelegateFactory.setDelegate(this.optionalOfStatusBarProvider, new PresentJdkOptionalInstanceProvider(this.statusBarGoogleProvider));
            Provider<ActivityStarterDelegate> provider140 = DoubleCheck.provider(new ActivityStarterDelegate_Factory(this.optionalOfStatusBarProvider, 0));
            this.activityStarterDelegateProvider = provider140;
            DelegateFactory.setDelegate(this.provideActivityStarterProvider, new CommunalSourceMonitor_Factory(provider140, DaggerTitanGlobalRootComponent.this.pluginDependencyProvider, 1));
            this.setTaskViewFactoryProvider = InstanceFactory.create(optional7);
            Provider<ControlsMetricsLoggerImpl> provider141 = DoubleCheck.provider(ControlsMetricsLoggerImpl_Factory.InstanceHolder.INSTANCE);
            this.controlsMetricsLoggerImplProvider = provider141;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent34 = DaggerTitanGlobalRootComponent.this;
            this.controlActionCoordinatorImplProvider = DoubleCheck.provider(ControlActionCoordinatorImpl_Factory.create(daggerTitanGlobalRootComponent34.contextProvider, this.provideDelayableExecutorProvider, daggerTitanGlobalRootComponent34.provideMainDelayableExecutorProvider, this.provideActivityStarterProvider, this.keyguardStateControllerImplProvider, this.setTaskViewFactoryProvider, provider141, this.vibratorHelperProvider));
            this.customIconCacheProvider = DoubleCheck.provider(CustomIconCache_Factory.InstanceHolder.INSTANCE);
            Provider<ControlsControllerImpl> provider142 = this.controlsControllerImplProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent35 = DaggerTitanGlobalRootComponent.this;
            this.controlsUiControllerImplProvider = DoubleCheck.provider(RotationLockTile_Factory.create(provider142, daggerTitanGlobalRootComponent35.contextProvider, daggerTitanGlobalRootComponent35.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsListingControllerImplProvider, this.provideSharePreferencesProvider, this.controlActionCoordinatorImplProvider, this.provideActivityStarterProvider, this.shadeControllerImplProvider, this.customIconCacheProvider, this.controlsMetricsLoggerImplProvider, this.keyguardStateControllerImplProvider));
            Provider<ControlsBindingControllerImpl> provider143 = DoubleCheck.provider(new KeyguardBiometricLockoutLogger_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsControllerImplProvider, this.provideUserTrackerProvider, 1));
            this.controlsBindingControllerImplProvider = provider143;
            Provider<Optional<ControlsFavoritePersistenceWrapper>> provider144 = DaggerTitanGlobalRootComponent.ABSENT_JDK_OPTIONAL_PROVIDER;
            this.optionalOfControlsFavoritePersistenceWrapperProvider = provider144;
            Provider<ControlsControllerImpl> provider145 = this.controlsControllerImplProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent36 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider145, DoubleCheck.provider(ControlsControllerImpl_Factory.create(daggerTitanGlobalRootComponent36.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsUiControllerImplProvider, provider143, this.controlsListingControllerImplProvider, this.providesBroadcastDispatcherProvider, provider144, daggerTitanGlobalRootComponent36.dumpManagerProvider, this.provideUserTrackerProvider)));
            this.controlsProviderSelectorActivityProvider = ControlsProviderSelectorActivity_Factory.create(DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.controlsListingControllerImplProvider, this.controlsControllerImplProvider, this.providesBroadcastDispatcherProvider, this.controlsUiControllerImplProvider);
        }

        public final void initialize6(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<Object> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.controlsFavoritingActivityProvider = SystemUIService_Factory.create$1(DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider, this.controlsControllerImplProvider, this.controlsListingControllerImplProvider, this.providesBroadcastDispatcherProvider, this.controlsUiControllerImplProvider);
            Provider<ControlsControllerImpl> provider = this.controlsControllerImplProvider;
            Provider<BroadcastDispatcher> provider2 = this.providesBroadcastDispatcherProvider;
            Provider<CustomIconCache> provider3 = this.customIconCacheProvider;
            Provider<ControlsUiControllerImpl> provider4 = this.controlsUiControllerImplProvider;
            this.controlsEditingActivityProvider = new ControlsEditingActivity_Factory(provider, provider2, provider3, provider4, 0);
            this.controlsRequestDialogProvider = new ActionProxyReceiver_Factory(provider, provider2, this.controlsListingControllerImplProvider, 1);
            this.controlsActivityProvider = new ControlsActivity_Factory(provider4, provider2, 0);
            this.userSwitcherActivityProvider = GarbageMonitor_Factory.create(this.userSwitcherControllerProvider, provider2, this.providerLayoutInflaterProvider, this.falsingManagerProxyProvider, DaggerTitanGlobalRootComponent.this.provideUserManagerProvider, this.shadeControllerImplProvider);
            Provider<KeyguardStateControllerImpl> provider5 = this.keyguardStateControllerImplProvider;
            Provider<KeyguardDismissUtil> provider6 = this.keyguardDismissUtilProvider;
            Provider<ActivityStarter> provider7 = this.provideActivityStarterProvider;
            Provider<Executor> provider8 = this.provideBackgroundExecutorProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
            this.walletActivityProvider = WalletActivity_Factory.create(provider5, provider6, provider7, provider8, daggerTitanGlobalRootComponent.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.falsingCollectorImplProvider, this.provideUserTrackerProvider, this.keyguardUpdateMonitorProvider, this.statusBarKeyguardViewManagerProvider, daggerTitanGlobalRootComponent.provideUiEventLoggerProvider);
            this.tunerActivityProvider = new TunerActivity_Factory(this.provideDemoModeControllerProvider, this.tunerServiceImplProvider);
            this.foregroundServicesDialogProvider = new ForegroundServicesDialog_Factory(this.provideMetricsLoggerProvider, 0);
            Provider<BroadcastDispatcher> provider9 = this.providesBroadcastDispatcherProvider;
            this.workLockActivityProvider = new DozeLogger_Factory(provider9, 2);
            this.brightnessDialogProvider = new BrightnessDialog_Factory(provider9, this.factoryProvider6, this.provideBgHandlerProvider);
            this.usbDebuggingActivityProvider = new UsbDebuggingActivity_Factory(provider9, 0);
            this.usbDebuggingSecondaryUserActivityProvider = new UsbDebuggingSecondaryUserActivity_Factory(provider9, 0);
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent2 = DaggerTitanGlobalRootComponent.this;
            Provider<Context> provider10 = daggerTitanGlobalRootComponent2.contextProvider;
            UserCreator_Factory userCreator_Factory = new UserCreator_Factory(provider10, daggerTitanGlobalRootComponent2.provideUserManagerProvider, 0);
            this.userCreatorProvider = userCreator_Factory;
            this.createUserActivityProvider = new CreateUserActivity_Factory(userCreator_Factory, daggerTitanGlobalRootComponent2.provideIActivityManagerProvider);
            TvNotificationHandler_Factory tvNotificationHandler_Factory = new TvNotificationHandler_Factory(provider10, this.notificationListenerProvider);
            this.tvNotificationHandlerProvider = tvNotificationHandler_Factory;
            this.tvNotificationPanelActivityProvider = new FrameworkServicesModule_ProvideOptionalVibratorFactory(tvNotificationHandler_Factory, 2);
            this.peopleSpaceActivityProvider = new ImageTileSet_Factory(this.peopleSpaceWidgetManagerProvider, 4);
            this.imageExporterProvider = new ImageExporter_Factory(daggerTitanGlobalRootComponent2.provideContentResolverProvider, 0);
            Provider<LongScreenshotData> provider11 = DoubleCheck.provider(LongScreenshotData_Factory.InstanceHolder.INSTANCE);
            this.longScreenshotDataProvider = provider11;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent3 = DaggerTitanGlobalRootComponent.this;
            this.longScreenshotActivityProvider = GoogleAssistLogger_Factory.create(daggerTitanGlobalRootComponent3.provideUiEventLoggerProvider, this.imageExporterProvider, daggerTitanGlobalRootComponent3.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, provider11);
            this.launchConversationActivityProvider = OpaEnabledReceiver_Factory.create(this.provideNotificationVisibilityProvider, this.provideCommonNotifCollectionProvider, this.provideBubblesManagerProvider, DaggerTitanGlobalRootComponent.this.provideUserManagerProvider, this.provideCommandQueueProvider);
            Provider<IndividualSensorPrivacyController> provider12 = this.provideIndividualSensorPrivacyControllerProvider;
            this.sensorUseStartedActivityProvider = new SensorUseStartedActivity_Factory(provider12, this.keyguardStateControllerImplProvider, this.keyguardDismissUtilProvider, this.provideBgHandlerProvider);
            this.tvUnblockSensorActivityProvider = new ActivityStarterDelegate_Factory(provider12, 3);
            Provider<HdmiCecSetMenuLanguageHelper> provider13 = DoubleCheck.provider(new ControlsActivity_Factory(this.provideBackgroundExecutorProvider, this.secureSettingsImplProvider, 1));
            this.hdmiCecSetMenuLanguageHelperProvider = provider13;
            this.hdmiCecSetMenuLanguageActivityProvider = new ForegroundServicesDialog_Factory(provider13, 2);
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent4 = DaggerTitanGlobalRootComponent.this;
            this.gameMenuActivityProvider = new GameMenuActivity_Factory(daggerTitanGlobalRootComponent4.contextProvider, this.entryPointControllerProvider, this.provideActivityStarterProvider, this.shortcutBarControllerProvider, this.gameModeDndControllerProvider, this.providerLayoutInflaterProvider, daggerTitanGlobalRootComponent4.provideMainHandlerProvider, this.gameDashboardUiEventLoggerProvider);
            MapProviderFactory.Builder builder = new MapProviderFactory.Builder(22);
            builder.put(ControlsProviderSelectorActivity.class, this.controlsProviderSelectorActivityProvider);
            builder.put(ControlsFavoritingActivity.class, this.controlsFavoritingActivityProvider);
            builder.put(ControlsEditingActivity.class, this.controlsEditingActivityProvider);
            builder.put(ControlsRequestDialog.class, this.controlsRequestDialogProvider);
            builder.put(ControlsActivity.class, this.controlsActivityProvider);
            builder.put(UserSwitcherActivity.class, this.userSwitcherActivityProvider);
            builder.put(WalletActivity.class, this.walletActivityProvider);
            builder.put(TunerActivity.class, this.tunerActivityProvider);
            builder.put(ForegroundServicesDialog.class, this.foregroundServicesDialogProvider);
            builder.put(WorkLockActivity.class, this.workLockActivityProvider);
            builder.put(BrightnessDialog.class, this.brightnessDialogProvider);
            builder.put(UsbDebuggingActivity.class, this.usbDebuggingActivityProvider);
            builder.put(UsbDebuggingSecondaryUserActivity.class, this.usbDebuggingSecondaryUserActivityProvider);
            builder.put(CreateUserActivity.class, this.createUserActivityProvider);
            builder.put(TvNotificationPanelActivity.class, this.tvNotificationPanelActivityProvider);
            builder.put(PeopleSpaceActivity.class, this.peopleSpaceActivityProvider);
            builder.put(LongScreenshotActivity.class, this.longScreenshotActivityProvider);
            builder.put(LaunchConversationActivity.class, this.launchConversationActivityProvider);
            builder.put(SensorUseStartedActivity.class, this.sensorUseStartedActivityProvider);
            builder.put(TvUnblockSensorActivity.class, this.tvUnblockSensorActivityProvider);
            builder.put(HdmiCecSetMenuLanguageActivity.class, this.hdmiCecSetMenuLanguageActivityProvider);
            builder.put(GameMenuActivity.class, this.gameMenuActivityProvider);
            this.mapOfClassOfAndProviderOfActivityProvider = builder.build();
            this.screenshotSmartActionsProvider = DoubleCheck.provider(ScreenshotSmartActions_Factory.InstanceHolder.INSTANCE);
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent5 = DaggerTitanGlobalRootComponent.this;
            Provider<Context> provider14 = daggerTitanGlobalRootComponent5.contextProvider;
            this.screenshotNotificationsControllerProvider = new ScreenshotNotificationsController_Factory(provider14, daggerTitanGlobalRootComponent5.provideWindowManagerProvider);
            this.scrollCaptureClientProvider = new ScrollCaptureClient_Factory(daggerTitanGlobalRootComponent5.provideIWindowManagerProvider, this.provideBackgroundExecutorProvider, provider14, 0);
            ImageTileSet_Factory imageTileSet_Factory = new ImageTileSet_Factory(this.provideHandlerProvider, 0);
            this.imageTileSetProvider = imageTileSet_Factory;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent6 = DaggerTitanGlobalRootComponent.this;
            this.scrollCaptureControllerProvider = new ScrollCaptureController_Factory(daggerTitanGlobalRootComponent6.contextProvider, this.provideBackgroundExecutorProvider, this.scrollCaptureClientProvider, imageTileSet_Factory, daggerTitanGlobalRootComponent6.provideUiEventLoggerProvider);
            KeyboardUI_Factory keyboardUI_Factory = new KeyboardUI_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 5);
            this.timeoutHandlerProvider = keyboardUI_Factory;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent7 = DaggerTitanGlobalRootComponent.this;
            ScreenshotController_Factory create = ScreenshotController_Factory.create(daggerTitanGlobalRootComponent7.contextProvider, this.screenshotSmartActionsProvider, this.screenshotNotificationsControllerProvider, this.scrollCaptureClientProvider, daggerTitanGlobalRootComponent7.provideUiEventLoggerProvider, this.imageExporterProvider, daggerTitanGlobalRootComponent7.provideMainExecutorProvider, this.scrollCaptureControllerProvider, this.longScreenshotDataProvider, daggerTitanGlobalRootComponent7.provideActivityManagerProvider, keyboardUI_Factory);
            this.screenshotControllerProvider = create;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent8 = DaggerTitanGlobalRootComponent.this;
            this.takeScreenshotServiceProvider = new TakeScreenshotService_Factory(create, daggerTitanGlobalRootComponent8.provideUserManagerProvider, daggerTitanGlobalRootComponent8.provideUiEventLoggerProvider, this.screenshotNotificationsControllerProvider, 0);
            Provider<DozeComponent.Builder> provider15 = new Provider<DozeComponent.Builder>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.9
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final DozeComponent.Builder mo144get() {
                    return new DozeComponentFactory();
                }
            };
            this.dozeComponentBuilderProvider = provider15;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent9 = DaggerTitanGlobalRootComponent.this;
            this.dozeServiceProvider = new DozeService_Factory(provider15, daggerTitanGlobalRootComponent9.providesPluginManagerProvider);
            Provider<KeyguardLifecyclesDispatcher> provider16 = DoubleCheck.provider(new KeyguardLifecyclesDispatcher_Factory(daggerTitanGlobalRootComponent9.screenLifecycleProvider, this.wakefulnessLifecycleProvider, 0));
            this.keyguardLifecyclesDispatcherProvider = provider16;
            this.keyguardServiceProvider = new LatencyTester_Factory(this.newKeyguardViewMediatorProvider, provider16, this.setTransitionsProvider, 2);
            Provider<DreamOverlayComponent.Factory> provider17 = new Provider<DreamOverlayComponent.Factory>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.10
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final DreamOverlayComponent.Factory mo144get() {
                    return new DreamOverlayComponentFactory();
                }
            };
            this.dreamOverlayComponentFactoryProvider = provider17;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent10 = DaggerTitanGlobalRootComponent.this;
            this.dreamOverlayServiceProvider = new DreamOverlayService_Factory(daggerTitanGlobalRootComponent10.contextProvider, daggerTitanGlobalRootComponent10.provideMainExecutorProvider, provider17, this.dreamOverlayStateControllerProvider, this.keyguardUpdateMonitorProvider);
            this.notificationListenerWithPluginsProvider = new WMShellBaseModule_ProvideRecentTasksFactory(DaggerTitanGlobalRootComponent.this.providesPluginManagerProvider, 3);
            Provider<ClipboardOverlayControllerFactory> provider18 = DoubleCheck.provider(new QSFragmentModule_ProvideThemedContextFactory(dependencyProvider, 6));
            this.provideClipboardOverlayControllerFactoryProvider = provider18;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent11 = DaggerTitanGlobalRootComponent.this;
            this.clipboardListenerProvider = DoubleCheck.provider(new ClipboardListener_Factory(daggerTitanGlobalRootComponent11.contextProvider, this.deviceConfigProxyProvider, provider18, daggerTitanGlobalRootComponent11.provideClipboardManagerProvider, 0));
            this.providesBackgroundMessageRouterProvider = new QSFragmentModule_ProvideRootViewFactory(this.provideBackgroundDelayableExecutorProvider, 4);
            Provider<String> provider19 = DoubleCheck.provider(SystemUIGoogleModule_ProvideLeakReportEmailFactory.InstanceHolder.INSTANCE);
            this.provideLeakReportEmailProvider = provider19;
            Provider<LeakReporter> provider20 = DoubleCheck.provider(new LeakReporter_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideLeakDetectorProvider, provider19, 0));
            this.leakReporterProvider = provider20;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent12 = DaggerTitanGlobalRootComponent.this;
            Provider<GarbageMonitor> provider21 = DoubleCheck.provider(GarbageMonitor_Factory.create$1(daggerTitanGlobalRootComponent12.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.providesBackgroundMessageRouterProvider, this.provideLeakDetectorProvider, provider20, daggerTitanGlobalRootComponent12.dumpManagerProvider));
            this.garbageMonitorProvider = provider21;
            this.serviceProvider = DoubleCheck.provider(new GarbageMonitor_Service_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, provider21));
            DelegateFactory delegateFactory = new DelegateFactory();
            this.globalActionsComponentProvider = delegateFactory;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent13 = DaggerTitanGlobalRootComponent.this;
            Provider<Context> provider22 = daggerTitanGlobalRootComponent13.contextProvider;
            Provider<AudioManager> provider23 = daggerTitanGlobalRootComponent13.provideAudioManagerProvider;
            Provider<IDreamManager> provider24 = daggerTitanGlobalRootComponent13.provideIDreamManagerProvider;
            Provider<DevicePolicyManager> provider25 = daggerTitanGlobalRootComponent13.provideDevicePolicyManagerProvider;
            Provider<LockPatternUtils> provider26 = this.provideLockPatternUtilsProvider;
            Provider<BroadcastDispatcher> provider27 = this.providesBroadcastDispatcherProvider;
            Provider<TelephonyListenerManager> provider28 = this.telephonyListenerManagerProvider;
            Provider provider29 = this.globalSettingsImplProvider;
            Provider provider30 = this.secureSettingsImplProvider;
            Provider<VibratorHelper> provider31 = this.vibratorHelperProvider;
            Provider<Resources> provider32 = daggerTitanGlobalRootComponent13.provideResourcesProvider;
            Provider<ConfigurationController> provider33 = this.provideConfigurationControllerProvider;
            Provider<KeyguardStateControllerImpl> provider34 = this.keyguardStateControllerImplProvider;
            Provider<UserManager> provider35 = daggerTitanGlobalRootComponent13.provideUserManagerProvider;
            Provider<TrustManager> provider36 = daggerTitanGlobalRootComponent13.provideTrustManagerProvider;
            Provider<IActivityManager> provider37 = daggerTitanGlobalRootComponent13.provideIActivityManagerProvider;
            Provider<TelecomManager> provider38 = daggerTitanGlobalRootComponent13.provideTelecomManagerProvider;
            Provider<MetricsLogger> provider39 = this.provideMetricsLoggerProvider;
            Provider<SysuiColorExtractor> provider40 = this.sysuiColorExtractorProvider;
            Provider<IStatusBarService> provider41 = daggerTitanGlobalRootComponent13.provideIStatusBarServiceProvider;
            Provider<NotificationShadeWindowControllerImpl> provider42 = this.notificationShadeWindowControllerImplProvider;
            Provider<IWindowManager> provider43 = daggerTitanGlobalRootComponent13.provideIWindowManagerProvider;
            Provider<Executor> provider44 = this.provideBackgroundExecutorProvider;
            Provider<UiEventLogger> provider45 = daggerTitanGlobalRootComponent13.provideUiEventLoggerProvider;
            Provider<RingerModeTrackerImpl> provider46 = this.ringerModeTrackerImplProvider;
            Provider<SysUiState> provider47 = this.provideSysUiStateProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent14 = DaggerTitanGlobalRootComponent.this;
            GlobalActionsDialogLite_Factory create2 = GlobalActionsDialogLite_Factory.create(provider22, delegateFactory, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32, provider33, provider34, provider35, provider36, provider37, provider38, provider39, provider40, provider41, provider42, provider43, provider44, provider45, provider46, provider47, daggerTitanGlobalRootComponent14.provideMainHandlerProvider, daggerTitanGlobalRootComponent14.providePackageManagerProvider, this.optionalOfStatusBarProvider, this.keyguardUpdateMonitorProvider, this.provideDialogLaunchAnimatorProvider, this.systemUIDialogManagerProvider);
            this.globalActionsDialogLiteProvider = create2;
            GlobalActionsImpl_Factory create3 = GlobalActionsImpl_Factory.create(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, create2, this.blurUtilsProvider, this.keyguardStateControllerImplProvider, this.provideDeviceProvisionedControllerProvider);
            this.globalActionsImplProvider = create3;
            DelegateFactory.setDelegate(this.globalActionsComponentProvider, DoubleCheck.provider(new GlobalActionsComponent_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.extensionControllerImplProvider, create3, this.statusBarKeyguardViewManagerProvider)));
            this.instantAppNotifierProvider = DoubleCheck.provider(new ClipboardListener_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.provideUiBackgroundExecutorProvider, this.setLegacySplitScreenProvider, 1));
            this.keyboardUIProvider = DoubleCheck.provider(new KeyboardUI_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 0));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent15 = DaggerTitanGlobalRootComponent.this;
            this.keyguardBiometricLockoutLoggerProvider = DoubleCheck.provider(new KeyguardBiometricLockoutLogger_Factory(daggerTitanGlobalRootComponent15.contextProvider, daggerTitanGlobalRootComponent15.provideUiEventLoggerProvider, this.keyguardUpdateMonitorProvider, this.sessionTrackerProvider, 0));
            this.latencyTesterProvider = DoubleCheck.provider(new LatencyTester_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.biometricUnlockControllerProvider, this.providesBroadcastDispatcherProvider, 0));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent16 = DaggerTitanGlobalRootComponent.this;
            Provider<Context> provider48 = daggerTitanGlobalRootComponent16.contextProvider;
            Provider<ActivityStarter> provider49 = this.provideActivityStarterProvider;
            Provider<BroadcastDispatcher> provider50 = this.providesBroadcastDispatcherProvider;
            PowerModuleGoogle_ProvideWarningsUiFactory powerModuleGoogle_ProvideWarningsUiFactory = new PowerModuleGoogle_ProvideWarningsUiFactory(provider48, provider49, provider50, daggerTitanGlobalRootComponent16.provideUiEventLoggerProvider);
            this.provideWarningsUiProvider = powerModuleGoogle_ProvideWarningsUiFactory;
            this.powerUIProvider = DoubleCheck.provider(PowerUI_Factory.create(provider48, provider50, this.provideCommandQueueProvider, this.optionalOfStatusBarProvider, powerModuleGoogle_ProvideWarningsUiFactory, this.enhancedEstimatesGoogleImplProvider, daggerTitanGlobalRootComponent16.providePowerManagerProvider));
            this.ringtonePlayerProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline2.m(DaggerTitanGlobalRootComponent.this.contextProvider, 0);
            this.systemEventCoordinatorProvider = DoubleCheck.provider(new SystemEventCoordinator_Factory(this.bindSystemClockProvider, this.provideBatteryControllerProvider, this.privacyItemControllerProvider, DaggerTitanGlobalRootComponent.this.contextProvider));
            Provider<StatusBarLocationPublisher> provider51 = DoubleCheck.provider(StatusBarLocationPublisher_Factory.InstanceHolder.INSTANCE);
            this.statusBarLocationPublisherProvider = provider51;
            SystemEventChipAnimationController_Factory systemEventChipAnimationController_Factory = new SystemEventChipAnimationController_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.statusBarWindowControllerProvider, provider51, 0);
            this.systemEventChipAnimationControllerProvider = systemEventChipAnimationController_Factory;
            Provider<SystemEventCoordinator> provider52 = this.systemEventCoordinatorProvider;
            Provider<StatusBarWindowController> provider53 = this.statusBarWindowControllerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent17 = DaggerTitanGlobalRootComponent.this;
            Provider<SystemStatusAnimationScheduler> provider54 = DoubleCheck.provider(SystemStatusAnimationScheduler_Factory.create(provider52, systemEventChipAnimationController_Factory, provider53, daggerTitanGlobalRootComponent17.dumpManagerProvider, this.bindSystemClockProvider, daggerTitanGlobalRootComponent17.provideMainDelayableExecutorProvider));
            this.systemStatusAnimationSchedulerProvider = provider54;
            this.privacyDotViewControllerProvider = DoubleCheck.provider(PrivacyDotViewController_Factory.create(DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider, this.statusBarStateControllerImplProvider, this.provideConfigurationControllerProvider, this.statusBarContentInsetsProvider, provider54));
            Provider<PrivacyDotDecorProviderFactory> provider55 = DoubleCheck.provider(new PackageManagerAdapter_Factory(DaggerTitanGlobalRootComponent.this.provideResourcesProvider, 2));
            this.privacyDotDecorProviderFactoryProvider = provider55;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent18 = DaggerTitanGlobalRootComponent.this;
            this.screenDecorationsProvider = DoubleCheck.provider(ScreenDecorations_Factory.create(daggerTitanGlobalRootComponent18.contextProvider, daggerTitanGlobalRootComponent18.provideMainExecutorProvider, this.secureSettingsImplProvider, this.providesBroadcastDispatcherProvider, this.tunerServiceImplProvider, this.provideUserTrackerProvider, this.privacyDotViewControllerProvider, provider55));
            this.shortcutKeyDispatcherProvider = DoubleCheck.provider(new ShortcutKeyDispatcher_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.setLegacySplitScreenProvider, 0));
            this.sliceBroadcastRelayHandlerProvider = DoubleCheck.provider(new SliceBroadcastRelayHandler_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.providesBroadcastDispatcherProvider, 0));
            this.storageNotificationProvider = DoubleCheck.provider(new QSLogger_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, 4));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent19 = DaggerTitanGlobalRootComponent.this;
            Provider<ThemeOverlayApplier> provider56 = DoubleCheck.provider(new ToastController_Factory(daggerTitanGlobalRootComponent19.contextProvider, this.provideBackgroundExecutorProvider, daggerTitanGlobalRootComponent19.provideMainExecutorProvider, daggerTitanGlobalRootComponent19.provideOverlayManagerProvider, daggerTitanGlobalRootComponent19.dumpManagerProvider, 1));
            this.provideThemeOverlayManagerProvider = provider56;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent20 = DaggerTitanGlobalRootComponent.this;
            this.themeOverlayControllerProvider = DoubleCheck.provider(ThemeOverlayController_Factory.create(daggerTitanGlobalRootComponent20.contextProvider, this.providesBroadcastDispatcherProvider, this.provideBgHandlerProvider, daggerTitanGlobalRootComponent20.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, provider56, this.secureSettingsImplProvider, daggerTitanGlobalRootComponent20.provideWallpaperManagerProvider, daggerTitanGlobalRootComponent20.provideUserManagerProvider, this.provideDeviceProvisionedControllerProvider, this.provideUserTrackerProvider, daggerTitanGlobalRootComponent20.dumpManagerProvider, this.featureFlagsReleaseProvider, this.wakefulnessLifecycleProvider));
            Provider<LogBuffer> provider57 = DoubleCheck.provider(new ActivityIntentHelper_Factory(this.logBufferFactoryProvider, 2));
            this.provideToastLogBufferProvider = provider57;
            ToastLogger_Factory toastLogger_Factory = new ToastLogger_Factory(provider57, 0);
            this.toastLoggerProvider = toastLogger_Factory;
            this.toastUIProvider = DoubleCheck.provider(new ToastUI_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideCommandQueueProvider, this.toastFactoryProvider, toastLogger_Factory));
            this.volumeUIProvider = DoubleCheck.provider(new VolumeUI_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.volumeDialogComponentProvider, 0));
            Provider<ModeSwitchesController> provider58 = DoubleCheck.provider(new DependencyProvider_ProvidesModeSwitchesControllerFactory(dependencyProvider, DaggerTitanGlobalRootComponent.this.contextProvider));
            this.providesModeSwitchesControllerProvider = provider58;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent21 = DaggerTitanGlobalRootComponent.this;
            this.windowMagnificationProvider = DoubleCheck.provider(WindowMagnification_Factory.create(daggerTitanGlobalRootComponent21.contextProvider, daggerTitanGlobalRootComponent21.provideMainHandlerProvider, this.provideCommandQueueProvider, provider58, this.provideSysUiStateProvider, this.overviewProxyServiceProvider));
            this.setHideDisplayCutoutProvider = InstanceFactory.create(optional8);
            this.setShellCommandHandlerProvider = InstanceFactory.create(optional9);
            this.setCompatUIProvider = InstanceFactory.create(optional14);
            this.setDragAndDropProvider = InstanceFactory.create(optional15);
            Provider<Context> provider59 = DaggerTitanGlobalRootComponent.this.contextProvider;
            Provider<Optional<Pip>> provider60 = this.setPipProvider;
            Provider<Optional<LegacySplitScreen>> provider61 = this.setLegacySplitScreenProvider;
            Provider<Optional<SplitScreen>> provider62 = this.setSplitScreenProvider;
            Provider<Optional<OneHanded>> provider63 = this.setOneHandedProvider;
            Provider<Optional<HideDisplayCutout>> provider64 = this.setHideDisplayCutoutProvider;
            Provider<Optional<ShellCommandHandler>> provider65 = this.setShellCommandHandlerProvider;
            Provider<Optional<CompatUI>> provider66 = this.setCompatUIProvider;
            Provider<Optional<DragAndDrop>> provider67 = this.setDragAndDropProvider;
            Provider<CommandQueue> provider68 = this.provideCommandQueueProvider;
            Provider<ConfigurationController> provider69 = this.provideConfigurationControllerProvider;
            Provider<KeyguardUpdateMonitor> provider70 = this.keyguardUpdateMonitorProvider;
            Provider<NavigationModeController> provider71 = this.navigationModeControllerProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent22 = DaggerTitanGlobalRootComponent.this;
            this.wMShellProvider = DoubleCheck.provider(WMShell_Factory.create(provider59, provider60, provider61, provider62, provider63, provider64, provider65, provider66, provider67, provider68, provider69, provider70, provider71, daggerTitanGlobalRootComponent22.screenLifecycleProvider, this.provideSysUiStateProvider, this.protoTracerProvider, this.wakefulnessLifecycleProvider, this.userInfoControllerImplProvider, daggerTitanGlobalRootComponent22.provideMainExecutorProvider));
            this.opaHomeButtonProvider = new OpaHomeButton_Factory(this.newKeyguardViewMediatorProvider, this.statusBarGoogleProvider, this.navigationModeControllerProvider, 0);
            this.opaLockscreenProvider = new OpaLockscreen_Factory(this.statusBarGoogleProvider, this.keyguardStateControllerImplProvider);
            this.assistInvocationEffectProvider = new TelephonyListenerManager_Factory(this.assistManagerGoogleProvider, this.opaHomeButtonProvider, this.opaLockscreenProvider, 2);
            this.builderProvider6 = new LaunchOpa_Builder_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.statusBarGoogleProvider);
            this.builderProvider7 = new SettingsAction_Builder_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.statusBarGoogleProvider);
            this.builderProvider8 = new CameraAction_Builder_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.statusBarGoogleProvider);
            this.builderProvider9 = new SetupWizardAction_Builder_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.statusBarGoogleProvider);
            this.squishyNavigationButtonsProvider = new GestureController_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.newKeyguardViewMediatorProvider, this.statusBarGoogleProvider, this.navigationModeControllerProvider, 1);
            this.optionalOfHeadsUpManagerProvider = new PresentJdkOptionalInstanceProvider(this.provideHeadsUpManagerPhoneProvider);
            this.unpinNotificationsProvider = new UnpinNotifications_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.optionalOfHeadsUpManagerProvider, 0);
            this.silenceCallProvider = new FeatureFlagsRelease_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.telephonyListenerManagerProvider, 5);
            this.telephonyActivityProvider = new OverlayUiHost_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.telephonyListenerManagerProvider, 1);
            this.serviceConfigurationGoogleProvider = new ServiceConfigurationGoogle_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.assistInvocationEffectProvider, this.builderProvider6, this.builderProvider7, this.builderProvider8, this.builderProvider9, this.squishyNavigationButtonsProvider, this.unpinNotificationsProvider, this.silenceCallProvider, this.telephonyActivityProvider);
            Provider<ContentResolverWrapper> m = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline2.m(DaggerTitanGlobalRootComponent.this.contextProvider, 6);
            this.contentResolverWrapperProvider = m;
            this.factoryProvider7 = DoubleCheck.provider(new ColumbusContentObserver_Factory_Factory(m, this.provideUserTrackerProvider, DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider, DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider));
            this.columbusSettingsProvider = DoubleCheck.provider(new ColumbusSettings_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideUserTrackerProvider, this.factoryProvider7));
            this.silenceAlertsDisabledProvider = DoubleCheck.provider(new DistanceClassifier_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.columbusSettingsProvider, 2));
        }

        /* JADX WARN: Type inference failed for: r6v52, types: [javax.inject.Provider<com.google.android.systemui.communal.dreams.dagger.SetupDreamComponent$Factory>, com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl$13] */
        /* JADX WARN: Unknown variable types count: 1 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void initialize7(com.android.systemui.dagger.DependencyProvider r17, com.android.systemui.dagger.NightDisplayListenerModule r18, com.android.systemui.unfold.SysUIUnfoldModule r19, java.util.Optional<com.android.wm.shell.pip.Pip> r20, java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreen> r21, java.util.Optional<com.android.wm.shell.splitscreen.SplitScreen> r22, java.util.Optional<java.lang.Object> r23, java.util.Optional<com.android.wm.shell.onehanded.OneHanded> r24, java.util.Optional<com.android.wm.shell.bubbles.Bubbles> r25, java.util.Optional<com.android.wm.shell.TaskViewFactory> r26, java.util.Optional<com.android.wm.shell.hidedisplaycutout.HideDisplayCutout> r27, java.util.Optional<com.android.wm.shell.ShellCommandHandler> r28, com.android.wm.shell.transition.ShellTransitions r29, java.util.Optional<com.android.wm.shell.startingsurface.StartingSurface> r30, java.util.Optional<com.android.wm.shell.displayareahelper.DisplayAreaHelper> r31, java.util.Optional<com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper> r32, java.util.Optional<com.android.wm.shell.recents.RecentTasks> r33, java.util.Optional<com.android.wm.shell.compatui.CompatUI> r34, java.util.Optional<com.android.wm.shell.draganddrop.DragAndDrop> r35, java.util.Optional<com.android.wm.shell.back.BackAnimation> r36) {
            /*
                Method dump skipped, instructions count: 1927
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.initialize7(com.android.systemui.dagger.DependencyProvider, com.android.systemui.dagger.NightDisplayListenerModule, com.android.systemui.unfold.SysUIUnfoldModule, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, com.android.wm.shell.transition.ShellTransitions, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional):void");
        }

        /* JADX WARN: Type inference failed for: r5v1, types: [javax.inject.Provider<com.android.systemui.media.dream.dagger.MediaComplicationComponent$Factory>, com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl$18] */
        /* JADX WARN: Unknown variable types count: 1 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void initialize8(com.android.systemui.dagger.DependencyProvider r135, com.android.systemui.dagger.NightDisplayListenerModule r136, com.android.systemui.unfold.SysUIUnfoldModule r137, java.util.Optional<com.android.wm.shell.pip.Pip> r138, java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreen> r139, java.util.Optional<com.android.wm.shell.splitscreen.SplitScreen> r140, java.util.Optional<java.lang.Object> r141, java.util.Optional<com.android.wm.shell.onehanded.OneHanded> r142, java.util.Optional<com.android.wm.shell.bubbles.Bubbles> r143, java.util.Optional<com.android.wm.shell.TaskViewFactory> r144, java.util.Optional<com.android.wm.shell.hidedisplaycutout.HideDisplayCutout> r145, java.util.Optional<com.android.wm.shell.ShellCommandHandler> r146, com.android.wm.shell.transition.ShellTransitions r147, java.util.Optional<com.android.wm.shell.startingsurface.StartingSurface> r148, java.util.Optional<com.android.wm.shell.displayareahelper.DisplayAreaHelper> r149, java.util.Optional<com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper> r150, java.util.Optional<com.android.wm.shell.recents.RecentTasks> r151, java.util.Optional<com.android.wm.shell.compatui.CompatUI> r152, java.util.Optional<com.android.wm.shell.draganddrop.DragAndDrop> r153, java.util.Optional<com.android.wm.shell.back.BackAnimation> r154) {
            /*
                Method dump skipped, instructions count: 3352
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.initialize8(com.android.systemui.dagger.DependencyProvider, com.android.systemui.dagger.NightDisplayListenerModule, com.android.systemui.unfold.SysUIUnfoldModule, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, com.android.wm.shell.transition.ShellTransitions, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional, java.util.Optional):void");
        }

        public final void initialize9(DependencyProvider dependencyProvider, NightDisplayListenerModule nightDisplayListenerModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<Object> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.microphoneToggleTileProvider = MicrophoneToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider, this.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider);
            this.providesControlsFeatureEnabledProvider = DoubleCheck.provider(new TypeClassifier_Factory(DaggerTitanGlobalRootComponent.this.providePackageManagerProvider, 2));
            Provider<GoogleControlsTileResourceConfigurationImpl> m = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline3.m(this.controlsControllerImplProvider, 8);
            this.googleControlsTileResourceConfigurationImplProvider = m;
            PresentJdkOptionalInstanceProvider presentJdkOptionalInstanceProvider = new PresentJdkOptionalInstanceProvider(m);
            this.optionalOfControlsTileResourceConfigurationProvider = presentJdkOptionalInstanceProvider;
            Provider<ControlsComponent> provider = DoubleCheck.provider(ControlsComponent_Factory.create(this.providesControlsFeatureEnabledProvider, DaggerTitanGlobalRootComponent.this.contextProvider, this.controlsControllerImplProvider, this.controlsUiControllerImplProvider, this.controlsListingControllerImplProvider, this.provideLockPatternUtilsProvider, this.keyguardStateControllerImplProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider, presentJdkOptionalInstanceProvider));
            this.controlsComponentProvider = provider;
            this.deviceControlsTileProvider = DeviceControlsTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, provider, this.keyguardStateControllerImplProvider);
            this.alarmTileProvider = AlarmTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.nextAlarmControllerImplProvider);
            Provider<QSTileHost> provider2 = this.qSTileHostProvider;
            Provider<Looper> provider3 = this.provideBgLooperProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
            this.overlayToggleTileProvider = new OverlayToggleTile_Factory(provider2, provider3, daggerTitanGlobalRootComponent.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, daggerTitanGlobalRootComponent.provideOverlayManagerProvider, 0);
            Provider<QuickAccessWalletClient> provider4 = DoubleCheck.provider(new DozeLogger_Factory(daggerTitanGlobalRootComponent.contextProvider, 6));
            this.provideQuickAccessWalletClientProvider = provider4;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent2 = DaggerTitanGlobalRootComponent.this;
            Provider<QuickAccessWalletController> provider5 = DoubleCheck.provider(QuickAccessWalletController_Factory.create(daggerTitanGlobalRootComponent2.contextProvider, daggerTitanGlobalRootComponent2.provideMainExecutorProvider, this.provideExecutorProvider, this.secureSettingsImplProvider, provider4, this.bindSystemClockProvider));
            this.quickAccessWalletControllerProvider = provider5;
            Provider<QSTileHost> provider6 = this.qSTileHostProvider;
            Provider<Looper> provider7 = this.provideBgLooperProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent3 = DaggerTitanGlobalRootComponent.this;
            this.quickAccessWalletTileProvider = QuickAccessWalletTile_Factory.create(provider6, provider7, daggerTitanGlobalRootComponent3.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.keyguardStateControllerImplProvider, daggerTitanGlobalRootComponent3.providePackageManagerProvider, this.secureSettingsImplProvider, provider5);
            Provider<QRCodeScannerController> provider8 = DoubleCheck.provider(DefaultUiController_Factory.create$1(DaggerTitanGlobalRootComponent.this.contextProvider, this.provideBackgroundExecutorProvider, this.secureSettingsImplProvider, this.deviceConfigProxyProvider, this.provideUserTrackerProvider));
            this.qRCodeScannerControllerProvider = provider8;
            this.qRCodeScannerTileProvider = QRCodeScannerTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, provider8);
            this.oneHandedModeTileProvider = OneHandedModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider);
            ColorCorrectionTile_Factory create = ColorCorrectionTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, DaggerTitanGlobalRootComponent.this.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider);
            this.colorCorrectionTileProvider = create;
            this.qSFactoryImplGoogleProvider = DoubleCheck.provider(QSFactoryImplGoogle_Factory.create(this.qSTileHostProvider, this.builderProvider10, this.wifiTileProvider, this.internetTileProvider, this.bluetoothTileProvider, this.cellularTileProvider, this.dndTileProvider, this.colorInversionTileProvider, this.airplaneModeTileProvider, this.workModeTileProvider, this.rotationLockTileGoogleProvider, this.flashlightTileProvider, this.locationTileProvider, this.castTileProvider, this.hotspotTileProvider, this.batterySaverTileGoogleProvider, this.dataSaverTileProvider, this.nightDisplayTileProvider, this.nfcTileProvider, this.memoryTileProvider, this.uiModeNightTileProvider, this.screenRecordTileProvider, this.reverseChargingTileProvider, this.reduceBrightColorsTileProvider, this.cameraToggleTileProvider, this.microphoneToggleTileProvider, this.deviceControlsTileProvider, this.alarmTileProvider, this.overlayToggleTileProvider, this.quickAccessWalletTileProvider, this.qRCodeScannerTileProvider, this.oneHandedModeTileProvider, create));
            Provider provider9 = this.secureSettingsImplProvider;
            Provider<BroadcastDispatcher> provider10 = this.providesBroadcastDispatcherProvider;
            Provider<QSTileHost> provider11 = this.qSTileHostProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent4 = DaggerTitanGlobalRootComponent.this;
            this.builderProvider12 = DoubleCheck.provider(AutoAddTracker_Builder_Factory.create(provider9, provider10, provider11, daggerTitanGlobalRootComponent4.dumpManagerProvider, daggerTitanGlobalRootComponent4.provideMainHandlerProvider, this.provideBackgroundExecutorProvider));
            this.deviceControlsControllerImplProvider = DoubleCheck.provider(new ClipboardListener_Factory(DaggerTitanGlobalRootComponent.this.contextProvider, this.controlsComponentProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider, 2));
            Provider<WalletControllerImpl> provider12 = DoubleCheck.provider(new ActivityStarterDelegate_Factory(this.provideQuickAccessWalletClientProvider, 5));
            this.walletControllerImplProvider = provider12;
            this.provideAutoTileManagerProvider = QSModuleGoogle_ProvideAutoTileManagerFactory.create(DaggerTitanGlobalRootComponent.this.contextProvider, this.builderProvider12, this.qSTileHostProvider, this.provideBgHandlerProvider, this.secureSettingsImplProvider, this.hotspotControllerImplProvider, this.provideDataSaverControllerProvider, this.managedProfileControllerImplProvider, this.provideNightDisplayListenerProvider, this.castControllerImplProvider, this.provideBatteryControllerProvider, this.provideReduceBrightColorsListenerProvider, this.deviceControlsControllerImplProvider, provider12, this.isReduceBrightColorsAvailableProvider);
            this.builderProvider13 = DoubleCheck.provider(new TileServiceRequestController_Builder_Factory(this.provideCommandQueueProvider, this.commandRegistryProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent5 = DaggerTitanGlobalRootComponent.this;
            Provider<Context> provider13 = daggerTitanGlobalRootComponent5.contextProvider;
            PackageManagerAdapter_Factory packageManagerAdapter_Factory = new PackageManagerAdapter_Factory(provider13, 0);
            this.packageManagerAdapterProvider = packageManagerAdapter_Factory;
            C0013TileLifecycleManager_Factory tileLifecycleManager_Factory = new C0013TileLifecycleManager_Factory(daggerTitanGlobalRootComponent5.provideMainHandlerProvider, provider13, this.tileServicesProvider, packageManagerAdapter_Factory, this.providesBroadcastDispatcherProvider);
            this.tileLifecycleManagerProvider = tileLifecycleManager_Factory;
            InstanceFactory create2 = InstanceFactory.create(new TileLifecycleManager_Factory_Impl(tileLifecycleManager_Factory));
            this.factoryProvider9 = create2;
            Provider<QSTileHost> provider14 = this.qSTileHostProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent6 = DaggerTitanGlobalRootComponent.this;
            DelegateFactory.setDelegate(provider14, DoubleCheck.provider(QSTileHost_Factory.create(daggerTitanGlobalRootComponent6.contextProvider, this.statusBarIconControllerImplProvider, this.qSFactoryImplGoogleProvider, daggerTitanGlobalRootComponent6.provideMainHandlerProvider, this.provideBgLooperProvider, daggerTitanGlobalRootComponent6.providesPluginManagerProvider, this.tunerServiceImplProvider, this.provideAutoTileManagerProvider, daggerTitanGlobalRootComponent6.dumpManagerProvider, this.providesBroadcastDispatcherProvider, this.optionalOfStatusBarProvider, this.qSLoggerProvider, daggerTitanGlobalRootComponent6.provideUiEventLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider, this.customTileStatePersisterProvider, this.builderProvider13, create2)));
            this.providesQSMediaHostProvider = DoubleCheck.provider(new MediaModule_ProvidesQSMediaHostFactory(this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesQuickQSMediaHostProvider = DoubleCheck.provider(new MediaModule_ProvidesQuickQSMediaHostFactory(this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.provideQSFragmentDisableLogBufferProvider = DoubleCheck.provider(new VrMode_Factory(this.logBufferFactoryProvider, 3));
            this.disableFlagsLoggerProvider = DoubleCheck.provider(DisableFlagsLogger_Factory.InstanceHolder.INSTANCE);
            this.notificationShelfComponentBuilderProvider = new Provider<NotificationShelfComponent.Builder>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.19
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final NotificationShelfComponent.Builder mo144get() {
                    return new NotificationShelfComponentBuilder();
                }
            };
            KeyguardLifecyclesDispatcher_Factory keyguardLifecyclesDispatcher_Factory = new KeyguardLifecyclesDispatcher_Factory(this.provideHandlerProvider, this.secureSettingsImplProvider, 1);
            this.communalSettingConditionProvider = keyguardLifecyclesDispatcher_Factory;
            this.provideCommunalConditionsProvider = new QSFragmentModule_ProvidesQuickQSPanelFactory(keyguardLifecyclesDispatcher_Factory, 2);
            int i = SetFactory.$r8$clinit;
            List emptyList = Collections.emptyList();
            ArrayList arrayList = new ArrayList(1);
            SetFactory m2 = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline0.m(arrayList, this.provideCommunalConditionsProvider, emptyList, arrayList);
            this.namedSetOfConditionProvider = m2;
            CommunalModule_ProvideCommunalSourceMonitorFactory communalModule_ProvideCommunalSourceMonitorFactory = new CommunalModule_ProvideCommunalSourceMonitorFactory(m2, this.monitorComponentFactoryProvider);
            this.provideCommunalSourceMonitorProvider = communalModule_ProvideCommunalSourceMonitorFactory;
            this.communalSourceMonitorProvider = DoubleCheck.provider(new CommunalSourceMonitor_Factory(DaggerTitanGlobalRootComponent.this.provideMainExecutorProvider, communalModule_ProvideCommunalSourceMonitorFactory, 0));
            this.keyguardQsUserSwitchComponentFactoryProvider = new Provider<KeyguardQsUserSwitchComponent.Factory>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.20
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final KeyguardQsUserSwitchComponent.Factory mo144get() {
                    return new KeyguardQsUserSwitchComponentFactory();
                }
            };
            this.keyguardUserSwitcherComponentFactoryProvider = new Provider<KeyguardUserSwitcherComponent.Factory>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.21
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final KeyguardUserSwitcherComponent.Factory mo144get() {
                    return new KeyguardUserSwitcherComponentFactory();
                }
            };
            this.keyguardStatusBarViewComponentFactoryProvider = new Provider<KeyguardStatusBarViewComponent.Factory>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.22
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final KeyguardStatusBarViewComponent.Factory mo144get() {
                    return new KeyguardStatusBarViewComponentFactory();
                }
            };
            this.communalViewComponentFactoryProvider = new Provider<CommunalViewComponent.Factory>() { // from class: com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.23
                @Override // javax.inject.Provider
                /* renamed from: get */
                public final CommunalViewComponent.Factory mo144get() {
                    return new CommunalViewComponentFactory();
                }
            };
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent7 = DaggerTitanGlobalRootComponent.this;
            this.privacyDialogControllerProvider = DoubleCheck.provider(PrivacyDialogController_Factory.create(daggerTitanGlobalRootComponent7.providePermissionManagerProvider, daggerTitanGlobalRootComponent7.providePackageManagerProvider, this.privacyItemControllerProvider, this.provideUserTrackerProvider, this.provideActivityStarterProvider, this.provideBackgroundExecutorProvider, daggerTitanGlobalRootComponent7.provideMainExecutorProvider, this.privacyLoggerProvider, this.keyguardStateControllerImplProvider, this.appOpsControllerImplProvider, daggerTitanGlobalRootComponent7.provideUiEventLoggerProvider));
            this.subscriptionManagerSlotIndexResolverProvider = DoubleCheck.provider(QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory.InstanceHolder.INSTANCE);
            this.qsFrameTranslateImplProvider = DoubleCheck.provider(new VrMode_Factory(this.statusBarGoogleProvider, 5));
            Provider<LowLightClockControllerImpl> provider15 = DoubleCheck.provider(new ManagedProfileControllerImpl_Factory(DaggerTitanGlobalRootComponent.this.provideResourcesProvider, this.providerLayoutInflaterProvider, 3));
            this.lowLightClockControllerImplProvider = provider15;
            PresentJdkOptionalInstanceProvider presentJdkOptionalInstanceProvider2 = new PresentJdkOptionalInstanceProvider(provider15);
            this.dynamicOverrideOptionalOfLowLightClockControllerProvider = presentJdkOptionalInstanceProvider2;
            this.provideLowLightClockControllerProvider = DoubleCheck.provider(new SystemUIModule_ProvideLowLightClockControllerFactory(presentJdkOptionalInstanceProvider2, 0));
            this.provideCollapsedSbFragmentLogBufferProvider = DoubleCheck.provider(new PowerSaveState_Factory(this.logBufferFactoryProvider, 2));
            Provider<UserInfoControllerImpl> provider16 = this.userInfoControllerImplProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent8 = DaggerTitanGlobalRootComponent.this;
            this.statusBarUserInfoTrackerProvider = DoubleCheck.provider(new StatusBarUserInfoTracker_Factory(provider16, daggerTitanGlobalRootComponent8.provideUserManagerProvider, daggerTitanGlobalRootComponent8.dumpManagerProvider, daggerTitanGlobalRootComponent8.provideMainExecutorProvider, this.provideBackgroundExecutorProvider));
            this.statusBarUserSwitcherFeatureControllerProvider = DoubleCheck.provider(new ImageExporter_Factory(this.featureFlagsReleaseProvider, 4));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent9 = DaggerTitanGlobalRootComponent.this;
            Provider<Context> provider17 = daggerTitanGlobalRootComponent9.contextProvider;
            Provider<UserSwitcherController> provider18 = this.userSwitcherControllerProvider;
            Provider<UiEventLogger> provider19 = daggerTitanGlobalRootComponent9.provideUiEventLoggerProvider;
            Provider<FalsingManagerProxy> provider20 = this.falsingManagerProxyProvider;
            UserDetailView_Adapter_Factory userDetailView_Adapter_Factory = new UserDetailView_Adapter_Factory(provider17, provider18, provider19, provider20);
            this.adapterProvider = userDetailView_Adapter_Factory;
            this.userSwitchDialogControllerProvider = DoubleCheck.provider(new UserSwitchDialogController_Factory(userDetailView_Adapter_Factory, this.provideActivityStarterProvider, provider20, this.provideDialogLaunchAnimatorProvider, provider19));
            Provider<ProximitySensor> provider21 = this.provideProximitySensorProvider;
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent10 = DaggerTitanGlobalRootComponent.this;
            this.provideProximityCheckProvider = new ForegroundServiceController_Factory(provider21, daggerTitanGlobalRootComponent10.provideMainDelayableExecutorProvider, 2);
            this.builderProvider14 = new FlingAnimationUtils_Builder_Factory(daggerTitanGlobalRootComponent10.provideDisplayMetricsProvider);
            this.providesDreamMediaHostProvider = DoubleCheck.provider(new MediaModule_ProvidesDreamMediaHostFactory(this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent11 = DaggerTitanGlobalRootComponent.this;
            this.fgsManagerControllerProvider = DoubleCheck.provider(FgsManagerController_Factory.create(daggerTitanGlobalRootComponent11.contextProvider, daggerTitanGlobalRootComponent11.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.bindSystemClockProvider, daggerTitanGlobalRootComponent11.provideIActivityManagerProvider, daggerTitanGlobalRootComponent11.providePackageManagerProvider, this.deviceConfigProxyProvider, this.provideDialogLaunchAnimatorProvider, daggerTitanGlobalRootComponent11.dumpManagerProvider));
            this.isPMLiteEnabledProvider = DoubleCheck.provider(new QSFlagsModule_IsPMLiteEnabledFactory(this.featureFlagsReleaseProvider, this.globalSettingsImplProvider, 0));
            this.factoryProvider10 = DoubleCheck.provider(new StatusBarIconController_TintedIconManager_Factory_Factory(this.featureFlagsReleaseProvider));
        }

        public final void inject(KeyguardSliceProvider keyguardSliceProvider) {
            injectKeyguardSliceProvider(keyguardSliceProvider);
        }

        public final ClockOptionsProvider injectClockOptionsProvider(ClockOptionsProvider clockOptionsProvider) {
            clockOptionsProvider.mClockInfosProvider = this.provideClockInfoListProvider;
            return clockOptionsProvider;
        }

        public final KeyguardSliceProvider injectKeyguardSliceProvider(KeyguardSliceProvider keyguardSliceProvider) {
            keyguardSliceProvider.mDozeParameters = this.dozeParametersProvider.mo144get();
            keyguardSliceProvider.mZenModeController = this.zenModeControllerImplProvider.mo144get();
            keyguardSliceProvider.mNextAlarmController = this.nextAlarmControllerImplProvider.mo144get();
            keyguardSliceProvider.mAlarmManager = DaggerTitanGlobalRootComponent.this.provideAlarmManagerProvider.mo144get();
            keyguardSliceProvider.mContentResolver = DaggerTitanGlobalRootComponent.this.provideContentResolverProvider.mo144get();
            keyguardSliceProvider.mMediaManager = this.provideNotificationMediaManagerProvider.mo144get();
            keyguardSliceProvider.mStatusBarStateController = this.statusBarStateControllerImplProvider.mo144get();
            keyguardSliceProvider.mKeyguardBypassController = this.keyguardBypassControllerProvider.mo144get();
            keyguardSliceProvider.mKeyguardUpdateMonitor = this.keyguardUpdateMonitorProvider.mo144get();
            return keyguardSliceProvider;
        }

        public final KeyguardSliceProviderGoogle injectKeyguardSliceProviderGoogle(KeyguardSliceProviderGoogle keyguardSliceProviderGoogle) {
            keyguardSliceProviderGoogle.mDozeParameters = this.dozeParametersProvider.mo144get();
            keyguardSliceProviderGoogle.mZenModeController = this.zenModeControllerImplProvider.mo144get();
            keyguardSliceProviderGoogle.mNextAlarmController = this.nextAlarmControllerImplProvider.mo144get();
            keyguardSliceProviderGoogle.mAlarmManager = DaggerTitanGlobalRootComponent.this.provideAlarmManagerProvider.mo144get();
            keyguardSliceProviderGoogle.mContentResolver = DaggerTitanGlobalRootComponent.this.provideContentResolverProvider.mo144get();
            keyguardSliceProviderGoogle.mMediaManager = this.provideNotificationMediaManagerProvider.mo144get();
            keyguardSliceProviderGoogle.mStatusBarStateController = this.statusBarStateControllerImplProvider.mo144get();
            keyguardSliceProviderGoogle.mKeyguardBypassController = this.keyguardBypassControllerProvider.mo144get();
            keyguardSliceProviderGoogle.mKeyguardUpdateMonitor = this.keyguardUpdateMonitorProvider.mo144get();
            keyguardSliceProviderGoogle.mSmartSpaceController = this.smartSpaceControllerProvider.mo144get();
            return keyguardSliceProviderGoogle;
        }

        public final PeopleProvider injectPeopleProvider(PeopleProvider peopleProvider) {
            peopleProvider.mPeopleSpaceWidgetManager = this.peopleSpaceWidgetManagerProvider.mo144get();
            return peopleProvider;
        }

        public final SystemUIAppComponentFactory injectSystemUIAppComponentFactory(SystemUIAppComponentFactory systemUIAppComponentFactory) {
            systemUIAppComponentFactory.mComponentHelper = this.contextComponentResolverProvider.mo144get();
            return systemUIAppComponentFactory;
        }

        public final FlingAnimationUtils namedFlingAnimationUtils() {
            return BouncerSwipeModule_ProvidesSwipeToBouncerFlingAnimationUtilsClosingFactory.providesSwipeToBouncerFlingAnimationUtilsClosing(this.builderProvider14);
        }

        public final FlingAnimationUtils namedFlingAnimationUtils2() {
            return BouncerSwipeModule_ProvidesSwipeToBouncerFlingAnimationUtilsOpeningFactory.providesSwipeToBouncerFlingAnimationUtilsOpening(this.builderProvider14);
        }

        public final float namedFloat() {
            DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent = DaggerTitanGlobalRootComponent.this;
            Provider provider = DaggerTitanGlobalRootComponent.ABSENT_JDK_OPTIONAL_PROVIDER;
            return BouncerSwipeModule.providesSwipeToBouncerStartRegion(daggerTitanGlobalRootComponent.mainResources());
        }

        public final View namedView() {
            View inflate = LayoutInflater.from(DaggerTitanGlobalRootComponent.this.context).inflate(2131624486, (ViewGroup) null);
            Objects.requireNonNull(inflate, "Cannot return null from a non-@Nullable @Provides method");
            return inflate;
        }

        @Override // com.android.systemui.dagger.SysUIComponent
        public final BootCompleteCacheImpl provideBootCacheImpl() {
            return this.bootCompleteCacheImplProvider.mo144get();
        }

        public final Object secureSettingsImpl() {
            return new SecureSettingsImpl(DaggerTitanGlobalRootComponent.this.provideContentResolverProvider.mo144get());
        }

        public final void inject(PeopleProvider peopleProvider) {
            injectPeopleProvider(peopleProvider);
        }

        public final DreamTouchHandler providesBouncerSwipeTouchHandler() {
            BouncerSwipeTouchHandler bouncerSwipeTouchHandler = bouncerSwipeTouchHandler();
            Objects.requireNonNull(bouncerSwipeTouchHandler, "Cannot return null from a non-@Nullable @Provides method");
            return bouncerSwipeTouchHandler;
        }

        public final void inject(KeyguardSliceProviderGoogle keyguardSliceProviderGoogle) {
            injectKeyguardSliceProviderGoogle(keyguardSliceProviderGoogle);
        }

        public final void inject(ClockOptionsProvider clockOptionsProvider) {
            clockOptionsProvider.mClockInfosProvider = this.provideClockInfoListProvider;
        }
    }

    /* loaded from: classes.dex */
    public final class WMComponentBuilder implements WMComponent.Builder {
        public HandlerThread setShellMainThread;

        public WMComponentBuilder() {
        }

        @Override // com.android.systemui.dagger.WMComponent.Builder
        public final WMComponent build() {
            return new WMComponentImpl(DaggerTitanGlobalRootComponent.this, this.setShellMainThread);
        }

        @Override // com.android.systemui.dagger.WMComponent.Builder
        /* renamed from: setShellMainThread */
        public final WMComponent.Builder mo143setShellMainThread(HandlerThread handlerThread) {
            this.setShellMainThread = handlerThread;
            return this;
        }
    }

    /* loaded from: classes.dex */
    public final class WMComponentImpl implements WMComponent {
        public Provider<Optional<DisplayImeController>> dynamicOverrideOptionalOfDisplayImeControllerProvider;
        public PresentJdkOptionalInstanceProvider dynamicOverrideOptionalOfFreeformTaskListenerProvider;
        public Provider<Optional<FullscreenTaskListener>> dynamicOverrideOptionalOfFullscreenTaskListenerProvider;
        public PresentJdkOptionalInstanceProvider dynamicOverrideOptionalOfFullscreenUnfoldControllerProvider;
        public PresentJdkOptionalInstanceProvider dynamicOverrideOptionalOfOneHandedControllerProvider;
        public PresentJdkOptionalInstanceProvider dynamicOverrideOptionalOfSplitScreenControllerProvider;
        public Provider<Optional<StartingWindowTypeAlgorithm>> dynamicOverrideOptionalOfStartingWindowTypeAlgorithmProvider;
        public PresentJdkOptionalInstanceProvider optionalOfAppPairsControllerProvider;
        public PresentJdkOptionalInstanceProvider optionalOfBubbleControllerProvider;
        public PresentJdkOptionalInstanceProvider optionalOfLegacySplitScreenControllerProvider;
        public PresentJdkOptionalInstanceProvider optionalOfPipTouchHandlerProvider;
        public PresentJdkOptionalInstanceProvider optionalOfShellUnfoldProgressProvider;
        public Provider<AppPairsController> provideAppPairsProvider;
        public Provider<Optional<Object>> provideAppPairsProvider2;
        public Provider<Optional<BackAnimationController>> provideBackAnimationControllerProvider;
        public Provider<Optional<BackAnimation>> provideBackAnimationProvider;
        public Provider<BubbleController> provideBubbleControllerProvider;
        public Provider<Optional<Bubbles>> provideBubblesProvider;
        public Provider<CompatUIController> provideCompatUIControllerProvider;
        public Provider<Optional<CompatUI>> provideCompatUIProvider;
        public Provider<Optional<DisplayAreaHelper>> provideDisplayAreaHelperProvider;
        public Provider<DisplayController> provideDisplayControllerProvider;
        public Provider<DisplayImeController> provideDisplayImeControllerProvider;
        public Provider<DisplayInsetsController> provideDisplayInsetsControllerProvider;
        public Provider<DisplayLayout> provideDisplayLayoutProvider;
        public Provider<DragAndDropController> provideDragAndDropControllerProvider;
        public Provider<Optional<DragAndDrop>> provideDragAndDropProvider;
        public Provider<FloatingContentCoordinator> provideFloatingContentCoordinatorProvider;
        public Provider<FreeformTaskListener> provideFreeformTaskListenerProvider;
        public Provider<Optional<FreeformTaskListener>> provideFreeformTaskListenerProvider2;
        public Provider<FullscreenTaskListener> provideFullscreenTaskListenerProvider;
        public Provider<FullscreenUnfoldController> provideFullscreenUnfoldControllerProvider;
        public Provider<Optional<FullscreenUnfoldController>> provideFullscreenUnfoldControllerProvider2;
        public Provider<Optional<HideDisplayCutoutController>> provideHideDisplayCutoutControllerProvider;
        public Provider<Optional<HideDisplayCutout>> provideHideDisplayCutoutProvider;
        public Provider<IconProvider> provideIconProvider;
        public Provider<LegacySplitScreenController> provideLegacySplitScreenProvider;
        public Provider<Optional<LegacySplitScreen>> provideLegacySplitScreenProvider2;
        public Provider<OneHandedController> provideOneHandedControllerProvider;
        public Provider<Optional<OneHanded>> provideOneHandedProvider;
        public Provider<PipAnimationController> providePipAnimationControllerProvider;
        public Provider<PipAppOpsListener> providePipAppOpsListenerProvider;
        public Provider<PipBoundsState> providePipBoundsStateProvider;
        public Provider<PipMediaController> providePipMediaControllerProvider;
        public Provider<PipMotionHelper> providePipMotionHelperProvider;
        public Provider<Optional<Pip>> providePipProvider;
        public Provider<PipSnapAlgorithm> providePipSnapAlgorithmProvider;
        public Provider<PipSurfaceTransactionHelper> providePipSurfaceTransactionHelperProvider;
        public Provider<PipTaskOrganizer> providePipTaskOrganizerProvider;
        public Provider<PipTouchHandler> providePipTouchHandlerProvider;
        public Provider<PipTransitionController> providePipTransitionControllerProvider;
        public Provider<PipTransitionState> providePipTransitionStateProvider;
        public Provider<PipUiEventLogger> providePipUiEventLoggerProvider;
        public Provider<Optional<RecentTasksController>> provideRecentTasksControllerProvider;
        public Provider<Optional<RecentTasks>> provideRecentTasksProvider;
        public Provider<ShellTransitions> provideRemoteTransitionsProvider;
        public Provider<RootDisplayAreaOrganizer> provideRootDisplayAreaOrganizerProvider;
        public Provider<RootTaskDisplayAreaOrganizer> provideRootTaskDisplayAreaOrganizerProvider;
        public Provider<ShellExecutor> provideShellAnimationExecutorProvider;
        public Provider<ShellCommandHandlerImpl> provideShellCommandHandlerImplProvider;
        public Provider<Optional<ShellCommandHandler>> provideShellCommandHandlerProvider;
        public Provider<ShellInitImpl> provideShellInitImplProvider;
        public Provider<ShellInit> provideShellInitProvider;
        public Provider<ShellExecutor> provideShellMainExecutorProvider;
        public Provider<AnimationHandler> provideShellMainExecutorSfVsyncAnimationHandlerProvider;
        public Provider<Handler> provideShellMainHandlerProvider;
        public Provider<ShellTaskOrganizer> provideShellTaskOrganizerProvider;
        public Provider<ShellExecutor> provideSplashScreenExecutorProvider;
        public Provider<SplitScreenController> provideSplitScreenControllerProvider;
        public Provider<Optional<SplitScreen>> provideSplitScreenProvider;
        public ControlsProviderSelectorActivity_Factory provideStageTaskUnfoldControllerProvider;
        public Provider<Optional<StartingSurface>> provideStartingSurfaceProvider;
        public Provider<StartingWindowController> provideStartingWindowControllerProvider;
        public Provider<StartingWindowTypeAlgorithm> provideStartingWindowTypeAlgorithmProvider;
        public Provider<SyncTransactionQueue> provideSyncTransactionQueueProvider;
        public Provider<ShellExecutor> provideSysUIMainExecutorProvider;
        public Provider<SystemWindows> provideSystemWindowsProvider;
        public WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory provideTaskSurfaceHelperControllerProvider;
        public Provider<Optional<TaskSurfaceHelper>> provideTaskSurfaceHelperProvider;
        public Provider<TaskViewFactoryController> provideTaskViewFactoryControllerProvider;
        public Provider<Optional<TaskViewFactory>> provideTaskViewFactoryProvider;
        public Provider<TaskViewTransitions> provideTaskViewTransitionsProvider;
        public Provider<TransactionPool> provideTransactionPoolProvider;
        public Provider<Transitions> provideTransitionsProvider;
        public Provider<UnfoldBackgroundController> provideUnfoldBackgroundControllerProvider;
        public Provider<WindowManagerShellWrapper> provideWindowManagerShellWrapperProvider;
        public Provider<TaskStackListenerImpl> providerTaskStackListenerImplProvider;
        public Provider<Optional<OneHandedController>> providesOneHandedControllerProvider;
        public Provider<PipBoundsAlgorithm> providesPipBoundsAlgorithmProvider;
        public Provider<PhonePipMenuController> providesPipPhoneMenuControllerProvider;
        public Provider<Optional<SplitScreenController>> providesSplitScreenControllerProvider;
        public InstanceFactory setShellMainThreadProvider;

        public WMComponentImpl(DaggerTitanGlobalRootComponent daggerTitanGlobalRootComponent, HandlerThread handlerThread) {
            InstanceFactory<Object> instanceFactory;
            if (handlerThread == null) {
                instanceFactory = InstanceFactory.NULL_INSTANCE_FACTORY;
            } else {
                instanceFactory = new InstanceFactory<>(handlerThread);
            }
            this.setShellMainThreadProvider = instanceFactory;
            Provider<Context> provider = daggerTitanGlobalRootComponent.contextProvider;
            WMShellConcurrencyModule_ProvideMainHandlerFactory wMShellConcurrencyModule_ProvideMainHandlerFactory = WMShellConcurrencyModule_ProvideMainHandlerFactory.InstanceHolder.INSTANCE;
            this.provideShellMainHandlerProvider = DoubleCheck.provider(new WMShellConcurrencyModule_ProvideShellMainHandlerFactory(provider, instanceFactory));
            Provider<ShellExecutor> provider2 = DoubleCheck.provider(new BootCompleteCacheImpl_Factory(wMShellConcurrencyModule_ProvideMainHandlerFactory, 5));
            this.provideSysUIMainExecutorProvider = provider2;
            Provider<ShellExecutor> provider3 = DoubleCheck.provider(new WMShellConcurrencyModule_ProvideShellMainExecutorFactory(daggerTitanGlobalRootComponent.contextProvider, this.provideShellMainHandlerProvider, provider2));
            this.provideShellMainExecutorProvider = provider3;
            Provider<DisplayController> provider4 = DoubleCheck.provider(new WMShellBaseModule_ProvideDisplayControllerFactory(daggerTitanGlobalRootComponent.contextProvider, daggerTitanGlobalRootComponent.provideIWindowManagerProvider, provider3));
            this.provideDisplayControllerProvider = provider4;
            Provider provider5 = DaggerTitanGlobalRootComponent.ABSENT_JDK_OPTIONAL_PROVIDER;
            this.dynamicOverrideOptionalOfDisplayImeControllerProvider = provider5;
            this.provideDisplayInsetsControllerProvider = DoubleCheck.provider(new RemoteInputQuickSettingsDisabler_Factory(daggerTitanGlobalRootComponent.provideIWindowManagerProvider, provider4, this.provideShellMainExecutorProvider, 1));
            Provider<TransactionPool> provider6 = DoubleCheck.provider(WMShellBaseModule_ProvideTransactionPoolFactory.InstanceHolder.INSTANCE);
            this.provideTransactionPoolProvider = provider6;
            this.provideDisplayImeControllerProvider = DoubleCheck.provider(RecordingService_Factory.create$1(this.dynamicOverrideOptionalOfDisplayImeControllerProvider, daggerTitanGlobalRootComponent.provideIWindowManagerProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideShellMainExecutorProvider, provider6));
            Provider<IconProvider> provider7 = DoubleCheck.provider(new EnhancedEstimatesGoogleImpl_Factory(daggerTitanGlobalRootComponent.contextProvider, 3));
            this.provideIconProvider = provider7;
            this.provideDragAndDropControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDragAndDropControllerFactory.create$1(daggerTitanGlobalRootComponent.contextProvider, this.provideDisplayControllerProvider, daggerTitanGlobalRootComponent.provideUiEventLoggerProvider, provider7, this.provideShellMainExecutorProvider));
            Provider<SyncTransactionQueue> provider8 = DoubleCheck.provider(new ResumeMediaBrowserFactory_Factory(this.provideTransactionPoolProvider, this.provideShellMainExecutorProvider, 1));
            this.provideSyncTransactionQueueProvider = provider8;
            this.provideCompatUIControllerProvider = DoubleCheck.provider(SmartReplyStateInflaterImpl_Factory.create$1(daggerTitanGlobalRootComponent.contextProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideDisplayImeControllerProvider, provider8, this.provideShellMainExecutorProvider));
            Provider<TaskStackListenerImpl> provider9 = DoubleCheck.provider(new SeekBarViewModel_Factory(this.provideShellMainHandlerProvider, 1));
            this.providerTaskStackListenerImplProvider = provider9;
            Provider<Optional<RecentTasksController>> provider10 = DoubleCheck.provider(new WMShellBaseModule_ProvideRecentTasksControllerFactory(daggerTitanGlobalRootComponent.contextProvider, provider9, this.provideShellMainExecutorProvider));
            this.provideRecentTasksControllerProvider = provider10;
            this.provideShellTaskOrganizerProvider = DoubleCheck.provider(new WMShellBaseModule_ProvideShellTaskOrganizerFactory(this.provideShellMainExecutorProvider, daggerTitanGlobalRootComponent.contextProvider, this.provideCompatUIControllerProvider, provider10));
            this.provideFloatingContentCoordinatorProvider = DoubleCheck.provider(WMShellBaseModule_ProvideFloatingContentCoordinatorFactory.InstanceHolder.INSTANCE);
            this.provideWindowManagerShellWrapperProvider = DoubleCheck.provider(new SecureSettingsImpl_Factory(this.provideShellMainExecutorProvider, 4));
            Provider<DisplayLayout> provider11 = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayLayoutFactory.InstanceHolder.INSTANCE);
            this.provideDisplayLayoutProvider = provider11;
            Provider<OneHandedController> provider12 = DoubleCheck.provider(WMShellModule_ProvideOneHandedControllerFactory.create(daggerTitanGlobalRootComponent.contextProvider, daggerTitanGlobalRootComponent.provideWindowManagerProvider, this.provideDisplayControllerProvider, provider11, this.providerTaskStackListenerImplProvider, daggerTitanGlobalRootComponent.provideUiEventLoggerProvider, daggerTitanGlobalRootComponent.provideInteractionJankMonitorProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider));
            this.provideOneHandedControllerProvider = provider12;
            this.dynamicOverrideOptionalOfOneHandedControllerProvider = new PresentJdkOptionalInstanceProvider(provider12);
            Provider<ShellExecutor> provider13 = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory.InstanceHolder.INSTANCE);
            this.provideShellAnimationExecutorProvider = provider13;
            Provider<Transitions> provider14 = DoubleCheck.provider(KeyguardMediaController_Factory.create$1(this.provideShellTaskOrganizerProvider, this.provideTransactionPoolProvider, this.provideDisplayControllerProvider, daggerTitanGlobalRootComponent.contextProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider, provider13));
            this.provideTransitionsProvider = provider14;
            Provider<TaskViewTransitions> provider15 = DoubleCheck.provider(new TaskbarDelegate_Factory(provider14, 3));
            this.provideTaskViewTransitionsProvider = provider15;
            Provider<BubbleController> provider16 = DoubleCheck.provider(WMShellModule_ProvideBubbleControllerFactory.create(daggerTitanGlobalRootComponent.contextProvider, this.provideFloatingContentCoordinatorProvider, daggerTitanGlobalRootComponent.provideIStatusBarServiceProvider, daggerTitanGlobalRootComponent.provideWindowManagerProvider, this.provideWindowManagerShellWrapperProvider, daggerTitanGlobalRootComponent.provideLauncherAppsProvider, this.providerTaskStackListenerImplProvider, daggerTitanGlobalRootComponent.provideUiEventLoggerProvider, this.provideShellTaskOrganizerProvider, this.provideDisplayControllerProvider, this.dynamicOverrideOptionalOfOneHandedControllerProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider, provider15, this.provideSyncTransactionQueueProvider));
            this.provideBubbleControllerProvider = provider16;
            this.optionalOfBubbleControllerProvider = new PresentJdkOptionalInstanceProvider(provider16);
            this.provideRootTaskDisplayAreaOrganizerProvider = DoubleCheck.provider(new KeyguardVisibility_Factory(this.provideShellMainExecutorProvider, daggerTitanGlobalRootComponent.contextProvider, 2));
            this.optionalOfShellUnfoldProgressProvider = new PresentJdkOptionalInstanceProvider(daggerTitanGlobalRootComponent.provideShellProgressProvider);
            Provider<UnfoldBackgroundController> provider17 = DoubleCheck.provider(new WMShellModule_ProvideUnfoldBackgroundControllerFactory(this.provideRootTaskDisplayAreaOrganizerProvider, daggerTitanGlobalRootComponent.contextProvider, 0));
            this.provideUnfoldBackgroundControllerProvider = provider17;
            PresentJdkOptionalInstanceProvider presentJdkOptionalInstanceProvider = this.optionalOfShellUnfoldProgressProvider;
            Provider<Context> provider18 = daggerTitanGlobalRootComponent.contextProvider;
            Provider<TransactionPool> provider19 = this.provideTransactionPoolProvider;
            Provider<DisplayInsetsController> provider20 = this.provideDisplayInsetsControllerProvider;
            Provider<ShellExecutor> provider21 = this.provideShellMainExecutorProvider;
            ControlsProviderSelectorActivity_Factory controlsProviderSelectorActivity_Factory = new ControlsProviderSelectorActivity_Factory(presentJdkOptionalInstanceProvider, provider18, provider19, provider17, provider20, provider21, 1);
            this.provideStageTaskUnfoldControllerProvider = controlsProviderSelectorActivity_Factory;
            Provider<SplitScreenController> provider22 = DoubleCheck.provider(WMShellModule_ProvideSplitScreenControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, provider18, this.provideRootTaskDisplayAreaOrganizerProvider, provider21, this.provideDisplayControllerProvider, this.provideDisplayImeControllerProvider, provider20, this.provideTransitionsProvider, provider19, this.provideIconProvider, this.provideRecentTasksControllerProvider, controlsProviderSelectorActivity_Factory));
            this.provideSplitScreenControllerProvider = provider22;
            PresentJdkOptionalInstanceProvider presentJdkOptionalInstanceProvider2 = new PresentJdkOptionalInstanceProvider(provider22);
            this.dynamicOverrideOptionalOfSplitScreenControllerProvider = presentJdkOptionalInstanceProvider2;
            this.providesSplitScreenControllerProvider = DoubleCheck.provider(new KeyguardLifecyclesDispatcher_Factory(presentJdkOptionalInstanceProvider2, daggerTitanGlobalRootComponent.contextProvider, 4));
            Provider<AppPairsController> provider23 = DoubleCheck.provider(new WMShellModule_ProvideAppPairsFactory(this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider, this.provideDisplayImeControllerProvider, this.provideDisplayInsetsControllerProvider, 0));
            this.provideAppPairsProvider = provider23;
            this.optionalOfAppPairsControllerProvider = new PresentJdkOptionalInstanceProvider(provider23);
            this.providePipBoundsStateProvider = DoubleCheck.provider(new DozeAuthRemover_Factory(daggerTitanGlobalRootComponent.contextProvider, 4));
            this.providePipMediaControllerProvider = DoubleCheck.provider(new VibratorHelper_Factory(daggerTitanGlobalRootComponent.contextProvider, this.provideShellMainHandlerProvider, 4));
            this.provideSystemWindowsProvider = DoubleCheck.provider(new WMShellBaseModule_ProvideSystemWindowsFactory(this.provideDisplayControllerProvider, daggerTitanGlobalRootComponent.provideIWindowManagerProvider, 0));
            Provider<PipUiEventLogger> provider24 = DoubleCheck.provider(new WMShellBaseModule_ProvidePipUiEventLoggerFactory(daggerTitanGlobalRootComponent.provideUiEventLoggerProvider, daggerTitanGlobalRootComponent.providePackageManagerProvider));
            this.providePipUiEventLoggerProvider = provider24;
            this.providesPipPhoneMenuControllerProvider = DoubleCheck.provider(UnfoldLightRevealOverlayAnimation_Factory.create$1(daggerTitanGlobalRootComponent.contextProvider, this.providePipBoundsStateProvider, this.providePipMediaControllerProvider, this.provideSystemWindowsProvider, this.providesSplitScreenControllerProvider, provider24, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider));
            Provider<PipSnapAlgorithm> provider25 = DoubleCheck.provider(WMShellModule_ProvidePipSnapAlgorithmFactory.InstanceHolder.INSTANCE);
            this.providePipSnapAlgorithmProvider = provider25;
            this.providesPipBoundsAlgorithmProvider = DoubleCheck.provider(new WMShellModule_ProvidesPipBoundsAlgorithmFactory(daggerTitanGlobalRootComponent.contextProvider, this.providePipBoundsStateProvider, provider25));
            this.providePipTransitionStateProvider = DoubleCheck.provider(WMShellModule_ProvidePipTransitionStateFactory.InstanceHolder.INSTANCE);
            Provider<PipSurfaceTransactionHelper> provider26 = DoubleCheck.provider(WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory.InstanceHolder.INSTANCE);
            this.providePipSurfaceTransactionHelperProvider = provider26;
            Provider<PipAnimationController> provider27 = DoubleCheck.provider(new AssistantWarmer_Factory(provider26, 3));
            this.providePipAnimationControllerProvider = provider27;
            this.providePipTransitionControllerProvider = DoubleCheck.provider(WMShellModule_ProvidePipTransitionControllerFactory.create(daggerTitanGlobalRootComponent.contextProvider, this.provideTransitionsProvider, this.provideShellTaskOrganizerProvider, provider27, this.providesPipBoundsAlgorithmProvider, this.providePipBoundsStateProvider, this.providePipTransitionStateProvider, this.providesPipPhoneMenuControllerProvider, this.providePipSurfaceTransactionHelperProvider, this.providesSplitScreenControllerProvider));
            Provider<AnimationHandler> provider28 = DoubleCheck.provider(new GameDashboardUiEventLogger_Factory(this.provideShellMainExecutorProvider, 1));
            this.provideShellMainExecutorSfVsyncAnimationHandlerProvider = provider28;
            Provider<LegacySplitScreenController> provider29 = DoubleCheck.provider(WMShellModule_ProvideLegacySplitScreenFactory.create(daggerTitanGlobalRootComponent.contextProvider, this.provideDisplayControllerProvider, this.provideSystemWindowsProvider, this.provideDisplayImeControllerProvider, this.provideTransactionPoolProvider, this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, this.providerTaskStackListenerImplProvider, this.provideTransitionsProvider, this.provideShellMainExecutorProvider, provider28));
            this.provideLegacySplitScreenProvider = provider29;
            PresentJdkOptionalInstanceProvider presentJdkOptionalInstanceProvider3 = new PresentJdkOptionalInstanceProvider(provider29);
            this.optionalOfLegacySplitScreenControllerProvider = presentJdkOptionalInstanceProvider3;
            Provider<PipTaskOrganizer> provider30 = DoubleCheck.provider(WMShellModule_ProvidePipTaskOrganizerFactory.create(daggerTitanGlobalRootComponent.contextProvider, this.provideSyncTransactionQueueProvider, this.providePipTransitionStateProvider, this.providePipBoundsStateProvider, this.providesPipBoundsAlgorithmProvider, this.providesPipPhoneMenuControllerProvider, this.providePipAnimationControllerProvider, this.providePipSurfaceTransactionHelperProvider, this.providePipTransitionControllerProvider, presentJdkOptionalInstanceProvider3, this.providesSplitScreenControllerProvider, this.provideDisplayControllerProvider, this.providePipUiEventLoggerProvider, this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider));
            this.providePipTaskOrganizerProvider = provider30;
            Provider<PipMotionHelper> provider31 = DoubleCheck.provider(new WMShellModule_ProvidePipMotionHelperFactory(daggerTitanGlobalRootComponent.contextProvider, this.providePipBoundsStateProvider, provider30, this.providesPipPhoneMenuControllerProvider, this.providePipSnapAlgorithmProvider, this.providePipTransitionControllerProvider, this.provideFloatingContentCoordinatorProvider));
            this.providePipMotionHelperProvider = provider31;
            Provider<PipTouchHandler> provider32 = DoubleCheck.provider(WMShellModule_ProvidePipTouchHandlerFactory.create(daggerTitanGlobalRootComponent.contextProvider, this.providesPipPhoneMenuControllerProvider, this.providesPipBoundsAlgorithmProvider, this.providePipBoundsStateProvider, this.providePipTaskOrganizerProvider, provider31, this.provideFloatingContentCoordinatorProvider, this.providePipUiEventLoggerProvider, this.provideShellMainExecutorProvider));
            this.providePipTouchHandlerProvider = provider32;
            this.optionalOfPipTouchHandlerProvider = new PresentJdkOptionalInstanceProvider(provider32);
            this.dynamicOverrideOptionalOfFullscreenTaskListenerProvider = provider5;
            Provider<FullscreenUnfoldController> provider33 = DoubleCheck.provider(new KeyguardSliceViewController_Factory(daggerTitanGlobalRootComponent.contextProvider, this.optionalOfShellUnfoldProgressProvider, this.provideUnfoldBackgroundControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideShellMainExecutorProvider, 1));
            this.provideFullscreenUnfoldControllerProvider = provider33;
            PresentJdkOptionalInstanceProvider presentJdkOptionalInstanceProvider4 = new PresentJdkOptionalInstanceProvider(provider33);
            this.dynamicOverrideOptionalOfFullscreenUnfoldControllerProvider = presentJdkOptionalInstanceProvider4;
            Provider<Optional<FullscreenUnfoldController>> provider34 = DoubleCheck.provider(new WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory(presentJdkOptionalInstanceProvider4, this.optionalOfShellUnfoldProgressProvider));
            this.provideFullscreenUnfoldControllerProvider2 = provider34;
            this.provideFullscreenTaskListenerProvider = DoubleCheck.provider(new ColumbusServiceWrapper_Factory(this.dynamicOverrideOptionalOfFullscreenTaskListenerProvider, this.provideSyncTransactionQueueProvider, provider34, this.provideRecentTasksControllerProvider, 1));
            Provider<FreeformTaskListener> provider35 = DoubleCheck.provider(new PackageManagerAdapter_Factory(this.provideSyncTransactionQueueProvider, 3));
            this.provideFreeformTaskListenerProvider = provider35;
            PresentJdkOptionalInstanceProvider presentJdkOptionalInstanceProvider5 = new PresentJdkOptionalInstanceProvider(provider35);
            this.dynamicOverrideOptionalOfFreeformTaskListenerProvider = presentJdkOptionalInstanceProvider5;
            this.provideFreeformTaskListenerProvider2 = DoubleCheck.provider(new VolumeUI_Factory(presentJdkOptionalInstanceProvider5, daggerTitanGlobalRootComponent.contextProvider, 2));
            this.provideSplashScreenExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory.InstanceHolder.INSTANCE);
            this.dynamicOverrideOptionalOfStartingWindowTypeAlgorithmProvider = provider5;
            Provider<StartingWindowTypeAlgorithm> provider36 = DoubleCheck.provider(new QSFragmentModule_ProvideThemedContextFactory(provider5, 5));
            this.provideStartingWindowTypeAlgorithmProvider = provider36;
            Provider<StartingWindowController> provider37 = DoubleCheck.provider(SystemKeyPress_Factory.create(daggerTitanGlobalRootComponent.contextProvider, this.provideSplashScreenExecutorProvider, provider36, this.provideIconProvider, this.provideTransactionPoolProvider));
            this.provideStartingWindowControllerProvider = provider37;
            Provider<ShellInitImpl> provider38 = DoubleCheck.provider(WMShellBaseModule_ProvideShellInitImplFactory.create(this.provideDisplayControllerProvider, this.provideDisplayImeControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideDragAndDropControllerProvider, this.provideShellTaskOrganizerProvider, this.optionalOfBubbleControllerProvider, this.providesSplitScreenControllerProvider, this.optionalOfAppPairsControllerProvider, this.optionalOfPipTouchHandlerProvider, this.provideFullscreenTaskListenerProvider, this.provideFullscreenUnfoldControllerProvider2, this.provideFreeformTaskListenerProvider2, this.provideRecentTasksControllerProvider, this.provideTransitionsProvider, provider37, this.provideShellMainExecutorProvider));
            this.provideShellInitImplProvider = provider38;
            this.provideShellInitProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline3.m(provider38, 7);
            this.providePipAppOpsListenerProvider = DoubleCheck.provider(new UnfoldSharedModule_HingeAngleProviderFactory(daggerTitanGlobalRootComponent.contextProvider, this.providePipTouchHandlerProvider, this.provideShellMainExecutorProvider));
            Provider<Optional<OneHandedController>> provider39 = DoubleCheck.provider(new PrivacyLogger_Factory(this.dynamicOverrideOptionalOfOneHandedControllerProvider, 5));
            this.providesOneHandedControllerProvider = provider39;
            this.providePipProvider = DoubleCheck.provider(WMShellModule_ProvidePipFactory.create(daggerTitanGlobalRootComponent.contextProvider, this.provideDisplayControllerProvider, this.providePipAppOpsListenerProvider, this.providesPipBoundsAlgorithmProvider, this.providePipBoundsStateProvider, this.providePipMediaControllerProvider, this.providesPipPhoneMenuControllerProvider, this.providePipTaskOrganizerProvider, this.providePipTouchHandlerProvider, this.providePipTransitionControllerProvider, this.provideWindowManagerShellWrapperProvider, this.providerTaskStackListenerImplProvider, provider39, this.provideShellMainExecutorProvider));
            Provider<Optional<HideDisplayCutoutController>> provider40 = DoubleCheck.provider(new DozeLog_Factory(daggerTitanGlobalRootComponent.contextProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider, 4));
            this.provideHideDisplayCutoutControllerProvider = provider40;
            Provider<ShellCommandHandlerImpl> provider41 = DoubleCheck.provider(WMShellBaseModule_ProvideShellCommandHandlerImplFactory.create(this.provideShellTaskOrganizerProvider, this.optionalOfLegacySplitScreenControllerProvider, this.providesSplitScreenControllerProvider, this.providePipProvider, this.providesOneHandedControllerProvider, provider40, this.optionalOfAppPairsControllerProvider, this.provideRecentTasksControllerProvider, this.provideShellMainExecutorProvider));
            this.provideShellCommandHandlerImplProvider = provider41;
            this.provideShellCommandHandlerProvider = DoubleCheck.provider(new ForegroundServicesDialog_Factory(provider41, 6));
            this.provideOneHandedProvider = DoubleCheck.provider(new DreamOverlayStateController_Factory(this.providesOneHandedControllerProvider, 2));
            this.provideLegacySplitScreenProvider2 = DoubleCheck.provider(new DozeLogger_Factory(this.optionalOfLegacySplitScreenControllerProvider, 7));
            this.provideSplitScreenProvider = DaggerGlobalRootComponent$SysUIComponentImpl$KeyguardBouncerComponentImpl$$ExternalSyntheticOutline1.m(this.providesSplitScreenControllerProvider, 0);
            this.provideAppPairsProvider2 = DoubleCheck.provider(new BootCompleteCacheImpl_Factory(this.optionalOfAppPairsControllerProvider, 4));
            this.provideBubblesProvider = DoubleCheck.provider(new WMShellBaseModule_ProvideBubblesFactory(this.optionalOfBubbleControllerProvider, 0));
            this.provideHideDisplayCutoutProvider = DoubleCheck.provider(new WMShellBaseModule_ProvideHideDisplayCutoutFactory(this.provideHideDisplayCutoutControllerProvider, 0));
            Provider<TaskViewFactoryController> provider42 = DoubleCheck.provider(new WMShellBaseModule_ProvideTaskViewFactoryControllerFactory(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider, this.provideSyncTransactionQueueProvider, this.provideTaskViewTransitionsProvider));
            this.provideTaskViewFactoryControllerProvider = provider42;
            this.provideTaskViewFactoryProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline2.m(provider42, 5);
            this.provideRemoteTransitionsProvider = DoubleCheck.provider(new UsbDebuggingActivity_Factory(this.provideTransitionsProvider, 4));
            this.provideStartingSurfaceProvider = DaggerGlobalRootComponent$SysUIComponentImpl$KeyguardBouncerComponentImpl$$ExternalSyntheticOutline0.m(this.provideStartingWindowControllerProvider, 6);
            Provider<RootDisplayAreaOrganizer> provider43 = DoubleCheck.provider(new ImageExporter_Factory(this.provideShellMainExecutorProvider, 6));
            this.provideRootDisplayAreaOrganizerProvider = provider43;
            this.provideDisplayAreaHelperProvider = DoubleCheck.provider(new SingleTapClassifier_Factory(this.provideShellMainExecutorProvider, provider43, 1));
            WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory wMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory = new WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider, 0);
            this.provideTaskSurfaceHelperControllerProvider = wMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory;
            this.provideTaskSurfaceHelperProvider = DoubleCheck.provider(new DependencyProvider_ProvideHandlerFactory(wMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory, 6));
            this.provideRecentTasksProvider = DoubleCheck.provider(new WMShellBaseModule_ProvideRecentTasksFactory(this.provideRecentTasksControllerProvider, 0));
            this.provideCompatUIProvider = DoubleCheck.provider(new ToastLogger_Factory(this.provideCompatUIControllerProvider, 3));
            this.provideDragAndDropProvider = DoubleCheck.provider(new ColorChangeHandler_Factory(this.provideDragAndDropControllerProvider, 6));
            Provider<Optional<BackAnimationController>> provider44 = DoubleCheck.provider(new DreamOverlayRegistrant_Factory(daggerTitanGlobalRootComponent.contextProvider, this.provideShellMainExecutorProvider, 2));
            this.provideBackAnimationControllerProvider = provider44;
            this.provideBackAnimationProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline1.m(provider44, 6);
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<Object> getAppPairs() {
            return this.provideAppPairsProvider2.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<BackAnimation> getBackAnimation() {
            return this.provideBackAnimationProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<Bubbles> getBubbles() {
            return this.provideBubblesProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<CompatUI> getCompatUI() {
            return this.provideCompatUIProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<DisplayAreaHelper> getDisplayAreaHelper() {
            return this.provideDisplayAreaHelperProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<DragAndDrop> getDragAndDrop() {
            return this.provideDragAndDropProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<HideDisplayCutout> getHideDisplayCutout() {
            return this.provideHideDisplayCutoutProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<LegacySplitScreen> getLegacySplitScreen() {
            return this.provideLegacySplitScreenProvider2.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<OneHanded> getOneHanded() {
            return this.provideOneHandedProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<Pip> getPip() {
            return this.providePipProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<RecentTasks> getRecentTasks() {
            return this.provideRecentTasksProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<ShellCommandHandler> getShellCommandHandler() {
            return this.provideShellCommandHandlerProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final ShellInit getShellInit() {
            return this.provideShellInitProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<SplitScreen> getSplitScreen() {
            return this.provideSplitScreenProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<StartingSurface> getStartingSurface() {
            return this.provideStartingSurfaceProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<TaskSurfaceHelper> getTaskSurfaceHelper() {
            return this.provideTaskSurfaceHelperProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final Optional<TaskViewFactory> getTaskViewFactory() {
            return this.provideTaskViewFactoryProvider.mo144get();
        }

        @Override // com.android.systemui.dagger.WMComponent
        public final ShellTransitions getTransitions() {
            return this.provideRemoteTransitionsProvider.mo144get();
        }
    }

    public static TitanGlobalRootComponent$Builder builder() {
        return new Builder();
    }

    @Override // com.android.systemui.dagger.GlobalRootComponent
    /* renamed from: getSysUIComponent */
    public final TitanSysUIComponent$Builder mo117getSysUIComponent() {
        return new TitanSysUIComponentBuilder();
    }

    @Override // com.android.systemui.dagger.GlobalRootComponent
    /* renamed from: getWMComponentBuilder */
    public final WMComponent.Builder mo118getWMComponentBuilder() {
        return new WMComponentBuilder();
    }

    public final void initialize(GlobalModule globalModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context) {
        this.contextProvider = InstanceFactory.create(context);
        this.provideIWindowManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIWindowManagerFactory.InstanceHolder.INSTANCE);
        this.provideUiEventLoggerProvider = DoubleCheck.provider(GlobalModule_ProvideUiEventLoggerFactory.InstanceHolder.INSTANCE);
        this.provideIStatusBarServiceProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIStatusBarServiceFactory.InstanceHolder.INSTANCE);
        this.provideWindowManagerProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline2.m(this.contextProvider, 1);
        this.provideLauncherAppsProvider = DoubleCheck.provider(new EnhancedEstimatesGoogleImpl_Factory(this.contextProvider, 1));
        this.provideInteractionJankMonitorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideInteractionJankMonitorFactory.InstanceHolder.INSTANCE);
        this.provideUnfoldTransitionConfigProvider = DoubleCheck.provider(new ShortcutKeyDispatcher_Factory(unfoldTransitionModule, this.contextProvider));
        Provider<ContentResolver> provider = DoubleCheck.provider(new AssistantWarmer_Factory(this.contextProvider, 1));
        this.provideContentResolverProvider = provider;
        C0015ScaleAwareTransitionProgressProvider_Factory scaleAwareTransitionProgressProvider_Factory = new C0015ScaleAwareTransitionProgressProvider_Factory(provider);
        this.scaleAwareTransitionProgressProvider = scaleAwareTransitionProgressProvider_Factory;
        this.factoryProvider = InstanceFactory.create(new ScaleAwareTransitionProgressProvider_Factory_Impl(scaleAwareTransitionProgressProvider_Factory));
        ImageTileSet_Factory imageTileSet_Factory = new ImageTileSet_Factory(unfoldTransitionModule, 10);
        this.tracingTagPrefixProvider = imageTileSet_Factory;
        this.aTraceLoggerTransitionProgressListenerProvider = new QSFragmentModule_ProvideQSPanelFactory(imageTileSet_Factory, 4);
        Provider<SensorManager> provider2 = DoubleCheck.provider(new TvPipModule_ProvideTvPipBoundsStateFactory(this.contextProvider, 1));
        this.providesSensorManagerProvider = provider2;
        this.hingeAngleProvider = new UnfoldSharedModule_HingeAngleProviderFactory(unfoldSharedModule, this.provideUnfoldTransitionConfigProvider, provider2);
        Provider<DumpManager> provider3 = DoubleCheck.provider(DumpManager_Factory.InstanceHolder.INSTANCE);
        this.dumpManagerProvider = provider3;
        Provider<ScreenLifecycle> provider4 = DoubleCheck.provider(new ScreenLifecycle_Factory(provider3, 0));
        this.screenLifecycleProvider = provider4;
        Provider<LifecycleScreenStatusProvider> provider5 = DoubleCheck.provider(new SecureSettingsImpl_Factory(provider4, 1));
        this.lifecycleScreenStatusProvider = provider5;
        this.screenStatusProvider = new PowerNotificationWarnings_Factory(unfoldTransitionModule, provider5);
        this.provideDeviceStateManagerProvider = DoubleCheck.provider(new TimeoutManager_Factory(this.contextProvider, 1));
        Provider<Executor> provider6 = DoubleCheck.provider(new FrameworkServicesModule_ProvideOptionalVibratorFactory(this.contextProvider, 3));
        this.provideMainExecutorProvider = provider6;
        GlobalConcurrencyModule_ProvideMainLooperFactory globalConcurrencyModule_ProvideMainLooperFactory = GlobalConcurrencyModule_ProvideMainLooperFactory.InstanceHolder.INSTANCE;
        WMShellBaseModule_ProvideRecentTasksFactory wMShellBaseModule_ProvideRecentTasksFactory = new WMShellBaseModule_ProvideRecentTasksFactory(globalConcurrencyModule_ProvideMainLooperFactory, 4);
        this.provideMainHandlerProvider = wMShellBaseModule_ProvideRecentTasksFactory;
        DeviceFoldStateProvider_Factory create = DeviceFoldStateProvider_Factory.create(this.contextProvider, this.hingeAngleProvider, this.screenStatusProvider, this.provideDeviceStateManagerProvider, provider6, wMShellBaseModule_ProvideRecentTasksFactory);
        this.deviceFoldStateProvider = create;
        Provider<FoldStateProvider> provider7 = DoubleCheck.provider(new VibratorHelper_Factory(unfoldSharedModule, create));
        this.provideFoldStateProvider = provider7;
        Provider<Optional<UnfoldTransitionProgressProvider>> provider8 = DoubleCheck.provider(new UnfoldSharedModule_UnfoldTransitionProgressProviderFactory(unfoldSharedModule, this.provideUnfoldTransitionConfigProvider, this.factoryProvider, this.aTraceLoggerTransitionProgressListenerProvider, provider7));
        this.unfoldTransitionProgressProvider = provider8;
        this.provideShellProgressProvider = DoubleCheck.provider(new ChargingState_Factory(unfoldTransitionModule, this.provideUnfoldTransitionConfigProvider, provider8));
        this.providePackageManagerProvider = DoubleCheck.provider(new UsbDebuggingActivity_Factory(this.contextProvider, 1));
        this.provideUserManagerProvider = DoubleCheck.provider(new LogModule_ProvidePrivacyLogBufferFactory(this.contextProvider, 2));
        this.provideMainDelayableExecutorProvider = DoubleCheck.provider(new MediaOutputDialogReceiver_Factory(globalConcurrencyModule_ProvideMainLooperFactory, 3));
        this.provideExecutionProvider = DoubleCheck.provider(ExecutionImpl_Factory.InstanceHolder.INSTANCE);
        this.provideActivityTaskManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideActivityTaskManagerFactory.InstanceHolder.INSTANCE);
        this.providesFingerprintManagerProvider = DoubleCheck.provider(new TaskbarDelegate_Factory(this.contextProvider, 1));
        this.provideFaceManagerProvider = DoubleCheck.provider(new FrameworkServicesModule_ProvideFaceManagerFactory(this.contextProvider, 0));
        Provider<Context> provider9 = this.contextProvider;
        this.provideDisplayMetricsProvider = new SystemUIDialogManager_Factory(globalModule, provider9);
        this.providePowerManagerProvider = DoubleCheck.provider(new UsbDebuggingSecondaryUserActivity_Factory(provider9, 1));
        this.provideAlarmManagerProvider = DoubleCheck.provider(new WMShellBaseModule_ProvideBubblesFactory(this.contextProvider, 1));
        this.provideIDreamManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIDreamManagerFactory.InstanceHolder.INSTANCE);
        this.provideDevicePolicyManagerProvider = DoubleCheck.provider(new DozeAuthRemover_Factory(this.contextProvider, 1));
        this.provideResourcesProvider = new ForegroundServicesDialog_Factory(this.contextProvider, 1);
        this.providesPluginExecutorProvider = DoubleCheck.provider(PluginsModule_ProvidesPluginExecutorFactory.create(ThreadFactoryImpl_Factory.InstanceHolder.INSTANCE));
        this.provideNotificationManagerProvider = DoubleCheck.provider(new MediaFlags_Factory(this.contextProvider, 1));
        this.pluginEnablerImplProvider = DoubleCheck.provider(PluginEnablerImpl_Factory.create(this.contextProvider, this.providePackageManagerProvider));
        PluginsModule_ProvidesPrivilegedPluginsFactory create2 = PluginsModule_ProvidesPrivilegedPluginsFactory.create(this.contextProvider);
        this.providesPrivilegedPluginsProvider = create2;
        Provider<PluginInstance.Factory> provider10 = DoubleCheck.provider(PluginsModule_ProvidesPluginInstanceFactoryFactory.create(create2, PluginsModule_ProvidesPluginDebugFactory.create()));
        this.providesPluginInstanceFactoryProvider = provider10;
        this.providePluginInstanceManagerFactoryProvider = DoubleCheck.provider(PluginsModule_ProvidePluginInstanceManagerFactoryFactory.create(this.contextProvider, this.providePackageManagerProvider, this.provideMainExecutorProvider, this.providesPluginExecutorProvider, this.provideNotificationManagerProvider, this.pluginEnablerImplProvider, this.providesPrivilegedPluginsProvider, provider10));
        this.providesPluginPrefsProvider = PluginsModule_ProvidesPluginPrefsFactory.create(this.contextProvider);
        this.providesPluginManagerProvider = DoubleCheck.provider(PluginsModule_ProvidesPluginManagerFactory.create(this.contextProvider, this.providePluginInstanceManagerFactoryProvider, PluginsModule_ProvidesPluginDebugFactory.create(), GlobalConcurrencyModule_ProvidesUncaughtExceptionHandlerFactory.InstanceHolder.INSTANCE, this.pluginEnablerImplProvider, this.providesPluginPrefsProvider, this.providesPrivilegedPluginsProvider));
        this.providePackageManagerWrapperProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePackageManagerWrapperFactory.InstanceHolder.INSTANCE);
        Provider<Optional<NaturalRotationUnfoldProgressProvider>> provider11 = DoubleCheck.provider(new MediaDreamSentinel_Factory(unfoldTransitionModule, this.contextProvider, this.provideIWindowManagerProvider, this.unfoldTransitionProgressProvider));
        this.provideNaturalRotationProgressProvider = provider11;
        this.provideStatusBarScopedTransitionProvider = DoubleCheck.provider(new NotificationPanelUnfoldAnimationController_Factory(unfoldTransitionModule, provider11));
        this.provideIActivityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIActivityManagerFactory.InstanceHolder.INSTANCE);
        this.provideAccessibilityManagerProvider = DoubleCheck.provider(new SystemUIModule_ProvideLowLightClockControllerFactory(this.contextProvider, 1));
        this.taskbarDelegateProvider = DoubleCheck.provider(new TaskbarDelegate_Factory(this.contextProvider, 0));
        this.provideCrossWindowBlurListenersProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory.InstanceHolder.INSTANCE);
        this.provideWallpaperManagerProvider = new DependencyProvider_ProvideHandlerFactory(this.contextProvider, 1);
        this.provideOptionalTelecomManagerProvider = DoubleCheck.provider(new MediaOutputDialogReceiver_Factory(this.contextProvider, 1));
        this.provideInputMethodManagerProvider = DoubleCheck.provider(new StatusBarInitializer_Factory(this.contextProvider, 1));
        this.opaEnabledSettingsProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline1.m(this.contextProvider, 7);
        this.provideMediaSessionManagerProvider = new DreamOverlayStateController_Factory(this.contextProvider, 1);
        this.provideMediaRouter2ManagerProvider = new DozeLogger_Factory(this.contextProvider, 1);
        this.provideLatencyTrackerProvider = DoubleCheck.provider(new WMShellBaseModule_ProvideHideDisplayCutoutFactory(this.contextProvider, 1));
        this.provideKeyguardManagerProvider = DoubleCheck.provider(new WallpaperController_Factory(this.contextProvider, 2));
        this.provideAudioManagerProvider = DoubleCheck.provider(new PackageManagerAdapter_Factory(this.contextProvider, 1));
        this.provideSensorPrivacyManagerProvider = DoubleCheck.provider(new PowerSaveState_Factory(this.contextProvider, 1));
        this.provideViewConfigurationProvider = DoubleCheck.provider(new KeyboardUI_Factory(this.contextProvider, 2));
        this.provideTrustManagerProvider = DoubleCheck.provider(new QSFragmentModule_ProvideThemedContextFactory(this.contextProvider, 2));
        this.provideTelephonyManagerProvider = DoubleCheck.provider(new QSFragmentModule_ProvideRootViewFactory(this.contextProvider, 2));
        this.provideVibratorProvider = DoubleCheck.provider(new VrMode_Factory(this.contextProvider, 2));
        this.provideDisplayManagerProvider = DoubleCheck.provider(new MediaControllerFactory_Factory(this.contextProvider, 2));
        this.provideIBatteryStatsProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIBatteryStatsFactory.InstanceHolder.INSTANCE);
        this.provideDisplayIdProvider = new ColorChangeHandler_Factory(this.contextProvider, 2);
        this.provideSubcriptionManagerProvider = DaggerGlobalRootComponent$SysUIComponentImpl$KeyguardBouncerComponentImpl$$ExternalSyntheticOutline1.m(this.contextProvider, 2);
        this.provideConnectivityManagagerProvider = DoubleCheck.provider(new TypeClassifier_Factory(this.contextProvider, 3));
        this.provideWifiManagerProvider = DoubleCheck.provider(new ActivityIntentHelper_Factory(this.contextProvider, 1));
        this.provideNetworkScoreManagerProvider = DoubleCheck.provider(new QSLogger_Factory(this.contextProvider, 1));
        this.provideShortcutManagerProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline3.m(this.contextProvider, 1);
        this.provideIAudioServiceProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIAudioServiceFactory.InstanceHolder.INSTANCE);
        this.pluginDependencyProvider = DoubleCheck.provider(PluginDependencyProvider_Factory.create(this.providesPluginManagerProvider));
        this.provideTelecomManagerProvider = DaggerGlobalRootComponent$SysUIComponentImpl$KeyguardBouncerComponentImpl$$ExternalSyntheticOutline0.m(this.contextProvider, 2);
        this.provideActivityManagerProvider = DaggerGlobalRootComponent$SysUIComponentImpl$$ExternalSyntheticOutline1.m(this.contextProvider, 1);
        this.provideClipboardManagerProvider = DoubleCheck.provider(new ToastLogger_Factory(this.contextProvider, 1));
        this.provideOverlayManagerProvider = DoubleCheck.provider(new WMShellBaseModule_ProvideRecentTasksFactory(this.contextProvider, 1));
        this.provideStatsManagerProvider = DoubleCheck.provider(new ActionClickLogger_Factory(this.contextProvider, 1));
        this.provideSmartspaceManagerProvider = DoubleCheck.provider(new SmartActionsReceiver_Factory(this.contextProvider, 1));
        Provider<Optional<FoldStateLoggingProvider>> provider12 = DoubleCheck.provider(new DarkIconDispatcherImpl_Factory(unfoldTransitionModule, this.provideUnfoldTransitionConfigProvider, this.provideFoldStateProvider));
        this.providesFoldStateLoggingProvider = provider12;
        this.providesFoldStateLoggerProvider = DoubleCheck.provider(new KeyguardVisibility_Factory(unfoldTransitionModule, provider12));
        this.provideColorDisplayManagerProvider = DoubleCheck.provider(new NavigationBarOverlayController_Factory(this.contextProvider, 1));
        this.provideIPackageManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIPackageManagerFactory.InstanceHolder.INSTANCE);
        this.providePermissionManagerProvider = DoubleCheck.provider(new ImageExporter_Factory(this.contextProvider, 2));
        this.provideOptionalVibratorProvider = DoubleCheck.provider(new FrameworkServicesModule_ProvideOptionalVibratorFactory(this.contextProvider, 0));
        this.qSExpansionPathInterpolatorProvider = DoubleCheck.provider(QSExpansionPathInterpolator_Factory.InstanceHolder.INSTANCE);
    }

    public final Resources mainResources() {
        Resources resources = this.context.getResources();
        Objects.requireNonNull(resources, "Cannot return null from a non-@Nullable @Provides method");
        return resources;
    }

    public DaggerTitanGlobalRootComponent(GlobalModule globalModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context) {
        this.context = context;
        initialize(globalModule, unfoldTransitionModule, unfoldSharedModule, context);
    }

    public final Handler mainHandler() {
        Looper mainLooper = Looper.getMainLooper();
        Objects.requireNonNull(mainLooper, "Cannot return null from a non-@Nullable @Provides method");
        return new Handler(mainLooper);
    }

    public static <T> Provider<Optional<T>> absentJdkOptionalProvider() {
        return ABSENT_JDK_OPTIONAL_PROVIDER;
    }
}
