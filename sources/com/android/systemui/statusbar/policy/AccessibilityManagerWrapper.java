package com.android.systemui.statusbar.policy;

import android.view.accessibility.AccessibilityManager;
/* loaded from: classes.dex */
public final class AccessibilityManagerWrapper implements CallbackController<AccessibilityManager.AccessibilityServicesStateChangeListener> {
    public final AccessibilityManager mAccessibilityManager;

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(AccessibilityManager.AccessibilityServicesStateChangeListener accessibilityServicesStateChangeListener) {
        this.mAccessibilityManager.addAccessibilityServicesStateChangeListener(accessibilityServicesStateChangeListener);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(AccessibilityManager.AccessibilityServicesStateChangeListener accessibilityServicesStateChangeListener) {
        this.mAccessibilityManager.removeAccessibilityServicesStateChangeListener(accessibilityServicesStateChangeListener);
    }

    public AccessibilityManagerWrapper(AccessibilityManager accessibilityManager) {
        this.mAccessibilityManager = accessibilityManager;
    }
}
