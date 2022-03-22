package com.android.systemui.biometrics;

import android.content.Context;
import android.util.AttributeSet;
/* loaded from: classes.dex */
public class AuthBiometricFingerprintView extends AuthBiometricView {
    public AuthBiometricFingerprintView(Context context) {
        this(context, null);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final int getDelayAfterAuthenticatedDurationMs() {
        return 0;
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final int getStateForAfterError() {
        return 2;
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final boolean supportsSmallDialog() {
        return false;
    }

    public AuthBiometricFingerprintView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final void handleResetAfterError() {
        this.mIndicatorView.setText(2131952363);
        this.mIndicatorView.setTextColor(this.mTextColorHint);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final void handleResetAfterHelp() {
        this.mIndicatorView.setText(2131952363);
        this.mIndicatorView.setTextColor(this.mTextColorHint);
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x0082, code lost:
        r5 = false;
     */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0026  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0045  */
    @Override // com.android.systemui.biometrics.AuthBiometricView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateState(int r10) {
        /*
            r9 = this;
            int r0 = r9.mState
            r1 = 2131231709(0x7f0803dd, float:1.8079507E38)
            r2 = 2
            r3 = 3
            r4 = 4
            r5 = 1
            r6 = 0
            if (r10 == r5) goto L_0x0017
            if (r10 == r2) goto L_0x0017
            if (r10 == r3) goto L_0x001e
            if (r10 == r4) goto L_0x001e
            r7 = 6
            if (r10 == r7) goto L_0x001e
            r1 = r6
            goto L_0x0024
        L_0x0017:
            if (r0 == r4) goto L_0x001b
            if (r0 != r3) goto L_0x001e
        L_0x001b:
            r1 = 2131231708(0x7f0803dc, float:1.8079505E38)
        L_0x001e:
            android.content.Context r7 = r9.mContext
            android.graphics.drawable.Drawable r1 = r7.getDrawable(r1)
        L_0x0024:
            if (r1 != 0) goto L_0x0045
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Animation not found, "
            r1.append(r2)
            r1.append(r0)
            java.lang.String r0 = " -> "
            r1.append(r0)
            r1.append(r10)
            java.lang.String r0 = r1.toString()
            java.lang.String r1 = "BiometricPrompt/AuthBiometricFingerprintView"
            android.util.Log.e(r1, r0)
            goto L_0x008b
        L_0x0045:
            boolean r7 = r1 instanceof android.graphics.drawable.AnimatedVectorDrawable
            if (r7 == 0) goto L_0x004d
            r7 = r1
            android.graphics.drawable.AnimatedVectorDrawable r7 = (android.graphics.drawable.AnimatedVectorDrawable) r7
            goto L_0x004e
        L_0x004d:
            r7 = r6
        L_0x004e:
            android.widget.ImageView r8 = r9.mIconView
            r8.setImageDrawable(r1)
            switch(r10) {
                case 0: goto L_0x0061;
                case 1: goto L_0x0061;
                case 2: goto L_0x0061;
                case 3: goto L_0x0057;
                case 4: goto L_0x0057;
                case 5: goto L_0x0061;
                case 6: goto L_0x0061;
                default: goto L_0x0056;
            }
        L_0x0056:
            goto L_0x006a
        L_0x0057:
            android.content.Context r1 = r9.mContext
            r6 = 2131951948(0x7f13014c, float:1.9540325E38)
            java.lang.String r6 = r1.getString(r6)
            goto L_0x006a
        L_0x0061:
            android.content.Context r1 = r9.mContext
            r6 = 2131951721(0x7f130069, float:1.9539865E38)
            java.lang.String r6 = r1.getString(r6)
        L_0x006a:
            if (r6 == 0) goto L_0x0071
            android.widget.ImageView r1 = r9.mIconView
            r1.setContentDescription(r6)
        L_0x0071:
            if (r7 == 0) goto L_0x008b
            r1 = 0
            if (r10 == r5) goto L_0x007d
            if (r10 == r2) goto L_0x007d
            if (r10 == r3) goto L_0x0083
            if (r10 == r4) goto L_0x0083
            goto L_0x0082
        L_0x007d:
            if (r0 == r4) goto L_0x0083
            if (r0 != r3) goto L_0x0082
            goto L_0x0083
        L_0x0082:
            r5 = r1
        L_0x0083:
            if (r5 == 0) goto L_0x008b
            r7.forceAnimationOnUI()
            r7.start()
        L_0x008b:
            super.updateState(r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.AuthBiometricFingerprintView.updateState(int):void");
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final void onAttachedToWindowInternal() {
        super.onAttachedToWindowInternal();
        this.mIndicatorView.setText(2131952363);
        this.mIndicatorView.setTextColor(this.mTextColorHint);
    }
}
