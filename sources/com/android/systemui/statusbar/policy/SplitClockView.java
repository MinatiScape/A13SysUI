package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextClock;
/* loaded from: classes.dex */
public class SplitClockView extends LinearLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public TextClock mAmPmView;
    public AnonymousClass1 mIntentReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.SplitClockView.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.TIME_SET".equals(action) || "android.intent.action.TIMEZONE_CHANGED".equals(action) || "android.intent.action.LOCALE_CHANGED".equals(action) || "android.intent.action.CONFIGURATION_CHANGED".equals(action) || "android.intent.action.USER_SWITCHED".equals(action)) {
                SplitClockView splitClockView = SplitClockView.this;
                int i = SplitClockView.$r8$clinit;
                splitClockView.updatePatterns();
            }
        }
    };
    public TextClock mTimeView;

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.policy.SplitClockView$1] */
    public SplitClockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        getContext().registerReceiverAsUser(this.mIntentReceiver, UserHandle.ALL, intentFilter, null, null);
        updatePatterns();
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unregisterReceiver(this.mIntentReceiver);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mTimeView = (TextClock) findViewById(2131429053);
        this.mAmPmView = (TextClock) findViewById(2131427482);
        this.mTimeView.setShowCurrentUserTime(true);
        this.mAmPmView.setShowCurrentUserTime(true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0036, code lost:
        r4 = -1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updatePatterns() {
        /*
            r9 = this;
            android.content.Context r0 = r9.getContext()
            int r1 = android.app.ActivityManager.getCurrentUser()
            java.lang.String r0 = android.text.format.DateFormat.getTimeFormatString(r0, r1)
            int r1 = r0.length()
            r2 = 1
            int r1 = r1 - r2
            r3 = 0
            r4 = r1
            r5 = r3
        L_0x0015:
            r6 = -1
            if (r4 < 0) goto L_0x003b
            char r7 = r0.charAt(r4)
            r8 = 97
            if (r7 != r8) goto L_0x0022
            r8 = r2
            goto L_0x0023
        L_0x0022:
            r8 = r3
        L_0x0023:
            boolean r7 = java.lang.Character.isWhitespace(r7)
            if (r8 == 0) goto L_0x002a
            r5 = r2
        L_0x002a:
            if (r8 != 0) goto L_0x0038
            if (r7 == 0) goto L_0x002f
            goto L_0x0038
        L_0x002f:
            if (r4 != r1) goto L_0x0032
            goto L_0x0036
        L_0x0032:
            if (r5 == 0) goto L_0x0036
            int r4 = r4 + r2
            goto L_0x003e
        L_0x0036:
            r4 = r6
            goto L_0x003e
        L_0x0038:
            int r4 = r4 + (-1)
            goto L_0x0015
        L_0x003b:
            if (r5 == 0) goto L_0x0036
            r4 = r3
        L_0x003e:
            if (r4 != r6) goto L_0x0045
            java.lang.String r1 = ""
            r2 = r1
            r1 = r0
            goto L_0x004d
        L_0x0045:
            java.lang.String r1 = r0.substring(r3, r4)
            java.lang.String r2 = r0.substring(r4)
        L_0x004d:
            android.widget.TextClock r3 = r9.mTimeView
            r3.setFormat12Hour(r1)
            android.widget.TextClock r3 = r9.mTimeView
            r3.setFormat24Hour(r1)
            android.widget.TextClock r1 = r9.mTimeView
            r1.setContentDescriptionFormat12Hour(r0)
            android.widget.TextClock r1 = r9.mTimeView
            r1.setContentDescriptionFormat24Hour(r0)
            android.widget.TextClock r0 = r9.mAmPmView
            r0.setFormat12Hour(r2)
            android.widget.TextClock r9 = r9.mAmPmView
            r9.setFormat24Hour(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SplitClockView.updatePatterns():void");
    }
}
