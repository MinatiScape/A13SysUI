package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import com.android.systemui.biometrics.AuthBiometricFaceView;
import com.android.systemui.biometrics.AuthBiometricView;
import com.android.systemui.biometrics.AuthContainerView;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.plugins.FalsingManager;
import java.util.Objects;
/* loaded from: classes.dex */
public class AuthBiometricFaceToFingerprintView extends AuthBiometricFaceView {
    public int mActiveSensorType = 8;
    public FingerprintSensorPropertiesInternal mFingerprintSensorProps;
    public ModalityListener mModalityListener;
    @VisibleForTesting
    public UdfpsIconController mUdfpsIconController;
    public UdfpsDialogMeasureAdapter mUdfpsMeasureAdapter;

    public AuthBiometricFaceToFingerprintView(Context context) {
        super(context);
    }

    /* loaded from: classes.dex */
    public static class UdfpsIconController extends AuthBiometricFaceView.IconController {
        public int mIconState = 0;

        @Override // com.android.systemui.biometrics.AuthBiometricFaceView.IconController
        public final void updateState(int i, int i2) {
            boolean z;
            if (i == 4 || i == 3) {
                z = true;
            } else {
                z = false;
            }
            switch (i2) {
                case 0:
                case 1:
                case 2:
                case 5:
                case FalsingManager.VERSION /* 6 */:
                    if (z) {
                        animateIcon(2131231708, false);
                    } else {
                        showStaticDrawable(2131231709);
                    }
                    this.mIconView.setContentDescription(this.mContext.getString(2131951721));
                    break;
                case 3:
                case 4:
                    if (!z) {
                        animateIcon(2131231709, false);
                    } else {
                        showStaticDrawable(2131231708);
                    }
                    this.mIconView.setContentDescription(this.mContext.getString(2131951948));
                    break;
                default:
                    KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Unknown biometric dialog state: ", i2, "BiometricPrompt/AuthBiometricFaceToFingerprintView");
                    break;
            }
            this.mState = i2;
            this.mIconState = i2;
        }

        public UdfpsIconController(Context context, ImageView imageView) {
            super(context, imageView);
        }
    }

    public final String checkErrorForFallback(String str) {
        if (this.mActiveSensorType != 8) {
            return str;
        }
        DialogFragment$$ExternalSyntheticOutline0.m("Falling back to fingerprint: ", str, "BiometricPrompt/AuthBiometricFaceToFingerprintView");
        ((AuthContainerView.BiometricCallback) this.mCallback).onAction(7);
        return ((LinearLayout) this).mContext.getString(2131952364);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricFaceView, com.android.systemui.biometrics.AuthBiometricView
    public final int getDelayAfterAuthenticatedDurationMs() {
        if (this.mActiveSensorType == 2) {
            return 0;
        }
        return 500;
    }

    @Override // com.android.systemui.biometrics.AuthBiometricFaceView, com.android.systemui.biometrics.AuthBiometricView
    public final int getStateForAfterError() {
        if (this.mActiveSensorType == 8) {
            return 2;
        }
        return 0;
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final void restoreState(Bundle bundle) {
        this.mSavedState = bundle;
        if (bundle != null) {
            this.mActiveSensorType = bundle.getInt("sensor_type", 8);
            this.mFingerprintSensorProps = bundle.getParcelable("sensor_props");
        }
    }

    @Override // com.android.systemui.biometrics.AuthBiometricFaceView, com.android.systemui.biometrics.AuthBiometricView
    public final void updateState(int i) {
        if (this.mActiveSensorType != 8) {
            UdfpsIconController udfpsIconController = this.mUdfpsIconController;
            Objects.requireNonNull(udfpsIconController);
            udfpsIconController.updateState(udfpsIconController.mIconState, i);
        } else if (i == 3 || i == 4) {
            this.mActiveSensorType = 2;
            this.mRequireConfirmation = false;
            this.mConfirmButton.setEnabled(false);
            this.mConfirmButton.setVisibility(8);
            ModalityListener modalityListener = this.mModalityListener;
            if (modalityListener != null) {
                AuthContainerView.this.maybeUpdatePositionForUdfps(true);
            }
            AuthBiometricFaceView.IconController iconController = this.mFaceIconController;
            Objects.requireNonNull(iconController);
            iconController.mDeactivated = true;
            UdfpsIconController udfpsIconController2 = this.mUdfpsIconController;
            Objects.requireNonNull(udfpsIconController2);
            udfpsIconController2.updateState(udfpsIconController2.mIconState, 0);
        }
        super.updateState(i);
    }

    public AuthBiometricFaceToFingerprintView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricFaceView, com.android.systemui.biometrics.AuthBiometricView
    public final void onAuthenticationFailed(int i, String str) {
        super.onAuthenticationFailed(i, checkErrorForFallback(str));
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final void onError(int i, String str) {
        super.onError(i, checkErrorForFallback(str));
    }

    @Override // com.android.systemui.biometrics.AuthBiometricFaceView, com.android.systemui.biometrics.AuthBiometricView, android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mUdfpsIconController = new UdfpsIconController(((LinearLayout) this).mContext, this.mIconView);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final AuthDialog.LayoutParams onMeasureInternal(int i, int i2) {
        AuthDialog.LayoutParams onMeasureInternal = super.onMeasureInternal(i, i2);
        if (!this.mFingerprintSensorProps.isAnyUdfpsType()) {
            return onMeasureInternal;
        }
        UdfpsDialogMeasureAdapter udfpsDialogMeasureAdapter = this.mUdfpsMeasureAdapter;
        if (udfpsDialogMeasureAdapter == null || udfpsDialogMeasureAdapter.mSensorProps != this.mFingerprintSensorProps) {
            this.mUdfpsMeasureAdapter = new UdfpsDialogMeasureAdapter(this, this.mFingerprintSensorProps);
        }
        return this.mUdfpsMeasureAdapter.onMeasureInternal(i, i2, onMeasureInternal);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final void onSaveState(Bundle bundle) {
        super.onSaveState(bundle);
        bundle.putInt("sensor_type", this.mActiveSensorType);
        bundle.putParcelable("sensor_props", this.mFingerprintSensorProps);
    }

    @VisibleForTesting
    public AuthBiometricFaceToFingerprintView(Context context, AttributeSet attributeSet, AuthBiometricView.Injector injector) {
        super(context, attributeSet, injector);
    }
}
