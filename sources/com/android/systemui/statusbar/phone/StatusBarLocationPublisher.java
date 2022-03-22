package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.policy.CallbackController;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StatusBarLocationPublisher.kt */
/* loaded from: classes.dex */
public final class StatusBarLocationPublisher implements CallbackController<StatusBarMarginUpdatedListener> {
    public final LinkedHashSet listeners = new LinkedHashSet();
    public int marginLeft;
    public int marginRight;

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(StatusBarMarginUpdatedListener statusBarMarginUpdatedListener) {
        this.listeners.add(new WeakReference(statusBarMarginUpdatedListener));
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(StatusBarMarginUpdatedListener statusBarMarginUpdatedListener) {
        StatusBarMarginUpdatedListener statusBarMarginUpdatedListener2 = statusBarMarginUpdatedListener;
        WeakReference weakReference = null;
        for (WeakReference weakReference2 : this.listeners) {
            if (Intrinsics.areEqual(weakReference2.get(), statusBarMarginUpdatedListener2)) {
                weakReference = weakReference2;
            }
        }
        if (weakReference != null) {
            this.listeners.remove(weakReference);
        }
    }
}
