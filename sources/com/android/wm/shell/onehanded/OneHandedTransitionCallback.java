package com.android.wm.shell.onehanded;

import android.graphics.Rect;
/* loaded from: classes.dex */
public interface OneHandedTransitionCallback {
    default void onStartFinished(Rect rect) {
    }

    default void onStartTransition() {
    }

    default void onStopFinished(Rect rect) {
    }
}
