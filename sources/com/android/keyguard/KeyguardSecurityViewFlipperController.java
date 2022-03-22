package com.android.keyguard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardInputViewController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.util.ViewController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardSecurityViewFlipperController extends ViewController<KeyguardSecurityViewFlipper> {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public final ArrayList mChildren = new ArrayList();
    public final EmergencyButtonController.Factory mEmergencyButtonControllerFactory;
    public final KeyguardInputViewController.Factory mKeyguardSecurityViewControllerFactory;
    public final LayoutInflater mLayoutInflater;

    /* loaded from: classes.dex */
    public static class NullKeyguardInputViewController extends KeyguardInputViewController<KeyguardInputView> {
        public NullKeyguardInputViewController(KeyguardSecurityModel.SecurityMode securityMode, KeyguardSecurityCallback keyguardSecurityCallback, EmergencyButtonController emergencyButtonController) {
            super(null, securityMode, keyguardSecurityCallback, emergencyButtonController);
        }

        @Override // com.android.keyguard.KeyguardSecurityView
        public final boolean needsInput() {
            return false;
        }

        @Override // com.android.keyguard.KeyguardSecurityView
        public final void onStartingToHide() {
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
    }

    @VisibleForTesting
    public KeyguardInputViewController<KeyguardInputView> getSecurityView(KeyguardSecurityModel.SecurityMode securityMode, KeyguardSecurityCallback keyguardSecurityCallback) {
        KeyguardPasswordViewController keyguardPasswordViewController;
        int i;
        KeyguardInputViewController<KeyguardInputView> keyguardInputViewController;
        Iterator it = this.mChildren.iterator();
        while (true) {
            if (!it.hasNext()) {
                keyguardPasswordViewController = null;
                break;
            }
            keyguardPasswordViewController = (KeyguardInputViewController) it.next();
            Objects.requireNonNull(keyguardPasswordViewController);
            if (keyguardPasswordViewController.mSecurityMode == securityMode) {
                break;
            }
        }
        if (!(keyguardPasswordViewController != null || securityMode == KeyguardSecurityModel.SecurityMode.None || securityMode == KeyguardSecurityModel.SecurityMode.Invalid)) {
            int ordinal = securityMode.ordinal();
            if (ordinal == 2) {
                i = 2131624164;
            } else if (ordinal == 3) {
                i = 2131624163;
            } else if (ordinal == 4) {
                i = 2131624165;
            } else if (ordinal == 5) {
                i = 2131624168;
            } else if (ordinal != 6) {
                i = 0;
            } else {
                i = 2131624169;
            }
            if (i != 0) {
                if (DEBUG) {
                    Log.v("KeyguardSecurityView", "inflating id = " + i);
                }
                KeyguardInputView keyguardInputView = (KeyguardInputView) this.mLayoutInflater.inflate(i, (ViewGroup) this.mView, false);
                ((KeyguardSecurityViewFlipper) this.mView).addView(keyguardInputView);
                KeyguardInputViewController.Factory factory = this.mKeyguardSecurityViewControllerFactory;
                Objects.requireNonNull(factory);
                EmergencyButtonController.Factory factory2 = factory.mEmergencyButtonControllerFactory;
                Objects.requireNonNull(factory2);
                EmergencyButtonController emergencyButtonController = new EmergencyButtonController((EmergencyButton) keyguardInputView.findViewById(2131427906), factory2.mConfigurationController, factory2.mKeyguardUpdateMonitor, factory2.mTelephonyManager, factory2.mPowerManager, factory2.mActivityTaskManager, factory2.mShadeController, factory2.mTelecomManager, factory2.mMetricsLogger);
                if (keyguardInputView instanceof KeyguardPatternView) {
                    keyguardInputViewController = new KeyguardPatternViewController((KeyguardPatternView) keyguardInputView, factory.mKeyguardUpdateMonitor, securityMode, factory.mLockPatternUtils, keyguardSecurityCallback, factory.mLatencyTracker, factory.mFalsingCollector, emergencyButtonController, factory.mMessageAreaControllerFactory, factory.mDevicePostureController);
                } else if (keyguardInputView instanceof KeyguardPasswordView) {
                    keyguardPasswordViewController = new KeyguardPasswordViewController((KeyguardPasswordView) keyguardInputView, factory.mKeyguardUpdateMonitor, securityMode, factory.mLockPatternUtils, keyguardSecurityCallback, factory.mMessageAreaControllerFactory, factory.mLatencyTracker, factory.mInputMethodManager, emergencyButtonController, factory.mMainExecutor, factory.mResources, factory.mFalsingCollector);
                    keyguardPasswordViewController.init();
                    this.mChildren.add(keyguardPasswordViewController);
                } else if (keyguardInputView instanceof KeyguardPINView) {
                    keyguardInputViewController = new KeyguardPinViewController((KeyguardPINView) keyguardInputView, factory.mKeyguardUpdateMonitor, securityMode, factory.mLockPatternUtils, keyguardSecurityCallback, factory.mMessageAreaControllerFactory, factory.mLatencyTracker, factory.mLiftToActivateListener, emergencyButtonController, factory.mFalsingCollector, factory.mDevicePostureController);
                } else if (keyguardInputView instanceof KeyguardSimPinView) {
                    keyguardInputViewController = new KeyguardSimPinViewController((KeyguardSimPinView) keyguardInputView, factory.mKeyguardUpdateMonitor, securityMode, factory.mLockPatternUtils, keyguardSecurityCallback, factory.mMessageAreaControllerFactory, factory.mLatencyTracker, factory.mLiftToActivateListener, factory.mTelephonyManager, factory.mFalsingCollector, emergencyButtonController);
                } else if (keyguardInputView instanceof KeyguardSimPukView) {
                    keyguardInputViewController = new KeyguardSimPukViewController((KeyguardSimPukView) keyguardInputView, factory.mKeyguardUpdateMonitor, securityMode, factory.mLockPatternUtils, keyguardSecurityCallback, factory.mMessageAreaControllerFactory, factory.mLatencyTracker, factory.mLiftToActivateListener, factory.mTelephonyManager, factory.mFalsingCollector, emergencyButtonController);
                } else {
                    throw new RuntimeException("Unable to find controller for " + keyguardInputView);
                }
                keyguardPasswordViewController = keyguardInputViewController;
                keyguardPasswordViewController.init();
                this.mChildren.add(keyguardPasswordViewController);
            }
        }
        if (keyguardPasswordViewController != null) {
            return keyguardPasswordViewController;
        }
        EmergencyButtonController.Factory factory3 = this.mEmergencyButtonControllerFactory;
        Objects.requireNonNull(factory3);
        return new NullKeyguardInputViewController(securityMode, keyguardSecurityCallback, new EmergencyButtonController(null, factory3.mConfigurationController, factory3.mKeyguardUpdateMonitor, factory3.mTelephonyManager, factory3.mPowerManager, factory3.mActivityTaskManager, factory3.mShadeController, factory3.mTelecomManager, factory3.mMetricsLogger));
    }

    public KeyguardSecurityViewFlipperController(KeyguardSecurityViewFlipper keyguardSecurityViewFlipper, LayoutInflater layoutInflater, KeyguardInputViewController.Factory factory, EmergencyButtonController.Factory factory2) {
        super(keyguardSecurityViewFlipper);
        this.mKeyguardSecurityViewControllerFactory = factory;
        this.mLayoutInflater = layoutInflater;
        this.mEmergencyButtonControllerFactory = factory2;
    }
}
