package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.util.Log;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.Objects;
import java.util.Optional;
/* compiled from: UnpinNotifications.kt */
/* loaded from: classes.dex */
public final class UnpinNotifications extends Action {
    public boolean hasPinnedHeadsUp;
    public final HeadsUpManager headsUpManager;
    public final SilenceAlertsDisabled silenceAlertsDisabled;
    public final String tag = "Columbus/UnpinNotif";
    public final UnpinNotifications$headsUpChangedListener$1 headsUpChangedListener = new OnHeadsUpChangedListener() { // from class: com.google.android.systemui.columbus.actions.UnpinNotifications$headsUpChangedListener$1
        @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
        public final void onHeadsUpPinnedModeChanged(boolean z) {
            boolean z2;
            UnpinNotifications unpinNotifications = UnpinNotifications.this;
            unpinNotifications.hasPinnedHeadsUp = z;
            if (unpinNotifications.silenceAlertsDisabled.isBlocking() || !unpinNotifications.hasPinnedHeadsUp) {
                z2 = false;
            } else {
                z2 = true;
            }
            unpinNotifications.setAvailable(z2);
        }
    };

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.systemui.columbus.actions.UnpinNotifications$headsUpChangedListener$1] */
    public UnpinNotifications(Context context, SilenceAlertsDisabled silenceAlertsDisabled, Optional<HeadsUpManager> optional) {
        super(context, null);
        boolean z;
        this.silenceAlertsDisabled = silenceAlertsDisabled;
        HeadsUpManager orElse = optional.orElse(null);
        this.headsUpManager = orElse;
        Gate.Listener unpinNotifications$gateListener$1 = new Gate.Listener() { // from class: com.google.android.systemui.columbus.actions.UnpinNotifications$gateListener$1
            @Override // com.google.android.systemui.columbus.gates.Gate.Listener
            public final void onGateChanged(Gate gate) {
                boolean z2;
                if (UnpinNotifications.this.silenceAlertsDisabled.isBlocking()) {
                    UnpinNotifications unpinNotifications = UnpinNotifications.this;
                    Objects.requireNonNull(unpinNotifications);
                    HeadsUpManager headsUpManager = unpinNotifications.headsUpManager;
                    if (headsUpManager != null) {
                        headsUpManager.mListeners.remove(unpinNotifications.headsUpChangedListener);
                        return;
                    }
                    return;
                }
                UnpinNotifications unpinNotifications2 = UnpinNotifications.this;
                Objects.requireNonNull(unpinNotifications2);
                HeadsUpManager headsUpManager2 = unpinNotifications2.headsUpManager;
                if (headsUpManager2 != null) {
                    headsUpManager2.addListener(unpinNotifications2.headsUpChangedListener);
                }
                HeadsUpManager headsUpManager3 = unpinNotifications2.headsUpManager;
                if (headsUpManager3 == null) {
                    z2 = false;
                } else {
                    z2 = headsUpManager3.mHasPinnedNotification;
                }
                unpinNotifications2.hasPinnedHeadsUp = z2;
            }
        };
        if (orElse == null) {
            Log.w("Columbus/UnpinNotif", "No HeadsUpManager");
        } else {
            silenceAlertsDisabled.registerListener(unpinNotifications$gateListener$1);
        }
        if (silenceAlertsDisabled.isBlocking() || !this.hasPinnedHeadsUp) {
            z = false;
        } else {
            z = true;
        }
        setAvailable(z);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        HeadsUpManager headsUpManager = this.headsUpManager;
        if (headsUpManager != null) {
            headsUpManager.unpinAll();
        }
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }
}
