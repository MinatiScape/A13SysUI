package com.android.systemui.statusbar.phone.userswitcher;

import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.statusbar.policy.CallbackController;
import java.util.ArrayList;
/* compiled from: StatusBarUserSwitcherFeatureController.kt */
/* loaded from: classes.dex */
public final class StatusBarUserSwitcherFeatureController implements CallbackController<OnUserSwitcherPreferenceChangeListener> {
    public final FeatureFlags flags;
    public final ArrayList listeners = new ArrayList();

    public final void addCallback(OnUserSwitcherPreferenceChangeListener onUserSwitcherPreferenceChangeListener) {
        if (!this.listeners.contains(onUserSwitcherPreferenceChangeListener)) {
            this.listeners.add(onUserSwitcherPreferenceChangeListener);
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(OnUserSwitcherPreferenceChangeListener onUserSwitcherPreferenceChangeListener) {
        this.listeners.remove(onUserSwitcherPreferenceChangeListener);
    }

    public StatusBarUserSwitcherFeatureController(FeatureFlags featureFlags) {
        this.flags = featureFlags;
        featureFlags.addListener();
    }
}
