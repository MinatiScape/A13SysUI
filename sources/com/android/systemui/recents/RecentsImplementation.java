package com.android.systemui.recents;

import android.content.Context;
/* loaded from: classes.dex */
public interface RecentsImplementation {
    default void hideRecentApps(boolean z, boolean z2) {
    }

    default void onStart(Context context) {
    }

    default void showRecentApps(boolean z) {
    }

    default void toggleRecentApps() {
    }
}
