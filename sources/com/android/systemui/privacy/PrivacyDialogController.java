package com.android.systemui.privacy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.permission.PermissionManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function4;
/* compiled from: PrivacyDialogController.kt */
/* loaded from: classes.dex */
public final class PrivacyDialogController {
    public final ActivityStarter activityStarter;
    public final AppOpsController appOpsController;
    public final Executor backgroundExecutor;
    public Dialog dialog;
    public final DialogProvider dialogProvider;
    public final KeyguardStateController keyguardStateController;
    public final PrivacyDialogController$onDialogDismissed$1 onDialogDismissed = new PrivacyDialogController$onDialogDismissed$1(this);
    public final PackageManager packageManager;
    public final PermissionManager permissionManager;
    public final PrivacyItemController privacyItemController;
    public final PrivacyLogger privacyLogger;
    public final UiEventLogger uiEventLogger;
    public final Executor uiExecutor;
    public final UserTracker userTracker;

    /* compiled from: PrivacyDialogController.kt */
    /* loaded from: classes.dex */
    public interface DialogProvider {
        PrivacyDialog makeDialog(Context context, ArrayList arrayList, Function4 function4);
    }

    public static Intent getDefaultManageAppPermissionsIntent(String str, int i) {
        Intent intent = new Intent("android.intent.action.MANAGE_APP_PERMISSIONS");
        intent.putExtra("android.intent.extra.PACKAGE_NAME", str);
        intent.putExtra("android.intent.extra.USER", UserHandle.of(i));
        return intent;
    }

    public PrivacyDialogController(PermissionManager permissionManager, PackageManager packageManager, PrivacyItemController privacyItemController, UserTracker userTracker, ActivityStarter activityStarter, Executor executor, Executor executor2, PrivacyLogger privacyLogger, KeyguardStateController keyguardStateController, AppOpsController appOpsController, UiEventLogger uiEventLogger, DialogProvider dialogProvider) {
        this.permissionManager = permissionManager;
        this.packageManager = packageManager;
        this.privacyItemController = privacyItemController;
        this.userTracker = userTracker;
        this.activityStarter = activityStarter;
        this.backgroundExecutor = executor;
        this.uiExecutor = executor2;
        this.privacyLogger = privacyLogger;
        this.keyguardStateController = keyguardStateController;
        this.appOpsController = appOpsController;
        this.uiEventLogger = uiEventLogger;
        this.dialogProvider = dialogProvider;
    }
}
