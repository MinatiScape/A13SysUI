package com.android.systemui.keyguard;

import android.app.IActivityTaskManager;
import android.content.Context;
/* loaded from: classes.dex */
public final class WorkLockActivityController {
    public final Context mContext;
    public final IActivityTaskManager mIatm;
    public final AnonymousClass1 mLockListener;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.shared.system.TaskStackChangeListener, com.android.systemui.keyguard.WorkLockActivityController$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public WorkLockActivityController(android.content.Context r2, com.android.systemui.shared.system.TaskStackChangeListeners r3, android.app.IActivityTaskManager r4) {
        /*
            r1 = this;
            r1.<init>()
            com.android.systemui.keyguard.WorkLockActivityController$1 r0 = new com.android.systemui.keyguard.WorkLockActivityController$1
            r0.<init>()
            r1.mLockListener = r0
            r1.mContext = r2
            r1.mIatm = r4
            r3.registerTaskStackListener(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.WorkLockActivityController.<init>(android.content.Context, com.android.systemui.shared.system.TaskStackChangeListeners, android.app.IActivityTaskManager):void");
    }
}
