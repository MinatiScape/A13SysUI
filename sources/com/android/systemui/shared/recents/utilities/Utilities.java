package com.android.systemui.shared.recents.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.view.WindowManager;
/* loaded from: classes.dex */
public final class Utilities {
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0009, code lost:
        if (r3 != 3) goto L_0x0013;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int calculateBackDispositionHints(int r2, int r3, boolean r4, boolean r5) {
        /*
            r0 = 2
            if (r3 == 0) goto L_0x000f
            r1 = 1
            if (r3 == r1) goto L_0x000f
            if (r3 == r0) goto L_0x000f
            r1 = 3
            if (r3 == r1) goto L_0x000c
            goto L_0x0013
        L_0x000c:
            r2 = r2 & (-2)
            goto L_0x0013
        L_0x000f:
            if (r4 == 0) goto L_0x000c
            r2 = r2 | 1
        L_0x0013:
            if (r4 == 0) goto L_0x0017
            r2 = r2 | r0
            goto L_0x0019
        L_0x0017:
            r2 = r2 & (-3)
        L_0x0019:
            if (r5 == 0) goto L_0x001e
            r2 = r2 | 4
            goto L_0x0020
        L_0x001e:
            r2 = r2 & (-5)
        L_0x0020:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.shared.recents.utilities.Utilities.calculateBackDispositionHints(int, int, boolean, boolean):int");
    }

    @TargetApi(30)
    public static boolean isTablet(Context context) {
        Rect bounds = ((WindowManager) context.getSystemService(WindowManager.class)).getCurrentWindowMetrics().getBounds();
        if (Math.min(bounds.width(), bounds.height()) / (context.getResources().getConfiguration().densityDpi / 160.0f) >= 600.0f) {
            return true;
        }
        return false;
    }
}
