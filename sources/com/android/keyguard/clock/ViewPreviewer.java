package com.android.keyguard.clock;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: classes.dex */
public final class ViewPreviewer {
    public final Handler mMainHandler = new Handler(Looper.getMainLooper());

    public static void dispatchVisibilityAggregated(View view, boolean z) {
        boolean z2;
        boolean z3 = true;
        if (view.getVisibility() == 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2 || !z) {
            view.onVisibilityAggregated(z);
        }
        if (view instanceof ViewGroup) {
            if (!z2 || !z) {
                z3 = false;
            }
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                dispatchVisibilityAggregated(viewGroup.getChildAt(i), z3);
            }
        }
    }
}
