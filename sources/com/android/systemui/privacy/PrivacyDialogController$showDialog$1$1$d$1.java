package com.android.systemui.privacy;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Intent;
import com.android.systemui.plugins.ActivityStarter;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: PrivacyDialogController.kt */
/* loaded from: classes.dex */
public /* synthetic */ class PrivacyDialogController$showDialog$1$1$d$1 extends FunctionReferenceImpl implements Function4<String, Integer, CharSequence, Intent, Unit> {
    public PrivacyDialogController$showDialog$1$1$d$1(PrivacyDialogController privacyDialogController) {
        super(4, privacyDialogController, PrivacyDialogController.class, "startActivity", "startActivity(Ljava/lang/String;ILjava/lang/CharSequence;Landroid/content/Intent;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function4
    public final void invoke(String str, Integer num, CharSequence charSequence, Intent intent) {
        Dialog dialog;
        int intValue = num.intValue();
        final PrivacyDialogController privacyDialogController = (PrivacyDialogController) this.receiver;
        Objects.requireNonNull(privacyDialogController);
        if (intent == null) {
            intent = PrivacyDialogController.getDefaultManageAppPermissionsIntent(str, intValue);
        }
        privacyDialogController.uiEventLogger.log(PrivacyDialogEvent.PRIVACY_DIALOG_ITEM_CLICKED_TO_APP_SETTINGS, intValue, str);
        privacyDialogController.privacyLogger.logStartSettingsActivityFromDialog(str, intValue);
        if (!privacyDialogController.keyguardStateController.isUnlocked() && (dialog = privacyDialogController.dialog) != null) {
            dialog.hide();
        }
        privacyDialogController.activityStarter.startActivity(intent, true, new ActivityStarter.Callback() { // from class: com.android.systemui.privacy.PrivacyDialogController$startActivity$1
            @Override // com.android.systemui.plugins.ActivityStarter.Callback
            public final void onActivityStarted(int i) {
                if (ActivityManager.isStartResultSuccessful(i)) {
                    PrivacyDialogController privacyDialogController2 = PrivacyDialogController.this;
                    Objects.requireNonNull(privacyDialogController2);
                    Dialog dialog2 = privacyDialogController2.dialog;
                    if (dialog2 != null) {
                        dialog2.dismiss();
                        return;
                    }
                    return;
                }
                Dialog dialog3 = PrivacyDialogController.this.dialog;
                if (dialog3 != null) {
                    dialog3.show();
                }
            }
        });
    }
}
