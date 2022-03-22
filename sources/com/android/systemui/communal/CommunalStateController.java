package com.android.systemui.communal;

import com.android.systemui.statusbar.policy.CallbackController;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CommunalStateController implements CallbackController<Callback> {
    public final ArrayList<Callback> mCallbacks = new ArrayList<>();
    public boolean mCommunalViewOccluded;
    public boolean mCommunalViewShowing;

    /* loaded from: classes.dex */
    public interface Callback {
        default void onCommunalViewShowingChanged() {
        }
    }

    public final void addCallback(Callback callback) {
        Objects.requireNonNull(callback, "Callback must not be null. b/128895449");
        if (!this.mCallbacks.contains(callback)) {
            this.mCallbacks.add(callback);
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(Callback callback) {
        Callback callback2 = callback;
        Objects.requireNonNull(callback2, "Callback must not be null. b/128895449");
        this.mCallbacks.remove(callback2);
    }
}
