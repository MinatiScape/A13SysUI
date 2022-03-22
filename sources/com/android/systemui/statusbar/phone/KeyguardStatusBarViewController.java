package com.android.systemui.statusbar.phone;

import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.BiometricSourceType;
import android.os.UserManager;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import com.android.keyguard.CarrierTextController;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.events.SystemStatusAnimationCallback;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.userswitcher.OnUserSwitcherPreferenceChangeListener;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherFeatureController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.util.ViewController;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
/* loaded from: classes.dex */
public final class KeyguardStatusBarViewController extends ViewController<KeyguardStatusBarView> {
    public static final AnimationProperties KEYGUARD_HUN_PROPERTIES;
    public final SystemStatusAnimationScheduler mAnimationScheduler;
    public final BatteryController mBatteryController;
    public boolean mBatteryListening;
    public final BatteryMeterViewController mBatteryMeterViewController;
    public final BiometricUnlockController mBiometricUnlockController;
    public final List<String> mBlockedIcons;
    public final CarrierTextController mCarrierTextController;
    public final ConfigurationController mConfigurationController;
    public boolean mDelayShowingKeyguardStatusBar;
    public boolean mDozing;
    public boolean mFirstBypassAttempt;
    public final AnimatableProperty.AnonymousClass6 mHeadsUpShowingAmountAnimation;
    public final StatusBarContentInsetsProvider mInsetsProvider;
    public final KeyguardBypassController mKeyguardBypassController;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final NotificationPanelViewController.NotificationPanelViewStateProvider mNotificationPanelViewStateProvider;
    public final int mNotificationsHeaderCollideDistance;
    public boolean mShowingKeyguardHeadsUp;
    public final StatusBarIconController mStatusBarIconController;
    public int mStatusBarState;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public final StatusBarUserInfoTracker mStatusBarUserInfoTracker;
    public StatusBarIconController.TintedIconManager mTintedIconManager;
    public final StatusBarIconController.TintedIconManager.Factory mTintedIconManagerFactory;
    public final UserInfoController mUserInfoController;
    public final UserManager mUserManager;
    public final StatusBarUserSwitcherController mUserSwitcherController;
    public float mKeyguardHeadsUpShowingAmount = 0.0f;
    public final AnonymousClass1 mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController.1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onConfigChanged(Configuration configuration) {
            KeyguardStatusBarViewController.this.updateUserSwitcher();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onDensityOrFontScaleChanged() {
            ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).loadDimens();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onThemeChanged() {
            ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).onOverlayChanged();
            KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
            Objects.requireNonNull(keyguardStatusBarViewController);
            ((KeyguardStatusBarView) keyguardStatusBarViewController.mView).onThemeChanged(keyguardStatusBarViewController.mTintedIconManager);
        }
    };
    public final AnonymousClass2 mAnimationCallback = new SystemStatusAnimationCallback() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController.2
        @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
        public final void onSystemChromeAnimationEnd() {
            KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
            KeyguardStatusBarView keyguardStatusBarView = (KeyguardStatusBarView) keyguardStatusBarViewController.mView;
            SystemStatusAnimationScheduler systemStatusAnimationScheduler = keyguardStatusBarViewController.mAnimationScheduler;
            Objects.requireNonNull(systemStatusAnimationScheduler);
            boolean z = true;
            if (systemStatusAnimationScheduler.animationState != 1) {
                z = false;
            }
            Objects.requireNonNull(keyguardStatusBarView);
            if (z) {
                keyguardStatusBarView.mSystemIconsContainer.setVisibility(4);
                keyguardStatusBarView.mSystemIconsContainer.setAlpha(0.0f);
                return;
            }
            keyguardStatusBarView.mSystemIconsContainer.setAlpha(1.0f);
            keyguardStatusBarView.mSystemIconsContainer.setVisibility(0);
        }

        @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
        public final void onSystemChromeAnimationStart() {
            boolean z;
            KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
            KeyguardStatusBarView keyguardStatusBarView = (KeyguardStatusBarView) keyguardStatusBarViewController.mView;
            SystemStatusAnimationScheduler systemStatusAnimationScheduler = keyguardStatusBarViewController.mAnimationScheduler;
            Objects.requireNonNull(systemStatusAnimationScheduler);
            if (systemStatusAnimationScheduler.animationState == 3) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                keyguardStatusBarView.mSystemIconsContainer.setVisibility(0);
                keyguardStatusBarView.mSystemIconsContainer.setAlpha(0.0f);
                return;
            }
            Objects.requireNonNull(keyguardStatusBarView);
        }

        @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
        public final void onSystemChromeAnimationUpdate(ValueAnimator valueAnimator) {
            KeyguardStatusBarView keyguardStatusBarView = (KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView;
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            Objects.requireNonNull(keyguardStatusBarView);
            keyguardStatusBarView.mSystemIconsContainer.setAlpha(floatValue);
        }
    };
    public final AnonymousClass3 mBatteryStateChangeCallback = new BatteryController.BatteryStateChangeCallback() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController.3
        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public final void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            KeyguardStatusBarView keyguardStatusBarView = (KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView;
            Objects.requireNonNull(keyguardStatusBarView);
            if (keyguardStatusBarView.mBatteryCharging != z2) {
                keyguardStatusBarView.mBatteryCharging = z2;
                keyguardStatusBarView.updateVisibilities();
            }
        }
    };
    public final KeyguardStatusBarViewController$$ExternalSyntheticLambda3 mOnUserInfoChangedListener = new UserInfoController.OnUserInfoChangedListener() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController$$ExternalSyntheticLambda3
        @Override // com.android.systemui.statusbar.policy.UserInfoController.OnUserInfoChangedListener
        public final void onUserInfoChanged(String str, Drawable drawable) {
            KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
            Objects.requireNonNull(keyguardStatusBarViewController);
            KeyguardStatusBarView keyguardStatusBarView = (KeyguardStatusBarView) keyguardStatusBarViewController.mView;
            Objects.requireNonNull(keyguardStatusBarView);
            keyguardStatusBarView.mMultiUserAvatar.setImageDrawable(drawable);
        }
    };
    public final KeyguardStatusBarViewController$$ExternalSyntheticLambda0 mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController$$ExternalSyntheticLambda0
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
            Objects.requireNonNull(keyguardStatusBarViewController);
            keyguardStatusBarViewController.mKeyguardStatusBarAnimateAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            keyguardStatusBarViewController.updateViewState();
        }
    };
    public final AnonymousClass4 mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController.4
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
            if (keyguardStatusBarViewController.mFirstBypassAttempt && keyguardStatusBarViewController.mKeyguardUpdateMonitor.isUnlockingWithBiometricAllowed(z)) {
                KeyguardStatusBarViewController.this.mDelayShowingKeyguardStatusBar = true;
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            boolean z2;
            int i;
            KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
            int i2 = keyguardStatusBarViewController.mStatusBarState;
            boolean z3 = true;
            if (i2 == 1 || i2 == 2) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!z && keyguardStatusBarViewController.mFirstBypassAttempt && z2 && !keyguardStatusBarViewController.mDozing && !keyguardStatusBarViewController.mDelayShowingKeyguardStatusBar) {
                BiometricUnlockController biometricUnlockController = keyguardStatusBarViewController.mBiometricUnlockController;
                Objects.requireNonNull(biometricUnlockController);
                if (!(biometricUnlockController.isWakeAndUnlock() || (i = biometricUnlockController.mMode) == 5 || i == 7)) {
                    z3 = false;
                }
                if (!z3) {
                    KeyguardStatusBarViewController keyguardStatusBarViewController2 = KeyguardStatusBarViewController.this;
                    keyguardStatusBarViewController2.mFirstBypassAttempt = false;
                    keyguardStatusBarViewController2.animateKeyguardStatusBarIn();
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onFinishedGoingToSleep(int i) {
            KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
            keyguardStatusBarViewController.mFirstBypassAttempt = keyguardStatusBarViewController.mKeyguardBypassController.getBypassEnabled();
            KeyguardStatusBarViewController.this.mDelayShowingKeyguardStatusBar = false;
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardVisibilityChanged(boolean z) {
            if (z) {
                KeyguardStatusBarViewController.this.updateUserSwitcher();
            }
        }
    };
    public final AnonymousClass5 mStatusBarStateListener = new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController.5
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onStateChanged(int i) {
            KeyguardStatusBarViewController.this.mStatusBarState = i;
        }
    };
    public float mKeyguardStatusBarAnimateAlpha = 1.0f;

    /* JADX WARN: Type inference failed for: r3v3, types: [com.android.systemui.statusbar.phone.KeyguardStatusBarViewController$1] */
    /* JADX WARN: Type inference failed for: r3v4, types: [com.android.systemui.statusbar.phone.KeyguardStatusBarViewController$2] */
    /* JADX WARN: Type inference failed for: r3v5, types: [com.android.systemui.statusbar.phone.KeyguardStatusBarViewController$3] */
    /* JADX WARN: Type inference failed for: r3v6, types: [com.android.systemui.statusbar.phone.KeyguardStatusBarViewController$$ExternalSyntheticLambda3] */
    /* JADX WARN: Type inference failed for: r3v7, types: [com.android.systemui.statusbar.phone.KeyguardStatusBarViewController$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r3v8, types: [com.android.systemui.statusbar.phone.KeyguardStatusBarViewController$4] */
    /* JADX WARN: Type inference failed for: r3v9, types: [com.android.systemui.statusbar.phone.KeyguardStatusBarViewController$5] */
    public KeyguardStatusBarViewController(KeyguardStatusBarView keyguardStatusBarView, CarrierTextController carrierTextController, ConfigurationController configurationController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, BatteryController batteryController, UserInfoController userInfoController, StatusBarIconController statusBarIconController, StatusBarIconController.TintedIconManager.Factory factory, BatteryMeterViewController batteryMeterViewController, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider, KeyguardStateController keyguardStateController, KeyguardBypassController keyguardBypassController, KeyguardUpdateMonitor keyguardUpdateMonitor, BiometricUnlockController biometricUnlockController, SysuiStatusBarStateController sysuiStatusBarStateController, StatusBarContentInsetsProvider statusBarContentInsetsProvider, UserManager userManager, StatusBarUserSwitcherFeatureController statusBarUserSwitcherFeatureController, StatusBarUserSwitcherController statusBarUserSwitcherController, StatusBarUserInfoTracker statusBarUserInfoTracker) {
        super(keyguardStatusBarView);
        BiConsumer keyguardStatusBarViewController$$ExternalSyntheticLambda4 = new BiConsumer() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
                View view = (View) obj;
                Objects.requireNonNull(keyguardStatusBarViewController);
                keyguardStatusBarViewController.mKeyguardHeadsUpShowingAmount = ((Float) obj2).floatValue();
                keyguardStatusBarViewController.updateViewState();
            }
        };
        Function keyguardStatusBarViewController$$ExternalSyntheticLambda5 = new Function() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
                View view = (View) obj;
                Objects.requireNonNull(keyguardStatusBarViewController);
                return Float.valueOf(keyguardStatusBarViewController.mKeyguardHeadsUpShowingAmount);
            }
        };
        AnimatableProperty.AnonymousClass7 r5 = AnimatableProperty.Y;
        this.mHeadsUpShowingAmountAnimation = new AnimatableProperty.AnonymousClass6(2131428175, 2131428176, 2131428177, new AnimatableProperty.AnonymousClass5("KEYGUARD_HEADS_UP_SHOWING_AMOUNT", keyguardStatusBarViewController$$ExternalSyntheticLambda5, keyguardStatusBarViewController$$ExternalSyntheticLambda4));
        this.mCarrierTextController = carrierTextController;
        this.mConfigurationController = configurationController;
        this.mAnimationScheduler = systemStatusAnimationScheduler;
        this.mBatteryController = batteryController;
        this.mUserInfoController = userInfoController;
        this.mStatusBarIconController = statusBarIconController;
        this.mTintedIconManagerFactory = factory;
        this.mBatteryMeterViewController = batteryMeterViewController;
        this.mNotificationPanelViewStateProvider = notificationPanelViewStateProvider;
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mBiometricUnlockController = biometricUnlockController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mInsetsProvider = statusBarContentInsetsProvider;
        this.mUserManager = userManager;
        this.mUserSwitcherController = statusBarUserSwitcherController;
        this.mStatusBarUserInfoTracker = statusBarUserInfoTracker;
        this.mFirstBypassAttempt = keyguardBypassController.getBypassEnabled();
        keyguardStateController.addCallback(new KeyguardStateController.Callback() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController.6
            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public final void onKeyguardFadingAwayChanged() {
                if (!KeyguardStatusBarViewController.this.mKeyguardStateController.isKeyguardFadingAway()) {
                    KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
                    keyguardStatusBarViewController.mFirstBypassAttempt = false;
                    keyguardStatusBarViewController.mDelayShowingKeyguardStatusBar = false;
                }
            }
        });
        Resources resources = getResources();
        this.mBlockedIcons = Collections.unmodifiableList(Arrays.asList(resources.getString(17041565), resources.getString(17041534), resources.getString(17041537)));
        this.mNotificationsHeaderCollideDistance = resources.getDimensionPixelSize(2131165795);
        Objects.requireNonNull(statusBarUserSwitcherFeatureController);
        Objects.requireNonNull(keyguardStatusBarView);
        keyguardStatusBarView.mKeyguardUserAvatarEnabled = !statusBarUserSwitcherFeatureController.flags.isEnabled(Flags.STATUS_BAR_USER_SWITCHER);
        keyguardStatusBarView.updateVisibilities();
        statusBarUserSwitcherFeatureController.addCallback(new OnUserSwitcherPreferenceChangeListener() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController$$ExternalSyntheticLambda2
        });
    }

    static {
        AnimationProperties animationProperties = new AnimationProperties();
        animationProperties.duration = 360L;
        KEYGUARD_HUN_PROPERTIES = animationProperties;
    }

    public final void animateKeyguardStatusBarIn() {
        ((KeyguardStatusBarView) this.mView).setVisibility(0);
        ((KeyguardStatusBarView) this.mView).setAlpha(0.0f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(this.mAnimatorUpdateListener);
        ofFloat.setDuration(360L);
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ofFloat.start();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        this.mCarrierTextController.init();
        this.mBatteryMeterViewController.init();
        this.mUserSwitcherController.init();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mAnimationScheduler.addCallback((SystemStatusAnimationCallback) this.mAnimationCallback);
        this.mUserInfoController.addCallback(this.mOnUserInfoChangedListener);
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        if (this.mTintedIconManager == null) {
            StatusBarIconController.TintedIconManager.Factory factory = this.mTintedIconManagerFactory;
            Objects.requireNonNull(factory);
            StatusBarIconController.TintedIconManager tintedIconManager = new StatusBarIconController.TintedIconManager((ViewGroup) ((KeyguardStatusBarView) this.mView).findViewById(2131428922), factory.mFeatureFlags);
            this.mTintedIconManager = tintedIconManager;
            List<String> list = this.mBlockedIcons;
            tintedIconManager.mBlockList.clear();
            if (list != null && !list.isEmpty()) {
                tintedIconManager.mBlockList.addAll(list);
            }
            this.mStatusBarIconController.addIconGroup(this.mTintedIconManager);
        }
        ((KeyguardStatusBarView) this.mView).setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController$$ExternalSyntheticLambda1
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                KeyguardStatusBarViewController keyguardStatusBarViewController = KeyguardStatusBarViewController.this;
                Objects.requireNonNull(keyguardStatusBarViewController);
                return ((KeyguardStatusBarView) keyguardStatusBarViewController.mView).updateWindowInsets(windowInsets, keyguardStatusBarViewController.mInsetsProvider);
            }
        });
        updateUserSwitcher();
        ((KeyguardStatusBarView) this.mView).onThemeChanged(this.mTintedIconManager);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mAnimationScheduler.removeCallback((SystemStatusAnimationCallback) this.mAnimationCallback);
        this.mUserInfoController.removeCallback(this.mOnUserInfoChangedListener);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        StatusBarIconController.TintedIconManager tintedIconManager = this.mTintedIconManager;
        if (tintedIconManager != null) {
            this.mStatusBarIconController.removeIconGroup(tintedIconManager);
        }
    }

    public final void setKeyguardUserSwitcherEnabled(boolean z) {
        KeyguardStatusBarView keyguardStatusBarView = (KeyguardStatusBarView) this.mView;
        Objects.requireNonNull(keyguardStatusBarView);
        keyguardStatusBarView.mKeyguardUserSwitcherEnabled = z;
        final StatusBarUserInfoTracker statusBarUserInfoTracker = this.mStatusBarUserInfoTracker;
        Objects.requireNonNull(statusBarUserInfoTracker);
        statusBarUserInfoTracker.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker$checkEnabled$1
            @Override // java.lang.Runnable
            public final void run() {
                StatusBarUserInfoTracker statusBarUserInfoTracker2 = StatusBarUserInfoTracker.this;
                Objects.requireNonNull(statusBarUserInfoTracker2);
                boolean z2 = statusBarUserInfoTracker2.userSwitcherEnabled;
                StatusBarUserInfoTracker statusBarUserInfoTracker3 = StatusBarUserInfoTracker.this;
                statusBarUserInfoTracker3.userSwitcherEnabled = statusBarUserInfoTracker3.userManager.isUserSwitcherEnabled();
                StatusBarUserInfoTracker statusBarUserInfoTracker4 = StatusBarUserInfoTracker.this;
                Objects.requireNonNull(statusBarUserInfoTracker4);
                if (z2 != statusBarUserInfoTracker4.userSwitcherEnabled) {
                    final StatusBarUserInfoTracker statusBarUserInfoTracker5 = StatusBarUserInfoTracker.this;
                    statusBarUserInfoTracker5.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker$checkEnabled$1.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            StatusBarUserInfoTracker statusBarUserInfoTracker6 = StatusBarUserInfoTracker.this;
                            Objects.requireNonNull(statusBarUserInfoTracker6);
                            Iterator it = statusBarUserInfoTracker6.listeners.iterator();
                            while (it.hasNext()) {
                                ((CurrentUserChipInfoUpdatedListener) it.next()).onStatusBarUserSwitcherSettingChanged();
                            }
                        }
                    });
                }
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0023  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateForHeadsUp() {
        /*
            r4 = this;
            int r0 = r4.mStatusBarState
            r1 = 0
            r2 = 1
            if (r0 != r2) goto L_0x0008
            r0 = r2
            goto L_0x0009
        L_0x0008:
            r0 = r1
        L_0x0009:
            if (r0 == 0) goto L_0x001e
            com.android.systemui.statusbar.phone.NotificationPanelViewController$NotificationPanelViewStateProvider r0 = r4.mNotificationPanelViewStateProvider
            com.android.systemui.statusbar.phone.NotificationPanelViewController$19 r0 = (com.android.systemui.statusbar.phone.NotificationPanelViewController.AnonymousClass19) r0
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = com.android.systemui.statusbar.phone.NotificationPanelViewController.this
            com.android.systemui.statusbar.phone.HeadsUpAppearanceController r0 = r0.mHeadsUpAppearanceController
            boolean r0 = r0.shouldBeVisible()
            if (r0 == 0) goto L_0x001e
            r0 = r2
            goto L_0x001f
        L_0x001e:
            r0 = r1
        L_0x001f:
            boolean r3 = r4.mShowingKeyguardHeadsUp
            if (r3 == r0) goto L_0x005b
            r4.mShowingKeyguardHeadsUp = r0
            int r3 = r4.mStatusBarState
            if (r3 != r2) goto L_0x002a
            r1 = r2
        L_0x002a:
            r3 = 0
            if (r1 == 0) goto L_0x003d
            T extends android.view.View r1 = r4.mView
            com.android.systemui.statusbar.phone.KeyguardStatusBarView r1 = (com.android.systemui.statusbar.phone.KeyguardStatusBarView) r1
            com.android.systemui.statusbar.notification.AnimatableProperty$6 r4 = r4.mHeadsUpShowingAmountAnimation
            if (r0 == 0) goto L_0x0037
            r3 = 1065353216(0x3f800000, float:1.0)
        L_0x0037:
            com.android.systemui.statusbar.notification.stack.AnimationProperties r0 = com.android.systemui.statusbar.phone.KeyguardStatusBarViewController.KEYGUARD_HUN_PROPERTIES
            com.android.systemui.statusbar.notification.PropertyAnimator.setProperty(r1, r4, r3, r0, r2)
            goto L_0x005b
        L_0x003d:
            T extends android.view.View r0 = r4.mView
            com.android.systemui.statusbar.phone.KeyguardStatusBarView r0 = (com.android.systemui.statusbar.phone.KeyguardStatusBarView) r0
            com.android.systemui.statusbar.notification.AnimatableProperty$6 r4 = r4.mHeadsUpShowingAmountAnimation
            int r1 = r4.getAnimatorTag()
            java.lang.Object r1 = r0.getTag(r1)
            android.animation.ValueAnimator r1 = (android.animation.ValueAnimator) r1
            if (r1 == 0) goto L_0x0052
            r1.cancel()
        L_0x0052:
            android.util.Property r4 = r4.val$property
            java.lang.Float r1 = java.lang.Float.valueOf(r3)
            r4.set(r0, r1)
        L_0x005b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController.updateForHeadsUp():void");
    }

    public final void updateUserSwitcher() {
        KeyguardStatusBarView keyguardStatusBarView = (KeyguardStatusBarView) this.mView;
        boolean isUserSwitcherEnabled = this.mUserManager.isUserSwitcherEnabled(getResources().getBoolean(2131034220));
        Objects.requireNonNull(keyguardStatusBarView);
        keyguardStatusBarView.mIsUserSwitcherEnabled = isUserSwitcherEnabled;
    }

    public final void updateViewState() {
        boolean z;
        float f;
        boolean z2;
        int i;
        float f2;
        int i2 = 0;
        boolean z3 = true;
        if (this.mStatusBarState == 1) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            NotificationPanelViewController.AnonymousClass19 r0 = (NotificationPanelViewController.AnonymousClass19) this.mNotificationPanelViewStateProvider;
            Objects.requireNonNull(r0);
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            if (notificationPanelViewController.mTransitioningToFullShadeProgress > 0.0f) {
                LockscreenShadeTransitionController lockscreenShadeTransitionController = notificationPanelViewController.mLockscreenShadeTransitionController;
                Objects.requireNonNull(lockscreenShadeTransitionController);
                f = lockscreenShadeTransitionController.qSDragProgress;
            } else {
                f = notificationPanelViewController.computeQsExpansionFraction();
            }
            float min = 1.0f - Math.min(1.0f, f * 2.0f);
            if (this.mStatusBarState == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                NotificationPanelViewController.AnonymousClass19 r5 = (NotificationPanelViewController.AnonymousClass19) this.mNotificationPanelViewStateProvider;
                Objects.requireNonNull(r5);
                NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
                Objects.requireNonNull(notificationPanelViewController2);
                f2 = notificationPanelViewController2.mExpandedHeight;
                i = ((KeyguardStatusBarView) this.mView).getHeight() + this.mNotificationsHeaderCollideDistance;
            } else {
                NotificationPanelViewController.AnonymousClass19 r52 = (NotificationPanelViewController.AnonymousClass19) this.mNotificationPanelViewStateProvider;
                Objects.requireNonNull(r52);
                NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
                Objects.requireNonNull(notificationPanelViewController3);
                f2 = notificationPanelViewController3.mExpandedHeight;
                i = ((KeyguardStatusBarView) this.mView).getHeight();
            }
            float min2 = (1.0f - this.mKeyguardHeadsUpShowingAmount) * Math.min((float) Math.pow(MathUtils.saturate(f2 / i), 0.75d), min) * this.mKeyguardStatusBarAnimateAlpha;
            if ((!this.mFirstBypassAttempt || !this.mKeyguardUpdateMonitor.shouldListenForFace()) && !this.mDelayShowingKeyguardStatusBar) {
                z3 = false;
            }
            if (min2 == 0.0f || this.mDozing || z3) {
                i2 = 4;
            }
            ((KeyguardStatusBarView) this.mView).setAlpha(min2);
            ((KeyguardStatusBarView) this.mView).setVisibility(i2);
        }
    }
}
