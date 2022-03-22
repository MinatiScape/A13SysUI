package com.android.keyguard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.util.Objects;
/* loaded from: classes.dex */
public class KeyguardSimPukView extends KeyguardPinBasedInputView {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;

    public KeyguardSimPukView(Context context) {
        this(context, null);
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public final int getPasswordTextViewId() {
        return 2131428633;
    }

    @Override // com.android.keyguard.KeyguardPinBasedInputView, com.android.keyguard.KeyguardAbsKeyInputView
    public final int getPromptReasonStringRes(int i) {
        return 0;
    }

    public final String getPukPasswordErrorMessage(int i, boolean z, boolean z2) {
        String str;
        int i2;
        int i3;
        if (i == 0) {
            str = getContext().getString(2131952585);
        } else if (i > 0) {
            if (z) {
                i3 = 2131820550;
            } else {
                i3 = 2131820552;
            }
            str = getContext().getResources().getQuantityString(i3, i, Integer.valueOf(i));
        } else {
            if (z) {
                i2 = 2131952595;
            } else {
                i2 = 2131952583;
            }
            str = getContext().getString(i2);
        }
        if (z2) {
            str = getResources().getString(2131952597, str);
        }
        if (DEBUG) {
            Log.d("KeyguardSimPukView", "getPukPasswordErrorMessage: attemptsRemaining=" + i + " displayMessage=" + str);
        }
        return str;
    }

    @Override // com.android.keyguard.KeyguardInputView
    public final void startAppearAnimation() {
    }

    public KeyguardSimPukView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.keyguard.KeyguardPinBasedInputView, com.android.keyguard.KeyguardInputView
    public final String getTitle() {
        return getContext().getString(17040506);
    }

    @Override // com.android.keyguard.KeyguardPinBasedInputView, com.android.keyguard.KeyguardAbsKeyInputView, android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        View view = this.mEcaView;
        if (view instanceof EmergencyCarrierArea) {
            EmergencyCarrierArea emergencyCarrierArea = (EmergencyCarrierArea) view;
            Objects.requireNonNull(emergencyCarrierArea);
            emergencyCarrierArea.mCarrierText.setVisibility(0);
        }
    }
}
