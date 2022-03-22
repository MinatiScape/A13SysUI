package com.google.android.systemui.assist;

import android.content.Context;
import android.os.ServiceManager;
import com.android.internal.widget.ILockSettings;
/* loaded from: classes.dex */
public final class OpaEnabledSettings {
    public final Context mContext;
    public final ILockSettings mLockSettings = ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings"));

    public OpaEnabledSettings(Context context) {
        this.mContext = context;
    }
}
