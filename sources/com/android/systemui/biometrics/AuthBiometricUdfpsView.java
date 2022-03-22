package com.android.systemui.biometrics;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.systemui.biometrics.AuthDialog;
import java.util.Objects;
/* loaded from: classes.dex */
public class AuthBiometricUdfpsView extends AuthBiometricFingerprintView {
    public UdfpsDialogMeasureAdapter mMeasureAdapter;

    public AuthBiometricUdfpsView(Context context) {
        this(context, null);
    }

    public AuthBiometricUdfpsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final void onLayoutInternal() {
        super.onLayoutInternal();
        UdfpsDialogMeasureAdapter udfpsDialogMeasureAdapter = this.mMeasureAdapter;
        Objects.requireNonNull(udfpsDialogMeasureAdapter);
        int i = udfpsDialogMeasureAdapter.mBottomSpacerHeight;
        GridLayoutManager$$ExternalSyntheticOutline1.m("bottomSpacerHeight: ", i, "AuthBiometricUdfpsView");
        if (i < 0) {
            float f = -i;
            ((FrameLayout) findViewById(2131427579)).setTranslationY(f);
            ((TextView) findViewById(2131428120)).setTranslationY(f);
        }
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final AuthDialog.LayoutParams onMeasureInternal(int i, int i2) {
        AuthDialog.LayoutParams onMeasureInternal = super.onMeasureInternal(i, i2);
        UdfpsDialogMeasureAdapter udfpsDialogMeasureAdapter = this.mMeasureAdapter;
        if (udfpsDialogMeasureAdapter != null) {
            return udfpsDialogMeasureAdapter.onMeasureInternal(i, i2, onMeasureInternal);
        }
        return onMeasureInternal;
    }
}
