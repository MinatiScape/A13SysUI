package com.android.systemui.statusbar;

import android.app.Notification;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public final class NotificationUiAdjustment {
    public final boolean isConversation;
    public final List<Notification.Action> smartActions;
    public final List<CharSequence> smartReplies;

    /* JADX WARN: Removed duplicated region for block: B:56:0x00b5 A[LOOP:1: B:37:0x0072->B:56:0x00b5, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00bc A[LOOP:0: B:12:0x0017->B:60:0x00bc, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x00bb A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x00b8 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean areDifferent(java.util.List<android.app.Notification.Action> r11, java.util.List<android.app.Notification.Action> r12) {
        /*
            r0 = 0
            if (r11 != r12) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 1
            if (r11 == 0) goto L_0x00c1
            if (r12 != 0) goto L_0x000b
            goto L_0x00c1
        L_0x000b:
            int r2 = r11.size()
            int r3 = r12.size()
            if (r2 == r3) goto L_0x0016
            return r1
        L_0x0016:
            r2 = r0
        L_0x0017:
            int r3 = r11.size()
            if (r2 >= r3) goto L_0x00c0
            java.lang.Object r3 = r11.get(r2)
            android.app.Notification$Action r3 = (android.app.Notification.Action) r3
            java.lang.Object r4 = r12.get(r2)
            android.app.Notification$Action r4 = (android.app.Notification.Action) r4
            java.lang.CharSequence r5 = r3.title
            java.lang.CharSequence r6 = r4.title
            boolean r5 = android.text.TextUtils.equals(r5, r6)
            if (r5 != 0) goto L_0x0034
            return r1
        L_0x0034:
            android.graphics.drawable.Icon r5 = r3.getIcon()
            android.graphics.drawable.Icon r6 = r4.getIcon()
            if (r5 != r6) goto L_0x0040
            r5 = r0
            goto L_0x004c
        L_0x0040:
            if (r5 == 0) goto L_0x004b
            if (r6 != 0) goto L_0x0045
            goto L_0x004b
        L_0x0045:
            boolean r5 = r5.sameAs(r6)
            r5 = r5 ^ r1
            goto L_0x004c
        L_0x004b:
            r5 = r1
        L_0x004c:
            if (r5 == 0) goto L_0x004f
            return r1
        L_0x004f:
            android.app.PendingIntent r5 = r3.actionIntent
            android.app.PendingIntent r6 = r4.actionIntent
            boolean r5 = java.util.Objects.equals(r5, r6)
            if (r5 != 0) goto L_0x005a
            return r1
        L_0x005a:
            android.app.RemoteInput[] r3 = r3.getRemoteInputs()
            android.app.RemoteInput[] r4 = r4.getRemoteInputs()
            if (r3 != r4) goto L_0x0067
        L_0x0064:
            r3 = r0
            goto L_0x00b9
        L_0x0067:
            if (r3 == 0) goto L_0x00b8
            if (r4 != 0) goto L_0x006c
            goto L_0x00b8
        L_0x006c:
            int r5 = r3.length
            int r6 = r4.length
            if (r5 == r6) goto L_0x0071
            goto L_0x00b8
        L_0x0071:
            r5 = r0
        L_0x0072:
            int r6 = r3.length
            if (r5 >= r6) goto L_0x0064
            r6 = r3[r5]
            r7 = r4[r5]
            java.lang.CharSequence r8 = r6.getLabel()
            java.lang.CharSequence r9 = r7.getLabel()
            boolean r8 = android.text.TextUtils.equals(r8, r9)
            if (r8 != 0) goto L_0x0088
            goto L_0x00b8
        L_0x0088:
            java.lang.CharSequence[] r6 = r6.getChoices()
            java.lang.CharSequence[] r7 = r7.getChoices()
            if (r6 != r7) goto L_0x0093
            goto L_0x00af
        L_0x0093:
            if (r6 == 0) goto L_0x00b1
            if (r7 != 0) goto L_0x0098
            goto L_0x00b1
        L_0x0098:
            int r8 = r6.length
            int r9 = r7.length
            if (r8 == r9) goto L_0x009d
            goto L_0x00b1
        L_0x009d:
            r8 = r0
        L_0x009e:
            int r9 = r6.length
            if (r8 >= r9) goto L_0x00af
            r9 = r6[r8]
            r10 = r7[r8]
            boolean r9 = android.text.TextUtils.equals(r9, r10)
            if (r9 != 0) goto L_0x00ac
            goto L_0x00b1
        L_0x00ac:
            int r8 = r8 + 1
            goto L_0x009e
        L_0x00af:
            r6 = r0
            goto L_0x00b2
        L_0x00b1:
            r6 = r1
        L_0x00b2:
            if (r6 == 0) goto L_0x00b5
            goto L_0x00b8
        L_0x00b5:
            int r5 = r5 + 1
            goto L_0x0072
        L_0x00b8:
            r3 = r1
        L_0x00b9:
            if (r3 == 0) goto L_0x00bc
            return r1
        L_0x00bc:
            int r2 = r2 + 1
            goto L_0x0017
        L_0x00c0:
            return r0
        L_0x00c1:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationUiAdjustment.areDifferent(java.util.List, java.util.List):boolean");
    }

    public NotificationUiAdjustment(String str, List<Notification.Action> list, List<CharSequence> list2, boolean z) {
        this.smartActions = list == null ? Collections.emptyList() : list;
        this.smartReplies = list2 == null ? Collections.emptyList() : list2;
        this.isConversation = z;
    }
}
