package com.android.systemui.statusbar;

import android.content.res.ColorStateList;
import android.os.VibrationEffect;
import android.text.TextUtils;
import android.view.MotionEvent;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda5;
import com.android.systemui.keyguard.KeyguardIndication;
import com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController;
import com.android.systemui.recents.OverviewProxyService;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class VibratorHelper$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ VibratorHelper$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                VibratorHelper vibratorHelper = (VibratorHelper) this.f$0;
                Objects.requireNonNull(vibratorHelper);
                vibratorHelper.mVibrator.vibrate((VibrationEffect) this.f$1);
                return;
            case 1:
                OverviewProxyService.AnonymousClass1 r0 = (OverviewProxyService.AnonymousClass1) this.f$0;
                int i = OverviewProxyService.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r0);
                OverviewProxyService.this.mStatusBarOptionalLazy.get().ifPresent(new ScreenDecorations$$ExternalSyntheticLambda5(r0, (MotionEvent) this.f$1, 1));
                return;
            default:
                KeyguardIndicationController keyguardIndicationController = (KeyguardIndicationController) this.f$0;
                String str = (String) this.f$1;
                Objects.requireNonNull(keyguardIndicationController);
                if (TextUtils.isEmpty(str) || !keyguardIndicationController.mKeyguardStateController.isShowing()) {
                    keyguardIndicationController.mRotateTextViewController.hideIndication(0);
                    return;
                }
                KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = keyguardIndicationController.mRotateTextViewController;
                ColorStateList colorStateList = keyguardIndicationController.mInitialTextColorState;
                if (TextUtils.isEmpty(str)) {
                    throw new IllegalStateException("message or icon must be set");
                } else if (colorStateList != null) {
                    keyguardIndicationRotateTextViewController.updateIndication(0, new KeyguardIndication(str, colorStateList, null, null, null, null), false);
                    return;
                } else {
                    throw new IllegalStateException("text color must be set");
                }
        }
    }
}
