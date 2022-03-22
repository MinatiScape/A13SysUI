package com.android.keyguard;

import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityContainerController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.settings.GlobalSettings;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardHostViewController extends ViewController<KeyguardHostView> {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public final AudioManager mAudioManager;
    public Runnable mCancelAction;
    public ActivityStarter.OnDismissAction mDismissAction;
    public final KeyguardSecurityContainerController mKeyguardSecurityContainerController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final AnonymousClass2 mSecurityCallback;
    public final TelephonyManager mTelephonyManager;
    public final ViewMediatorCallback mViewMediatorCallback;
    public final AnonymousClass1 mUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.KeyguardHostViewController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onUserSwitchComplete(int i) {
            KeyguardHostViewController.this.mKeyguardSecurityContainerController.showPrimarySecurityScreen(false);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onTrustGrantedWithFlags(int i, int i2) {
            boolean z;
            if (i2 == KeyguardUpdateMonitor.getCurrentUser()) {
                boolean isVisibleToUser = ((KeyguardHostView) KeyguardHostViewController.this.mView).isVisibleToUser();
                boolean z2 = true;
                if ((i & 1) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                if ((i & 2) == 0) {
                    z2 = false;
                }
                if (!z && !z2) {
                    return;
                }
                if (!KeyguardHostViewController.this.mViewMediatorCallback.isScreenOn() || (!isVisibleToUser && !z2)) {
                    KeyguardHostViewController.this.mViewMediatorCallback.playTrustedSound();
                    return;
                }
                if (!isVisibleToUser) {
                    Log.i("KeyguardViewBase", "TrustAgent dismissed Keyguard.");
                }
                KeyguardHostViewController.this.mSecurityCallback.dismiss(false, i2, false);
            }
        }
    };
    public KeyguardHostViewController$$ExternalSyntheticLambda0 mOnKeyListener = new View.OnKeyListener() { // from class: com.android.keyguard.KeyguardHostViewController$$ExternalSyntheticLambda0
        @Override // android.view.View.OnKeyListener
        public final boolean onKey(View view, int i, KeyEvent keyEvent) {
            KeyguardHostViewController keyguardHostViewController = KeyguardHostViewController.this;
            Objects.requireNonNull(keyguardHostViewController);
            return keyguardHostViewController.interceptMediaKey(keyEvent);
        }
    };

    /* renamed from: com.android.keyguard.KeyguardHostViewController$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements KeyguardSecurityContainer.SecurityCallback {
        public AnonymousClass2() {
        }

        /* JADX WARN: Removed duplicated region for block: B:40:0x00cf  */
        /* JADX WARN: Removed duplicated region for block: B:47:0x00f1  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x0107  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x0120  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean dismiss(boolean r12, int r13, boolean r14) {
            /*
                Method dump skipped, instructions count: 297
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardHostViewController.AnonymousClass2.dismiss(boolean, int, boolean):boolean");
        }

        public final void finish(boolean z, int i) {
            boolean z2;
            ActivityStarter.OnDismissAction onDismissAction = KeyguardHostViewController.this.mDismissAction;
            if (onDismissAction != null) {
                z2 = onDismissAction.onDismiss();
                KeyguardHostViewController keyguardHostViewController = KeyguardHostViewController.this;
                keyguardHostViewController.mDismissAction = null;
                keyguardHostViewController.mCancelAction = null;
            } else {
                z2 = false;
            }
            ViewMediatorCallback viewMediatorCallback = KeyguardHostViewController.this.mViewMediatorCallback;
            if (viewMediatorCallback == null) {
                return;
            }
            if (z2) {
                viewMediatorCallback.keyguardDonePending(i);
            } else {
                viewMediatorCallback.keyguardDone(i);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [com.android.keyguard.KeyguardHostViewController$1] */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.keyguard.KeyguardHostViewController$$ExternalSyntheticLambda0] */
    public KeyguardHostViewController(KeyguardHostView keyguardHostView, KeyguardUpdateMonitor keyguardUpdateMonitor, AudioManager audioManager, TelephonyManager telephonyManager, ViewMediatorCallback viewMediatorCallback, KeyguardSecurityContainerController.Factory factory) {
        super(keyguardHostView);
        AnonymousClass2 r2 = new AnonymousClass2();
        this.mSecurityCallback = r2;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mAudioManager = audioManager;
        this.mTelephonyManager = telephonyManager;
        this.mViewMediatorCallback = viewMediatorCallback;
        Objects.requireNonNull(factory);
        this.mKeyguardSecurityContainerController = new KeyguardSecurityContainerController(factory.mView, factory.mAdminSecondaryLockScreenControllerFactory, factory.mLockPatternUtils, factory.mKeyguardUpdateMonitor, factory.mKeyguardSecurityModel, factory.mMetricsLogger, factory.mUiEventLogger, factory.mKeyguardStateController, r2, factory.mSecurityViewFlipperController, factory.mConfigurationController, factory.mFalsingCollector, factory.mFalsingManager, factory.mUserSwitcherController, factory.mFeatureFlags, factory.mGlobalSettings, factory.mSessionTracker);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        this.mKeyguardSecurityContainerController.init();
        updateResources();
    }

    public final void onResume() {
        GlobalSettings globalSettings;
        FalsingManager falsingManager;
        UserSwitcherController userSwitcherController;
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("screen on, instance ");
            m.append(Integer.toHexString(hashCode()));
            Log.d("KeyguardViewBase", m.toString());
        }
        KeyguardSecurityContainerController keyguardSecurityContainerController = this.mKeyguardSecurityContainerController;
        Objects.requireNonNull(keyguardSecurityContainerController);
        KeyguardSecurityModel.SecurityMode securityMode = keyguardSecurityContainerController.mCurrentSecurityMode;
        KeyguardSecurityModel.SecurityMode securityMode2 = KeyguardSecurityModel.SecurityMode.None;
        boolean z = true;
        if (securityMode != securityMode2) {
            int i = 2;
            KeyguardSecurityContainer keyguardSecurityContainer = (KeyguardSecurityContainer) keyguardSecurityContainerController.mView;
            Objects.requireNonNull(keyguardSecurityContainer);
            if (keyguardSecurityContainer.mCurrentMode == 1) {
                if (((KeyguardSecurityContainer) keyguardSecurityContainerController.mView).isOneHandedModeLeftAligned()) {
                    i = 3;
                } else {
                    i = 4;
                }
            }
            SysUiStatsLog.write(63, i);
            keyguardSecurityContainerController.getCurrentSecurityController().onResume(1);
        }
        KeyguardSecurityContainer keyguardSecurityContainer2 = (KeyguardSecurityContainer) keyguardSecurityContainerController.mView;
        KeyguardSecurityModel.SecurityMode securityMode3 = keyguardSecurityContainerController.mSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser());
        boolean isFaceAuthEnabled = keyguardSecurityContainerController.mKeyguardStateController.isFaceAuthEnabled();
        Objects.requireNonNull(keyguardSecurityContainer2);
        keyguardSecurityContainer2.mSecurityViewFlipper.setWindowInsetsAnimationCallback(keyguardSecurityContainer2.mWindowInsetsAnimationCallback);
        if (!isFaceAuthEnabled || securityMode3 == KeyguardSecurityModel.SecurityMode.SimPin || securityMode3 == KeyguardSecurityModel.SecurityMode.SimPuk || securityMode3 == securityMode2) {
            z = false;
        }
        keyguardSecurityContainer2.mSwipeUpToRetry = z;
        KeyguardSecurityViewFlipper keyguardSecurityViewFlipper = keyguardSecurityContainer2.mSecurityViewFlipper;
        if (!(keyguardSecurityViewFlipper == null || (globalSettings = keyguardSecurityContainer2.mGlobalSettings) == null || (falsingManager = keyguardSecurityContainer2.mFalsingManager) == null || (userSwitcherController = keyguardSecurityContainer2.mUserSwitcherController) == null)) {
            keyguardSecurityContainer2.mViewMode.init(keyguardSecurityContainer2, globalSettings, keyguardSecurityViewFlipper, falsingManager, userSwitcherController);
        }
        ((KeyguardHostView) this.mView).requestFocus();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        KeyguardHostView keyguardHostView = (KeyguardHostView) this.mView;
        ViewMediatorCallback viewMediatorCallback = this.mViewMediatorCallback;
        Objects.requireNonNull(keyguardHostView);
        keyguardHostView.mViewMediatorCallback = viewMediatorCallback;
        this.mViewMediatorCallback.setNeedsInput(this.mKeyguardSecurityContainerController.needsInput());
        this.mKeyguardUpdateMonitor.registerCallback(this.mUpdateCallback);
        ((KeyguardHostView) this.mView).setOnKeyListener(this.mOnKeyListener);
        this.mKeyguardSecurityContainerController.showPrimarySecurityScreen(false);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mUpdateCallback);
        ((KeyguardHostView) this.mView).setOnKeyListener(null);
    }

    public final void resetSecurityContainer() {
        KeyguardSecurityContainerController keyguardSecurityContainerController = this.mKeyguardSecurityContainerController;
        Objects.requireNonNull(keyguardSecurityContainerController);
        KeyguardSecurityContainer keyguardSecurityContainer = (KeyguardSecurityContainer) keyguardSecurityContainerController.mView;
        Objects.requireNonNull(keyguardSecurityContainer);
        keyguardSecurityContainer.mDisappearAnimRunning = false;
        KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController = keyguardSecurityContainerController.mSecurityViewFlipperController;
        Objects.requireNonNull(keyguardSecurityViewFlipperController);
        Iterator it = keyguardSecurityViewFlipperController.mChildren.iterator();
        while (it.hasNext()) {
            ((KeyguardInputViewController) it.next()).reset();
        }
    }

    public final void updateResources() {
        int i;
        Resources resources = ((KeyguardHostView) this.mView).getResources();
        if (resources.getBoolean(2131034116)) {
            i = resources.getInteger(2131492942);
        } else {
            i = resources.getInteger(2131492941);
        }
        if (((KeyguardHostView) this.mView).getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ((KeyguardHostView) this.mView).getLayoutParams();
            if (layoutParams.gravity != i) {
                layoutParams.gravity = i;
                ((KeyguardHostView) this.mView).setLayoutParams(layoutParams);
            }
        }
        KeyguardSecurityContainerController keyguardSecurityContainerController = this.mKeyguardSecurityContainerController;
        if (keyguardSecurityContainerController != null) {
            Objects.requireNonNull(keyguardSecurityContainerController);
            int i2 = keyguardSecurityContainerController.getResources().getConfiguration().orientation;
            if (i2 != keyguardSecurityContainerController.mLastOrientation) {
                keyguardSecurityContainerController.mLastOrientation = i2;
                keyguardSecurityContainerController.configureMode();
            }
        }
    }

    public final boolean interceptMediaKey(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyEvent.getAction() == 0) {
            if (!(keyCode == 79 || keyCode == 130 || keyCode == 222)) {
                if (!(keyCode == 126 || keyCode == 127)) {
                    switch (keyCode) {
                        case 85:
                            break;
                        case 86:
                        case 87:
                        case 88:
                        case 89:
                        case 90:
                        case 91:
                            break;
                        default:
                            return false;
                    }
                }
                TelephonyManager telephonyManager = this.mTelephonyManager;
                if (!(telephonyManager == null || telephonyManager.getCallState() == 0)) {
                    return true;
                }
            }
            this.mAudioManager.dispatchMediaKeyEvent(keyEvent);
            return true;
        } else if (keyEvent.getAction() != 1) {
            return false;
        } else {
            if (!(keyCode == 79 || keyCode == 130 || keyCode == 222 || keyCode == 126 || keyCode == 127)) {
                switch (keyCode) {
                    case 85:
                    case 86:
                    case 87:
                    case 88:
                    case 89:
                    case 90:
                    case 91:
                        break;
                    default:
                        return false;
                }
            }
            this.mAudioManager.dispatchMediaKeyEvent(keyEvent);
            return true;
        }
    }
}
