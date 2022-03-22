package com.airbnb.lottie;

import android.view.View;
import androidx.core.view.ViewPropertyAnimatorListener;
/* loaded from: classes.dex */
public class L implements ViewPropertyAnimatorListener {
    public static int depthPastMaxDepth;

    @Override // androidx.core.view.ViewPropertyAnimatorListener
    public void onAnimationCancel(View view) {
    }

    @Override // androidx.core.view.ViewPropertyAnimatorListener
    public void onAnimationStart() {
    }

    public static void endSection() {
        int i = depthPastMaxDepth;
        if (i > 0) {
            depthPastMaxDepth = i - 1;
        }
    }
}
