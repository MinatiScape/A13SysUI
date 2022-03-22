package com.android.wm.shell.onehanded;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
/* loaded from: classes.dex */
public final class OneHandedAccessibilityUtil {
    public final AccessibilityManager mAccessibilityManager;
    public String mDescription;
    public final String mPackageName;
    public final String mStartOneHandedDescription;
    public final String mStopOneHandedDescription;

    public final void announcementForScreenReader(String str) {
        if (this.mAccessibilityManager.isTouchExplorationEnabled()) {
            this.mDescription = str;
            AccessibilityEvent obtain = AccessibilityEvent.obtain();
            obtain.setPackageName(this.mPackageName);
            obtain.setEventType(16384);
            obtain.getText().add(this.mDescription);
            this.mAccessibilityManager.sendAccessibilityEvent(obtain);
        }
    }

    public OneHandedAccessibilityUtil(Context context) {
        this.mAccessibilityManager = AccessibilityManager.getInstance(context);
        this.mPackageName = context.getPackageName();
        this.mStartOneHandedDescription = context.getResources().getString(2131951670);
        this.mStopOneHandedDescription = context.getResources().getString(2131951671);
    }
}
