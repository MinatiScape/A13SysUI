package com.android.settingslib.widget;

import android.content.Intent;
import android.view.View;
import com.android.keyguard.KeyguardVisibilityHelper;
import com.android.systemui.biometrics.AuthBiometricView;
import com.android.systemui.clipboardoverlay.EditTextActivity;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.android.systemui.statusbar.notification.row.NotificationConversationInfo;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class LayoutPreference$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ LayoutPreference$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                LayoutPreference layoutPreference = (LayoutPreference) this.f$0;
                Objects.requireNonNull(layoutPreference);
                layoutPreference.performClick();
                return;
            case 1:
                AuthBiometricView.m29$r8$lambda$321443sPT2_vmgCOTtrDZ3nF1M((AuthBiometricView) this.f$0);
                return;
            case 2:
                EditTextActivity editTextActivity = (EditTextActivity) this.f$0;
                int i = EditTextActivity.$r8$clinit;
                Objects.requireNonNull(editTextActivity);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", editTextActivity.mEditText.getText());
                intent.setType("text/plain");
                editTextActivity.startActivity(Intent.createChooser(intent, null));
                return;
            case 3:
                LongScreenshotActivity.$r8$lambda$qOUpLbBDnQlAC3CzBI4dsyuYNs4((LongScreenshotActivity) this.f$0, view);
                return;
            case 4:
                NotificationConversationInfo notificationConversationInfo = (NotificationConversationInfo) this.f$0;
                int i2 = NotificationConversationInfo.$r8$clinit;
                Objects.requireNonNull(notificationConversationInfo);
                notificationConversationInfo.setSelectedAction(0);
                notificationConversationInfo.updateToggleActions(notificationConversationInfo.mSelectedAction, true);
                return;
            default:
                KeyguardQsUserSwitchController keyguardQsUserSwitchController = (KeyguardQsUserSwitchController) this.f$0;
                boolean z = KeyguardQsUserSwitchController.DEBUG;
                Objects.requireNonNull(keyguardQsUserSwitchController);
                if (!keyguardQsUserSwitchController.mFalsingManager.isFalseTap(1)) {
                    KeyguardVisibilityHelper keyguardVisibilityHelper = keyguardQsUserSwitchController.mKeyguardVisibilityHelper;
                    Objects.requireNonNull(keyguardVisibilityHelper);
                    if (!keyguardVisibilityHelper.mKeyguardViewVisibilityAnimating) {
                        keyguardQsUserSwitchController.mUiEventLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_SWITCH_USER_TAP);
                        keyguardQsUserSwitchController.mUserSwitchDialogController.showDialog(keyguardQsUserSwitchController.mView);
                        return;
                    }
                    return;
                }
                return;
        }
    }
}
