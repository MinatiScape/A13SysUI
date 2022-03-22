package com.android.wm.shell.legacysplitscreen;

import android.view.View;
import android.view.WindowManager;
import com.android.wm.shell.common.SystemWindows;
/* loaded from: classes.dex */
public final class DividerWindowManager {
    public WindowManager.LayoutParams mLp;
    public final SystemWindows mSystemWindows;
    public View mView;

    /* JADX WARN: Removed duplicated region for block: B:14:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setSlippery(boolean r5) {
        /*
            r4 = this;
            r0 = 536870912(0x20000000, float:1.0842022E-19)
            if (r5 == 0) goto L_0x0011
            android.view.WindowManager$LayoutParams r1 = r4.mLp
            int r2 = r1.flags
            r3 = r2 & r0
            if (r3 != 0) goto L_0x0011
            r5 = r2 | r0
            r1.flags = r5
            goto L_0x0020
        L_0x0011:
            if (r5 != 0) goto L_0x0022
            android.view.WindowManager$LayoutParams r5 = r4.mLp
            int r1 = r5.flags
            r0 = r0 & r1
            if (r0 == 0) goto L_0x0022
            r0 = -536870913(0xffffffffdfffffff, float:-3.6893486E19)
            r0 = r0 & r1
            r5.flags = r0
        L_0x0020:
            r5 = 1
            goto L_0x0023
        L_0x0022:
            r5 = 0
        L_0x0023:
            if (r5 == 0) goto L_0x002e
            com.android.wm.shell.common.SystemWindows r5 = r4.mSystemWindows
            android.view.View r0 = r4.mView
            android.view.WindowManager$LayoutParams r4 = r4.mLp
            r5.updateViewLayout(r0, r4)
        L_0x002e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.legacysplitscreen.DividerWindowManager.setSlippery(boolean):void");
    }

    public DividerWindowManager(SystemWindows systemWindows) {
        this.mSystemWindows = systemWindows;
    }
}
