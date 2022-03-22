package com.android.systemui.controls.management;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.os.UserHandle;
import android.service.controls.Control;
import android.util.Log;
/* compiled from: ControlsRequestReceiver.kt */
/* loaded from: classes.dex */
public final class ControlsRequestReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        String str;
        int i;
        if (context.getPackageManager().hasSystemFeature("android.software.controls")) {
            try {
                ComponentName componentName = (ComponentName) intent.getParcelableExtra("android.intent.extra.COMPONENT_NAME");
                try {
                    Parcelable parcelable = (Control) intent.getParcelableExtra("android.service.controls.extra.CONTROL");
                    if (componentName == null) {
                        str = null;
                    } else {
                        str = componentName.getPackageName();
                    }
                    if (str != null) {
                        boolean z = false;
                        try {
                            int packageUid = context.getPackageManager().getPackageUid(str, 0);
                            ActivityManager activityManager = (ActivityManager) context.getSystemService(ActivityManager.class);
                            if (activityManager == null) {
                                i = 1000;
                            } else {
                                i = activityManager.getUidImportance(packageUid);
                            }
                            if (i != 100) {
                                Log.w("ControlsRequestReceiver", "Uid " + packageUid + " not in foreground");
                            } else {
                                z = true;
                            }
                        } catch (PackageManager.NameNotFoundException unused) {
                            Log.w("ControlsRequestReceiver", "Package " + str + " not found");
                        }
                        if (z) {
                            Intent intent2 = new Intent(context, ControlsRequestDialog.class);
                            intent2.putExtra("android.intent.extra.COMPONENT_NAME", componentName);
                            intent2.putExtra("android.service.controls.extra.CONTROL", parcelable);
                            intent2.addFlags(268566528);
                            intent2.putExtra("android.intent.extra.USER_ID", context.getUserId());
                            context.startActivityAsUser(intent2, UserHandle.SYSTEM);
                        }
                    }
                } catch (ClassCastException e) {
                    Log.e("ControlsRequestReceiver", "Malformed intent extra Control", e);
                }
            } catch (ClassCastException e2) {
                Log.e("ControlsRequestReceiver", "Malformed intent extra ComponentName", e2);
            }
        }
    }
}
