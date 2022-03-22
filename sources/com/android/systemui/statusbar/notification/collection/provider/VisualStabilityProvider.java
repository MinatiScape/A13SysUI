package com.android.systemui.statusbar.notification.collection.provider;

import android.util.ArraySet;
import com.android.systemui.util.ListenerSet;
import java.util.Iterator;
/* compiled from: VisualStabilityProvider.kt */
/* loaded from: classes.dex */
public final class VisualStabilityProvider {
    public final ListenerSet<OnReorderingAllowedListener> allListeners = new ListenerSet<>();
    public final ArraySet<OnReorderingAllowedListener> temporaryListeners = new ArraySet<>();
    public boolean isReorderingAllowed = true;

    public final void setReorderingAllowed(boolean z) {
        if (this.isReorderingAllowed != z) {
            this.isReorderingAllowed = z;
            if (z) {
                Iterator<OnReorderingAllowedListener> it = this.allListeners.iterator();
                while (it.hasNext()) {
                    OnReorderingAllowedListener next = it.next();
                    if (this.temporaryListeners.remove(next)) {
                        this.allListeners.remove(next);
                    }
                    next.onReorderingAllowed();
                }
            }
        }
    }
}
