package com.android.systemui;

import android.app.ActivityThread;
import android.app.AlarmManager;
import android.app.INotificationManager;
import android.app.IWallpaperManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.IWindowManager;
import androidx.coordinatorlayout.R$styleable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.clock.ClockManager;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.Dependency;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dagger.DaggerGlobalRootComponent;
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.WMComponent;
import com.android.systemui.dock.DockManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.fragments.FragmentService;
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
import com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider;
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
import com.android.wm.shell.transition.ShellTransitions;
import dagger.Lazy;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class SystemUIFactory {
    public static SystemUIFactory mFactory;
    public boolean mInitializeComponents;
    public GlobalRootComponent mRootComponent;
    public SysUIComponent mSysUIComponent;
    public WMComponent mWMComponent;

    @VisibleForTesting
    public static void cleanup() {
        mFactory = null;
    }

    @VisibleForTesting
    public void init(Context context, boolean z) throws ExecutionException, InterruptedException {
        boolean z2;
        SysUIComponent.Builder builder;
        if (z || !Process.myUserHandle().isSystem() || !ActivityThread.currentProcessName().equals(ActivityThread.currentPackageName())) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.mInitializeComponents = z2;
        GlobalRootComponent buildGlobalRootComponent = buildGlobalRootComponent(context);
        this.mRootComponent = buildGlobalRootComponent;
        final WMComponent.Builder wMComponentBuilder = buildGlobalRootComponent.mo118getWMComponentBuilder();
        if (!this.mInitializeComponents || !context.getResources().getBoolean(2131034135)) {
            this.mWMComponent = wMComponentBuilder.build();
        } else {
            final HandlerThread handlerThread = new HandlerThread("wmshell.main", -4);
            handlerThread.start();
            if (!Handler.createAsync(handlerThread.getLooper()).runWithScissors(new Runnable() { // from class: com.android.systemui.SystemUIFactory$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SystemUIFactory systemUIFactory = SystemUIFactory.this;
                    WMComponent.Builder builder2 = wMComponentBuilder;
                    HandlerThread handlerThread2 = handlerThread;
                    Objects.requireNonNull(systemUIFactory);
                    builder2.mo143setShellMainThread(handlerThread2);
                    systemUIFactory.mWMComponent = builder2.build();
                }
            }, 5000L)) {
                Log.w("SystemUIFactory", "Failed to initialize WMComponent");
                throw new RuntimeException();
            }
        }
        if (this.mInitializeComponents) {
            this.mWMComponent.init();
        }
        SysUIComponent.Builder sysUIComponent = this.mRootComponent.mo117getSysUIComponent();
        if (this.mInitializeComponents) {
            builder = sysUIComponent.mo129setPip(this.mWMComponent.getPip()).mo127setLegacySplitScreen(this.mWMComponent.getLegacySplitScreen()).mo132setSplitScreen(this.mWMComponent.getSplitScreen()).mo128setOneHanded(this.mWMComponent.getOneHanded()).mo122setBubbles(this.mWMComponent.getBubbles()).mo126setHideDisplayCutout(this.mWMComponent.getHideDisplayCutout()).mo131setShellCommandHandler(this.mWMComponent.getShellCommandHandler()).mo120setAppPairs(this.mWMComponent.getAppPairs()).mo135setTaskViewFactory(this.mWMComponent.getTaskViewFactory()).mo136setTransitions(this.mWMComponent.getTransitions()).mo133setStartingSurface(this.mWMComponent.getStartingSurface()).mo124setDisplayAreaHelper(this.mWMComponent.getDisplayAreaHelper()).mo134setTaskSurfaceHelper(this.mWMComponent.getTaskSurfaceHelper()).mo130setRecentTasks(this.mWMComponent.getRecentTasks()).mo123setCompatUI(this.mWMComponent.getCompatUI()).mo125setDragAndDrop(this.mWMComponent.getDragAndDrop()).mo121setBackAnimation(this.mWMComponent.getBackAnimation());
        } else {
            builder = sysUIComponent.mo129setPip(Optional.ofNullable(null)).mo127setLegacySplitScreen(Optional.ofNullable(null)).mo132setSplitScreen(Optional.ofNullable(null)).mo128setOneHanded(Optional.ofNullable(null)).mo122setBubbles(Optional.ofNullable(null)).mo126setHideDisplayCutout(Optional.ofNullable(null)).mo131setShellCommandHandler(Optional.ofNullable(null)).mo120setAppPairs(Optional.ofNullable(null)).mo135setTaskViewFactory(Optional.ofNullable(null)).mo136setTransitions(new ShellTransitions() { // from class: com.android.systemui.SystemUIFactory.1
            }).mo124setDisplayAreaHelper(Optional.ofNullable(null)).mo133setStartingSurface(Optional.ofNullable(null)).mo134setTaskSurfaceHelper(Optional.ofNullable(null)).mo130setRecentTasks(Optional.ofNullable(null)).mo123setCompatUI(Optional.ofNullable(null)).mo125setDragAndDrop(Optional.ofNullable(null)).mo121setBackAnimation(Optional.ofNullable(null));
        }
        SysUIComponent build = builder.build();
        this.mSysUIComponent = build;
        if (this.mInitializeComponents) {
            build.init();
        }
        Dependency createDependency = this.mSysUIComponent.createDependency();
        Objects.requireNonNull(createDependency);
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap = createDependency.mProviders;
        Dependency.DependencyKey<Handler> dependencyKey = Dependency.TIME_TICK_HANDLER;
        final Lazy<Handler> lazy = createDependency.mTimeTickHandler;
        Objects.requireNonNull(lazy);
        arrayMap.put(dependencyKey, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap2 = createDependency.mProviders;
        Dependency.DependencyKey<Looper> dependencyKey2 = Dependency.BG_LOOPER;
        final Lazy<Looper> lazy2 = createDependency.mBgLooper;
        Objects.requireNonNull(lazy2);
        arrayMap2.put(dependencyKey2, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy2.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap3 = createDependency.mProviders;
        Dependency.DependencyKey<Looper> dependencyKey3 = Dependency.MAIN_LOOPER;
        final Lazy<Looper> lazy3 = createDependency.mMainLooper;
        Objects.requireNonNull(lazy3);
        arrayMap3.put(dependencyKey3, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy3.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap4 = createDependency.mProviders;
        Dependency.DependencyKey<Handler> dependencyKey4 = Dependency.MAIN_HANDLER;
        final Lazy<Handler> lazy4 = createDependency.mMainHandler;
        Objects.requireNonNull(lazy4);
        arrayMap4.put(dependencyKey4, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy4.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap5 = createDependency.mProviders;
        Dependency.DependencyKey<Executor> dependencyKey5 = Dependency.MAIN_EXECUTOR;
        final Lazy<Executor> lazy5 = createDependency.mMainExecutor;
        Objects.requireNonNull(lazy5);
        arrayMap5.put(dependencyKey5, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda10
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy5.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap6 = createDependency.mProviders;
        Dependency.DependencyKey<Executor> dependencyKey6 = Dependency.BACKGROUND_EXECUTOR;
        final Lazy<Executor> lazy6 = createDependency.mBackgroundExecutor;
        Objects.requireNonNull(lazy6);
        arrayMap6.put(dependencyKey6, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda21
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy6.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap7 = createDependency.mProviders;
        final Lazy<ActivityStarter> lazy7 = createDependency.mActivityStarter;
        Objects.requireNonNull(lazy7);
        arrayMap7.put(ActivityStarter.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda26
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy7.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap8 = createDependency.mProviders;
        final Lazy<BroadcastDispatcher> lazy8 = createDependency.mBroadcastDispatcher;
        Objects.requireNonNull(lazy8);
        arrayMap8.put(BroadcastDispatcher.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda27
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy8.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap9 = createDependency.mProviders;
        final Lazy<AsyncSensorManager> lazy9 = createDependency.mAsyncSensorManager;
        Objects.requireNonNull(lazy9);
        arrayMap9.put(AsyncSensorManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda28
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy9.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap10 = createDependency.mProviders;
        final Lazy<BluetoothController> lazy10 = createDependency.mBluetoothController;
        Objects.requireNonNull(lazy10);
        arrayMap10.put(BluetoothController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda7
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy10.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap11 = createDependency.mProviders;
        final Lazy<SensorPrivacyManager> lazy11 = createDependency.mSensorPrivacyManager;
        Objects.requireNonNull(lazy11);
        arrayMap11.put(SensorPrivacyManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy11.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap12 = createDependency.mProviders;
        final Lazy<LocationController> lazy12 = createDependency.mLocationController;
        Objects.requireNonNull(lazy12);
        arrayMap12.put(LocationController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy12.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap13 = createDependency.mProviders;
        final Lazy<RotationLockController> lazy13 = createDependency.mRotationLockController;
        Objects.requireNonNull(lazy13);
        arrayMap13.put(RotationLockController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy13.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap14 = createDependency.mProviders;
        final Lazy<ZenModeController> lazy14 = createDependency.mZenModeController;
        Objects.requireNonNull(lazy14);
        arrayMap14.put(ZenModeController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy14.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap15 = createDependency.mProviders;
        final Lazy<HotspotController> lazy15 = createDependency.mHotspotController;
        Objects.requireNonNull(lazy15);
        arrayMap15.put(HotspotController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy15.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap16 = createDependency.mProviders;
        final Lazy<CastController> lazy16 = createDependency.mCastController;
        Objects.requireNonNull(lazy16);
        arrayMap16.put(CastController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy16.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap17 = createDependency.mProviders;
        final Lazy<FlashlightController> lazy17 = createDependency.mFlashlightController;
        Objects.requireNonNull(lazy17);
        arrayMap17.put(FlashlightController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy17.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap18 = createDependency.mProviders;
        final Lazy<KeyguardStateController> lazy18 = createDependency.mKeyguardMonitor;
        Objects.requireNonNull(lazy18);
        arrayMap18.put(KeyguardStateController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda26
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy18.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap19 = createDependency.mProviders;
        final Lazy<KeyguardUpdateMonitor> lazy19 = createDependency.mKeyguardUpdateMonitor;
        Objects.requireNonNull(lazy19);
        arrayMap19.put(KeyguardUpdateMonitor.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy19.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap20 = createDependency.mProviders;
        final Lazy<UserSwitcherController> lazy20 = createDependency.mUserSwitcherController;
        Objects.requireNonNull(lazy20);
        arrayMap20.put(UserSwitcherController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy20.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap21 = createDependency.mProviders;
        final Lazy<UserInfoController> lazy21 = createDependency.mUserInfoController;
        Objects.requireNonNull(lazy21);
        arrayMap21.put(UserInfoController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy21.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap22 = createDependency.mProviders;
        final Lazy<BatteryController> lazy22 = createDependency.mBatteryController;
        Objects.requireNonNull(lazy22);
        arrayMap22.put(BatteryController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy22.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap23 = createDependency.mProviders;
        final Lazy<NightDisplayListener> lazy23 = createDependency.mNightDisplayListener;
        Objects.requireNonNull(lazy23);
        arrayMap23.put(NightDisplayListener.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy23.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap24 = createDependency.mProviders;
        final Lazy<ReduceBrightColorsController> lazy24 = createDependency.mReduceBrightColorsController;
        Objects.requireNonNull(lazy24);
        arrayMap24.put(ReduceBrightColorsController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy24.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap25 = createDependency.mProviders;
        final Lazy<ManagedProfileController> lazy25 = createDependency.mManagedProfileController;
        Objects.requireNonNull(lazy25);
        arrayMap25.put(ManagedProfileController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy25.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap26 = createDependency.mProviders;
        final Lazy<NextAlarmController> lazy26 = createDependency.mNextAlarmController;
        Objects.requireNonNull(lazy26);
        arrayMap26.put(NextAlarmController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy26.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap27 = createDependency.mProviders;
        final Lazy<DataSaverController> lazy27 = createDependency.mDataSaverController;
        Objects.requireNonNull(lazy27);
        arrayMap27.put(DataSaverController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy27.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap28 = createDependency.mProviders;
        final Lazy<AccessibilityController> lazy28 = createDependency.mAccessibilityController;
        Objects.requireNonNull(lazy28);
        arrayMap28.put(AccessibilityController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda27
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy28.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap29 = createDependency.mProviders;
        final Lazy<DeviceProvisionedController> lazy29 = createDependency.mDeviceProvisionedController;
        Objects.requireNonNull(lazy29);
        arrayMap29.put(DeviceProvisionedController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy29.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap30 = createDependency.mProviders;
        final Lazy<PluginManager> lazy30 = createDependency.mPluginManager;
        Objects.requireNonNull(lazy30);
        arrayMap30.put(PluginManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy30.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap31 = createDependency.mProviders;
        final Lazy<AssistManager> lazy31 = createDependency.mAssistManager;
        Objects.requireNonNull(lazy31);
        arrayMap31.put(AssistManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy31.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap32 = createDependency.mProviders;
        final Lazy<SecurityController> lazy32 = createDependency.mSecurityController;
        Objects.requireNonNull(lazy32);
        arrayMap32.put(SecurityController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy32.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap33 = createDependency.mProviders;
        final Lazy<LeakDetector> lazy33 = createDependency.mLeakDetector;
        Objects.requireNonNull(lazy33);
        arrayMap33.put(LeakDetector.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy33.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap34 = createDependency.mProviders;
        Dependency.DependencyKey<String> dependencyKey7 = Dependency.LEAK_REPORT_EMAIL;
        final Lazy<String> lazy34 = createDependency.mLeakReportEmail;
        Objects.requireNonNull(lazy34);
        arrayMap34.put(dependencyKey7, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy34.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap35 = createDependency.mProviders;
        final Lazy<LeakReporter> lazy35 = createDependency.mLeakReporter;
        Objects.requireNonNull(lazy35);
        arrayMap35.put(LeakReporter.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy35.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap36 = createDependency.mProviders;
        final Lazy<GarbageMonitor> lazy36 = createDependency.mGarbageMonitor;
        Objects.requireNonNull(lazy36);
        arrayMap36.put(GarbageMonitor.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy36.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap37 = createDependency.mProviders;
        final Lazy<TunerService> lazy37 = createDependency.mTunerService;
        Objects.requireNonNull(lazy37);
        arrayMap37.put(TunerService.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy37.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap38 = createDependency.mProviders;
        final Lazy<NotificationShadeWindowController> lazy38 = createDependency.mNotificationShadeWindowController;
        Objects.requireNonNull(lazy38);
        arrayMap38.put(NotificationShadeWindowController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda28
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy38.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap39 = createDependency.mProviders;
        final Lazy<StatusBarWindowController> lazy39 = createDependency.mTempStatusBarWindowController;
        Objects.requireNonNull(lazy39);
        arrayMap39.put(StatusBarWindowController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy39.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap40 = createDependency.mProviders;
        final Lazy<DarkIconDispatcher> lazy40 = createDependency.mDarkIconDispatcher;
        Objects.requireNonNull(lazy40);
        arrayMap40.put(DarkIconDispatcher.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy40.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap41 = createDependency.mProviders;
        final Lazy<ConfigurationController> lazy41 = createDependency.mConfigurationController;
        Objects.requireNonNull(lazy41);
        arrayMap41.put(ConfigurationController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy41.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap42 = createDependency.mProviders;
        final Lazy<StatusBarIconController> lazy42 = createDependency.mStatusBarIconController;
        Objects.requireNonNull(lazy42);
        arrayMap42.put(StatusBarIconController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda1
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy42.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap43 = createDependency.mProviders;
        final Lazy<ScreenLifecycle> lazy43 = createDependency.mScreenLifecycle;
        Objects.requireNonNull(lazy43);
        arrayMap43.put(ScreenLifecycle.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda2
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy43.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap44 = createDependency.mProviders;
        final Lazy<WakefulnessLifecycle> lazy44 = createDependency.mWakefulnessLifecycle;
        Objects.requireNonNull(lazy44);
        arrayMap44.put(WakefulnessLifecycle.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda3
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy44.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap45 = createDependency.mProviders;
        final Lazy<FragmentService> lazy45 = createDependency.mFragmentService;
        Objects.requireNonNull(lazy45);
        arrayMap45.put(FragmentService.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda4
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy45.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap46 = createDependency.mProviders;
        final Lazy<ExtensionController> lazy46 = createDependency.mExtensionController;
        Objects.requireNonNull(lazy46);
        arrayMap46.put(ExtensionController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda5
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy46.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap47 = createDependency.mProviders;
        final Lazy<PluginDependencyProvider> lazy47 = createDependency.mPluginDependencyProvider;
        Objects.requireNonNull(lazy47);
        arrayMap47.put(PluginDependencyProvider.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda6
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy47.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap48 = createDependency.mProviders;
        final Lazy<LocalBluetoothManager> lazy48 = createDependency.mLocalBluetoothManager;
        Objects.requireNonNull(lazy48);
        arrayMap48.put(LocalBluetoothManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda7
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy48.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap49 = createDependency.mProviders;
        final Lazy<VolumeDialogController> lazy49 = createDependency.mVolumeDialogController;
        Objects.requireNonNull(lazy49);
        arrayMap49.put(VolumeDialogController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda8
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy49.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap50 = createDependency.mProviders;
        final Lazy<MetricsLogger> lazy50 = createDependency.mMetricsLogger;
        Objects.requireNonNull(lazy50);
        arrayMap50.put(MetricsLogger.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda9
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy50.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap51 = createDependency.mProviders;
        final Lazy<AccessibilityManagerWrapper> lazy51 = createDependency.mAccessibilityManagerWrapper;
        Objects.requireNonNull(lazy51);
        arrayMap51.put(AccessibilityManagerWrapper.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy51.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap52 = createDependency.mProviders;
        final Lazy<SysuiColorExtractor> lazy52 = createDependency.mSysuiColorExtractor;
        Objects.requireNonNull(lazy52);
        arrayMap52.put(SysuiColorExtractor.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda11
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy52.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap53 = createDependency.mProviders;
        final Lazy<TunablePadding.TunablePaddingService> lazy53 = createDependency.mTunablePaddingService;
        Objects.requireNonNull(lazy53);
        arrayMap53.put(TunablePadding.TunablePaddingService.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda13
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy53.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap54 = createDependency.mProviders;
        final Lazy<ForegroundServiceController> lazy54 = createDependency.mForegroundServiceController;
        Objects.requireNonNull(lazy54);
        arrayMap54.put(ForegroundServiceController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda14
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy54.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap55 = createDependency.mProviders;
        final Lazy<UiOffloadThread> lazy55 = createDependency.mUiOffloadThread;
        Objects.requireNonNull(lazy55);
        arrayMap55.put(UiOffloadThread.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda15
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy55.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap56 = createDependency.mProviders;
        final Lazy<PowerUI.WarningsUI> lazy56 = createDependency.mWarningsUI;
        Objects.requireNonNull(lazy56);
        arrayMap56.put(PowerUI.WarningsUI.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda16
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy56.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap57 = createDependency.mProviders;
        final Lazy<LightBarController> lazy57 = createDependency.mLightBarController;
        Objects.requireNonNull(lazy57);
        arrayMap57.put(LightBarController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda17
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy57.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap58 = createDependency.mProviders;
        final Lazy<IWindowManager> lazy58 = createDependency.mIWindowManager;
        Objects.requireNonNull(lazy58);
        arrayMap58.put(IWindowManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda18
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy58.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap59 = createDependency.mProviders;
        final Lazy<OverviewProxyService> lazy59 = createDependency.mOverviewProxyService;
        Objects.requireNonNull(lazy59);
        arrayMap59.put(OverviewProxyService.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda19
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy59.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap60 = createDependency.mProviders;
        final Lazy<NavigationModeController> lazy60 = createDependency.mNavBarModeController;
        Objects.requireNonNull(lazy60);
        arrayMap60.put(NavigationModeController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda20
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy60.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap61 = createDependency.mProviders;
        final Lazy<AccessibilityButtonModeObserver> lazy61 = createDependency.mAccessibilityButtonModeObserver;
        Objects.requireNonNull(lazy61);
        arrayMap61.put(AccessibilityButtonModeObserver.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy61.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap62 = createDependency.mProviders;
        final Lazy<AccessibilityButtonTargetsObserver> lazy62 = createDependency.mAccessibilityButtonListController;
        Objects.requireNonNull(lazy62);
        arrayMap62.put(AccessibilityButtonTargetsObserver.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda22
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy62.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap63 = createDependency.mProviders;
        final Lazy<EnhancedEstimates> lazy63 = createDependency.mEnhancedEstimates;
        Objects.requireNonNull(lazy63);
        arrayMap63.put(EnhancedEstimates.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda23
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy63.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap64 = createDependency.mProviders;
        final Lazy<VibratorHelper> lazy64 = createDependency.mVibratorHelper;
        Objects.requireNonNull(lazy64);
        arrayMap64.put(VibratorHelper.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda24
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy64.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap65 = createDependency.mProviders;
        final Lazy<IStatusBarService> lazy65 = createDependency.mIStatusBarService;
        Objects.requireNonNull(lazy65);
        arrayMap65.put(IStatusBarService.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda25
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy65.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap66 = createDependency.mProviders;
        final Lazy<DisplayMetrics> lazy66 = createDependency.mDisplayMetrics;
        Objects.requireNonNull(lazy66);
        arrayMap66.put(DisplayMetrics.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy66.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap67 = createDependency.mProviders;
        final Lazy<LockscreenGestureLogger> lazy67 = createDependency.mLockscreenGestureLogger;
        Objects.requireNonNull(lazy67);
        arrayMap67.put(LockscreenGestureLogger.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy67.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap68 = createDependency.mProviders;
        final Lazy<NotificationEntryManager.KeyguardEnvironment> lazy68 = createDependency.mKeyguardEnvironment;
        Objects.requireNonNull(lazy68);
        arrayMap68.put(NotificationEntryManager.KeyguardEnvironment.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy68.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap69 = createDependency.mProviders;
        final Lazy<ShadeController> lazy69 = createDependency.mShadeController;
        Objects.requireNonNull(lazy69);
        arrayMap69.put(ShadeController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy69.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap70 = createDependency.mProviders;
        final Lazy<NotificationRemoteInputManager.Callback> lazy70 = createDependency.mNotificationRemoteInputManagerCallback;
        Objects.requireNonNull(lazy70);
        arrayMap70.put(NotificationRemoteInputManager.Callback.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy70.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap71 = createDependency.mProviders;
        final Lazy<AppOpsController> lazy71 = createDependency.mAppOpsController;
        Objects.requireNonNull(lazy71);
        arrayMap71.put(AppOpsController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy71.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap72 = createDependency.mProviders;
        final Lazy<NavigationBarController> lazy72 = createDependency.mNavigationBarController;
        Objects.requireNonNull(lazy72);
        arrayMap72.put(NavigationBarController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy72.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap73 = createDependency.mProviders;
        final Lazy<AccessibilityFloatingMenuController> lazy73 = createDependency.mAccessibilityFloatingMenuController;
        Objects.requireNonNull(lazy73);
        arrayMap73.put(AccessibilityFloatingMenuController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy73.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap74 = createDependency.mProviders;
        final Lazy<StatusBarStateController> lazy74 = createDependency.mStatusBarStateController;
        Objects.requireNonNull(lazy74);
        arrayMap74.put(StatusBarStateController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy74.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap75 = createDependency.mProviders;
        final Lazy<NotificationLockscreenUserManager> lazy75 = createDependency.mNotificationLockscreenUserManager;
        Objects.requireNonNull(lazy75);
        arrayMap75.put(NotificationLockscreenUserManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy75.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap76 = createDependency.mProviders;
        final Lazy<VisualStabilityManager> lazy76 = createDependency.mVisualStabilityManager;
        Objects.requireNonNull(lazy76);
        arrayMap76.put(VisualStabilityManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy76.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap77 = createDependency.mProviders;
        final Lazy<NotificationGroupManagerLegacy> lazy77 = createDependency.mNotificationGroupManager;
        Objects.requireNonNull(lazy77);
        arrayMap77.put(NotificationGroupManagerLegacy.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy77.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap78 = createDependency.mProviders;
        final Lazy<NotificationGroupAlertTransferHelper> lazy78 = createDependency.mNotificationGroupAlertTransferHelper;
        Objects.requireNonNull(lazy78);
        arrayMap78.put(NotificationGroupAlertTransferHelper.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy78.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap79 = createDependency.mProviders;
        final Lazy<NotificationMediaManager> lazy79 = createDependency.mNotificationMediaManager;
        Objects.requireNonNull(lazy79);
        arrayMap79.put(NotificationMediaManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy79.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap80 = createDependency.mProviders;
        final Lazy<NotificationGutsManager> lazy80 = createDependency.mNotificationGutsManager;
        Objects.requireNonNull(lazy80);
        arrayMap80.put(NotificationGutsManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy80.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap81 = createDependency.mProviders;
        final Lazy<NotificationRemoteInputManager> lazy81 = createDependency.mNotificationRemoteInputManager;
        Objects.requireNonNull(lazy81);
        arrayMap81.put(NotificationRemoteInputManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy81.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap82 = createDependency.mProviders;
        final Lazy<SmartReplyConstants> lazy82 = createDependency.mSmartReplyConstants;
        Objects.requireNonNull(lazy82);
        arrayMap82.put(SmartReplyConstants.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy82.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap83 = createDependency.mProviders;
        final Lazy<NotificationListener> lazy83 = createDependency.mNotificationListener;
        Objects.requireNonNull(lazy83);
        arrayMap83.put(NotificationListener.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy83.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap84 = createDependency.mProviders;
        final Lazy<NotificationLogger> lazy84 = createDependency.mNotificationLogger;
        Objects.requireNonNull(lazy84);
        arrayMap84.put(NotificationLogger.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy84.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap85 = createDependency.mProviders;
        final Lazy<NotificationViewHierarchyManager> lazy85 = createDependency.mNotificationViewHierarchyManager;
        Objects.requireNonNull(lazy85);
        arrayMap85.put(NotificationViewHierarchyManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy85.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap86 = createDependency.mProviders;
        final Lazy<NotificationFilter> lazy86 = createDependency.mNotificationFilter;
        Objects.requireNonNull(lazy86);
        arrayMap86.put(NotificationFilter.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy86.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap87 = createDependency.mProviders;
        final Lazy<KeyguardDismissUtil> lazy87 = createDependency.mKeyguardDismissUtil;
        Objects.requireNonNull(lazy87);
        arrayMap87.put(KeyguardDismissUtil.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy87.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap88 = createDependency.mProviders;
        final Lazy<SmartReplyController> lazy88 = createDependency.mSmartReplyController;
        Objects.requireNonNull(lazy88);
        arrayMap88.put(SmartReplyController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy88.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap89 = createDependency.mProviders;
        final Lazy<RemoteInputQuickSettingsDisabler> lazy89 = createDependency.mRemoteInputQuickSettingsDisabler;
        Objects.requireNonNull(lazy89);
        arrayMap89.put(RemoteInputQuickSettingsDisabler.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy89.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap90 = createDependency.mProviders;
        final Lazy<NotificationEntryManager> lazy90 = createDependency.mNotificationEntryManager;
        Objects.requireNonNull(lazy90);
        arrayMap90.put(NotificationEntryManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy90.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap91 = createDependency.mProviders;
        final Lazy<ForegroundServiceNotificationListener> lazy91 = createDependency.mForegroundServiceNotificationListener;
        Objects.requireNonNull(lazy91);
        arrayMap91.put(ForegroundServiceNotificationListener.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy91.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap92 = createDependency.mProviders;
        final Lazy<ClockManager> lazy92 = createDependency.mClockManager;
        Objects.requireNonNull(lazy92);
        arrayMap92.put(ClockManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy92.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap93 = createDependency.mProviders;
        final Lazy<PrivacyItemController> lazy93 = createDependency.mPrivacyItemController;
        Objects.requireNonNull(lazy93);
        arrayMap93.put(PrivacyItemController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda12
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy93.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap94 = createDependency.mProviders;
        final Lazy<ActivityManagerWrapper> lazy94 = createDependency.mActivityManagerWrapper;
        Objects.requireNonNull(lazy94);
        arrayMap94.put(ActivityManagerWrapper.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy94.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap95 = createDependency.mProviders;
        final Lazy<DevicePolicyManagerWrapper> lazy95 = createDependency.mDevicePolicyManagerWrapper;
        Objects.requireNonNull(lazy95);
        arrayMap95.put(DevicePolicyManagerWrapper.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda1
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy95.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap96 = createDependency.mProviders;
        final Lazy<PackageManagerWrapper> lazy96 = createDependency.mPackageManagerWrapper;
        Objects.requireNonNull(lazy96);
        arrayMap96.put(PackageManagerWrapper.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda2
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy96.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap97 = createDependency.mProviders;
        final Lazy<SensorPrivacyController> lazy97 = createDependency.mSensorPrivacyController;
        Objects.requireNonNull(lazy97);
        arrayMap97.put(SensorPrivacyController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda3
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy97.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap98 = createDependency.mProviders;
        final Lazy<DockManager> lazy98 = createDependency.mDockManager;
        Objects.requireNonNull(lazy98);
        arrayMap98.put(DockManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda4
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy98.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap99 = createDependency.mProviders;
        final Lazy<INotificationManager> lazy99 = createDependency.mINotificationManager;
        Objects.requireNonNull(lazy99);
        arrayMap99.put(INotificationManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda5
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy99.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap100 = createDependency.mProviders;
        final Lazy<SysUiState> lazy100 = createDependency.mSysUiStateFlagsContainer;
        Objects.requireNonNull(lazy100);
        arrayMap100.put(SysUiState.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda6
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy100.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap101 = createDependency.mProviders;
        final Lazy<AlarmManager> lazy101 = createDependency.mAlarmManager;
        Objects.requireNonNull(lazy101);
        arrayMap101.put(AlarmManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda1
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy101.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap102 = createDependency.mProviders;
        final Lazy<KeyguardSecurityModel> lazy102 = createDependency.mKeyguardSecurityModel;
        Objects.requireNonNull(lazy102);
        arrayMap102.put(KeyguardSecurityModel.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda2
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy102.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap103 = createDependency.mProviders;
        final Lazy<DozeParameters> lazy103 = createDependency.mDozeParameters;
        Objects.requireNonNull(lazy103);
        arrayMap103.put(DozeParameters.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda3
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy103.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap104 = createDependency.mProviders;
        final Lazy<IWallpaperManager> lazy104 = createDependency.mWallpaperManager;
        Objects.requireNonNull(lazy104);
        arrayMap104.put(IWallpaperManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda4
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy104.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap105 = createDependency.mProviders;
        final Lazy<CommandQueue> lazy105 = createDependency.mCommandQueue;
        Objects.requireNonNull(lazy105);
        arrayMap105.put(CommandQueue.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda5
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy105.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap106 = createDependency.mProviders;
        final Lazy<ProtoTracer> lazy106 = createDependency.mProtoTracer;
        Objects.requireNonNull(lazy106);
        arrayMap106.put(ProtoTracer.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda6
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy106.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap107 = createDependency.mProviders;
        final Lazy<DeviceConfigProxy> lazy107 = createDependency.mDeviceConfigProxy;
        Objects.requireNonNull(lazy107);
        arrayMap107.put(DeviceConfigProxy.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda7
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    case 1:
                    default:
                        return lazy107.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap108 = createDependency.mProviders;
        final Lazy<TelephonyListenerManager> lazy108 = createDependency.mTelephonyListenerManager;
        Objects.requireNonNull(lazy108);
        arrayMap108.put(TelephonyListenerManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda8
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy108.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap109 = createDependency.mProviders;
        final Lazy<AutoHideController> lazy109 = createDependency.mAutoHideController;
        Objects.requireNonNull(lazy109);
        arrayMap109.put(AutoHideController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda9
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy109.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap110 = createDependency.mProviders;
        final Lazy<RecordingController> lazy110 = createDependency.mRecordingController;
        Objects.requireNonNull(lazy110);
        arrayMap110.put(RecordingController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda10
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy110.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap111 = createDependency.mProviders;
        final Lazy<MediaOutputDialogFactory> lazy111 = createDependency.mMediaOutputDialogFactory;
        Objects.requireNonNull(lazy111);
        arrayMap111.put(MediaOutputDialogFactory.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda11
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy111.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap112 = createDependency.mProviders;
        final Lazy<NavigationBarOverlayController> lazy112 = createDependency.mNavbarButtonsControllerLazy;
        Objects.requireNonNull(lazy112);
        arrayMap112.put(NavigationBarOverlayController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda13
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy112.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap113 = createDependency.mProviders;
        final Lazy<SystemStatusAnimationScheduler> lazy113 = createDependency.mSystemStatusAnimationSchedulerLazy;
        Objects.requireNonNull(lazy113);
        arrayMap113.put(SystemStatusAnimationScheduler.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda14
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy113.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap114 = createDependency.mProviders;
        final Lazy<PrivacyDotViewController> lazy114 = createDependency.mPrivacyDotViewControllerLazy;
        Objects.requireNonNull(lazy114);
        arrayMap114.put(PrivacyDotViewController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda15
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy114.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap115 = createDependency.mProviders;
        final Lazy<InternetDialogFactory> lazy115 = createDependency.mInternetDialogFactory;
        Objects.requireNonNull(lazy115);
        arrayMap115.put(InternetDialogFactory.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda16
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy115.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap116 = createDependency.mProviders;
        final Lazy<EdgeBackGestureHandler.Factory> lazy116 = createDependency.mEdgeBackGestureHandlerFactoryLazy;
        Objects.requireNonNull(lazy116);
        arrayMap116.put(EdgeBackGestureHandler.Factory.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda17
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy116.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap117 = createDependency.mProviders;
        final Lazy<UiEventLogger> lazy117 = createDependency.mUiEventLogger;
        Objects.requireNonNull(lazy117);
        arrayMap117.put(UiEventLogger.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda18
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy117.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap118 = createDependency.mProviders;
        final Lazy<FeatureFlags> lazy118 = createDependency.mFeatureFlagsLazy;
        Objects.requireNonNull(lazy118);
        arrayMap118.put(FeatureFlags.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda19
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy118.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap119 = createDependency.mProviders;
        final Lazy<StatusBarContentInsetsProvider> lazy119 = createDependency.mContentInsetsProviderLazy;
        Objects.requireNonNull(lazy119);
        arrayMap119.put(StatusBarContentInsetsProvider.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda20
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy119.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap120 = createDependency.mProviders;
        final Lazy<NotificationSectionsManager> lazy120 = createDependency.mNotificationSectionsManagerLazy;
        Objects.requireNonNull(lazy120);
        arrayMap120.put(NotificationSectionsManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda21
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy120.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap121 = createDependency.mProviders;
        final Lazy<ScreenOffAnimationController> lazy121 = createDependency.mScreenOffAnimationController;
        Objects.requireNonNull(lazy121);
        arrayMap121.put(ScreenOffAnimationController.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda22
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy121.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap122 = createDependency.mProviders;
        final Lazy<AmbientState> lazy122 = createDependency.mAmbientStateLazy;
        Objects.requireNonNull(lazy122);
        arrayMap122.put(AmbientState.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda23
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy122.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap123 = createDependency.mProviders;
        final Lazy<GroupMembershipManager> lazy123 = createDependency.mGroupMembershipManagerLazy;
        Objects.requireNonNull(lazy123);
        arrayMap123.put(GroupMembershipManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda24
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy123.get();
                }
            }
        });
        ArrayMap<Object, Dependency.LazyDependencyCreator> arrayMap124 = createDependency.mProviders;
        final Lazy<GroupExpansionManager> lazy124 = createDependency.mGroupExpansionManagerLazy;
        Objects.requireNonNull(lazy124);
        arrayMap124.put(GroupExpansionManager.class, new Dependency.LazyDependencyCreator() { // from class: com.android.systemui.Dependency$$ExternalSyntheticLambda25
            @Override // com.android.systemui.Dependency.LazyDependencyCreator
            public final Object createDependency() {
                switch (r2) {
                    case 0:
                    default:
                        return lazy124.get();
                }
            }
        });
        Dependency.setInstance(createDependency);
    }

    @VisibleForTesting
    public static void createFromConfig(Context context, boolean z) {
        if (mFactory == null) {
            String string = context.getString(2131952147);
            if (string == null || string.length() == 0) {
                throw new RuntimeException("No SystemUIFactory component configured");
            }
            try {
                SystemUIFactory systemUIFactory = (SystemUIFactory) context.getClassLoader().loadClass(string).newInstance();
                mFactory = systemUIFactory;
                systemUIFactory.init(context, z);
            } catch (Throwable th) {
                Log.w("SystemUIFactory", "Error creating SystemUIFactory component: " + string, th);
                throw new RuntimeException(th);
            }
        }
    }

    public R$styleable createBackGestureTfClassifierProvider(AssetManager assetManager, String str) {
        return new R$styleable();
    }

    public ScreenshotNotificationSmartActionsProvider createScreenshotNotificationSmartActionsProvider(Context context, Executor executor, Handler handler) {
        return new ScreenshotNotificationSmartActionsProvider();
    }

    public GlobalRootComponent buildGlobalRootComponent(Context context) {
        return DaggerGlobalRootComponent.builder().mo119context(context).build();
    }
}
