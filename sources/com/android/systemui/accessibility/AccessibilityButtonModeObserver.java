package com.android.systemui.accessibility;

import android.content.Context;
import android.util.Log;
/* loaded from: classes.dex */
public final class AccessibilityButtonModeObserver extends SecureSettingsContentObserver<ModeChangedListener> {

    /* loaded from: classes.dex */
    public interface ModeChangedListener {
        void onAccessibilityButtonModeChanged(int i);
    }

    public AccessibilityButtonModeObserver(Context context) {
        super(context, "accessibility_button_mode");
    }

    @Override // com.android.systemui.accessibility.SecureSettingsContentObserver
    public final void onValueChanged(ModeChangedListener modeChangedListener, String str) {
        modeChangedListener.onAccessibilityButtonModeChanged(parseAccessibilityButtonMode(str));
    }

    public static int parseAccessibilityButtonMode(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            Log.e("A11yButtonModeObserver", "Invalid string for  " + e);
            return 0;
        }
    }
}
