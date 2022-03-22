package com.android.systemui.accessibility;

import android.content.Context;
/* loaded from: classes.dex */
public final class AccessibilityButtonTargetsObserver extends SecureSettingsContentObserver<TargetsChangedListener> {

    /* loaded from: classes.dex */
    public interface TargetsChangedListener {
        void onAccessibilityButtonTargetsChanged(String str);
    }

    public AccessibilityButtonTargetsObserver(Context context) {
        super(context, "accessibility_button_targets");
    }

    @Override // com.android.systemui.accessibility.SecureSettingsContentObserver
    public final void onValueChanged(TargetsChangedListener targetsChangedListener, String str) {
        targetsChangedListener.onAccessibilityButtonTargetsChanged(str);
    }
}
