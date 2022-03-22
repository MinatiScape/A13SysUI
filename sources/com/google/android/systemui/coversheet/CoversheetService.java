package com.google.android.systemui.coversheet;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0;
import com.android.systemui.Dependency;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CoversheetService {
    public static final boolean DEBUG = Log.isLoggable("Coversheet", 3);
    public final String mBuildId;
    public final AnonymousClass1 mCallback;
    public final Context mContext;
    public boolean mKeyguardShowing;
    public boolean mUserUnlocked;

    /* renamed from: -$$Nest$mstartCoversheetIfNeeded  reason: not valid java name */
    public static void m167$$Nest$mstartCoversheetIfNeeded(CoversheetService coversheetService) {
        boolean z;
        boolean z2 = DEBUG;
        if (z2) {
            Objects.requireNonNull(coversheetService);
            StringBuilder sb = new StringBuilder();
            sb.append("mKeyguardShowing: ");
            sb.append(coversheetService.mKeyguardShowing);
            sb.append(", mUserUnlocked: ");
            KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(sb, coversheetService.mUserUnlocked, "Coversheet");
        }
        if (!coversheetService.mKeyguardShowing && coversheetService.mUserUnlocked) {
            ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.sInstance.getRunningTask();
            if (runningTask == null) {
                Log.w("Coversheet", "Not able to get any running task");
                return;
            }
            if (runningTask.configuration.windowConfiguration.getActivityType() == 2) {
                z = true;
            } else {
                z = false;
            }
            if (z2) {
                ViewCompat$$ExternalSyntheticLambda0.m("Going to home now? ", z, "Coversheet");
            }
            if (z) {
                Intent intent = new Intent("com.google.android.apps.tips.action.COVERSHEET");
                intent.setPackage("com.google.android.apps.tips");
                intent.setFlags(335544320);
                try {
                    coversheetService.mContext.startActivity(intent);
                } catch (ActivityNotFoundException unused) {
                    Log.w("Coversheet", "Coversheet was not found");
                }
                Settings.System.putString(coversheetService.mContext.getContentResolver(), "coversheet_id", coversheetService.mBuildId);
                ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).removeCallback(coversheetService.mCallback);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.keyguard.KeyguardUpdateMonitorCallback, com.google.android.systemui.coversheet.CoversheetService$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public CoversheetService(android.content.Context r5) {
        /*
            r4 = this;
            r4.<init>()
            com.google.android.systemui.coversheet.CoversheetService$1 r0 = new com.google.android.systemui.coversheet.CoversheetService$1
            r0.<init>()
            r4.mCallback = r0
            java.lang.String r1 = android.os.Build.ID
            java.lang.String r2 = "\\."
            java.lang.String[] r1 = r1.split(r2)
            r2 = 0
            r1 = r1[r2]
            r4.mBuildId = r1
            r4.mContext = r5
            java.lang.Class<com.android.systemui.statusbar.policy.DeviceProvisionedController> r4 = com.android.systemui.statusbar.policy.DeviceProvisionedController.class
            java.lang.Object r4 = com.android.systemui.Dependency.get(r4)
            com.android.systemui.statusbar.policy.DeviceProvisionedController r4 = (com.android.systemui.statusbar.policy.DeviceProvisionedController) r4
            boolean r4 = r4.isDeviceProvisioned()
            java.lang.String r2 = "Coversheet"
            java.lang.String r3 = "coversheet_id"
            if (r4 != 0) goto L_0x003c
            boolean r4 = com.google.android.systemui.coversheet.CoversheetService.DEBUG
            if (r4 == 0) goto L_0x0034
            java.lang.String r4 = "Store initial ID: "
            androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0.m(r4, r1, r2)
        L_0x0034:
            android.content.ContentResolver r4 = r5.getContentResolver()
            android.provider.Settings.System.putString(r4, r3, r1)
            goto L_0x005e
        L_0x003c:
            android.content.ContentResolver r4 = r5.getContentResolver()
            java.lang.String r4 = android.provider.Settings.System.getString(r4, r3)
            boolean r4 = android.text.TextUtils.equals(r1, r4)
            if (r4 != 0) goto L_0x005e
            boolean r4 = com.google.android.systemui.coversheet.CoversheetService.DEBUG
            if (r4 == 0) goto L_0x0053
            java.lang.String r4 = "Register callback."
            android.util.Log.d(r2, r4)
        L_0x0053:
            java.lang.Class<com.android.keyguard.KeyguardUpdateMonitor> r4 = com.android.keyguard.KeyguardUpdateMonitor.class
            java.lang.Object r4 = com.android.systemui.Dependency.get(r4)
            com.android.keyguard.KeyguardUpdateMonitor r4 = (com.android.keyguard.KeyguardUpdateMonitor) r4
            r4.registerCallback(r0)
        L_0x005e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.coversheet.CoversheetService.<init>(android.content.Context):void");
    }
}
