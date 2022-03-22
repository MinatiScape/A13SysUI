package com.android.systemui.navigationbar;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.IActivityTaskManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.telecom.TelecomManager;
import android.util.Log;
import android.util.MathUtils;
import android.view.Display;
import android.view.IWindowManager;
import android.view.InsetsState;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import androidx.leanback.R$color;
import com.android.internal.accessibility.dialog.AccessibilityButtonChooserActivity;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.view.RotationPolicy;
import com.android.systemui.Dependency;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda1;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUIComponent$$ExternalSyntheticLambda1;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavBarHelper;
import com.android.systemui.navigationbar.NavigationBarTransitions;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.navigationbar.buttons.ButtonInterface;
import com.android.systemui.navigationbar.buttons.KeyButtonView;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.navigationbar.gestural.QuickswitchOrientedNavHandle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.Recents;
import com.android.systemui.shared.navigationbar.RegionSamplingHelper;
import com.android.systemui.shared.recents.utilities.Utilities;
import com.android.systemui.shared.rotation.FloatingRotationButton;
import com.android.systemui.shared.rotation.FloatingRotationButtonPositionCalculator;
import com.android.systemui.shared.rotation.RotationButtonController;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.statusbar.AutoHideUiElement;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.BarTransitions;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.util.Utils;
import com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda0;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda0;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda10;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda6;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda8;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda19;
import com.android.wm.shell.dagger.WMShellBaseModule$$ExternalSyntheticLambda3;
import com.android.wm.shell.dagger.WMShellBaseModule$$ExternalSyntheticLambda4;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda4;
import dagger.Lazy;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class NavigationBar implements View.OnAttachStateChangeListener, CommandQueue.Callbacks, NavigationModeController.ModeChangedListener {
    public final AccessibilityManager mAccessibilityManager;
    public boolean mAllowForceNavBarHandleOpaque;
    public int mAppearance;
    public final Lazy<AssistManager> mAssistManagerLazy;
    public final ScreenDecorations$$ExternalSyntheticLambda1 mAutoDim;
    public AutoHideController mAutoHideController;
    public final AutoHideController.Factory mAutoHideControllerFactory;
    public final AnonymousClass1 mAutoHideUiElement;
    public final Optional<BackAnimation> mBackAnimation;
    public int mBehavior;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final AnonymousClass8 mBroadcastReceiver;
    public final CommandQueue mCommandQueue;
    public final Context mContext;
    public int mCurrentRotation;
    public final AnonymousClass7 mDepthListener;
    public final DeviceProvisionedController mDeviceProvisionedController;
    public int mDisabledFlags1;
    public int mDisabledFlags2;
    public int mDisplayId;
    public final StandardWifiEntry$$ExternalSyntheticLambda0 mEnableLayoutTransitions;
    public boolean mForceNavBarHandleOpaque;
    public NavigationBarFrame mFrame;
    public final Handler mHandler;
    public boolean mHomeBlockedThisTouch;
    public Optional<Long> mHomeButtonLongPressDurationMs;
    public final InputMethodManager mInputMethodManager;
    public boolean mIsCurrentUserSetup;
    public boolean mIsOnDefaultDisplay;
    public long mLastLockToAppLongPress;
    public int mLayoutDirection;
    public LightBarController mLightBarController;
    public final LightBarController.Factory mLightBarControllerFactory;
    public Locale mLocale;
    public boolean mLongPressHomeEnabled;
    public final AutoHideController mMainAutoHideController;
    public final LightBarController mMainLightBarController;
    public final MetricsLogger mMetricsLogger;
    public final NavBarHelper mNavBarHelper;
    public int mNavBarMode;
    public final NavigationBarOverlayController mNavbarOverlayController;
    public final AnonymousClass2 mNavbarTaskbarStateUpdater;
    public int mNavigationBarMode;
    public NavigationBarView mNavigationBarView;
    public int mNavigationBarWindowState;
    public int mNavigationIconHints;
    public final NavigationModeController mNavigationModeController;
    public final NotificationRemoteInputManager mNotificationRemoteInputManager;
    public final NotificationShadeDepthController mNotificationShadeDepthController;
    public final AnonymousClass5 mOnPropertiesChangedListener;
    public final BubbleStackView$$ExternalSyntheticLambda19 mOnVariableDurationHomeLongClick;
    public QuickswitchOrientedNavHandle mOrientationHandle;
    public NavigationBar$$ExternalSyntheticLambda10 mOrientationHandleGlobalLayoutListener;
    public AnonymousClass4 mOrientationHandleIntensityListener;
    public WindowManager.LayoutParams mOrientationParams;
    public final AnonymousClass3 mOverviewProxyListener;
    public final OverviewProxyService mOverviewProxyService;
    public final Optional<Pip> mPipOptional;
    public final Optional<Recents> mRecentsOptional;
    public final NavigationBar$$ExternalSyntheticLambda13 mRotationWatcher;
    public Bundle mSavedState;
    public final ShadeController mShadeController;
    public boolean mShowOrientedHandleForImmersiveMode;
    public final Optional<LegacySplitScreen> mSplitScreenOptional;
    public int mStartingQuickSwitchRotation;
    public final Lazy<Optional<StatusBar>> mStatusBarOptionalLazy;
    public final StatusBarStateController mStatusBarStateController;
    public final SysUiState mSysUiFlagsContainer;
    public final Optional<TelecomManager> mTelecomManagerOptional;
    public boolean mTransientShown;
    public boolean mTransientShownFromGestureOnSystemBar;
    public final UiEventLogger mUiEventLogger;
    public final AnonymousClass6 mUserSetupListener;
    public final WindowManager mWindowManager;

    /* renamed from: com.android.systemui.navigationbar.NavigationBar$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements NavBarHelper.NavbarTaskbarStateUpdater {
        public AnonymousClass2() {
        }

        @Override // com.android.systemui.navigationbar.NavBarHelper.NavbarTaskbarStateUpdater
        public final void updateAccessibilityServicesState() {
            NavigationBar.this.updateAccessibilityStateFlags();
        }

        @Override // com.android.systemui.navigationbar.NavBarHelper.NavbarTaskbarStateUpdater
        public final void updateAssistantAvailable(boolean z) {
            NavigationBar navigationBar = NavigationBar.this;
            if (navigationBar.mNavigationBarView != null) {
                NavBarHelper navBarHelper = navigationBar.mNavBarHelper;
                Objects.requireNonNull(navBarHelper);
                navigationBar.mLongPressHomeEnabled = navBarHelper.mLongPressHomeEnabled;
                NavigationBar navigationBar2 = NavigationBar.this;
                Objects.requireNonNull(navigationBar2);
                OverviewProxyService overviewProxyService = navigationBar2.mOverviewProxyService;
                Objects.requireNonNull(overviewProxyService);
                if (overviewProxyService.mOverviewProxy != null) {
                    try {
                        OverviewProxyService overviewProxyService2 = navigationBar2.mOverviewProxyService;
                        Objects.requireNonNull(overviewProxyService2);
                        overviewProxyService2.mOverviewProxy.onAssistantAvailable(z);
                    } catch (RemoteException unused) {
                        Log.w("NavigationBar", "Unable to send assistant availability data to launcher");
                    }
                }
                navigationBar2.reconfigureHomeLongClick();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class Factory {
        public final AccessibilityButtonModeObserver mAccessibilityButtonModeObserver;
        public final AccessibilityManager mAccessibilityManager;
        public final Lazy<AssistManager> mAssistManagerLazy;
        public final AutoHideController.Factory mAutoHideControllerFactory;
        public final Optional<BackAnimation> mBackAnimation;
        public final BroadcastDispatcher mBroadcastDispatcher;
        public final CommandQueue mCommandQueue;
        public final DeviceProvisionedController mDeviceProvisionedController;
        public final InputMethodManager mInputMethodManager;
        public final LightBarController.Factory mLightBarControllerFactory;
        public final AutoHideController mMainAutoHideController;
        public final Handler mMainHandler;
        public final LightBarController mMainLightBarController;
        public final MetricsLogger mMetricsLogger;
        public final NavBarHelper mNavBarHelper;
        public final NavigationBarOverlayController mNavbarOverlayController;
        public final NavigationModeController mNavigationModeController;
        public final NotificationRemoteInputManager mNotificationRemoteInputManager;
        public final NotificationShadeDepthController mNotificationShadeDepthController;
        public final OverviewProxyService mOverviewProxyService;
        public final Optional<Pip> mPipOptional;
        public final Optional<Recents> mRecentsOptional;
        public final ShadeController mShadeController;
        public final Optional<LegacySplitScreen> mSplitScreenOptional;
        public final Lazy<Optional<StatusBar>> mStatusBarOptionalLazy;
        public final StatusBarStateController mStatusBarStateController;
        public final SysUiState mSysUiFlagsContainer;
        public final Optional<TelecomManager> mTelecomManagerOptional;
        public final UiEventLogger mUiEventLogger;

        public Factory(Lazy<AssistManager> lazy, AccessibilityManager accessibilityManager, DeviceProvisionedController deviceProvisionedController, MetricsLogger metricsLogger, OverviewProxyService overviewProxyService, NavigationModeController navigationModeController, AccessibilityButtonModeObserver accessibilityButtonModeObserver, StatusBarStateController statusBarStateController, SysUiState sysUiState, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<Recents> optional3, Lazy<Optional<StatusBar>> lazy2, ShadeController shadeController, NotificationRemoteInputManager notificationRemoteInputManager, NotificationShadeDepthController notificationShadeDepthController, Handler handler, NavigationBarOverlayController navigationBarOverlayController, UiEventLogger uiEventLogger, NavBarHelper navBarHelper, LightBarController lightBarController, LightBarController.Factory factory, AutoHideController autoHideController, AutoHideController.Factory factory2, Optional<TelecomManager> optional4, InputMethodManager inputMethodManager, Optional<BackAnimation> optional5) {
            this.mAssistManagerLazy = lazy;
            this.mAccessibilityManager = accessibilityManager;
            this.mDeviceProvisionedController = deviceProvisionedController;
            this.mMetricsLogger = metricsLogger;
            this.mOverviewProxyService = overviewProxyService;
            this.mNavigationModeController = navigationModeController;
            this.mAccessibilityButtonModeObserver = accessibilityButtonModeObserver;
            this.mStatusBarStateController = statusBarStateController;
            this.mSysUiFlagsContainer = sysUiState;
            this.mBroadcastDispatcher = broadcastDispatcher;
            this.mCommandQueue = commandQueue;
            this.mPipOptional = optional;
            this.mSplitScreenOptional = optional2;
            this.mRecentsOptional = optional3;
            this.mStatusBarOptionalLazy = lazy2;
            this.mShadeController = shadeController;
            this.mNotificationRemoteInputManager = notificationRemoteInputManager;
            this.mNotificationShadeDepthController = notificationShadeDepthController;
            this.mMainHandler = handler;
            this.mNavbarOverlayController = navigationBarOverlayController;
            this.mUiEventLogger = uiEventLogger;
            this.mNavBarHelper = navBarHelper;
            this.mMainLightBarController = lightBarController;
            this.mLightBarControllerFactory = factory;
            this.mMainAutoHideController = autoHideController;
            this.mAutoHideControllerFactory = factory2;
            this.mTelecomManagerOptional = optional4;
            this.mInputMethodManager = inputMethodManager;
            this.mBackAnimation = optional5;
        }
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier removed */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public static final class NavBarActionEvent extends Enum<NavBarActionEvent> implements UiEventLogger.UiEventEnum {
        public static final /* synthetic */ NavBarActionEvent[] $VALUES;
        public static final NavBarActionEvent NAVBAR_ASSIST_LONGPRESS;
        private final int mId = 550;

        static {
            NavBarActionEvent navBarActionEvent = new NavBarActionEvent();
            NAVBAR_ASSIST_LONGPRESS = navBarActionEvent;
            $VALUES = new NavBarActionEvent[]{navBarActionEvent};
        }

        public static NavBarActionEvent valueOf(String str) {
            return (NavBarActionEvent) Enum.valueOf(NavBarActionEvent.class, str);
        }

        public static NavBarActionEvent[] values() {
            return (NavBarActionEvent[]) $VALUES.clone();
        }

        public final int getId() {
            return this.mId;
        }
    }

    public NavigationBar() {
        throw null;
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.navigationbar.NavigationBar$8] */
    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.systemui.navigationbar.NavigationBar$1] */
    /* JADX WARN: Type inference failed for: r3v10, types: [com.android.systemui.navigationbar.NavigationBar$7] */
    /* JADX WARN: Type inference failed for: r3v3, types: [com.android.systemui.navigationbar.NavigationBar$3] */
    /* JADX WARN: Type inference failed for: r3v4, types: [com.android.systemui.navigationbar.NavigationBar$4] */
    /* JADX WARN: Type inference failed for: r3v9, types: [com.android.systemui.navigationbar.NavigationBar$6] */
    public NavigationBar(Context context, WindowManager windowManager, Lazy lazy, AccessibilityManager accessibilityManager, DeviceProvisionedController deviceProvisionedController, MetricsLogger metricsLogger, OverviewProxyService overviewProxyService, NavigationModeController navigationModeController, StatusBarStateController statusBarStateController, SysUiState sysUiState, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, Optional optional, Optional optional2, Optional optional3, Lazy lazy2, ShadeController shadeController, NotificationRemoteInputManager notificationRemoteInputManager, NotificationShadeDepthController notificationShadeDepthController, Handler handler, NavigationBarOverlayController navigationBarOverlayController, UiEventLogger uiEventLogger, NavBarHelper navBarHelper, LightBarController lightBarController, LightBarController.Factory factory, AutoHideController autoHideController, AutoHideController.Factory factory2, Optional optional4, InputMethodManager inputMethodManager, Optional optional5) {
        this.mNavigationBarWindowState = 0;
        this.mNavigationIconHints = 0;
        this.mNavBarMode = 0;
        this.mStartingQuickSwitchRotation = -1;
        this.mAutoHideUiElement = new AutoHideUiElement() { // from class: com.android.systemui.navigationbar.NavigationBar.1
            @Override // com.android.systemui.statusbar.AutoHideUiElement
            public final void hide() {
                NavigationBar navigationBar = NavigationBar.this;
                Objects.requireNonNull(navigationBar);
                if (navigationBar.mTransientShown) {
                    navigationBar.mTransientShown = false;
                    navigationBar.mTransientShownFromGestureOnSystemBar = false;
                    navigationBar.handleTransientChanged();
                }
            }

            @Override // com.android.systemui.statusbar.AutoHideUiElement
            public final boolean isVisible() {
                NavigationBar navigationBar = NavigationBar.this;
                Objects.requireNonNull(navigationBar);
                return navigationBar.mTransientShown;
            }

            @Override // com.android.systemui.statusbar.AutoHideUiElement
            public final boolean shouldHideOnTouch() {
                return !NavigationBar.this.mNotificationRemoteInputManager.isRemoteInputActive();
            }

            @Override // com.android.systemui.statusbar.AutoHideUiElement
            public final void synchronizeState() {
                NavigationBar.this.checkNavBarModes();
            }
        };
        this.mNavbarTaskbarStateUpdater = new AnonymousClass2();
        this.mOverviewProxyListener = new OverviewProxyService.OverviewProxyListener() { // from class: com.android.systemui.navigationbar.NavigationBar.3
            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public final void onConnectionChanged(boolean z) {
                NavigationBar.this.mNavigationBarView.updateStates();
                NavigationBar.this.updateScreenPinningGestures();
            }

            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public final void onHomeRotationEnabled(boolean z) {
                NavigationBarView navigationBarView = NavigationBar.this.mNavigationBarView;
                Objects.requireNonNull(navigationBarView);
                RotationButtonController rotationButtonController = navigationBarView.mRotationButtonController;
                Objects.requireNonNull(rotationButtonController);
                rotationButtonController.mHomeRotationEnabled = z;
                if (rotationButtonController.mIsRecentsAnimationRunning && !z) {
                    rotationButtonController.setRotateSuggestionButtonState(false, true);
                }
            }

            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public final void onNavBarButtonAlphaChanged(float f, boolean z) {
                boolean z2;
                ButtonDispatcher buttonDispatcher;
                boolean z3;
                boolean z4;
                boolean z5;
                NavigationBar navigationBar = NavigationBar.this;
                if (navigationBar.mIsCurrentUserSetup) {
                    int i = navigationBar.mNavBarMode;
                    int i2 = 0;
                    if (i == 0) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (!z2) {
                        ButtonDispatcher buttonDispatcher2 = null;
                        if (R$color.isGesturalMode(i)) {
                            NavigationBar navigationBar2 = NavigationBar.this;
                            if (!navigationBar2.mAllowForceNavBarHandleOpaque || !navigationBar2.mForceNavBarHandleOpaque) {
                                z4 = false;
                            } else {
                                z4 = true;
                            }
                            NavigationBarView navigationBarView = navigationBar2.mNavigationBarView;
                            Objects.requireNonNull(navigationBarView);
                            buttonDispatcher = navigationBarView.mButtonDispatchers.get(2131428097);
                            if (NavigationBar.this.getBarTransitions() != null) {
                                NavigationBarTransitions barTransitions = NavigationBar.this.getBarTransitions();
                                Objects.requireNonNull(barTransitions);
                                BarTransitions.BarBackgroundDrawable barBackgroundDrawable = barTransitions.mBarBackground;
                                Objects.requireNonNull(barBackgroundDrawable);
                                barBackgroundDrawable.mOverrideAlpha = f;
                                barBackgroundDrawable.invalidateSelf();
                            }
                            z3 = false;
                        } else {
                            NavigationBar navigationBar3 = NavigationBar.this;
                            if (navigationBar3.mNavBarMode == 1) {
                                z5 = true;
                            } else {
                                z5 = false;
                            }
                            if (z5) {
                                buttonDispatcher2 = navigationBar3.mNavigationBarView.getBackButton();
                            }
                            z3 = z;
                            z4 = false;
                            buttonDispatcher = buttonDispatcher2;
                        }
                        if (buttonDispatcher != null) {
                            if (!z4 && f <= 0.0f) {
                                i2 = 4;
                            }
                            buttonDispatcher.setVisibility(i2);
                            if (z4) {
                                f = 1.0f;
                            }
                            buttonDispatcher.setAlpha(f, z3, true);
                        }
                    }
                }
            }

            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public final void onOverviewShown() {
                NavigationBarView navigationBarView = NavigationBar.this.mNavigationBarView;
                Objects.requireNonNull(navigationBarView);
                RotationButtonController rotationButtonController = navigationBarView.mRotationButtonController;
                Objects.requireNonNull(rotationButtonController);
                rotationButtonController.mSkipOverrideUserLockPrefsOnce = !rotationButtonController.mIsRecentsAnimationRunning;
            }

            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public final void onPrioritizedRotation(int i) {
                NavigationBar navigationBar = NavigationBar.this;
                navigationBar.mStartingQuickSwitchRotation = i;
                if (i == -1) {
                    navigationBar.mShowOrientedHandleForImmersiveMode = false;
                }
                navigationBar.orientSecondaryHomeHandle();
            }

            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public final void onTaskbarStatusUpdated(boolean z, boolean z2) {
                NavigationBarView navigationBarView = NavigationBar.this.mNavigationBarView;
                Objects.requireNonNull(navigationBarView);
                FloatingRotationButton floatingRotationButton = navigationBarView.mFloatingRotationButton;
                Objects.requireNonNull(floatingRotationButton);
                floatingRotationButton.mIsTaskbarVisible = z;
                floatingRotationButton.mIsTaskbarStashed = z2;
                if (floatingRotationButton.mIsShowing) {
                    FloatingRotationButtonPositionCalculator.Position calculatePosition = floatingRotationButton.mPositionCalculator.calculatePosition(floatingRotationButton.mDisplayRotation, z, z2);
                    int i = calculatePosition.translationX;
                    FloatingRotationButtonPositionCalculator.Position position = floatingRotationButton.mPosition;
                    Objects.requireNonNull(position);
                    if (i == position.translationX) {
                        int i2 = calculatePosition.translationY;
                        FloatingRotationButtonPositionCalculator.Position position2 = floatingRotationButton.mPosition;
                        Objects.requireNonNull(position2);
                        if (i2 == position2.translationY) {
                            return;
                        }
                    }
                    floatingRotationButton.updateTranslation(calculatePosition, true);
                    floatingRotationButton.mPosition = calculatePosition;
                }
            }

            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public final void onToggleRecentApps() {
                NavigationBarView navigationBarView = NavigationBar.this.mNavigationBarView;
                Objects.requireNonNull(navigationBarView);
                RotationButtonController rotationButtonController = navigationBarView.mRotationButtonController;
                Objects.requireNonNull(rotationButtonController);
                rotationButtonController.mSkipOverrideUserLockPrefsOnce = !rotationButtonController.mIsRecentsAnimationRunning;
            }

            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public final void startAssistant(Bundle bundle) {
                NavigationBar.this.mAssistManagerLazy.get().startAssist(bundle);
            }
        };
        this.mOrientationHandleIntensityListener = new NavigationBarTransitions.DarkIntensityListener() { // from class: com.android.systemui.navigationbar.NavigationBar.4
            @Override // com.android.systemui.navigationbar.NavigationBarTransitions.DarkIntensityListener
            public final void onDarkIntensity(float f) {
                NavigationBar.this.mOrientationHandle.setDarkIntensity(f);
            }
        };
        this.mAutoDim = new ScreenDecorations$$ExternalSyntheticLambda1(this, 3);
        this.mEnableLayoutTransitions = new StandardWifiEntry$$ExternalSyntheticLambda0(this, 3);
        this.mOnVariableDurationHomeLongClick = new BubbleStackView$$ExternalSyntheticLambda19(this, 3);
        this.mOnPropertiesChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.systemui.navigationbar.NavigationBar.5
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (properties.getKeyset().contains("nav_bar_handle_force_opaque")) {
                    NavigationBar.this.mForceNavBarHandleOpaque = properties.getBoolean("nav_bar_handle_force_opaque", true);
                }
                if (properties.getKeyset().contains("home_button_long_press_duration_ms")) {
                    NavigationBar.this.mHomeButtonLongPressDurationMs = Optional.of(Long.valueOf(properties.getLong("home_button_long_press_duration_ms", 0L))).filter(NavigationBar$5$$ExternalSyntheticLambda0.INSTANCE);
                    NavigationBar.this.reconfigureHomeLongClick();
                }
            }
        };
        this.mUserSetupListener = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.android.systemui.navigationbar.NavigationBar.6
            @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
            public final void onUserSetupChanged() {
                NavigationBar navigationBar = NavigationBar.this;
                navigationBar.mIsCurrentUserSetup = navigationBar.mDeviceProvisionedController.isCurrentUserSetup();
            }
        };
        this.mDepthListener = new NotificationShadeDepthController.DepthListener() { // from class: com.android.systemui.navigationbar.NavigationBar.7
            public boolean mHasBlurs;

            @Override // com.android.systemui.statusbar.NotificationShadeDepthController.DepthListener
            public final void onWallpaperZoomOutChanged(float f) {
            }

            @Override // com.android.systemui.statusbar.NotificationShadeDepthController.DepthListener
            public final void onBlurRadiusChanged(int i) {
                boolean z;
                if (i != 0) {
                    z = true;
                } else {
                    z = false;
                }
                if (z != this.mHasBlurs) {
                    this.mHasBlurs = z;
                    NavigationBarView navigationBarView = NavigationBar.this.mNavigationBarView;
                    Objects.requireNonNull(navigationBarView);
                    RegionSamplingHelper regionSamplingHelper = navigationBarView.mRegionSamplingHelper;
                    Objects.requireNonNull(regionSamplingHelper);
                    regionSamplingHelper.mWindowHasBlurs = z;
                    regionSamplingHelper.updateSamplingListener();
                }
            }
        };
        this.mRotationWatcher = new NavigationBar$$ExternalSyntheticLambda13(this, 0);
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.navigationbar.NavigationBar.8
            @Override // android.content.BroadcastReceiver
            public final void onReceive(Context context2, Intent intent) {
                if (NavigationBar.this.mNavigationBarView != null) {
                    String action = intent.getAction();
                    if ("android.intent.action.SCREEN_OFF".equals(action) || "android.intent.action.SCREEN_ON".equals(action)) {
                        NavigationBar navigationBar = NavigationBar.this;
                        Objects.requireNonNull(navigationBar);
                        navigationBar.mNavigationBarView.updateNavButtonIcons();
                        NavigationBarView navigationBarView = NavigationBar.this.mNavigationBarView;
                        boolean equals = "android.intent.action.SCREEN_ON".equals(action);
                        Objects.requireNonNull(navigationBarView);
                        navigationBarView.mScreenOn = equals;
                        if (!equals) {
                            navigationBarView.mRegionSamplingHelper.stop();
                        } else if (Utils.isGesturalModeOnDefaultDisplay(navigationBarView.getContext(), navigationBarView.mNavBarMode)) {
                            navigationBarView.mRegionSamplingHelper.start(navigationBarView.mSamplingBounds);
                        }
                    }
                    if ("android.intent.action.USER_SWITCHED".equals(action)) {
                        NavigationBar.this.updateAccessibilityStateFlags();
                    }
                }
            }
        };
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mAccessibilityManager = accessibilityManager;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mStatusBarStateController = statusBarStateController;
        this.mMetricsLogger = metricsLogger;
        this.mAssistManagerLazy = lazy;
        this.mSysUiFlagsContainer = sysUiState;
        this.mStatusBarOptionalLazy = lazy2;
        this.mShadeController = shadeController;
        this.mNotificationRemoteInputManager = notificationRemoteInputManager;
        this.mOverviewProxyService = overviewProxyService;
        this.mNavigationModeController = navigationModeController;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mCommandQueue = commandQueue;
        this.mPipOptional = optional;
        this.mSplitScreenOptional = optional2;
        this.mRecentsOptional = optional3;
        this.mBackAnimation = optional5;
        this.mHandler = handler;
        this.mNavbarOverlayController = navigationBarOverlayController;
        this.mUiEventLogger = uiEventLogger;
        this.mNavBarHelper = navBarHelper;
        this.mNotificationShadeDepthController = notificationShadeDepthController;
        this.mMainLightBarController = lightBarController;
        this.mLightBarControllerFactory = factory;
        this.mMainAutoHideController = autoHideController;
        this.mAutoHideControllerFactory = factory2;
        this.mTelecomManagerOptional = optional4;
        this.mInputMethodManager = inputMethodManager;
        this.mNavBarMode = navigationModeController.addListener(this);
    }

    public static int barMode(boolean z, int i) {
        if (z) {
            return 1;
        }
        if ((i & 6) == 6) {
            return 3;
        }
        if ((i & 4) != 0) {
            return 6;
        }
        if ((i & 2) != 0) {
            return 4;
        }
        return (i & 64) != 0 ? 1 : 0;
    }

    public final boolean onLongPressNavigationButtons(View view, int i) {
        boolean z;
        ButtonDispatcher buttonDispatcher;
        try {
            IActivityTaskManager service = ActivityTaskManager.getService();
            boolean isTouchExplorationEnabled = this.mAccessibilityManager.isTouchExplorationEnabled();
            boolean isInLockTaskMode = service.isInLockTaskMode();
            if (isInLockTaskMode && !isTouchExplorationEnabled) {
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - this.mLastLockToAppLongPress < 200) {
                    service.stopSystemLockTaskMode();
                    this.mNavigationBarView.updateNavButtonIcons();
                    return true;
                }
                if (view.getId() == 2131427535) {
                    if (i == 2131428669) {
                        buttonDispatcher = this.mNavigationBarView.getRecentsButton();
                    } else {
                        buttonDispatcher = this.mNavigationBarView.getHomeButton();
                    }
                    Objects.requireNonNull(buttonDispatcher);
                    if (!buttonDispatcher.mCurrentView.isPressed()) {
                        z = true;
                        this.mLastLockToAppLongPress = currentTimeMillis;
                    }
                }
                z = false;
                this.mLastLockToAppLongPress = currentTimeMillis;
            } else if (view.getId() == 2131427535) {
                z = true;
            } else if (isTouchExplorationEnabled && isInLockTaskMode) {
                service.stopSystemLockTaskMode();
                this.mNavigationBarView.updateNavButtonIcons();
                return true;
            } else if (view.getId() != i) {
                z = false;
            } else if (i == 2131428669) {
                return onLongPressRecents();
            } else {
                ButtonDispatcher homeButton = this.mNavigationBarView.getHomeButton();
                Objects.requireNonNull(homeButton);
                return onHomeLongClick(homeButton.mCurrentView);
            }
            if (z) {
                KeyButtonView keyButtonView = (KeyButtonView) view;
                keyButtonView.sendEvent(0, 128);
                keyButtonView.sendAccessibilityEvent(2);
                return true;
            }
        } catch (RemoteException e) {
            Log.d("NavigationBar", "Unable to reach activity manager", e);
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void onRecentsAnimationStateChanged(boolean z) {
        if (z) {
            this.mNavbarOverlayController.setButtonState(false, true);
        }
        NavigationBarView navigationBarView = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView);
        RotationButtonController rotationButtonController = navigationBarView.mRotationButtonController;
        Objects.requireNonNull(rotationButtonController);
        rotationButtonController.mIsRecentsAnimationRunning = z;
        if (z && !rotationButtonController.mHomeRotationEnabled) {
            rotationButtonController.setRotateSuggestionButtonState(false, true);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void abortTransient(int i, int[] iArr) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 1) && this.mTransientShown) {
            this.mTransientShown = false;
            this.mTransientShownFromGestureOnSystemBar = false;
            handleTransientChanged();
        }
    }

    public final void checkNavBarModes() {
        boolean z;
        if (!((Boolean) this.mStatusBarOptionalLazy.get().map(WifiPickerTracker$$ExternalSyntheticLambda10.INSTANCE$1).orElse(Boolean.FALSE)).booleanValue() || this.mNavigationBarWindowState == 2) {
            z = false;
        } else {
            z = true;
        }
        NavigationBarView navigationBarView = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView);
        NavigationBarTransitions navigationBarTransitions = navigationBarView.mBarTransitions;
        int i = this.mNavigationBarMode;
        Objects.requireNonNull(navigationBarTransitions);
        int i2 = navigationBarTransitions.mMode;
        if (i2 != i) {
            navigationBarTransitions.mMode = i;
            navigationBarTransitions.onTransition(i2, i, z);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void disable(int i, int i2, int i3, boolean z) {
        int i4;
        if (i == this.mDisplayId) {
            int i5 = 56623104 & i2;
            if (i5 != this.mDisabledFlags1) {
                this.mDisabledFlags1 = i5;
                this.mNavigationBarView.setDisabledFlags(i2);
                updateScreenPinningGestures();
            }
            if (this.mIsOnDefaultDisplay && (i4 = i3 & 16) != this.mDisabledFlags2) {
                this.mDisabledFlags2 = i4;
                setDisabled2Flags(i4);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00b1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.view.WindowManager.LayoutParams getBarLayoutParamsForRotation(int r15) {
        /*
            Method dump skipped, instructions count: 239
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.NavigationBar.getBarLayoutParamsForRotation(int):android.view.WindowManager$LayoutParams");
    }

    public final NavigationBarTransitions getBarTransitions() {
        NavigationBarView navigationBarView = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView);
        return navigationBarView.mBarTransitions;
    }

    public final void handleTransientChanged() {
        LightBarController lightBarController;
        boolean z;
        boolean z2;
        NavigationBarView navigationBarView = this.mNavigationBarView;
        boolean z3 = this.mTransientShown;
        boolean z4 = this.mTransientShownFromGestureOnSystemBar;
        Objects.requireNonNull(navigationBarView);
        EdgeBackGestureHandler edgeBackGestureHandler = navigationBarView.mEdgeBackGestureHandler;
        Objects.requireNonNull(edgeBackGestureHandler);
        edgeBackGestureHandler.mIsNavBarShownTransiently = z3;
        boolean z5 = true;
        if (navigationBarView.mNavBarOverlayController.isNavigationBarOverlayEnabled()) {
            if (!R$color.isGesturalMode(navigationBarView.mNavBarMode) || z4) {
                z = true;
            } else {
                z = false;
            }
            if (!z3 || !z) {
                z2 = false;
            } else {
                z2 = true;
            }
            navigationBarView.mNavBarOverlayController.setButtonState(z2, false);
        }
        int barMode = barMode(this.mTransientShown, this.mAppearance);
        if (this.mNavigationBarMode != barMode) {
            this.mNavigationBarMode = barMode;
            checkNavBarModes();
            AutoHideController autoHideController = this.mAutoHideController;
            if (autoHideController != null) {
                autoHideController.touchAutoHide();
            }
        } else {
            z5 = false;
        }
        if (z5 && (lightBarController = this.mLightBarController) != null) {
            lightBarController.mHasLightNavigationBar = LightBarController.isLight(lightBarController.mAppearance, barMode, 16);
        }
    }

    public boolean onHomeLongClick(View view) {
        boolean z;
        if (!this.mNavigationBarView.isRecentsButtonVisible()) {
            Objects.requireNonNull(ActivityManagerWrapper.sInstance);
            if (ActivityManagerWrapper.isScreenPinningActive()) {
                return onLongPressNavigationButtons(view, 2131428094);
            }
        }
        if (!this.mDeviceProvisionedController.isDeviceProvisioned() || (this.mDisabledFlags1 & 33554432) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return false;
        }
        this.mMetricsLogger.action(239);
        this.mUiEventLogger.log(NavBarActionEvent.NAVBAR_ASSIST_LONGPRESS);
        Bundle bundle = new Bundle();
        bundle.putInt("invocation_type", 5);
        this.mAssistManagerLazy.get().startAssist(bundle);
        this.mStatusBarOptionalLazy.get().ifPresent(NavigationBar$$ExternalSyntheticLambda17.INSTANCE);
        NavigationBarView navigationBarView = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView);
        ButtonDispatcher homeButton = navigationBarView.getHomeButton();
        Objects.requireNonNull(homeButton);
        int size = homeButton.mViews.size();
        for (int i = 0; i < size; i++) {
            if (homeButton.mViews.get(i) instanceof ButtonInterface) {
                ((ButtonInterface) homeButton.mViews.get(i)).abortCurrentGesture();
            }
        }
        return true;
    }

    public boolean onHomeTouch(View view, MotionEvent motionEvent) {
        if (this.mHomeBlockedThisTouch && motionEvent.getActionMasked() != 0) {
            return true;
        }
        Optional<StatusBar> optional = this.mStatusBarOptionalLazy.get();
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mHomeBlockedThisTouch = false;
            if (this.mTelecomManagerOptional.isPresent() && this.mTelecomManagerOptional.get().isRinging() && ((Boolean) optional.map(WifiPickerTracker$$ExternalSyntheticLambda6.INSTANCE$1).orElse(Boolean.FALSE)).booleanValue()) {
                Log.i("NavigationBar", "Ignoring HOME; there's a ringing incoming call. No heads up");
                this.mHomeBlockedThisTouch = true;
                return true;
            } else if (this.mLongPressHomeEnabled) {
                this.mHomeButtonLongPressDurationMs.ifPresent(new NavigationBar$$ExternalSyntheticLambda12(this, 0));
            }
        } else if (action == 1 || action == 3) {
            this.mHandler.removeCallbacks(this.mOnVariableDurationHomeLongClick);
            optional.ifPresent(NavigationBar$$ExternalSyntheticLambda16.INSTANCE);
        }
        return false;
    }

    public final boolean onLongPressRecents() {
        if (this.mRecentsOptional.isPresent() || !ActivityTaskManager.supportsMultiWindow(this.mContext) || ActivityManager.isLowRamDeviceStatic()) {
            return false;
        }
        OverviewProxyService overviewProxyService = this.mOverviewProxyService;
        Objects.requireNonNull(overviewProxyService);
        if (overviewProxyService.mOverviewProxy != null) {
            return false;
        }
        Optional<U> map = this.mSplitScreenOptional.map(WMShellBaseModule$$ExternalSyntheticLambda4.INSTANCE$2);
        Boolean bool = Boolean.FALSE;
        if (!((Boolean) map.orElse(bool)).booleanValue()) {
            return false;
        }
        return ((Boolean) this.mStatusBarOptionalLazy.get().map(WMShellBaseModule$$ExternalSyntheticLambda3.INSTANCE$2).orElse(bool)).booleanValue();
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public final void onNavigationModeChanged(int i) {
        boolean z;
        this.mNavBarMode = i;
        if (!R$color.isGesturalMode(i) && getBarTransitions() != null) {
            NavigationBarTransitions barTransitions = getBarTransitions();
            Objects.requireNonNull(barTransitions);
            BarTransitions.BarBackgroundDrawable barBackgroundDrawable = barTransitions.mBarBackground;
            Objects.requireNonNull(barBackgroundDrawable);
            barBackgroundDrawable.mOverrideAlpha = 1.0f;
            barBackgroundDrawable.invalidateSelf();
        }
        updateScreenPinningGestures();
        if (this.mNavBarMode != 2 || this.mOrientationHandle == null) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            resetSecondaryHandle();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void onRotationProposal(int i, boolean z) {
        boolean z2;
        boolean z3;
        int i2;
        int i3;
        if (this.mNavigationBarView.isAttachedToWindow()) {
            int i4 = this.mDisabledFlags2;
            LinearInterpolator linearInterpolator = RotationButtonController.LINEAR_INTERPOLATOR;
            boolean z4 = false;
            if ((i4 & 16) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            NavigationBarView navigationBarView = this.mNavigationBarView;
            Objects.requireNonNull(navigationBarView);
            RotationButtonController rotationButtonController = navigationBarView.mRotationButtonController;
            Objects.requireNonNull(rotationButtonController);
            if (!z2) {
                int intValue = rotationButtonController.mWindowRotationProvider.get().intValue();
                if (rotationButtonController.mRotationButton.acceptRotationProposal()) {
                    if (!rotationButtonController.mHomeRotationEnabled && rotationButtonController.mIsRecentsAnimationRunning) {
                        return;
                    }
                    if (!z) {
                        rotationButtonController.setRotateSuggestionButtonState(false, false);
                    } else if (i == intValue) {
                        rotationButtonController.mMainThreadHandler.removeCallbacks(rotationButtonController.mRemoveRotationProposal);
                        rotationButtonController.setRotateSuggestionButtonState(false, false);
                    } else {
                        rotationButtonController.mLastRotationSuggestion = i;
                        if (!(intValue == 0 && i == 1) && ((intValue == 0 && i == 2) || ((intValue == 0 && i == 3) || ((intValue == 1 && i == 0) || (!(intValue == 1 && i == 2) && ((intValue == 1 && i == 3) || ((intValue == 2 && i == 0) || ((intValue == 2 && i == 1) || (!(intValue == 2 && i == 3) && (!(intValue == 3 && i == 0) && ((intValue == 3 && i == 1) || (intValue == 3 && i == 2)))))))))))) {
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        if (intValue == 0 || intValue == 2) {
                            if (z3) {
                                i2 = rotationButtonController.mIconCcwStart0ResId;
                            } else {
                                i2 = rotationButtonController.mIconCwStart0ResId;
                            }
                            rotationButtonController.mIconResId = i2;
                        } else {
                            if (z3) {
                                i3 = rotationButtonController.mIconCcwStart90ResId;
                            } else {
                                i3 = rotationButtonController.mIconCwStart90ResId;
                            }
                            rotationButtonController.mIconResId = i3;
                        }
                        rotationButtonController.mRotationButton.updateIcon(rotationButtonController.mLightIconColor, rotationButtonController.mDarkIconColor);
                        if (rotationButtonController.mIsNavigationBarShowing || rotationButtonController.mBehavior == 1) {
                            z4 = true;
                        }
                        if (z4) {
                            rotationButtonController.showAndLogRotationSuggestion();
                            return;
                        }
                        rotationButtonController.mPendingRotationSuggestion = true;
                        rotationButtonController.mMainThreadHandler.removeCallbacks(rotationButtonController.mCancelPendingRotationProposal);
                        rotationButtonController.mMainThreadHandler.postDelayed(rotationButtonController.mCancelPendingRotationProposal, 20000L);
                    }
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x002a  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0033  */
    /* JADX WARN: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onSystemBarAttributesChanged(int r2, int r3, com.android.internal.view.AppearanceRegion[] r4, boolean r5, int r6, android.view.InsetsVisibilities r7, java.lang.String r8) {
        /*
            r1 = this;
            int r4 = r1.mDisplayId
            if (r2 == r4) goto L_0x0005
            return
        L_0x0005:
            int r2 = r1.mAppearance
            r4 = 1
            r7 = 0
            if (r2 == r3) goto L_0x0025
            r1.mAppearance = r3
            boolean r2 = r1.mTransientShown
            int r2 = barMode(r2, r3)
            int r8 = r1.mNavigationBarMode
            if (r8 == r2) goto L_0x0025
            r1.mNavigationBarMode = r2
            r1.checkNavBarModes()
            com.android.systemui.statusbar.phone.AutoHideController r2 = r1.mAutoHideController
            if (r2 == 0) goto L_0x0023
            r2.touchAutoHide()
        L_0x0023:
            r2 = r4
            goto L_0x0026
        L_0x0025:
            r2 = r7
        L_0x0026:
            com.android.systemui.statusbar.phone.LightBarController r8 = r1.mLightBarController
            if (r8 == 0) goto L_0x002f
            int r0 = r1.mNavigationBarMode
            r8.onNavigationBarAppearanceChanged(r3, r2, r0, r5)
        L_0x002f:
            int r2 = r1.mBehavior
            if (r2 == r6) goto L_0x0056
            r1.mBehavior = r6
            com.android.systemui.navigationbar.NavigationBarView r2 = r1.mNavigationBarView
            java.util.Objects.requireNonNull(r2)
            com.android.systemui.shared.rotation.RotationButtonController r2 = r2.mRotationButtonController
            int r3 = r2.mBehavior
            if (r3 == r6) goto L_0x0053
            r2.mBehavior = r6
            boolean r3 = r2.mIsNavigationBarShowing
            if (r3 != 0) goto L_0x004a
            if (r6 != r4) goto L_0x0049
            goto L_0x004a
        L_0x0049:
            r4 = r7
        L_0x004a:
            if (r4 == 0) goto L_0x0053
            boolean r3 = r2.mPendingRotationSuggestion
            if (r3 == 0) goto L_0x0053
            r2.showAndLogRotationSuggestion()
        L_0x0053:
            r1.updateSystemUiStateFlags()
        L_0x0056:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.NavigationBar.onSystemBarAttributesChanged(int, int, com.android.internal.view.AppearanceRegion[], boolean, int, android.view.InsetsVisibilities, java.lang.String):void");
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public final void onViewDetachedFromWindow(View view) {
        NavigationBarView navigationBarView = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView);
        RotationButtonController rotationButtonController = navigationBarView.mRotationButtonController;
        Objects.requireNonNull(rotationButtonController);
        rotationButtonController.mRotWatcherListener = null;
        NavigationBarView navigationBarView2 = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView2);
        NavigationBarTransitions navigationBarTransitions = navigationBarView2.mBarTransitions;
        Objects.requireNonNull(navigationBarTransitions);
        try {
            ((IWindowManager) Dependency.get(IWindowManager.class)).unregisterWallpaperVisibilityListener(navigationBarTransitions.mWallpaperVisibilityListener, 0);
        } catch (RemoteException unused) {
        }
        LightBarTransitionsController lightTransitionsController = this.mNavigationBarView.getLightTransitionsController();
        Objects.requireNonNull(lightTransitionsController);
        lightTransitionsController.mCommandQueue.removeCallback((CommandQueue.Callbacks) lightTransitionsController);
        lightTransitionsController.mStatusBarStateController.removeCallback(lightTransitionsController);
        this.mOverviewProxyService.removeCallback((OverviewProxyService.OverviewProxyListener) this.mOverviewProxyListener);
        this.mBroadcastDispatcher.unregisterReceiver(this.mBroadcastReceiver);
        if (this.mOrientationHandle != null) {
            resetSecondaryHandle();
            NavigationBarTransitions barTransitions = getBarTransitions();
            AnonymousClass4 r1 = this.mOrientationHandleIntensityListener;
            Objects.requireNonNull(barTransitions);
            barTransitions.mDarkIntensityListeners.remove(r1);
            this.mWindowManager.removeView(this.mOrientationHandle);
            this.mOrientationHandle.getViewTreeObserver().removeOnGlobalLayoutListener(this.mOrientationHandleGlobalLayoutListener);
        }
        this.mHandler.removeCallbacks(this.mAutoDim);
        this.mHandler.removeCallbacks(this.mOnVariableDurationHomeLongClick);
        this.mHandler.removeCallbacks(this.mEnableLayoutTransitions);
        NavBarHelper navBarHelper = this.mNavBarHelper;
        AnonymousClass2 r12 = this.mNavbarTaskbarStateUpdater;
        Objects.requireNonNull(navBarHelper);
        navBarHelper.mA11yEventListeners.remove(r12);
        Optional<Pip> optional = this.mPipOptional;
        NavigationBarView navigationBarView3 = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView3);
        optional.ifPresent(new NavigationBar$$ExternalSyntheticLambda14(navigationBarView3, 0));
        this.mFrame = null;
        this.mNavigationBarView = null;
        this.mOrientationHandle = null;
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0097  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void orientSecondaryHomeHandle() {
        /*
            r7 = this;
            int r0 = r7.mNavBarMode
            r1 = 1
            r2 = 0
            r3 = 2
            if (r0 != r3) goto L_0x000d
            com.android.systemui.navigationbar.gestural.QuickswitchOrientedNavHandle r0 = r7.mOrientationHandle
            if (r0 == 0) goto L_0x000d
            r0 = r1
            goto L_0x000e
        L_0x000d:
            r0 = r2
        L_0x000e:
            if (r0 != 0) goto L_0x0011
            return
        L_0x0011:
            int r0 = r7.mStartingQuickSwitchRotation
            r4 = -1
            if (r0 == r4) goto L_0x00b5
            java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreen> r0 = r7.mSplitScreenOptional
            com.android.wm.shell.dagger.WMShellBaseModule$$ExternalSyntheticLambda5 r5 = com.android.wm.shell.dagger.WMShellBaseModule$$ExternalSyntheticLambda5.INSTANCE$2
            java.util.Optional r0 = r0.map(r5)
            java.lang.Boolean r5 = java.lang.Boolean.FALSE
            java.lang.Object r0 = r0.orElse(r5)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x002e
            goto L_0x00b5
        L_0x002e:
            int r0 = r7.mCurrentRotation
            int r5 = r7.mStartingQuickSwitchRotation
            int r0 = r5 - r0
            if (r0 >= 0) goto L_0x0038
            int r0 = r0 + 4
        L_0x0038:
            if (r5 == r4) goto L_0x003c
            if (r0 != r4) goto L_0x0056
        L_0x003c:
            java.lang.String r4 = "secondary nav delta rotation: "
            java.lang.String r5 = " current: "
            java.lang.StringBuilder r4 = androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0.m(r4, r0, r5)
            int r5 = r7.mCurrentRotation
            r4.append(r5)
            java.lang.String r5 = " starting: "
            r4.append(r5)
            int r5 = r7.mStartingQuickSwitchRotation
            java.lang.String r6 = "NavigationBar"
            com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(r4, r5, r6)
        L_0x0056:
            android.view.WindowManager r4 = r7.mWindowManager
            android.view.WindowMetrics r4 = r4.getCurrentWindowMetrics()
            android.graphics.Rect r4 = r4.getBounds()
            com.android.systemui.navigationbar.gestural.QuickswitchOrientedNavHandle r5 = r7.mOrientationHandle
            java.util.Objects.requireNonNull(r5)
            r5.mDeltaRotation = r0
            r5 = 3
            if (r0 == 0) goto L_0x007e
            if (r0 == r1) goto L_0x0073
            if (r0 == r3) goto L_0x007e
            if (r0 == r5) goto L_0x0073
            r3 = r2
            r4 = r3
            goto L_0x0090
        L_0x0073:
            int r3 = r4.height()
            com.android.systemui.navigationbar.NavigationBarView r4 = r7.mNavigationBarView
            int r4 = r4.getHeight()
            goto L_0x0090
        L_0x007e:
            boolean r3 = r7.mShowOrientedHandleForImmersiveMode
            if (r3 != 0) goto L_0x0086
            r7.resetSecondaryHandle()
            return
        L_0x0086:
            int r4 = r4.width()
            com.android.systemui.navigationbar.NavigationBarView r3 = r7.mNavigationBarView
            int r3 = r3.getHeight()
        L_0x0090:
            android.view.WindowManager$LayoutParams r6 = r7.mOrientationParams
            if (r0 != 0) goto L_0x0097
            r5 = 80
            goto L_0x009b
        L_0x0097:
            if (r0 != r1) goto L_0x009a
            goto L_0x009b
        L_0x009a:
            r5 = 5
        L_0x009b:
            r6.gravity = r5
            r6.height = r3
            r6.width = r4
            android.view.WindowManager r0 = r7.mWindowManager
            com.android.systemui.navigationbar.gestural.QuickswitchOrientedNavHandle r1 = r7.mOrientationHandle
            r0.updateViewLayout(r1, r6)
            com.android.systemui.navigationbar.NavigationBarView r0 = r7.mNavigationBarView
            r1 = 8
            r0.setVisibility(r1)
            com.android.systemui.navigationbar.gestural.QuickswitchOrientedNavHandle r7 = r7.mOrientationHandle
            r7.setVisibility(r2)
            goto L_0x00b8
        L_0x00b5:
            r7.resetSecondaryHandle()
        L_0x00b8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.NavigationBar.orientSecondaryHomeHandle():void");
    }

    public final void prepareNavigationBarView() {
        this.mNavigationBarView.reorient();
        ButtonDispatcher recentsButton = this.mNavigationBarView.getRecentsButton();
        recentsButton.setOnClickListener(new NavigationBar$$ExternalSyntheticLambda2(this, 0));
        recentsButton.mTouchListener = new View.OnTouchListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda9
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                NavigationBar navigationBar = NavigationBar.this;
                Objects.requireNonNull(navigationBar);
                int action = motionEvent.getAction() & 255;
                if (action == 0) {
                    navigationBar.mCommandQueue.preloadRecentApps();
                    return false;
                } else if (action == 3) {
                    navigationBar.mCommandQueue.cancelPreloadRecentApps();
                    return false;
                } else if (action != 1 || view.isPressed()) {
                    return false;
                } else {
                    navigationBar.mCommandQueue.cancelPreloadRecentApps();
                    return false;
                }
            }
        };
        int size = recentsButton.mViews.size();
        for (int i = 0; i < size; i++) {
            recentsButton.mViews.get(i).setOnTouchListener(recentsButton.mTouchListener);
        }
        ButtonDispatcher homeButton = this.mNavigationBarView.getHomeButton();
        View.OnTouchListener navigationBar$$ExternalSyntheticLambda7 = new View.OnTouchListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda7
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return NavigationBar.this.onHomeTouch(view, motionEvent);
            }
        };
        Objects.requireNonNull(homeButton);
        homeButton.mTouchListener = navigationBar$$ExternalSyntheticLambda7;
        int size2 = homeButton.mViews.size();
        for (int i2 = 0; i2 < size2; i2++) {
            homeButton.mViews.get(i2).setOnTouchListener(homeButton.mTouchListener);
        }
        reconfigureHomeLongClick();
        NavigationBarView navigationBarView = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView);
        ButtonDispatcher buttonDispatcher = navigationBarView.mButtonDispatchers.get(2131427368);
        buttonDispatcher.setOnClickListener(new NavigationBar$$ExternalSyntheticLambda0(this, 0));
        buttonDispatcher.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda6
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                NavigationBar navigationBar = NavigationBar.this;
                Objects.requireNonNull(navigationBar);
                Intent intent = new Intent("com.android.internal.intent.action.CHOOSE_ACCESSIBILITY_BUTTON");
                intent.addFlags(268468224);
                intent.setClassName(ThemeOverlayApplier.ANDROID_PACKAGE, AccessibilityButtonChooserActivity.class.getName());
                navigationBar.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
                return true;
            }
        });
        updateAccessibilityStateFlags();
        NavigationBarView navigationBarView2 = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView2);
        navigationBarView2.mButtonDispatchers.get(2131428119).setOnClickListener(new NavigationBar$$ExternalSyntheticLambda1(this, 0));
        updateScreenPinningGestures();
    }

    public final void reconfigureHomeLongClick() {
        ButtonDispatcher homeButton = this.mNavigationBarView.getHomeButton();
        Objects.requireNonNull(homeButton);
        if (homeButton.mCurrentView != null) {
            if (this.mHomeButtonLongPressDurationMs.isPresent() || !this.mLongPressHomeEnabled) {
                ButtonDispatcher homeButton2 = this.mNavigationBarView.getHomeButton();
                Objects.requireNonNull(homeButton2);
                homeButton2.mCurrentView.setLongClickable(false);
                ButtonDispatcher homeButton3 = this.mNavigationBarView.getHomeButton();
                Objects.requireNonNull(homeButton3);
                homeButton3.mCurrentView.setHapticFeedbackEnabled(false);
                this.mNavigationBarView.getHomeButton().setOnLongClickListener(null);
                return;
            }
            ButtonDispatcher homeButton4 = this.mNavigationBarView.getHomeButton();
            Objects.requireNonNull(homeButton4);
            homeButton4.mCurrentView.setLongClickable(true);
            ButtonDispatcher homeButton5 = this.mNavigationBarView.getHomeButton();
            Objects.requireNonNull(homeButton5);
            homeButton5.mCurrentView.setHapticFeedbackEnabled(true);
            this.mNavigationBarView.getHomeButton().setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda3
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    return NavigationBar.this.onHomeLongClick(view);
                }
            });
        }
    }

    public final void resetSecondaryHandle() {
        QuickswitchOrientedNavHandle quickswitchOrientedNavHandle = this.mOrientationHandle;
        if (quickswitchOrientedNavHandle != null) {
            quickswitchOrientedNavHandle.setVisibility(8);
        }
        this.mNavigationBarView.setVisibility(0);
        NavigationBarView navigationBarView = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView);
        navigationBarView.mOrientedHandleSamplingRegion = null;
        navigationBarView.mRegionSamplingHelper.updateSamplingRect();
    }

    public final void setDisabled2Flags(int i) {
        boolean z;
        NavigationBarView navigationBarView = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView);
        RotationButtonController rotationButtonController = navigationBarView.mRotationButtonController;
        LinearInterpolator linearInterpolator = RotationButtonController.LINEAR_INTERPOLATOR;
        if ((i & 16) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            rotationButtonController.setRotateSuggestionButtonState(false, true);
            rotationButtonController.mMainThreadHandler.removeCallbacks(rotationButtonController.mRemoveRotationProposal);
            return;
        }
        Objects.requireNonNull(rotationButtonController);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
        boolean z2;
        if (i == this.mDisplayId) {
            boolean isImeShown = this.mNavBarHelper.isImeShown(i2);
            if (!isImeShown || !z) {
                z2 = false;
            } else {
                z2 = true;
            }
            int calculateBackDispositionHints = Utilities.calculateBackDispositionHints(this.mNavigationIconHints, i3, isImeShown, z2);
            if (calculateBackDispositionHints != this.mNavigationIconHints) {
                this.mNavigationIconHints = calculateBackDispositionHints;
                if (!Utilities.isTablet(this.mContext)) {
                    this.mNavigationBarView.setNavigationIconHints(calculateBackDispositionHints);
                }
                if (this.mIsOnDefaultDisplay) {
                    this.mStatusBarOptionalLazy.get().ifPresent(SysUIComponent$$ExternalSyntheticLambda1.INSTANCE$1);
                } else {
                    checkNavBarModes();
                }
                updateSystemUiStateFlags();
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void setWindowState(int i, int i2, int i3) {
        boolean z;
        boolean z2;
        if (i == this.mDisplayId && i2 == 2 && this.mNavigationBarWindowState != i3) {
            this.mNavigationBarWindowState = i3;
            updateSystemUiStateFlags();
            boolean z3 = true;
            if (i3 == 2) {
                z = true;
            } else {
                z = false;
            }
            this.mShowOrientedHandleForImmersiveMode = z;
            if (!(this.mOrientationHandle == null || this.mStartingQuickSwitchRotation == -1)) {
                orientSecondaryHomeHandle();
            }
            NavigationBarView navigationBarView = this.mNavigationBarView;
            if (this.mNavigationBarWindowState == 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            Objects.requireNonNull(navigationBarView);
            RegionSamplingHelper regionSamplingHelper = navigationBarView.mRegionSamplingHelper;
            Objects.requireNonNull(regionSamplingHelper);
            regionSamplingHelper.mWindowVisible = z2;
            regionSamplingHelper.updateSamplingListener();
            RotationButtonController rotationButtonController = navigationBarView.mRotationButtonController;
            Objects.requireNonNull(rotationButtonController);
            if (rotationButtonController.mIsNavigationBarShowing != z2) {
                rotationButtonController.mIsNavigationBarShowing = z2;
                if (!z2 && rotationButtonController.mBehavior != 1) {
                    z3 = false;
                }
                if (z3 && rotationButtonController.mPendingRotationSuggestion) {
                    rotationButtonController.showAndLogRotationSuggestion();
                }
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void showTransient(int i, int[] iArr, boolean z) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 1) && !this.mTransientShown) {
            this.mTransientShown = true;
            this.mTransientShownFromGestureOnSystemBar = z;
            handleTransientChanged();
        }
    }

    public final void updateAccessibilityStateFlags() {
        boolean z;
        if (this.mNavigationBarView != null) {
            NavBarHelper navBarHelper = this.mNavBarHelper;
            Objects.requireNonNull(navBarHelper);
            int i = navBarHelper.mA11yButtonState;
            boolean z2 = true;
            if ((i & 16) != 0) {
                z = true;
            } else {
                z = false;
            }
            if ((i & 32) == 0) {
                z2 = false;
            }
            NavigationBarView navigationBarView = this.mNavigationBarView;
            Objects.requireNonNull(navigationBarView);
            navigationBarView.mButtonDispatchers.get(2131427368).setLongClickable(z2);
            navigationBarView.mContextualButtonGroup.setButtonVisibility(2131427368, z);
        }
        updateSystemUiStateFlags();
    }

    public final void updateScreenPinningGestures() {
        View.OnLongClickListener onLongClickListener;
        Objects.requireNonNull(ActivityManagerWrapper.sInstance);
        boolean isScreenPinningActive = ActivityManagerWrapper.isScreenPinningActive();
        ButtonDispatcher backButton = this.mNavigationBarView.getBackButton();
        ButtonDispatcher recentsButton = this.mNavigationBarView.getRecentsButton();
        if (isScreenPinningActive) {
            if (this.mNavigationBarView.isRecentsButtonVisible()) {
                onLongClickListener = new View.OnLongClickListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda5
                    @Override // android.view.View.OnLongClickListener
                    public final boolean onLongClick(View view) {
                        NavigationBar navigationBar = NavigationBar.this;
                        Objects.requireNonNull(navigationBar);
                        return navigationBar.onLongPressNavigationButtons(view, 2131428669);
                    }
                };
            } else {
                onLongClickListener = new View.OnLongClickListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda4
                    @Override // android.view.View.OnLongClickListener
                    public final boolean onLongClick(View view) {
                        NavigationBar navigationBar = NavigationBar.this;
                        Objects.requireNonNull(navigationBar);
                        return navigationBar.onLongPressNavigationButtons(view, 2131428094);
                    }
                };
            }
            backButton.setOnLongClickListener(onLongClickListener);
            recentsButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda5
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    NavigationBar navigationBar = NavigationBar.this;
                    Objects.requireNonNull(navigationBar);
                    return navigationBar.onLongPressNavigationButtons(view, 2131428669);
                }
            });
        } else {
            backButton.setOnLongClickListener(null);
            recentsButton.setOnLongClickListener(null);
        }
        backButton.setLongClickable(isScreenPinningActive);
        recentsButton.setLongClickable(isScreenPinningActive);
    }

    public final void updateSystemUiStateFlags() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        NavBarHelper navBarHelper = this.mNavBarHelper;
        Objects.requireNonNull(navBarHelper);
        int i = navBarHelper.mA11yButtonState;
        boolean z6 = true;
        if ((i & 16) != 0) {
            z = true;
        } else {
            z = false;
        }
        if ((i & 32) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        SysUiState sysUiState = this.mSysUiFlagsContainer;
        sysUiState.setFlag(16, z);
        sysUiState.setFlag(32, z2);
        if (this.mNavigationBarWindowState == 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        sysUiState.setFlag(2, !z3);
        if ((this.mNavigationIconHints & 1) != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        sysUiState.setFlag(262144, z4);
        if ((this.mNavigationIconHints & 4) != 0) {
            z5 = true;
        } else {
            z5 = false;
        }
        sysUiState.setFlag(1048576, z5);
        if (this.mBehavior == 2) {
            z6 = false;
        }
        sysUiState.setFlag(131072, z6);
        sysUiState.commitUpdate(this.mDisplayId);
    }

    /* JADX WARN: Type inference failed for: r11v25, types: [com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda10] */
    @Override // android.view.View.OnAttachStateChangeListener
    public final void onViewAttachedToWindow(View view) {
        boolean z;
        LightBarController lightBarController;
        AutoHideController autoHideController;
        boolean z2;
        boolean z3;
        Display display = view.getDisplay();
        NavigationBarView navigationBarView = this.mNavigationBarView;
        Optional<Recents> optional = this.mRecentsOptional;
        Objects.requireNonNull(navigationBarView);
        navigationBarView.mRecentsOptional = optional;
        NavigationBarView navigationBarView2 = this.mNavigationBarView;
        StatusBar statusBar = this.mStatusBarOptionalLazy.get().get();
        Objects.requireNonNull(statusBar);
        NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
        Objects.requireNonNull(navigationBarView2);
        navigationBarView2.mPanelView = notificationPanelViewController;
        if (notificationPanelViewController != null) {
            notificationPanelViewController.updateSystemUiStateFlags();
        }
        this.mNavigationBarView.setDisabledFlags(this.mDisabledFlags1);
        NavigationBarView navigationBarView3 = this.mNavigationBarView;
        NavigationBar$$ExternalSyntheticLambda11 navigationBar$$ExternalSyntheticLambda11 = new NavigationBar$$ExternalSyntheticLambda11(this);
        Objects.requireNonNull(navigationBarView3);
        navigationBarView3.mOnVerticalChangedListener = navigationBar$$ExternalSyntheticLambda11;
        navigationBarView3.notifyVerticalChangedListener(navigationBarView3.mIsVertical);
        this.mNavigationBarView.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda8
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                NavigationBar navigationBar = NavigationBar.this;
                Objects.requireNonNull(navigationBar);
                AutoHideController autoHideController2 = navigationBar.mAutoHideController;
                if (autoHideController2 == null) {
                    return false;
                }
                autoHideController2.checkUserAutoHide(motionEvent);
                return false;
            }
        });
        if (this.mSavedState != null) {
            LightBarTransitionsController lightTransitionsController = this.mNavigationBarView.getLightTransitionsController();
            Bundle bundle = this.mSavedState;
            Objects.requireNonNull(lightTransitionsController);
            float f = bundle.getFloat("dark_intensity", 0.0f);
            lightTransitionsController.mDarkIntensity = f;
            lightTransitionsController.mApplier.applyDarkIntensity(MathUtils.lerp(f, 0.0f, lightTransitionsController.mDozeAmount));
            lightTransitionsController.mNextDarkIntensity = lightTransitionsController.mDarkIntensity;
        }
        this.mNavigationBarView.setNavigationIconHints(this.mNavigationIconHints);
        NavigationBarView navigationBarView4 = this.mNavigationBarView;
        if (this.mNavigationBarWindowState == 0) {
            z = true;
        } else {
            z = false;
        }
        Objects.requireNonNull(navigationBarView4);
        RegionSamplingHelper regionSamplingHelper = navigationBarView4.mRegionSamplingHelper;
        Objects.requireNonNull(regionSamplingHelper);
        regionSamplingHelper.mWindowVisible = z;
        regionSamplingHelper.updateSamplingListener();
        RotationButtonController rotationButtonController = navigationBarView4.mRotationButtonController;
        Objects.requireNonNull(rotationButtonController);
        if (rotationButtonController.mIsNavigationBarShowing != z) {
            rotationButtonController.mIsNavigationBarShowing = z;
            if (z || rotationButtonController.mBehavior == 1) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (z3 && rotationButtonController.mPendingRotationSuggestion) {
                rotationButtonController.showAndLogRotationSuggestion();
            }
        }
        NavigationBarView navigationBarView5 = this.mNavigationBarView;
        int i = this.mBehavior;
        Objects.requireNonNull(navigationBarView5);
        RotationButtonController rotationButtonController2 = navigationBarView5.mRotationButtonController;
        if (rotationButtonController2.mBehavior != i) {
            rotationButtonController2.mBehavior = i;
            if (rotationButtonController2.mIsNavigationBarShowing || i == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2 && rotationButtonController2.mPendingRotationSuggestion) {
                rotationButtonController2.showAndLogRotationSuggestion();
            }
        }
        NavBarHelper navBarHelper = this.mNavBarHelper;
        AnonymousClass2 r1 = this.mNavbarTaskbarStateUpdater;
        Objects.requireNonNull(navBarHelper);
        navBarHelper.mA11yEventListeners.add(r1);
        r1.updateAccessibilityServicesState();
        r1.updateAssistantAvailable(navBarHelper.mAssistantAvailable);
        Optional<LegacySplitScreen> optional2 = this.mSplitScreenOptional;
        NavigationBarView navigationBarView6 = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView6);
        optional2.ifPresent(new WifiPickerTracker$$ExternalSyntheticLambda0(navigationBarView6, 2));
        Optional<Pip> optional3 = this.mPipOptional;
        NavigationBarView navigationBarView7 = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView7);
        optional3.ifPresent(new PipController$$ExternalSyntheticLambda4(navigationBarView7, 1));
        Optional<BackAnimation> optional4 = this.mBackAnimation;
        NavigationBarView navigationBarView8 = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView8);
        optional4.ifPresent(new BubbleController$$ExternalSyntheticLambda8(navigationBarView8, 2));
        prepareNavigationBarView();
        checkNavBarModes();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        this.mBroadcastDispatcher.registerReceiverWithHandler(this.mBroadcastReceiver, intentFilter, Handler.getMain(), UserHandle.ALL);
        this.mNavigationBarView.updateNavButtonIcons();
        this.mOverviewProxyService.addCallback((OverviewProxyService.OverviewProxyListener) this.mOverviewProxyListener);
        updateSystemUiStateFlags();
        if (this.mIsOnDefaultDisplay) {
            NavigationBarView navigationBarView9 = this.mNavigationBarView;
            Objects.requireNonNull(navigationBarView9);
            RotationButtonController rotationButtonController3 = navigationBarView9.mRotationButtonController;
            NavigationBar$$ExternalSyntheticLambda13 navigationBar$$ExternalSyntheticLambda13 = this.mRotationWatcher;
            Objects.requireNonNull(rotationButtonController3);
            rotationButtonController3.mRotWatcherListener = navigationBar$$ExternalSyntheticLambda13;
            if (display != null && RotationPolicy.isRotationLocked(rotationButtonController3.mContext)) {
                RotationPolicy.setRotationLockAtAngle(rotationButtonController3.mContext, true, display.getRotation());
            }
        } else {
            this.mDisabledFlags2 |= 16;
        }
        setDisabled2Flags(this.mDisabledFlags2);
        if (this.mNavBarMode == 2) {
            QuickswitchOrientedNavHandle quickswitchOrientedNavHandle = new QuickswitchOrientedNavHandle(this.mContext);
            this.mOrientationHandle = quickswitchOrientedNavHandle;
            quickswitchOrientedNavHandle.setId(2131428816);
            NavigationBarTransitions barTransitions = getBarTransitions();
            AnonymousClass4 r0 = this.mOrientationHandleIntensityListener;
            Objects.requireNonNull(barTransitions);
            barTransitions.mDarkIntensityListeners.add(r0);
            Objects.requireNonNull(barTransitions.mLightTransitionsController);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(0, 0, 2024, 536871224, -3);
            this.mOrientationParams = layoutParams;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("SecondaryHomeHandle");
            m.append(this.mContext.getDisplayId());
            layoutParams.setTitle(m.toString());
            WindowManager.LayoutParams layoutParams2 = this.mOrientationParams;
            layoutParams2.privateFlags |= 64;
            this.mWindowManager.addView(this.mOrientationHandle, layoutParams2);
            this.mOrientationHandle.setVisibility(8);
            this.mOrientationParams.setFitInsetsTypes(0);
            this.mOrientationHandleGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda10
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public final void onGlobalLayout() {
                    NavigationBar navigationBar = NavigationBar.this;
                    Objects.requireNonNull(navigationBar);
                    if (navigationBar.mStartingQuickSwitchRotation != -1) {
                        RectF computeHomeHandleBounds = navigationBar.mOrientationHandle.computeHomeHandleBounds();
                        navigationBar.mOrientationHandle.mapRectFromViewToScreenCoords(computeHomeHandleBounds, true);
                        Rect rect = new Rect();
                        computeHomeHandleBounds.roundOut(rect);
                        NavigationBarView navigationBarView10 = navigationBar.mNavigationBarView;
                        Objects.requireNonNull(navigationBarView10);
                        navigationBarView10.mOrientedHandleSamplingRegion = rect;
                        navigationBarView10.mRegionSamplingHelper.updateSamplingRect();
                    }
                }
            };
            this.mOrientationHandle.getViewTreeObserver().addOnGlobalLayoutListener(this.mOrientationHandleGlobalLayoutListener);
        }
        if (this.mIsOnDefaultDisplay) {
            lightBarController = this.mMainLightBarController;
        } else {
            LightBarController.Factory factory = this.mLightBarControllerFactory;
            Context context = this.mContext;
            Objects.requireNonNull(factory);
            lightBarController = new LightBarController(context, factory.mDarkIconDispatcher, factory.mBatteryController, factory.mNavModeController, factory.mDumpManager);
        }
        this.mLightBarController = lightBarController;
        if (lightBarController != null) {
            LightBarTransitionsController lightTransitionsController2 = this.mNavigationBarView.getLightTransitionsController();
            lightBarController.mNavigationBarController = lightTransitionsController2;
            if (lightTransitionsController2 != null && lightTransitionsController2.supportsIconTintForNavMode(lightBarController.mNavigationMode)) {
                lightBarController.mNavigationBarController.setIconsDark(lightBarController.mNavigationLight, lightBarController.animateChange());
            }
        }
        if (this.mIsOnDefaultDisplay) {
            autoHideController = this.mMainAutoHideController;
        } else {
            AutoHideController.Factory factory2 = this.mAutoHideControllerFactory;
            Context context2 = this.mContext;
            Objects.requireNonNull(factory2);
            autoHideController = new AutoHideController(context2, factory2.mHandler, factory2.mIWindowManager);
        }
        this.mAutoHideController = autoHideController;
        if (autoHideController != null) {
            autoHideController.mNavigationBar = this.mAutoHideUiElement;
        }
        NavigationBarView navigationBarView10 = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView10);
        navigationBarView10.mAutoHideController = autoHideController;
        int barMode = barMode(this.mTransientShown, this.mAppearance);
        this.mNavigationBarMode = barMode;
        checkNavBarModes();
        AutoHideController autoHideController2 = this.mAutoHideController;
        if (autoHideController2 != null) {
            autoHideController2.touchAutoHide();
        }
        LightBarController lightBarController2 = this.mLightBarController;
        if (lightBarController2 != null) {
            lightBarController2.onNavigationBarAppearanceChanged(this.mAppearance, true, barMode, false);
        }
    }

    public int getNavigationIconHints() {
        return this.mNavigationIconHints;
    }
}
