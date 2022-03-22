package com.android.wm.shell.bubbles;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda1(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x008d, code lost:
        if (r7.mRanking.isSuspended() != false) goto L_0x008f;
     */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void run() {
        /*
            r11 = this;
            int r0 = r11.$r8$classId
            switch(r0) {
                case 0: goto L_0x0007;
                default: goto L_0x0005;
            }
        L_0x0005:
            goto L_0x00ba
        L_0x0007:
            java.lang.Object r0 = r11.f$0
            com.android.wm.shell.bubbles.BubbleController$BubblesImpl r0 = (com.android.wm.shell.bubbles.BubbleController.BubblesImpl) r0
            java.lang.Object r1 = r11.f$1
            android.service.notification.NotificationListenerService$RankingMap r1 = (android.service.notification.NotificationListenerService.RankingMap) r1
            java.lang.Object r11 = r11.f$2
            java.util.HashMap r11 = (java.util.HashMap) r11
            java.util.Objects.requireNonNull(r0)
            com.android.wm.shell.bubbles.BubbleController r0 = com.android.wm.shell.bubbles.BubbleController.this
            java.util.Objects.requireNonNull(r0)
            android.service.notification.NotificationListenerService$Ranking r2 = r0.mTmpRanking
            if (r2 != 0) goto L_0x0026
            android.service.notification.NotificationListenerService$Ranking r2 = new android.service.notification.NotificationListenerService$Ranking
            r2.<init>()
            r0.mTmpRanking = r2
        L_0x0026:
            java.lang.String[] r2 = r1.getOrderedKeys()
            r3 = 0
            r4 = r3
        L_0x002c:
            int r5 = r2.length
            if (r4 >= r5) goto L_0x00b9
            r5 = r2[r4]
            java.lang.Object r6 = r11.get(r5)
            android.util.Pair r6 = (android.util.Pair) r6
            java.lang.Object r7 = r6.first
            com.android.wm.shell.bubbles.BubbleEntry r7 = (com.android.wm.shell.bubbles.BubbleEntry) r7
            java.lang.Object r6 = r6.second
            java.lang.Boolean r6 = (java.lang.Boolean) r6
            boolean r6 = r6.booleanValue()
            r8 = 1
            if (r7 == 0) goto L_0x0064
            android.service.notification.StatusBarNotification r9 = r7.mSbn
            android.os.UserHandle r9 = r9.getUser()
            int r9 = r9.getIdentifier()
            r10 = -1
            if (r9 == r10) goto L_0x0060
            android.util.SparseArray<android.content.pm.UserInfo> r10 = r0.mCurrentProfiles
            if (r10 == 0) goto L_0x005e
            java.lang.Object r9 = r10.get(r9)
            if (r9 == 0) goto L_0x005e
            goto L_0x0060
        L_0x005e:
            r9 = r3
            goto L_0x0061
        L_0x0060:
            r9 = r8
        L_0x0061:
            if (r9 != 0) goto L_0x0064
            goto L_0x00b9
        L_0x0064:
            android.service.notification.NotificationListenerService$Ranking r9 = r0.mTmpRanking
            r1.getRanking(r5, r9)
            com.android.wm.shell.bubbles.BubbleData r9 = r0.mBubbleData
            boolean r9 = r9.hasAnyBubbleWithKey(r5)
            if (r9 == 0) goto L_0x0080
            android.service.notification.NotificationListenerService$Ranking r10 = r0.mTmpRanking
            boolean r10 = r10.canBubble()
            if (r10 != 0) goto L_0x0080
            com.android.wm.shell.bubbles.BubbleData r6 = r0.mBubbleData
            r7 = 4
            r6.dismissBubbleWithKey(r5, r7)
            goto L_0x00b5
        L_0x0080:
            if (r9 == 0) goto L_0x0097
            if (r6 == 0) goto L_0x008f
            java.util.Objects.requireNonNull(r7)
            android.service.notification.NotificationListenerService$Ranking r10 = r7.mRanking
            boolean r10 = r10.isSuspended()
            if (r10 == 0) goto L_0x0097
        L_0x008f:
            com.android.wm.shell.bubbles.BubbleData r6 = r0.mBubbleData
            r7 = 14
            r6.dismissBubbleWithKey(r5, r7)
            goto L_0x00b5
        L_0x0097:
            if (r7 == 0) goto L_0x00b5
            android.service.notification.NotificationListenerService$Ranking r5 = r0.mTmpRanking
            boolean r5 = r5.isBubble()
            if (r5 == 0) goto L_0x00b5
            if (r9 != 0) goto L_0x00b5
            r7.setFlagBubble(r8)
            if (r6 == 0) goto L_0x00b1
            android.service.notification.NotificationListenerService$Ranking r5 = r7.mRanking
            boolean r5 = r5.isSuspended()
            if (r5 != 0) goto L_0x00b1
            goto L_0x00b2
        L_0x00b1:
            r8 = r3
        L_0x00b2:
            r0.onEntryUpdated(r7, r8)
        L_0x00b5:
            int r4 = r4 + 1
            goto L_0x002c
        L_0x00b9:
            return
        L_0x00ba:
            java.lang.Object r0 = r11.f$0
            com.android.systemui.statusbar.notification.stack.StackStateAnimator r0 = (com.android.systemui.statusbar.notification.stack.StackStateAnimator) r0
            java.lang.Object r1 = r11.f$1
            java.lang.String r1 = (java.lang.String) r1
            java.lang.Object r11 = r11.f$2
            com.android.systemui.statusbar.notification.row.ExpandableView r11 = (com.android.systemui.statusbar.notification.row.ExpandableView) r11
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.notification.stack.StackStateLogger r0 = r0.mLogger
            r0.disappearAnimationEnded(r1)
            r11.removeFromTransientContainer()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda1.run():void");
    }
}
