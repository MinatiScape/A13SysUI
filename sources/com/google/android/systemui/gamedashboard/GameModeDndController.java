package com.google.android.systemui.gamedashboard;

import android.app.AutomaticZenRule;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.Uri;
import android.service.notification.Condition;
import android.util.Log;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.util.Map;
/* loaded from: classes.dex */
public final class GameModeDndController {
    public final Context mContext;
    public boolean mFilterActive;
    public boolean mFilterActiveOld;
    public boolean mGameActive;
    public boolean mGameActiveOld;
    public final AnonymousClass2 mIntentReceiver;
    public final NotificationManager mNotificationManager;
    public String mRuleId = getOrCreateRuleId();
    public String mRuleName;
    public boolean mUserActive;
    public boolean mUserActiveOld;
    public final AnonymousClass1 mUserTracker;
    public static final Uri CONDITION_ID = new Uri.Builder().scheme("android-app").authority(ThemeOverlayApplier.SYSUI_PACKAGE).appendPath("game-mode-dnd-controller").build();
    public static final ComponentName COMPONENT_NAME = new ComponentName(ThemeOverlayApplier.SYSUI_PACKAGE, GameDndConfigActivity.class.getName());

    public final AutomaticZenRule fetchRule() {
        String str = this.mRuleId;
        if (str == null) {
            return null;
        }
        AutomaticZenRule automaticZenRule = this.mNotificationManager.getAutomaticZenRule(str);
        if (automaticZenRule != null) {
            return automaticZenRule;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Fetched new rule id outside of user switch handler: ");
        m.append(this.mRuleId);
        Log.v("GameModeDndController", m.toString());
        String orCreateRuleId = getOrCreateRuleId();
        this.mRuleId = orCreateRuleId;
        return this.mNotificationManager.getAutomaticZenRule(orCreateRuleId);
    }

    public final String getOrCreateRuleId() {
        for (Map.Entry<String, AutomaticZenRule> entry : this.mNotificationManager.getAutomaticZenRules().entrySet()) {
            if (entry.getValue().getConditionId().equals(CONDITION_ID)) {
                return entry.getKey();
            }
        }
        try {
            return this.mNotificationManager.addAutomaticZenRule(new AutomaticZenRule(this.mRuleName, null, COMPONENT_NAME, CONDITION_ID, null, 1, true));
        } catch (IllegalArgumentException unused) {
            Log.w("GameModeDndController", "Failed to create zen rule due to missing configuration Activity.");
            return null;
        }
    }

    public final boolean isGameModeDndOn() {
        if (!this.mGameActive || !this.mFilterActive || !this.mUserActive) {
            return false;
        }
        return true;
    }

    public final void updateRule() {
        Object[] objArr;
        boolean z;
        int i;
        try {
            AutomaticZenRule fetchRule = fetchRule();
            if (fetchRule != null) {
                boolean z2 = this.mFilterActive;
                boolean z3 = false;
                if (z2 != this.mFilterActiveOld) {
                    objArr = 1;
                } else {
                    objArr = null;
                }
                if (objArr != null) {
                    if (z2) {
                        i = 2;
                    } else {
                        i = 1;
                    }
                    fetchRule.setInterruptionFilter(i);
                    this.mNotificationManager.updateAutomaticZenRule(this.mRuleId, fetchRule);
                    Log.v("GameModeDndController", "Updated filter: " + this.mFilterActive);
                }
                if (!fetchRule.getName().equals(this.mRuleName)) {
                    fetchRule.setName(this.mRuleName);
                    this.mNotificationManager.updateAutomaticZenRule(this.mRuleId, fetchRule);
                }
                ComponentName configurationActivity = fetchRule.getConfigurationActivity();
                ComponentName componentName = COMPONENT_NAME;
                if (!configurationActivity.equals(componentName)) {
                    fetchRule.setConfigurationActivity(componentName);
                    this.mNotificationManager.updateAutomaticZenRule(this.mRuleId, fetchRule);
                }
                if (!this.mUserActiveOld || !this.mGameActiveOld || !this.mFilterActiveOld) {
                    z = false;
                } else {
                    z = true;
                }
                if (z != isGameModeDndOn()) {
                    z3 = true;
                }
                if (z3) {
                    Condition condition = new Condition(fetchRule.getConditionId(), "", isGameModeDndOn() ? 1 : 0);
                    Log.v("GameModeDndController", "Updated condition: " + Condition.stateToString(condition.state));
                    this.mNotificationManager.setAutomaticZenRuleState(this.mRuleId, condition);
                }
                this.mGameActiveOld = this.mGameActive;
                this.mUserActiveOld = this.mUserActive;
                this.mFilterActiveOld = this.mFilterActive;
            }
        } catch (Exception e) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Failed to update Game Mode DND rule: ");
            m.append(this.mRuleId);
            Log.e("GameModeDndController", m.toString(), e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.google.android.systemui.gamedashboard.GameModeDndController$2, android.content.BroadcastReceiver] */
    /* JADX WARN: Type inference failed for: r2v1, types: [com.google.android.systemui.gamedashboard.GameModeDndController$1, com.android.systemui.settings.CurrentUserTracker] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public GameModeDndController(android.content.Context r2, android.app.NotificationManager r3, com.android.systemui.broadcast.BroadcastDispatcher r4) {
        /*
            r1 = this;
            r1.<init>()
            r1.mContext = r2
            r1.mNotificationManager = r3
            r3 = 2131952389(0x7f130305, float:1.954122E38)
            java.lang.String r3 = r2.getString(r3)
            r1.mRuleName = r3
            java.lang.String r3 = r1.getOrCreateRuleId()
            r1.mRuleId = r3
            android.app.AutomaticZenRule r3 = r1.fetchRule()
            if (r3 == 0) goto L_0x0025
            int r3 = r3.getInterruptionFilter()
            r0 = 2
            if (r3 != r0) goto L_0x0025
            r3 = 1
            goto L_0x0026
        L_0x0025:
            r3 = 0
        L_0x0026:
            r1.mFilterActive = r3
            android.content.IntentFilter r3 = new android.content.IntentFilter
            r3.<init>()
            java.lang.String r0 = "android.intent.action.USER_PRESENT"
            r3.addAction(r0)
            java.lang.String r0 = "android.intent.action.SCREEN_OFF"
            r3.addAction(r0)
            java.lang.String r0 = "android.intent.action.LOCALE_CHANGED"
            r3.addAction(r0)
            com.google.android.systemui.gamedashboard.GameModeDndController$2 r0 = new com.google.android.systemui.gamedashboard.GameModeDndController$2
            r0.<init>()
            r1.mIntentReceiver = r0
            r2.registerReceiver(r0, r3)
            com.google.android.systemui.gamedashboard.GameModeDndController$1 r2 = new com.google.android.systemui.gamedashboard.GameModeDndController$1
            r2.<init>(r4)
            r1.mUserTracker = r2
            r2.startTracking()
            r1.updateRule()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.gamedashboard.GameModeDndController.<init>(android.content.Context, android.app.NotificationManager, com.android.systemui.broadcast.BroadcastDispatcher):void");
    }
}
