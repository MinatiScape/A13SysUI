package com.android.systemui.biometrics;

import android.hardware.biometrics.SensorPropertiesInternal;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.util.List;
/* loaded from: classes.dex */
public final class Utils {
    public static boolean containsSensorId(List<? extends SensorPropertiesInternal> list, int i) {
        if (list == null) {
            return false;
        }
        for (SensorPropertiesInternal sensorPropertiesInternal : list) {
            if (sensorPropertiesInternal.sensorId == i) {
                return true;
            }
        }
        return false;
    }

    public static void notifyAccessibilityContentChanged(AccessibilityManager accessibilityManager, ViewGroup viewGroup) {
        if (accessibilityManager.isEnabled()) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain();
            obtain.setEventType(2048);
            obtain.setContentChangeTypes(1);
            viewGroup.sendAccessibilityEventUnchecked(obtain);
            viewGroup.notifySubtreeAccessibilityStateChanged(viewGroup, viewGroup, 1);
        }
    }
}
