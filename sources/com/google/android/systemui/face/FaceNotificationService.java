package com.google.android.systemui.face;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
/* loaded from: classes.dex */
public final class FaceNotificationService {
    public FaceNotificationBroadcastReceiver mBroadcastReceiver;
    public Context mContext;
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public final AnonymousClass1 mKeyguardUpdateMonitorCallback;
    public boolean mNotificationQueued;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.systemui.face.FaceNotificationService$1, com.android.keyguard.KeyguardUpdateMonitorCallback] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public FaceNotificationService(android.content.Context r3) {
        /*
            r2 = this;
            r2.<init>()
            android.os.Handler r0 = new android.os.Handler
            android.os.Looper r1 = android.os.Looper.getMainLooper()
            r0.<init>(r1)
            r2.mHandler = r0
            com.google.android.systemui.face.FaceNotificationService$1 r0 = new com.google.android.systemui.face.FaceNotificationService$1
            r0.<init>()
            r2.mKeyguardUpdateMonitorCallback = r0
            r2.mContext = r3
            java.lang.Class<com.android.keyguard.KeyguardUpdateMonitor> r3 = com.android.keyguard.KeyguardUpdateMonitor.class
            java.lang.Object r3 = com.android.systemui.Dependency.get(r3)
            com.android.keyguard.KeyguardUpdateMonitor r3 = (com.android.keyguard.KeyguardUpdateMonitor) r3
            r3.registerCallback(r0)
            com.google.android.systemui.face.FaceNotificationBroadcastReceiver r3 = new com.google.android.systemui.face.FaceNotificationBroadcastReceiver
            android.content.Context r0 = r2.mContext
            r3.<init>(r0)
            r2.mBroadcastReceiver = r3
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.face.FaceNotificationService.<init>(android.content.Context):void");
    }
}
