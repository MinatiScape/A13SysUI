package com.android.systemui.controls;

import android.content.Context;
import android.content.pm.ServiceInfo;
import com.android.settingslib.applications.DefaultAppInfo;
/* compiled from: ControlsServiceInfo.kt */
/* loaded from: classes.dex */
public final class ControlsServiceInfo extends DefaultAppInfo {
    public final ServiceInfo serviceInfo;

    public ControlsServiceInfo(Context context, ServiceInfo serviceInfo) {
        super(context, context.getPackageManager(), context.getUserId(), serviceInfo.getComponentName());
        this.serviceInfo = serviceInfo;
    }
}
