package com.android.systemui.qs.tiles.dialog;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.android.systemui.keyguard.KeyguardIndication;
import com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class InternetDialog$$ExternalSyntheticLambda10 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ InternetDialog$$ExternalSyntheticLambda10(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                InternetDialog internetDialog = (InternetDialog) this.f$0;
                Objects.requireNonNull(internetDialog);
                internetDialog.mSignalIcon.setImageDrawable((Drawable) this.f$1);
                return;
            case 1:
                KeyguardIndicationController keyguardIndicationController = (KeyguardIndicationController) this.f$0;
                CharSequence charSequence = (CharSequence) this.f$1;
                Objects.requireNonNull(keyguardIndicationController);
                if (keyguardIndicationController.mKeyguardStateController.isShowing()) {
                    KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = keyguardIndicationController.mRotateTextViewController;
                    ColorStateList colorStateList = keyguardIndicationController.mInitialTextColorState;
                    if (TextUtils.isEmpty(charSequence)) {
                        throw new IllegalStateException("message or icon must be set");
                    } else if (colorStateList != null) {
                        keyguardIndicationRotateTextViewController.updateIndication(1, new KeyguardIndication(charSequence, colorStateList, null, null, null, null), false);
                        return;
                    } else {
                        throw new IllegalStateException("text color must be set");
                    }
                } else {
                    return;
                }
            default:
                NotificationIconAreaController notificationIconAreaController = (NotificationIconAreaController) this.f$0;
                int i = NotificationIconAreaController.$r8$clinit;
                Objects.requireNonNull(notificationIconAreaController);
                notificationIconAreaController.updateTintForIcon((StatusBarIconView) this.f$1, notificationIconAreaController.mAodIconTint);
                return;
        }
    }
}
