package com.android.systemui.statusbar;

import com.android.internal.statusbar.IStatusBarService;
import java.util.ArrayList;
import java.util.concurrent.Executor;
/* compiled from: NotificationClickNotifier.kt */
/* loaded from: classes.dex */
public final class NotificationClickNotifier {
    public final IStatusBarService barService;
    public final ArrayList listeners = new ArrayList();
    public final Executor mainExecutor;

    public NotificationClickNotifier(IStatusBarService iStatusBarService, Executor executor) {
        this.barService = iStatusBarService;
        this.mainExecutor = executor;
    }
}
