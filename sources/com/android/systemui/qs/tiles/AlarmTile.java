package com.android.systemui.qs.tiles;

import android.app.AlarmManager;
import android.content.Intent;
import android.text.TextUtils;
import android.text.format.DateFormat;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.settings.UserTracker;
import java.util.Locale;
import kotlin.Unit;
/* compiled from: AlarmTile.kt */
/* loaded from: classes.dex */
public final class AlarmTile extends QSTileImpl<QSTile.State> {
    public final AlarmTile$callback$1 callback;
    public AlarmManager.AlarmClockInfo lastAlarmInfo;
    public final UserTracker userTracker;
    public final QSTile.Icon icon = QSTileImpl.ResourceIcon.get(2131231749);
    public final Intent defaultIntent = new Intent("android.intent.action.SHOW_ALARMS");

    public static /* synthetic */ void getDefaultIntent$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x004b  */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void handleClick(android.view.View r5) {
        /*
            r4 = this;
            r0 = 0
            if (r5 != 0) goto L_0x0004
            goto L_0x0032
        L_0x0004:
            r1 = 32
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            android.view.ViewParent r2 = r5.getParent()
            boolean r2 = r2 instanceof android.view.ViewGroup
            if (r2 != 0) goto L_0x0034
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Skipping animation as view "
            r1.append(r2)
            r1.append(r5)
            java.lang.String r5 = " is not attached to a ViewGroup"
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            java.lang.Exception r1 = new java.lang.Exception
            r1.<init>()
            java.lang.String r2 = "ActivityLaunchAnimator"
            android.util.Log.wtf(r2, r5, r1)
        L_0x0032:
            r2 = r0
            goto L_0x003a
        L_0x0034:
            com.android.systemui.animation.GhostedViewLaunchAnimatorController r2 = new com.android.systemui.animation.GhostedViewLaunchAnimatorController
            r3 = 4
            r2.<init>(r5, r1, r3)
        L_0x003a:
            android.app.AlarmManager$AlarmClockInfo r5 = r4.lastAlarmInfo
            if (r5 != 0) goto L_0x003f
            goto L_0x0043
        L_0x003f:
            android.app.PendingIntent r0 = r5.getShowIntent()
        L_0x0043:
            if (r0 == 0) goto L_0x004b
            com.android.systemui.plugins.ActivityStarter r4 = r4.mActivityStarter
            r4.postStartActivityDismissingKeyguard(r0, r2)
            goto L_0x0053
        L_0x004b:
            com.android.systemui.plugins.ActivityStarter r5 = r4.mActivityStarter
            android.content.Intent r4 = r4.defaultIntent
            r0 = 0
            r5.postStartActivityDismissingKeyguard(r4, r0, r2)
        L_0x0053:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.AlarmTile.handleClick(android.view.View):void");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953314);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.State state, Object obj) {
        Unit unit;
        String str;
        state.icon = this.icon;
        state.label = getTileLabel();
        AlarmManager.AlarmClockInfo alarmClockInfo = this.lastAlarmInfo;
        if (alarmClockInfo == null) {
            unit = null;
        } else {
            if (DateFormat.is24HourFormat(this.mContext, this.userTracker.getUserId())) {
                str = "EHm";
            } else {
                str = "Ehma";
            }
            state.secondaryLabel = DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), str), alarmClockInfo.getTriggerTime()).toString();
            state.state = 2;
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            state.secondaryLabel = this.mContext.getString(2131953043);
            state.state = 1;
        }
        state.contentDescription = TextUtils.concat(state.label, ", ", state.secondaryLabel);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.State newTileState() {
        QSTile.State state = new QSTile.State();
        state.handlesLongClick = false;
        return state;
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.qs.tiles.AlarmTile$callback$1, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public AlarmTile(com.android.systemui.qs.QSHost r1, android.os.Looper r2, android.os.Handler r3, com.android.systemui.plugins.FalsingManager r4, com.android.internal.logging.MetricsLogger r5, com.android.systemui.plugins.statusbar.StatusBarStateController r6, com.android.systemui.plugins.ActivityStarter r7, com.android.systemui.qs.logging.QSLogger r8, com.android.systemui.settings.UserTracker r9, com.android.systemui.statusbar.policy.NextAlarmController r10) {
        /*
            r0 = this;
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            r0.userTracker = r9
            r1 = 2131231749(0x7f080405, float:1.8079588E38)
            com.android.systemui.plugins.qs.QSTile$Icon r1 = com.android.systemui.qs.tileimpl.QSTileImpl.ResourceIcon.get(r1)
            r0.icon = r1
            android.content.Intent r1 = new android.content.Intent
            java.lang.String r2 = "android.intent.action.SHOW_ALARMS"
            r1.<init>(r2)
            r0.defaultIntent = r1
            com.android.systemui.qs.tiles.AlarmTile$callback$1 r1 = new com.android.systemui.qs.tiles.AlarmTile$callback$1
            r1.<init>()
            r0.callback = r1
            r10.observe(r0, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.AlarmTile.<init>(com.android.systemui.qs.QSHost, android.os.Looper, android.os.Handler, com.android.systemui.plugins.FalsingManager, com.android.internal.logging.MetricsLogger, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.plugins.ActivityStarter, com.android.systemui.qs.logging.QSLogger, com.android.systemui.settings.UserTracker, com.android.systemui.statusbar.policy.NextAlarmController):void");
    }
}
