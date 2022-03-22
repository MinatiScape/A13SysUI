package com.android.systemui.biometrics;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.biometrics.AuthContainerView;
import com.google.android.systemui.ambientmusic.AmbientIndicationContainer;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AuthBiometricView$$ExternalSyntheticLambda4 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ ViewGroup f$0;

    public /* synthetic */ AuthBiometricView$$ExternalSyntheticLambda4(ViewGroup viewGroup, int i) {
        this.$r8$classId = i;
        this.f$0 = viewGroup;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                AuthBiometricView authBiometricView = (AuthBiometricView) this.f$0;
                int i = AuthBiometricView.$r8$clinit;
                Objects.requireNonNull(authBiometricView);
                ((AuthContainerView.BiometricCallback) authBiometricView.mCallback).onAction(2);
                return;
            default:
                int i2 = AmbientIndicationContainer.$r8$clinit;
                ((AmbientIndicationContainer) this.f$0).onTextClick(view);
                return;
        }
    }
}
