package com.android.keyguard;

import android.app.AlertDialog;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import android.view.MotionEvent;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.keyguard.KeyguardHostViewController;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.DejankUtils;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.log.SessionTracker;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.settings.GlobalSettings;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class KeyguardSecurityContainerController extends ViewController<KeyguardSecurityContainer> implements KeyguardSecurityView {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public final AdminSecondaryLockScreenController mAdminSecondaryLockScreenController;
    public final ConfigurationController mConfigurationController;
    public final FalsingCollector mFalsingCollector;
    public final FalsingManager mFalsingManager;
    public final FeatureFlags mFeatureFlags;
    public final GlobalSettings mGlobalSettings;
    public AnonymousClass2 mKeyguardSecurityCallback;
    public final KeyguardStateController mKeyguardStateController;
    public int mLastOrientation;
    public final LockPatternUtils mLockPatternUtils;
    public final MetricsLogger mMetricsLogger;
    public final KeyguardSecurityContainer.SecurityCallback mSecurityCallback;
    public final KeyguardSecurityModel mSecurityModel;
    public final KeyguardSecurityViewFlipperController mSecurityViewFlipperController;
    public final SessionTracker mSessionTracker;
    public final UiEventLogger mUiEventLogger;
    public final KeyguardUpdateMonitor mUpdateMonitor;
    public final UserSwitcherController mUserSwitcherController;
    public KeyguardSecurityModel.SecurityMode mCurrentSecurityMode = KeyguardSecurityModel.SecurityMode.Invalid;
    @VisibleForTesting
    public final Gefingerpoken mGlobalTouchListener = new Gefingerpoken() { // from class: com.android.keyguard.KeyguardSecurityContainerController.1
        public MotionEvent mTouchDown;

        @Override // com.android.systemui.Gefingerpoken
        public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // com.android.systemui.Gefingerpoken
        public final boolean onTouchEvent(MotionEvent motionEvent) {
            boolean isOneHandedModeLeftAligned;
            if (motionEvent.getActionMasked() == 0) {
                KeyguardSecurityContainer keyguardSecurityContainer = (KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView;
                Objects.requireNonNull(keyguardSecurityContainer);
                if (keyguardSecurityContainer.mCurrentMode == 1 && (((isOneHandedModeLeftAligned = ((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).isOneHandedModeLeftAligned()) && motionEvent.getX() > ((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).getWidth() / 2.0f) || (!isOneHandedModeLeftAligned && motionEvent.getX() <= ((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).getWidth() / 2.0f))) {
                    KeyguardSecurityContainerController.this.mFalsingCollector.avoidGesture();
                }
                MotionEvent motionEvent2 = this.mTouchDown;
                if (motionEvent2 != null) {
                    motionEvent2.recycle();
                    this.mTouchDown = null;
                }
                this.mTouchDown = MotionEvent.obtain(motionEvent);
                return false;
            } else if (this.mTouchDown == null) {
                return false;
            } else {
                if (motionEvent.getActionMasked() != 1 && motionEvent.getActionMasked() != 3) {
                    return false;
                }
                this.mTouchDown.recycle();
                this.mTouchDown = null;
                return false;
            }
        }
    };
    public AnonymousClass3 mSwipeListener = new AnonymousClass3();
    public AnonymousClass4 mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.keyguard.KeyguardSecurityContainerController.4
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onThemeChanged() {
            KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController = KeyguardSecurityContainerController.this.mSecurityViewFlipperController;
            Objects.requireNonNull(keyguardSecurityViewFlipperController);
            Iterator it = keyguardSecurityViewFlipperController.mChildren.iterator();
            while (it.hasNext()) {
                ((KeyguardInputViewController) it.next()).reloadColors();
            }
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onUiModeChanged() {
            KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController = KeyguardSecurityContainerController.this.mSecurityViewFlipperController;
            Objects.requireNonNull(keyguardSecurityViewFlipperController);
            Iterator it = keyguardSecurityViewFlipperController.mChildren.iterator();
            while (it.hasNext()) {
                ((KeyguardInputViewController) it.next()).reloadColors();
            }
        }
    };

    /* renamed from: com.android.keyguard.KeyguardSecurityContainerController$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements KeyguardSecurityCallback {
        @Override // com.android.keyguard.KeyguardSecurityCallback
        public final void dismiss(int i) {
            dismiss(i, false);
        }

        public AnonymousClass2() {
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public final void dismiss(int i, boolean z) {
            ((KeyguardHostViewController.AnonymousClass2) KeyguardSecurityContainerController.this.mSecurityCallback).dismiss(true, i, z);
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public final void onCancelClicked() {
            KeyguardHostViewController.AnonymousClass2 r0 = (KeyguardHostViewController.AnonymousClass2) KeyguardSecurityContainerController.this.mSecurityCallback;
            Objects.requireNonNull(r0);
            KeyguardHostViewController.this.mViewMediatorCallback.onCancelClicked();
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public final void onUserInput() {
            KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardSecurityContainerController.this.mUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            keyguardUpdateMonitor.stopListeningForFace();
        }

        /* JADX WARN: Code restructure failed: missing block: B:26:0x0071, code lost:
            if (r5 != (-10000)) goto L_0x0075;
         */
        /* JADX WARN: Removed duplicated region for block: B:29:0x0077  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x007f  */
        @Override // com.android.keyguard.KeyguardSecurityCallback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void reportUnlockAttempt(int r10, boolean r11, int r12) {
            /*
                Method dump skipped, instructions count: 243
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardSecurityContainerController.AnonymousClass2.reportUnlockAttempt(int, boolean, int):void");
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public final void reset() {
            KeyguardHostViewController.AnonymousClass2 r0 = (KeyguardHostViewController.AnonymousClass2) KeyguardSecurityContainerController.this.mSecurityCallback;
            Objects.requireNonNull(r0);
            KeyguardHostViewController.this.mViewMediatorCallback.resetKeyguard();
        }

        @Override // com.android.keyguard.KeyguardSecurityCallback
        public final void userActivity() {
            KeyguardSecurityContainer.SecurityCallback securityCallback = KeyguardSecurityContainerController.this.mSecurityCallback;
            if (securityCallback != null) {
                KeyguardHostViewController.AnonymousClass2 r0 = (KeyguardHostViewController.AnonymousClass2) securityCallback;
                Objects.requireNonNull(r0);
                KeyguardHostViewController.this.mViewMediatorCallback.userActivity();
            }
        }
    }

    /* renamed from: com.android.keyguard.KeyguardSecurityContainerController$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements KeyguardSecurityContainer.SwipeListener {
        public AnonymousClass3() {
        }
    }

    /* loaded from: classes.dex */
    public static class Factory {
        public final AdminSecondaryLockScreenController.Factory mAdminSecondaryLockScreenControllerFactory;
        public final ConfigurationController mConfigurationController;
        public final FalsingCollector mFalsingCollector;
        public final FalsingManager mFalsingManager;
        public final FeatureFlags mFeatureFlags;
        public final GlobalSettings mGlobalSettings;
        public final KeyguardSecurityModel mKeyguardSecurityModel;
        public final KeyguardStateController mKeyguardStateController;
        public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        public final LockPatternUtils mLockPatternUtils;
        public final MetricsLogger mMetricsLogger;
        public final KeyguardSecurityViewFlipperController mSecurityViewFlipperController;
        public final SessionTracker mSessionTracker;
        public final UiEventLogger mUiEventLogger;
        public final UserSwitcherController mUserSwitcherController;
        public final KeyguardSecurityContainer mView;

        public Factory(KeyguardSecurityContainer keyguardSecurityContainer, AdminSecondaryLockScreenController.Factory factory, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel keyguardSecurityModel, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController, ConfigurationController configurationController, FalsingCollector falsingCollector, FalsingManager falsingManager, UserSwitcherController userSwitcherController, FeatureFlags featureFlags, GlobalSettings globalSettings, SessionTracker sessionTracker) {
            this.mView = keyguardSecurityContainer;
            this.mAdminSecondaryLockScreenControllerFactory = factory;
            this.mLockPatternUtils = lockPatternUtils;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mKeyguardSecurityModel = keyguardSecurityModel;
            this.mMetricsLogger = metricsLogger;
            this.mUiEventLogger = uiEventLogger;
            this.mKeyguardStateController = keyguardStateController;
            this.mSecurityViewFlipperController = keyguardSecurityViewFlipperController;
            this.mConfigurationController = configurationController;
            this.mFalsingCollector = falsingCollector;
            this.mFalsingManager = falsingManager;
            this.mFeatureFlags = featureFlags;
            this.mGlobalSettings = globalSettings;
            this.mUserSwitcherController = userSwitcherController;
            this.mSessionTracker = sessionTracker;
        }
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.keyguard.KeyguardSecurityContainerController$4] */
    public KeyguardSecurityContainerController(KeyguardSecurityContainer keyguardSecurityContainer, AdminSecondaryLockScreenController.Factory factory, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel keyguardSecurityModel, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, KeyguardHostViewController.AnonymousClass2 r16, KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController, ConfigurationController configurationController, FalsingCollector falsingCollector, FalsingManager falsingManager, UserSwitcherController userSwitcherController, FeatureFlags featureFlags, GlobalSettings globalSettings, SessionTracker sessionTracker) {
        super(keyguardSecurityContainer);
        this.mLastOrientation = 0;
        AnonymousClass2 r2 = new AnonymousClass2();
        this.mKeyguardSecurityCallback = r2;
        this.mLockPatternUtils = lockPatternUtils;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mSecurityModel = keyguardSecurityModel;
        this.mMetricsLogger = metricsLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mKeyguardStateController = keyguardStateController;
        this.mSecurityCallback = r16;
        this.mSecurityViewFlipperController = keyguardSecurityViewFlipperController;
        Objects.requireNonNull(factory);
        this.mAdminSecondaryLockScreenController = new AdminSecondaryLockScreenController(factory.mContext, factory.mParent, factory.mUpdateMonitor, r2, factory.mHandler);
        this.mConfigurationController = configurationController;
        this.mLastOrientation = getResources().getConfiguration().orientation;
        this.mFalsingCollector = falsingCollector;
        this.mFalsingManager = falsingManager;
        this.mUserSwitcherController = userSwitcherController;
        this.mFeatureFlags = featureFlags;
        this.mGlobalSettings = globalSettings;
        this.mSessionTracker = sessionTracker;
    }

    public final void configureMode() {
        boolean z;
        boolean z2;
        KeyguardSecurityModel.SecurityMode securityMode = this.mCurrentSecurityMode;
        int i = 0;
        if (securityMode == KeyguardSecurityModel.SecurityMode.SimPin || securityMode == KeyguardSecurityModel.SecurityMode.SimPuk) {
            z = true;
        } else {
            z = false;
        }
        if (!this.mFeatureFlags.isEnabled(Flags.BOUNCER_USER_SWITCHER) || z) {
            KeyguardSecurityModel.SecurityMode securityMode2 = this.mCurrentSecurityMode;
            if (securityMode2 == KeyguardSecurityModel.SecurityMode.Pattern || securityMode2 == KeyguardSecurityModel.SecurityMode.PIN) {
                z2 = getResources().getBoolean(2131034116);
            } else {
                z2 = false;
            }
            if (z2) {
                i = 1;
            }
        } else {
            i = 2;
        }
        KeyguardSecurityContainer keyguardSecurityContainer = (KeyguardSecurityContainer) this.mView;
        GlobalSettings globalSettings = this.mGlobalSettings;
        FalsingManager falsingManager = this.mFalsingManager;
        UserSwitcherController userSwitcherController = this.mUserSwitcherController;
        Objects.requireNonNull(keyguardSecurityContainer);
        if (keyguardSecurityContainer.mCurrentMode != i) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Switching mode from ");
            m.append(KeyguardSecurityContainer.modeToString(keyguardSecurityContainer.mCurrentMode));
            m.append(" to ");
            m.append(KeyguardSecurityContainer.modeToString(i));
            Log.i("KeyguardSecurityView", m.toString());
            keyguardSecurityContainer.mCurrentMode = i;
            if (i == 1) {
                keyguardSecurityContainer.mViewMode = new KeyguardSecurityContainer.OneHandedViewMode();
            } else if (i != 2) {
                keyguardSecurityContainer.mViewMode = new KeyguardSecurityContainer.DefaultViewMode();
            } else {
                keyguardSecurityContainer.mViewMode = new KeyguardSecurityContainer.UserSwitcherViewMode();
            }
            keyguardSecurityContainer.mGlobalSettings = globalSettings;
            keyguardSecurityContainer.mFalsingManager = falsingManager;
            keyguardSecurityContainer.mUserSwitcherController = userSwitcherController;
            KeyguardSecurityViewFlipper keyguardSecurityViewFlipper = keyguardSecurityContainer.mSecurityViewFlipper;
            if (keyguardSecurityViewFlipper != null && globalSettings != null && falsingManager != null && userSwitcherController != null) {
                keyguardSecurityContainer.mViewMode.init(keyguardSecurityContainer, globalSettings, keyguardSecurityViewFlipper, falsingManager, userSwitcherController);
            }
        }
    }

    public final KeyguardInputViewController<KeyguardInputView> getCurrentSecurityController() {
        return this.mSecurityViewFlipperController.getSecurityView(this.mCurrentSecurityMode, this.mKeyguardSecurityCallback);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        this.mSecurityViewFlipperController.init();
    }

    public final void onPause() {
        this.mAdminSecondaryLockScreenController.hide();
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().onPause();
        }
        KeyguardSecurityContainer keyguardSecurityContainer = (KeyguardSecurityContainer) this.mView;
        Objects.requireNonNull(keyguardSecurityContainer);
        AlertDialog alertDialog = keyguardSecurityContainer.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            keyguardSecurityContainer.mAlertDialog = null;
        }
        keyguardSecurityContainer.mSecurityViewFlipper.setWindowInsetsAnimationCallback(null);
        keyguardSecurityContainer.mViewMode.reset();
    }

    @Override // com.android.keyguard.KeyguardSecurityView
    public final void onStartingToHide() {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().onStartingToHide();
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        KeyguardSecurityContainer keyguardSecurityContainer = (KeyguardSecurityContainer) this.mView;
        AnonymousClass3 r1 = this.mSwipeListener;
        Objects.requireNonNull(keyguardSecurityContainer);
        keyguardSecurityContainer.mSwipeListener = r1;
        KeyguardSecurityContainer keyguardSecurityContainer2 = (KeyguardSecurityContainer) this.mView;
        Gefingerpoken gefingerpoken = this.mGlobalTouchListener;
        Objects.requireNonNull(keyguardSecurityContainer2);
        keyguardSecurityContainer2.mMotionEventListeners.add(gefingerpoken);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        KeyguardSecurityContainer keyguardSecurityContainer = (KeyguardSecurityContainer) this.mView;
        Gefingerpoken gefingerpoken = this.mGlobalTouchListener;
        Objects.requireNonNull(keyguardSecurityContainer);
        keyguardSecurityContainer.mMotionEventListeners.remove(gefingerpoken);
    }

    public final void showPrimarySecurityScreen(boolean z) {
        KeyguardSecurityModel.SecurityMode securityMode = (KeyguardSecurityModel.SecurityMode) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.keyguard.KeyguardSecurityContainerController$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                KeyguardSecurityContainerController keyguardSecurityContainerController = KeyguardSecurityContainerController.this;
                Objects.requireNonNull(keyguardSecurityContainerController);
                return keyguardSecurityContainerController.mSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser());
            }
        });
        if (DEBUG) {
            Log.v("KeyguardSecurityView", "showPrimarySecurityScreen(turningOff=" + z + ")");
        }
        showSecurityScreen(securityMode);
    }

    @VisibleForTesting
    public void showSecurityScreen(KeyguardSecurityModel.SecurityMode securityMode) {
        boolean z;
        if (DEBUG) {
            Log.d("KeyguardSecurityView", "showSecurityScreen(" + securityMode + ")");
        }
        if (securityMode != KeyguardSecurityModel.SecurityMode.Invalid && securityMode != this.mCurrentSecurityMode) {
            KeyguardInputViewController<KeyguardInputView> currentSecurityController = getCurrentSecurityController();
            if (currentSecurityController != null) {
                currentSecurityController.onPause();
            }
            this.mCurrentSecurityMode = securityMode;
            KeyguardInputViewController<KeyguardInputView> currentSecurityController2 = getCurrentSecurityController();
            if (currentSecurityController2 != null) {
                currentSecurityController2.onResume(2);
                KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController = this.mSecurityViewFlipperController;
                Objects.requireNonNull(keyguardSecurityViewFlipperController);
                int indexOfChild = ((KeyguardSecurityViewFlipper) keyguardSecurityViewFlipperController.mView).indexOfChild(currentSecurityController2.mView);
                if (indexOfChild != -1) {
                    ((KeyguardSecurityViewFlipper) keyguardSecurityViewFlipperController.mView).setDisplayedChild(indexOfChild);
                }
                configureMode();
            }
            KeyguardSecurityContainer.SecurityCallback securityCallback = this.mSecurityCallback;
            if (currentSecurityController2 == null || !currentSecurityController2.needsInput()) {
                z = false;
            } else {
                z = true;
            }
            KeyguardHostViewController.AnonymousClass2 r3 = (KeyguardHostViewController.AnonymousClass2) securityCallback;
            Objects.requireNonNull(r3);
            KeyguardHostViewController.this.mViewMediatorCallback.setNeedsInput(z);
        }
    }

    @Override // com.android.keyguard.KeyguardSecurityView
    public final boolean needsInput() {
        return getCurrentSecurityController().needsInput();
    }
}
